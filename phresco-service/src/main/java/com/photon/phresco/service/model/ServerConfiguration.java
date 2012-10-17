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
package com.photon.phresco.service.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import com.photon.phresco.configuration.ConfigReader;
import com.photon.phresco.configuration.Configuration;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.util.Utility;

public class ServerConfiguration {

	private static final String PHRESCO_SERVER_CONFIG_FILE = "phresco.server.dependency.config.file";
	private static final String KEY_PHRESCO_MAVEN_REPOSITORY_URL = "phresco.maven.repository.url";
	private static final String KEY_PHRESCO_MAVEN_REPOSITORY_USER = "phresco.maven.repository.user.name";
	private static final String KEY_PHRESCO_MAVEN_REPOSITORY_PASSWORD = "phresco.maven.repository.user.password";
	private static final String KEY_PHRESCO_SERVICE_URL ="phresco.service.url";
	private static final String KEY_CREDENTIAL_URL ="phresco.credential.path";
	private static final String LIBRARY = "lib.";
	private static final String MODULE = "module.";
	private static final String PLATFORM = "platform.";
	private static final String DATABASE = "database.";
	private static final String EDITOR = "editor.";
//	private static final String JSLIBRARIES = "lib.tech-js";
	private static final String JSLIBRARIES = "jslibrary.";
	private static final String APPSERVER = "appserver.";
	private static final String PILOTS = "pilots.";
	private static final String PILOTSURL="pilots.url.";
	private static final String KEY_APPTYPE_CONFIG = "apptype.config";
	private static final String DEFAULT_VALUE_APPTYPE_CONFIG = "apptype.xml";
	private static final String PHRESCO_CI_JENKINS_CONFIG_FILE = "phresco.ci.jenkins.config.file";
	private static final String PHRESCO_CI_JENKINS_SVN_FILE = "phresco.ci.jenkins.svn.credential";
	private static final String PHRESCO_CI_CREDENTIAL_XML_FILE = "phresco.ci.credential.url";
	private static final String PHRESCO_CI_JAVAHOMECONFIG_XML_FILE = "phresco.ci.javahomeconfig.file";
	private static final String PHRESCO_CI_MAVENHOMECONFIG_XML_FILE = "phresco.ci.mavenhomeconfig.file";
	private static final String SETTING_CONFIG_FILE = "settings.config.file";
	private static final String HOMEPAGE_JSON_FILE = "homepage.json.file";
	private static final String ADMIN_CONFIG_FILE = "admin.config.file";
	private static final String SERVICE_LATEST_VERSION  = "phresco.service.latest.version"; 
	private static final String FRAMEWORK_LATEST_VERSION  = "phresco.framework.latest.version"; 
	private static final String AUTHENTICATION_SERVICE_URL = "phresco.authenticate.url";
	private static final String EMAIL_EXT_FILE = "phresco.ci.emailext.file";
	private static final String PHRESCO_SERVER_CONTEXT_NAME = "phresco.server.context.name";
	private static final String PHRESCO_DB_COLLECTION = "db.defaultcollection";
	private static final String PHRESCO_TWITTER_SERVICE_URL = "phresco.twitter.service.url";
	private static final String PHRESCO_FRAMEWORK_LATEST_URL = "phresco.framework.latest.version.file";
	private static final String DATABASES = "Database";
	private String repositoryURL;
	private String repositoryUser;
	private String repositoryPassword;
	private String dependencyConfigFile;
	private Properties dependencyConfig = new Properties();
	private Properties serverProps;
	private String serviceURL;
	private String credentialurl;
	private String authenticateurl;
	private String repoBaseURL;
	private String repoUserName;
	private String repoPassword;
	private String serviceContextName;
	private String dbHost;
	private String dbPort;
	private String dbName;
	private String dbUserName;
	private String dbPassword;
	private static final String HOST = "host";
	private static final String PORT= "port";
	private static final String DBNAME = "dbname";
	private static final String DBUSERNAME = "username";
	private static final String DBPASSWORD = "password";
	private String dbDefaultCollectionName;
	private String twitterServiceURL; 
	private String configFilePath =  "phresco-env-config.xml";

	public ServerConfiguration(String fileName) throws PhrescoException {
//		initServerConfig(fileName);
//		initDependencyConfig();
	}

	private void initDependencyConfig() throws PhrescoException {
		InputStream is = null;
		try {
			is = getArtifactAsStream(dependencyConfigFile);
			dependencyConfig.load(is);
		} catch (IOException e) {
			throw new PhrescoException(e);
		} finally {
			Utility.closeStream(is);
		}
	}

	private InputStream getArtifactAsStream(String filePath) throws PhrescoException {
		try {
			return new URL(repositoryURL + filePath).openStream();
		} catch (MalformedURLException e) {
			throw new PhrescoException(e);
		} catch (IOException e) {
			throw new PhrescoException(e);
		}
	}

	private void initServerConfig(String fileName) throws PhrescoException {
		InputStream is = null;
		try {
			is = this.getClass().getClassLoader().getResourceAsStream(fileName);
			serverProps = new Properties();
			serverProps.load(is);

			// Initialize the Server URL
			this.repositoryURL = serverProps.getProperty(KEY_PHRESCO_MAVEN_REPOSITORY_URL);
			this.repositoryUser = serverProps.getProperty(KEY_PHRESCO_MAVEN_REPOSITORY_USER);
			this.repositoryPassword = serverProps.getProperty(KEY_PHRESCO_MAVEN_REPOSITORY_PASSWORD);
			this.dependencyConfigFile = serverProps.getProperty(PHRESCO_SERVER_CONFIG_FILE);
			this.serviceURL = serverProps.getProperty(KEY_PHRESCO_SERVICE_URL);
			this.credentialurl = serverProps.getProperty(KEY_CREDENTIAL_URL);
			this.authenticateurl= serverProps.getProperty(AUTHENTICATION_SERVICE_URL);
			this.serviceContextName = serverProps.getProperty(PHRESCO_SERVER_CONTEXT_NAME);
//			this.dbHost = serverProps.getProperty(PHRESCO_DB_HOST);
//			this.dbPort = serverProps.getProperty(PHRESCO_DB_PORT);
//			this.dbName = serverProps.getProperty(PHRESCO_DB_NAME);
			this.dbDefaultCollectionName = serverProps.getProperty(PHRESCO_DB_COLLECTION);
			this.twitterServiceURL = serverProps.getProperty(PHRESCO_TWITTER_SERVICE_URL);
		} catch (IOException e) {
			throw new PhrescoException(e);
		} finally {
			Utility.closeStream(is);
		}
	}
	
	public Properties getServerProperties() {
		return serverProps;
	}

	public String getApptypeFile() {
		return dependencyConfig.getProperty(KEY_APPTYPE_CONFIG,
				DEFAULT_VALUE_APPTYPE_CONFIG);
	}

	public String getModuleFile(String techId) {
		return dependencyConfig.getProperty(MODULE.concat(techId));
	}

	public String getLibraryFile(String techId) {
		return dependencyConfig.getProperty(LIBRARY.concat(techId));
	}

	public String getPlatformFile(String techId) {
		return dependencyConfig.getProperty(PLATFORM.concat(techId));
	}

	public String getDatabaseFile(String techId) {
		return dependencyConfig.getProperty(DATABASE.concat(techId));
	}

	public String getAppserverFile(String techId) {
		return dependencyConfig.getProperty(APPSERVER.concat(techId));
	}

	public String getjsLibrariesFile(String techId) {
		return dependencyConfig.getProperty(JSLIBRARIES.concat(techId));
	}

	public String getEditorFile(String techId) {
		return dependencyConfig.getProperty(EDITOR.concat(techId));
	}
	
	public String getPilotFile(String techId) {
		return dependencyConfig.getProperty(PILOTS.concat(techId));
	}

	public String getPilotUrls(String techId) {
		return dependencyConfig.getProperty(PILOTSURL.concat(techId));
	}

	public String getRepositoryURL() {
		return repositoryURL;
	}

	public String getRepositoryUser() {
		return repositoryUser;
	}

	public String getRepositoryPassword() {
		return repositoryPassword;
	}
	
	public String getServerContextName() {
        return serviceContextName;
	}
	
	public String getTwitterServiceURL() {
        return twitterServiceURL;
	}
	
	public String getCiConfigFile() {
	    return dependencyConfig.getProperty(PHRESCO_CI_JENKINS_CONFIG_FILE);
	}
	
	public String getCiSvnFile() {
        return dependencyConfig.getProperty(PHRESCO_CI_JENKINS_SVN_FILE);
    }

	public String getCiCredentialXmlFilePath() {
        return dependencyConfig.getProperty(PHRESCO_CI_CREDENTIAL_XML_FILE);
    }
	
	public String getJavaHomeConfigFile(){
		return dependencyConfig.getProperty(PHRESCO_CI_JAVAHOMECONFIG_XML_FILE);
	}
	
	public String getMavenHomeConfigFile(){
		return dependencyConfig.getProperty(PHRESCO_CI_MAVENHOMECONFIG_XML_FILE);
	}
	
	public String getSettingConfigFile(){
		return dependencyConfig.getProperty(SETTING_CONFIG_FILE);
	}
	
	public String getHomePageJsonFile(){
		return dependencyConfig.getProperty(HOMEPAGE_JSON_FILE);
	}
	
	public String getAdminConfigFile(){
		return dependencyConfig.getProperty(ADMIN_CONFIG_FILE);
	}

	public String getLatestFrameworkVersion(){
		return dependencyConfig.getProperty(FRAMEWORK_LATEST_VERSION);
	}
	public String getLatestServiceVersion(){
		return dependencyConfig.getProperty(SERVICE_LATEST_VERSION);
	}
	public String getServiceUrl() {
		return serviceURL;
	}

	public String getCredentialFile() {
		return credentialurl;
	}

	public String getAuthServiceURL() throws PhrescoException {
		List<Configuration> configurations = configurationList("WebService");
		for (Configuration configuration : configurations) {
			String protocol = configuration.getProperties().getProperty("protocol");
			String host = configuration.getProperties().getProperty("host");
			String port = configuration.getProperties().getProperty("port");
			String context = configuration.getProperties().getProperty("context");
			authenticateurl = protocol + "://" + host + ":" +  port + "/" + context;
		}
		return authenticateurl;
	}
	
	public String getRepoBaseURL() throws PhrescoException {
        List<Configuration> configurations = configurationList("Server");
        for (Configuration configuration : configurations) {
            String protocol = configuration.getProperties().getProperty("protocol");
            String host = configuration.getProperties().getProperty("host");
            String port = configuration.getProperties().getProperty("port");
            String context = configuration.getProperties().getProperty("context");
            repoBaseURL = protocol + "://" + host + ":" +  port + "/" + context;
        }
        return repoBaseURL;
    }
	
	private List<Configuration> configurationList(String configType) throws PhrescoException {
		InputStream stream = null;
		stream = this.getClass().getClassLoader().getResourceAsStream(configFilePath);
		try {
			ConfigReader configReader = new ConfigReader(stream);
			String environment = System.getProperty("SERVER_ENVIRONMENT");
			if (environment == null || environment.isEmpty() ) {
				environment = configReader.getDefaultEnvName();
			}
			return configReader.getConfigurations(environment, configType);
		} catch (Exception e) {
		    throw new PhrescoException(e);
		}
	}

	public String getEmailExtFile() {
		return dependencyConfig.getProperty(EMAIL_EXT_FILE);
	}
	
	public String getDbHost() throws PhrescoException {
		List<Configuration> configurations = configurationList(DATABASES);
		if (configurations != null) {
			for (Configuration configuration : configurations) {
				 dbHost = configuration.getProperties().getProperty(HOST);
			}
		}
		return dbHost;
	}
	
	public int getDbPort() throws PhrescoException {
		List<Configuration> configurations = configurationList(DATABASES);
		if (configurations != null) {
			for (Configuration configuration : configurations) {
				 dbPort = configuration.getProperties().getProperty(PORT);
			}
		}
		return Integer.parseInt(dbPort);
	}
	
	public String getDbName() throws PhrescoException {
		List<Configuration> configurations = configurationList(DATABASES);
		if (configurations != null) {
			for (Configuration configuration : configurations) {
				 dbName = configuration.getProperties().getProperty(DBNAME);
			}
		}
		return dbName;
	}
	
	public String getDbUserName() throws PhrescoException {
		List<Configuration> configurations = configurationList(DATABASES);
		if (configurations != null) {
			for (Configuration configuration : configurations) {
				 dbUserName = configuration.getProperties().getProperty(DBUSERNAME);
			}
		}
		return dbUserName;
	}
	
	public String getDbPassword() throws PhrescoException {
		List<Configuration> configurations = configurationList(DATABASES);
		if (configurations != null) {
			for (Configuration configuration : configurations) {
				 dbPassword = configuration.getProperties().getProperty(DBPASSWORD);
			}
		}
		return dbPassword;
	}
	
	public String getDbCollection() {
		return dbDefaultCollectionName;
	}

	public String getFrameWorkLatestFile() {
		return dependencyConfig.getProperty(PHRESCO_FRAMEWORK_LATEST_URL);
	}
	
	public String getRepoUserName() throws PhrescoException {
	    List<Configuration> configurations = configurationList("Server");
	    if (configurations != null) {
            for (Configuration configuration : configurations) {
                 repoUserName = configuration.getProperties().getProperty("admin_username");
            }
        }
	    return repoUserName;
	}
	
	public String getRepoPassword() throws PhrescoException {
	    List<Configuration> configurations = configurationList("Server");
        if (configurations != null) {
            for (Configuration configuration : configurations) {
                 repoPassword = configuration.getProperties().getProperty("admin_password");
            }
        }
        return repoPassword;
    }
	
}
