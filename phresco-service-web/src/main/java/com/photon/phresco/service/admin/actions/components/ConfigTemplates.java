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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.commons.model.Element;
import com.photon.phresco.commons.model.PropertyTemplate;
import com.photon.phresco.commons.model.SettingsTemplate;
import com.photon.phresco.commons.model.Technology;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.util.ServiceConstants;
import com.sun.jersey.api.client.ClientResponse;

public class ConfigTemplates extends ServiceBaseAction { 
	
	private static final long serialVersionUID = 6801037145464060759L;
	private static final Logger S_LOGGER = Logger.getLogger(ConfigTemplates.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	private String name = null;
	private String description = null;
	private String nameError = null;
	private String applies = null;
	private String applyError = null;
	private boolean errorFound = false;
    private String customerId = null;
    private List<Element> appliesTo = null;
    private String configId = null;
    private String fromPage = null;
    private String oldName = null;
    
	public String list() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method ConfigTemplates.list()");
		}
		
		try {
			List<SettingsTemplate> configTemplates = getServiceManager().getconfigTemplates(customerId);
			setReqAttribute(REQ_CONFIG_TEMPLATES, configTemplates);
			setReqAttribute(REQ_CUST_CUSTOMER_ID, customerId);
		} catch (PhrescoException e) {
			showErrorPopup(e, CONFIG_TEMP_LIST_EXCEPTION);
		}
		
		return COMP_CONFIGTEMPLATE_LIST;
	}
	
	public String add() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method ConfigTemplates.add()");
		}
		
		try {
			List<Technology> technologies = getServiceManager().getArcheTypes(customerId);
			setReqAttribute(REQ_ARCHE_TYPES, technologies);
		} catch (PhrescoException e) {
			showErrorPopup(e, CONFIG_TEMP_ADD_EXCEPTION);
		}
		
		return COMP_CONFIGTEMPLATE_ADD;
	}
	
	public String edit() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method ConfigTemplates.edit()");
		}
		
		try {
		    SettingsTemplate configTemp = getServiceManager().getConfigTemplate(configId, customerId);
		    setReqAttribute(REQ_CONFIG_TEMP , configTemp);
			List<Technology> technologies = getServiceManager().getArcheTypes(customerId);
			setReqAttribute(REQ_ARCHE_TYPES, technologies);
			setReqAttribute(REQ_FROM_PAGE, EDIT);
		} catch (PhrescoException e) {
			showErrorPopup(e, CONFIG_TEMP_EDIT_EXCEPTION);
		}
		
		return COMP_CONFIGTEMPLATE_ADD;
	}
	
	public String save() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method ConfigTemplates.save()");
		}
		
		try  {
			List<SettingsTemplate> settingsTemplates = new ArrayList<SettingsTemplate>();
			SettingsTemplate settingTemplate = new SettingsTemplate();
            settingTemplate.setType(name);
            settingTemplate.setDescription(description);
            settingTemplate.setAppliesToTechs(appliesTo);
            List<String> customerIds = new ArrayList<String>();
            customerIds.add(customerId);
            settingTemplate.setCustomerIds(customerIds);
            settingTemplate.setProperties(createPropertyTemplates());
            settingsTemplates.add(settingTemplate);
            ClientResponse clientResponse = getServiceManager().createConfigTemplates(settingsTemplates, customerId);
            if (clientResponse.getStatus() != ServiceConstants.RES_CODE_200 
                    && clientResponse.getStatus() != ServiceConstants.RES_CODE_201) {
            	addActionError(getText(CONFIGTEMPLATE_NOT_ADDED, Collections.singletonList(name)));
            } else {
            	addActionMessage(getText(CONFIGTEMPLATE_ADDED, Collections.singletonList(name)));
            }
		} catch (PhrescoException e) {
			showErrorPopup(e, CONFIG_TEMP_SAVE_EXCEPTION);
		}
		
		return  list();
	}

	public String update() throws PhrescoException {
    	if (isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  ConfigTemplates.update()");
    	}
    	
    	try {
            SettingsTemplate settingTemplate = new SettingsTemplate();
            settingTemplate.setType(name);
            settingTemplate.setDescription(description);
            settingTemplate.setAppliesToTechs(appliesTo);
            List<String> customerIds = new ArrayList<String>();
            customerIds.add(customerId);
            settingTemplate.setCustomerIds(customerIds);
            settingTemplate.setId(configId);
            settingTemplate.setProperties(createPropertyTemplates());
    		getServiceManager().updateConfigTemp(settingTemplate, configId, customerId);
    	} catch (PhrescoException e) {
    		showErrorPopup(e, CONFIG_TEMP_UPDATE_EXCEPTION);
		}

    	return list();
    }
	
	private List<PropertyTemplate> createPropertyTemplates() throws PhrescoException {
        if (isDebugEnabled) {
            S_LOGGER.debug("Entering Method ConfigTemplates.createPropertyTemplates()");
        }
        
        List<PropertyTemplate> propertyTemplates;
        try {
            propertyTemplates = new ArrayList<PropertyTemplate>();
            String[] propTempKeys = getHttpRequest().getParameterValues(REQ_CONFIG_KEY);
            if (ArrayUtils.isNotEmpty(propTempKeys)) {
                for (String propTempKey : propTempKeys) {
                    String propTempName = getHttpRequest().getParameter(
                            propTempKey + REQ_CONFIG_NAME);
                    String propTempType = getHttpRequest().getParameter(
                            propTempKey + REQ_CONFIG_TYPE);
                    String propTempHlpTxt = getHttpRequest().getParameter(
                            propTempKey + REQ_CONFIG_HELP_TEXT);
                    String propTempMndtry = getHttpRequest().getParameter(
                            propTempKey + REQ_CONFIG_MANDATORY);
                    String propTempMul = getHttpRequest().getParameter(
                            propTempKey + REQ_CONFIG_MULTIPLE);
                    String csvPsblValues = getHttpRequest().getParameter(
                            propTempKey + REQ_CONFIG_PSBL_VAL);
                    List<String> possibleValues = Arrays
                            .asList(csvPsblValues.split("\\s*,\\s*"));

                    PropertyTemplate propertyTemplate = new PropertyTemplate();
                    propertyTemplate.setKey(propTempKey);
                    propertyTemplate.setName(propTempName);
                    propertyTemplate.setType(propTempType);
                    propertyTemplate.setPossibleValues(possibleValues);
                    propertyTemplate.setHelpText(propTempHlpTxt);

                    if (StringUtils.isNotEmpty(propTempMndtry)) {
                        propertyTemplate.setRequired(true);
                    } else {
                        propertyTemplate.setRequired(false);
                    }

                    if (StringUtils.isNotEmpty(propTempMul)) {
                        propertyTemplate.setMultiple(true);
                    } else {
                        propertyTemplate.setMultiple(false);
                    }
                    propertyTemplates.add(propertyTemplate);
                }
            }
        } catch (Exception e) {
            throw new PhrescoException(e);
        }
        
        return propertyTemplates;
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
					if (clientResponse.getStatus() != ServiceConstants.RES_CODE_200) {
						addActionError(getText(CONFIGTEMPLATE_NOT_DELETED));
					}
				}
				addActionMessage(getText(CONFIGTEMPLATE_DELETED));
			}
		} catch (PhrescoException e) {
			showErrorPopup(e, CONFIG_TEMP_DELETE_EXCEPTION);
		}

		return list();
	}
	
	public String validateForm() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method ConfigTemplates.validateForm()");
		}

		boolean isError = false;
		if (StringUtils.isEmpty(name)) {
			setNameError(getText(KEY_I18N_ERR_NAME_EMPTY ));
			isError = true;
		} else if(StringUtils.isEmpty(fromPage) || (!name.equals(oldName))) {
			// to check duplication of name
			List<SettingsTemplate> configTemplates = getServiceManager().getconfigTemplates(customerId);
			if (CollectionUtils.isNotEmpty(configTemplates)) {
				for (SettingsTemplate configTemplate : configTemplates) {
					if (configTemplate.getType().equalsIgnoreCase(name)) {
						setNameError(getText(KEY_I18N_ERR_NAME_ALREADY_EXIST));
			    		isError = true;
						break;
					}
				}
			}
		}
		
		if (appliesTo == null) {
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
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<Element> getAppliesTo() {
		return appliesTo;
	}

	public void setAppliesTo(List<Element> appliesTo) {
		this.appliesTo = appliesTo;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	public String getFromPage() {
		return fromPage;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getOldName() {
		return oldName;
	}
}