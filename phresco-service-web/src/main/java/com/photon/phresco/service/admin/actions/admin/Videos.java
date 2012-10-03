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

package com.photon.phresco.service.admin.actions.admin;

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

import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.VideoInfo;
import com.photon.phresco.commons.model.VideoType;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.service.client.api.ServiceManager;
public class Videos extends ServiceBaseAction { 

	private static final long serialVersionUID = -3065717999492844302L;

	private static final Logger S_LOGGER = Logger.getLogger(Videos.class);
	private static Boolean s_isDebugEnabled = S_LOGGER.isDebugEnabled();

	private static byte[] s_videoByteArray = null;
	private static byte[] s_imgByteArray = null;

	private String name = "";
	private String description = "";
	private String groupId = "";
	private String artifactId = "";
	private String version = "";
	private String videoId = "";
	private String nameError = "";
	private String videoError = "";
	private String imgError = "";
	private Boolean errorFound = false;

	public String list() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method Videos.list()");
		}
		try {
			List<VideoInfo> videoInfos = getServiceManager().getVideoInfos();
			setReqAttribute(REQ_VIDEO_INFO, videoInfos);
		} catch (PhrescoException e) {
			return showErrorPopup(e, EXCEPTION_VIDEO_LIST);
		}

		s_videoByteArray = null;
		s_imgByteArray = null;

		return ADMIN_VIDEO_LIST;	
	}

	public String add() {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method Videos.list()");
		}

		return ADMIN_VIDEO_ADD;
	}

	public String edit() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method PilotProjects.edit()");
		}
		try {
			ServiceManager serviceManager = getServiceManager();
			VideoInfo videoInfo = serviceManager.getVideo(getVideoId());
			setReqAttribute(REQ_VIDEO_INFO, videoInfo);
			setReqAttribute(REQ_FROM_PAGE, EDIT);
		} catch (PhrescoException e) {
			return showErrorPopup(e, EXCEPTION_VIDEO_EDIT);
		}

		return ADMIN_VIDEO_ADD;
	}
	public String save() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method Videos.save()");
		}
		try {
			List<InputStream> inputStreams = new ArrayList<InputStream>();
			inputStreams.add(new ByteArrayInputStream(s_videoByteArray));
			inputStreams.add(new ByteArrayInputStream(s_imgByteArray));
			getServiceManager().createVideos(createVideoInstance(), inputStreams);
			addActionMessage(getText(VIDEO_ADDED, Collections.singletonList(getName())));
		} catch (PhrescoException e) {
			return showErrorPopup(e, EXCEPTION_VIDEO_SAVE);
		}

		return list();
	}
	
	public String update() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  PilotProjects.update()");
		}
		try {
			List<InputStream> inputStreams = new ArrayList<InputStream>();
			inputStreams.add(new ByteArrayInputStream(s_videoByteArray));
			inputStreams.add(new ByteArrayInputStream(s_imgByteArray));
			getServiceManager().updateVideo(createVideoInstance(), inputStreams, getVideoId());
			addActionMessage(getText(PLTPROJ_UPDATED, Collections.singletonList(getName())));
		} catch (PhrescoException e) {
			return showErrorPopup(e, EXCEPTION_VIDEO_UPDATE);
		}

		return list();
	}

	private VideoInfo createVideoInstance() throws PhrescoException {
		VideoInfo videoInfo = new VideoInfo();
		if (StringUtils.isNotEmpty(getVideoId())) { 
			videoInfo.setId(getVideoId());
		}

		videoInfo.setName(getName());
		videoInfo.setDescription(getDescription());


		List<VideoType> videoList =new ArrayList<VideoType>();
		VideoType vType=new VideoType();

		ArtifactGroup artifactGroup = new ArtifactGroup();
		artifactGroup.setGroupId(getGroupId());
		artifactGroup.setArtifactId(getArtifactId());
		List<ArtifactInfo> artifactInfos =new ArrayList<ArtifactInfo>();
		ArtifactInfo artiInfo=new ArtifactInfo();
		artiInfo.setVersion(getVersion());
//    	long fileSize=8;
//    	artiInfo.setFileSize(fileSize);
		artifactInfos.add(artiInfo);
		artifactGroup.setVersions(artifactInfos);
		vType.setArtifactGroup(artifactGroup);
		videoList.add(vType);

		videoInfo.setVideoList(videoList);
		return videoInfo;

	}	 

	public String delete() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method PilotProjects.delete()");
		}
		try {
			String[] videoIds = getHttpRequest().getParameterValues("videoId");
			if (ArrayUtils.isNotEmpty(videoIds)) {
				for (String videoId : videoIds) {
					getServiceManager().deleteVideo(videoId);
				}
				addActionMessage(getText(VIDEO_DELETED));
			}
		}catch (PhrescoException e) {
			return showErrorPopup(e, EXCEPTION_VIDEO_DELETE);
		}

		return list();
	} 

	public String uploadFile() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method Videos.uploadFile()");
		}
		String type = getHttpRequest().getParameter(REQ_VIDEO_FILE_TYPE);
		if(REQ_VIDEO_UPLOAD.equals(type)) {
			s_videoByteArray = getByteArray();
		} else {
			s_imgByteArray = getByteArray();
		}

		return SUCCESS;
	}

	private byte[] getByteArray() throws PhrescoException {
		PrintWriter writer = null;
		byte[] byteArray = null;
		try {
			writer = getHttpResponse().getWriter();
			InputStream is = getHttpRequest().getInputStream();
			byteArray  = IOUtils.toByteArray(is);
			getHttpResponse().setStatus(getHttpResponse().SC_OK);
			writer.print(SUCCESS_TRUE);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			getHttpResponse().setStatus(getHttpResponse().SC_INTERNAL_SERVER_ERROR);
			writer.print(SUCCESS_FALSE);
			throw new PhrescoException(e);
		}

		return byteArray;
	}

	public void removeUploadedFile() {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method Archetypes.removeUploadedJar()");
		}

		String type = getHttpRequest().getParameter(REQ_VIDEO_FILE_TYPE);
		if (REQ_VIDEO_UPLOAD.equals(type)) {
			s_videoByteArray = null;
		} else {
			s_imgByteArray = null;
		}
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
		} 
		//empty validation for fileupload
		if (s_videoByteArray == null) {
			setVideoError(getText(KEY_I18N_ERR_VIDEO_EMPTY));
			isError = true;
		}

		if (s_imgByteArray == null) {
			setImgError(getText(KEY_I18N_ERR_IMAGE_EMPTY));
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getVideoError() {
		return videoError;
	}

	public void setVideoError(String videoError) {
		this.videoError = videoError;
	}

	public String getImgError() {
		return imgError;
	}

	public void setImgError(String imgError) {
		this.imgError = imgError;
	}

	public Boolean getErrorFound() {
		return errorFound;
	}

	public void setErrorFound(Boolean errorFound) {
		this.errorFound = errorFound;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getVideoId() {
		return videoId;
	}
}