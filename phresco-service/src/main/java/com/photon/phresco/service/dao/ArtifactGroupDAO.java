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
package com.photon.phresco.service.dao;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.CoreOption;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtifactGroupDAO extends CustomerBaseDAO {
    
    private static final long serialVersionUID = 1L;
   
    /**
     * 
     */
    private String groupId;
    
    /**
     * 
     */
    private String artifactId;
    
    /**
     * 
     */
    private String packaging;
    
    /**
     * 
     */
    private String classifier;
    
    /**
     * 
     */
    private ArtifactGroup.Type type;
    
    /**
     * 
     */
    private String imageURL;
    
    /**
     * 
     */
    private List<String> versionIds;
    
    /**
     * 
     */
    private List<CoreOption> appliesTo;
    
    /**
     * 
     */
    private String licenseId;

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return this.groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the artifactId
	 */
	public String getArtifactId() {
		return this.artifactId;
	}

	/**
	 * @param artifactId the artifactId to set
	 */
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	/**
	 * @return the packaging
	 */
	public String getPackaging() {
		return this.packaging;
	}

	/**
	 * @param packaging the packaging to set
	 */
	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	/**
	 * @return the classifier
	 */
	public String getClassifier() {
		return this.classifier;
	}

	/**
	 * @param classifier the classifier to set
	 */
	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}

	/**
	 * @return the type
	 */
	public ArtifactGroup.Type getType() {
		return this.type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ArtifactGroup.Type type) {
		this.type = type;
	}

	/**
	 * @return the imageURL
	 */
	public String getImageURL() {
		return this.imageURL;
	}

	/**
	 * @param imageURL the imageURL to set
	 */
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	/**
	 * @return the versionIds
	 */
	public List<String> getVersionIds() {
		return this.versionIds;
	}

	/**
	 * @param versionIds the versionIds to set
	 */
	public void setVersionIds(List<String> versionIds) {
		this.versionIds = versionIds;
	}

	/**
	 * @return the appliesTo
	 */
	public List<CoreOption> getAppliesTo() {
		return this.appliesTo;
	}

	/**
	 * @param appliesTo the appliesTo to set
	 */
	public void setAppliesTo(List<CoreOption> appliesTo) {
		this.appliesTo = appliesTo;
	}

	/**
	 * @return the licenseId
	 */
	public String getLicenseId() {
		return this.licenseId;
	}

	/**
	 * @param licenseId the licenseId to set
	 */
	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}
    
    
}
