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

import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactGroupInfo;
import com.photon.phresco.commons.model.Element;
import com.photon.phresco.commons.model.TechnologyInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationInfoDAO extends CustomerBaseDAO {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String code;
    
	/**
     * 
     */
    private String version;
    
    /**
     * 
     */
    private TechnologyInfo techInfo;
    
    /**
     * 
     */
    private Element pilotInfo;
    
    /**
     * 
     */
    private List<String> selectedModules;
    
    /**
     * 
     */
    private List<String> selectedJSLibs;
    
    /**
     * 
     */
    private List<ArtifactGroup> selectedFrameworks;
    
    /**
     * 
     */
    private List<String> selectedComponents;
    
    /**
     * 
     */
    private List<ArtifactGroupInfo> selectedServers;
    
    /**
     * 
     */
    private List<ArtifactGroupInfo> selectedDatabases;
    
    /**
     * 
     */
    private List<String> selectedWebservices;
    
    /**
     * 
     */
    private boolean emailSupported;
    
    /**
     * 
     */
    private String artifactGroupId;
    
    /**
     * 
     */
    private boolean phoneEnabled;
    
    /**
     * 
     */
    private boolean tabletEnabled;
    
    /**
     * 
     */
    private boolean pilot;
    
    /**
     * 
     */
    private String functionalFramework;

	/**
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the techInfo
	 */
	public TechnologyInfo getTechInfo() {
		return this.techInfo;
	}

	/**
	 * @param techInfo the techInfo to set
	 */
	public void setTechInfo(TechnologyInfo techInfo) {
		this.techInfo = techInfo;
	}

	/**
	 * @return the pilotInfo
	 */
	public Element getPilotInfo() {
		return this.pilotInfo;
	}

	/**
	 * @param pilotInfo the pilotInfo to set
	 */
	public void setPilotInfo(Element pilotInfo) {
		this.pilotInfo = pilotInfo;
	}

	/**
	 * @return the selectedModules
	 */
	public List<String> getSelectedModules() {
		return this.selectedModules;
	}

	/**
	 * @param selectedModules the selectedModules to set
	 */
	public void setSelectedModules(List<String> selectedModules) {
		this.selectedModules = selectedModules;
	}

	/**
	 * @return the selectedJSLibs
	 */
	public List<String> getSelectedJSLibs() {
		return this.selectedJSLibs;
	}

	/**
	 * @param selectedJSLibs the selectedJSLibs to set
	 */
	public void setSelectedJSLibs(List<String> selectedJSLibs) {
		this.selectedJSLibs = selectedJSLibs;
	}

	/**
	 * @return the selectedFrameworks
	 */
	public List<ArtifactGroup> getSelectedFrameworks() {
		return this.selectedFrameworks;
	}

	/**
	 * @param selectedFrameworks the selectedFrameworks to set
	 */
	public void setSelectedFrameworks(List<ArtifactGroup> selectedFrameworks) {
		this.selectedFrameworks = selectedFrameworks;
	}

	/**
	 * @return the selectedComponents
	 */
	public List<String> getSelectedComponents() {
		return this.selectedComponents;
	}

	/**
	 * @param selectedComponents the selectedComponents to set
	 */
	public void setSelectedComponents(List<String> selectedComponents) {
		this.selectedComponents = selectedComponents;
	}

	/**
	 * @return the selectedServers
	 */
	public List<ArtifactGroupInfo> getSelectedServers() {
		return this.selectedServers;
	}

	/**
	 * @param selectedServers the selectedServers to set
	 */
	public void setSelectedServers(List<ArtifactGroupInfo> selectedServers) {
		this.selectedServers = selectedServers;
	}

	/**
	 * @return the selectedDatabases
	 */
	public List<ArtifactGroupInfo> getSelectedDatabases() {
		return this.selectedDatabases;
	}

	/**
	 * @param selectedDatabases the selectedDatabases to set
	 */
	public void setSelectedDatabases(List<ArtifactGroupInfo> selectedDatabases) {
		this.selectedDatabases = selectedDatabases;
	}

	/**
	 * @return the selectedWebservices
	 */
	public List<String> getSelectedWebservices() {
		return this.selectedWebservices;
	}

	/**
	 * @param selectedWebservices the selectedWebservices to set
	 */
	public void setSelectedWebservices(List<String> selectedWebservices) {
		this.selectedWebservices = selectedWebservices;
	}

	/**
	 * @return the emailSupported
	 */
	public boolean isEmailSupported() {
		return this.emailSupported;
	}

	/**
	 * @param emailSupported the emailSupported to set
	 */
	public void setEmailSupported(boolean emailSupported) {
		this.emailSupported = emailSupported;
	}

	/**
	 * @return the artifactGroupId
	 */
	public String getArtifactGroupId() {
		return this.artifactGroupId;
	}

	/**
	 * @param artifactGroupId the artifactGroupId to set
	 */
	public void setArtifactGroupId(String artifactGroupId) {
		this.artifactGroupId = artifactGroupId;
	}

	/**
	 * @return the phoneEnabled
	 */
	public boolean isPhoneEnabled() {
		return this.phoneEnabled;
	}

	/**
	 * @param phoneEnabled the phoneEnabled to set
	 */
	public void setPhoneEnabled(boolean phoneEnabled) {
		this.phoneEnabled = phoneEnabled;
	}

	/**
	 * @return the tabletEnabled
	 */
	public boolean isTabletEnabled() {
		return this.tabletEnabled;
	}

	/**
	 * @param tabletEnabled the tabletEnabled to set
	 */
	public void setTabletEnabled(boolean tabletEnabled) {
		this.tabletEnabled = tabletEnabled;
	}

	/**
	 * @return the pilot
	 */
	public boolean isPilot() {
		return this.pilot;
	}

	/**
	 * @param pilot the pilot to set
	 */
	public void setPilot(boolean pilot) {
		this.pilot = pilot;
	}

	/**
	 * @return the functionalFramework
	 */
	public String getFunctionalFramework() {
		return this.functionalFramework;
	}

	/**
	 * @param functionalFramework the functionalFramework to set
	 */
	public void setFunctionalFramework(String functionalFramework) {
		this.functionalFramework = functionalFramework;
	}
    
	
}