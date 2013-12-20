/**
 * Phresco Service
 *
 * Copyright (C) 1999-2013 Photon Infotech Inc.
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

import java.util.List;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.DownloadInfo;
import com.photon.phresco.commons.model.ProjectInfo;
import com.photon.phresco.commons.model.RepoInfo;
import com.photon.phresco.commons.model.Technology;
import com.photon.phresco.commons.model.User;
import com.photon.phresco.commons.model.VideoInfo;
import com.photon.phresco.commons.model.WebService;
import com.photon.phresco.exception.PhrescoException;

public interface DbManager {
    
    /**
     * Returns archetype information details based on techId
     * @param techId
     * @return
     * @throws PhrescoException
     */
    ArtifactGroup getArchetypeInfo(String techId, String customerId) throws PhrescoException;
    
    /**
     * Returns the project info based on techId and projectName
     * @param techId
     * @param projectName
     * @return
     * @throws PhrescoException
     */
    ApplicationInfo getApplicationInfo(String pilotId) throws PhrescoException;
    
    /**
     * Returns the documents for the given technology type
     * @param techId
     * @return
     * @throws PhrescoException
     */
    Technology getTechnologyDoc(String techId) throws PhrescoException;

    /**
     * Returns the repository information for the given customerId
     * @param customerId
     * @throws PhrescoException
     */
    RepoInfo getRepoInfo(String customerId) throws PhrescoException;
    
    /**
     * Store the created project details in Db
     * @param projectInfo
     * @throws PhrescoException
     */
    void storeCreatedProjects(ProjectInfo projectInfo) throws PhrescoException;
    
    /**
     * To update the created project information in db
     * @param projectInfo
     * @throws PhrescoException
     */
    void updateCreatedProjects(ApplicationInfo projectInfo) throws PhrescoException;
    
    void updateUsedObjects(ProjectInfo projectInfo) throws PhrescoException;
    
    List<ArtifactGroup> findSelectedArtifacts(List<String> ids, String customerId, String type) throws PhrescoException;
    
    List<WebService> findSelectedWebservices(List<String> ids) throws PhrescoException;
    
    List<DownloadInfo> findSelectedServers(List<String> ids, String customerId) throws PhrescoException;
    
    List<DownloadInfo> findSelectedDatabases(List<String> ids, String customerId) throws PhrescoException;
    
    User authenticate(String username, String password) throws PhrescoException;
    
	User authenticateUserId(String username) throws PhrescoException;

    ProjectInfo getProjectInfo(String projectInfoId) throws PhrescoException;
    
    List<VideoInfo> getVideos() throws PhrescoException;
    
    String getLatestFrameWorkVersion() throws PhrescoException;
    
    RepoInfo getRepoInfoById(String id) throws PhrescoException;
    
    List<ArtifactGroup> findDefaultFeatures(String techId, String type, String customerId) throws PhrescoException;
    
    List<ArtifactGroup> findSelectedArtifacts(List<String> ids) throws PhrescoException;
    
    ArtifactGroup getArtifactGroup(String artifactInfoId) throws PhrescoException;
}
