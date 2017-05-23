
package com.TestSuite.MobileWelcomeCentre;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;

import io.appium.java_client.AppiumDriver;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.AssertJUnit;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.Engine.AppiumSetup;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Screens.CommonScreens.AIP_WelcomeCenter;
import com.SharedModules.Constants;
import com.WelcomeCentre.BusinessModules.ChangeAppointment;
import com.WelcomeCentre.BusinessModules.MyOrderChecks;
import com.WelcomeCentre.BusinessModules.MyPhone_BBGoLiveModule;
import com.WelcomeCentre.BusinessModules.MyRouterDelivery;
import com.WelcomeCentre.BusinessModules.WelcomePageChecks;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class TC38_AIP_NL_291_TVLITE_MI_Complete extends AppiumSetup implements Constants{

	/**Inputsheet Variable**/
	String InputSheet;
	public ExtentReports Extent;
	
	/** Test Pre Processing	Q
	 * @param method
	 * @throws Exception
	 */
	@BeforeTest()
	public void myBeforeTestMethod(){
		try {
			Extent=InitializeExtentReports(this.getClass().getSimpleName(),Extent);
			DeleteDeviceId( );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test(threadPoolSize = 1, invocationCount=1)
	public void TC01SBBNewlinePostCodeOnly() throws Exception {
		
		/************************** Objects Declaration **********************************/
		String CLI="", Password="password1",CustDet=null,PackName="Fast Broadband";
		
		Object[] Data=getRuntimeData(Extent);
		AppiumDriver driver=(AppiumDriver) Data[0];
		Extent=(ExtentReports) Data[1];
		ExtentTest test=(ExtentTest) Data[2];
		
	
		/************************** Start of Main Test **********************************/
		AIP_WelcomeCenter WC=new AIP_WelcomeCenter(driver, test);
		MyOrderChecks MyOrders=new MyOrderChecks(driver, test);
		WelcomePageChecks WCCheck=new WelcomePageChecks(driver, test);
		ChangeAppointment CA=new ChangeAppointment(driver, test);
		MyRouterDelivery MYrouter=new MyRouterDelivery(driver, test);
		MyPhone_BBGoLiveModule MyPhone_BB =new MyPhone_BBGoLiveModule (driver, test);
		
		WC.loginIntoMyAccount_CSA(CLI,Password);
		
		WCCheck.CheckFirstName(CustDet.split(",")[1]);
		WCCheck.BB_AfterGoliveDate();
		
		WCCheck.CheckFirstName(CustDet.split(",")[1]);
		WCCheck.AppointmentConfirmationText();
		
		MyOrders.ModuleName("Fast Broadband","PLUS");
		MyOrders.PackageName("Fast Broadband","PLUS");	
		MyOrders.FullPackDetailsButon();
		MyOrders.FullPackDetails("Fast Broadband","PLUS");
	
		
		WC.WCLogout();
		
		/************************** End of Main Test ***********************************/
			
			clearRuntimeData(Data);
			
	}
	
	@AfterTest()
	public void tearDown() throws Exception {
		Extent.flush();
	}
}