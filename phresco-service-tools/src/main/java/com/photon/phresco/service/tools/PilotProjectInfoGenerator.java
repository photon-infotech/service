package com.photon.phresco.service.tools;

import java.util.ArrayList;
import java.util.List;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.service.api.DbService;
import com.photon.phresco.util.TechnologyTypes;

public class PilotProjectInfoGenerator extends DbService {
	
	private String[] versionNumber = new String[] {"2.0.0.12000", "2.0.0.16000"};
	
	public PilotProjectInfoGenerator() {
		super();
	}
	
	
	private ApplicationInfo createApplicationInfo(String id, String name, String code, String version, 
			String description, boolean emailSupported) {
		ApplicationInfo appInfo = new ApplicationInfo();
		List<String> customerIds = new ArrayList<String>();
		appInfo.setId(id);
		appInfo.setCode(code);
		appInfo.setCustomerIds(customerIds);
		appInfo.setDescription(description);
		appInfo.setEmailSupported(emailSupported);
		appInfo.setName(name);
		appInfo.setSystem(true);
		appInfo.setVersion(version);
		return appInfo;
	}
	
	private void createAndroidHybridPiots() {
		createAndroidHybridEshop();
		createAndroidHybridHelloWorld();
	}
	
	private void createAndroidHybridHelloWorld() {
		ApplicationInfo applicationInfo = createApplicationInfo("PHR_androidhybrid", "HelloWorld", "PHR_androidhybrid", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.ANDROID_HYBRID, "PHR_androidhybrid"));
		applicationInfo.setSelectedModules(createSelectedModules(new String[] {"mod_phonegap_tech_android_hybrid1.2.0"}));
	}

	private void createAndroidHybridEshop() {
		ApplicationInfo applicationInfo = createApplicationInfo("PHR_androidhybrid", "EShop", "PHR_androidhybrid", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.ANDROID_HYBRID, "PHR_androidhybrid"));
		applicationInfo.setSelectedDatabases(createSelectedDatabases("58adb08c-cab1-4839-82c5-4684ae5179ed"));
		applicationInfo.setSelectedModules(createSelectedModules(new String[] {"mod_phonegap_tech_android_hybrid1.2.0"}));
	}

	private List<String> createSelectedModules(String[] strings) {
		List<String> selectedModules = new ArrayList<String>();
		for (String string : strings) {
			selectedModules.add(string);
		}
		return selectedModules;
	}


	private List<String> createSelectedDatabases(String id) {
		List<String> databaseList = new ArrayList<String>();
		databaseList.add(id);
		return databaseList;
	}


	private ArtifactGroup createPilotContent(String id, String piloContentId) {
		ArtifactGroup artifactGroup = new ArtifactGroup();
		artifactGroup.setId(piloContentId);
		artifactGroup.setGroupId("pilots");
		artifactGroup.setArtifactId(id);
		artifactGroup.setVersions(createVersions());
		return artifactGroup;
	}


	private List<ArtifactInfo> createVersions() {
		List<ArtifactInfo> infos = new ArrayList<ArtifactInfo>();
		for (String version : versionNumber) {
			ArtifactInfo info = new ArtifactInfo();
			info.setVersion(version);
			infos.add(info);
		}
		return infos;
	}


	private void publish() {
		createAndroidHybridPiots();
		createAndroidNativePilots();
	}
	
	private void createAndroidNativePilots() {
		createAndroidNativeEshop();
		
	}

	private void createAndroidNativeEshop() {
		ApplicationInfo applicationInfo = createApplicationInfo("PHR_androidnative", "EShop", "PHR_androidnative", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.ANDROID_NATIVE, "PHR_androidnative"));
		applicationInfo.setSelectedModules(createSelectedModules(new String[] {"mod_phonegap_tech_android_hybrid1.2.0"}));
	}


	public static void main(String[] args) {
		PilotProjectInfoGenerator generator = new PilotProjectInfoGenerator();
		generator.publish();
	}

}
