/**
 * Phresco Service Root
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
package com.photon.phresco.service.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.photon.phresco.commons.model.Element;
import com.photon.phresco.commons.model.PropertyTemplate;
import com.photon.phresco.commons.model.SettingsTemplate;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.impl.DbService;
import com.photon.phresco.util.Constants;
import com.photon.phresco.util.TechnologyTypes;

public class ConfigurationDataGenerator extends DbService implements Constants {
	
	public ConfigurationDataGenerator() {
		super();
	}
	
    public List<SettingsTemplate> createSettings()
            throws PhrescoException {
        List<SettingsTemplate> settings = new ArrayList<SettingsTemplate>();
        settings.add(createServerTemplate());
        settings.add(createDatabaseTemplate());
        settings.add(createEmailTemplate());
        settings.add(createWebServiceTemplate());
        settings.add(createSAPTemplate());
        settings.add(createLDAPTemplate());
        return settings;
    }

    private SettingsTemplate createServerTemplate() {
        List<PropertyTemplate> props = new ArrayList<PropertyTemplate>(8);
        List<PropertyTemplate> propsTeamplates = new ArrayList<PropertyTemplate>();
        
        List<String> possibleValues = new ArrayList<String>(8);
        possibleValues.add("http");
        possibleValues.add("https");
        props.add(createPropertyTemplate(SERVER_PROTOCOL, "Protocol", "String", false, true,
                possibleValues, "Protocol to access the application"));
        props.add(createPropertyTemplate(SERVER_HOST, "Host", "String", false, true,
                null, "Host Name or IPAddress of the server"));
        props.add(createPropertyTemplate(SERVER_PORT, "Port", "Number", false, true, null,
                "Port number of the server"));
        props.add(createPropertyTemplate(SERVER_ADMIN_USERNAME, "Admin Username", "String", false, false, null,
                "User name to access the server"));
        props.add(createPropertyTemplate(SERVER_ADMIN_PASSWORD, "Admin Password", "Password", false, false, null,
                "Password to access the server"));
        props.add(createPropertyTemplate(SERVER_REMOTE_DEPLOYMENT, "Remote Deployment", "Boolean", false, false, null,
        "Remote Deployment"));
        props.add(createPropertyTemplate("certificate", "Certificate", "String", false, false, null,
        "Certificate"));
        //Prperty Templates For Server Details
        propsTeamplates.add(createPropertyTemplate("type", "Type", "String", false, true, null, "Type of the server"));
        propsTeamplates.add(createPropertyTemplate("version", "Version", "String", false, true, null, "Version of the server"));
        props.add(createPropertyTemplateUsingList("Server", "Server Type", "String", false, true, null,
        "Server details", propsTeamplates));
        
        props.add(createPropertyTemplate(SERVER_DEPLOY_DIR, "Deploy Directory", "String", false,true, null,
                "Deployment directory of the server on the instance"));
        props.add(createPropertyTemplate(SERVER_CONTEXT, "Context", "String", false, true, null,
                "Server context of the application"));
        props.add(createPropertyTemplate("additional_context", "Additional Context Path", "String", false, false, null,
        "Additional context path of the application"));

        List<Element> appsTo = new ArrayList<Element>(8);
        appsTo.add(createElement(TechnologyTypes.PHP));
        appsTo.add(createElement(TechnologyTypes.PHP_DRUPAL6));
        appsTo.add(createElement(TechnologyTypes.PHP_DRUPAL7));
        appsTo.add(createElement(TechnologyTypes.SHAREPOINT));
        appsTo.add(createElement(TechnologyTypes.HTML5_WIDGET));
        appsTo.add(createElement(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET));
        appsTo.add(createElement(TechnologyTypes.HTML5_JQUERY_MOBILE_WIDGET));
        appsTo.add(createElement(TechnologyTypes.HTML5_MOBILE_WIDGET));
        appsTo.add(createElement(TechnologyTypes.DOT_NET));
        appsTo.add(createElement(TechnologyTypes.WORDPRESS));
        appsTo.add(createElement(TechnologyTypes.IPHONE_NATIVE));
        appsTo.add(createElement(TechnologyTypes.IPHONE_HYBRID));
        appsTo.add(createElement(TechnologyTypes.ANDROID_NATIVE));
        appsTo.add(createElement(TechnologyTypes.ANDROID_HYBRID));
        appsTo.add(createElement(TechnologyTypes.NODE_JS_WEBSERVICE));
        appsTo.add(createElement(TechnologyTypes.JAVA_WEBSERVICE));
        return createSettingsTemplate(SETTINGS_TEMPLATE_SERVER, props, appsTo);
    }
    
    private PropertyTemplate createPropertyTemplateUsingList(String key, String name, String type,
            boolean isProjSpecific, boolean isRequired , List<String> possibleValues, String description, List<PropertyTemplate> templates) {
    	PropertyTemplate propTemplate = new PropertyTemplate(key, type, isProjSpecific ,isRequired);
        propTemplate.setPossibleValues(possibleValues);
        propTemplate.setName(name);
        propTemplate.setDescription(description);
        propTemplate.setPropertyTemplates(templates);
        return propTemplate;
	}

	private PropertyTemplate createPropertyTemplate(String key, String name, String type,
            boolean isProjSpecific, boolean isRequired , List<String> possibleValues, String description) {
        PropertyTemplate propTemplate = new PropertyTemplate(key, type, isProjSpecific ,isRequired);
        if(CollectionUtils.isNotEmpty(possibleValues)) {
        	propTemplate.setPossibleValues(possibleValues);
        }
        propTemplate.setName(name);
        propTemplate.setDescription(description);
        return propTemplate;
    }

    private SettingsTemplate createDatabaseTemplate() {
        List<PropertyTemplate> props = new ArrayList<PropertyTemplate>(8);
        List<PropertyTemplate> propsTeamplates = new ArrayList<PropertyTemplate>();
        
        props.add(createPropertyTemplate(DB_HOST, "Host", "String", false,true,
                null, "Name or IPAddress of the database server"));
        props.add(createPropertyTemplate(DB_PORT, "Port", "Number", false,true, null,
                "Port number of the database server"));
        props.add(createPropertyTemplate(DB_USERNAME, "Username", "Password", false,true, null,
                "User name to access the database"));
        props.add(createPropertyTemplate(DB_PASSWORD, "Password", "String", false,false, null,
                "Password to access the database"));
        props.add(createPropertyTemplate(DB_NAME, "DB Name", "String", true,true, null,
                "Name of the database"));
        props.add(createPropertyTemplate(DB_TYPE, "Type", "String", false,true, null,
                "Type of the database"));
        
        propsTeamplates.add(createPropertyTemplate("type", "Type", "String", false, true, null, "Type of the Database"));
        propsTeamplates.add(createPropertyTemplate("version", "Version", "String", false, true, null, "Version of the Database"));
        props.add(createPropertyTemplateUsingList("Database", "DB Type", "String", false, true, null,
        "Database details", propsTeamplates));

        List<Element> appsTo = new ArrayList<Element>(16);
        appsTo.add(createElement(TechnologyTypes.PHP));
        appsTo.add(createElement(TechnologyTypes.PHP_DRUPAL6));
        appsTo.add(createElement(TechnologyTypes.PHP_DRUPAL7));
        appsTo.add(createElement(TechnologyTypes.SHAREPOINT));
        appsTo.add(createElement(TechnologyTypes.HTML5_WIDGET));
        appsTo.add(createElement(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET));
        appsTo.add(createElement(TechnologyTypes.HTML5_JQUERY_MOBILE_WIDGET));
        appsTo.add(createElement(TechnologyTypes.HTML5_MOBILE_WIDGET));
        appsTo.add(createElement(TechnologyTypes.WORDPRESS));
        appsTo.add(createElement(TechnologyTypes.IPHONE_NATIVE));
        appsTo.add(createElement(TechnologyTypes.IPHONE_HYBRID));
        appsTo.add(createElement(TechnologyTypes.NODE_JS_WEBSERVICE));
        appsTo.add(createElement(TechnologyTypes.JAVA_WEBSERVICE));
        return createSettingsTemplate(SETTINGS_TEMPLATE_DB, props, appsTo);
    }

    private SettingsTemplate createEmailTemplate() {
        List<PropertyTemplate> props = new ArrayList<PropertyTemplate>(8);
        
        props.add(createPropertyTemplate("incoming_mail_server", "Incoming Mail Server", "String", false,false, null,
                "Name or IPAddress of the incoming email server"));
        props.add(createPropertyTemplate("incoming_mail_port", "Incoming Port", "String", false, false, null,
                "Name or IPAddress of the incoming email server"));
        props.add(createPropertyTemplate("host", "Outgoing Server Name", "String", false,true, null,
                "Name or IPAddress of the email server"));
        props.add(createPropertyTemplate("port", "Outgoing Port", "String", false, true, null,
                "Name or IPAddress of the outgoing email server"));
        props.add(createPropertyTemplate("username", "Username", "String", false, true, null,
        "Username of the Email address"));
        props.add(createPropertyTemplate("password", "Password", "Password", false, true, null,
        "Password for the email address"));
        props.add(createPropertyTemplate("emailid", "Email Id", "String", false, true, null,
        "Email Id"));

        List<Element> appsTo = new ArrayList<Element>(8);
        appsTo.add(createElement(TechnologyTypes.PHP));
        appsTo.add(createElement(TechnologyTypes.PHP_DRUPAL6));
        appsTo.add(createElement(TechnologyTypes.PHP_DRUPAL7));
        appsTo.add(createElement(TechnologyTypes.SHAREPOINT));
        appsTo.add(createElement(TechnologyTypes.DOT_NET));
        appsTo.add(createElement(TechnologyTypes.NODE_JS_WEBSERVICE));
        appsTo.add(createElement(TechnologyTypes.JAVA_WEBSERVICE));
        return createSettingsTemplate(SETTINGS_TEMPLATE_EMAIL, props, appsTo);
    }


    private SettingsTemplate createWebServiceTemplate() {
        List<PropertyTemplate> props = new ArrayList<PropertyTemplate>(8);

        List<String> possibleValues = new ArrayList<String>(8);
        possibleValues.add("http");
        possibleValues.add("https");

        props.add(createPropertyTemplate(WEB_SERVICE_PROTOCOL, "Protocol", "String", false,true,
                possibleValues, "Protocol to access the service"));
        props.add(createPropertyTemplate(WEB_SERVICE_HOST, "Host", "String", false,true,
                null, "Name or IPAddress of the service"));
        props.add(createPropertyTemplate(WEB_SERVICE_PORT, "Port", "Number", false,true, null,
                "Port number of the service"));
        props.add(createPropertyTemplate(WEB_SERVICE_USERNAME, "Username", "String", false, false, null,
                "User name to access the service"));
        props.add(createPropertyTemplate(WEB_SERVICE_PASSWORD, "Password", "Password", false,true, null,
                "Password to access the service"));
        props.add(createPropertyTemplate(WEB_SERVICE_CONTEXT, "Context", "String", false,true, null,
                "Context of the service"));

        List<Element> appsTo = new ArrayList<Element>(8);
        appsTo.add(createElement(TechnologyTypes.PHP));
        appsTo.add(createElement(TechnologyTypes.PHP_DRUPAL6));
        appsTo.add(createElement(TechnologyTypes.PHP_DRUPAL7));
        appsTo.add(createElement(TechnologyTypes.SHAREPOINT));
        appsTo.add(createElement(TechnologyTypes.HTML5));
        appsTo.add(createElement(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET));
        appsTo.add(createElement(TechnologyTypes.HTML5_JQUERY_MOBILE_WIDGET));
        appsTo.add(createElement(TechnologyTypes.HTML5_MOBILE_WIDGET));
        appsTo.add(createElement(TechnologyTypes.HTML5_WIDGET));
        appsTo.add(createElement(TechnologyTypes.IPHONE_NATIVE));
        appsTo.add(createElement(TechnologyTypes.IPHONE_HYBRID));
        appsTo.add(createElement(TechnologyTypes.ANDROID_NATIVE));
        appsTo.add(createElement(TechnologyTypes.ANDROID_HYBRID));
        return createSettingsTemplate(SETTINGS_TEMPLATE_WEBSERVICE, props, appsTo);
    }
    
    private SettingsTemplate createSAPTemplate() {
        List<PropertyTemplate> props = new ArrayList<PropertyTemplate>(8);

        props.add(createPropertyTemplate("searchHosts", "SearchHosts", "String", false, false,
                null, "SearchHosts"));
        props.add(createPropertyTemplate("sapSvcHost", "SapSvcHost", "String", false, false,
                null, "SapSvcHost"));
        props.add(createPropertyTemplate("sapSvcPort", "SapSvcPort", "Number", false, false, null,
                "SapSvcPort"));
        props.add(createPropertyTemplate("smtp-host", "SmtpHost", "String", false, false, null,
                "SmtpHost"));
        props.add(createPropertyTemplate("useProductionConfig", "UseProductionConfig", "String", false, false, null,
                "UseProductionConfig"));
        props.add(createPropertyTemplate("auth-token-cache-ttl", "AuthTokenCache ttl", "String", false, false, null,
                "AuthToken Cache ttl"));

        List<Element> appsTo = new ArrayList<Element>(8);
        appsTo.add(createElement(TechnologyTypes.PHP));
        appsTo.add(createElement(TechnologyTypes.PHP_DRUPAL6));
        appsTo.add(createElement(TechnologyTypes.PHP_DRUPAL7));
        appsTo.add(createElement(TechnologyTypes.SHAREPOINT));
        appsTo.add(createElement(TechnologyTypes.HTML5));
        appsTo.add(createElement(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET));
        appsTo.add(createElement(TechnologyTypes.HTML5_JQUERY_MOBILE_WIDGET));
        appsTo.add(createElement(TechnologyTypes.HTML5_MOBILE_WIDGET));
        appsTo.add(createElement(TechnologyTypes.HTML5_WIDGET));
        appsTo.add(createElement(TechnologyTypes.IPHONE_NATIVE));
        appsTo.add(createElement(TechnologyTypes.IPHONE_HYBRID));
        appsTo.add(createElement(TechnologyTypes.ANDROID_NATIVE));
        appsTo.add(createElement(TechnologyTypes.ANDROID_HYBRID));
        return createSettingsTemplate("SAP", props, appsTo);
    }
    
    private SettingsTemplate createLDAPTemplate() {
        List<PropertyTemplate> props = new ArrayList<PropertyTemplate>(8);
        
        List<String> appTo = new ArrayList<String>();
        appTo.add("LDAP");
        appTo.add("LDAPS");
        props.add(createPropertyTemplate("protocol", "Protocol", "String", false, true,
        		appTo, "Protocol to access the application"));
        props.add(createPropertyTemplate("host", "Host", "String", false, true,
                null, "Host Name or IPAddress of the server"));
        props.add(createPropertyTemplate("port", "Port", "Number", false, true, null,
                "Port number of the server"));
        props.add(createPropertyTemplate("baseDN", "BaseDN", "String", false, true, null,
                "BaseDN"));
        List<String> typeVals = new ArrayList<String>();
        typeVals.add("NONE");
        typeVals.add("SIMPLE");
        props.add(createPropertyTemplate("authenticationType", "Authentication Type", "String", false, true, typeVals,
                "Type Of Authendication"));
        props.add(createPropertyTemplate("bindDN", "Bind DN or User", "String", false, false, null,
                "Bind DN or User"));
        props.add(createPropertyTemplate("bindPassword", "Bind Password", "Password", false, false, null,
        "Bind Password"));
        
        List<Element> appsTo = new ArrayList<Element>(8);
        appsTo.add(createElement(TechnologyTypes.PHP));
        appsTo.add(createElement(TechnologyTypes.PHP_DRUPAL6));
        appsTo.add(createElement(TechnologyTypes.PHP_DRUPAL7));
        appsTo.add(createElement(TechnologyTypes.SHAREPOINT));
        appsTo.add(createElement(TechnologyTypes.HTML5));
        appsTo.add(createElement(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET));
        appsTo.add(createElement(TechnologyTypes.HTML5_JQUERY_MOBILE_WIDGET));
        appsTo.add(createElement(TechnologyTypes.HTML5_MOBILE_WIDGET));
        appsTo.add(createElement(TechnologyTypes.HTML5_WIDGET));
        appsTo.add(createElement(TechnologyTypes.IPHONE_NATIVE));
        appsTo.add(createElement(TechnologyTypes.IPHONE_HYBRID));
        appsTo.add(createElement(TechnologyTypes.ANDROID_NATIVE));
        appsTo.add(createElement(TechnologyTypes.ANDROID_HYBRID));
        return createSettingsTemplate("LDAP", props, appsTo);
    }
    
    private Element createElement(String id) {
    	Element element = new Element();
    	element.setId(id);
    	element.setName(id);
		return element;
	}

	private SettingsTemplate createSettingsTemplate(String name, List<PropertyTemplate> props, List<Element> appliesTo) {
    	SettingsTemplate template = new SettingsTemplate();
    	template.setId("config_" + name);
    	template.setName(name);
    	template.setProperties(props);
    	template.setSystem(true);
    	List<String> customerIds = new ArrayList<String>();
    	customerIds.add("photon");
		template.setCustomerIds(customerIds);
		template.setAppliesToTechs(appliesTo);
		return template;
    }
	
    public void publish() throws PhrescoException{
    	ConfigurationDataGenerator conf = new ConfigurationDataGenerator();
        List<SettingsTemplate> template = conf.createSettings();
        mongoOperation.insertList(SETTINGS_COLLECTION_NAME, template);
    }
    
    public static void main(String args[]) throws PhrescoException {
        ConfigurationDataGenerator generator = new ConfigurationDataGenerator();
        generator.publish();
    }

}
