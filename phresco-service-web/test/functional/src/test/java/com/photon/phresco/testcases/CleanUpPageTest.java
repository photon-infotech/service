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

public class CleanUpPageTest {
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

			System.out.println("---------testLoginAdminUI()-------------");
			String methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
				baseScreen.validLoginAdminUI(methodName);
				baseScreen.customerSelection(methodName);
				//baseScreen.deleteArchetypes(methodName);

			
		} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	@Test
	public void testDeleteCreatedArchetypes()
			throws InterruptedException, IOException, Exception {
		try {

			System.out.println("---------testDeleteArchetypes()-------------");
			String	methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.selectArchetypes(methodName);
			baseScreen.deleteCreatedArchetype(methodName);
			
		} catch (Exception t) {
			t.printStackTrace();

		}
	}
	
	
	@Test
	 public void testdeleteCustomer()throws InterruptedException, IOException, Exception{
		try {

			System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
			String methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			/*baseScreen.validLoginAdminUI(methodName);*/
			baseScreen.deleteCustomer(methodName);
			
			} catch (Exception t) {
			t.printStackTrace();

		}
	 }
	
	@Test
	public void testDeleteCreatedApplicationtypes()
			throws InterruptedException, IOException, Exception {
		try {

			System.out.println("---------testDeleteApplicationtypes()-------------");
			String	methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.selectApplicationtypes(methodName);
			baseScreen.deleteCreatedSamples(methodName);
			
		} catch (Exception t) {
			t.printStackTrace();

		}
	}
	@Test
	public void testDeleteCreatedConfigurationTemplates()
			throws InterruptedException, IOException, Exception {
		try {

			System.out.println("---------testDeleteConfigTemplates()-------------");
			String	methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.selectConfigurationTemplates(methodName);
			baseScreen.deleteCreatedSamples(methodName);
			
		} catch (Exception t) {
			t.printStackTrace();

		}
	}
	@Test
	public void testDeleteCreatedPilotProject()
			throws InterruptedException, IOException, Exception {
		try {

			System.out.println("---------testDeletePilotProjects()-------------");
			String	methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.selectPilotProjects(methodName);
			baseScreen.deleteCreatedSamples(methodName);
			
		} catch (Exception t) {
			t.printStackTrace();

		}
	}
	@Test
	public void testDeleteCreatedDownload()
			throws InterruptedException, IOException, Exception {
		try {

			System.out.println("---------testDeleteDownload()-------------");
			String	methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.selectDownload(methodName);
			baseScreen.deleteCreatedSamples(methodName);
			
		} catch (Exception t) {
			t.printStackTrace();

		}
	}
	@Test
	 public void testDeleteAdminGlobalUrl()throws InterruptedException, IOException, Exception{
			try {

				System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
				String methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.selectGlobalurl(methodName);
				baseScreen.DeleteAdminGlobalUrl(methodName);
				
				} catch (Exception t) {
				t.printStackTrace();

			}
		 }
	
	@AfterTest
	public  void tearDown() {
		baseScreen.closeBrowser();
	}
}





