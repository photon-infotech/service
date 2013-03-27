/**
 * phresco-ldap
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
package com.photon.phresco.ldap.impl;

import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.photon.phresco.commons.model.User;
import com.photon.phresco.configuration.Configuration;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.ldap.api.LDAPManager;
import com.photon.phresco.ldap.model.LDAPConfiguration;
import com.photon.phresco.util.Constants;
import com.photon.phresco.util.Credentials;

public class LDAPManagerImpl implements LDAPManager {
	
	private static final Logger S_LOGGER = Logger.getLogger(LDAPManagerImpl.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	private LDAPConfiguration ldapConfig;

	public LDAPManagerImpl(Properties props, List<Configuration> configurations) throws PhrescoException {
		ldapConfig = new LDAPConfiguration(props, configurations);
	}

	@Override
	public User authenticate(Credentials credentials) throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method LDAPManagerImpl.authenticate(Credentials credentials)");
		}
		String userName = credentials.getUsername();
		String passwordEncoded = credentials.getPassword();
		byte[] decodedBytes = Base64.decodeBase64(passwordEncoded);
		String password = new String(decodedBytes);
		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, ldapConfig.getLdapContextFactory());
		env.put(Context.PROVIDER_URL, ldapConfig.getLdapUrl());
		env.put(Context.SECURITY_PRINCIPAL, getUserPrincipal(userName));
		env.put(Context.SECURITY_CREDENTIALS, password);

		DirContext dc = null;
		try {
			dc = new InitialDirContext(env);
			if (isDebugEnabled) {
				S_LOGGER.debug("authenticate() Login Success for " + userName);
			}
			return getUser(credentials, dc);
		} catch (Exception e) {
		    throw new PhrescoException(e);
		} finally {
			try {
				if (dc != null) {
					dc.close();
				}
			} catch (NamingException e) {
				throw new PhrescoException(e);
			}
		}
	}

	private String getUserPrincipal(String userName) throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method LDAPManagerImpl.getUserPrincipal(String userName)");
		}
		StringBuffer userPrincipal = new StringBuffer();
		userPrincipal.append(ldapConfig.getLdapLoginAttribute());
		userPrincipal.append(Constants.STR_EQUALS);
		userPrincipal.append(userName.trim());
		userPrincipal.append(Constants.STR_COMMA);
		userPrincipal.append(ldapConfig.getLdapBaseDn());
		return userPrincipal.toString();
	}

	private User getUser(Credentials credentials, DirContext ctx) throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method LDAPManagerImpl.getUserInfo(String userName, DirContext ctx)");
		}
		User user = new User();
		try {
			String userName = credentials.getUsername();
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String[] attrIDs = { "*" };
			constraints.setReturningAttributes(attrIDs);
			NamingEnumeration<SearchResult> ne = ctx.search(ldapConfig.getLdapBaseDn(), ldapConfig
					.getLdapLoginAttribute()
					+ Constants.STR_EQUALS + userName, constraints);
			if (ne.hasMore()) {
				Attributes attrs = ne.next().getAttributes();

				user.setName(userName);
				user.setDisplayName(getDisplayName(attrs));
				user.setEmail(getMailId(attrs));
				user.setPhrescoEnabled(isPhrescoEnabled(attrs)); 
				
			}
			
		} catch (Exception e) {
			throw new PhrescoException(e);
		}
		return user;
	}

	private boolean isPhrescoEnabled(Attributes attrs) throws NamingException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method LDAPManagerImpl.isPhrescoEnabled(Attributes attrs");
		}
		String phrescoEnabled = "false";
		Attribute attribute=attrs.get(ldapConfig.getPhrescoEnabledAttribute());
		if (attribute != null) {
			phrescoEnabled=(String)attribute.get();
		} else {
            phrescoEnabled = "true";
        }
		return Boolean.parseBoolean(phrescoEnabled);
	}

	private String getDisplayName(Attributes attrs) throws NamingException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method LDAPManagerImpl.getDisplayName(Attributes attrs)");
		}
		String displayName = null;
		Attribute attribute = attrs.get(ldapConfig.getDisplayNameAttribute());
		if (attribute != null) {
			displayName = (String) attribute.get();
		}
		return displayName;
	}

	private String getMailId(Attributes attrs) throws NamingException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method LDAPManagerImpl.getMailId(Attributes attrs");
		}
		String mailId = null;
		Attribute attribute = attrs.get(ldapConfig.getMailIdAttribute());
		if (attribute != null) {
			mailId = (String) attribute.get();
		}
		return mailId;
	}
	
}