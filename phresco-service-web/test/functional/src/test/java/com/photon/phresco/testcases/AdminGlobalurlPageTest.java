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

public class AdminGlobalurlPageTest {
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

		System.out.println("---------testLoginAdminUI()-------------");
		methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		baseScreen.validLoginAdminUI(methodName);
		baseScreen.customerSelection(methodName);

		
	} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void testCreateAdminGlobalUrl()throws InterruptedException, IOException, Exception{
	try {

		System.out.println("---------testReusableComponentsAddAndroidHybridModulesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.validLoginAdminUI(methodName);
		baseScreen.CreateAdminGlobalUrl(methodName);
		
		} catch (Exception t) {
		t.printStackTrace();	

	}
 }
 

@Test
public void testAndroidHybridCreateMobileLayerArchetypes()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateMobileLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createMobileLayerArchetypes(methodName);
	
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
}

@Test
public void testAndroidLibraryCreateMobileLayerArchetypes()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createMobileLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testAndroidNativeCreateMobileLayerArchetypes()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createMobileLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testDOTNETCreateApplicationLayerArchetypesPage()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createApplicationLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
}

@Test
public void testBlackBerryHybridCreateMobileLayerArchetypes()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createMobileLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testDrupal6CreateApplicationLayerArchetypesPage()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createApplicationLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testDrupal7CreateApplicationLayerArchetypesPage()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createApplicationLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testJQueryMobileCreateWebLayerArchetypesPage()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createWebLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testMultiJQueryCreateWebLayerArchetypesPage()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createWebLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testMultiYUICreateWebLayerArchetypesPage()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createWebLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testYUIMobileCreateWebLayerArchetypesPage()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createWebLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testiPhoneHybridCreateMobileLayerArchetypes()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.selectArchetypes(methodName);
		baseScreen.createMobileLayerArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testiPhoneLibraryCreateMobileLayerArchetypes()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createMobileLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
}
@Test
public void testiphoneNativeCreateMobileLayerArchetypes()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createMobileLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
}
 @Test
 public void testJavaStandaloneCreateApplicationLayerArchetypesPage()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createApplicationLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void testJavaWebServiceCreateApplicationLayerArchetypesPage()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createApplicationLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void testNodejsCreateApplicationLayerArchetypesPage()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createApplicationLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void testPHPCreateApplicationLayerArchetypesPage()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createApplicationLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void testSharePointCreateApplicationLayerArchetypesPage()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createApplicationLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void testSiteCoreCreateApplicationLayerArchetypesPage()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createApplicationLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
 }

 @Test
 public void testWindowsMetroCreateMobileLayerArchetypes()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createMobileLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void testWindowsPhoneCreateMobileLayerArchetypes()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createMobileLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
 }
 @Test
 public void testWordPressCreateApplicationLayerArchetypesPage()
		throws InterruptedException, IOException, Exception {
	try {

		System.out
				.println("---------testPHPCreateApplicationLayerArchetypesPage()-------------");
		methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		baseScreen.createApplicationLayerArchetypes(methodName);
		baseScreen.selectArchetypes(methodName);
		baseScreen.deleteCreatedArchetype(methodName);
	
	} catch (Exception t) {
		t.printStackTrace();

	}
 }

 
 
@AfterTest
public  void tearDown() {
	baseScreen.closeBrowser();
}
}
