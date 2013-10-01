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
package com.photon.phresco.service.admin.actions.components;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.Technology;
import com.photon.phresco.commons.model.TechnologyInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.service.client.api.Content;
import com.photon.phresco.service.client.api.ServiceManager;

public class PilotProjects extends ServiceBaseAction { 
	
	private static final long serialVersionUID = 6801037145464060759L;
	
	private static final SplunkLogger S_LOGGER = SplunkLogger.getSplunkLogger(PilotProjects.class.getName());
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
		
	private static Map<String, InputStream> inputStreamMap = new HashMap<String, InputStream>();
	
	private String name = "";
	private String description = "";
    private String groupId = "";
    private String artifactId = "";
    private String jarVersion = "";
    private String pilotContentId = "";
    private String pilotContentArtiId = "";
    private String pilotContentGroId = "";
    private String pilotContentVersion = "";
    
	private String nameError = "";
	private String artifactIdError = "";
    private String groupIdError = "";
	private String verError = "";
	private String technologyError = "";
	private String jarVerError = "";

	private String fileError = "";
	private boolean errorFound = false;
	private boolean system = false;
	private String projectId = "";
	private String fromPage = "";
	
	private String customerId = "";
	
	private String techId = "";
	private String oldName = "";
	private String versioning = "";
	private String pilotURL = "";
	private String extFileName = "";
	private InputStream fileInputStream;
	private String contentType = "";
	private int contentLength;
	private static byte[] pilotProByteArray = null;
	private boolean tempError = false;
	private static String versionFile=""; 
	
	 /**
     * To get all Pilot Projects form DB
     * @return List of Pilot Projects
     */
	public String list() {
		if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.list : Entry");
	    }

		try {
			if (isDebugEnabled) {
				if (StringUtils.isEmpty(getCustomerId())) {
					S_LOGGER.warn("PilotProjects.list", "status=\"Bad Request\"", "message=\"Customer Id is empty\"");
					return showErrorPopup(new PhrescoException("Customer Id is empty"), getText(EXCEPTION_PILOT_PROJECTS_LIST));
				}
				S_LOGGER.info("Features.setTechnologiesInRequest", "customerId=" + "\"" + getCustomerId() + "\"");
			}
			List<ApplicationInfo> pilotProjects = getServiceManager().getPilotProjects(getCustomerId());
			List<Technology> technologies = getServiceManager().getArcheTypes(getCustomerId());
			if (CollectionUtils.isNotEmpty(pilotProjects)) {
				Collections.sort(pilotProjects, sortPilotProjectInAlphaOrder());
			}
			setReqAttribute(REQ_PILOT_PROJECTS, pilotProjects);
			setReqAttribute(REQ_ARCHE_TYPES, technologies);
			setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
		} catch (PhrescoException e) {
			if (isDebugEnabled) {
		        S_LOGGER.error("PilotProjects.list", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
		    }
			return showErrorPopup(e, getText(EXCEPTION_PILOT_PROJECTS_LIST));
		}
		
		//to clear file input stream and byte array
		inputStreamMap.clear();
		pilotProByteArray = null;
		if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.list : Exit");
	    }
		
		return COMP_PILOTPROJ_LIST;
	}
	
	private Comparator sortPilotProjectInAlphaOrder() {
		return new Comparator() {
		    public int compare(Object firstObject, Object secondObject) {
		    	ApplicationInfo pilotProject1 = (ApplicationInfo) firstObject;
		    	ApplicationInfo pilotProject2 = (ApplicationInfo) secondObject;
		       return pilotProject1.getName().compareToIgnoreCase(pilotProject2.getName());
		    }
		};
	}
	
	 /**
     * To return to the page to add Pilot projects
     * @return 
     */
    public String add() {
    	if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.add : Entry");
	    }
    	
    	try {
    		if (isDebugEnabled) {
				if (StringUtils.isEmpty(getCustomerId())) {
					S_LOGGER.warn("PilotProjects.add", "status=\"Bad Request\"", "message=\"Customer Id is empty\"");
					return showErrorPopup(new PhrescoException("Customer Id is empty"), getText(EXCEPTION_PILOT_PROJECTS_ADD));
				}
				S_LOGGER.info("PilotProjects.add", "customerId=" + "\"" + getCustomerId() + "\"");
			}
    		List<Technology> technologies = getServiceManager().getArcheTypes(getCustomerId());
    		if (CollectionUtils.isNotEmpty(technologies)) {
    			Collections.sort(technologies, TECHNAME_COMPARATOR);
    		}
    		setReqAttribute(REQ_ARCHE_TYPES, technologies);
    		setReqAttribute(REQ_FROM_PAGE, ADD);
    	} catch (PhrescoException e) {
    		if (isDebugEnabled) {
		        S_LOGGER.error("PilotProjects.add", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
		    }
    		return showErrorPopup(e, getText(EXCEPTION_PILOT_PROJECTS_ADD));
		}
    	if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.add : Exit");
	    }
    	
    	return COMP_PILOTPROJ_ADD;
    }
	
    /**
	 * To return the edit page with the details of the selected Pilot Projects
	 * @return
	 */
    public String edit() {
    	if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.edit : Entry");
	    }
    	try {
    		if (isDebugEnabled) {
				if (StringUtils.isEmpty(getCustomerId())) {
					S_LOGGER.warn("PilotProjects.edit", "status=\"Bad Request\"", "message=\"Customer Id is empty\"");
					return showErrorPopup(new PhrescoException("Customer Id is empty"), getText(EXCEPTION_PILOT_PROJECTS_EDIT));
				}
				if (StringUtils.isEmpty(getProjectId())) {
					S_LOGGER.warn("PilotProjects.edit", "status=\"Bad Request\"", "message=\"Pilot project Id is empty\"");
					return showErrorPopup(new PhrescoException("Pilot project Id is empty"), getText(EXCEPTION_PILOT_PROJECTS_EDIT));
				}
				S_LOGGER.info("PilotProjects.edit", "customerId=" + "\"" + getCustomerId() + "\"", "pilotProjectId=" + "\"" + getProjectId() + "\"");
			}
    		versionFile = getVersioning();
    		ServiceManager serviceManager = getServiceManager();
			ApplicationInfo applicationInfo = serviceManager.getPilotProject(getProjectId(), getCustomerId());
    		setReqAttribute(REQ_PILOT_PROINFO, applicationInfo);
    		List<Technology> technologies = serviceManager.getArcheTypes(getCustomerId());
    		setReqAttribute(REQ_ARCHE_TYPES, technologies);
    		setReqAttribute(REQ_FROM_PAGE, EDIT);
    		setReqAttribute(REQ_VERSIONING, getVersioning());
    	} catch (PhrescoException e) {
    		if (isDebugEnabled) {
		        S_LOGGER.error("PilotProjects.edit", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
		    }
    		return showErrorPopup(e, getText(EXCEPTION_PILOT_PROJECTS_EDIT));
		}
    	if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.edit : Exit");
	    }

    	return COMP_PILOTPROJ_ADD;
    }
    
    /**
     * To create a pilot projects with the provided details
     * @return
     */
    public String save() {
    	if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.save : Entry");
	    }
    	
    	try {
    		if (isDebugEnabled) {
				if (StringUtils.isEmpty(getCustomerId())) {
					S_LOGGER.warn("PilotProjects.save", "status=\"Bad Request\"", "message=\"Customer Id is empty\"");
					return showErrorPopup(new PhrescoException("Customer Id is empty"), getText(EXCEPTION_PILOT_PROJECTS_SAVE));
				}
				S_LOGGER.info("PilotProjects.save", "customerId=" + "\"" + getCustomerId() + "\"");
			}
            ApplicationInfo pilotProInfo = createPilotProj();     		
    		//save pilot project jar files
			if(pilotProByteArray != null){
				inputStreamMap.put(pilotProInfo.getName(),  new ByteArrayInputStream(pilotProByteArray));
			} 
    		
    		getServiceManager().createPilotProjects(pilotProInfo, inputStreamMap, getCustomerId());
			addActionMessage(getText(PLTPROJ_ADDED, Collections.singletonList(getName())));
    	} catch (PhrescoException e) {
    		if (isDebugEnabled) {
		        S_LOGGER.error("PilotProjects.save", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
		    }
    		return showErrorPopup(e, getText(EXCEPTION_PILOT_PROJECTS_SAVE));
		}
    	if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.save : Entry");
	    }

    	return list();
    }
    
    /**
	 * To update the pilot projects with the provided details
	 * @return List of pilot projects
	 */
    public String update() {
    	if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.update : Entry");
	    }
    	
    	try {
    		if (isDebugEnabled) {
				if (StringUtils.isEmpty(getCustomerId())) {
					S_LOGGER.warn("PilotProjects.update", "status=\"Bad Request\"", "message=\"Customer Id is empty\"");
					return showErrorPopup(new PhrescoException("Customer Id is empty"), getText(EXCEPTION_PILOT_PROJECTS_UPDATE));
				}
				if (StringUtils.isEmpty(getProjectId())) {
					S_LOGGER.warn("PilotProjects.update", "status=\"Bad Request\"", "message=\"Pilot project Id is empty\"");
					return showErrorPopup(new PhrescoException("Pilot project Id is empty"), getText(EXCEPTION_PILOT_PROJECTS_UPDATE));
				}
				S_LOGGER.info("PilotProjects.update", "customerId=" + "\"" + getCustomerId() + "\"", "pilotProjectId=" + "\"" + getProjectId() + "\"");
			}
    		ApplicationInfo pilotProInfo = createPilotProj();
    		//update pilot project jar files
    		if (pilotProByteArray != null) {
    			inputStreamMap.put(pilotProInfo.getName(),  new ByteArrayInputStream(pilotProByteArray));
    		} 
    		getServiceManager().createPilotProjects(pilotProInfo, inputStreamMap, getCustomerId());
    		addActionMessage(getText(PLTPROJ_UPDATED, Collections.singletonList(getName())));
    	} catch (PhrescoException e) {
    		if (isDebugEnabled) {
		        S_LOGGER.error("PilotProjects.update", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
		    }
    		return showErrorPopup(e, getText(EXCEPTION_PILOT_PROJECTS_UPDATE));
		}
    	if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.update : Exit");
	    }
    	
    	return list();
    }
    
    private ApplicationInfo createPilotProj() throws PhrescoException {
        ApplicationInfo pilotProInfo = new ApplicationInfo();
        pilotProInfo.setPilot(true);
        if (StringUtils.isNotEmpty(getProjectId())) { 
        	pilotProInfo.setId(getProjectId());
        }
        pilotProInfo.setSystem(isSystem());
        List<String> customerIds = new ArrayList<String>();
        customerIds.add(getCustomerId());
        pilotProInfo.setName(getName());
        pilotProInfo.setDescription(getDescription());
       
        ArtifactGroup pilotContent = new ArtifactGroup();
        pilotContent.setName(getName());
        if(StringUtils.isNotEmpty(getPilotContentId())) {
        	pilotContent.setId(getPilotContentId());
        }
        if(StringUtils.isEmpty(getGroupId()) && StringUtils.isEmpty(getArtifactId())) {
        	pilotContent.setGroupId(getPilotContentGroId());
        	pilotContent.setArtifactId(getPilotContentArtiId());
        } else {
           pilotContent.setGroupId(getGroupId());
           pilotContent.setArtifactId(getArtifactId());
        }
        
        pilotContent.setPackaging(Content.Type.ZIP.name());
        List<ArtifactInfo> jarVersions = new ArrayList<ArtifactInfo>();
        ArtifactInfo jarversion = new ArtifactInfo();
        jarversion.setName(getName());
        if(StringUtils.isEmpty(getJarVersion())) {
        	jarversion.setVersion(getPilotContentVersion());
        } else {
        	jarversion.setVersion(getJarVersion());
        }
        
        jarVersions.add(jarversion);
        pilotContent.setVersions(jarVersions);
        pilotContent.setCustomerIds(customerIds);
       
        pilotProInfo.setCustomerIds(customerIds);
        pilotProInfo.setPilotContent(pilotContent);
        
        TechnologyInfo techInfo = new TechnologyInfo();
        techInfo.setName(getTechId());
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
    	if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.delete : Entry");
	    }
    	
    	try {
    		if (isDebugEnabled) {
				if (StringUtils.isEmpty(getCustomerId())) {
					S_LOGGER.warn("PilotProjects.delete", "status=\"Bad Request\"", "message=\"Customer Id is empty\"");
					return showErrorPopup(new PhrescoException("Customer Id is empty"), getText(EXCEPTION_PILOT_PROJECTS_DELETE));
				}
				S_LOGGER.info("PilotProjects.delete", "customerId=" + "\"" + getCustomerId() + "\"");
			}
    		String[] projectIds = getReqParameterValues(REQ_PILOT_PROJ_ID);
    		if (isDebugEnabled) {
    			if (ArrayUtils.isEmpty(projectIds)) {
					S_LOGGER.warn("PilotProjects.delete", "status=\"Bad Request\"", "message=\"No pilot project to delete\"");
					return showErrorPopup(new PhrescoException("No pilot project to delete"), getText(EXCEPTION_PILOT_PROJECTS_DELETE));
				}
    		}
    		if (ArrayUtils.isNotEmpty(projectIds)) {
    			for (String projectid : projectIds) {
    				getServiceManager().deletePilotProject(projectid, getCustomerId());
    			}
    			addActionMessage(getText(PLTPROJ_DELETED));
    		}
    	} catch (PhrescoException e) {
    		if (isDebugEnabled) {
		        S_LOGGER.error("PilotProjects.delete", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
		    }
    		return showErrorPopup(e, getText(EXCEPTION_PILOT_PROJECTS_DELETE));
		}
    	if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.delete : Exit");
	    }
    	
    	return list();
    }
    
    /**
	 * To upload file
	 * @return
	 */
    public String uploadFile() {
    	if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.uploadFile : Entry");
	    }

    	PrintWriter writer = null;
    	try {
    		writer = getHttpResponse().getWriter();
    		InputStream is = getHttpRequest().getInputStream();
    		pilotProByteArray = IOUtils.toByteArray(is);
    		getHttpResponse().setStatus(getHttpResponse().SC_OK);
    		writer.print(SUCCESS_TRUE);
    		writer.flush();
    		writer.close();
    	} catch (Exception e) {
    		getHttpResponse().setStatus(getHttpResponse().SC_INTERNAL_SERVER_ERROR);
    		writer.print(SUCCESS_FALSE);
    		if (isDebugEnabled) {
		        S_LOGGER.error("PilotProjects.uploadFile", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
		    }
    		return showErrorPopup(new PhrescoException(e), getText(EXCEPTION_UPLOAD_FILE));
    	}
    	if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.uploadFile : Exit");
	    }

    	return SUCCESS;
    }
    
    public String downloadPilotProjects() {
    	if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.downloadPilotProjects : Entry");
	    }

    	try {
    		if (isDebugEnabled) {
				if (StringUtils.isEmpty(getCustomerId())) {
					S_LOGGER.warn("PilotProjects.downloadPilotProjects", "status=\"Bad Request\"", "message=\"Customer Id is empty\"");
					return showErrorPopup(new PhrescoException("Customer Id is empty"), getText(DOWNLOAD_FAILED));
				}
				if (StringUtils.isEmpty(getProjectId())) {
					S_LOGGER.warn("PilotProjects.downloadPilotProjects", "status=\"Bad Request\"", "message=\"Pilot project Id is empty\"");
					return showErrorPopup(new PhrescoException("Pilot project Id is empty"), getText(DOWNLOAD_FAILED));
				}
				S_LOGGER.info("PilotProjects.downloadPilotProjects", "customerId=" + "\"" + getCustomerId() + "\"", "pilotProjectId=" + "\"" + getProjectId() + "\"");
			}
    		ApplicationInfo pilotProjInfo = getServiceManager().getPilotProject(getProjectId(), getCustomerId());
    		pilotURL = pilotProjInfo.getPilotContent().getVersions().get(0).getDownloadURL();

    		URL url = new URL(pilotURL);
    		fileInputStream = url.openStream();
    		String[] parts = pilotURL.split(FORWARD_SLASH);
    		extFileName = parts[parts.length - 1];
    		contentType = url.openConnection().getContentType();
    		contentLength = url.openConnection().getContentLength();
    	} catch(Exception e) {
    		if (isDebugEnabled) {
		        S_LOGGER.error("PilotProjects.downloadPilotProjects", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
		    }
    		return showErrorPopup(new PhrescoException(e), getText(DOWNLOAD_FAILED));
    	}
    	if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.downloadPilotProjects : Exit");
	    }
		
		return SUCCESS;
	}
	
    /**
	 * To remove uploaded file
	 * @return
	 * @throws PhrescoException
	 */
	public void removeUploadedFile() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  PilotProjects.removeUploadedFile()");
		}
		
		pilotProByteArray = null;
	}
	
    public String validateForm() throws PhrescoException {
    	if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.validateForm : Entry");
	    }
    	boolean isError = false;
    	//Empty validation for name
    	isError = nameValidation(isError);
    	  
    	//empty validation for fileupload
    	isError = fileuploadValidation(isError);
    	
    	//Empty Validation for pilot Project
    	isError = pilotProjectValidation(isError);
    	
    	if (StringUtils.isEmpty(getTechId())) {
    		setTechnologyError(getText(KEY_I18N_SINGLE_TECH_EMPTY));
    		isError = true;
    	}

    	if (isError) {
    		setErrorFound(true);
    	}
    	if (isDebugEnabled) {
	        S_LOGGER.debug("PilotProjects.validateForm : Exit");
	    }
    	
    	return SUCCESS;
    }

	public boolean pilotProjectValidation(boolean isError) {
		if (pilotProByteArray != null) {
    		//Empty validation for groupId if file is selected
    		if (StringUtils.isEmpty(getGroupId())) {
    			setGroupIdError(getText(KEY_I18N_ERR_GROUPID_EMPTY));
    			tempError = true;
    		}

    		//Empty validation for artifactId if file is selected
    		if (StringUtils.isEmpty(getArtifactId())) {
    			setArtifactIdError(getText(KEY_I18N_ERR_ARTIFACTID_EMPTY));
    			tempError = true;
    		}

    		//Empty validation for version if file is selected
    		if (StringUtils.isEmpty(getJarVersion())) {
    			setJarVerError(getText(KEY_I18N_ERR_VER_EMPTY));
    			tempError = true;
    		}
    	}
		return tempError;
	}

	public boolean fileuploadValidation(boolean isError) {
		if ((!EDIT.equals(getFromPage()) && pilotProByteArray == null) || (StringUtils.isNotEmpty(versionFile) && pilotProByteArray == null)) {
    		setFileError(getText(KEY_I18N_ERR_PLTPROJ_EMPTY));
    		tempError = true;
    	}
		return tempError;
	}

	public boolean nameValidation(boolean isError) throws PhrescoException {
		if (StringUtils.isEmpty(getName())) {
    		setNameError(getText(KEY_I18N_ERR_NAME_EMPTY ));
    		tempError = true;
    	} else if (ADD.equals(getFromPage()) || (!getName().equals(getOldName()))) {
    		//To check whether the name already exist (Technology wise)
			List<ApplicationInfo> pilotProjInfos = getServiceManager().getPilotProjects(getCustomerId());
			if (pilotProjInfos != null) {
				for (ApplicationInfo pilotProjectInfo : pilotProjInfos) {
					if (pilotProjectInfo.getTechInfo().getVersion().equals(getTechId()) && pilotProjectInfo.getName().equalsIgnoreCase(getName())) {
						setNameError(getText(KEY_I18N_ERR_NAME_ALREADY_EXIST_TECH));
						tempError = true;
			    		break;
					} 
				}	
			}
    	}
		return tempError;
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
	
	public boolean isSystem() {
		return system;
	}

	public void setSystem(boolean system) {
		this.system = system;
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

	public String getVerError() {
		return verError;
	}

	public void setVerError(String verError) {
		this.verError = verError;
	}

	public String getArtifactIdError() {
		return artifactIdError;
	}

	public void setArtifactIdError(String artifactIdError) {
		this.artifactIdError = artifactIdError;
	}

	public String getGroupIdError() {
		return groupIdError;
	}

	public void setGroupIdError(String groupIdError) {
		this.groupIdError = groupIdError;
	}
	
	public String getJarVerError() {
		return jarVerError;
	}

	public void setJarVerError(String jarVerError) {
		this.jarVerError = jarVerError;
	}
    
    public String getVersioning() {
		return versioning;
	}
    
    public void setVersioning(String versioning) {
		this.versioning = versioning;
	} 
    
	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public String getPilotContentId() {
		return pilotContentId;
	}

	public void setPilotContentId(String pilotContentId) {
		this.pilotContentId = pilotContentId;
	}

	public String getPilotContentArtiId() {
		return pilotContentArtiId;
	}

	public void setPilotContentArtiId(String pilotContentArtiId) {
		this.pilotContentArtiId = pilotContentArtiId;
	}

	public String getPilotContentGroId() {
		return pilotContentGroId;
	}

	public void setPilotContentGroId(String pilotContentGroId) {
		this.pilotContentGroId = pilotContentGroId;
	}

	public String getPilotContentVersion() {
		return pilotContentVersion;
	}

	public void setPilotContentVersion(String pilotContentVersion) {
		this.pilotContentVersion = pilotContentVersion;
	}

	public String getExtFileName() {
		return extFileName;
	}

	public void setExtFileName(String extFileName) {
		this.extFileName = extFileName;
	}

	public void setTechnologyError(String technologyError) {
		this.technologyError = technologyError;
	}

	public String getTechnologyError() {
		return technologyError;
	}


}