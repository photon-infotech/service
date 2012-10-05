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
import com.photon.phresco.service.api.DbService;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.util.AuthenticationUtil;
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
		Client client = Client.create();
		PhrescoServerFactory.initialize();
		RepositoryManager repoMgr = PhrescoServerFactory.getRepositoryManager();
		WebResource resource = client.resource(repoMgr.getAuthServiceURL() + ServerConstants.AUTHENTICATE);
		
        resource.accept(MediaType.APPLICATION_JSON_TYPE);
        ClientResponse response = resource.type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, credentials);
        GenericType<User> genericType = new GenericType<User>() {};
        User user = response.getEntity(genericType);
        AuthenticationUtil authTokenUtil = AuthenticationUtil.getInstance();
        user.setToken(authTokenUtil.generateToken(credentials.getUsername()));
        user.setPhrescoEnabled(true);
        user.setValidLogin(true);
        return user;
        
        
//        UserDAO userDao = mongoOperation.findOne(ServiceConstants.USERDAO_COLLECTION_NAME, 
//                new Query(Criteria.whereId().is(user.getName())), UserDAO.class);
//        user.setId(user.getName());
//        Converter<UserDAO, User> converter = (Converter<UserDAO, User>) ConvertersFactory.getConverter(UserDAO.class);
//        User convertedUser = new User();
//        if(userDao != null) {
//        	convertedUser = converter.convertDAOToObject(userDao, mongoOperation);
//        }
////        List<Customer> customers = mongoOperation.getCollection(ServiceConstants.CUSTOMERS_COLLECTION_NAME , Customer.class);
////        List<Customer> customers = new ArrayList<Customer>();
////        Converter<CustomerDAO, Customer> customerConverter = 
////            (Converter<CustomerDAO, Customer>) ConvertersFactory.getConverter(CustomerDAO.class);
////        List<CustomerDAO> customerDAOs = mongoOperation.getCollection(ServiceConstants.CUSTOMERDAO_COLLECTION_NAME , CustomerDAO.class);
////        for (CustomerDAO customerDAO : customerDAOs) {
////            customers.add(customerConverter.convertDAOToObject(customerDAO, mongoOperation));
////        }
//        
//        AuthenticationUtil authTokenUtil = AuthenticationUtil.getInstance();
//        convertedUser.setLoginId(credentials.getUsername());
//        convertedUser.setToken(authTokenUtil.generateToken(credentials.getUsername()));
//        convertedUser.setDisplayName(user.getDisplayName());
//        convertedUser.setPhrescoEnabled(user.isPhrescoEnabled());
//        convertedUser.setCustomers(customers);
//        return convertedUser;
    }
	
}
