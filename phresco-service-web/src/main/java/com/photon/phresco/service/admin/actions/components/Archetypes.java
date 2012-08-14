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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.ApplicationType;
import com.photon.phresco.model.Technology;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.service.util.Content;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;

public class Archetypes extends ServiceBaseAction { 

	private static final long serialVersionUID = 6801037145464060759L;
	private static final Logger S_LOGGER = Logger.getLogger(Archetypes.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	/* plugin and appln jar upload*/
	private static Map<String, InputStream> pluginMap = new HashMap<String, InputStream>();
	private static InputStream applnIs = null;
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
			getHttpRequest().setAttribute(REQ_FROM_PAGE, fromPage);
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
		 	
		 	List<String> appTypes = new ArrayList<String>();
	        appTypes.add(apptype);
	    	List<String> versions = new ArrayList<String>();
	    	versions.add(version);
	        Technology technology = new Technology(name, description, versions, appTypes);
	        technology.setVersionComment(versionComment);
	        technology.setCustomerId(customerId);
	        
		    BodyPart jsonPart = new BodyPart();
		    jsonPart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		    jsonPart.setEntity(technology);
		    
		    multiPart.bodyPart(jsonPart);
			   
			if (!pluginMap.isEmpty()) {
				Iterator iter = pluginMap.keySet().iterator();
			    while (iter.hasNext()) {
				    String key = (String) iter.next();
				    InputStream pluginJarIs = (InputStream) pluginMap.get(key);
				   
				    BodyPart binaryPart = new BodyPart();
				    binaryPart.setMediaType(MediaType.APPLICATION_OCTET_STREAM_TYPE);
				    binaryPart.setEntity(pluginJarIs);
				    Content content = new Content(FILE_FOR_PLUGIN, name, null, null, null, 0);
					binaryPart.setContentDisposition(content);
				    multiPart.bodyPart(binaryPart);
			    }
			}

			if (StringUtils.isNotEmpty(applnJarName)) {
				BodyPart binaryPart2 = new BodyPart();
			    binaryPart2.setMediaType(MediaType.APPLICATION_OCTET_STREAM_TYPE);
			    binaryPart2.setEntity(applnIs);
			    Content content = new Content(FILE_FOR_APPTYPE, name, null, null, null, 0);
		        binaryPart2.setContentDisposition(content);
		        multiPart.bodyPart(binaryPart2);
			}
			
			ClientResponse clientResponse = getServiceManager().createArcheTypes(multiPart, customerId);
			if (clientResponse.getStatus() != 200 && clientResponse.getStatus() != 201) {
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
	        	applnIs = getHttpRequest().getInputStream();
	            getHttpResponse().setStatus(getHttpResponse().SC_OK);
	            writer.print(SUCCESS_TRUE);
		        writer.flush();
		        writer.close();
	        } 
		} catch (Exception e) {
			getHttpResponse().setStatus(getHttpResponse().SC_INTERNAL_SERVER_ERROR);
            writer.print(SUCCESS_FALSE);
			throw new PhrescoException(e);
		} finally {
            try {
                applnIs.close();
            } catch (IOException e) {
            	throw new PhrescoException(e);
            }
        }
		
		return SUCCESS;
	}
	
	public String uploadPluginJar() throws PhrescoException {
		PrintWriter writer = null;
        InputStream is = null;
		try {
			writer = getHttpResponse().getWriter();
	        String jarName = getHttpRequest().getHeader(X_FILE_NAME);
	        if (jarName.endsWith(REQ_JAR_FILE_EXTENSION) || jarName.endsWith(REQ_ZIP_FILE_EXTENSION) 
	        		|| jarName.endsWith(REQ_TAR_GZ_FILE_EXTENSION)) {
	        	is = getHttpRequest().getInputStream();
	            pluginMap.put(jarName, is);
	            getHttpResponse().setStatus(getHttpResponse().SC_OK);
	            writer.print(SUCCESS_TRUE);
		        writer.flush();
		        writer.close();
	        } 
		} catch (Exception e) {
			getHttpResponse().setStatus(getHttpResponse().SC_INTERNAL_SERVER_ERROR);
            writer.print(SUCCESS_FALSE);
			throw new PhrescoException(e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				throw new PhrescoException(e);
			}
		}
		
		return SUCCESS;
	}
	
	public void removeUploadedJar() {
		String type = getHttpRequest().getParameter(REQ_JAR_TYPE);
		if (REQ_PLUGIN_JAR.equals(type)) {
			pluginMap.remove(getHttpRequest().getParameter(REQ_UPLOADED_JAR));
		} else {
			applnJarName = null;
			applnIs = null;
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
					if (clientResponse.getStatus() != 200) {
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

		if (StringUtils.isEmpty(applnJarName) || applnJarName == null) {
			setFileError(getText(KEY_I18N_ERR_APPLNJAR_EMPTY));
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
}