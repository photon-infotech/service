/**
 * Service Web Archive
 *
 * Copyright (C) 1999-2014 Photon Infotech Inc.
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
package com.photon.phresco.service.admin.actions.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.photon.phresco.commons.model.Property;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;

public class GlobalUrlAction extends ServiceBaseAction { 

	private static final long serialVersionUID = 6801037145464060759L;

	//	private static final Logger S_LOGGER = Logger.getLogger(GlobalUrlAction.class);
	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(GlobalUrlAction.class.getName());
	private static Boolean s_isDebugEnabled = LOGGER.isDebugEnabled();

	private String name = "";
	private String description = "";
	private String url = "";

	private String nameError = "";
	private String urlError = "";
	private boolean errorFound = false;
	private boolean errorUrl = false;

	private String globalurlId = "";

	private String fromPage = "";
	private String oldName = "";

	//To get the all the globalURLs from the DB
	public String list() throws PhrescoException {
		if (s_isDebugEnabled) {
			LOGGER.debug("GlobalUrlAction.list : Entry");
		}
		try {
			List<Property> globalUrls = getServiceManager().getGlobalUrls();
			setReqAttribute(REQ_GLOBURL_URL, globalUrls);
		} catch (PhrescoException e) {
			if(s_isDebugEnabled) {
				LOGGER.error("GlobalUrlAction.list", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			return showErrorPopup(e, getText(EXCEPTION_GLOBAL_URL_LIST));
		}

		if (s_isDebugEnabled) {
			LOGGER.debug("GlobalUrlAction.list : Exit");
		}

		return ADMIN_GLOBALURL_LIST;	
	}

	//To return the page to add GlobalURL
	public String add() {
		if (s_isDebugEnabled) {
			LOGGER.debug("GlobalUrlAction.add : Entry");
		}
		setReqAttribute(REQ_FROM_PAGE, ADD);

		if (s_isDebugEnabled) {
			LOGGER.debug("GlobalUrlAction.add : Exit");
		}

		return ADMIN_GLOBALURL_ADD;
	}

	//To return the edit page with the details of the selected GlobalURL
	public String edit() {
		if (s_isDebugEnabled) {
			LOGGER.debug("GlobalUrlAction.edit : Entry");
		}
		try {
			if(s_isDebugEnabled) {
				if (StringUtils.isEmpty(getGlobalurlId())) {
					LOGGER.warn("GlobalUrlAction.edit", "status=\"Bad Request\"", "message=\"Globalurl id is empty\"");
					return showErrorPopup(new PhrescoException("Globalurl id is empty"), getText(EXCEPTION_GLOBAL_URL_EDIT));
				}
				LOGGER.info("GlobalUrlAction.edit", "globalUrlId=" + "\"" + getGlobalurlId());
			}
			Property globalUrl = getServiceManager().getGlobalUrl(getGlobalurlId());
			setReqAttribute(REQ_GLOBURL_URL , globalUrl);
			setReqAttribute(REQ_FROM_PAGE, EDIT);
		} catch (PhrescoException e) {
			if(s_isDebugEnabled) {
				LOGGER.error("GlobalUrlAction.edit", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			return showErrorPopup(e, getText(EXCEPTION_GLOBAL_URL_EDIT));
		}

		if (s_isDebugEnabled) {
			LOGGER.debug("GlobalUrlAction.edit : Exit");
		}

		return ADMIN_GLOBALURL_ADD;
	}

	//To create a GlobalURL with the provided details
	public String save() throws PhrescoException {
		if (s_isDebugEnabled) {
			LOGGER.debug("GlobalUrlAction.save : Entry");
		}
		try  {
			if(s_isDebugEnabled) {
				if (createGlobalURL() == null) {
					LOGGER.warn("GlobalUrlAction.save", "status=\"Bad Request\"", "message=\"Globalurl is empty\"");
					return showErrorPopup(new PhrescoException("Globalurl is empty"), getText(EXCEPTION_GLOBAL_URL_SAVE));
				}
				LOGGER.info("GlobalUrlAction.edit", "globalUrl=" + "\"" + createGlobalURL());
			}
			List<Property> globalURLs = new ArrayList<Property>();
			globalURLs.add(createGlobalURL());
			getServiceManager().createGlobalUrl(globalURLs);
			addActionMessage(getText(URL_ADDED, Collections.singletonList(getName())));
		}  catch (PhrescoException e) {
			if(s_isDebugEnabled) {
				LOGGER.error("GlobalUrlAction.save", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			return showErrorPopup(e, getText(EXCEPTION_GLOBAL_URL_SAVE));
		}

		if (s_isDebugEnabled) {
			LOGGER.debug("GlobalUrlAction.save : Exit");
		}

		return list();
	}

	private Property createGlobalURL() {
		Property globalUrl = new Property();
		if (StringUtils.isNotEmpty(getGlobalurlId())) {
			globalUrl.setId(getGlobalurlId());
		}

		globalUrl.setName(getName());
		globalUrl.setDescription(getDescription());
		globalUrl.setKey(globalUrl.getId());
		globalUrl.setValue(getUrl());
		return globalUrl;
	}

	//To update the details of the selected GlobalURL
	public String update() throws PhrescoException { 
		if (s_isDebugEnabled) {
			LOGGER.debug("GlobalUrlAction.update : Entry");
		}
		try {
			if(s_isDebugEnabled) {
				if (createGlobalURL() == null) {
					LOGGER.warn("GlobalUrlAction.update", "status=\"Bad Request\"", "message=\"Globalurl is empty\"");
					return showErrorPopup(new PhrescoException("Globalurl is empty"), getText(EXCEPTION_GLOBAL_URL_UPDATE));
				}
				LOGGER.info("GlobalUrlAction.update", "globalUrl=" + "\"" + createGlobalURL());
			}
			if(s_isDebugEnabled) {
				if (StringUtils.isEmpty(getGlobalurlId())) {
					LOGGER.warn("GlobalUrlAction.update", "status=\"Bad Request\"", "message=\"Globalurl id is empty\"");
					return showErrorPopup(new PhrescoException("Globalurl id is empty"), getText(EXCEPTION_GLOBAL_URL_UPDATE));
				}
				LOGGER.info("GlobalUrlAction.update", "globalUrlId=" + "\"" + getGlobalurlId());
			}
			getServiceManager().updateGlobalUrl(createGlobalURL(), getGlobalurlId());
			addActionMessage(getText(URL_UPDATED, Collections.singletonList(getName())));
		} catch (PhrescoException e) {
			if(s_isDebugEnabled) {
				LOGGER.error("GlobalUrlAction.update", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			return showErrorPopup(e, getText(EXCEPTION_GLOBAL_URL_UPDATE));
		}

		if (s_isDebugEnabled) {
			LOGGER.debug("GlobalUrlAction.update : Exit");
		}

		return list();
	}

	//To delete the selected GlobalURLs
	public String delete() throws PhrescoException {
		if (s_isDebugEnabled) {
			LOGGER.debug("GlobalUrlAction.delete : Entry");
		}
		try {
			String[] globalUrlIds = getHttpRequest().getParameterValues(REQ_GLOBURL_ID);
			if(s_isDebugEnabled) {
				if (globalUrlIds == null) {
					LOGGER.warn("GlobalUrlAction.delete", "status=\"Bad Request\"", "message=\"Globalurl ids is empty\"");
					return showErrorPopup(new PhrescoException("Globalurl ids is empty"), getText(EXCEPTION_GLOBAL_URL_UPDATE));
				}
				LOGGER.info("GlobalUrlAction.delete", "globalUrlIds=" + "\"" + globalUrlIds);
			}
			if (globalUrlIds != null && globalUrlIds.length != 0) {
				for (String globalUrlId : globalUrlIds) {
					getServiceManager().deleteGlobalUrl(globalUrlId);
				}
				addActionMessage(getText(URL_DELETED));
			}
		} catch (PhrescoException e) {
			if(s_isDebugEnabled) {
				LOGGER.error("GlobalUrlAction.delete", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			return showErrorPopup(e, getText(EXCEPTION_GLOBAL_URL_DELETE));
		}

		if (s_isDebugEnabled) {
			LOGGER.debug("GlobalUrlAction.delete : Exit");
		}

		return list();
	}

	//To validate the form values passed from the jsp
	public String validateForm() throws PhrescoException {
		if (s_isDebugEnabled) {
			LOGGER.debug("GlobalUrlAction.validateForm : Entry");
		}

		boolean isError = false;
		//Empty validation for name
		isError = nameValidation();

		//validation for URL
		isError = UrlValidation();

		if (isError) {
			setErrorFound(true);
		}
		if (s_isDebugEnabled) {
			LOGGER.debug("GlobalUrlAction.validateForm : Exit");
		}
		return SUCCESS;
	}

	public boolean nameValidation() throws PhrescoException {
		if (StringUtils.isEmpty(getName())) {
			setNameError(getText(KEY_I18N_ERR_NAME_EMPTY));
			errorUrl = true;
		} else if (StringUtils.isEmpty(getFromPage()) || (!getName().equals(getOldName()))) {
			List<Property> globalUrl = getServiceManager().getGlobalUrls();
			if (CollectionUtils.isNotEmpty(globalUrl)) {
				for (Property property : globalUrl) {
					if(property.getName().equalsIgnoreCase(getName())) {
						setNameError(getText(KEY_I18N_ERR_NAME_ALREADY_EXIST));
						errorUrl=true;
						break;
					}
				}
			}			
		}
		return errorUrl ;
	}

	public boolean UrlValidation() throws PhrescoException {
		if (StringUtils.isEmpty(getUrl())) {
			setUrlError(getText(KEY_I18N_ERR_URL_EMPTY));
			errorUrl= true;
		} else if (StringUtils.isNotEmpty(getUrl())) {			
			String urlPattern = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
			Pattern pattern = Pattern.compile(urlPattern);
			Matcher matcher = pattern.matcher(getUrl());
			boolean matchFound = matcher.matches();
			if (!matchFound) {
				setUrlError(getText(KEY_I18N_ERR_URL_NOT_VALID));
				errorUrl=true;
			}
		}
		return errorUrl;
	}

	public String cancel() {
		if (s_isDebugEnabled) {
			LOGGER.debug("GlobalUrlAction.list : Entry");
		}

		if (s_isDebugEnabled) {
			LOGGER.debug("GlobalUrlAction.list : Exit");
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

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	public String getFromPage() {
		return fromPage;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}


}