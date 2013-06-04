/**
 * Service Web Archive
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
package com.photon.phresco.service.admin.actions.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.photon.phresco.commons.model.ApplicationType;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;

public class ApplicationTypes extends ServiceBaseAction { 

	private static final long serialVersionUID = 6801037145464060759L;
	
	private static final SplunkLogger S_LOGGER = SplunkLogger.getSplunkLogger(ApplicationTypes.class.getName());
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();

	private String customerId = "";
	
	private String name = "";
	private String description = "";
	
	private String appTypeId = "";
	private String oldName = "";
	
	private String fromPage = "";
	
    private String nameError = "";
    private boolean errorFound = false;

    public String list() {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("ApplicationTypes.list : Entry");
	    }

		try {
		    List<ApplicationType> applicationTypes = getServiceManager().getApplicationTypes();
		    setReqAttribute(REQ_APP_TYPES, applicationTypes);
		    setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
		} catch (PhrescoException e) {
			if (isDebugEnabled) {
		        S_LOGGER.error("ApplicationTypes.list", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
		    }
		    return showErrorPopup(e, getText(EXCEPTION_APPTYPES_LIST));
		}
		if (isDebugEnabled) {
	        S_LOGGER.debug("ApplicationTypes.list : Exit");
	    }
		return COMP_APPTYPE_LIST;
	}

	public String add() {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("ApplicationTypes.add : Entry");
	    }
	    
	    setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
	    if (isDebugEnabled) {
	        S_LOGGER.debug("ApplicationTypes.add : Exit");
	    } 
	    
		return COMP_APPTYPE_ADD;
	}

	public String edit() {
		if (isDebugEnabled) {
	        S_LOGGER.debug("ApplicationTypes.edit : Entry");
	    }
		
		try {
			if (isDebugEnabled) {
				if (StringUtils.isEmpty(getAppTypeId())) {
					S_LOGGER.warn("ApplicationTypes.edit", "status=\"Bad Request\"", "message=\"Application type Id is empty\"");
					return showErrorPopup(new PhrescoException("Application type Id is empty"), getText(EXCEPTION_APPTYPES_EDIT));
				}
				if (StringUtils.isEmpty(getCustomerId())) {
					S_LOGGER.warn("ApplicationTypes.edit", "status=\"Bad Request\"", "message=\"Customer Id is empty\"");
					return showErrorPopup(new PhrescoException("Customer Id is empty"), getText(EXCEPTION_APPTYPES_EDIT));
				}
				S_LOGGER.info("ApplicationTypes.edit", "customerId=" + "\"" + getCustomerId() + "\"", "appTypeId=" + "\"" + getAppTypeId() + "\"");
			}
		    ApplicationType appType = getServiceManager().getApplicationType(getAppTypeId(), getCustomerId());
		    setReqAttribute(REQ_APP_TYPE, appType);
		    setReqAttribute(REQ_FROM_PAGE, EDIT);
		} catch (PhrescoException e) {
			if (isDebugEnabled) {
		        S_LOGGER.error("ApplicationTypes.edit", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
		    }
		    return showErrorPopup(e, getText(EXCEPTION_APPTYPES_EDIT));
		}
		if (isDebugEnabled) {
	        S_LOGGER.debug("ApplicationTypes.edit : Exit");
	    }
		
		return COMP_APPTYPE_ADD;
	}

	public String save() {
		if (isDebugEnabled) {
	        S_LOGGER.debug("ApplicationTypes.save : Entry");
	    }
		
		try {
		    List<ApplicationType> appTypes = new ArrayList<ApplicationType>();
			appTypes.add(createAppType());
			if (isDebugEnabled) {
				if (StringUtils.isEmpty(getCustomerId())) {
					S_LOGGER.warn("ApplicationTypes.save", "status=\"Bad Request\"", "message=\"Customer Id is empty\"");
					return showErrorPopup(new PhrescoException("Customer Id is empty"), getText(EXCEPTION_APPTYPES_SAVE));
				}
				if (CollectionUtils.isEmpty(appTypes)) {
					S_LOGGER.warn("ApplicationTypes.save", "status=\"Bad Request\"", "message=\"Applcation Type is empty\"");
					return showErrorPopup(new PhrescoException("Application type is empty"), getText(EXCEPTION_APPTYPES_SAVE));
				}
				S_LOGGER.info("ApplicationTypes.save", "customerId=" + "\"" + getCustomerId() + "\"");
			}
			getServiceManager().createApplicationTypes(appTypes, getCustomerId());
			addActionMessage(getText(APPTYPES_ADDED, Collections.singletonList(getName())));
		} catch (PhrescoException e) {
			if (isDebugEnabled) {
		        S_LOGGER.error("ApplicationTypes.save", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
		    }
            return showErrorPopup(e, getText(EXCEPTION_APPTYPES_SAVE));
        }
		if (isDebugEnabled) {
	        S_LOGGER.debug("ApplicationTypes.save : Exit");
	    }
		
		return list();
	}

	public String update() {
	    if (isDebugEnabled) {
	    	S_LOGGER.debug("ApplicationTypes.update : Entry");
	    }

		try {
			if (isDebugEnabled) {
				if (StringUtils.isEmpty(getAppTypeId())) {
					S_LOGGER.warn("ApplicationTypes.update", "status=\"Bad Request\"", "message=\"Application type Id is empty\"");
					return showErrorPopup(new PhrescoException("Application type Id is empty"), getText(EXCEPTION_APPTYPES_UPDATE));
				}
				if (StringUtils.isEmpty(getCustomerId())) {
					S_LOGGER.warn("ApplicationTypes.update", "status=\"Bad Request\"", "message=\"Customer Id is empty\"");
					return showErrorPopup(new PhrescoException("Customer Id is empty"), getText(EXCEPTION_APPTYPES_UPDATE));
				}
				S_LOGGER.info("ApplicationTypes.update", "customerId=" + "\"" + getCustomerId() + "\"");
			}
			ApplicationType appType = createAppType();
			getServiceManager().updateApplicationType(appType, getAppTypeId(), getCustomerId());
			addActionMessage(getText(APPTYPES_UPDATED, Collections.singletonList(getName())));
		} catch (PhrescoException e) {
			if (isDebugEnabled) {
		        S_LOGGER.error("ApplicationTypes.update", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
		    }
            return showErrorPopup(e, getText(EXCEPTION_APPTYPES_UPDATE));
        }
		if (isDebugEnabled) {
	    	S_LOGGER.debug("ApplicationTypes.update : Exit");
	    }

		return list();
	}

    private ApplicationType createAppType() throws PhrescoException {
    	if (isDebugEnabled) {
	    	S_LOGGER.debug("ApplicationTypes.createAppType : Entry");
	    }
        ApplicationType appType = new ApplicationType();
        if(StringUtils.isNotEmpty(getAppTypeId())) {
        	appType.setId(getAppTypeId());
        }
        appType.setName(getName());
        appType.setDescription(getDescription());
        appType.setCustomerIds(Arrays.asList(getCustomerId()));
        if (isDebugEnabled) {
	    	S_LOGGER.debug("ApplicationTypes.createAppType : Exit");
	    }
        
        return appType;
    }

	public String delete() throws PhrescoException {
		if (isDebugEnabled) {
	    	S_LOGGER.debug("ApplicationTypes.delete : Entry");
	    }

		try {
			String[] appTypeIds = getHttpRequest().getParameterValues(REQ_APP_TYPEID);
			if (isDebugEnabled) {
				if (ArrayUtils.isEmpty(appTypeIds)) {
					S_LOGGER.warn("ApplicationTypes.delete", "status=\"Bad Request\"", "message=\"No Application Type Ids found to delete\"");
					return showErrorPopup(new PhrescoException("No Applicaation Type Ids found to delete"), getText(EXCEPTION_APPTYPES_DELETE));
				}
			}
			if (ArrayUtils.isNotEmpty(appTypeIds)) {
				if (isDebugEnabled) {
					S_LOGGER.info("ApplicationTypes.delete", "appTypeIds=" + "\"" + appTypeIds.toString() + "\"");
				}
				for (String appTypeid : appTypeIds) {
					getServiceManager().deleteApplicationType(appTypeid, getCustomerId());
				}
				addActionMessage(getText(APPTYPES_DELETED));
			}
		} catch (PhrescoException e) {
			if (isDebugEnabled) {
		        S_LOGGER.error("ApplicationTypes.delete", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
		    }
		    return showErrorPopup(e, getText(EXCEPTION_APPTYPES_DELETE));
		}
		if (isDebugEnabled) {
	    	S_LOGGER.debug("ApplicationTypes.delete : Exit");
	    }

		return list();
	}

	public String validateForm() {
		if (isDebugEnabled) {
	    	S_LOGGER.debug("ApplicationTypes.validateForm : Entry");
	    }

	    boolean isError = false;
	    //Empty validation for name
		if (StringUtils.isEmpty(getName())) {			
		    setNameError(getText(KEY_I18N_ERR_NAME_EMPTY ));
			isError = true;
		}
		
		if (isError) {
            setErrorFound(true);
        }
		if (isDebugEnabled) {
	    	S_LOGGER.debug("ApplicationTypes.validateForm : Exit");
	    }
		
		return SUCCESS;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNameError() {
		return nameError;
	}

	public void setNameError(String nameError) {
		this.nameError = nameError;
	}

	public boolean isErrorFound() {
		return errorFound;
	}

	public void setErrorFound(boolean errorFound) {
		this.errorFound = errorFound;
	}

	public String getAppTypeId() {
		return appTypeId;
	}

	public void setAppTypeId(String appTypeId) {
		this.appTypeId = appTypeId;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	
	public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getFromPage() {
        return fromPage;
    }

    public void setFromPage(String fromPage) {
        this.fromPage = fromPage;
    }
}