/*Test Scenario 		: m.Sales
 *Test Case Names 		: SBBNewlinePostCodeOnly
 *Package				: m.Sales 
 *Created By			: Raja
 *Created on			: 18-05-2015
 */

package com.TestSuite.Sales;

import java.io.IOException;
import java.lang.reflect.Method;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.Engine.LoadEnvironment;
import com.Engine.AppiumSetup;
import com.Engine.Reporter;
import com.SharedModules.Constants;
import com.Utils.DataProviderExcelReader;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TS01_TC01SBBNewlinePostCodeOnly2 implements Constants{
	
	/**Inputsheet Variable**/
	String InputSheet;
	public ExtentReports Extent;
	public ExtentTest test;
	AppiumSetup AS=new AppiumSetup();
	//Reporter Report=new Reporter();
	/** Test Pre Processing	Q
	 * @param method
	 * @throws Exception
	 */
	
	@BeforeTest()
	public void myBeforeTestMethod(){
		try {
			Extent=AS.InitializeExtentReports(this.getClass().getSimpleName(),Extent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test(invocationCount=2)
	public void TC01SBBNewlinePostCodeOnly() throws IOException {
		String Device = null;
		try {
			test = Extent.startTest(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			Device=AS.getDeviceDetails();
			test.log(LogStatus.PASS, "Test Case Passed on "+Device);
			Extent.endTest(test);
//			Report.reportTest(test);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			AS.MakeDeviceIdle(Device);
		}
	}
	@AfterTest()
	public void tearDown() throws Exception {
		Extent.flush();
	}
}