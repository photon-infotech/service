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
package com.photon.phresco.service.admin.actions.admin;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.photon.phresco.commons.model.Permission;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;

public class PermissionsAction extends ServiceBaseAction {

	private static final long serialVersionUID = 1L;

	//	private static final Logger S_LOGGER = Logger.getLogger(PermissionsAction.class);
	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(PermissionsAction.class.getName());
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();

	private String fromPage = "";
	private String appliesTo = "";

	//To get the all the permissions 
	public String list() throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("PermissionsAction.list : Entry");
		}

		try {
			if(isDebugEnabled) {
				if (StringUtils.isEmpty(getAppliesTo())) {
					LOGGER.warn("PermissionsAction.list", "status=\"Bad Request\"", "message=\"Applies to is empty\"");
					return showErrorPopup(new PhrescoException("Applies to is empty"), getText(EXCEPTION_PERMISSION_LIST));
				}
				LOGGER.info("PermissionsAction.list", "appliesTo=" + "\"" + getAppliesTo());
			}
			List<Permission> permissions = getServiceManager().getPermissions(getAppliesTo());
			setReqAttribute(REQ_PERMISSIONS_LIST, permissions);
		} catch (PhrescoException e) {
			if(isDebugEnabled) {
				LOGGER.error("PermissionsAction.list", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			return showErrorPopup(e, getText(EXCEPTION_PERMISSION_LIST));
		}

		if (isDebugEnabled) {
			LOGGER.debug("PermissionsAction.list : Exit");
		}

		return ADMIN_PERMISSION_LIST;	
	}

	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	public void setAppliesTo(String appliesTo) {
		this.appliesTo = appliesTo;
	}

	public String getAppliesTo() {
		return appliesTo;
	}
}