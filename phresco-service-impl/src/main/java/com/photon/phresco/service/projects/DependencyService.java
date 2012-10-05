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

package com.photon.phresco.service.projects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.DownloadInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.dependency.impl.DependencyUtils;
import com.photon.phresco.service.util.ServerUtil;
import com.photon.phresco.util.Constants;
import com.phresco.pom.exception.PhrescoPomException;
import com.phresco.pom.util.PomProcessor;

public class DependencyService {
	
	private static Map<String, String> artifactTypeMap = new HashMap<String, String>();
	private static Map<String, String> testPomFiles = new HashMap<String, String>();
	private String type = null;
	
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
	
	protected void updatePOMWithArtifacts(File path, List<ArtifactGroup> modules, String type) throws PhrescoException {
		this.type = type;
		System.out.println("Type Is" + type);
		if(CollectionUtils.isEmpty(modules)) {
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
		} catch (IOException e) {
			throw new PhrescoException(e);
		} catch (JAXBException e) {
			throw new PhrescoException(e);
		} catch (PhrescoPomException e) {
			throw new PhrescoException(e);
		}
	}
	
	private void updatePOMWithPluginArtifact(File path, List<ArtifactGroup> modules) throws PhrescoException {
		try {
			if(CollectionUtils.isEmpty(modules)) {
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
							 "unpack-module", "validate", "unpack", configList, false, doc);
					}
				}
				processor.save();
			}
		} catch (JAXBException e) {
			throw new PhrescoException(e);
		} catch (IOException e) {
			throw new PhrescoException(e);
		} catch (PhrescoPomException e) {
			throw new PhrescoException(e);
		} catch (ParserConfigurationException e) {
			throw new PhrescoException(e);
		}
	}

	private List<Element> configList(File pomFile,String moduleGroupId, String moduleArtifactId, 
			String moduleVersion, Document doc) throws PhrescoException {
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
		System.out.println("TExt Comntent is" + artifactTypeMap.get(type));
		outputDirectory.setTextContent(artifactTypeMap.get(type));	
		configList.add(groupId);
		configList.add(artifactId);
		configList.add(version);
		configList.add(eleType);
		configList.add(overWrite);
		configList.add(outputDirectory);
		return configList;
	}
	
	protected void extractPilots(ApplicationInfo projectInfo, File projectPath, String customerId) throws PhrescoException {
		ArtifactGroup pilotContent = projectInfo.getPilotContent();
        String contentURL = ServerUtil.createContentURL(pilotContent.getGroupId(), pilotContent.getArtifactId(),
        		pilotContent.getVersions().get(0).getVersion(), pilotContent.getPackaging());
        if(contentURL != null) {
            DependencyUtils.extractFiles(contentURL, projectPath, customerId);
        }
	}
	
	protected static void updateTestPom(File path) throws PhrescoException {
		try {
			File sourcePom = new File(path + "/pom.xml");
			if (!sourcePom.exists()) {
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
			
		} catch (Exception e) {
			throw new PhrescoException(e);
		}
	}
	
	protected void createSqlFolder(List<DownloadInfo> selectedDbs, File path) throws PhrescoException {
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
		} catch (IOException e) {
			throw new PhrescoException(e);
		}
	}
	
	private File getMysqlVersionFolder(File mysqlFolder) {
		File[] mysqlFolderFiles = mysqlFolder.listFiles();
		if (mysqlFolderFiles != null && mysqlFolderFiles.length > 0) {
			return mysqlFolderFiles[0];
		}
		return null;
	}
	
}
