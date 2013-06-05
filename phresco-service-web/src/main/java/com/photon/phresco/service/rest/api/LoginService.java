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
package com.photon.phresco.service.rest.api;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.photon.phresco.commons.model.User;
import com.photon.phresco.commons.model.User.AuthType;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.exception.PhrescoWebServiceException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.api.DbManager;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.impl.DbService;
import com.photon.phresco.service.rest.util.AuthenticationUtil;
import com.photon.phresco.service.util.ServerConstants;
import com.photon.phresco.service.util.ServerUtil;
import com.photon.phresco.util.Credentials;
import com.photon.phresco.util.ServiceConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

@Path(ServiceConstants.REST_API_LOGIN)
public class LoginService extends DbService {
	
	private final String SERVICE_VIEW_ROLE_ID = "4e8c0bed7-fb39-4erta-ae73-2d1286ae4ad0";
	private final String FRAMEWORK_VIEW_ROLE_ID = "4e8c0bed7-fb39-4aea-ae73-2d1286ae4ad0";
	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(LoginService.class.getName());
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();
	
	public LoginService() {
		super();
	}

	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User login(@Context HttpServletRequest request, Credentials credentials) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("LoginService.login : Entry");
			LOGGER.debug("LoginService.login ", "remoteAddress=" + request.getRemoteAddr() , "userName=" + credentials.getUsername(),
					"endpoint=" + request.getRequestURI(),  "method=" + request.getMethod());
		}
		User user = null;
		PhrescoServerFactory.initialize();
		DbManager dbManager = PhrescoServerFactory.getDbManager();
		user = dbManager.authenticate(credentials.getUsername(), credentials.getPassword());
        if(user != null){
        	user.setValidLogin(true);
        	user.setToken(createAuthToken(credentials.getUsername()));
        	user.setPhrescoEnabled(true);
            user.setValidLogin(true);
            user.setCustomers(findCustomersFromDB());
        	return user;
        }
        user = loginUsingAuth(credentials);
        if(isDebugEnabled) {
        	LOGGER.debug("LoginService.createProject : Exit");
		}
		return user;
    }
	
	private User loginUsingAuth(Credentials credentials) throws PhrescoException {
		Client client = Client.create();
		PhrescoServerFactory.initialize();
		RepositoryManager repoMgr = PhrescoServerFactory.getRepositoryManager();
		WebResource resource = client.resource(repoMgr.getAuthServiceURL() + ServerConstants.AUTHENTICATE);
		
        resource.accept(MediaType.APPLICATION_JSON_TYPE);
        ClientResponse response = resource.type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, credentials);
        if(response.getStatus() == 204) {
        	LOGGER.info("LoginService.loginUsingAuth", "authUrl=" + repoMgr.getAuthServiceURL(), "userName=" + credentials.getUsername());
        	throw new PhrescoWebServiceException(Response.status(Status.NO_CONTENT).build());
        }
        GenericType<User> genericType = new GenericType<User>() {};
        User user = response.getEntity(genericType);
        user.setToken(createAuthToken(credentials.getUsername()));
        user.setPhrescoEnabled(true);
        user.setValidLogin(true);
        user.setRoleIds(Arrays.asList(SERVICE_VIEW_ROLE_ID, FRAMEWORK_VIEW_ROLE_ID));
        if(user != null) {
        	user.setCreationDate(new Date());
        	user.setPassword(ServerUtil.encodeUsingHash(credentials.getUsername(), 
        			ServerUtil.decryptString(credentials.getPassword())));
        	user.setAuthType(AuthType.LOCAL);
        	mongoOperation.save(USERS_COLLECTION_NAME, user);
        }
        user.setCustomers(findCustomersFromDB());
        return user;
	}
	
	private String createAuthToken(String userName) throws PhrescoException {
		AuthenticationUtil authTokenUtil = AuthenticationUtil.getInstance();
	    return authTokenUtil.generateToken(userName);
	}
}
