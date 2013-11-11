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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.ModuleInfo;
import com.photon.phresco.commons.model.ProjectInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.api.DbManager;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.ProjectServiceManager;
import com.photon.phresco.service.dao.ArtifactGroupDAO;
import com.photon.phresco.service.impl.DbService;
import com.photon.phresco.service.util.ServerUtil;
import com.photon.phresco.util.ArchiveUtil;
import com.photon.phresco.util.ArchiveUtil.ArchiveType;
import com.photon.phresco.util.ServiceConstants;
import com.photon.phresco.util.Utility;
import com.phresco.pom.exception.PhrescoPomException;
import com.phresco.pom.util.PomProcessor;
import com.wordnik.swagger.annotations.ApiError;
import com.wordnik.swagger.annotations.ApiErrors;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Phresco Service Class hosted at the URI path "/api"
 */

@Controller
@RequestMapping(value = ServiceConstants.REST_API_PROJECT)
public class ProjectService extends DbService {
	private static final String ZIP = ".zip";
	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger("SplunkLogger");
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();
	private DbManager dbManager;
	
	public ProjectService() throws PhrescoException {
		PhrescoServerFactory.initialize();
		dbManager = PhrescoServerFactory.getDbManager();
    }
	
	@ApiOperation(value = " Create Project ")
	@ApiErrors(value = {@ApiError(code=500, reason = "Unable To Create")})
    @RequestMapping(value= ServiceConstants.REST_API_PROJECT_CREATE, consumes = MediaType.APPLICATION_JSON_VALUE, 
    		produces = ServiceConstants.MEDIATYPE_ZIP, method = RequestMethod.POST)
	public @ResponseBody byte[] createProject(HttpServletRequest request, 
			@ApiParam(value = "Projectinfo to create",	name = "projectInfo")@RequestBody ProjectInfo projectInfo) 
		throws PhrescoException, IOException {
		LOGGER.debug("ProjectService.createProject : Entry");
		if(projectInfo == null) {
			LOGGER.warn("ProjectService.createProject" , "status=\"Bad Request\"" , "remoteAddress=" + request.getRemoteAddr(),
					"user=" + request.getParameter("userId"));
			return null;
		}
		FileInputStream fis  = null;
		String tempFolderPath = "";
		try {
			tempFolderPath = ServerUtil.getTempFolderPath();
			ProjectServiceManager projectService = PhrescoServerFactory.getProjectService();
			buildCreateLogMessage(request, projectInfo);
			for (int i=0; i < projectInfo.getNoOfApps(); i++) {
				projectService.createProject(cloneProjectInfo(projectInfo, i), tempFolderPath);
			}
			createParentPom(tempFolderPath, projectInfo);
			handleDependencies(tempFolderPath, projectInfo);
			ArchiveUtil.createArchive(tempFolderPath, tempFolderPath + ZIP, ArchiveType.ZIP);
			LOGGER.debug("ProjectService.createProject() : Exit");
			fis = new FileInputStream(new File(tempFolderPath + ZIP));
			return IOUtils.toByteArray(fis);
		} catch (Exception pe) {
			LOGGER.error("ProjectService.createProject", "remoteAddress=" + request.getRemoteAddr(),
					"user=" + request.getParameter("userId"), "status=\"Failure\"", "message=\"" + pe.getLocalizedMessage() + "\"");
			throw new PhrescoException(pe);
		} finally {
			Utility.closeStream(fis);
		}
	}
	
	private void createParentPom(String tempFolderPath, ProjectInfo projectInfo) throws PhrescoException {
		if(! projectInfo.isMultiModule()) {
			return;
		}
		List<ApplicationInfo> appInfos = projectInfo.getAppInfos();
		for (ApplicationInfo applicationInfo : appInfos) {
			if(applicationInfo.isCreated()) {
				continue;
			}
			List<ModuleInfo> modules = applicationInfo.getModules();
			File pomFile = new File(tempFolderPath + File.separator + applicationInfo.getAppDirName(), "pom.xml");
			try {
				PomProcessor processor = new PomProcessor(pomFile);
				processor.setGroupId("com.photon.phresco");
				processor.setArtifactId(applicationInfo.getCode());
				processor.setVersion(applicationInfo.getVersion());
				processor.setPackaging("pom");
				if(CollectionUtils.isNotEmpty(modules)) {
					for (ModuleInfo moduleInfo : modules) {
						processor.addModule(moduleInfo.getCode());
					}
				}
				processor.save();
			} catch (PhrescoPomException e) {
				throw new PhrescoException(e);
			}
		}
		
	}
	
	private void handleDependencies(String tempFolderPath, ProjectInfo projectInfo) throws PhrescoException {
		if(! projectInfo.isMultiModule()) {
			return;
		}
		List<ApplicationInfo> appInfos = projectInfo.getAppInfos();
		for (ApplicationInfo applicationInfo : appInfos) {
			List<String> dependentModules = applicationInfo.getDependentModules();
			if(CollectionUtils.isNotEmpty(dependentModules)) {
				for (String appCode : dependentModules) {
					ApplicationInfo depApp = getApplicationInfo(appCode, projectInfo.getAppInfos());
					File file = new File(tempFolderPath + File.separator + applicationInfo.getCode() + File.separator + "pom.xml");
					addDependency(depApp, file,	projectInfo.getVersion());
				}
			}
			if(CollectionUtils.isNotEmpty(applicationInfo.getModules())) {
				createModuleDependencies(applicationInfo.getModules(), tempFolderPath, projectInfo, applicationInfo.getCode());
			}
		}
	}
	
	private void createModuleDependencies(List<ModuleInfo> modules, String tempFolderPath, 
			ProjectInfo projectInfo, String appCode) throws PhrescoException {
		for (ModuleInfo moduleInfo : modules) {
			List<String> dependentApps = moduleInfo.getDependentApps();
			List<String> dependentModules = moduleInfo.getDependentModules();
			if(CollectionUtils.isNotEmpty(dependentApps)) {
				for (String depAppCode : dependentApps) {
					ApplicationInfo applicationInfo = getApplicationInfo(depAppCode, projectInfo.getAppInfos());
					if(applicationInfo != null) {
						File file = new File(tempFolderPath + File.separator + appCode + 
							File.separator + moduleInfo.getCode() + File.separator + "pom.xml");
						addDependency(applicationInfo, file, projectInfo.getVersion());
					}
				}
			}
			if(CollectionUtils.isNotEmpty(dependentModules)) {
				for (String module : dependentModules) {
					ModuleInfo depModule = getModuleInfo(modules, module);
					if(depModule != null) {
						File file = new File(tempFolderPath + File.separator + appCode + File.separator + 
								moduleInfo.getCode() + File.separator + "pom.xml");
						addModuleInfoDependency(depModule, file, projectInfo.getVersion());
					}
				}
			}
		}
	}
	
	private ModuleInfo getModuleInfo(List<ModuleInfo> moduleInfos, String moduleName) {
		for (ModuleInfo moduleInfo : moduleInfos) {
			if(moduleInfo.getCode().equals(moduleName)) {
				return moduleInfo;
			}
		}
		return null;
	}
	
	private void addModuleInfoDependency(ModuleInfo moduleInfo, File pomFile, String version) throws PhrescoException {
		try {
			PomProcessor processor = new PomProcessor(pomFile);
			processor.addDependency("com.photon.phresco", moduleInfo.getCode(), version);
			processor.save();
		} catch (PhrescoPomException e) {
			throw new PhrescoException(e);
		}
	}
	
	private void addDependency(ApplicationInfo applicationInfo, File pomFile, String version) throws PhrescoException {
		try {
			PomProcessor processor = new PomProcessor(pomFile);
			processor.addDependency("com.photon.phresco", applicationInfo.getCode(), version);
			processor.save();
		} catch (PhrescoPomException e) {
			throw new PhrescoException(e);
		}
	}
	
	private ApplicationInfo getApplicationInfo(String appCode, List<ApplicationInfo> appInfos) {
		for (ApplicationInfo applicationInfo : appInfos) {
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
		applicationInfo.setPomFile("pom.xml");
		clonedProjectInfo.setAppInfos(Collections.singletonList(applicationInfo));
		return clonedProjectInfo;
	}

	@ApiOperation(value = "Update Project ")
	@ApiErrors(value = {@ApiError(code=500, reason = "Unable To update")})
    @RequestMapping(value= ServiceConstants.REST_API_PROJECT_UPDATE, consumes = MediaType.APPLICATION_JSON_VALUE, 
    		produces = ServiceConstants.MEDIATYPE_ZIP, method = RequestMethod.POST)
	public @ResponseBody byte[] updateProject(HttpServletRequest request, 
			@ApiParam(value = "ProjectInfo to update",	name = "projectInfo")@RequestBody ProjectInfo projectInfo) throws PhrescoException, IOException {
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
    	FileInputStream fis = null;
    	try {
    		buildUpdateLogMessage(request, projectInfo);
    		String projectPathStr = ServerUtil.getTempFolderPath();
    		ProjectServiceManager projectService = PhrescoServerFactory.getProjectService();
    		projectService.updateProject(projectInfo, projectPathStr);
    		ArchiveUtil.createArchive(projectPathStr, projectPathStr + ZIP, ArchiveType.ZIP);
    		fis = new FileInputStream(new File(projectPathStr + ZIP));
    		return IOUtils.toByteArray(fis);
    	} catch (Exception e) {
    		LOGGER.error("ProjectService.updateProject", "remoteAddress=" + request.getRemoteAddr(),
					"user=" + request.getParameter("userId"), "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			throw new PhrescoException(e);
		} finally {
			Utility.closeStream(fis);
		}
	}
	
/*	class ServiceOutput implements StreamingOutput {
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
	}*/
	
}