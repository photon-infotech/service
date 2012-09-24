package com.photon.phresco.service.converters;

import org.springframework.data.document.mongodb.MongoOperations;

import com.photon.phresco.commons.model.DownloadInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.dao.DownloadInfoDAO;
import com.photon.phresco.util.ServiceConstants;

public class DownloadsConverter implements Converter<DownloadInfoDAO, DownloadInfo>, ServiceConstants {

	@Override
	public DownloadInfo convertDAOToObject(DownloadInfoDAO dao,
			MongoOperations mongoOperation) throws PhrescoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DownloadInfoDAO convertObjectToDAO(DownloadInfo element)
			throws PhrescoException {
		// TODO Auto-generated method stub
		return null;
	}

}
