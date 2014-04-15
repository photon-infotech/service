/**
 * Service Web Archive
 *
 * Copyright (C) 1999-2014 Photon Infotech Inc.
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
package com.photon.phresco.service.admin.actions.admin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactGroup.Type;
import com.photon.phresco.commons.model.VideoInfo;
import com.photon.phresco.commons.model.VideoType;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.service.client.api.ServiceManager;
import com.photon.phresco.service.util.ServerUtil;
public class Videos extends ServiceBaseAction { 

	private static final long serialVersionUID = -3065717999492844302L;

	//	private static final Logger S_LOGGER = Logger.getLogger(Videos.class);
	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(Videos.class.getName());
	private static Boolean s_isDebugEnabled = LOGGER.isDebugEnabled();
	private static Map<String, InputStream> inputStreamMap = new HashMap<String, InputStream>();

	private static byte[] videoByteArray = null;
	private static byte[] imgByteArray = null;
	private static String videoName;

	private String name = "";
	private String description = "";
	private String videoId = "";
	private String videoArtiId = "";
	private String nameError = "";
	private String videoError = "";
	private String imgError = "";
	private String fromPage = "";
	private String extFileName="";	
	private String videoUrl = "";
	private String oldName = "";
	private InputStream fileInputStream;
	private String contentType;
	private int contentLength;
	private Boolean errorFound = false;

	public String list() throws PhrescoException {
		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.list : Entry");
		}
		try {
			List<VideoInfo> videoInfos = getServiceManager().getVideoInfos();
			setReqAttribute(REQ_VIDEO_INFO, videoInfos);
		} catch (PhrescoException e) {
			if(s_isDebugEnabled) {
				LOGGER.error("Videos.list", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			return showErrorPopup(e, getText(EXCEPTION_VIDEO_LIST));
		}

		inputStreamMap.clear();
		videoByteArray = null;
		imgByteArray = null;

		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.list : Exit");
		}
		return ADMIN_VIDEO_LIST;	
	}

	public String add() {
		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.add : Entry");
		}
		setReqAttribute(REQ_FROM_PAGE, ADD);

		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.add : Exit");
		}

		return ADMIN_VIDEO_ADD;
	}

	public String edit() throws PhrescoException {
		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.edit : Entry");
		}

		try {
			if(s_isDebugEnabled) {
				if (StringUtils.isEmpty(getVideoId())) {
					LOGGER.warn("Videos.edit", "status=\"Bad Request\"", "message=\"Video Info is empty\"");
					return showErrorPopup(new PhrescoException("videoInfo is Empty"), getText(EXCEPTION_VIDEO_EDIT));
				}
				LOGGER.info("Videos.edit", "videoId=" + "\"" + getVideoId());
			}
			ServiceManager serviceManager = getServiceManager();
			VideoInfo videoInfo = serviceManager.getVideo(getVideoId());
			setReqAttribute(REQ_VIDEO_INFO, videoInfo);
			setReqAttribute(REQ_FROM_PAGE, EDIT);
		} catch (PhrescoException e) {
			if(s_isDebugEnabled) {
				LOGGER.error("Videos.edit", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			return showErrorPopup(e, getText(EXCEPTION_VIDEO_EDIT));
		}

		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.edit : Exit");
		}

		return ADMIN_VIDEO_ADD;
	}
	public String save() throws PhrescoException {
		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.save : Entry");
		}

		try {
			VideoInfo videoInfo = createVideoInstance();
			if(videoByteArray != null){
				inputStreamMap.put(videoInfo.getName(),  new ByteArrayInputStream(videoByteArray));
			} 
			if(imgByteArray != null){
				inputStreamMap.put(Type.ICON.name(),  new ByteArrayInputStream(imgByteArray));
			} 

//			getServiceManager().createVideos(createVideoInstance(), inputStreamMap);
			addActionMessage(getText(VIDEO_ADDED, Collections.singletonList(getName())));
		} catch (PhrescoException e) {
			if(s_isDebugEnabled) {
				LOGGER.error("Videos.save", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			return showErrorPopup(e, getText(EXCEPTION_VIDEO_SAVE));
		}

		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.save : Exit");
		}

		return list();
	}

	public String update() throws PhrescoException {
		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.update : Entry");
		}

		try {
			if (s_isDebugEnabled) {
				if (StringUtils.isEmpty(getVideoId())) {
					LOGGER.warn("Videos.update", "status=\"Bad Request\"", "message=\"Video Id is empty\"");
					return showErrorPopup(new PhrescoException("Video Id is empty"), getText(EXCEPTION_VIDEO_UPDATE));
				}
				LOGGER.info("Videos.update", "videoId=" + "\"" + getVideoId());
			}
			VideoInfo videoInfo = createVideoInstance();
			if(videoByteArray != null){
				inputStreamMap.put(videoInfo.getName(),  new ByteArrayInputStream(videoByteArray));
			} 
			if(imgByteArray != null){
				inputStreamMap.put(videoInfo.getName(),  new ByteArrayInputStream(imgByteArray));
			} 
//			getServiceManager().updateVideo(createVideoInstance(), inputStreamMap, getVideoId());
			addActionMessage(getText(PLTPROJ_UPDATED, Collections.singletonList(getName())));
		} catch (PhrescoException e) {
			if(s_isDebugEnabled) {
				LOGGER.error("Videos.update", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			return showErrorPopup(e, getText(EXCEPTION_VIDEO_UPDATE));
		}

		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.update : Exit");
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
		VideoType videoType = new VideoType();
		ArtifactGroup artifactGroup = new ArtifactGroup();
		artifactGroup.setName(getName());
		if(StringUtils.isNotEmpty(getVideoArtiId())) {
			artifactGroup.setId(getVideoArtiId());
		}
		artifactGroup.setPackaging(ServerUtil.getFileExtension(videoName));
		videoType.setArtifactGroup(artifactGroup);
		videoInfo.setVideoList(Arrays.asList(videoType));
		return videoInfo;
	}	 

	public String delete() throws PhrescoException {
		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.delete : Entry");
		}

		try {
			String[] videoIds = getHttpRequest().getParameterValues("videoId");
			if (s_isDebugEnabled) {
				if (videoIds == null) {
					LOGGER.warn("Videos.delete", "status=\"Bad Request\"", "message=\"VideoIds is empty\"");
					return showErrorPopup(new PhrescoException("VideoIds is empty"), getText(VIDEO_DELETED));
				}
				LOGGER.info("Videos.delete", "videoIds=" + "\"" + videoIds);
			}
			if (ArrayUtils.isNotEmpty(videoIds)) {
				for (String videoid : videoIds) {
					getServiceManager().deleteVideo(videoid);
				}
				addActionMessage(getText(VIDEO_DELETED));
			}
		}catch (PhrescoException e) {
			if(s_isDebugEnabled) {
				LOGGER.error("Videos.delete", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			return showErrorPopup(e, getText(EXCEPTION_VIDEO_DELETE));
		}

		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.delete : Exit");
		}
		return list();
	} 

	public String uploadFile() throws PhrescoException {
		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.uploadFile : Entry");
		}

		PrintWriter writer = null;
		try {
			writer = getHttpResponse().getWriter();
			String type = getReqParameter(REQ_VIDEO_FILE_TYPE);
			if (REQ_VIDEO_UPLOAD.equals(type)) {
				videoByteArray = getByteArray();
				videoName = getFileName();

			} else {
				imgByteArray = getFileByteArray();
			}
			writer.print(MAVEN_JAR_FALSE);
			getHttpResponse().setStatus(getHttpResponse().SC_OK);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			if(s_isDebugEnabled) {
				LOGGER.error("Videos.uploadFile", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			getHttpResponse().setStatus(getHttpResponse().SC_INTERNAL_SERVER_ERROR);
			writer.print(SUCCESS_FALSE);
		}

		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.uploadFile : Exit");
		}

		return SUCCESS;
	}

	private byte[] getFileByteArray() throws PhrescoException {
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
			LOGGER.debug("Videos.removeUploadedFile : Entry");
		}

		String type = getHttpRequest().getParameter(REQ_VIDEO_FILE_TYPE);
		if (REQ_VIDEO_UPLOAD.equals(type)) {
			videoByteArray = null;
		} else {
			imgByteArray = null;
		}
		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.removeUploadedFile : Exit");
		}
	}

	public String downloadVideo() throws PhrescoException {
		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.downloadVideo : Entry");
		}

		try {
			if (s_isDebugEnabled) {
				if (StringUtils.isEmpty(videoId)) {
					LOGGER.warn("Videos.downloadVideo", "status=\"Bad Request\"", "message=\"videoId is empty\"");
					return showErrorPopup(new PhrescoException("videoId is empty"), getText(DOWNLOAD_FAILED));
				}
				LOGGER.info("Videos.downloadVideo", "videoId=" + "\"" + videoId);
			}
			VideoInfo videoInfo = getServiceManager().getVideo(videoId);
			ArtifactGroup artifactGroup = videoInfo.getVideoList().get(0).getArtifactGroup();
			videoUrl = artifactGroup.getVersions().get(0).getDownloadURL();

			URL url = new URL(videoUrl);
			fileInputStream = url.openStream();
			String[] parts = videoUrl.split(FORWARD_SLASH);
			extFileName = parts[parts.length - 1];
			contentType = url.openConnection().getContentType();
			contentLength = url.openConnection().getContentLength();
		} catch(Exception e) {
			if(s_isDebugEnabled) {
				LOGGER.error("Videos.downloadVideo", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			return showErrorPopup(new PhrescoException(e), getText(DOWNLOAD_FAILED));
		}

		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.downloadVideo : Exit");
		}

		return SUCCESS;
	}

	public String validateForm() throws PhrescoException {
		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.validateForm : Entry");
		}

		boolean isError = false;
		//Empty validation for name
		if (StringUtils.isEmpty(getName())) {
			setNameError(getText(KEY_I18N_ERR_NAME_EMPTY ));
			isError = true;
		} else if (StringUtils.isEmpty(getFromPage()) || (!getName().equals(getOldName()))) {
			// to check duplication of name
			List<VideoInfo> videoInfos = getServiceManager().getVideoInfos();
			if (CollectionUtils.isNotEmpty(videoInfos)) {
				for (VideoInfo videoInfo : videoInfos) {
					if (videoInfo.getName().equalsIgnoreCase(getName())) {
						setNameError(getText(KEY_I18N_ERR_NAME_ALREADY_EXIST));
						isError = true;
						break;
					}
				}
			}
		}

		//empty validation for fileupload
		if (!EDIT.equals(getFromPage()) && videoByteArray == null) {
			setVideoError(getText(KEY_I18N_ERR_VIDEO_EMPTY));
			isError = true;
		}

		if (!EDIT.equals(getFromPage()) && imgByteArray == null) {
			setImgError(getText(KEY_I18N_ERR_IMAGE_EMPTY));
			isError = true;
		}

		if (isError) {
			setErrorFound(true);
		}

		if (s_isDebugEnabled) {
			LOGGER.debug("Videos.validateForm : Exit");
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

	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	public String getVideoArtiId() {
		return videoArtiId;
	}

	public void setVideoArtiId(String videoArtiId) {
		this.videoArtiId = videoArtiId;
	}

	public String getExtFileName() {
		return extFileName;
	}

	public void setExtFileName(String extFileName) {
		this.extFileName = extFileName;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
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

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
}