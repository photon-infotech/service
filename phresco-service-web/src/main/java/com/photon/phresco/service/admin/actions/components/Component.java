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

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.CoreOption;
import com.photon.phresco.commons.model.RequiredOption;
import com.photon.phresco.commons.model.Technology;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.service.client.api.Content;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;

public class Component extends ServiceBaseAction {

	private static final long serialVersionUID = 6801037145464060759L;
	
	private static final Logger S_LOGGER = Logger.getLogger(Features.class);
	private static Boolean s_isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	private String name = "";
	private String nameError = "";
	private String versError = "";
	private String fileError = "";
	private boolean errorFound = false;
	
    private String customerId = "";
    private String fromPage = "";
    private String type = "component";
    
    private String artifactId = "";
    private String groupId = "";
    
	private static byte[] componentByteArray = null;
	private static String componentJarName = "";
    
	private String description = "";
	private String helpText = "";
	private String versions = "";
	private String technology = "";
	
	private String version = "";
   
	private String verError = "";
	private String oldVersion = "";
	private String moduleType = "";
	private String defaultType = "";
	
	private String from = "";
	private String appJarError = "";
	
    public String list() {
    	if (s_isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  Component.list()");
    	}
    	
    	try {
      		setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
    		List<Technology> technologies = getServiceManager().getArcheTypes(getCustomerId());
    		setReqAttribute(REQ_ARCHE_TYPES, technologies);
    	} catch (PhrescoException e){
    	    return showErrorPopup(e, EXCEPTION_COMPONENT_LIST);
    	}
    	
    	return COMP_COMPONENT_LIST;
    }
    
    public String componentWithList() {
    	if (s_isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  Component.componentlist()");
    	}
    	
    	try {    		
    		List<ArtifactGroup> moduleGroup = getServiceManager().getModules(getCustomerId(), getTechnology(), getType());
    		setReqAttribute(REQ_FEATURES_MOD_GRP, moduleGroup);
    		setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
    		if (StringUtils.isNotEmpty(getFrom())) {
    		    return COMP_FEATURES_DEPENDENCY;
    		}
    	} catch (PhrescoException e){
    	    return showErrorPopup(e, EXCEPTION_COMPONENT_LIST);
    	}
    	
    	return COMP_COMPONENT_LIST;
    }
	
	public String add() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Component.add()");
		}
		
		try {
		
		List<Technology> technologies = getServiceManager().getArcheTypes(getCustomerId());
		setReqAttribute(REQ_ARCHE_TYPES, technologies);
		setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
		}catch (PhrescoException e) {
		    return showErrorPopup(e, EXCEPTION_COMPONENT_ADD);
		}
	
		return COMP_COMPONENT_ADD;
	}
	
	public String edit() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Component.edit()");
		}
		
		try {
		    ArtifactGroup moduleGroup = getServiceManager().getFeature(getTechnology(), getCustomerId());
		    setReqAttribute(REQ_FEATURES_MOD_GRP, moduleGroup);
		    setReqAttribute(REQ_FROM_PAGE, EDIT);
		    setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
		} catch (PhrescoException e) {
		    return showErrorPopup(e, EXCEPTION_COMPONENT_EDIT);
		}

		return COMP_COMPONENT_ADD;
	}
	
	public String save() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Component.save()");
		}
		
		try {
			MultiPart multiPart = new MultiPart();
			BodyPart jsonPart = new BodyPart();
			jsonPart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			jsonPart.setEntity(createModuleGroup());
			Content content = new Content(Content.Type.JSON, getName(), null, null, null, 0);
			jsonPart.setContentDisposition(content);
			multiPart.bodyPart(jsonPart);
			if (StringUtils.isNotEmpty(componentJarName)) {
				InputStream featureIs = new ByteArrayInputStream(componentByteArray);
				BodyPart binaryPart = getServiceManager().createBodyPart(getName(), Content.Type.JAR, featureIs);
				multiPart.bodyPart(binaryPart);
			}
			
			ClientResponse clientResponse = getServiceManager().createFeatures(multiPart, getCustomerId());
			if (clientResponse.getStatus() != RES_CODE_200 && clientResponse.getStatus() != RES_CODE_201) {
				addActionError(getText(COMPONENT_NOT_ADDED, Collections.singletonList(getName())));
			} else {
				addActionMessage(getText(COMPONENT_ADDED, Collections.singletonList(getName())));
			}
		} catch (PhrescoException e) {
		    return showErrorPopup(e, EXCEPTION_COMPONENT_SAVE);    		
		} 

		return list();
	}
	
	private ArtifactGroup createModuleGroup() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Component.createModuleGroup()");
		}

		ArtifactGroup moduleGroup;
		CoreOption moduleCoreOption;
		RequiredOption requiredOption;
		List<CoreOption> appliesTo = new ArrayList<CoreOption>();
		List<RequiredOption> required = new ArrayList<RequiredOption>();
		try {
			moduleGroup = new ArtifactGroup();
			List<ArtifactInfo> modules = new ArrayList<ArtifactInfo>();
			ArtifactInfo module = new ArtifactInfo();
			moduleGroup.setName(getName());
			if (FEATURES_CORE.equals(getModuleType())) {
				moduleCoreOption= new CoreOption(getTechnology(), true);
			} else {
				moduleCoreOption= new CoreOption(getTechnology(), false);;
			}
			appliesTo.add(moduleCoreOption);
			moduleGroup.setAppliesTo(appliesTo);
            //TODO:change the data type : ARUN PRASANNA
//			moduleGroup.setType(type);
			List<String> customerIds = new ArrayList<String>();
			moduleGroup.setCustomerIds(customerIds);
			moduleGroup.setArtifactId(getArtifactId());
			moduleGroup.setGroupId(getGroupId());
			module.setName(getName());
			module.setDescription(getDescription());
			module.setHelpText(getHelpText());
			module.setVersion(getVersion());
			if (StringUtils.isNotEmpty(getDefaultType())) {
				requiredOption = new RequiredOption(getTechnology(), true);
			} else {
				requiredOption = new RequiredOption(getTechnology(), false);
			}
			required.add(requiredOption);
			module.setAppliesTo(required);
			modules.add(module);
			moduleGroup.setVersions(modules);
		} catch(Exception e) {
			throw new PhrescoException(e);
		}

		return moduleGroup;
	}
	
	public String update() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Component.update()");
		}
		
		try {
			MultiPart multiPart = new MultiPart();			
			BodyPart jsonPart = new BodyPart();
			jsonPart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			jsonPart.setEntity(createModuleGroup());
			Content content = new Content(Content.Type.JSON, getName(), null, null, null, 0);
			jsonPart.setContentDisposition(content);
			multiPart.bodyPart(jsonPart);
			    
			if (StringUtils.isNotEmpty(componentJarName)) {
				InputStream featureIs = new ByteArrayInputStream(componentByteArray);
				BodyPart binaryPart2 = getServiceManager().createBodyPart(getName(), Content.Type.JAR, featureIs);
				multiPart.bodyPart(binaryPart2);
			}
			
			getServiceManager().updateFeature(multiPart, getTechnology(), getCustomerId());
		} catch (PhrescoException e) {
		    return showErrorPopup(e, EXCEPTION_COMPONENT_UPDATE);
		}
		
		return list();	
	}
	
	public String delete() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Component.delete()");
		}
		
		try {
			String[] techIds = getHttpRequest().getParameterValues(REST_QUERY_TECHID);
			if (ArrayUtils.isNotEmpty(techIds)) {
				for (String techId : techIds) {
					ClientResponse clientResponse = getServiceManager().deleteFeature(techId, getCustomerId());
					if (clientResponse.getStatus() != RES_CODE_200) {
						addActionError(getText(COMPONENT_NOT_DELETED));
					}
				}
				addActionMessage(getText(COMPONENT_DELETED));
			}
		} catch (PhrescoException e) {
		    return showErrorPopup(e, EXCEPTION_COMPONENT_DELETE);
		}
		
		return list();
	}
	
	public String uploadFile() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Component.uploadFile()");
		}
		
		PrintWriter writer = null;
		try {
            writer = getHttpResponse().getWriter();
            componentJarName = getHttpRequest().getHeader(X_FILE_NAME);
	       
	        InputStream is = getHttpRequest().getInputStream();
	        componentByteArray = IOUtils.toByteArray(is);
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
	
	public void removeUploadedFile() {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Component.removeUploadedFile()");
		}
		
		componentByteArray = null;
		componentJarName = null;
	}

	public String validateForm() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Component.validateForm()");
		}
		
		boolean isError = false;
		//Empty validation for name
		if (StringUtils.isEmpty(getName())) {
			setNameError(getText(KEY_I18N_ERR_NAME_EMPTY));
			isError = true;
		} 
		
		//Empty validation for version
		if (StringUtils.isEmpty(getVersion())) {
			setVersError(getText(KEY_I18N_ERR_VER_EMPTY));
			isError = true;
		}
		
		//Validate whether file is selected
		if (componentByteArray == null) {
		    setAppJarError(getText(KEY_I18N_ERR_APPLNJAR_EMPTY));
            isError = true;
		}
		/*else if (StringUtils.isEmpty(fromPage) || (!version.equals(oldVersion))) {
			//To check whether the version already exist
			List<ModuleGroup> moduleGroups = getServiceManager().getFeatures(customerId);
			if (CollectionUtils.isNotEmpty(versions)) {
				for (ModuleGroup moduleGroup : moduleGroups) {
				    List<Module> versions = moduleGroup.getVersions();
				    if (CollectionUtils.isNotEmpty(versions)) {
				        for (Module module : versions) {
	                        if (module.getName().equalsIgnoreCase(name) && module.getVersion().equals(version)) {
	                            setVerError(getText(KEY_I18N_ERR_VER_ALREADY_EXISTS));
	                            isError = true;
	                            break;
	                        }
                        }
				    }
				}
			}
		}*/
		
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
	
	public String getVersError() {
		return versError;
	}

	public void setVersError(String versError) {
		this.versError = versError;
	}

	public String getFileError() {
		return fileError;
	}

	public void setFileError(String fileError) {
		this.fileError = fileError;
	}

	public boolean getErrorFound() {
		return errorFound;
	}

	public void setErrorFound(boolean errorFound) {
		this.errorFound = errorFound;
	}
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
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

	public String getOldVersion() {
		return oldVersion;
	}

	public void setOldVersion(String oldVersion) {
		this.oldVersion = oldVersion;
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
	
	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	
	public String getDefaultType() {
		return defaultType;
	}

	public void setDefaultType(String defaultType) {
		this.defaultType = defaultType;
	}

	public String getHelpText() {
		return helpText;
	}

	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}
	
	public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

	public String getAppJarError() {
		return appJarError;
	}

	public void setAppJarError(String appJarError) {
		this.appJarError = appJarError;
	}
	
	public String getVersions() {
		return versions;
	}

	public void setVersions(String versions) {
		this.versions = versions;
	}
}