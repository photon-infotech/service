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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;

import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.ApplicationType;
import com.photon.phresco.model.ArchetypeInfo;
import com.photon.phresco.model.Technology;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.service.client.api.Content;
import com.photon.phresco.service.util.ServerUtil;
import com.photon.phresco.util.ServiceConstants;



public class Archetypes extends ServiceBaseAction { 

	private static final long serialVersionUID = 6801037145464060759L;
	private static final Logger S_LOGGER = Logger.getLogger(Archetypes.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	/* plugin and appln jar upload*/
	private static Map<String, byte[]> pluginMap = new HashMap<String, byte[]>();
	private static byte[] applnByteArray = null;
	private static String applnJarName = null;

	private String name = null;
	private String nameError = null;
	private String version = null;
	
	private String verError = null;
	private String apptype = null;
	private String appError = null;
	private String fileError = null;
	private boolean errorFound = false;

	private String description;
	private String fromPage = null;
	private String techId = null;
    private String customerId = null;
	
	private String versionComment = null;
	
	private String jarVersion = "";
	private String groupId = "";
	private String artifactId = "";
	
	public String list() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method Archetypes.list()");
		}

		try {
			List<Technology> technologies = getServiceManager().getArcheTypes(customerId);
			getHttpRequest().setAttribute(REQ_ARCHE_TYPES, technologies);
			getHttpRequest().setAttribute(REQ_CUST_CUSTOMER_ID, customerId);
		} catch (Exception e) {
			throw new PhrescoException(e);
		}
		
		/* To clear appln & plugin input streams */
		pluginMap.clear();
		applnByteArray = null;
		applnJarName = null;

		return COMP_ARCHETYPE_LIST;
	}

	public String add() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method Archetypes.add()");
	    }

		try {
			List<ApplicationType> appTypes = getServiceManager().getApplicationTypes(customerId);
			getHttpRequest().setAttribute(REQ_APP_TYPES, appTypes);
		} catch (PhrescoException e) {
			throw new PhrescoException(e);
		}

		return COMP_ARCHETYPE_ADD;
	}

	public String edit() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method Archetypes.edit()");
		}

		try {
			Technology technology = getServiceManager().getArcheType(techId, customerId);
			getHttpRequest().setAttribute(REQ_ARCHE_TYPE,  technology);
			getHttpRequest().setAttribute(REQ_FROM_PAGE, REQ_EDIT);
			List<ApplicationType> appTypes = getServiceManager().getApplicationTypes(customerId);
			getHttpRequest().setAttribute(REQ_APP_TYPES, appTypes);
		} catch (Exception e) {
			throw new PhrescoException(e);
		}

		return COMP_ARCHETYPE_ADD;
	}

	public String save() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method Archetypes.save()");
	    }
		
		try {
		 	MultiPart multiPart = new MultiPart();
		 	
	    	List<String> versions = new ArrayList<String>();
	    	versions.add(version);
	        Technology technology = new Technology();
	        technology.setName(name);
	        technology.setDescription(description);
	        technology.setAppTypeId(apptype);
	        technology.setVersionComment(versionComment);
	        technology.setCustomerId(customerId);
	        ArchetypeInfo archetypeInfo = new ArchetypeInfo(groupId, artifactId, version, "jar");
	        technology.setArchetypeInfo(archetypeInfo);
	        
		    BodyPart jsonPart = new BodyPart();
		    jsonPart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		    jsonPart.setEntity(technology);
		    Content content = new Content(name, name, null, null, null, 0);
		    jsonPart.setContentDisposition(content);
		    multiPart.bodyPart(jsonPart);
			   
			if (!pluginMap.isEmpty()) {
				Iterator iter = pluginMap.keySet().iterator();
			    while (iter.hasNext()) {
				    String key = (String) iter.next();
				    byte[] byteArray = (byte[]) pluginMap.get(key);
				    InputStream pluginJarIs = new ByteArrayInputStream(byteArray);
				    BodyPart binaryPart = getServiceManager().createBodyPart(name, FILE_FOR_PLUGIN, pluginJarIs);
				    multiPart.bodyPart(binaryPart);
			    }
			}

			if (StringUtils.isNotEmpty(applnJarName)) {
				InputStream applnIs = new ByteArrayInputStream(applnByteArray);
				BodyPart binaryPart2 = getServiceManager().createBodyPart(name, FILE_FOR_APPTYPE, applnIs);
		        multiPart.bodyPart(binaryPart2);
			}
			
			ClientResponse clientResponse = getServiceManager().createArcheTypes(multiPart, customerId);
			if (clientResponse.getStatus() != ServiceConstants.RES_CODE_200 && clientResponse.getStatus() != ServiceConstants.RES_CODE_201) {
				addActionError(getText(ARCHETYPE_NOT_ADDED, Collections.singletonList(name)));
			} else {
				addActionMessage(getText(ARCHETYPE_ADDED, Collections.singletonList(name)));
			}
		} catch (Exception e) {
			throw new PhrescoException(e);
		} 
		
		return list();
	}
	
	public String uploadJar() throws PhrescoException {
		String type = getHttpRequest().getParameter(REQ_JAR_TYPE);
		if (REQ_PLUGIN_JAR.equals(type)) {
			uploadPluginJar();
		} else {
			uploadApplnJar();
		}
		
		return SUCCESS;
	}
	
	public String uploadApplnJar() throws PhrescoException {
		PrintWriter writer = null;
		try {
            writer = getHttpResponse().getWriter();
	        applnJarName = getHttpRequest().getHeader(X_FILE_NAME);
	        if (applnJarName.endsWith(REQ_JAR_FILE_EXTENSION) || applnJarName.endsWith(REQ_ZIP_FILE_EXTENSION) 
	        		|| applnJarName.endsWith(REQ_TAR_GZ_FILE_EXTENSION)) {
	        	InputStream is = getHttpRequest().getInputStream();
	        	byte[] tempApplnByteArray = IOUtils.toByteArray(is);
	        	boolean isArchetypeJar = ServerUtil.validateArchetypeJar(new ByteArrayInputStream(tempApplnByteArray));
	        	if (isArchetypeJar) {
	        		applnByteArray = tempApplnByteArray;
		        	ArchetypeInfo archetypeInfo = ServerUtil.getArtifactinfo(new ByteArrayInputStream(tempApplnByteArray));
		            getHttpResponse().setStatus(getHttpResponse().SC_OK);
		            if (archetypeInfo != null) {
		            	archetypeInfo.setMavenJar(true);
		            	archetypeInfo.setSuccess(true);
		            	Gson gson = new Gson();
		                String json = gson.toJson(archetypeInfo);
		            	writer.print(json);
		            } else {
		            	writer.print(MAVEN_JAR_FALSE);
		        	}
	        	} else {
	            	writer.print(INVALID_ARCHETYPE_JAR);
	        	}
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
	
	public String uploadPluginJar() throws PhrescoException {
		PrintWriter writer = null;
		try {
			writer = getHttpResponse().getWriter();
	        String jarName = getHttpRequest().getHeader(X_FILE_NAME);
	        if (jarName.endsWith(REQ_JAR_FILE_EXTENSION) || jarName.endsWith(REQ_ZIP_FILE_EXTENSION) 
	        		|| jarName.endsWith(REQ_TAR_GZ_FILE_EXTENSION)) {
	        	InputStream is = getHttpRequest().getInputStream();
	        	byte[] byteArray = IOUtils.toByteArray(is);
	        	boolean isPluginJar = ServerUtil.validatePluginJar(new ByteArrayInputStream(byteArray));
	        	if (isPluginJar) {
	        		pluginMap.put(jarName, byteArray);
	        		writer.print(SUCCESS_TRUE);
	        	} else {
	        		writer.print(INVALID_PLUGIN_JAR);
	        	}
	            getHttpResponse().setStatus(getHttpResponse().SC_OK);
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
	
	public void removeUploadedJar() {
		if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method Archetypes.removeUploadedJar()");
	    }
		
		String type = getHttpRequest().getParameter(REQ_JAR_TYPE);
		if (REQ_PLUGIN_JAR.equals(type)) {
			pluginMap.remove(getHttpRequest().getParameter(REQ_UPLOADED_JAR));
		} else {
			applnJarName = null;
			applnByteArray = null;
		}
	}
	
	public String update() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method Archetypes.update()");
	    }

		try {
			List<String> appTypes = new ArrayList<String>();
	        appTypes.add(apptype);
	    	List<String> versions = new ArrayList<String>();
	    	versions.add(version);
			Technology technology = new Technology(name, description, versions, appTypes);
			technology.setId(techId);
			technology.setCustomerId(customerId);
			getServiceManager().updateArcheType(technology, techId, customerId);
		} catch(Exception e) {
			throw new PhrescoException(e);
		}
		
		return list();
	}

	public String delete() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method Archetypes.delete()");
		}

		try {
			String[] techTypeIds = getHttpRequest().getParameterValues(REQ_ARCHE_TECHID);
			if (techTypeIds != null) {
				for (String techId : techTypeIds) {
					ClientResponse clientResponse = getServiceManager().deleteArcheType(techId, customerId);
					if (clientResponse.getStatus() != ServiceConstants.RES_CODE_200) {
						addActionError(getText(ARCHETYPE_NOT_DELETED));
					}
				}
				addActionMessage(getText(ARCHETYPE_DELETED));
			}
		} catch (Exception e) {
			throw new PhrescoException(e);
		}

		return list();
	}

	public String validateForm() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method Archetypes.validateForm()");
		}
		
		boolean isError = false;
		if (StringUtils.isEmpty(name)) {
			setNameError(getText(KEY_I18N_ERR_NAME_EMPTY ));
			isError = true;
		}

		if (StringUtils.isEmpty(version)) {
			setVerError(getText(KEY_I18N_ERR_VER_EMPTY));
			isError = true;
		}

		if (StringUtils.isEmpty(apptype)) {
			setAppError(getText(KEY_I18N_ERR_APPTYPE_EMPTY));
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

	public String getApptype() {
		return apptype;
	}

	public void setApptype(String apptype) {
		this.apptype = apptype;
	}

	public String getAppError() {
		return appError;
	}

	public void setAppError(String appError) {
		this.appError = appError;
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

	public String getTechId() {
		return techId;
	}

	public void setTechId(String techId) {
		this.techId = techId;
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

	public String getVersionComment() {
		return versionComment;
	}

	public void setVersionComment(String versionComment) {
		this.versionComment = versionComment;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getJarVersion() {
		return jarVersion;
	}

	public void setJarVersion(String jarVersion) {
		this.jarVersion = jarVersion;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
}