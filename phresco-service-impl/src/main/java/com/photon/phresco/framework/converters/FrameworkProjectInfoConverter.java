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
package com.photon.phresco.framework.converters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.document.mongodb.MongoOperations;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ProjectInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.converters.ConvertersFactory;
import com.photon.phresco.service.dao.ApplicationInfoDAO;
import com.photon.phresco.service.dao.ProjectInfoDAO;
import com.photon.phresco.util.ServiceConstants;

public class FrameworkProjectInfoConverter implements Converter<ProjectInfoDAO, ProjectInfo>, ServiceConstants {

	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(FrameworkProjectInfoConverter.class.getName());
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();
	
	@Override
	public ProjectInfo convertDAOToObject(ProjectInfoDAO dao,
			MongoOperations mongoOperation) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("ArtifactGroupConverter.convertDAOToObject:Entry");
		}
		ProjectInfo projectInfo = new ProjectInfo();
		projectInfo.setName(dao.getName());
		projectInfo.setDisplayName(dao.getDisplayName());
		projectInfo.setDescription(dao.getDescription());
		projectInfo.setId(dao.getId());
		projectInfo.setProjectCode(dao.getProjectCode());
		projectInfo.setStartDate(dao.getStartDate());
		projectInfo.setEndDate(dao.getEndDate());
		List<ApplicationInfo> applicationInfos = getApplicationInfos(dao.getApplicationInfoIds(), mongoOperation);
		if(CollectionUtils.isNotEmpty(applicationInfos)) {
			projectInfo.setAppInfos(applicationInfos);
		}
		projectInfo.setCustomerIds(dao.getCustomerIds());
		projectInfo.setPreBuilt(dao.isPreBuilt());
		if (isDebugEnabled) {
			LOGGER.debug("ArtifactGroupConverter.convertDAOToObject:Exit");
		}
		return projectInfo;
	}

	@Override
	public ProjectInfoDAO convertObjectToDAO(ProjectInfo projectInfo)
			throws PhrescoException {
		ProjectInfoDAO projectInfoDAO = new ProjectInfoDAO();
		projectInfoDAO.setId(projectInfo.getId());
		projectInfoDAO.setStartDate(projectInfo.getStartDate());
		projectInfoDAO.setEndDate(projectInfo.getEndDate());
		projectInfoDAO.setProjectCode(projectInfo.getProjectCode());
		projectInfoDAO.setApplicationInfoIds(createAppInfoIds(projectInfo));
		projectInfoDAO.setCustomerIds(projectInfo.getCustomerIds());
		projectInfoDAO.setPreBuilt(projectInfo.isPreBuilt());
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
	
	@SuppressWarnings("unchecked")
	private List<ApplicationInfo> getApplicationInfos(List<String> appIds, MongoOperations mongoOperation) throws PhrescoException {
		List<ApplicationInfo> applicationInfos = new ArrayList<ApplicationInfo>();
		List<ApplicationInfoDAO> appInfoDAOs = mongoOperation.find(APPLICATION_INFO_COLLECTION_NAME, 
				new Query(Criteria.whereId().in(appIds.toArray())), ApplicationInfoDAO.class);
		Converter<ApplicationInfoDAO, ApplicationInfo> converter = 
			(Converter<ApplicationInfoDAO, ApplicationInfo>) ConvertersFactory.getFrameworkConverter(ApplicationInfoDAO.class);
		for (ApplicationInfoDAO applicationInfoDAO : appInfoDAOs) {
			ApplicationInfo applicationInfo = converter.convertDAOToObject(applicationInfoDAO, mongoOperation);
			applicationInfos.add(applicationInfo);
		}
		if (isDebugEnabled) {
			LOGGER.debug("ArtifactGroupConverter.getApplicationInfos:Exit");
		}
		return applicationInfos;
	}
}
