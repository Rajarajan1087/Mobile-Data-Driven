package com.WelcomeCentre.BusinessModules;

import io.appium.java_client.AppiumDriver;

import org.openqa.selenium.WebDriver;

import com.DataFeeder.XP_WelcomeCentre;
import com.Engine.Reporter;
import com.WebActions.New_WebActions;
import com.WebActions.WebActions;
import com.relevantcodes.extentreports.ExtentTest;

public class MyPhone_BBGoLiveModule  implements XP_WelcomeCentre{

	public ExtentTest test;
	public AppiumDriver driver;
	New_WebActions WB = null;
	Reporter reporter = null;
	
	public MyPhone_BBGoLiveModule(AppiumDriver Driver, ExtentTest Test) 
	{
		driver=Driver;
		test=Test;
		WB = new New_WebActions (driver,test);
		reporter = new Reporter(driver, test);
	}

	public void MyPhone_BBGoLiveModuleName() throws Exception
	{
		if(WB.VerifyElementPresent(XP_PHONE_BB, "Router Delivery Heading"))
		{
			reporter.fnReportPass("Router Delivery Heading id found");
		}
		
		
	}
	
	public void checkDate(String date) throws Exception
	{
		
		if(WB.VerifyElementPresent(XP_CheckDay.replaceAll("M_DAY", date.split(",")[0]), "Router Delivery Heading"))
		{
			reporter.fnReportPass(date.split(",")[0]+" is present in Phone_BB module");
		}
		
		if(WB.VerifyElementPresent(XP_CheckMonth.replaceAll("M_DAY", date.split(",")[1]), "Router Delivery Heading"))
		{
			reporter.fnReportPass(date.split(",")[1]+" is present in Phone_BB module");
		}
		
	}

	
	public void Check_GetDeliverySup_Hyperlink() throws Exception
	{
			
			if(WB.VerifyElementPresent(XP_getSupportHPLink, "Get Phone and Broadband Support"))
			{
				reporter.fnReportPass("GetDelivery Support  Hyperlink");
			}
		
	}
	
	
}