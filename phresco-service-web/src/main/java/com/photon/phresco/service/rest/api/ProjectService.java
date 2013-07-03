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
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.ProjectInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.api.DbManager;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.ProjectServiceManager;
import com.photon.phresco.service.dao.ArtifactGroupDAO;
import com.photon.phresco.service.impl.DbService;
import com.photon.phresco.service.util.MAGICNUMBER;
import com.photon.phresco.service.util.ServerUtil;
import com.photon.phresco.util.ArchiveUtil;
import com.photon.phresco.util.ArchiveUtil.ArchiveType;
import com.photon.phresco.util.ServiceConstants;
import com.photon.phresco.util.Utility;
import com.phresco.pom.exception.PhrescoPomException;
import com.phresco.pom.util.PomProcessor;

/**
 * Phresco Service Class hosted at the URI path "/api"
 */

@Path(ServiceConstants.REST_API_PROJECT)
public class ProjectService extends DbService {
	private static final String ZIP = ".zip";
	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(ProjectService.class.getName());
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();
	private DbManager dbManager;
	
	public ProjectService() throws PhrescoException {
		PhrescoServerFactory.initialize();
		dbManager = PhrescoServerFactory.getDbManager();
    }
	
	@POST
	@Path(ServiceConstants.REST_API_PROJECT_CREATE)
	@Produces(ServiceConstants.MEDIATYPE_ZIP)
	@Consumes(MediaType.APPLICATION_JSON)
	public StreamingOutput createProject(@Context HttpServletRequest request, ProjectInfo projectInfo) throws PhrescoException, IOException {
		LOGGER.debug("ProjectService.createProject : Entry");
		if(projectInfo == null) {
			LOGGER.warn("ProjectService.createProject" , "status=\"Bad Request\"" , "remoteAddress=" + request.getRemoteAddr(),
					"user=" + request.getParameter("userId"));
			return null;
		}
		String tempFolderPath = "";
		try {
			tempFolderPath = ServerUtil.getTempFolderPath();
			ProjectServiceManager projectService = PhrescoServerFactory.getProjectService();
			buildCreateLogMessage(request, projectInfo);
			for (int i=0; i < projectInfo.getNoOfApps(); i++) {
				projectService.createProject(cloneProjectInfo(projectInfo, i), tempFolderPath);
			}
			createParentPom(tempFolderPath, projectInfo);
			createDependency(tempFolderPath, projectInfo);
			ArchiveUtil.createArchive(tempFolderPath, tempFolderPath + ZIP, ArchiveType.ZIP);
			ServiceOutput serviceOutput = new ServiceOutput(tempFolderPath);
			dbManager.storeCreatedProjects(projectInfo);
			dbManager.updateUsedObjects(projectInfo);
			LOGGER.debug("ProjectService.createProject() : Exit");
			return serviceOutput;
		} catch (Exception pe) {
			LOGGER.error("ProjectService.createProject", "remoteAddress=" + request.getRemoteAddr(),
					"user=" + request.getParameter("userId"), "status=\"Failure\"", "message=\"" + pe.getLocalizedMessage() + "\"");
			throw new PhrescoException(pe);
		}
	}
	
	private void createParentPom(String tempFolderPath,
			ProjectInfo projectInfo) throws PhrescoException {
		if(!projectInfo.isMultiModule()) {
			return;
		}
		tempFolderPath = tempFolderPath + "/" + projectInfo.getName();
		List<ApplicationInfo> appInfos = projectInfo.getAppInfos();
		File pomFile = new File(tempFolderPath, "pom.xml");
		PomProcessor processor;
		try {
			processor = new PomProcessor(pomFile);
			processor.setGroupId("com.photon.phresco");
			processor.setArtifactId(projectInfo.getName());
			processor.setVersion(projectInfo.getVersion());
			processor.setPackaging("pom");
			for (ApplicationInfo applicationInfo : appInfos) {
				processor.addModule(applicationInfo.getCode());
				processor.save();
			}
		} catch (PhrescoPomException e) {
			throw new PhrescoException(e);
		}
		
	}
	
	private void createDependency(String tempFolderPath, ProjectInfo projectInfo) {
		if(!projectInfo.isMultiModule()) {
			return;
		}
		tempFolderPath = tempFolderPath + "/" + projectInfo.getName();
		List<ApplicationInfo> appInfos = projectInfo.getAppInfos();
		for (ApplicationInfo applicationInfo : appInfos) {
			List<String> dependentModules = applicationInfo.getDependentModules();
			if(CollectionUtils.isNotEmpty(dependentModules)) {
				File projectPom = new File(tempFolderPath + File.separator + applicationInfo.getCode() + File.separator + "pom.xml");
				try {
					PomProcessor processor = new PomProcessor(projectPom);
					for (String appCode : dependentModules) {
						ApplicationInfo appInfo = getAppInfo(appCode, appInfos);
						processor.addDependency("com.photon.phresco", appInfo.getCode(), projectInfo.getVersion());
						processor.save();
					}
				} catch (PhrescoPomException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private ApplicationInfo getAppInfo(String appCode, List<ApplicationInfo> applicationInfos) {
		for (ApplicationInfo applicationInfo : applicationInfos) {
			if(applicationInfo.getCode().equals(appCode)) {
				return applicationInfo;
			}
		}
		return null;
	}
	
	private void buildCreateLogMessage(HttpServletRequest request, ProjectInfo projectInfo) throws PhrescoException {
		try {
			if (isDebugEnabled) {
				for (ApplicationInfo applicationInfo : projectInfo.getAppInfos()) {
					LOGGER.warn("ProjectService.createProject", "remoteAddress=" + request.getRemoteAddr() , "technology=" + 
							applicationInfo.getTechInfo().getName(), "user=" + request.getParameter("userId"),
							"authType=" + request.getParameter("authType"),"customer=" + getCustomerNameById(projectInfo.getCustomerIds().get(0)),
							"action=" + "CREATE", "endpoint=" + request.getRequestURI(),  "method=" + request.getMethod(), 
							"projectCode=" + "\"" + projectInfo.getProjectCode() + "\"", "totalNoOfApps=" + projectInfo.getNoOfApps(), getApplications(projectInfo));
				}
			}
		} catch (Exception e) {
			throw new PhrescoException(e); 
		}
	}
	
	private void buildUpdateLogMessage(HttpServletRequest request, ProjectInfo projectInfo) throws PhrescoException {
		try {
			if (isDebugEnabled) {
				for (ApplicationInfo applicationInfo : projectInfo.getAppInfos()) {
					LOGGER.warn("ProjectService.updateProject", "remoteAddress=" + request.getRemoteAddr() , "technology=" + 
							applicationInfo.getTechInfo().getName(), "user=" + request.getParameter("userId"),
							"authType=" + request.getParameter("authType"),"customer=" + getCustomerNameById(projectInfo.getCustomerIds().get(0)),
							"action=" + "UPDATE", "endpoint=" + request.getRequestURI(),  "method=" + request.getMethod(), 
							"projectCode=" + "\"" + projectInfo.getProjectCode() + "\"", "totalNoOfApps=" + projectInfo.getNoOfApps(), 
							getApplications(projectInfo), getFeatures(applicationInfo), getJslibs(applicationInfo));
				}
			}
		} catch (Exception e) {
			throw new PhrescoException(e); 
		}
	}	

	private String getApplications(ProjectInfo projectInfo) {
		StringBuffer stringBuffer = new StringBuffer();
		List<ApplicationInfo> appInfos = projectInfo.getAppInfos();
		int j = 1;
		for (int i = 0; i < appInfos.size(); i++) {
			stringBuffer.append("application" + j + "_appCode=");
			stringBuffer.append( "\""+ appInfos.get(i).getCode() + "\"" + ",");
			stringBuffer.append("application" + j + "_technology=");
			stringBuffer.append( "\""+ appInfos.get(i).getTechInfo().getName() + "\"");
			if(! (i == appInfos.size()-1 )) {
				stringBuffer.append(",");
			}
			j++;
		}
		return stringBuffer.toString();
	}
	
	private String getFeatures(ApplicationInfo applicationInfo) {
		StringBuffer buffer = new StringBuffer();
		List<String> selectedModules = applicationInfo.getSelectedModules();
		if(CollectionUtils.isEmpty(selectedModules)) {
			return "";
		}
		int j = 1;
		List<ArtifactInfo> infos = DbService.getMongoOperation().find(ARTIFACT_INFO_COLLECTION_NAME, 
				new Query(Criteria.whereId().in(selectedModules.toArray())), ArtifactInfo.class);
		for (int i = 0; i < infos.size(); i++) {
			ArtifactGroupDAO group = DbService.getMongoOperation().findOne(ARTIFACT_GROUP_COLLECTION_NAME, 
					new Query(Criteria.whereId().is(infos.get(i).getArtifactGroupId())), ArtifactGroupDAO.class);
			if(! (i == 0)) {
				buffer.append(",");
			}
			buffer.append("feature" + j + "_name=" + "\""+ group.getName() + "\"" + ",");
			buffer.append("feature" + j + "_version=" + "\""+ infos.get(i).getVersion() + "\"");
			j++;
		}
		return buffer.toString();
	}
	
	private String getJslibs(ApplicationInfo applicationInfo) {
		StringBuffer buffer = new StringBuffer();
		List<String> selectedModules = applicationInfo.getSelectedJSLibs();
		if(CollectionUtils.isEmpty(selectedModules)) {
			return "";
		}
		int j = 1;
		List<ArtifactInfo> infos = DbService.getMongoOperation().find(ARTIFACT_INFO_COLLECTION_NAME, 
				new Query(Criteria.whereId().in(selectedModules.toArray())), ArtifactInfo.class);
		for (int i = 0; i < infos.size(); i++) {
			ArtifactGroupDAO group = DbService.getMongoOperation().findOne(ARTIFACT_GROUP_COLLECTION_NAME, 
					new Query(Criteria.whereId().is(infos.get(i).getArtifactGroupId())), ArtifactGroupDAO.class);
			if(! (i == 0)) {
				buffer.append(",");
			}
			buffer.append("jslibrary" + j + "_name=" + "\""+ group.getName() + "\"" + ",");
			buffer.append("jslibrary" + j + "_version=" + "\""+ infos.get(i).getVersion() + "\"");
			j++;
		}
		return buffer.toString();
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
	public StreamingOutput updateProject(@Context HttpServletRequest request, ProjectInfo projectInfo) throws PhrescoException {
    	if (isDebugEnabled) {
			LOGGER.debug("ProjectService.updateProject : Entry");
		}
    	if(projectInfo == null) {
			if (isDebugEnabled) {
				LOGGER.warn("ProjectService.updateProject" , "status=\"Bad Request\"" , "remoteAddress=" + request.getRemoteAddr(),
						"user=" + request.getParameter("userId"));
			}
			return null;
		}
    	buildUpdateLogMessage(request, projectInfo);
		String projectPathStr = Utility.getPhrescoTemp() + UUID.randomUUID().toString();
		try {
			ProjectServiceManager projectService = PhrescoServerFactory.getProjectService();
			projectService.updateProject(projectInfo, projectPathStr);
			
			ArchiveUtil.createArchive(projectPathStr, projectPathStr + ZIP, ArchiveType.ZIP);
		} catch (Exception pe) {
			LOGGER.error("ProjectService.updateProject", "remoteAddress=" + request.getRemoteAddr(),
					"user=" + request.getParameter("userId"), "status=\"Failure\"", "message=\"" + pe.getLocalizedMessage() + "\"");
			throw new PhrescoException(pe);
		}
		if (isDebugEnabled) {
			LOGGER.debug("ProjectService.updateProject : Exit");
		}
		return new ServiceOutput(projectPathStr);
	}
	
	@POST
	@Path(ServiceConstants.REST_APP_UPDATEDOCS)
	@Produces(ServiceConstants.MEDIATYPE_ZIP)
	@Consumes(MediaType.APPLICATION_JSON)
	public StreamingOutput updateDoc(ApplicationInfo appInfo) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("Entering Method PhrescoService.updateDoc(ProjectInfo projectInfo)");
			LOGGER.debug("updateProject() ProjectInfo=" + appInfo.getCode());
		}
		String projectPathStr = "";
		try {
			ProjectServiceManager projectService = PhrescoServerFactory.getProjectService();
			File projectPath = projectService.updateDocumentProject(appInfo);
			projectPathStr = projectPath.getPath();
			ArchiveUtil.createArchive(projectPathStr, projectPathStr + ZIP, ArchiveType.ZIP);
		} catch (Exception pe) {
			LOGGER.error("Error During updateProject(projectInfo)" + pe);
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
				LOGGER.debug("ServiceOutput.write : Entry");
			}
			FileInputStream fis = null;
			File path = new File(projectPath);
			try {
				fis = new FileInputStream(projectPath + ZIP);
				byte[] buf = new byte[MAGICNUMBER.BYTESMALLSIZE];
				int i = 0;
				while ((i = fis.read(buf)) != -1) {
					output.write(buf, 0, i);
				}
			} catch (Exception e) {
				LOGGER.error("ServiceOutput.write " , "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"" );
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