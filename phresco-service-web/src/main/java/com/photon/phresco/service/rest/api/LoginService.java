/**
 * Service Web Archive
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
package com.photon.phresco.service.rest.api;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.photon.phresco.commons.model.Customer;
import com.photon.phresco.commons.model.User;
import com.photon.phresco.commons.model.User.AuthType;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.api.DbManager;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.converters.ConvertersFactory;
import com.photon.phresco.service.dao.CustomerDAO;
import com.photon.phresco.service.impl.DbService;
import com.photon.phresco.service.model.ServerConfiguration;
import com.photon.phresco.service.rest.util.AuthenticationUtil;
import com.photon.phresco.service.util.ServerConstants;
import com.photon.phresco.service.util.ServerUtil;
import com.photon.phresco.util.Credentials;
import com.photon.phresco.util.ServiceConstants;
import com.photon.phresco.util.Utility;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = ServiceConstants.REST_API_LOGIN, consumes = MediaType.APPLICATION_JSON_VALUE, 
		produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginService extends DbService {
	
	private final String SERVICE_VIEW_ROLE_ID = "4e8c0bed7-fb39-4erta-ae73-2d1286ae4ad0";
	private final String FRAMEWORK_VIEW_ROLE_ID = "4e8c0bd7-fb39-4aea-ae73-2d1286ae4ae0";
	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger("SplunkLogger");
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();
	
	public LoginService() {
		super();
	}
	
	@ApiOperation(value = " Login service ")
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody User login(HttpServletRequest request, HttpServletResponse response,
    		@ApiParam(value = "user credentials to login", name = "credentials")@RequestBody Credentials credentials) throws PhrescoException {
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
        user = loginUsingAuth(credentials, response);
        if(isDebugEnabled) {
        	LOGGER.debug("LoginService.createProject : Exit");
		}
		return user;
	}

	@ApiOperation(value = " Login service ")
	@RequestMapping(value= "/changePassword", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public @ResponseBody Boolean resetPassword(HttpServletRequest request, HttpServletResponse response,
			@ApiParam(value = "old credentials", name = "credentials")@RequestBody List<Credentials> credentials
	) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("LoginService.login : Entry");
			LOGGER.debug("LoginService.login ", "remoteAddress=" + request.getRemoteAddr() , "userName=" + credentials.get(0).getUsername(),
					"endpoint=" + request.getRequestURI(),  "method=" + request.getMethod());
		}
		Credentials oldCred = credentials.get(0);
		Credentials newCred = credentials.get(1);
		User user = null;
		PhrescoServerFactory.initialize();
		DbManager dbManager = PhrescoServerFactory.getDbManager();
		
		user = dbManager.authenticate(oldCred.getUsername(), oldCred.getPassword());
		if(user != null) {
			if(!user.getAuthType().equals(AuthType.LOCAL)) {
				return false;
			}
			user.setPassword(ServerUtil.encodeUsingHash(newCred.getUsername(), 
					ServerUtil.decryptString(newCred.getPassword())));
			DbService.getMongoOperationMaster().save(USERS_COLLECTION_NAME, user);
			return true;
		}
		return false;
	}


	@ApiOperation(value = " Login service ")
	@RequestMapping(value= "/forgotPassword", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public @ResponseBody Boolean forgotPasword(HttpServletRequest request, HttpServletResponse response,@ApiParam(value = "userId", 
			name = "userId") @QueryParam("userId") String userId, @ApiParam(value = "custId", name = "custId") @QueryParam("custId") String custId) throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("LoginService.forgotPassword : Entry");

		}
		PhrescoServerFactory.initialize();
		DbManager dbManager = PhrescoServerFactory.getDbManager();
		User user = dbManager.authenticateUserId(userId);
		if (user != null ) {
			if(!user.getAuthType().equals(AuthType.LOCAL)) {
				return false;
			}
			List<Customer> customers = new ArrayList<Customer>();
			Converter<CustomerDAO, Customer> converter = (Converter<CustomerDAO, Customer>) ConvertersFactory.getConverter(CustomerDAO.class);
			List<CustomerDAO> customerDAOs = DbService.getMongoOperation().
			find(CUSTOMERS_COLLECTION_NAME, new Query(Criteria.where(REST_QUERY_CONTEXT).is(custId)), CustomerDAO.class);
			for (CustomerDAO customerDAO : customerDAOs) {
				Customer convertDAOToObject = converter.convertDAOToObject(customerDAO, DbService.getMongoOperation());
				customers.add(convertDAOToObject);
			}
			Customer customer = customers.get(0);
			UUID uniqueId = UUID.randomUUID();
			String newPwd = uniqueId.toString();
			String body = "New password is "+newPwd;
			String screen = "Screenshot";
			String[] mailIds = new String[] {user.getEmail()};
			Utility.sendTemplateEmail(mailIds, customer.getSupportEmail(),"test","Phresco new password", body, customer.getSupportEmail(), customer.getSupportPassword(),customer.getSupportSmtpHost(),screen,"test");
			user.setPassword(ServerUtil.encodeUsingHash(user.getName(), newPwd));	
			DbService.getMongoOperationMaster().save(USERS_COLLECTION_NAME, user);
			return true;
		} else {
			throw new PhrescoException("Null user");
		}
	}

	private User loginUsingAuth(Credentials credentials, HttpServletResponse response) throws PhrescoException {
		ServerConfiguration serverConfig = PhrescoServerFactory.getServerConfig();
		Client client = Client.create();
		PhrescoServerFactory.initialize();
		User user = null; 
		RepositoryManager repoMgr = PhrescoServerFactory.getRepositoryManager();
		boolean checkLDAPAvailable = checkLDAPAvailable(repoMgr);
		if(!checkLDAPAvailable) {
			response.setStatus(204);
			return user;
		}
		WebResource resource = client.resource(repoMgr.getAuthServiceURL() + ServerConstants.AUTHENTICATE);
        resource.accept(MediaType.APPLICATION_JSON_VALUE);
        ClientResponse clientResponse = resource.type(MediaType.APPLICATION_JSON_VALUE).post(ClientResponse.class, credentials);
        if(clientResponse.getStatus() == 204) {
        	response.setStatus(204);
        	LOGGER.info("LoginService.loginUsingAuth", "authUrl=" + repoMgr.getAuthServiceURL(), "userName=" + credentials.getUsername());
        	return null;
        }
        GenericType<User> genericType = new GenericType<User>() {};
        user = clientResponse.getEntity(genericType);
        user.setToken(createAuthToken(credentials.getUsername()));
        user.setPhrescoEnabled(true);
        user.setValidLogin(true);
        user.setRoleIds(serverConfig.getDefaultRoles());
        if(user != null) {
        	user.setCreationDate(new Date());
        	user.setPassword(ServerUtil.encodeUsingHash(credentials.getUsername(), 
        			ServerUtil.decryptString(credentials.getPassword())));
        	user.setAuthType(AuthType.AUTHSERVICE);
        	DbService.getMongoOperation().save(USERS_COLLECTION_NAME, user);
        }
        user.setCustomers(findCustomersFromDB());
        return user;
	}

	private boolean checkLDAPAvailable(RepositoryManager repoMgr) {
		boolean isAlive = true;
		try {
			URL url = new URL(repoMgr.getAuthServiceURL());
			URLConnection connection = url.openConnection();
			connection.connect();
		} catch (Exception e) {
			isAlive = false;
		}
		return isAlive;
	}
	
	private String createAuthToken(String userName) throws PhrescoException {
		AuthenticationUtil authTokenUtil = AuthenticationUtil.getInstance();
		return authTokenUtil.generateToken(userName);
	}
}
