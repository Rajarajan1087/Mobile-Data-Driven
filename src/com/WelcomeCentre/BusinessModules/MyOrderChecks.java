package com.WelcomeCentre.BusinessModules;

import io.appium.java_client.AppiumDriver;

import org.openqa.selenium.WebDriver;

import com.DataFeeder.XP_WelcomeCentre;
import com.Engine.Reporter;
import com.WebActions.New_WebActions;
import com.WebActions.WebActions;
import com.relevantcodes.extentreports.ExtentTest;

public class MyOrderChecks implements XP_WelcomeCentre{

	public ExtentTest test;
	public AppiumDriver driver;
	New_WebActions WB = null;
	Reporter reporter = null;
	
	public MyOrderChecks(AppiumDriver Driver, ExtentTest Test) 
	{
		driver=Driver;
		test=Test;
		WB = new New_WebActions (driver,test);
		reporter = new Reporter(driver, test);
	}

	
	public void ModuleName(String Pack,String TVBOX) throws Exception
	{
		if(TVBOX.equals("")||TVBOX.toUpperCase().equals("NOBOX"))
		{
			if(WB.VerifyElementPresent(XP_ModName.replaceAll("M_PACK", Pack), "Module Name contains"+Pack))
			{
				reporter.fnReportPass("Module Name contains"+Pack);
			}
		}
		else
		{
			if(TVBOX.toUpperCase().equals("PLUS"))
			{
				if(WB.VerifyElementPresent(XP_ModName_TV.replaceAll("M_PACK", Pack).replaceAll("M_TVBOX", "TalkTalk TV Plus"), "Module Name contains"+Pack+ " and"+TVBOX))
				{
					reporter.fnReportPass("Module Name contains"+Pack+ " and"+TVBOX);
				}
			}
			else
			{
				if(WB.VerifyElementPresent(XP_ModName_TV.replaceAll("M_PACK", Pack).replaceAll("M_TVBOX", "TalkTalk TV"), "Module Name contains"+Pack+ " and"+TVBOX))
				{
					reporter.fnReportPass("Module Name contains"+Pack+ " and"+TVBOX);
				}
			}
		}
	}
	
	public void PackageName(String Pack,String TVBOX) throws Exception
	{
		if(TVBOX.equals("")||TVBOX.toUpperCase().equals("NOBOX"))
		{
			if(WB.VerifyElementPresent(XP_PackName.replaceAll("M_PACK", Pack), "Package Name contains"+Pack))
			{
				reporter.fnReportPass("Package Name contains"+Pack+" ");
			}
		}
		else
		{
			if(TVBOX.toUpperCase().equals("PLUS"))
			{
				if(WB.VerifyElementPresent(XP_PackName_TV.replaceAll("M_PACK", Pack).replaceAll("M_TVBOX", "TalkTalk TV Plus"), "Package Name contains"+Pack+ " and"+TVBOX))
				{
					reporter.fnReportPass("Package Name contains"+Pack+ " and"+TVBOX);
				}
				if(WB.VerifyElementPresent(XP_SubPackName, "Package Name contains"+Pack))
					{
					reporter.fnReportPass("Package Name contains"+Pack+ " and"+TVBOX);
				}
			}
			else
			{
				if(WB.VerifyElementPresent(XP_PackName_TV.replaceAll("M_PACK", Pack).replaceAll("M_TVBOX", "TalkTalk TV"), "Package Name contains"+Pack+ " and"+TVBOX))
				{
					reporter.fnReportPass("Package Name contains"+Pack+ " and"+TVBOX);
				}
				
			}
		}

	}


	public void FullPackDetails(String Pack,String TVBOX) throws Exception
	{
		
		
		if(TVBOX.equals("")||TVBOX.toUpperCase().equals("NOBOX"))
		{
			if(WB.VerifyElementPresent(XP_SubPackName.replaceAll("M_PACK", Pack), "Package Name in FULL PACKAGE DETAILS POPUP contains"+Pack))
			{
				reporter.fnReportPass("Package Name in FULL PACKAGE DETAILS POPUP  contains"+Pack+" ");
			}
		}
		else
		{
			if(TVBOX.toUpperCase().equals("PLUS"))
			{
				if(WB.VerifyElementPresent(XP_SubPackName_TV.replaceAll("M_PACK", Pack).replaceAll("M_TVBOX", "TalkTalk TV Plus"), "Package Name in FULL PACKAGE DETAILS POPUP contains"+Pack+ " and"+TVBOX))
				{
					reporter.fnReportPass("Package Name in FULL PACKAGE DETAILS POPUP contains"+Pack+ " and"+TVBOX);
				}
				if(WB.VerifyElementPresent(XP_SubPackName_TV, "Package Name in FULL PACKAGE DETAILS POPUP  contains"+Pack))
					{
					reporter.fnReportPass("Package Name in FULL PACKAGE DETAILS POPUP contains"+Pack+ " and"+TVBOX);
				}
			}
			else
			{
				if(WB.VerifyElementPresent(XP_SubPackName_TV.replaceAll("M_PACK", Pack).replaceAll("M_TVBOX", "TalkTalk TV"), "Package Name in FULL PACKAGE DETAILS POPUP contains"+Pack+ " and"+TVBOX))
				{
					reporter.fnReportPass("Package Name in FULL PACKAGE DETAILS POPUP contains"+Pack+ " and"+TVBOX);
				}
				if(WB.VerifyElementPresent(XP_SubPackName, "Package Name contains"+Pack))
				{
					reporter.fnReportPass("Package Name contains"+Pack+ " and"+TVBOX);
			}
			}
		}
	}
	
	
	public void FullPackDetailsButon() throws Exception
	{
		
		WB.VerifyElementPresentAndClick(XP_FPButton, "Full Package Details Button");
	}
}