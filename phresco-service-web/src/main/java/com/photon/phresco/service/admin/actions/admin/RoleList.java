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

import com.photon.phresco.commons.model.Role;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.util.ServiceConstants;
import com.sun.jersey.api.client.ClientResponse;

public class RoleList extends ServiceBaseAction { 
	
	private static final long serialVersionUID = 6801037145464060759L;
	private static final Logger S_LOGGER = Logger.getLogger(RoleList.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	private String name = null;
	private String nameError = null;
	private boolean errorFound = false;
	private String description = null;
	private String oldName = null;
	private String fromPage = null;
	private String customerId = null;
	private String roleId = null;
	
	public String list() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method RolesList.list()");
		}
		
		try {
			List<Role> roleList = getServiceManager().getRoles();
			getHttpRequest().setAttribute(REQ_ROLE_LIST, roleList);
		} catch(Exception e) {
			throw new PhrescoException(e);
		}

		return ADMIN_ROLE_LIST;	
	}
	
	public String add() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method RolesList.add()");
		}
		
		return ADMIN_ROLE_ADD;	
	}
	
	public String edit() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method RoleList.edit()");
	    }
		
		try {
		    Role role = getServiceManager().getRole(roleId);
			getHttpRequest().setAttribute(REQ_ROLE_ROLE , role);
			getHttpRequest().setAttribute(REQ_FROM_PAGE, fromPage);
		} catch (Exception e) {
		    throw new PhrescoException(e);
		}
		
		return ADMIN_ROLE_ADD;
	}
	
	public String save() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method RolesList.save()");
		}
		
		try  {
			List<Role> roleList = new ArrayList<Role>();
			Role role = new Role();
			role.setName(name);
			role.setDescription(description);
			roleList.add(role);
			ClientResponse clientResponse = getServiceManager().createRoles(roleList);
			if(clientResponse.getStatus() != ServiceConstants.RES_CODE_200){
				addActionError(getText(ROLE_NOT_ADDED, Collections.singletonList(name)));
			} else {
				addActionMessage(getText(ROLE_ADDED, Collections.singletonList(name)));
			}	
		} catch (Exception e) {

		}
		
		return  list();
	}
	
	public String update() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method RoleList.update()");
	    }
 
		try {
			Role role = new Role(name, description);
			role.setId(roleId);
			getServiceManager().updateRole(role, roleId);
		} catch(Exception e)  {
			throw new PhrescoException(e);
		}

		return list();
	}
	
	public String delete() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method RoleList.delete()");
	    }

		try {
			String[] roleIds = getHttpRequest().getParameterValues(REQ_ROLE_ID);
			if (roleIds != null) {
				for (String roleId : roleIds) {
					ClientResponse clientResponse = getServiceManager().deleteRole(roleId);
					if (clientResponse.getStatus() != ServiceConstants.RES_CODE_200) {
						addActionError(getText(ROLE_NOT_DELETED));
					}
				}
				addActionMessage(getText(ROLE_DELETED));
			}
		} catch (Exception e) {
			throw new PhrescoException(e);
		}

		return list();
	}
		
	public String validateForm() {
		if (isDebugEnabled) {
            S_LOGGER.debug("Entering Method RoleList.validateForm()");
        }
		
		boolean isError = false;
		if (StringUtils.isEmpty(name)) {
			setNameError(getText(KEY_I18N_ERR_NAME_EMPTY));
			isError = true;
		} 
		
		if (isError) {
            setErrorFound(true);
        }
		
		return SUCCESS;
	}
	
	public String assign() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method RolesList.assign()");
		}
		
		return ADMIN_ROLE_ASSIGN;	
	}
	
	public String assignSave() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method RolesList.assignSave()");
		}
		
		return ADMIN_ROLE_ASSIGN_SAVE;	
	}
	
	public String assignCancel() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method RolesList.assignCancel()");
		}
		
		return ADMIN_ROLE_ASSIGN_CANCEL;	
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

	public void setErrorFound(boolean errorFound) {
		this.errorFound = errorFound;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}
	
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}