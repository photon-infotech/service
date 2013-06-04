/**
 * Phresco Service Implemenation
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
package com.photon.phresco.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactGroupInfo;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.DownloadInfo;
//import com.photon.phresco.commons.model.Element;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.api.DbManager;
import com.photon.phresco.service.api.DependencyManager;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.util.DependencyUtils;
import com.photon.phresco.service.util.ServerUtil;
import com.photon.phresco.util.Constants;
import com.phresco.pom.exception.PhrescoPomException;
import com.phresco.pom.util.PomProcessor;

public class DependencyManagerImpl implements DependencyManager {
	
	private DbManager dbManager = null;
	private static Map<String, String> artifactTypeMap = new HashMap<String, String>();
	private static Map<String, String> testPomFiles = new HashMap<String, String>();
	private String artifactType = null;
	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(DependencyManagerImpl.class.getName());
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();
	
	static {
		artifactTypeMap.put("FEATURE", "${feature.directory}");
		artifactTypeMap.put("JAVASCRIPT", "${js.directory}");
		artifactTypeMap.put("database", "${db.directory}");
		
		testPomFiles.put("functional", "/test/functional/pom.xml");
		testPomFiles.put("load", "/test/load/pom.xml");
		testPomFiles.put("performance.database", "/test/performance/database/pom.xml");
		testPomFiles.put("performance.server", "/test/performance/server/pom.xml");
		testPomFiles.put("performance.webservice", "/test/performance/webservices/pom.xml");
		testPomFiles.put("unit", "/test/unit/pom.xml");
		testPomFiles.put("performance", "/test/performance/pom.xml");
	}
	
	public DependencyManagerImpl() throws PhrescoException {
		PhrescoServerFactory.initialize();
		setDbManager(PhrescoServerFactory.getDbManager());
	}
	
	@Override
	public void configureProject(ApplicationInfo applicationInfo, File projectPath) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("DependencyManagerImpl.configureProject:Entry");
			if(applicationInfo == null) {
				LOGGER.warn("DependencyManagerImpl.configureProject","status=\"Bad Request\"", "message=\"applicationInfo is empty\"");
				throw new PhrescoException("applicationInfo is empty");
			}
			LOGGER.info("DependencyManagerImpl.configureProject", "projectPath=\"" + projectPath.getPath() + "\"");
		}
		String customerId = null;
		customerId = applicationInfo.getCustomerIds().get(0);
		if(StringUtils.isEmpty(customerId)) {
			LOGGER.warn("DependencyManagerImpl.configureProject","status=\"Bad Request\"", "message=\"customerId is empty\"");
			throw new PhrescoException("CustomerId Should Not Be Null");
		}
		
		if(CollectionUtils.isNotEmpty(applicationInfo.getSelectedModules())) {
			String type = ArtifactGroup.Type.FEATURE.name();
			List<ArtifactGroup> selectedFeatures = dbManager.findSelectedArtifacts(applicationInfo.getSelectedModules(), customerId, type);
			updatePOMWithArtifacts(projectPath, selectedFeatures, type);
		}
		
		if(CollectionUtils.isNotEmpty(applicationInfo.getSelectedJSLibs())) {
			String type = ArtifactGroup.Type.JAVASCRIPT.name();
			List<ArtifactGroup> selectedFeatures = dbManager.findSelectedArtifacts(applicationInfo.getSelectedModules(), customerId, type);
			updatePOMWithArtifacts(projectPath, selectedFeatures, type);
		}
		
		com.photon.phresco.commons.model.Element pilotInfo = applicationInfo.getPilotInfo();
		String pilotId = pilotInfo.getId();
		ApplicationInfo projectInfo = dbManager.getApplicationInfo(pilotId);
		if(projectInfo != null) {
			extractPilots(projectInfo, projectPath, customerId);
		}
		
		if(CollectionUtils.isNotEmpty(applicationInfo.getSelectedDatabases())) {
			for (ArtifactGroupInfo artifactGroupInfo : applicationInfo.getSelectedDatabases()) {
				List<DownloadInfo> selectedDbs = dbManager.findSelectedDatabases(artifactGroupInfo.getArtifactInfoIds(), customerId);
				createSqlFolder(selectedDbs, projectPath);
			}
		}
		
		updateTestPom(projectPath);
		if(isDebugEnabled) {
			LOGGER.debug("DependencyManagerImpl.configureProject:Exit");
		}
	}
	
	public void setDbManager(DbManager dbManager) {
		this.dbManager = dbManager;
	}

	public DbManager getDbManager() {
		return dbManager;
	}

	private void updatePOMWithArtifacts(File path, List<ArtifactGroup> modules, String type) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("DependencyManagerImpl.updatePOMWithArtifacts:Entry");
			LOGGER.info("DependencyManagerImpl.updatePOMWithArtifacts", "path=\""+ path.getPath() +"\"", "type=\""+ type +"\"");
		}
		this.artifactType = type;
		if(CollectionUtils.isEmpty(modules)) {
			if(isDebugEnabled) {
				LOGGER.warn("DependencyManagerImpl.configureProject","status=\"Bad Request\"", "message=\"Modules is empty\"");
			}
			return;
		}
		
		try {
			File pomFile = new File(path, "pom.xml");
			if (pomFile.exists()) {
				PomProcessor processor = new PomProcessor(pomFile);
				for (ArtifactGroup module : modules) {
					if (module != null) {
						String groupId = module.getGroupId();
						String artifactId = module.getArtifactId();
						processor.addDependency(groupId, artifactId, module.getVersions()
								.get(0).getVersion(), "" , module.getPackaging(), "");
					}
				}
				processor.save();
			}
			updatePOMWithPluginArtifact(path, modules);
			if(isDebugEnabled) {
				LOGGER.debug("DependencyManagerImpl.updatePOMWithArtifacts:Exit");
			}
		} catch (PhrescoPomException e) {
			if(isDebugEnabled) {
				LOGGER.error("DependencyManagerImpl.updatePOMWithArtifacts", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			throw new PhrescoException(e);
		}
	}
	
	private void updatePOMWithPluginArtifact(File path, List<ArtifactGroup> modules) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("DependencyManagerImpl.updatePOMWithPluginArtifact:Entry");
			LOGGER.info("DependencyManagerImpl.updatePOMWithPluginArtifact", "path=\""+ path.getPath() +"\"");
		}
		try {
			if(CollectionUtils.isEmpty(modules)) {
				if(isDebugEnabled) {
					LOGGER.warn("DependencyManagerImpl.updatePOMWithPluginArtifact","status=\"Bad Request\"", "message=\"Modules is empty\"");
				}
				return;
			}
			
			List<Element> configList = new ArrayList<Element>();
			File pomFile = new File(path, "pom.xml");
			if (pomFile.exists()) {
				PomProcessor processor = new PomProcessor(pomFile);
				DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
				Document doc = docBuilder.newDocument();
				for (ArtifactGroup module : modules) {
					if (module != null) {
						String groupId = module.getGroupId();
						String artifactId = module.getArtifactId();
						String version = module.getVersions().get(0).getVersion();
						configList = configList(pomFile, groupId, artifactId, version, doc);
						processor.addExecutionConfiguration("org.apache.maven.plugins", "maven-dependency-plugin", 
							 "unpack-module", "validate", "unpack", configList, doc);
					}
				}
				processor.save();
			}
			if(isDebugEnabled) {
				LOGGER.debug("DependencyManagerImpl.updatePOMWithPluginArtifact:Exit");
			}
		} catch (PhrescoPomException e) {
			LOGGER.error("DependencyManagerImpl.updatePOMWithPluginArtifact", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			throw new PhrescoException(e);
		} catch (ParserConfigurationException e) {
			LOGGER.error("DependencyManagerImpl.updatePOMWithPluginArtifact", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			throw new PhrescoException(e);
		}
	}

	private List<Element> configList(File pomFile, String moduleGroupId, String moduleArtifactId, 
			String moduleVersion, Document doc) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("DependencyManagerImpl.configList:Entry");
			LOGGER.info("DependencyManagerImpl.configList", "path=\""+ pomFile.getPath() +"\"", "moduleGroupId=\""+ moduleGroupId +"\"",
					"moduleArtifactId=\""+ moduleArtifactId +"\"", "moduleVersion=\""+ moduleVersion +"\"");
		}
		List<Element> configList = new ArrayList<Element>();
		Element groupId = doc.createElement("groupId");
		groupId.setTextContent(moduleGroupId);
		Element artifactId = doc.createElement("artifactId");
		artifactId.setTextContent(moduleArtifactId);
		Element version = doc.createElement("version");
		version.setTextContent(moduleVersion);
		Element eleType = doc.createElement("type");
		eleType.setTextContent("zip");
		Element overWrite = doc.createElement("overWrite");
		overWrite.setTextContent("false");
		Element outputDirectory = doc.createElement("outputDirectory");
		outputDirectory.setTextContent(artifactTypeMap.get(artifactType));	
		configList.add(groupId);
		configList.add(artifactId);
		configList.add(version);
		configList.add(eleType);
		configList.add(overWrite);
		configList.add(outputDirectory);
		if(isDebugEnabled) {
			LOGGER.debug("DependencyManagerImpl.configList:Exit");
		}
		return configList;
	}
	
	private void extractPilots(ApplicationInfo projectInfo, File projectPath, String customerId) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("DependencyManagerImpl.extractPilots:Entry");
			LOGGER.info("DependencyManagerImpl.extractPilots","projectPath=\""+ projectPath +"\"", "customerId=\""+ customerId +"\"");
		}
		ArtifactGroup pilotContent = projectInfo.getPilotContent();
        String contentURL = ServerUtil.createContentURL(pilotContent.getGroupId(), pilotContent.getArtifactId(),
        		pilotContent.getVersions().get(0).getVersion(), pilotContent.getPackaging());
        if(contentURL != null) {
            DependencyUtils.extractFiles(contentURL, projectPath, customerId);
        }
        if(isDebugEnabled) {
        	LOGGER.debug("DependencyManagerImpl.extractPilots:Exit");
        }
	}
	
	private static void updateTestPom(File path) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("DependencyManagerImpl.updateTestPom:Entry");
			LOGGER.info("DependencyManagerImpl.updateTestPom","path=\""+ path.getPath() +"\"");
		}
		try {
			File sourcePom = new File(path + "/pom.xml");
			if (!sourcePom.exists()) {
				if(isDebugEnabled) {
					LOGGER.warn("DependencyManagerImpl.updateTestPom","status=\"Bad Request\"", "message=\"pom path not exist\"");
				}
				return;
			}
			
			PomProcessor processor;
			processor = new PomProcessor(sourcePom);
			String groupId = processor.getGroupId();
			String artifactId = processor.getArtifactId();
			String version = processor.getVersion();
			String name = processor.getName();
			Set<String> keySet = testPomFiles.keySet();
			for (String string : keySet) {
			    File testPomFile = new File(path + testPomFiles.get(string));
			    if (testPomFile.exists()) {
                  processor = new PomProcessor(testPomFile);
                  processor.setGroupId(groupId + "." + string);
                  processor.setArtifactId(artifactId);
                  processor.setVersion(version);
                  if (name != null && !name.isEmpty()) {
                      processor.setName(name);
                  }
                  processor.save();
              }
            }
			if(isDebugEnabled) {
				LOGGER.debug("DependencyManagerImpl.updateTestPom:Exit");
			}
		} catch (Exception e) {
			LOGGER.error("DependencyManagerImpl.updateTestPom", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			throw new PhrescoException(e);
		}
	}
	
	private void createSqlFolder(List<DownloadInfo> selectedDbs, File path) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("DependencyManagerImpl.createSqlFolder:Entry");
			LOGGER.info("DependencyManagerImpl.createSqlFolder", "path=\""+ path.getPath() +"\"");
		}
		String databaseType = "";
		try {
			File mysqlFolder = new File(path, artifactTypeMap.get("database") + Constants.DB_MYSQL);
			File mysqlVersionFolder = getMysqlVersionFolder(mysqlFolder);
			
			for (DownloadInfo db : selectedDbs) {
				databaseType = db.getName().toLowerCase();
				List<ArtifactInfo> versions = db.getArtifactGroup().getVersions();
				for (ArtifactInfo version : versions) {
					String sqlPath = databaseType + File.separator + version.getVersion();
					File sqlFolder = new File(path, artifactTypeMap.get("database") + sqlPath);
					sqlFolder.mkdirs();
					if (databaseType.equals(Constants.DB_MYSQL) && mysqlVersionFolder != null
							&& !(mysqlVersionFolder.getPath().equals(sqlFolder.getPath()))) {						
						FileUtils.copyDirectory(mysqlVersionFolder, sqlFolder);
					} else {
						File sqlFile = new File(sqlFolder, Constants.SITE_SQL);
						sqlFile.createNewFile();
					}
				}
			}
			if(isDebugEnabled) {
				LOGGER.debug("DependencyManagerImpl.createSqlFolder:Exit");
			}
		} catch (IOException e) {
			LOGGER.error("DependencyManagerImpl.createSqlFolder", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			throw new PhrescoException(e);
		}
	}
	
	private File getMysqlVersionFolder(File mysqlFolder) {
		if(isDebugEnabled) {
			LOGGER.debug("DependencyManagerImpl.getMysqlVersionFolder:Entry");
			LOGGER.info("DependencyManagerImpl.getMysqlVersionFolder","path=\""+ mysqlFolder.getPath() +"\"");
		}
		File[] mysqlFolderFiles = mysqlFolder.listFiles();
		if (mysqlFolderFiles != null && mysqlFolderFiles.length > 0) {
			return mysqlFolderFiles[0];
		}
		if(isDebugEnabled) {
			LOGGER.debug("DependencyManagerImpl.getMysqlVersionFolder:Exit");
		}
		return null;
	}
	
}
