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

public class JsLibrariesPageTest {
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
	 public void testAndroidHybridReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testAndroidLibraryReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidLibraryModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testAndroidNativeReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidNativeModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testDOTNETReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddDOTNETModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testDeleteBlackBerryHybridJSLibraries()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testDeleteBlackBerryHybridJSLibraries()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.selectJSLibraries(methodName);
			baseScreen.deleteReusableComponents(methodName);
			baseScreen.deleteCreatedSamples(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testDrupal6ReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddPhpModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			

			
		} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testDrupal7ReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddPhpModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testJQueryMobileReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testJQueryMobileReusableComponentsAddJSlibrariesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testMultiJQueryReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddPhpModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testMultiYUIReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddPhpModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testYUIMobileReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddPhpModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testiPhoneHybridReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddiPhoneHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testiPhoneLibraryReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddiPhoneHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testiphoneNativeReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddiPhoneHybridModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testJavaStandaloneReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddPhpModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			

			
		} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testJavaWebServiceReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddPhpModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			

			
		} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testNodejsReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddNodejsModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testPHPReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddPhpModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testSharePointReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddDOTNETModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testSiteCoreReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddDOTNETModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testWindowsMetroReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddWindowsMetroModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testWindowsPhoneReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddWindowsMetroModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testWordPressReusableComponentsAddJSlibrariesPage()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddWordPressModulesPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			
			baseScreen.addingJSLibraries(methodName);
			
			

			
		} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	@AfterTest
	public  void tearDown() {
		baseScreen.closeBrowser();
	}

	}
	