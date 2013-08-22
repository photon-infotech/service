/**
 * Service Web Archive
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
package com.photon.phresco.service.rest.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroupInfo;
import com.photon.phresco.commons.model.ProjectInfo;
import com.photon.phresco.commons.model.TechnologyInfo;
import com.photon.phresco.commons.model.User.AuthType;
import com.photon.phresco.exception.PhrescoException;

public class PhrescoServiceTest {

	@Test
	public void testCreateProject() throws PhrescoException, IOException {
		ProjectService service = new ProjectService();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("userId", "admin");
		request.setParameter("authType", AuthType.LOCAL.name());
		request.setMethod("POST");
		request.setRemoteAddr("localhost");
		request.setRequestURI("/rest/api/create");
		request.setAuthType(AuthType.LOCAL.name());
		
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		byte[] createProject = service.createProject(httpServletRequest, createProjectInfo());
		Assert.assertNotNull(createProject);
	}
	
	
	@Test(expected=Exception.class)
	public void testFailCreateProject() throws PhrescoException, IOException {
		ProjectService service = new ProjectService();
		ProjectInfo pro = new ProjectInfo();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("userId", "testUser");
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		byte[] createProject = service.createProject(httpServletRequest, pro);
		Assert.assertNull(createProject);
	}
	
	@Test
	public void testUpdateProject() throws PhrescoException, IOException {
		ProjectService service = new ProjectService();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("userId", "admin");
		request.setParameter("authType", AuthType.LOCAL.name());
		request.setMethod("POST");
		request.setRemoteAddr("localhost");
		request.setRequestURI("/rest/api/create");
		request.setAuthType(AuthType.LOCAL.name());
		
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		ProjectInfo createProjectInfo = createProjectInfo();
		List<String> selectedWebservices = new ArrayList<String>();
		selectedWebservices.add("restjson");
		createProjectInfo.getAppInfos().get(0).setSelectedWebservices(selectedWebservices);
		byte[] updateProject = service.updateProject(httpServletRequest, createProjectInfo);
		Assert.assertNotNull(updateProject);
	}
	
	@Test(expected=Exception.class)
	public void testFailUpdateProject() throws PhrescoException, IOException {
		ProjectService service = new ProjectService();
		ProjectInfo info = new ProjectInfo();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("userId", "testUser");
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		byte[] updateProject = service.updateProject(httpServletRequest, info);
		Assert.assertNull(updateProject);
	}
	
	
	
	private ProjectInfo createProjectInfo() {
		ProjectInfo projectInfo = new ProjectInfo();
		projectInfo.setNoOfApps(1);
		projectInfo.setVersion("1.0");
		projectInfo.setCustomerIds(Collections.singletonList("photon"));
		projectInfo.setProjectCode("JqueryMobileEshop");
		List<ApplicationInfo> appInfos = new ArrayList<ApplicationInfo>();
		appInfos.add(createAppInfo());
		projectInfo.setAppInfos(appInfos);
		return projectInfo;
	}
	
	private ApplicationInfo createAppInfo() {
		ApplicationInfo applicationInfo = new ApplicationInfo();
		applicationInfo.setId("JqueryMobileEshop");
		List<String> customerIds = new ArrayList<String>();
		customerIds.add("photon");
		applicationInfo.setCustomerIds(customerIds);
		List<String> selectedModules = new ArrayList<String>();
		selectedModules.add("a69c6875-0bb0-462c-86d5-e361d02157cc");
		selectedModules.add("2d41a182-85f1-42a3-a67c-a0836792ba02");
		applicationInfo.setSelectedModules(selectedModules);
		
		List<String> selectedJslibs = new ArrayList<String>();
		selectedJslibs.add("c4a8d772-305e-441a-993e-703e63795aac");
		selectedJslibs.add("c7008489-b264-442c-ad8c-2c422284d171");
		selectedJslibs.add("bb9b5d04-2afe-4722-b87b-c1d9cdefbf8e");
		selectedJslibs.add("ceb6006b-b7aa-4600-9cdb-d52f5ad724ff");
		selectedJslibs.add("6afdf1d3-80f0-44a5-a9f5-843ce3db7ea0");
		selectedJslibs.add("deda98f8-c350-47f1-8b22-a0816a695127");
		selectedJslibs.add("402ded74-e007-4cdf-8fe5-ee11ca01b7db");
		selectedJslibs.add("4f889fa1-fe7a-4dee-8ed8-fb95605dcc85");
		selectedJslibs.add("444cd5e9-6d16-4e38-8f95-c3f1d84f3c6e");
		applicationInfo.setSelectedJSLibs(selectedJslibs);
		
		ArtifactGroupInfo selectServer = new ArtifactGroupInfo();
		selectServer.setArtifactGroupId("downloads_apache-tomcat");
		selectServer.setArtifactInfoIds(Collections.singletonList("0e34ab53-1b9e-493d-aa72-6ecacddc5338"));
		selectServer.setId("d23855eb-4309-4193-aaa5-b0e3fcede3a1");
		applicationInfo.setSelectedServers(Collections.singletonList(selectServer));
		
		TechnologyInfo techInfo = new TechnologyInfo();
		techInfo.setVersion("3.10.3");
		techInfo.setId("tech-html5-jquery-mobile-widget");
		techInfo.setAppTypeId("web-layer");
		techInfo.setName("html5");
		applicationInfo.setTechInfo(techInfo);
		applicationInfo.setAppDirName("JqueryMobileEshop");
		applicationInfo.setCode("JqueryMobileEshop");
		applicationInfo.setVersion("1.0");
		return applicationInfo;
	}
	
}
