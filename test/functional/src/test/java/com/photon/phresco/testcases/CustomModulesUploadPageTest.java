package com.photon.phresco.testcases;

import java.io.IOException;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


import com.photon.phresco.Screens.BaseScreen;
import com.photon.phresco.uiconstants.AdminUIData;
import com.photon.phresco.uiconstants.PhrescoUiConstants;
import com.photon.phresco.uiconstants.UIConstants;
import com.photon.phresco.uiconstants.UserInfoConstants;

public class CustomModulesUploadPageTest {
	private  UIConstants uiConstants;
	private  PhrescoUiConstants phrescoUIConstants;
	private  BaseScreen baseScreen;
	private  String methodName;
	//private static String selectedBrowser;
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
			
			baseScreen = new BaseScreen(selectedBrowser, selectedPlatform, applicationURL,phrescoUIConstants.CONTEXT,phrescoUIConstants,uiConstants,userInfoConstants,adminUIConstants);
			
		} catch (Exception exception) {
			exception.printStackTrace();

		}

	}

	@Test
	 public void testValidLogin()
			throws InterruptedException, IOException, Exception {
		try {

			System.out
					.println("---------testLoginAdminUI()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
				baseScreen.validLoginAdminUI(methodName);
				baseScreen.customerSelection(methodName);

			
		} catch (Exception t) {
			t.printStackTrace();
		}
		}
	
@Test
public void testDrupal6ReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddPhpModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
					
	} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testDrupal7ReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddPhpModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testJQueryMobileReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddPhpModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testMultiJQueryReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddPhpModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testMultiYUIReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddPhpModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testYUIMobileReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddPhpModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testiPhoneHybridReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddiPhoneHybridModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testiPhoneLibraryReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddiPhoneHybridModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testiphoneNativeReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddiPhoneHybridModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testJavaStandaloneReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddPhpModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
		
		

		
	} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testJavaWebServiceReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddPhpModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
		
		

		
	} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testNodejsReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddNodejsModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testPHPReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddPhpModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testSharePointReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddDOTNETModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testSiteCoreReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddDOTNETModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testWindowsMetroReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddWindowsMetroModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testWindowsPhoneReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddWindowsMetroModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testWordPressReusableComponentsAddModulesPage()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddWordPressModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
		baseScreen.addModules(methodName);
	} catch (Exception t) {
		t.printStackTrace();

	}
}
@AfterTest
public  void tearDown() {
	baseScreen.closeBrowser();
}

}