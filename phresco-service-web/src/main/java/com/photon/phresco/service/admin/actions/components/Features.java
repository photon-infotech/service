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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.text.pdf.codec.Base64.OutputStream;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.ApplicationType;
import com.photon.phresco.model.Module;
import com.photon.phresco.model.ModuleGroup;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.sun.jersey.api.client.ClientResponse;

public class Features extends ServiceBaseAction {

	private static final long serialVersionUID = 6801037145464060759L;
	private static final Logger S_LOGGER = Logger.getLogger(Features.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	private String name = null;
	private String version = null;
	private String nameError = null;
	private String versError = null;
	private String fileError = null;
	private boolean errorFound = false;
	
	private File featureArc;
	private String featureArcFileName;
	private String featureArcContentType;
    private String customerId = null;
    private String fromPage = null;
    private String techId = null;
    private String type = null;
    
	private String description = null;
    private List<Module> versions = null;
	
    public String list() throws PhrescoException {
    	if (isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  Features.list()");
    	}
    	
    	try {
    		List<ModuleGroup> moduleGroup = getServiceManager().getModules(customerId);
    		getHttpRequest().setAttribute(REQ_MODULE_GROUP, moduleGroup);
    		getHttpRequest().setAttribute(REQ_CUST_CUSTOMER_ID, customerId);
    	} catch(Exception e){
    		throw new PhrescoException(e);
    	}
    	
    	return COMP_FEATURES_LIST;
    }
	
	public String add() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.add()");
		}
		
		return COMP_FEATURES_ADD;
	}
	
	public String edit() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.edit()");
		}
		
		try {
		    ModuleGroup moduleGroup = getServiceManager().getModule(techId);
			getHttpRequest().setAttribute(REQ_MODULE_GROUP, moduleGroup);
			getHttpRequest().setAttribute(REQ_FROM_PAGE, fromPage);
		} catch (Exception e) {
		    throw new PhrescoException(e);
		}

		return COMP_FEATURES_ADD;
	}
	
	
	public String save() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.save()");
		}
		
		try {
			//InputStream inputStream = new FileInputStream(featureArc);
			/*FileOutputStream outputStream = new FileOutputStream(new File("c:/" + featureArcFileName));
			IOUtils.copy(inputStream, outputStream);*/
			
			List<ModuleGroup> moduleGroups = new ArrayList<ModuleGroup>();
			ModuleGroup moduleGroup = new ModuleGroup();
			moduleGroup.setName(name);
			moduleGroup.setDescription(description);
			moduleGroup.setVersions(versions);
			moduleGroup.setType(REST_QUERY_TYPE_MODULE);
			moduleGroup.setCustomerId(customerId);
			moduleGroups.add(moduleGroup);
			ClientResponse clientResponse = getServiceManager().createModules(moduleGroups);
			if(clientResponse.getStatus() != 200 && clientResponse.getStatus() != 201){
				addActionError(getText(FEATURE_NOT_ADDED, Collections.singletonList(name)));
			} else {
			addActionMessage(getText(FEATURE_ADDED, Collections.singletonList(name)));
			}
		} catch (Exception e) {
			throw new PhrescoException(e);
		} 

		return list();
	}
	

	public String update() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.update()");
		}
		
		try {
			ModuleGroup moduleGroup = new ModuleGroup();
			moduleGroup.setName(name);
			moduleGroup.setDescription(description);
			moduleGroup.setVersions(versions);
			getServiceManager().updateModuleGroups(moduleGroup, techId);
		} catch(Exception e){
			throw new PhrescoException(e);
		}
		
		return list();	
	}
	
	public String delete() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.delete()");
		}
		
		try {
			String[] techIds = getHttpRequest().getParameterValues(REST_QUERY_TECHID);
			if (techIds != null) {
				for (String techId : techIds) {
					ClientResponse clientResponse = getServiceManager().deleteModule(techId);
					if (clientResponse.getStatus() != 200) {
						addActionError(getText(ROLE_NOT_DELETED));
					}
				}
				addActionMessage(getText(ROLE_DELETED));
			}
		} catch (Exception e) {
			throw new PhrescoException(e);
		}
		
		return list();
	}
		
	public String validateForm() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.validateForm()");
		}
		
		boolean isError = false;
		if (StringUtils.isEmpty(name)) {
			setNameError(getText(KEY_I18N_ERR_NAME_EMPTY));
			isError = true;
		} 

		if (StringUtils.isEmpty(version)) {
			setVersError(getText(KEY_I18N_ERR_VER_EMPTY));
			isError = true;
		}
		
		/*if (StringUtils.isEmpty(featureArcFileName) || featureArc == null) {
			setFileError(getText(KEY_I18N_ERR_FILE_EMPTY));
			isError = true;
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
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public File getFeatureArc() {
		return featureArc;
	}

	public void setFeatureArc(File featureArc) {
		this.featureArc = featureArc;
	}

	public String getFeatureArcFileName() {
		return featureArcFileName;
	}

	public void setFeatureArcFileName(String featureArcFileName) {
		this.featureArcFileName = featureArcFileName;
	}

	public String getFeatureArcContentType() {
		return featureArcContentType;
	}

	public void setFeatureArcContentType(String featureArcContentType) {
		this.featureArcContentType = featureArcContentType;
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
	
	public String getTechId() {
		return techId;
	}

	public void setTechId(String techId) {
		this.techId = techId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Module> getVersions() {
		return versions;
	}

	public void setVersions(List<Module> versions) {
		this.versions = versions;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}