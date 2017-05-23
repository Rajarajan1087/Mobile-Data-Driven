//updated 08 Aug 2016

package com.WebActions;

import io.appium.java_client.AppiumDriver;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;





//import com.DataFeeder.XP_OnlineSales;
//import com.DataFeeder.XP_UpgradeSales;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Support.Support;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

@SuppressWarnings("deprecation")
public class New_WebActions {

	public ExtentTest test;
	public AppiumDriver driver;
	
	public Selenium selenium;
	public Reporter Report;
	public static int timeOut = LoadEnvironment.timeOut;
	public static int t = timeOut;
	public static String xpath;
	public static int empty_int=999;
	Reporter reporter = null;
	Support support =null;
	
	public New_WebActions(AppiumDriver Driver, ExtentTest Test) {
		
		driver = Driver;
		test = Test;
		reporter = new Reporter(driver,test);
		support = new Support(driver,test);
	}
	
	public void AssertIsTrue(boolean condition, String testname,
			String whatYouAreLookingFor) throws Exception {

		try {
			Assert.assertTrue(condition, "Please refer to the screeen shot "
					+ testname + ".jpg for more details");
			Report.ReporterLog(whatYouAreLookingFor + " is found", LogStatus.PASS
					);
		} catch (Throwable t) {
			Report.ReporterLog(whatYouAreLookingFor + " is not found", LogStatus.FAIL
					);
			Report.result = false;
			Report.testPassed = false;
			Report.exception(testname, "");
		}
	}
	// -- custom function to perform action on assertTrue
	/*public void VerifyIsTrue(boolean condition, String testname,
			String whatYouAreLookingFor) throws Exception {

		try {
			Assert.assertTrue(condition, "Verification failed");
			Report.ReporterLog(whatYouAreLookingFor + " is found", LogStatus.PASS
					);
		} catch (Throwable t) {
			Report.testPassed = false;
			Report.result = false;
			errorCollector.addError(t);
			Report.ReporterLog(whatYouAreLookingFor + " is not found", LogStatus.FAIL
					);
		}
	}*/

	/*public void SpecCheck(String SpecFile,String... DeviceList)
    {
		try{
    	Galen GL=new Galen();
    	if(DeviceList.length>0)
    	{
    	LayoutReport layoutReport = GL.checkLayout(driver, "Specifications/"+SpecFile, Arrays.asList(DeviceList));
		// Creating an object that will contain the information about the test
		GalenTestInfo test = GalenTestInfo.fromString(SpecFile);

		// Adding layout report to the test report
		test.getReport().layout(layoutReport, "check layout on "+DeviceList+" device");
		System.out.println("Adding Tests to List");
		System.out.println(test);
		Report.tests.add(test);
    	}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }*/

	// Function that waits till the loading wheel vanishes
	public void fnWaitTillLoadingWheelVanishes() {
		WebDriverWait mylocalWait = new WebDriverWait(driver, timeOut * 3);
		mylocalWait.until(ExpectedConditions.invisibilityOfElementLocated(By
				.xpath("//*[@id='loadingBar']")));
	}
	public boolean VerifyElementPresentAndGetCheckBoxEnableDisableStatus(String element, String elementname) throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

			}
			try {
				By byElement = getElementFromLoc(element);
				WebElement checkBox = driver.findElement(byElement);
				if (waitForElementToAppear(byElement, timeOut)) {
					if(checkBox.isDisplayed()){
						return checkBox.isEnabled();
					}else{
						return checkBox.isEnabled();
					}
				}
			} catch (Exception e) {
				Report.exception(Report.testname, elementname);
			}
		}
	}
	public static By getElementFromLoc(String target) {
		StringBuffer element = new StringBuffer(target);
		if (target.startsWith("//") || target.startsWith("(//") ) {
			return (By.xpath(element.toString()));
		}
		if (target.startsWith("xpath=") || target.startsWith("XPATH=")) {
			element.delete(0, 6);
			return (By.xpath(element.toString()));
		}
		if (target.startsWith("id=") || target.startsWith("ID=")) {
			element.delete(0, 3);
			return (By.id(element.toString()));
		}
		if (target.startsWith("name=") || target.startsWith("NAME=")) {
			element.delete(0, 5);
			return (By.name(element.toString()));
		}
		if (target.startsWith("css=") || target.startsWith("CSS=")) {
			element.delete(0, 4);
			return (By.cssSelector(element.toString()));
		}
		if (target.startsWith("link=") || target.startsWith("LINK=")) {
			element.delete(0, 5);
			return (By.linkText(element.toString()));
		}
		if (target.startsWith("tagname=") || target.startsWith("TAGNAME=")) {
			element.delete(0, 8);
			return (By.tagName(element.toString()));
		}
		if (target.startsWith("classname=") || target.startsWith("CLASSNAME=")) {
			element.delete(0, 10);
			return (By.className(element.toString()));
		}
		return null;
	}

	// /Waits for a webelement
	// /<Returns> The webelement
	// /<Parameter elementId > the id of the element we are looking for
	// /<Parameter timeoutInSeconds> number of seconds to wait for the element
	public WebElement waitForElement(By elementId, int timeoutInSeconds) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds * 3);
			// wait.until(ExpectedConditions.invisibilityOfElementLocated(By
			// .xpath("//*[@id='loadingBar']")));
			wait.until(ExpectedConditions.visibilityOfElementLocated(elementId));
			System.out.println("element found " + elementId);
			return driver.findElement(elementId);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("element not found " + elementId
					+ " Error message:" + e.getMessage());
			return null;
		}

	}//
	public boolean DoesElementExists(String element) {
		try {
			System.out.println("Does element Exists : "+element);
			By byElement = getElementFromLoc(element);
			//if (waitForElementToAppear(byElement, timeOut)) {
			WebElement Newelement	=	driver.findElement(byElement);
			if(Newelement.isDisplayed()){
				return true;
			}else{
				return false;
			}

			//}

		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("element not found " + element);
			return false;
		}
	}
	public void waitForElementToDisappear(String element, int timeoutInSeconds) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds * 10);
			System.out.println("element found " + element);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(getElementFromLoc(element)));
			System.out.println("element disappeared " + element);
		} catch (Exception e) {
			System.out.println("element not found " + element
					+ " Error message:" + e.getMessage());
		}

	}

	/*
	 * public boolean waitForCondition(ExpectedConditions custCondition, String
	 * condition) { try {
	 * 
	 * myWait.until((Predicate<WebDriver>) custCondition);
	 * System.out.println(condition+" : achieved");
	 * 
	 * return true; } catch (Exception e) { System.out.println(condition);
	 * return false; }
	 * 
	 * }
	 */

	// /Waits for a webelement and returns true if found or false if not found
	// /<Returns> True if element is found in the given time or returns false
	// /<Parameter elementId > the id of the element we are looking for
	// /<Parameter timeoutInSeconds> number of seconds to wait for the element
	public boolean waitForElementToAppear(By elementId, int time) {
		if (waitForElement(elementId, time) != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean waitForElementToAppear(String element, int time) {
		if (waitForElement(getElementFromLoc(element), time) != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean waitForTableRefresh(By elementId, int time) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, time * 3);
			// wait.until(ExpectedConditions.invisibilityOfElementLocated(By
			// .xpath("//*[@id='loadingBar']")));
			wait.until(ExpectedConditions.refreshed(ExpectedConditions
					.visibilityOfElementLocated(elementId)));
			System.out.println("element found " + elementId);
			return true;
		} catch (Exception e) {
			System.out.println("element not found " + elementId
					+ " Error message:" + e.getMessage());
			return false;
		}
	}

	public void VerifyElementPresentAndAssertClick(String TargetElement, String elementname,String ResultantElement)throws Exception {
		int i=0;
		while(i==0)
		{
			for (int second = 0;; second++) {
				if (second >= t) {

					Report.ReporterLog(elementname + " link is not found", LogStatus.FAIL
							);
					Report.result = false;
					Report.exception(Report.testname, elementname);
				}
				try {
					By byElement = getElementFromLoc(TargetElement);
					if (waitForElementToAppear(byElement, timeOut)) {

						Report.ReporterLog(elementname + " is found", LogStatus.PASS
								);
						WebElement WE=driver.findElement(byElement);
						Thread.sleep(200);
						//selenium.click(xpath);
						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",WE);
						driver.findElement(byElement).click();
						isAlertPresent();

						Report.ReporterLog("Clicked on " + elementname, LogStatus.PASS
								);
						System.out.println("clicked on item, breaking");
						break;
					}
				} catch (Exception e) {
					System.out
					.println("-----------------------------------------------------------------------");
					System.out.println(e.getLocalizedMessage());
					System.out
					.println("-----------------------------------------------------------------------");


					Report.ReporterLog(elementname + " link is not found", LogStatus.FAIL
							);
					Report.result = false;
					Report.exception(Report.testname, elementname);
					i=1;

				}
				// Thread.sleep(1000);
			}
			try
			{
				driver.findElement(getElementFromLoc(ResultantElement));
				i=1;
			}
			catch(Exception E)
			{
				System.out.println("Trying to click on Target Element");
			}
		}
	}


	public boolean elementExists(String element, int localTimeout) {
		if (waitForElement(getElementFromLoc(element), localTimeout) != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isAlertPresent() 
	{ 
		boolean result=true;

		try 
		{ 
			final WebDriverWait myCustWait = new WebDriverWait(driver, LoadEnvironment.custTimeOut/5);
			try
			{
				myCustWait.until(ExpectedConditions.not(ExpectedConditions.alertIsPresent()));
				result=false;
			}
			catch(Exception E){
				System.out.println("Checking for Alert box...");
				myCustWait.until(ExpectedConditions.alertIsPresent());
				String alertText=driver.switchTo().alert().getText();
				if((alertText.matches("(?s)Are you sure you want to(.*)"))||alertText.matches("(?s)Please confirm that you wish to add the following bars:(.*)"))
				{	
					System.out.println("Cancel Button is pressed");
					driver.switchTo().alert().accept();
					result=true;
				}
				else
				{
					System.out.println("Alert box is found");
					driver.switchTo().alert().dismiss();
					myCustWait.until(ExpectedConditions.not(ExpectedConditions.alertIsPresent()));
					result=true;
				}
			}
		}
		catch (NoAlertPresentException Ex) 
		{ 
			System.out.println("catchNoAlert");
			result=false; 
		}   // catch 
		return result;
	}
	public void VerifyElementPresentAndType(String element, String elementname,
			String content){
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " link is not found", LogStatus.FAIL
						);
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			try {
				By byElement = getElementFromLoc(element);
				if (waitForElementToAppear(byElement, timeOut)) {
					// selenium.type(xpath, content);
					driver.findElement(byElement).sendKeys(content);

					System.out.println(elementname + " text box is found and "+ content + " is entered");
					break;
				}
			} catch (Exception e) {
			}
			// Thread.sleep(1000);
		}
	}
/*
	public void VerifyElementPresentAndClearType(String element, String elementname, String content) throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " link is not found", LogStatus.FAIL);
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			try {
				By byElement = getElementFromLoc(element);
				if (waitForElementToAppear(byElement, timeOut)) {
					// selenium.type(xpath, content);
					driver.findElement(byElement).clear();
					driver.findElement(byElement).sendKeys(content);
					System.out.println(elementname + " text box is found and "+ content + " is entered");
					break;
				}
			} catch (Exception e) {
			}
		}
		
		}
	*/
	public boolean VerifyElementPresentAndClearType(String element, String elementname, String content) throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " link is not found", LogStatus.FAIL);
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			try {
				By byElement = getElementFromLoc(element);
				if (waitForElementToAppear(byElement, timeOut)) {
					// selenium.type(xpath, content);
					driver.findElement(byElement).clear();
					driver.findElement(byElement).sendKeys(content);
					System.out.println(elementname + " text box is found and "+ content + " is entered");
					return true;
				}
			} catch (Exception e) {
			}
		}
		
		}


	public void VerifyElementPresentAndJType(String element, String elementname, String content) throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " link is not found", LogStatus.FAIL
						);
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			try {
				By byElement = getElementFromLoc(element);
				if (waitForElementToAppear(byElement, timeOut)) {
					selenium.type(element, content);
					System.out.println(elementname + " text box is found and "+ content + " is entered");
					break;
				}
			} catch (Exception e) {
			}}}

	public void VerifyElementPresentAndSelect(String element,
			String elementname, String content) {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " drop down is not found",
						LogStatus.FAIL );
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			try {
				System.out.println("Select By :"+element);
				By byElement = getElementFromLoc(element);
				WebElement WE=driver.findElement(byElement);
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",WE);
				if (waitForElementToAppear(byElement, timeOut)) {
					WebElement selectElm = driver.findElement(byElement);
					Select select = new Select(selectElm);
					select.selectByVisibleText(content);
					System.out.println(elementname + " drop down is found and "+ content + " is entered");
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}}}
	public void VerifyElementPresentAndSelect(String element,
			String elementname, int Option) {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " drop down is not found",
						LogStatus.FAIL );
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			try {
				By byElement = getElementFromLoc(element);
				if (waitForElementToAppear(byElement, timeOut)) {
					WebElement selectElm = driver.findElement(byElement);
					Select select = new Select(selectElm);
					select.selectByIndex(Option);
					String SelectedValue=select.getFirstSelectedOption().getText();
					System.out.println(elementname + " drop down is found and "+ SelectedValue + " is entered");
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}}}
	public List<WebElement> VerifyElementPresentAndGetElementList(String element){
		try{
			By byElement = getElementFromLoc(element);
			List<WebElement> ElementList	=	driver.findElements(byElement);

			return ElementList;
		}catch(NoSuchElementException nsee){
			nsee.printStackTrace();
		}catch(ElementNotVisibleException enve){
			enve.printStackTrace();
		}
		return null;
	}
	public String VerifyElementPresentAndGetText(String element,
			String elementname) {
		String returnText="";
		System.out.println(element);
		for (int StaleCount = 1; StaleCount < 4; StaleCount++) {
			try {
				By byElement = getElementFromLoc(element);
				List<WebElement> we=driver.findElements(byElement);
				//					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",WE);
				returnText=we.get(0).getText();
				/*if (waitForElementToAppear(byElement, timeOut)) {
						 highlight section added 
						//highlightElement(driver.findElement(byElement));
						//unhighlightElement(driver.findElement(byElement));
						//WebElement WE1=driver.findElement(byElement);
						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",WE);
						String text = driver.findElement(byElement).getText();
						System.out.println("TEXT RETRIVED IS :" + text
								+ "TEXT RETRIVED BY SEL IS :"
								+ selenium.getText(element));
						return text.trim();
					}*/
			} catch (Exception e) {
				if (StaleCount == 3) {
					return "NA";
				} else {
					continue;
				}
			}
		}
		return returnText.replaceAll("^,", "");
	}
	public String VerifyElementPresentAndGetSingleTextUsingJS(String element,
			String elementname) {
		String returnText="";
		System.out.println(element);
		for (int StaleCount = 1; StaleCount < 4; StaleCount++) {
			try {
				By byElement = getElementFromLoc(element);
				WebElement WE=driver.findElement(byElement);
				//if (waitForElementToAppear(byElement, timeOut)) {
				
						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",WE);
						String text = driver.findElement(byElement).getText();
						System.out.println("TEXT RETRIVED IS :" + text
								+ "TEXT RETRIVED BY SEL IS :"
								+ selenium.getText(element));
						return text.trim();
					//}
			} catch (Exception e) {
				if (StaleCount == 3) {
					return "NA";
				} else {
					continue;
				}
			}
		}
		return returnText.replaceAll("^,", "");
	}
	public String VerifyElementPresentAndGetTextByJS(String element,String elementName){
		for (int StaleCount = 1; StaleCount < 4; StaleCount++) {
			try {
				By byElement = getElementFromLoc(element);
				//if (waitForElementToAppear(byElement, timeOut)) {
				JavascriptExecutor js = (JavascriptExecutor)driver;
				WebElement webElement = driver.findElement(byElement);
				System.out.println(element);
				//js.executeScript("return arguments[0].text", webElement);
				//String sText =  js.executeScript("return document.documentElement.innerText;").toString();
				String text = js.executeScript("return arguments[0].value;",webElement).toString();
				System.out.println("TEXT RETRIVED IS :" + text
						+ "TEXT RETRIVED BY SEL IS :"
						+ selenium.getText(element));
				return text.trim();
				//}
			} catch (Exception e) {
				if (StaleCount == 3) {
					return "";
				} else {
					continue;
				}
			}
		}
		return null;
	}

	public String VerifyElementPresentAndGetTextForSelect(String element,
			String elementname) throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + "is not found", LogStatus.FAIL
						);
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			for (int StaleCount = 1; StaleCount < 4; StaleCount++) {
				try {
					By byElement = getElementFromLoc(element);
					if (waitForElementToAppear(byElement, timeOut)) {
						// String text = selenium.getText(xpath);
						String text = driver.findElement(byElement).getText();
						System.out.println("TEXT RETRIVED IS :" + text
								+ "TEXT RETRIVED BY SEL IS :"
								+ selenium.getText(element));
						return text;
					}
				} catch (Exception e) {
					if (StaleCount == 3) {
						return "";
					} else {
						continue;
					}
				}
			}
		}
	}

	public String VerifyElementPresentAndGetAttribute(String element,
			String Attribute) throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(Attribute + "is not found", LogStatus.FAIL
						);
				Report.result = false;
				Report.exception(Report.testname, Attribute);
			}
			try {
				By byElement = getElementFromLoc(element);
				if (waitForElementToAppear(byElement, timeOut)) {
					String text = null;
					try {
						text = driver.findElement(byElement).getAttribute(
								Attribute);
					} catch (Exception e) {
						Report.fnReportFailAndTerminateTest("Attribute not found", "Attribute "+Attribute+" is not found", driver);
					}
					//System.out.println(Attribute + " is found");
					return text.trim();
				}
			} catch (Exception e) {
				return "";
			}
			// Thread.sleep(100);
		}

	}
	public String VerifyElementPresentAndGetAttributeUsingJS(String element, String Attribute) throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(Attribute + "is not found", LogStatus.FAIL
						);
				Report.result = false;
				Report.exception(Report.testname, Attribute);
			}
			try {
				By byElement = getElementFromLoc(element);



				WebElement elementId = driver.findElement(byElement);
				JavascriptExecutor	js	=	(JavascriptExecutor)driver;
				String checkedState =   js.executeScript("return arguments[0].getAttribute('"+Attribute+"');", elementId).toString();
				System.out.println("checkedState : "+checkedState);
				return checkedState.trim();

			} catch (Exception e) {
				return "";
			}
			// Thread.sleep(100);
		}

	}
	public String VerifyElementPresentAndGetValue(String element,
			String elementname) throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + "is not found", LogStatus.FAIL
						);
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			try {
				By byElement = getElementFromLoc(element);
				if (waitForElementToAppear(byElement, timeOut)) {
					String text = selenium.getValue(element);
					System.out.println(elementname + " is found");
					return text;
				}
			} catch (Exception e) {
				return "";
			}
			// Thread.sleep(1000);
		}
	}

	public String VerifyElementPresentAndGetclass(String element,
			String elementname) throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + "is not found", LogStatus.FAIL
						);
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			try {
				By byElement = getElementFromLoc(element);
				if (waitForElementToAppear(byElement, timeOut)) {
					// String text = selenium.getValue(xpath);
					String text = driver.findElement(byElement).getAttribute(
							"class");
					System.out.println(elementname + " is found");
					return text;
				}
			} catch (Exception e) {
				return "";
			}
			// Thread.sleep(1000);
		}
	}

	/*
	 * Method Name: MouseOver Description: This method is used to mouse over the
	 * element for a few seconds. xpath of the required element is passed for
	 * mouse over
	 */
	public void MouseOver(String element) throws Exception {
		Actions builder = new Actions(driver);
		try {
			By byElement = getElementFromLoc(element);
			System.out.println("before mousehover");
			WebElement tagElement = driver.findElement(byElement);
			builder.moveToElement(tagElement).build().perform();
			Thread.sleep(1000);
			System.out.println("after mousehover");
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest(xpath,"Exception while mouse hover : " + xpath,driver);
		}
	}

	// //Gets the text of the selected option from the dropdown
	public String GetTheSelectedOption(By element) {
		try {
			WebElement webElement = waitForElement(element, timeOut);
			Select select = new Select(webElement);
			String text = select.getFirstSelectedOption().getText();
			return text;
		} catch (Exception e) {
			return "";
		}
	}
	public int GetElementSizeByXpath(String element){
		try{

			By byElement = getElementFromLoc(element);
			if(waitForElementToAppear(byElement, timeOut))
			{
				List<WebElement> allAddresses = driver.findElements(byElement); 
				return allAddresses.size();
			}else{
				System.out.println("No Such Element to fetch the list");
			}
		}catch(NullPointerException npe){

		}catch(Exception e){

		}
		return 0;
	}
	public boolean VerifyElementPresentAndGetCheckBoxStatus(String element, String elementname) throws Exception {
		try {
			By byElement = getElementFromLoc(element);
			if(waitForElementToAppear(byElement, timeOut))
			{
				WebElement checkBox = driver.findElement(byElement);
				return checkBox.isSelected();
			}
			else{
				Report.ReporterLog(elementname + " link is not found", LogStatus.FAIL,driver);
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
		} catch (Exception e) {
			Report.exception(Report.testname, elementname);
		}
		return false;
	}
	public void VerifyElementPresentAndClickFirst(String element, String elementname){
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " is not found", LogStatus.FAIL,driver);
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			try {
				By byElement = getElementFromLoc(element);
				if (waitForElementToAppear(byElement, timeOut)) {
					Thread.sleep(200);
					//selenium.click(xpath);
					//	WebElement WE=driver.findElement(byElement);
					List<WebElement> WE=driver.findElements(byElement);
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",WE.get(0));
					//highlightElement(driver.findElement(byElement));
					//unhighlightElement(driver.findElement(byElement));
					WE.get(0).click();
					System.out.println("clicked on item, breaking");
					break;
				}
			} catch (Exception e) {
				System.out
				.println("-----------------------------------------------------------------------");
				System.out.println(e.getLocalizedMessage());
				System.out
				.println("-----------------------------------------------------------------------");


				Report.ReporterLog(elementname + " link is not found", LogStatus.FAIL
						);
				Report.result = false;
				Report.exception(Report.testname, elementname);

			}
			// Thread.sleep(1000);
		}
	}
	// -- Function which waits for the presence of the web element and clicks
	public boolean VerifyElementPresentAndClick(String element, String elementname){
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " is not found", LogStatus.FAIL,driver);
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			try {
				System.out.println("element :"+element);
				By byElement = getElementFromLoc(element);
				WebElement WE=driver.findElement(byElement);
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",WE);
				if (waitForElementToAppear(byElement, timeOut)) {
					Thread.sleep(200);
					//selenium.click(xpath);
					//	WebElement WE=driver.findElement(byElement);
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",WE);
					//highlightElement(driver.findElement(byElement));
					//unhighlightElement(driver.findElement(byElement));
					driver.findElement(byElement).click();
					System.out.println("clicked on item, breaking");
					return true;
					//break;
				}
			} catch (Exception e) {
				System.out
				.println("-----------------------------------------------------------------------");
				System.out.println(e.getLocalizedMessage());
				System.out
				.println("-----------------------------------------------------------------------");


				Report.ReporterLog(elementname + " link is not found", LogStatus.FAIL
						);
				Report.result = false;
				Report.exception(Report.testname, elementname);

			}
			// Thread.sleep(1000);
		}
	}

	public  boolean VerifyElementPresentAndSelectFromDropDown(String element,String elementValue)throws Exception {
		By byElement = getElementFromLoc(element);
		//if (waitForElementToAppear(byElement, timeOut)) {
		WebElement selectElement = driver.findElement(byElement); 
		Select select = new Select(selectElement);

		List <WebElement> allOptions = select.getOptions();

		for(WebElement ele:allOptions){
			String mainString = ele.getText();
			if(mainString.equalsIgnoreCase(elementValue)){
				//select.selectByVisibleText(mainString);
				select.selectByValue(mainString);
				return true;
			}else{
				continue;
			}
		} 
		//}
		return false;
	}

	// -- Function which waits for the presence of the web element and hover on
	// element
	public void VerifyElementPresentAndHover(String element, String elementname)
			throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " link is not found", LogStatus.FAIL
						);
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			try {
				By byElement = getElementFromLoc(element);
				if (waitForElementToAppear(byElement, timeOut)) {

					Report.ReporterLog(elementname + " is found", LogStatus.PASS
							);
					Thread.sleep(200);
					// selenium.click(xpath);
					Actions action = new Actions(driver);
					WebElement we = driver.findElement(byElement);
					action.moveToElement(we).perform();


					Report.ReporterLog("Clicked on " + elementname, LogStatus.PASS
							);
					System.out.println("clicked on item, breaking");
					break;
				}
			} catch (Exception e) {
				System.out
				.println("-----------------------------------------------------------------------");
				System.out.println(e.getLocalizedMessage());
				System.out
				.println("-----------------------------------------------------------------------");


				Report.ReporterLog(elementname + " link is not found", LogStatus.FAIL
						);
				Report.result = false;
				Report.exception(Report.testname, elementname);

			}
			// Thread.sleep(1000);
		}
	}


	// -- function to check the option box
	public void VerifyElementPresentAndClickOption(String element,
			String elementname) throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " check box is not found",
						LogStatus.FAIL );
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			try {
				By byElement = getElementFromLoc(element);
				WebElement checkBox = driver.findElement(byElement);
				if (waitForElementToAppear(byElement, timeOut)) {
					int i = 0;
					while (!checkBox.isSelected()) {
						checkBox.click();
						i = i + 1;
						if (i == 5) {
							break;
						}
					}
					if (!checkBox.isSelected()) {
						Report.ReporterLog("Failed to set the check box "
								+ elementname, LogStatus.FAIL );
						Report.result = false;
						Report.exception(Report.testname, elementname);
					} else {

						Report.ReporterLog(elementname
								+ " option box is found clicked ", LogStatus.PASS
								);
						break;
					}
				}
			} catch (Exception e) {
				Report.ReporterLog(
						"Failed to set the check box " + elementname, LogStatus.FAIL
						);
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			// Thread.sleep(1000);
		}
	}

	// -- function to get all elements
	public List<WebElement> VerifyElementPresentAndGetElements(String element,
			String elementname) throws Exception {
		List<WebElement> elementsList = new ArrayList<WebElement>();
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + "is not found", LogStatus.FAIL
						);
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			try {
				By byElement = getElementFromLoc(element);
				if (waitForElementToAppear(byElement, timeOut)) {
					elementsList = driver.findElements(byElement);

					Report.ReporterLog(elementname + " is found", LogStatus.PASS
							);
					return elementsList;
				}
			} catch (Exception e) {
				return null;
			}
			// Thread.sleep(1000);
		}
	}

	// -- function to check/uncheck the check box
	public void VerifyElementPresentAndCheck(String element,
			String elementname, boolean setCheck){
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " check box is not found",
						LogStatus.FAIL );
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			try {
				By byElement = getElementFromLoc(element);
				WebElement checkBox = driver.findElement(byElement);
				if (waitForElementToAppear(byElement, timeOut)) {
					if (setCheck && !checkBox.isSelected()) {
						checkBox.click();
					} else if (!setCheck && checkBox.isSelected()) {
						checkBox.click();
					}

					System.out.println(elementname
							+ " check box is found and set to " + setCheck);
					break;
				}
			} catch (Exception e) {
				Report.ReporterLog(
						"Failed to set the check box " + elementname, LogStatus.FAIL
						);
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			// Thread.sleep(1000);
		}
	}
	public boolean VerifyElementDisabled(String element, String elementname)
			throws Exception {
		try 
		{
			if (VerifyElementPresent(element,elementname)) 
			{
				if(driver.findElement(getElementFromLoc(element)).isEnabled())
				{


					Report.result = false;
					Report.ReporterLog(elementname + " is Enabled", LogStatus.FAIL
							);
					Report.exception(Report.testname, elementname);

				}
				else
				{

					Report.ReporterLog(elementname + " is Disabled", LogStatus.PASS
							);
					Report.result = true;
				}
			}
		}
		catch (Exception e) {
		}
		Thread.sleep(1000);
		return Report.result;
	}
	public int getElementCount(String element,String elementname) throws Exception
	{
		int count=0;
		try {
			By byElement = getElementFromLoc(element);
			driver.findElements(byElement);
			if (waitForElementToAppear(byElement, timeOut)) {
				List<WebElement> WE=driver.findElements(byElement);
				count=WE.size();
			}
		} catch (Exception e) {
			Report.ReporterLog(elementname + " is not found", LogStatus.FAIL,driver);
			Report.result = false;
			Report.testPassed = false;
			Report.exception(Report.testname, elementname);
			e.printStackTrace();
		}
		return count;
	}
	// -- Function which waits for the presence of the web element
	public boolean VerifyElementPresent(String element, String elementname)
			throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " is not found", LogStatus.FAIL,driver);
				Report.result = false;
				Report.testPassed = false;
				Report.exception(Report.testname, elementname);
				break;
			}
			try {
				By byElement = getElementFromLoc(element);
				if (waitForElementToAppear(byElement, timeOut)) {

					highlightElement(driver.findElement(byElement));

					System.out.println(elementname + " is found");
					Report.result = true;
					//					Report.TakeScreenshot(driver);
					unhighlightElement(driver.findElement(byElement));
					break;
				}
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		return Report.result;
	}
	public boolean VerifyElementPresentByJavaScriptExecuor(String element, String elementname)
			throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " is not found", LogStatus.FAIL
						);
				Report.result = false;
				Report.testPassed = false;
				//Report.exception(Report.testname, elementname);
				break;
			}
			try {
				if(second==0){
					By byElement = getElementFromLoc(element);
					WebElement testelement	=	driver.findElement(byElement);
					JavascriptExecutor js	=	(JavascriptExecutor) driver;
					js.executeScript("arguments[0].click();", testelement);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(1000);
		}
		return Report.result;
	}
	public boolean VerifyElementSelected(String element, String elementname)
			throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " is not found", LogStatus.FAIL
						);
				Report.result = false;
				Report.testPassed = false;
				//Report.exception(Report.testname, elementname);
				break;
			}
			try {
				if(second==0){
					By byElement = getElementFromLoc(element);
					WebElement testelement	=	driver.findElement(byElement);
					JavascriptExecutor js	=	(JavascriptExecutor) driver;
					String scrollHeight	=	js.executeScript("return document.body.scrollHeight", testelement).toString();
					for(int scrollCount=0;scrollCount<Integer.parseInt(scrollHeight);scrollCount++){
						String scrollJS	=	"window.scrollBy(0,"+Integer.toString(scrollCount)+")";
						js.executeScript(scrollJS, testelement);
						if(testelement.isEnabled() && !testelement.isSelected()){
							js.executeScript("arguments[0].click();", testelement);
							System.out.println("Clicking venky");
							Thread.sleep(500);
							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(1000);
		}
		return Report.result;
	}
	public boolean VerifyElementPresentScrollByExecutor(String element, String elementname)
			throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " is not found", LogStatus.FAIL
						);
				Report.result = false;
				Report.testPassed = false;
				//Report.exception(Report.testname, elementname);
				break;
			}
			try {
				By byElement = getElementFromLoc(element);
				WebElement testelement	=	driver.findElement(byElement);



				JavascriptExecutor js	=	(JavascriptExecutor) driver;
				String height	=	js.executeScript("return document.body.scrollHeight", testelement).toString();
				System.out.println("Height : "+height);
				js.executeScript("window.scrollBy(0,100)", testelement);//document.body.scrollHeight
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(1000);
		}
		return Report.result;
	}
	public boolean VerifyElementPresent(String element, String elementname,boolean Terminate)
			throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " is not found", LogStatus.FAIL);
				Report.result = false;
				if(Terminate){
					Report.exception(Report.testname, elementname);
				}
			}
			try {
				By byElement = getElementFromLoc(element);
				if (waitForElementToAppear(byElement, timeOut)) {

					Report.ReporterLog(elementname + " is found", LogStatus.PASS
							);
					Report.result = true;
					break;
				}
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		return Report.result;
	}

	public boolean VerifyElementRefreshed(String element, String elementname)
			throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " is not found", LogStatus.FAIL
						);
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			try {
				By byElement = getElementFromLoc(element);
				if (waitForTableRefresh(byElement, timeOut)) {

					Report.ReporterLog(elementname + " is found", LogStatus.PASS
							);
					Report.result = true;
					break;
				}
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		return Report.result;
	}

	// -- Function which waits for the Absence of the web element
	public boolean VerifyElementNotPresent(String element, String elementname)
			throws Exception {
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname + " link is not found", LogStatus.PASS
						);
				Report.result = true;
				break;
			}
			try {
				By byElement = getElementFromLoc(element);
				if (waitForElementToAppear(byElement, timeOut)) {


					Report.result = false;
					Report.ReporterLog(elementname + " is found", LogStatus.FAIL
							);
					Report.exception(Report.testname, elementname);

				}
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		return Report.result;
	}

	// -- Function which waits for the presence of the web element and verifies
	// the presence of text
	public boolean VerifyElementAndTextPresent(String element,
			String elementname) throws Exception {
		String Str_verifyText = "";
		for (int second = 0;; second++) {
			if (second >= t) {

				Report.ReporterLog(elementname
						+ " link is not found , Instead found:"
						+ Str_verifyText, LogStatus.FAIL );
				Report.result = false;
				Report.exception(Report.testname, elementname);
			}
			try {
				By byElement = getElementFromLoc(element);
				if (waitForElementToAppear(byElement, timeOut)) {
					Str_verifyText = driver.findElement(byElement).getText()
							.trim();
					if (elementname.equalsIgnoreCase(Str_verifyText)
							|| elementname.matches(Str_verifyText)
							|| elementname.matches("(?s)(.*)" + Str_verifyText
									+ "(.*)")
									|| elementname.contains(Str_verifyText)) {

						Report.ReporterLog(elementname + " is found", LogStatus.PASS
								);
						Report.result = true;
						break;
					}
				}
			} catch (Exception e) {
				Report.result = false;
			}
			// Thread.sleep(1000);
		}
		return Report.result;
	}

	public void waitTillAjaxbyJqueryIsComplete() {
		while (true) // Handle timeout somewhere
		{
			System.out.println(((JavascriptExecutor) driver)
					.executeScript("return jQuery.active == 0"));
			boolean ajaxIsComplete;
			ajaxIsComplete = (Boolean) (((JavascriptExecutor) driver)
					.executeScript("return jQuery.active == 0"));
			if (ajaxIsComplete)
				break;
		}

	}

	// Function to wait till the title is available
	public void fnWaitTillTitle(String strTitle) throws Exception {
		try {
			WebDriverWait myCustWait = new WebDriverWait(driver, timeOut * 3);
			myCustWait.until(ExpectedConditions.titleIs(strTitle));
			Report.fnReportPass(strTitle,driver);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest(strTitle,"Waiting for Page title for too long",driver);
		}
	}
	public boolean highlightElement(WebElement elem)
	{
		System.out.println("in highlight");
		try {
			JavascriptExecutor js = (JavascriptExecutor)this.driver;

			js.executeScript("arguments[0].style.outlineStyle='solid';arguments[0].style.outlineWidth='5px';arguments[0].style.outlineColor='yellow';", new Object[] { elem });
			return true;
		}
		catch (Exception e)
		{
			return false;
		}

	}



	public boolean unhighlightElement(WebElement elem)
	{

		try {
			JavascriptExecutor js = (JavascriptExecutor)this.driver;

			js.executeScript("arguments[0].style.outlineStyle='none';", new Object[] { elem });
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

}

