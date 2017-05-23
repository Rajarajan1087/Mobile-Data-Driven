package com.Screens.CommonScreens;

import java.util.ArrayList;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Support.Support;
import com.WebActions.New_WebActions;
//import com.WelcomeCentre.BusinessModules.RegisterToMyAccount;



public class AIP_WelcomeCenter {
	
	public ExtentTest test;
	public AppiumDriver driver;
	New_WebActions WB = null;
	Reporter reporter = null;
	
	public AIP_WelcomeCenter(AppiumDriver Driver, ExtentTest Test) 
	{
		driver=Driver;
		test=Test;
		WB = new New_WebActions (driver,test);
		reporter = new Reporter(driver, test);
	}
	
	public enum MAMobileCategory{
		Minutes,Texts,Data,OnNetMinutes
	}
	
	public void WCNewLogin(String getDevice) throws Exception
	{
		
		String XP_Username ="//*[@name='loginForm']//button[@type='submit']";
		System.out.println("Entering into script");
		Thread.sleep(5000);
		SwitchToWebView();
		Thread.sleep(3000);
		
		WebElement getUserName = driver.findElement(By.xpath(XP_Username));
		getUserName.click();
		test.log(LogStatus.PASS, "Verified Submit Button");
		System.out.println("exit............");
		Thread.sleep(2000);
		
	}
	
	
	// CSA Login
	public void loginIntoMyAccount_CSA(String username, String password) throws Exception {
		
		String XP_Logo="//div[contains(@class,'logo')]";
		String XP_VerifyText="//*[text()='";
		String []VerifyTextLogin={"My Account login","Login with your My Account details below.","Email:","Email is mandatory","Password:","Enter your password","Login","Forgot My Password","Register now"};
		String XP_Username="//input[@ng-model='username']";
		String XP_Password="//input[@ng-model='password']";
		String XP_LogIN="//*[@name='loginForm']//button[@type='submit']";
		//String  Email=Str_CLI+"@autocentral.com";
		String  Email="01564217880@autocentral.com";
		String XP_HomepageLogo="//*[contains(@class,'logo')]/ancestor::div[1][not(contains(@style,'opacity: 0'))]";
		boolean Terminate = false;
		
/*********************** Verify Texts  **************************/
		
		SwitchToWebView();
		
		//WB.VerifyElementPresent(XP_Logo, "Logo", Terminate);
      boolean getStatus = WB.VerifyElementPresentAndClearType(XP_Username, "Username",Email);
      if(getStatus){
    	reporter.ReporterLog("username", LogStatus.PASS);
      }
      Thread.sleep(2000);

     boolean getStatus1 =	WB.VerifyElementPresentAndClearType(XP_Password, "Password","password1");
      if(getStatus1){
      	reporter.ReporterLog("password", LogStatus.PASS);
        }
      
     boolean getStatus2 =WB.VerifyElementPresentAndClick(XP_LogIN, "Login");
      if(getStatus2){
        	reporter.ReporterLog("click login button", LogStatus.PASS);
        }
      /*
	boolean Verify_HomepageLogo = WB.VerifyElementPresent(XP_HomepageLogo, "HomePage Logo", Terminate);
	if(Verify_HomepageLogo){
    	reporter.ReporterLog("TalkTalk logo is displayed", LogStatus.PASS);
    }
    */
      Thread.sleep(5000);
	}
	
	public void WCLogout() throws Exception
	{
		String XP_Logo="//div[@class='nav-bar-block' and @nav-bar='active']//*[contains(@class,'logo')]";
		String XP_Menu="//div[@class='nav-bar-block' and @nav-bar='active']//button[contains(@class,'hamburger')]";
		String XP_Logout="//*[text()='Log Out']";
		
/*********************** Verify Texts  **************************/
		
		SwitchToWebView();
		
		WB.VerifyElementPresent(XP_Logo, "Logo",false); 
		WB.VerifyElementPresentAndClick(XP_Menu, "Menu");
		WB.VerifyElementPresentAndClick(XP_Logout, "Log Out");
	}
	
	public void SwitchToWebView()
	{
		System.out.println("Entering into webview functions");
//		String[] Views={"WEBVIEW","NATIVE_VIEW"};
		Set<String> contextNames = driver.getContextHandles();
        for (String contextName : contextNames) {
//            System.out.println("Context name"+contextName);
            if (contextName.contains("WEBVIEW")){
           
               driver.context(contextName);
               System.out.println("Changed to webview");
              // System.out.println("driver status "+driver);
            }
         } 
	}
	
	public void SwitchToNativeView() throws InterruptedException
	{
		int i=0;
		while(i<500)
		{
		Set<String> contextNames = driver.getContextHandles();
        for (String contextName : contextNames) {
//            System.out.println("Context name"+contextName);
            if (contextName.contains("NATIVE_APP")){
            	i=500;
            System.out.println("Changed to Native View");
               driver.context(contextName);
            }
         }
		}
	}

}