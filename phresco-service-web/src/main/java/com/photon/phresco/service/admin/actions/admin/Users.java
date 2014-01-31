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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.photon.phresco.commons.model.Role;
import com.photon.phresco.commons.model.User;
import com.photon.phresco.commons.model.User.AuthType;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.service.util.ServerUtil;
import com.photon.phresco.util.ServiceConstants;

public class Users extends ServiceBaseAction { 

	private static final long serialVersionUID = 1L;

	//    private static final Logger S_LOGGER = Logger.getLogger(Customers.class);
	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(Users.class.getName());
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();

	private String id = "";
	private String name = "";
	private String userId = "";
	private String userName = "";
	private String selectedRoles = "";
	private String firstname = "";
	private String lastname = "";
	private String loginid = "";
	private String userPwd = "";
	private String userrePwd = "";
	private String email = "";
	private String fromPage = "";
	private String oldName = "";
	
	private String firstNameError = "";
	private String lastNameError  = "";
	private String loginidError   = "";
	private String userPwdErr     = "";
	private String userRePwdErr   = "";
	private String mailError      = "";  
	private boolean errorFound = false;

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

	public String add() {
		if (isDebugEnabled) {
			LOGGER.debug("Users.add : Entry");
		}
        
		setReqAttribute(REQ_FROM_PAGE, ADD);
		
		return ADMIN_USER_ADD;
	}

	public String save() throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("Users.save : Entry");
		}

		List<User> users = new ArrayList<User>();
		users.add(createUser());
		getServiceManager().createUsers(users, getId(), getFromPage());
		addActionMessage(getText(USER_ADDED, Collections.singletonList(getFirstname() + getLastname())));

		return ADMIN_USER_LIST;
	}

	public String update() throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("Users.update : Entry");
		}

		List<User> users = new ArrayList<User>();
		users.add(createUser());
		getServiceManager().createUsers(users, getId(), getFromPage());
		addActionMessage(getText(USER_UPDATED, Collections.singletonList(getFirstname() + getLastname())));

		return ADMIN_USER_LIST;
	}

	public String edit() throws PhrescoException{
		if (isDebugEnabled) {
			LOGGER.debug("Users.edit : Entry");
		}

		User user= getServiceManager().getUser(getId());
		setReqAttribute(REQ_FROM_PAGE, getFromPage());
		setReqAttribute(USERS, user);

		return ADMIN_USER_ADD;	
	}
	
	public String delete() throws PhrescoException{
		if (isDebugEnabled) {
			LOGGER.debug("Users.delete : Entry");
		}
		
		String[] userIds = getHttpRequest().getParameterValues("userId");
		if (ArrayUtils.isNotEmpty(userIds)) {
			for (String userid : userIds) {
				getServiceManager().deleteUser(userid);
			}
			addActionMessage(getText(USER_DELETED));
		}

		return ADMIN_USER_LIST;
	}

	private User createUser() throws PhrescoException {
		User user = new User();
		if (StringUtils.isNotEmpty(getId())) {
			user.setId(getId());
		}
		user.setFirstName(getFirstname());
		user.setLastName(getLastname());
		user.setName(getLoginid());
		user.setDisplayName(getFirstname()+getLastname());
		if (ADD.equals(getFromPage())) {
		   user.setPassword(ServerUtil.encodeUsingHash(getLoginid(), getUserPwd()));
		} else {
			User userPwd= getServiceManager().getUser(getId());
			user.setPassword(userPwd.getPassword());
		}
		user.setEmail(getEmail());
		user.setLoginId(getLoginid());
		AuthType authtype = AuthType.valueOf("LOCAL");
		user.setAuthType(authtype);

		return user;
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
				getServiceManager().createUsers(Collections.singletonList(user), getUserId(), EDIT);
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

	public String validateForm() throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("User.validateForm : Entry");
		}

		try {
			boolean isError = false;
			//Empty validation for name

			if (StringUtils.isEmpty(getFirstname())) {
				setFirstNameError(getText(KEY_I18N_ERR_FIRST_NAME_EMPTY));
				isError = true;
			}
			
			if (StringUtils.isEmpty(getLastname())) {
				setLastNameError(getText(KEY_I18N_ERR_LAST_NAME_EMPTY));
				isError = true;
			}
			
			if (StringUtils.isEmpty(getLoginid())) {
				setLoginidError(getText(KEY_I18N_ERR_LOGINID_EMPTY));
				isError = true;
			} 
			
			if (ADD.equals(getFromPage()) || (!getLoginid().equals(getOldName()))) {
				// to check duplication of name
				List<User> usersv = getServiceManager().getUsersFromDB();
				if (CollectionUtils.isNotEmpty(usersv)) {
					for (User userte : usersv) {
						if (StringUtils.isNotEmpty(userte.getLoginId())) {
							if (userte.getLoginId().equalsIgnoreCase(getLoginid())) {
								setLoginidError(getText(KEY_I18N_ERR_NAME_ALREADY_EXIST));
								isError = true;
								break;
							}
						}
					}
				}
			}
            
			if (StringUtils.isEmpty(getUserPwd()) && ADD.equals(getFromPage())) {
				setUserPwdErr(getText(KEY_I18N_ERR_PWD_EMPTY));
				isError = true;
			}
			
			if (StringUtils.isEmpty(getUserrePwd()) && ADD.equals(getFromPage())) {
				setUserRePwdErr(getText(KEY_I18N_ERR_REPWD_EMPTY));
				isError = true;
			}
			
			if (!getUserPwd().equals(getUserrePwd())) {
				setUserRePwdErr(getText(KEY_I18N_ERR_PWD_MISMATCH));
				isError = true;
			}
			
			if (StringUtils.isEmpty(getEmail())) {
				setMailError(getText(KEY_I18N_ERR_EMAIL_EMPTY));
				isError = true;
			}
			
			if (StringUtils.isNotEmpty(getEmail())) {
				Pattern p = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
				Matcher m = p.matcher(getEmail());
				boolean b = m.matches();
				if (!b) {
					setMailError(getText(INVALID_EMAIL));
					isError = true;
				}
			}
			
			if (isError) {
				setErrorFound(true);
			}
		} catch (Exception e) {
			if(isDebugEnabled) {
				LOGGER.error("User.validateForm", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
			//return showErrorPopup(e, getText(EXCEPTION_USERS_VALIDATE));
		}

		if (isDebugEnabled) {
			LOGGER.debug("User.validateForm : Exit");
		}

		return SUCCESS;
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

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getLoginid() {
		return loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserrePwd() {
		return userrePwd;
	}

	public void setUserrePwd(String userrePwd) {
		this.userrePwd = userrePwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstNameError() {
		return firstNameError;
	}

	public void setFirstNameError(String firstNameError) {
		this.firstNameError = firstNameError;
	}

	public String getLastNameError() {
		return lastNameError;
	}

	public void setLastNameError(String lastNameError) {
		this.lastNameError = lastNameError;
	}

	public String getLoginidError() {
		return loginidError;
	}

	public void setLoginidError(String loginidError) {
		this.loginidError = loginidError;
	}

	public String getUserPwdErr() {
		return userPwdErr;
	}

	public void setUserPwdErr(String userPwdErr) {
		this.userPwdErr = userPwdErr;
	}

	public String getUserRePwdErr() {
		return userRePwdErr;
	}

	public void setUserRePwdErr(String userRePwdErr) {
		this.userRePwdErr = userRePwdErr;
	}

	public String getMailError() {
		return mailError;
	}

	public void setMailError(String mailError) {
		this.mailError = mailError;
	}

	public boolean isErrorFound() {
		return errorFound;
	}

	public void setErrorFound(boolean errorFound) {
		this.errorFound = errorFound;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
}