/**
 * Service Web Archive
 *
 * Copyright (C) 1999-2014 Photon Infotech Inc.
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
package com.photon.phresco.service.admin.commons;

public interface ServiceActions {

	/*****************************
     * Login Action path
     * String LOGIN_XXX
     *****************************/
	String LOGIN_RESULT		= "login";
    String LOGIN_SUCCESS 	= "success";
    String LOGIN_FAILURE 	= "failure";
	
	/*****************************
     * Dashboard Action path
     * String DASHBOARD_XXX
     *****************************/
    String DASHBOARD_LIST = "dashboard";
	
	/*****************************
     * Components Action path
     * String COMP_XXX
     *****************************/
    String COMP_MENU 			    	= "compMenu";
    
	String COMP_FEATURES_LIST 	    	= "featuresList";
	String COMP_FEATURES_ADD        	= "featuresAdd";
	String COMP_FEATURES_CANCEL     	= "featuresCancel";
	String COMP_FEATURES_DEPENDENCY     = "dependency";
	
	String COMP_COMPONENT_LIST 	    	= "componentList";
	String COMP_COMPONENT_ADD        	= "componentAdd";
	String COMP_COMPONENT_CANCEL     	= "componentCancel";
		
	String COMP_ARCHETYPE_LIST      	= "archetypesList";
	String COMP_ARCHETYPE_ADD       	= "archetypeAdd";
	String COMP_ARCHETYPE_SAVE      	= "archetypeSave";
	String COMP_ARCHETYPE_CANCEL    	= "archetypeCancel";
	
	String COMP_APPTYPE_LIST        	= "applntypesList";
	String COMP_APPTYPE_ADD				= "applicationAdd";
	String COMP_APPTYPE_SAVE			= "applicationSave";	
	String COMP_APPTYPE_EDIT            = "applicationEdit";
	String COMP_APPTYPE_UPDATE          = "applicationUpdate";
	String COMP_APPTYPE_CANCEL			= "applicationCancel";
	
	String COMP_TECH_OPTIONS_LIST = "techOptions";
	
	String COMP_CONFIGTEMPLATE_LIST 	= "configtempList";
	String COMP_CONFIGTEMPLATE_ADD 		= "configtempAdd";
	String COMP_CONFIGTEMPLATE_SAVE 	= "configtempSave";
	String COMP_CONFIGTEMPLATE_CANCEL 	= "configtempCancel";
	
	String COMP_PILOTPROJ_LIST		    = "pilotprojList";
	String COMP_PILOTPROJ_ADD      		= "pilotprojAdd";
	String COMP_PILOTPROJ_SAVE      	= "pilotprojSave";
	String COMP_PILOTPROJ_CANCEL      	= "pilotprojCancel";
	
	String COMP_DOWNLOAD_LIST  			= "downloadList";
	String COMP_DOWNLOAD_ADD  			= "downloadAdd";
	String COMP_DOWNLOAD_SAVE  			= "downloadSave";
	String COMP_DOWNLOAD_CANCEL  		= "downloadCancel";
	/*****************************
     * Components Action path
     * String ADMIN_XXX
     *****************************/	
	String ADMIN_MENU       	 	= "adminMenu";
	
	String ADMIN_CUSTOMER_LIST   	= "customerList";
	String ADMIN_CUSTOMER_ADD    	= "customerAdd";
	String ADMIN_CUSTOMER_SAVE   	= "customerSave";
	String ADMIN_CUSTOMER_CANCEL 	= "customerCancel";
	
	String ADMIN_USER_LIST  	 	= "userList";
	String ADMIN_USER		  	 	= "user";
	String ADMIN_USER_ADD           = "userAdd"; 
	
	String ADMIN_ROLE_LIST  	 	= "roleList";
	String ADMIN_ROLE_ADD	  	 	= "roleAdd";
	String ADMIN_ROLE_SAVE			= "roleSave";
	String ADMIN_ROLE_CANCEL		= "roleCancel";
	String ADMIN_ROLE_ASSIGN 		= "assignPermission";
	String ADMIN_ROLE_ASSIGN_SAVE	= "permissionSave";
	String ADMIN_ROLE_ASSIGN_CANCEL = "permissionCancel";
	
	String ADMIN_PERMISSION_LIST 	= "permissionList";
	
	String ADMIN_LDAP_LIST = "ldapList";
	
	String ADMIN_VIDEO_LIST = "videoList";
	String ADMIN_VIDEO_ADD = "videoAdd";
	
	String ADMIN_GLOBALURL_LIST 	= "globalurlList";
	String ADMIN_GLOBALURL_ADD  	= "globalurlAdd";
	String ADMIN_GLOBALURL_SAVE  	= "globalurlSave";
	String ADMIN_GLOBALURL_CANCEL  	= "globalurlCancel";
	
	
	
}