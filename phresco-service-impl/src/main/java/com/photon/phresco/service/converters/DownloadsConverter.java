/**
 * Phresco Service Implemenation
 *
 * Copyright (C) 1999-2013 Photon Infotech Inc.
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
package com.photon.phresco.service.converters;

import org.springframework.data.document.mongodb.MongoOperations;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;

import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.DownloadInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.dao.ArtifactGroupDAO;
import com.photon.phresco.service.dao.DownloadsDAO;
import com.photon.phresco.util.ServiceConstants;

public class DownloadsConverter implements Converter<DownloadsDAO, DownloadInfo>, ServiceConstants {

	@Override
	public DownloadInfo convertDAOToObject(DownloadsDAO dao,
			MongoOperations mongoOperation) throws PhrescoException {
		DownloadInfo downloadInfo = new DownloadInfo();
		downloadInfo.setId(dao.getId());
		downloadInfo.setAppliesToTechIds(dao.getAppliesToTechIds());
		downloadInfo.setCategory(dao.getCategory());
		downloadInfo.setDescription(dao.getDescription());
		downloadInfo.setCreationDate(dao.getCreationDate());
		downloadInfo.setName(dao.getName());
		downloadInfo.setPlatformTypeIds(dao.getPlatformTypeIds());
		downloadInfo.setStatus(dao.getStatus());
		downloadInfo.setSystem(dao.isSystem());
		downloadInfo.setArtifactGroup(createArtifactGroup(dao.getArtifactGroupId(), mongoOperation));
		return downloadInfo;
	}

	@Override
	public DownloadsDAO convertObjectToDAO(DownloadInfo downloadInfo)
			throws PhrescoException {
		DownloadsDAO downloadsDAO = new DownloadsDAO();
		downloadsDAO.setId(downloadInfo.getId());
		downloadsDAO.setName(downloadInfo.getName());
		downloadsDAO.setAppliesToTechIds(downloadInfo.getAppliesToTechIds());
		downloadsDAO.setArtifactGroupId(downloadInfo.getArtifactGroup().getId());
		downloadsDAO.setCategory(downloadInfo.getCategory());
		downloadsDAO.setDescription(downloadInfo.getDescription());
		downloadsDAO.setPlatform(downloadInfo.getPlatformTypeIds());
		downloadsDAO.setStatus(downloadInfo.getStatus());
		downloadsDAO.setSystem(downloadInfo.isSystem());
		downloadsDAO.setCustomerIds(downloadInfo.getCustomerIds());
		return downloadsDAO;
	}
	
	private ArtifactGroup createArtifactGroup(String artifactGroupId, MongoOperations mongoOperation) throws PhrescoException {
		ArtifactGroupDAO artifactGroupDAO = mongoOperation.findOne(ARTIFACT_GROUP_COLLECTION_NAME, 
				new Query(Criteria.whereId().is(artifactGroupId)), ArtifactGroupDAO.class);
		Converter<ArtifactGroupDAO, ArtifactGroup> artifactConverter = 
			(Converter<ArtifactGroupDAO, ArtifactGroup>) ConvertersFactory.getConverter(ArtifactGroupDAO.class);
		return artifactConverter.convertDAOToObject(artifactGroupDAO, mongoOperation);
	}
	
}
