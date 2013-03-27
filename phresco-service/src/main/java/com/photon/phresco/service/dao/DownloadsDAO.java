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

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.photon.phresco.commons.model.DownloadInfo.Category;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DownloadsDAO extends CustomerBaseDAO {
	
	private static final long serialVersionUID = 1L;
	private List<String> appliesToTechIds;
	private List<String> platformTypeIds;
	private Category category;
	private String artifactGroupId;
	
	public DownloadsDAO() {
		
	}
	
	public List<String> getAppliesToTechIds() {
		return appliesToTechIds;
	}
	
	public void setAppliesToTechIds(List<String> appliesToTechIds) {
		this.appliesToTechIds = appliesToTechIds;
	}
	
	public List<String> getPlatformTypeIds() {
		return platformTypeIds;
	}
	
	public void setPlatform(List<String> platformTypeIds) {
		this.platformTypeIds = platformTypeIds;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public String getArtifactGroupId() {
		return artifactGroupId;
	}
	
	public void setArtifactGroupId(String artifactGroupId) {
		this.artifactGroupId = artifactGroupId;
	}
	
}
