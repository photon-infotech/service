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
package com.photon.phresco.service.admin.actions.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.commons.model.Property;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.sun.jersey.api.client.ClientResponse;

public class GlobalUrl extends ServiceBaseAction { 
	
	private static final long serialVersionUID = 6801037145464060759L;
	private static final Logger S_LOGGER = Logger.getLogger(GlobalUrl.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	private String name = null;
	private String description = "";
	private String nameError = null;
	private String url = null;
	private String urlError = null;
	private boolean errorFound = false;
	private String customerId = "";
	private String globalurlId ="";
	
	public String list() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method GlobalUrlList.list()");
		}
         
		try {
    		List<Property> globalUrl = getServiceManager().getGlobalUrls(customerId);
    		getHttpRequest().setAttribute(REQ_GLOBURL_URL, globalUrl);
    		getHttpRequest().setAttribute(REQ_CUST_CUSTOMER_ID, customerId);
    	} catch (PhrescoException e) {
//			new LogErrorReport(e, GLOBAL_URL_LIST_EXCEPTION);
			
			return LOG_ERROR;	
		}

    	
		return ADMIN_GLOBALURL_LIST;	
	}
	
	public String add() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method GlobalUrlList.add()");
		}

		return ADMIN_GLOBALURL_ADD;
	}
	
	public String edit() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method GlobalUrlList.edit()");
		}
		
		try {
			  Property globalUrl = getServiceManager().getGlobalUrl(globalurlId, customerId);
			  getHttpRequest().setAttribute(REQ_GLOBURL_URL , globalUrl);
			  getHttpRequest().setAttribute(REQ_FROM_PAGE, EDIT);
		} catch (PhrescoException e) {
//			new LogErrorReport(e, GLOBAL_URL_EDIT_EXCEPTION);
			
			return LOG_ERROR;	
		}
		
		return ADMIN_GLOBALURL_ADD;
	}
	
	public String save() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method GlobalUrlList.save()");
		}

		try  {
			List<Property> globalUrls = new ArrayList<Property>();
			Property globalUrl = new Property();
			globalUrl.setName(name);
			globalUrl.setDescription(description);
			//TODO Arunpraanna
			//globalUrl.setUrl(url);
			globalUrls.add(globalUrl);
			ClientResponse clientResponse = getServiceManager().createGlobalUrl(globalUrls, customerId);
			if( clientResponse.getStatus() != 200 && clientResponse.getStatus() != 201) {
				addActionError(getText(URL_NOT_ADDED, Collections.singletonList(name)));
			} else {
			    addActionMessage(getText(URL_ADDED, Collections.singletonList(name)));
			}
		}  catch (PhrescoException e) {
//			new LogErrorReport(e, GLOBAL_URL_SAVE_EXCEPTION);
			
			return LOG_ERROR;	
		}

		return  list();
	}
	
	public String update() throws PhrescoException { 
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method GlobalUrlList.update()");
		}
		
		try {
			Property globalUrl = new Property();
			globalUrl.setName(name);
			globalUrl.setDescription(description);
			//TODO Arunprasanna
			//globalUrl.setUrl(url);
			getServiceManager().updateGlobalUrl(globalUrl, globalurlId, customerId);
		} catch (PhrescoException e) {
//			new LogErrorReport(e, GLOBAL_URL_UPDATE_EXCEPTION);
			
			return LOG_ERROR;	
		}
		
		return list();
	}
	
	public String delete() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method GlobalUrlList.delete()");
	    }

		try {
			String[] globalUrlIds = getHttpRequest().getParameterValues(REQ_GLOBURL_ID);
			if (globalUrlIds != null) {
				for (String globalUrlId : globalUrlIds) {
					ClientResponse clientResponse = getServiceManager().deleteglobalUrl(globalUrlId, customerId);
					if (clientResponse.getStatus() != 200) {
						addActionError(getText(URL_NOT_DELETED));
					}
				}
				addActionMessage(getText(URL_DELETED));
			}
		} catch (PhrescoException e) {
//			new LogErrorReport(e, GLOBAL_URL_DELETE_EXCEPTION);
			
			return LOG_ERROR;	
		}

		return list();
	}
	
	public String validateForm() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method GlobalUrlList.validateForm()");
		}

		boolean isError = false;
		if (StringUtils.isEmpty(name)) {
			setNameError(getText(KEY_I18N_ERR_NAME_EMPTY));
			isError = true;
		} 

		if (StringUtils.isEmpty(url)) {
			setUrlError(getText(KEY_I18N_ERR_URL_EMPTY));
			isError = true;
		}
		
		if (isError) {
            setErrorFound(true);
        }

		return SUCCESS;
	}
	
	public String cancel() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method GlobalUrlList.list()");
		}

		return ADMIN_GLOBALURL_CANCEL;
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

	public boolean isErrorFound() {
		return errorFound;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlError() {
		return urlError;
	}

	public void setUrlError(String urlError) {
		this.urlError = urlError;
	}

	public void setErrorFound(boolean errorFound) {
		this.errorFound = errorFound;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGlobalurlId() {
		return globalurlId;
	}

	public void setGlobalurlId(String globalurlId) {
		this.globalurlId = globalurlId;
	}

	
}