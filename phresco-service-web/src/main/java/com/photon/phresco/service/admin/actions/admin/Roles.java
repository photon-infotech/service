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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.commons.model.Role;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.sun.jersey.api.client.ClientResponse;

public class Roles extends ServiceBaseAction { 
	
    private static final long serialVersionUID = 1L;
    
    private static final Logger S_LOGGER = Logger.getLogger(Roles.class);
	private static Boolean s_isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	private String name = "";
	private String description = "";
	
	private String nameError = "";
    private boolean errorFound = false;
	
	private String oldName = "";
	
	private String fromPage = "";
	
	private String roleId = "";
	
	public String list() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method Roles.list()");
		}
		
		try {
			List<Role> roleList = getServiceManager().getRoles();
			setReqAttribute(REQ_ROLE_LIST, roleList);
		} catch (PhrescoException e) {
		    return showErrorPopup(e, EXCEPTION_ROLE_LIST);
		}

		return ADMIN_ROLE_LIST;	
	}
	
	public String add() {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method Roles.add()");
		}
		
		return ADMIN_ROLE_ADD;	
	}
	
	public String edit() throws PhrescoException {
	    if (s_isDebugEnabled) {
	        S_LOGGER.debug("Entering Method Roles.edit()");
	    }
		
		try {
		    Role role = getServiceManager().getRole(getRoleId());
			setReqAttribute(REQ_ROLE_ROLE , role);
			setReqAttribute(REQ_FROM_PAGE, EDIT);
		} catch (PhrescoException e) {
		    return showErrorPopup(e, EXCEPTION_ROLE_EDIT);
		}
		
		return ADMIN_ROLE_ADD;
	}
	
	public String save() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method Roles.save()");
		}
		
		try  {
			List<Role> roleList = new ArrayList<Role>();
			Role role = new Role();
			role.setName(getName());
			role.setDescription(getDescription());
			roleList.add(role);
			ClientResponse clientResponse = getServiceManager().createRoles(roleList);
			if (clientResponse.getStatus() != RES_CODE_200) {
				addActionError(getText(ROLE_NOT_ADDED, Collections.singletonList(getName())));
			} else {
				addActionMessage(getText(ROLE_ADDED, Collections.singletonList(getName())));
			}	
		} catch (PhrescoException e) {
		    return showErrorPopup(e, EXCEPTION_ROLE_SAVE);
		}
		
		return  list();
	}
	
	public String update() throws PhrescoException {
	    if (s_isDebugEnabled) {
	        S_LOGGER.debug("Entering Method Roles.update()");
	    }
 
		try {
			Role role = new Role(getRoleId(), getName(), getDescription());
			getServiceManager().updateRole(role, getRoleId());
		} catch (PhrescoException e) {
		    return showErrorPopup(e, EXCEPTION_ROLE_UPDATE);
		}

		return list();
	}
	
	public String delete() throws PhrescoException {
	    if (s_isDebugEnabled) {
	        S_LOGGER.debug("Entering Method Roles.delete()");
	    }

		try {
			String[] roleIds = getHttpRequest().getParameterValues(REQ_ROLE_ID);
			if (ArrayUtils.isNotEmpty(roleIds)) {
				for (String roleId : roleIds) {
					ClientResponse clientResponse = getServiceManager().deleteRole(roleId);
					if (clientResponse.getStatus() != RES_CODE_200) {
						addActionError(getText(ROLE_NOT_DELETED));
					}
				}
				addActionMessage(getText(ROLE_DELETED));
			}
		} catch (PhrescoException e) {
		    return showErrorPopup(e, EXCEPTION_ROLE_DELETE);
		}

		return list();
	}
		
	public String validateForm() {
		if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method Roles.validateForm()");
        }
		
		boolean isError = false;
		
		//Empty validation for name
		if (StringUtils.isEmpty(getName())) {
			setNameError(getText(KEY_I18N_ERR_NAME_EMPTY));
			isError = true;
		} 
		
		if (isError) {
            setErrorFound(true);
        }
		
		return SUCCESS;
	}
	
	public String assign() {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method RolesList.assign()");
		}
		
		return ADMIN_ROLE_ASSIGN;	
	}
	
	public String assignSave() {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method RolesList.assignSave()");
		}
		
		return ADMIN_ROLE_ASSIGN_SAVE;	
	}
	
	public String assignCancel() {
		if (s_isDebugEnabled) {
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