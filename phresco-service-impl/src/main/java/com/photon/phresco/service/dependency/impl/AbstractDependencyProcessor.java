/*
/*
 * ###
 * Phresco Service Implemenation
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
package com.photon.phresco.service.dependency.impl;

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
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.Database;
import com.photon.phresco.model.ProjectInfo;
import com.photon.phresco.model.Technology;
import com.photon.phresco.service.api.DependencyProcessor;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.util.ServerConstants;
import com.photon.phresco.util.Constants;
import com.photon.phresco.util.TechnologyTypes;
import com.phresco.pom.exception.PhrescoPomException;
import com.phresco.pom.util.PomProcessor;

/**
 * Abstract dependency processor has methods to get the binaries from
 * dependencies.
 * 
 * @author arunachalam_l
 * 
 */
public abstract class AbstractDependencyProcessor implements DependencyProcessor {

	private static final Logger S_LOGGER = Logger.getLogger(AbstractDependencyProcessor.class);
	private static Map<String, String> sqlFolderPathMap = new HashMap<String, String>();
	private static Map<String, String> testPomFiles = new HashMap<String, String>();

	private RepositoryManager repoManager = null;

	static {
		initDbPathAndTestPom();
	}

	/**
	 * @param dependencyManager
	 */
	public AbstractDependencyProcessor(RepositoryManager repoManager) {
		this.repoManager = repoManager;
	}

	
	protected RepositoryManager getRepositoryManager() {
		return repoManager;
	}

	private static void initDbPathAndTestPom() {
		//FIXME: this map needs to be removed and should go into respective subclasses
		sqlFolderPathMap.put(TechnologyTypes.PHP, ServerConstants.SOURCE_SQL);
		sqlFolderPathMap.put(TechnologyTypes.PHP_DRUPAL6, ServerConstants.SOURCE_SQL);
		sqlFolderPathMap.put(TechnologyTypes.PHP_DRUPAL7, ServerConstants.SOURCE_SQL);
		sqlFolderPathMap.put(TechnologyTypes.NODE_JS_WEBSERVICE, ServerConstants.SOURCE_SQL);
		sqlFolderPathMap.put(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET, ServerConstants.SRC_SQL);
		sqlFolderPathMap.put(TechnologyTypes.HTML5_MOBILE_WIDGET, ServerConstants.SRC_SQL);
		sqlFolderPathMap.put(TechnologyTypes.HTML5_WIDGET, ServerConstants.SRC_SQL);
		sqlFolderPathMap.put(TechnologyTypes.JAVA_WEBSERVICE, ServerConstants.SRC_SQL);
		sqlFolderPathMap.put(TechnologyTypes.WORDPRESS, ServerConstants.SOURCE_SQL);
	
		testPomFiles.put("functional", "/test/functional/pom.xml");
		testPomFiles.put("load", "/test/load/pom.xml");
		testPomFiles.put("performance.database", "/test/performance/database/pom.xml");
		testPomFiles.put("performance.server", "/test/performance/server/pom.xml");
		testPomFiles.put("performance.webservice", "/test/performance/webservices/pom.xml");
		testPomFiles.put("unit", "/test/unit/pom.xml");
		testPomFiles.put("performance", "/test/performance/pom.xml");
	}

	@Override
	public void process(ProjectInfo info, File path) throws PhrescoException {
		S_LOGGER.debug("Entering Method  AbstractDependencyProcessor.process(ProjectInfo info, File path)");
		S_LOGGER.debug("process() FilePath=" + path.getPath());
		S_LOGGER.debug("process() ProjectCode=" + info.getCode());

		Technology technology = info.getTechnology();

		// pilot projects
		extractPilots(info, path, technology);
	}

	protected void extractPilots(ProjectInfo info, File path,
			Technology technology) throws PhrescoException {
	    
	    ProjectInfo projectInfo = PhrescoServerFactory.getDbManager().getProjectInfo(info.getTechnology().getId(), info.getPilotProjectName());
        if(projectInfo != null) {
            DependencyUtils.extractFiles(projectInfo.getProjectURL(), path, info.getCustomerId());
        }
	}
	
	/*
	 * For Update Test Folders Pom Files
	 */
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
	
	protected abstract String getModulePathKey();

	protected void updatePOMWithModules(File path, List<com.photon.phresco.model.ModuleGroup> modules)
	throws PhrescoException, JAXBException, PhrescoPomException {
		try {
			S_LOGGER.debug("updatePOMWithModules() path=" + path.getPath());
			File pomFile = new File(path, "pom.xml");
			if (pomFile.exists()) {
				PomProcessor processor = new PomProcessor(pomFile);
				for (com.photon.phresco.model.ModuleGroup module : modules) {
					if (module != null) {
						processor.addDependency(module.getGroupId(), module.getArtifactId(), module.getVersions()
								.get(0).getVersion());
					}
				}
				processor.save();
			}
		} catch (IOException e) {
			throw new PhrescoException(e);
		}
	}
	
	protected void updatePOMWithJsLibs(File path, List<com.photon.phresco.model.ModuleGroup> jsLibs) throws PhrescoException{
        try {
        	S_LOGGER.debug("updatePOMWithJsLibs() path=" + path.getPath());
            File pomFile = new File(path, "pom.xml");
            if (pomFile.exists()) {
                PomProcessor processor = new PomProcessor(pomFile);
                for (com.photon.phresco.model.ModuleGroup jsLibrary : jsLibs) {
                    if (jsLibrary != null) {
                        String groupId = "jslibraries.files";
                        String artifactId = "jslib_" + jsLibrary.getName().toLowerCase();
                        processor.addDependency(groupId, artifactId, jsLibrary.getVersions()
                                .get(0).getVersion(), "", "js","");
                    }
                }
                processor.save();
            }
        } catch (IOException e) {
            throw new PhrescoException(e);
        } catch (JAXBException e) {
            throw new PhrescoException(e);
        } catch (PhrescoPomException e) {
            throw new PhrescoException(e);
        }
    }
	
	protected void createSqlFolder(ProjectInfo info, File path) throws PhrescoException {
		String databaseType = "";
		try {
			List<Database> databaseList = info.getTechnology().getDatabases();
			String techId = info.getTechnology().getId();
			if (databaseList == null || databaseList.size() == 0) {
				return;
			}
			File mysqlFolder = new File(path, sqlFolderPathMap.get(techId) + Constants.DB_MYSQL);
			File mysqlVersionFolder = getMysqlVersionFolder(mysqlFolder);
			for (Database db : databaseList) {
				databaseType = db.getName().toLowerCase();
				List<String> versions = db.getVersions();
				for (String version : versions) {
					String sqlPath = databaseType + File.separator + version.trim();
					File sqlFolder = new File(path, sqlFolderPathMap.get(techId) + sqlPath);
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
	
	protected void updatePOMWithModules(File path, List<com.photon.phresco.model.ModuleGroup> modules, String id) throws PhrescoException {
		if(CollectionUtils.isEmpty(modules)) {
			return;
		}
		try {
			File pomFile = new File(path, "pom.xml");
			if (pomFile.exists()) {
				PomProcessor processor = new PomProcessor(pomFile);
				for (com.photon.phresco.model.ModuleGroup module : modules) {
					if (module != null) {
						String groupId ="modules." + id + ".files";
						processor.addDependency(groupId, module.getId(), module.getVersions()
								.get(0).getVersion(), "" ,"Zip" , "");
					}
				}
				processor.save();
			}
		} catch (IOException e) {
			throw new PhrescoException(e);
		} catch (JAXBException e) {
			throw new PhrescoException(e);
		} catch (PhrescoPomException e) {
			throw new PhrescoException(e);
		}
	}
	
	protected void updatePOMWithPluginArtifact(File path, List<com.photon.phresco.model.ModuleGroup> modules, String techId) throws PhrescoException {
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
				for (com.photon.phresco.model.ModuleGroup module : modules) {
					if (module != null) {
						String groupId ="modules." + techId + ".files";
						String artifactId = module.getId();
						String version = module.getVersions().get(0).getVersion();
						configList = configList(pomFile, groupId, artifactId, version, doc, techId);
					 processor.addExecutionConfiguration("org.apache.maven.plugins", "maven-dependency-plugin", "unpack-module", "validate", "unpack", configList, false, doc);
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

	protected List<Element> configList(File pomFile,String moduleGroupId, String moduleArtifactId, String moduleVersion, Document doc, String techId) throws PhrescoException {
		String modulePathKey = getModulePathKey();
		String modulesPathString = DependencyProcessorMessages.getString(modulePathKey);
		List<Element> configList = new ArrayList<Element>();
		Element groupId = doc.createElement("groupId");
		groupId.setTextContent(moduleGroupId);
		Element artifactId = doc.createElement("artifactId");
		artifactId.setTextContent(moduleArtifactId);
		Element version = doc.createElement("version");
		version.setTextContent(moduleVersion);
		Element type = doc.createElement("type");
		type.setTextContent("zip");
		Element overWrite = doc.createElement("overWrite");
		overWrite.setTextContent("false");
		Element outputDirectory = doc.createElement("outputDirectory");
		outputDirectory.setTextContent("${project.basedir}/" + modulesPathString);	
		configList.add(groupId);
		configList.add(artifactId);
		configList.add(version);
		configList.add(type);
		configList.add(overWrite);
		configList.add(outputDirectory);
		return configList;
	}
	
}
