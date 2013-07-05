package com.photon.phresco.Screens;

import java.awt.AWTException;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.sourceforge.htmlunit.corejs.javascript.ast.ThrowStatement;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.internal.selenesedriver.SendKeys;
import org.openqa.selenium.internal.seleniumemulation.FireEvent;
import org.openqa.selenium.internal.seleniumemulation.IsElementPresent;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import org.openqa.selenium.server.commands.CaptureNetworkTrafficCommand;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.google.common.base.Function;
import com.photon.phresco.selenium.util.Constants;
import com.photon.phresco.selenium.util.GetCurrentDir;
import com.photon.phresco.selenium.util.ScreenException;
import com.photon.phresco.uiconstants.AdminUIData;
import com.photon.phresco.uiconstants.PhrescoUiConstants;
import com.photon.phresco.uiconstants.UIConstants;
import com.photon.phresco.uiconstants.UserInfoConstants;


public class BaseScreen {

	private WebDriver driver;
	private ChromeDriverService chromeService;
	private Log log = LogFactory.getLog("BaseScreen");
	private WebElement element;	
	private AdminUIData adminUIConstants;
	private UIConstants uiConstants;
	private UserInfoConstants userInfoConstants;
	private PhrescoUiConstants phrscEnv;
	private final long TIMEOUT=120;
	//DesiredCapabilities capabilities;
	public String resolution  = null;
	

	// private Log log = LogFactory.getLog(getClass());

	public BaseScreen() {

	}


	public BaseScreen(String selectedBrowser, String selectedPlatform, String applicationURL,
			String applicatinContext,PhrescoUiConstants phrscEnv, UIConstants uiConstants, UserInfoConstants userInfoConstants,
			AdminUIData adminUIConstants) throws ScreenException, MalformedURLException {
		this.phrscEnv=phrscEnv;
		this.userInfoConstants = userInfoConstants;
		this.uiConstants = uiConstants;
		this.adminUIConstants = adminUIConstants;

		instantiateBrowser(selectedBrowser,selectedPlatform, applicationURL, applicatinContext);

	}

	public void instantiateBrowser(String selectedBrowser,String selectedPlatform,
			String applicationURL, String applicationContext)
					throws ScreenException, MalformedURLException {
		
	
		if (selectedBrowser.equalsIgnoreCase(Constants.BROWSER_CHROME)) {
			try {
				// "D:/Selenium-jar/chromedriver_win_19.0.1068.0/chromedriver.exe"
				chromeService = new ChromeDriverService.Builder()
						.usingDriverExecutable(
								new File(getChromeLocation()))
						.usingAnyFreePort().build();	
				
				log.info("-------------***LAUNCHING GOOGLECHROME***--------------");	
				
				driver=new ChromeDriver(chromeService);
				//driver.manage().window().maximize();
			//	driver = new ChromeDriver(chromeService, chromeOption);
				// driver.manage().timeouts().implicitlyWait(30,
				// TimeUnit.SECONDS);				
				//driver.navigate().to(applicationURL + applicationContext);
				driver.manage().window().maximize();
				driver.navigate().to(applicationURL+applicationContext);
			


			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (selectedBrowser.equalsIgnoreCase(Constants.BROWSER_IE)) {
			log.info("---------------***LAUNCHING INTERNET EXPLORE***-----------");
			driver = new InternetExplorerDriver();
			driver.manage().window().maximize();
			driver.navigate().to(applicationURL + applicationContext);

		} else if (selectedBrowser.equalsIgnoreCase(Constants.BROWSER_FIREFOX)) {
			log.info("-------------***LAUNCHING FIREFOX***--------------");
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
			driver.navigate().to(applicationURL + applicationContext);

		}
		else if (selectedBrowser.equalsIgnoreCase(Constants.BROWSER_OPERA)) {
			log.info("-------------***LAUNCHING OPERA***--------------");
			System.out.println("******entering window maximize********");
			try {
				System.out.println("******entering window maximize********");
				  Robot robot; try { robot = new Robot();
				  robot.keyPress(KeyEvent.VK_ALT);
				  robot.keyPress(KeyEvent.VK_SPACE);
				  robot.keyRelease(KeyEvent.VK_ALT);
				  robot.keyRelease(KeyEvent.VK_SPACE);
				  robot.keyPress(KeyEvent.VK_X); robot.keyRelease(KeyEvent.VK_X); }
				  catch (AWTException e) {
				  
				  e.printStackTrace(); }

			} catch (Exception e) {

				e.printStackTrace();
			}

		}else {
			throw new ScreenException(
					"------Only FireFox,InternetExplore and Chrome works-----------");
		}
			
		}

	public void setBrowserResolution() {
		resolution = this.phrscEnv.RESOLUTION;
		if (resolution != null) {
			String[] tokens = resolution.split("x");
			String resolutionX = tokens[0];
			String resolutionY = tokens[1];
			int Xpath = Integer.parseInt(resolutionX);
			int Ypath = Integer.parseInt(resolutionY);
			Dimension screenResolution = new Dimension(Xpath, Ypath);
			driver.manage().window().setSize(screenResolution);
		} else {
			driver.manage().window().maximize();
		}
	}

	public void closeBrowser() {
		log.info("-------------***BROWSER CLOSING***--------------");
		if (driver != null) {
			driver.quit();
			if (chromeService != null) {
			}
		}
	}

	public String getChromeLocation() {
		log.info("getChromeLocation:*****CHROME TARGET LOCATION FOUND***");
		String directory = System.getProperty("user.dir");
		String targetDirectory = getChromeFile();
		String location = directory + targetDirectory;
		return location;
	}

	public String getChromeFile() {
		if (System.getProperty("os.name").startsWith(Constants.WINDOWS_OS)) {
			log.info("*******WINDOWS MACHINE FOUND*************");
			return Constants.WINDOWS_DIRECTORY + "/chromedriver.exe";
		} else if (System.getProperty("os.name").startsWith(Constants.LINUX_OS)) {
			log.info("*******LINUX MACHINE FOUND*************");
			return Constants.LINUX_DIRECTORY_64 + "/chromedriver";
		} else if (System.getProperty("os.name").startsWith(Constants.MAC_OS)) {
			log.info("*******MAC MACHINE FOUND*************");
			return Constants.MAC_DIRECTORY + "/chromedriver";
		} else {
			throw new NullPointerException("******PLATFORM NOT FOUND********");
		}

	}

	public WebElement getXpathWebElement(String xpath) throws Exception {
		log.info("Entering:-----getXpathWebElement-------");
		try {

			element = driver.findElement(By.xpath(xpath));

		} catch (Throwable t) {
			log.info("Entering:---------Exception in getXpathWebElement()-----------");
			t.printStackTrace();

		}
		return element;

	}

	public void getIdWebElement(String id) throws ScreenException {
		log.info("Entering:---getIdWebElement-----");
		try {
			element = driver.findElement(By.id(id));

		} catch (Throwable t) {
			log.info("Entering:---------Exception in getIdWebElement()----------");
			t.printStackTrace();

		}

	}

	public void getcssWebElement(String selector) throws ScreenException {
		log.info("Entering:----------getIdWebElement----------");
		try {
			element = driver.findElement(By.cssSelector(selector));

		} catch (Throwable t) {
			log.info("Entering:---------Exception in getIdWebElement()--------");

			t.printStackTrace();

		}

	}

	public void waitForElementPresent(String locator, String methodName)
			throws Exception {
		try {
			By by=null;
			log.info("Entering:--------waitForElementPresent()--------");

			if(locator.startsWith("//")){
				log.info("Entering:--------Xpath checker--------");
				by = By.xpath(locator);	
			}else{
				log.info("Entering:--------Non-Xpath checker----------------");
				by=By.id(locator);
			}

			WebDriverWait wait = new WebDriverWait(driver, 20);			
			wait.until(presenceOfElementLocated(by));

		}

		catch (Exception e) {
			log.info("Entering:------presenceOfElementLocated()-----End"+"--("+ locator +")--");
			try{
				System.out.println("--------------Time out for Finding a text");
			}
                    catch(Exception p)
                    {
                    	p.printStackTrace();
                    }
		}
	}
	/*public void waitForTextPresent(String text) throws IOException, Exception{
		try{
		log.info("Entering:*********waitForElementPresent()******");
	    //By by= By.linkText(locator);
		WebDriverWait wait=new WebDriverWait(driver, TIMEOUT);
		driver.findElement(By.tagName(text)).getText();
		log.info("Waiting:*************One second***********");
		//wait.until(presenceOfElementLocated(by));
		}catch(Exception e){
			e.printStackTrace();
			//ScreenCapturer();

		}


	}
	 */
	public boolean waitForTextPresent(String text) throws InterruptedException, ScreenException {

		if (text!= null){

			for(int i=0;i<40;i++){
				System.out.println("--for loop---");
				if(driver.findElement(By.tagName("body")).getText().contains(text)){
					break; 
				}else{
					if(i==39){
						throw new RuntimeException("---- Time out for finding the Text----");
					}
					System.out.println("-------wating for 1");
					Thread.sleep(1000);
				}
			}


		}
		else
		{
			throw new RuntimeException("---- Text not existed----");
		}

		return true;

	}    



	Function<WebDriver, WebElement> presenceOfElementLocated(final By locator) {
		log.info("Entering:------presenceOfElementLocated()-----Start");
		return new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {	
				log.info("Entering:*********presenceOfElementLocated()******End");
				return driver.findElement(locator);

			}

		};

	}
	public void currenDirectory()
	{
		File dir1 = new File(".");
		File dir2 = new File("..");
		try {
			System.out.println("Current dir : " + dir1.getCanonicalPath());
			System.out.println("Parent  dir : " + dir2.getCanonicalPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/*
	..........Empty Login AdminUI scenario........
	public  void emptyLoginAdminUI(String methodName) throws Exception {
		 if (StringUtils.isEmpty(methodName)) {
           methodName = Thread.currentThread().getStackTrace()[1]
                           .getMethodName();
   }
	    	log.info("@testLoginPage::******executing emptyLoginAdminUI scenario****");
			try {
			Thread.sleep(2000);
				element=getXpathWebElement(uiConstants.LOGIN_USERNAME);
				waitForElementPresent(uiConstants.LOGIN_USERNAME,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.LOGIN_PASSWORD);
				waitForElementPresent(uiConstants.LOGIN_PASSWORD,methodName);
				click();
				Thread.sleep(1000);

			    element=getXpathWebElement(uiConstants.LOGIN_BUTTON);
				waitForElementPresent(uiConstants.LOGIN_BUTTON,methodName);
				click();
				Thread.sleep(3000);

				isTextPresent(adminUIConstants.EMPTY_ERROR_MSG_LOGIN);

			} catch (InterruptedException e) {

				e.printStackTrace();
			}		
	 }

	..........Invalid Login AdminUI scenario........
	 public  void invalidLoginAdminUI(String methodName) throws Exception {
		 if (StringUtils.isEmpty(methodName)) {
           methodName = Thread.currentThread().getStackTrace()[1]
                           .getMethodName();
   }
	    	log.info("@testLoginPage::******executing invalidLoginAdminUI scenario****");
			try {
					Thread.sleep(2000);		
				element=getXpathWebElement(uiConstants.LOGIN_USERNAME);
				waitForElementPresent(uiConstants.LOGIN_USERNAME,methodName);
				type(adminUIConstants.INVALID_LOGIN_USERNAME);
				Thread.sleep(1000);

			    element=getXpathWebElement(uiConstants.LOGIN_PASSWORD);
			    waitForElementPresent(uiConstants.LOGIN_PASSWORD,methodName);
				type(adminUIConstants.INVALID_LOGIN_PASSWORD);
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.LOGIN_BUTTON);
			    waitForElementPresent(uiConstants.LOGIN_BUTTON,methodName);
				click();			
				Thread.sleep(3000);


				//isTextPresent("Phresco");


			} catch (InterruptedException e) {

				e.printStackTrace();
			}		
	 }*/
	/*.......... Valid Login AdminUI scenario........*/
	public  void validLoginAdminUI(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testLoginPage::******executing validLoginAdminUI scenario****");
		try {
			Thread.sleep(5000);		
			element=getXpathWebElement(uiConstants.LOGIN_USERNAME);
			waitForElementPresent(uiConstants.LOGIN_USERNAME,methodName);
			type(userInfoConstants.USERNAME);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.LOGIN_PASSWORD);
			waitForElementPresent(uiConstants.LOGIN_PASSWORD,methodName);
			type(userInfoConstants.PASSWORD);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.LOGIN_BUTTON);
			waitForElementPresent(uiConstants.LOGIN_BUTTON,methodName);
			click();
			Thread.sleep(10000);

		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}
	/*...........Dashboard AdminUI Scenario.......*/

	public  void dashboardLink(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testDashboardPage::******Executing dashboardLink scenario****");
		try {
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DADHBOARD_LINK);
			waitForElementPresent(uiConstants.DADHBOARD_LINK,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MOST_USED_ARCHETYPE);
			waitForElementPresent(uiConstants.MOST_USED_ARCHETYPE,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MOST_USED_ARCHETYPE);
			waitForElementPresent(uiConstants.MOST_USED_ARCHETYPE,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MOST_USED_ARCHETYPE);
			waitForElementPresent(uiConstants.MOST_USED_ARCHETYPE,methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.MOST_USED_FEATURE);
			waitForElementPresent(uiConstants.MOST_USED_FEATURE,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MOST_USED_FEATURE);
			waitForElementPresent(uiConstants.MOST_USED_FEATURE,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MOST_USED_FEATURE);
			waitForElementPresent(uiConstants.MOST_USED_FEATURE,methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.MOST_USED_PILOT_PROJECT);
			waitForElementPresent(uiConstants.MOST_USED_PILOT_PROJECT,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MOST_USED_PILOT_PROJECT);
			waitForElementPresent(uiConstants.MOST_USED_PILOT_PROJECT,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MOST_USED_PILOT_PROJECT);
			waitForElementPresent(uiConstants.MOST_USED_PILOT_PROJECT,methodName);
			click();
			Thread.sleep(3000);

		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}

	/*public  void addEmptyModules(String methodName) throws Exception {
			 if (StringUtils.isEmpty(methodName)) {
	             methodName = Thread.currentThread().getStackTrace()[1]
	                             .getMethodName();
	     }
		    	log.info("@testReusableComponentsAddEmptyModulesPage::******executing addEmptyModules scenario****");
				try {
					Thread.sleep(2000);
					element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
					waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
					click();
					Thread.sleep(1000);

					element=getXpathWebElement(uiConstants.REASUABLE_COMPONENT_TAB);
					waitForElementPresent(uiConstants.REASUABLE_COMPONENT_TAB,methodName);
					click();
					Thread.sleep(1000);

					element=getXpathWebElement(uiConstants.REASUABLE_COMPONENT_MODULES_TAB);
					waitForElementPresent(uiConstants.REASUABLE_COMPONENT_MODULES_TAB,methodName);
					click();
					Thread.sleep(1000);

					element=getXpathWebElement(uiConstants.ADD_MODULES_BUTTON);
					waitForElementPresent(uiConstants.ADD_MODULES_BUTTON,methodName);
					click();
					Thread.sleep(1000);
					Thread.sleep(5000);


					element=getXpathWebElement(uiConstants.MODULES_NAME);
					waitForElementPresent(uiConstants.MODULES_NAME,methodName);
					click();
					Thread.sleep(1000);

					element=getXpathWebElement(uiConstants.MODULE_DESCRIPTION);
					waitForElementPresent(uiConstants.MODULE_DESCRIPTION,methodName);
					click();
					Thread.sleep(1000);

					element=getXpathWebElement(uiConstants.MODULE_HELPTEXT);
					waitForElementPresent(uiConstants.MODULE_HELPTEXT,methodName);
					click();
					Thread.sleep(1000);

					element=getXpathWebElement(uiConstants.MODULE_SAVE_BUTTON);
					waitForElementPresent(uiConstants.MODULE_SAVE_BUTTON, methodName);
					click();
					Thread.sleep(1000);

					isTextPresent(adminUIConstants.EMPTY_ADD_MODULE_NAME_MSG);
					isTextPresent(adminUIConstants.EMPTY_ADD_MODULE_LICENSE_TYPE);
					isTextPresent(adminUIConstants.EMPTY_ADD_MODULE_UPLOAD_FILE_MSG);

					Thread.sleep(1000);
					element=getXpathWebElement(uiConstants.MODULE_CANCEL_BUTTON);
					waitForElementPresent(uiConstants.MODULE_CANCEL_BUTTON, methodName);
					click();
					Thread.sleep(1000);


				} catch (InterruptedException e) {

					e.printStackTrace();
				}		
		 }

		public  void addInvalidModules(String methodName) throws Exception {
			 if (StringUtils.isEmpty(methodName)) {
	             methodName = Thread.currentThread().getStackTrace()[1]
	                             .getMethodName();
	     }
		    	log.info("@testReusableComponentsAddInvalidModulesPage::******executing addInvalidModules scenario****");
				try {
					Thread.sleep(2000);
					element=getXpathWebElement(uiConstants.ADD_MODULES_BUTTON);
					waitForElementPresent(uiConstants.ADD_MODULES_BUTTON,methodName);
					click();
					Thread.sleep(1000);

					element=getXpathWebElement(uiConstants.MODULES_NAME);
					waitForElementPresent(uiConstants.MODULES_NAME,methodName);
					type(adminUIConstants.ADD_MODULE_NAME_VALUE);
					Thread.sleep(1000);

					element=getXpathWebElement(uiConstants.MODULE_DESCRIPTION);
					waitForElementPresent(uiConstants.MODULE_DESCRIPTION,methodName);
					type(adminUIConstants.ADD_MODULE_DESCRIPTION_VALUE);
					Thread.sleep(1000);

					element=getXpathWebElement(uiConstants.MODULE_HELPTEXT);
					waitForElementPresent(uiConstants.MODULE_HELPTEXT,methodName);
					type(adminUIConstants.ADD_MODULE_HELPTEXT_VALUE);
					Thread.sleep(1000);

					element=getXpathWebElement(uiConstants.MODULE_TECHNOLOGY);
					waitForElementPresent(uiConstants.MODULE_TECHNOLOGY, methodName);
					click();
					Thread.sleep(1000);

					type(adminUIConstants.ADD_MODULE_TECHNOLOGY_VALUE);
					click();
					Thread.sleep(3000);

					element=getXpathWebElement(uiConstants.MODULE_MODULETYPE);
					waitForElementPresent(uiConstants.MODULE_MODULETYPE, methodName);
					click();
					type(adminUIConstants.ADD_MODULE_TYPE_CUSTOM_MODULE_VALUE);
					click();
					Thread.sleep(3000);

					element=getXpathWebElement(uiConstants.MODULE_LICENSE_TYPE);
					waitForElementPresent(uiConstants.MODULE_LICENSE_TYPE, methodName);
					click();
					type(adminUIConstants.ADD_MODULE_LICENSE_TYPE_VALUE);
					click();
					Thread.sleep(2000);


					//adminUIConstants.INVALID_CREATE_MODULE_UPLOAD_FILE_NAME;


					element=getXpathWebElement(uiConstants.MODULE_SELECT_DEPENDENCY);
					waitForElementPresent(uiConstants.MODULE_SELECT_DEPENDENCY, methodName);
					click();
					Thread.sleep(3000);

					element=getXpathWebElement(uiConstants.MODULE_DEPENDENCY_LOGIN_CHECKBOX);
					waitForElementPresent(uiConstants.MODULE_DEPENDENCY_LOGIN_CHECKBOX, methodName);
					click();

					element=getXpathWebElement(uiConstants.MODULE_DEPENDENCY_OK_BUTTON);
					waitForElementPresent(uiConstants.MODULE_DEPENDENCY_OK_BUTTON, methodName);
					click();
					Thread.sleep(1000);

					element=getXpathWebElement(uiConstants.MODULE_SAVE_BUTTON);
					waitForElementPresent(uiConstants.MODULE_SAVE_BUTTON, methodName);
					click();
					Thread.sleep(1000);


					isTextPresent(adminUIConstants.EMPTY_ADD_MODULE_UPLOAD_FILE_MSG);
					Thread.sleep(1000);

					element=getXpathWebElement(uiConstants.MODULE_CANCEL_BUTTON);
					waitForElementPresent(uiConstants.MODULE_CANCEL_BUTTON, methodName);
					click();
					Thread.sleep(1000);



				} catch (InterruptedException e) {

					e.printStackTrace();
				}		
		 }

	 */ 
	public  void AddingTechnology(String methodName) throws Exception {
		try {

			if( methodName == "testPHPReusableComponentsAddModulesPage" || methodName == "testPHPReusableComponentsAddJSlibrariesPage" || methodName == "testPHPReusableComponentsAddvalidComponentPage"|| methodName == "testPHPComponentsAddConfigurationTemplatePage" || methodName == "testPHPReusableComponentsAddExternalModules"
					|| methodName == "testAndroidHybridCompAddConfigTemplateNumber")
			{
				element=getXpathWebElement(uiConstants.ADD_MODULE_PHP_TECHNOLOGY_VALUE);
				waitForElementPresent(uiConstants.ADD_MODULE_PHP_TECHNOLOGY_VALUE,methodName);
				click();
				System.out.println("--------------PHP Selected----------");

			}
			else if( methodName == "testDrupal6ReusableComponentsAddModulesPage" || methodName == "testDrupal6ReusableComponentsAddJSlibrariesPage" || methodName == "testDrupal6ReusableComponentsAddvalidComponentPage"|| methodName == "testDrupal6ComponentsAddConfigurationTemplatePage" || methodName == "testDrupal6ReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_DRUPAL6_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_DRUPAL6_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testDrupal7ReusableComponentsAddModulesPage" || methodName == "testDrupal7ReusableComponentsAddJSlibrariesPage" || methodName == "testDrupal7ReusableComponentsAddvalidComponentPage"|| methodName == "testDrupal7ComponentsAddConfigurationTemplatePage" || methodName == "testDrupal7ReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_DRUPAL7_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_DRUPAL7_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testWordPressReusableComponentsAddModulesPage" || methodName == "testWordPressReusableComponentsAddJSlibrariesPage" || methodName == "testWordPressReusableComponentsAddvalidComponentPage"|| methodName == "testWordPressComponentsAddConfigurationTemplatePage" || methodName == "testWordPressReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_WORDPRESS_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_WORDPRESS_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testMultiYUIReusableComponentsAddModulesPage" || methodName == "testMultiYUIReusableComponentsAddJSlibrariesPage" || methodName == "testMultiYUIReusableComponentsAddvalidComponentPage"|| methodName == "testMultiYUIComponentsAddConfigurationTemplatePage" || methodName == "testMultiYUIReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_MULTIYUI_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_MULTIYUI_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testMultiJQueryReusableComponentsAddModulesPage" || methodName == "testMultiJQueryReusableComponentsAddJSlibrariesPage" || methodName == "testMultiJQueryReusableComponentsAddvalidComponentPage"|| methodName == "testMultiJQueryComponentsAddConfigurationTemplatePage" || methodName == "testMultiJQueryReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_MULTIJQUERY_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_MULTIJQUERY_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testYUIMobileReusableComponentsAddModulesPage" || methodName == "testYUIMobileReusableComponentsAddJSlibrariesPage" || methodName == "testYUIMobileReusableComponentsAddvalidComponentPage"|| methodName == "testYUIMobileComponentsAddConfigurationTemplatePage" || methodName == "testYUIMobileReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_YUIMOBILE_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_YUIMOBILE_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testJQueryMobileReusableComponentsAddModulesPage" || methodName == "testJQueryMobileReusableComponentsAddJSlibrariesPage" || methodName == "testJQueryMobileReusableComponentsAddvalidComponentPage"|| methodName == "testJQueryMobileComponentsAddConfigurationTemplatePage" || methodName == "testJQueryMobileReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_JQUERYMOBILE_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_JQUERYMOBILE_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testDOTNETReusableComponentsAddModulesPage" || methodName == "testDOTNETReusableComponentsAddJSlibrariesPage" || methodName == "testDOTNETReusableComponentsAddvalidComponentPage"|| methodName == "testDOTNETComponentsAddConfigurationTemplatePage" || methodName == "testDOTNETReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_DOTNET_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_DOTNET_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testSharePointReusableComponentsAddModulesPage" || methodName == "testSharePointReusableComponentsAddJSlibrariesPage" || methodName == "testSharePointReusableComponentsAddvalidComponentPage"|| methodName == "testSharePointComponentsAddConfigurationTemplatePage" || methodName == "testSharePointReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_SHAREPOINT_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_SHAREPOINT_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testSiteCoreReusableComponentsAddModulesPage" || methodName == "testSiteCoreReusableComponentsAddJSlibrariesPage" || methodName == "testSiteCoreReusableComponentsAddvalidComponentPage"|| methodName == "testSiteCoreComponentsAddConfigurationTemplatePage" || methodName == "testSiteCoreReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_SITECORE_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_SITECORE_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testNodejsReusableComponentsAddModulesPage" || methodName == "testNodejsReusableComponentsAddJSlibrariesPage" || methodName == "testNodejsReusableComponentsAddvalidComponentPage"|| methodName == "testNodejsComponentsAddConfigurationTemplatePage" || methodName == "testNodejsReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_NODEJS_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_NODEJS_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testJavaStandaloneReusableComponentsAddModulesPage" || methodName == "testJavaStandaloneReusableComponentsAddJSlibrariesPage" || methodName == "testJavaStandaloneReusableComponentsAddvalidComponentPage"|| methodName == "testJavaStandaloneComponentsAddConfigurationTemplatePage" || methodName == "testJavaStandaloneReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_JAVASTANDALONE_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_JAVASTANDALONE_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testJavaWebServiceReusableComponentsAddModulesPage" || methodName == "testJavaWebServiceReusableComponentsAddJSlibrariesPage" || methodName == "testJavaWebServiceReusableComponentsAddvalidComponentPage"|| methodName == "testJavaWebServiceComponentsAddConfigurationTemplatePage" || methodName == "testJavaWebServiceReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_JAVAWEBSERVICE_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_JAVAWEBSERVICE_TECHONOLGY_VALUE,methodName);
				click();
			}

			else if( methodName == "testiPhoneHybridReusableComponentsAddModulesPage" || methodName == "testiPhoneHybridReusableComponentsAddJSlibrariesPage" || methodName == "testiPhoneHybridReusableComponentsAddvalidComponentPage"|| methodName == "testiPhoneHybridComponentsAddConfigurationTemplatePage" || methodName == "testiPhoneHybridReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_IPHONEHYBRID_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_IPHONEHYBRID_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testiphoneNativeReusableComponentsAddModulesPage" || methodName == "testiphoneNativeReusableComponentsAddJSlibrariesPage" || methodName == "testiphoneNativeReusableComponentsAddvalidComponentPage"|| methodName == "testiphoneNativeComponentsAddConfigurationTemplatePage" || methodName == "testiphoneNativeReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_IPHONENATIVE_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_IPHONENATIVE_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testiPhoneLibraryReusableComponentsAddModulesPage" || methodName == "testiPhoneLibraryReusableComponentsAddJSlibrariesPage" || methodName == "testiPhoneLibraryReusableComponentsAddvalidComponentPage"|| methodName == "testiPhoneLibraryComponentsAddConfigurationTemplatePage" || methodName == "testiPhoneLibraryReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_IPHONELIBRARY_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_IPHONELIBRARY_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testiPhoneWorkSpaceReusableComponentsAddModulesPage" || methodName == "testiPhoneWorkSpaceReusableComponentsAddJSlibrariesPage" || methodName == "testiPhoneWorkSpaceReusableComponentsAddvalidComponentPage"|| methodName == "testiPhoneWorkSpaceComponentsAddConfigurationTemplatePage" || methodName == "testiPhoneWorkSpaceReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_IPHONEWORKSPACE_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_IPHONEWORKSPACE_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testAndroidHybridReusableComponentsAddModulesPage" || methodName == "testAndroidHybridReusableComponentsAddJSlibrariesPage" || methodName == "testAndroidHybridReusableComponentsAddvalidComponentPage"|| methodName == "testAndroidHybridComponentsAddConfigurationTemplatePage"|| methodName == "testAndroidHybridReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_ANDROIDHYBRID_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_ANDROIDHYBRID_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testAndroidNativeReusableComponentsAddModulesPage" || methodName == "testAndroidNativeReusableComponentsAddJSlibrariesPage" || methodName == "testAndroidNativeReusableComponentsAddvalidComponentPage"|| methodName == "testAndroidNativeComponentsAddConfigurationTemplatePage" || methodName == "testAndroidNativeReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_ANDROIDNATIVE_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_ANDROIDNATIVE_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testAndroidLibraryReusableComponentsAddModulesPage" || methodName == "testAndroidLibraryReusableComponentsAddJSlibrariesPage" || methodName == "testAndroidLibraryReusableComponentsAddvalidComponentPage"|| methodName == "testAndroidLibraryComponentsAddConfigurationTemplatePage"|| methodName == "testAndroidLibraryReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_ANDROIDLIBRARY_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_ANDROIDLIBRARY_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testWindowsMetroReusableComponentsAddModulesPage" || methodName == "testWindowsMetroReusableComponentsAddJSlibrariesPage" || methodName == "testWindowsMetroReusableComponentsAddvalidComponentPage"|| methodName == "testWindowsMetroComponentsAddConfigurationTemplatePage" || methodName == "testWindowsMetroReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_WINDOWSMETRO_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_WINDOWSMETRO_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testWindowsPhoneReusableComponentsAddModulesPage" || methodName == "testWindowsPhoneReusableComponentsAddJSlibrariesPage" || methodName == "testWindowsPhoneReusableComponentsAddvalidComponentPage"|| methodName == "testWindowsPhoneComponentsAddConfigurationTemplatePage" || methodName == "testWindowsPhoneReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_WINDOWSPHONE_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_WINDOWSPHONE_TECHONOLGY_VALUE,methodName);
				click();
			}
			else if( methodName == "testBlackBerryHybridReusableComponentsAddModulesPage" || methodName == "testBlackBerryHybridReusableComponentsAddJSlibrariesPage" || methodName == "testBlackBerryHybridReusableComponentsAddvalidComponentPage"|| methodName == "testBlackBerryHybridComponentsAddConfigurationTemplatePage" || methodName == "testBlackBerryHybridReusableComponentsAddExternalModules")
			{
				element=getXpathWebElement(uiConstants.ADD_COMPONENT_BLACKBERRY_HYBRID_TECHONOLGY_VALUE);
				waitForElementPresent(uiConstants.ADD_COMPONENT_BLACKBERRY_HYBRID_TECHONOLGY_VALUE,methodName);
				click();
			}
		} catch (Exception e) {

			e.printStackTrace();
		}		
	} 
	public  void deleteReusableComponents(String methodName)throws Exception {
		try {

			System.out.println("----------deleteRUC starts ----------");
			if(methodName == "testDeletePHPModules"||methodName == "testDeletePHPJSLibraries"||methodName == "testDeletePHPComponents")
			{
				System.out.println("----------Technology dropdown starts----------");
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_PHP_TECHNOLOGY_VALUE).click();
				Thread.sleep(2000);
				System.out.println("----------deleteRUC Completed----------");			
			}

			else if(methodName == "testDeleteDrupal6Modules"||methodName == "testDeleteDrupal6JSLibraries"||methodName == "testDeleteDrupal6Components")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_DRUPAL6_TECHONOLGY_VALUE).click();
				
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteDrupal7Modules"||methodName == "testDeleteDrupal7JSLibraries"||methodName == "testDeleteDrupal7Components")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_DRUPAL7_TECHONOLGY_VALUE).click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteWordPressModules"||methodName == "testDeleteWordPressJSLibraries"||methodName == "testDeleteWordPressComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_WORDPRESS_TECHONOLGY_VALUE).click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteMultiYUIComponents"||methodName == "testDeleteMultiYUIJSLibraries"||methodName == "testDeleteMultiYUIComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_MULTIYUI_TECHONOLGY_VALUE).click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteMultiJQueryModules"||methodName == "testDeleteMultiJQueryJSLibraries"||methodName == "testDeleteMultiJQueryComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_MULTIJQUERY_TECHONOLGY_VALUE).click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteYUIMobileModules"||methodName == "testDeleteYUIMobileJSLibraries"||methodName == "testDeleteYUIMobileComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_YUIMOBILE_TECHONOLGY_VALUE).click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteJQueryMobileModules"||methodName == "testDeleteJQueryMobileJSLibraries"||methodName == "testDeleteJQueryMobileComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_JQUERYMOBILE_TECHONOLGY_VALUE).click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteASP_DotnetModules"||methodName == "testDeleteASP_DotnetJSLibraries"||methodName == "testDeleteASP_dotnetComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_DOTNET_TECHONOLGY_VALUE).click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteSharePointModules"||methodName == "testDeleteSharePointJSLibraries"||methodName == "testDeleteSharePointComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_SHAREPOINT_TECHONOLGY_VALUE).click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteSiteCoreModules"||methodName == "testDeleteSiteCoreJSLibraries"||methodName == "testDeleteSiteCoreComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_SITECORE_TECHONOLGY_VALUE).click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteNodejsModules"||methodName == "testDeleteNodejsJSLibraries"||methodName == "testDeleteNodejsComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_NODEJS_TECHONOLGY_VALUE).click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteJavaStandaloneModules"||methodName == "testDeleteJavaStandaloneJSLibraries"||methodName == "testDeleteJavaStandaloneComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_JAVASTANDALONE_TECHONOLGY_VALUE).click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteJavaWebServiceModules"||methodName == "testDeleteJavaWebServiceJSLibraries"||methodName == "testDeleteJavaWebServiceComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_JAVAWEBSERVICE_TECHONOLGY_VALUE).click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteiPhoneHybridModules"||methodName == "testDeleteiPhoneHybridJSLibraries"||methodName == "testDeleteiPhoneHybridComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEHYBRID_TECHONOLGY_VALUE).click();
				Thread.sleep(2000);
			}

			else if(methodName == "testDeleteiphoneNativeModules"||methodName == "testDeleteiphoneNativeJSLibraries"||methodName == "testDeleteiphoneNativeComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONENATIVE_TECHONOLGY_VALUE).click();
				Thread.sleep(2000);
			}

			else if(methodName == "testDeleteiPhoneLibraryModules"||methodName == "testDeleteiPhoneLibraryJSLibraries"||methodName == "testDeleteiPhoneLibraryComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONELIBRARY_TECHONOLGY_VALUE).click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteiPhoneWorkSpaceModules"||methodName == "testDeleteiPhoneWorkSpaceJSLibraries"||methodName == "testDeleteiPhoneWorkSpaceComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEWORKSAPCE_TECHONOLGY_VALUE).click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteAndroidHybridModules"||methodName == "testDeleteAndroidHybridJSLibraries"||methodName == "testDeleteAndroidHybridComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDHYBRID_TECHONOLGY_VALUE);
				click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteAndroidNativeModules"||methodName == "testDeleteAndroidNativeJSLibraries"||methodName == "testDeleteAndroidNativeComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDNATIVE_TECHONOLGY_VALUE);
				click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteAndroidLibraryComponents"||methodName == "testDeleteAndroidLibraryJSLibraries"||methodName == "testDeleteAndroidLibraryModules")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDLIBRARY_TECHONOLGY_VALUE);
				click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteWindowsMetroModules"||methodName == "testDeleteWindowsMetroJSLibraries"||methodName == "testDeleteWindowsMetroComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSMETRO_TECHONOLGY_VALUE);
				click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteWindowsPhoneModules"||methodName == "testDeleteWindowsPhoneJSLibraries"||methodName == "testDeleteWindowsPhoneComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSPHONE_TECHONOLGY_VALUE);
				click();
				Thread.sleep(2000);
			}
			else if(methodName == "testDeleteBlackBerryHybridModules"||methodName == "testDeleteBlackBerryHybridJSLibraries"||methodName == "testDeleteBlackBerryHybridComponents")
			{
				Thread.sleep(3000);
				getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
				getXpathWebElement(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE);
				click();
				Thread.sleep(2000);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}		
	} 

	public  void PilotProjectsAddingTechnology(String methodName)throws Exception {
		try {

			if(methodName == "testPHPComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_PHP_TECHNOLOGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_PHP_TECHNOLOGY_VALUE);
						click();*/
			}
			else if(methodName == "testDrupal6ComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_DRUPAL6_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_DRUPAL6_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testDrupal7ComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_DRUPAL7_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_DRUPAL7_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testWordPressComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_WORDPRESS_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_WORDPRESS_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testMultiYUIComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_MULTIYUI_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_MULTIYUI_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testMultiJQueryComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_MULTIJQUERY_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_MULTIJQUERY_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testYUIMobileComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_YUIMOBILE_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_YUIMOBILE_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testJQueryMobileComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_JQUERYMOBILE_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_JQUERYMOBILE_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testDOTNETComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_DOTNET_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_DOTNET_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testSharepointComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_SHAREPOINT_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_SHAREPOINT_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testSiteCoreComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_SITECORE_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_SITECORE_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testNodejsComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_NODEJS_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_NODEJS_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testJavaStandaloneComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_JAVASTANDALONE_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_JAVASTANDALONE_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testJavaWebServiceComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_JAVAWEBSERVICE_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_JAVAWEBSERVICE_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testiPhoneHybridComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_IPHONEHYBRID_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_IPHONEHYBRID_TECHONOLGY_VALUE);
						click();*/
			}

			else if(methodName == "testiphoneNativeComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_IPHONENATIVE_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_IPHONENATIVE_TECHONOLGY_VALUE);
						click();*/
			}

			else if(methodName == "testiPhoneLibraryComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_IPHONELIBRARY_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_IPHONELIBRARY_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testiPhoneWorkSpaceComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_IPHONEWORKSAPCE_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_IPHONEWORKSAPCE_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testAndroidHybridComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_ANDROIDHYBRID_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_ANDROIDHYBRID_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testAndroidNativeComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_ANDROIDNATIVE_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_ANDROIDNATIVE_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testAndroidLibraryComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_ANDROIDLIBRARY_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_ANDROIDLIBRARY_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testWindowsMetroComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_WINDOWSMETRO_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_WINDOWSMETRO_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testWindowsPhoneComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_WINDOWSPHONE_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_WINDOWSPHONE_TECHONOLGY_VALUE);
						click();*/
			}
			else if(methodName == "testBlackBerryHybridComponentsAddValidPilotProjectsPage")
			{
				element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
				selectText(element,adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE);
				/*type(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE);
						click();*/
			}

		} catch (Exception e) {

			e.printStackTrace();
		}		
	} 
	public  void ArcheTypeAddingTechnology(String methodName) throws Exception {
		try {

			if(methodName == "testPHPCreateApplicationLayerArchetypesPage" || methodName == "testDrupal6CreateApplicationLayerArchetypesPage" || methodName == "testDrupal7CreateApplicationLayerArchetypesPage"|| methodName == "testWordPressCreateApplicationLayerArchetypesPage")
			{
				type(adminUIConstants.ADD_PilotProjects_PHP_TECHNOLOGY_VALUE);
				click();
			}
			if(methodName == "testDOTNETCreateApplicationLayerArchetypesPage" || methodName == "testSharePointCreateApplicationLayerArchetypesPage" || methodName == "testSiteCoreCreateApplicationLayerArchetypesPage")
			{
				type(adminUIConstants.ADD_TECH_GROUP_DOTNET_TECHONOLGY_VALUE);
				click();              
			}
			if(methodName == "testNodejsCreateApplicationLayerArchetypesPage")
			{
				type(adminUIConstants.ADD_PilotProjects_NODEJS_TECHONOLGY_VALUE);
				click();              
			}
			if(methodName == "testJavaStandaloneCreateApplicationLayerArchetypesPage" || methodName == "testJavaWebServiceCreateApplicationLayerArchetypesPage")
			{
				type(adminUIConstants.ADD_TECH_GROUP_JAVA_TECHONOLGY_VALUE);
				click();              
			}
			if(methodName == "testiPhoneHybridCreateMobileLayerArchetypes" || methodName == "testiphoneNativeCreateMobileLayerArchetypes"|| methodName == "testiPhoneLibraryCreateMobileLayerArchetypes"|| methodName == "testiPhoneWorkSpaceCreateMobileLayerArchetypes")
			{
				type(adminUIConstants.ADD_TECH_GROUP_IPHONE_TECHONOLGY_VALUE);
				click();              
			}
			if(methodName == "testAndroidHybridCreateMobileLayerArchetypes" || methodName == "testAndroidNativeCreateMobileLayerArchetypes"|| methodName == "testAndroidLibraryCreateMobileLayerArchetypes")
			{
				type(adminUIConstants.ADD_TECH_GROUP_ANDROID_TECHONOLGY_VALUE);
				click();              
			}
			if(methodName == "testWindowsMetroCreateMobileLayerArchetypes" || methodName == "testWindowsPhoneCreateMobileLayerArchetypes")
			{
				type(adminUIConstants.ADD_TECH_GROUP_WINDOWS_TECHONOLGY_VALUE);
				click();              
			}
			if(methodName == "testBlackBerryHybridCreateMobileLayerArchetypes")
			{
				type(adminUIConstants.ADD_TECH_GROUP_BLACKBERRY_TECHONOLGY_VALUE);
				click();              
			}

		} catch (Exception e) {

			e.printStackTrace();
		}		
	} 


	public  void addModules(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testReusableComponentsAddPhpModulesPage::******executing addPhpModules scenario****");
		try {
			
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();
			Thread.sleep(3000);
   
			waitForElementPresent(uiConstants.CUSTOMER_DROPDOWN_VALUE,methodName);
			getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN_VALUE).click();
			Thread.sleep(15000);
			waitForTextPresent("Android Hybrid");
	        Thread.sleep(2000);
			
			
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENT_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENT_TAB,methodName);
			click();
			Thread.sleep(1000);
       
			try {

				if(methodName == "testDeletePHPModules" || methodName == "testPHPReusableComponentsAddModulesPage" || methodName == "testPHPReusableComponentsAddExternalModules"|| methodName == "testDeletePHPJSLibraries" || methodName =="testPHPReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_PHP_TECHNOLOGY_VALUE).click();
					Thread.sleep(2000);
				}
				if(methodName == "testDeleteDrupal6Modules" || methodName == "testDrupal6ReusableComponentsAddModulesPage" || methodName == "testDrupal6ReusableComponentsAddExternalModules" ||  methodName == "testDeleteDrupal6JSLibraries" || methodName == "testDrupal6ReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteDrupal6Components" || methodName == "testDrupal6ReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DRUPAL6_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteNodejsModules" || methodName == "testNodejsReusableComponentsAddModulesPage" || methodName == "testNodejsReusableComponentsAddExternalModules" || methodName == "testDeleteNodejsJSLibraries" || methodName == "testNodejsReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteNodejsComponents" || methodName == "testNodejsReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_NODEJS_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteJavaStandaloneModules" || methodName == "testJavaStandaloneReusableComponentsAddModulesPage" || methodName == "testJavaStandaloneReusableComponentsAddExternalModules" || methodName == "testDeleteJavaStandaloneJSLibraries" || methodName == "testJavaStandaloneReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteJavaStandaloneComponents" || methodName == "testJavaStandaloneReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_JAVASTANDALONE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteiPhoneHybridModules" || methodName == "testiPhoneHybridReusableComponentsAddModulesPage"|| methodName == "testiPhoneHybridReusableComponentsAddExternalModules"|| methodName == "testDeleteiPhoneHybridJSLibraries" || methodName == "testiPhoneHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiPhoneHybridComponents" || methodName == "testiPhoneHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);             
				}
				if(methodName == "testDeleteAndroidHybridModules" || methodName == "testAndroidHybridReusableComponentsAddModulesPage"|| methodName == "testAndroidHybridReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidHybridJSLibraries" || methodName == "testAndroidHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidHybridComponents" || methodName == "testAndroidHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWindowsMetroModules" || methodName == "testWindowsMetroReusableComponentsAddModulesPage"|| methodName == "testWindowsMetroReusableComponentsAddExternalModules" || methodName == "testDeleteWindowsMetroJSLibraries" ||methodName == "testWindowsMetroReusableComponentsAddJSlibrariesPage" || methodName =="testDeleteWindowsMetroComponents" || methodName == "testWindowsMetroReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSMETRO_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);           
				}
				if(methodName == "testDeleteBlackBerryHybridModules" ||methodName == "testBlackBerryHybridReusableComponentsAddModulesPage" || methodName == "testBlackBerryHybridReusableComponentsAddExternalModules" || methodName == "testDeleteBlackBerryHybridJSLibraries" || methodName == "testBlackBerryHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteBlackBerryHybridComponents" || methodName == "testBlackBerryHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteAndroidLibraryModules" ||methodName == "testAndroidLibraryReusableComponentsAddModulesPage" || methodName == "testAndroidLibraryReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidLibraryJSLibraries" || methodName == "testAndroidLibraryReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidLibraryComponents" || methodName == "testAndroidLibraryReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDLIBRARY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteAndroidNativeModules" ||methodName == "testAndroidNativeReusableComponentsAddModulesPage" || methodName == "testAndroidNativeReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidNativeJSLibraries" || methodName == "testAndroidNativeReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidNativeComponents" || methodName == "testAndroidNativeReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteASP_DotnetModules" ||methodName == "testDOTNETReusableComponentsAddModulesPage" || methodName == "testDOTNETReusableComponentsAddExternalModules" || methodName == "testDeleteASP_DotnetJSLibraries" || methodName == "testDOTNETReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteASP_dotnetComponents" || methodName == "testDOTNETReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DOTNET_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteDrupal7Modules" ||methodName == "testDrupal7ReusableComponentsAddModulesPage" || methodName == "testDrupal7ReusableComponentsAddExternalModules" || methodName == "testDeleteDrupal7JSLibraries" || methodName == "testDrupal7ReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteDrupal7Components" || methodName == "testDrupal7ReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DRUPAL7_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteJQueryMobileModules" ||methodName == "testJQueryMobileReusableComponentsAddModulesPage" || methodName == "testJQueryMobileReusableComponentsAddExternalModules" || methodName == "testDeleteJQueryMobileJSLibraries" || methodName == "testJQueryMobileReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteJQueryMobileComponents" || methodName == "testJQueryMobileReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_JQUERYMOBILE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteMultiJQueryModules" ||methodName == "testMultiJQueryReusableComponentsAddModulesPage" || methodName == "testMultiJQueryReusableComponentsAddExternalModules" || methodName == "testDeleteMultiJQueryJSLibraries" || methodName == "testMultiJQueryReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteMultiJQueryComponents" || methodName == "testMultiJQueryReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_MULTIJQUERY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);                 
				}
				if(methodName == "testDeleteMultiYUIModules" ||methodName == "testMultiYUIReusableComponentsAddModulesPage" || methodName == "testMultiYUIReusableComponentsAddExternalModules" || methodName == "testDeleteMultiYUIJSLibraries" || methodName == "testMultiYUIReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteMultiYUIComponents" || methodName == "testMultiYUIReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_MULTIYUI_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteYUIMobileModules" ||methodName == "testYUIMobileReusableComponentsAddModulesPage" || methodName == "testYUIMobileReusableComponentsAddExternalModules" || methodName == "testDeleteYUIMobileJSLibraries" || methodName == "testYUIMobileReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteYUIMobileComponents" || methodName == "testYUIMobileReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_YUIMOBILE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiPhoneLibraryModules" ||methodName == "testiPhoneLibraryReusableComponentsAddModulesPage" || methodName == "testiPhoneLibraryReusableComponentsAddExternalModules" || methodName == "testDeleteiPhoneLibraryJSLibraries" || methodName == "testiPhoneLibraryReusableComponentsAddJSlibrariesPage" || methodName == "testiPhoneLibraryReusableComponentsAddvalidComponentPage" || methodName == "testDeleteiPhoneLibraryComponents")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONELIBRARY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiphoneNativeModules" ||methodName == "testiphoneNativeReusableComponentsAddModulesPage" || methodName == "testiphoneNativeReusableComponentsAddExternalModules" || methodName == "testDeleteiphoneNativeJSLibraries" || methodName == "testiphoneNativeReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiphoneNativeComponents" || methodName == "testiphoneNativeReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONENATIVE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiPhoneWorkSpaceModules" ||methodName == "testiPhoneWorkSpaceReusableComponentsAddModulesPage" || methodName == "testiPhoneWorkSpaceReusableComponentsAddExternalModules" || methodName == "testDeleteiPhoneWorkSpaceJSLibraries" || methodName == "testiPhoneWorkSpaceReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiPhoneWorkSpaceComponents" || methodName == "testiPhoneWorkSpaceReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEWORKSAPCE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteSharePointModules" ||methodName == "testSharePointReusableComponentsAddModulesPage" || methodName == "testSharePointReusableComponentsAddExternalModules" || methodName == "testDeleteSharePointJSLibraries" || methodName == "testSharePointReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteSharePointComponents" || methodName == "testSharePointReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_SHAREPOINT_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteSiteCoreModules" ||methodName == "testSiteCoreReusableComponentsAddModulesPage" || methodName == "testSiteCoreReusableComponentsAddExternalModules" || methodName == "testDeleteSiteCoreJSLibraries" || methodName == "testSiteCoreReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteSiteCoreComponents" || methodName == "testSiteCoreReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_SITECORE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWindowsPhoneModules" ||methodName == "testWindowsPhoneReusableComponentsAddModulesPage" || methodName == "testWindowsPhoneReusableComponentsAddExternalModules" || methodName == "testDeleteWindowsPhoneJSLibraries" || methodName == "testWindowsPhoneReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteWindowsPhoneComponents" || methodName == "testWindowsPhoneReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSPHONE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWordPressModules" ||methodName == "testWordPressReusableComponentsAddModulesPage" || methodName == "testWordPressReusableComponentsAddExternalModules" || methodName == "testDeleteWordPressJSLibraries" || methodName == "testWordPressReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteWordPressComponents" || methodName == "testWordPressReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WORDPRESS_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				

			} catch (Exception e) {

				e.printStackTrace();
			}		
		

		     

			element=getXpathWebElement(uiConstants.ADD_MODULES_BUTTON);
			waitForElementPresent(uiConstants.ADD_MODULES_BUTTON,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MODULES_NAME);
			waitForElementPresent(uiConstants.MODULES_NAME,methodName);
			//type(adminUIConstants.ADD_MODULE_PHP_NAME_VALUE);
			type(methodName);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MODULE_DESCRIPTION);
			waitForElementPresent(uiConstants.MODULE_DESCRIPTION,methodName);
			type(adminUIConstants.ADD_MODULE_PHP_DESCRIPTION_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MODULE_HELPTEXT);
			waitForElementPresent(uiConstants.MODULE_HELPTEXT,methodName);
			type(adminUIConstants.ADD_MODULE_PHP_HELPTEXT_VALUE);
			Thread.sleep(1000);

			/*element=getXpathWebElement(uiConstants.MODULE_TECHNOLOGY);
					waitForElementPresent(uiConstants.MODULE_TECHNOLOGY, methodName);
					click();*/
			Thread.sleep(6000);					
			AddingTechnology(methodName);

			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.MODULE_MODULETYPE);
			waitForElementPresent(uiConstants.MODULE_MODULETYPE, methodName);
			click();
			type(adminUIConstants.ADD_MODULE_PHP_CUSTOM_MODULE_VALUE);
			click();
			Thread.sleep(3000);
			element=getXpathWebElement(uiConstants.MODULE_DEFAULTMODULE);
			waitForElementPresent(uiConstants.MODULE_DEFAULTMODULE, methodName);
			click();
			element=getXpathWebElement(uiConstants.MODULE_LICENSE_TYPE);
			waitForElementPresent(uiConstants.MODULE_LICENSE_TYPE, methodName);
			click();
			selectText(element,adminUIConstants.ADD_MODULE_PHP_LICENSE_TYPE_VALUE);
			click();
			Thread.sleep(3000);

			System.out.println("--------------Upload File----------");
			Thread.sleep(10000);
			driver.findElement(By.name("file")).sendKeys("C:\\Documents and Settings\\kalish_ma\\Desktop\\jars\\File_Explorer-31750.jar");
		    Thread.sleep(10000);         
          
		

			/*element=getXpathWebElement("//div/label[contains(text(),'Upload File')]");
					Thread.sleep(2000);
					element.sendKeys("c:\\");
					element.submit();*/

			//	Runtime.getRuntime().exec("C:/Documents and Settings/srividya_su/Desktop/site_uploadfile.exe");
			//	Thread.sleep(8000);

			//SELECT DEPENDENCY CURRENTLY NOT WORKING

			/*element=getXpathWebElement(uiConstants.MODULE_SELECT_DEPENDENCY);
					waitForElementPresent(uiConstants.MODULE_SELECT_DEPENDENCY, methodName);
					click();
					Thread.sleep(3000);

					element=getXpathWebElement(uiConstants.MODULE_DEPENDENCY_LOGIN_CHECKBOX);
					waitForElementPresent(uiConstants.MODULE_DEPENDENCY_LOGIN_CHECKBOX, methodName);
					click();

					element=getXpathWebElement(uiConstants.MODULE_DEPENDENCY_OK_BUTTON);
					waitForElementPresent(uiConstants.MODULE_DEPENDENCY_OK_BUTTON, methodName);
					click();*/
			Thread.sleep(1000);	
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			element=getXpathWebElement(uiConstants.MODULE_SAVE_BUTTON);
			log.info("--("+ element +")--");
			waitForElementPresent(uiConstants.MODULE_SAVE_BUTTON, methodName);
			click();
			System.out.println("--------------Module Creation----------");
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);
			System.out.println("--------------Module Created----------");
			//isTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);
			//Thread.sleep(10000);


			/*element=getXpathWebElement(uiConstants.MODULE_CANCEL_BUTTON);
					waitForElementPresent(uiConstants.MODULE_CANCEL_BUTTON, methodName);
					click();
					Thread.sleep(1000);*/
			Thread.sleep(3000);
			AddModuelsAndJarsVerification(methodName);

		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}
	public  void addModulesSelectingExternalModule(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testReusableComponentsAddPhpModulesPage::******executing addPhpModules scenario****");
		try {
           
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();
			Thread.sleep(3000);
   
			waitForElementPresent(uiConstants.CUSTOMER_DROPDOWN_VALUE,methodName);
			getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN_VALUE).click();
			Thread.sleep(15000);
			waitForTextPresent("Android Hybrid");
			
			
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENT_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENT_TAB,methodName);
			click();
			Thread.sleep(1000);

			
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENT_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENT_TAB,methodName);
			click();
			Thread.sleep(1000);
			try {

				if(methodName == "testDeletePHPModules" || methodName == "testPHPReusableComponentsAddModulesPage" || methodName == "testPHPReusableComponentsAddExternalModules"|| methodName == "testDeletePHPJSLibraries" || methodName =="testPHPReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_PHP_TECHNOLOGY_VALUE).click();
					Thread.sleep(2000);
				}
				if(methodName == "testDeleteDrupal6Modules" || methodName == "testDrupal6ReusableComponentsAddModulesPage" || methodName == "testDrupal6ReusableComponentsAddExternalModules" ||  methodName == "testDeleteDrupal6JSLibraries" || methodName == "testDrupal6ReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteDrupal6Components" || methodName == "testDrupal6ReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DRUPAL6_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteNodejsModules" || methodName == "testNodejsReusableComponentsAddModulesPage" || methodName == "testNodejsReusableComponentsAddExternalModules" || methodName == "testDeleteNodejsJSLibraries" || methodName == "testNodejsReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteNodejsComponents" || methodName == "testNodejsReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_NODEJS_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteJavaStandaloneModules" || methodName == "testJavaStandaloneReusableComponentsAddModulesPage" || methodName == "testJavaStandaloneReusableComponentsAddExternalModules" || methodName == "testDeleteJavaStandaloneJSLibraries" || methodName == "testJavaStandaloneReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteJavaStandaloneComponents" || methodName == "testJavaStandaloneReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_JAVASTANDALONE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteiPhoneHybridModules" || methodName == "testiPhoneHybridReusableComponentsAddModulesPage"|| methodName == "testiPhoneHybridReusableComponentsAddExternalModules"|| methodName == "testDeleteiPhoneHybridJSLibraries" || methodName == "testiPhoneHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiPhoneHybridComponents" || methodName == "testiPhoneHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);             
				}
				if(methodName == "testDeleteAndroidHybridModules" || methodName == "testAndroidHybridReusableComponentsAddModulesPage"|| methodName == "testAndroidHybridReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidHybridJSLibraries" || methodName == "testAndroidHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidHybridComponents" || methodName == "testAndroidHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWindowsMetroModules" || methodName == "testWindowsMetroReusableComponentsAddModulesPage"|| methodName == "testWindowsMetroReusableComponentsAddExternalModules" || methodName == "testDeleteWindowsMetroJSLibraries" ||methodName == "testWindowsMetroReusableComponentsAddJSlibrariesPage" || methodName =="testDeleteWindowsMetroComponents" || methodName == "testWindowsMetroReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSMETRO_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);           
				}
				if(methodName == "testDeleteBlackBerryHybridModules" ||methodName == "testBlackBerryHybridReusableComponentsAddModulesPage" || methodName == "testBlackBerryHybridReusableComponentsAddExternalModules" || methodName == "testDeleteBlackBerryHybridJSLibraries" || methodName == "testBlackBerryHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteBlackBerryHybridComponents" || methodName == "testBlackBerryHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteAndroidLibraryModules" ||methodName == "testAndroidLibraryReusableComponentsAddModulesPage" || methodName == "testAndroidLibraryReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidLibraryJSLibraries" || methodName == "testAndroidLibraryReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidLibraryComponents" || methodName == "testAndroidLibraryReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDLIBRARY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteAndroidNativeModules" ||methodName == "testAndroidNativeReusableComponentsAddModulesPage" || methodName == "testAndroidNativeReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidNativeJSLibraries" || methodName == "testAndroidNativeReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidNativeComponents" || methodName == "testAndroidNativeReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteASP_DotnetModules" ||methodName == "testDOTNETReusableComponentsAddModulesPage" || methodName == "testDOTNETReusableComponentsAddExternalModules" || methodName == "testDeleteASP_DotnetJSLibraries" || methodName == "testDOTNETReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteASP_dotnetComponents" || methodName == "testDOTNETReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DOTNET_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteDrupal7Modules" ||methodName == "testDrupal7ReusableComponentsAddModulesPage" || methodName == "testDrupal7ReusableComponentsAddExternalModules" || methodName == "testDeleteDrupal7JSLibraries" || methodName == "testDrupal7ReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteDrupal7Components" || methodName == "testDrupal7ReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DRUPAL7_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteJQueryMobileModules" ||methodName == "testJQueryMobileReusableComponentsAddModulesPage" || methodName == "testJQueryMobileReusableComponentsAddExternalModules" || methodName == "testDeleteJQueryMobileJSLibraries" || methodName == "testJQueryMobileReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteJQueryMobileComponents" || methodName == "testJQueryMobileReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_JQUERYMOBILE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteMultiJQueryModules" ||methodName == "testMultiJQueryReusableComponentsAddModulesPage" || methodName == "testMultiJQueryReusableComponentsAddExternalModules" || methodName == "testDeleteMultiJQueryJSLibraries" || methodName == "testMultiJQueryReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteMultiJQueryComponents" || methodName == "testMultiJQueryReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_MULTIJQUERY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);                 
				}
				if(methodName == "testDeleteMultiYUIModules" ||methodName == "testMultiYUIReusableComponentsAddModulesPage" || methodName == "testMultiYUIReusableComponentsAddExternalModules" || methodName == "testDeleteMultiYUIJSLibraries" || methodName == "testMultiYUIReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteMultiYUIComponents" || methodName == "testMultiYUIReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_MULTIYUI_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteYUIMobileModules" ||methodName == "testYUIMobileReusableComponentsAddModulesPage" || methodName == "testYUIMobileReusableComponentsAddExternalModules" || methodName == "testDeleteYUIMobileJSLibraries" || methodName == "testYUIMobileReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteYUIMobileComponents" || methodName == "testYUIMobileReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_YUIMOBILE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiPhoneLibraryModules" ||methodName == "testiPhoneLibraryReusableComponentsAddModulesPage" || methodName == "testiPhoneLibraryReusableComponentsAddExternalModules" || methodName == "testDeleteiPhoneLibraryJSLibraries" || methodName == "testiPhoneLibraryReusableComponentsAddJSlibrariesPage" || methodName == "testiPhoneLibraryReusableComponentsAddvalidComponentPage" || methodName == "testDeleteiPhoneLibraryComponents")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONELIBRARY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiphoneNativeModules" ||methodName == "testiphoneNativeReusableComponentsAddModulesPage" || methodName == "testiphoneNativeReusableComponentsAddExternalModules" || methodName == "testDeleteiphoneNativeJSLibraries" || methodName == "testiphoneNativeReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiphoneNativeComponents" || methodName == "testiphoneNativeReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONENATIVE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiPhoneWorkSpaceModules" ||methodName == "testiPhoneWorkSpaceReusableComponentsAddModulesPage" || methodName == "testiPhoneWorkSpaceReusableComponentsAddExternalModules" || methodName == "testDeleteiPhoneWorkSpaceJSLibraries" || methodName == "testiPhoneWorkSpaceReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiPhoneWorkSpaceComponents" || methodName == "testiPhoneWorkSpaceReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEWORKSAPCE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteSharePointModules" ||methodName == "testSharePointReusableComponentsAddModulesPage" || methodName == "testSharePointReusableComponentsAddExternalModules" || methodName == "testDeleteSharePointJSLibraries" || methodName == "testSharePointReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteSharePointComponents" || methodName == "testSharePointReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_SHAREPOINT_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteSiteCoreModules" ||methodName == "testSiteCoreReusableComponentsAddModulesPage" || methodName == "testSiteCoreReusableComponentsAddExternalModules" || methodName == "testDeleteSiteCoreJSLibraries" || methodName == "testSiteCoreReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteSiteCoreComponents" || methodName == "testSiteCoreReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_SITECORE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWindowsPhoneModules" ||methodName == "testWindowsPhoneReusableComponentsAddModulesPage" || methodName == "testWindowsPhoneReusableComponentsAddExternalModules" || methodName == "testDeleteWindowsPhoneJSLibraries" || methodName == "testWindowsPhoneReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteWindowsPhoneComponents" || methodName == "testWindowsPhoneReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSPHONE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWordPressModules" ||methodName == "testWordPressReusableComponentsAddModulesPage" || methodName == "testWordPressReusableComponentsAddExternalModules" || methodName == "testDeleteWordPressJSLibraries" || methodName == "testWordPressReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteWordPressComponents" || methodName == "testWordPressReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WORDPRESS_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				

			} catch (Exception e) {

				e.printStackTrace();
			}		


			element=getXpathWebElement(uiConstants.ADD_MODULES_BUTTON);
			waitForElementPresent(uiConstants.ADD_MODULES_BUTTON,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MODULES_NAME);
			waitForElementPresent(uiConstants.MODULES_NAME,methodName);
			//type(adminUIConstants.ADD_MODULE_PHP_NAME_VALUE);
			type(methodName);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MODULE_DESCRIPTION);
			waitForElementPresent(uiConstants.MODULE_DESCRIPTION,methodName);
			type(adminUIConstants.ADD_MODULE_PHP_DESCRIPTION_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MODULE_HELPTEXT);
			waitForElementPresent(uiConstants.MODULE_HELPTEXT,methodName);
			type(adminUIConstants.ADD_MODULE_PHP_HELPTEXT_VALUE);
			Thread.sleep(1000);

			/*element=getXpathWebElement(uiConstants.MODULE_TECHNOLOGY);
					waitForElementPresent(uiConstants.MODULE_TECHNOLOGY, methodName);
					click();*/
			Thread.sleep(6000);					
			AddingTechnology(methodName);

			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.MODULE_MODULETYPE_EXTERNALMODULE);
			waitForElementPresent(uiConstants.MODULE_MODULETYPE_EXTERNALMODULE, methodName);
			click();
			type(adminUIConstants.ADD_MODULE_PHP_EXTERNAL_MODULE_VALUE);
			click();
			element=getXpathWebElement(uiConstants.MODULE_DEFAULTMODULE);
			waitForElementPresent(uiConstants.MODULE_DEFAULTMODULE, methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.MODULE_LICENSE_TYPE);
			waitForElementPresent(uiConstants.MODULE_LICENSE_TYPE, methodName);
			click();
			type(adminUIConstants.ADD_MODULE_PHP_LICENSE_TYPE_VALUE);
			click();
			Thread.sleep(3000);

			System.out.println("--------------Upload File----------");
			driver.findElement(By.name("file")).sendKeys("C:\\Documents and Settings\\vairamuthu_m\\Desktop\\jars\\tech-php-2.0.0.34000.zip");
			Thread.sleep(4000);
			System.out.println("--------------Upload File----------");

			/*element=getXpathWebElement("//div/label[contains(text(),'Upload File')]");
					Thread.sleep(2000);
					element.sendKeys("c:\\");
					element.submit();*/

			//	Runtime.getRuntime().exec("C:/Documents and Settings/srividya_su/Desktop/site_uploadfile.exe");
			//	Thread.sleep(8000);

			//SELECT DEPENDENCY CURRENTLY NOT WORKING

			/*element=getXpathWebElement(uiConstants.MODULE_SELECT_DEPENDENCY);
					waitForElementPresent(uiConstants.MODULE_SELECT_DEPENDENCY, methodName);
					click();
					Thread.sleep(3000);

					element=getXpathWebElement(uiConstants.MODULE_DEPENDENCY_LOGIN_CHECKBOX);
					waitForElementPresent(uiConstants.MODULE_DEPENDENCY_LOGIN_CHECKBOX, methodName);
					click();

					element=getXpathWebElement(uiConstants.MODULE_DEPENDENCY_OK_BUTTON);
					waitForElementPresent(uiConstants.MODULE_DEPENDENCY_OK_BUTTON, methodName);
					click();*/
			Thread.sleep(1000);	
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			element=getXpathWebElement(uiConstants.MODULE_SAVE_BUTTON);
			log.info("--("+ element +")--");
			waitForElementPresent(uiConstants.MODULE_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(1000);
			System.out.println("--------------Module Creation----------");
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);
			System.out.println("--------------Module Created----------");
			//isTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);
			//Thread.sleep(10000);


			/*element=getXpathWebElement(uiConstants.MODULE_CANCEL_BUTTON);
					waitForElementPresent(uiConstants.MODULE_CANCEL_BUTTON, methodName);
					click();
					Thread.sleep(1000);*/


		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}
	public  void RC_MCreateSampleMobileLayerArchetypes(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testReusableComponentsAddPhpModulesPage::******executing addSampleModules scenario****");
		try {

			
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENT_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENT_TAB,methodName);
			click();
			Thread.sleep(1000);


			element=getXpathWebElement(uiConstants.ADD_MODULES_BUTTON);
			waitForElementPresent(uiConstants.ADD_MODULES_BUTTON,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MODULES_NAME);
			waitForElementPresent(uiConstants.MODULES_NAME,methodName);
			type(adminUIConstants.RC_CREATEDARCHETYPE);
			//type(methodName);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MODULE_DESCRIPTION);
			waitForElementPresent(uiConstants.MODULE_DESCRIPTION,methodName);
			type(adminUIConstants.ADD_MODULE_PHP_DESCRIPTION_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MODULE_HELPTEXT);
			waitForElementPresent(uiConstants.MODULE_HELPTEXT,methodName);
			type(adminUIConstants.ADD_MODULE_PHP_HELPTEXT_VALUE);
			Thread.sleep(1000);

			/*element=getXpathWebElement(uiConstants.RC_MODULETECHNOLOGYFORNEWARCHETYPE);
					waitForElementPresent(uiConstants.RC_MODULETECHNOLOGYFORNEWARCHETYPE, methodName);
					click();
					Thread.sleep(6000);	
			 */
			Thread.sleep(1000);

			System.out.println("--------------Technology----------");

			element=getXpathWebElement(uiConstants.RC_SELECTMODULETECHNOLOGY);
			waitForElementPresent(uiConstants.RC_SELECTMODULETECHNOLOGY, methodName);
			click();
			Thread.sleep(6000);	
			//AddingTechnology(methodName);

			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.MODULE_MODULETYPE);
			waitForElementPresent(uiConstants.MODULE_MODULETYPE, methodName);
			click();
			type(adminUIConstants.ADD_MODULE_PHP_CUSTOM_MODULE_VALUE);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.MODULE_LICENSE_TYPE);
			waitForElementPresent(uiConstants.MODULE_LICENSE_TYPE, methodName);
			click();
			type(adminUIConstants.ADD_MODULE_PHP_LICENSE_TYPE_VALUE);
			click();
			Thread.sleep(3000);

			System.out.println("--------------Upload File----------");
			driver.findElement(By.name("file")).sendKeys("C:\\Documents and Settings\\aruna_k\\Desktop\\archetype.jar");

			Thread.sleep(4000);
			System.out.println("--------------Upload File----------");

			/*element=getXpathWebElement("//div/label[contains(text(),'Upload File')]");
					Thread.sleep(2000);
					element.sendKeys("c:\\");
					element.submit();*/

			//	Runtime.getRuntime().exec("C:/Documents and Settings/srividya_su/Desktop/site_uploadfile.exe");
			//	Thread.sleep(8000);

			//SELECT DEPENDENCY CURRENTLY NOT WORKING

			/*element=getXpathWebElement(uiConstants.MODULE_SELECT_DEPENDENCY);
					waitForElementPresent(uiConstants.MODULE_SELECT_DEPENDENCY, methodName);
					click();
					Thread.sleep(3000);

					element=getXpathWebElement(uiConstants.MODULE_DEPENDENCY_LOGIN_CHECKBOX);
					waitForElementPresent(uiConstants.MODULE_DEPENDENCY_LOGIN_CHECKBOX, methodName);
					click();

					element=getXpathWebElement(uiConstants.MODULE_DEPENDENCY_OK_BUTTON);
					waitForElementPresent(uiConstants.MODULE_DEPENDENCY_OK_BUTTON, methodName);
					click();*/
			Thread.sleep(1000);	
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			element=getXpathWebElement(uiConstants.MODULE_SAVE_BUTTON);
			log.info("--("+ element +")--");
			waitForElementPresent(uiConstants.MODULE_SAVE_BUTTON, methodName);
			click();
			System.out.println("--------------Module Creation----------");
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);
			System.out.println("--------------Module Created----------");
			//isTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);
			//Thread.sleep(10000);


			/*element=getXpathWebElement(uiConstants.MODULE_CANCEL_BUTTON);
					waitForElementPresent(uiConstants.MODULE_CANCEL_BUTTON, methodName);
					click();
					Thread.sleep(1000);*/


		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}



	public  void updateModules(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testReusableComponentsUpdateValidModulesPage::******executing updateModules scenario****");
		try {
			Thread.sleep(3000);
			element=getXpathWebElement(uiConstants.UPDATE_MODULE_CHECK_BOX);
			waitForElementPresent(uiConstants.UPDATE_MODULE_CHECK_BOX,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.UPDATE_MODULE_LINK);
			waitForElementPresent(uiConstants.UPDATE_MODULE_LINK,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MODULES_NAME);
			waitForElementPresent(uiConstants.MODULES_NAME,methodName);
			clear();
			type(adminUIConstants.UPDATE_MODULE_NAME_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MODULE_DESCRIPTION);
			waitForElementPresent(uiConstants.MODULE_DESCRIPTION,methodName);
			clear();
			type(adminUIConstants.UPDATE_MODULE_DESCRIPTION_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MODULE_HELPTEXT);
			waitForElementPresent(uiConstants.MODULE_HELPTEXT,methodName);
			clear();
			type(adminUIConstants.UPDATE_MODULE_HELP_TEXT_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.UPDATE_MODULE_UPDATE_BUTTON);
			waitForElementPresent(uiConstants.UPDATE_MODULE_UPDATE_BUTTON,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MODULE_CANCEL_BUTTON);
			waitForElementPresent(uiConstants.MODULE_CANCEL_BUTTON, methodName);
			click();
			Thread.sleep(1000);

		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}

	public  void deleteModules(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testComponentsFeaturePage::******executing deleteModules scenario****");
		try {
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.DELETE_MODULE_CHECK_BOX);
			waitForElementPresent(uiConstants.DELETE_MODULE_CHECK_BOX, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.DELETE_MODULE_DELETE_BUTTON);
			waitForElementPresent(uiConstants.DELETE_MODULE_DELETE_BUTTON, methodName);
			click();
			Thread.sleep(2000);

			element=getXpathWebElement(uiConstants.DELETE_MODULE_DIALOG_BOX_OK_BUTTON);
			waitForElementPresent(uiConstants.DELETE_MODULE_DIALOG_BOX_OK_BUTTON, methodName);
			click();
			Thread.sleep(2000);

		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}

	public  void addEmptyJSLibraries(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testReusableComponentsAddEmptyJSlibrariesPage::******executing testReusableComponentsAddEmptyJSlibrariesPage scenario****");
		try {
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();
			Thread.sleep(3000);
   
			waitForElementPresent(uiConstants.CUSTOMER_DROPDOWN_VALUE,methodName);
			getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN_VALUE).click();
			Thread.sleep(15000);
			waitForTextPresent("Android Hybrid");
			try {

				if(methodName == "testPHPCreateApplicationLayerArchetypesPage" || methodName == "testDrupal6CreateApplicationLayerArchetypesPage" || methodName == "testDrupal7CreateApplicationLayerArchetypesPage"|| methodName == "testWordPressCreateApplicationLayerArchetypesPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_PHP_TECHNOLOGY_VALUE).click();
					Thread.sleep(2000);
				}
				if(methodName == "testDOTNETCreateApplicationLayerArchetypesPage" || methodName == "testSharePointCreateApplicationLayerArchetypesPage" || methodName == "testSiteCoreCreateApplicationLayerArchetypesPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DOTNET_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testNodejsCreateApplicationLayerArchetypesPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_NODEJS_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testJavaStandaloneCreateApplicationLayerArchetypesPage" || methodName == "testJavaWebServiceCreateApplicationLayerArchetypesPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_JAVASTANDALONE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testiPhoneHybridCreateMobileLayerArchetypes" || methodName == "testiphoneNativeCreateMobileLayerArchetypes"|| methodName == "testiPhoneLibraryCreateMobileLayerArchetypes"|| methodName == "testiPhoneWorkSpaceCreateMobileLayerArchetypes")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);             
				}
				if(methodName == "testAndroidHybridCreateMobileLayerArchetypes" || methodName == "testAndroidNativeCreateMobileLayerArchetypes"|| methodName == "testAndroidLibraryCreateMobileLayerArchetypes")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testWindowsMetroCreateMobileLayerArchetypes" || methodName == "testWindowsPhoneCreateMobileLayerArchetypes")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSMETRO_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);           
				}
				if(methodName == "testBlackBerryHybridCreateMobileLayerArchetypes")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}

			} catch (Exception e) {

				e.printStackTrace();
			}		
		

		     
			
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENTS_JSLIBRARIES_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENTS_JSLIBRARIES_TAB,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_ADD_BUTTON);
			waitForElementPresent(uiConstants.JSLIBRARIES_ADD_BUTTON,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_NAME);
			waitForElementPresent(uiConstants.JSLIBRARIES_NAME,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_DESCRIPTION);
			waitForElementPresent(uiConstants.JSLIBRARIES_DESCRIPTION,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_HELPTEXT);
			waitForElementPresent(uiConstants.JSLIBRARIES_HELPTEXT,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_SAVE_BUTTON);
			waitForElementPresent(uiConstants.JSLIBRARIES_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(3000);


			isTextPresent(adminUIConstants.EMPTY_ADD_JSLIBRARIES_NAME_MSG);
			isTextPresent(adminUIConstants.EMPTY_ADD_JSLIBRARIES_UPLOAD_FILE_MSG);
			isTextPresent(adminUIConstants.EMPTY_ADD_JSLIBRARIES_LICENSE_TYPE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_CANCEL_BUTTON);
			waitForElementPresent(uiConstants.JSLIBRARIES_CANCEL_BUTTON, methodName);
			click();
			Thread.sleep(1000);

		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}
	public  void addInvalidJSLibraries(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testReusableComponentsAddInvalidJSlibrariesPage::******executing addInvalidJSLibraries scenario****");
		try {
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();
			Thread.sleep(3000);
   
			waitForElementPresent(uiConstants.CUSTOMER_DROPDOWN_VALUE,methodName);
			getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN_VALUE).click();
			Thread.sleep(15000);
			waitForTextPresent("Android Hybrid");
			try {

				if(methodName == "testPHPCreateApplicationLayerArchetypesPage" || methodName == "testDrupal6CreateApplicationLayerArchetypesPage" || methodName == "testDrupal7CreateApplicationLayerArchetypesPage"|| methodName == "testWordPressCreateApplicationLayerArchetypesPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_PHP_TECHNOLOGY_VALUE).click();
					Thread.sleep(2000);
				}
				if(methodName == "testDOTNETCreateApplicationLayerArchetypesPage" || methodName == "testSharePointCreateApplicationLayerArchetypesPage" || methodName == "testSiteCoreCreateApplicationLayerArchetypesPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DOTNET_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testNodejsCreateApplicationLayerArchetypesPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_NODEJS_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testJavaStandaloneCreateApplicationLayerArchetypesPage" || methodName == "testJavaWebServiceCreateApplicationLayerArchetypesPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_JAVASTANDALONE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testiPhoneHybridCreateMobileLayerArchetypes" || methodName == "testiphoneNativeCreateMobileLayerArchetypes"|| methodName == "testiPhoneLibraryCreateMobileLayerArchetypes"|| methodName == "testiPhoneWorkSpaceCreateMobileLayerArchetypes")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);             
				}
				if(methodName == "testAndroidHybridCreateMobileLayerArchetypes" || methodName == "testAndroidNativeCreateMobileLayerArchetypes"|| methodName == "testAndroidLibraryCreateMobileLayerArchetypes")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testWindowsMetroCreateMobileLayerArchetypes" || methodName == "testWindowsPhoneCreateMobileLayerArchetypes")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSMETRO_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);           
				}
				if(methodName == "testBlackBerryHybridCreateMobileLayerArchetypes")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}

			} catch (Exception e) {

				e.printStackTrace();
			}		
		

		     
			
			Thread.sleep(3000);					
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENTS_JSLIBRARIES_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENTS_JSLIBRARIES_TAB,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_ADD_BUTTON);
			waitForElementPresent(uiConstants.JSLIBRARIES_ADD_BUTTON,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_NAME);
			waitForElementPresent(uiConstants.JSLIBRARIES_NAME,methodName);
			type(adminUIConstants.ADD_JSLIBRARIES_NAME_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_DESCRIPTION);
			waitForElementPresent(uiConstants.JSLIBRARIES_DESCRIPTION,methodName);
			type(adminUIConstants.ADD_JSLIBRARIES_DESCRIPTION_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_HELPTEXT);
			waitForElementPresent(uiConstants.JSLIBRARIES_HELPTEXT,methodName);
			type(adminUIConstants.ADD_JSLIBRARIES_HELPTEXT_VALUE);
			Thread.sleep(1000);


			element=getXpathWebElement(uiConstants.JSLIBRARIES_TECHNOLOGY);
			waitForElementPresent(uiConstants.JSLIBRARIES_TECHNOLOGY, methodName);
			click();
			Thread.sleep(1000);

			type(adminUIConstants.ADD_JSLIBRARIES_PHP_TECHONOLGY_VALUE);
			click();
			Thread.sleep(3000);


			element=getXpathWebElement(uiConstants.JSLIBRARIES_LICENSE_TYPE);
			waitForElementPresent(uiConstants.JSLIBRARIES_LICENSE_TYPE, methodName);
			click();
			Thread.sleep(1000);

			type(uiConstants.ADD_JSLIBRARIES_LICENSE_TYPE);
			click();
			Thread.sleep(3000);

			//element.findElement(By.className("file")).sendKeys("D:/Jagathes/selenium-server-standalone-2.25.0.jar");
			//element=getXpathWebElement(uiConstants.MODULEUPLOADFILE_XPATH);
			//waitForElementPresent(uiConstants.MODULEUPLOADFILE_XPATH, methodName);
			//click();

			element=getXpathWebElement(uiConstants.JSLIBRARIES_SELECT_DEPENDENCY);
			waitForElementPresent(uiConstants.JSLIBRARIES_SELECT_DEPENDENCY, methodName);
			click();
			Thread.sleep(3000);


			element=getXpathWebElement(uiConstants.JSLIBRARIES_SELECT_JSON_CHECKBOX);
			waitForElementPresent(uiConstants.JSLIBRARIES_SELECT_JSON_CHECKBOX, methodName);
			click();

			element=getXpathWebElement(uiConstants.JSLIBRARIES_DEPENDENCY_OK_BUTTON);
			waitForElementPresent(uiConstants.JSLIBRARIES_DEPENDENCY_OK_BUTTON, methodName);
			click();
			Thread.sleep(1000);	


			element=getXpathWebElement(uiConstants.JSLIBRARIES_SAVE_BUTTON);
			waitForElementPresent(uiConstants.JSLIBRARIES_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(1000);


			isTextPresent(adminUIConstants.EMPTY_ADD_JSLIBRARIES_UPLOAD_FILE_MSG);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_CANCEL_BUTTON);
			waitForElementPresent(uiConstants.JSLIBRARIES_CANCEL_BUTTON, methodName);
			click();
			Thread.sleep(1000);


		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}

	public  void addValidJSLibraries(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testReusableComponentsAddValidJSlibrariesPage::******executing addValidJSLibraries scenario****");
		try {
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();
			Thread.sleep(3000);
   
			waitForElementPresent(uiConstants.CUSTOMER_DROPDOWN_VALUE,methodName);
			getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN_VALUE).click();
			Thread.sleep(15000);
			waitForTextPresent("Android Hybrid");
			try {

				if(methodName == "testPHPCreateApplicationLayerArchetypesPage" || methodName == "testDrupal6CreateApplicationLayerArchetypesPage" || methodName == "testDrupal7CreateApplicationLayerArchetypesPage"|| methodName == "testWordPressCreateApplicationLayerArchetypesPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_PHP_TECHNOLOGY_VALUE).click();
					Thread.sleep(2000);
				}
				if(methodName == "testDOTNETCreateApplicationLayerArchetypesPage" || methodName == "testSharePointCreateApplicationLayerArchetypesPage" || methodName == "testSiteCoreCreateApplicationLayerArchetypesPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DOTNET_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testNodejsCreateApplicationLayerArchetypesPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_NODEJS_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testJavaStandaloneCreateApplicationLayerArchetypesPage" || methodName == "testJavaWebServiceCreateApplicationLayerArchetypesPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_JAVASTANDALONE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testiPhoneHybridCreateMobileLayerArchetypes" || methodName == "testiphoneNativeCreateMobileLayerArchetypes"|| methodName == "testiPhoneLibraryCreateMobileLayerArchetypes"|| methodName == "testiPhoneWorkSpaceCreateMobileLayerArchetypes")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);             
				}
				if(methodName == "testAndroidHybridCreateMobileLayerArchetypes" || methodName == "testAndroidNativeCreateMobileLayerArchetypes"|| methodName == "testAndroidLibraryCreateMobileLayerArchetypes")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testWindowsMetroCreateMobileLayerArchetypes" || methodName == "testWindowsPhoneCreateMobileLayerArchetypes")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSMETRO_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);           
				}
				if(methodName == "testBlackBerryHybridCreateMobileLayerArchetypes")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}

			} catch (Exception e) {

				e.printStackTrace();
			}		
		

		     
			
			Thread.sleep(3000);
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENTS_JSLIBRARIES_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENTS_JSLIBRARIES_TAB,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_ADD_BUTTON);
			waitForElementPresent(uiConstants.JSLIBRARIES_ADD_BUTTON,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_NAME);
			waitForElementPresent(uiConstants.JSLIBRARIES_NAME,methodName);
			type(adminUIConstants.ADD_JSLIBRARIES_NAME_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_DESCRIPTION);
			waitForElementPresent(uiConstants.JSLIBRARIES_DESCRIPTION,methodName);
			type(adminUIConstants.ADD_JSLIBRARIES_DESCRIPTION_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_HELPTEXT);
			waitForElementPresent(uiConstants.JSLIBRARIES_HELPTEXT,methodName);
			type(adminUIConstants.ADD_JSLIBRARIES_HELPTEXT_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_TECHNOLOGY);
			waitForElementPresent(uiConstants.JSLIBRARIES_TECHNOLOGY, methodName);
			click();
			Thread.sleep(1000);

			type(adminUIConstants.ADD_JSLIBRARIES_PHP_TECHONOLGY_VALUE);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_LICENSE_TYPE);
			waitForElementPresent(uiConstants.JSLIBRARIES_LICENSE_TYPE, methodName);
			click();
			Thread.sleep(1000);

			type(uiConstants.ADD_JSLIBRARIES_LICENSE_TYPE);
			click();
			Thread.sleep(3000);


			//element.findElement(By.className("file")).sendKeys("D:/Jagathes/selenium-server-standalone-2.25.0.jar");
			//element=getXpathWebElement(uiConstants.MODULEUPLOADFILE_XPATH);
			//waitForElementPresent(uiConstants.MODULEUPLOADFILE_XPATH, methodName);
			//click();

			element=getXpathWebElement(uiConstants.JSLIBRARIES_SELECT_DEPENDENCY);
			waitForElementPresent(uiConstants.JSLIBRARIES_SELECT_DEPENDENCY, methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_SELECT_JSON_CHECKBOX);
			waitForElementPresent(uiConstants.JSLIBRARIES_SELECT_JSON_CHECKBOX, methodName);
			click();

			element=getXpathWebElement(uiConstants.JSLIBRARIES_DEPENDENCY_OK_BUTTON);
			waitForElementPresent(uiConstants.JSLIBRARIES_DEPENDENCY_OK_BUTTON, methodName);
			click();
			Thread.sleep(1000);	

			element=getXpathWebElement(uiConstants.JSLIBRARIES_SAVE_BUTTON);
			waitForElementPresent(uiConstants.JSLIBRARIES_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(1000);



		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}
	
	
	
	// RBAC Test case 
	
	public  void RBAC(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testRBACMechanism::******executing RBAC scenario****");
		try {
			Thread.sleep(3000);
			element=getXpathWebElement(uiConstants.ADMIN_LINK);
			waitForElementPresent(uiConstants.ADMIN_LINK,methodName);
			click();
			waitForTextPresent("Customers");
			Thread.sleep(20000);

			element=getXpathWebElement(uiConstants.USERS);
			waitForElementPresent(uiConstants.USERS,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.ROLES);
			waitForElementPresent(uiConstants.ROLES,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.SERVICE);
			waitForElementPresent(uiConstants.SERVICE,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.SERVICE_ADD_BUTTON);
			waitForElementPresent(uiConstants.SERVICE_ADD_BUTTON,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.SERVICE_NAME);
			waitForElementPresent(uiConstants.SERVICE_NAME,methodName);
			click();
			type(adminUIConstants.NAME_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.SERVICE_DERSCRIPTION_VALUE);
			waitForElementPresent(uiConstants.SERVICE_DERSCRIPTION_VALUE,methodName);
			click();
			type(adminUIConstants.DERSCRIPTION_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.SERVICE_SAVE);
			waitForElementPresent(uiConstants.SERVICE_SAVE,methodName);
			click();
			Thread.sleep(5000);
			
			element=getXpathWebElement(uiConstants.FRAMEWORK);
			waitForElementPresent(uiConstants.FRAMEWORK,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.FRAMEWORK_ADD_BUTTON);
			waitForElementPresent(uiConstants.FRAMEWORK_ADD_BUTTON,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.FRAMEWORK_NAME);
			waitForElementPresent(uiConstants.FRAMEWORK_NAME,methodName);
			click();
			type(adminUIConstants.NAME_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.FRAMEWORK_DERSCRIPTION_VALUE);
			waitForElementPresent(uiConstants.FRAMEWORK_DERSCRIPTION_VALUE,methodName);
			click();
			type(adminUIConstants.DERSCRIPTION_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.FRAMEWORK_SAVE);
			waitForElementPresent(uiConstants.FRAMEWORK_SAVE,methodName);
			click();
			Thread.sleep(5000);
			
			element=getXpathWebElement(uiConstants.PERMISSION);
			waitForElementPresent(uiConstants.PERMISSION,methodName);
			click();
			Thread.sleep(2000);
			
			element=getXpathWebElement(uiConstants.PERMISSION_SERVICE);
			waitForElementPresent(uiConstants.PERMISSION_SERVICE,methodName);
			click();
			Thread.sleep(2000);
			
			element=getXpathWebElement(uiConstants.PERMISSION_FRAMEWORK);
			waitForElementPresent(uiConstants.PERMISSION_FRAMEWORK,methodName);
			click();
			Thread.sleep(5000);
			
		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}
	public  void addingJSLibraries(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testReusableComponentsAddValidJSlibrariesPage::******executing addValidJSLibraries scenario****");
		try {
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();
			Thread.sleep(3000);
   
			waitForElementPresent(uiConstants.CUSTOMER_DROPDOWN_VALUE,methodName);
			getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN_VALUE).click();
			Thread.sleep(15000);
			waitForTextPresent("Android Hybrid");
			Thread.sleep(1000);
			
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENT_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENT_TAB,methodName);
			click();
			Thread.sleep(2000);

			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENTS_JSLIBRARIES_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENTS_JSLIBRARIES_TAB,methodName);
			click();
			Thread.sleep(1000);
			try {

				if(methodName == "testDeletePHPModules" || methodName == "testPHPReusableComponentsAddModulesPage" || methodName == "testPHPReusableComponentsAddExternalModules"|| methodName == "testDeletePHPJSLibraries" || methodName =="testPHPReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_PHP_TECHNOLOGY_VALUE).click();
					Thread.sleep(2000);
				}
				if(methodName == "testDeleteDrupal6Modules" || methodName == "testDrupal6ReusableComponentsAddModulesPage" || methodName == "testDrupal6ReusableComponentsAddExternalModules" ||  methodName == "testDeleteDrupal6JSLibraries" || methodName == "testDrupal6ReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteDrupal6Components" || methodName == "testDrupal6ReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DRUPAL6_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteNodejsModules" || methodName == "testNodejsReusableComponentsAddModulesPage" || methodName == "testNodejsReusableComponentsAddExternalModules" || methodName == "testDeleteNodejsJSLibraries" || methodName == "testNodejsReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteNodejsComponents" || methodName == "testNodejsReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_NODEJS_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteJavaStandaloneModules" || methodName == "testJavaStandaloneReusableComponentsAddModulesPage" || methodName == "testJavaStandaloneReusableComponentsAddExternalModules" || methodName == "testDeleteJavaStandaloneJSLibraries" || methodName == "testJavaStandaloneReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteJavaStandaloneComponents" || methodName == "testJavaStandaloneReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_JAVASTANDALONE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteiPhoneHybridModules" || methodName == "testiPhoneHybridReusableComponentsAddModulesPage"|| methodName == "testiPhoneHybridReusableComponentsAddExternalModules"|| methodName == "testDeleteiPhoneHybridJSLibraries" || methodName == "testiPhoneHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiPhoneHybridComponents" || methodName == "testiPhoneHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);             
				}
				if(methodName == "testDeleteAndroidHybridModules" || methodName == "testAndroidHybridReusableComponentsAddModulesPage"|| methodName == "testAndroidHybridReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidHybridJSLibraries" || methodName == "testAndroidHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidHybridComponents" || methodName == "testAndroidHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWindowsMetroModules" || methodName == "testWindowsMetroReusableComponentsAddModulesPage"|| methodName == "testWindowsMetroReusableComponentsAddExternalModules" || methodName == "testDeleteWindowsMetroJSLibraries" ||methodName == "testWindowsMetroReusableComponentsAddJSlibrariesPage" || methodName =="testDeleteWindowsMetroComponents" || methodName == "testWindowsMetroReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSMETRO_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);           
				}
				if(methodName == "testDeleteBlackBerryHybridModules" ||methodName == "testBlackBerryHybridReusableComponentsAddModulesPage" || methodName == "testBlackBerryHybridReusableComponentsAddExternalModules" || methodName == "testDeleteBlackBerryHybridJSLibraries" || methodName == "testBlackBerryHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteBlackBerryHybridComponents" || methodName == "testBlackBerryHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteAndroidLibraryModules" ||methodName == "testAndroidLibraryReusableComponentsAddModulesPage" || methodName == "testAndroidLibraryReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidLibraryJSLibraries" || methodName == "testAndroidLibraryReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidLibraryComponents" || methodName == "testAndroidLibraryReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDLIBRARY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteAndroidNativeModules" ||methodName == "testAndroidNativeReusableComponentsAddModulesPage" || methodName == "testAndroidNativeReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidNativeJSLibraries" || methodName == "testAndroidNativeReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidNativeComponents" || methodName == "testAndroidNativeReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteASP_DotnetModules" ||methodName == "testDOTNETReusableComponentsAddModulesPage" || methodName == "testDOTNETReusableComponentsAddExternalModules" || methodName == "testDeleteASP_DotnetJSLibraries" || methodName == "testDOTNETReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteASP_dotnetComponents" || methodName == "testDOTNETReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DOTNET_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteDrupal7Modules" ||methodName == "testDrupal7ReusableComponentsAddModulesPage" || methodName == "testDrupal7ReusableComponentsAddExternalModules" || methodName == "testDeleteDrupal7JSLibraries" || methodName == "testDrupal7ReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteDrupal7Components" || methodName == "testDrupal7ReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DRUPAL7_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteJQueryMobileModules" ||methodName == "testJQueryMobileReusableComponentsAddModulesPage" || methodName == "testJQueryMobileReusableComponentsAddExternalModules" || methodName == "testDeleteJQueryMobileJSLibraries" || methodName == "testJQueryMobileReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteJQueryMobileComponents" || methodName == "testJQueryMobileReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_JQUERYMOBILE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteMultiJQueryModules" ||methodName == "testMultiJQueryReusableComponentsAddModulesPage" || methodName == "testMultiJQueryReusableComponentsAddExternalModules" || methodName == "testDeleteMultiJQueryJSLibraries" || methodName == "testMultiJQueryReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteMultiJQueryComponents" || methodName == "testMultiJQueryReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_MULTIJQUERY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);                 
				}
				if(methodName == "testDeleteMultiYUIModules" ||methodName == "testMultiYUIReusableComponentsAddModulesPage" || methodName == "testMultiYUIReusableComponentsAddExternalModules" || methodName == "testDeleteMultiYUIJSLibraries" || methodName == "testMultiYUIReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteMultiYUIComponents" || methodName == "testMultiYUIReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_MULTIYUI_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteYUIMobileModules" ||methodName == "testYUIMobileReusableComponentsAddModulesPage" || methodName == "testYUIMobileReusableComponentsAddExternalModules" || methodName == "testDeleteYUIMobileJSLibraries" || methodName == "testYUIMobileReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteYUIMobileComponents" || methodName == "testYUIMobileReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_YUIMOBILE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiPhoneLibraryModules" ||methodName == "testiPhoneLibraryReusableComponentsAddModulesPage" || methodName == "testiPhoneLibraryReusableComponentsAddExternalModules" || methodName == "testDeleteiPhoneLibraryJSLibraries" || methodName == "testiPhoneLibraryReusableComponentsAddJSlibrariesPage" || methodName == "testiPhoneLibraryReusableComponentsAddvalidComponentPage" || methodName == "testDeleteiPhoneLibraryComponents")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONELIBRARY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiphoneNativeModules" ||methodName == "testiphoneNativeReusableComponentsAddModulesPage" || methodName == "testiphoneNativeReusableComponentsAddExternalModules" || methodName == "testDeleteiphoneNativeJSLibraries" || methodName == "testiphoneNativeReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiphoneNativeComponents" || methodName == "testiphoneNativeReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONENATIVE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiPhoneWorkSpaceModules" ||methodName == "testiPhoneWorkSpaceReusableComponentsAddModulesPage" || methodName == "testiPhoneWorkSpaceReusableComponentsAddExternalModules" || methodName == "testDeleteiPhoneWorkSpaceJSLibraries" || methodName == "testiPhoneWorkSpaceReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiPhoneWorkSpaceComponents" || methodName == "testiPhoneWorkSpaceReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEWORKSAPCE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteSharePointModules" ||methodName == "testSharePointReusableComponentsAddModulesPage" || methodName == "testSharePointReusableComponentsAddExternalModules" || methodName == "testDeleteSharePointJSLibraries" || methodName == "testSharePointReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteSharePointComponents" || methodName == "testSharePointReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_SHAREPOINT_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteSiteCoreModules" ||methodName == "testSiteCoreReusableComponentsAddModulesPage" || methodName == "testSiteCoreReusableComponentsAddExternalModules" || methodName == "testDeleteSiteCoreJSLibraries" || methodName == "testSiteCoreReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteSiteCoreComponents" || methodName == "testSiteCoreReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_SITECORE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWindowsPhoneModules" ||methodName == "testWindowsPhoneReusableComponentsAddModulesPage" || methodName == "testWindowsPhoneReusableComponentsAddExternalModules" || methodName == "testDeleteWindowsPhoneJSLibraries" || methodName == "testWindowsPhoneReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteWindowsPhoneComponents" || methodName == "testWindowsPhoneReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSPHONE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWordPressModules" ||methodName == "testWordPressReusableComponentsAddModulesPage" || methodName == "testWordPressReusableComponentsAddExternalModules" || methodName == "testDeleteWordPressJSLibraries" || methodName == "testWordPressReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteWordPressComponents" || methodName == "testWordPressReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WORDPRESS_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				

			} catch (Exception e) {

				e.printStackTrace();
			}		

		     

			element=getXpathWebElement(uiConstants.JSLIBRARIES_ADD_BUTTON);
			waitForElementPresent(uiConstants.JSLIBRARIES_ADD_BUTTON,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_NAME);
			waitForElementPresent(uiConstants.JSLIBRARIES_NAME,methodName);
			//type(adminUIConstants.ADD_JSLIBRARIES_NAME_VALUE);
			type(methodName);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_DESCRIPTION);
			waitForElementPresent(uiConstants.JSLIBRARIES_DESCRIPTION,methodName);
			type(adminUIConstants.ADD_JSLIBRARIES_DESCRIPTION_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_HELPTEXT);
			waitForElementPresent(uiConstants.JSLIBRARIES_HELPTEXT,methodName);
			type(adminUIConstants.ADD_JSLIBRARIES_HELPTEXT_VALUE);
			Thread.sleep(1000);

			/*element=getXpathWebElement(uiConstants.JSLIBRARIES_TECHNOLOGY);
					waitForElementPresent(uiConstants.JSLIBRARIES_TECHNOLOGY, methodName);
					click();*/
			Thread.sleep(3000);

			AddingTechnology(methodName);
			//Thread.sleep(4000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_LICENSE_TYPE);
			waitForElementPresent(uiConstants.JSLIBRARIES_LICENSE_TYPE, methodName);
			click();
			Thread.sleep(3000);

			type(uiConstants.ADD_JSLIBRARIES_LICENSE_TYPE);
			click();
			Thread.sleep(8000);

			System.out.println("--------------Upload File----------");
			driver.findElement(By.name("file")).sendKeys("C:\\Documents and Settings\\kalish_ma\\Desktop\\jars\\helloworldTest.js");

			Thread.sleep(4000);
			System.out.println("--------------Upload File----------");

			/*Runtime.getRuntime().exec("C://Documents and Settings/srividya_su/Desktop/site_uploadfile.exe");
					Thread.sleep(4000);*/

			//SELECT DEPENDENCY CURRENTLY NOT WORKING

			/*element=getXpathWebElement(uiConstants.JSLIBRARIES_SELECT_DEPENDENCY);
					waitForElementPresent(uiConstants.JSLIBRARIES_SELECT_DEPENDENCY, methodName);
					click();
					Thread.sleep(3000);

					element=getXpathWebElement(uiConstants.JSLIBRARIES_SELECT_JSON_CHECKBOX);
					waitForElementPresent(uiConstants.JSLIBRARIES_SELECT_JSON_CHECKBOX, methodName);
					click();

					element=getXpathWebElement(uiConstants.JSLIBRARIES_DEPENDENCY_OK_BUTTON);
					waitForElementPresent(uiConstants.JSLIBRARIES_DEPENDENCY_OK_BUTTON, methodName);
					click();*/
			Thread.sleep(1000);	

			element=getXpathWebElement(uiConstants.JSLIBRARIES_SAVE_BUTTON);
			waitForElementPresent(uiConstants.JSLIBRARIES_SAVE_BUTTON, methodName);
			click();
			//waitForElementPresent(uiConstants.PROJCREATIONSUCCESSMSG,methodName);
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);



		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}
	
	
	public  void RC_JSCreateSampleMobileLayerArchetypes(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testReusableComponentsAddValidJSlibrariesPage::******executing addValidJSLibraries scenario****");
		try {
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();
			Thread.sleep(3000);
   
			waitForElementPresent(uiConstants.CUSTOMER_DROPDOWN_VALUE,methodName);
			getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN_VALUE).click();
			Thread.sleep(15000);
			waitForTextPresent("Android Hybrid");
			try {

				if(methodName == "testPHPCreateApplicationLayerArchetypesPage" || methodName == "testDrupal6CreateApplicationLayerArchetypesPage" || methodName == "testDrupal7CreateApplicationLayerArchetypesPage"|| methodName == "testWordPressCreateApplicationLayerArchetypesPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_PHP_TECHNOLOGY_VALUE).click();
					Thread.sleep(2000);
				}
				if(methodName == "testDOTNETCreateApplicationLayerArchetypesPage" || methodName == "testSharePointCreateApplicationLayerArchetypesPage" || methodName == "testSiteCoreCreateApplicationLayerArchetypesPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DOTNET_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testNodejsCreateApplicationLayerArchetypesPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_NODEJS_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testJavaStandaloneCreateApplicationLayerArchetypesPage" || methodName == "testJavaWebServiceCreateApplicationLayerArchetypesPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_JAVASTANDALONE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testiPhoneHybridCreateMobileLayerArchetypes" || methodName == "testiphoneNativeCreateMobileLayerArchetypes"|| methodName == "testiPhoneLibraryCreateMobileLayerArchetypes"|| methodName == "testiPhoneWorkSpaceCreateMobileLayerArchetypes")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);             
				}
				if(methodName == "testAndroidHybridCreateMobileLayerArchetypes" || methodName == "testAndroidNativeCreateMobileLayerArchetypes"|| methodName == "testAndroidLibraryCreateMobileLayerArchetypes")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testWindowsMetroCreateMobileLayerArchetypes" || methodName == "testWindowsPhoneCreateMobileLayerArchetypes")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSMETRO_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);           
				}
				if(methodName == "testBlackBerryHybridCreateMobileLayerArchetypes")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}

			} catch (Exception e) {

				e.printStackTrace();
			}		
		

		     
			
			Thread.sleep(3000);
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENTS_JSLIBRARIES_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENTS_JSLIBRARIES_TAB,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_ADD_BUTTON);
			waitForElementPresent(uiConstants.JSLIBRARIES_ADD_BUTTON,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_NAME);
			waitForElementPresent(uiConstants.JSLIBRARIES_NAME,methodName);
			//type(adminUIConstants.ADD_JSLIBRARIES_NAME_VALUE);
			type(methodName);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_DESCRIPTION);
			waitForElementPresent(uiConstants.JSLIBRARIES_DESCRIPTION,methodName);
			type(adminUIConstants.ADD_JSLIBRARIES_DESCRIPTION_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_HELPTEXT);
			waitForElementPresent(uiConstants.JSLIBRARIES_HELPTEXT,methodName);
			type(adminUIConstants.ADD_JSLIBRARIES_HELPTEXT_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.RC_SELECTMODULETECHNOLOGY);
			waitForElementPresent(uiConstants.RC_SELECTMODULETECHNOLOGY, methodName);
			click();
			Thread.sleep(3000);

			//AddingTechnology(methodName);
			//Thread.sleep(4000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_LICENSE_TYPE);
			waitForElementPresent(uiConstants.JSLIBRARIES_LICENSE_TYPE, methodName);
			click();
			Thread.sleep(3000);

			type(uiConstants.ADD_JSLIBRARIES_LICENSE_TYPE);
			click();
			Thread.sleep(8000);

			System.out.println("--------------Upload File----------");
			driver.findElement(By.name("file")).sendKeys("C:\\Documents and Settings\\aruna_k\\Desktop\\helloworldTest.js");

			Thread.sleep(4000);
			System.out.println("--------------Upload File----------");

			element=getXpathWebElement(uiConstants.JS_TECHNOLOGY_VERSION);
			waitForElementPresent(uiConstants.JS_TECHNOLOGY_VERSION, methodName);
			click();
			type(adminUIConstants.JS_TECHNOLOGY_VERSION_TECHNOLGY_VERSION_VALUE);

			/*Runtime.getRuntime().exec("C://Documents and Settings/srividya_su/Desktop/site_uploadfile.exe");
					Thread.sleep(4000);*/

			//SELECT DEPENDENCY CURRENTLY NOT WORKING

			/*element=getXpathWebElement(uiConstants.JSLIBRARIES_SELECT_DEPENDENCY);
					waitForElementPresent(uiConstants.JSLIBRARIES_SELECT_DEPENDENCY, methodName);
					click();
					Thread.sleep(3000);

					element=getXpathWebElement(uiConstants.JSLIBRARIES_SELECT_JSON_CHECKBOX);
					waitForElementPresent(uiConstants.JSLIBRARIES_SELECT_JSON_CHECKBOX, methodName);
					click();

					element=getXpathWebElement(uiConstants.JSLIBRARIES_DEPENDENCY_OK_BUTTON);
					waitForElementPresent(uiConstants.JSLIBRARIES_DEPENDENCY_OK_BUTTON, methodName);
					click();*/
			Thread.sleep(1000);	

			element=getXpathWebElement(uiConstants.JSLIBRARIES_SAVE_BUTTON);
			waitForElementPresent(uiConstants.JSLIBRARIES_SAVE_BUTTON, methodName);
			click();
			//waitForElementPresent(uiConstants.PROJCREATIONSUCCESSMSG,methodName);
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);
			Thread.sleep(1000);



		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}



	public  void updateJSLibraries(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testComponentsFeatureJSlibrariesPage::******executing updateJSLibraries scenario****");
		try {
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.UPDATE_JSLIBRARIES_LINK_XPATH);
			waitForElementPresent(uiConstants.UPDATE_JSLIBRARIES_LINK_XPATH,methodName);
			click();

			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.UPDATE_JSLIBRARIES_OPTION_BUTTON_XPATH);
			waitForElementPresent(uiConstants.UPDATE_JSLIBRARIES_OPTION_BUTTON_XPATH,methodName);
			click();

			element=getXpathWebElement(uiConstants.JSLIBRARIES_NAME);
			waitForElementPresent(uiConstants.JSLIBRARIES_NAME,methodName);
			type(adminUIConstants.UPDATE_FEATURE_JSLIBRARIES_NAME_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_DESCRIPTION);
			waitForElementPresent(uiConstants.JSLIBRARIES_DESCRIPTION,methodName);
			type(adminUIConstants.UPDATE_FEATURE_JSLIBRARIES_DESCRIPTION_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_HELPTEXT);
			waitForElementPresent(uiConstants.JSLIBRARIES_HELPTEXT,methodName);
			type(adminUIConstants.UPDATE_FEATURE_JSLIBRARIES_HELP_TEXT_VALUE);
			Thread.sleep(1000);



			element=getXpathWebElement(uiConstants.UPDATE_JSLIBRARIES_UPDATE_BUTTON_XPATH);
			waitForElementPresent(uiConstants.UPDATE_JSLIBRARIES_UPDATE_BUTTON_XPATH,methodName);
			click();

		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}

	public  void deleteJSLibraries(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testComponentsFeaturePage::******executing deleteJSLibraries scenario****");
		try {

			//element=getXpathWebElement(uiConstants.DELETE_JSLIBRARIES_XPATH);
			waitForElementPresent(uiConstants.DELETE_JSLIBRARIES_XPATH,methodName);
			click();


		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}

	public  void addEmptyComponent(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testReusableComponentsAddEmptyComponentPage::******executing addEmptyComponent scenario****");
		try {
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENTS_COMPONENT_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENTS_COMPONENT_TAB,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.COMPONENTS_ADD_BUTTON);
			waitForElementPresent(uiConstants.COMPONENTS_ADD_BUTTON,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.COMPONENTS_NAME);
			waitForElementPresent(uiConstants.COMPONENTS_NAME,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.COMPONENTS_DESCRIPTION);
			waitForElementPresent(uiConstants.COMPONENTS_DESCRIPTION,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.COMPONENTS_HELPTEXT);
			waitForElementPresent(uiConstants.COMPONENTS_HELPTEXT,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.COMPONENTS_SAVE_BUTTON);
			waitForElementPresent(uiConstants.COMPONENTS_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(3000);


			isTextPresent(adminUIConstants.EMPTY_ADD_COMPONENT_NAME_MSG);
			isTextPresent(adminUIConstants.EMPTY_ADD_COMPONENT_UPLOAD_FILE_MSG);
			isTextPresent(adminUIConstants.EMPTY_ADD_COMPONENT_LICENSE_TYPE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.COMPONENTS_CANCEL_BUTTON);
			waitForElementPresent(uiConstants.COMPONENTS_CANCEL_BUTTON, methodName);
			click();
			Thread.sleep(1000);

		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}



	public  void addInValidComponent(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testReusableComponentsAddEmptyComponentPage::******executing addInValidComponent scenario****");
		try {
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENTS_COMPONENT_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENTS_COMPONENT_TAB,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.COMPONENTS_ADD_BUTTON);
			waitForElementPresent(uiConstants.COMPONENTS_ADD_BUTTON,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.COMPONENTS_NAME);
			waitForElementPresent(uiConstants.COMPONENTS_NAME,methodName);
			click();
			Thread.sleep(1000);
			type(adminUIConstants.ADD_COMPONENT_NAME_VALUE);
			Thread.sleep(2000);


			element=getXpathWebElement(uiConstants.COMPONENTS_DESCRIPTION);
			waitForElementPresent(uiConstants.COMPONENTS_DESCRIPTION,methodName);
			click();
			Thread.sleep(1000);
			type(adminUIConstants.ADD_COMPONENT_DESCRIPTION_VALUE);
			Thread.sleep(2000);

			element=getXpathWebElement(uiConstants.COMPONENTS_HELPTEXT);
			waitForElementPresent(uiConstants.COMPONENTS_HELPTEXT,methodName);
			click();
			Thread.sleep(1000);
			type(adminUIConstants.ADD_COMPONENT_HELPTEXT_VALUE);
			Thread.sleep(2000);

			element=getXpathWebElement(uiConstants.COMPONENTS_TECHNOLOGY);
			waitForElementPresent(uiConstants.COMPONENTS_TECHNOLOGY,methodName);
			click();
			Thread.sleep(1000);
			type(adminUIConstants.ADD_COMPONENT_PHP_TECHONOLGY_VALUE);
			Thread.sleep(2000);

			element=getXpathWebElement(uiConstants.COMPONENTS_LICENSE_TYPE);
			waitForElementPresent(uiConstants.COMPONENTS_LICENSE_TYPE,methodName);
			click();
			Thread.sleep(1000);
			type(adminUIConstants.ADD_COMPONENT_LICENSE_TYPE);
			Thread.sleep(2000);

			element=getXpathWebElement(uiConstants.COMPONENTS_SELECT_DEPENDENCY);
			waitForElementPresent(uiConstants.COMPONENTS_SELECT_DEPENDENCY,methodName);
			click();
			Thread.sleep(1000);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.COMPONENTS_DEPENDENCY_OK_BUTTON);
			waitForElementPresent(uiConstants.COMPONENTS_DEPENDENCY_OK_BUTTON,methodName);
			click();
			Thread.sleep(1000);




			element=getXpathWebElement(uiConstants.COMPONENTS_SAVE_BUTTON);
			waitForElementPresent(uiConstants.COMPONENTS_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(3000);



			isTextPresent(adminUIConstants.EMPTY_ADD_COMPONENT_UPLOAD_FILE_MSG);

			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.COMPONENTS_CANCEL_BUTTON);
			waitForElementPresent(uiConstants.COMPONENTS_CANCEL_BUTTON, methodName);
			click();
			Thread.sleep(1000);

		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}



	public  void addValidComponent(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testReusableComponentsAddEmptyComponentPage::******executing addValidComponent scenario****");
		try {
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();
			Thread.sleep(3000);

			System.out.println("----------Components Completed----------");
   
			waitForElementPresent(uiConstants.CUSTOMER_DROPDOWN_VALUE,methodName);
			getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN_VALUE).click();
			Thread.sleep(15000);
			waitForTextPresent("Android Hybrid");
		     
			
			
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENT_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENT_TAB,methodName);
			click();
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENTS_COMPONENT_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENTS_COMPONENT_TAB,methodName);
			click();
			Thread.sleep(1000);
			
			
			try {

				if(methodName == "testDeletePHPModules" || methodName == "testPHPReusableComponentsAddModulesPage" || methodName == "testPHPReusableComponentsAddExternalModules"|| methodName == "testDeletePHPJSLibraries" || methodName =="testPHPReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_PHP_TECHNOLOGY_VALUE).click();
					Thread.sleep(2000);
				}
				if(methodName == "testDeleteDrupal6Modules" || methodName == "testDrupal6ReusableComponentsAddModulesPage" || methodName == "testDrupal6ReusableComponentsAddExternalModules" ||  methodName == "testDeleteDrupal6JSLibraries" || methodName == "testDrupal6ReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteDrupal6Components" || methodName == "testDrupal6ReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DRUPAL6_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteNodejsModules" || methodName == "testNodejsReusableComponentsAddModulesPage" || methodName == "testNodejsReusableComponentsAddExternalModules" || methodName == "testDeleteNodejsJSLibraries" || methodName == "testNodejsReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteNodejsComponents" || methodName == "testNodejsReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_NODEJS_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteJavaStandaloneModules" || methodName == "testJavaStandaloneReusableComponentsAddModulesPage" || methodName == "testJavaStandaloneReusableComponentsAddExternalModules" || methodName == "testDeleteJavaStandaloneJSLibraries" || methodName == "testJavaStandaloneReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteJavaStandaloneComponents" || methodName == "testJavaStandaloneReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_JAVASTANDALONE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteiPhoneHybridModules" || methodName == "testiPhoneHybridReusableComponentsAddModulesPage"|| methodName == "testiPhoneHybridReusableComponentsAddExternalModules"|| methodName == "testDeleteiPhoneHybridJSLibraries" || methodName == "testiPhoneHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiPhoneHybridComponents" || methodName == "testiPhoneHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);             
				}
				if(methodName == "testDeleteAndroidHybridModules" || methodName == "testAndroidHybridReusableComponentsAddModulesPage"|| methodName == "testAndroidHybridReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidHybridJSLibraries" || methodName == "testAndroidHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidHybridComponents" || methodName == "testAndroidHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWindowsMetroModules" || methodName == "testWindowsMetroReusableComponentsAddModulesPage"|| methodName == "testWindowsMetroReusableComponentsAddExternalModules" || methodName == "testDeleteWindowsMetroJSLibraries" ||methodName == "testWindowsMetroReusableComponentsAddJSlibrariesPage" || methodName =="testDeleteWindowsMetroComponents" || methodName == "testWindowsMetroReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSMETRO_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);           
				}
				if(methodName == "testDeleteBlackBerryHybridModules" ||methodName == "testBlackBerryHybridReusableComponentsAddModulesPage" || methodName == "testBlackBerryHybridReusableComponentsAddExternalModules" || methodName == "testDeleteBlackBerryHybridJSLibraries" || methodName == "testBlackBerryHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteBlackBerryHybridComponents" || methodName == "testBlackBerryHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteAndroidLibraryModules" ||methodName == "testAndroidLibraryReusableComponentsAddModulesPage" || methodName == "testAndroidLibraryReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidLibraryJSLibraries" || methodName == "testAndroidLibraryReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidLibraryComponents" || methodName == "testAndroidLibraryReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDLIBRARY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteAndroidNativeModules" ||methodName == "testAndroidNativeReusableComponentsAddModulesPage" || methodName == "testAndroidNativeReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidNativeJSLibraries" || methodName == "testAndroidNativeReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidNativeComponents" || methodName == "testAndroidNativeReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteASP_DotnetModules" ||methodName == "testDOTNETReusableComponentsAddModulesPage" || methodName == "testDOTNETReusableComponentsAddExternalModules" || methodName == "testDeleteASP_DotnetJSLibraries" || methodName == "testDOTNETReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteASP_dotnetComponents" || methodName == "testDOTNETReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DOTNET_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteDrupal7Modules" ||methodName == "testDrupal7ReusableComponentsAddModulesPage" || methodName == "testDrupal7ReusableComponentsAddExternalModules" || methodName == "testDeleteDrupal7JSLibraries" || methodName == "testDrupal7ReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteDrupal7Components" || methodName == "testDrupal7ReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DRUPAL7_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteJQueryMobileModules" ||methodName == "testJQueryMobileReusableComponentsAddModulesPage" || methodName == "testJQueryMobileReusableComponentsAddExternalModules" || methodName == "testDeleteJQueryMobileJSLibraries" || methodName == "testJQueryMobileReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteJQueryMobileComponents" || methodName == "testJQueryMobileReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_JQUERYMOBILE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteMultiJQueryModules" ||methodName == "testMultiJQueryReusableComponentsAddModulesPage" || methodName == "testMultiJQueryReusableComponentsAddExternalModules" || methodName == "testDeleteMultiJQueryJSLibraries" || methodName == "testMultiJQueryReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteMultiJQueryComponents" || methodName == "testMultiJQueryReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_MULTIJQUERY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);                 
				}
				if(methodName == "testDeleteMultiYUIModules" ||methodName == "testMultiYUIReusableComponentsAddModulesPage" || methodName == "testMultiYUIReusableComponentsAddExternalModules" || methodName == "testDeleteMultiYUIJSLibraries" || methodName == "testMultiYUIReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteMultiYUIComponents" || methodName == "testMultiYUIReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_MULTIYUI_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteYUIMobileModules" ||methodName == "testYUIMobileReusableComponentsAddModulesPage" || methodName == "testYUIMobileReusableComponentsAddExternalModules" || methodName == "testDeleteYUIMobileJSLibraries" || methodName == "testYUIMobileReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteYUIMobileComponents" || methodName == "testYUIMobileReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_YUIMOBILE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiPhoneLibraryModules" ||methodName == "testiPhoneLibraryReusableComponentsAddModulesPage" || methodName == "testiPhoneLibraryReusableComponentsAddExternalModules" || methodName == "testDeleteiPhoneLibraryJSLibraries" || methodName == "testiPhoneLibraryReusableComponentsAddJSlibrariesPage" || methodName == "testiPhoneLibraryReusableComponentsAddvalidComponentPage" || methodName == "testDeleteiPhoneLibraryComponents")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONELIBRARY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiphoneNativeModules" ||methodName == "testiphoneNativeReusableComponentsAddModulesPage" || methodName == "testiphoneNativeReusableComponentsAddExternalModules" || methodName == "testDeleteiphoneNativeJSLibraries" || methodName == "testiphoneNativeReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiphoneNativeComponents" || methodName == "testiphoneNativeReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONENATIVE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiPhoneWorkSpaceModules" ||methodName == "testiPhoneWorkSpaceReusableComponentsAddModulesPage" || methodName == "testiPhoneWorkSpaceReusableComponentsAddExternalModules" || methodName == "testDeleteiPhoneWorkSpaceJSLibraries" || methodName == "testiPhoneWorkSpaceReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiPhoneWorkSpaceComponents" || methodName == "testiPhoneWorkSpaceReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEWORKSAPCE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteSharePointModules" ||methodName == "testSharePointReusableComponentsAddModulesPage" || methodName == "testSharePointReusableComponentsAddExternalModules" || methodName == "testDeleteSharePointJSLibraries" || methodName == "testSharePointReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteSharePointComponents" || methodName == "testSharePointReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_SHAREPOINT_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteSiteCoreModules" ||methodName == "testSiteCoreReusableComponentsAddModulesPage" || methodName == "testSiteCoreReusableComponentsAddExternalModules" || methodName == "testDeleteSiteCoreJSLibraries" || methodName == "testSiteCoreReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteSiteCoreComponents" || methodName == "testSiteCoreReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_SITECORE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWindowsPhoneModules" ||methodName == "testWindowsPhoneReusableComponentsAddModulesPage" || methodName == "testWindowsPhoneReusableComponentsAddExternalModules" || methodName == "testDeleteWindowsPhoneJSLibraries" || methodName == "testWindowsPhoneReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteWindowsPhoneComponents" || methodName == "testWindowsPhoneReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSPHONE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWordPressModules" ||methodName == "testWordPressReusableComponentsAddModulesPage" || methodName == "testWordPressReusableComponentsAddExternalModules" || methodName == "testDeleteWordPressJSLibraries" || methodName == "testWordPressReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteWordPressComponents" || methodName == "testWordPressReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WORDPRESS_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				

			} catch (Exception e) {

				e.printStackTrace();
			}		


			element=getXpathWebElement(uiConstants.COMPONENTS_ADD_BUTTON);
			waitForElementPresent(uiConstants.COMPONENTS_ADD_BUTTON,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.COMPONENTS_NAME);
			waitForElementPresent(uiConstants.COMPONENTS_NAME,methodName);
			click();
			Thread.sleep(1000);
			//type(adminUIConstants.ADD_COMPONENT_NAME_VALUE);
			type(methodName);
			Thread.sleep(2000);


			element=getXpathWebElement(uiConstants.COMPONENTS_DESCRIPTION);
			waitForElementPresent(uiConstants.COMPONENTS_DESCRIPTION,methodName);
			click();
			Thread.sleep(1000);
			type(adminUIConstants.ADD_COMPONENT_DESCRIPTION_VALUE);
			Thread.sleep(2000);

			element=getXpathWebElement(uiConstants.COMPONENTS_HELPTEXT);
			waitForElementPresent(uiConstants.COMPONENTS_HELPTEXT,methodName);
			click();
			Thread.sleep(1000);
			type(adminUIConstants.ADD_COMPONENT_HELPTEXT_VALUE);
			Thread.sleep(2000);

			/*element=getXpathWebElement(uiConstants.COMPONENTS_TECHNOLOGY);
						waitForElementPresent(uiConstants.COMPONENTS_TECHNOLOGY,methodName);
						click();*/
			//Thread.sleep(6000);

			AddingTechnology(methodName);

			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.COMPONENTS_LICENSE_TYPE);
			waitForElementPresent(uiConstants.COMPONENTS_LICENSE_TYPE,methodName);
			click();
			Thread.sleep(3000);
			type(adminUIConstants.ADD_COMPONENT_LICENSE_TYPE);
			click();
			Thread.sleep(8000);

			System.out.println("--------------Upload File----------");
			Thread.sleep(8000);
			driver.findElement(By.name("file")).sendKeys("C:\\Documents and Settings\\kalish_ma\\Desktop\\jars\\tech-android-hybrid-2.0.0.34000.zip");
			Thread.sleep(10000);
			System.out.println("--------------Upload File----------");

			/*Runtime.getRuntime().exec("C://Documents and Settings/srividya_su/Desktop/site_uploadfile.exe");
						Thread.sleep(10000);*/

			//SELECT DEPENDENCY CURRENTLY NOT WORKING

			/*element=getXpathWebElement(uiConstants.COMPONENTS_SELECT_DEPENDENCY);
						waitForElementPresent(uiConstants.COMPONENTS_SELECT_DEPENDENCY,methodName);
						click();

						Thread.sleep(2000);
						element=getXpathWebElement(uiConstants.COMPONENTS_DEPENDENCY_OK_BUTTON);
						waitForElementPresent(uiConstants.COMPONENTS_DEPENDENCY_OK_BUTTON,methodName);
						click();*/
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.COMPONENTS_SAVE_BUTTON);
			waitForElementPresent(uiConstants.COMPONENTS_SAVE_BUTTON, methodName);
			click();
			//waitForElementPresent(uiConstants.PROJCREATIONSUCCESSMSG,methodName);
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);

			/*isTextPresent(adminUIConstants.EMPTY_ADD_COMPONENT_UPLOAD_FILE_MSG);
					    Thread.sleep(1000);

						element=getXpathWebElement(uiConstants.COMPONENTS_CANCEL_BUTTON);
						waitForElementPresent(uiConstants.COMPONENTS_CANCEL_BUTTON, methodName);
						click();
						Thread.sleep(1000);*/

		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}

	public  void RC_COMCreateSampleMobileLayerArchetypes(String methodName) throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testReusableComponentsAddEmptyComponentPage::******executing addValidComponent scenario****");
		try {
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN);
			selectText(element,adminUIConstants.CUSTOMER_DROPDOWN_VALUE);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENTS_COMPONENT_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENTS_COMPONENT_TAB,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.COMPONENTS_ADD_BUTTON);
			waitForElementPresent(uiConstants.COMPONENTS_ADD_BUTTON,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.COMPONENTS_NAME);
			waitForElementPresent(uiConstants.COMPONENTS_NAME,methodName);
			click();
			Thread.sleep(1000);
			//type(adminUIConstants.ADD_COMPONENT_NAME_VALUE);
			type(methodName);
			Thread.sleep(2000);


			element=getXpathWebElement(uiConstants.COMPONENTS_DESCRIPTION);
			waitForElementPresent(uiConstants.COMPONENTS_DESCRIPTION,methodName);
			click();
			Thread.sleep(1000);
			type(adminUIConstants.ADD_COMPONENT_DESCRIPTION_VALUE);
			Thread.sleep(2000);

			element=getXpathWebElement(uiConstants.COMPONENTS_HELPTEXT);
			waitForElementPresent(uiConstants.COMPONENTS_HELPTEXT,methodName);
			click();
			Thread.sleep(1000);
			type(adminUIConstants.ADD_COMPONENT_HELPTEXT_VALUE);
			Thread.sleep(2000);

			element=getXpathWebElement(uiConstants.RC_SELECTMODULETECHNOLOGY);
			waitForElementPresent(uiConstants.RC_SELECTMODULETECHNOLOGY,methodName);
			click();
			//Thread.sleep(6000);

			//AddingTechnology(methodName);

			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.COMPONENTS_LICENSE_TYPE);
			waitForElementPresent(uiConstants.COMPONENTS_LICENSE_TYPE,methodName);
			click();
			Thread.sleep(3000);
			type(adminUIConstants.ADD_COMPONENT_LICENSE_TYPE);
			click();
			Thread.sleep(8000);

			System.out.println("--------------Upload File----------");
			driver.findElement(By.name("file")).sendKeys("C:\\Documents and Settings\\aruna_k\\Desktop\\archetype.jar");

			Thread.sleep(4000);
			System.out.println("--------------Upload File----------");

			/*Runtime.getRuntime().exec("C://Documents and Settings/srividya_su/Desktop/site_uploadfile.exe");
						Thread.sleep(10000);*/

			//SELECT DEPENDENCY CURRENTLY NOT WORKING

			/*element=getXpathWebElement(uiConstants.COMPONENTS_SELECT_DEPENDENCY);
						waitForElementPresent(uiConstants.COMPONENTS_SELECT_DEPENDENCY,methodName);
						click();

						Thread.sleep(2000);
						element=getXpathWebElement(uiConstants.COMPONENTS_DEPENDENCY_OK_BUTTON);
						waitForElementPresent(uiConstants.COMPONENTS_DEPENDENCY_OK_BUTTON,methodName);
						click();*/
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.COMPONENTS_SAVE_BUTTON);
			waitForElementPresent(uiConstants.COMPONENTS_SAVE_BUTTON, methodName);
			click();
			//waitForElementPresent(uiConstants.PROJCREATIONSUCCESSMSG,methodName);
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);

			/*isTextPresent(adminUIConstants.EMPTY_ADD_COMPONENT_UPLOAD_FILE_MSG);
					    Thread.sleep(1000);

						element=getXpathWebElement(uiConstants.COMPONENTS_CANCEL_BUTTON);
						waitForElementPresent(uiConstants.COMPONENTS_CANCEL_BUTTON, methodName);
						click();*/
			Thread.sleep(1000);

		} catch (InterruptedException e) {

			e.printStackTrace();
		}		
	}


	public void createEmptyApplicationType(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testComponentsApplicationTypesPage::******executing createEmptyApplicationType scenario****");
		try {
			Thread.sleep(3000);
			element=getXpathWebElement(uiConstants.APPLICATION_TYPES_TAB);
			waitForElementPresent(uiConstants.APPLICATION_TYPES_TAB, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.APPLICATION_TYPES_CREATE);
			waitForElementPresent(uiConstants.APPLICATION_TYPES_CREATE, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.APPLICATION_TYPE_NAME);
			waitForElementPresent(uiConstants.APPLICATION_TYPE_NAME, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.APPLICATION_TYPE_DESCRIPTION);
			waitForElementPresent(uiConstants.APPLICATION_TYPE_DESCRIPTION, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.APPLICATION_TYPE_SAVE_BUTTON);
			waitForElementPresent(uiConstants.APPLICATION_TYPE_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(1000);
			//isTextPresent(adminUIConstants.CREATE_EMPTY_APPLICATION_TYPE_NAME_ERROR_MSG);

			element=getXpathWebElement(uiConstants.APPLICATION_TYPE_CANCEL_BUTTON);
			waitForElementPresent(uiConstants.APPLICATION_TYPE_CANCEL_BUTTON, methodName);
			click();
			Thread.sleep(1000);



		} catch (Exception e) {

			e.printStackTrace();
		}

	}


	public void createValidApplicationType(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testComponentsApplicationTypesPage::******executing createValidApplicationType scenario****");
		try {
			Thread.sleep(3000);
			element=getXpathWebElement(uiConstants.APPLICATION_TYPES_TAB);
			waitForElementPresent(uiConstants.APPLICATION_TYPES_TAB, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.APPLICATION_TYPES_CREATE);
			waitForElementPresent(uiConstants.APPLICATION_TYPES_CREATE, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.APPLICATION_TYPE_NAME);
			waitForElementPresent(uiConstants.APPLICATION_TYPE_NAME, methodName);
			type(adminUIConstants.CREATE_APPLICATION_TYPE_NAME_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.APPLICATION_TYPE_DESCRIPTION);
			waitForElementPresent(uiConstants.APPLICATION_TYPE_DESCRIPTION, methodName);
			type(adminUIConstants.CREATE_APPLICATION_TYPE_DESCRIPTION_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.APPLICATION_TYPE_SAVE_BUTTON);
			waitForElementPresent(uiConstants.APPLICATION_TYPE_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.APPLICATION_TYPE_CANCEL_BUTTON);
			waitForElementPresent(uiConstants.APPLICATION_TYPE_CANCEL_BUTTON, methodName);
			click();
			Thread.sleep(1000);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void updateApplicationType(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testComponentsApplicationTypesPage::******executing updateApplicationType scenario****");
		try {
			Thread.sleep(3000);
			element=getXpathWebElement(uiConstants.UPDATE_APPLICATION_TYPES_LINK);
			waitForElementPresent(uiConstants.UPDATE_APPLICATION_TYPES_LINK, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.UPDATE_APPLICATION_TYPES_DESCRIPTION);
			waitForElementPresent(uiConstants.UPDATE_APPLICATION_TYPES_DESCRIPTION, methodName);
			type(adminUIConstants.UPDATE_APPLICATION_TYPE_DESCRIPTION_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.UPDATE_APPLICATION_TYPES_UPDATE_BUTTON);
			waitForElementPresent(uiConstants.UPDATE_APPLICATION_TYPES_UPDATE_BUTTON, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.APPLICATION_TYPE_CANCEL_BUTTON);
			waitForElementPresent(uiConstants.APPLICATION_TYPE_CANCEL_BUTTON, methodName);
			click();
			Thread.sleep(1000);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}


	public void createValidCustomer(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testAdminCustomerPage::******executing createValidCustomer scenario****");
		try {

			Thread.sleep(3000);
			element=getXpathWebElement(uiConstants.ADMIN_LINK);
			waitForElementPresent(uiConstants.ADMIN_LINK,methodName);
			click();
			Thread.sleep(30000);
			waitForTextPresent("Customers");

			waitForElementPresent(uiConstants.ADMIN_CUSTOMER_TAB, methodName);
			element=getXpathWebElement(uiConstants.ADMIN_CUSTOMER_TAB);
			click();
			Thread.sleep(10000);

			waitForElementPresent(uiConstants.CUSTOMER_CREATE_BUTTON, methodName);
			element=getXpathWebElement(uiConstants.CUSTOMER_CREATE_BUTTON);
			click();
			Thread.sleep(3000);

			waitForElementPresent(uiConstants.CUSTOMER_NAME, methodName);
			element=getXpathWebElement(uiConstants.CUSTOMER_NAME);
			type(adminUIConstants.CREATE_CUSTOMER_NAME_VALUE);
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.CUSTOMER_DESCRIPTION);
			waitForElementPresent(uiConstants.CUSTOMER_DESCRIPTION, methodName); 
			type(adminUIConstants.CREATE_CUSTOMER_DESCRIPTION_VALUE);
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.CUSTOMER_EMAIL);
			waitForElementPresent(uiConstants.CUSTOMER_EMAIL, methodName);
			type(adminUIConstants.CREATE_CUSTOMER_EMAIL_VALUE);
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.CUSTOMER_ADDRESS);
			waitForElementPresent(uiConstants.CUSTOMER_ADDRESS, methodName);
			type(adminUIConstants.CREATE_CUSTOMER_ADDRESS_VALUE);
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.CUSTOMER_COUNTRY);
			waitForElementPresent(uiConstants.CUSTOMER_COUNTRY, methodName);
			click();
			Thread.sleep(6000);
			type(adminUIConstants.CREATE_CUSTOMER_COUNTRY_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_STATE);
			waitForElementPresent(uiConstants.CUSTOMER_STATE, methodName);
			type(adminUIConstants.CREATE_CUSTOMER_STATE_VALUE);
			Thread.sleep(1000); 

			element=getXpathWebElement(uiConstants.CUSTOMER_ZIPCODE);
			waitForElementPresent(uiConstants.CUSTOMER_ZIPCODE, methodName);
			type(adminUIConstants.CREATE_CUSTOMER_ZIPCODE_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_CONTACT_NUMBER);
			waitForElementPresent(uiConstants.CUSTOMER_CONTACT_NUMBER, methodName);
			type(adminUIConstants.CREATE_CUSTOMER_CONTACT_NUMBER_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_FAX);
			waitForElementPresent(uiConstants.CUSTOMER_FAX, methodName);
			type(adminUIConstants.CREATE_CUSTOMER_FAX_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_HELP_TEXT);
			waitForElementPresent(uiConstants.CUSTOMER_HELP_TEXT, methodName);
			type(adminUIConstants.CREATE_CUSTOMER_HELPTEXT_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_LICENCE_TYPE);
			waitForElementPresent(uiConstants.CUSTOMER_LICENCE_TYPE, methodName);
			click();
			Thread.sleep(1000);
			type(adminUIConstants.CREATE_CUSTOMER_LICENCE_TYPE_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_FROM);
			waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_FROM, methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_FROM_FRONT_ARROW);
			waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_FROM_FRONT_ARROW, methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_FROM_DATE);
			waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_FROM_DATE, methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_UPTO);
			waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_UPTO, methodName);
			click();
			Thread.sleep(2000);

			for(int i=0;i<=9;i++)
			{				 
				element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_UPTO_FRONT_ARROW);
				waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_UPTO_FRONT_ARROW, methodName);
				click();
				Thread.sleep(1000);				 
			}

			element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_UPTO_DATE);
			waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_UPTO_DATE, methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.CUSTOMER_REPONAME);
			waitForElementPresent(uiConstants.CUSTOMER_REPONAME, methodName);
			click();
			Thread.sleep(1000);
			type(adminUIConstants.CREATE_CUSTOMER_REPONAME);
			Thread.sleep(1000);

			/*element=getXpathWebElement(uiConstants.CUSTOMER_REPOURL);
				 waitForElementPresent(uiConstants.CUSTOMER_REPOURL, methodName);
				 click();
				 Thread.sleep(1000);
				 type(adminUIConstants. CREATE_CUSTOMER_REPOURL);
				 Thread.sleep(1000);*/
				 element=getXpathWebElement(uiConstants.CUSTOMER_APPLIESTOCHECKBOX);
				 waitForElementPresent(uiConstants.CUSTOMER_APPLIESTOCHECKBOX, methodName);
				 click();
				 Thread.sleep(1000);

				 element=getXpathWebElement(uiConstants.CUSTOMER_SAVE_BUTTON);
				 waitForElementPresent(uiConstants.CUSTOMER_SAVE_BUTTON, methodName);
				 click();
				 Thread.sleep(6000);

				 element=getXpathWebElement(uiConstants.CUSTOMER_CANCEL_BUTTON);
				 waitForElementPresent(uiConstants.CUSTOMER_CANCEL_BUTTON, methodName);
				 click();
				 Thread.sleep(1000);


		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void updateCustomer(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testAdminCustomerPage::******executing updateCustomer scenario****");
		try {

			element=getXpathWebElement(uiConstants.UPDATE_CUSTOMER_LINK);
			waitForElementPresent(uiConstants.UPDATE_CUSTOMER_LINK, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_NAME);
			waitForElementPresent(uiConstants.CUSTOMER_NAME, methodName);
			type(adminUIConstants.UPDATE_CUSTOMER_NAME_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_DESCRIPTION);
			waitForElementPresent(uiConstants.CUSTOMER_DESCRIPTION, methodName); 
			type(adminUIConstants.UPDATE_CUSTOMER_DESCRIPTION_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_EMAIL);
			waitForElementPresent(uiConstants.CUSTOMER_EMAIL, methodName);
			type(adminUIConstants.UPDATE_CUSTOMER_EMAIL_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_ADDRESS);
			waitForElementPresent(uiConstants.CUSTOMER_ADDRESS, methodName);
			type(adminUIConstants.UPDATE_CUSTOMER_ADDRESS_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_HELP_TEXT);
			waitForElementPresent(uiConstants.CUSTOMER_HELP_TEXT, methodName);
			type(adminUIConstants.UPDATE_CUSTOMER_HELP_TEXT_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_FROM);
			waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_FROM, methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_FROM_FRONT_ARROW);
			waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_FROM_FRONT_ARROW, methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_FROM_DATE);
			waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_FROM_DATE, methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_UPTO);
			waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_UPTO, methodName);
			click();
			Thread.sleep(2000);

			for(int i=0;i<=9;i++)
			{				 
				element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_UPTO_FRONT_ARROW);
				waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_UPTO_FRONT_ARROW, methodName);
				click();
				Thread.sleep(1000);				 
			}

			element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_UPTO_DATE);
			waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_UPTO_DATE, methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.UPDATE_CUSTOMER_UPDATE_BUTTON);
			waitForElementPresent(uiConstants.UPDATE_CUSTOMER_UPDATE_BUTTON, methodName);
			click();
			Thread.sleep(6000);



		} catch (Exception e) {

			e.printStackTrace();
		}
	}




	public void createInvalidCustomer(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testAdminCustomerPage::******executing createInvalidCustomer scenario****");
		try {

			element=getXpathWebElement(uiConstants.CUSTOMER_CREATE_BUTTON);
			waitForElementPresent(uiConstants.CUSTOMER_CREATE_BUTTON, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_NAME);
			waitForElementPresent(uiConstants.CUSTOMER_NAME, methodName);
			type(adminUIConstants.INVALID_CREATE_CUSTOMER_NAME_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_DESCRIPTION);
			waitForElementPresent(uiConstants.CUSTOMER_DESCRIPTION, methodName); 
			type(adminUIConstants.INVALID_CREATE_CUSTOMER_DESCRIPTION_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_EMAIL);
			waitForElementPresent(uiConstants.CUSTOMER_EMAIL, methodName);
			type(adminUIConstants.INVALID_CREATE_CUSTOMER_EMAIL_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_ADDRESS);
			waitForElementPresent(uiConstants.CUSTOMER_ADDRESS, methodName);
			type(adminUIConstants.INVALID_CREATE_CUSTOMER_ADDRESS_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_COUNTRY);
			waitForElementPresent(uiConstants.CUSTOMER_COUNTRY, methodName);
			click();
			Thread.sleep(3000);
			type(adminUIConstants.INVALID_CREATE_CUSTOMER_COUNTRY_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_STATE);
			waitForElementPresent(uiConstants.CUSTOMER_STATE, methodName);
			type(adminUIConstants.INVALID_CREATE_CUSTOMER_STATE_VALUE);
			Thread.sleep(1000); 

			element=getXpathWebElement(uiConstants.CUSTOMER_ZIPCODE);
			waitForElementPresent(uiConstants.CUSTOMER_ZIPCODE, methodName);
			type(adminUIConstants.INVALID_CREATE_CUSTOMER_ZIPCODE_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_CONTACT_NUMBER);
			waitForElementPresent(uiConstants.CUSTOMER_CONTACT_NUMBER, methodName);
			type(adminUIConstants.INVALID_CREATE_CUSTOMER_CONTACT_NUMBER_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_FAX);
			waitForElementPresent(uiConstants.CUSTOMER_FAX, methodName);
			type(adminUIConstants.INVALID_CREATE_CUSTOMER_FAX_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_HELP_TEXT);
			waitForElementPresent(uiConstants.CUSTOMER_HELP_TEXT, methodName);
			type(adminUIConstants.INVALID_CREATE_CUSTOMER_HELP_TEXT_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_LICENCE_TYPE);
			waitForElementPresent(uiConstants.CUSTOMER_LICENCE_TYPE, methodName);
			click();
			Thread.sleep(1000);
			type(adminUIConstants.INVALID_CREATE_CUSTOMER_LICENCE_TYPE_VALUE);
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_FROM);
			waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_FROM, methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_FROM_FRONT_ARROW);
			waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_FROM_FRONT_ARROW, methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_FROM_DATE);
			waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_FROM_DATE, methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_UPTO);
			waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_UPTO, methodName);
			click();
			Thread.sleep(2000);

			for(int i=0;i<=9;i++)
			{				 
				element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_UPTO_FRONT_ARROW);
				waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_UPTO_FRONT_ARROW, methodName);
				click();
				Thread.sleep(1000);				 
			}

			element=getXpathWebElement(uiConstants.CUSTOMER_CALENDER_VALID_UPTO_DATE);
			waitForElementPresent(uiConstants.CUSTOMER_CALENDER_VALID_UPTO_DATE, methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.CUSTOMER_SAVE_BUTTON);
			waitForElementPresent(uiConstants.CUSTOMER_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(3000);
			isTextPresent(adminUIConstants.ERROR_INVALID_CREATE_CUSTOMER_EMAIL_VALUE);
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.CUSTOMER_CANCEL_BUTTON);
			waitForElementPresent(uiConstants.CUSTOMER_CANCEL_BUTTON, methodName);
			click();
			Thread.sleep(1000);


		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	public void createEmptyCustomer(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		log.info("@testAdminCustomerPage::******executing createEmptyCustomer scenario****");
		try {
			Thread.sleep(3000);
			element=getXpathWebElement(uiConstants.ADMIN_LINK);
			waitForElementPresent(uiConstants.ADMIN_LINK, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.ADMIN_CUSTOMER_TAB);
			waitForElementPresent(uiConstants.ADMIN_CUSTOMER_TAB, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_CREATE_BUTTON);
			waitForElementPresent(uiConstants.CUSTOMER_CREATE_BUTTON, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_NAME);
			waitForElementPresent(uiConstants.CUSTOMER_NAME, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_DESCRIPTION);
			waitForElementPresent(uiConstants.CUSTOMER_DESCRIPTION, methodName); 
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_EMAIL);
			waitForElementPresent(uiConstants.CUSTOMER_EMAIL, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_ADDRESS);
			waitForElementPresent(uiConstants.CUSTOMER_ADDRESS, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_COUNTRY);
			waitForElementPresent(uiConstants.CUSTOMER_COUNTRY, methodName);
			Thread.sleep(2000);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_STATE);
			waitForElementPresent(uiConstants.CUSTOMER_STATE, methodName);
			click();
			Thread.sleep(1000); 

			element=getXpathWebElement(uiConstants.CUSTOMER_ZIPCODE);
			waitForElementPresent(uiConstants.CUSTOMER_ZIPCODE, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_CONTACT_NUMBER);
			waitForElementPresent(uiConstants.CUSTOMER_CONTACT_NUMBER, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_FAX);
			waitForElementPresent(uiConstants.CUSTOMER_FAX, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_HELP_TEXT);
			waitForElementPresent(uiConstants.CUSTOMER_HELP_TEXT, methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_LICENCE_TYPE);
			waitForElementPresent(uiConstants.CUSTOMER_LICENCE_TYPE, methodName);
			Thread.sleep(1000);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_SAVE_BUTTON);
			waitForElementPresent(uiConstants.CUSTOMER_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(6000);

			isTextPresent(adminUIConstants.ERROR_CUSTOMER_NAME_MSG);
			isTextPresent(adminUIConstants.ERROR_CUSTOMER_EMAIL_MSG);
			isTextPresent(adminUIConstants.ERROR_CUSTOMER_ADDRESS_MSG);
			isTextPresent(adminUIConstants.ERROR_CUSTOMER_COUNTRY_MSG);
			isTextPresent(adminUIConstants.ERROR_CUSTOMER_ZIPCODE_MSG);
			isTextPresent(adminUIConstants.ERROR_CUSTOMER_CONTACT_NUMBER_MSG);
			isTextPresent(adminUIConstants.ERROR_CUSTOMER_FAX_NSG);
			isTextPresent(adminUIConstants.ERROR_CUSTOMER_LINCENCE_TYPE_MSG);



			Thread.sleep(10000);
			element=getXpathWebElement(uiConstants.CUSTOMER_CANCEL_BUTTON);
			waitForElementPresent(uiConstants.CUSTOMER_CANCEL_BUTTON, methodName);
			click();
			Thread.sleep(1000);


		} catch (Exception e) {

			e.printStackTrace();
		}
	}






	/*	 public void  createEmptyArchetypes(String methodName)
		 {
		  if (StringUtils.isEmpty(methodName)) {
		      methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		     		 log.info("@testAdminCustomerPage::******executing createEmptyArchetypes scenario****");
		  }
		      try {
		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_TAB);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_TAB, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_CREATE_BUTTON);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_CREATE_BUTTON, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_NAME);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_NAME, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_DESCRIPTION);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_DESCRIPTION, methodName);
		     	 		click();


		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_TECHNOLOGY_VERSION);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_TECHNOLOGY_VERSION, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_VERSION_COMMENT);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_VERSION_COMMENT, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_SAVE_BUTTON);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_SAVE_BUTTON, methodName);
		     	 		click();


		     	 		isTextPresent(adminUIConstants.CREATE_EMPTY_ARCHETYPE_NAME_ERROR_MSG);
		     	 		isTextPresent(adminUIConstants.CREATE_EMPTY_ARCHETYPE_TECHNOLOGY_VERSION_ERROR_MSG);
		     	 		isTextPresent(adminUIConstants.CREATE_EMPTY_ARCHETYPE_JAR_ERROR_MSG);
		     	 		isTextPresent(adminUIConstants.CREATE_EMPTY_ARCHETYPE_FEATURE_ERROR_MSG);





		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_CANCEL_BUTTON);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_CANCEL_BUTTON, methodName);
		     	 		click();

		      }      


		 catch (Exception e) {

		 	e.printStackTrace();
		 }
		 }
	 */

	/* public void  createInvalidArchetypes(String methodName)
		 {
		  if (StringUtils.isEmpty(methodName)) {
		      methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		     		 log.info("@testAdminCustomerPage::******executing createInvalidArchetypes scenario****");
		  }
		      try {

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_CREATE_BUTTON);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_CREATE_BUTTON, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_NAME);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_NAME, methodName);
		     	 		click();
		     	 		type(adminUIConstants.CREATE_WEB_LAYER_ARCHETYPES_NAME_VALUE);


		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_DESCRIPTION);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_DESCRIPTION, methodName);
		     	 		click();
		     	 		type(adminUIConstants.CREATE_WEB_LAYER_ARCHETYPES_DESCRIPTION_VALUE);

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_TECHNOLOGY_VERSION);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_TECHNOLOGY_VERSION, methodName);
		     	 		click();
		     	 		type(adminUIConstants.CREATE_WEB_LAYER_ARCHETYPES_TECHNOLGY_VERSION_VALUE);

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_VERSION_COMMENT);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_VERSION_COMMENT, methodName);
		     	 		click();
		     	 		type(adminUIConstants.CREATE_WEB_LAYER_ARCHETYPES_VERSION_COMMENT_VALUE);

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_TYPE);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_TYPE, methodName);
		     	 		click();
		     	 		type(adminUIConstants.CREATE_WEB_LAYER_ARCHETYPES_APPLICATION_TYPE_VALUE);

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_UPLOAD_JAR);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_UPLOAD_JAR, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_UPLOAD_PLUGIN_JAR);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_UPLOAD_PLUGIN_JAR, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_FEATURES_ALL);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_FEATURES_ALL, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_FEATURES_CODE);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_FEATURES_CODE, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_FEATURES_BUILD);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_FEATURES_BUILD, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_FEATURES_DEPLOY);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_FEATURES_DEPLOY, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_FEATURES_RUN_AGAINST_SOURCE);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_FEATURES_RUN_AGAINST_SOURCE, methodName);
		     	 		click();


		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_FEATURES_UNIT_TEST);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_FEATURES_UNIT_TEST, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_FEATURES_FUNCTONAL_TEST);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_FEATURES_FUNCTONAL_TEST, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_FEATURES_PERFORMANCE_TEST);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_FEATURES_PERFORMANCE_TEST, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_FEATURES_LOAD_TEST);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_FEATURES_LOAD_TEST, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_FEATURES_REPORTS);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_FEATURES_REPORTS, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_FEATURES_CI);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_FEATURES_CI, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_ALL);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_ALL, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_PROJECT_INFO_REPORT);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_PROJECT_INFO_REPORT, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_JAVA_DOC_REPORT);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_JAVA_DOC_REPORT, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_COBERTURA_REPORT);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_COBERTURA_REPORT, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_JDEPEND_REPORT);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_JDEPEND_REPORT, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_JXR_REPORT);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_JXR_REPORT, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_PMD_REPORT);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_PMD_REPORT, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_SURFIRE_REPORT);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_SURFIRE_REPORT, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_SAVE_BUTTON);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_SAVE_BUTTON, methodName);
		     	 		click();

		     	 		Thread.sleep(2000);
		     	 		element=getXpathWebElement(uiConstants.ARCHETYPES_CANCEL_BUTTON);
		     	 		waitForElementPresent(uiConstants.ARCHETYPES_CANCEL_BUTTON, methodName);
		     	 		click();





		      }      


		 catch (Exception e) {

		 	e.printStackTrace();
		 }
		 }


	 */

	public void  createWebLayerArchetypes(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testAdminCustomerPage::******executing createWebLayerArchetypes scenario****");
		}
		try {

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_TAB);
			waitForElementPresent(uiConstants.ARCHETYPES_TAB, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_CREATE_BUTTON);
			waitForElementPresent(uiConstants.ARCHETYPES_CREATE_BUTTON, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_NAME);
			waitForElementPresent(uiConstants.ARCHETYPES_NAME, methodName);
			click();
			//type(adminUIConstants.CREATE_WEB_LAYER_ARCHETYPES_NAME_VALUE);
			type(methodName);


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_DESCRIPTION);
			waitForElementPresent(uiConstants.ARCHETYPES_DESCRIPTION, methodName);
			click();
			type(adminUIConstants.CREATE_WEB_LAYER_ARCHETYPES_DESCRIPTION_VALUE);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_TECHNOLOGY_VERSION);
			waitForElementPresent(uiConstants.ARCHETYPES_TECHNOLOGY_VERSION, methodName);
			click();
			type(adminUIConstants.CREATE_WEB_LAYER_ARCHETYPES_TECHNOLGY_VERSION_VALUE);

			Thread.sleep(2000);
			/*element=getXpathWebElement(uiConstants.ARCHETYPES_VERSION_COMMENT);
    	 		waitForElementPresent(uiConstants.ARCHETYPES_VERSION_COMMENT, methodName);
    	 		click();
    	 		type(adminUIConstants.CREATE_WEB_LAYER_ARCHETYPES_VERSION_COMMENT_VALUE);
			 */
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_TYPE);
			waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_TYPE, methodName);
			click();
			type(adminUIConstants.CREATE_WEB_LAYER_ARCHETYPES_APPLICATION_TYPE_VALUE);
			click();
			Thread.sleep(2000);

			element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_GROUP_TYPE);
			waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_GROUP_TYPE, methodName);
			click();
			Thread.sleep(2000);

			ArcheTypeAddingTechnology(methodName);


			System.out.println("--------------Upload archeytype File----------");
			driver.findElement(By.name("file")).sendKeys("C:\\Documents and Settings\\kalish_ma\\Desktop\\jars\\phresco-drupal7-archetype-2.0.0.34001.jar");

			Thread.sleep(4000);
			System.out.println("--------------Upload archeytype File----------");

			/*element=getXpathWebElement(uiConstants.ARCHETYPES_UPLOAD_JAR);
    	 		waitForElementPresent(uiConstants.ARCHETYPES_UPLOAD_JAR, methodName);
    	 		click();

    	 		Thread.sleep(2000);*/
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_FEATURES_ALL);
			waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_FEATURES_ALL, methodName);
			click();
			Thread.sleep(2000);
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_ALL);
			waitForElementPresent(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_ALL, methodName);
			click();

			Thread.sleep(4000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_SAVE_BUTTON);
			waitForElementPresent(uiConstants.ARCHETYPES_SAVE_BUTTON, methodName);
			click();
			//waitForElementPresent(uiConstants.PROJCREATIONSUCCESSMSG,methodName);
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);

		}      


		catch (Exception e) {

			e.printStackTrace();
		}
	}


	public void  createApplicationLayerArchetypes(String methodName)
	{
		
		
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testAdminCustomerPage::******executing createApplicationLayerArchetypes scenario****");
		}
		try {
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			click();
			Thread.sleep(1000);

			/*getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN).click();
		    getXpathWebElement(adminUIConstants.CUSTOMER_DROPDOWN_VALUE).click();
			click();*/
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_TAB);
			waitForElementPresent(uiConstants.ARCHETYPES_TAB, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_CREATE_BUTTON);
			waitForElementPresent(uiConstants.ARCHETYPES_CREATE_BUTTON, methodName);
			click();

			Thread.sleep(20000);
			waitForElementPresent(uiConstants.ARCHETYPES_NAME, methodName);
			element=getXpathWebElement(uiConstants.ARCHETYPES_NAME);
			click();
			type(adminUIConstants.CREATE_APPLICATION_LAYER_ARCHETYPES_NAME_VALUE);
			//type(methodName);


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_DESCRIPTION);
			waitForElementPresent(uiConstants.ARCHETYPES_DESCRIPTION, methodName);
			click();
			type(adminUIConstants.CREATE_APPLICATION_LAYER_ARCHETYPES_DESCRIPTION_VALUE);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_TECHNOLOGY_VERSION);
			waitForElementPresent(uiConstants.ARCHETYPES_TECHNOLOGY_VERSION, methodName);
			click();
			type(adminUIConstants.CREATE_APPLICATION_LAYER_ARCHETYPES_TECHNOLGY_VERSION_VALUE);

			Thread.sleep(2000);
			/*element=getXpathWebElement(uiConstants.ARCHETYPES_VERSION_COMMENT);
    	 		waitForElementPresent(uiConstants.ARCHETYPES_VERSION_COMMENT, methodName);
    	 		click();
    	 		type(adminUIConstants.CREATE_APPLICATION_LAYER_ARCHETYPES_VERSION_COMMENT_VALUE);

    	 		Thread.sleep(2000);*/
			element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_TYPE);
			waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_TYPE, methodName);
			click();
			Thread.sleep(2000);
			type(adminUIConstants.CREATE_APPLICATION_LAYER_ARCHETYPES_APPLICATION_TYPE_VALUE);
			click();
			Thread.sleep(2000);

			element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_GROUP_TYPE);
			waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_GROUP_TYPE, methodName);
			click();
			Thread.sleep(2000);

			ArcheTypeAddingTechnology(methodName);

        
			System.out.println("--------------Upload archeytype File----------");
			Thread.sleep(3000);
			driver.findElement(By.xpath("//div[@id='appln-file-uploader']/div/div[2]/input")).sendKeys("C:\\Documents and Settings\\kalish_ma\\Desktop\\jars\\archetype.jar");
		    Thread.sleep(5000);
          
          

            	
        
			/*Runtime.getRuntime().exec("C://Documents and Settings/srividya_su/Desktop/archeytype_uploadfile.exe");

				Thread.sleep(4000);*/

			/*element=getXpathWebElement(uiConstants.ARCHETYPES_UPLOAD_JAR);
    	 		waitForElementPresent(uiConstants.ARCHETYPES_UPLOAD_JAR, methodName);
    	 		click();*/

			/*Thread.sleep(2000);
    	 		element=getXpathWebElement(uiConstants.ARCHETYPES_UPLOAD_PLUGIN_JAR);
    	 		waitForElementPresent(uiConstants.ARCHETYPES_UPLOAD_PLUGIN_JAR, methodName);
    	 		click();*/

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_FEATURES_ALL);
			waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_FEATURES_ALL, methodName);
			click();
			Thread.sleep(2000);
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_ALL);
			waitForElementPresent(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_ALL, methodName);
			click();
			
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_FUNCTIONAL_FRAMEWORK);
			waitForElementPresent(uiConstants.ARCHETYPES_FUNCTIONAL_FRAMEWORK, methodName);
			click();

			Thread.sleep(4000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_SAVE_BUTTON);
			waitForElementPresent(uiConstants.ARCHETYPES_SAVE_BUTTON, methodName);
			click();
			//waitForElementPresent(uiConstants.PROJCREATIONSUCCESSMSG,methodName);
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);
			
			Thread.sleep(2000);
			isTextPresent(adminUIConstants.CREATE_APPLICATION_LAYER_ARCHETYPES_NAME_VALUE);
			
			System.out.println("--------------Recent uploaded archetype is ----------" +adminUIConstants.CREATE_APPLICATION_LAYER_ARCHETYPES_NAME_VALUE);
			
		

		}      


		catch (Exception e) {

			e.printStackTrace();
		}
	}


	public void  createMobileLayerArchetypes(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testAdminCustomerPage::******executing createMobileLayerArchetypes scenario****");
		}
		try {

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			click();
			Thread.sleep(1000);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_TAB);
			waitForElementPresent(uiConstants.ARCHETYPES_TAB, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_CREATE_BUTTON);
			waitForElementPresent(uiConstants.ARCHETYPES_CREATE_BUTTON, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_NAME);
			waitForElementPresent(uiConstants.ARCHETYPES_NAME, methodName);
			click();
			//type(adminUIConstants.CREATE_MOBILE_LAYER_ARCHETYPES_NAME_VALUE);
			type(methodName);


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_DESCRIPTION);
			waitForElementPresent(uiConstants.ARCHETYPES_DESCRIPTION, methodName);
			click();
			type(adminUIConstants.CREATE_MOBILE_LAYER_ARCHETYPES_DESCRIPTION_VALUE);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_TECHNOLOGY_VERSION);
			waitForElementPresent(uiConstants.ARCHETYPES_TECHNOLOGY_VERSION, methodName);
			click();
			type(adminUIConstants.CREATE_MOBILE_LAYER_ARCHETYPES_TECHNOLGY_VERSION_VALUE);

			Thread.sleep(2000);
			/*element=getXpathWebElement(uiConstants.ARCHETYPES_VERSION_COMMENT);
	 		waitForElementPresent(uiConstants.ARCHETYPES_VERSION_COMMENT, methodName);
	 		click();
	 		type(adminUIConstants.CREATE_MOBILE_LAYER_ARCHETYPES_VERSION_COMMENT_VALUE);

	 		Thread.sleep(2000);*/
			element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_TYPE);
			waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_TYPE, methodName);
			click();
			Thread.sleep(2000);
			type(adminUIConstants.CREATE_MOBILE_LAYER_ARCHETYPES_APPLICATION_TYPE_VALUE);
			click();
			Thread.sleep(2000);

			element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_GROUP_TYPE);
			waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_GROUP_TYPE, methodName);
			click();
			Thread.sleep(2000);

			ArcheTypeAddingTechnology(methodName);

			Thread.sleep(4000);

			System.out.println("--------------Upload archeytype File----------");
			driver.findElement(By.name("file")).sendKeys("C:\\Documents and Settings\\vairamuthu_m\\Desktop\\jars\\phresco-android-hybrid-archetype-2.0.0.34000.jar");

			Thread.sleep(4000);
			System.out.println("--------------Upload archeytype File----------");


			/*Runtime.getRuntime().exec("C://Documents and Settings/srividya_su/Desktop/archeytype_uploadfile.exe");

			Thread.sleep(4000);*/

			/*element=getXpathWebElement(uiConstants.ARCHETYPES_UPLOAD_JAR);
	 		waitForElementPresent(uiConstants.ARCHETYPES_UPLOAD_JAR, methodName);
	 		click();*/

			/*Thread.sleep(2000);
	 		element=getXpathWebElement(uiConstants.ARCHETYPES_UPLOAD_PLUGIN_JAR);
	 		waitForElementPresent(uiConstants.ARCHETYPES_UPLOAD_PLUGIN_JAR, methodName);
	 		click();*/

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_FEATURES_ALL);
			waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_FEATURES_ALL, methodName);
			click();
			Thread.sleep(2000);
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_ALL);
			waitForElementPresent(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_ALL, methodName);
			click();

			Thread.sleep(4000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_SAVE_BUTTON);
			waitForElementPresent(uiConstants.ARCHETYPES_SAVE_BUTTON, methodName);
			click();
			//waitForElementPresent(uiConstants.PROJCREATIONSUCCESSMSG,methodName);
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);



		}       


		catch (Exception e) {

			e.printStackTrace();
		}
	}
	public void  createSampleMobileLayerArchetypes(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testAdminCustomerPage::******executing createMobileLayerArchetypes scenario****");
		}
		try {

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			click();
			Thread.sleep(1000);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_TAB);
			waitForElementPresent(uiConstants.ARCHETYPES_TAB, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_CREATE_BUTTON);
			waitForElementPresent(uiConstants.ARCHETYPES_CREATE_BUTTON, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_NAME);
			waitForElementPresent(uiConstants.ARCHETYPES_NAME, methodName);
			click();
			type(adminUIConstants.CREATE_MOBILE_LAYER_ARCHETYPES_NAME_VALUE);
			//type(methodName);


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_DESCRIPTION);
			waitForElementPresent(uiConstants.ARCHETYPES_DESCRIPTION, methodName);
			click();
			type(adminUIConstants.CREATE_MOBILE_LAYER_ARCHETYPES_DESCRIPTION_VALUE);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_TECHNOLOGY_VERSION);
			waitForElementPresent(uiConstants.ARCHETYPES_TECHNOLOGY_VERSION, methodName);
			click();
			type(adminUIConstants.CREATE_MOBILE_LAYER_ARCHETYPES_TECHNOLGY_VERSION_VALUE);

			Thread.sleep(2000);
			/*element=getXpathWebElement(uiConstants.ARCHETYPES_VERSION_COMMENT);
	 		waitForElementPresent(uiConstants.ARCHETYPES_VERSION_COMMENT, methodName);
	 		click();
	 		type(adminUIConstants.CREATE_MOBILE_LAYER_ARCHETYPES_VERSION_COMMENT_VALUE);

	 		Thread.sleep(2000);*/
			element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_TYPE);
			waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_TYPE, methodName);
			click();
			Thread.sleep(2000);
			type(adminUIConstants.CREATE_MOBILE_LAYER_ARCHETYPES_APPLICATION_TYPE_VALUE);
			click();
			Thread.sleep(2000);

			element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_GROUP_TYPE);
			waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_GROUP_TYPE, methodName);
			click();
			Thread.sleep(2000);



			System.out.println("--------------Upload archeytype File----------");
			driver.findElement(By.name("file")).sendKeys("C:\\Documents and Settings\\aruna_k\\Desktop\\archetype.jar");

			Thread.sleep(4000);
			System.out.println("--------------Upload archeytype File----------");


			/*Runtime.getRuntime().exec("C://Documents and Settings/srividya_su/Desktop/archeytype_uploadfile.exe");

			Thread.sleep(4000);*/

			/*element=getXpathWebElement(uiConstants.ARCHETYPES_UPLOAD_JAR);
	 		waitForElementPresent(uiConstants.ARCHETYPES_UPLOAD_JAR, methodName);
	 		click();*/

			/*Thread.sleep(2000);
	 		element=getXpathWebElement(uiConstants.ARCHETYPES_UPLOAD_PLUGIN_JAR);
	 		waitForElementPresent(uiConstants.ARCHETYPES_UPLOAD_PLUGIN_JAR, methodName);
	 		click();*/

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICATION_FEATURES_ALL);
			waitForElementPresent(uiConstants.ARCHETYPES_APPLICATION_FEATURES_ALL, methodName);
			click();
			Thread.sleep(2000);
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_ALL);
			waitForElementPresent(uiConstants.ARCHETYPES_APPLICABLE_REPORTS_ALL, methodName);
			click();

			/*ArcheTypeAddingTechnology(methodName);*/

			Thread.sleep(4000);

			Thread.sleep(4000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_SAVE_BUTTON);
			waitForElementPresent(uiConstants.ARCHETYPES_SAVE_BUTTON, methodName);
			click();
			//waitForElementPresent(uiConstants.PROJCREATIONSUCCESSMSG,methodName);
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);
			Thread.sleep(1000);



		}       


		catch (Exception e) {

			e.printStackTrace();
		}
	}
	public void  addEmptyPilotProject(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testComponentsAddEmptyPilotProjectsPage::******executing addEmptyPilotProject scenario****");
		}
		try {

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			waitForElementPresent(uiConstants.COMPONENTS_LINK, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_TAB);
			waitForElementPresent(uiConstants.PILOT_PROJECT_TAB, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_BUTTON);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_BUTTON, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_NAME);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_NAME, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_DESCRIPTION);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_DESCRIPTION, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_VERSION);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_VERSION, methodName);
			click();



			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_SAVE_BUTTON);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(2000);

			isTextPresent(adminUIConstants.PILOT_PROJECT_EMPTY_NAME_ERROR_MSG);
			isTextPresent(adminUIConstants.PILOT_PROJECT_EMPTY_VERSION_ERROR_MSG);
			isTextPresent(adminUIConstants.PILOT_PROJECT_EMPTY_PROJECT_SOURCE_ERROR_MSG);


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_CANCEL_BUTTON);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_CANCEL_BUTTON, methodName);
			click();

		}      


		catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void  addInValidPilotProject(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testComponentsAddInValidPilotProjectsPage::******executing addInValidPilotProject scenario****");
		}
		try {


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_BUTTON);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_BUTTON, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_NAME);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_NAME, methodName);
			click();
			Thread.sleep(2000);
			type(adminUIConstants.PILOT_PROJECT_ADD_NAME_VALUE);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_DESCRIPTION);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_DESCRIPTION, methodName);
			click();
			Thread.sleep(2000);
			type(adminUIConstants.PILOT_PROJECT_ADD_DESCRIPTION_VALUE);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_VERSION);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_VERSION, methodName);
			click();
			Thread.sleep(2000);
			type(adminUIConstants.PILOT_PROJECT_ADD_VERSION_VALUE);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY, methodName);
			click();
			Thread.sleep(2000);
			type(adminUIConstants.PILOT_PROJECT_ADD_PHP_TECHNOLOGY_VALUE);
			click();

			/*	 Thread.sleep(2000);
    	 element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_);
    	 waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY, methodName);
    	 click();
    	 Thread.sleep(2000);
    	 sendKeys(adminUIConstants.PILOT_PROJECT_ADD_TECHNOLOGY_VALUE);*/

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_SAVE_BUTTON);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(2000);


			isTextPresent(adminUIConstants.PILOT_PROJECT_EMPTY_PROJECT_SOURCE_ERROR_MSG);



			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_CANCEL_BUTTON);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_CANCEL_BUTTON, methodName);
			click();

		}      


		catch (Exception e) {

			e.printStackTrace();
		}
	}



	public void  addValidPilotProject(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testComponentsAddValidPilotProjectsPage::******executing addValidPilotProject scenario****");
		}
		try {

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			click();

			Thread.sleep(1000);

			getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN).click();
			getXpathWebElement(adminUIConstants.CUSTOMER_DROPDOWN_VALUE).click();
			

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_TAB);
			waitForElementPresent(uiConstants.PILOT_PROJECT_TAB, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_BUTTON);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_BUTTON, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_NAME);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_NAME, methodName);
			click();
			Thread.sleep(2000);
			//type(adminUIConstants.PILOT_PROJECT_ADD_NAME_VALUE);
			type(methodName);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_DESCRIPTION);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_DESCRIPTION, methodName);
			click();
			Thread.sleep(2000);
			type(adminUIConstants.PILOT_PROJECT_ADD_DESCRIPTION_VALUE);

			Thread.sleep(2000);
			/*element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_VERSION);
	 waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_VERSION, methodName);
	 click();
	 Thread.sleep(2000);
	 type(adminUIConstants.PILOT_PROJECT_ADD_VERSION_VALUE);*/

			/* Thread.sleep(2000);
	 element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY);
	 waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_TECHNOLOGY, methodName);
	 click();*/
			Thread.sleep(3000);

			PilotProjectsAddingTechnology(methodName);

			Thread.sleep(3000);
			System.out.println("--------------Upload Zip File----------");
			driver.findElement(By.name("file")).sendKeys("C:\\Documents and Settings\\kalish_ma\\Desktop\\jars\\tech-nodejs-webservice-2.0.0.34001.zip");

			Thread.sleep(4000);
			System.out.println("--------------Upload Zip File----------");

			/*Runtime.getRuntime().exec("C://Documents and Settings/srividya_su/Desktop/zip_uploadfile.exe");
	 Thread.sleep(4000);*/

			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_GROUP_ID);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_GROUP_ID, methodName);
			click();
			type(adminUIConstants.PILOT_PROJECT_ADD_GROUP_ID_VALUE);
			Thread.sleep(1000);

			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_ARTIFACT_ID);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_ARTIFACT_ID, methodName);
			click();
			type(adminUIConstants.PILOT_PROJECT_ADD_ARTIFACT_VALUE);
			Thread.sleep(1000);

			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_VERSION_PROJECT);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_VERSION_PROJECT, methodName);
			click();
			type(adminUIConstants.PILOT_PROJECT_ADD_VERSION_PROJECT_VALUE);
			Thread.sleep(1000);

			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_SAVE_BUTTON);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_SAVE_BUTTON, methodName);
			click();
			//waitForElementPresent(uiConstants.PROJCREATIONSUCCESSMSG,methodName);
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);

			//isTextPresent(adminUIConstants.PILOT_PROJECT_EMPTY_PROJECT_SOURCE_ERROR_MSG);




		}      


		catch (Exception e) {

			e.printStackTrace();
		}
	}


	public void  updatePilotProject(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testComponentsupdatePilotProjectPage::******executing updatePilotProject scenario****");
		}
		try {


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_UPDATE_PROJECR_LINK);
			waitForElementPresent(uiConstants.PILOT_PROJECT_UPDATE_PROJECR_LINK, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_NAME);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_NAME, methodName);
			click();
			clear();
			Thread.sleep(2000);
			type(adminUIConstants.PILOT_PROJECT_UPDATE_NAME_VALUE);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_DESCRIPTION);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_DESCRIPTION, methodName);
			click();
			clear();
			Thread.sleep(2000);
			type(adminUIConstants.PILOT_PROJECT_UPDATE_DESCRIPTION_VALUE);


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_UPDATE_BUTTON);
			waitForElementPresent(uiConstants.PILOT_PROJECT_UPDATE_BUTTON, methodName);
			click();
			Thread.sleep(2000);
			isTextPresent(adminUIConstants.PILOT_PROJECT_EMPTY_PROJECT_SOURCE_ERROR_MSG);
			Thread.sleep(2000);
		}      


		catch (Exception e) {

			e.printStackTrace();
		}
	}


	public void  addEmptyDownload(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testComponentsaddEmptyDownloadPage::******executing addEmptyDownload scenario****");
		}
		try {

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			waitForElementPresent(uiConstants.COMPONENTS_LINK, methodName);
			click();	 

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_TAB);
			waitForElementPresent(uiConstants.DOWNLOAD_TAB, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_BUTTON);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_BUTTON, methodName);
			click();
			/*clear();
	 Thread.sleep(2000);
	 type(adminUIConstants.PILOT_PROJECT_UPDATE_NAME_VALUE);*/

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_NAME);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_NAME, methodName);
			click();

			/* clear();
	 Thread.sleep(2000);
	 type(adminUIConstants.PILOT_PROJECT_UPDATE_DESCRIPTION_VALUE);*/


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_DESCRIPTION);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_DESCRIPTION, methodName);
			click();

			/*	 Thread.sleep(2000);
	 isTextPresent(adminUIConstants.PILOT_PROJECT_EMPTY_PROJECT_SOURCE_ERROR_MSG);
	 Thread.sleep(2000);*/


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_LICENSE_TYPE);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_LICENSE_TYPE, methodName);
			click();


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_VERSION);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_VERSION, methodName);
			click();


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_GROUP);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_GROUP, methodName);
			click();


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_SAVE_BUTTON);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_SAVE_BUTTON, methodName);
			click();

			//isTextPresent(adminUIConstants.DOWNLOAD_ADD_EMPTY_NAME_ERROR_MSG);
			isTextPresent(adminUIConstants.DOWNLOAD_ADD_EMPTY_APPLICATION_PLATFORM_ERROR_MSG);
			isTextPresent(adminUIConstants.DOWNLOAD_ADD_EMPTY_LICENSE_TYPE_ERROR_MSG);
			isTextPresent(adminUIConstants.DOWNLOAD_ADD_EMPTY_VERSION_ERROR_MSG);
			isTextPresent(adminUIConstants.DOWNLOAD_ADD_EMPTY_TECHNOLOGY_ERROR_MSG);
			// isTextPresent(adminUIConstants.DOWNLOAD_ADD_EMPTY_GROUP_ERROR_MSG);


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_CANCEL_BUTTON);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_CANCEL_BUTTON, methodName);
			click();

		}      


		catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void  addInValidDownload(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testComponentsAddInValidDownloadPage::******executing addInValidDownload scenario****");
		}
		try {



			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_BUTTON);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_BUTTON, methodName);
			click();


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_NAME);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_NAME, methodName);
			click();

			clear();
			Thread.sleep(2000);
			type(adminUIConstants.DOWNLOAD_ADD_NAME_VALUE);


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_DESCRIPTION);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_DESCRIPTION, methodName);
			click();

			Thread.sleep(2000);
			type(adminUIConstants.DOWNLOAD_ADD_DESCRIPTION_VALUE);
			Thread.sleep(2000);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_TECHNOLGY);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_TECHNOLGY, methodName);
			click();


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_LICENSE_TYPE);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_LICENSE_TYPE, methodName);
			click();

			type(adminUIConstants.DOWNLOAD_ADD_LICENSE_TYPE_VALUE);
			click();


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_APPLICATION_PLATFORM);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_APPLICATION_PLATFORM, methodName);
			click();


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_VERSION);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_VERSION, methodName);
			click();

			type(adminUIConstants.DOWNLOAD_ADD_VERSION_VALUE);
			click();



			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_GROUP);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_GROUP, methodName);
			click();

			type(adminUIConstants.DOWNLOAD_ADD_GROUP_VALUE);
			click();
			Thread.sleep(2000);



			/*	 Thread.sleep(2000);
	 element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_SAVE_BUTTON);
	 waitForElementPresent(uiConstants.DOWNLOAD_ADD_SAVE_BUTTON, methodName);
	 click();
			 */

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_CANCEL_BUTTON);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_CANCEL_BUTTON, methodName);
			click();
		}      


		catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void  addValidDownload(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testComponentsAddValidDownloadPage::******executing addValidDownloadDownload scenario****");
		}
		try {
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			click();
			Thread.sleep(1000);

			waitForElementPresent(uiConstants.CUSTOMER_DROPDOWN,methodName);
		    getXpathWebElement(adminUIConstants.CUSTOMER_DROPDOWN_VALUE).click();
			click();
			
			waitForTextPresent("Downloads");
			Thread.sleep(20000);
			waitForElementPresent(uiConstants.DOWNLOAD_TAB, methodName);
			element=getXpathWebElement(uiConstants.DOWNLOAD_TAB);
			click();

			Thread.sleep(5000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_BUTTON);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_BUTTON, methodName);
			click();


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_NAME);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_NAME, methodName);
			click();
			Thread.sleep(2000);
			type(adminUIConstants.DOWNLOAD_ADD_NAME_VALUE);
			
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_DESCRIPTION);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_DESCRIPTION, methodName);
			click();
			type(adminUIConstants.DOWNLOAD_ADD_DESCRIPTION_VALUE);
			Thread.sleep(2000);

		
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_TECHNOLGY);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_TECHNOLGY, methodName);
			click();
			Thread.sleep(10000);

			System.out.println("--------------Upload File----------");
			driver.findElement(By.name("file")).sendKeys("D:\\jars\\drupal-maven-plugin-2.0.0.6001-SNAPSHOT.jar");

			Thread.sleep(5000);
			System.out.println("-------------- File-Upload Done---------");
			Thread.sleep(4000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_VERSION);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_VERSION, methodName);
			click();
			type(adminUIConstants.DOWNLOAD_ADD_VERSION_VALUE);
	

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_GROUPID);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_GROUPID, methodName);
			click();
			type(adminUIConstants.DOWNLOAD_ADD_GROUPID_VALUE);
		
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_ARTIFACTID);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_ARTIFACTID, methodName);
			click();
			type(adminUIConstants.DOWNLOAD_ADD_ARTIFACTIDID_VALUE);
			

			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_LICENSE_TYPE);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_LICENSE_TYPE, methodName);
			click();
			selectText(element,adminUIConstants.DOWNLOAD_ADD_LICENSE_TYPE_VALUE);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_APPLICATION_PLATFORM);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_APPLICATION_PLATFORM, methodName);
			click();

			
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_GROUP);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_GROUP, methodName);
			click();
			selectText(element,adminUIConstants.DOWNLOAD_ADD_GROUP_VALUE);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_SAVE_BUTTON);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_SAVE_BUTTON, methodName);
			click();
			//waitForElementPresent(uiConstants.PROJCREATIONSUCCESSMSG,methodName);
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);
			
			Thread.sleep(2000);
			isTextPresent(adminUIConstants.DOWNLOAD_ADD_NAME_VALUE);
			
			System.out.println("--------------Recent uploaded Download is ----------" +adminUIConstants.DOWNLOAD_ADD_NAME_VALUE);
			
		
		}      


		catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void  UpdateDownload(String methodName)
	{
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testComponentsUpdateDownloadPage::******executing UpdateDownload scenario****");
		}
		try {

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_UPDATE_DOWNLOAD_LINK);
			waitForElementPresent(uiConstants.DOWNLOAD_UPDATE_DOWNLOAD_LINK, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_UPDATE_BUTTON);
			waitForElementPresent(uiConstants.DOWNLOAD_UPDATE_BUTTON, methodName);
			click();
			Thread.sleep(2000);
			isTextPresent(adminUIConstants.DOWNLOAD_ADD_EMPTY_LICENSE_TYPE_ERROR_MSG);

			Thread.sleep(2000);


		}      


		catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void addConfigurationTemplate(String methodName) throws Exception 
	{
		if(StringUtils.isEmpty(methodName))
		{
			methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testComponentsAddConfigurationTemplatePage::******executing addEmptyConfigurationTemplate scenario****");
		}

		try {
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			click();
			Thread.sleep(1000);

			getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN).click();
			getXpathWebElement(adminUIConstants.CUSTOMER_DROPDOWN_VALUE).click();
			


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_TAB);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_TAB, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON, methodName);
			click();


			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME, methodName);
			click();
			//type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_NAME);
			type(methodName);

			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION, methodName);
			click();
			type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_DESCRIPTION);

			Thread.sleep(1000);
			/*element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO, methodName);
	click();*/
			AddingTechnology(methodName);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_CUSTOMPROPERTIES);
			waitForElementPresent(uiConstants.CONFIGURATION_CUSTOMPROPERTIES, methodName);
			click();

			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_KEY);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_KEY, methodName);
			click();
			type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_KEYVALUE);

			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAMEFIELD);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAMEFIELD, methodName);
			click();
			type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_NAMEFIELD_VALUE);
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(3000);
			//waitForElementPresent(uiConstants.PROJCREATIONSUCCESSMSG,methodName);
			//isTextPresent(adminUIConstants.ADD_CONFIGURATION_TEMP_SUCCESS_MSG);
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);
		} 

		catch (InterruptedException e) {

			e.printStackTrace();
		}

	}
	public void addConfigurationTemplateNumber(String methodName) throws Exception 
	{
		if(StringUtils.isEmpty(methodName))
		{
			methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testComponentsAddConfigurationTemplatePage::******executing addEmptyConfigurationTemplate scenario****");
		}

		try {
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN);
			selectText(element,adminUIConstants.CUSTOMER_DROPDOWN_VALUE);


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_TAB);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_TAB, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON, methodName);
			click();


			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME, methodName);
			click();
			//type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_NAME);
			type(methodName);

			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION, methodName);
			click();
			type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_DESCRIPTION);

			Thread.sleep(1000);
			/*element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO, methodName);
	click();*/
			AddingTechnology(methodName);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_CUSTOMPROPERTIES);
			waitForElementPresent(uiConstants.CONFIGURATION_CUSTOMPROPERTIES, methodName);
			click();
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_KEY);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_KEY, methodName);
			click();
			type(adminUIConstants.ADD_CONFIGURATION_TEMPLETS_KEYVALUE_NUMBER);
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAMEFIELD);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAMEFIELD, methodName);
			click();
			type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_NAMEFIELD_VALUE);

			element=getXpathWebElement(uiConstants.CONFIGURATION_PROPERTYTEMP_TYPE_NUMBER);
			waitForElementPresent(uiConstants.CONFIGURATION_PROPERTYTEMP_TYPE_NUMBER, methodName);
			click();
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(3000);
			//waitForElementPresent(uiConstants.PROJCREATIONSUCCESSMSG,methodName);
			//isTextPresent(adminUIConstants.ADD_CONFIGURATION_TEMP_SUCCESS_MSG);
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);
		} 

		catch (InterruptedException e) {

			e.printStackTrace();
		}

	}
	public void addConfigurationTemplatePassword(String methodName) throws Exception 
	{
		if(StringUtils.isEmpty(methodName))
		{
			methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testComponentsAddConfigurationTemplatePage::******executing addEmptyConfigurationTemplate scenario****");
		}

		try {
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN);
			selectText(element,adminUIConstants.CUSTOMER_DROPDOWN_VALUE);


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_TAB);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_TAB, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON, methodName);
			click();


			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME, methodName);
			click();
			//type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_NAME);
			type(methodName);

			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION, methodName);
			click();
			type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_DESCRIPTION);

			Thread.sleep(1000);
			/*element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO, methodName);
	click();*/
			AddingTechnology(methodName);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_CUSTOMPROPERTIES);
			waitForElementPresent(uiConstants.CONFIGURATION_CUSTOMPROPERTIES, methodName);
			click();
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_KEY);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_KEY, methodName);
			click();
			type(adminUIConstants.ADD_CONFIGURATION_TEMPLETS_KEYVALUE_PASSWORD);
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAMEFIELD);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAMEFIELD, methodName);
			click();
			type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_NAMEFIELD_VALUE);

			element=getXpathWebElement(uiConstants.CONFIGURATION_PROPERTYTEMP_TYPE_PASSWORD);
			waitForElementPresent(uiConstants.CONFIGURATION_PROPERTYTEMP_TYPE_PASSWORD, methodName);
			click();
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(3000);
			//waitForElementPresent(uiConstants.PROJCREATIONSUCCESSMSG,methodName);
			//isTextPresent(adminUIConstants.ADD_CONFIGURATION_TEMP_SUCCESS_MSG);
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);
		} 

		catch (InterruptedException e) {

			e.printStackTrace();
		}

	}
	public void addConfigurationTemplateFiletype(String methodName) throws Exception 
	{
		if(StringUtils.isEmpty(methodName))
		{
			methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testComponentsAddConfigurationTemplatePage::******executing addEmptyConfigurationTemplate scenario****");
		}

		try {
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN);
			selectText(element,adminUIConstants.CUSTOMER_DROPDOWN_VALUE);


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_TAB);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_TAB, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON, methodName);
			click();


			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME, methodName);
			click();
			//type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_NAME);
			type(methodName);

			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION, methodName);
			click();
			type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_DESCRIPTION);

			Thread.sleep(1000);
			/*element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO, methodName);
	click();*/
			AddingTechnology(methodName);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_CUSTOMPROPERTIES);
			waitForElementPresent(uiConstants.CONFIGURATION_CUSTOMPROPERTIES, methodName);
			click();
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_KEY);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_KEY, methodName);
			click();
			type(adminUIConstants.ADD_CONFIGURATION_TEMPLETS_KEYVALUE_FILETYPE);
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAMEFIELD);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAMEFIELD, methodName);
			click();
			type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_NAMEFIELD_VALUE);

			element=getXpathWebElement(uiConstants.CONFIGURATION_PROPERTYTEMP_TYPE_FILETYPE);
			waitForElementPresent(uiConstants.CONFIGURATION_PROPERTYTEMP_TYPE_FILETYPE, methodName);
			click();
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(3000);
			//waitForElementPresent(uiConstants.PROJCREATIONSUCCESSMSG,methodName);
			//isTextPresent(adminUIConstants.ADD_CONFIGURATION_TEMP_SUCCESS_MSG);
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);
		} 

		catch (InterruptedException e) {

			e.printStackTrace();
		}

	}
	public void addConfigurationTemplateBoolean(String methodName) throws Exception 
	{
		if(StringUtils.isEmpty(methodName))
		{
			methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testComponentsAddConfigurationTemplatePage::******executing addEmptyConfigurationTemplate scenario****");
		}

		try {
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN);
			selectText(element,adminUIConstants.CUSTOMER_DROPDOWN_VALUE);


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_TAB);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_TAB, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON, methodName);
			click();


			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME, methodName);
			click();
			//type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_NAME);
			type(methodName);

			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION, methodName);
			click();
			type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_DESCRIPTION);

			Thread.sleep(1000);
			/*element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO, methodName);
	click();*/
			AddingTechnology(methodName);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_CUSTOMPROPERTIES);
			waitForElementPresent(uiConstants.CONFIGURATION_CUSTOMPROPERTIES, methodName);
			click();
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_KEY);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_KEY, methodName);
			click();
			type(adminUIConstants.ADD_CONFIGURATION_TEMPLETS_KEYVALUE_BOOLEAN);
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAMEFIELD);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAMEFIELD, methodName);
			click();
			type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_NAMEFIELD_VALUE);

			element=getXpathWebElement(uiConstants.CONFIGURATION_PROPERTYTEMP_TYPE_BOOLEAN);
			waitForElementPresent(uiConstants.CONFIGURATION_PROPERTYTEMP_TYPE_BOOLEAN, methodName);
			click();
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(3000);
			//waitForElementPresent(uiConstants.PROJCREATIONSUCCESSMSG,methodName);
			//isTextPresent(adminUIConstants.ADD_CONFIGURATION_TEMP_SUCCESS_MSG);
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);
		} 

		catch (InterruptedException e) {

			e.printStackTrace();
		}

	}
	public void addConfigurationTemplateActions(String methodName) throws Exception 
	{
		if(StringUtils.isEmpty(methodName))
		{
			methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testComponentsAddConfigurationTemplatePage::******executing addEmptyConfigurationTemplate scenario****");
		}

		try {
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN);
			selectText(element,adminUIConstants.CUSTOMER_DROPDOWN_VALUE);


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_TAB);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_TAB, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON, methodName);
			click();


			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME, methodName);
			click();
			//type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_NAME);
			type(methodName);

			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION, methodName);
			click();
			type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_DESCRIPTION);

			Thread.sleep(1000);
			/*element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO, methodName);
	click();*/
			AddingTechnology(methodName);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_CUSTOMPROPERTIES);
			waitForElementPresent(uiConstants.CONFIGURATION_CUSTOMPROPERTIES, methodName);
			click();
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_KEY);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_KEY, methodName);
			click();
			type(adminUIConstants.ADD_CONFIGURATION_TEMPLETS_KEYVALUE_ACTIONS);
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAMEFIELD);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAMEFIELD, methodName);
			click();
			type(adminUIConstants.ADD__CONFIGURATION_TEMPLETS_NAMEFIELD_VALUE);

			element=getXpathWebElement(uiConstants.CONFIGURATION_PROPERTYTEMP_TYPE_ACTIONS);
			waitForElementPresent(uiConstants.CONFIGURATION_PROPERTYTEMP_TYPE_ACTIONS, methodName);
			click();
			Thread.sleep(1000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(3000);
			//waitForElementPresent(uiConstants.PROJCREATIONSUCCESSMSG,methodName);
			//isTextPresent(adminUIConstants.ADD_CONFIGURATION_TEMP_SUCCESS_MSG);
			waitForTextPresent(adminUIConstants.TEXT_SUCCESSS_MSG);
		} 

		catch (InterruptedException e) {

			e.printStackTrace();
		}

	}



	/*public void addInValidConfigurationTemplate(String methodName) throws Exception 
{
if(StringUtils.isEmpty(methodName))
{
	methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
	log.info("@testComponentsAddConfigurationTemplatePage::******executing addInValidConfigurationTemplate scenario****");
}

try {



	Thread.sleep(2000);
	element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON, methodName);
	click();


	Thread.sleep(2000);
	element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME, methodName);
	click();

	Thread.sleep(2000);
	element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION, methodName);
	click();

	Thread.sleep(2000);
	element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO, methodName);
	click();


	Thread.sleep(2000);
	element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_KEY);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_KEY, methodName);
	click();

	Thread.sleep(2000);
	element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON, methodName);
	click();

} 

catch (InterruptedException e) {

	e.printStackTrace();
}

}
public void addValidConfigurationTemplate(String methodName) throws Exception 
{
if(StringUtils.isEmpty(methodName))
{
	methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
	log.info("@testComponentsAddEmptyConfigurationTemplatePage::******executing addValidConfigurationTemplate scenario****");
}

try {


	Thread.sleep(2000);
	element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON, methodName);
	click();


	Thread.sleep(2000);
	element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME, methodName);
	click();

	Thread.sleep(2000);
	element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION, methodName);
	click();

	Thread.sleep(2000);
	element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO, methodName);
	click();

	Thread.sleep(2000);
	element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_CHECK_ALL_APPLIES_TO, methodName);
	click();


	Thread.sleep(2000);
	element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_KEY);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_KEY, methodName);
	click();

	Thread.sleep(2000);
	element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON);
	waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON, methodName);
	click();

} 

catch (InterruptedException e) {

	e.printStackTrace();
}

}*/
	public void addUpdateConfigurationTemplate(String methodName) throws Exception 
	{
		if(StringUtils.isEmpty(methodName))
		{
			methodName=Thread.currentThread().getStackTrace()[1].getMethodName();
			log.info("@testComponentsAddEmptyConfigurationTemplatePage::******executing addUpdateConfigurationTemplate scenario****");
		}

		try {


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON, methodName);
			click();


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_SAVE_BUTTON, methodName);
			click();

		} 

		catch (InterruptedException e) {

			e.printStackTrace();
		}

	}
	public void deleteCreatedArchetype(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {
			System.out.println("----------deleteCreateSample Starts----------");
			Thread.sleep(2000);
			System.out.println("----------Check box Selection starts----------");
			waitForElementPresent(uiConstants.SELECT_ARCHETYPECHECKBOX,methodName);
			element=getXpathWebElement(uiConstants.SELECT_ARCHETYPECHECKBOX);
			click();
			System.out.println("----------Check box Selection ends----------");
			Thread.sleep(2000);

			isdeleteButtonEnabled(methodName);
			System.out.println("----------browser closw test----------");

			waitForElementPresent(uiConstants.DELETE_BUTTON,methodName);
				element=getXpathWebElement(uiConstants.DELETE_BUTTON);
				click();
				Thread.sleep(2000);
				waitForElementPresent(uiConstants.ACCEPT_DELETE_OK,methodName);
				element=getXpathWebElement(uiConstants.ACCEPT_DELETE_OK);
				click();
		}
		catch (InterruptedException e) {

			e.printStackTrace();
		}

	}
	public void deleteCreatedSamples(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {
			System.out.println("----------deleteCreateSample Starts----------");
			Thread.sleep(2000);
			System.out.println("----------Check box Selection starts----------");
			waitForElementPresent(uiConstants.SELECT_CHECKBOX,methodName);
			element=getXpathWebElement(uiConstants.SELECT_CHECKBOX);
			click();
			System.out.println("----------Check box Selection ends----------");
			Thread.sleep(2000);

			isdeleteButtonEnabled(methodName);
			System.out.println("----------browser close test----------");

			waitForElementPresent(uiConstants.DELETE_BUTTON,methodName);
				element=getXpathWebElement(uiConstants.DELETE_BUTTON);
				click();
				Thread.sleep(2000);
				waitForElementPresent(uiConstants.ACCEPT_DELETE_OK,methodName);
				element=getXpathWebElement(uiConstants.ACCEPT_DELETE_OK);
				click();
		}
		catch (InterruptedException e) {

			e.printStackTrace();
		}

	}
	public void deleteCustomer(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {
			element=getXpathWebElement(uiConstants.ADMIN_LINK);
			waitForElementPresent(uiConstants.ADMIN_LINK, methodName);
			click();
			Thread.sleep(10000);

			element=getXpathWebElement(uiConstants.ADMIN_CUSTOMER_TAB);
			waitForElementPresent(uiConstants.ADMIN_CUSTOMER_TAB, methodName);
			click();
			Thread.sleep(3000);
			System.out.println("----------deleteCreateSample Starts----------");
			Thread.sleep(2000);
			System.out.println("----------Check box Selection starts----------");
			waitForElementPresent(uiConstants.SELECT_CHECKBOX,methodName);
			element=getXpathWebElement(uiConstants.SELECT_CHECKBOX);
			click();
			System.out.println("----------Check box Selection ends----------");
			Thread.sleep(2000);

			isdeleteButtonEnabled(methodName);
			System.out.println("----------browser closw test----------");

		        waitForElementPresent(uiConstants.DELETE_BUTTON,methodName);
				element=getXpathWebElement(uiConstants.DELETE_BUTTON);
				click();
				Thread.sleep(2000);
				waitForElementPresent(uiConstants.ACCEPT_DELETE_OK,methodName);
				element=getXpathWebElement(uiConstants.ACCEPT_DELETE_OK);
				click();
		}
		catch (InterruptedException e) {

			e.printStackTrace();
		}

	}
	public void selectArchetypes(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {


			Thread.sleep(2000);
			waitForElementPresent(uiConstants.ARCHETYPES_TAB,methodName);
			element=getXpathWebElement(uiConstants.ARCHETYPES_TAB);
			click();
			Thread.sleep(2000);
		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	public void Components(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1] .getMethodName();
		}
		try {
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();
			Thread.sleep(3000);

			System.out.println("----------Components Completed----------");
   
			waitForElementPresent(uiConstants.CUSTOMER_DROPDOWN_VALUE,methodName);
			getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN_VALUE).click();
			Thread.sleep(15000);
			waitForTextPresent("Android Hybrid");
		


		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	public void customerSelection(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1] .getMethodName();
		}
		try {
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();

			Thread.sleep(20000);

				waitForElementPresent(uiConstants.CUSTOMER_DROPDOWN,methodName);
				element=getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN);
				click();
				Thread.sleep(2000);

				waitForElementPresent(uiConstants.CUSTOMER_DROPDOWN_VALUE,methodName);
				getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN_VALUE).click();
				Thread.sleep(5000);

		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	public void addingNewVideos(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1] .getMethodName();
		}
		try {
			
			Thread.sleep(3000);
			waitForElementPresent(uiConstants.ADMIN_LINK,methodName);
			element=getXpathWebElement(uiConstants.ADMIN_LINK);
			click();
		//	waitForTextPresent("Customers");
		 //   Thread.sleep(20000);
			    getXpathWebElement(uiConstants.VIDEOS_BUTTON);
				waitForElementPresent(uiConstants.VIDEOS_BUTTON,methodName);
				click();
				Thread.sleep(2000);
				getXpathWebElement(uiConstants.VIDEOS_ADD);
                waitForElementPresent(uiConstants.VIDEOS_ADD, methodName);
                 click();
				Thread.sleep(1000);
				getXpathWebElement(uiConstants.VIDEOS_ADD);
				waitForElementPresent(uiConstants.VIDEOS_NAME,methodName);
				click();
				type(adminUIConstants.VIDEO_NAME_VALUE);
				getXpathWebElement(uiConstants.VIDEOS_DESCRIPTION);
				waitForElementPresent(uiConstants.VIDEOS_DESCRIPTION, methodName);
				click();
				type(adminUIConstants.VIDEO_DESCRIPTION_VALUE);
				System.out.println("--------------Upload MP4 File----------");
				driver.findElement(By.name("file")).sendKeys("C:\\Documents and Settings\\kalish_ma\\Desktop\\jars\\Thavarugal Paesum [_______ ______] - Tamil Short Film Teaser Trailer HD.mp4");
				Thread.sleep(5000);
				System.out.println("--------------Upload Png Image File----------");
				driver.findElement(By.name("file")).sendKeys("C:\\Documents and Settings\\vairamuthu_m\\Desktop\\fsdfdsf.PNG");
				Thread.sleep(5000);
				getXpathWebElement(uiConstants.VIDEOS_SAVE);
				waitForElementPresent(uiConstants.VIDEOS_SAVE, methodName);
				click();
				Thread.sleep(7000);
				waitForTextPresent("Videos uploaded successfully");
				
			}catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		
	public void CancelNewVideos(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1] .getMethodName();
		}
		try {
			
			Thread.sleep(3000);
			waitForElementPresent(uiConstants.ADMIN_LINK,methodName);
			element=getXpathWebElement(uiConstants.ADMIN_LINK);
			click();
		//	waitForTextPresent("Customers");
		 //   Thread.sleep(20000);
			    getXpathWebElement(uiConstants.VIDEOS_BUTTON);
				waitForElementPresent(uiConstants.VIDEOS_BUTTON,methodName);
				click();
				Thread.sleep(2000);
				getXpathWebElement(uiConstants.VIDEOS_ADD);
                waitForElementPresent(uiConstants.VIDEOS_ADD, methodName);
                 click();
				Thread.sleep(1000);
				getXpathWebElement(uiConstants.VIDEOS_ADD);
				waitForElementPresent(uiConstants.VIDEOS_NAME,methodName);
				click();
				type(adminUIConstants.VIDEO_NAME_VALUE);
				getXpathWebElement(uiConstants.VIDEOS_DESCRIPTION);
				waitForElementPresent(uiConstants.VIDEOS_DESCRIPTION, methodName);
				click();
				type(adminUIConstants.VIDEO_DESCRIPTION_VALUE);
				System.out.println("--------------Upload MP4 File----------");
				driver.findElement(By.name("file")).sendKeys("C:\\Documents and Settings\\vairamuthu_m\\Desktop\\Ima\\Thavarugal Paesum [_______ ______] - Tamil Short Film Teaser Trailer HD.mp4");
				Thread.sleep(5000);
				System.out.println("--------------Upload Png Image File----------");
				driver.findElement(By.name("file")).sendKeys("C:\\Documents and Settings\\vairamuthu_m\\Desktop\\fsdfdsf.PNG");
				Thread.sleep(5000);
				getXpathWebElement(uiConstants.VIDEOS_CANCEL);
				waitForElementPresent(uiConstants.VIDEOS_CANCEL,methodName);
				click();
				Thread.sleep(7000);
					
			}catch (InterruptedException e) {

					e.printStackTrace();
				}
			}			
				
				
	public void deleteAddedVideo(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1] .getMethodName();
		}
		try {
			
			Thread.sleep(3000);
			waitForElementPresent(uiConstants.ADMIN_LINK,methodName);
			element=getXpathWebElement(uiConstants.ADMIN_LINK);
			click();
		//	waitForTextPresent("Customers");
		 //   Thread.sleep(20000);
			    getXpathWebElement(uiConstants.VIDEOS_BUTTON);
				waitForElementPresent(uiConstants.VIDEOS_BUTTON,methodName);
				click();
				Thread.sleep(2000);
							
				getXpathWebElement(uiConstants.SELECT_CHECKBOX);
				waitForElementPresent(uiConstants.SELECT_CHECKBOX, methodName);
				click();
				Thread.sleep(2000);
				getXpathWebElement(uiConstants.DELETE_BUTTON);
				waitForElementPresent(uiConstants.DELETE_BUTTON, methodName);
				click();
				getXpathWebElement(uiConstants.DELETE_BUTTON_OK);
				waitForElementPresent(uiConstants.DELETE_BUTTON_OK, methodName);
				click();
				Thread.sleep(5000);
				//waitForTextPresent("videos deleted successfully");
		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
						
				


	public void selectModules(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1] .getMethodName();
		}
		try {
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENT_MODULES_TAB,methodName);
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENT_MODULES_TAB);
			click();
		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	public void selectJSLibraries(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1] .getMethodName();
		}
		try {
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();
			Thread.sleep(3000);

			System.out.println("----------Components Completed----------");
   
			waitForElementPresent(uiConstants.CUSTOMER_DROPDOWN_VALUE,methodName);
			getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN_VALUE).click();
			Thread.sleep(15000);
			waitForTextPresent("Android Hybrid");
		    Thread.sleep(2000);
			
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENT_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENT_TAB,methodName);
			click();
			Thread.sleep(2000);
            
			waitForElementPresent(uiConstants.REASUABLE_COMPONENTS_JSLIBRARIES_TAB,methodName);
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENTS_JSLIBRARIES_TAB);
			click();
			try {

				if(methodName == "testDeletePHPModules" || methodName == "testPHPReusableComponentsAddModulesPage" || methodName == "testPHPReusableComponentsAddExternalModules"|| methodName == "testDeletePHPJSLibraries" || methodName =="testPHPReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_PHP_TECHNOLOGY_VALUE).click();
					Thread.sleep(2000);
				}
				if(methodName == "testDeleteDrupal6Modules" || methodName == "testDrupal6ReusableComponentsAddModulesPage" || methodName == "testDrupal6ReusableComponentsAddExternalModules" ||  methodName == "testDeleteDrupal6JSLibraries" || methodName == "testDrupal6ReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteDrupal6Components" || methodName == "testDrupal6ReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DRUPAL6_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteNodejsModules" || methodName == "testNodejsReusableComponentsAddModulesPage" || methodName == "testNodejsReusableComponentsAddExternalModules" || methodName == "testDeleteNodejsJSLibraries" || methodName == "testNodejsReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteNodejsComponents" || methodName == "testNodejsReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_NODEJS_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteJavaStandaloneModules" || methodName == "testJavaStandaloneReusableComponentsAddModulesPage" || methodName == "testJavaStandaloneReusableComponentsAddExternalModules" || methodName == "testDeleteJavaStandaloneJSLibraries" || methodName == "testJavaStandaloneReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteJavaStandaloneComponents" || methodName == "testJavaStandaloneReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_JAVASTANDALONE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteiPhoneHybridModules" || methodName == "testiPhoneHybridReusableComponentsAddModulesPage"|| methodName == "testiPhoneHybridReusableComponentsAddExternalModules"|| methodName == "testDeleteiPhoneHybridJSLibraries" || methodName == "testiPhoneHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiPhoneHybridComponents" || methodName == "testiPhoneHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);             
				}
				if(methodName == "testDeleteAndroidHybridModules" || methodName == "testAndroidHybridReusableComponentsAddModulesPage"|| methodName == "testAndroidHybridReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidHybridJSLibraries" || methodName == "testAndroidHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidHybridComponents" || methodName == "testAndroidHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWindowsMetroModules" || methodName == "testWindowsMetroReusableComponentsAddModulesPage"|| methodName == "testWindowsMetroReusableComponentsAddExternalModules" || methodName == "testDeleteWindowsMetroJSLibraries" ||methodName == "testWindowsMetroReusableComponentsAddJSlibrariesPage" || methodName =="testDeleteWindowsMetroComponents" || methodName == "testWindowsMetroReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSMETRO_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);           
				}
				if(methodName == "testDeleteBlackBerryHybridModules" ||methodName == "testBlackBerryHybridReusableComponentsAddModulesPage" || methodName == "testBlackBerryHybridReusableComponentsAddExternalModules" || methodName == "testDeleteBlackBerryHybridJSLibraries" || methodName == "testBlackBerryHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteBlackBerryHybridComponents" || methodName == "testBlackBerryHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteAndroidLibraryModules" ||methodName == "testAndroidLibraryReusableComponentsAddModulesPage" || methodName == "testAndroidLibraryReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidLibraryJSLibraries" || methodName == "testAndroidLibraryReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidLibraryComponents" || methodName == "testAndroidLibraryReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDLIBRARY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteAndroidNativeModules" ||methodName == "testAndroidNativeReusableComponentsAddModulesPage" || methodName == "testAndroidNativeReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidNativeJSLibraries" || methodName == "testAndroidNativeReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidNativeComponents" || methodName == "testAndroidNativeReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteASP_DotnetModules" ||methodName == "testDOTNETReusableComponentsAddModulesPage" || methodName == "testDOTNETReusableComponentsAddExternalModules" || methodName == "testDeleteASP_DotnetJSLibraries" || methodName == "testDOTNETReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteASP_dotnetComponents" || methodName == "testDOTNETReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DOTNET_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteDrupal7Modules" ||methodName == "testDrupal7ReusableComponentsAddModulesPage" || methodName == "testDrupal7ReusableComponentsAddExternalModules" || methodName == "testDeleteDrupal7JSLibraries" || methodName == "testDrupal7ReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteDrupal7Components" || methodName == "testDrupal7ReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DRUPAL7_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteJQueryMobileModules" ||methodName == "testJQueryMobileReusableComponentsAddModulesPage" || methodName == "testJQueryMobileReusableComponentsAddExternalModules" || methodName == "testDeleteJQueryMobileJSLibraries" || methodName == "testJQueryMobileReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteJQueryMobileComponents" || methodName == "testJQueryMobileReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_JQUERYMOBILE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteMultiJQueryModules" ||methodName == "testMultiJQueryReusableComponentsAddModulesPage" || methodName == "testMultiJQueryReusableComponentsAddExternalModules" || methodName == "testDeleteMultiJQueryJSLibraries" || methodName == "testMultiJQueryReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteMultiJQueryComponents" || methodName == "testMultiJQueryReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_MULTIJQUERY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);                 
				}
				if(methodName == "testDeleteMultiYUIModules" ||methodName == "testMultiYUIReusableComponentsAddModulesPage" || methodName == "testMultiYUIReusableComponentsAddExternalModules" || methodName == "testDeleteMultiYUIJSLibraries" || methodName == "testMultiYUIReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteMultiYUIComponents" || methodName == "testMultiYUIReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_MULTIYUI_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteYUIMobileModules" ||methodName == "testYUIMobileReusableComponentsAddModulesPage" || methodName == "testYUIMobileReusableComponentsAddExternalModules" || methodName == "testDeleteYUIMobileJSLibraries" || methodName == "testYUIMobileReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteYUIMobileComponents" || methodName == "testYUIMobileReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_YUIMOBILE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiPhoneLibraryModules" ||methodName == "testiPhoneLibraryReusableComponentsAddModulesPage" || methodName == "testiPhoneLibraryReusableComponentsAddExternalModules" || methodName == "testDeleteiPhoneLibraryJSLibraries" || methodName == "testiPhoneLibraryReusableComponentsAddJSlibrariesPage" || methodName == "testiPhoneLibraryReusableComponentsAddvalidComponentPage" || methodName == "testDeleteiPhoneLibraryComponents")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONELIBRARY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiphoneNativeModules" ||methodName == "testiphoneNativeReusableComponentsAddModulesPage" || methodName == "testiphoneNativeReusableComponentsAddExternalModules" || methodName == "testDeleteiphoneNativeJSLibraries" || methodName == "testiphoneNativeReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiphoneNativeComponents" || methodName == "testiphoneNativeReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONENATIVE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiPhoneWorkSpaceModules" ||methodName == "testiPhoneWorkSpaceReusableComponentsAddModulesPage" || methodName == "testiPhoneWorkSpaceReusableComponentsAddExternalModules" || methodName == "testDeleteiPhoneWorkSpaceJSLibraries" || methodName == "testiPhoneWorkSpaceReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiPhoneWorkSpaceComponents" || methodName == "testiPhoneWorkSpaceReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEWORKSAPCE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteSharePointModules" ||methodName == "testSharePointReusableComponentsAddModulesPage" || methodName == "testSharePointReusableComponentsAddExternalModules" || methodName == "testDeleteSharePointJSLibraries" || methodName == "testSharePointReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteSharePointComponents" || methodName == "testSharePointReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_SHAREPOINT_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteSiteCoreModules" ||methodName == "testSiteCoreReusableComponentsAddModulesPage" || methodName == "testSiteCoreReusableComponentsAddExternalModules" || methodName == "testDeleteSiteCoreJSLibraries" || methodName == "testSiteCoreReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteSiteCoreComponents" || methodName == "testSiteCoreReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_SITECORE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWindowsPhoneModules" ||methodName == "testWindowsPhoneReusableComponentsAddModulesPage" || methodName == "testWindowsPhoneReusableComponentsAddExternalModules" || methodName == "testDeleteWindowsPhoneJSLibraries" || methodName == "testWindowsPhoneReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteWindowsPhoneComponents" || methodName == "testWindowsPhoneReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSPHONE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWordPressModules" ||methodName == "testWordPressReusableComponentsAddModulesPage" || methodName == "testWordPressReusableComponentsAddExternalModules" || methodName == "testDeleteWordPressJSLibraries" || methodName == "testWordPressReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteWordPressComponents" || methodName == "testWordPressReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WORDPRESS_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				

			} catch (Exception e) {

				e.printStackTrace();
			}		

	}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	public void selectReusableComponent(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1] .getMethodName();
		}
		try {
			
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();
			Thread.sleep(3000);

			System.out.println("----------Components Completed----------");
   
			waitForElementPresent(uiConstants.CUSTOMER_DROPDOWN_VALUE,methodName);
			getXpathWebElement(uiConstants.CUSTOMER_DROPDOWN_VALUE).click();
			Thread.sleep(15000);
			waitForTextPresent("Android Hybrid");
		  
			Thread.sleep(2000);
			
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENT_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENT_TAB,methodName);
			click();
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENTS_COMPONENT_TAB,methodName);
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENTS_COMPONENT_TAB);
			click();
			try {

				if(methodName == "testDeletePHPModules" || methodName == "testPHPReusableComponentsAddModulesPage" || methodName == "testPHPReusableComponentsAddExternalModules"|| methodName == "testDeletePHPJSLibraries" || methodName =="testPHPReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_PHP_TECHNOLOGY_VALUE).click();
					Thread.sleep(2000);
				}
				if(methodName == "testDeleteDrupal6Modules" || methodName == "testDrupal6ReusableComponentsAddModulesPage" || methodName == "testDrupal6ReusableComponentsAddExternalModules" ||  methodName == "testDeleteDrupal6JSLibraries" || methodName == "testDrupal6ReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteDrupal6Components" || methodName == "testDrupal6ReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DRUPAL6_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteNodejsModules" || methodName == "testNodejsReusableComponentsAddModulesPage" || methodName == "testNodejsReusableComponentsAddExternalModules" || methodName == "testDeleteNodejsJSLibraries" || methodName == "testNodejsReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteNodejsComponents" || methodName == "testNodejsReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_NODEJS_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteJavaStandaloneModules" || methodName == "testJavaStandaloneReusableComponentsAddModulesPage" || methodName == "testJavaStandaloneReusableComponentsAddExternalModules" || methodName == "testDeleteJavaStandaloneJSLibraries" || methodName == "testJavaStandaloneReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteJavaStandaloneComponents" || methodName == "testJavaStandaloneReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_JAVASTANDALONE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);            
				}
				if(methodName == "testDeleteiPhoneHybridModules" || methodName == "testiPhoneHybridReusableComponentsAddModulesPage"|| methodName == "testiPhoneHybridReusableComponentsAddExternalModules"|| methodName == "testDeleteiPhoneHybridJSLibraries" || methodName == "testiPhoneHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiPhoneHybridComponents" || methodName == "testiPhoneHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);             
				}
				if(methodName == "testDeleteAndroidHybridModules" || methodName == "testAndroidHybridReusableComponentsAddModulesPage"|| methodName == "testAndroidHybridReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidHybridJSLibraries" || methodName == "testAndroidHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidHybridComponents" || methodName == "testAndroidHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDHYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWindowsMetroModules" || methodName == "testWindowsMetroReusableComponentsAddModulesPage"|| methodName == "testWindowsMetroReusableComponentsAddExternalModules" || methodName == "testDeleteWindowsMetroJSLibraries" ||methodName == "testWindowsMetroReusableComponentsAddJSlibrariesPage" || methodName =="testDeleteWindowsMetroComponents" || methodName == "testWindowsMetroReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSMETRO_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);           
				}
				if(methodName == "testDeleteBlackBerryHybridModules" ||methodName == "testBlackBerryHybridReusableComponentsAddModulesPage" || methodName == "testBlackBerryHybridReusableComponentsAddExternalModules" || methodName == "testDeleteBlackBerryHybridJSLibraries" || methodName == "testBlackBerryHybridReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteBlackBerryHybridComponents" || methodName == "testBlackBerryHybridReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteAndroidLibraryModules" ||methodName == "testAndroidLibraryReusableComponentsAddModulesPage" || methodName == "testAndroidLibraryReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidLibraryJSLibraries" || methodName == "testAndroidLibraryReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidLibraryComponents" || methodName == "testAndroidLibraryReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDLIBRARY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteAndroidNativeModules" ||methodName == "testAndroidNativeReusableComponentsAddModulesPage" || methodName == "testAndroidNativeReusableComponentsAddExternalModules" || methodName == "testDeleteAndroidNativeJSLibraries" || methodName == "testAndroidNativeReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteAndroidNativeComponents" || methodName == "testAndroidNativeReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_BLACKBERRY_HYBRID_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteASP_DotnetModules" ||methodName == "testDOTNETReusableComponentsAddModulesPage" || methodName == "testDOTNETReusableComponentsAddExternalModules" || methodName == "testDeleteASP_DotnetJSLibraries" || methodName == "testDOTNETReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteASP_dotnetComponents" || methodName == "testDOTNETReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DOTNET_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteDrupal7Modules" ||methodName == "testDrupal7ReusableComponentsAddModulesPage" || methodName == "testDrupal7ReusableComponentsAddExternalModules" || methodName == "testDeleteDrupal7JSLibraries" || methodName == "testDrupal7ReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteDrupal7Components" || methodName == "testDrupal7ReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_DRUPAL7_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteJQueryMobileModules" ||methodName == "testJQueryMobileReusableComponentsAddModulesPage" || methodName == "testJQueryMobileReusableComponentsAddExternalModules" || methodName == "testDeleteJQueryMobileJSLibraries" || methodName == "testJQueryMobileReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteJQueryMobileComponents" || methodName == "testJQueryMobileReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_JQUERYMOBILE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteMultiJQueryModules" ||methodName == "testMultiJQueryReusableComponentsAddModulesPage" || methodName == "testMultiJQueryReusableComponentsAddExternalModules" || methodName == "testDeleteMultiJQueryJSLibraries" || methodName == "testMultiJQueryReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteMultiJQueryComponents" || methodName == "testMultiJQueryReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_MULTIJQUERY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);                 
				}
				if(methodName == "testDeleteMultiYUIModules" ||methodName == "testMultiYUIReusableComponentsAddModulesPage" || methodName == "testMultiYUIReusableComponentsAddExternalModules" || methodName == "testDeleteMultiYUIJSLibraries" || methodName == "testMultiYUIReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteMultiYUIComponents" || methodName == "testMultiYUIReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_MULTIYUI_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteYUIMobileModules" ||methodName == "testYUIMobileReusableComponentsAddModulesPage" || methodName == "testYUIMobileReusableComponentsAddExternalModules" || methodName == "testDeleteYUIMobileJSLibraries" || methodName == "testYUIMobileReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteYUIMobileComponents" || methodName == "testYUIMobileReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_YUIMOBILE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiPhoneLibraryModules" ||methodName == "testiPhoneLibraryReusableComponentsAddModulesPage" || methodName == "testiPhoneLibraryReusableComponentsAddExternalModules" || methodName == "testDeleteiPhoneLibraryJSLibraries" || methodName == "testiPhoneLibraryReusableComponentsAddJSlibrariesPage" || methodName == "testiPhoneLibraryReusableComponentsAddvalidComponentPage" || methodName == "testDeleteiPhoneLibraryComponents")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONELIBRARY_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiphoneNativeModules" ||methodName == "testiphoneNativeReusableComponentsAddModulesPage" || methodName == "testiphoneNativeReusableComponentsAddExternalModules" || methodName == "testDeleteiphoneNativeJSLibraries" || methodName == "testiphoneNativeReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiphoneNativeComponents" || methodName == "testiphoneNativeReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONENATIVE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteiPhoneWorkSpaceModules" ||methodName == "testiPhoneWorkSpaceReusableComponentsAddModulesPage" || methodName == "testiPhoneWorkSpaceReusableComponentsAddExternalModules" || methodName == "testDeleteiPhoneWorkSpaceJSLibraries" || methodName == "testiPhoneWorkSpaceReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteiPhoneWorkSpaceComponents" || methodName == "testiPhoneWorkSpaceReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_IPHONEWORKSAPCE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteSharePointModules" ||methodName == "testSharePointReusableComponentsAddModulesPage" || methodName == "testSharePointReusableComponentsAddExternalModules" || methodName == "testDeleteSharePointJSLibraries" || methodName == "testSharePointReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteSharePointComponents" || methodName == "testSharePointReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_SHAREPOINT_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteSiteCoreModules" ||methodName == "testSiteCoreReusableComponentsAddModulesPage" || methodName == "testSiteCoreReusableComponentsAddExternalModules" || methodName == "testDeleteSiteCoreJSLibraries" || methodName == "testSiteCoreReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteSiteCoreComponents" || methodName == "testSiteCoreReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_SITECORE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWindowsPhoneModules" ||methodName == "testWindowsPhoneReusableComponentsAddModulesPage" || methodName == "testWindowsPhoneReusableComponentsAddExternalModules" || methodName == "testDeleteWindowsPhoneJSLibraries" || methodName == "testWindowsPhoneReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteWindowsPhoneComponents" || methodName == "testWindowsPhoneReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WINDOWSPHONE_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				if(methodName == "testDeleteWordPressModules" ||methodName == "testWordPressReusableComponentsAddModulesPage" || methodName == "testWordPressReusableComponentsAddExternalModules" || methodName == "testDeleteWordPressJSLibraries" || methodName == "testWordPressReusableComponentsAddJSlibrariesPage" || methodName == "testDeleteWordPressComponents" || methodName == "testWordPressReusableComponentsAddvalidComponentPage")
				{
					getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
					getXpathWebElement(adminUIConstants.ADD_PilotProjects_WORDPRESS_TECHONOLGY_VALUE).click();
					Thread.sleep(2000);              
				}
				

			} catch (Exception e) {

				e.printStackTrace();
			}		
		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	public void selectApplicationtypes(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.APPLICATION_TYPES_TAB,methodName);
			element=getXpathWebElement(uiConstants.APPLICATION_TYPES_TAB);
			click();
		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	public void selectConfigurationTemplates(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_TAB,methodName);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_TAB);
			click();
		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	public void selectPilotProjects(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.PILOT_PROJECT_TAB,methodName);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_TAB);
			click();
		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	public void selectDownload(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {

			Thread.sleep(2000);
			waitForElementPresent(uiConstants.DOWNLOAD_TAB,methodName);
			element=getXpathWebElement(uiConstants.DOWNLOAD_TAB);
			click();
		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	public void selectGlobalurl(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.ADMIN_LINK,methodName);
			element=getXpathWebElement(uiConstants.ADMIN_LINK);
			click();
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.ADMIN_GLOBALURL_TAB,methodName);
			element=getXpathWebElement(uiConstants.ADMIN_GLOBALURL_TAB);
			click();
		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}


	public void toolTipVerification(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();

			Thread.sleep(2000);
			waitForElementPresent(uiConstants.ARCHETYPES_TAB,methodName);
			element=getXpathWebElement(uiConstants.ARCHETYPES_TAB);
			click();

			Thread.sleep(3000);
			waitForElementPresent(uiConstants.ARCHETYPES_CREATE_BUTTON,methodName);
			element=getXpathWebElement(uiConstants.ARCHETYPES_CREATE_BUTTON);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_NAME);
			waitForElementPresent(uiConstants.ARCHETYPES_NAME, methodName);
			click();


			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_DESCRIPTION);
			waitForElementPresent(uiConstants.ARCHETYPES_DESCRIPTION, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.ARCHETYPES_TECHNOLOGY_VERSION);
			waitForElementPresent(uiConstants.ARCHETYPES_TECHNOLOGY_VERSION, methodName);
			click();




			toolTipVerify(uiConstants.ARCHETYPES_NAME);
			toolTipVerify(uiConstants.ARCHETYPES_DESCRIPTION);
			toolTipVerify(uiConstants.ARCHETYPES_TECHNOLOGY_VERSION);




		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	public void testToolTipVerifyforComponent(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENTS_COMPONENT_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENTS_COMPONENT_TAB,methodName);
			click();
			element=getXpathWebElement(uiConstants.COMPONENTS_ADD_BUTTON);
			waitForElementPresent(uiConstants.COMPONENTS_ADD_BUTTON,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.COMPONENTS_NAME);
			waitForElementPresent(uiConstants.COMPONENTS_NAME,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.COMPONENTS_DESCRIPTION);
			waitForElementPresent(uiConstants.COMPONENTS_DESCRIPTION,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.COMPONENTS_HELPTEXT);
			waitForElementPresent(uiConstants.COMPONENTS_HELPTEXT,methodName);
			click();
			toolTipVerify(uiConstants.COMPONENTS_NAME);
			toolTipVerify(uiConstants.COMPONENTS_DESCRIPTION);
			toolTipVerify(uiConstants.COMPONENTS_HELPTEXT);


		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	public void toolTipVerificationforModules(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {
			Thread.sleep(5000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();

			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENT_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENT_TAB,methodName);
			click();

			Thread.sleep(3000);
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENT_MODULES_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENT_MODULES_TAB,methodName);
			click();

			Thread.sleep(5000);

			element=getXpathWebElement(uiConstants.ADD_MODULES_BUTTON);
			waitForElementPresent(uiConstants.ADD_MODULES_BUTTON,methodName);
			click();

			Thread.sleep(5000);


			element=getXpathWebElement(uiConstants.MODULES_NAME);
			waitForElementPresent(uiConstants.MODULES_NAME,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MODULE_DESCRIPTION);
			waitForElementPresent(uiConstants.MODULE_DESCRIPTION,methodName);
			click();
			Thread.sleep(1000);

			element=getXpathWebElement(uiConstants.MODULE_HELPTEXT);
			waitForElementPresent(uiConstants.MODULE_HELPTEXT,methodName);
			click();

			toolTipVerify(uiConstants.MODULES_NAME);
			toolTipVerify(uiConstants.MODULE_DESCRIPTION);
			toolTipVerify(uiConstants.MODULE_HELPTEXT);

		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	public void toolTipVerificationforJsLibraries(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();

			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENT_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENT_TAB,methodName);
			click();

			Thread.sleep(3000);
			element=getXpathWebElement(uiConstants.REASUABLE_COMPONENTS_JSLIBRARIES_TAB);
			waitForElementPresent(uiConstants.REASUABLE_COMPONENTS_JSLIBRARIES_TAB,methodName);
			click();
			Thread.sleep(5000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_ADD_BUTTON);
			waitForElementPresent(uiConstants.JSLIBRARIES_ADD_BUTTON,methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_NAME);
			waitForElementPresent(uiConstants.JSLIBRARIES_NAME,methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_DESCRIPTION);
			waitForElementPresent(uiConstants.JSLIBRARIES_DESCRIPTION,methodName);
			click();
			Thread.sleep(3000);

			element=getXpathWebElement(uiConstants.JSLIBRARIES_HELPTEXT);
			waitForElementPresent(uiConstants.JSLIBRARIES_HELPTEXT,methodName);
			click();

			toolTipVerify(uiConstants.JSLIBRARIES_NAME);
			toolTipVerify(uiConstants.JSLIBRARIES_DESCRIPTION);
			toolTipVerify(uiConstants.JSLIBRARIES_HELPTEXT);






		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	public void testToolTipVerifyforConfigurationTemplate(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {
			Thread.sleep(5000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();
			Thread.sleep(5000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_TAB);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_TAB, methodName);
			click();

			Thread.sleep(5000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_BUTTON, methodName);
			click();


			Thread.sleep(3000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME, methodName);
			click();
			Thread.sleep(3000);
			element=getXpathWebElement(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION);
			waitForElementPresent(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION, methodName);
			click();
			toolTipVerify(uiConstants.CONFIGURATION_TEMPLATE_ADD_NAME);
			toolTipVerify(uiConstants.CONFIGURATION_TEMPLATE_ADD_DESCRIPTION);
		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	public void testToolTipVerifyforPilotProjetcs(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {
			Thread.sleep(5000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();
			Thread.sleep(5000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_TAB);
			waitForElementPresent(uiConstants.PILOT_PROJECT_TAB, methodName);
			click();

			Thread.sleep(5000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_BUTTON);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_BUTTON, methodName);
			click();

			Thread.sleep(5000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_NAME);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_NAME, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.PILOT_PROJECT_ADD_DESCRIPTION);
			waitForElementPresent(uiConstants.PILOT_PROJECT_ADD_DESCRIPTION, methodName);
			click();
			toolTipVerify(uiConstants.PILOT_PROJECT_ADD_NAME);
			toolTipVerify(uiConstants.PILOT_PROJECT_ADD_DESCRIPTION);
		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	public void testToolTipVerifyforDownloads(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {
			Thread.sleep(5000);
			waitForElementPresent(uiConstants.COMPONENTS_LINK,methodName);
			element=getXpathWebElement(uiConstants.COMPONENTS_LINK);
			click();
			Thread.sleep(5000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_TAB);
			waitForElementPresent(uiConstants.DOWNLOAD_TAB, methodName);
			click();

			Thread.sleep(5000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_BUTTON);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_BUTTON, methodName);
			click();
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_NAME);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_NAME, methodName);
			click();
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_DESCRIPTION);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_DESCRIPTION, methodName);
			click();
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.DOWNLOAD_ADD_VERSION);
			waitForElementPresent(uiConstants.DOWNLOAD_ADD_VERSION, methodName);
			click();

			toolTipVerify(uiConstants.DOWNLOAD_ADD_NAME);
			toolTipVerify(uiConstants.DOWNLOAD_ADD_DESCRIPTION);
			toolTipVerify(uiConstants.DOWNLOAD_ADD_VERSION);
		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}


	public void click() throws ScreenException {
		log.info("Entering:********click operation start********");
		try {
			element.click();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		log.info("Entering:********click operation end********");

	}
	public void type(String text)throws ScreenException{
		log.info("Entering:********enterText operation start********");
		try{

			element.sendKeys(text);

		}catch(Throwable t){
			t.printStackTrace();
		}
		log.info("Entering:********enterText operation end********");
	}


	public void clear() throws ScreenException {
		log.info("Entering:********clear operation start********");
		try {
			element.clear();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		log.info("Entering:********clear operation end********");

	}

  public void submit() throws ScreenException {
		log.info("Entering:********submit operation start********");
		try {
			element.submit();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		log.info("Entering:********submit operation end********");

	}
	public boolean isTextPresent(String text)
	{
		if(text!=null)
		{
			boolean value=driver.findElement(By.tagName("body")).getText().contains(text);
			System.out.println("--------TextCheck value---->"+text+"------------Result is-------------"+value); 
			Assert.assertTrue(value);
			return value;
		}
		else
		{
			throw new RuntimeException("---- Text not present----");
		}

	}
	
	
	public void  AddModuelsAndJarsVerification(String methodName)
	{
		try
		{
		if(methodName == "testPHPReusableComponentsAddModulesPage" || methodName == "testDrupal6ReusableComponentsAddModulesPage" 
				|| methodName == "testDrupal7ReusableComponentsAddModulesPage" || methodName =="testWordPressReusableComponentsAddModulesPage"|| methodName =="testMultiYUIReusableComponentsAddModulesPage"|| methodName =="testMultiJQueryReusableComponentsAddModulesPage"|| methodName =="testYUIMobileReusableComponentsAddModulesPage"|| methodName =="testJQueryMobileReusableComponentsAddModulesPage"|| methodName =="testDOTNETReusableComponentsAddModulesPage"|| methodName =="testSharePointReusableComponentsAddModulesPage"|| methodName =="testSiteCoreReusableComponentsAddModulesPage"
				|| methodName =="testJavaaloneReusableComponentsAddModulesPage"||methodName =="testNodejsReusableComponentsAddModulesPage")
		{
			getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
			getXpathWebElement(adminUIConstants.ADD_PilotProjects_PHP_TECHNOLOGY_VALUE).click();
			Thread.sleep(2000);
			isTextPresent(adminUIConstants.ADD_MODULE_PHP_NAME_VALUE);
			
			Thread.sleep(3000);
			getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
			getXpathWebElement(adminUIConstants.ADD_PilotProjects_DRUPAL6_TECHONOLGY_VALUE).click();
			Thread.sleep(2000);  
			isTextPresent(adminUIConstants.ADD_MODULE_PHP_NAME_VALUE);
			
			Thread.sleep(3000);
			getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
			getXpathWebElement(adminUIConstants.ADD_PilotProjects_DRUPAL7_TECHONOLGY_VALUE).click();
			Thread.sleep(2000);    
			isTextPresent(adminUIConstants.ADD_MODULE_PHP_NAME_VALUE);
			Thread.sleep(3000);
			
			
		}
		if(methodName == "testMultiYUIReusableComponentsAddJSlibrariesPage" || methodName == "testMultiJQueryReusableComponentsAddJSlibrariesPage" || methodName == "testYUIMobileReusableComponentsAddJSlibrariesPage" ||  methodName == "testJQueryMobileReusableComponentsAddJSlibrariesPage")
		{
			getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
			getXpathWebElement(adminUIConstants.ADD_PilotProjects_JQUERYMOBILE_TECHONOLGY_VALUE).click();
			Thread.sleep(2000);   
			isTextPresent(adminUIConstants.ADD_JSLIBRARIES_NAME_VALUE);
		
			getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
			getXpathWebElement(adminUIConstants.ADD_PilotProjects_MULTIJQUERY_TECHONOLGY_VALUE).click();
			Thread.sleep(2000);                 
		
			isTextPresent(adminUIConstants.ADD_JSLIBRARIES_NAME_VALUE);
			getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
			getXpathWebElement(adminUIConstants.ADD_PilotProjects_MULTIYUI_TECHONOLGY_VALUE).click();
			Thread.sleep(2000);              
		
			isTextPresent(adminUIConstants.ADD_JSLIBRARIES_NAME_VALUE);
			getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
			getXpathWebElement(adminUIConstants.ADD_PilotProjects_YUIMOBILE_TECHONOLGY_VALUE).click();
			Thread.sleep(2000);              
		
		}
		if(methodName == "testiPhoneHybridReusableComponentsAddvalidComponentPage" ||methodName == "testiphoneNativeReusableComponentsAddvalidComponentPage" || methodName == "testiPhoneLibraryReusableComponentsAddvalidComponentPage" || methodName == "testiPhoneWorkSpaceReusableComponentsAddvalidComponentPage" || methodName == "teroidHybridReusableComponentsAddvalidComponentPage"
				||methodName =="teroidNativeReusableComponentsAddvalidComponentPage")
		{
			getXpathWebElement(uiConstants.SELECT_TECHNOLOGY).click();
			getXpathWebElement(adminUIConstants.ADD_PilotProjects_ANDROIDHYBRID_TECHONOLGY_VALUE).click();
			Thread.sleep(2000);    
			isTextPresent(adminUIConstants.ADD_COMPONENT_NAME_VALUE);
		}}
		catch (Exception e)
		
		{
			e.printStackTrace();
		}
	}

	public void isdeleteButtonEnabled(String methodName) throws InterruptedException, IOException,
	Exception {


		if(driver.findElement(By.xpath(uiConstants.DELETE_BUTTON)).isEnabled()){

			System.out.println("-------isEnabled()----------------");
			waitForElementPresent(uiConstants.DELETE_BUTTON,methodName);
			element=getXpathWebElement(uiConstants.DELETE_BUTTON);
			click();
			Thread.sleep(10000);
			waitForElementPresent(uiConstants.ACCEPT_DELETE_OK,methodName);
			element=getXpathWebElement(uiConstants.ACCEPT_DELETE_OK);
			click();
			Thread.sleep(2000);


		}}

	//}




	public void selectText(WebElement element,String TextValue) throws ScreenException {
		log.info("Entering:---------get Select Text Webelement---------");
		try {
			Select selObj=new Select(element);
			selObj.selectByVisibleText(TextValue);
		} catch (Throwable t) {
			log.info("Entering:--------Exception in SelectextWebElement()-------");

			t.printStackTrace();

		}





	}


	public void toolTipVerify(String toolTipXpath) throws ScreenException {
		log.info("Entering:********toolTipVerify********");
		try {
			String tooltip=element.findElement(By.xpath(toolTipXpath)).getAttribute("title");
			System.out.println(tooltip);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		log.info("Entering:********enterText operation end********");
	}



	public void CreateAdminGlobalUrl(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {
			Thread.sleep(3000);
			element=getXpathWebElement(uiConstants.ADMIN_LINK);
			waitForElementPresent(uiConstants.ADMIN_LINK,methodName);
			click();
			waitForTextPresent("Customers");
			Thread.sleep(20000);
			
			element=getXpathWebElement(uiConstants.ADMIN_GLOBALURL_TAB);
			waitForElementPresent(uiConstants.ADMIN_GLOBALURL_TAB, methodName);
			click();

			Thread.sleep(5000);
			element=getXpathWebElement(uiConstants.GLOBALURL_ADD_BUTTON);
			waitForElementPresent(uiConstants.GLOBALURL_ADD_BUTTON, methodName);
			click();

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.GLOBALURL_NAME);
			waitForElementPresent(uiConstants.GLOBALURL_NAME, methodName);
			click();
			type(adminUIConstants.GLOBALURL_NAME_VALUE);
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.GLOBALURL_DESCRIPTION);
			waitForElementPresent(uiConstants.GLOBALURL_DESCRIPTION, methodName);
			click();
			type(adminUIConstants.GLOBALURL_DESCRIPTION_VALUE);
			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.GLOBALURL_URL);
			waitForElementPresent(uiConstants.GLOBALURL_URL, methodName);
			click();
			type(adminUIConstants.GLOBALURL_URL_VALUE);

			Thread.sleep(2000);
			element=getXpathWebElement(uiConstants.GLOBALURL_SAVE_BUTTON);
			waitForElementPresent(uiConstants.GLOBALURL_SAVE_BUTTON, methodName);
			click();
			Thread.sleep(2000);
            waitForTextPresent("new url added successfully");



		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	public void DeleteAdminGlobalUrl(String methodName)throws Exception {
		if (StringUtils.isEmpty(methodName)) {
			methodName = Thread.currentThread().getStackTrace()[1]
					.getMethodName();
		}
		try {
			System.out.println("----------deleteCreateSample Starts----------");
			Thread.sleep(2000);
			System.out.println("----------Check box Selection starts----------");
			waitForElementPresent(uiConstants.SELECT_CHECKBOX,methodName);
			element=getXpathWebElement(uiConstants.SELECT_CHECKBOX);
			click();
			System.out.println("----------Check box Selection ends----------");
			Thread.sleep(2000);

			isdeleteButtonEnabled(methodName);
			System.out.println("----------browser closw test----------");

			/*waitForElementPresent(uiConstants.DELETE_BUTTON,methodName);
			element=getXpathWebElement(uiConstants.DELETE_BUTTON);
			click();
			Thread.sleep(2000);
			waitForElementPresent(uiConstants.ACCEPT_DELETE_OK,methodName);
			element=getXpathWebElement(uiConstants.ACCEPT_DELETE_OK);
			click();*/
		}
		catch (InterruptedException e) {

			e.printStackTrace();
		}

	}
	/// RBAC Service Admin Test case Starts here 
	
	
		public void RBACServiceAdmin(String methodName)throws Exception {
			if (StringUtils.isEmpty(methodName)) {
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
			}
			try {
				Thread.sleep(3000);
				element=getXpathWebElement(uiConstants.ADMIN_LINK);
				waitForElementPresent(uiConstants.ADMIN_LINK,methodName);
				click();
				Thread.sleep(50000);
				waitForTextPresent("Customers");

				element=getXpathWebElement(uiConstants.USERS);
				waitForElementPresent(uiConstants.USERS,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.ADMIN_ROLES);
				waitForElementPresent(uiConstants.ADMIN_ROLES,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.ADMIN_ROLES_ASSIGN);
				waitForElementPresent(uiConstants.ADMIN_ROLES_ASSIGN,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.SERVICE_ADD_OK);
				waitForElementPresent(uiConstants.SERVICE_ADD_OK,methodName);
				click();
				Thread.sleep(1000);

				
			}catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		
		public void RBACServiceAssignPermissionMultiple(String methodName)throws Exception {
			if (StringUtils.isEmpty(methodName)) {
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
			}
			try {
				Thread.sleep(3000);
				element=getXpathWebElement(uiConstants.ADMIN_LINK);
				waitForElementPresent(uiConstants.ADMIN_LINK,methodName);
				click();
				Thread.sleep(50000);
				waitForTextPresent("Customers");
				
				element=getXpathWebElement(uiConstants.USERS);
				waitForElementPresent(uiConstants.USERS,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.ROLES);
				waitForElementPresent(uiConstants.ROLES,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.SERVICE);
				waitForElementPresent(uiConstants.SERVICE,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.SERVICE_ADD_BUTTON);
				waitForElementPresent(uiConstants.SERVICE_ADD_BUTTON,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.SERVICE_NAME);
				waitForElementPresent(uiConstants.SERVICE_NAME,methodName);
				click();
				type(adminUIConstants.NAME_VALUE);
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.SERVICE_DERSCRIPTION_VALUE);
				waitForElementPresent(uiConstants.SERVICE_DERSCRIPTION_VALUE,methodName);
				click();
				type(adminUIConstants.DERSCRIPTION_VALUE);
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.SERVICE_SAVE);
				waitForElementPresent(uiConstants.SERVICE_SAVE,methodName);
				click();
				Thread.sleep(5000);
				
				
				element=getXpathWebElement(uiConstants.ASSIGN_PERMISSION);
				waitForElementPresent(uiConstants.ASSIGN_PERMISSION,methodName);
				click();
				Thread.sleep(1000);
				
				element=getXpathWebElement(uiConstants.ADMIN_ROLES_ASSIGN);
				waitForElementPresent(uiConstants.ADMIN_ROLES_ASSIGN,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.SERVICE_ADD_OK);
				waitForElementPresent(uiConstants.SERVICE_ADD_OK,methodName);
				click();
				Thread.sleep(1000);
				
				element=getXpathWebElement(uiConstants.USERS);
				waitForElementPresent(uiConstants.USERS,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.ADMIN_ROLES);
				waitForElementPresent(uiConstants.ADMIN_ROLES,methodName);
				click();
				Thread.sleep(2000);
				
				isTextPresent(adminUIConstants .RBAC_SERVICE_MESSAGE_TEXT);
				
				Thread.sleep(2000);
				
				element=getXpathWebElement(uiConstants.ADMIN_ROLES_ASSIGN);
				waitForElementPresent(uiConstants.ADMIN_ROLES_ASSIGN,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.SERVICE_ADD_OK);
				waitForElementPresent(uiConstants.SERVICE_ADD_OK,methodName);
				click();
				Thread.sleep(1000);
						
			}catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		
		
		public void RBACServiceAssignPermissionSingle(String methodName)throws Exception {
			if (StringUtils.isEmpty(methodName)) {
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
			}
			try {
				Thread.sleep(3000);
				element=getXpathWebElement(uiConstants.ADMIN_LINK);
				waitForElementPresent(uiConstants.ADMIN_LINK,methodName);
				click();
				Thread.sleep(50000);
				waitForTextPresent("Customers");
				
				element=getXpathWebElement(uiConstants.USERS);
				waitForElementPresent(uiConstants.USERS,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.ROLES);
				waitForElementPresent(uiConstants.ROLES,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.SERVICE);
				waitForElementPresent(uiConstants.SERVICE,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.SERVICE_ADD_BUTTON);
				waitForElementPresent(uiConstants.SERVICE_ADD_BUTTON,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.SERVICE_NAME);
				waitForElementPresent(uiConstants.SERVICE_NAME,methodName);
				click();
				type(adminUIConstants.NAME_VALUE_SINGLE);
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.SERVICE_DERSCRIPTION_VALUE);
				waitForElementPresent(uiConstants.SERVICE_DERSCRIPTION_VALUE,methodName);
				click();
				type(adminUIConstants.DERSCRIPTION_VALUE);
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.SERVICE_SAVE);
				waitForElementPresent(uiConstants.SERVICE_SAVE,methodName);
				click();
				Thread.sleep(5000);
				
				isTextPresent(adminUIConstants.NAME_VALUE_SINGLE);
				Thread.sleep(2000);
				
				element=getXpathWebElement(uiConstants.ASSIGN_PERMISSION_SINGLE);
				waitForElementPresent(uiConstants.ASSIGN_PERMISSION_SINGLE,methodName);
				click();
				Thread.sleep(3000);
				
			   
				selectText(element, adminUIConstants.ROLE_VALUE_SINGLE);
				element=getXpathWebElement(uiConstants.ADMIN_ROLES_ASSIGN_SINGLE);
				waitForElementPresent(uiConstants.ADMIN_ROLES_ASSIGN_SINGLE,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.SERVICE_ADD_OK);
				waitForElementPresent(uiConstants.SERVICE_ADD_OK,methodName);
				click();
				Thread.sleep(1000);
				
				element=getXpathWebElement(uiConstants.USERS);
				waitForElementPresent(uiConstants.USERS,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.ADMIN_ROLES);
				waitForElementPresent(uiConstants.ADMIN_ROLES,methodName);
				click();
				Thread.sleep(2000);
				
				isTextPresent(adminUIConstants .NAME_VALUE_SINGLE);

				element=getXpathWebElement(uiConstants.POP_UP_CANCEL_BTN);
				waitForElementPresent(uiConstants.POP_UP_CANCEL_BTN,methodName);
				click();
				Thread.sleep(1000);
						
			}catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		public void ServiceAdminDeleteSingle(String methodName)throws Exception {
			if (StringUtils.isEmpty(methodName)) {
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
			}
			try {
				Thread.sleep(3000);
				element=getXpathWebElement(uiConstants.ADMIN_LINK);
				waitForElementPresent(uiConstants.ADMIN_LINK,methodName);
				click();
				Thread.sleep(50000);
				waitForTextPresent("Customers");

				element=getXpathWebElement(uiConstants.USERS);
				waitForElementPresent(uiConstants.USERS,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.SERVICE);
				waitForElementPresent(uiConstants.SERVICE,methodName);
				click();
				Thread.sleep(1000);
				
				element=getXpathWebElement(uiConstants.SELECT_SINGLE_SERVICE);
				waitForElementPresent(uiConstants.SELECT_SINGLE_SERVICE,methodName);
				click();
				Thread.sleep(1000);
				
				isdeleteButtonEnabled(methodName);
				
				element=getXpathWebElement(uiConstants.DELETE_BUTTON);
				waitForElementPresent(uiConstants.DELETE_BUTTON,methodName);
				click();
				Thread.sleep(2000);
	  
				element=getXpathWebElement(uiConstants.DELETE_BUTTON_OK);
				waitForElementPresent(uiConstants.DELETE_BUTTON_OK,methodName);
				click();
				Thread.sleep(5000);
			
				
			}catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		public void ServiceAdminDeleteMultiple(String methodName)throws Exception {
			if (StringUtils.isEmpty(methodName)) {
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
			}
			try {
				Thread.sleep(3000);
				element=getXpathWebElement(uiConstants.ADMIN_LINK);
				waitForElementPresent(uiConstants.ADMIN_LINK,methodName);
				click();
				Thread.sleep(50000);
				waitForTextPresent("Customers");

				element=getXpathWebElement(uiConstants.USERS);
				waitForElementPresent(uiConstants.USERS,methodName);
				click();
				Thread.sleep(1000);

				element=getXpathWebElement(uiConstants.SERVICE);
				waitForElementPresent(uiConstants.SERVICE,methodName);
				click();
				Thread.sleep(1000);
				
				element=getXpathWebElement(uiConstants.SELECT_MUTIPLE_SERVICE);
				waitForElementPresent(uiConstants.SELECT_MUTIPLE_SERVICE,methodName);
				click();
				Thread.sleep(1000);
				
				isdeleteButtonEnabled(methodName);
				
				element=getXpathWebElement(uiConstants.DELETE_BUTTON);
				waitForElementPresent(uiConstants.DELETE_BUTTON,methodName);
				click();
				Thread.sleep(2000);
	  
				element=getXpathWebElement(uiConstants.DELETE_BUTTON_OK);
				waitForElementPresent(uiConstants.DELETE_BUTTON_OK,methodName);
				click();
				Thread.sleep(5000);

				
			}catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		public void ServiceAdminVeification(String methodName)throws Exception {
			if (StringUtils.isEmpty(methodName)) {
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
			}
			try {
				Thread.sleep(3000);
				element=getXpathWebElement(uiConstants.ADMIN_LINK);
				waitForElementPresent(uiConstants.ADMIN_LINK,methodName);
				click();
				Thread.sleep(50000);
				waitForTextPresent("Customers");

				element=getXpathWebElement(uiConstants.USERS);
				waitForElementPresent(uiConstants.USERS,methodName);
				click();
				Thread.sleep(1000);

				getXpathWebElement(uiConstants.ROLES);
				waitForElementPresent(uiConstants.ROLES, methodName);
				click();
				element=getXpathWebElement(uiConstants.SERVICE);
				waitForElementPresent(uiConstants.SERVICE,methodName);
				click();
				Thread.sleep(1000);
				
				element=getXpathWebElement(uiConstants.SERVICE_ADMIN_PERMISSION);
				waitForElementPresent(uiConstants.SERVICE_ADMIN_PERMISSION,methodName);
				click();
				Thread.sleep(2000);
				
				isTextPresent("Manage Reusable Components");
				
				
				element=getXpathWebElement(uiConstants.POP_UP_CANCEL_BTN);
				waitForElementPresent(uiConstants.POP_UP_CANCEL_BTN,methodName);
				click();
				Thread.sleep(1000);
				
				
				
			}catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		public void ProjectAdminVeification(String methodName)throws Exception {
			if (StringUtils.isEmpty(methodName)) {
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
			}
			try {
				Thread.sleep(3000);
				element=getXpathWebElement(uiConstants.ADMIN_LINK);
				waitForElementPresent(uiConstants.ADMIN_LINK,methodName);
				click();
				Thread.sleep(50000);
				waitForTextPresent("Customers");

				element=getXpathWebElement(uiConstants.USERS);
				waitForElementPresent(uiConstants.USERS,methodName);
				click();
				Thread.sleep(1000);
				
				getXpathWebElement(uiConstants.ROLES);
				waitForElementPresent(uiConstants.ROLES, methodName);
				click();
				

				element=getXpathWebElement(uiConstants.SERVICE);
				waitForElementPresent(uiConstants.SERVICE,methodName);
				click();
				Thread.sleep(1000);
				
				element=getXpathWebElement(uiConstants.PROJECT_ADMIN_PERMISSION);
				waitForElementPresent(uiConstants.PROJECT_ADMIN_PERMISSION,methodName);
				click();
				Thread.sleep(2000);
				
				isTextPresent("Manage Reusable Components");
				
				
				element=getXpathWebElement(uiConstants.POP_UP_CANCEL_BTN);
				waitForElementPresent(uiConstants.POP_UP_CANCEL_BTN,methodName);
				click();
				Thread.sleep(1000);
				
				
				
			}catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		public void viewServiceVeification(String methodName)throws Exception {
			if (StringUtils.isEmpty(methodName)) {
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
			}
			try {
				Thread.sleep(3000);
				element=getXpathWebElement(uiConstants.ADMIN_LINK);
				waitForElementPresent(uiConstants.ADMIN_LINK,methodName);
				click();
				Thread.sleep(50000);
				waitForTextPresent("Customers");

				element=getXpathWebElement(uiConstants.USERS);
				waitForElementPresent(uiConstants.USERS,methodName);
				click();
				Thread.sleep(1000);
				
				getXpathWebElement(uiConstants.ROLES);
				waitForElementPresent(uiConstants.ROLES, methodName);
				click();
				

				element=getXpathWebElement(uiConstants.SERVICE);
				waitForElementPresent(uiConstants.SERVICE,methodName);
				click();
				Thread.sleep(1000);
				
				element=getXpathWebElement(uiConstants.VIEW_SERVICE_PERMISSION);
				waitForElementPresent(uiConstants.VIEW_SERVICE_PERMISSION,methodName);
				click();
				Thread.sleep(2000);
				
				isTextPresent("View Reusable Components");
				
				
				element=getXpathWebElement(uiConstants.POP_UP_CANCEL_BTN);
				waitForElementPresent(uiConstants.POP_UP_CANCEL_BTN,methodName);
				click();
				Thread.sleep(1000);
				
				
				
			}catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		public void ModuleAdminVeification(String methodName)throws Exception {
			if (StringUtils.isEmpty(methodName)) {
				methodName = Thread.currentThread().getStackTrace()[1]
						.getMethodName();
			}
			try {
				Thread.sleep(3000);
				element=getXpathWebElement(uiConstants.ADMIN_LINK);
				waitForElementPresent(uiConstants.ADMIN_LINK,methodName);
				click();
				Thread.sleep(50000);
				waitForTextPresent("Customers");

				element=getXpathWebElement(uiConstants.USERS);
				waitForElementPresent(uiConstants.USERS,methodName);
				click();
				Thread.sleep(1000);
				getXpathWebElement(uiConstants.ROLES);
				waitForElementPresent(uiConstants.ROLES, methodName);
				click();
				

				element=getXpathWebElement(uiConstants.SERVICE);
				waitForElementPresent(uiConstants.SERVICE,methodName);
				click();
				Thread.sleep(1000);
				
				element=getXpathWebElement(uiConstants.MODULE_ADMIN_PERMISSION);
				waitForElementPresent(uiConstants.MODULE_ADMIN_PERMISSION,methodName);
				click();
				Thread.sleep(2000);
				
				isTextPresent("Manage Reusable Components");
				
				
				element=getXpathWebElement(uiConstants.POP_UP_CANCEL_BTN);
				waitForElementPresent(uiConstants.POP_UP_CANCEL_BTN,methodName);
				click();
				Thread.sleep(1000);
				
				
				
			}catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
}




 

