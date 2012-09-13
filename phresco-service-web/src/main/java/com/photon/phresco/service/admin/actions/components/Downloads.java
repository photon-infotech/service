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
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.ArchetypeInfo;
import com.photon.phresco.model.DownloadInfo;
import com.photon.phresco.model.Technology;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.service.admin.commons.LogErrorReport;
import com.photon.phresco.service.client.api.Content;
import com.photon.phresco.service.util.ServerUtil;
import com.photon.phresco.util.ServiceConstants;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;

public class Downloads extends ServiceBaseAction { 

	private static final long serialVersionUID = 6801037145464060759L;
	private static final Logger S_LOGGER = Logger.getLogger(Downloads.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();

	private String name = null;
	private String nameError = null;
	private String version = null;
	private String verError = null;
	private String application = null;
	private String appltError = null;
	private String group = null;
	private String groupError = null;
	private String fileError = null;
	private boolean errorFound = false;
	private String description=null;
	private String id = null;
	private List<String> technology = null;
	private String techError = null;
	private String fromPage = null;
	private String customerId = null;
	private String oldVersion = null;
	private String type = null; // type of the file uploaded (file or image) 
   
	private static byte[] downloadByteArray = null;
	private static byte[] byteArray = null;
	private static String downloadJarName = null;
	private static String downloadImageName = null;

	public String list() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method Downloads.list()");
		}
		
		try {
			List<DownloadInfo> downloadInfo = getServiceManager().getDownloads(customerId);
			getHttpRequest().setAttribute(REQ_DOWNLOAD_INFO, downloadInfo);
			getHttpRequest().setAttribute(REQ_CUST_CUSTOMER_ID, customerId);
		} catch (PhrescoException e) {
			new LogErrorReport(e, DOWNLOADS_LIST_EXCEPTION);
			
			return LOG_ERROR;	
		}

		//to clear file inpustreams
		downloadByteArray = null;
		byteArray = null;
		downloadJarName = null;
		downloadImageName = null;
		
		return COMP_DOWNLOAD_LIST;	
	}

	public String add() throws PhrescoException {
		if (isDebugEnabled) {	
			S_LOGGER.debug("Entering Method Downloads.add()");
		}
		
		try {
			List<Technology> technologies = getServiceManager().getArcheTypes(customerId);
			getHttpRequest().setAttribute(REQ_ARCHE_TYPES, technologies);
		} catch (PhrescoException e) {
			new LogErrorReport(e, DOWNLOADS_ADD_EXCEPTION);
			
			return LOG_ERROR;	
		}
		
		return COMP_DOWNLOAD_ADD;
	}
	
	public String edit() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method Downloads.edit()");
		}
		
		try {
			DownloadInfo downloadInfo = getServiceManager().getDownload(id, customerId);
			getHttpRequest().setAttribute(REQ_DOWNLOAD_INFO, downloadInfo);
			getHttpRequest().setAttribute(REQ_FROM_PAGE, REQ_EDIT);
			List<Technology> technologies = getServiceManager().getArcheTypes(customerId);
			getHttpRequest().setAttribute(REQ_ARCHE_TYPES, technologies);
		} catch (PhrescoException e) {
			new LogErrorReport(e, DOWNLOADS_EDIT_EXCEPTION);
			
			return LOG_ERROR;	
		}

		return COMP_DOWNLOAD_ADD;
	}
	
	public String save() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method Downloads.save()");
		}

		try {
			MultiPart multiPart = new MultiPart();

			List<DownloadInfo> downloadInfo = new ArrayList<DownloadInfo>();
			DownloadInfo download = new DownloadInfo();
			download.setName(name);
			download.setDescription(description);
			download.setVersion(version);
			download.setCustomerId(customerId);
			download.setAppliesTo(technology);
			download.setType(group);
			
			BodyPart jsonPart = new BodyPart();
		    jsonPart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		    jsonPart.setEntity(download);
		    Content content = new Content("object", name, null, null, null, 0);
		    jsonPart.setContentDisposition(content);
		    multiPart.bodyPart(jsonPart);
		    if (StringUtils.isNotEmpty(downloadJarName)) {
		    	InputStream downloadIs = new ByteArrayInputStream(downloadByteArray);
				BodyPart binaryPart2 = getServiceManager().createBodyPart(name, FILE_FOR_APPTYPE, downloadIs);
		        multiPart.bodyPart(binaryPart2);
			}
			
		    if(StringUtils.isNotEmpty(downloadImageName)){
		    	InputStream downloadImage=new ByteArrayInputStream(byteArray);
		    	BodyPart binaryPart = getServiceManager().createBodyPart(name, FILE_FOR_APPTYPE, downloadImage);
		        multiPart.bodyPart(binaryPart);
		    }
			downloadInfo.add(download);
			ClientResponse clientResponse = getServiceManager().createDownloads(downloadInfo, customerId);
			if (clientResponse.getStatus() != ServiceConstants.RES_CODE_200) {
				addActionError(getText(DOWNLOAD_NOT_ADDED, Collections.singletonList(name)));
			} else {
				addActionMessage(getText(DOWNLOAD_ADDED, Collections.singletonList(name)));
			}
		} catch (PhrescoException e) {
			new LogErrorReport(e, DOWNLOADS_SAVE_EXCEPTION);
			
			return LOG_ERROR;	
		}
		
		return list();
	}
	
	public String update() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Downloads.update()");
		}

		try {
			MultiPart multiPart = new MultiPart();
			
			DownloadInfo download = new DownloadInfo();
			download.setId(id);
			download.setName(name);
			download.setDescription(description);
			download.setVersion(version);
			download.setCustomerId(customerId);
			download.setAppliesTo(technology);
			download.setType(group);
			
			BodyPart jsonPart = new BodyPart();
		    jsonPart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		    jsonPart.setEntity(download);
		    Content content = new Content("object", name, null, null, null, 0);
		    jsonPart.setContentDisposition(content);
		    multiPart.bodyPart(jsonPart);
		    if (StringUtils.isNotEmpty(downloadJarName)) {
		    	InputStream downloadIs = new ByteArrayInputStream(downloadByteArray);
				BodyPart binaryPart2 = getServiceManager().createBodyPart(name, FILE_FOR_APPTYPE, downloadIs);
		        multiPart.bodyPart(binaryPart2);
			}
			
		    if(StringUtils.isNotEmpty(downloadImageName)){
		    	InputStream downloadImage=new ByteArrayInputStream(byteArray);
		    	BodyPart binaryPart = getServiceManager().createBodyPart(name, FILE_FOR_APPTYPE, downloadImage);
		        multiPart.bodyPart(binaryPart);
		    }
			
			
			getServiceManager().updateDownload(download, id, customerId);
		} catch (PhrescoException e) {
			new LogErrorReport(e, DOWNLOADS_UPDATE_EXCEPTION);
			
			return LOG_ERROR;	
		}

		return list();
	}

	public String delete() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method Downloads.delete()");
		}

		try {
			String[] downloadIds = getHttpRequest().getParameterValues(REQ_DOWNLOAD_ID);
			if (downloadIds != null) {
				for (String downloadId : downloadIds) {
					ClientResponse clientResponse =getServiceManager().deleteDownloadInfo(downloadId, customerId);
					if (clientResponse.getStatus() != ServiceConstants.RES_CODE_200) {
						addActionError(getText(DOWNLOAD_NOT_DELETED));
					}
				}
				addActionMessage(getText(DOWNLOAD_DELETED));
			}
		}catch (PhrescoException e) {
			new LogErrorReport(e, DOWNLOADS_DELETE_EXCEPTION);
			
			return LOG_ERROR;	
		}

		return list();
	}

	public String uploadFile() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Downloads.uploadFile()");
		}
		
		PrintWriter writer = null;
		try {
            writer = getHttpResponse().getWriter();
	        downloadJarName = getHttpRequest().getHeader(X_FILE_NAME);
	        if (downloadJarName.endsWith(REQ_JAR_FILE_EXTENSION) || downloadJarName.endsWith(REQ_ZIP_FILE_EXTENSION) 
	        		|| downloadJarName.endsWith(REQ_TAR_GZ_FILE_EXTENSION) || downloadJarName.endsWith(REQ_IMAGE_JPG_EXTENSION) 
	        		|| downloadJarName.endsWith(REQ_IMAGE_JPEG_EXTENSION) || downloadJarName.endsWith(REQ_IMAGE_PNG_EXTENSION)) {
	        
	        	InputStream is = getHttpRequest().getInputStream();
	        	downloadByteArray = IOUtils.toByteArray(is);
	        	InputStream applnIs = new ByteArrayInputStream(downloadByteArray);
	        	ArchetypeInfo archetypeInfo = ServerUtil.getArtifactinfo(applnIs);
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
	
	public String uploadImage() throws PhrescoException {
		PrintWriter writer = null;
		try {
			writer = getHttpResponse().getWriter();
	        downloadImageName = getHttpRequest().getHeader(X_FILE_NAME);
        	InputStream is = getHttpRequest().getInputStream();
        	byteArray = IOUtils.toByteArray(is);
        	InputStream applnIs = new ByteArrayInputStream(byteArray);
            getHttpResponse().setStatus(getHttpResponse().SC_OK);
        	writer.print(MAVEN_JAR_FALSE);
            getHttpResponse().setStatus(getHttpResponse().SC_OK);
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
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Downloads.removeUploadedFile()");
		}
		
		if(REQ_DOWNLOAD_UPLOAD_FILE.equals(type)) {
			downloadByteArray = null;
			downloadJarName = null;
		} else {
			downloadImageName = null;
			byteArray = null;
		}
	}

	public String validateForm() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method Downloads.validateForm()");
		}
		
		boolean isError = false;
		if (StringUtils.isEmpty(name)) {
			setNameError(getText(KEY_I18N_ERR_NAME_EMPTY));
			isError = true;
		}

		if (StringUtils.isEmpty(version)) {
			setVerError(getText(KEY_I18N_ERR_VER_EMPTY));
			isError = true;
		}  else if (StringUtils.isEmpty(fromPage) || (!version.equals(oldVersion))) {
			//To check whether the version already exist
			List<DownloadInfo> downloads = getServiceManager().getDownloads(customerId);
			if (downloads != null) {
				for (DownloadInfo download : downloads) {
					if (download.getName().equalsIgnoreCase(name) && download.getVersion().equals(version)) {
						setVerError(getText(KEY_I18N_ERR_VER_ALREADY_EXISTS));
						isError = true;
						break;
					}
				}
			}
		}

		if (StringUtils.isEmpty(application)) {
			setAppltError(getText(KEY_I18N_ERR_APPLNPLTF_EMPTY));
			isError = true;
		} 

		if (StringUtils.isEmpty(group)) {
			setGroupError(getText(KEY_I18N_ERR_GROUP_EMPTY));
			isError = true;
		}
		
		if (technology == null) {
			setTechError(getText(KEY_I18N_ERR_TECH_EMPTY));
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

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getAppltError() {
		return appltError;
	}

	public void setAppltError(String appltError) {
		this.appltError = appltError;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getGroupError() {
		return groupError;
	}

	public void setGroupError(String groupError) {
		this.groupError = groupError;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public void setOldVersion(String oldVersion) {
		this.oldVersion = oldVersion;
	}

	public String getOldVersion() {
		return oldVersion;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public void setTechnology(List<String> technology) {
		this.technology = technology;
	}
	
	public List<String> getTechnology() {
		return technology;
	}
	
	public void setTechError(String techError) {
		this.techError = techError;
	}
	
	public String getTechError() {
		return techError;
	}
}
