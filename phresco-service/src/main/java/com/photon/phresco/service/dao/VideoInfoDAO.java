package com.photon.phresco.service.dao;

import java.util.List;

import com.photon.phresco.commons.model.VideoType;

public class VideoInfoDAO extends CustomerBaseDAO {
	
	private String imageurl;
	private String videoListId;
	
	public String getImageurl() {
		return imageurl;
	}
	
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	
	public String getVideoListId() {
		return videoListId;
	}
	
	public void setVideoListId(String videoListId) {
		this.videoListId = videoListId;
	}

}
