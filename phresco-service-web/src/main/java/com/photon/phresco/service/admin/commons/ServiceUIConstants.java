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
package com.photon.phresco.service.admin.commons;

public interface ServiceUIConstants {

    /*****************************
     * Common Constants
     *****************************/ 
    String SQUARE_CLOSE = "]";
    String COMMA = ",";
    String SQUARE_OPEN = "[";
    String ADD = "add";
    String REQ_FROM_PAGE = "fromPage";
    String REQ_EDIT = "edit";
    String REQ_USER_INFO = "userInfo";
    String SESSION_USER_INFO = "userInfo";
    String SUCCESS_FALSE = "{\"success\": false}";
	String SUCCESS_TRUE = "{\"success\": true}";
	String MAVEN_JAR_FALSE = "{\"mavenJar\": false,\"success\": true}";
	String INVALID_ARCHETYPE_JAR = "{\"isJarTypeValid\": false,\"errorMsg\": \"Not a valid application jar\"}";
	String INVALID_PLUGIN_JAR = "{\"isJarTypeValid\": false,\"errorMsg\": \"Not a valid plugin jar\"}";
    
    
	/*****************************
     * Login Request Constants
     * String REQ_LOGIN_XXX
     *****************************/	
    String REQ_LOGIN_ERROR = "loginError";
    
    
    /*****************************
     * AppType Request Constants
     * String REQ_APP_XXX
     *****************************/
    String REQ_APP_TYPE = "appType";
    String REQ_APP_TYPES = "appTypes";  
    String REQ_APP_TYPEID = "apptypeId";
    
    
    /*****************************
     * Archetype Request Constants
     * String REQ_ARCHE_XXX
     *****************************/
    String REQ_ARCHE_TYPES = "technologies";
    String REQ_ARCHE_TYPE = "technology";
    String REQ_ARCHE_TECHID = "techId";
    String REQ_JAR_TYPE = "type";
    String REQ_PLUGIN_JAR = "pluginJar";
    String REQ_UPLOADED_JAR = "uploadedJar";
    String REQ_JAR_FILE_EXTENSION = ".jar";
    String REQ_ZIP_FILE_EXTENSION  = ".zip";
    String REQ_TAR_GZ_FILE_EXTENSION = ".gz";
    String REQ_IMAGE_JPG_EXTENSION = "jpg";
    String REQ_IMAGE_JPEG_EXTENSION = "jpeg";
    String REQ_IMAGE_PNG_EXTENSION = "png";
    
    
    /*****************************
     * Archetype Constants
     *****************************/
    String FILE_FOR_APPTYPE = "appType";
	String FILE_FOR_PLUGIN = "plugin";
    String X_FILE_NAME = "X-File-Name";

    
    /*****************************
     * Customer Request Constants
     * String REQ_CUST_XXX
     *****************************/ 
    String REQ_CUST_CUSTOMER = "customer";
    String REQ_CUST_CUSTOMERS = "customers";
    String REQ_CUST_CUSTOMER_ID = "customerId";
    
    
    /*****************************
     * ConfigTemp Request Constants
     * String REQ_CONFIG_XXX
     *****************************/ 
    String REQ_CONFIG_TEMP = "configTemp";
    String REQ_CONFIG_TEMPLATES = "configTemplates";
    String REQ_CONFIG_ID = "configId";
    
    /*****************************
     * Download Request Constants
     * String REQ_DOWNLOAD_XXX
     *****************************/ 
    String REQ_DOWNLOAD_INFO = "downloadInfo";
    String REQ_DOWNLOAD_ID = "downloadId";
    
    /*****************************
     * PilotProject Request Constants
     * String REQ_PILOT_XXX
     *****************************/ 
    String REQ_PILOT_PROJECTS = "pilotProjects";
    String REQ_PILOT_PROINFO = "pilotProjectInfo";
    
    
    /*****************************
     * Download Request Constants
     * String REQ_DOWNLOAD_XXX
     *****************************/ 
    String REQ_MODULE_GROUP = "moduleGroup";
    
    /*****************************
     * GlobalUrl Request Constants
     * String REQ_GLOBURL_XXX
     *****************************/ 
    String REQ_GLOBURL_URL = "globalUrl";
    String REQ_GLOBURL_ID = "globalurlId";
    
    
    /*****************************
     * Role Request Constants
     * String REQ_ROLE_XXX
     *****************************/ 
    String REQ_ROLE_ROLE = "role";
    String REQ_ROLE_LIST = "roleList";
    String REQ_ROLE_ID = "roleId";
    
    
	/*****************************
     * I18N Keys Constants
     * String KEY_I18N_XXX_YYY
     *****************************/	
    String KEY_I18N_ERROR_LOGIN = "err.login.invalid.cred";
    String KEY_I18N_ERROR_LOGIN_ACCESS_DENIED = "err.login.access.denied";
    String KEY_I18N_LOGIN_INVALID_CRED = "err.login.invalid.cred";
    String KEY_I18N_LOGIN_EMPTY_CRED = "err.login.empty.cred";
    String KEY_I18N_LOGIN_ACCESS_DENIED	= "err.login.invalid.cred";

    String KEY_I18N_ERR_NAME_EMPTY = "err.msg.name.empty";
    String KEY_I18N_ERR_DESC_EMPTY = "err.msg.desc.empty";
    String KEY_I18N_ERR_VER_EMPTY = "err.msg.ver.empty";
    String KEY_I18N_ERR_FILE_EMPTY	= "err.msg.file.empty";
    
    String KEY_I18N_ERR_APPTYPE_EMPTY = "err.msg.apptye.empty";
    String KEY_I18N_ERR_APPLIES_EMPTY = "err.msg.applies.empty";
    String KEY_I18N_ERR_APPLNJAR_EMPTY = "err.msg.applnjar.empty";
    String KEY_I18N_ERR_PLTPROJ_EMPTY = "err.msg.pltproj.empty";
    String KEY_I18N_ERR_PLUGINJAR_INVALID = "err.msg.plugin.invalid";
    
    String KEY_I18N_ERR_EMAIL_EMPTY = "err.msg.email.empty";
    String KEY_I18N_ERR_ADDRS_EMPTY	= "err.msg.addrs.empty";
    String KEY_I18N_ERR_ZIPCODE_EMPTY = "err.msg.zip.empty";
    String KEY_I18N_ERR_CONTNUM_EMPTY = "err.msg.contnum.empty";
    String KEY_I18N_ERR_FAXNUM_EMPTY = "err.msg.faxnum.empty";
    String KEY_I18N_ERR_COUN_EMPTY = "err.msg.country.empty";
    String KEY_I18N_ERR_LICEN_EMPTY = "err.msg.licence.empty";
    String KEY_I18N_ERR_URL_EMPTY = "err.msg.url.empty";
    String KEY_I18N_ERR_APPLNPLTF_EMPTY = "err.msg.applnpltf.empty";
    String KEY_I18N_ERR_GROUP_EMPTY = "err.msg.group.empty";
    
    String FEATURE_ADDED = "succ.feature.add";
    String FEATURE_NOT_ADDED = "fail.feature.add";
    String FEATURE_DELETED = "succ.feature.delete";
    String FEATURE_NOT_DELETED = "fail.feature.delete" ;
    
    String APPLNTYPES_ADDED = "succ.appType.add";
    String APPLNTYPES_NOT_ADDED = "fail.appType.add";
    String APPLNTYPES_UPDATED = "succ.appType.update";
    String APPLNTYPES_NOT_UPDATED = "fail.appType.update";
    String APPLNTYPES_DELETED = "succ.appType.delete";
    String APPLNTYPES_NOT_DELETED = "fail.appType.delete" ;
    
    String CONFIGTEMPLATE_ADDED = "succ.configtemplate.add";
    String CONFIGTEMPLATE_NOT_ADDED = "fail.configtemplate.add";
    String CONFIGTEMPLATE_DELETED = "succ.configtemplate.delete";
    String CONFIGTEMPLATE_NOT_DELETED = "fail.configtemplate.delete" ;
    
    
    String ARCHETYPE_ADDED = "succ.archetype.add";
    String ARCHETYPE_NOT_ADDED = "fail.archetype.add";
    String ARCHETYPE_UPDATED = "succ.archetype.update";
    String ARCHETYPE_NOT_UPDATED = "fail.archetype.update";
    String ARCHETYPE_DELETED = "succ.archetype.delete";
    String ARCHETYPE_NOT_DELETED = "fail.archetype.delete" ;
    
    String PLTPROJ_ADDED = "succ.pltproj.add";
    String PLTPROJ_NOT_ADDED = "fail.pltproj.add";
    String PLTPROJ_DELETED = "succ.pltproj.delete";
    String PLTPROJ_NOT_DELETED = "fail.pltproj.delete";
    
    
    String CUSTOMER_ADDED = "succ.customer.add";
    String CUSTOMER_UPDATED = "succ.customer.update";
    String CUSTOMER_DELETED = "succ.customer.delete";
    String CUSTOMER_NOT_ADDED = "fail.customer.add";
    String CUSTOMER_NOT_UPDATED = "fail.customer.update";
    String CUSTOMER_NOT_DELETED = "fail.customer.delete";
    
    String ROLE_ADDED = "succ.role.add";
    String ROLE_NOT_ADDED = "fail.role.add";
    String ROLE_DELETED = "succ.role.delete";
    String ROLE_NOT_DELETED = "fail.role.delete" ;
    
    String URL_ADDED = "succ.url.add";
    String URL_NOT_ADDED = "fail.url.add";
    String URL_DELETED = "succ.globurl.delete";
    String URL_NOT_DELETED = "fail.globurl.delete";
    
    
    String DOWNLOAD_ADDED = "succ.download.add";
    String DOWNLOAD_NOT_ADDED = "fail.download.add";
    String DOWNLOAD_DELETED = "succ.download.delete";
    String DOWNLOAD_NOT_DELETED = "fail.download.delete";
}