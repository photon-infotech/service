/*
 * ###
 * Phresco Service
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
/*******************************************************************************
 * Copyright (c) 2011 Photon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Photon Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.photon.in/legal/ppl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *     Photon - initial API and implementation
 ******************************************************************************/
package com.photon.phresco.ldap.model;

import java.util.List;
import java.util.Properties;

import com.photon.phresco.configuration.Configuration;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.ldap.LDAPConstants;

public class LDAPConfiguration implements LDAPConstants {
	
	private Properties ldapProps;
	private String ldapContextFactory;
	private String ldaphost;
	private String ldapPort;
	private String ldapProtocol;
	private String ldapBaseDn;
	private String ldapLoginAttribute;
	private String displayNameAttribute;
	private String mailIdAttribute;
	private String phrescoEnabledAttribute;
	private String customerNameAttribute;

	
	public LDAPConfiguration(Properties serverProps , List<Configuration> configurations) throws PhrescoException {
		this.ldapProps = serverProps;
		configurationList(configurations);
		init();
	}

	private void init() throws PhrescoException {
		if (ldapProps == null) {
			throw new PhrescoException("LDAP Properties cannot be null");
		}
		ldapContextFactory = (String) ldapProps.get(LDAP_CONTEXT_FACTORY);
//		ldapUrl = (String) ldapProps.get(LDAP_URL);
//		ldapBaseDn = (String) ldapProps.get(LDAP_BASEDN);
		ldapLoginAttribute = (String) ldapProps.get(LDAP_LOGIN_ATTRIBUTE);
		displayNameAttribute = (String) ldapProps.get(LDAP_DISPLAY_NAME_ATTRIBUTE);
		mailIdAttribute = (String) ldapProps.get(LDAP_MAIL_ATTRIBUTE);
		phrescoEnabledAttribute=(String) ldapProps.get(LDAP_PHRESCO_ENABLED);
		customerNameAttribute=(String) ldapProps.get(LDAP_CUSTOMER_NAME);
		
	}
	
	private void configurationList(List<Configuration> configurations) throws PhrescoException {
		try {
			 if (configurations != null) {
					for (Configuration  configuration : configurations) {
						 ldapProtocol = configuration.getProperties().getProperty("protocol");
						 ldaphost = configuration.getProperties().getProperty("host");
						 ldapPort = configuration.getProperties().getProperty("port");
						 ldapBaseDn = configuration.getProperties().getProperty("baseDN");
					  }		
					}
			 
		} catch (Exception e) {
		    throw new PhrescoException(e);
		}
	}


	/**
	 * @return the ldapContextFactory
	 */
	public String getLdapContextFactory() {
		return ldapContextFactory;
	}

	/**
	 * @return the ldapUrl
	 */
	public String getLdapUrl()throws PhrescoException {
		return ldapProtocol + "://" + ldaphost + ":" + ldapPort;
	}

	/**
	 * @return the ldapBaseDn
	 */
	public String getLdapBaseDn()throws PhrescoException {
		return ldapBaseDn;
	}

	/**
	 * @return the ldapLoginAttribute
	 */
	public String getLdapLoginAttribute() {
		return ldapLoginAttribute;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayNameAttribute() {
		return displayNameAttribute;
	}

	/**
	 * @return the mail
	 */
	public String getMailIdAttribute() {
		return mailIdAttribute;
	}
	/**
	 * 
	 * @return the customerName
	 */
	public String getCustomerNameAttribute(){
		return customerNameAttribute;
	}
	/**
	 * 
	 * @return the phrescoEnabled true or false
	 */
	public String getPhrescoEnabledAttribute(){
		return phrescoEnabledAttribute;
	}

	
}
