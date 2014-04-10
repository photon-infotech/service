/**
 * Phresco Service
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
package com.photon.phresco.service.api;

import java.io.File;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ProjectInfo;
import com.photon.phresco.exception.PhrescoException;

/**
 * A Facade class should organize the project creation using several Managers and Processors.
 * 
 * @author arunachalam_l
 *
 */
public interface ProjectServiceManager {
	
	/**
	 * @param projectInfo
	 * @param projectPath
	 */
	void createProject(ProjectInfo projectInfo, String tempFolderPath)  throws PhrescoException;
	
	/**
	 * @param projectInfo
	 * @param projectPath
	 */
	void updateProject(ProjectInfo projectInfo, String tempFolderPath)  throws PhrescoException;
	
	/**
	 * @param projectInfo
	 * @param projectPath
	 * @return 
	 */
	File updateDocumentProject(ApplicationInfo projectInfo)  throws PhrescoException;
	
	/**
	 * @param projectInfo
	 * @param projectPath
	 */
	void deleteProject(ProjectInfo projectInfo, File projectPath)  throws PhrescoException;

}
