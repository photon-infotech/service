/*
 * ###
 * Phresco Service
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
package com.photon.phresco.service.api;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.AdminConfigInfo;
import com.photon.phresco.model.ApplicationType;
import com.photon.phresco.model.DownloadInfo;
import com.photon.phresco.model.ProjectInfo;
import com.photon.phresco.model.SettingsTemplate;
import com.photon.phresco.model.Technology;
import com.photon.phresco.model.VideoInfo;
import com.photon.phresco.service.model.ArtifactInfo;
import com.photon.phresco.service.model.EntityType;

 public interface RepositoryManager {

    /**
     * Returns the list of application types and technologies.
     * @return list of application types
     * @throws PhrescoException
     */
    List<ApplicationType> getApplicationTypes() throws PhrescoException;

    /**
     * Adds the application type to the existing list of application types
     * @param apptypes
     * @throws PhrescoException
     */
    void addApplicationTypes(List<ApplicationType> apptypes) throws PhrescoException;

    /**
     * Adds the application type to the existing list of application types
     * @param apptypes
     * @throws PhrescoException
     */
    void addTechnology(String apptype, Technology tech) throws PhrescoException;

    void addVideo(VideoInfo videoInfo, File dirPath) throws PhrescoException;

    void removeVideo(VideoInfo videoInfo) throws PhrescoException;

    void addSettings(SettingsTemplate settingsTemplte, File Dirpath) throws PhrescoException;

    void addAdminConfig(AdminConfigInfo configInfo) throws PhrescoException;

    void removeAdminConfig(AdminConfigInfo videoInfo)throws PhrescoException;

    void addDownload(DownloadInfo downloadInfo, File dirPath);

    void Download(DownloadInfo downloadInfo);

    String getArtifactAsString(String filePath) throws PhrescoException;

    boolean isExist(String filePath) throws PhrescoException;

    InputStream getArtifactAsStream(String filePath) throws PhrescoException;

    String getRepositoryURL() throws PhrescoException;

     String getFrameworkVersion() throws PhrescoException;

	 String getServerVersion() throws PhrescoException;

	 String getCiConfigPath() throws PhrescoException;

	 String getCiSvnPath() throws PhrescoException;
	
	 String getCiCredentialXmlFilePath() throws PhrescoException;
	
	 String getJavaHomeConfigPath() throws PhrescoException;
	
	 String getMavenHomeConfigPath() throws PhrescoException;
	
	 String getSettingConfigFile() throws PhrescoException;
	
	 String getHomePageJsonFile() throws PhrescoException;
	
	
	 String getAdminConfigFile() throws PhrescoException;
	
	 String getCredentialFile() throws PhrescoException;
	
	 String getAuthServiceURL() throws PhrescoException;
	 
	 String getEmailExtFile() throws PhrescoException;

	List<ProjectInfo> getPilotProjects(String id);

    String addArtifact(ArtifactInfo info, File artifactFile)
            throws PhrescoException;

	String getFrameWorkLatestFile() throws PhrescoException;

}
