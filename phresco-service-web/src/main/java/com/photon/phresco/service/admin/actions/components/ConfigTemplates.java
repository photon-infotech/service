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

import javax.servlet.http.HttpServletRequest;

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
import com.photon.phresco.service.client.api.ServiceManager;

public class ConfigTemplates extends ServiceBaseAction {
	
	private static final long serialVersionUID = 6801037145464060759L;
	
	private static final Logger S_LOGGER = Logger.getLogger(ConfigTemplates.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	private String name = "";
	private String dispName = "";
	private String description = "";
	private List<String> appliesTo = null;
	private String defaultCustProp = "";
	
	private String nameError = "";
	private String dispError = "";
	private String applyError = "";
	private boolean errorFound = false;
	
    private String customerId = "";
    
    private String configId = ""; // it will be generated by the mongodb
    private String oldName = "";
    
    private String fromPage = "";
    
    /**
     * To get all config templates form DB
     * @return List of config templates
     * @throws PhrescoException
     */
	public String list() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method ConfigTemplates.list()");
		}
		
		try {
			List<SettingsTemplate> configTemplates = getServiceManager().getConfigTemplates(getCustomerId());
			setReqAttribute(REQ_CONFIG_TEMPLATES, configTemplates);
			setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
		} catch (PhrescoException e) {
			return showErrorPopup(e, getText(EXCEPTION_CONFIG_TEMP_LIST));
		}
		
		return COMP_CONFIGTEMPLATE_LIST;
	}
	
	/**
	 * To return to the page to add config templates
	 * @return 
	 * @throws PhrescoException
	 */
	public String add() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method ConfigTemplates.add()");
		}
		
		try {
			List<Technology> technologies = getServiceManager().getArcheTypes(getCustomerId());
			setReqAttribute(REQ_ARCHE_TYPES, technologies);
			setReqAttribute(REQ_FROM_PAGE, ADD);
		} catch (PhrescoException e) {
		    return showErrorPopup(e, getText(EXCEPTION_CONFIG_TEMP_ADD));
		}
		
		return COMP_CONFIGTEMPLATE_ADD;
	}
	
	/**
	 * To return the edit page with the details of the selected config template
	 * @return
	 * @throws PhrescoException
	 */
	public String edit() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method ConfigTemplates.edit()");
		}
		
		try {
		    ServiceManager serviceManager = getServiceManager();
			SettingsTemplate configTemp = serviceManager.getConfigTemplate(getConfigId(), getCustomerId());
			List<Technology> technologies = serviceManager.getArcheTypes(getCustomerId());
		    setReqAttribute(REQ_CONFIG_TEMP, configTemp);
			setReqAttribute(REQ_ARCHE_TYPES, technologies);
			setReqAttribute(REQ_FROM_PAGE, EDIT);
		} catch (PhrescoException e) {
		    return showErrorPopup(e, getText(EXCEPTION_CONFIG_TEMP_EDIT));
		}
		
		return COMP_CONFIGTEMPLATE_ADD;
	}
	
	/**
	 * To create a config template with the provided details
	 * @return List of Config templates
	 * @throws PhrescoException
	 */
	public String save() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method ConfigTemplates.save()");
		}

		try  {
			List<SettingsTemplate> settingsTemplates = new ArrayList<SettingsTemplate>();
            settingsTemplates.add(createSettingsTemplateInstance());
            getServiceManager().createConfigTemplates(settingsTemplates, getCustomerId());
        	addActionMessage(getText(CONFIGTEMPLATE_ADDED, Collections.singletonList(getName())));
		} catch (PhrescoException e) {
		    return showErrorPopup(e, getText(EXCEPTION_CONFIG_TEMP_SAVE));
		}
		
		return  list();
	}
	
	/**
	 * To update the details of the selected config template
	 * @return
	 * @throws PhrescoException
	 */
	public String update() throws PhrescoException {
    	if (isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  ConfigTemplates.update()");
    	}
    	
    	try {
    		getServiceManager().updateConfigTemp(createSettingsTemplateInstance(), getConfigId(), getCustomerId());
    		addActionMessage(getText(CONFIGTEMPLATE_UPDATED, Collections.singletonList(getName())));
    	} catch (PhrescoException e) {
    	    return showErrorPopup(e, getText(EXCEPTION_CONFIG_TEMP_UPDATE));
		}

    	return list();
    }
	
	/**
	 * To update the details of the selected config template
	 * @return List of config templates
	 * @throws PhrescoException
	 */
	private SettingsTemplate createSettingsTemplateInstance() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method ConfigTemplates.createPropertyTemplates()");
		}
		SettingsTemplate settingTemplate = null;
		try {
			List<String> techIds = getAppliesTo();
			if(!DEFAULT_CUSTOMER_NAME.equalsIgnoreCase(getCustomerId()) && EDIT.equalsIgnoreCase(fromPage)) {
				settingTemplate = getServiceManager().getConfigTemplate(getConfigId());
				settingTemplate.setCustomerIds(Arrays.asList(getCustomerId()));
				boolean customProp = Boolean.parseBoolean(getDefaultCustProp());
				settingTemplate.setCustomProp(customProp);
				List<Element> appliesTos = new ArrayList<Element>();
				for (String techId : techIds) {
					String techName = getServiceManager().getTechnology(techId).getName();
					Element element = new Element();
					element.setId(techId);
					element.setName(techName);
					appliesTos.add(element);
				}
				settingTemplate.setAppliesToTechs(appliesTos);
			} else {
				settingTemplate = new SettingsTemplate();
				settingTemplate.setName(getName());
				settingTemplate.setDisplayName(StringUtils.isEmpty(getDispName()) ? getName() : getDispName());
				settingTemplate.setDescription(getDescription());
				boolean customProp = Boolean.parseBoolean(getDefaultCustProp());
				settingTemplate.setCustomProp(customProp);
				settingTemplate.setCustomerIds(Arrays.asList(getCustomerId()));
				if (StringUtils.isNotEmpty(getConfigId())) {
					settingTemplate.setId(getConfigId());
				}
				List<Element> appliesTos = new ArrayList<Element>();
				for (String techId : techIds) {
					String techName = getServiceManager().getTechnology(techId).getName();
					Element element = new Element();
					element.setId(techId);
					element.setName(techName);
					appliesTos.add(element);
				}

				settingTemplate.setAppliesToTechs(appliesTos);
				setPropertTemplate(settingTemplate);
			}
			
		} catch (Exception e) {
			throw new PhrescoException(e);
		}
		
		return settingTemplate;
	}

	private void setPropertTemplate(SettingsTemplate settingTemplate) {
		List<PropertyTemplate> propertyTemplates = new ArrayList<PropertyTemplate>();
		HttpServletRequest request = getHttpRequest();
		String[] propTempKeys = request.getParameterValues(REQ_CONFIG_KEY);

		if (ArrayUtils.isNotEmpty(propTempKeys)) {
			for (String propTempKey : propTempKeys) {
				String propTempName = request.getParameter(propTempKey + REQ_CONFIG_NAME);
				String propTempType = request.getParameter(propTempKey + REQ_CONFIG_TYPE);
				String propTempHlpTxt = request.getParameter(propTempKey + REQ_CONFIG_HELP_TEXT);
				String propTempMandat = request.getParameter(propTempKey + REQ_CONFIG_MANDATORY);
				String propTempMulti = request.getParameter(propTempKey + REQ_CONFIG_MULTIPLE);
				String csvPsblValues = request.getParameter(propTempKey + REQ_CONFIG_PSBL_VAL);
				List<String> possibleValues = new ArrayList<String>();
				if (StringUtils.isNotEmpty(csvPsblValues)) {
				   possibleValues = Arrays.asList(csvPsblValues.split(CSV_PATTERN));
				}
				PropertyTemplate propertyTemplate = new PropertyTemplate();
				propertyTemplate.setKey(propTempKey);
				propertyTemplate.setName(propTempName);
				propertyTemplate.setType(propTempType);
				propertyTemplate.setPossibleValues(possibleValues);
				propertyTemplate.setHelpText(propTempHlpTxt);
				propertyTemplate.setRequired(Boolean.parseBoolean(propTempMandat));
				propertyTemplate.setMultiple(Boolean.parseBoolean(propTempMulti));
				propertyTemplates.add(propertyTemplate);
			}
		}
		settingTemplate.setProperties(propertyTemplates);
	}

	/**
	 * To delete selected config templates
	 * @return
	 * @throws PhrescoException
	 */
	public String delete() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method ConfigTemplates.delete()");
	    }

		try {
			String[] configIds = getHttpRequest().getParameterValues(REQ_CONFIG_ID);
			if (ArrayUtils.isNotEmpty(configIds)) {
				for (String configid : configIds) {
					getServiceManager().deleteConfigTemp(configid, getCustomerId());
				}
			addActionMessage(getText(CONFIGTEMPLATE_DELETED));
			}
		} catch (PhrescoException e) {
		    return showErrorPopup(e, getText(EXCEPTION_CONFIG_TEMP_DELETE));
		}

		return list();
	}
	
	/**
	 * To validate mandatory fields
	 * @return
	 * @throws PhrescoException
	 */
	public String validateForm() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method ConfigTemplates.validateForm()");
		}
		boolean isError = false;
		
		if (!DEFAULT_CUSTOMER_NAME.equalsIgnoreCase(customerId) && EDIT.equalsIgnoreCase(fromPage)) {
			//Empty validation for applies to technology
			if (getAppliesTo() == null) {
				setApplyError(getText(KEY_I18N_ERR_APPLIES_EMPTY ));
				isError = true;
			}
			
			return SUCCESS;
		}
		
		if (StringUtils.isEmpty(getName())) {//Empty validation for name
			setNameError(getText(KEY_I18N_ERR_NAME_EMPTY ));
			isError = true;
		} else if(ADD.equals(getFromPage()) || (!getName().equals(getOldName()))) {
			// to check duplication of dispname
			List<SettingsTemplate> configTemplates = getServiceManager().getConfigTemplates(getCustomerId());
			if (CollectionUtils.isNotEmpty(configTemplates)) { //TODO: this should handled by query
				for (SettingsTemplate configTemplate : configTemplates) {
					if (configTemplate.getName().equalsIgnoreCase(getName())) {
						setNameError(getText(KEY_I18N_ERR_NAME_ALREADY_EXIST));
			    		isError = true;
						break;
					}
				}
			}
		}
		
		if ((StringUtils.isNotEmpty(getDispName())) && (ADD.equals(getFromPage()))) {//Empty validation for dispName
			// to check duplication of dispName
			List<SettingsTemplate> configTemplates = getServiceManager().getConfigTemplates(getCustomerId());
			if (CollectionUtils.isNotEmpty(configTemplates)) { 
				for (SettingsTemplate configTemplate : configTemplates) {
					if(StringUtils.isNotEmpty(configTemplate.getDisplayName())) {
						if (configTemplate.getDisplayName().equalsIgnoreCase(getDispName())) {
							setDispError(getText(KEY_I18N_ERR_DISPLAYNAME_ALREADY_EXIST));
				    		isError = true;
							break;
						}
					}
				}
			}
		}
		
		//Empty validation for applies to technology
		if (getAppliesTo() == null) {
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
	
	public List<String> getAppliesTo() {
		return appliesTo;
	}

	public void setAppliesTo(List<String> appliesTo) {
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

	public String getDefaultCustProp() {
		return defaultCustProp;
	}

	public void setDefaultCustProp(String defaultCustProp) {
		this.defaultCustProp = defaultCustProp;
	}

	public String getDispName() {
		return dispName;
	}

	public void setDispName(String dispName) {
		this.dispName = dispName;
	}

	public String getDispError() {
		return dispError;
	}

	public void setDispError(String dispError) {
		this.dispError = dispError;
	}

}