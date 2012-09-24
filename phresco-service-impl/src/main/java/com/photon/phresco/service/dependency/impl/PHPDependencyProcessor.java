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

import org.apache.log4j.Logger;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.util.ServerUtil;

/**
 * Dependency handler for PHP projects
 *
 * @author arunachalam_l
 *
 */
public class PHPDependencyProcessor  extends AbstractJsLibDependencyProcessor {
	private static final Logger S_LOGGER = Logger.getLogger(PHPDependencyProcessor.class);
    /**
     * @param dependencyManager
     */
    public PHPDependencyProcessor(RepositoryManager repoManager) {
        super(repoManager);
    }
    
    @Override
    public void process(ApplicationInfo info, File path) throws PhrescoException {
    	S_LOGGER.debug("Entering Method PHPDependencyProcessor.process(ProjectInfo info, File path)");
    	S_LOGGER.debug("process() Path=" + path.getPath());
        S_LOGGER.debug("process() ProjectCode=" + info.getCode());

            
//            //copy pilot projects
//            if(StringUtils.isNotBlank(info.getPilotProjectName())){
////	            List<ProjectInfo> pilotProjects = getRepositoryManager().getPilotProjects(technology.getId());
//	            if(CollectionUtils.isEmpty(pilotProjects)){
//	                return;
//	            }
//	
//	            for (ProjectInfo projectInfo : pilotProjects) {
//	                List<String> urls = projectInfo.getPilotProjectUrls();
//	                if(urls != null){
//	                    for (String url : urls) {
//	                        DependencyUtils.extractFiles(url, path);
//	                    }
//	                }
//	            }
//            }
            ApplicationInfo projectInfo = PhrescoServerFactory.getDbManager().getProjectInfo(info.getTechInfo().getVersion(), info.getName());
            ArtifactGroup pilotContent = projectInfo.getPilotContent();
            String contentURL = ServerUtil.createContentURL(pilotContent.getGroupId(), pilotContent.getArtifactId(),
            		pilotContent.getVersions().get(0).getVersion(), pilotContent.getPackaging());
            if(projectInfo != null) {
                DependencyUtils.extractFiles(contentURL, path, "");
            }
            String id = info.getTechInfo().getVersion();
//            updatePOMWithModules(path, info.getSelectedModules(), id);
//            updatePOMWithPluginArtifact(path,info.getSelectedModules(), id);
//            extractJsLibraries(path, info.getSelectedJSLibs());
            createSqlFolder(info, path);
            updateTestPom(path);
    }

    @Override
	protected String getModulePathKey() {
		return "php.modules.path";
	}
	
	@Override
	protected String getJsLibPathKey() {
		return "php.jslib.path";
	}
}
