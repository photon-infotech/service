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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.commons.model.Permission;
import com.photon.phresco.commons.model.Role;
import com.photon.phresco.commons.model.User;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.service.client.api.ServiceManager;

public class Roles extends ServiceBaseAction { 
	
    private static final long serialVersionUID = 1L;
    
    private static final Logger S_LOGGER = Logger.getLogger(Roles.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	private String name = "";
	private String description = "";
	
	private String nameError = "";
    private boolean errorFound = false;
	
	private String oldName = "";
	
	private boolean editable = false;
	private String fromPage = "";
	
	private String roleId = "";
	private String appliesTo = "";
	private String userId = "";
	private List<Role> availableRoles = new ArrayList<Role>();
	private List<Role> roles = new ArrayList<Role>();
	
	private List<String> selectedPermissions = new ArrayList<String>();
	
	public String list() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method Roles.list()");
		}
		
		try {
			List<Role> roleList = getServiceManager().getRoles(getAppliesTo());
			setReqAttribute(REQ_ROLE_LIST, roleList);
			setReqAttribute(REQ_APPLIES_TO, getAppliesTo());
		} catch (PhrescoException e) {
		    return showErrorPopup(e, getText(EXCEPTION_ROLE_LIST));
		}

		return ADMIN_ROLE_LIST;	
	}
	
	public String add() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method Roles.add()");
		}
		
		setReqAttribute(REQ_APPLIES_TO, getAppliesTo());
		
		return ADMIN_ROLE_ADD;	
	}
	
	public String edit() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method Roles.edit()");
	    }
		
		try {
		    Role role = getServiceManager().getRole(getRoleId());
			setReqAttribute(REQ_ROLE_ROLE , role);
			setReqAttribute(REQ_FROM_PAGE, EDIT);
			setReqAttribute(REQ_APPLIES_TO, getAppliesTo());
		} catch (PhrescoException e) {
		    return showErrorPopup(e, getText(EXCEPTION_ROLE_EDIT));
		}
		
		return ADMIN_ROLE_ADD;
	}
	
	public String save() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method Roles.save()");
		}

		try  {
			List<Role> roles = new ArrayList<Role>();
			roles.add(createRole());
			getServiceManager().createRoles(roles);
			addActionMessage(getText(ROLE_ADDED, Collections.singletonList(getName())));
		} catch (PhrescoException e) {
			return showErrorPopup(e, getText(EXCEPTION_ROLE_SAVE));
		}

		return  list();
	}
	
	public String update() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method Roles.update()");
	    }
 
		try {
			getServiceManager().updateRole(createRole(), getRoleId());
			addActionMessage(getText(ROLE_UPDATED, Collections.singletonList(getName())));
		} catch (PhrescoException e) {
		    return showErrorPopup(e, getText(EXCEPTION_ROLE_UPDATE));
		}

		return list();
	}
	
	public String roleListToAssign() {
		if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method Roles.roleListToAssign()");
	    }
		
		try {
			ServiceManager serviceManager = getServiceManager();
			setRoles(serviceManager.getRoles(getAppliesTo()));
			User user = serviceManager.getUserInfo(getUserId());
			List<String> roleIds = user.getRoleIds();
			if (CollectionUtils.isNotEmpty(roleIds)) {
				for (String roleId : roleIds) {
					Role role = getServiceManager().getRole(roleId);
					if (role.getAppliesTo().equalsIgnoreCase(getAppliesTo())) {
						availableRoles.add(role);
					}
				}
			}
				
		} catch (PhrescoException e) {
			
		}
		
		return SUCCESS;
	}
	
	private Role createRole() {
		Role role = new Role();
		if (StringUtils.isNotEmpty(getRoleId())) {
			role.setId(getRoleId());
		}
		role.setName(getName());
		role.setDescription(getDescription());
		role.setAppliesTo(getAppliesTo());
		role.setAppliesTo(getAppliesTo());
		
		return role;
	}
	
	public String delete() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method Roles.delete()");
	    }

		try {
			String[] roleIds = getHttpRequest().getParameterValues(REQ_ROLE_ID);
			if (ArrayUtils.isNotEmpty(roleIds)) {
				for (String roleid : roleIds) {
					getServiceManager().deleteRole(roleid);
				}
				addActionMessage(getText(ROLE_DELETED));
			}
		} catch (PhrescoException e) {
		    return showErrorPopup(e, getText(EXCEPTION_ROLE_DELETE));
		}

		return list();
	}
		
	public String validateForm() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method Roles.validateForm()");
		}

		boolean isError = false;
		try {
			//Empty validation for name
			if (StringUtils.isEmpty(getName())) {
				setNameError(getText(KEY_I18N_ERR_NAME_EMPTY));
				isError = true;
			}
			
			//Duplicate validation for name
			if (StringUtils.isNotEmpty(getName())) {
				List<Role> roles = getServiceManager().getRoles();
				if (CollectionUtils.isNotEmpty(roles)) {
					for (Role role : roles) {
						if (!getOldName().equalsIgnoreCase(getName()) && role.getName().equalsIgnoreCase(getName())) {
							setNameError(getText(KEY_I18N_ERR_NAME_ALREADY_EXIST));
							isError = true;
							break;
						}
					}
				}
			}
			if (isError) {
				setErrorFound(true);
			}
		} catch (PhrescoException e) {
			return showErrorPopup(e, getText(EXCEPTION_ROLE_VALIDATE));
		}
		
		return SUCCESS;
	}
	
	public String showAssignPermPopup() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method RolesList.showAssignPermPopup()");
		}
		
		try {
			setReqAttribute(REQ_ROLE_NAME, getName());
			setReqAttribute(REQ_ROLE_ID, getRoleId());
			ServiceManager serviceManager = getServiceManager();
			Role role = serviceManager.getRole(getRoleId());
			setReqAttribute(REQ_SELECTED_PERMISSION_IDS, role.getPermissionIds());
			List<Permission> permissions = serviceManager.getPermissions(getAppliesTo());
			setReqAttribute(REQ_PERMISSIONS_LIST, permissions);
			setReqAttribute(REQ_ROLE_EDITABLE, isEditable());
		} catch (PhrescoException e) {
			return showErrorPopup(e, getText(EXCEPTION_ROLE_ASSIGN_PERMISSION_POPUP));
		}
		
		return ADMIN_ROLE_ASSIGN;	
	}
	
	public String assignPermission() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method RolesList.assignPermission()");
		}
		
		try {
			ServiceManager serviceManager = getServiceManager();
			Role role = serviceManager.getRole(getRoleId());
			role.setPermissionIds(getSelectedPermissions());
			serviceManager.updateRole(role, getRoleId());
			addActionMessage(getText(PERMISSION_ADDED_TO_ROLE, Collections.singletonList(role.getName())));
		} catch (PhrescoException e) {
			return showErrorPopup(e, getText(EXCEPTION_ROLE_ASSIGN_PERMISSION));
		}
		
		return list();	
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

	public void setSelectedPermissions(List<String> selectedPermissions) {
		this.selectedPermissions = selectedPermissions;
	}

	public List<String> getSelectedPermissions() {
		return selectedPermissions;
	}

	public void setAppliesTo(String appliesTo) {
		this.appliesTo = appliesTo;
	}

	public String getAppliesTo() {
		return appliesTo;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setAvailableRoles(List<Role> availableRoles) {
		this.availableRoles = availableRoles;
	}

	public List<Role> getAvailableRoles() {
		return availableRoles;
	}
}