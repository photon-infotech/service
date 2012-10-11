package com.photon.phresco.service.rest.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ProjectInfo;
import com.photon.phresco.commons.model.TechnologyInfo;
import com.photon.phresco.exception.PhrescoException;

public class PhrescoServiceTest {

	@Test
	public void testCreateProject() throws PhrescoException, IOException {
		ProjectService service = new ProjectService();
		service.createProject(createApplicationInfo());
	}
	
	private ProjectInfo createApplicationInfo() {
		ProjectInfo projectInfo = new ProjectInfo();
		projectInfo.setNoOfApps(3);
		projectInfo.setVersion("1.0");
		projectInfo.setProjectCode("PHR_sampleproject");
		List<ApplicationInfo> appInfos = new ArrayList<ApplicationInfo>();
		appInfos.add(createAppInfo("Test1", "tech-php"));
		appInfos.add(createAppInfo("Test2", "tech-php"));
		appInfos.add(createAppInfo("Test3", "tech-php"));
		projectInfo.setAppInfos(appInfos);
		return projectInfo;
	}
	
	private ApplicationInfo createAppInfo(String dirName, String techId) {
		ApplicationInfo applicationInfo = new ApplicationInfo();
		applicationInfo.setId("PHR_Test");
		List<String> customerIds = new ArrayList<String>();
		customerIds.add("photon");
		applicationInfo.setCustomerIds(customerIds);
		List<String> selectedModules = new ArrayList<String>();
		selectedModules.add("mod_weather_tech_php1.0");
		selectedModules.add("mod_commenting_system._tech_php1.0");
		selectedModules.add("mod_reportgenerator_tech_php1.0");
		applicationInfo.setSelectedModules(selectedModules);
		List<String> selectedWebservices = new ArrayList<String>();
		selectedWebservices.add("restjson");
		applicationInfo.setSelectedWebservices(selectedWebservices);
		TechnologyInfo techInfo = new TechnologyInfo();
		techInfo.setVersion(techId);
		applicationInfo.setTechInfo(techInfo);
		applicationInfo.setAppDirName(dirName);
		return applicationInfo;
	}
	
}
