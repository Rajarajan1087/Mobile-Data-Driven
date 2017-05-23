package com.Engine;
import io.appium.java_client.AppiumDriver;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;

import com.Utils.Reusables;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Reporter {

	public String filepath;
	public String OutputFilename;
	public boolean result = false;
	public String strOutputFilePath;
	public String strOutputScreenShotsFilePath;
	public String filename;
	public int rownumber;
	public boolean testPassed = true;
	public String testtype;
	public String testname;
	public String methodname;
	public String[][] TestingResults;
	public static String absolutepath = System.getProperty("user.dir");
	public String CurrentRowOfExecution="";
	public String StartTime="";
	public String EndTime="";
	public String r="";
	public String TerminateReason="";
	public ExtentReports Extent;
	public AppiumDriver driver;

	public ExtentTest test;
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar cal = Calendar.getInstance();
	Date d = cal.getTime();
	public String ExtentReportPath;
	public String Tempdata;
	
	public Reporter(AppiumDriver Driver, ExtentTest Test) {
		driver = Driver;
		test = Test;
		
	}
	

	public void fnReportPass(String strPassingItem,WebDriver...driver){
		ReporterLog(strPassingItem, LogStatus.PASS,driver);
		System.out.println(strPassingItem);
	} 

	// Function to report Fail
	public void fnReportWarning(String strPassingItem,WebDriver...driver) throws Exception {
		ReporterLog(strPassingItem, LogStatus.WARNING,driver);
	}
	public void fnReportFail(String strPassingItem,WebDriver...driver) throws Exception{
		result = false;
		testPassed = false;
		ReporterLog(Reusables.getdateFormat("dd/MM/yyyy hh:mm:ss :", 0) + strPassingItem, LogStatus.FAIL,driver);
		System.out.println(strPassingItem);
	}

	// Function to report Fail and Terminate Test

	public void fnReportFailAndTerminateTest(String strFailingItem, String strReportMessage,WebDriver...driver) throws Exception{
		result = false;
		testPassed = false;
		ReporterLog(Reusables.getdateFormat("dd/MM/yyyy hh:mm:ss :", 0) + strReportMessage, LogStatus.FAIL,driver);
		TerminateReason = strReportMessage;
		exception(methodname + "_" + rownumber, strFailingItem);
	}

	public void exception(String testname1, String objname){
		EndTime = sdf1.format(d);
		ReporterLog("Test Failed", LogStatus.FAIL);
		System.out.println("-------[ Test hits a user defined exception ]-------");
		Assert.fail(testname1 + " script failed, " + objname + " - " + testname1+ ".jpg for more details.");
	}



	// Takes a new screenshot

	public void takescreenshot(WebDriver driver, String Str_ReportMessage,LogStatus LS){
		String photographWithPath = ((TakesScreenshot) driver)

				.getScreenshotAs(OutputType.BASE64);

		//test.log(LS, Str_ReportMessage+test.addBase64ScreenShot("data:image/jpg;base64,"+photographWithPath));


	}

	// reports each step as pass pass or fail to console and to an array
	public void ReporterLog(String Reprot_Text, LogStatus LS,WebDriver... driver){
		System.out.println("Logging....."+LS.name()+"..."+Reprot_Text);
		if(driver.length>0)
		{
			takescreenshot(driver[0], Reprot_Text, LS);
		}
		else{
			test.log(LS, Reprot_Text);
		}
	}

	public void reportTest() throws Exception {

		EndTime = sdf1.format(d);
		if (testPassed) {
			ReporterLog("Test Passed",LogStatus.PASS);
		} else {
			try {
				fnReportFailAndTerminateTest("Test Case Failed", "Failed");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void reportTestSkip() throws SkipException {
			throw new SkipException("Skipping the test case");        
	}
}

