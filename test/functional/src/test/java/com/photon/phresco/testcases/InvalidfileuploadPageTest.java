



package com.photon.phresco.testcases;

import java.io.IOException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.photon.phresco.Screens.InvalidJarBase;
import com.photon.phresco.uiconstants.AdminUIData;
import com.photon.phresco.uiconstants.PhrescoUiConstants;
import com.photon.phresco.uiconstants.UIConstants;
import com.photon.phresco.uiconstants.UserInfoConstants;

public class InvalidfileuploadPageTest  {
	private  UIConstants uiConstants;
	private  PhrescoUiConstants phrescoUIConstants;
	private  InvalidJarBase invalidjarBase;
	
	private  String methodName;
	//private  String selectedBrowser;
	private  AdminUIData adminUIConstants;
	private  UserInfoConstants userInfoConstants;
	
	@Parameters(value = { "browser", "platform" })
	@BeforeTest
	public  void setUp(String browser, String platform) throws Exception {
		try {
			System.out.println("----------------------LoginPage-----------------");
			phrescoUIConstants = new PhrescoUiConstants();
			uiConstants = new UIConstants();
			userInfoConstants =new UserInfoConstants();
			adminUIConstants = new AdminUIData();
			String selectedBrowser = browser;
			String selectedPlatform = platform;
			
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			System.out
			.println("-----------Selected Browser to execute testcases--->>"
					+ selectedBrowser);
			
			String applicationURL = phrescoUIConstants.PROTOCOL + "://"
					+ phrescoUIConstants.HOST + ":" + phrescoUIConstants.PORT
					+ "/";
			
			invalidjarBase = new InvalidJarBase(selectedBrowser, selectedPlatform, applicationURL,phrescoUIConstants.CONTEXT,phrescoUIConstants,uiConstants,userInfoConstants,adminUIConstants);
			
		} catch (Exception exception) {
			exception.printStackTrace();

		}

	}

 @Test
 public void testValidLogin()
		throws InterruptedException, IOException, Exception {
	try {

		System.out.println("---------testLoginAdminUI()-------------");
		methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		invalidjarBase.validLoginAdminUI(methodName);
		invalidjarBase.customerSelection(methodName);
		
		
	} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void addInvalidJarPhpModules()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		/*baseScreen.validLoginAdminUI(methodName);*/
		invalidjarBase.addInvalidJarModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void addInvalidJarDrupal6Modules()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		/*baseScreen.validLoginAdminUI(methodName);*/
		invalidjarBase.addInvalidJarModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void addInvalidJarDrupal7Modules()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		/*baseScreen.validLoginAdminUI(methodName);*/
		invalidjarBase.addInvalidJarModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void addInvalidJarWordPressModules()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		/*baseScreen.validLoginAdminUI(methodName);*/
		invalidjarBase.addInvalidJarModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void addInvalidJarMultiYUIModules()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		/*baseScreen.validLoginAdminUI(methodName);*/
		invalidjarBase.addInvalidJarModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void addInvalidJarMultiJQueryModules()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		/*baseScreen.validLoginAdminUI(methodName);*/
		invalidjarBase.addInvalidJarModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void addInvalidJarYUIMobileModules()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		/*baseScreen.validLoginAdminUI(methodName);*/
		invalidjarBase.addInvalidJarModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void addInvalidJarJQueryMobileModules()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		/*baseScreen.validLoginAdminUI(methodName);*/
		invalidjarBase.addInvalidJarModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void addInvalidJarDOTNETModules()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		/*baseScreen.validLoginAdminUI(methodName);*/
		invalidjarBase.addInvalidJarModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void addInvalidJarSharePointModules()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		/*baseScreen.validLoginAdminUI(methodName);*/
		invalidjarBase.addInvalidJarModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void addInvalidJarSiteCoreModules()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInvalidJarModules(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarNodejsModules()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInvalidJarModules(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 
 @Test
 public void addInvalidJarJavaStandaloneModules()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInvalidJarModules(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarJavaWebServiceModules()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInvalidJarModules(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJariPhoneHybridModules()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInvalidJarModules(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJariphoneNativeModules()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInvalidJarModules(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJariPhoneLibraryModules()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInvalidJarModules(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarAndroidHybridModules()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInvalidJarModules(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarAndroidNativeModules()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInvalidJarModules(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarAndroidLibraryModules()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInvalidJarModules(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarWindowsMetroModules()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInvalidJarModules(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarWindowsPhoneModules()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInvalidJarModules(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarBlackBerryHybridModules()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInvalidJarModules(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 
 @Test
 public void addInvalidJarPhpJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarDrupal6Jslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarDrupal7Jslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarWordPressJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarMultiYUIJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarMultiJQueryJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarYUIMobileJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarJQueryMobileJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarDOTNETJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarSharePointJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarSiteCoreJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarNodejsJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarJavaStandaloneJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarJavaWebServiceJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJariPhoneHybridJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJariphoneNativeJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJariPhoneLibraryJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }

 @Test
 public void addInvalidJarAndroidHybridJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarAndroidNativeJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarAndroidLibraryJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarWindowsMetroJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarWindowsPhoneJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarBlackBerryHybridJslibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addingInvalidJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarPHPComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarDrupal6Components()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarDrupal7Components()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarWordPressComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarMultiYUIComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarMultiJQueryComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarYUIMobileComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarJQueryMobileComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarDOTNETComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarSharePointComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarSiteCoreComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarNodejsComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarJavaStandaloneComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarJavaWebServiceComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJariPhoneHybridComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJariphoneNativeComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJariPhoneLibraryComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }

 @Test
 public void addInvalidJarAndroidHybridComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarAndroidNativeComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarAndroidLibraryComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarWindowsMetroComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarWindowsPhoneComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 @Test
 public void addInvalidJarBlackBerryHybridComponents()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidComponent(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 
 @Test
 public void addInvalidJarArchetypeCreationAppLayer()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidArchetypeApplayer(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 
 public void addInvalidJarArchetypeCreationWebLayer()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidArchetypeWeblayer(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
 
 public void addInvalidJarArchetypeCreationMobileLayer()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInValidArchetypeMobilelayer(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }

 public void addInvalidPilotProjectPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInvalidPilotProject(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }

 public void addInvalidVideoFilePage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInvalidVideoFile(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }

 public void addInvalidComponentInDownloadFile()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			invalidjarBase.addInvalidComponentInDownload(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }


 
 @AfterTest
 public  void tearDown() {
	 invalidjarBase.closeBrowser();
 }

}
