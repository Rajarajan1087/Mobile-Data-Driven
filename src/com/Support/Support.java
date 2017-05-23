package com.Support;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.Engine.AppiumSetup;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.SharedModules.Constants;
import com.SharedModules.FetchALK;
import com.SharedModules.NameGenerator;
import com.Stubs.StubFilePlacing;
import com.Stubs.StubFilePlacing.StubType;
import com.WebActions.New_WebActions;
import com.WebActions.WebActions;
import com.relevantcodes.extentreports.ExtentTest;

public class Support{
	public String Str_CLI;
	public String Str_Account;
	public ExtentTest test;
	public AppiumDriver driver;
	New_WebActions WB = null;
	Reporter reporter = null;
	public static enum windowBanner{
		windowBannerName,windowBannerSubTitle,windowBannerDescription
	}
	public static enum ProcessType{
		Kill,Run,ClearApp,INSTALL,UNINSTALL,none,KILL_PORT,ClearApp_Android,ClearApp_IOS
	}
	public Support(AppiumDriver Driver, ExtentTest Test) {
		driver = Driver;
		test = Test;
	}
	
	public static String Str_windowBanner_element = "classname=windowBanner";
	public void SwitchToWebView()
	{
//		String[] Views={"WEBVIEW","NATIVE_VIEW"};
		Set<String> contextNames = driver.getContextHandles();
        for (String contextName : contextNames) {
//            System.out.println("Context name"+contextName);
            if (contextName.contains("WEBVIEW")){
            System.out.println("Changed to webview");
               driver.context(contextName);
            }
         } 
	}
	
	public void Swipe(String StartElement,String MoveElement) throws Exception
	{
		SwitchToNativeView();
		Dimension d=driver.manage().window().getSize();
		System.out.println(d);
		float X=d.getHeight()/2;
		float Y=d.getWidth();
		System.out.println(X);
		System.out.println(Y);
		AppiumSetup AS=new AppiumSetup();
		AS.ExecuteCommand("", ProcessType.Kill, "adb shell input swipe "+X+" "+0.25*Y+" "+X+" "+0.75*Y);
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
	

	public String get_custProfile_frameDetails(windowBanner windowchoice) throws Exception{
		String Str_windowBannerValue;
		switch(windowchoice){
		case windowBannerDescription:
			Str_windowBannerValue=WB.VerifyElementPresentAndGetAttribute(Str_windowBanner_element, "windowDescription");
			break;
		case windowBannerName:
			Str_windowBannerValue=WB.VerifyElementPresentAndGetAttribute(Str_windowBanner_element, "windowName");
			break;
		case windowBannerSubTitle:
			Str_windowBannerValue=WB.VerifyElementPresentAndGetAttribute(Str_windowBanner_element, "windowSubTitle");
			break;
		default:
			Str_windowBannerValue="";
			break;
		}
		return Str_windowBannerValue;
	}
}
