package com.photon.phresco.service.admin.actions;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.commons.model.User;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.util.ServerUtil;

public class Login extends ServiceBaseAction {

	private static final long serialVersionUID = -1858839078372821734L;
	private static final Logger S_LOGGER = Logger.getLogger(Login.class);
	private static Boolean s_debugEnabled = S_LOGGER.isDebugEnabled();
	
	private String username = "";
	private String password = "";
	
	private boolean loginFirst = true;
	
	private boolean errorFound = false;
	
	private String errorMsg;
	
	public String login() {
	    if (s_debugEnabled) {
	        S_LOGGER.debug("Entering Method  Login.login()");
	    }
	    
	    User user = (User) getSessionAttribute(SESSION_USER_INFO);
	    if (user != null) {
	    	return SUCCESS;
	    }
		
		return LOGIN_FAILURE;
	}
	
	public String logout() {
		if (s_debugEnabled) {
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
	
	public String authenticate() {
	    if (s_debugEnabled) {
	        S_LOGGER.debug("Entering Method  Login.validateLogin()");
	    }
		
	    // Username and passwor empty field validation
		if (StringUtils.isEmpty(getUsername()) || StringUtils.isEmpty(getPassword())) {
		    setErrorFound(true);
		    setErrorMsg(getText(KEY_I18N_LOGIN_EMPTY_CRED));
		    return SUCCESS;
		}

		try {
	        String encodedPassword = ServerUtil.encryptString(getPassword());
		    User user = doLogin(getUsername(), encodedPassword);
		    
		    // username and password validation
		    if (StringUtils.isEmpty(user.getDisplayName())) {
		        setErrorFound(true);
		        setErrorMsg(getText(KEY_I18N_ERROR_LOGIN));
		        return SUCCESS;
		    }
		    
		    // phresco enabled usename validation
		    if (!user.isPhrescoEnabled()) {
		        setErrorFound(true);
		        setErrorMsg(getText(KEY_I18N_ERROR_LOGIN_ACCESS_DENIED));
		        return SUCCESS;
		    }
		    
		    // userInfo setting into the session if validated
		    setSessionAttribute(SESSION_USER_INFO, user);
		} catch (PhrescoException e) {
		    return showErrorPopup(e, EXCEPTION_LOGIN);
		}
		return DASHBOARD_LIST;
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

	public boolean isErrorFound() {
        return errorFound;
	}

	public void setErrorFound(boolean errorFound) {
	    this.errorFound = errorFound;
	}

	public String getErrorMsg() {
        return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
	    this.errorMsg = errorMsg;
	}

}