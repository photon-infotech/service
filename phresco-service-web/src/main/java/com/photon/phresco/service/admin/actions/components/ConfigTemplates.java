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
package com.photon.phresco.service.admin.actions.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.I18NString;
import com.photon.phresco.model.PropertyTemplate;
import com.photon.phresco.model.SettingsTemplate;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.sun.jersey.api.client.ClientResponse;

public class ConfigTemplates extends ServiceBaseAction { 
	
	private static final long serialVersionUID = 6801037145464060759L;
	private static final Logger S_LOGGER = Logger.getLogger(ConfigTemplates.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	private String name = null;
	private I18NString description = null;
	private String nameError = null;
	private String applies = null;
	private String applyError = null;
	private boolean errorFound = false;
    private String customerId = null;
    private List<String> appliesTo = null;
    private String configId = null;
	
	public String list() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method ConfigTemplates.list()");
		}
		
		try {
			List<SettingsTemplate> configTemplates = getServiceManager().getconfigTemplates(customerId);
			getHttpRequest().setAttribute(REQ_CONFIG_TEMPLATES, configTemplates);
			getHttpRequest().setAttribute(REQ_CUST_CUSTOMER_ID, customerId);
		} catch (PhrescoException e) {
			throw new PhrescoException(e);
		}
		
		return COMP_CONFIGTEMPLATE_LIST;
	}
	
	public String add() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method ConfigTemplates.add()");
		}
		
		return COMP_CONFIGTEMPLATE_ADD;
	}
	
	public String edit() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method ConfigTemplates.edit()");
		}
		
		try {
		    SettingsTemplate configTemp = getServiceManager().getConfigTemplate(configId, customerId);
			getHttpRequest().setAttribute(REQ_CONFIG_TEMP , configTemp);
			getHttpRequest().setAttribute(REQ_FROM_PAGE, REQ_EDIT);
		} catch(Exception e) {
			throw new PhrescoException(e);
		}
		
		return COMP_CONFIGTEMPLATE_ADD;
	}
	
	public String save() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method ConfigTemplates.save()");
		}
		
		try  {
			List<SettingsTemplate> configTemps = new ArrayList<SettingsTemplate>();
			List<PropertyTemplate> propTemps = new ArrayList<PropertyTemplate>();
			
			PropertyTemplate propTemp = new PropertyTemplate();
			propTemp.setiName(name);
            propTemp.setDescription(description);
			propTemps.add(propTemp);
            
            SettingsTemplate configTemp = new SettingsTemplate();
            configTemp.setAppliesTo(appliesTo);	
            configTemp.setProperties(propTemps);
            configTemp.setCustomerId(customerId);
            
            configTemps.add(configTemp);
            
            ClientResponse clientResponse = getServiceManager().createConfigTemplates(configTemps, customerId);
            if (clientResponse.getStatus() != 200 && clientResponse.getStatus() != 201) {
            	addActionError(getText(CONFIGTEMPLATE_NOT_ADDED, Collections.singletonList(name)));
            } else {
            	addActionMessage(getText(CONFIGTEMPLATE_ADDED, Collections.singletonList(name)));
            }
		} catch (Exception e) {
			 throw new PhrescoException(e);  
		}
		
		 return  list();
		 
	}
	
	public String update() throws PhrescoException {
    	if (isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  ConfigTemplates.update()");
    	}

    	try {
    		List<PropertyTemplate> propTemps = new ArrayList<PropertyTemplate>();
    		
    		PropertyTemplate propTemp = new PropertyTemplate();
			propTemp.setiName(name);
            propTemp.setDescription(description);
            propTemps.add(propTemp);
            
            SettingsTemplate configTemp = new SettingsTemplate();
            configTemp.setAppliesTo(appliesTo);	
            configTemp.setProperties(propTemps);
            configTemp.setId(configId);
    		getServiceManager().updateConfigTemp(configTemp, configId, customerId);
    	} catch(Exception e) {
    		throw new PhrescoException(e);
    	}

    	return list();
    }
	
	
	public String delete() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method ConfigTemplates.delete()");
	    }

		try {
			String[] configIds = getHttpRequest().getParameterValues(REQ_CONFIG_ID);
			if (configIds != null) {
				for (String configId : configIds) {
					ClientResponse clientResponse = getServiceManager().deleteConfigTemp(configId, customerId);
					if (clientResponse.getStatus() != 200) {
						addActionError(getText(CONFIGTEMPLATE_NOT_DELETED));
					}
				}
				addActionMessage(getText(CONFIGTEMPLATE_DELETED));
			}
		} catch (Exception e) {
			throw new PhrescoException(e);
		}

		return list();
	}
	
	public String validateForm() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method ConfigTemplates.validateForm()");
		}
		
		boolean isError = false;
		if (StringUtils.isEmpty(name)) {
			setNameError(getText(KEY_I18N_ERR_NAME_EMPTY ));
			isError = true;
		}
		
		if (StringUtils.isEmpty(applies)) {
			setApplyError(getText(KEY_I18N_ERR_APPLIES_EMPTY ));
			isError = true;
		}
		
		if (isError) {
            setErrorFound(true);
        }
		
		return SUCCESS;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameError() {
		return nameError;
	}

	public void setNameError(String nameError) {
		this.nameError = nameError;
	}

	public String getApplies() {
		return applies;
	}

	public void setApplies(String applies) {
		this.applies = applies;
	}

	public String getApplyError() {
		return applyError;
	}

	public void setApplyError(String applyError) {
		this.applyError = applyError;
	}

	public boolean isErrorFound() {
		return errorFound;
	}

	public void setErrorFound(boolean errorFound) {
		this.errorFound = errorFound;
	}
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public void setDescription(I18NString description) {
		this.description = description;
	}
	
	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}
}