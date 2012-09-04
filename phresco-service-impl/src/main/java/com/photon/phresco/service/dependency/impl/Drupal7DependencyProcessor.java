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
package com.photon.phresco.service.dependency.impl;

import java.io.File;

import org.apache.log4j.Logger;

import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.ProjectInfo;
import com.photon.phresco.service.api.RepositoryManager;

/**
 * Dependency handler for PHP with Drupal 7
 * @author arunachalam_l
 *
 */
public class Drupal7DependencyProcessor  extends AbstractJsLibDependencyProcessor {
	private static final Logger S_LOGGER = Logger.getLogger(Drupal7DependencyProcessor.class);
	/**
     * @param dependencyManager
     */
    public Drupal7DependencyProcessor(RepositoryManager repoManager) {
        super(repoManager);
    }

	protected String getModulePathKey() {
		return "drupal7.modules.path";
	}
	
	@Override
	protected String getJsLibPathKey() {
		return "drupal7.jslib.path";
	}

	/* (non-Javadoc)
	 * @see com.photon.phresco.service.dependency.impl.AbstractJsLibDependencyProcessor#process(com.photon.phresco.model.ProjectInfo, java.io.File)
	 */
	@Override
	public void process(ProjectInfo info, File path) throws PhrescoException {
		S_LOGGER.debug("Entering Method Drupal7DependencyProcessor.process(ProjectInfo info, File path)");
		S_LOGGER.debug("process() Path=" + path.getPath());
		
		super.process(info, path);
		String id = info.getTechnology().getId();
		updatePOMWithModules(path, info.getTechnology().getModules(), id);
		updatePOMWithPluginArtifact(path,info.getTechnology().getModules(), id);
		createSqlFolder(info, path);
		updateTestPom(path);
	}
}
