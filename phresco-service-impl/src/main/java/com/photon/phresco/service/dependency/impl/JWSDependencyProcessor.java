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

import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.DbManager;
import com.photon.phresco.service.api.RepositoryManager;
import com.phresco.pom.exception.PhrescoPomException;

public class JWSDependencyProcessor extends AbstractDependencyProcessor {
	private static final Logger S_LOGGER = Logger.getLogger(JWSDependencyProcessor.class);
	public JWSDependencyProcessor(RepositoryManager repoManager) {
		super(repoManager);
	}

	@Override
	protected String getModulePathKey() {
		return null;
	}
	
	@Override
	public void process(ApplicationInfo info, File path) throws PhrescoException {
		S_LOGGER.debug("Entering Method JWSDependencyProcessor.process(ProjectInfo info, File path)");
		S_LOGGER.debug("process() Path=" + path.getPath());
		
		super.process(info, path);
		String customerId = getCustomerId(info);
		
		try {
			if(CollectionUtils.isNotEmpty(info.getSelectedModules())) {
				List<ArtifactGroup> selectedFeatures = getSelectedArtifacts(info.getSelectedModules(), customerId);
				updatePOMWithModules(path, selectedFeatures);
			}
			createSqlFolder(info, path);
			updateTestPom(path);
		} catch (JAXBException e) {
			throw new PhrescoException(e);
		} catch (PhrescoPomException e) {
			throw new PhrescoException(e);
		}
		
	}
}