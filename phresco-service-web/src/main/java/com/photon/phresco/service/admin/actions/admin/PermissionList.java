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

import java.util.List;

import org.apache.log4j.Logger;

import com.photon.phresco.commons.model.Permission;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.service.admin.commons.LogErrorReport;
import com.photon.phresco.util.ServiceConstants;
import com.sun.jersey.api.client.ClientResponse;

public class PermissionList extends ServiceBaseAction {
	
	private static final long serialVersionUID = 1L;
	private static final Logger S_LOGGER = Logger.getLogger(PermissionList.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	public String list() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method PermissionList.list()");
		}
		
		try {
			List<Permission> permissions = getServiceManager().getPermissions();
			getHttpRequest().setAttribute(REQ_PERMISSIONS_LIST, permissions);
		} catch (PhrescoException e) {
			new LogErrorReport(e, PERMISSION_LIST_EXCEPTION);

			return LOG_ERROR;
		}

		return ADMIN_PERMISSION_LIST;	
	}
	
	public String delete() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method Archetypes.delete()");
		}

		try {
			String[] permissionIds = getHttpRequest().getParameterValues(REQ_PERMISSIONS_ID);
			if (permissionIds != null) {
				for (String permissionId : permissionIds) {
					ClientResponse clientResponse = getServiceManager().deletePermission(permissionId);
					if (clientResponse.getStatus() != ServiceConstants.RES_CODE_200) {
						addActionError(getText(PERMISSION_NOT_DELETED));
					}
				}
				addActionMessage(getText(PERMISSION_DELETED));
			}
		} catch (PhrescoException e) {
			new LogErrorReport(e, PERMISSION_DELETE_EXCEPTION);
			
			return LOG_ERROR;	
		}

		return list();
	}
}