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
	private String code;
    private String version;
    private TechnologyInfo techInfo;
    private Element pilotInfo;
    private List<String> selectedModules;
    private List<String> selectedJSLibs;
    private List<ArtifactGroup> selectedFrameworks;
    private List<String> selectedComponents;
    private List<ArtifactGroupInfo> selectedServers;
    private List<ArtifactGroupInfo> selectedDatabases;
    private List<String> selectedWebservices;
    private boolean emailSupported;
    private String artifactGroupId;
    private boolean phoneEnabled;
    private boolean tabletEnabled;
    private boolean pilot;
    private String functionalFramework;
    
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public TechnologyInfo getTechInfo() {
		return techInfo;
	}
	
	public void setTechInfo(TechnologyInfo techInfo) {
		this.techInfo = techInfo;
	}
	
	public Element getPilotInfo() {
		return pilotInfo;
	}
	
	public void setPilotInfo(Element pilotInfo) {
		this.pilotInfo = pilotInfo;
	}
	
	public List<String> getSelectedModules() {
		return selectedModules;
	}
	
	public void setSelectedModules(List<String> selectedModules) {
		this.selectedModules = selectedModules;
	}
	
	public List<String> getSelectedJSLibs() {
		return selectedJSLibs;
	}
	
	public void setSelectedJSLibs(List<String> selectedJSLibs) {
		this.selectedJSLibs = selectedJSLibs;
	}
	
	public List<ArtifactGroup> getSelectedFrameworks() {
		return selectedFrameworks;
	}
	
	public void setSelectedFrameworks(List<ArtifactGroup> selectedFrameworks) {
		this.selectedFrameworks = selectedFrameworks;
	}
	
	public List<String> getSelectedComponents() {
		return selectedComponents;
	}
	
	public void setSelectedComponents(List<String> selectedComponents) {
		this.selectedComponents = selectedComponents;
	}
	
	public List<ArtifactGroupInfo> getSelectedServers() {
		return selectedServers;
	}
	
	public void setSelectedServers(List<ArtifactGroupInfo> selectedServers) {
		this.selectedServers = selectedServers;
	}
	
	public List<ArtifactGroupInfo> getSelectedDatabases() {
		return selectedDatabases;
	}
	
	public void setSelectedDatabases(List<ArtifactGroupInfo> selectedDatabases) {
		this.selectedDatabases = selectedDatabases;
	}
	
	public List<String> getSelectedWebservices() {
		return selectedWebservices;
	}
	
	public void setSelectedWebservices(List<String> selectedWebservices) {
		this.selectedWebservices = selectedWebservices;
	}
	
	public boolean isEmailSupported() {
		return emailSupported;
	}
	
	public void setEmailSupported(boolean emailSupported) {
		this.emailSupported = emailSupported;
	}
	
	public String getArtifactGroupId() {
		return artifactGroupId;
	}
	
	public void setArtifactGroupId(String artifactGroupId) {
		this.artifactGroupId = artifactGroupId;
	}

    public boolean isPhoneEnabled() {
        return phoneEnabled;
    }

    public void setPhoneEnabled(boolean phoneEnabled) {
        this.phoneEnabled = phoneEnabled;
    }

    public boolean isTabletEnabled() {
        return tabletEnabled;
    }

    public void setTabletEnabled(boolean tabletEnabled) {
        this.tabletEnabled = tabletEnabled;
    }

	public void setPilot(boolean pilot) {
		this.pilot = pilot;
	}

	public boolean getPilot() {
		return pilot;
	}
	
	public void setFunctionalFramework(String functionalFrameworkId) {
		this.functionalFramework = functionalFrameworkId;
	}

	public String getFunctionalFramework() {
		return functionalFramework;
	}
}