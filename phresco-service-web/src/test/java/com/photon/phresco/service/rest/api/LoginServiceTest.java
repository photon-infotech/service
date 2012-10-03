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

	@Test
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
