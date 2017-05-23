package com.WelcomeCentre.BusinessModules;

import io.appium.java_client.AppiumDriver;

import org.openqa.selenium.WebDriver;

import com.DataFeeder.XP_WelcomeCentre;
import com.Engine.Reporter;
import com.WebActions.New_WebActions;
import com.WebActions.WebActions;
import com.relevantcodes.extentreports.ExtentTest;

public class MyRouterDelivery  implements XP_WelcomeCentre{

	public ExtentTest test;
	public AppiumDriver driver;
	New_WebActions WB = null;
	Reporter reporter = null;
	
	public MyRouterDelivery(AppiumDriver Driver, ExtentTest Test) 
	{
		driver=Driver;
		test=Test;
		WB = new New_WebActions (driver,test);
		reporter = new Reporter(driver, test);
	}


	public void RouterDelivery() throws Exception
	{
		if(WB.VerifyElementPresent(XP_RouDel, "Router Delivery Heading"))
		{
			reporter.fnReportPass("Router Delivery Heading id found");
		}
		
		if(WB.VerifyElementPresent(XP_ArriDueText, "This is due to arrive by "))
		{
			reporter.fnReportPass("This is due to arrive by ");
		}
	}
	
	
	public String getDate() throws Exception
	{
		String day=null;
		
		day=WB.VerifyElementPresentAndGetText(XP_getDay, "Go live day");
		day=day+","+WB.VerifyElementPresentAndGetText(XP_getMonth, "Go live day");
		System.out.println("GO live Date in Router Module is "+day);
		
		return day;
	}

	
	public void Check_GetDeliverySup_Hyperlink() throws Exception
	{
			
			if(WB.VerifyElementPresent(XP_getDeliveryHPLink, "GetDelivery Support Hyperlink"))
			{
				reporter.fnReportPass("GetDelivery Support  Hyperlink");
			}
		
	}
	
	
	public void Check_Later_Text() throws Exception
	{
			
			if(WB.VerifyElementPresent(XP_checklatertext, "It's too soon to send your router just yet. Check back later for tracking details"))
			{
			reporter.fnReportPass("t's too soon to send your router just yet. Check back later for tracking details is found");
			}
		
	}
}