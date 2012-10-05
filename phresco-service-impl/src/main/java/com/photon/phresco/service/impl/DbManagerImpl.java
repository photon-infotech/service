package com.photon.phresco.service.impl;

import java.util.List;

import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;
import org.springframework.data.document.mongodb.query.Update;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.Customer;
import com.photon.phresco.commons.model.DownloadInfo;
import com.photon.phresco.commons.model.RepoInfo;
import com.photon.phresco.commons.model.Technology;
import com.photon.phresco.commons.model.WebService;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.DbManager;
import com.photon.phresco.service.api.DbService;
import com.photon.phresco.service.dao.ApplicationInfoDAO;
import com.photon.phresco.service.dao.ArtifactGroupDAO;
import com.photon.phresco.service.dao.DownloadsDAO;
import com.photon.phresco.service.dao.TechnologyDAO;
import com.photon.phresco.util.ServiceConstants;

public class DbManagerImpl extends DbService implements DbManager, ServiceConstants {

    @Override
    public ArtifactGroup getArchetypeInfo(String techId, String customerId)
            throws PhrescoException {
    	Query techQuery = createCustomerIdQuery(customerId);
    	Criteria criteria = Criteria.whereId().is(techId);
    	techQuery.addCriteria(criteria);
    	TechnologyDAO techDAO = mongoOperation.findOne(TECHNOLOGIES_COLLECTION_NAME, 
    			techQuery, TechnologyDAO.class);
    	ArtifactGroupDAO artifactGroupDAO = mongoOperation.findOne(ARTIFACT_GROUP_COLLECTION_NAME, 
    			new Query(Criteria.whereId().is(techDAO.getArchetypeGroupDAOId())), ArtifactGroupDAO.class);
        return convertArtifactDAO(artifactGroupDAO);
    }

    @Override
    public ApplicationInfo getProjectInfo(String pilotId, String customerId)
            throws PhrescoException {
    	Query pilotQuery = createCustomerIdQuery(customerId);
    	Criteria criteria = Criteria.whereId().is(pilotId);
    	pilotQuery.addCriteria(criteria);
    	ApplicationInfoDAO appDAO = mongoOperation.findOne(APPLICATION_INFO_COLLECTION_NAME, pilotQuery, ApplicationInfoDAO.class);
        return convertApplicationDAO(appDAO);
    }

    @Override
    public Technology getTechnologyDoc(String techId)
            throws PhrescoException {
        Technology technology = mongoOperation.findOne(TECHNOLOGIES_COLLECTION_NAME, 
                new Query(Criteria.whereId().is(techId)), Technology.class);
        return technology;
    }

    @Override
    public RepoInfo getRepoInfo(String customerId) throws PhrescoException {
    	Customer customer = mongoOperation.findOne(CUSTOMERS_COLLECTION_NAME, new Query(Criteria.whereId().is(customerId)), Customer.class);
        return customer.getRepoInfo();
    }

    @Override
    public void storeCreatedProjects(ApplicationInfo projectInfo) throws PhrescoException {
        if(projectInfo != null) {
            mongoOperation.save(CREATEDPROJECTS_COLLECTION_NAME, projectInfo);
        }
    }

    @Override
    public void updateCreatedProjects(ApplicationInfo projectInfo)
            throws PhrescoException {
        if(projectInfo != null) {
            mongoOperation.save(CREATEDPROJECTS_COLLECTION_NAME, projectInfo);
        }
    }
    
    @Override
    public void updateUsedObjects(String collectionName, String criteriaKey, String criteriaValue)
            throws PhrescoException {
        Query query = new Query(Criteria.where(criteriaKey).is(criteriaValue));
        mongoOperation.updateFirst(collectionName, query, Update.update(REST_API_USED, true));
    }

	@Override
	public List<ArtifactGroup> findSelectedArtifacts(List<String> ids, String customerId)
			throws PhrescoException {
		Query customerQuery = createCustomerIdQuery(customerId);
		Criteria idQuery = Criteria.whereId().in(ids.toArray());
		customerQuery.addCriteria(idQuery);
		List<ArtifactGroupDAO> artifactGroupDAOs = mongoOperation.find(ARTIFACT_GROUP_COLLECTION_NAME, 
				customerQuery, ArtifactGroupDAO.class);
		return convertArtifactDAOs(artifactGroupDAOs);
	}

	@Override
	public List<WebService> findSelectedWebservices(List<String> ids)
			throws PhrescoException {
		return mongoOperation.find(WEBSERVICES_COLLECTION_NAME, new Query(Criteria.whereId().in(ids.toArray())), WebService.class);
	}

	@Override
	public List<DownloadInfo> findSelectedServers(List<String> ids, String customerId)
			throws PhrescoException {
		Query query = createCustomerIdQuery(customerId);
		Criteria idCriteria = Criteria.whereId().in(ids.toArray());
		query.addCriteria(idCriteria);
		Criteria categoryCriteria = Criteria.where("category").is("Server");
		query.addCriteria(categoryCriteria);
		List<DownloadsDAO> downloadsDAOs = mongoOperation.find(DOWNLOAD_COLLECTION_NAME, query, DownloadsDAO.class);
		return convertDownloadDAOs(downloadsDAOs);
	}

	@Override
	public List<DownloadInfo> findSelectedDatabases(List<String> ids, String customerId)
			throws PhrescoException {
		Query query = createCustomerIdQuery(customerId);
		Criteria idCriteria = Criteria.whereId().in(ids.toArray());
		query.addCriteria(idCriteria);
		Criteria categoryCriteria = Criteria.where("category").is("Database");
		query.addCriteria(categoryCriteria);
		List<DownloadsDAO> downloadsDAOs = mongoOperation.find(DOWNLOAD_COLLECTION_NAME, query, DownloadsDAO.class);
		return convertDownloadDAOs(downloadsDAOs);
	}

}
