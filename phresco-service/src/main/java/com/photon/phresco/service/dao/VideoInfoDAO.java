package com.photon.phresco.service.dao;

import java.util.List;

import com.photon.phresco.commons.model.VideoType;

public class VideoInfoDAO extends CustomerBaseDAO {
	
	private String imageurl;
	private List<String> videoListId;
	
	public String getImageurl() {
		return imageurl;
	}
	
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	
	public List<String> getVideoListId() {
		return videoListId;
	}
	
	public void setVideoListId(List<String> videoListId) {
		this.videoListId = videoListId;
	}

}
