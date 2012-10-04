package com.photon.phresco.service.dao;

import java.util.List;

import com.photon.phresco.commons.model.VideoType;

public class VideoTypeDAO extends CustomerBaseDAO {

	private static final long serialVersionUID = 1L;
	
	private String videoInfoId;
	private String url;
	private String artifactGroupId;
	
	public String getVideoInfoId() {
		return videoInfoId;
	}

	public void setVideoInfoId(String videoInfoId) {
		this.videoInfoId = videoInfoId;
	}

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getArtifactGroupId() {
		return artifactGroupId;
	}
	
	public void setArtifactGroupId(String artifactGroupId) {
		this.artifactGroupId = artifactGroupId;
	}
}
