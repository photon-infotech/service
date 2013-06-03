/**
 * Phresco Service
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
package com.photon.phresco.service.dao;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoTypeDAO extends CustomerBaseDAO {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private String videoInfoId;
	
	/**
	 * 
	 */
	private String url;
	
	/**
	 * 
	 */
	private String artifactGroupId;
	
	/**
	 * 
	 */
	private String type;
	
	/**
	 * 
	 */
	private String codec;

	/**
	 * @return the videoInfoId
	 */
	public String getVideoInfoId() {
		return videoInfoId;
	}

	/**
	 * @param videoInfoId the videoInfoId to set
	 */
	public void setVideoInfoId(String videoInfoId) {
		this.videoInfoId = videoInfoId;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the artifactGroupId
	 */
	public String getArtifactGroupId() {
		return artifactGroupId;
	}

	/**
	 * @param artifactGroupId the artifactGroupId to set
	 */
	public void setArtifactGroupId(String artifactGroupId) {
		this.artifactGroupId = artifactGroupId;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the codec
	 */
	public String getCodec() {
		return codec;
	}

	/**
	 * @param codec the codec to set
	 */
	public void setCodec(String codec) {
		this.codec = codec;
	}
	
}