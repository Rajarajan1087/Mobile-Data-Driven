package com.WelcomeCentre.BusinessModules;

import io.appium.java_client.AppiumDriver;

import org.openqa.selenium.WebDriver;

import com.DataFeeder.XP_WelcomeCentre;
import com.Engine.Reporter;
import com.WebActions.New_WebActions;
import com.WebActions.WebActions;
import com.relevantcodes.extentreports.ExtentTest;

public class ChangeAppointment implements XP_WelcomeCentre{

	public ExtentTest test;
	public AppiumDriver driver;
	New_WebActions WB = null;
	Reporter reporter = null;
	
	public ChangeAppointment(AppiumDriver Driver, ExtentTest Test) 
	{
		driver=Driver;
		test=Test;
		WB = new New_WebActions (driver,test);
		reporter = new Reporter(driver, test);
	}
	/** Description 		: 	To check the Module Name of the MYORDER module 
	 * 	Coded by 			:	Sneha
	 * 	Modified By 		:	
	 * 	Created Data		:	25 Aug 2016
	 *	Parameters			: 	Package name ,TVBOx type
	 * 	Return value		: 	void
	 * 	Last Modified Date	:	17 Aug 2016
	 */
	public void ModuleName() throws Exception
	{
		
				if(WB.VerifyElementPresent(XP_ModName_EngApp,"My Engineer Appointment text"))
				{
					reporter.fnReportPass("My Engineer Appointment text is found");
				}
			
	}
	
	public void RescheduleButton() throws Exception
	{		
		WB.VerifyElementPresentAndClick(XP_App_Reshedule, "Reshedule Buton");
							
	}
	
	public void RescheduleDate() throws Exception
	{		
				
				if(WB.VerifyElementPresent(XP_date_Text, "Choose a date for your engineer visit"))
				{
					reporter.fnReportPass("Choose a date for your engineer visit");
				}
				WB.VerifyElementPresentAndClick(XP_date_Sel, "Resheduled date "+WB.VerifyElementPresentAndGetText("XP_date_Sel", "Date"));
				
				if(WB.VerifyElementPresent(XP_Slot_Text, "Choose a date for your engineer visit"))
				{
					reporter.fnReportPass("Choose a slot for your engineer visit is found");
				}			
				WB.VerifyElementPresentAndSelect(XP_Slot_selector, "Slot Selector", "6pm-8pm");
				WB.VerifyElementPresentAndClick(XP_ConfirmChanges, "Confirm Changes");
				
				
	}
	
	public void ConfirmationCheck() throws Exception
	{		
				
				if(WB.VerifyElementPresent(XP_ConfirmPage, "Choose a date for your engineer visit"))
				{
					reporter.fnReportPass("Confirmation popup");
				}
				String date= WB.VerifyElementPresentAndGetText(XP_ConfirmDate, "Resheduled Date ")	;
				date=date+" "+WB.VerifyElementPresentAndGetText(XP_ConfirmSlot, "Resheduled Slot ");
				reporter.fnReportPass("Resheduled date and time "+ date, driver);
				System.out.println("Resheduled date and time "+ date);
				WB.VerifyElementPresentAndClick(XP_Refresh, "Refresh Button");
					
				
	}
	
	public void WelcmeMessageCheck() throws Exception
	{		
				
				if(WB.VerifyElementPresent(XP_ConfirmPage, "Choose a date for your engineer visit"))
				{
					reporter.fnReportPass("Confirmation popup");
				}
				
					
				
	}
	
	
}