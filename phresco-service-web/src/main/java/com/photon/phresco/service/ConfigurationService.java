/*
 * ###
 * Service Web Archive
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
package com.photon.phresco.service;

import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.photon.phresco.commons.model.SettingsTemplate;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.util.ServerConstants;
import com.photon.phresco.util.ServiceConstants;

/**
 * Example resource class hosted at the URI path "/apps"
 */
@Path("/settings")
public class ConfigurationService implements ServerConstants {
	private static final Logger S_LOGGER = Logger.getLogger(ConfigurationService.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<SettingsTemplate> getSettingsTemplates(@QueryParam(ServiceConstants.REST_QUERY_CUSTOMERID) String customerId) throws PhrescoException, JSONException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method ConfigurationService.getSettingsTemplates()");
		}
		PhrescoServerFactory.initialize();
		RepositoryManager repoManager = PhrescoServerFactory.getRepositoryManager();
		String settingsJson = repoManager.getArtifactAsString(repoManager.getSettingConfigFile(), customerId);
		Gson gson = new Gson();
		Type type = new TypeToken<List<SettingsTemplate>>(){}.getType();
		return (List<SettingsTemplate>)gson.fromJson(settingsJson, type);
	}
	
}
