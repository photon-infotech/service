/**
 * Service Web Archive
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
package com.photon.phresco.service.rest.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.UUID;

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
import com.photon.phresco.commons.model.ProjectInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.DbManager;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.ProjectServiceManager;
import com.photon.phresco.service.util.MAGICNUMBER;
import com.photon.phresco.service.util.ServerUtil;
import com.photon.phresco.util.ArchiveUtil;
import com.photon.phresco.util.ArchiveUtil.ArchiveType;
import com.photon.phresco.util.ServiceConstants;
import com.photon.phresco.util.Utility;

/**
 * Phresco Service Class hosted at the URI path "/api"
 */

@Path(ServiceConstants.REST_API_PROJECT)
public class ProjectService {
	private static final String ZIP = ".zip";
    private static final Logger S_LOGGER = Logger.getLogger(ProjectService.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	private DbManager dbManager;
	
	public ProjectService() throws PhrescoException {
		PhrescoServerFactory.initialize();
		dbManager = PhrescoServerFactory.getDbManager();
    }
	
	@POST
	@Path(ServiceConstants.REST_API_PROJECT_CREATE)
	@Produces(ServiceConstants.MEDIATYPE_ZIP)
	@Consumes(MediaType.APPLICATION_JSON)
	public StreamingOutput createProject(ProjectInfo projectInfo) throws PhrescoException, IOException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method PhrescoService.createProject(ProjectInfo projectInfo)");
		}
		String tempFolderPath = "";
		try {
			tempFolderPath = ServerUtil.getTempFolderPath();
			
			if (isDebugEnabled) {
				S_LOGGER.debug("createProject() ProjectInfo=" + projectInfo.getId());
				S_LOGGER.debug("Project Path = " + tempFolderPath);
			}
			
			ProjectServiceManager projectService = PhrescoServerFactory.getProjectService();
			for (int i=0; i < projectInfo.getNoOfApps(); i++) {
				projectService.createProject(cloneProjectInfo(projectInfo, i), tempFolderPath);
			}
			
			ArchiveUtil.createArchive(tempFolderPath, tempFolderPath + ZIP, ArchiveType.ZIP);
			ServiceOutput serviceOutput = new ServiceOutput(tempFolderPath);
			dbManager.storeCreatedProjects(projectInfo);
			dbManager.updateUsedObjects(projectInfo);
			return serviceOutput;
		} catch (Exception pe) {
			S_LOGGER.error("Error During createProject(projectInfo)", pe);
			throw new PhrescoException(pe);
		}
	}

	private ProjectInfo cloneProjectInfo(ProjectInfo projectInfo, int i) {
		ProjectInfo clonedProjectInfo = projectInfo.clone();
		ApplicationInfo applicationInfo = clonedProjectInfo.getAppInfos().get(i);
		clonedProjectInfo.setAppInfos(Collections.singletonList(applicationInfo));
		return clonedProjectInfo;
	}

    @POST
	@Path(ServiceConstants.REST_API_PROJECT_UPDATE)
	@Produces(ServiceConstants.MEDIATYPE_ZIP)
	@Consumes(MediaType.APPLICATION_JSON)
	public StreamingOutput updateProject(ProjectInfo projectInfo) throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method PhrescoService.updateProject(ProjectInfo projectInfo)");
			S_LOGGER.debug("updateProject() ProjectInfo=" + projectInfo.getProjectCode());
		}
		String projectPathStr = Utility.getPhrescoTemp() + UUID.randomUUID().toString();
		try {
			ProjectServiceManager projectService = PhrescoServerFactory.getProjectService();
			projectService.updateProject(projectInfo, projectPathStr);
			
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
		return new ServiceOutput(projectPathStr);
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
			ProjectServiceManager projectService = PhrescoServerFactory.getProjectService();
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
				byte[] buf = new byte[MAGICNUMBER.BYTESMALLSIZE];
				int i = 0;
				while ((i = fis.read(buf)) != -1) {
					output.write(buf, 0, i);
				}
			} catch (Exception e) {
				S_LOGGER.error("Error During Stream write()", e);
				throw new WebApplicationException(e);
			} finally {
				if (fis != null) {
					fis.close();
//					FileUtils.deleteDirectory(path.getParentFile());
				}
			}
		}
	}
	
}