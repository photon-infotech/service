/**
 * Phresco Service
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
package com.photon.phresco.service.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Repository {
	
	private String id;
	private String name;
	private String resourceURI;
	
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

	@Override
	public String toString() {
		return "Repository [id=" + id + ", name=" + name + ", resourceURI="
				+ resourceURI + "]";
	}
}
