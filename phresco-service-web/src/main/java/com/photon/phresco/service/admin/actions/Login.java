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
package com.photon.phresco.service.admin.actions;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.photon.phresco.commons.model.Customer;
import com.photon.phresco.commons.model.Role;
import com.photon.phresco.commons.model.User;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.exception.PhrescoWebServiceException;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.util.Utility;

public class Login extends ServiceBaseAction {

	private static final long serialVersionUID = -1858839078372821734L;
	private static final Logger S_LOGGER = Logger.getLogger(Login.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	private static Boolean debugEnabled  = S_LOGGER.isDebugEnabled();
	
	private String username = null;
	private String password = null;
	private boolean loginFirst = true;
	private String customerId = "";
	
	private String currentVersion = "";
	
	private String logoImgUrl = "";
	private String copyRightColor = "";
	private String copyRight = "";
	
	private static Map<String, String> s_encodeImgMap = new HashMap<String, String>();
	private static Map<String, Map<String, String>> s_themeMap = new HashMap<String, Map<String, String>>();
	
	public String login() throws PhrescoException, IOException, ParseException {
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
		removeSessionAttribute(SESSION_PERMISSION_IDS);
		String errorTxt = (String) getSessionAttribute(REQ_LOGIN_ERROR);
		if (StringUtils.isNotEmpty(errorTxt)) {
			setReqAttribute(REQ_LOGIN_ERROR, getText(errorTxt));
		} else {
			setReqAttribute(REQ_LOGIN_ERROR, getText(KEY_I18N_SUCCESS_LOGOUT));
		}
		removeSessionAttribute(REQ_LOGIN_ERROR);
		s_encodeImgMap.clear();
		s_themeMap.clear();
		
        return SUCCESS;
    }
	
	public String fetchLogoImgUrl() {
    	InputStream fileInputStream = null;
    	try {
    		String encodeImg = s_encodeImgMap.get(getCustomerId());
    		if (StringUtils.isEmpty(encodeImg)) {
    			fileInputStream = getServiceManager().getIcon(getCustomerId());
    			if(fileInputStream != null) {
    				byte[] imgByte = null;
        			imgByte = IOUtils.toByteArray(fileInputStream);
        			byte[] encodedImage = Base64.encodeBase64(imgByte);
        			encodeImg = new String(encodedImage);
        			s_encodeImgMap.put(getCustomerId(), encodeImg);
    			}
    		}
            setLogoImgUrl(encodeImg);
            
            Map<String, String> themeMap = s_themeMap.get(getCustomerId());
            if (MapUtils.isEmpty(s_themeMap.get(getCustomerId()))) {
            	User user = (User) getSessionAttribute(SESSION_USER_INFO);
            	List<Customer> customers = user.getCustomers();
            	for (Customer customer : customers) {
            		if (customer.getId().equals(getCustomerId())) {
            			themeMap = customer.getFrameworkTheme();
            			s_themeMap.put(getCustomerId(), themeMap);
            			break;
            		}
            	}
            }
            if (MapUtils.isNotEmpty(themeMap)) {
            	setCopyRightColor(themeMap.get(COPYRIGHT_COLOR));
            	setCopyRight(themeMap.get(COPYRIGHT));
            }
    	} catch (PhrescoException e) {
    		
    	} catch (IOException e) {
    		
		} finally {
    		try {
    			if (fileInputStream != null) {
    				fileInputStream.close();
    			}
			} catch (IOException e) {
				
			}
    	}
    	
    	return SUCCESS;
    }
	
	public String about() {
		if (debugEnabled) {
			S_LOGGER.debug("Entering Method VersionUpdate.about()");
		}

		return ABOUT;
	}

	public String versionInfo() throws PhrescoException {
		if (debugEnabled) {
			S_LOGGER.debug("Entering Method VersionUpdate.versionInfo()");
		}
		try {
			String latestFrameWorkVersion = PhrescoServerFactory.getDbManager().getLatestFrameWorkVersion();
			setCurrentVersion(latestFrameWorkVersion);
		} catch (PhrescoException e) {
			throw new PhrescoException(e);
		}

		return SUCCESS;
	}
	
	private String authenticate() throws IOException, ParseException  {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method  Login.authenticate()");
	    }
		
		User user = null;
		try {
			user = doLogin(getUsername(), getPassword());
			List<Customer> customers = user.getCustomers();
			if (CollectionUtils.isNotEmpty(customers)) {
				Collections.sort(customers, sortingCusNameInAlphaOrder());
			}
        	setSessionAttribute(REQ_CUST_CUSTOMERS, customers);
        	
        	if (user == null) {
				setReqAttribute(REQ_LOGIN_ERROR, getText(KEY_I18N_ERROR_LOGIN));
				return LOGIN_FAILURE;
		 	}
			 
			if (!user.isPhrescoEnabled()) {
				setReqAttribute(REQ_LOGIN_ERROR, getText(KEY_I18N_ERROR_LOGIN_ACCESS_DENIED));
				return LOGIN_FAILURE;
			}
			setSessionAttribute(SESSION_USER_INFO, user);
			
			File tempPath = new File(Utility.getSystemTemp() + File.separator + USER_JSON);
        	String userId = user.getId();
        	JSONObject userjson = new JSONObject();
        	JSONParser parser = new JSONParser();
        	String customerId = PHOTON;
        	if (tempPath.exists()) {
        		FileReader reader = new FileReader(tempPath);
        		userjson = (JSONObject)parser.parse(reader);
        		if (userjson.get(userId) != null) {
        			customerId = (String) userjson.get(userId);
        		}
        		reader.close();
        	} 
        	
        	userjson.put(userId, customerId);
        	
        	List<String> customerList = new ArrayList<String>();
        	for (Customer c : customers) {
				customerList.add(c.getId());
			}
        	//If photon is present in customer list , then ui should load with photon customer
        	if ((StringUtils.isEmpty(customerId) || PHOTON.equals(customerId)) && customerList.contains(PHOTON)) {
        		customerId = PHOTON;
        	}
			setSessionAttribute(REQ_CUST_CUSTOMER_ID, customerId);
			String enCodedLogo = getEncodedLogo(customerId);
			setReqAttribute("enCodedLogo", enCodedLogo);

			FileWriter  writer = new FileWriter(tempPath);
        	writer.write(userjson.toString());
        	writer.close();
			
			List<String> roleIds = user.getRoleIds();
			if (CollectionUtils.isNotEmpty(roleIds)) {
				List<String> permisionIds = new ArrayList<String>();
				for (String roleId : roleIds) {
					Role role = getServiceManager().getRole(roleId);
					permisionIds.addAll(role.getPermissionIds());
				}
				setSessionAttribute(SESSION_PERMISSION_IDS, permisionIds);
			}
		} catch (PhrescoWebServiceException e) {
			if (e.getResponse().getStatus() == 204) {
				setReqAttribute(REQ_LOGIN_ERROR, getText(KEY_I18N_ERROR_LOGIN));
				return LOGIN_FAILURE;
			} else {
				setReqAttribute(REQ_LOGIN_ERROR, getText(KEY_I18N_SERVER_DOWN));
				return LOGIN_FAILURE;
			}
		} catch (PhrescoException e) {
			// TODO Auto-generated catch block
		} 
			
		return SUCCESS;
	}
	
	private String getEncodedLogo(String customerId) throws IOException {
		InputStream fileInputStream = null;
		String encodeImg = s_encodeImgMap.get(customerId);
    	try {
    		if (StringUtils.isEmpty(encodeImg)) {
    			fileInputStream = getServiceManager().getIcon(customerId);
    			if(fileInputStream != null) {
    				byte[] imgByte = null;
        			imgByte = IOUtils.toByteArray(fileInputStream);
        			byte[] encodedImage = Base64.encodeBase64(imgByte);
        			encodeImg = new String(encodedImage);
        			s_encodeImgMap.put(customerId, encodeImg);
    			}
    		}
    	} catch (Exception e) {
			// TODO: handle exception
		} finally {
    		try {
    			if (fileInputStream != null) {
    				fileInputStream.close();
    			}
			} catch (IOException e) {
				
			}
    	}
    	
    	return encodeImg;
	}
	
	 @SuppressWarnings("unchecked")
		public String fetchCustomerId() {
	    	try {
	    		User user = (User) getSessionAttribute(SESSION_USER_INFO);
	    		String userId = user.getId();
	    		String customerId = (String) getSessionAttribute("customerId");
	    		if (!customerId.equals(getCustomerId())) {
	    			File tempPath = new File(Utility.getSystemTemp() + File.separator + USER_JSON);
	    			JSONObject userjson = null;
	    			JSONParser parser = new JSONParser();
	    			customerId = getCustomerId();
	    			if (tempPath.exists()) {
	    				FileReader reader = new FileReader(tempPath);
	    				userjson = (JSONObject)parser.parse(reader);
	    				reader.close();
	    			} else {
	    				userjson = new JSONObject();
	    			}

	    			userjson.put(userId, customerId);
	    			FileWriter  writer = new FileWriter(tempPath);
	    			writer.write(userjson.toString());
	    			writer.close();
	    		}
	    		setSessionAttribute(SESSION_USER_INFO, user);
	    		setSessionAttribute("customerId", customerId);
			} catch (IOException e) {
				return showErrorPopup(new PhrescoException(e), getText(EXCEPTION_FRAMEWORKSTREAM));
			} catch (ParseException e) {
				return showErrorPopup(new PhrescoException(e), getText(EXCEPTION_FRAMEWORKSTREAM));
			}
	    	return SUCCESS;
	    }
	
	private Comparator sortingCusNameInAlphaOrder() {
		return new Comparator(){
		    public int compare(Object firstObject, Object secondObject) {
		    	Customer customerName1 = (Customer) firstObject;
		    	Customer customerName2 = (Customer) secondObject;
		       return customerName1.getName().compareToIgnoreCase(customerName2.getName());
		    }
		};
	}

	private boolean validateLogin() {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method  Login.validateLogin()");
	    }
		
		if (StringUtils.isEmpty(getUsername())) {
            setReqAttribute(REQ_LOGIN_ERROR, getText(KEY_I18N_LOGIN_USER_NAME_EMPTY));
            return false;
        }
        if (StringUtils.isEmpty(getPassword())) {
            setReqAttribute(REQ_LOGIN_ERROR, getText(KEY_I18N_LOGIN_PASSWORD_EMPTY));
            return false;
        }
		
		setReqAttribute(REQ_USER_NAME, getUsername());
        setReqAttribute(REQ_PASSWORD, getPassword());
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
	
	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}
	
	public String getLogoImgUrl() {
		return logoImgUrl;
	}

	public void setLogoImgUrl(String logoImgUrl) {
		this.logoImgUrl = logoImgUrl;
	}
	
	public String getCustomerId() {
	    return customerId;
	}

	public void setCustomerId(String customerId) {
	    this.customerId = customerId;
	}

	public void setCopyRightColor(String copyRightColor) {
		this.copyRightColor = copyRightColor;
	}

	public String getCopyRightColor() {
		return copyRightColor;
	}

	public void setCopyRight(String copyRight) {
		this.copyRight = copyRight;
	}

	public String getCopyRight() {
		return copyRight;
	}
}