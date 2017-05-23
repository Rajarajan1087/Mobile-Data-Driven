package com.WelcomeCentre.BusinessModules;

import io.appium.java_client.AppiumDriver;

import org.openqa.selenium.WebDriver;

import com.DataFeeder.XP_WelcomeCentre;
import com.Engine.Reporter;
import com.WebActions.New_WebActions;
import com.WebActions.WebActions;
import com.relevantcodes.extentreports.ExtentTest;

public class WelcomePageChecks  implements XP_WelcomeCentre{
	
	public ExtentTest test;
	public AppiumDriver driver;
	New_WebActions WB = null;
	Reporter reporter = null;
	
	public WelcomePageChecks(AppiumDriver Driver, ExtentTest Test) 
	{
		driver=Driver;
		test=Test;
		WB = new New_WebActions (driver,test);
		reporter = new Reporter(driver, test);
	}
	
	public void CheckFirstName(String FName) throws Exception
	{
		char Fletter=FName.charAt(0);
		String RestName=FName.substring(1).toLowerCase();
		FName=Fletter+RestName;
		System.out.println("Entered CheckFirstName");
		WB.VerifyElementPresent("//*[@id='welcome-message']//*[contains(text(),'Hi "+FName+"')  and contains(@data-ng-show,'!maAuto') ]", "Great News"+FName);
	}
	

	public void AppointmentConfirmationText() throws Exception
	{
		System.out.println("Entered AppointmentConfirmationText");
		WB.VerifyElementPresent(XP_APPconfirmText, "We've got your order and everything is running smoothly Text is present");
	}

	public void GreatNews_FirstName(String FName) throws Exception
	{
		char Fletter=FName.charAt(0);
		String RestName=FName.substring(1).toLowerCase();;
		 String fName=Fletter+RestName;
		 System.out.println(fName);
				 
		 System.out.println("Entered GreatNews_FirstName");
				 
		 WB.VerifyElementPresent(XP_GNews_Fname.replaceAll("M_FName", fName), "Great News"+FName);
			
	}
	
	public void Welcome_FirstName(String FName) throws Exception
	{
		char Fletter=FName.charAt(0);
		String RestName=FName.substring(1).toLowerCase();;
		 String fName=Fletter+RestName;
		 System.out.println(fName);
				 
		 System.out.println("Entered Welcome_Firstname");
				 
		 WB.VerifyElementPresent(XP_Wel_Fname.replaceAll("M_FName", fName), "Welcome "+FName);
			
	}
	

	public void MissedAppointment( ) throws Exception
	{
		
				 
		if( WB.VerifyElementPresent(XP_MissedApp, "Missed Appointment"))
		{
			reporter.fnReportPass("t looks like your appointment was missed. You can reschedule it from the relevant section below. text is found", driver);
		}
			
	}
	
	public void Cancelled_Text(String Package,String TVBOX) throws Exception
	{
		WB.VerifyElementPresent(XP_Wel_Warning, "Welcome Message wraning"); 
		if(TVBOX.equals("")||TVBOX.toUpperCase().equals("NOBOX"))
		{
			WB.VerifyElementPresent(XP_Cancelled.replaceAll("M_PACK",Package), "Cancelled Confirmation text");
		}
		else
		{
			if(TVBOX.toUpperCase().equals("PLUS"))
			{
				WB.VerifyElementPresent(XP_Cancelled_TV.replaceAll("M_PACK",Package).replaceAll("M_TVBOX", "TalkTalk TV Plus"), "Cancelled Confirmation text");
			}
			else
			{
				WB. VerifyElementPresent(XP_Cancelled_TV.replaceAll("M_PACK",Package).replaceAll("M_TVBOX", "TalkTalk TV"), "Cancelled Confirmation text");
				}
		}
		WB. VerifyElementPresent(XP_PlaceOrder, "Place another order");
		 
			
	}
	
	public void GreatNews_CompleteOrder(String TVBox,String PackName) throws Exception
	{
		
		 if(!TVBox.equals(""))
		 {
			 WB. VerifyElementPresent(XP_GNews_Ordercom_TV.replaceAll("M_TV",TVBox).replaceAll("M_PACK", PackName) , "We've got your order and everything is running smoothly Text");
		 }
		 else
		 {
			 WB. VerifyElementPresent(XP_GNews_Ordercom.replace("M_PACK", PackName), " forget to come back to make the most of your TalkTalk");
			 		 }
		
		
	}

	public void GreatNews_BBComplete() throws Exception
	{
		WB. VerifyElementPresent(XP_GNews_Ordercom_TV," Broadband are up and running and your TV is on its way");
			
	}
	
	public void ToggleButton() throws Exception
	{
		WB. VerifyElementPresentAndClick(XP_ToggeIcon, "Toggle Icon");
			
	}
	
	public void UpgradetoTFO() throws Exception
	{
		if(WB.VerifyElementPresent(XP_UpgradeUFO," Broadband are up and running and your TV is on its way"))
		{
			reporter.fnReportPass("Thanks for upgrading to UFO. We've got your order and we are confirming your appointment. Check back later for an update is found", driver);
		}
			
	}
	
	
	public void BB_AfterGoliveDate() throws Exception
	{
		if(WB.VerifyElementPresent(XP_UpgradeUFO," Broadband are up and running and your TV is on its way"))
		{
			reporter.fnReportPass("Thanks for upgrading to UFO. We've got your order and we are confirming your appointment. Check back later for an update is found", driver);
		}
			
	}
	
	
	
	public void Rejected_Text(String Package,String TVBOX,String FName) throws Exception
	{
		char Fletter=FName.charAt(0);
		String RestName=FName.substring(1).toLowerCase();
		String fname=Fletter+RestName;
		System.out.println("Entered CheckFirstName");
		
		if(TVBOX.equals("")||TVBOX.toUpperCase().equals("NOBOX"))
		{
		 if(WB.VerifyElementPresent(XP_Rejected.replaceAll("M_PACK",Package).replaceAll("M_FName", fname), "Rejected Confirmation text"))
		 {
			 reporter.fnReportPass("Rejected Confirmation text is found ");
		 }
		}
		else
		{
			if(TVBOX.toUpperCase().equals("PLUS"))
			{
			 if(WB.VerifyElementPresent(XP_Rejected_TV.replaceAll("M_PACK",Package).replaceAll("M_TVBOX", "TalkTalk TV Plus").replaceAll("M_FName", fname), "Rejected Confirmation text"))
			 {
				 reporter.fnReportPass("Rejected Confirmation text is found ");
			 }
			}
			else
			{
				 if(WB.VerifyElementPresent(XP_Rejected_TV.replaceAll("M_PACK",Package).replaceAll("M_TVBOX", "TalkTalk TV").replaceAll("M_FName", fname), "Rejected Confirmation text"))
				 {
					 reporter.fnReportPass("Rejected Confirmation text is found ");
				 }
				}
		}
		
			
	}
}
