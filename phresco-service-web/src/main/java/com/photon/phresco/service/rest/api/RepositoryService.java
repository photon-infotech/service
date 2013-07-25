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
/*
 * ###
\ * Service Web Archive
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
package com.photon.phresco.service.rest.api;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.DbManager;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.util.ServiceConstants;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/repo")
public class RepositoryService {
    
    private static RepositoryManager repoMgr = null;
    
    public RepositoryService() throws PhrescoException {
        PhrescoServerFactory.initialize();
        repoMgr = PhrescoServerFactory.getRepositoryManager();
    }
    
	@GET
	@Path("/ci/config")
	@Produces(MediaType.TEXT_PLAIN_VALUE)
	public String getCiConfigPath(@QueryParam(ServiceConstants.REST_QUERY_CUSTOMERID) String customerId) throws PhrescoException {
		PhrescoServerFactory.initialize();
		DbManager dbMgr = PhrescoServerFactory.getDbManager();
		RepositoryManager repoMngr = PhrescoServerFactory.getRepositoryManager();
		return dbMgr.getRepoInfo(customerId).getGroupRepoURL() + repoMngr.getCiConfigPath();
	}

	@GET
	@Path("/ci/svn")
	@Produces(MediaType.TEXT_PLAIN_VALUE)
	public String getCiSvnPath(@QueryParam(ServiceConstants.REST_QUERY_CUSTOMERID) String customerId) throws PhrescoException {
		PhrescoServerFactory.initialize();
		DbManager dbMgr = PhrescoServerFactory.getDbManager();
        RepositoryManager repoMngr = PhrescoServerFactory.getRepositoryManager();
		return dbMgr.getRepoInfo(customerId).getGroupRepoURL() + repoMngr.getCiSvnPath();
	}

	@GET
	@Path("/ci/credentialsxml")
	@Produces(MediaType.APPLICATION_XML_VALUE)
	public InputStream getCredentialXmlFile(@QueryParam(ServiceConstants.REST_QUERY_CUSTOMERID) String customerId) throws PhrescoException {
		PhrescoServerFactory.initialize();
		repoMgr.getCiCredentialXmlFilePath();
		return repoMgr.getArtifactAsStream(repoMgr.getCiCredentialXmlFilePath(), customerId);
	}
	
	@GET
	@Path("/ci/javahomexml")
	@Produces(MediaType.APPLICATION_XML_VALUE)
	public InputStream getJavaHomeXmlFile(@QueryParam(ServiceConstants.REST_QUERY_CUSTOMERID) String customerId) throws PhrescoException {
		repoMgr.getJavaHomeConfigPath();
		return repoMgr.getArtifactAsStream(repoMgr.getJavaHomeConfigPath(), customerId);
	}
	
	@GET
	@Path("/ci/mavenhomexml")
	@Produces(MediaType.APPLICATION_XML_VALUE)
	public InputStream getMavenHomeXmlFile(@QueryParam(ServiceConstants.REST_QUERY_CUSTOMERID) String customerId) throws PhrescoException {
		repoMgr.getMavenHomeConfigPath();
		return repoMgr.getArtifactAsStream(repoMgr.getMavenHomeConfigPath(), customerId);
	}
	
	@GET
	@Path("/ci/mailxml")
	@Produces({ MediaType.APPLICATION_XML_VALUE })
	public InputStream getProducts(@QueryParam(ServiceConstants.REST_QUERY_CUSTOMERID) String customerId) throws PhrescoException {
		try {
	        return repoMgr.getArtifactAsStream(repoMgr.getCredentialFile(), customerId);
		} catch (Exception e) {
            throw new PhrescoException(e);
        }
	}
	
	@GET
	@Path("/ci/emailext")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public InputStream getEmailExtFile(@QueryParam(ServiceConstants.REST_QUERY_CUSTOMERID) String customerId) throws PhrescoException {
	    try {
	        return repoMgr.getArtifactAsStream(repoMgr.getEmailExtFile(), customerId);
	    } catch (Exception e) {
            throw new PhrescoException(e);
        }
	}
	
	@ApiOperation(value = " Get Latest Version content to update")
    @RequestMapping(value= "/update", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public @ResponseBody InputStream getLatestPom(@ApiParam(value = "Customerid", 
			name = ServiceConstants.REST_QUERY_CUSTOMERID)@QueryParam(ServiceConstants.REST_QUERY_CUSTOMERID) String customerId) 
			throws PhrescoException {
	    try {
	        return repoMgr.getArtifactAsStream(repoMgr.getFrameWorkLatestFile(), customerId);
	    } catch (Exception e) {
            throw new PhrescoException(e);
        }
	}
	
}
