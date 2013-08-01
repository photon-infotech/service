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
package com.photon.phresco.service.impl;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.Element;
import com.photon.phresco.commons.model.ProjectInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.DbManager;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.ProjectServiceManager;
import com.photon.phresco.service.util.DependencyUtils;
import com.photon.phresco.service.util.ServerUtil;
import com.photon.phresco.util.Constants;
import com.photon.phresco.util.Utility;

public class ProjectServiceManagerImpl implements ProjectServiceManager, Constants {
	
	private static final Logger S_LOGGER = Logger.getLogger(ProjectServiceManagerImpl.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	private DbManager dbManager;
	
	public ProjectServiceManagerImpl() throws PhrescoException {
		PhrescoServerFactory.initialize();
		dbManager = PhrescoServerFactory.getDbManager();
	}
	
	public synchronized void createProject(ProjectInfo projectInfo, String tempFolderPath) throws PhrescoException {
		// TODO:This code should be moved into server initialization
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method DefaultProjectService.createProject(ProjectInfo projectInfo)");
			S_LOGGER.debug("createProject() ProjectInfo =" + projectInfo.getProjectCode());
		}

		PhrescoServerFactory.initialize();
		PhrescoServerFactory.getArchetypeExecutor().execute(projectInfo, tempFolderPath);
	}

	public void updateProject(ProjectInfo projectInfo, String tempFolderPath) throws PhrescoException {
		File projectPath = new File(tempFolderPath);
		projectPath.mkdirs();
		String customerId = projectInfo.getCustomerIds().get(0);
		PhrescoServerFactory.initialize();
		List<ApplicationInfo> appInfos = projectInfo.getAppInfos();
		
		ProjectInfo projectInfoClone = projectInfo.clone();
		if(CollectionUtils.isNotEmpty(projectInfo.getAppInfos())) {
			for (int i = 0; i < projectInfo.getAppInfos().size(); i++) {
				if(!projectInfo.getAppInfos().get(i).isCreated()) {
					projectInfoClone.setAppInfos(Arrays.asList(projectInfo.getAppInfos().get(i)));
					createProject(projectInfoClone, tempFolderPath);
				}
			}
		}
		
		for (ApplicationInfo applicationInfo : appInfos) {
			if(applicationInfo.getPilotInfo() != null) {
				createPilots(applicationInfo, tempFolderPath, customerId);
			}
		}
		if (isDebugEnabled) {
			S_LOGGER.info("successfully updated application :" + projectInfo.getName());
		}
	}

	public void findNewlyAddedProject(Map<String, String> appInfoMap, List<ApplicationInfo> appInfosInDB,
			List<ApplicationInfo> appInfos, List<ApplicationInfo> createdAppInfos) {
		for (ApplicationInfo appInfoInDB : appInfosInDB) {
			appInfoMap.put(appInfoInDB.getId(), appInfoInDB.getId());
		}
		
		for (ApplicationInfo appInfo : appInfos) {
			if (!appInfoMap.containsKey(appInfo.getId())) {
				createdAppInfos.add(appInfo);
			}
		}
	}

	private void createPilots(ApplicationInfo applicationInfo, String tempFolderPath, String customerId) throws PhrescoException {
		Element pilotElement = applicationInfo.getPilotInfo();
		ApplicationInfo pilotInfo = dbManager.getApplicationInfo(pilotElement.getId());
		ArtifactGroup pilotContent = pilotInfo.getPilotContent();
		String version = pilotContent.getVersions().get(0).getVersion();
		String contentURL = ServerUtil.createContentURL(pilotContent.getGroupId(), pilotContent.getArtifactId(), 
				version, pilotContent.getPackaging());
		DependencyUtils.extractFiles(contentURL, new File(tempFolderPath, applicationInfo.getAppDirName()), customerId);
	}

	public File updateDocumentProject(ApplicationInfo projectInfo) throws PhrescoException {
		File tempPath = new File(Utility.getPhrescoTemp(), UUID.randomUUID().toString() + File.separator + projectInfo.getCode());
		try {
//			PhrescoServerFactory.getDocumentGenerator().generate(projectInfo, tempPath);
		} catch (Exception e) {
			throw new PhrescoException(e);
		}
		return tempPath;
	}
	
	public void deleteProject(ProjectInfo projectInfo, File projectPath) throws PhrescoException {

	}
}
