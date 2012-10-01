package com.photon.phresco.service.tools;

import java.util.ArrayList;
import java.util.List;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.TechnologyInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.api.DbService;
import com.photon.phresco.service.converters.ConvertersFactory;
import com.photon.phresco.service.dao.ApplicationInfoDAO;
import com.photon.phresco.service.dao.ArtifactGroupDAO;
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
		customerIds.add("photon");
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
	
	private void createAndroidHybridPiots() throws PhrescoException {
		createAndroidHybridEshop();
		createAndroidHybridHelloWorld();
	}
	
	private void createAndroidHybridHelloWorld() throws PhrescoException {
		ApplicationInfo applicationInfo = createApplicationInfo("PHR_androidhybrid_helloworld", "HelloWorld", "PHR_androidhybrid_helloworld", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.ANDROID_HYBRID, "PHR_androidhybrid_helloworld"));
		applicationInfo.setSelectedModules(createSelectedIds(new String[] {"mod_phonegap_tech_android_hybrid1.2.0"}));
		saveApplicationInfo(applicationInfo);
	}

	private void createAndroidHybridEshop() throws PhrescoException {
		ApplicationInfo applicationInfo = createApplicationInfo("PHR_androidhybrid_eshop", "EShop", "PHR_androidhybrid_eshop", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.ANDROID_HYBRID, "PHR_androidhybrid_eshop"));
		applicationInfo.setSelectedDatabases(createSelectedDatabases("58adb08c-cab1-4839-82c5-4684ae5179ed"));
		applicationInfo.setSelectedModules(createSelectedIds(new String[] {"mod_phonegap_tech_android_hybrid1.2.0"}));
		saveApplicationInfo(applicationInfo);
	}

	private List<String> createSelectedIds(String[] strings) {
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


	private void publish() throws PhrescoException {
		createAndroidHybridPiots();
		createAndroidNativePilots();
		createPhpPilots();
		createDrupal7Pilots();
		createDrupal6Pilots();
		createSharepointPilots();
		createNodeJSPilots();
		createIphoneNativePilots();
		createIphoneHybridPilots();
		createHtml5JqueryMobilePilots();
	}
	
	private void createHtml5JqueryMobilePilots() {
		createHtml5JqueryMobileHello();
		createHtml5JqueryMobileEshop();
	}

	private void createHtml5JqueryMobileEshop() {
		ApplicationInfo applicationInfo = createApplicationInfo("PHR_h5_jquery_mobile_eshop", "Eshop", "PHR_h5_jquery_mobile_eshop", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.HTML5_JQUERY_MOBILE_WIDGET, "PHR_h5_jquery_mobile_eshop"));
		applicationInfo.setSelectedJSLibs(createSelectedIds(new String[]{"jslib_yui3", "jslib_jquery", "jslib_jsonpath"}));
		applicationInfo.setSelectedWebservices(createSelectedIds(new String[]{"restjson"}));
		applicationInfo.setSelectedServers(createSelectedIds(new String[]{"210ee46e-a9dd-4c29-a940-f12f3a24352f"}));
	}

	private void createHtml5JqueryMobileHello() {
		ApplicationInfo applicationInfo = createApplicationInfo("PHR_h5_jquery_mobile_helloworld", "HelloWorld", "PHR_h5_jquery_mobile_helloworld", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.HTML5_JQUERY_MOBILE_WIDGET, "PHR_h5_jquery_mobile_helloworld"));
		applicationInfo.setSelectedWebservices(createSelectedIds(new String[]{"restjson"}));
	}

	private void createIphoneHybridPilots() throws PhrescoException {
		createIphoneHybridHelloWorld();
		createIphoneHybridEshop();
	}

	private void createIphoneHybridEshop() throws PhrescoException {
		ApplicationInfo applicationInfo = createApplicationInfo("PHTN_iPhone_hybrid_eshop", "Eshop", "PHTN_iPhone_hybrid_eshop", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.IPHONE_HYBRID, "PHTN_iPhone_hybrid_eshop"));
		applicationInfo.setSelectedJSLibs(createSelectedIds(new String[]{"jslib_jquery"}));
		saveApplicationInfo(applicationInfo);
	}

	private void createIphoneHybridHelloWorld() throws PhrescoException {
		ApplicationInfo applicationInfo = createApplicationInfo("PHTN_iPhone_hybrid_helloworld", "HelloWorld", "PHTN_iPhone_hybrid_helloworld", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.IPHONE_HYBRID, "PHTN_iPhone_hybrid_helloworld"));
		saveApplicationInfo(applicationInfo);
	}

	private void createIphoneNativePilots() throws PhrescoException {
		createIphoneNativeHelloWorld();
		createIphoneNativeEshop();
	}

	private void createIphoneNativeEshop() throws PhrescoException {
		ApplicationInfo applicationInfo = createApplicationInfo("PHTN_iPhone_native_eshop", "Eshop", "PHTN_iPhone_native_eshop", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.IPHONE_NATIVE, "PHTN_iPhone_native_eshop"));
		applicationInfo.setSelectedModules(createSelectedIds(new String[]{"mod_sbjson_tech_iphone_hybrid2.3.2"}));
		applicationInfo.setSelectedWebservices(createSelectedIds(new String[]{"restjson"}));
		saveApplicationInfo(applicationInfo);
	}

	private void createIphoneNativeHelloWorld() throws PhrescoException {
		ApplicationInfo applicationInfo = createApplicationInfo("PHTN_iPhone_native_helloworld", "HelloWorld", "PHTN_iPhone_native_helloworld", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.NODE_JS_WEBSERVICE, "PHTN_iPhone_native_helloworld"));
		saveApplicationInfo(applicationInfo);
	}

	private void createNodeJSPilots() throws PhrescoException {
		createNodeJSHelloWorld();
		createNodeJSEshop();
	}

	private void createNodeJSEshop() throws PhrescoException {
		ApplicationInfo applicationInfo = createApplicationInfo("PHTN_NODEJS_eshop-WebService", "Eshop", "PHTN_NODEJS_eshop-WebService", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.NODE_JS_WEBSERVICE, "PHTN_NODEJS_eshop-WebService"));
		applicationInfo.setSelectedDatabases(createSelectedIds(new String[]{"853b6912-fb53-4269-89b2-577f11d025b9"}));
		applicationInfo.setSelectedServers(createSelectedIds(new String[]{"3b7b8d12-d998-4732-9615-96060d6e2628"}));
		applicationInfo.setSelectedModules(createSelectedIds(new String[]{"mod_connect_tech_nodejs_webservice1.7.2", "mod_express_tech_nodejs_webservice0.5.0", 
				"mod_formidable_tech_nodejs_webservice1.0.8", "mod_hashish_tech_nodejs_webservice0.0.4", "mod_hashring_tech_nodejs_webservice0.0.5",
				"mod_lingo_tech_nodejs_webservice0.0.4", "mod_mime_tech_nodejs_webservice1.2.4", "mod_moment_tech_nodejs_webservice1.1.2",
				"mod_mysql_tech_nodejs_webservice0.9.4", "mod_qs_tech_nodejs_webservice0.3.1","mod_sequelize_tech_nodejs_webservice1.2.1",
				"mod_traverse_tech_nodejs_webservice0.5.1","mod_underscore_tech_nodejs_webservice1.2.2", 
				"mod_underscore.string_tech_nodejs_webservice1.2.0", "mod_validator_tech_nodejs_webservice0.3.5"}));
		saveApplicationInfo(applicationInfo);
	}

	private void createNodeJSHelloWorld() throws PhrescoException {
		ApplicationInfo applicationInfo = createApplicationInfo("PHTN_NODEJS_Helloworld", "HelloWorld", "PHTN_NODEJS_Helloworld", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.NODE_JS_WEBSERVICE, "PHTN_NODEJS_Helloworld"));
		saveApplicationInfo(applicationInfo);
	}

	private void createSharepointPilots() throws PhrescoException {
		createSharepointResourceMgt();
		createSharepointHelloWorld();
	}

	private void createSharepointHelloWorld() throws PhrescoException {
		ApplicationInfo applicationInfo = createApplicationInfo("PHR_PilotSharePoint_Helloworld", "HelloWorld", "PHR_PilotSharePoint_Helloworld", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.SHAREPOINT, "PHR_PilotSharePoint_Helloworld"));
		saveApplicationInfo(applicationInfo);
	}

	private void createSharepointResourceMgt() throws PhrescoException {
		ApplicationInfo applicationInfo = createApplicationInfo("PHR_PilotSharePoint_Eshop", "Resource Management", "PHR_PilotSharePoint_Eshop", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.SHAREPOINT, "PHR_PilotSharePoint_Eshop"));
		applicationInfo.setSelectedModules(createSelectedIds(new String[]{"mod_fusioncharts_tech_sharepoint1.3", "mod_popupwebpart_tech_sharepoint1.0", 
				"mod_regularexpression_tech_sharepoint1.1", "mod_alertmanager_tech_sharepoint1.0", "mod_eventreceiver_tech_sharepoint1.0",
				"mod_listdefinition_tech_sharepoint1.0","mod_masterpage_tech_sharepoint1.0"}));
		saveApplicationInfo(applicationInfo);
	}

	private void createDrupal6Pilots() throws PhrescoException {
		ApplicationInfo applicationInfo = createApplicationInfo("PHR_Drupal6HelloWorld", "HelloWorld", "PHR_Drupal6HelloWorld", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.PHP_DRUPAL6, "PHR_Drupal6HelloWorld"));
		applicationInfo.setSelectedDatabases(createSelectedIds(new String[]{"853b6912-fb53-4269-89b2-577f11d025b9"}));
		applicationInfo.setSelectedServers(createSelectedIds(new String[]{"2ad3b32a-df3b-4997-b8b6-f4e9b0e27e5a"}));
		saveApplicationInfo(applicationInfo);
	}

	private void createDrupal7Pilots() throws PhrescoException {
		createDrupal7Eshop();
		createDrupal7HelloWorld();
	}

	private void createDrupal7HelloWorld() throws PhrescoException {
		ApplicationInfo applicationInfo = createApplicationInfo("PHR_Drupal7HelloWorld", "HelloWorld", "PHR_Drupal7HelloWorld", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.PHP_DRUPAL7, "PHR_Drupal7HelloWorld"));
		applicationInfo.setSelectedDatabases(createSelectedIds(new String[]{"853b6912-fb53-4269-89b2-577f11d025b9"}));
		applicationInfo.setSelectedServers(createSelectedIds(new String[]{"2ad3b32a-df3b-4997-b8b6-f4e9b0e27e5a"}));
		saveApplicationInfo(applicationInfo);
	}

	private void createDrupal7Eshop() throws PhrescoException {
		ApplicationInfo applicationInfo = createApplicationInfo("PHR_Drupal7Eshop", "Eshop", "PHR_Drupal7Eshop", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.PHP_DRUPAL7, "PHR_Drupal7Eshop"));
		applicationInfo.setSelectedModules(createSelectedIds(new String[]{"mod_chaos_tool_suite_tech_phpdru77.x-1.0-rc1", "mod_rules_tech_phpdru77.x-2.0-rc2",
				"mod_views_tech_phpdru66.x-2.16", "mod_entity_api_tech_phpdru77.x-1.0-rc1", "mod_taxonomy_manager_tech_phpdru77.x-1.0-beta2",
				"mod_taxonomy_subterms__tech_phpdru77.x-1.x-dev", "mod_ubercart_tech_phpdru77.x-3.0-rc2", "mod_shopmenu_tech_phpdru77.0"}));
		applicationInfo.setSelectedDatabases(createSelectedIds(new String[]{"853b6912-fb53-4269-89b2-577f11d025b9"}));
		applicationInfo.setSelectedServers(createSelectedIds(new String[]{"2ad3b32a-df3b-4997-b8b6-f4e9b0e27e5a"}));
		saveApplicationInfo(applicationInfo);
	}

	private void createPhpPilots() throws PhrescoException {
		createPhpEshop();
		createPhpHelloWorld();
	}

	private void createPhpHelloWorld() throws PhrescoException {
		ApplicationInfo applicationInfo = createApplicationInfo("PHR_PhpHelloWorld", "HelloWorld", "PHR_PhpHelloWorld", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.PHP, "PHR_PhpHelloWorld"));
		saveApplicationInfo(applicationInfo);
	}

	private void createPhpEshop() throws PhrescoException {
		ApplicationInfo applicationInfo = createApplicationInfo("PHR_phpblog", "Blog", "PHR_phpblog", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.PHP, "PHR_phpblog"));
		applicationInfo.setSelectedModules(createSelectedIds(new String[]{"mod_blog_tech_php1.0"}));
		applicationInfo.setSelectedDatabases(createSelectedIds(new String[]{"853b6912-fb53-4269-89b2-577f11d025b9"}));
		applicationInfo.setSelectedServers(createSelectedIds(new String[]{"2ad3b32a-df3b-4997-b8b6-f4e9b0e27e5a"}));
		saveApplicationInfo(applicationInfo);
	}

	private void createAndroidNativePilots() throws PhrescoException {
		createAndroidNativeEshop();
		createAndroidNativeHelloWorld();
	}

	private void createAndroidNativeEshop() throws PhrescoException {
		ApplicationInfo applicationInfo = createApplicationInfo("PHR_androidnative_eshop", "EShop", "PHR_androidnative_eshop", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.ANDROID_NATIVE, "PHR_androidnative_eshop"));
		applicationInfo.setSelectedModules(createSelectedIds(new String[] {"mod_phonegap_tech_android_hybrid1.2.0"}));
		applicationInfo.setSelectedWebservices(createSelectedIds(new String[]{"restjson"}));
		applicationInfo.setTechInfo(createTechInfo("PHR_androidnative_eshop", "2.3.3"));
		saveApplicationInfo(applicationInfo);
	}

	private void createAndroidNativeHelloWorld() throws PhrescoException {
		ApplicationInfo applicationInfo = createApplicationInfo("PHR_androidnative_helloworld", "HelloWorld", "PHR_androidnative_helloworld", "1.0.0", "", false);
		applicationInfo.setPilotContent(createPilotContent(TechnologyTypes.ANDROID_NATIVE, "PHR_androidnative_helloworld"));
		applicationInfo.setSelectedModules(createSelectedIds(new String[] {"mod_phonegap_tech_android_hybrid1.2.0"}));
		applicationInfo.setSelectedWebservices(createSelectedIds(new String[]{"restjson"}));
		applicationInfo.setTechInfo(createTechInfo("PHR_androidnative_helloworld", "2.3.3"));
		saveApplicationInfo(applicationInfo);
	}
	
	private TechnologyInfo createTechInfo(String appId, String version) {
		TechnologyInfo info = new TechnologyInfo();
		info.setAppTypeId(appId);
		info.setVersion(version);
		return info;
	}
	
	private void saveApplicationInfo(ApplicationInfo applicationInfo) throws PhrescoException {
		Converter<ApplicationInfoDAO, ApplicationInfo> appConverter = 
			(Converter<ApplicationInfoDAO, ApplicationInfo>) ConvertersFactory.getConverter(ApplicationInfoDAO.class);
		ApplicationInfoDAO applicationInfoDAO = appConverter.convertObjectToDAO(applicationInfo);
		mongoOperation.save(APPLICATION_INFO_COLLECTION_NAME, applicationInfoDAO);
		ArtifactGroup pilotContent = applicationInfo.getPilotContent();
		convertAndStrore(pilotContent);
	}
	
	private void convertAndStrore(ArtifactGroup artifactGroup) throws PhrescoException {
        List<ArtifactInfo> versions = artifactGroup.getVersions();
        List<String> versionIds = new ArrayList<String>();
        Converter<ArtifactGroupDAO, ArtifactGroup> converter = 
            (Converter<ArtifactGroupDAO, ArtifactGroup>) ConvertersFactory.getConverter(ArtifactGroupDAO.class);
        ArtifactGroupDAO moduleGroupDAO = converter.convertObjectToDAO(artifactGroup);
        String id2 = moduleGroupDAO.getId();
        for (ArtifactInfo module : versions) {
        	versionIds.add(module.getId());
            module.setArtifactGroupId(id2);
            mongoOperation.save(ARTIFACT_INFO_COLLECTION_NAME, module);
        }
        moduleGroupDAO.setVersionIds(versionIds);
      mongoOperation.save(ARTIFACT_GROUP_COLLECTION_NAME, moduleGroupDAO);
    }
	
	public static void main(String[] args) throws PhrescoException {
		PilotProjectInfoGenerator generator = new PilotProjectInfoGenerator();
		generator.publish();
	}
}
