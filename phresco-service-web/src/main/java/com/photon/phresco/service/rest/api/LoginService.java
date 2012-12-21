/*
 * ###
 * Service Web Archive
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

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.photon.phresco.commons.model.User;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.DbManager;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.impl.DbService;
import com.photon.phresco.service.rest.util.AuthenticationUtil;
import com.photon.phresco.service.util.ServerConstants;
import com.photon.phresco.util.Credentials;
import com.photon.phresco.util.ServiceConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

@Path(ServiceConstants.REST_API_LOGIN)
public class LoginService extends DbService {
	
	public LoginService() {
		super();
	}

//	@POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public User login(Credentials credentials) throws PhrescoException {
//		System.out.println("In Login Service");
//        User user = new User();
//        user.setLoginId("demo_user");
//        user.setEmail("demo_user@photon.in");
//        user.setFirstName("Demo");
//        user.setLastName("User");
//        user.setDisplayName("Demo User");
//        
//        AuthenticationUtil authTokenUtil = AuthenticationUtil.getInstance();
//        user.setToken(authTokenUtil.generateToken(credentials.getUsername()));
//        user.setPhrescoEnabled(true);
//        return user;
//	}
	
	
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User login(Credentials credentials) throws PhrescoException {
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
		return loginUsingAuth(credentials);
    }
	
	private User loginUsingAuth(Credentials credentials) throws PhrescoException {
		Client client = Client.create();
		PhrescoServerFactory.initialize();
		RepositoryManager repoMgr = PhrescoServerFactory.getRepositoryManager();
		WebResource resource = client.resource(repoMgr.getAuthServiceURL() + ServerConstants.AUTHENTICATE);
		
        resource.accept(MediaType.APPLICATION_JSON_TYPE);
        ClientResponse response = resource.type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, credentials);
        GenericType<User> genericType = new GenericType<User>() {};
        User user = response.getEntity(genericType);
        user.setToken(createAuthToken(credentials.getUsername()));
        user.setPhrescoEnabled(true);
        user.setValidLogin(true);
        user.setCustomers(findCustomersFromDB());
        return user;
	}
	
	private String createAuthToken(String userName) throws PhrescoException {
		AuthenticationUtil authTokenUtil = AuthenticationUtil.getInstance();
	    return authTokenUtil.generateToken(userName);
	}
}
