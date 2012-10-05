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
package com.photon.phresco.service.dependency.impl;

import java.io.File;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.RepositoryManager;

/**
 * Dependency handler for Sharepoint
 * @author sathishkumar_dh
 *
 */
public class SharePointDependencyProcessor extends AbstractDependencyProcessor {
	private static final Logger S_LOGGER = Logger.getLogger(JWSDependencyProcessor.class);

	public SharePointDependencyProcessor(RepositoryManager repoManager) {
		super(repoManager);
	}

	@Override
	protected String getModulePathKey() {
		return "sharepoint.modules.path";
	}
	
	public void process(ApplicationInfo info, File path) throws PhrescoException {
		S_LOGGER.debug("Entering Method WordPressDependencyProcessor.process(ProjectInfo info, File path)");
		S_LOGGER.debug("process() Path=" + path.getPath());
		
		super.process(info, path);
		String id = info.getTechInfo().getVersion();
		String customerId = getCustomerId(info);
		if(CollectionUtils.isNotEmpty(info.getSelectedModules())) {
			List<ArtifactGroup> selectedFeatures = getSelectedArtifacts(info.getSelectedModules(), customerId);
			updatePOMWithModules(path, selectedFeatures, id);
			updatePOMWithPluginArtifact(path, selectedFeatures, id);
		}
		updateTestPom(path);
	}
}