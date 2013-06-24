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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.photon.phresco.commons.model.Role;
import com.photon.phresco.commons.model.User;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.util.ServiceConstants;

public class Users extends ServiceBaseAction { 

	private static final long serialVersionUID = 1L;

	//    private static final Logger S_LOGGER = Logger.getLogger(Customers.class);
	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(Users.class.getName());
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();

	private String userId = "";
	private String userName = "";
	private String selectedRoles = "";

	public String fetchUsersFromDB() throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("Users.getUsersFromDB : Entry");
		}

		try {
			List<User> userList = getServiceManager().getUsersFromDB();
			setReqAttribute(REQ_USER_LIST, userList);
		} catch (PhrescoException e){
			if(isDebugEnabled) {
				LOGGER.error("Users.fetchUsersFromDB", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			return showErrorPopup(e, getText(EXCEPTION_USERS_LIST));
		}

		if (isDebugEnabled) {
			LOGGER.debug("Users.getUsersFromDB : Exit");
		}

		return ADMIN_USER_LIST;
	}

	public String syncUsers() throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("Users.getSyncUsers : Entry");
		}

		try {
			List<User> userList = getServiceManager().getSyncUsers();
			setReqAttribute(REQ_USER_LIST, userList);
		} catch (PhrescoException e){
			if(isDebugEnabled) {
				LOGGER.error("Users.syncUsers", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			return showErrorPopup(e, getText(EXCEPTION_USERS_LIST));
		}

		if (isDebugEnabled) {
			LOGGER.debug("Users.getSyncUsers : Exit");
		}

		return ADMIN_USER_LIST;
	}

	public String showAssignRolesPopup() throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("Users.showAssignRolesPopup : Entry");
		}

		try {
			if(isDebugEnabled) {
				if (StringUtils.isEmpty(getUserId())) {
					LOGGER.warn("Users.showAssignRolesPopup", "status=\"Bad Request\"", "message=\"UserId is empty\"");
					return showErrorPopup(new PhrescoException("UserId is empty"), getText(EXCEPTION_ASSIGN_ROLE_TO_USER));
				}
				LOGGER.info("Users.showAssignRolesPopup", "userId=" + "\"" + getUserId());
			}

			if (StringUtils.isNotEmpty(getUserId())) {
				User user = getServiceManager().getUserInfo(getUserId());
				setReqAttribute(REQ_USER, user);
				List<Role> allRoles = getServiceManager().getRoles();
				setReqAttribute(REQ_ROLE_LIST, allRoles);
				Map<String, String> availableRoleMap = new HashMap<String, String>();
				if (CollectionUtils.isNotEmpty(user.getRoleIds())) {
					for (String availableUserRoleId : user.getRoleIds()) {
						Role role = getServiceManager().getRole(availableUserRoleId);
						availableRoleMap.put(availableUserRoleId, role.getName());
					}
				}
				setReqAttribute(REQ_ROLES_MAP, availableRoleMap);
			}
		} catch (PhrescoException e){
			if(isDebugEnabled) {
				LOGGER.error("Users.showAssignRolesPopup", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			return showErrorPopup(e, getText(EXCEPTION_ASSIGN_ROLE_TO_USER));
		}

		if (isDebugEnabled) {
			LOGGER.debug("Users.showAssignRolesPopup : Exit");
		}

		return ADMIN_USER;
	}

	public String assignRoles() throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("Users.assignRoles : Entry");
		}

		try {
			if(isDebugEnabled) {
				if (StringUtils.isEmpty(getUserId())) {
					LOGGER.warn("Users.assignRoles", "status=\"Bad Request\"", "message=\"UserId is empty\"");
					return showErrorPopup(new PhrescoException("UserId is empty"), getText(EXCEPTION_ASSIGN_ROLE_TO_USER));
				}
				LOGGER.info("Users.assignRoles", "userId=" + "\"" + getUserId());
			}
			if(isDebugEnabled) {
				if (StringUtils.isEmpty(getSelectedRoles())) {
					LOGGER.warn("Users.assignRoles", "status=\"Bad Request\"", "message=\"Selected Roles is empty\"");
					return showErrorPopup(new PhrescoException("Selected Roles is empty"), getText(EXCEPTION_ASSIGN_ROLE_TO_USER));
				}
				LOGGER.info("Users.assignRoles", "selectedRoles=" + "\"" + getSelectedRoles());
			}
			User user = getServiceManager().getUserInfo(getUserId());
			if (user != null && StringUtils.isNotEmpty(getSelectedRoles())) {
				List<String> rolesIds = Arrays.asList(getSelectedRoles().split(ServiceConstants.COMMA));
				user.setRoleIds(rolesIds);
				getServiceManager().updateUserInfo(user, userId);
				setUserName(user.getName());
				addActionMessage(getText(ROLE_ADDED_TO_USER, Collections.singletonList(user.getName())));
			}
		} catch (PhrescoException e) {
			if(isDebugEnabled) {
				LOGGER.error("Users.assignRoles", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			return showErrorPopup(e, getText(EXCEPTION_ASSIGN_ROLE_TO_USER));
		}

		if (isDebugEnabled) {
			LOGGER.debug("Users.assignRoles : Exit");
		}

		return fetchUsersFromDB();
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setSelectedRoles(String selectedRoles) {
		this.selectedRoles = selectedRoles;
	}

	public String getSelectedRoles() {
		return selectedRoles;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}