/*
 * ###
 * Phresco Service Tools
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
/*******************************************************************************
 * Copyright (c) 2011 Photon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Photon Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.photon.in/legal/ppl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Photon - initial API and implementation
 ******************************************************************************/
package com.photon.phresco.service.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.photon.phresco.commons.model.ApplicationType;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.Technology;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.DbService;
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
    
//    public ServiceContext context = null;
//    public ServiceManager serviceManager = null;
    private Map<String, String> archetypeMap = new HashMap<String, String>();
    private Map<String, String> docMap = new HashMap<String, String>();
    private Map<String, ArtifactGroup> pluginMap = new HashMap<String, ArtifactGroup>();
    
    private final String[] artifactVersion = new String[]{"2.0.0.16000", "2.0.0.15000", "2.0.0.14000"};
    
    public ApptypeGenerator() throws PhrescoException {
        super();
        initArchetypeMap();
        initDocMap();
        initPluginAndDependencyMap();
//        context = new ServiceContext();
//        context.put(ServiceClientConstant.SERVICE_URL, "http://localhost:3030/service/rest/api");
//        context.put(ServiceClientConstant.SERVICE_USERNAME, "demouser");
//        context.put(ServiceClientConstant.SERVICE_PASSWORD, "phresco");
//        serviceManager = ServiceClientFactory.getServiceManager(context);
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
    }
    
    private void initPluginAndDependencyMap() {
        pluginMap.put(TechnologyTypes.PHP, new ArtifactGroup("com.photon.phresco.plugins", "php-maven-plugin"));
        pluginMap.put(TechnologyTypes.PHP_DRUPAL6, new ArtifactGroup("com.photon.phresco.plugins", "drupal-maven-plugin"));
        pluginMap.put(TechnologyTypes.PHP_DRUPAL7, new ArtifactGroup("com.photon.phresco.plugins", "drupal-maven-plugin"));
        pluginMap.put(TechnologyTypes.ANDROID_HYBRID, new ArtifactGroup("com.photon.maven.plugins.android.generation2", "android-maven-plugin"));
        pluginMap.put(TechnologyTypes.ANDROID_NATIVE, new ArtifactGroup("com.photon.maven.plugins.android.generation2", "android-maven-plugin"));
        pluginMap.put(TechnologyTypes.DOT_NET, new ArtifactGroup("com.photon.phresco.plugins", "dotnet-maven-plugin"));
        pluginMap.put(TechnologyTypes.HTML5_JQUERY_MOBILE_WIDGET, new ArtifactGroup("com.photon.phresco.plugins", "java-maven-plugin"));
        pluginMap.put(TechnologyTypes.HTML5_MOBILE_WIDGET, new ArtifactGroup("com.photon.phresco.plugins", "java-maven-plugin"));
        pluginMap.put(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET, new ArtifactGroup("com.photon.phresco.plugins", "java-maven-plugin"));
        pluginMap.put(TechnologyTypes.IPHONE_HYBRID, new ArtifactGroup("com.photon.phresco.plugins.xcode", "xcode-maven-plugin"));
        pluginMap.put(TechnologyTypes.IPHONE_NATIVE, new ArtifactGroup("com.photon.phresco.plugins.xcode", "xcode-maven-plugin"));
        pluginMap.put(TechnologyTypes.JAVA_STANDALONE, new ArtifactGroup("com.photon.phresco.plugins", "java-maven-plugin"));
        pluginMap.put(TechnologyTypes.JAVA_WEBSERVICE, new ArtifactGroup("com.photon.phresco.plugins", "java-maven-plugin"));
        pluginMap.put(TechnologyTypes.NODE_JS_WEBSERVICE, new ArtifactGroup("com.photon.phresco.plugins", "nodejs-maven-plugin"));
        pluginMap.put(TechnologyTypes.SHAREPOINT, new ArtifactGroup("com.photon.phresco.plugins", "sharepoint-maven-plugin"));
        pluginMap.put(TechnologyTypes.WORDPRESS, new ArtifactGroup("com.photon.phresco.plugins", "wordpress-maven-plugin"));
        pluginMap.put(TechnologyTypes.WIN_METRO, new ArtifactGroup("com.photon.phresco.plugins", "windows-phone-maven-plugin"));
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
//        RestClient<ApplicationType> applicationTypeClient = serviceManager.getRestClient(REST_API_COMPONENT + REST_API_APPTYPES);
//        ClientResponse response = applicationTypeClient.create(applicationTypes);
//        System.out.println(response.getStatus());
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
        techs.add(createTechnology(TechnologyTypes.PHP, "PHP", new String[]{"5.4.x", "5.3.x", "5.2.x", "5.1.x", "5.0.x"},"apptype-webapp"));
        techs.add(createTechnology(TechnologyTypes.PHP_DRUPAL6, "Drupal6", new String[]{"6.3", "6.25", "6.19"},"apptype-webapp"));
        techs.add(createTechnology(TechnologyTypes.PHP_DRUPAL7, "Drupal7", new String[]{"7.8"},"apptype-webapp"));
        techs.add(createTechnology(TechnologyTypes.SHAREPOINT, "Sharepoint", new String[]{"3.5", "3.0", "2.0"},"apptype-webapp"));
        techs.add(createTechnology(TechnologyTypes.HTML5_WIDGET, "HTML5 Multichannel YUI Widget", new String[]{"1.6", "1.5"},"apptype-webapp"));
        techs.add(createTechnology(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET, "HTML5 Multichannel JQuery Widget", new String[]{"1.6", "1.5"},"apptype-webapp"));
        techs.add(createTechnology(TechnologyTypes.HTML5_JQUERY_MOBILE_WIDGET, "HTML5 JQuery Mobile Widget", new String[]{"1.6", "1.5"},"apptype-webapp"));
        techs.add(createTechnology(TechnologyTypes.HTML5_MOBILE_WIDGET, "HTML5 YUI Mobile Widget", new String[]{"1.6", "1.5"},"apptype-webapp"));
        techs.add(createTechnology(TechnologyTypes.DOT_NET, "ASP.NET", new String[]{"3.5", "3.0", "2.0"},"apptype-webapp"));
        techs.add(createTechnology(TechnologyTypes.WORDPRESS, "WordPress", new String[]{"3.3.1"},"apptype-webapp"));
        techs.add(createTechnology(TechnologyTypes.JAVA_STANDALONE, "Java Standalone", new String[]{"1.6", "1.5"},"apptype-webapp"));
        
//        serviceManager = ServiceClientFactory.getServiceManager(context);
//        RestClient<Technology> techClient = serviceManager.getRestClient("/components/technologies");
//        techClient.queryString("techId", "apptype-webapp");
//        ClientResponse response = techClient.create(techs);
    }
    
    public void createMobAppTechs() throws PhrescoException {
        List<Technology> techs = new ArrayList<Technology>();
        techs.add(createTechnology(TechnologyTypes.ANDROID_NATIVE, "Android Native", new String[]{"4.0.3", "2.3.3", "2.2"}, "apptype-mobile"));
        techs.add(createTechnology(TechnologyTypes.ANDROID_HYBRID, "Android Hybrid", new String[]{"4.0.3", "2.3.3", "2.2"}, "apptype-mobile"));
        techs.add(createTechnology(TechnologyTypes.IPHONE_NATIVE, "iPhone Native", new String[]{}, "apptype-mobile"));
        techs.add(createTechnology(TechnologyTypes.IPHONE_HYBRID, "iPhone Hybrid", new String[]{}, "apptype-mobile"));
        techs.add(createTechnology(TechnologyTypes.WIN_METRO, "Windows Metro", new String[]{}, "apptype-mobile"));
        techs.add(createTechnology(TechnologyTypes.ANDROID_LIBRARY, "Android Library", new String[]{"4.0.3", "2.3.3", "2.2"}, "apptype-mobile"));
        
//        serviceManager = ServiceClientFactory.getServiceManager(context);
//        RestClient<Technology> techClient = serviceManager.getRestClient("/components/technologies");
//        techClient.queryString("techId", "apptype-mobile");
//        ClientResponse response = techClient.create(techs);
//        System.out.println("response " + response.getStatus());
        
    }
    
    public void createWebServiceAppTechs() throws PhrescoException {
        List<Technology> techs = new ArrayList<Technology>();
        techs.add(createTechnology(TechnologyTypes.JAVA_WEBSERVICE, "Java Web Service", new String[]{"1.6", "1.5"}, "apptype-web-services"));
        techs.add(createTechnology(TechnologyTypes.NODE_JS_WEBSERVICE, "Node JS Web Service", new String[]{"6.14","6.11", "6.8","6.7", "6.1"}, "apptype-web-services"));

//        serviceManager = ServiceClientFactory.getServiceManager(context);
//        RestClient<Technology> techClient = serviceManager.getRestClient("/components/technologies");
//        techClient.queryString("techId", "apptype-web-services");
//        ClientResponse response = techClient.create(techs);
//        System.out.println("response " + response.getStatus());
        
    }
    
    public void publish() throws PhrescoException {
        generateApptypes();
//        createWebAppTechs();
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
        technology.setDependencies(createDependencies(id));
        return technology;
    }
    
    private List<ArtifactGroup> createDependencies(String id) {
        List<ArtifactGroup> infos = new ArrayList<ArtifactGroup>();
        ArtifactGroup artifactGroup = pluginMap.get(id);
        artifactGroup.setSystem(true);
        artifactGroup.setCustomerIds(getDefaultCustomers());
        artifactGroup.setVersions(createArtifactInfo());
        infos.add(artifactGroup);
        return infos;
    }

    private ArtifactGroup createArchetypeInfo(String id, String name) {
        ArtifactGroup group = new ArtifactGroup();
        group.setGroupId("archetypes");
        group.setArtifactId(archetypeMap.get(id));
        group.setCustomerIds(getDefaultCustomers());
        group.setDescription(name + "Technology Archetype");
        group.setHelpText(name + "Technology Archetype");
        group.setName(name + "Technology Archetype");
        group.setSystem(true);
        group.setType("archetype");
        group.setVersions(createArtifactInfo());
        return group;
    }

    private List<ArtifactInfo> createArtifactInfo() {
        List<ArtifactInfo> infos = new ArrayList<ArtifactInfo>();
        ArtifactInfo artifactInfo;
        for (String version : artifactVersion) {
            artifactInfo = new ArtifactInfo();
            artifactInfo.setSystem(true);
            artifactInfo.setVersion(version);
            infos.add(artifactInfo);
        }
        return infos;
    }

    private List<String> getDefaultCustomers() {
        List<String> customerIds = new ArrayList<String>();
        customerIds.add(PHOTON);
        return customerIds;
    }
    
    public static void main(String[] args) throws PhrescoException {
        ApptypeGenerator generator = new ApptypeGenerator();
        generator.publish();
	}

}
