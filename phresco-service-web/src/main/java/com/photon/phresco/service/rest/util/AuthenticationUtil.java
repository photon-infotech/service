package com.photon.phresco.service.rest.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.util.MAGICNUMBER;
public class AuthenticationUtil {
	
	private static final String AUTH_TOKEN_CACHE_TIME = "auth.token.cache.ttl";
	private static AuthenticationUtil authTokenUtil = null;
	private static Cache<String, String> tokenCache = null;

	public static AuthenticationUtil getInstance() throws PhrescoException {
		return getInstance(null);
	}
	
	public static AuthenticationUtil getInstance(InputStream is) throws PhrescoException {
		if (authTokenUtil == null) {
			authTokenUtil = new AuthenticationUtil(is);
		}
		return authTokenUtil;
	}
	
	private AuthenticationUtil(InputStream is) throws PhrescoException {
		InputStream tempIs = null;
		tempIs = is;
		try {
			Properties properties = new Properties();
			if (tempIs == null) {
				tempIs = this.getClass().getClassLoader().getResourceAsStream("server.config");
			}
			properties.load(tempIs);
			String cacheTTL = properties.getProperty(AUTH_TOKEN_CACHE_TIME); 
			Long tokenIdleTime = Long.parseLong(cacheTTL);
			tokenCache = CacheBuilder.newBuilder().expireAfterAccess(tokenIdleTime, TimeUnit.MINUTES).removalListener(
					new RemovalListener() 
					{
						@Override
						public void onRemoval(RemovalNotification notification) {
						}
					}).build();
		} catch (NumberFormatException e) {
			throw new PhrescoException(e);
		} catch (FileNotFoundException e) {
			throw new PhrescoException(e);
		} catch (IOException e) {
			throw new PhrescoException(e);
		}
	}

	public String generateToken(String userName) throws PhrescoException {
		String token = "";
		try {
			SecureRandom random = new SecureRandom();
			byte[] seed = random.generateSeed(MAGICNUMBER.BYTENUM);
			random.setSeed(seed);
			token = new BigInteger(MAGICNUMBER.BYTENUM, random).toString(MAGICNUMBER.BYTENUMSTR);
			tokenCache.put(token, userName);
		} catch (Exception e) {
			throw new PhrescoException(e);
		}
		return token;
	}
	
	public String getUserName(String token) {
		return tokenCache.getIfPresent(token);
	}

	public boolean isValidToken(String token) {
		boolean validToken = false;
		if (tokenCache.getIfPresent(token) != null) {
			validToken = true;
		}
		return validToken;
	}
}
