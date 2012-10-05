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
import com.photon.phresco.commons.model.Element;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.DbManager;
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
        	
        	DbManager dbManager = getDbManager();
        	Element pilotInfo = info.getPilotInfo();
        	String id = pilotInfo.getId();
        	String customerId = info.getCustomerIds().get(0);
        	ApplicationInfo projectInfo = dbManager.getProjectInfo(id, customerId);
            ArtifactGroup pilotContent = projectInfo.getPilotContent();
            
            String contentURL = ServerUtil.createContentURL(pilotContent.getGroupId(), pilotContent.getArtifactId(),
            		pilotContent.getVersions().get(0).getVersion(), pilotContent.getPackaging());
            
            if(projectInfo != null) {
                DependencyUtils.extractFiles(contentURL, path, customerId);
            }
            
            String techId = info.getTechInfo().getVersion();
            
            if(CollectionUtils.isNotEmpty(info.getSelectedModules())) {
            	List<ArtifactGroup> selectedArtifacts = getSelectedArtifacts(info.getSelectedModules(), customerId);
            	updatePOMWithModules(path, selectedArtifacts, techId);
                updatePOMWithPluginArtifact(path, selectedArtifacts, techId);
            }
            
            if(CollectionUtils.isNotEmpty(info.getSelectedJSLibs())) {
            	List<ArtifactGroup> selectedArtifacts = getSelectedArtifacts(info.getSelectedJSLibs(), customerId);
            	extractJsLibraries(path, selectedArtifacts, customerId);
            }
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
