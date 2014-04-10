/**
 * Service Web Archive
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
package com.photon.phresco.service;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.photon.phresco.commons.model.DownloadInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.util.ServerConstants;
import com.photon.phresco.util.ServiceConstants;

public class DownloadsServiceTest implements ServerConstants{

	private static final String SOFTWARE_REPO_PATH = "/softwares/info/1.0/info-1.0.json";
	@Test
	public void testGetAvailableDownloads() throws PhrescoException {
    	PhrescoServerFactory.initialize();
    	RepositoryManager repoMgr = PhrescoServerFactory.getRepositoryManager();
		String downloadInfoJSON = repoMgr.getArtifactAsString(SOFTWARE_REPO_PATH, ServiceConstants.DEFAULT_CUSTOMER_NAME);
    	Type type = new TypeToken<List<DownloadInfo>>() {}.getType();
		Gson gson = new Gson();
		List<DownloadInfo> downloadInfoList = gson.fromJson(downloadInfoJSON, type);
    	assertNotNull(downloadInfoList);
	}

}
