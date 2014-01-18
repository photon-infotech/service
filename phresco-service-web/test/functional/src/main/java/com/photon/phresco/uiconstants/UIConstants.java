package com.photon.phresco.uiconstants;

import java.lang.reflect.Field;

public class UIConstants {
	
	private ReadXMLFile readXml;
	
	
  /**
   * InvalidArchetypePage creation weblayer	
   */


	
	public String LOGIN_USERNAME="usernameXpath";
	public String LOGIN_PASSWORD="passwordXpath";
	public String LOGIN_BUTTON="loginButtonXpath";
	

	public String DADHBOARD_LINK="dashboardLinkXpath";
	public String MOST_USED_ARCHETYPE="mostUsedArchetypesXpath";
	public String MOST_USED_FEATURE="mostusedFeatureXpath";
	public String MOST_USED_PILOT_PROJECT="mostusedPilotProjectsXpath";
	

	public String COMPONENTS_LINK = "componentsLinkXpath";
	
	public String CUSTOMER_DROPDOWN= "customerDropdown";
	public String CUSTOMER_DROPDOWN_VALUE="customerDropdownValue";
	
	
	public String REASUABLE_COMPONENT_TAB="componentsReasuableComponentsXpath";
	
	public String REASUABLE_COMPONENT_MODULES_TAB="ReasuableComponentsModulesTabXpath";
	
	
	public String ADD_MODULES_BUTTON="modulesAddButtonXpath";
	
	
	public String MODULES_NAME="modulesNameXpath";
	public String MODULE_DESCRIPTION="modulesDescriptionXpath";
	public String MODULE_HELPTEXT="modulesHelpTextXpath";
	public String MODULE_TECHNOLOGY="modulesTechnologyXpath";
	public String MODULE_MODULETYPE="modulesModuleTypeXpath";
	public String MODULE_MODULETYPE_EXTERNALMODULE="modulesExternalModuleTypeXpath";
	public String MODULE_DEFAULTMODULE="moduleDefaultmodule";
	public String MODULE_LICENSE_TYPE="moduleLicenseTypeXpath";
	public String MODULE_UPLOAD_FILE="modulesUploadFileXpath";
	public String MODULE_SELECT_DEPENDENCY="modulesSelectDependencyXpath";
	public String MODULE_DEPENDENCY_LOGIN_CHECKBOX="modulesDependencyLoginCheckboxXpath";
	public String MODULE_DEPENDENCY_OK_BUTTON="modulesDependencyOKButtonXpath";
	public String MODULE_SAVE_BUTTON="moduleSaveButtonXpath";
	public String MODULE_CANCEL_BUTTON="moduleCancelButtonXpath";
	public String PROJCREATIONSUCCESSMSG="moduleCreationSuccessMsg";
	

	
	public String UPDATE_MODULE_CHECK_BOX="updateModulesCheckBoxXpath";
	public String UPDATE_MODULE_LINK="updateModulesLinkXpath";
	public String UPDATE_MODULE_UPDATE_BUTTON="updateModuleUpdateButtonXpath";
	
	
	public String DELETE_MODULE_CHECK_BOX="deleteModuleCheckBoxXpath";
	public String DELETE_MODULE_DELETE_BUTTON="deleteModuleDeleteButtonXpath";
	public String DELETE_MODULE_DIALOG_BOX_OK_BUTTON="deleteModuleDialogBoxOkButtonXpath";
	
	public String REASUABLE_COMPONENTS_JSLIBRARIES_TAB="ReasuableComponentsJSlibrariesTabXpath";
	public String JSLIBRARIES_BUTTON="JSlibrariesButtonXpath";
	public String JSLIBRARIES_ADD_BUTTON="JSlibrariesAddButtonXpath";
	
	public String JSLIBRARIES_NAME="JSlibrariesNameXpath";
	public String JSLIBRARIES_DESCRIPTION="JSlibrariesDescriptionXpath";
	public String JSLIBRARIES_HELPTEXT="JSlibrariesHelpTextXpath";
	public String JSLIBRARIES_TECHNOLOGY="JSlibrariesTechnologyXpath";
	public String JSLIBRARIES_LICENSE_TYPE="JSlibrariesLicenseTypeXpath";
	public String ADD_JSLIBRARIES_LICENSE_TYPE="addJSlibrariesLicenseType";
	
	//public String MODULE_UPLOAD_FILE_JSLIBRARIES_XPATH="moduleuploadFileXpath";
	public String JSLIBRARIES_SELECT_DEPENDENCY="JSlibrariesSelectDependencyXpath";
	public String JSLIBRARIES_SELECT_JSON_CHECKBOX="dependencyPhonegapCheckboxButtonXpath";
	public String JSLIBRARIES_DEPENDENCY_OK_BUTTON="JSlibrariesDependencyOKButtonXpath";
	
	public String JSLIBRARIES_SAVE_BUTTON="JSlibrarieSaveButtonXpath";
	public String JSLIBRARIES_CANCEL_BUTTON="JSlibrariesCancelButtonXpath";

	public String UPDATE_JSLIBRARIES_LINK_XPATH="updateJSlibrariesLinkXpath";
	public String UPDATE_JSLIBRARIES_OPTION_BUTTON_XPATH="updateJSlibrariesOptionButtonXpath";
	public String UPDATE_JSLIBRARIES_UPDATE_BUTTON_XPATH="JSlibrariesUpdateButtonXpath";
	
	public String REASUABLE_COMPONENTS_COMPONENT_TAB="ReasuableComponentsComponentTabXpath";
	public String COMPONENTS_ADD_BUTTON="ComponentAddButtonXpath";
	public String COMPONENTS_NAME="ComponentNameXpath";
	public String COMPONENTS_DESCRIPTION="ComponentDescriptionXpath";
	public String COMPONENTS_HELPTEXT="ComponentHelpTextXpath";
	public String COMPONENTS_TECHNOLOGY="ComponentTechnologyXpath";
	public String COMPONENTS_LICENSE_TYPE="ComponentLicenseTypeXpath";
	public String COMPONENTS_SELECT_DEPENDENCY="ComponentSelectDependencyXpath";
	public String COMPONENTS_SELECT_JSON_CHECKBOX="dependencyPhonegapCheckboxButtonXpath";
	public String COMPONENTS_DEPENDENCY_OK_BUTTON="ComponentDependencyOKButtonXpath";
	public String COMPONENTS_SAVE_BUTTON="ComponentSaveButtonXpath";
	public String COMPONENTS_CANCEL_BUTTON="ComponentCancelButtonXpath";
																	
	
	public String APPLICATION_TYPES_TAB="applicationTypesTabXpath";
	public String APPLICATION_TYPES_CREATE="applicationTypesCreateButtonXpath";
	public String APPLICATION_TYPE_NAME="applicationTypeNameXpath";
	public String APPLICATION_TYPE_DESCRIPTION="applicationTypeDescriptionXpath";
	public String APPLICATION_TYPE_SAVE_BUTTON="applicationTypeSaveButtonXpath";
	public String APPLICATION_TYPE_CANCEL_BUTTON="applicationTypeCancelButtonXpath";
	
	public String UPDATE_APPLICATION_TYPES_LINK="updateApplicationTypesLinkXpath";
	public String UPDATE_APPLICATION_TYPES_DESCRIPTION="updateApplicationTypesDescriptionXpath";
	public String UPDATE_APPLICATION_TYPES_UPDATE_BUTTON="updateDescriptionUpdateButtonXpath";

	public String ADMIN_LINK="adminLinkXpath";
	
	// new one
	public String VIDEOS_BUTTON="videoLinkXpath";
	public String VIDEOS_ADD="videosAddXpath";
	public String VIDEOS_NAME="videoName";
	public String VIDEOS_DESCRIPTION="videoDescription";
	public String VIDEOS_SAVE="videoSavebutton";
	public String VIDEOS_CANCEL="videocancelbutton";
	
	
	
	public String ADMIN_CUSTOMER_TAB="adminCustomerTabXpath";
	public String CUSTOMER_CREATE_BUTTON="customerCreateButtonXpath";
	public String CUSTOMER_NAME="customerNameXpath";
	public String CUSTOMER_DESCRIPTION="customerDescriptionXpath";
	public String CUSTOMER_EMAIL="customerEmailXpath";
	public String CUSTOMER_ADDRESS="customerAddressXpath";
	public String CUSTOMER_COUNTRY="customerCountryXpath";
	public String CUSTOMER_STATE="customerStateXpath";
	public String CUSTOMER_ZIPCODE="customerZipcodeXpath";
	public String CUSTOMER_CONTACT_NUMBER="customerContactNumberXpath";
	public String CUSTOMER_FAX="customerFaxXpath";
	public String CUSTOMER_HELP_TEXT="customerHelpTextXpath";
	public String CUSTOMER_LICENCE_TYPE="customerLicenceTypeXpath";
	
	public String CUSTOMER_SAVE_BUTTON="customerSaveButtonXpath";
	public String CUSTOMER_CANCEL_BUTTON="customerCancelButtonXpath";
	
	public String CUSTOMER_CALENDER_VALID_FROM="customerCalenderValidFromXpath";
	public String CUSTOMER_CALENDER_VALID_FROM_FRONT_ARROW="customerCalenderValidFromBackArrowXpath";
	public String CUSTOMER_CALENDER_VALID_FROM_DATE="customerCalenderValidFromDateXpath";
	
	public String CUSTOMER_CALENDER_VALID_UPTO="customerCalenderValidUptoXpath";
	public String CUSTOMER_CALENDER_VALID_UPTO_FRONT_ARROW="customerCalenderValidUptoBackArrowXpath";
	public String CUSTOMER_CALENDER_VALID_UPTO_DATE="customerCalenderValidUptoDateXpath";
	public String CUSTOMER_REPONAME="customerReponameXpath";
	public String CUSTOMER_REPOURL="customerRepoUrl";
	public String CUSTOMER_APPLIESTOCHECKBOX="customerAppliestoCheckboxXpath";

	public String UPDATE_CUSTOMER_LINK="updateCustomerLinkXpath";
	public String UPDATE_CUSTOMER_UPDATE_BUTTON="updateCustomerUpdateButtonXpath";
	
	
	public String ADMIN_GLOBALURL_TAB="adminGlobalurlTabXpath";
	public String GLOBALURL_ADD_BUTTON="adminGlobalurlAddXpath";
	public String GLOBALURL_NAME="adminGlobalurlNameXpath";
	public String GLOBALURL_DESCRIPTION="adminGlobalurlDescriptionXpath";
	public String GLOBALURL_URL="adminGlobalurlUrlXpath";
	public String GLOBALURL_SAVE_BUTTON="adminGlobalurlSavebuttonXpath";
	public String GLOBALURL_CANCEL_BUTTON="adminGlobalurlCancelbuttonXpath";

	

	
	public String DELETE_JSLIBRARIES_XPATH="deleteJSlibrariesXpath";
	
	public String RC_MODULETECHNOLOGYFORNEWARCHETYPE="rccreatedsamplemobilelayer";
	public String JS_TECHNOLOGY_VERSION="jstechnologyversion";
	
	
	
	
	public String RC_SELECTMODULETECHNOLOGY="rcselectmobiletechnology";
	public String ARCHETYPES_TAB="archetypeTabXpath";
	public String ARCHETYPES_CREATE_BUTTON="archetypeCreateButtonXpath";
	public String ARCHETYPES_NAME="archetypeNameXpath";
	public String ARCHETYPES_DESCRIPTION="archetypeDescriptionXpath";
	public String ARCHETYPES_TECHNOLOGY_VERSION="archetypeTechnologyVersionXpath";
	public String ARCHETYPES_VERSION_COMMENT="archetypeVersionCommentXpath";
	public String ARCHETYPES_APPLICATION_TYPE="archetypeApplicationTypeXpath";
	public String ARCHETYPES_UPLOAD_JAR="archetypeUploadJarXpath";
	public String ARCHETYPES_UPLOAD_PLUGIN_JAR="archetypeUploadPluginJarXpath";
	public String ARCHETYPES_APPLICATION_FEATURES_ALL="archetypeApplicationFeaturesAllXpath";
	public String ARCHETYPES_APPLICATION_GROUP_TYPE="createApplicationLayerArchetypeGroupTypeXpath";
	
/*	public String ARCHETYPES_APPLICATION_FEATURES_CODE="archetypeApplicationFeaturesCodeXpath";
	public String ARCHETYPES_APPLICATION_FEATURES_BUILD="archetypeApplicationFeaturesBuildXpath";
	public String ARCHETYPES_APPLICATION_FEATURES_DEPLOY="archetypeApplicationFeaturesDeployXpath";
	public String ARCHETYPES_APPLICATION_FEATURES_RUN_AGAINST_SOURCE="archetypeApplicationFeaturesRunAgainstSourceXpath";
	public String ARCHETYPES_APPLICATION_FEATURES_UNIT_TEST="archetypeApplicationFeaturesUnitTestXpath";
	public String ARCHETYPES_APPLICATION_FEATURES_FUNCTONAL_TEST="archetypeApplicationFeaturesFunctionalTestXpath";
	public String ARCHETYPES_APPLICATION_FEATURES_PERFORMANCE_TEST="archetypeApplicationFeaturesPerformanceTestRunXpath";
	public String ARCHETYPES_APPLICATION_FEATURES_LOAD_TEST="archetypeApplicationFeaturesLoadTestXpath";
	public String ARCHETYPES_APPLICATION_FEATURES_REPORTS="archetypeApplicationFeaturesReportsXpath";*/
	public String ARCHETYPES_APPLICATION_FEATURES_CI="archetypeApplicationFeaturesCIXpath";
	public String ARCHETYPES_APPLICABLE_REPORTS_ALL="archetypeApplicableReportsAllXpath";
	public String ARCHETYPES_APPLICABLE_REPORTS_PROJECT_INFO_REPORT="archetypeApplicableReportsProjectInfoReportXpath";
	public String ARCHETYPES_APPLICABLE_REPORTS_JAVA_DOC_REPORT="archetypeApplicableReportsJavadocReportXpath";
	public String ARCHETYPES_APPLICABLE_REPORTS_COBERTURA_REPORT="archetypeApplicableReportsCoberturaReportXpath";
	public String ARCHETYPES_APPLICABLE_REPORTS_JDEPEND_REPORT="archetypeApplicableReportsJdependReportXpath";
	public String ARCHETYPES_APPLICABLE_REPORTS_JXR_REPORT="archetypeApplicableReportsJXRReportXpath";
	public String ARCHETYPES_APPLICABLE_REPORTS_PMD_REPORT="archetypeApplicableReportsPMDReportXpath";
	public String ARCHETYPES_APPLICABLE_REPORTS_SURFIRE_REPORT="archetypeApplicableReportsSurefireReportXpath";
	public String ARCHETYPES_SAVE_BUTTON="archetypeSaveButtonXpath";
	public String ARCHETYPES_CANCEL_BUTTON="archetypeCancelButtonXpath";
	public String ARCHETYPES_FUNCTIONAL_FRAMEWORK="functionalTestclickXpath";

	public String PILOT_PROJECT_TAB="pilotProjectTabXpath";
	public String PILOT_PROJECT_ADD_BUTTON="pilotProjectAddButtonXpath";
	public String PILOT_PROJECT_ADD_NAME="pilotProjectAddNameXpath";
	public String PILOT_PROJECT_ADD_DESCRIPTION="pilotProjectAddDescriptionXpath";
	public String PILOT_PROJECT_ADD_VERSION="pilotProjectAddVersionXpath";
	public String PILOT_PROJECT_ADD_TECHNOLOGY="pilotProjectAddTechnologyXpath";
	public String PILOT_PROJECT_ADD_GROUP_ID="pilotProjectAddGroupidXpath";
	public String PILOT_PROJECT_ADD_ARTIFACT_ID="pilotProjectAddArtifactidXpath";
	public String PILOT_PROJECT_ADD_VERSION_PROJECT="pilotProjectAddVersionOfProject";
	public String PILOT_PROJECT_ADD_SAVE_BUTTON="pilotProjectAddSaveButtonXpath";
	public String PILOT_PROJECT_ADD_CANCEL_BUTTON="pilotProjectAddCancelButtonXpath";
	
	public String PILOT_PROJECT_UPDATE_PROJECR_LINK="pilotProjectUpdateProjectLinkXpath";
	public String PILOT_PROJECT_UPDATE_BUTTON="pilotProjectUpdateButtonXpath";
	
	
	
	public String DOWNLOAD_TAB="downloadTabXpath";
	public String DOWNLOAD_ADD_BUTTON="downloadAddButtonXpath";
	public String DOWNLOAD_ADD_NAME="downloadAddNameXpath";
	public String DOWNLOAD_ADD_DESCRIPTION="downloadAddDescriptionXpath";
	public String DOWNLOAD_ADD_TECHNOLGY="downloadAddTechnologyXpath";
	public String DOWNLOAD_ADD_LICENSE_TYPE="downloadAddLicenseTypeXpath";
	public String DOWNLOAD_ADD_APPLICATION_PLATFORM="downloadApplicationPlatformXpath";
	public String DOWNLOAD_ADD_VERSION="downloadAddVersionXpath";
	public String DOWNLOAD_ADD_GROUPID="downloadAddGroupIdXpath";
	public String DOWNLOAD_ADD_ARTIFACTID="downloadAddArtifactIdXpath";
	public String DOWNLOAD_ADD_GROUP="downloadAddGroupXpath";
	public String DOWNLOAD_ADD_SAVE_BUTTON="downloadSaveButtonXpath";
	public String DOWNLOAD_ADD_CANCEL_BUTTON="downloadCancelXpath";
	
	

	public String DOWNLOAD_UPDATE_DOWNLOAD_LINK="downloadUpdateDownloadLinkXpath";
	public String DOWNLOAD_UPDATE_BUTTON="downloadUpdateButtonXpath";
	
	
	public String CONFIGURATION_TEMPLATE_TAB="configurationTemplateTabXpath";
	public String CONFIGURATION_TEMPLATE_ADD_BUTTON="configurationTemplateAddButtonXpath";
	public String CONFIGURATION_TEMPLATE_ADD_NAME="configurationTemplateAddNameXpath";
	public String CONFIGURATION_TEMPLATE_ADD_DESCRIPTION="configurationTemplateAddDescriptionXpath";
	public String CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO="configurationTemplateAddCheckAllAppliesToXpath";
	public String CONFIGURATION_CUSTOMPROPERTIES="configurationcustomproperties";
	public String CONFIGURATION_PROPERTYTEMP_TYPE_NUMBER="configurationPropertyTemTypeNumber";
	public String CONFIGURATION_PROPERTYTEMP_TYPE_PASSWORD="configurationPropertyTempTypePassword";
	public String CONFIGURATION_PROPERTYTEMP_TYPE_FILETYPE="configurationPropertyTempTypeFileType";
	public String CONFIGURATION_PROPERTYTEMP_TYPE_BOOLEAN="configurationPropertyTempTypeBoolean";
	public String CONFIGURATION_PROPERTYTEMP_TYPE_ACTIONS="configurationPropertyTempTypeActions";
	public String CONFIGURATION_TEMPLATE_ADD_KEY="configurationTemplateAddKeyXpath";
	public String CONFIGURATION_TEMPLATE_ADD_NAMEFIELD="congigurationTemplateAddNameXpath";
	public String CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON="configurationTemplateAddSaveButtonXpath";
	
	
 //TECHNOLOGIES
	public String ADD_MODULE_PHP_TECHNOLOGY_VALUE="addModulePhpTechnologyValue";
	public String ADD_COMPONENT_DRUPAL6_TECHONOLGY_VALUE="addComponentTechnologyDrupal6";
	public String ADD_COMPONENT_DRUPAL7_TECHONOLGY_VALUE="addComponentTechnologyDrupal7";
	public String ADD_COMPONENT_WORDPRESS_TECHONOLGY_VALUE="addComponentTechnologyWordPress";
	public String ADD_COMPONENT_MULTIYUI_TECHONOLGY_VALUE="addComponentTechnologyMultiYUI";
	public String ADD_COMPONENT_MULTIJQUERY_TECHONOLGY_VALUE="addComponentTechnologyMultiJQuery";
	public String ADD_COMPONENT_YUIMOBILE_TECHONOLGY_VALUE="addComponentTechnologyYUIMobile";
	public String ADD_COMPONENT_JQUERYMOBILE_TECHONOLGY_VALUE="addComponentTechnologyJQueryMobile";
	public String ADD_COMPONENT_DOTNET_TECHONOLGY_VALUE="addComponentTechnologyDotNet";
	public String ADD_COMPONENT_SHAREPOINT_TECHONOLGY_VALUE="addComponentTechnologySharePoint";
	public String ADD_COMPONENT_SITECORE_TECHONOLGY_VALUE="addComponentTechnologySiteCore";
	public String ADD_COMPONENT_NODEJS_TECHONOLGY_VALUE="addComponentTechnologyNodejs";
	public String ADD_COMPONENT_JAVASTANDALONE_TECHONOLGY_VALUE="addComponentTechnologyJavaStandalone";
	public String ADD_COMPONENT_JAVAWEBSERVICE_TECHONOLGY_VALUE="addComponentTechnologyJavaWebservice";
	public String ADD_COMPONENT_IPHONEHYBRID_TECHONOLGY_VALUE="addComponentTechnologyiPhoneHybrid";
	public String ADD_COMPONENT_IPHONENATIVE_TECHONOLGY_VALUE="addComponentTechnologyiPhoneNative";
	public String ADD_COMPONENT_IPHONELIBRARY_TECHONOLGY_VALUE="addComponentTechnologyiPhoneLibrary";
	public String ADD_COMPONENT_IPHONEWORKSPACE_TECHONOLGY_VALUE="addComponentTechnologyiPhoneWorkSpace";
	public String ADD_COMPONENT_ANDROIDHYBRID_TECHONOLGY_VALUE="addComponentTechnologyAndroidHybrid";
	public String ADD_COMPONENT_ANDROIDNATIVE_TECHONOLGY_VALUE="addComponentTechnologyAndroidNative";
	public String ADD_COMPONENT_ANDROIDLIBRARY_TECHONOLGY_VALUE="addComponentTechnologyAvdroidLibrary";
	public String ADD_COMPONENT_WINDOWSMETRO_TECHONOLGY_VALUE="addComponentTechnologyWindowsMetro";
	public String ADD_COMPONENT_WINDOWSPHONE_TECHONOLGY_VALUE="addComponentTechnologyWindowsPhone";
	public String ADD_COMPONENT_BLACKBERRY_HYBRID_TECHONOLGY_VALUE="addComponentTechnologyBlackBerryHybrid";
	
	// Element ID for deleting
	public String SELECT_ARCHETYPECHECKBOX="Archetypecheckbox";
	public String SELECT_CHECKBOX="CheckAll";
	public String DELETE_BUTTON="DeleteButton";
	public String ACCEPT_DELETE_OK="acceptDelete";
	
	// Element ID for Technology in modules,js libraries and components
	public String SELECT_TECHNOLOGY="selectTechnologyDropDown";
	
	// variable declartion for RBAC machanism
	public String USERS="userxpath";
	public String ROLES="rolesxpath";
	public String SERVICE="servicexpath";
	public String SERVICE_ADD_BUTTON="serviceAddButtonxpath";
	public String SERVICE_NAME="serviceNamevalueXpath";
	public String SERVICE_DERSCRIPTION_VALUE="serviceDescriptionValueXpath";
	public String SERVICE_SAVE="serviceSaveXpath";
	public String FRAMEWORK="frameworkXpath";
	public String FRAMEWORK_ADD_BUTTON="frameworkAddButtonXpath";
	public String FRAMEWORK_NAME="frameworkNameValeXpath";
	public String FRAMEWORK_DERSCRIPTION_VALUE="frameworkDescriptionXpath";
	public String FRAMEWORK_SAVE="frameworkSavaXpath";
	public String PERMISSION="permissionxpath";
	public String PERMISSION_SERVICE="permissionServiceXpath";
	public String PERMISSION_FRAMEWORK="permissionFrameworkxpath";
	
	//Service Admin for RBAC Test case 
	
	public String ADMIN_ROLES="adminRolesXpath";
	public String ADMIN_ROLES_ASSIGN="AdminRolesAssignXpath";
	public String SERVICE_ADD_OK="serviceAddOkXpath";
	public String ASSIGN_PERMISSION="AssignPermissionXpath";
    public String ASSIGN_PERMISSION_SINGLE="AssingPermissionSingle";
    public String ADMIN_ROLES_ASSIGN_SINGLE ="AssignSingleValueXpath";
    public String POP_UP_CANCEL_BTN ="PopUpCancelButton";
    public String SELECT_SINGLE_SERVICE="SelectSingleService";
    public String DELETE_BUTTON_OK="DeletebuttonOkXpath";
    public String SELECT_MUTIPLE_SERVICE="SelectMultipleServiceXpath";
	public String SERVICE_ADMIN_PERMISSION="serviceAdminXpath";
	public String PROJECT_ADMIN_PERMISSION="ProjectAdminXpath";
	public String VIEW_SERVICE_PERMISSION ="ViewServiceXpath";
	public String MODULE_ADMIN_PERMISSION="ModuleAdminXpath";
	
	public UIConstants() {
		try {
			readXml = new ReadXMLFile();
			readXml.loadUIConstants();
			Field[] arrayOfField1 = super.getClass().getFields();
			Field[] arrayOfField2 = arrayOfField1;
			int i = arrayOfField2.length;
			for (int j = 0; j < i; ++j) {
				Field localField = arrayOfField2[j];
				Object localObject = localField.get(this);
				if (localObject instanceof String)
					localField
							.set(this, readXml.getValue((String) localObject));

			}
		} catch (Exception localException) {
			throw new RuntimeException("Loading "
					+ super.getClass().getSimpleName() + " failed",
					localException);
		}
	}
}
