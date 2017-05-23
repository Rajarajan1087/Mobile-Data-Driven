/*Test Scenario 		: m.Sales
 *Test Case Names 		: SBBNewlinePostCodeOnly
 *Package				: m.Sales 
 *Created By			: Raja
 *Created on			: 18-05-2015
 */

package com.TestSuite.Sales;

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
import org.testng.ITestContext;
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
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class TS01_TC01SBBNewlinePostCodeOnly extends AppiumSetup implements Constants{

	/**Inputsheet Variable**/
	String InputSheet;
	public ExtentReports Extent;
	//Reporter Report=new Reporter();
	
	/** Test Pre Processing	Q
	 * @param method
	 * @throws Exception
	 */
	@BeforeTest()
	public void myBeforeTestMethod(){
		try {
			Extent=InitializeExtentReports(this.getClass().getSimpleName(),Extent);
			//ExtentTestManager.startTest(this.getClass().getSimpleName(), "This is a simple test.");
			DeleteDeviceId( );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test(threadPoolSize = 1, invocationCount=1)
	public void TC01SBBNewlinePostCodeOnly(ITestContext ctx) throws Exception {
		/************************** Objects Declaration **********************************/
		System.out.println("Entering into testcase");
		Object[] Data=getRuntimeData(Extent);
		AppiumDriver driver=(AppiumDriver) Data[0];
		Extent=(ExtentReports) Data[1];
		ExtentTest test=(ExtentTest) Data[2];
		String DeviceId =(String) Data[3];
		String ChromePort=(String) Data[4];
		String NodePort =(String) Data[5];
		System.out.println("getting node Port "+NodePort);
		
		ctx.getCurrentXmlTest().addParameter(NodePort, DeviceId);
	
		/************************** Start of Main Test **********************************/
		AIP_WelcomeCenter WC=new AIP_WelcomeCenter(driver, test);
		WC.loginIntoMyAccount_CSA("","");
		//WC.WCNewLogin("");
		/************************** End of Main Test ***********************************/
			
			clearRuntimeData(Data);
			System.out.println("Exit test case");
			
	}
	
	@AfterMethod()
	public void myAfterTestMethod(ITestContext ctx){
		// System.out.println(ctx.getCurrentXmlTest().getParameter(DeviceId));
		//clearRuntimeData(Data);
	}
	
	@AfterTest()
	public void tearDown() throws Exception {
		
		//DeleteDeviceId( );
		
		Extent.flush();
		System.out.println("After test executed");
	}
}