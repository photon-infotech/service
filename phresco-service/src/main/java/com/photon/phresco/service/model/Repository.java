package com.photon.phresco.service.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Repository {
	
	String id;
	String name;
	String resourceURI;
	
	public Repository() {
	}
	
	public Repository(String id, String name, String resourceURI) {
		this.id = id;
		this.name = name;
		this.resourceURI = resourceURI;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getResourceURI() {
		return resourceURI;
	}
	
	public void setResourceURI(String resourceURI) {
		this.resourceURI = resourceURI;
	}
}
