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

public class PilotProjectPageTest {
	private  UIConstants uiConstants;
	private  PhrescoUiConstants phrescoUIConstants;
	private  BaseScreen baseScreen;
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
		public void testAndroidHybridComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 
	 @Test
		public void testAndroidLibraryComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 
	 @Test
		public void testAndroidNativeComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 
	 @Test
		public void testDOTNETComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 @Test
		public void testBlackBerryHybridComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 @Test
		public void testDrupal6ComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				

				
			} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 
	 @Test
		public void testDrupal7ComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 @Test
		public void testJQueryMobileComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 @Test
		public void testMultiJQueryComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 
	 
	 @Test
		public void testMultiYUIComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 @Test
		public void testYUIMobileComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 
	 @Test
		public void testiPhoneHybridComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 
	 @Test
		public void testiPhoneLibraryComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 @Test
		public void testiphoneNativeComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 
	 @Test
		public void testJavaStandaloneComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
					
	
			} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 @Test
		public void testJavaWebServiceComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
			
			} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 
	 
	 @Test
		public void testNodejsComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 @Test
		public void testPHPComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 
	 @Test
		public void testSharepointComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 @Test
		public void testSiteCoreComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 @Test
		public void testWindowsMetroComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 @Test
		public void testWindowsPhoneComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 
	 @Test
		public void testWordPressComponentsAddValidPilotProjectsPage()throws InterruptedException, IOException, Exception{
			try {
			
				System.out.println("---------testWordPressComponentsAddValidPilotProjectsPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidPilotProject(methodName);
			
				

				
			} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 
	
	
	@AfterTest
	public  void tearDown() {
		baseScreen.closeBrowser();
	}

	}

	
	