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
package com.photon.phresco.service.converters;

import org.springframework.data.document.mongodb.MongoOperations;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;

import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.VideoType;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.dao.ArtifactGroupDAO;
import com.photon.phresco.service.dao.VideoTypeDAO;
import com.photon.phresco.util.ServiceConstants;

public class VideoTypeConverter implements Converter<VideoTypeDAO, VideoType>, ServiceConstants {
	
	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(VideoTypeConverter.class.getName());
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();
	
	@Override
	public VideoType convertDAOToObject(VideoTypeDAO dao,
			MongoOperations mongoOperation) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("ArtifactGroupConverter.convertDAOToObject:Entry");
		}
		VideoType videoType = new VideoType();
		videoType.setId(dao.getId());
		videoType.setUrl(dao.getUrl());
		videoType.setName(dao.getName());
		videoType.setArtifactGroup(createArtifactGroup(dao.getArtifactGroupId(),mongoOperation));
		videoType.setDescription(dao.getDescription());
		videoType.setHelpText(dao.getHelpText());
		videoType.setStatus(dao.getStatus());
		videoType.setVideoInfoId(dao.getVideoInfoId());
		videoType.setSystem(dao.isSystem());
		videoType.setType(dao.getType());
		videoType.setCodec(dao.getCodec());
		if (isDebugEnabled) {
			LOGGER.debug("ArtifactGroupConverter.convertDAOToObject:Exit");
		}
		return videoType;
	}

	@Override
	public VideoTypeDAO convertObjectToDAO(VideoType videoType)
			throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("ArtifactGroupConverter.convertObjectToDAO:Entry");
		}
		VideoTypeDAO videoDAO = new VideoTypeDAO();
		videoDAO.setId(videoType.getId());
		videoDAO.setVideoInfoId(videoType.getVideoInfoId());
		videoDAO.setUrl(videoType.getUrl());
		videoDAO.setName(videoType.getName());
		videoDAO.setArtifactGroupId(videoType.getArtifactGroup().getId());
		videoDAO.setDescription(videoType.getDescription());
		videoDAO.setHelpText(videoType.getHelpText());
		videoDAO.setStatus(videoType.getStatus());
		videoDAO.setSystem(videoType.isSystem());
		videoDAO.setType(videoType.getType());
		videoDAO.setCodec(videoType.getCodec());
		if (isDebugEnabled) {
			LOGGER.debug("ArtifactGroupConverter.convertObjectToDAO:Exit");
		}
		return videoDAO;
	}
	
	private ArtifactGroup createArtifactGroup(String artifactGroupId,MongoOperations mongoOperation) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("ArtifactGroupConverter.createArtifactGroup:Entry");
		}
		ArtifactGroupDAO artifactGroupDAO = mongoOperation.findOne(ARTIFACT_GROUP_COLLECTION_NAME, 
				new Query(Criteria.whereId().is(artifactGroupId)), ArtifactGroupDAO.class);
		Converter<ArtifactGroupDAO, ArtifactGroup> artifactConverter = 
			(Converter<ArtifactGroupDAO, ArtifactGroup>) ConvertersFactory.getConverter(ArtifactGroupDAO.class);
		if (isDebugEnabled) {
			LOGGER.debug("ArtifactGroupConverter.createArtifactGroup:Exit");
		}
		return artifactConverter.convertDAOToObject(artifactGroupDAO, mongoOperation);
	}


}
