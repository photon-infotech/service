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
package com.photon.phresco.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.log4j.Logger;

import com.photon.phresco.commons.model.RepoInfo;
import com.photon.phresco.commons.model.VideoInfo;
import com.photon.phresco.commons.model.VideoType;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.DbManager;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.util.MAGICNUMBER;
import com.photon.phresco.service.util.ServerConstants;
import com.photon.phresco.util.Constants;
import com.photon.phresco.util.ServiceConstants;
import com.photon.phresco.util.Utility;

public class VideoDownloader extends Thread implements ServerConstants {

	private static final String VIDEO_FOLDER = "/webapps";
	private static final Logger S_LOGGER = Logger.getLogger(VideoDownloader.class);
	private static Boolean debugEnabled = S_LOGGER.isDebugEnabled();
	private String serverContext;
	private String repoUrl;
	
	public VideoDownloader(String serverContext) {
		this.serverContext = serverContext;
	}
	
	public void run()  {
		if (debugEnabled) {
			S_LOGGER.debug("Entering Method  VideoDownloader.run()");
		}
		try {
		    PhrescoServerFactory.initialize();
			downloadFiles();
		} catch (PhrescoException e) {
		}
		
	}

	private void downloadFiles() throws PhrescoException{
		PhrescoServerFactory.initialize();
		DbManager dbManager = PhrescoServerFactory.getDbManager();
		List<VideoInfo> videos = dbManager.getVideos();
		downloadVideos(videos);
		downloadImageFiles(videos);
		
	}
	
	private void downloadVideos(List<VideoInfo> videoInfoList) throws PhrescoException {
	    for (VideoInfo videoinfo : videoInfoList) {
            List<VideoType> videoList = videoinfo.getVideoList();
            for (VideoType videoType : videoList) {
                saveFile(videoType.getUrl());
            }
        }
	}
	
	private void downloadImageFiles(List<VideoInfo> videoInfoList) throws PhrescoException {
	    for (VideoInfo videoinfo : videoInfoList) {
            String imgUrl = videoinfo.getImageurl();
            saveFile(imgUrl);
            }
	}
	
	private void saveFile(String videoURL) throws PhrescoException {
		InputStream in = null;
		FileOutputStream fos = null;
		try {
			URL url = new URL(getRepositoryUrl() + videoURL);
			URLConnection connection = url.openConnection();
			in = connection.getInputStream();
			int index = videoURL.lastIndexOf(Constants.SLASH);
			String fileName = videoURL.substring(index + 1);
			String filePathStr = videoURL.substring(0, index);
			File filePath = new File("../" + VIDEO_FOLDER + serverContext + filePathStr);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			File videoFile = new File(filePath, fileName);
			if(!videoFile.exists()){
			fos = new FileOutputStream(videoFile);
			byte[] buf = new byte[MAGICNUMBER.BYTELARSIZE];
			while (true) {
				int len;
				len = in.read(buf);
				if (len == -1) {
					break;
				}
				fos.write(buf, 0, len);
			}
			}
		} catch (Exception e) {
			throw new PhrescoException(e);
		} finally {
			Utility.closeStream(in);
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					throw new PhrescoException(e);
				}
			}
		}
	}
	
	private String getRepositoryUrl() throws PhrescoException {
		DbManager dbManager = PhrescoServerFactory.getDbManager();
	    RepoInfo repoInfo = dbManager.getRepoInfo(ServiceConstants.DEFAULT_CUSTOMER_NAME);
	    String repoURL = "";
	    if(repoInfo == null) {
			repoInfo = dbManager.getRepoInfoById(ServiceConstants.VIDEO_REPO_ID);
			repoURL = repoInfo.getReleaseRepoURL();
		} else {
			repoURL = repoInfo.getGroupRepoURL();
		}
        return repoURL;
	}
}
