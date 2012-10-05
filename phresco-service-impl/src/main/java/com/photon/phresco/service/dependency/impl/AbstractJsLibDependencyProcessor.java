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
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.util.ServerUtil;

public abstract class AbstractJsLibDependencyProcessor extends AbstractDependencyProcessor {
	private static final Logger S_LOGGER = Logger.getLogger(AbstractJsLibDependencyProcessor.class);

	public AbstractJsLibDependencyProcessor(RepositoryManager repoManager) {
		super(repoManager);
	}
	@Override
	public void process(ApplicationInfo info, File path) throws PhrescoException {
		super.process(info, path);
	}

	@Override
	protected String getModulePathKey() {
		return null;
	}

	protected void extractJsLibraries(File path, List<ArtifactGroup> jsLibraries, String customerId) throws PhrescoException {
		S_LOGGER.debug("Entering Method AbstractJsLibDependencyProcessor.extractJsLibraries(File path, List<TupleBean> jsLibraries)");
		S_LOGGER.debug("extractJsLibraries() Filepath="+path.getPath());

		// should be implemented by clients who requires js libraries.
		if (CollectionUtils.isEmpty(jsLibraries)) {
			return;
		}
		
		File libPath = path;
		String jsLibPathKey = getJsLibPathKey();
		if(!StringUtils.isEmpty(jsLibPathKey)) {
			String modulesPathString=DependencyProcessorMessages.getString(jsLibPathKey);
			libPath = new File(path, modulesPathString);
		}
		
		for (ArtifactGroup moduleGroup : jsLibraries) {
            List<ArtifactInfo> versions = moduleGroup.getVersions();
            for (ArtifactInfo module : versions) {
                if(module != null) {
                	String contentURL = ServerUtil.createContentURL(moduleGroup.getGroupId(), moduleGroup.getArtifactId(), 
                			module.getVersion(), moduleGroup.getPackaging());
                    DependencyUtils.extractFiles(contentURL, libPath, customerId);
                }
            }
        }
//		for (ModuleGroup tupleBean : jsLibraries) {
//			Library library = getRepositoryManager().getJsLibrary(tupleBean.getId());
//
//			if (library == null) {
//				S_LOGGER.warn("JS library not found " + tupleBean.getId() + "  --> Path" + path.getPath());
//			}
//
//			String contentURL = library.getContentURL();
//			if(!StringUtils.isEmpty(contentURL)){
//				DependencyUtils.extractFiles(contentURL, libPath);
//			}
//		}
	}

	protected abstract String getJsLibPathKey();
}
