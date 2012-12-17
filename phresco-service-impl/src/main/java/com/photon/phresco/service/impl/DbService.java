/*
 * ###
 * Phresco Service
 * 
 * Copyright (C) 1999 - 2012 Photon Infotech Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ###
 */

package com.photon.phresco.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.document.mongodb.MongoOperations;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.DownloadInfo;
import com.photon.phresco.commons.model.Technology;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.exception.PhrescoWebServiceException;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.api.MongoConfig;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.converters.ConvertersFactory;
import com.photon.phresco.service.dao.ApplicationInfoDAO;
import com.photon.phresco.service.dao.ApplicationTypeDAO;
import com.photon.phresco.service.dao.ArtifactGroupDAO;
import com.photon.phresco.service.dao.DownloadsDAO;
import com.photon.phresco.service.dao.TechnologyDAO;
import com.photon.phresco.service.util.ServerUtil;
import com.photon.phresco.util.FileUtil;
import com.photon.phresco.util.ServiceConstants;

public class DbService implements ServiceConstants {

	private static final Logger S_LOGGER = Logger.getLogger(DbService.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	private static final String MONGO_TEMPLATE = "mongoTemplate";
	protected MongoOperations mongoOperation;

	protected DbService() {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
    	mongoOperation = (MongoOperations)ctx.getBean(MONGO_TEMPLATE);
	}

	protected Query createCustomerIdQuery(String customerId) {
		List<String> customerIds = new ArrayList<String>();
		customerIds.add(customerId);
		
		if(!customerId.equals(DEFAULT_CUSTOMER_NAME)) {
			customerIds.add(DEFAULT_CUSTOMER_NAME);
		}

		Criteria criteria = Criteria.where(DB_COLUMN_CUSTOMERIDS).in(customerIds.toArray());
		Query query = new Query(criteria);

	    if (isDebugEnabled) {
	        S_LOGGER.debug("query.getQueryObject() " + query.getQueryObject());
	    }
	    
	    return query;
	}
	
	protected List<ArtifactGroup> convertArtifactDAOs(List<ArtifactGroupDAO> artifactGroupDAOs) throws PhrescoException {
		Converter<ArtifactGroupDAO, ArtifactGroup> converter = 
			(Converter<ArtifactGroupDAO, ArtifactGroup>) ConvertersFactory.getConverter(ArtifactGroupDAO.class);
		List<ArtifactGroup> artifactGroups = new ArrayList<ArtifactGroup>();
		for (ArtifactGroupDAO artifactDAO : artifactGroupDAOs) {
			artifactGroups.add(converter.convertDAOToObject(artifactDAO, mongoOperation));
		}
		return artifactGroups;
	}
	
	protected List<DownloadInfo> convertDownloadDAOs(List<DownloadsDAO> downloadsDAOs) throws PhrescoException {
		List<DownloadInfo> infos = new ArrayList<DownloadInfo>();
		Converter<DownloadsDAO, DownloadInfo> converter = 
			(Converter<DownloadsDAO, DownloadInfo>) ConvertersFactory.getConverter(DownloadsDAO.class);
		for (DownloadsDAO downloadsDAO : downloadsDAOs) {
			infos.add(converter.convertDAOToObject(downloadsDAO, mongoOperation));
		}
		return infos;
	}
	
	protected ArtifactGroup convertArtifactDAO(ArtifactGroupDAO artifactGroupDAO) throws PhrescoException {
		Converter<ArtifactGroupDAO, ArtifactGroup> converter = 
			(Converter<ArtifactGroupDAO, ArtifactGroup>) ConvertersFactory.getConverter(ArtifactGroupDAO.class);
		return converter.convertDAOToObject(artifactGroupDAO, mongoOperation);
	}
	
	protected ApplicationInfo convertApplicationDAO(ApplicationInfoDAO applicationInfoDAO) throws PhrescoException {
		Converter<ApplicationInfoDAO, ApplicationInfo> appConverter = 
			(Converter<ApplicationInfoDAO, ApplicationInfo>) ConvertersFactory.getConverter(ApplicationInfoDAO.class);
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
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		javax.validation.Validator validator = factory.getValidator();
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
		if (constraintViolations.isEmpty()) {
			return true;
		}
		for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
			throw new PhrescoException(constraintViolation.getMessage());
		}
		return false;
	}
	
	protected void createArtifact(String collectionName, Object object) {
		try {
			if(validate(object)) {
				mongoOperation.save(collectionName, object);
			}
		} catch (Exception e) {
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
    	PhrescoServerFactory.initialize();
    	RepositoryManager repositoryManager = PhrescoServerFactory.getRepositoryManager();
        File pomFile = ServerUtil.createPomFile(archetypeInfo);
        
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
        return addArtifact;
    }
    
    protected Technology getTechnologyById(String techId) throws PhrescoException {
    	TechnologyDAO technologyDAO = mongoOperation.findOne(TECHNOLOGIES_COLLECTION_NAME, 
    			new Query(Criteria.whereId().is(techId)), TechnologyDAO.class);
    	Converter<TechnologyDAO, Technology> technologyConverter = 
	          (Converter<TechnologyDAO, Technology>) ConvertersFactory.getConverter(TechnologyDAO.class);
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
}
