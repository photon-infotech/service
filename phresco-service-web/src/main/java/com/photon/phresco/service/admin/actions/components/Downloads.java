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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.CoreOption;
import com.photon.phresco.commons.model.DownloadInfo;
import com.photon.phresco.commons.model.Technology;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.service.client.api.Content;
import com.photon.phresco.service.model.FileInfo;
import com.photon.phresco.service.util.ServerUtil;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;

public class Downloads extends ServiceBaseAction { 

	private static final long serialVersionUID = 6801037145464060759L;
	
	private static final Logger S_LOGGER = Logger.getLogger(Downloads.class);
	private static Boolean s_isDebugEnabled = S_LOGGER.isDebugEnabled();

	private String downloadId = "";
	private String name = "";
	private String version = "";
	private String platform = "";
	private String group = "";
	private String description = "";
	private String technology = "";
	
	private String nameError = "";
	private String verError = "";
	private String appltError = "";
	private String groupError = "";
	private String techError = "";
	private String fileError = "";
	private boolean errorFound = false;
	
	private String fromPage = "";
	private String customerId = "";
	private String oldVersion = "";
	private String type = ""; // type of the file uploaded (file or image) 
   
	private static byte[] downloadByteArray = null;
	private static byte[] byteArray = null;
	private static String downloadJarName = "";
	private static String downloadImageName = "";

	public String list() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method Downloads.list()");
		}
		
		try {
			List<DownloadInfo> downloadInfo = getServiceManager().getDownloads(getCustomerId());
			setReqAttribute(REQ_DOWNLOAD_INFO, downloadInfo);
			setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
		} catch (PhrescoException e) {
			return showErrorPopup(e, EXCEPTION_DOWNLOADS_LIST);
		}

		//to clear file inpustreams
		downloadByteArray = null;
		byteArray = null;
		downloadJarName = null;
		downloadImageName = null;
		
		return COMP_DOWNLOAD_LIST;	
	}
	
	public String add() throws PhrescoException {
		if (s_isDebugEnabled) {	
			S_LOGGER.debug("Entering Method Downloads.add()");
		}
		
		try {
			List<Technology> technologies = getServiceManager().getArcheTypes(getCustomerId());
			setReqAttribute(REQ_ARCHE_TYPES, technologies);
		} catch (PhrescoException e) {
			return showErrorPopup(e, EXCEPTION_DOWNLOADS_ADD);
		}
		
		return COMP_DOWNLOAD_ADD;
	}
	
	public String edit() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method Downloads.edit()");
		}
		
		try {
			DownloadInfo downloadInfo = getServiceManager().getDownload(getDownloadId(), getCustomerId());
			setReqAttribute(REQ_DOWNLOAD_INFO, downloadInfo);
			setReqAttribute(REQ_FROM_PAGE, EDIT);
			List<Technology> technologies = getServiceManager().getArcheTypes(getCustomerId());
			setReqAttribute(REQ_ARCHE_TYPES, technologies);
		} catch (PhrescoException e) {
			return showErrorPopup(e, EXCEPTION_DOWNLOADS_EDIT);
		}

		return COMP_DOWNLOAD_ADD;
	}
	
	public String save() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method Downloads.save()");
		}

		try {
			ClientResponse clientResponse = getServiceManager().createDownloads(getDownloadInfo(), getCustomerId());
			if (clientResponse.getStatus() != RES_CODE_200) {
				addActionError(getText(DOWNLOAD_NOT_ADDED, Collections.singletonList(getName())));
			} else {
				addActionMessage(getText(DOWNLOAD_ADDED, Collections.singletonList(getName())));
			}
		} catch (PhrescoException e) {
			return showErrorPopup(e, EXCEPTION_DOWNLOADS_SAVE);
		}
		
		return list();
	}

	public String update() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Downloads.update()");
		}

		try {
			getServiceManager().updateDownload(getDownloadInfo(), getDownloadId(), getCustomerId());
		} catch (PhrescoException e) {
			return showErrorPopup(e, EXCEPTION_DOWNLOADS_UPDATE);
		}

		return list();
	}
	
	 private MultiPart getDownloadInfo() throws PhrescoException {
	        MultiPart multiPart = new MultiPart();
	        
	        DownloadInfo download = new DownloadInfo();
	        if (StringUtils.isNotEmpty(getFromPage())) {
	            download.setId(getDownloadId());
	        }
	        download.setName(getName());
	        download.setDescription(getDescription());
	        
	        List<String> customerIds = new ArrayList<String>();
	        customerIds.add(getCustomerId());
	        download.setCustomerIds(customerIds);
	        
	        List<ArtifactInfo> downloadVersions = new ArrayList<ArtifactInfo>();
	        ArtifactInfo downloadVersion = new ArtifactInfo();
	        downloadVersion.setVersion(getVersion());
	        downloadVersions.add(downloadVersion);
	        download.setVersions(downloadVersions);
	        //TODO Arunprasanna
	        List<CoreOption> appliesTo = new ArrayList<CoreOption>();
	        CoreOption coreOption = new CoreOption();
	        coreOption.setTechId(getTechnology());
	        appliesTo.add(coreOption);
	        download.setAppliesTo(appliesTo); 
	        //download.setType(group);
	        
	        BodyPart jsonPart = new BodyPart();
	        jsonPart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
	        jsonPart.setEntity(download);
	        Content content = new Content(Content.Type.JSON, getName(), null, null, null, 0);
	        jsonPart.setContentDisposition(content);
	        multiPart.bodyPart(jsonPart);
	        if (StringUtils.isNotEmpty(downloadJarName)) {
	            InputStream downloadIs = new ByteArrayInputStream(downloadByteArray);
	            BodyPart bodyPart = getServiceManager().createBodyPart(getName(), Content.Type.JAR, downloadIs);
	            multiPart.bodyPart(bodyPart);
	        }
	        
	        if(StringUtils.isNotEmpty(downloadImageName)){
	            InputStream downloadImage=new ByteArrayInputStream(byteArray);
	            BodyPart binaryPart = getServiceManager().createBodyPart(getName(), Content.Type.JAR, downloadImage);
	            multiPart.bodyPart(binaryPart);
	        }
	        return multiPart;
	    }

	public String delete() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method Downloads.delete()");
		}

		try {
			String[] downloadIds = getHttpRequest().getParameterValues(REQ_DOWNLOAD_ID);
			if (ArrayUtils.isNotEmpty(downloadIds)) {
				for (String downloadId : downloadIds) {
					ClientResponse clientResponse =getServiceManager().deleteDownloadInfo(downloadId, getCustomerId());
					if (clientResponse.getStatus() != RES_CODE_200) {
						addActionError(getText(DOWNLOAD_NOT_DELETED));
					}
				}
				addActionMessage(getText(DOWNLOAD_DELETED));
			}
		}catch (PhrescoException e) {
			return showErrorPopup(e, EXCEPTION_DOWNLOADS_DELETE);
		}

		return list();
	}

	public String uploadFile() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Downloads.uploadFile()");
		}
		
		PrintWriter writer = null;
		try {
            writer = getHttpResponse().getWriter();
	        downloadJarName = getHttpRequest().getHeader(X_FILE_NAME);
	        InputStream is = getHttpRequest().getInputStream();
	        downloadByteArray = IOUtils.toByteArray(is);
	        InputStream applnIs = new ByteArrayInputStream(downloadByteArray);
	        //TODO Arunprasanna
	        ArtifactGroup archetypeInfo = ServerUtil.getArtifactinfo(applnIs);
	        FileInfo fileInfo = new FileInfo();
	        getHttpResponse().setStatus(getHttpResponse().SC_OK);
	        if (archetypeInfo != null) {
	        	//archetypeInfo.setMavenJar(true);
	        	//archetypeInfo.setSuccess(true);
	        	//fileInfo.setArtifactId(artifactId);
        		//fileInfo.setGroupId(groupId);
        		List<ArtifactInfo> versions = new ArrayList<ArtifactInfo>();
        		ArtifactInfo fileInfoversion = new ArtifactInfo();
        		fileInfoversion.setVersion(getVersion());
        		versions.add(fileInfoversion);
        		fileInfo.setVersions(versions);
        		fileInfo.setMavenJar(true);
        		fileInfo.setSuccess(true);
	        	Gson gson = new Gson();
	        	String json = gson.toJson(fileInfo);
	        	writer.print(json);
	        } else {
	        	writer.print(MAVEN_JAR_FALSE);
	        }
	        writer.flush();
	        writer.close();
		} catch (Exception e) {
			getHttpResponse().setStatus(getHttpResponse().SC_INTERNAL_SERVER_ERROR);
            writer.print(SUCCESS_FALSE);
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
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Downloads.removeUploadedFile()");
		}
		
		if(REQ_DOWNLOAD_UPLOAD_FILE.equals(getType())) {
			downloadByteArray = null;
			downloadJarName = null;
		} else {
			downloadImageName = null;
			byteArray = null;
		}
	}

	public String validateForm() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method Downloads.validateForm()");
		}
		
		boolean isError = false;
		//Empty validation for name
		if (StringUtils.isEmpty(getName())) {
			setNameError(getText(KEY_I18N_ERR_NAME_EMPTY));
			isError = true;
		}

		if (StringUtils.isEmpty(getVersion())) {//Empty validation for version
			setVerError(getText(KEY_I18N_ERR_VER_EMPTY));
			isError = true;
		}  else if (StringUtils.isEmpty(getFromPage()) || (!getVersion().equals(getOldVersion()))) {
			//To check whether the version already exist
			List<DownloadInfo> downloads = getServiceManager().getDownloads(getCustomerId());
			if (CollectionUtils.isNotEmpty(downloads)) {
				for (DownloadInfo download : downloads) {
					if (download.getName().equalsIgnoreCase(getName()) && download.getVersions().equals(getVersion())) {
						setVerError(getText(KEY_I18N_ERR_VER_ALREADY_EXISTS));
						isError = true;
						break;
					}
				}
			}
		}
		//Empty validation for platform 
		if (StringUtils.isEmpty(getPlatform())) {
			setAppltError(getText(KEY_I18N_ERR_APPLNPLTF_EMPTY));
			isError = true;
		} 

		//Empty validation for group
		if (StringUtils.isEmpty(getGroup())) {
			setGroupError(getText(KEY_I18N_ERR_GROUP_EMPTY));
			isError = true;
		}
		
		//Empty validation for technology
		if (StringUtils.isNotEmpty(getTechnology())) {
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

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String application) {
		this.platform = application;
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

	public String getDownloadId() {
		return downloadId;
	}

	public void setDownloadId(String id) {
		this.downloadId = id;
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
	
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	
	public String getTechnology() {
		return technology;
	}
	
	public void setTechError(String techError) {
		this.techError = techError;
	}
	
	public String getTechError() {
		return techError;
	}
}
