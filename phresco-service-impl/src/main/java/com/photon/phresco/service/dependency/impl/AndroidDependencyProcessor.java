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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.Module;
import com.photon.phresco.model.ModuleGroup;
import com.photon.phresco.model.ProjectInfo;
import com.photon.phresco.model.Technology;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.pom.AndroidTestPOMUpdater;
import com.photon.phresco.util.TechnologyTypes;
import com.phresco.pom.exception.PhrescoPomException;
import com.phresco.pom.util.PomProcessor;

/**
 * Android Dependency processor
 * 
 * @author arunachalam_l
 * 
 */
public class AndroidDependencyProcessor extends AbstractJsLibDependencyProcessor {

	private static final String UNIT_TEST_POM_XML = "/test/unit/pom.xml";
    private static final String PERFORMANCE_TEST_POM_XML = "/test/performance/pom.xml";
    private static final String FUNCTIONAL_TEST_POM_XML = "/test/functional/pom.xml";
    private static final Logger S_LOGGER = Logger.getLogger(AndroidDependencyProcessor.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	private static final String ANDROID_VERSION = "android.version";
	private static final String POM_FILE = "pom.xml";

	/**
	 * @param dependencyManager
	 */
	public AndroidDependencyProcessor(RepositoryManager repoManager) {
		super(repoManager);
	}

	@Override
	public void process(ProjectInfo info, File path) throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method AndroidDependencyProcessor.process(ProjectInfo info, File path)");
			S_LOGGER.debug("process() ProjectCode=" + info.getCode());
			S_LOGGER.debug("process() Path=" + path.getPath());
		}
		Technology technology = info.getTechnology();
		// copy pilot projects
//		if (StringUtils.isNotBlank(info.getPilotProjectName())) {
//			List<ProjectInfo> pilotProjects = getRepositoryManager().getPilotProjects(technology.getId());
//			if (CollectionUtils.isEmpty(pilotProjects)) {
//				return;
//			}
//			for (ProjectInfo projectInfo : pilotProjects) {
//				List<String> urls = projectInfo.getPilotProjectUrls();
//				if (urls != null) {
//					for (String url : urls) {
//						DependencyUtils.extractFiles(url, path);
//					}
//				}
//			}
//		}
		
		//To Get Pilot Project
		ProjectInfo projectInfo = PhrescoServerFactory.getDbManager().
		    getProjectInfo(info.getTechnology().getId(), info.getPilotProjectName());
		if(projectInfo != null) {
		    DependencyUtils.extractFiles(projectInfo.getProjectURL(), path, info.getCustomerId());
		}
		
		updateAndroidVersion(path, info);
		try {
	    	 List<ModuleGroup> modules = technology.getModules();
	    	 if((CollectionUtils.isNotEmpty(modules)) && modules != null) {
	    		 updatePOMModules(path, modules);
	    	 }
		} catch (JAXBException e) {
		    throw new PhrescoException(e);
		} catch (PhrescoPomException e) {
		    throw new PhrescoException(e);
		}
		AndroidTestPOMUpdater.updatePOM(path);
		if (technology.getId().equals(TechnologyTypes.ANDROID_HYBRID)) {
			extractJsLibraries(path, info.getTechnology().getJsLibraries());
		}
		updateTestPom(path);
	}

	private void updatePOMModules(File path, List<com.photon.phresco.model.ModuleGroup> modules)
	throws PhrescoException, JAXBException, PhrescoPomException {
		try {
			if (isDebugEnabled) {
				S_LOGGER.debug("extractModules() path=" + path.getPath());
			}
			File pomFile = new File(path, "pom.xml");
			if (pomFile.exists()) {
				PomProcessor processor = new PomProcessor(pomFile);
				for (com.photon.phresco.model.ModuleGroup moduleGroup : modules) {
					if (moduleGroup != null) {
					    List<Module> versions = moduleGroup.getVersions();
					    if (CollectionUtils.isNotEmpty(versions)) {
					        for (Module module : versions) {
					            processor.addDependency(module.getGroupId(), module.getArtifactId(), module.getVersion());
                            }
					    }
					}
				}
				processor.save();
			}
		} catch (IOException e) {
			throw new PhrescoException(e);
		}
	}

	@Override
	protected String getModulePathKey() {
		return "";
	}

	public void updateAndroidVersion(File path, ProjectInfo info) throws PhrescoException {
		File pomPath = new File(path ,POM_FILE);
		if(!pomPath.exists()) {
			return;
		}
		PomProcessor processor;
		List<File> fileList = new ArrayList<File>();
		fileList.add(new File(path, POM_FILE));
		fileList.add(new File(path, FUNCTIONAL_TEST_POM_XML));
		fileList.add(new File(path, PERFORMANCE_TEST_POM_XML));
		fileList.add(new File(path, UNIT_TEST_POM_XML));
		for (File pomFile : fileList) {
		    if(pomFile.exists()) {
    		    try {
    	            processor = new PomProcessor(pomFile);
    	            List<String> name = info.getTechnology().getVersions();
    	            for (String string : name) {
    	                String selectedVersion = string;
    	                processor.setProperty(ANDROID_VERSION, selectedVersion);
    	            }
    	            processor.save();
    	        } catch (Exception e) {
    	            throw new PhrescoException(e);
    	        }
            }
		}
	}

	@Override
	protected String getJsLibPathKey() {
		return "android.jslib.path";
	}

}