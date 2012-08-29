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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.AdminConfigInfo;
import com.photon.phresco.model.LogInfo;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.util.ServerConstants;

/**
 * Admin config file and Log service"
 */
@Path("admin")
public class AdminService implements ServerConstants {

		private static final Logger S_LOGGER = Logger.getLogger(AdminService.class);
		private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
		@GET
		@Path("/config")
		@Produces({ MediaType.APPLICATION_JSON })
		public List<AdminConfigInfo> getProducts() throws PhrescoException {
		    try {
		        PhrescoServerFactory.initialize();
	            RepositoryManager repoMgr = PhrescoServerFactory.getRepositoryManager();
	            String config = repoMgr.getArtifactAsString(repoMgr.getAdminConfigFile(), "photon");
	            Gson gson = new Gson();
	            Type type = new TypeToken<List<AdminConfigInfo>>(){}.getType();
	            return gson.fromJson(config, type);
		    } catch (Exception e) {
                throw new PhrescoException(e);
            }
		}

		@POST
		@Path("/log")
		@Consumes(MediaType.APPLICATION_JSON)
		public void submitReport(LogInfo phrescoCrashReport) {
	    	if (isDebugEnabled) {
				S_LOGGER.debug("Entering Method AdminService.submitReport" + phrescoCrashReport.toString());
			}
		}
		
		public static void main(String[] args) throws PhrescoException {
			String key = "phresco.forum.url";
			String value = "http://172.16.18.86:7070/jforum";
			AdminConfigInfo adminConfigInfo = new AdminConfigInfo(key, value);
			List<AdminConfigInfo> list = new ArrayList<AdminConfigInfo>(1);
			list.add(adminConfigInfo);
			Gson gson = new Gson();
			String jsonAdminConfig = gson.toJson(list);
			File file = new File ("/Users/bharatkumarradha/work/admin_config.json");
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				fos.write(jsonAdminConfig.getBytes());
			} catch (FileNotFoundException e) {
			    throw new PhrescoException(e);
			} catch (IOException e) {
			    throw new PhrescoException(e);
			} finally {
				if (fos != null) {
					try {
						fos.flush();
						fos.close();
					} catch (IOException e) {
						throw new PhrescoException(e);
					}
				}
			}
			
		}
	}
