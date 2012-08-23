package com.photon.phresco.service.api;

import com.photon.phresco.commons.model.RepoInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.ArchetypeInfo;
import com.photon.phresco.model.ProjectInfo;
import com.photon.phresco.model.Technology;

public interface DbManager {
    
    /**
     * Returns archetype information details based on techId
     * @param techId
     * @return
     * @throws PhrescoException
     */
    ArchetypeInfo getArchetypeInfo(String techId) throws PhrescoException;
    
    /**
     * Returns the project info based on techId and projectName
     * @param techId
     * @param projectName
     * @return
     * @throws PhrescoException
     */
    ProjectInfo getProjectInfo(String techId, String projectName) throws PhrescoException;
    
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
    void updateCreatedProjects(ProjectInfo projectInfo) throws PhrescoException;
}
