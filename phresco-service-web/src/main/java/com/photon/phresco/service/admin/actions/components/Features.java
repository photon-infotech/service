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
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.ArchetypeInfo;
import com.photon.phresco.model.Module;
import com.photon.phresco.model.ModuleGroup;
import com.photon.phresco.model.Technology;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.service.client.api.Content;
import com.photon.phresco.service.util.ServerUtil;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;

public class Features extends ServiceBaseAction {

	private static final long serialVersionUID = 6801037145464060759L;
	private static final Logger S_LOGGER = Logger.getLogger(Features.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	private String name = null;
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
    
    private String artifactId = "";
    private String groupId = "";
    private String jarVersion = "";
    
	private static byte[] featureByteArray = null;
	private static String featureJarName = null;
    
	private String description = null;
    private List<Module> versions = null;
	
    public String list() throws PhrescoException {
    	if (isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  Features.list()");
    	}
    	
    	try {
    		List<ModuleGroup> moduleGroup = getServiceManager().getFeatures(customerId);
    		getHttpRequest().setAttribute(REQ_MODULE_GROUP, moduleGroup);
    		getHttpRequest().setAttribute(REQ_CUST_CUSTOMER_ID, customerId);
    	} catch(Exception e){
    		throw new PhrescoException(e);
    	}
    	
    	return COMP_FEATURES_LIST;
    }
	
	public String add() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.add()");
		}
		
		List<Technology> technologies = getServiceManager().getArcheTypes(customerId);
		getHttpRequest().setAttribute(REQ_ARCHE_TYPES, technologies);
		getHttpRequest().setAttribute(REQ_CUST_CUSTOMER_ID, customerId);
		
		return COMP_FEATURES_ADD;
	}
	
	public String edit() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.edit()");
		}
		
		try {
		    ModuleGroup moduleGroup = getServiceManager().getFeature(techId, customerId);
			getHttpRequest().setAttribute(REQ_MODULE_GROUP, moduleGroup);
			getHttpRequest().setAttribute(REQ_FROM_PAGE, REQ_EDIT);
			getHttpRequest().setAttribute(REQ_CUST_CUSTOMER_ID, customerId);
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
			MultiPart multiPart = new MultiPart();
			
			List<ModuleGroup> moduleGroups = new ArrayList<ModuleGroup>();
			ModuleGroup moduleGroup = new ModuleGroup();
			moduleGroup.setName(name);
			moduleGroup.setDescription(description);
			moduleGroup.setVersions(versions);
			moduleGroup.setType(REST_QUERY_TYPE_MODULE);
			moduleGroup.setCustomerId(customerId);
			moduleGroup.setArtifactId(artifactId);
			moduleGroup.setGroupId(groupId);
			moduleGroup.setVersions(versions);
		     
			BodyPart jsonPart = new BodyPart();
			jsonPart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			jsonPart.setEntity(moduleGroup);
			Content content = new Content("object", name, null, null, null, 0);
			jsonPart.setContentDisposition(content);
			multiPart.bodyPart(jsonPart);
			    
			if (StringUtils.isNotEmpty(featureJarName)) {
				InputStream featureIs = new ByteArrayInputStream(featureByteArray);
				BodyPart binaryPart2 = getServiceManager().createBodyPart(name, FILE_FOR_APPTYPE, featureIs);
				multiPart.bodyPart(binaryPart2);
			}

			moduleGroups.add(moduleGroup);   
			
			ClientResponse clientResponse = getServiceManager().createFeatures(moduleGroups, customerId);
			if (clientResponse.getStatus() != 200 && clientResponse.getStatus() != 201) {
				addActionError(getText(FEATURE_NOT_ADDED, Collections.singletonList(name)));
			} else {
			addActionMessage(getText(FEATURE_ADDED, Collections.singletonList(name)));
			}
		} catch (Exception e) {
			throw new PhrescoException(e);
		} 

		return list();
	}
	
	public String uploadFile() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.uploadFile()");
		}
		
		PrintWriter writer = null;
		try {
            writer = getHttpResponse().getWriter();
	        featureJarName = getHttpRequest().getHeader(X_FILE_NAME);
	        if (featureJarName.endsWith(REQ_JAR_FILE_EXTENSION) || featureJarName.endsWith(REQ_ZIP_FILE_EXTENSION) 
	        		|| featureJarName.endsWith(REQ_TAR_GZ_FILE_EXTENSION)) {
	        	InputStream is = getHttpRequest().getInputStream();
	        	featureByteArray = IOUtils.toByteArray(is);
	            getHttpResponse().setStatus(getHttpResponse().SC_OK);
	            writer.print(SUCCESS_TRUE);
		        writer.flush();
		        writer.close();
	        }
		} catch (Exception e) {
			getHttpResponse().setStatus(getHttpResponse().SC_INTERNAL_SERVER_ERROR);
            writer.print(SUCCESS_FALSE);
			throw new PhrescoException(e);
		}
		
		return SUCCESS;
	}
	
	public void removeUploadedFile() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.removeUploadedFile()");
		}
		
		featureByteArray = null;
		featureJarName = null;
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
			moduleGroup.setCustomerId(customerId);
			getServiceManager().updateFeature(moduleGroup, techId, customerId);
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
					ClientResponse clientResponse = getServiceManager().deleteFeature(techId, customerId);
					if (clientResponse.getStatus() != 200) {
						addActionError(getText(FEATURE_NOT_DELETED));
					}
				}
				addActionMessage(getText(FEATURE_DELETED));
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

		if (CollectionUtils.isEmpty(versions)) {
			setVersError(getText(KEY_I18N_ERR_VER_EMPTY));
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