/**
 * Service Web Archive
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
package com.photon.phresco.service.admin.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.LogInfo;
import com.photon.phresco.commons.model.Technology;
import com.photon.phresco.commons.model.User;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.exception.PhrescoWebServiceException;
import com.photon.phresco.service.admin.commons.ServiceActions;
import com.photon.phresco.service.admin.commons.ServiceUIConstants;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.client.api.ServiceClientConstant;
import com.photon.phresco.service.client.api.ServiceContext;
import com.photon.phresco.service.client.api.ServiceManager;
import com.photon.phresco.service.client.factory.ServiceClientFactory;
import com.photon.phresco.service.model.FileInfo;
import com.photon.phresco.service.util.ServerUtil;
import com.photon.phresco.util.ServiceConstants;

public class ServiceBaseAction extends ActionSupport implements ServiceActions, ServiceUIConstants, ServiceClientConstant, ServiceConstants {
	private static final Logger S_LOGGER = Logger.getLogger(ServiceBaseAction.class);
	private static Boolean debugEnabled = S_LOGGER.isDebugEnabled();

    private static final long serialVersionUID = 1L;
    
    private static ServiceManager serviceManager = null;
    
    private String copyToClipboard = "";
    
    private String fileName = "";
    
    private String moduleName = "";
    
    private Long fileSize = null;
    
    protected ServiceManager getServiceManager() {
		return serviceManager;
	}

	protected User doLogin(String userName, String password)  {
		try {
		String adminServiceURL = PhrescoServerFactory.getServerConfig().getAdminServiceURL();
		StringBuilder serverURL = new StringBuilder();
		serverURL.append(getHttpRequest().getScheme());
		serverURL.append(COLON_DOUBLE_SLASH);
		serverURL.append("localhost");
		serverURL.append(COLON);
		serverURL.append(getHttpRequest().getServerPort());
		serverURL.append(getHttpRequest().getContextPath());
		serverURL.append(SLASH_REST_SLASH_API);
    	ServiceContext context = new ServiceContext();
		context.put(SERVICE_URL, adminServiceURL);
		context.put(SERVICE_USERNAME, userName);
		context.put(SERVICE_PASSWORD, password);
		serviceManager = ServiceClientFactory.getServiceManager(context);
		} catch (PhrescoWebServiceException ex) {
			ex.printStackTrace();
            S_LOGGER.error(ex.getMessage());
            throw new PhrescoWebServiceException(ex.getResponse());
        } catch (PhrescoException e) {
        	S_LOGGER.error(e.getMessage());
            throw new PhrescoWebServiceException(e);
		}
		return serviceManager.getUserInfo();
    }
	
	protected String showErrorPopup(PhrescoException e, String action) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stacktrace = sw.toString();
            User userInfo = (User) getHttpSession().getAttribute(SESSION_USER_INFO);
            LogInfo log = new LogInfo();
            log.setMessage(e.getLocalizedMessage());
            log.setTrace(stacktrace);
            log.setAction(action);
            log.setUserId(userInfo.getLoginId());
            setReqAttribute(REQ_LOG_REPORT, log);
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    //Do nothing due to error popup
                }
            }
        }
        return LOG_ERROR;
	}
	
	//To get byte array of uploaded jar
	protected byte[] getByteArray() throws PhrescoException {
    	PrintWriter writer = null;
    	byte[] byteArray = null;
		try {
            writer = getHttpResponse().getWriter();
	        fileName = getHttpRequest().getHeader(X_FILE_NAME);
	        moduleName = getHttpRequest().getHeader("Module-Name");
	        String size = getHttpRequest().getHeader("Content-Length");
	        fileSize = Long.parseLong(size);
        	InputStream is = getHttpRequest().getInputStream();
        	byteArray = IOUtils.toByteArray(is);
		} catch (Exception e) {
			getHttpResponse().setStatus(getHttpResponse().SC_INTERNAL_SERVER_ERROR);
            writer.print(SUCCESS_FALSE);
			throw new PhrescoException(e);
		}
		return byteArray;
	}
	
	// To get groupId, artfId, version from byteArray
	protected ArtifactGroup getArtifactGroupInfo(PrintWriter writer, byte[] tempByteArray) throws PhrescoException {
		ArtifactGroup artifactGroupInfo = 
			ServerUtil.getArtifactinfo(new ByteArrayInputStream(tempByteArray), ServerUtil.getFileExtension(getFileName()));
		FileInfo fileInfo = new FileInfo();
		getHttpResponse().setStatus(getHttpResponse().SC_OK);
		if (artifactGroupInfo != null) {
			fileInfo.setArtifactId(artifactGroupInfo.getArtifactId());
			fileInfo.setGroupId(artifactGroupInfo.getGroupId());
			List<ArtifactInfo> versions = artifactGroupInfo.getVersions();
			fileInfo.setVersion(versions.get(0).getVersion());
			fileInfo.setMavenJar(true);
			fileInfo.setSuccess(true);
			Gson gson = new Gson();
			String json = gson.toJson(fileInfo);
			writer.print(json);
		} else {
			writer.print(MAVEN_JAR_FALSE);
		}

		return artifactGroupInfo;
	}
	
	// To set groupId, artfId, version in ArtifactGroup
	protected ArtifactGroup getArtifactGroupInfo(String name, String artfactId, String groupId, String packaging, String version, String customerId) {
		ArtifactGroup artifactGroup = new ArtifactGroup();
		artifactGroup.setName(name);
        artifactGroup.setArtifactId(artfactId);
        artifactGroup.setGroupId(groupId);
        artifactGroup.setPackaging(packaging);
        artifactGroup.setCustomerIds(Arrays.asList(customerId));
        List<ArtifactInfo> artifactVersion = new ArrayList<ArtifactInfo>();
        ArtifactInfo artifactInfo = new ArtifactInfo();
        artifactInfo.setVersion(version);
        artifactVersion.add(artifactInfo);
        artifactGroup.setVersions(artifactVersion);
        
        return artifactGroup;
	}
	
    public void copyToClipboard () {
    	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    	clipboard.setContents(new StringSelection(copyToClipboard.replaceAll(" ", "").replaceAll("(?m)^[ \t]*\r?\n", "")), null);
    }
    
    public static Comparator<ArtifactGroup> ARTIFACTGROUP_COMPARATOR_ASCEND  = new Comparator<ArtifactGroup>() {

		public int compare(ArtifactGroup artiFirst, ArtifactGroup artiSecond) {
			String artiNameFirst = artiFirst.getName().toUpperCase();
			String artiNameSecond = artiSecond.getName().toUpperCase();
			//ascending order
			return artiNameFirst.compareTo(artiNameSecond);
		}
	};
	
	 public static Comparator<ArtifactInfo> ARTIFACTINFO__COMPARATOR_DESCEND  = new Comparator<ArtifactInfo>() {

		public int compare(ArtifactInfo artiFirst, ArtifactInfo artiSecond) {
			String artiNameFirst = artiFirst.getVersion();
			String artiNameSecond = artiSecond.getVersion();
			//descending  order
			return artiNameSecond.compareTo(artiNameFirst);
		}
	};
	
	public static Comparator<Technology> TECHNAME_COMPARATOR  = new Comparator<Technology>() {

		public int compare(Technology techFirst, Technology techSecond) {
			String techNameFirst = techFirst.getName().toUpperCase();
			String techNameSecond = techSecond.getName().toUpperCase();
			//ascending order
			return techNameFirst.compareTo(techNameSecond);
		}
	};
	
	protected void setReqAttribute(String key, Object value) {
	    getHttpRequest().setAttribute(key, value);
	}
	
	protected Object getReqAttribute(String key) {
        return getHttpRequest().getAttribute(key);
    }
	
	protected void setSessionAttribute(String key, Object value) {
	    getHttpSession().setAttribute(key, value);
    }
	
	protected void removeSessionAttribute(String key) {
	    getHttpSession().removeAttribute(key);
	}
    
    protected Object getSessionAttribute(String key) {
        return getHttpSession().getAttribute(key);
    }
    
	protected String getReqParameter(String key) {
		return getHttpRequest().getParameter(key);
	}
	
	protected String[] getReqParameterValues(String key) {
		return getHttpRequest().getParameterValues(key);
	}
    
    protected HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
    }
    
    protected HttpSession getHttpSession() {
        return getHttpRequest().getSession();
    }
    
    protected HttpServletResponse getHttpResponse() {
        return (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
    }

    public void setCopyToClipboard(String copyToClipboard) {
		this.copyToClipboard = copyToClipboard;
	}

	public String getCopyToClipboard() {
		return copyToClipboard;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Long getFileSize() {
		return fileSize;
	}
}