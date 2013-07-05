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

public class ConfigurationTemplatePageTest {
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
	 public void testAndroidHybridCompAddConfigTemplateNumber()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplateNumber(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testAndroidLibraryComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testAndroidNativeComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testDOTNETComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testBlackBerryHybridComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testDrupal6ComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testDrupal7ComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testJQueryMobileComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testMultiJQueryComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testMultiYUIComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testYUIMobileComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testiPhoneHybridComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testiPhoneLibraryComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testiphoneNativeComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testJavaStandaloneComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testJavaWebServiceComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testNodejsComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testPHPComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testSharePointComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testSiteCoreComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testWindowsMetroComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testWindowsPhoneComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	 @Test
	 public void testWordPressComponentsAddConfigurationTemplatePage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out.println("---------testWordPressComponentsAddEmptyConfigurationTemplatePage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addConfigurationTemplate(methodName);
		
			
			} catch (Exception t) {
			t.printStackTrace();
		}
	 }
	@AfterTest
	public  void tearDown() {
		baseScreen.closeBrowser();
	}
	}
	