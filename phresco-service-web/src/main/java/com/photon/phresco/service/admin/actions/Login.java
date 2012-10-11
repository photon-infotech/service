package com.photon.phresco.service.admin.actions;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.commons.model.User;

public class Login extends ServiceBaseAction {

	private static final long serialVersionUID = -1858839078372821734L;
	private static final Logger S_LOGGER = Logger.getLogger(Login.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	private static Boolean debugEnabled  = S_LOGGER.isDebugEnabled();
	
	private String username = null;
	private String password = null;
	private boolean loginFirst = true;
	
	public String login() {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method  Login.login()");
	    }
	    
	    User user = (User) getSessionAttribute(SESSION_USER_INFO);
	    if (user != null) {
	    	return SUCCESS;
	    }
	    if (loginFirst) {
	    	setReqAttribute(REQ_LOGIN_ERROR, "");
        	return LOGIN_FAILURE;	
		}
		if (validateLogin()) {
			return authenticate();
		}
		
		return LOGIN_FAILURE;
	}
	
	public String logout() {
		if (debugEnabled) {
			S_LOGGER.debug("Entering Method  Login.logout()");
		}
		
		removeSessionAttribute(SESSION_USER_INFO);
		String errorTxt = (String) getSessionAttribute(REQ_LOGIN_ERROR);
		if (StringUtils.isNotEmpty(errorTxt)) {
			setReqAttribute(REQ_LOGIN_ERROR, getText(errorTxt));
		} else {
			setReqAttribute(REQ_LOGIN_ERROR, getText(KEY_I18N_SUCCESS_LOGOUT));
		}
		removeSessionAttribute(REQ_LOGIN_ERROR);
		
        return SUCCESS;
    }
	
	private String authenticate() {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method  Login.authenticate()");
	    }
		
		User user = null;
		try {
			byte[] encodeBase64 = Base64.encodeBase64(password.getBytes());
			String encodedPassword = new String(encodeBase64);
			
			user = doLogin(username, encodedPassword);
			if (StringUtils.isEmpty(user.getDisplayName())) {
				setReqAttribute(REQ_LOGIN_ERROR, getText(KEY_I18N_ERROR_LOGIN));
				
				return LOGIN_FAILURE;
			}
			if (!user.isPhrescoEnabled()) {
				setReqAttribute(REQ_LOGIN_ERROR, getText(KEY_I18N_ERROR_LOGIN_ACCESS_DENIED));
				
				return LOGIN_FAILURE;
			}
			setSessionAttribute(SESSION_USER_INFO, user);
		} catch (Exception e) {
			return LOGIN_FAILURE;
		}
			
		return SUCCESS;
	}

	private boolean validateLogin() {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method  Login.validateLogin()");
	    }
		
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			setReqAttribute(REQ_LOGIN_ERROR, getText(KEY_I18N_LOGIN_EMPTY_CRED));
			return false;
		}
		
		return true;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isLoginFirst() {
		return loginFirst;
	}

	public void setLoginFirst(boolean loginFirst) {
		this.loginFirst = loginFirst;
	}
}