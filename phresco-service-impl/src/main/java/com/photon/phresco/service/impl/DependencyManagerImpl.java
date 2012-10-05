/*
 * ###
 * Phresco Service Implemenation
 * 
 * Copyright (C) 1999 - 2012 Photon Infotech Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ###
 */
/*******************************************************************************
 * Copyright (c) 2011 Photon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Photon Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.photon.in/legal/ppl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *     Photon - initial API and implementation
 ******************************************************************************/

package com.photon.phresco.service.impl;

import java.io.File;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.DownloadInfo;
import com.photon.phresco.commons.model.Element;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.DbManager;
import com.photon.phresco.service.api.DependencyManager;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.projects.DependencyService;

public class DependencyManagerImpl extends DependencyService implements DependencyManager {
	
	private DbManager dbManager = null;
	private String customerId = null;
	
	public DependencyManagerImpl() throws PhrescoException {
		PhrescoServerFactory.initialize();
		setDbManager(PhrescoServerFactory.getDbManager());
	}
	
	@Override
	public void configureProject(ApplicationInfo applicationInfo, File projectPath) throws PhrescoException {
		customerId = applicationInfo.getCustomerIds().get(0);
		
		if(StringUtils.isEmpty(customerId)) {
			throw new PhrescoException("CustomerId Should Not Be Null");
		}
		
		if(CollectionUtils.isNotEmpty(applicationInfo.getSelectedModules())) {
			String type = ArtifactGroup.Type.FEATURE.name();
			List<ArtifactGroup> selectedFeatures = dbManager.findSelectedArtifacts(applicationInfo.getSelectedModules(), customerId, type);
			updatePOMWithArtifacts(projectPath, selectedFeatures, type);
		}
		
		if(CollectionUtils.isNotEmpty(applicationInfo.getSelectedJSLibs())) {
			String type = ArtifactGroup.Type.JAVASCRIPT.name();
			List<ArtifactGroup> selectedFeatures = dbManager.findSelectedArtifacts(applicationInfo.getSelectedModules(), customerId, type);
			updatePOMWithArtifacts(projectPath, selectedFeatures, type);
		}
		
		Element pilotInfo = applicationInfo.getPilotInfo();
		String pilotId = pilotInfo.getId();
		ApplicationInfo projectInfo = dbManager.getProjectInfo(pilotId, customerId);
		if(projectInfo != null) {
			extractPilots(projectInfo, projectPath, customerId);
		}
		
		if(CollectionUtils.isNotEmpty(applicationInfo.getSelectedDatabases())) {
			List<DownloadInfo> selectedDbs = dbManager.findSelectedDatabases(applicationInfo.getSelectedDatabases(), customerId);
			createSqlFolder(selectedDbs, projectPath);
		}
		
		updateTestPom(projectPath);
	}
	
	public void setDbManager(DbManager dbManager) {
		this.dbManager = dbManager;
	}

	public DbManager getDbManager() {
		return dbManager;
	}
	
}
