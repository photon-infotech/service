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
import java.io.InputStream;

import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.RepoInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.model.ArtifactInfo;

public interface RepositoryManager {

    String getArtifactAsString(String filePath, String customerId) throws PhrescoException;

    InputStream getArtifactAsStream(String filePath, String customerId) throws PhrescoException;

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

    boolean addArtifact(ArtifactInfo info, File artifactFile, String customerId) throws PhrescoException;

    String getFrameWorkLatestFile() throws PhrescoException;

    boolean isExist(String filePath, String customerId) throws PhrescoException;
    
    RepoInfo createCustomerRepository(String customerId, String repoName) throws PhrescoException;
    
    String getLatestArtifact(String groupId, String artifactId) throws PhrescoException;
    
    boolean deleteArtifact(String customerId,ArtifactGroup artifactGroup) throws PhrescoException;
    
    boolean deleteCustomerRepo(String customerId) throws PhrescoException;
    
}
