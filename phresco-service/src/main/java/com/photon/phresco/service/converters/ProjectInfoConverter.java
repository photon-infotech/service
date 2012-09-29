package com.photon.phresco.service.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.document.mongodb.MongoOperations;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ProjectInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.dao.ProjectInfoDAO;
import com.photon.phresco.util.ServiceConstants;

public class ProjectInfoConverter implements Converter<ProjectInfoDAO, ProjectInfo>, ServiceConstants {

	@Override
	public ProjectInfo convertDAOToObject(ProjectInfoDAO dao,
			MongoOperations mongoOperation) throws PhrescoException {
		ProjectInfo projectInfo = new ProjectInfo();
		projectInfo.setId(dao.getId());
		projectInfo.setProjectCode(dao.getProjectCode());
		List<ApplicationInfo> appInfos = mongoOperation.find(APPLICATION_INFO_COLLECTION_NAME, 
				new Query(Criteria.whereId().in(dao.getApplicationInfoIds().toArray())), ApplicationInfo.class);
		projectInfo.setAppInfos(appInfos);
		return projectInfo;
	}

	@Override
	public ProjectInfoDAO convertObjectToDAO(ProjectInfo projectInfo)
			throws PhrescoException {
		ProjectInfoDAO projectInfoDAO = new ProjectInfoDAO();
		projectInfoDAO.setId(projectInfo.getId());
		projectInfoDAO.setProjectCode(projectInfo.getProjectCode());
		projectInfoDAO.setApplicationInfoIds(createAppInfoIds(projectInfo));
		return projectInfoDAO;
	}

	private List<String> createAppInfoIds(ProjectInfo projectInfo) {
		List<ApplicationInfo> appInfos = projectInfo.getAppInfos();
		List<String> appInfoIds = new ArrayList<String>();
		for (ApplicationInfo applicationInfo : appInfos) {
			appInfoIds.add(applicationInfo.getId());
		}
		return appInfoIds;
	}

}
