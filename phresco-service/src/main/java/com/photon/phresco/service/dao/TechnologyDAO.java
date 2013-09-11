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

@JsonIgnoreProperties(ignoreUnknown = true)
public class TechnologyDAO extends CustomerBaseDAO {
	
    private static final long serialVersionUID = 1L;
    
    private String appTypeId;
    private List<String> techVersions;
	private String archetypeGroupDAOId;
	private List<String> dependencyIds;
	private List<String> pluginIds;
	private List<String> options;
	private String techGroupId;
	private List<String> reports;
	private List<String> archetypeFeatures; 
	private List<String> applicableEmbedTechnology;
	private List<String> functionalFrameworks;
	
	public TechnologyDAO() {
		super();
	}

    public String getArchetypeGroupDAOId() {
        return this.archetypeGroupDAOId;
    }

    public void setArchetypeGroupDAOId(String archetypeGroupDAOId) {
        this.archetypeGroupDAOId = archetypeGroupDAOId;
    }

    public List<String> getDependencyIds() {
        return this.dependencyIds;
    }

    public String getAppTypeId() {
        return this.appTypeId;
    }

    public void setAppTypeId(String appTypeId) {
        this.appTypeId = appTypeId;
    }

    public List<String> getTechVersions() {
        return this.techVersions;
    }

    public void setTechVersions(List<String> techVersions) {
        this.techVersions = techVersions;
    }

    public void setDependencyIds(List<String> dependencyIds) {
        this.dependencyIds = dependencyIds;
    }

	public List<String> getPluginIds() {
		return this.pluginIds;
	}

	public void setPluginIds(List<String> pluginIds) {
		this.pluginIds = pluginIds;
	}

	public List<String> getOptions() {
		return this.options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public String getTechGroupId() {
		return this.techGroupId;
	}

	public void setTechGroupId(String techGroupId) {
		this.techGroupId = techGroupId;
	}
	
	public void setReports(List<String> reports) {
		this.reports = reports;
	}

	public List<String> getReports() {
		return this.reports;
	}

	public void setArchetypeFeatures(List<String> archetypeFeatures) {
		this.archetypeFeatures = archetypeFeatures;
	}

	public List<String> getArchetypeFeatures() {
		return this.archetypeFeatures;
	}

	public void setFunctionalFrameworks(List<String> functionalFrameworks) {
		this.functionalFrameworks = functionalFrameworks;
	}

	public List<String> getFunctionalFrameworks() {
		return this.functionalFrameworks;
	}

	public void setApplicableEmbedTechnology(
			List<String> applicableEmbedTechnology) {
		this.applicableEmbedTechnology = applicableEmbedTechnology;
	}

	public List<String> getApplicableEmbedTechnology() {
		return applicableEmbedTechnology;
	}
}
