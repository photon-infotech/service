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

public class DownloadsPageTest {
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
	public void testDrupal6ComponentsAddValidDownloadPage()throws InterruptedException, IOException, Exception{
		try {
			
			System.out
					.println("---------testComponentsAddValidDownloadPage()-------------");
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
			baseScreen.addValidDownload(methodName);
		
			

			
		} catch (Exception t) {
			t.printStackTrace();

		}
	}
	 @Test
		public void testComponentsAddValidDownloadPage()throws InterruptedException, IOException, Exception{
			try {
				
				System.out
						.println("---------testComponentsAddValidDownloadPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidDownload(methodName);
			  
			} catch (Exception t) {
				t.printStackTrace();

			}
		}
	 @Test
		public void testDrupal7ComponentsAddValidDownloadPage()throws InterruptedException, IOException, Exception{
			try {
				
				System.out
						.println("---------testComponentsAddValidDownloadPage()-------------");
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
				baseScreen.addValidDownload(methodName);
			  
			} catch (Exception t) {
				t.printStackTrace();

			}
		}


	@AfterTest
	public  void tearDown() {
		baseScreen.closeBrowser();
	}


}