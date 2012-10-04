package com.photon.phresco.service.dao;

import java.util.List;

public class DownloadsDAO extends CustomerBaseDAO {
	
	private static final long serialVersionUID = 1L;
	private List<String> appliesToTechIds;
	private List<String> platformTypeIds;
	private String category;
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
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getArtifactGroupId() {
		return artifactGroupId;
	}
	
	public void setArtifactGroupId(String artifactGroupId) {
		this.artifactGroupId = artifactGroupId;
	}
	
}
