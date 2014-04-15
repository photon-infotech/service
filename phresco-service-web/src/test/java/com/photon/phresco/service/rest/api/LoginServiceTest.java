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

import java.util.List;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import com.photon.phresco.commons.model.User;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.client.impl.ClientHelper;
import com.photon.phresco.util.Credentials;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

public class LoginServiceTest {

//	@Test
	public void testLogin() throws PhrescoException {
		String userName = "kumar_s";
        String password = "Phresco@123";
        byte[] encodeBase64 = Base64.encodeBase64(password.getBytes());
        String encodedPassword = new String(encodeBase64);
        PhrescoServerFactory.initialize();
        RepositoryManager repoMgr = PhrescoServerFactory.getRepositoryManager();
        Credentials credentials = new Credentials(userName, encodedPassword); 
    	Client client = ClientHelper.createClient();
        WebResource resource = client.resource(repoMgr.getAuthServiceURL() + "/ldap/authenticate");
        resource.accept(MediaType.APPLICATION_JSON);
        ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, credentials);
        GenericType<User> genericType = new GenericType<User>() {};
        User user = response.getEntity(genericType);
        Assert.assertEquals(true, user.isPhrescoEnabled());
	}
	
}
