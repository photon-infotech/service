/**
 * Phresco Service Implemenation
 *
 * Copyright (C) 1999-2014 Photon Infotech Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.photon.phresco.service.impl;

import java.io.File;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.document.mongodb.MongoOperations;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Order;
import org.springframework.data.document.mongodb.query.Query;
import org.springframework.data.document.mongodb.query.Sort;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactGroupInfo;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.Customer;
import com.photon.phresco.commons.model.DownloadInfo;
import com.photon.phresco.commons.model.Technology;
import com.photon.phresco.commons.model.TechnologyGroup;
import com.photon.phresco.commons.model.TechnologyInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.exception.PhrescoWebServiceException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.api.MongoConfig;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.converters.ConvertersFactory;
import com.photon.phresco.service.dao.ApplicationInfoDAO;
import com.photon.phresco.service.dao.ApplicationTypeDAO;
import com.photon.phresco.service.dao.ArtifactGroupDAO;
import com.photon.phresco.service.dao.CustomerDAO;
import com.photon.phresco.service.dao.DownloadsDAO;
import com.photon.phresco.service.dao.TechnologyDAO;
import com.photon.phresco.service.model.ServerConfiguration;
import com.photon.phresco.service.util.ServerUtil;
import com.photon.phresco.util.FileUtil;
import com.photon.phresco.util.ServiceConstants;

public class DbService implements ServiceConstants {

	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(DbService.class.getName());
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();
	
	private static final String MONGO_TEMPLATE = "mongoTemplate";
	private static final String MONGO_TEMPLATE_MASTER="mongoTemplateMaster";
	
	private static MongoOperations mongoOperation;
	private static MongoOperations mongoOperationMaster;
	
	private static ServerConfiguration serverConfig = null;
	
	private static Map<String, String> customerMap = new HashMap<String, String>();
	
	public static MongoOperations getMongoOperation() {
		return mongoOperation;
	}
	
	public static MongoOperations getMongoOperationMaster() {
		return mongoOperationMaster;
	}
	
	public static void setMongoOperationMaster(MongoOperations mongoOperationMaster) {
		DbService.mongoOperationMaster = mongoOperationMaster;
	}

	public static void setMongoOperation(MongoOperations mongoOperation) {
		DbService.mongoOperation = mongoOperation;
	}

	protected DbService() {
		if(mongoOperation == null) {
			ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
	    	mongoOperation = (MongoOperations)ctx.getBean(MONGO_TEMPLATE);
	    	mongoOperationMaster = (MongoOperations)ctx.getBean(MONGO_TEMPLATE_MASTER);
	    	serverConfig = PhrescoServerFactory.getServerConfig();
		}
	}

	protected Query createCustomerIdQuery(String customerId) {
		if (isDebugEnabled) {
			LOGGER.debug("DbService.createCustomerIdQuery:Entry");
			if(StringUtils.isEmpty(customerId)) {
				LOGGER.warn("DbService.createCustomerIdQuery",STATUS_BAD_REQUEST, "message=\"customerId is empty\"");
			}
			LOGGER.info("DbManagerImpl.getArchetypeInfo", CUSTOMER_ID_EQUALS_SLASH + customerId + "\"");
		}
		List<String> customerIds = new ArrayList<String>();
		customerIds.add(customerId);
		
		Criteria criteria = Criteria.where(DB_COLUMN_CUSTOMERIDS).in(customerIds.toArray());
		Query query = new Query(criteria);

	    if (isDebugEnabled) {
	        LOGGER.debug("QueryObject=" + "\""+query.getQueryObject()+"\"");
	    }
	    if (isDebugEnabled) {
			LOGGER.debug("DbService.createCustomerIdQuery:Exit");
	    }
	    return query;
	}
	
	@SuppressWarnings(UNCHECKED)
	protected List<ArtifactGroup> convertArtifactDAOs(List<ArtifactGroupDAO> artifactGroupDAOs) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("DbService.convertArtifactDAOs:Entry");
			if(CollectionUtils.isEmpty(artifactGroupDAOs)) {
				LOGGER.warn("DbService.convertArtifactDAOs",STATUS_BAD_REQUEST, "message=\"artifactGroupDAOs is empty\"");
				throw new PhrescoException("artifactGroupDAOs is empty");
			}
		}
		Converter<ArtifactGroupDAO, ArtifactGroup> converter = 
			(Converter<ArtifactGroupDAO, ArtifactGroup>) ConvertersFactory.getConverter(ArtifactGroupDAO.class);
		List<ArtifactGroup> artifactGroups = new ArrayList<ArtifactGroup>();
		for (ArtifactGroupDAO artifactDAO : artifactGroupDAOs) {
			artifactGroups.add(converter.convertDAOToObject(artifactDAO, mongoOperation));
		}
		if(isDebugEnabled) {
			LOGGER.debug("DbService.convertArtifactDAOs:Exit");
		}
		return artifactGroups;
	}
	
	@SuppressWarnings(UNCHECKED)
	protected List<DownloadInfo> convertDownloadDAOs(List<DownloadsDAO> downloadsDAOs) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("DbService.convertDownloadDAOs:Entry");
			if(CollectionUtils.isEmpty(downloadsDAOs)) {
				LOGGER.warn("DbService.convertDownloadDAOs",STATUS_BAD_REQUEST, "message=\"downloadsDAOs is empty\"");
				throw new PhrescoException("downloadsDAOs is empty");
			}
		}
		List<DownloadInfo> infos = new ArrayList<DownloadInfo>();
		Converter<DownloadsDAO, DownloadInfo> converter = 
			(Converter<DownloadsDAO, DownloadInfo>) ConvertersFactory.getConverter(DownloadsDAO.class);
		for (DownloadsDAO downloadsDAO : downloadsDAOs) {
			infos.add(converter.convertDAOToObject(downloadsDAO, mongoOperation));
		}
		if(isDebugEnabled) {
			LOGGER.debug("DbService.convertDownloadDAOs:Exit");
		}
		return infos;
	}
	
	@SuppressWarnings(UNCHECKED)
	protected ArtifactGroup convertArtifactDAO(ArtifactGroupDAO artifactGroupDAO) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("DbService.convertArtifactDAO:Entry");
			if(artifactGroupDAO == null) {
				LOGGER.warn("DbService.convertDownloadDAOs",STATUS_BAD_REQUEST, "message=\"artifactGroupDAO is null\"");
				throw new PhrescoException("artifactGroupDAO is null");
			} LOGGER.info("DbService.convertDownloadDAOs","arifactId=\"" + artifactGroupDAO.getArtifactId() +"\"");
		}
		Converter<ArtifactGroupDAO, ArtifactGroup> converter = 
			(Converter<ArtifactGroupDAO, ArtifactGroup>) ConvertersFactory.getFrameworkConverter(ArtifactGroupDAO.class);
		if(isDebugEnabled) {
			LOGGER.debug("DbService.convertArtifactDAO:Exit");
		}
		return converter.convertDAOToObject(artifactGroupDAO, mongoOperation);
	}
	
	@SuppressWarnings(UNCHECKED)
	protected ApplicationInfo convertApplicationDAO(ApplicationInfoDAO applicationInfoDAO) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("DbService.convertApplicationDAO:Entry");
			if(applicationInfoDAO == null) {
				LOGGER.warn("DbService.convertApplicationDAO",STATUS_BAD_REQUEST, "message=\"applicationInfoDAO is null\"");
				throw new PhrescoException("applicationInfoDAO is null");
			} 
		}
		Converter<ApplicationInfoDAO, ApplicationInfo> appConverter = 
			(Converter<ApplicationInfoDAO, ApplicationInfo>) ConvertersFactory.getConverter(ApplicationInfoDAO.class);
		if(isDebugEnabled) {
			LOGGER.debug("DbService.convertApplicationDAO:Exit");
		}
		return appConverter.convertDAOToObject(applicationInfoDAO, mongoOperation);
	}
	
	protected long count(String collection, Query query ) {  
        return mongoOperation.executeCommand(
            "{ " +
                "\"count\" : \"" + collection + "\"," +
                "\"query\" : " + query.getQueryObject().toString() + 
            " }"  ).getLong( "n" );
    }
	
	protected boolean validate(Object object) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("DbService.validate:Entry");
		}
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		javax.validation.Validator validator = factory.getValidator();
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
		if (constraintViolations.isEmpty()) {
			LOGGER.warn("DbService.validate",STATUS_BAD_REQUEST, "message=\"constraintViolations is empty\"");
			return true;
		}
		for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
			throw new PhrescoException(constraintViolation.getMessage());
		}
		if(isDebugEnabled) {
			LOGGER.debug("DbService.validate:Exit");
		}
		return false;
	}
	
	protected void createArtifact(String collectionName, Object object) {
		if(isDebugEnabled) {
			LOGGER.debug("DbService.createArtifact:Entry");
		}
		try {
			if(validate(object)) {
				mongoOperation.save(collectionName, object);
			}
			if(isDebugEnabled) {
				LOGGER.debug("DbService.createArtifact:Exit");
			}
		} catch (Exception e) {
			if(isDebugEnabled) {
				LOGGER.error("DbService.createArtifact", STATUS_FAILURE, MESSAGE_EQUALS + "\"" + e.getLocalizedMessage() + "\"");
			}
			throw new PhrescoWebServiceException(e);
		}
	}
	
	/**
     * Upload binaries using the given artifact info
     * @param archetypeInfo
     * @param artifactFile
     * @param customerId
     * @return
     * @throws PhrescoException
     */
    protected boolean uploadBinary(ArtifactGroup archetypeInfo, File artifactFile) throws PhrescoException {
    	if(isDebugEnabled) {
    		LOGGER.debug("DbService.uploadBinary:Entry");
    	}
    	PhrescoServerFactory.initialize();
    	RepositoryManager repositoryManager = PhrescoServerFactory.getRepositoryManager();
        File pomFile = null;       
        InputStream artifactPomStream = ServerUtil.getArtifactPomStream(artifactFile);
        if(artifactPomStream != null) {        	
        	pomFile = ServerUtil.getArtifactPomFile(artifactFile);
        } else {        	
        	pomFile= ServerUtil.createPomFile(archetypeInfo);
        }
        if(isDebugEnabled) {
        	LOGGER.debug("DbService.uploadBinary","pomFileDir=\""+ pomFile +"\"");
        }
        //Assuming there will be only one version for the artifactGroup
        List<com.photon.phresco.commons.model.ArtifactInfo> versions = archetypeInfo.getVersions();
        com.photon.phresco.commons.model.ArtifactInfo artifactInfo = versions.get(0);
        
		com.photon.phresco.service.model.ArtifactInfo info = new com.photon.phresco.service.model.ArtifactInfo(archetypeInfo.getGroupId(), 
				archetypeInfo.getArtifactId(), archetypeInfo.getClassifier(), archetypeInfo.getPackaging(), artifactInfo.getVersion());
		
        info.setPomFile(pomFile);
        
        List<String> customerIds = archetypeInfo.getCustomerIds();
        String customerId = customerIds.get(0);
        boolean addArtifact = repositoryManager.addArtifact(info, artifactFile, customerId);
        FileUtil.delete(pomFile);
        if(isDebugEnabled) {
    		LOGGER.debug("DbService.uploadBinary:Exit");
    	}
        return addArtifact;
    }
    
    protected boolean iconuploadBinary(ArtifactGroup archetypeInfo, File artifactFile,String ICON_EXT) throws PhrescoException {
    	if(isDebugEnabled) {
    		LOGGER.debug("DbService.iconuploadBinary:Entry");
    	}
    	PhrescoServerFactory.initialize();
    	RepositoryManager repositoryManager = PhrescoServerFactory.getRepositoryManager();
        File pomFile = null;       
        InputStream artifactPomStream = ServerUtil.getArtifactPomStream(artifactFile);
        if(artifactPomStream != null) {        	
        	pomFile = ServerUtil.getArtifactPomFile(artifactFile);
        } else {        	
        	pomFile= ServerUtil.createPomFile(archetypeInfo);
        }
        if(isDebugEnabled) {
        	LOGGER.debug("DbService.iconuploadBinary","pomFileDir=\""+ pomFile +"\"");
        }
        //Assuming there will be only one version for the artifactGroup
        List<com.photon.phresco.commons.model.ArtifactInfo> versions = archetypeInfo.getVersions();
        com.photon.phresco.commons.model.ArtifactInfo artifactInfo = versions.get(0);
        
		com.photon.phresco.service.model.ArtifactInfo info = new com.photon.phresco.service.model.ArtifactInfo(archetypeInfo.getGroupId(), 
				archetypeInfo.getArtifactId(), archetypeInfo.getClassifier(), ICON_EXT, artifactInfo.getVersion());
		
        info.setPomFile(pomFile);
        
        List<String> customerIds = archetypeInfo.getCustomerIds();
        String customerId = customerIds.get(0);
        boolean addArtifact = repositoryManager.addArtifact(info, artifactFile, customerId);
        FileUtil.delete(pomFile);
        if(isDebugEnabled) {
    		LOGGER.debug("DbService.iconuploadBinary:Exit");
    	}
        return addArtifact;
    }
    
    @SuppressWarnings(UNCHECKED)
	protected Technology getTechnologyById(String techId) throws PhrescoException {
    	if(isDebugEnabled) {
    		LOGGER.debug("DbService.getTechnologyById:Entry");
    		if(StringUtils.isEmpty(techId)) {
    			LOGGER.warn("DbService.getTechnologyById",STATUS_BAD_REQUEST, "message=\"techId is empty\"");
				throw new PhrescoException("techId is empty");
    		}
    		LOGGER.info("DbService.getTechnologyById","techId=\""+ techId + "\"");
    	}
    	TechnologyDAO technologyDAO = mongoOperation.findOne(TECHNOLOGIES_COLLECTION_NAME, 
    			new Query(Criteria.whereId().is(techId)), TechnologyDAO.class);
    	Converter<TechnologyDAO, Technology> technologyConverter = 
	          (Converter<TechnologyDAO, Technology>) ConvertersFactory.getConverter(TechnologyDAO.class);
    	if(isDebugEnabled) {
    		LOGGER.debug("DbService.getTechnologyById:Exit");
    	}
    	return technologyConverter.convertDAOToObject(technologyDAO, mongoOperation);
    }
    
    protected ApplicationTypeDAO getApptypeById(String id) {
    	return mongoOperation.findOne(APPTYPES_COLLECTION_NAME, new Query(Criteria.whereId().is(id)), 
    			ApplicationTypeDAO.class);
    }
    
    protected Object performFindOne(String id, String collectionName) {
    	return mongoOperation.findOne(collectionName, 
    			new Query(Criteria.whereId().is(id)), Object.class);
    }
    
    protected void saveFileToDB(String id, InputStream is) throws PhrescoException {
    	if(isDebugEnabled) {
    		LOGGER.debug("DbService.saveFileToDB:Entry");
    		if(StringUtils.isEmpty(id)) {
    			LOGGER.warn("DbService.saveFileToDB",STATUS_BAD_REQUEST, "message=\"id is empty\"");
				throw new PhrescoException("id is empty");
    		} 
    		LOGGER.info("DbService.saveFileToDB","id=\""+ id +"\"");
    	}
    	getGridFs().remove(id);
		GridFSInputFile file = getGridFs().createFile(is);
		file.setFilename(id);
		file.save(); 
		if(isDebugEnabled) {
    		LOGGER.debug("DbService.saveFileToDB:Exit");
    	}
    }
    
    protected InputStream getFileFromDB(String id) throws PhrescoException {
    	if(isDebugEnabled) {
    		LOGGER.debug("DbService.getFileFromDB:Entry");
    		if(StringUtils.isEmpty(id)) {
    			LOGGER.warn("DbService.getFileFromDB",STATUS_BAD_REQUEST, "message=\"id is empty\"");
				throw new PhrescoException("id is empty");
    		} 
    		LOGGER.info("DbService.getFileFromDB","id=\""+ id +"\"");
    	}
		GridFSDBFile imageForOutput = getGridFs().findOne(id);
		InputStream inputStream = imageForOutput.getInputStream();
		if(isDebugEnabled) {
			LOGGER.debug("DbService.getFileFromDB:Exit");
		}
		return inputStream; 
    }
    
	private GridFS getGridFs() throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("DbService.getGridFs:Entry");
		}
		try {
			Mongo mongo = new Mongo(serverConfig.getDbHost(),
					serverConfig.getDbPort());
			DB db = mongo.getDB(serverConfig.getDbName());
			GridFS gfsPhoto = new GridFS(db, "icons");
			if(isDebugEnabled) {
				LOGGER.debug("DbService.getGridFs:Exit");
			}
			return gfsPhoto;
		} catch (UnknownHostException e) {
			if(isDebugEnabled) {
				LOGGER.error("DbService.getGridFs", STATUS_FAILURE,MESSAGE_EQUALS + "\"" + e.getLocalizedMessage() + "\"");
			}
			throw new PhrescoException(e);
		} catch (MongoException e) {
			if(isDebugEnabled) {
				LOGGER.error("DbService.getGridFs", STATUS_FAILURE,MESSAGE_EQUALS + "\"" + e.getLocalizedMessage() + "\"");
			}
			throw new PhrescoException(e);
		}
	}
	
	@SuppressWarnings(UNCHECKED)
	protected List<Customer> findCustomersFromDB() {
		if(isDebugEnabled) {
			LOGGER.debug("DbService.findCustomersFromDB:Entry");
		}
    	try {
    		Query query = new Query();
    		query.sort().on("name", Order.ASCENDING);
    		List<CustomerDAO> customersDAOs = mongoOperation.find(CUSTOMERDAO_COLLECTION_NAME, query, CustomerDAO.class);
    		List<Customer> customersInDb = new ArrayList<Customer>();
    		Converter<CustomerDAO, Customer> customerConverter = 
    			(Converter<CustomerDAO, Customer>) ConvertersFactory.getFrameworkConverter(CustomerDAO.class);
    		for (CustomerDAO customerDAO : customersDAOs) {
				customersInDb.add(customerConverter.convertDAOToObject(customerDAO, mongoOperation));
			}
    		if(isDebugEnabled) {
    			LOGGER.debug("DbService.findCustomersFromDB:Exit");
    		}
    		return customersInDb;
    	} catch (Exception e) {
    		if(isDebugEnabled) {
				LOGGER.error("DbService.findCustomersFromDB", STATUS_FAILURE,MESSAGE_EQUALS + "\"" + e.getLocalizedMessage() + "\"");
			}
    		throw new PhrescoWebServiceException(e, EX_PHEX00005, CUSTOMERS_COLLECTION_NAME);
		}
	}
	
	protected List<TechnologyGroup> getTechGroupByCustomer(String customerId, String appTypeId) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("DbService.getTechGroupByCustomer:Entry");
			if(StringUtils.isEmpty(customerId)) {
				LOGGER.warn("DbService.getTechGroupByCustomer",STATUS_BAD_REQUEST, "message=\"customerId is empty\"");
				throw new PhrescoException("customerId is empty");
			}
			if(StringUtils.isEmpty(appTypeId)) {
				LOGGER.warn("DbService.getTechGroupByCustomer",STATUS_BAD_REQUEST, "message=\"appTypeId is empty\"");
				throw new PhrescoException("appTypeId is empty");
			}
			LOGGER.info("DbService.getTechGroupByCustomer", CUSTOMER_ID_EQUALS_SLASH + customerId + "\"","appTypeId\""+ appTypeId +"\"");
		}
		List<String> applicableTechnologies = new ArrayList<String>();
		CustomerDAO customer = mongoOperation.findOne(CUSTOMERS_COLLECTION_NAME, 
				new Query(Criteria.whereId().is(customerId)), CustomerDAO.class);
		List<String> customerApplicableTechnologies = customer.getApplicableTechnologies();
		List<String> fromDB = getApplicableForomDB(customer.getId(), customerApplicableTechnologies);
		if(CollectionUtils.isNotEmpty(customerApplicableTechnologies)) {
			applicableTechnologies.addAll(customerApplicableTechnologies);
		}
		if(CollectionUtils.isNotEmpty(fromDB)) {
			applicableTechnologies.addAll(fromDB);
		}
		List<Technology> technologies = createTechnology(applicableTechnologies);
		List<TechnologyGroup> technologyGroups = createTechnologyInfo(technologies);
		if(isDebugEnabled) {
			LOGGER.debug("DbService.getTechGroupByCustomer:Exit");
		}
		return createTechGroup(technologyGroups, appTypeId);
	}
	
	private List<TechnologyGroup> createTechGroup(List<TechnologyGroup> technologyGroups, String appTypeId) {
		if(isDebugEnabled) {
			LOGGER.debug("DbService.createTechGroup:Entry");
			if(StringUtils.isEmpty(appTypeId)) {
				LOGGER.warn("DbService.createTechGroup",STATUS_BAD_REQUEST, "message=\"appTypeId is empty\"");
			}
			LOGGER.info("DbService.createTechGroup","appTypeId\""+ appTypeId +"\"");
		}
		List<TechnologyGroup> groups = new ArrayList<TechnologyGroup>();
		for (TechnologyGroup technologyGroup : technologyGroups) {
			if(technologyGroup.getAppTypeId().equals(appTypeId)) {
				groups.add(technologyGroup);
			}
		}
		if(isDebugEnabled) {
			LOGGER.debug("DbService.createTechGroup:Exit");
		}
		return groups;
	}

	private List<String> getApplicableForomDB(String customerId, List<String> applicableTechnologies) {
		if(isDebugEnabled) {
			LOGGER.debug("DbService.getApplicableForomDB:Entry");
			if(StringUtils.isEmpty(customerId)) {
				LOGGER.warn("DbService.getApplicableForomDB",STATUS_BAD_REQUEST, "message=\"customerId is empty\"");
			}
			LOGGER.info("DbService.getApplicableForomDB", CUSTOMER_ID_EQUALS_SLASH + customerId + "\"");
		}
		List<String> techIds = new ArrayList<String>();
		List<TechnologyDAO> techs = mongoOperation.find(TECHNOLOGIES_COLLECTION_NAME, 
				new Query(Criteria.where(DB_COLUMN_CUSTOMERIDS).is(customerId)), TechnologyDAO.class);
		if(CollectionUtils.isNotEmpty(techs)) {
			for (TechnologyDAO tech : techs) {
				if(!applicableTechnologies.contains(tech.getId())) {
					techIds.add(tech.getId());
				}
			}
			return techIds;
		}
		if(isDebugEnabled) {
			LOGGER.debug("DbService.getApplicableForomDB:Exit");
		}
		return techIds;
	}

	private List<Technology> createTechnology(List<String> applicableTechnologies) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("DbService.createTechnology:Entry");
		}
		List<Technology> technologies = new ArrayList<Technology>();
		try {
			for (String techId : applicableTechnologies) {
				technologies.add(getTechnologyById(techId));
			}
		} catch (Exception e) {
			if(isDebugEnabled) {
				LOGGER.error("DbService.createTechnology", STATUS_FAILURE,MESSAGE_EQUALS + "\"" + e.getLocalizedMessage() + "\"");
			}
			throw new PhrescoException(e);
		}
		if(isDebugEnabled) {
			LOGGER.debug("DbService.createTechnology:Exit");
		}
		return technologies;
	}
	
	private List<TechnologyGroup> createTechnologyInfo(List<Technology> technologies) {
		if(isDebugEnabled) {
			LOGGER.debug("DbService.createTechnologyInfo:Entry");
		}
		Map<String, List<TechnologyInfo>> techGroupMap = new HashMap<String, List<TechnologyInfo>>();
		for (Technology technology : technologies) {
			createTechGroupMap(technology, techGroupMap);
		}
		List<TechnologyGroup> technologyGroups = createTechnologyGroup(techGroupMap);
		if(isDebugEnabled) {
			LOGGER.debug("DbService.createTechnologyInfo:Exit");
		}
		return technologyGroups;
	}
	
	private List<TechnologyGroup> createTechnologyGroup(Map<String, List<TechnologyInfo>> techGroupMap) {
		if(isDebugEnabled) {
			LOGGER.debug("DbService.createTechnologyGroup:Entry");
		}
		Set<String> keySet = techGroupMap.keySet();
		List<TechnologyGroup> techGroups = new ArrayList<TechnologyGroup>();
		for (String key : keySet) {
			List<TechnologyInfo> list = techGroupMap.get(key);
			TechnologyGroup techGroup = new TechnologyGroup();
			TechnologyGroup technologyGroup = getTechnologyGroup(key);
			techGroup.setId(key);
			techGroup.setName(technologyGroup.getName());
			techGroup.setAppTypeId(list.get(0).getAppTypeId());
			techGroup.setTechInfos(list);
			techGroups.add(techGroup);
		}
		if(isDebugEnabled) {
			LOGGER.debug("DbService.createTechnologyGroup:Exit");
		}
		return techGroups;
	}
	
	private void createTechGroupMap(Technology technology, Map<String, List<TechnologyInfo>> techGroupMap) {
		if(isDebugEnabled) {
			LOGGER.debug("DbService.createTechGroupMap:Entry");
		}
		TechnologyInfo techInfo = createTechInfo(technology);
		String techGroupId = technology.getTechGroupId();
		if(StringUtils.isEmpty(techGroupId) && isDebugEnabled) {
			LOGGER.warn("DbService.createTechGroupMap",STATUS_BAD_REQUEST, "message=\"techGroupId is empty\"");
		}
		List<TechnologyInfo> infos = new ArrayList<TechnologyInfo>();
		if(techGroupMap.containsKey(techGroupId)) {
			List<TechnologyInfo> list = techGroupMap.get(techGroupId);
			list.add(techInfo);
			techGroupMap.put(techGroupId, list);
		} else {
			infos.add(techInfo);
			techGroupMap.put(techGroupId, infos);
		}
		if(isDebugEnabled) {
			LOGGER.debug("DbService.createTechGroupMap:Exit");
		}
	}

	private TechnologyInfo createTechInfo(Technology technology) {
		if(isDebugEnabled) {
			LOGGER.debug("DbService.createTechInfo:Entry");
		}
		TechnologyInfo info = new TechnologyInfo();
		info.setId(technology.getId());
		info.setName(technology.getName());
		info.setAppTypeId(technology.getAppTypeId());
		info.setTechVersions(technology.getTechVersions());
		if(isDebugEnabled) {
			LOGGER.debug("DbService.createTechInfo:Exit");
		}
		return info;
	}
	
	private TechnologyGroup getTechnologyGroup(String id) {
		return mongoOperation.findOne(TECH_GROUP_COLLECTION_NAME, new Query(Criteria.whereId().is(id)), 
    			TechnologyGroup.class);
	}
	
	protected String getCustomerNameById(String id) {
		if(customerMap.get(id) != null) {
			return customerMap.get(id);
		}
		Customer cstomer = mongoOperation.findOne(CUSTOMERDAO_COLLECTION_NAME, new Query(Criteria.whereId().is(id)), Customer.class);
		if(cstomer != null) {
			customerMap.put(id, cstomer.getName());
			return cstomer.getName();
		}
		return "";
	}
	
	protected String getSelectedFeatureString(List<String> selectedFeatures) {
		if(CollectionUtils.isEmpty(selectedFeatures)) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		List<ArtifactInfo> infos = mongoOperation.find(ARTIFACT_INFO_COLLECTION_NAME, 
				new Query(Criteria.whereId().in(selectedFeatures.toArray())), ArtifactInfo.class);
		for (ArtifactInfo artifactInfo : infos) {
			ArtifactGroupDAO group = mongoOperation.findOne(ARTIFACT_GROUP_COLLECTION_NAME, 
					new Query(Criteria.whereId().is(artifactInfo.getArtifactGroupId())), ArtifactGroupDAO.class);
			buffer.append(group.getName());
			buffer.append(HYPEN);
			buffer.append(artifactInfo.getVersion());
			buffer.append(COMMA);
		}
		return buffer.toString();
	}
	
	protected String getSelectedDownloadString(List<ArtifactGroupInfo> selected) {
		if(CollectionUtils.isEmpty(selected)) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for (ArtifactGroupInfo artifactGroupInfo : selected) {
			buffer.append(artifactGroupInfo.getName());
			buffer.append(HYPEN);
			List<ArtifactInfo> infos = mongoOperation.find(ARTIFACT_INFO_COLLECTION_NAME, 
					new Query(Criteria.whereId().in(artifactGroupInfo.getArtifactInfoIds().toArray())), ArtifactInfo.class);
			for (ArtifactInfo artifactInfo : infos) {
				buffer.append(artifactInfo.getVersion());
				buffer.append(COMMA);
			}
			buffer.append(COMMA);
		}
		return buffer.toString();
	}
}
