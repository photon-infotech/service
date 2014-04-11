/**
 * Phresco Service Root
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
package com.photon.phresco.service.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;

import com.photon.phresco.commons.model.ApplicationType;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.Technology;
import com.photon.phresco.commons.model.TechnologyOptions;
import com.photon.phresco.exception.AIException;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.converters.ConvertersFactory;
import com.photon.phresco.service.dao.ArtifactGroupDAO;
import com.photon.phresco.service.dao.TechnologyDAO;
import com.photon.phresco.service.impl.DbService;
import com.photon.phresco.util.ServiceConstants;
import com.photon.phresco.util.TechnologyTypes;

public class ApptypeGenerator extends DbService implements ServiceConstants {


    private static final String PHOTON = "photon";

    private final static String phpTechDoc = "PHP is a general-purpose server-side scripting language originally designed for web development to produce dynamic web pages. For this purpose, PHP code is embedded into the HTML source document and interpreted by a web server with a PHP processor module, which generates the web page document. It also has evolved to include a command-line interface capability and can be used in standalone graphical applications." +
            "PHP can be deployed on most web servers and as a standalone interpreter, on almost every operating system and platform free of charge.";

    private final static String drupalTechDoc = "Drupal is a general-purpose server-side scripting language originally designed for web development to produce dynamic web pages. For this purpose, PHP code is embedded into the HTML source document and interpreted by a web server with a PHP processor module, which generates the web page document. It also has evolved to include a command-line interface capability and can be used in standalone graphical applications." +
    "PHP can be deployed on most web servers and as a standalone interpreter, on almost every operating system and platform free of charge.";

    private final static String nodejsDoc = "nodejs is a general-purpose server-side scripting language originally designed for web development to produce dynamic web pages. For this purpose, PHP code is embedded into the HTML source document and interpreted by a web server with a PHP processor module, which generates the web page document. It also has evolved to include a command-line interface capability and can be used in standalone graphical applications." +
    "PHP can be deployed on most web servers and as a standalone interpreter, on almost every operating system and platform free of charge.";

    private final static String sharepointDoc = "Sharepoint is a general-purpose server-side scripting language originally designed for web development to produce dynamic web pages. For this purpose, PHP code is embedded into the HTML source document and interpreted by a web server with a PHP processor module, which generates the web page document. It also has evolved to include a command-line interface capability and can be used in standalone graphical applications." +
    "PHP can be deployed on most web servers and as a standalone interpreter, on almost every operating system and platform free of charge.";
    
    private Map<String, String> archetypeMap = new HashMap<String, String>();
    private Map<String, String> docMap = new HashMap<String, String>();
    private Map<String, ArtifactGroup> pluginMap = new HashMap<String, ArtifactGroup>();
    private Converter<ArtifactGroupDAO, ArtifactGroup> artifactConverter;
    
    private String[] artifactVersion = new String[]{"2.0.0.16000", "2.0.0.17000"};
    
    public ApptypeGenerator() throws PhrescoException {
        super();
        initArchetypeMap();
        initDocMap();
        initPluginAndDependencyMap();
        artifactConverter = (Converter<ArtifactGroupDAO, ArtifactGroup>) ConvertersFactory.getConverter(ArtifactGroupDAO.class);
    }
    
    private void initArchetypeMap() {
        archetypeMap.put(TechnologyTypes.PHP, "phresco-php-archetype");
        archetypeMap.put(TechnologyTypes.PHP_DRUPAL6, "phresco-drupal6-archetype");
        archetypeMap.put(TechnologyTypes.PHP_DRUPAL7, "phresco-drupal7-archetype");
        archetypeMap.put(TechnologyTypes.ANDROID_HYBRID, "phresco-android-hybrid-archetype");
        archetypeMap.put(TechnologyTypes.ANDROID_NATIVE, "phresco-android-native-archetype");
        archetypeMap.put(TechnologyTypes.DOT_NET, "phresco-dot-net-archetype");
        archetypeMap.put(TechnologyTypes.HTML5_JQUERY_MOBILE_WIDGET, "phresco-html5-jquery-archetype");
        archetypeMap.put(TechnologyTypes.HTML5_MOBILE_WIDGET, "phresco-html5-archetype");
        archetypeMap.put(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET, "phresco-html5-jquery-archetype");
        archetypeMap.put(TechnologyTypes.IPHONE_HYBRID, "phresco-iphone-hybrid-archetype");
        archetypeMap.put(TechnologyTypes.IPHONE_NATIVE, "phresco-iphone-native-archetype");
        archetypeMap.put(TechnologyTypes.JAVA_STANDALONE, "phresco-java-quickstart-archetype");
        archetypeMap.put(TechnologyTypes.JAVA_WEBSERVICE, "phresco-javawebservice-archetype");
        archetypeMap.put(TechnologyTypes.NODE_JS_WEBSERVICE, "phresco-nodejs-archetype");
        archetypeMap.put(TechnologyTypes.SHAREPOINT, "phresco-sharepoint-archetype");
        archetypeMap.put(TechnologyTypes.WORDPRESS, "phresco-wordpress-archetype");
        archetypeMap.put(TechnologyTypes.ANDROID_LIBRARY, "phresco-android-library-archetype");
        archetypeMap.put(TechnologyTypes.WIN_METRO, "phresco-windows8-archetype");
        archetypeMap.put(TechnologyTypes.HTML5_WIDGET, "phresco-html5-archetype");
    }
    
    private void initPluginAndDependencyMap() {
        pluginMap.put(TechnologyTypes.PHP, createGroup("com.photon.phresco.plugins", "php-maven-plugin"));
        pluginMap.put(TechnologyTypes.PHP_DRUPAL6, createGroup("com.photon.phresco.plugins", "drupal-maven-plugin"));
        pluginMap.put(TechnologyTypes.PHP_DRUPAL7, createGroup("com.photon.phresco.plugins", "drupal-maven-plugin"));
        pluginMap.put(TechnologyTypes.ANDROID_HYBRID, createGroup("com.photon.maven.plugins.android.generation2", "android-maven-plugin"));
        pluginMap.put(TechnologyTypes.ANDROID_NATIVE, createGroup("com.photon.maven.plugins.android.generation2", "android-maven-plugin"));
        pluginMap.put(TechnologyTypes.DOT_NET, createGroup("com.photon.phresco.plugins", "dotnet-maven-plugin"));
        pluginMap.put(TechnologyTypes.HTML5_JQUERY_MOBILE_WIDGET, createGroup("com.photon.phresco.plugins", "java-maven-plugin"));
        pluginMap.put(TechnologyTypes.HTML5_MOBILE_WIDGET, createGroup("com.photon.phresco.plugins", "java-maven-plugin"));
        pluginMap.put(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET, createGroup("com.photon.phresco.plugins", "java-maven-plugin"));
        pluginMap.put(TechnologyTypes.IPHONE_HYBRID, createGroup("com.photon.phresco.plugins.xcode", "xcode-maven-plugin"));
        pluginMap.put(TechnologyTypes.IPHONE_NATIVE, createGroup("com.photon.phresco.plugins.xcode", "xcode-maven-plugin"));
        pluginMap.put(TechnologyTypes.JAVA_STANDALONE, createGroup("com.photon.phresco.plugins", "java-maven-plugin"));
        pluginMap.put(TechnologyTypes.JAVA_WEBSERVICE, createGroup("com.photon.phresco.plugins", "java-maven-plugin"));
        pluginMap.put(TechnologyTypes.NODE_JS_WEBSERVICE, createGroup("com.photon.phresco.plugins", "nodejs-maven-plugin"));
        pluginMap.put(TechnologyTypes.SHAREPOINT, createGroup("com.photon.phresco.plugins", "sharepoint-maven-plugin"));
        pluginMap.put(TechnologyTypes.WORDPRESS, createGroup("com.photon.phresco.plugins", "wordpress-maven-plugin"));
        pluginMap.put(TechnologyTypes.WIN_METRO, createGroup("com.photon.phresco.plugins", "windows-phone-maven-plugin"));
        pluginMap.put(TechnologyTypes.HTML5_WIDGET, createGroup("com.photon.phresco.plugins", "java-maven-plugin"));
        pluginMap.put(TechnologyTypes.ANDROID_LIBRARY, createGroup("com.photon.maven.plugins.android.generation2", "android-maven-plugin"));
    }
    
    private ArtifactGroup createGroup(String groupId, String artifactId) {
        ArtifactGroup group = new ArtifactGroup();
        group.setGroupId(groupId);
        group.setArtifactId(artifactId);
        return group;
    }
    
    private void initDocMap() {
        docMap.put(TechnologyTypes.PHP, phpTechDoc);
        docMap.put(TechnologyTypes.PHP_DRUPAL6, drupalTechDoc);
        docMap.put(TechnologyTypes.PHP_DRUPAL7, drupalTechDoc);
        docMap.put(TechnologyTypes.NODE_JS_WEBSERVICE, nodejsDoc);
        docMap.put(TechnologyTypes.SHAREPOINT, sharepointDoc);
    }
    
    public void generateApptypes() throws PhrescoException {
        List<ApplicationType> applicationTypes = new ArrayList<ApplicationType>();
        applicationTypes.add(createApplicationType("apptype-webapp", "Web Application"));
        applicationTypes.add(createApplicationType("apptype-mobile", "Mobile Applications"));
        applicationTypes.add(createApplicationType("apptype-web-services", "Web Services"));
        
        mongoOperation.insertList(APPTYPES_COLLECTION_NAME , applicationTypes);
    }

    private ApplicationType createApplicationType(String id, String name) {
        ApplicationType applicationType = new ApplicationType();
        applicationType.setId(id);
        applicationType.setName(name);
        applicationType.setSystem(true);
        applicationType.setDescription(name);
        applicationType.setHelpText(name);
        applicationType.setCustomerIds(getDefaultCustomers());
        return applicationType;
    }

    public void createWebAppTechs() throws PhrescoException {
        List<Technology> techs = new ArrayList<Technology>();
        techs.add(createTechnology(TechnologyTypes.PHP, "PHP", new String[]{"5.4.x", "5.3.x", "5.2.x", "5.1.x", "5.0.x"}, "apptype-webapp"));
//        techs.add(createTechnology(TechnologyTypes.PHP_DRUPAL6, "Drupal6", new String[]{"6.3", "6.25", "6.19"}, "apptype-webapp"));
//        techs.add(createTechnology(TechnologyTypes.PHP_DRUPAL7, "Drupal7", new String[]{"7.8"},"apptype-webapp"));
//        techs.add(createTechnology(TechnologyTypes.SHAREPOINT, "Sharepoint", new String[]{"3.5", "3.0", "2.0"}, "apptype-webapp"));
//        techs.add(createTechnology(TechnologyTypes.HTML5_WIDGET, "HTML5 Multichannel YUI Widget", new String[]{"1.6", "1.5"}, "apptype-webapp"));
//        techs.add(createTechnology(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET, "HTML5 Multichannel JQuery Widget", new String[]{"1.6", "1.5"},"apptype-webapp"));
//        techs.add(createTechnology(TechnologyTypes.HTML5_JQUERY_MOBILE_WIDGET, "HTML5 JQuery Mobile Widget", new String[]{"1.6", "1.5"},"apptype-webapp"));
//        techs.add(createTechnology(TechnologyTypes.HTML5_MOBILE_WIDGET, "HTML5 YUI Mobile Widget", new String[]{"1.6", "1.5"},"apptype-webapp"));
//        techs.add(createTechnology(TechnologyTypes.DOT_NET, "ASP.NET", new String[]{"3.5", "3.0", "2.0"},"apptype-webapp"));
//        techs.add(createTechnology(TechnologyTypes.WORDPRESS, "WordPress", new String[]{"3.3.1"},"apptype-webapp"));
//        techs.add(createTechnology(TechnologyTypes.JAVA_STANDALONE, "Java Standalone", new String[]{"1.6", "1.5"},"apptype-webapp"));
        
        saveTechnologies(techs);
    }

    public void createMobAppTechs() throws PhrescoException {
        List<Technology> techs = new ArrayList<Technology>();
        techs.add(createTechnology(TechnologyTypes.ANDROID_NATIVE, "Android Native", new String[]{"4.0.3", "2.3.3", "2.2"}, "apptype-mobile"));
        techs.add(createTechnology(TechnologyTypes.ANDROID_HYBRID, "Android Hybrid", new String[]{"4.0.3", "2.3.3", "2.2"}, "apptype-mobile"));
        techs.add(createTechnology(TechnologyTypes.IPHONE_NATIVE, "iPhone Native", new String[]{}, "apptype-mobile"));
        techs.add(createTechnology(TechnologyTypes.IPHONE_HYBRID, "iPhone Hybrid", new String[]{}, "apptype-mobile"));
        techs.add(createTechnology(TechnologyTypes.WIN_METRO, "Windows Metro", new String[]{}, "apptype-mobile"));
        techs.add(createTechnology(TechnologyTypes.ANDROID_LIBRARY, "Android Library", new String[]{"4.0.3", "2.3.3", "2.2"}, "apptype-mobile"));
        saveTechnologies(techs);
        
    }
    
    public void createWebServiceAppTechs() throws PhrescoException {
        List<Technology> techs = new ArrayList<Technology>();
        techs.add(createTechnology(TechnologyTypes.JAVA_WEBSERVICE, "Java Web Service", new String[]{"1.6", "1.5"}, "apptype-web-services"));
        techs.add(createTechnology(TechnologyTypes.NODE_JS_WEBSERVICE, "Node JS Web Service", new String[]{"6.14","6.11", "6.8","6.7", "6.1"}, "apptype-web-services"));
        saveTechnologies(techs);
    }
    
    public void publish() throws PhrescoException {
//        generateApptypes();
        createWebAppTechs();
//        createMobAppTechs();
//        createWebServiceAppTechs();
    }
    
    private Technology createTechnology(String id, String name, String[] versions, String appId) throws PhrescoException {
        Technology technology = new Technology();
        technology.setId(id);
        technology.setAppTypeId(appId);
        technology.setName(name);
        technology.setSystem(true);
        technology.setCustomerIds(getDefaultCustomers());
        technology.setDescription(docMap.get(id));
        technology.setHelpText(docMap.get(id));
        technology.setTechVersions(Arrays.asList(versions));
        technology.setArchetypeInfo(createArchetypeInfo(id, name));
        technology.setPlugins(createPlugins(id));
        technology.setOptions(createTechOptionsInit(id));
        return technology;
    }
    
    private List<TechnologyOptions> createTechOptionsInit(String techId) {
    	Map<String, String[]> techOptions = new HashMap<String, String[]>();
    	techOptions.put(TechnologyTypes.PHP, new String[]{"Code", "Build","Deploy", "Unit Test", "Functional Test", 
    			"Perfomance Test", "Load Test" , "Reports", "CI"});
    	techOptions.put(TechnologyTypes.PHP_DRUPAL7, new String[]{"Code", "Build","Deploy", "Unit Test", "Functional Test", 
    			"Perfomance Test", "Load Test" , "Reports", "CI"});
    	techOptions.put(TechnologyTypes.PHP_DRUPAL7, new String[]{"Code", "Build","Deploy", "Unit Test", "Functional Test", 
    			"Perfomance Test", "Load Test" , "Reports", "CI"});
    	techOptions.put(TechnologyTypes.ANDROID_HYBRID, new String[]{"Code", "Build","Deploy", "Unit Test", "Functional Test", 
    			"Perfomance Test", "Load Test" , "Reports", "CI"});
    	techOptions.put(TechnologyTypes.ANDROID_NATIVE, new String[]{"Code", "Build","Deploy", "Unit Test", "Functional Test", 
    			"Perfomance Test", "Load Test" , "Reports", "CI"});
    	techOptions.put(TechnologyTypes.IPHONE_NATIVE, new String[]{"Code", "Build","Deploy", "Unit Test", "Functional Test", 
    			"Perfomance Test", "Load Test" , "Reports", "CI"});
    	techOptions.put(TechnologyTypes.IPHONE_HYBRID, new String[]{"Code", "Build","Deploy", "Unit Test", "Functional Test", 
    			"Perfomance Test", "Load Test" , "Reports", "CI"});
    	techOptions.put(TechnologyTypes.HTML5_JQUERY_MOBILE_WIDGET, new String[]{"Code", "Build","Deploy", "Unit Test", "Functional Test", 
    			"Perfomance Test", "Load Test" , "Reports", "CI" , "Run Against Source"});
    	techOptions.put(TechnologyTypes.HTML5_MOBILE_WIDGET, new String[]{"Code", "Build","Deploy", "Unit Test", "Functional Test", 
    			"Perfomance Test", "Load Test" , "Reports", "CI" , "Run Against Source"});
    	techOptions.put(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET, new String[]{"Code", "Build","Deploy", "Unit Test", "Functional Test", 
    			"Perfomance Test", "Load Test" , "Reports", "CI" , "Run Against Source"});
    	techOptions.put(TechnologyTypes.HTML5_WIDGET, new String[]{"Code", "Build","Deploy", "Unit Test", "Functional Test", 
    			"Perfomance Test", "Load Test" , "Reports", "CI" , "Run Against Source"});
    	techOptions.put(TechnologyTypes.JAVA_WEBSERVICE, new String[]{"Code", "Build","Deploy", "Unit Test", "Functional Test", 
    			"Perfomance Test", "Load Test" , "Reports", "CI" , "Run Against Source"});
    	techOptions.put(TechnologyTypes.WORDPRESS, new String[]{"Code", "Build","Deploy", "Unit Test", "Functional Test", 
    			"Perfomance Test", "Load Test" , "Reports", "CI"});
    	techOptions.put(TechnologyTypes.SHAREPOINT, new String[]{"Code", "Build","Deploy", "Unit Test", "Functional Test", 
    			"Perfomance Test", "Load Test" , "Reports", "CI"});
    	techOptions.put(TechnologyTypes.DOT_NET, new String[]{"Code", "Build","Deploy", "Unit Test", "Functional Test", 
    			"Perfomance Test", "Load Test" , "Reports", "CI"});
    			
    	return createTechOptionList(techOptions.get(techId));
    }

    private List<TechnologyOptions> createTechOptionList(String[] strings) {
    	List<TechnologyOptions> techOptions = new ArrayList<TechnologyOptions>();
    	for (String option : strings) {
			TechnologyOptions options = new TechnologyOptions();
			options.setOption(option);
			techOptions.add(options);
		}
		return techOptions;
	}

	private ArtifactGroup createArchetypeInfo(String id, String name) throws PhrescoException {
        ArtifactGroup group = new ArtifactGroup();
        group.setGroupId("archetypes");
        group.setArtifactId(archetypeMap.get(id));
        group.setCustomerIds(getDefaultCustomers());
        group.setDescription(name + "Technology Archetype");
        group.setHelpText(name + "Technology Archetype");
        group.setName(name + "Technology Archetype");
        group.setSystem(true);
        List<ArtifactInfo> createArtifactInfo = createArtifactInfo(id);
        group.setVersions(createArtifactInfo);
        return group;
    }

    private List<ArtifactInfo> createArtifactInfo(String id) throws PhrescoException {
        List<ArtifactInfo> infos = new ArrayList<ArtifactInfo>();
        for (String version : artifactVersion) {
        	ArtifactInfo artifactInfo = new ArtifactInfo();
            artifactInfo.setSystem(true);
            artifactInfo.setVersion(version);
            infos.add(artifactInfo);
		}
        System.out.println("Returning Size" + infos.size());
        return infos;
    }

    private List<ArtifactGroup> createPlugins(String id) throws PhrescoException {
    	List<ArtifactGroup> groups = new ArrayList<ArtifactGroup>();
    	ArtifactGroup group = new ArtifactGroup();
    	ArtifactGroup artifactGroup = pluginMap.get(id);
    	group.setGroupId(artifactGroup.getGroupId());
    	group.setArtifactId(artifactGroup.getArtifactId());
    	group.setVersions(createArtifactInfo(id));
    	groups.add(group);
		return groups;
	}

	private List<String> getDefaultCustomers() {
        List<String> customerIds = new ArrayList<String>();
        customerIds.add(PHOTON);
        return customerIds;
    }
    
    private void saveTechnologies(List<Technology> techs) throws PhrescoException {
        for (Technology technology : techs) {
        	saveInfo(technology);
        	ArtifactGroup archetypeInfo = technology.getArchetypeInfo();
        	convertAndStrore(archetypeInfo);
        	List<ArtifactGroup> plugins = technology.getPlugins();
        	for (ArtifactGroup artifactGroup : plugins) {
        		System.out.println("Entered");
        		convertAndStrore(artifactGroup);
			}
		}
    }
    
    private void saveInfo(Technology technology) throws PhrescoException {
    	Converter<TechnologyDAO, Technology> techConverter = 
    		(Converter<TechnologyDAO, Technology>) ConvertersFactory.getConverter(TechnologyDAO.class);
    	TechnologyDAO tech = techConverter.convertObjectToDAO(technology);
    	TechnologyDAO techDAO = mongoOperation.findOne(TECHNOLOGIES_COLLECTION_NAME, 
    			new Query(Criteria.where("name").is(tech.getName())), TechnologyDAO.class);
    	if(techDAO == null) {
    		mongoOperation.save(TECHNOLOGIES_COLLECTION_NAME, tech);
    	} else {
    		List<String> pluginIds = techDAO.getPluginIds();
    		pluginIds.addAll(tech.getPluginIds());
    		techDAO.setPluginIds(pluginIds);
    		mongoOperation.save(TECHNOLOGIES_COLLECTION_NAME, techDAO);
    	}
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
        ApptypeGenerator generator = new ApptypeGenerator();
        generator.publish();
    }

}
