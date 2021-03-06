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
package com.photon.phresco.service.impl;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.Element;
import com.photon.phresco.commons.model.ProjectInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.api.DbManager;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.ProjectServiceManager;
import com.photon.phresco.service.util.DependencyUtils;
import com.photon.phresco.service.util.ServerUtil;
import com.photon.phresco.util.Constants;
import com.photon.phresco.util.ProjectUtils;
import com.photon.phresco.util.ServiceConstants;
import com.photon.phresco.util.Utility;

public class ProjectServiceManagerImpl implements ProjectServiceManager, Constants {
	
	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(ProjectServiceManagerImpl.class.getName());
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();
	private DbManager dbManager;
	
	public ProjectServiceManagerImpl() throws PhrescoException {
		PhrescoServerFactory.initialize();
		dbManager = PhrescoServerFactory.getDbManager();
	}
	
	public synchronized void createProject(ProjectInfo projectInfo, String tempFolderPath) throws PhrescoException {
		// TODO:This code should be moved into server initialization
		if (isDebugEnabled) {
			LOGGER.debug("ProjectServiceManagerImpl.createProject:Entry");
			if(projectInfo == null) {
				LOGGER.warn("ProjectServiceManagerImpl.createProject","status=\"Bad Request\"", "message=\"ProjectInfo is empty\"");
				throw new PhrescoException("ProjectInfo is empty");
			}
			LOGGER.info("ProjectServiceManagerImpl.createProject", "customerId=\"" + projectInfo.getCustomerIds().get(0) + "\"", "creationDate=\"" + projectInfo.getCreationDate() + "\"",
					"projectCode=\"" + projectInfo.getProjectCode() + "\"");
		}
		PhrescoServerFactory.initialize();
		PhrescoServerFactory.getArchetypeExecutor().execute(projectInfo, tempFolderPath);
		if(projectInfo.isPreBuilt()) {
			createPilots(projectInfo.getAppInfos().get(0), tempFolderPath, projectInfo.getCustomerIds().get(0));
		}
		if(projectInfo.isMultiModule() && !projectInfo.getAppInfos().get(0).isCreated()) {
			createProjectFolders(projectInfo, projectInfo.getAppInfos().get(0).getAppDirName(), new File(tempFolderPath));
		}
		if(isDebugEnabled) {
			LOGGER.debug("ProjectServiceManagerImpl.createProject:Exit");
		}
	}
	
	private void createProjectFolders(ProjectInfo info, String appDirName,
			File file) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("ArchetypeExecutorImpl.createProjectFolders:Entry");
			if (info == null) {
				LOGGER.warn("ArchetypeExecutorImpl.createProjectFolders",
						ServiceConstants.STATUS_BAD_REQUEST,
						"message=\"ProjectInfo is empty\"");
				throw new PhrescoException("ProjectInfo is empty");
			}
			LOGGER.info("ArchetypeExecutorImpl.createProjectFolders",
					"customerId=\"" + info.getCustomerIds().get(0) + "\"",
					"creationDate=\"" + info.getCreationDate() + "\"",
					"projectCode=\"" + info.getProjectCode() + "\"");
		}
		StringBuilder builder = new StringBuilder(file.getPath());
		builder.append(File.separator);
		builder.append(appDirName);
		builder.append(File.separator);
		builder.append(DOT_PHRESCO_FOLDER);
		File phrescoFolder = new File(builder.toString());
		phrescoFolder.mkdirs();
		if (isDebugEnabled) {
			LOGGER.info("create .phresco folder inside the project");
		}
		ApplicationInfo applicationInfo = info.getAppInfos().get(0);
		applicationInfo.setId(new Element().getId());
		info.setAppInfos(Collections.singletonList(applicationInfo));
		ProjectUtils.writeProjectInfo(info, phrescoFolder);
		if (isDebugEnabled) {
			LOGGER.debug("ArchetypeExecutorImpl.createProjectFolders:Exit");
		}
	}
	
	public void updateProject(ProjectInfo projectInfo, String tempFolderPath) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("ProjectServiceManagerImpl.updateProject:Entry");
			if(projectInfo == null) {
				LOGGER.warn("ProjectServiceManagerImpl.updateProject","status=\"Bad Request\"", "message=\"ProjectInfo is empty\"");
				throw new PhrescoException("ProjectInfo is empty");
			}
			LOGGER.info("ProjectServiceManagerImpl.updateProject", "customerId=\"" + projectInfo.getCustomerIds().get(0) + "\"", "creationDate=\"" + projectInfo.getCreationDate() + "\"",
					"projectCode=\"" + projectInfo.getProjectCode() + "\"", "tempFolderPath=\""+ tempFolderPath +"\"");
		}
		File projectPath = new File(tempFolderPath);
		projectPath.mkdirs();
		PhrescoServerFactory.initialize();
		
		ProjectInfo projectInfoClone = projectInfo.clone();
		if(CollectionUtils.isNotEmpty(projectInfo.getAppInfos())) {
			for (int i = 0; i < projectInfo.getAppInfos().size(); i++) {
				if(!projectInfo.getAppInfos().get(i).isCreated()) {
					projectInfoClone.setAppInfos(Arrays.asList(projectInfo.getAppInfos().get(i)));
					createProject(projectInfoClone, tempFolderPath);
				}
			}
		}
		if (isDebugEnabled) {
			LOGGER.debug("ProjectServiceManagerImpl.updateProject:Exit");
		}
	}

	private void pilotCreationFromAppInfos(String tempFolderPath, String customerId,
			DbManager dBManager, List<ApplicationInfo> appInfos)
			throws PhrescoException {
		for (ApplicationInfo applicationInfo : appInfos) {
			if(applicationInfo.getPilotInfo() != null) {
				ApplicationInfo appInfo = dBManager.getApplicationInfo(applicationInfo.getId());
				if(appInfo.getPilotInfo() == null) {
					createPilots(applicationInfo, tempFolderPath, customerId);
				} else if(!StringUtils.equals(appInfo.getPilotInfo().getId(), applicationInfo.getPilotInfo().getId())) {
					createPilots(applicationInfo, tempFolderPath, customerId);
				}
			}
		}
	}

	public void findNewlyAddedProject(Map<String, String> appInfoMap, List<ApplicationInfo> appInfosInDB,
			List<ApplicationInfo> appInfos, List<ApplicationInfo> createdAppInfos) {
		if(isDebugEnabled) {
			LOGGER.debug("ProjectServiceManagerImpl.findNewlyAddedProject:Entry");
		}
		for (ApplicationInfo appInfoInDB : appInfosInDB) {
			appInfoMap.put(appInfoInDB.getId(), appInfoInDB.getId());
		}
		
		for (ApplicationInfo appInfo : appInfos) {
			if (!appInfoMap.containsKey(appInfo.getId())) {
				createdAppInfos.add(appInfo);
			}
		}
		if(isDebugEnabled) {
			LOGGER.debug("ProjectServiceManagerImpl.findNewlyAddedProject:Exit");
		}
	}

	private void createPilots(ApplicationInfo applicationInfo, String tempFolderPath, String customerId) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("ProjectServiceManagerImpl.createPilots:Entry");
			if(StringUtils.isEmpty(customerId)) {
				LOGGER.warn("ProjectServiceManagerImpl.createPilots","status=\"Bad Request\"", "message=\"customerId is empty\"");
			}
			LOGGER.info("ProjectServiceManagerImpl.createPilots", "customerId=\""+ customerId +"\"");
		}
		String appId = "";
		Element pilotElement = applicationInfo.getPilotInfo();
		if(pilotElement != null) {
			appId = pilotElement.getId();
		} else {
			appId = applicationInfo.getId();
		}
		ApplicationInfo pilotInfo = dbManager.getApplicationInfo(appId);
		ArtifactGroup pilotContent = pilotInfo.getPilotContent();
		String version = pilotContent.getVersions().get(0).getVersion();
		String contentURL = ServerUtil.createContentURL(pilotContent.getGroupId(), pilotContent.getArtifactId(), 
				version, pilotContent.getPackaging());
		DependencyUtils.extractFiles(contentURL, new File(tempFolderPath, applicationInfo.getAppDirName()), customerId);
		if(isDebugEnabled) {
			LOGGER.debug("ProjectServiceManagerImpl.createPilots:Exit");
		}
	}

	public File updateDocumentProject(ApplicationInfo projectInfo) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("ProjectServiceManagerImpl.updateDocumentProject:Entry");
		}
		File tempPath = new File(Utility.getPhrescoTemp(), UUID.randomUUID().toString() + File.separator + projectInfo.getCode());
		if(isDebugEnabled) {
			LOGGER.debug("ProjectServiceManagerImpl.updateDocumentProject:Exit");
		}
		return tempPath;
	}
	
	public void deleteProject(ProjectInfo projectInfo, File projectPath) throws PhrescoException {

	}
}
