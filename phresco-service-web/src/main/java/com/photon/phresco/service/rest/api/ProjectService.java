/*
 * ###
 * Service Web Archive
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
package com.photon.phresco.service.rest.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

import org.apache.log4j.Logger;
import org.codehaus.plexus.util.FileUtils;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.DbManager;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.ProjectServiceManager;
import com.photon.phresco.util.ArchiveUtil;
import com.photon.phresco.util.ArchiveUtil.ArchiveType;
import com.photon.phresco.util.FileUtil;
import com.photon.phresco.util.ServiceConstants;

/**
 * Phresco Service Class hosted at the URI path "/api"
 */

@Path(ServiceConstants.REST_API_PROJECT)
public class ProjectService {
	private static final String ZIP = ".zip";
    private static final Logger S_LOGGER = Logger.getLogger(ProjectService.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	private DbManager dbManager = null;
	
	public ProjectService() throws PhrescoException {
		PhrescoServerFactory.initialize();
		dbManager = PhrescoServerFactory.getDbManager();
    }
	
	@POST
	@Path(ServiceConstants.REST_API_PROJECT_CREATE)
	@Produces(ServiceConstants.MEDIATYPE_ZIP)
	@Consumes(MediaType.APPLICATION_JSON)
	public StreamingOutput createProject(ApplicationInfo applicationInfo) throws PhrescoException, IOException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method PhrescoService.createProject(ProjectInfo projectInfo)");
		}
		String projectPathStr = "";
		File projectPath = null;
		try {
			if (isDebugEnabled) {
				S_LOGGER.debug("createProject() ProjectInfo=" + applicationInfo.getCode());
			}
			ProjectServiceManager projectService = PhrescoServerFactory.getProjectService(applicationInfo);
			projectPath = projectService.createProject(applicationInfo);
			
			projectPathStr = projectPath.getPath();

			if (isDebugEnabled) {
				S_LOGGER.debug("Project Path = " + projectPathStr);
			}

			ArchiveUtil.createArchive(projectPathStr, projectPathStr + ZIP, ArchiveType.ZIP);
			FileUtil.delete(projectPath);
			ServiceOutput serviceOutput = new ServiceOutput(projectPathStr);
			if(serviceOutput != null) {
			    dbManager.storeCreatedProjects(applicationInfo);
			  //  updateUsedObjects(projectInfo);
			}
			
			return serviceOutput;
		} catch (Exception pe) {
			S_LOGGER.error("Error During createProject(projectInfo)", pe);
			throw new PhrescoException(pe);
		}
	}

	/*private void updateUsedObjects(ApplicationInfo projectInfo) throws PhrescoException {
	    
        if(projectInfo.getTechInfo() != null) {
           Technology archetypeInfo = projectInfo.getTechInfo();
            dbManager.updateUsedObjects(ServiceConstants.ARCHETYPEINFO_COLLECTION_NAME, 
                    ServiceConstants.REST_API_ARTIFACTID, archetypeInfo.getArtifactId());
            dbManager.updateUsedObjects(ServiceConstants.ARCHETYPEINFO_COLLECTION_NAME, 
                    ServiceConstants.REST_API_ARTIFACTID, archetypeInfo.);
            
        }
        
        if(projectInfo.getTechnology().getModules() != null) {
            List<ArtifactGroup> modules = projectInfo.getTechnology().getModules();
            for (ArtifactGroup moduleGroup : modules) {
                ArtifactInfo module = moduleGroup.getVersions().get(0);
                dbManager.updateUsedObjects(ServiceConstants.MODULES_COLLECTION_NAME, ServiceConstants.REST_API_NAME, module.getName());
            }
        }
        
        if (StringUtils.isNotEmpty(projectInfo.getPilotProjectName())) {
            dbManager.updateUsedObjects(ServiceConstants.PILOTS_COLLECTION_NAME, ServiceConstants.REST_API_NAME, projectInfo.getName());
        }
        
        if (CollectionUtils.isNotEmpty(projectInfo.getSelectedServers())) {
            List<DownloadInfo> servers = projectInfo.getServers();
            for (DownloadInfo server : servers) {
                dbManager.updateUsedObjects(ServiceConstants.DOWNLOAD_COLLECTION_NAME, ServiceConstants.REST_API_NAME, server.getName());
            }
        }
        
        if (CollectionUtils.isNotEmpty(projectInfo.getTechnology().getDatabases())) {
            List<DownloadInfo> databases = projectInfo.getTechnology().getDatabases();
            for (DownloadInfo database : databases) {
                dbManager.updateUsedObjects(ServiceConstants.DOWNLOAD_COLLECTION_NAME, ServiceConstants.REST_API_NAME, database.getName());
            }
        }
        
    }
*/
    @POST
	@Path(ServiceConstants.REST_API_PROJECT_UPDATE)
	@Produces(ServiceConstants.MEDIATYPE_ZIP)
	@Consumes(MediaType.APPLICATION_JSON)
	public StreamingOutput updateProject(ApplicationInfo appInfo) throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method PhrescoService.updateProject(ProjectInfo projectInfo)");
			S_LOGGER.debug("updateProject() ProjectInfo=" + appInfo.getCode());
		}
		String projectPathStr = "";
		try {
			ProjectServiceManager projectService = PhrescoServerFactory.getProjectService(appInfo);
			File projectPath = projectService.updateProject(appInfo);
			projectPathStr = projectPath.getPath();
			if (isDebugEnabled) {
				S_LOGGER.debug("updateProject() ProjectPath=" + projectPathStr);
			}
			ArchiveUtil.createArchive(projectPathStr, projectPathStr + ZIP, ArchiveType.ZIP);
		} catch (Exception pe) {
			S_LOGGER.error("Error During updateProject(projectInfo)" + pe);
			throw new PhrescoException(pe);
			// //TODO: Need to design a proper way to throw the error response
			// to client
		}
		ServiceOutput serviceOutput = new ServiceOutput(projectPathStr);
		if(serviceOutput != null) {
		    dbManager.updateCreatedProjects(appInfo);
		}
		return serviceOutput;
	}
	
	@POST
	@Path(ServiceConstants.REST_APP_UPDATEDOCS)
	@Produces(ServiceConstants.MEDIATYPE_ZIP)
	@Consumes(MediaType.APPLICATION_JSON)
	public StreamingOutput updateDoc(ApplicationInfo appInfo) throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method PhrescoService.updateDoc(ProjectInfo projectInfo)");
			S_LOGGER.debug("updateProject() ProjectInfo=" + appInfo.getCode());
		}
		String projectPathStr = "";
		try {
			ProjectServiceManager projectService = PhrescoServerFactory.getProjectService(appInfo);
			File projectPath = projectService.updateDocumentProject(appInfo);
			projectPathStr = projectPath.getPath();
			ArchiveUtil.createArchive(projectPathStr, projectPathStr + ZIP, ArchiveType.ZIP);
		} catch (Exception pe) {
			S_LOGGER.error("Error During updateProject(projectInfo)" + pe);
			throw new PhrescoException(pe);
		}
		return new ServiceOutput(projectPathStr);
	}
	
	class ServiceOutput implements StreamingOutput {
        private String projectPath = "";

		public ServiceOutput(String projectPath) {
			this.projectPath = projectPath;
		}

		public void write(OutputStream output) throws IOException{
			if (isDebugEnabled) {
				S_LOGGER.debug("Entering Method PhrescoService.write(OutputStream output)");
			}
			FileInputStream fis = null;
			File path = new File(projectPath);
			if (isDebugEnabled) {
				S_LOGGER.debug("PhrescoService.write() FILe PATH = " + path.getPath());
			}
			try {
				fis = new FileInputStream(projectPath + ZIP);
				byte[] buf = new byte[1024];
				int i = 0;
				while ((i = fis.read(buf)) != -1) {
					output.write(buf, 0, i);
				}
			} catch (Exception e) {
				S_LOGGER.error("Error During Stream write()", e);
				throw new WebApplicationException(e);
			}
			// //TODO: Temporay File path deleted. Need to revisit the logic
			finally {
				if (fis != null) {
					fis.close();
					FileUtils.deleteDirectory(path.getParentFile());
				}
			}
		}
	}
	
}