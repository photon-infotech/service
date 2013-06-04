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

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.document.mongodb.MongoOperations;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;

import com.photon.phresco.commons.model.VideoInfo;
import com.photon.phresco.commons.model.VideoType;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.dao.VideoInfoDAO;
import com.photon.phresco.service.dao.VideoTypeDAO;
import com.photon.phresco.util.ServiceConstants;

public class VideoConverter implements Converter<VideoInfoDAO, VideoInfo>, ServiceConstants {

	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(VideoConverter.class.getName());
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();
	
	@Override
	public VideoInfo convertDAOToObject(VideoInfoDAO dao,
			MongoOperations mongoOperation) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("ArtifactGroupConverter.convertDAOToObject:Entry");
		}
		VideoInfo videoInfo = new VideoInfo();
		videoInfo.setId(dao.getId());
		videoInfo.setImageurl(dao.getImageurl());
		videoInfo.setName(dao.getName());
		videoInfo.setVideoList(createVideoType(dao.getVideoListId(), mongoOperation));
		videoInfo.setDescription(dao.getDescription());
		videoInfo.setHelpText(dao.getHelpText());
		videoInfo.setStatus(dao.getStatus());
		videoInfo.setSystem(dao.isSystem());
		if (isDebugEnabled) {
			LOGGER.debug("ArtifactGroupConverter.convertDAOToObject:Exit");
		}
		return videoInfo;
	}

	@Override
	public VideoInfoDAO convertObjectToDAO(VideoInfo videoInfo)
			throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("ArtifactGroupConverter.convertObjectToDAO:Entry");
		}
		VideoInfoDAO videoDAO = new VideoInfoDAO();
		videoDAO.setId(videoInfo.getId());
		videoDAO.setName(videoInfo.getName());
		videoDAO.setImageurl(videoInfo.getImageurl());
		videoDAO.setVideoListId(createTypeId(videoInfo.getVideoList()));
		videoDAO.setDescription(videoInfo.getDescription());
		videoDAO.setHelpText(videoInfo.getHelpText());
		videoDAO.setStatus(videoInfo.getStatus());
		videoDAO.setSystem(videoInfo.isSystem());
		if (isDebugEnabled) {
			LOGGER.debug("ArtifactGroupConverter.convertObjectToDAO:Entry");
		}
		return videoDAO;
	}

	private List<String> createTypeId(List<VideoType> videoList) {
		if (isDebugEnabled) {
			LOGGER.debug("ArtifactGroupConverter.createTypeId:Entry");
		}
		List<String> ids = new ArrayList<String>();
		for (VideoType videoType : videoList) {
			ids.add(videoType.getId());
		}
		if (isDebugEnabled) {
			LOGGER.debug("ArtifactGroupConverter.createTypeId:Exit");
		}
		return ids;
	}

	private List<VideoType> createVideoType(List<String> videoListId, MongoOperations mongoOperation) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("ArtifactGroupConverter.createVideoType:Entry");
		}
		List<VideoType> videoTypes = new ArrayList<VideoType>();
		List<VideoTypeDAO> videoTypeDAO = mongoOperation.find(VIDEOTYPESDAO_COLLECTION_NAME, 
				new Query(Criteria.whereId().in(videoListId.toArray())), VideoTypeDAO.class);
		
		for(VideoTypeDAO vType : videoTypeDAO){
			Converter<VideoTypeDAO, VideoType> videoConverter = 
				(Converter<VideoTypeDAO, VideoType>) ConvertersFactory.getConverter(VideoTypeDAO.class);
			VideoType videoType = videoConverter.convertDAOToObject(vType, mongoOperation);
			videoTypes.add(videoType);
		}
		if (isDebugEnabled) {
			LOGGER.debug("ArtifactGroupConverter.createVideoType:Exit");
		}
		return videoTypes;
	}

}
