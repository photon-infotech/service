/*
 * ###
 * Phresco Service Implemenation
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
/*******************************************************************************
 * Copyright (c) 2011 Photon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Photon Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.photon.in/legal/ppl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *     Photon - initial API and implementation
 ******************************************************************************/

package com.photon.phresco.service.impl;

import java.util.ArrayList;
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
	public List<ArtifactGroup> findSelectedArtifacts(List<String> ids, String customerId, String type)
			throws PhrescoException {
		List<ArtifactGroup> artifactGroups = new ArrayList<ArtifactGroup>();
		for (String id : ids) {
			if(getArtifactGroup(id, customerId, type) != null) {
				artifactGroups.add(getArtifactGroup(id, customerId, type));
			}
		}
		
		return artifactGroups;
	}

	private ArtifactGroup getArtifactGroup(String id, String customerId, String type) throws PhrescoException {
		com.photon.phresco.commons.model.ArtifactInfo artifactInfo = mongoOperation.findOne(ARTIFACT_INFO_COLLECTION_NAME, 
				new Query(Criteria.whereId().is(id)), com.photon.phresco.commons.model.ArtifactInfo.class);
		if(artifactInfo == null) {
			return null;
		}
		String artifactGroupId = artifactInfo.getArtifactGroupId();
		Query query = createCustomerIdQuery(customerId);
		Criteria typeCriteria = Criteria.whereId().is(type);
		query.addCriteria(typeCriteria);
		Criteria artifactGroupCriteria = Criteria.whereId().is(artifactGroupId);
		query.addCriteria(artifactGroupCriteria);
		ArtifactGroupDAO artifactGroupDAO = mongoOperation.findOne(ARTIFACT_GROUP_COLLECTION_NAME, query, ArtifactGroupDAO.class);
		ArtifactGroup artifactGroup = convertArtifactDAO(artifactGroupDAO);
		return makeArtifactGroup(artifactGroup, artifactInfo);
	}

	private ArtifactGroup makeArtifactGroup(ArtifactGroup artifactGroup,
			com.photon.phresco.commons.model.ArtifactInfo artifactInfo) {
		ArtifactGroup group = new ArtifactGroup();
		group.setGroupId(artifactGroup.getGroupId());
		group.setArtifactId(artifactGroup.getArtifactId());
		group.setPackaging(artifactGroup.getPackaging());
		List<com.photon.phresco.commons.model.ArtifactInfo> artifactInfos = new ArrayList<com.photon.phresco.commons.model.ArtifactInfo>();
		artifactInfos.add(artifactInfo);
		group.setVersions(artifactInfos);
		return artifactGroup;
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
