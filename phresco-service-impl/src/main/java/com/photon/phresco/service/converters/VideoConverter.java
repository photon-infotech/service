package com.photon.phresco.service.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.document.mongodb.MongoOperations;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;

import com.photon.phresco.commons.model.VideoInfo;
import com.photon.phresco.commons.model.VideoType;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.dao.VideoInfoDAO;
import com.photon.phresco.service.dao.VideoTypeDAO;
import com.photon.phresco.util.ServiceConstants;

public class VideoConverter implements Converter<VideoInfoDAO, VideoInfo>, ServiceConstants {

	@Override
	public VideoInfo convertDAOToObject(VideoInfoDAO dao,
			MongoOperations mongoOperation) throws PhrescoException {
		VideoInfo videoInfo = new VideoInfo();
		videoInfo.setId(dao.getId());
		videoInfo.setImageurl(dao.getImageurl());
		videoInfo.setName(dao.getName());
		videoInfo.setVideoList(createVideoType(dao.getVideoListId(), mongoOperation));
		videoInfo.setDescription(dao.getDescription());
		videoInfo.setHelpText(dao.getHelpText());
		videoInfo.setStatus(dao.getStatus());
		videoInfo.setSystem(dao.isSystem());
		return videoInfo;
	}

	@Override
	public VideoInfoDAO convertObjectToDAO(VideoInfo videoInfo)
			throws PhrescoException {
		VideoInfoDAO videoDAO = new VideoInfoDAO();
		videoDAO.setId(videoInfo.getId());
		videoDAO.setName(videoInfo.getName());
		videoDAO.setImageurl(videoInfo.getImageurl());
		videoDAO.setVideoListId(createTypeId(videoInfo.getVideoList()));
		videoDAO.setDescription(videoInfo.getDescription());
		videoDAO.setHelpText(videoInfo.getHelpText());
		videoDAO.setStatus(videoInfo.getStatus());
		videoDAO.setSystem(videoInfo.isSystem());
		return videoDAO;
	}

	private List<String> createTypeId(List<VideoType> videoList) {
		List<String> ids = new ArrayList<String>();
		for (VideoType videoType : videoList) {
			ids.add(videoType.getId());
		}
		return ids;
	}

	private List<VideoType> createVideoType(List<String> videoListId,MongoOperations mongoOperation) throws PhrescoException {
		List<VideoType> videoTypes = new ArrayList<VideoType>();
		List<VideoTypeDAO> videoTypeDAO = mongoOperation.find(VIDEOTYPESDAO_COLLECTION_NAME, 
				new Query(Criteria.whereId().in(videoListId.toArray())), VideoTypeDAO.class);
		for(VideoTypeDAO vType : videoTypeDAO){
			Converter<VideoTypeDAO, VideoType> videoConverter = 
				(Converter<VideoTypeDAO, VideoType>) ConvertersFactory.getConverter(VideoTypeDAO.class);
			VideoType videoType = videoConverter.convertDAOToObject(vType, mongoOperation);
			videoTypes.add(videoType);
		}
		return videoTypes;
	}

}
