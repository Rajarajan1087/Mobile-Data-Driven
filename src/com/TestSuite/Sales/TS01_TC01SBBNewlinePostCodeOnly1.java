/*Test Scenario 		: m.Sales
 *Test Case Names 		: SBBNewlinePostCodeOnly
 *Package				: m.Sales 
 *Created By			: Raja
 *Created on			: 18-05-2015
 */

package com.TestSuite.Sales;

import io.appium.java_client.AppiumDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.Engine.AppiumSetup;
import com.Engine.Reporter;
import com.SharedModules.Constants;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TS01_TC01SBBNewlinePostCodeOnly1 extends AppiumSetup implements Constants{

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test()
	public void TC01SBBNewlinePostCodeOnly() throws Exception {
		/************************** Objects Declaration **********************************/
		
			Object[] Data=getRuntimeData(Extent);
			Extent=(ExtentReports) Data[1];
			AppiumDriver driver=(AppiumDriver) Data[0];
			ExtentTest test=(ExtentTest) Data[2];
		
		/************************** Start of Main Test **********************************/
		
			test.log(LogStatus.PASS, "Test Case Passed");
			
		/************************** End of Main Test ***********************************/
			
			clearRuntimeData(Data);
			
	}
	@AfterTest()
	public void tearDown() throws Exception {
		Extent.flush();
	}
}