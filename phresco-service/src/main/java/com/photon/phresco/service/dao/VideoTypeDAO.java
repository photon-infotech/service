package com.photon.phresco.service.dao;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoTypeDAO extends CustomerBaseDAO {

	private static final long serialVersionUID = 1L;
	
	private String videoInfoId;
	private String url;
	private String artifactGroupId;
	private String type;
	private String codec;
	
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}
}
