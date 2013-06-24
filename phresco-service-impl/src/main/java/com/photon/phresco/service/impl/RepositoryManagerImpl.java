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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.deployment.DeployRequest;
import org.sonatype.aether.deployment.DeploymentException;
import org.sonatype.aether.repository.Authentication;
import org.sonatype.aether.repository.LocalRepository;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactRequest;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.resolution.VersionRangeRequest;
import org.sonatype.aether.resolution.VersionRangeResolutionException;
import org.sonatype.aether.resolution.VersionRangeResult;
import org.sonatype.aether.transfer.NoRepositoryConnectorException;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.artifact.SubArtifact;
import org.sonatype.aether.version.Version;

import com.google.gson.Gson;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.RepoInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.api.DbManager;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.model.ArtifactInfo;
import com.photon.phresco.service.model.GroupRepository;
import com.photon.phresco.service.model.ReleaseRepo;
import com.photon.phresco.service.model.Repository;
import com.photon.phresco.service.model.ServerConfiguration;
import com.photon.phresco.service.util.MAGICNUMBER;
import com.photon.phresco.service.util.ServerUtil;
import com.photon.phresco.util.ServiceConstants;
import com.photon.phresco.util.TechnologyTypes;
import com.photon.phresco.util.Utility;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public  class RepositoryManagerImpl implements RepositoryManager, ServiceConstants {

	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(RepositoryManagerImpl.class.getName());
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();
	private static final String DEFAULT = "default";
	private static final int HTTP_NOT_FOUND = 404;
	private static final String LOCAL_REPO = "../temp/target/local-repo";
	private ServerConfiguration config = null;

	// TODO:Add ehcaching
	private static Map<String, String[]> versionMap = new HashMap<String, String[]>(MAGICNUMBER.MAPVALUE);

	private void initMap() {
		versionMap.put(TechnologyTypes.PHP, new String[]{"5.4.x", "5.3.x", "5.2.x", "5.1.x", "5.0.x"});
		versionMap.put(TechnologyTypes.PHP_DRUPAL6, new String[]{"6.3", "6.25", "6.19","6.14"});
		versionMap.put(TechnologyTypes.PHP_DRUPAL7, new String[]{"7.8","7.12","7.14"});
		versionMap.put(TechnologyTypes.JAVA_WEBSERVICE, new String[]{MAGICNUMBER.VERSIONNUMSIX, MAGICNUMBER.VERSIONNUMFIVE});
		versionMap.put(TechnologyTypes.JAVA_STANDALONE, new String[]{MAGICNUMBER.VERSIONNUMSIX, MAGICNUMBER.VERSIONNUMFIVE});
		versionMap.put(TechnologyTypes.HTML5_MOBILE_WIDGET, new String[]{MAGICNUMBER.VERSIONNUMSIX, MAGICNUMBER.VERSIONNUMFIVE});
		versionMap.put(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET, new String[]{MAGICNUMBER.VERSIONNUMSIX, MAGICNUMBER.VERSIONNUMFIVE});
		versionMap.put(TechnologyTypes.HTML5_WIDGET, new String[]{MAGICNUMBER.VERSIONNUMSIX, MAGICNUMBER.VERSIONNUMFIVE});
		versionMap.put(TechnologyTypes.ANDROID_HYBRID, new String[]{"4.0.3", "2.3.3", "2.2"});
		versionMap.put(TechnologyTypes.ANDROID_NATIVE, new String[]{"4.0.3", "2.3.3", "2.2"});
		versionMap.put(TechnologyTypes.WORDPRESS, new String[]{"3.3.1"});
		versionMap.put(TechnologyTypes.DOT_NET, new String[]{"3.5", "3.0", "2.0"});
		versionMap.put(TechnologyTypes.SHAREPOINT, new String[]{"3.5", "3.0", "2.0"});
		versionMap.put(TechnologyTypes.NODE_JS_WEBSERVICE, new String[]{"0.6.x","0.7.x", "0.8.x"});

	}

	public RepositoryManagerImpl(ServerConfiguration config) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.RepositoryManagerImpl : Entry");
		}
		this.config = config;
		try {
			initMap();
		} catch (Exception e) {
			if(isDebugEnabled) {
				LOGGER.error("RepositoryManagerImpl.RepositoryManagerImpl", STATUS_FAILURE, MESSAGE_EQUALS + "\"" + e.getLocalizedMessage() + "\"");
			}
			throw new PhrescoException(e);
		}
		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.RepositoryManagerImpl : Exit");
		}
	}

	public List<ArtifactGroup> getModules(String techId) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.getModules : Entry");
		}

		if(isDebugEnabled) {
			if (StringUtils.isEmpty(techId)) {
				LOGGER.warn(REPO_MGR_IMPL_GET_MODULES, STATUS_BAD_REQUEST, "message=\"Video Info is empty\"");
			}
			LOGGER.info(REPO_MGR_IMPL_GET_MODULES, "techId=" + "\"" + techId);
		}

		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.getModules : Exit");
		}
		return null;
	}

	public List<ArtifactGroup> getJSLibraries(String techId) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.getJSLibraries : Entry");
		}

		if(isDebugEnabled) {
			if (StringUtils.isEmpty(techId)) {
				LOGGER.warn(REPO_MGR_IMPL_GET_MODULES, STATUS_BAD_REQUEST, "message=\"Video Info is empty\"");
			}
			LOGGER.info(REPO_MGR_IMPL_GET_MODULES, "techId=" + "\"" + techId);
		}

		String jslibraryFile = config.getjsLibrariesFile(techId);
		if (Utility.isEmpty(jslibraryFile)) {
			return Collections.emptyList();
		}
		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.getJSLibraries : Exit");
		}
		return null;
	}

	// TODO:Initialize only once on the constructor
	private RepositorySystem newRepositorySystem() throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.newRepositorySystem : Entry");
		}
		try {
			if (isDebugEnabled) {
				LOGGER.debug("RepositoryManagerImpl.newRepositorySystem() : Exit");
			}
			return new DefaultPlexusContainer().lookup(RepositorySystem.class);
		} catch (ComponentLookupException e) {
			if(isDebugEnabled) {
				LOGGER.error("RepositoryManagerImpl.newRepositorySystem", STATUS_FAILURE, MESSAGE_EQUALS + "\"" + e.getLocalizedMessage() + "\"");
			}
			throw new PhrescoException(e);
		} catch (PlexusContainerException e) {
			if(isDebugEnabled) {
				LOGGER.error("RepositoryManagerImpl.newRepositorySystem", STATUS_FAILURE, MESSAGE_EQUALS + "\"" + e.getLocalizedMessage() + "\"");
			}
			throw new PhrescoException(e);
		}
	}

	private RepositorySystemSession newRepositorySystemSession(RepositorySystem system) {
		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.newRepositorySystemSession : Entry");
		}

		if(isDebugEnabled) {
			if (system == null) {
				LOGGER.warn("RepositoryManagerImpl.newRepositorySystemSession", STATUS_BAD_REQUEST, "message=\"RepositorySystem is empty\"");
			}
			LOGGER.info("RepositoryManagerImpl.newRepositorySystemSession", "system=" + "\"" + system);
		}

		MavenRepositorySystemSession session = new MavenRepositorySystemSession();

		LocalRepository localRepo = new LocalRepository(LOCAL_REPO);
		session.setLocalRepositoryManager(system.newLocalRepositoryManager(localRepo));
		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.newRepositorySystemSession : Exit");
		}
		return session;
	}

	public boolean addArtifact(ArtifactInfo info, File artifactFile, String customerId) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.addArtifact : Entry");
		}

		if(isDebugEnabled) {
			if (StringUtils.isEmpty(customerId)) {
				LOGGER.warn(REPO_MGR_IMPL_ADD_ARTIFACT, STATUS_BAD_REQUEST, MESSAGE_CUST_ID_EMPTY);
				throw new PhrescoException(CUST_ID_EMPTY);
			}
			LOGGER.info(REPO_MGR_IMPL_ADD_ARTIFACT, CUSTOMER_ID_EQUALS + "\"" + customerId);
		}

		if(isDebugEnabled) {
			if (artifactFile == null) {
				LOGGER.warn(REPO_MGR_IMPL_ADD_ARTIFACT, STATUS_BAD_REQUEST, "message=\"artifactFile is empty\"");
				throw new PhrescoException("artifactFile is Empty");
			}
			LOGGER.info(REPO_MGR_IMPL_ADD_ARTIFACT, "artifactFile=" + "\"" + artifactFile);
		}

		DbManager dbManager = PhrescoServerFactory.getDbManager();
		RepoInfo repoInfo = dbManager.getRepoInfo(customerId);
		if(repoInfo == null) {
			throw new PhrescoException("Repo Information Not Available........");
		}
		String password = ServerUtil.decryptString(repoInfo.getRepoPassword());
		RepositorySystem system = newRepositorySystem();
		RepositorySystemSession session = newRepositorySystemSession(system);
		Artifact artifact = new DefaultArtifact(info.getGroupId(), info.getArtifact(), info.getClassifier(), info
				.getPackage(), info.getVersion());

		artifact = artifact.setFile(artifactFile);

		RemoteRepository distRepo = new RemoteRepository("", DEFAULT, repoInfo.getReleaseRepoURL());
		Authentication authentication = new Authentication(repoInfo.getRepoUserName(), password);
		distRepo.setAuthentication(authentication);
		DeployRequest deployRequest = addArtifactForDeploy(info, artifact);

		deployRequest.setRepository(distRepo);

		try {
			system.deploy(session, deployRequest);
		} catch (DeploymentException e) {
			if(isDebugEnabled) {
				LOGGER.error(REPO_MGR_IMPL_ADD_ARTIFACT, STATUS_FAILURE, MESSAGE_EQUALS + "\"" + e.getLocalizedMessage() + "\"");
			}
			throw new PhrescoException(e);
		}

		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.addArtifact : Exit");
		}

		return true;
	}

	private DeployRequest addArtifactForDeploy(ArtifactInfo info,
			Artifact artifact) {
		DeployRequest deployRequest = new DeployRequest();
		deployRequest.addArtifact(artifact);
		if (info.getPomFile() != null) {
			Artifact pom = new SubArtifact(artifact, null, "pom");
			pom = pom.setFile(info.getPomFile());
			deployRequest.addArtifact(pom);
		}
		return deployRequest;
	}

	@Override
	public String getLatestArtifact(String groupId, String artifactId) throws PhrescoException {

		RepositorySystem system = newRepositorySystem();

		RepositorySystemSession session = newRepositorySystemSession( system );

		Artifact artifact = new DefaultArtifact( groupId + ":" + artifactId + ":[0,)" );

		RemoteRepository distRepo = new RemoteRepository("", DEFAULT, "http://172.16.18.178:8080/nexus/content/repositories/releases/");

		VersionRangeRequest rangeRequest = new VersionRangeRequest();
		rangeRequest.setArtifact( artifact );
		rangeRequest.addRepository( distRepo );

		VersionRangeResult rangeResult;
		try {
			rangeResult = system.resolveVersionRange( session, rangeRequest );
			Version newestVersion = rangeResult.getHighestVersion();
			return newestVersion.toString();
		} catch (VersionRangeResolutionException e) {
			throw new PhrescoException(e);
		}
	}

	@Override
	public boolean isExist(String filePath, String customerId) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.isExist : Entry");
		}
		InputStream is = null;
		try {
			if(isDebugEnabled) {
				if (StringUtils.isEmpty(customerId)) {
					LOGGER.warn(REPO_MGR_IMPL_IS_EXIST, STATUS_BAD_REQUEST, MESSAGE_CUST_ID_EMPTY);
					throw new PhrescoException(CUST_ID_EMPTY);
				}
				LOGGER.info(REPO_MGR_IMPL_IS_EXIST, CUSTOMER_ID_EQUALS + "\"" + customerId);
			}
			RepoInfo repoInfo = getDBManager().getRepoInfo(customerId);
			URL url = new URL(repoInfo.getGroupRepoURL() + filePath);
			URLConnection openConnection = url.openConnection();
			int responseCode = ((HttpURLConnection) openConnection).getResponseCode();
			if (isDebugEnabled) {
				LOGGER.debug("RepositoryManagerImpl.isExist : Exit");
			}
			return (responseCode != HTTP_NOT_FOUND);
		} catch (IOException e) {
			if(isDebugEnabled) {
				LOGGER.error(REPO_MGR_IMPL_IS_EXIST, STATUS_FAILURE, MESSAGE_EQUALS + "\"" + e.getLocalizedMessage() + "\"");
			}
			throw new PhrescoException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					if(isDebugEnabled) {
						LOGGER.error(REPO_MGR_IMPL_IS_EXIST, STATUS_FAILURE, MESSAGE_EQUALS + "\"" + e.getLocalizedMessage() + "\"");
					}
					throw new PhrescoException(e);
				}
			}
		}
	}

	@Override
	public String getArtifactAsString(String filePath, String customerId) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.getArtifactAsString : Entry");
		}
		InputStream is = null;
		try {
			if(isDebugEnabled) {
				if (StringUtils.isEmpty(customerId)) {
					LOGGER.warn(REPO_MGR_IMPL_GET_ARTF_AS_STRING, STATUS_BAD_REQUEST, MESSAGE_CUST_ID_EMPTY);
					throw new PhrescoException(CUST_ID_EMPTY);
				}
				LOGGER.info(REPO_MGR_IMPL_GET_ARTF_AS_STRING, CUSTOMER_ID_EQUALS + "\"" + customerId);
			}
			RepoInfo repoInfo = getDBManager().getRepoInfo(customerId);
			URL url = new URL(repoInfo.getGroupRepoURL() + filePath);
			is = url.openStream();
			if (isDebugEnabled) {
				LOGGER.debug("RepositoryManagerImpl.getArtifactAsString : Exit");
			}
			return IOUtils.toString(is);
		} catch (IOException e) {
			if(isDebugEnabled) {
				LOGGER.error(REPO_MGR_IMPL_GET_ARTF_AS_STRING, STATUS_FAILURE, MESSAGE_EQUALS + "\"" + e.getLocalizedMessage() + "\"");
			}
			throw new PhrescoException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					if(isDebugEnabled) {
						LOGGER.error(REPO_MGR_IMPL_GET_ARTF_AS_STRING, STATUS_FAILURE, MESSAGE_EQUALS + "\"" + e.getLocalizedMessage() + "\"");
					}
					throw new PhrescoException(e);
				}
			}
		}
	}

	private static DbManager getDBManager() throws PhrescoException {
		PhrescoServerFactory.initialize();
		return PhrescoServerFactory.getDbManager();
	}

	@Override
	public InputStream getArtifactAsStream(String filePath, String customerId) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.getArtifactAsStream : Entry");
		}
		try {
			if(isDebugEnabled) {
				if (StringUtils.isEmpty(customerId)) {
					LOGGER.warn(REPO_MGR_IMPL_GET_ARTF_AS_STREAM, STATUS_BAD_REQUEST, MESSAGE_CUST_ID_EMPTY);
					throw new PhrescoException(CUST_ID_EMPTY);
				}
				LOGGER.info(REPO_MGR_IMPL_GET_ARTF_AS_STREAM, CUSTOMER_ID_EQUALS + "\"" + customerId);
			}
			RepoInfo repoInfo = getDBManager().getRepoInfo(customerId);
			URL url = new URL(repoInfo.getGroupRepoURL() + "/" + filePath);
			if (isDebugEnabled) {
				LOGGER.debug("RepositoryManagerImpl.getArtifactAsStream : Exit");
			}
			return url.openStream();
		} catch (MalformedURLException e) {
			if(isDebugEnabled) {
				LOGGER.error(REPO_MGR_IMPL_GET_ARTF_AS_STREAM, STATUS_FAILURE, MESSAGE_EQUALS + "\"" + e.getLocalizedMessage() + "\"");
			}
			throw new PhrescoException(e);
		} catch (IOException e) {
			if(isDebugEnabled) {
				LOGGER.error(REPO_MGR_IMPL_GET_ARTF_AS_STREAM, STATUS_FAILURE, MESSAGE_EQUALS + "\"" + e.getLocalizedMessage() + "\"");
			}
			throw new PhrescoException(e);
		}
	}

	// get highest service version
	@Override
	public String getFrameworkVersion() throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.getFrameworkVersion : Entry");
		}

		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.getFrameworkVersion : Exit");
		}
		return config.getLatestFrameworkVersion();
	}

	@Override
	public String getServerVersion() throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.getServerVersion : Entry");
		}
		if (isDebugEnabled) {
			LOGGER.debug("RepositoryManagerImpl.getServerVersion : Exit");
		}
		return config.getLatestServiceVersion();
	}

	public static boolean removeDirectory(File directory) {
		if (directory == null) {
			return false;
		}
		if (!directory.exists()) {
			return true;
		}
		if (!directory.isDirectory()) {
			return false;
		}

		String[] list = directory.list();

		// Some JVMs return null for File.list() when the
		// directory is empty.
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				File entry = new File(directory, list[i]);
				if (entry.isDirectory()) {
					if (!removeDirectory(entry)) {
						return false;
					}
				} else {
					if (!entry.delete()) {
						return false;
					}
				}
			}
		}

		return directory.delete();
	}

	@Override
	public String getCiConfigPath() throws PhrescoException {
		return config.getCiConfigFile();
	}

	@Override
	public String getCiSvnPath() throws PhrescoException {
		return config.getCiSvnFile();
	}

	@Override
	public String getJavaHomeConfigPath() throws PhrescoException {
		return config.getJavaHomeConfigFile();
	}

	@Override
	public String getMavenHomeConfigPath() throws PhrescoException {
		return config.getMavenHomeConfigFile();
	}

	@Override
	public String getCiCredentialXmlFilePath() throws PhrescoException {
		return config.getCiCredentialXmlFilePath();
	}

	public String getSettingConfigFile() throws PhrescoException {
		return config.getSettingConfigFile();
	}

	public String getHomePageJsonFile() throws PhrescoException {
		return config.getHomePageJsonFile();
	}

	public String getAdminConfigFile() throws PhrescoException {
		return config.getAdminConfigFile();
	}

	public String getCredentialFile() throws PhrescoException {
		return config.getCredentialFile();
	}

	public String getAuthServiceURL() throws PhrescoException {
		return config.getAuthServiceURL();
	}

	@Override
	public String getEmailExtFile() throws PhrescoException {
		return config.getEmailExtFile();
	} 

	@Override
	public String getFrameWorkLatestFile() throws PhrescoException {
		return config.getFrameWorkLatestFile();
	}

	@Override
	public RepoInfo createCustomerRepository(String customerId, String repoName) throws PhrescoException {
		String repoBaseURL = config.getRepoBaseURL();
		RepoInfo repoInfo = new RepoInfo();
		repoInfo.setCustomerId(customerId);
		repoInfo.setBaseRepoURL(repoBaseURL);
		repoInfo.setRepoUserName(config.getRepoUserName());
		repoInfo.setRepoPassword(config.getRepoPassword());
		repoInfo.setRepoName(repoName);
		String releaseRepo = createHostedRepo(repoName, repoBaseURL, REPOTYPE_RELEASE);
		if(StringUtils.isNotEmpty(releaseRepo)) {
			repoInfo.setReleaseRepoURL(releaseRepo);
		}
		String groupRepo = createGroupRepo(repoName, repoBaseURL, REPOTYPE_GROUP);
		if(StringUtils.isNotEmpty(groupRepo)) {
			repoInfo.setGroupRepoURL(groupRepo);
		}
		return repoInfo;
	}

	private String createGroupRepo(String repoName, String repoBaseURL, String repoType) throws PhrescoException {
		String repoURL = repoBaseURL + REPO_GROUP_CONTENT + repoName + repoType.toLowerCase();
		if(!isRepoAvailable(repoURL )) {
			Client client = new Client();
			client.addFilter(new HTTPBasicAuthFilter(config.getRepoUserName(), ServerUtil.decryptString(config.getRepoPassword())));
			WebResource resource = client.resource(repoBaseURL + REPO_GROUP_PATH);
			ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).
			post(ClientResponse.class, createGroupRepoData(repoName, repoBaseURL, repoType));
			if(response.getStatus() == MAGICNUMBER.RESPONSECODESUCCESS) {
				return repoURL;
			} else {
				throw new PhrescoException(REPO_FAILURE_MSG);
			}
		}
		return repoURL;
	}

	private String createHostedRepo(String customerId, String repoBaseURL,
			String repoType) throws PhrescoException {
		String repoURL = repoBaseURL + REPO_HOSTED_CONTENT + customerId + repoType.toLowerCase();
		if(!isRepoAvailable(repoURL )) {
			Client client = new Client();
			client.addFilter(new HTTPBasicAuthFilter(config.getRepoUserName(), ServerUtil.decryptString(config.getRepoPassword())));
			WebResource resource = client.resource(repoBaseURL + REPO_HOSTED_PATH);
			ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).
			post(ClientResponse.class, createReleaseRepoData(customerId, repoBaseURL, repoType));
			if(response.getStatus() == MAGICNUMBER.RESPONSECODESUCCESS) {
				return repoURL;
			} else {
				throw new PhrescoException(REPO_FAILURE_MSG);
			}
		}
		return repoURL;
	}

	private boolean isRepoAvailable(String repoURL) throws PhrescoException {
		URL url;
		try {
			url = new URL(repoURL);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			int rc = conn.getResponseCode();
			if(rc == RES_CODE_200) {
				return true;
			}
		} catch (MalformedURLException e) {
			throw new PhrescoException(e);
		} catch (IOException e) {
			throw new PhrescoException(e);
		}
		return false;
	}

	private String createReleaseRepoData(String customerId, String repoBaseURL, String repoType) {
		String repoId = customerId + repoType.toLowerCase();
		ReleaseRepo data =  new ReleaseRepo(repoBaseURL + REPO_HOSTED_CONTENT + repoId, repoId, repoId, REPO_PROVIDER, REPO_PROVIDER_ROLE, 
				REPO_PROVIDER, REPO_HOSTED, true, REPO_ALLOW_WRITE, true, true, NOT_FOUND_CACHE, repoType, false);
		String json = new Gson().toJson(data);
		return OPEN_PHRASE + SLASH + REPO_OBJECT_ID + SLASH + COLON + json + CLOSE_PHRASE;
	}

	private String createGroupRepoData(String repoName, String repoBaseURL, String repoType) {
		String repoId = repoName + repoType.toLowerCase();
		String releaseId = repoName + REPO_RELEASE_NAME;
		List<Repository> repositories = new ArrayList<Repository>();
		Repository repo = new Repository(releaseId, releaseId, repoBaseURL + LOCAL_REPO_GROUP + releaseId);
		repositories.add(repo);
		String phrescoRepoId = PHRESCO_REPO_NAME.toLowerCase();
		repo = new Repository(phrescoRepoId, PHRESCO_REPO_NAME, repoBaseURL + LOCAL_REPO_GROUP + phrescoRepoId);
		repositories.add(repo);
		GroupRepository data =  new GroupRepository(repoBaseURL + REPO_GROUP_CONTENT + repoId, repoId, repoId, REPO_PROVIDER, 
				REPO_PROVIDER, REPO_HOSTED, true, repositories);
		String json = new Gson().toJson(data);
		return OPEN_PHRASE + SLASH + REPO_OBJECT_ID + SLASH + COLON + json + CLOSE_PHRASE;
	}

	@Override
	public boolean deleteArtifact(String customerId, ArtifactGroup artifactGroup)
	throws PhrescoException {
		PhrescoServerFactory.initialize();
		DbManager dbManager = PhrescoServerFactory.getDbManager();
		RepoInfo repoInfo = dbManager.getRepoInfo(customerId);
		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(repoInfo.getRepoUserName(), ServerUtil.decryptString(repoInfo.getRepoPassword())));
		WebResource resource = client.resource(repoInfo.getBaseRepoURL() + REPO_LOCAL + 
				customerId + REPOTYPE_RELEASE.toLowerCase() + CONTENT + createFileUrl(artifactGroup));
		ClientResponse response = resource.delete(ClientResponse.class);
		if(response.getStatus() == MAGICNUMBER.RESPONSECODEERROR) {
			return true;
		}
		return false;
	}

	private String createFileUrl(ArtifactGroup artifactGroup) {
		return artifactGroup.getGroupId().replace(DOT, FORWARD_SLASH) + FORWARD_SLASH + artifactGroup.getArtifactId() + FORWARD_SLASH + 
		artifactGroup.getVersions().get(0).getVersion();
	}

	@Override
	public boolean deleteCustomerRepo(String customerId) throws PhrescoException {
		PhrescoServerFactory.initialize();
		DbManager dbManager = PhrescoServerFactory.getDbManager();
		RepoInfo repoInfo = dbManager.getRepoInfo(customerId);
		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(repoInfo.getRepoUserName(), ServerUtil.decryptString(repoInfo.getRepoPassword())));
		WebResource resource = client.resource(repoInfo.getBaseRepoURL() + REPO_LOCAL + 
				customerId + REPOTYPE_RELEASE.toLowerCase());
		ClientResponse response = resource.delete(ClientResponse.class);
		if(response.getStatus() != MAGICNUMBER.RESPONSECODEERROR) {
			throw new PhrescoException("Repository Deletion Failed");
		}
		WebResource resource1 = client.resource(repoInfo.getBaseRepoURL() + REPO_LOCAL + 
				customerId + REPOTYPE_SNAPSHOT.toLowerCase());
		ClientResponse response1 = resource1.delete(ClientResponse.class);
		if(response1.getStatus() != MAGICNUMBER.RESPONSECODEERROR) {
			throw new PhrescoException("Repository Deletion Failed");
		}
		WebResource resource2 = client.resource(repoInfo.getBaseRepoURL() + REPO_GROUPURL + 
				customerId + REPOTYPE_GROUP.toLowerCase());
		ClientResponse response2 = resource2.delete(ClientResponse.class);
		if(response2.getStatus() != MAGICNUMBER.RESPONSECODEERROR) {
			throw new PhrescoException("Repository Deletion Failed");
		}
		return true;
	}

	private void resolveArtifact() throws ArtifactResolutionException, PhrescoException, NoRepositoryConnectorException {
		RepositorySystem system = newRepositorySystem();
		RepositorySystemSession session = newRepositorySystemSession(system);
		Artifact artifact = new DefaultArtifact( "org.sonatype.aether:aether-util:1.9" );
		RemoteRepository repo = new RemoteRepository();
		Authentication authentication = new Authentication("admin", "devrepo2");
		repo.setAuthentication(authentication);
		repo.setUrl("http://172.16.17.226:8080/repository/content/repositories/releases/");
		ArtifactRequest artifactRequest = new ArtifactRequest();
		artifactRequest.setArtifact( artifact );
		artifactRequest.addRepository( repo );
		ArtifactResult artifactResult = system.resolveArtifact( session, artifactRequest );
		artifactResult.isMissing();
	}

	public static void main(String[] args) throws PhrescoException, ArtifactResolutionException, NoRepositoryConnectorException {
		ServerConfiguration config = new ServerConfiguration();
		RepositoryManagerImpl impl = new RepositoryManagerImpl(config);
		impl.resolveArtifact();
	}
}
