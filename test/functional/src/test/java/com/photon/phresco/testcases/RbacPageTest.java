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

public class RbacPageTest {
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
		methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		baseScreen.validLoginAdminUI(methodName);
		//baseScreen.customerSelection(methodName);

		
	} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void testRBACMechanism()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testRBACVerification()-------------");
		methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		baseScreen.RBAC(methodName);
		
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void testServiceAdmin()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------ServiceAdminVerification()-------------");
		methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		baseScreen.RBACServiceAdmin(methodName);
		
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 
 @Test
 public void testRBACAssignPermissionMultiple()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------RBACServiceAssignPermissionmultiple()-------------");
		methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		baseScreen.RBACServiceAssignPermissionMultiple(methodName);
		
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 
 
 @Test
 public void testRBACAssignPermissionSingle()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------RBACServiceAssignPermissionSingle()-------------");
		methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		baseScreen.RBACServiceAssignPermissionSingle(methodName);
		
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 
 @Test
 public void testDeleteAssignPermissionSingle()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------ServiceAdminDeleteSingle-------------");
		methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		baseScreen.ServiceAdminDeleteSingle(methodName);
		
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 
 @Test
 public void testDeleteAssignPermissionMultiple()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------ServiceAdminDeleteMultiple)-------------");
		methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		baseScreen.ServiceAdminDeleteMultiple(methodName);
		
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void testServiceAdminVerification()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------ServiceAdminVerification-------------");
		methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		baseScreen.ServiceAdminVeification(methodName);
		
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void testProjectAdminVerification()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("--------ProjectAdminVeification-------------");
		methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		baseScreen.ProjectAdminVeification(methodName);
		
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void testViewServiceVerification()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------viewServiceVeification-------------");
		methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		baseScreen.viewServiceVeification(methodName);
		
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void testModuleAdminVerification()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------ModuleAdminVeification-------------");
		methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		baseScreen.ModuleAdminVeification(methodName);
		
		
		} catch (Exception t) {
		t.printStackTrace();

	}
 }
 
 @AfterTest
 public  void tearDown() {
	baseScreen.closeBrowser();
 }

}