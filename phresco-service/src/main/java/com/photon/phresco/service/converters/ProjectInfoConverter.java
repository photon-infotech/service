package com.photon.phresco.service.converters;

import org.springframework.data.document.mongodb.MongoOperations;

import com.photon.phresco.commons.model.ProjectInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.dao.ProjectInfoDAO;
import com.photon.phresco.util.ServiceConstants;

public class ProjectInfoConverter implements Converter<ProjectInfoDAO, ProjectInfo>, ServiceConstants {

	@Override
	public ProjectInfo convertDAOToObject(ProjectInfoDAO dao,
			MongoOperations mongoOperation) throws PhrescoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProjectInfoDAO convertObjectToDAO(ProjectInfo element)
			throws PhrescoException {
		// TODO Auto-generated method stub
		return null;
	}

}
