/*
 * ###
 * Phresco Service
 * 
 * Copyright (C) 1999 - 2012 Photon Infotech Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ###
 */

package com.photon.phresco.service.dao;

import java.util.List;

public class TechnologyDAO extends CustomerBaseDAO {
	
    private static final long serialVersionUID = 1L;
    private String appTypeId;
    private List<String> techVersions;
	private String archetypeGroupDAOId;
	private List<String> dependencyIds;
	private List<String> pluginIds;
	
	public TechnologyDAO() {
		super();
	}

    public String getArchetypeGroupDAOId() {
        return archetypeGroupDAOId;
    }

    public void setArchetypeGroupDAOId(String archetypeGroupDAOId) {
        this.archetypeGroupDAOId = archetypeGroupDAOId;
    }

    public List<String> getDependencyIds() {
        return dependencyIds;
    }

    public String getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(String appTypeId) {
        this.appTypeId = appTypeId;
    }

    public List<String> getTechVersions() {
        return techVersions;
    }

    public void setTechVersions(List<String> techVersions) {
        this.techVersions = techVersions;
    }

    public void setDependencyIds(List<String> dependencyIds) {
        this.dependencyIds = dependencyIds;
    }

	public List<String> getPluginIds() {
		return pluginIds;
	}

	public void setPluginIds(List<String> pluginIds) {
		this.pluginIds = pluginIds;
	}

}
