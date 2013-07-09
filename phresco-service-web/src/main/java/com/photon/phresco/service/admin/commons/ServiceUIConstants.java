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
package com.photon.phresco.service.admin.commons;

public interface ServiceUIConstants {

    /*****************************
     * Common Constants
     *****************************/ 
    String SQUARE_CLOSE = "]";
    String COMMA = ",";
    String DOT = ".";
    String LBL_HDR = "lbl.hdr.";
    String LBL_BTN = "lbl.btn";
    String LBL_URL = "lbl.pageurl.";
    String LBL_PROG_TXT = "lbl.prog.txt.";
    String SQUARE_OPEN = "[";
    String ADD = "add";
    String REQ_FROM_PAGE = "fromPage";
    String REQ_APPLIES_TO = "appliesTo";
    String EDIT = "edit";
    String REQ_USER_INFO = "userInfo";
    String SESSION_USER_INFO = "userInfo";
    String SESSION_PERMISSION_IDS = "sessionPermissionIds";
    String SUCCESS_FALSE = "{\"success\": false}";
	String SUCCESS_TRUE = "{\"success\": true}";
	String MAVEN_JAR_FALSE = "{\"mavenJar\": false,\"success\": true}";
	String INVALID_ARCHETYPE_JAR = "{\"isJarTypeValid\": false,\"errorMsg\": \"Not a valid archetype jar\"}";
	String INVALID_PLUGIN_JAR = "{\"isJarTypeValid\": false,\"errorMsg\": \"Not a valid plugin jar\"}";
	String INVALID_MODULE_NAME = "{\"isNameValid\": false,\"errorMsg\": \"ZIP Folder Name and Feature Name Does Not Match\"}";
	String CSV_PATTERN = "\\s*,\\s*";
	String REQ_ARTIFACT_ID = "_artifactId";
	String REQ_GROUP_ID = "_groupId";
	String REQ_VERSION = "_version";
	String REQ_PLUGIN_INFO = "pluginInfo";
	String REQ_VERSIONING = "versioning";
    
	String VIDEOS = "video";
	String ARCHETYPES = "archetype";
	String CONFIG_TEMPLATES = "configtemp";
	String PILOT_PROJECTS = "pilot";
	String DOWNLOADS = "download";
	String FEATURES = "features";
	String CUSTOMERS = "customer";
	String GLOBALURLS = "globalurl";
	String JSLIBS = "jslibs";
	String COMPONENT = "component";
	String FILE_FORMAT = "zip";
	String TEMP_FOLDER = "newtemp";
	String TECHGROUP_LIST = "techGrpList";
	String ABOUT = "about";
	String USER_JSON = "user.json";
	String PHOTON = "photon";
    
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
     * Login Request Constants
     *****************************/
    String REQ_USER_NAME = "username";
	String REQ_PASSWORD = "password";
    
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
    String REQ_JAR_FILE = "jar";
    String REQ_TECHNOLOGY_OPTION = "options";
    String REQ_TECHNOLOGY_REPORTS = "reports";
    String REQ_TECHNOLOGY_GROUPS = "TechnologyGroups";
    String REQ_TECH_GROUP = "techGroup";
    String REQ_ZIP_FILE = "zip";
    String REQ_FUNCTIONAL_FRAMEWORKS = "functionalFrameworks";
    String REQ_SELECTED_FUNCTIONAL_FRAMEWORKS = "selectedFunctionalFrameworks";
    
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
     * Users Request Constants
     * String REQ_USER_XXX
     *****************************/ 
    String REQ_USER_LIST = "userList";
    String REQ_USER = "user";
    String REQ_USER_SYNCLIST = "userSyncList";
    
    
    /*****************************
     * ConfigTemp Request Constants
     * String REQ_CONFIG_XXX
     *****************************/ 
    String REQ_CONFIG_TEMP = "configTemp";
    String REQ_CONFIG_TEMPLATES = "configTemplates";
    String REQ_CONFIG_ID = "configId";
    
    String REQ_CONFIG_KEY = "propTempKey";
    String REQ_CONFIG_NAME = "_propTempName";
	String REQ_CONFIG_PSBL_VAL = "_psblVal";
	String REQ_CONFIG_MULTIPLE = "_propMul";
	String REQ_CONFIG_MANDATORY = "_propMand";
	String REQ_CONFIG_HELP_TEXT = "_helpText";
	String REQ_CONFIG_TYPE = "_type";
    
    /*****************************
     * Download Request Constants
     * String REQ_DOWNLOAD_XXX
     *****************************/ 
    String REQ_DOWNLOAD_INFO = "downloadInfo";
    String REQ_DOWNLOAD_ID = "downloadId";
    String REQ_DOWNLOAD_UPLOAD_FILE = "uploadFile";
    String REQ_DOWNLOAD_PLATFORMS = "platforms";
    
    
    /*****************************
     * PilotProject Request Constants
     * String REQ_PILOT_XXX
     *****************************/ 
    String REQ_PILOT_PROJECTS = "pilotProjects";
    String REQ_PILOT_PROINFO = "pilotProjectInfo";
    String REQ_PILOT_PROJ_ID = "projectId";
    
    
    /*****************************
     * Features Request Constants
     * String REQ_FEATURES_XXX
     *****************************/ 
    String REQ_FEATURES_MOD_GRP = "moduleGroup";
    String REQ_FEATURES_TYPE = "type";
    String REQ_FEATURES_TYPE_JS = "JAVASCRIPT";
    String REQ_FEATURES_TYPE_MODULE = "FEATURE";
    String REQ_FEATURES_TYPE_COMPONENT = "COMPONENT";
    String REQ_FEATURES_SELECTED_MODULEID = "selectedModuleId";
    String REQ_FEATURES_LICENSE = "licenses";
    String REQ_FEATURES_UPLOADTYPE = "featureJar";
    String REQ_FEATURE_JS = "js";
    /*****************************
     * Features Common Constants
     * String FEATURES_XXX
     *****************************/
    String FEATURES_CORE = "core";
    String FEATURES_SELECTED_TECHNOLOGY = "selectedTechnology";
    
    
    /*****************************
     * Features Session Constants
     * String SESSION_COMP_XXX
     *****************************/
    String SESSION_FEATURES_DEPENDENT_MOD_IDS = "dependentModuleIds";
    
    
    /*****************************
     * Component Session Constants
     * String SESSION_COMP_XXX
     *****************************/
    String SESSION_COMP_DEPENDENT_MOD_IDS = "compDependentModuleIds";
    
    /*****************************
     * Permissions Request Constants
     * String REQ_PERMISSIONS_XXX
     *****************************/ 
    String REQ_PERMISSIONS_LIST = "permissions";
    String REQ_PERMISSIONS_ID = "permissionId";
    String REQ_SELECTED_PERMISSION_IDS = "permissionIds";
    
    
    /*****************************
     * Role Request Constants
     * String REQ_VIDEO_XXX
     *****************************/ 
    String REQ_VIDEO_FILE_TYPE = "type";
    String REQ_VIDEO_UPLOAD = "videoFile";
    String REQ_VIDEO_INFO = "videoInfo";
    
    String REQ_VIDEO_SAVE = "videoSave";
    String REQ_VIDEO_UPDATE = "videoUpdate";
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
    String REQ_ROLE_NAME = "roleName";
    String REQ_ROLE_ROLE = "role";
    String REQ_ROLE_LIST = "roleList";
    String REQ_ROLE_ID = "roleId";
    String REQ_ROLE_EDITABLE = "editable";
    String REQ_ROLES_MAP = "availableRolesMap";
    
    
    /*****************************
     * Error Report
     * I18N Keys Constants
     * String REQ_LOG_REPORT
     *****************************/ 
    String REQ_LOG_REPORT = "logReport";
    String LOG_ERROR = "logError";
    
    String EXCEPTION_LOGIN = "excep.hdr.admin.cust.login";
    String EXCEPTION_CUSTOMERS_DELETE = "excep.hdr.admin.cust.delete";
	String EXCEPTION_CUSTOMERS_UPDATE = "excep.hdr.admin.cust.update";
	String EXCEPTION_CUSTOMERS_SAVE = "excep.hdr.admin.cust.save";
	String EXCEPTION_CUSTOMERS_ADD = "excep.hdr.admin.cust.add";
	String EXCEPTION_CUSTOMERS_LIST = "excep.hdr.admin.cust.list";
	String EXCEPTION_CUSTOMERS_VALIDATE = "excep.hdr.admin.cust.validate";
	
	String EXCEPTION_USERS_LIST = "excep.hdr.admin.user.list";
    
	String EXCEPTION_ROLE_DELETE = "excep.hdr.admin.role.delete";
	String EXCEPTION_ROLE_VALIDATE = "excep.hdr.admin.role.validate";
	String EXCEPTION_ROLE_UPDATE = "excep.hdr.admin.role.update";
	String EXCEPTION_ROLE_SAVE = "excep.hdr.admin.role.save";
	String EXCEPTION_ROLE_EDIT = "excep.hdr.admin.role.edit";
	String EXCEPTION_ROLE_LIST = "excep.hdr.admin.role.list";
	String EXCEPTION_ROLE_ASSIGN_PERMISSION_POPUP = "excep.hdr.admin.role.assign.permission.popup";
	String EXCEPTION_ROLE_ASSIGN_PERMISSION = "excep.hdr.admin.role.assign.permission";
	String EXCEPTION_ASSIGN_ROLE_TO_USER= "excep.hdr.admin.assign.role.to.user";
	
	String EXCEPTION_PERMISSION_LIST = "excep.hdr.admin.asgnprm.list";
	String EXCEPTION_PERMISSION_DELETE = "excep.hdr.admin.asgnprm.delete";
	
	String EXCEPTION_VIDEO_DELETE = "excep.hdr.admin.video.delete";
	String EXCEPTION_VIDEO_UPDATE = "excep.hdr.admin.video.update";
	String EXCEPTION_VIDEO_SAVE = "excep.hdr.admin.video.save";
	String EXCEPTION_VIDEO_EDIT = "excep.hdr.admin.video.edit";
	String EXCEPTION_VIDEO_LIST = "excep.hdr.admin.video.list";
	
	String EXCEPTION_GLOBAL_URL_DELETE = "excep.hdr.admin.glblurl.delete";
	String EXCEPTION_GLOBAL_URL_UPDATE = "excep.hdr.admin.glblurl.update";
	String EXCEPTION_GLOBAL_URL_SAVE = "excep.hdr.admin.glblurl.save";
	String EXCEPTION_GLOBAL_URL_EDIT = "excep.hdr.admin.glblurl.edit";
	String EXCEPTION_GLOBAL_URL_LIST = "excep.hdr.admin.glblurl.list";
	
	String EXCEPTION_FEATURE_DELETE = "excep.hdr.comp.feature.delete";
	String EXCEPTION_FEATURE_UPDATE = "excep.hdr.comp.feature.update";
	String EXCEPTION_FEATURE_SAVE = "excep.hdr.comp.feature.save";
	String EXCEPTION_FEATURE_EDIT = "excep.hdr.comp.feature.edit";
	String EXCEPTION_FEATURE_LIST = "excep.hdr.comp.feature.list";
	String EXCEPTION_FEATURE_ADD = "excep.hdr.comp.feature.add";
	String EXCEPTION_FEATURE_VALIDATE = "Feature validate";
	
	String EXCEPTION_COMPONENT_DELETE = "excep.hdr.comp.compon.delete";
	String EXCEPTION_COMPONENT_UPDATE = "excep.hdr.comp.compon.update";
	String EXCEPTION_COMPONENT_SAVE = "excep.hdr.comp.compon.save";
	String EXCEPTION_COMPONENT_ADD = "excep.hdr.comp.compon.add";
	String EXCEPTION_COMPONENT_EDIT = "excep.hdr.comp.compon.edit";
	String EXCEPTION_COMPONENT_LIST = "excep.hdr.comp.compon.list";
	String EXCEPTION_COMPONENT_LIST_DEPENDENCY = "excep.hdr.comp.compon.dependency";
	String EXCEPTION_COMPONENT_UPLOAD_FILE = "excep.hdr.comp.compon.uploadfile";
	String EXCEPTION_FRAMEWORKSTREAM = "changecustomer.exception.message.stream";
	
	String EXCEPTION_ARCHETYPE_DELETE = "excep.hdr.comp.archetype.delete";
	String EXCEPTION_ARCHETYPE_UPDATE = "excep.hdr.comp.archetype.update";
	String EXCEPTION_ARCHETYPE_SAVE = "excep.hdr.comp.archetype.save";
	String EXCEPTION_ARCHETYPE_EDIT = "excep.hdr.comp.archetype.edit";
	String EXCEPTION_ARCHETYPE_ADD = "excep.hdr.comp.archetype.add";
	String EXCEPTION_ARCHETYPE_LIST = "excep.hdr.comp.archetype.list";
	
	String EXCEPTION_APPTYPES_LIST = "excep.hdr.comp.appln.list";
	String EXCEPTION_APPTYPES_EDIT = "excep.hdr.comp.appln.edit";
	String EXCEPTION_APPTYPES_SAVE = "excep.hdr.comp.appln.save";
	String EXCEPTION_APPTYPES_UPDATE = "excep.hdr.comp.appln.update";
	String EXCEPTION_APPTYPES_DELETE = "excep.hdr.comp.appln.delete";
	
	String EXCEPTION_CONFIG_TEMP_DELETE = "excep.hdr.comp.configtemp.delete";
	String EXCEPTION_CONFIG_TEMP_UPDATE = "excep.hdr.comp.configtemp.update";
	String EXCEPTION_CONFIG_TEMP_SAVE = "excep.hdr.comp.configtemp.save";
	String EXCEPTION_CONFIG_TEMP_EDIT = "excep.hdr.comp.configtemp.edit";
	String EXCEPTION_CONFIG_TEMP_ADD = "excep.hdr.comp.configtemp.add";
	String EXCEPTION_CONFIG_TEMP_LIST = "excep.hdr.comp.configtemp.list";
	
	String EXCEPTION_PILOT_PROJECTS_DELETE = "excep.hdr.comp.pilotpro.delete";
	String EXCEPTION_PILOT_PROJECTS_UPDATE = "excep.hdr.comp.pilotpro.update";
	String EXCEPTION_PILOT_PROJECTS_SAVE = "excep.hdr.comp.pilotpro.save";
	String EXCEPTION_PILOT_PROJECTS_EDIT = "excep.hdr.comp.pilotpro.edit";
	String EXCEPTION_PILOT_PROJECTS_ADD = "excep.hdr.comp.pilotpro.add";
	String EXCEPTION_PILOT_PROJECTS_LIST = "excep.hdr.comp.pilotpro.list";
	
	String EXCEPTION_DOWNLOADS_DELETE = "excep.hdr.comp.download.delete";
	String EXCEPTION_DOWNLOADS_UPDATE = "excep.hdr.comp.download.update";
	String EXCEPTION_DOWNLOADS_SAVE = "excep.hdr.comp.download.save";
	String EXCEPTION_DOWNLOADS_EDIT = "excep.hdr.comp.download.edit";
	String EXCEPTION_DOWNLOADS_ADD = "excep.hdr.comp.download.add";
	String EXCEPTION_DOWNLOADS_LIST = "excep.hdr.comp.download.list";
	String EXCEPTION_ARTIFACTINFO_MISSING = "excep.hdr.comp.comp.artifactInfos.missing";
	
	/*****************************
     * I18N Keys Constants
     * String KEY_I18N_XXX_YYY
     *****************************/	
    String KEY_I18N_ERROR_LOGIN = "err.login.invalid.cred";
    String KEY_I18N_ERROR_LOGIN_ACCESS_DENIED = "err.login.access.denied";
    String KEY_I18N_LOGIN_INVALID_CRED = "err.login.invalid.cred";
    String KEY_I18N_LOGIN_USER_NAME_EMPTY = "err.login.user.name.empty";
    String KEY_I18N_LOGIN_PASSWORD_EMPTY = "err.login.password.empty";
    String KEY_I18N_LOGIN_ACCESS_DENIED	= "err.login.invalid.cred";
    String KEY_I18N_SESSION_EXPIRED = "err.login.session.expired";
    String KEY_I18N_SUCCESS_LOGOUT = "succ.logout";
    String KEY_I18N_SERVER_DOWN = "err.login.server.down";
    
    String KEY_I18N_ERR_NAME_EMPTY = "err.msg.name.empty";
    String KEY_I18N_ERR_URL_EMPTY = "err.msg.url.empty";
    String KEY_I18N_ERR_URL_NOT_VALID = "err.msg.url.notvalid";
    String KEY_I18N_ERR_GROUPID_EMPTY = "err.msg.groupid.empty";
    String KEY_I18N_ERR_ARTIFACTID_EMPTY = "err.msg.artifactid.empty";
    String KEY_I18N_ERR_NAME_ALREADY_EXIST = "err.msg.name.exists";
    String KEY_I18N_ERR_DISPLAYNAME_ALREADY_EXIST = "err.msg.displayname.exists";
    String KEY_I18N_ERR_NAME_ALREADY_EXIST_TECH = "err.msg.name.tech.exists";
    String KEY_I18N_ERR_NAME_ALREADY_EXIST_APPTYPE = "err.msg.name.apptype.exists";
    String KEY_18N_ERR_CONTEXT_ALREADY_EXIST = "err.msg.context.exists";
    String KEY_I18N_ERR_DESC_EMPTY = "err.msg.desc.empty";
    String KEY_I18N_ERR_VER_EMPTY = "err.msg.ver.empty";
    String KEY_I18N_ERR_TECHVER_EMPTY = "err.msg.techver.empty";
    String KEY_I18N_ERR_GID_EMPTY = "err.msg.gId.empty";
    String KEY_I18N_ERR_ARFID_EMPTY = "err.msg.aId.empty";
    String KEY_I18N_ERR_VER_ALREADY_EXISTS = "err.msg.ver.exists";
    String KEY_I18N_ERR_FILE_EMPTY	= "err.msg.file.empty";
    String KEY_I18N_MULTI_TECH_EMPTY = "err.msg.multitech.empty";
    String KEY_I18N_NAME_CHANGE_NOT_APPLICABLE = "err.msg.name.chnge.not.appl";
	String KEY_I18N_SINGLE_TECH_EMPTY = "err.msg.singletech.empty";
	String KEY_I18N_ERR_CONTEXT_EMPTY = "err.msg.context.empty";
    
    String KEY_I18N_ERR_APPTYPE_EMPTY = "err.msg.apptye.empty";
    String KEY_I18N_ERR_APPLIES_EMPTY = "err.msg.applies.empty";
    String KEY_I18N_ERR_APPLICABLE_EMPTY = "err.msg.applicable.empty";
    String KEY_I18N_ERR_FUNCTIONAL_FRAMEWORK_EMPTY = "err.msg.functional.framework.empty";
    String KEY_I18N_ERR_APPLNJAR_EMPTY = "err.msg.applnjar.empty";
    String KEY_I18N_ERR_ARCHETYPEJAR_EMPTY = "err.msg.archetypejar.empty";
    String KEY_I18N_ERR_PLTPROJ_EMPTY = "err.msg.pltproj.empty";
    String KEY_I18N_ERR_PLUGINJAR_INVALID = "err.msg.plugin.invalid";
    
    String KEY_I18N_ERR_EMAIL_EMPTY = "err.msg.email.empty";
    String KEY_I18N_ERR_ADDRS_EMPTY	= "err.msg.addrs.empty";
    String KEY_I18N_ERR_ZIPCODE_EMPTY = "err.msg.zip.empty";
    String KEY_I18N_ERR_CONTNUM_EMPTY = "err.msg.contnum.empty";
    String KEY_I18N_ERR_FAXNUM_EMPTY = "err.msg.faxnum.empty";
    String KEY_I18N_ERR_COUN_EMPTY = "err.msg.country.empty";
    String KEY_I18N_ERR_LICEN_EMPTY = "err.msg.licence.empty";
    String KEY_I18N_ERR_REPO_NAME_EMPTY = "err.msg.reponame.empty";
    String KEY_I18N_ERR_REPO_URL_INVALID = "err.msg.repourl.invalid";
    String KEY_I18N_ERR_REPO_USERNAME_EMPTY = "err.msg.repousername.empty";
    String KEY_I18N_ERR_REPO_PASSWORD_EMPTY = "err.msg.repopassword.empty";
    String KEY_I18N_ERR_APPLNPLTF_EMPTY = "err.msg.applnpltf.empty";
    String KEY_I18N_ERR_GROUP_EMPTY = "err.msg.group.empty";
    String KEY_I18N_ERR_TECH_EMPTY = "err.msg.applies.empty";
    
    String KEY_I18N_ERR_VIDEO_EMPTY = "err.msg.vdeo.empty";
    String KEY_I18N_ERR_IMAGE_EMPTY = "err.msg.img.empty";
    String KEY_I18N_VIDEO_UPDATING = "lbl.prog.vdeo.update";
    String KEY_I18N_VIDEO_CREATING = "lbl.prog.vdeo.save";
    String KEY_I18N_ERR_DWN_GRPID_EMPTY = "err.msg.grpid.empty";
    String KEY_I18N_ERR_DWN_ARTFID_EMPTY = "err.msg.artfid.empty";
        
    String KEY_I18N_BUTTON_SAVE = "lbl.hdr.comp.save";
    String KEY_I18N_BUTTON_UPDATE = "lbl.hdr.comp.update";
    String KEY_I18N_VIDEO_TITLE_ADD = "lbl.hdr.adm.vdeoadd";
    String KEY_I18N_VIDEO_TITLE_EDIT = "lbl.hdr.adm.vdoedit";
    
    
    /*****************************
     * I18N Keys Constants
     *****************************/	
    String FEATURE_ADDED = "succ.feature.add";
    String FEATURE_UPDATED = "succ.feature.update";
    String FEATURE_DELETED = "succ.feature.delete";
    
    String COMPONENT_ADDED = "succ.component.add";
    String COMPONENT_NOT_ADDED = "fail.component.add";
    String COMPONENT_DELETED = "succ.component.delete";
    String COMPONENT_NOT_DELETED = "fail.component.delete" ;
    
    String APPTYPES_ADDED = "succ.appType.add";
    String APPTYPES_UPDATED = "succ.appType.update";
    String APPTYPES_DELETED = "succ.appType.delete";
    
    String CONFIGTEMPLATE_ADDED = "succ.configtemplate.add";
    String CONFIGTEMPLATE_UPDATED = "succ.configtemplate.update";
    String CONFIGTEMPLATE_DELETED = "succ.configtemplate.delete";
    
    String ARCHETYPE_ADDED = "succ.archetype.add";
    String ARCHETYPE_NOT_ADDED = "fail.archetype.add";
    String ARCHETYPE_UPDATED = "succ.archetype.update";
    String ARCHETYPE_NOT_UPDATED = "fail.archetype.update";
    String ARCHETYPE_DELETED = "succ.archetype.delete";
    String ARCHETYPE_NOT_DELETED = "fail.archetype.delete" ;
    String TECH_GROUP_UPDATED = "succ.techgroup.update";
    
    String PLTPROJ_ADDED = "succ.pltproj.add";
    String PLTPROJ_UPDATED = "succ.pltproj.updated";
    String PLTPROJ_DELETED = "succ.pltproj.delete";
    String DOWNLOAD_FAILED = "download.failed";
    
    String CUSTOMER_ADDED = "succ.customer.add";
    String CUSTOMER_UPDATED = "succ.customer.update";
    String CUSTOMER_DELETED = "succ.customer.delete";
    String CUSTOMER_NOT_ADDED = "fail.customer.add";
    String CUSTOMER_NOT_UPDATED = "fail.customer.update";
    String CUSTOMER_NOT_DELETED = "fail.customer.delete";
    
    String PERMISSION_DELETED = "succ.permission.delete";
    String PERMISSION_NOT_DELETED = "fail.permission.delete";
    
    String ROLE_ADDED = "succ.role.add";
    String ROLE_UPDATED = "succ.role.update";
    String ROLE_NOT_ADDED = "fail.role.add";
    String ROLE_DELETED = "succ.role.delete";
    String ROLE_NOT_DELETED = "fail.role.delete" ;
    String ROLE_ADDED_TO_USER = "succ.role.add.to.user";
    String PERMISSION_ADDED_TO_ROLE = "succ.permission.add.to.role";
    
    String VIDEO_ADDED = "succ.vdeo.add";
    String VIDEO_DELETED = "succ.vdeo.delete";
    
    String URL_ADDED = "succ.url.add";
    String URL_NOT_ADDED = "fail.url.add";
    String URL_DELETED = "succ.globurl.delete";
    String URL_NOT_DELETED = "fail.globurl.delete";
    String URL_UPDATED = "succ.url.update";
    
    String DOWNLOAD_ADDED = "succ.download.add";
    String DOWNLOAD_UPDATED = "succ.download.update";
    String DOWNLOAD_DELETED = "succ.download.delete";
    
    //Email validation
    String INVALID_EMAIL = "err.msg.invalid.email";
    
    // Framework Theme Color Keys
    String BRANDING_COLOR =  "brandingColor";
    String ACCORDION_BACKGROUND_COLOR = "accordionBackGroundColor";
    String BODYBACKGROUND_COLOR = "bodyBackGroundColor";
    String BUTTON_COLOR = "ButtonColor";
    String PAGEHEADER_COLOR = "PageHeaderColor";
    String COPYRIGHT_COLOR = "CopyRightColor";
    String LABEL_COLOR = "LabelColor";
    String MENU_FONT_COLOR = "MenufontColor"; 
    String MENU_BACKGROUND_COLOR = "MenuBackGround";
    String SUB_MENU_BACKGROUND_COLOR = "SubMenuBackGround";
    String COPYRIGHT = "CopyRight";
    String DISABLED_LABEL_COLOR = "DisabledLabelColor";
    String EDIT_MENU_BACKGROUND_COLOR = "editMenuColor";
    String HEADER_BACKGROUND_COLOR = "headerBackGroundcolor";
    String FOOTER_BACKGROUND_COLOR = "footerBackGroundcolor";
    
    /*****************************
     * Permission Constants
     * String PER_XXX
     *****************************/
    String PER_MANAGE_REUSABLE_COMPONENTS = "manage_reusable_components";
    String PER_VIEW_REUSABLE_COMPONENTS = "view_reusable_components";
    String PER_MANAGE_ARCHETYPES = "manage_archetypes";
    String PER_VIEW_ARCHETYPES = "view_archetypes";
    String PER_MANAGE_CONFIG_TEMPLATES = "manage_configTemplates";
    String PER_VIEW_CONFIG_TEMPLATES = "view_configTemplates";
    String PER_MANAGE_PILOT_PROJECTS = "manage_pilotProjects";
    String PER_VIEW_PILOT_PROJECTS = "view_pilotProjects";
    String PER_MANAGE_DOWNLOADS = "manage_downloads";
    String PER_VIEW_DOWNLOADS = "view_downloads";
    String PER_MANAGE_CUSTOMERS = "manage_customers";
    String PER_VIEW_CUSTOMERS = "view_customers";
    String PER_MANAGE_USERS = "manage_users";
    String PER_VIEW_USERS = "view_users";
    String PER_MANAGE_ROLES = "manage_roles";
    String PER_VIEW_ROLES = "view_roles";
    String PER_MANAGE_PERMISSIONS = "manage_permissions";
    String PER_VIEW_PERMISSIONS = "view_permissions";
    String PER_MANAGE_LDAP = "manage_ldap";
    String PER_VIEW_LDAP = "view_ldap";
    String PER_MANAGE_VIDEOS = "manage_videos";
    String PER_VIEW_VIDEOS = "view_videos";
    String PER_MANAGE_GLOBALURL = "manage_globalurl";
    String PER_VIEW_GLOBALURL = "view_globalurl";
}