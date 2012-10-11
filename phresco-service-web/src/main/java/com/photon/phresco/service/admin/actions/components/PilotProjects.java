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
package com.photon.phresco.service.admin.actions.components;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.Technology;
import com.photon.phresco.commons.model.TechnologyInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.service.client.api.Content;
import com.photon.phresco.service.client.api.ServiceManager;

public class PilotProjects extends ServiceBaseAction { 
	
	private static final long serialVersionUID = 6801037145464060759L;
	
	private static final Logger S_LOGGER = Logger.getLogger(PilotProjects.class);
	private static Boolean s_isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	private String name = "";
	private String description = "";
    private String version = "";
    private String groupId = "";
    private String artifactId = "";
    private String jarVersion = "";
    
	private String nameError = "";
	private String verError = "";
	private String fileError = "";
	private boolean errorFound = false;
	
	private String projectId = "";
	private String fromPage = "";
	
	private String customerId = "";
	
	private String techId = "";
	private String oldName = "";
	
	private static byte[] s_pilotProByteArray = null;
	
	 /**
     * To get all Pilot Projects form DB
     * @return List of Pilot Projects
     * @throws PhrescoException
     */
	public String list() throws PhrescoException {
        if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method PilotProjects.list()");
        }

		try {
			List<ApplicationInfo> pilotProjects = getServiceManager().getPilotProjects(getCustomerId());
			setReqAttribute(REQ_PILOT_PROJECTS, pilotProjects);
			setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
		} catch (PhrescoException e) {
			return showErrorPopup(e, getText(EXCEPTION_PILOT_PROJECTS_LIST));
		}
		
		//to clear file input stream and byte array
		s_pilotProByteArray = null;
		
		return COMP_PILOTPROJ_LIST;
	}
	
	 /**
     * To return to the page to add Pilot projects
     * @return 
     * @throws PhrescoException
     */
    public String add() throws PhrescoException {
    	if (s_isDebugEnabled) {	
    		S_LOGGER.debug("Entering Method PilotProjects.add()");
    	}
    	
    	try {
    		List<Technology> technologies = getServiceManager().getArcheTypes(getCustomerId());
    		setReqAttribute(REQ_ARCHE_TYPES, technologies);
    		setReqAttribute(REQ_FROM_PAGE, ADD);
    	} catch (PhrescoException e) {
    		return showErrorPopup(e, getText(EXCEPTION_PILOT_PROJECTS_ADD));
		}
    	
    	return COMP_PILOTPROJ_ADD;
    }
	
    /**
	 * To return the edit page with the details of the selected Pilot Projects
	 * @return
	 * @throws PhrescoException
	 */
    public String edit() throws PhrescoException {
    	if (s_isDebugEnabled) {
    		S_LOGGER.debug("Entering Method PilotProjects.edit()");
    	}
    	try {
    		ServiceManager serviceManager = getServiceManager();
			ApplicationInfo applicationInfo = serviceManager.getPilotProject(getProjectId(), getCustomerId());
    		setReqAttribute(REQ_PILOT_PROINFO, applicationInfo);
    		List<Technology> technologies = serviceManager.getArcheTypes(getCustomerId());
    		setReqAttribute(REQ_ARCHE_TYPES, technologies);
    		setReqAttribute(REQ_FROM_PAGE, EDIT);
    	} catch (PhrescoException e) {
    		return showErrorPopup(e, getText(EXCEPTION_PILOT_PROJECTS_EDIT));
		}

    	return COMP_PILOTPROJ_ADD;
    }
    
    /**
	 * To create a pilot projects with the provided details
	 * @return List of pilot projects
	 * @throws PhrescoException
	 */
    public String save() throws PhrescoException {
    	if (s_isDebugEnabled) {
    		S_LOGGER.debug("Entering Method PilotProjects.save()");
    	}
    	
    	try {
    		List<InputStream> inputStreams = new ArrayList<InputStream>();
    		inputStreams.add(new ByteArrayInputStream(s_pilotProByteArray));
    		getServiceManager().createPilotProjects(createPilotProj(), inputStreams, getCustomerId());
			addActionMessage(getText(PLTPROJ_ADDED, Collections.singletonList(getName())));
    	} catch (PhrescoException e) {
    		return showErrorPopup(e, getText(EXCEPTION_PILOT_PROJECTS_SAVE));
		}

    	return list();
    }
    
    /**
	 * To update the pilot projects with the provided details
	 * @return List of pilot projects
	 * @throws PhrescoException
	 */
    public String update() throws PhrescoException {
    	if (s_isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  PilotProjects.update()");
    	}
    	try {
    		List<InputStream> inputStreams = new ArrayList<InputStream>();
    		inputStreams.add(new ByteArrayInputStream(s_pilotProByteArray));
    		getServiceManager().updatePilotProject(createPilotProj(), inputStreams, getProjectId(), getCustomerId());
    		addActionMessage(getText(PLTPROJ_UPDATED, Collections.singletonList(getName())));
    	} catch (PhrescoException e) {
    		return showErrorPopup(e, getText(EXCEPTION_PILOT_PROJECTS_UPDATE));
		}

    	return list();
    }
    
    private ApplicationInfo createPilotProj() throws PhrescoException {
        ApplicationInfo pilotProInfo = new ApplicationInfo();
        if (StringUtils.isNotEmpty(getProjectId())) { 
        	pilotProInfo.setId(getProjectId());
        }
        pilotProInfo.setName(getName());
        pilotProInfo.setDescription(getDescription());
//        pilotProInfo.setVersion(getVersion());
        
        ArtifactGroup pilotContent = new ArtifactGroup();
        pilotContent.setGroupId(getGroupId());
        pilotContent.setArtifactId(getArtifactId());
        pilotContent.setPackaging(Content.Type.ZIP.name());
        List<String> customerIds = new ArrayList<String>();
        customerIds.add(getCustomerId());
        pilotProInfo.setCustomerIds(customerIds);
        
        List<ArtifactInfo> jarVersions = new ArrayList<ArtifactInfo>();
        ArtifactInfo jarVersion = new ArtifactInfo();
        jarVersions.add(jarVersion);
        pilotContent.setVersions(jarVersions);
        
        pilotProInfo.setPilotContent(pilotContent);
        
        TechnologyInfo techInfo = new TechnologyInfo();
        techInfo.setVersion(getTechId());
        pilotProInfo.setTechInfo(techInfo);
        
        return pilotProInfo;
    }
	
    /**
	 * To delete selected pilot projects 
	 * @return List of pilot projects
	 * @throws PhrescoException
	 */
    public String delete() throws PhrescoException {
    	if (s_isDebugEnabled) {
    		S_LOGGER.debug("Entering Method PilotProjects.delete()");
    	}
    	
    	try {
    		String[] projectIds = getHttpRequest().getParameterValues(REQ_PILOT_PROJ_ID);
    		if (ArrayUtils.isNotEmpty(projectIds)) {
    			for (String projectId : projectIds) {
    				getServiceManager().deletePilotProject(projectId, getCustomerId());
    			}
    			addActionMessage(getText(PLTPROJ_DELETED));
    		}
    	}catch (PhrescoException e) {
    		return showErrorPopup(e, getText(EXCEPTION_PILOT_PROJECTS_DELETE));
		}

    	return list();
    }
    
    /**
	 * To upload file
	 * @return
	 * @throws PhrescoException
	 */
    public String uploadFile() throws PhrescoException {
    	if (s_isDebugEnabled) {
    	S_LOGGER.debug("Entering Method PilotProjects.uploadFile()");
    	}

    	PrintWriter writer = null;
    	try {
    		writer = getHttpResponse().getWriter();
    		InputStream is = getHttpRequest().getInputStream();
    		s_pilotProByteArray = IOUtils.toByteArray(is);
    		getHttpResponse().setStatus(getHttpResponse().SC_OK);
    		writer.print(SUCCESS_TRUE);
    		writer.flush();
    		writer.close();
    	} catch (Exception e) {
    		getHttpResponse().setStatus(getHttpResponse().SC_INTERNAL_SERVER_ERROR);
    		writer.print(SUCCESS_FALSE);
    		throw new PhrescoException(e);
    	}

    	return SUCCESS;
    }
	
    /**
	 * To remove uploaded file
	 * @return
	 * @throws PhrescoException
	 */
	public void removeUploadedFile() {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  PilotProjects.removeUploadedFile()");
		}
		
		s_pilotProByteArray = null;
	}
	
    public String validateForm() throws PhrescoException {
    	if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  PilotProjects.validateForm()");
		}
    	boolean isError = false;
    	//Empty validation for name
    	if (StringUtils.isEmpty(getName())) {
    		setNameError(getText(KEY_I18N_ERR_NAME_EMPTY ));
    		isError = true;
    	} else if (ADD.equals(getFromPage()) || (!getName().equals(getOldName()))) {
    		//To check whether the name already exist (Technology wise)
			List<ApplicationInfo> pilotProjInfos = getServiceManager().getPilotProjects(getCustomerId());
			if (pilotProjInfos != null) {
				for (ApplicationInfo pilotProjectInfo : pilotProjInfos) {
					if (pilotProjectInfo.getTechInfo().getVersion().equals(getTechId()) && pilotProjectInfo.getName().equalsIgnoreCase(getName())) {
						setNameError(getText(KEY_I18N_ERR_NAME_ALREADY_EXIST_TECH));
			    		isError = true;
			    		break;
					} 
				}	
			}
    	}
    	//empty validation for version
    	if (StringUtils.isEmpty(getVersion())) {
    		setVerError(getText(KEY_I18N_ERR_VER_EMPTY ));
    		isError = true;
    	}
    	//empty validation for fileupload
    	if (s_pilotProByteArray == null) {
    		setFileError(getText(KEY_I18N_ERR_PLTPROJ_EMPTY));
    		isError = true;
    	}
    	
    	if (isError) {
    		setErrorFound(true);
    	}
		
    	return SUCCESS;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameError() {
		return nameError;
	}

	public void setNameError(String nameError) {
		this.nameError = nameError;
	}
	
	public String getFileError() {
		return fileError;
	}

	public void setFileError(String fileError) {
		this.fileError = fileError;
	}

	public boolean isErrorFound() {
		return errorFound;
	}

	public void setErrorFound(boolean errorFound) {
		this.errorFound = errorFound;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getTechId() {
		return techId;
	}

	public void setTechId(String techId) {
		this.techId = techId;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getOldName() {
		return oldName;
	}
	
	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public String getJarVersion() {
		return jarVersion;
	}

	public void setJarVersion(String jarVersion) {
		this.jarVersion = jarVersion;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVerError() {
		return verError;
	}

	public void setVerError(String verError) {
		this.verError = verError;
	}
}