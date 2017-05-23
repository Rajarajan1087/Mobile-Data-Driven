package com.Engine;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.Config.Config;
import com.SharedModules.NameGenerator;
import com.Support.Support;
import com.Support.Support.ProcessType;
import com.Utils.ReadExcelSheet;
import com.WebActions.WebActions;
import com.google.common.collect.Multiset.Entry;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

@SuppressWarnings("deprecation")
public class AppiumSetup extends LoadEnvironment{

	// Common variables
	public static String deviceID ="";
	public static String IOSWebKitDebugProxy ="";
	public String Tempdata;
	public static String IEversion = "8";
	public static String WhereToRun = "local";// grid ; local
	public String[] Addresskey;
	public static String testENV;
	public static String RunType;
	public static String MA_ONLINE;
	public static String platform;
	public Selenium selenium;
	//public AppiumDriver driver ;
	public WebDriver driver1;
	public static Reporter Report ;
	public String Str_PostCode;
	public static String AddressKey="";
	public String Password;
	public static boolean booleanFAQ;
	public static String PlatformVersion;
	public static String DeviceName;
	public String strOutputFilePath;
	public String filename;
	public static String CanStart="No";
	public static final int empty_int = 999;
	public static String DeviceType;
	public int Udid_Count =40;
	
	static AppiumDriverLocalService service;
	

	public enum Browser {
		FIREFOX, GOOGLECHROME, IE, SAFARI, PHANTOM, FIREFOX_PROXY, APPIUMDRIVER, FIREFOX1, FIREFOX_1
	}	
	public static boolean testPassed = true;

	@Rule
	public ErrorCollector errorCollector = new ErrorCollector();
	//NameGenerator nameGenerator = new NameGenerator();

	/*---------------------------Main Components ------------------------------------------------------------*/

	public static String sendGet(String Url) throws Exception {
		
		URL obj = new URL(Url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		System.setProperty("http.keepAlive", "false");
		con.setRequestProperty("Connection", "close");
		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + Url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		//		System.out.println(response.toString());
		return response.toString();
	}
	
	public Object[] getRuntimeData(ExtentReports Extent) throws Exception
	{
		ExtentTest test=null;String Device=null,ChromePort =null,NodePort=null;AppiumDriver driver=null;
		//test = Extent.startTest(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		
		Object[] DeviceDetails=CreateDeviceDaemon();
		driver=(AppiumDriver) DeviceDetails[0];
		Device=(String) DeviceDetails[1];
		ChromePort=(String) DeviceDetails[2];
		NodePort= (String) DeviceDetails[3];
		test = Extent.startTest("Test Name", "Sample description");
		Object[] returnData={driver,Extent,test,Device,ChromePort,NodePort};
		
		return returnData;
	}
	
	
	
	@BeforeSuite(alwaysRun = true)
	public void setupBeforeSuite(ITestContext context) throws Exception {
		System.out.println("<------------------------ Loading Env variables ------------------------>");
		testENV = context.getCurrentXmlTest().getParameter("test.env").toLowerCase();
		MA_ONLINE = "auto";
		WhereToRun = context.getCurrentXmlTest().getParameter("whereToRun").toLowerCase();
		RunType = context.getCurrentXmlTest().getParameter("RunType").toLowerCase();

		System.out.println("DownStreamENV:'"+testENV+"'");
		System.out.println("UpStreamENV:'"+MA_ONLINE+"'");
		System.out.println("Mode of Execution:'"+WhereToRun+"'");
		if(LoadEnvironment.workingDir.contains("/"))
		{
			LoadEnvironment.Slash="/";
		}
		LoadGenericSystemProperties();
		LoadDownStreamENV("env_"+testENV);
		LoadUpStreamEnvironment("MA_ONLINE_"+MA_ONLINE);
		LoadScreenProperties();
		LoadXpathProperties();
		loadSystemProperties();
		Assert.assertTrue(!testENV.isEmpty());
		try{
			/*ExecuteCommand("chromedriver", ProcessType.Kill);
			ExecuteCommand("com.android.chrome", ProcessType.ClearApp);
			ExecuteCommand(mPackageName, ProcessType.ClearApp);
			ExecuteCommand("node", ProcessType.Kill);*/
		}
		catch(Exception E)
		{
			E.printStackTrace();
			System.exit(0);
		}
	}
	// -- This method is invoked at the end of each test case.
	public ExtentReports InitializeExtentReports(String FileName,ExtentReports Extent) throws Exception{
		String ExtentReportPath=System.getProperty("user.dir") + "/Outputfiles/"+ FileName;
		DateFormat df = new SimpleDateFormat("yyyyMMMdd_HHmmssSSS");
		Date dateobj = new Date();
		String D=df.format(dateobj);
		return Extent = new ExtentReports(ExtentReportPath+"_"+D+".html", false);
	}
	
	public void RandomGeneratorSleepTime() throws Exception{
		int minimum =1, maximum =15,sleep=1000;
		Random rand = new Random();
		int randomNum = minimum + rand.nextInt((maximum - minimum) + 1);
		 sleep = sleep * randomNum;
		
		 System.out.println("Random generator no "+sleep);
		 Thread.sleep(sleep);
	}
	
	public String getiOSDeviceDetails() throws Exception{
		
		System.out.println("Enetered in to IOS device");
		String deviceName					=	getDeviceProperties("xcrun instruments -s devices");
		ArrayList<String> splitDeviceName	=	new ArrayList<String>();
		String[] splitDeviceList			=	deviceName.split(",");
		String serialNo = "";
		
		for(int i=0;i<splitDeviceList.length;i++){
			//System.out.println(splitDeviceList[i]);
			if(!splitDeviceList[i].contains("-") && !splitDeviceList[i].contains("Known Devices") && (splitDeviceList[i].contains("["))){
				splitDeviceName.add(splitDeviceList[i]);
				
			}
			
		}

		for(Iterator itr = splitDeviceName.iterator(); itr.hasNext();){
			
			Object element	=	itr.next();
			String elementValue	=	(String) element;
			String[] splitDeviceDetails	=	elementValue.split(" ");
			
			String lastWord = splitDeviceDetails[splitDeviceDetails.length - 1];
			serialNo =serialNo+(lastWord.substring(1, lastWord.length()-1)+", ").trim();
			System.out.println("UDID's of IOS device"+serialNo);
			
		}
		
		return serialNo;
		
	}
	
	public String getAndroidDeviceDetails() throws Exception{
		String getAndroidSerialNo="";
		while(getAndroidSerialNo.equals(""))
		{
			//System.out.println("Execute commands in terminal "+ExecuteCommand("", ProcessType.none, "adb devices").replaceAll("List of devices attached ,", "").trim().replaceAll("\\s+", " ").trim());
			String[] DeviceList=ExecuteCommand("", ProcessType.none, "adb devices").replaceAll("List of devices attached ,", "").trim().replaceAll("\\s+", " ").trim().split(",");
		
			if(!DeviceList[0].isEmpty()){
			for(String Device:DeviceList)
			{
				String[] DeviceDetails=Device.split(" ");
				if(DeviceDetails[1].contains("device"))
				{
					getAndroidSerialNo=getAndroidSerialNo+DeviceDetails[0].trim()+",";
					
				}
			}
			
		}else{
			System.out.println("Android device is not connected");
			break;
		}
		}
		System.out.println("Get android device lists "+getAndroidSerialNo);
		return getAndroidSerialNo;
	}
	
	public void getPIDDetails(String Port) throws Exception{
		
		int portNumber = Integer.parseInt(Port);
		System.out.println("Node delete using port no "+portNumber);
		String getPortNumber = "lsof -ti:"+portNumber+ " | xargs kill";
		System.out.println("PortNumber using commands "+getPortNumber);
		
		//AppiumServiceBuilder appiumServiceBuilder = new AppiumServiceBuilder();
		
	
/*
		 service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
						.usingDriverExecutable(new File(LoadEnvironment.NodeLocation))
						.withAppiumJS(new File(LoadEnvironment.AppiumLocation))
						.withIPAddress("127.0.0.1").usingPort(portNumber));
		
		service.stop();
*/
		/*
		//String NodePortNo=ExecuteCommand("", ProcessType.none, getPortNumber);
		Process process = Runtime.getRuntime().exec(getPortNumber);
		 process.wait();
		
			System.out.println("the output stream is "+process);
	        BufferedReader reader=new BufferedReader( new InputStreamReader(process.getInputStream()));
	        String s; 
	        while ((s = reader.readLine()) != null){
	           System.out.println("The input stream is " + s);
			
	        }
	       reader.close();
	       
	       ProcessBuilder pb = new ProcessBuilder(getPortNumber);
	       Process p = pb.start();
	       int exitCode = p.waitFor();
	       
		
		BufferedInputStream bis = new BufferedInputStream(process.getInputStream()); 
		StringBuffer sb = new StringBuffer();
		int c;
		while((c=bis.read()) != -1) { 
		sb.append(c);
		} 
		bis.close();
		System.out.println("end process");
		*/
	}
	
	
	public String getDeviceDetails() throws Exception
	{	
		//String getName = nameGenerator.randomName(7);
		//System.out.println("Name Generator "+getName);
		RandomGeneratorSleepTime();
		String SerialNo="";
		String DeviceList = getAndroidDeviceDetails().concat(getiOSDeviceDetails());
		System.out.println("Concatenate the devices "+DeviceList);
		String splitDeviceList[] =DeviceList.trim().replaceAll("\\s+", " ").trim().split(",");
		
		for(String Device:splitDeviceList)
		{
			System.out.println("Getting all devices id's length"+Device.length());
			System.out.println("Getting device id's "+Device);
		}
		
		for(String Device:splitDeviceList)
		{
			if(!readWorkingDevice().contains(Device))
			{
				SerialNo=Device.trim();
				break;
			}
		}
		UpdateWorkingDevice(readWorkingDevice()+SerialNo);
		return SerialNo;
		
		/*
		 RandomGeneratorSleepTime();
		String SerialNo="";
		while(SerialNo.equals(""))
		{
			String[] DeviceList=(ExecuteCommand("", ProcessType.none, "adb devices").replaceAll("List of devices attached ,", "").trim().replaceAll("\\s+", " ").trim()+getiOSDeviceDetails()).split(",");
			
			
			for(String Device:DeviceList)
			{
				
				String[] DeviceDetails=Device.split(" ");
				if(DeviceDetails[1].contains("device")&&!readWorkingDevice().contains(DeviceDetails[0].trim()))
				{
					SerialNo=DeviceDetails[0].trim();
					break;
				}
			}
		}
		UpdateWorkingDevice(readWorkingDevice()+SerialNo);
		return SerialNo;
		*/
	}
	public String readWorkingDevice() throws IOException
	{
		File f=new File(System.getProperty("user.dir")+"/PropertyFiles/DeviceProperties/DeviceStatus.properties");
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line = "", template="";
		while((line = reader.readLine()) != null)
			template += line + "\r\n";
		reader.close();
		return template;
	}
	public void UpdateWorkingDevice(String String_to_write) throws IOException
	{
		FileWriter writer = new FileWriter(System.getProperty("user.dir")+"/PropertyFiles/DeviceProperties/DeviceStatus.properties");
		writer.write(String_to_write);writer.flush();writer.close();
	}
	
	public void MakeDeviceIdle(String Device_Serial_Number) throws IOException
	{
		UpdateWorkingDevice(readWorkingDevice().replaceAll(Device_Serial_Number, ""));
	}
	
	public void DeleteDeviceId( ) throws IOException{
		
		FileOutputStream writer = new FileOutputStream(System.getProperty("user.dir")+"/PropertyFiles/DeviceProperties/DeviceStatus.properties");
		writer.write(("").getBytes());
		writer.close();
	}
	
	public Object[] CreateDeviceDaemon() throws Exception
	{
		String SerialNumber=getDeviceDetails();
		return WhereToExecute(SerialNumber);
	}
	
	public static String ExecuteCommand(String ProcessName,ProcessType Pro,String... Commands) throws Exception{
	
		String line=new String();
		if(Commands.length==0)
		{
			try {
				Commands=new String[1];
				switch (Pro.name().toUpperCase())
				{
				case "KILL_PORT":
					if(SYSTEM_OS_NAME.toUpperCase().contains("WINDOWS")){
						//Commands[0]="taskkill /f /im "+ProcessName+".exe";
						break;
					}else{
						Commands[0]="lsof -P | grep ':"+ProcessName+"' | awk '{print $2}' | xargs kill -9";
						System.out.println("chromeport running "+Commands[0]);
						break;
					}
					
				case "KILL":
					if(SYSTEM_OS_NAME.toUpperCase().contains("WINDOWS")){
						Commands[0]="taskkill /f /im "+ProcessName+".exe";
						break;
					}else{
						Commands[0]="pkill -9 "+ProcessName;
						
						break;
					}
				case "RUN":
					if(SYSTEM_OS_NAME.toUpperCase().contains("WINDOWS")){
						Commands[0]="start "+ProcessName;
						break;
					}else{
						Commands[0]="open -a "+ProcessName;
						break;
					}
				case "CLEARAPP":
					
					if(DeviceType.equalsIgnoreCase("android"))
					{
						Commands[0]="adb shell pm clear "+ProcessName;
						break;
					}
					else
					{
						Commands[0]="xcrun simctl erase "+ProcessName;
						break;
					}
					
					
				case "ClearApp_Android":
					Commands[0]="adb shell pm clear "+ProcessName;
					break;
					
				case "ClearApp_IOS":
					Commands[0]="xcrun simctl erase "+ProcessName;
					break;
				
				case "INSTALL":
					if(DeviceType.equalsIgnoreCase("android"))
					{
						Commands[0]="adb install -r "+ProcessName;
					}
					else
					{
						Commands[0]="xcrun simctl install booted "+ProcessName;
					}
				case "UNINSTALL":
					Commands[0]="adb uninstall "+ProcessName;
				}
			} catch (Exception e) {
				throw e;
			}
		}
		
	
		
		for(String Command:Commands)
		{
			try
			{
				Process process=Runtime.getRuntime().exec(Command);
				process.waitFor();
				BufferedReader br =new BufferedReader(new InputStreamReader(process.getInputStream()));
				line=br.readLine();
				String Temp;
				int i=0;
				while (i==0) {
					Temp=br.readLine();
					if(Temp!=null)
					{
						
						line=line+","+Temp;
						
					}
					else
					{
						i=1;
					}
				}
				
			}
			catch(Exception N)
			{
				System.out.println("Expection "+N);
				//N.printStackTrace();
				throw N;
				//	    		Report.fnReportFailAndTerminateTest("Device Connectivity", "Device Not Connected to the System");
			}
		}
		return line;
	}

	public Object[] WhereToExecute(String SerialNumber) throws Exception {
		deviceID =SerialNumber;
		System.out.println("Serial Number : "+SerialNumber);
		AppiumDriver driver = null;
		
		
		AppiumServer AS=new AppiumServer();
		Thread t = new Thread(AS);
		t.start();
		
		
		iOSWebKit iOSWebkit =new iOSWebKit();
		Thread t1 = new Thread(iOSWebkit);
		//t1.start();
		
		String ChromeDriverPorts=AS.getChromeDriverPort();	
		String DefaultPort=AS.getDefaultPort();
		IOSWebKitDebugProxy =AS.getiOSWebkitDebugProxy();
		System.out.println("node port no "+DefaultPort);
		
		//String PlatformVersion=ExecuteCommand(mPackageName, ProcessType.Kill,"adb shell -s "+SerialNumber+" getprop ro.build.version.release");
		//String DeviceName=ExecuteCommand(mPackageName, ProcessType.Kill,"adb shell -s "+SerialNumber+" getprop ro.product.model");
		
		//Code for MAC to get the device details
		//String PlatformVersion=ExecuteCommand(mPackageName, ProcessType.Kill,"adb -s "+SerialNumber+" shell getprop ro.build.version.release");
		//String DeviceName=ExecuteCommand(mPackageName, ProcessType.Kill,"adb -s "+SerialNumber+" shell getprop ro.product.model");
		
		
		while(!AS.getCanStart().equals("")){
			System.out.println("Get Value "+AS.getCanStart());
			if(AS.getCanStart().equals("Quit"))
			{
				Report.fnReportFailAndTerminateTest("AppiumServer", "Appium Server Not Started within given time");
			}
			if(AS.getCanStart().equals("Appium"))
			{
				Report.fnReportFailAndTerminateTest("AppiumServer", "Couldn't find the Appium.js in the given Location "+AppiumLocation);
			}
			if(AS.getCanStart().equals("Node"))
			{
				Report.fnReportFailAndTerminateTest("AppiumServer", "Couldn't find the node in the given Location "+NodeLocation);
			}
			
		}
		
		
		
		String getRandomGenerateName =	NameGenerator.randomName(7);
		System.out.println("Name Generator.........." +getRandomGenerateName);
	
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
	
		if (SerialNumber.length()==Udid_Count){
			System.out.println("Setting iOS mobile capabilities");
			
			String PROXY = "127.0.0.1:"+DefaultPort;
			org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
			proxy.setHttpProxy(PROXY)
			     .setFtpProxy(PROXY)
			     .setSslProxy(PROXY);
			/*
			if(System.getProperty("os.name").toUpperCase().contains("MAC")){
				
				startIOSWebKit(SerialNumber, DefaultPort);	
				//checkPermissionsForIOSWebKit();
			
			}
			*/
			
			//capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,"appium");
			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,"iOS");
			capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,"8.1");
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, getRandomGenerateName);
			//capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
			//capabilities.setCapability("app", "/Users/tcs/Desktop/Sakthi/LibraryForAPP/MyTalkTalk2.6.02devdebug.ipa");
			//capabilities.setCapability(CapabilityType.PROXY,proxy);
			capabilities.setCapability("bundleId", "com.talktalk.mytalktalk25");
			capabilities.setCapability("autoAcceptAlerts", true);
			capabilities.setCapability("noReset", true);
			//capabilities.setCapability("showIOSLog",true);
			
		}else{
			System.out.println("Setting Android mobile capabilities");
		//	RandomGeneratorSleepTime();
			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,"Android");
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, getRandomGenerateName);
			//capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
			
			capabilities.setCapability("app", "com.talktalk.mytalktalk25");
			capabilities.setCapability("appActivity", "com.talktalk.mytalktalk25.MainActivity");
			//capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
			
		}
	
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, LoadEnvironment.AppiumTimeout);
		capabilities.setCapability("udid", SerialNumber);
		
		
		System.out.println(capabilities);
	//	RandomGeneratorSleepTime();
		URL url = new URL("http://127.0.0.1:"+DefaultPort+"/wd/hub");
		
		try
		{
			System.out.println("Launching Appium driver");
			//driver1 = new AndroidDriver(url,capabilities);
			
			driver = new AppiumDriver(url,capabilities) {
				@Override
				public MobileElement scrollToExact(String arg0) {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public MobileElement scrollTo(String arg0) {
					// TODO Auto-generated method stub
					return null;
				}
			};
			
			//driver = new IOSDriver(url,capabilities);
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			System.out.println("Appium driver launched");
			
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
		
		
		Object[] ReturnValue={driver,SerialNumber,ChromeDriverPorts,DefaultPort};
		System.out.println("Return Value "+ReturnValue);
		return ReturnValue;
	}
	
	public void clearRuntimeData(Object[] Data) throws Exception
	{
		String DeviceId =(String) Data[3];
		int getDeviceId = DeviceId.length();
		System.out.println("Cleared device id's in properties files... "+DeviceId );
		
		((ExtentReports) Data[1]).endTest((ExtentTest) Data[2]);
		MakeDeviceIdle((String) Data[3]);
		((RemoteWebDriver) Data[0]).quit();
		getPIDDetails((String) Data[5]);
		
		try {
			if(getDeviceId < 40){
			//ExecuteCommand("com.android.chrome", ProcessType.ClearApp);
			//ExecuteCommand(mPackageName, ProcessType.ClearApp_Android);
			}else{
			//ExecuteCommand(mPackageName, ProcessType.ClearApp_IOS);
			}
		} catch (Exception e) {
			System.out.println("Exception while clear runtime data "+e);
		}
	}
	
	
	public boolean TestPreProcessing(String ROW,String WorkBook,String Sheet,boolean... FAQ) throws Exception{

		System.out.println("<------------------------ Pre Processing ------------------------>");
		ReadExcelSheet RX = new ReadExcelSheet(Report);
		Report.fnReportPass("OS : "+System.getProperty("os.name")+" OSVersion : "+System.getProperty("os.version")+" OSArchitecture : "+System.getProperty("os.arch"));
		if(RX.ReadFromExcelWithRows(WorkBook,Sheet, ROW, "TO_BE_EXECUTED").equalsIgnoreCase("YES")){
			Report.CurrentRowOfExecution=ROW;
			Report.rownumber = Report.rownumber + 1;
			//Report.ReporterLog(Report.methodname+ " from Row number "+ROW+" -> Test Started", "Pass", Report.rownumber);
			//Report.ReporterLog(Report.methodname+ " from Row number "+ROW+" -> Test Started", "Pass", LogStatus.PASS);
			platform=Sheet;
			System.out.println(Sheet);
			if(FAQ.length>0)
			{
				booleanFAQ=FAQ[0];
			}
			else
			{
				booleanFAQ=false;
			}
			Password=LoadEnvironment.MAPassword;
			//			WhereToExecute("Welcome",Report.methodname);
			//			InitiateAppiumServerStartUp();
		}
		return true;
	}
	
	
	public static void startIOSWebKit(String udid, String port) throws Exception {
		String ios_web_lit_proxy_runner="/Applications/Appium4.app/Contents/Resources/node_modules/appium/bin/ios-webkit-debug-proxy-launcher.js";
		String webkitRunner= ios_web_lit_proxy_runner+ " -c " + udid + ":27753" + " -d";
		//String webkitRunner= "ios_webkit_debug_proxy"+ " -c " + udid + ":27753" + " -d";
		System.out.println(webkitRunner);
		//Process process = Runtime.getRuntime().exec(webkitRunner);
		//String commandValue	=	executeCommand(webkitRunner);
		//String commandValue	=	executeCommand("/Applications/Appium4.app/Contents/Resources/node_modules/appium/bin/ios-webkit-debug-proxy-launcher.js -c 5aef5ba4cfdc74ccb2caf0e4d8cbf364c6feb0f3:27753");
		/*
		if(commandValue != null){
			System.out.println("Started WebKit proxy on device " + "27753" + " and with port number "+ port );
		}else{
			System.out.println("Not ******* Started WebKit proxy on device " + "27753" + " and with port number "+ port );
		}
		*/
        
	}
	
	
	public static void checkPermissionsForIOSWebKit(){
		try{
			File executePermission = new File("/Applications/Appium4.app/Contents/Resources/node_modules/appium/bin/ios-webkit-debug-proxy-launcher.js");
			if(executePermission.exists()){
				System.out.println("File Exists");
			}else{
				System.out.println("File doesn't Exists");
			}
			if(executePermission.canExecute() == false){
				executePermission.setExecutable(true);
				System.out.println("Access Granted for iOSWebKitProxyLauncher");
			}else{
				System.out.println("iOSWebKitProxyLauncher File already has access to execute");
			}
		}catch(NullPointerException npe){
			System.out.println("Null Pointer Exception while checking the permissions for Web Kit");
		}catch(Exception e){
			System.out.println("Exception while checking the permissions for Web Kit");
		}
	}
	
	
	/*public static void InitiateAppiumServerStartUp(){
		try{
			System.out.println("enetered into appium");
			System.out.println("System : "+SYSTEM_OS_NAME);
			String[] splitSystemOs	=	SYSTEM_OS_NAME.split(" ");
			System.out.println("platform : "+PLATFORM_NAME);
			if(splitSystemOs[0].toUpperCase().contains("WINDOWS") && PLATFORM_NAME.contains("Android")){
				System.out.println("Win_Android");
				if(WHERE_TO_EXECUTE.equalsIgnoreCase("DEVICE") && EXECUTION_MODE.equalsIgnoreCase("BROWSER")){
					if(EXECUTION_TYPE.equalsIgnoreCase("SEQUENTIAL")){
						//Even though a single device/multiple is configured/Connected
						//It works only single senario at a time.
						//Initiates appium server only once with default port, since execution type is sequential. 
						//Even though multiple devices are connected we take only the first device in the list.
						for(int rowCount=0;rowCount<Config.Device_Proiperties.size();rowCount++){
							getRequirdDeviceProperties(rowCount);
							if(PLATFORM_NAME.equalsIgnoreCase("Android")){
								break;
							}
						}
						AppiumServer as	=	new AppiumServer(EXECUTION_TYPE, DEFAULT_BOOTSTRAP_PORT);
						Thread thread	=	new Thread(as);
						thread.start();
						//Add join condition here
						thread.join();
						Thread.sleep(1000);
						DesiredCapabilities capabilities	=	new DesiredCapabilities();
						capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PLATFORM_NAME);
						capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, DEVICE_VERSION);
						if(Config.Device_Proiperties.size() == 1){
							capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, DEVICE_NAME);	
						}else{
							capabilities.setCapability("udid", DEVICE_SERIAL_NO);
						}
						capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BROWSER_NAME);
						//appPackage key varies and it depends on java client we used
						//capabilities.setCapability("appPackage", Config.DEVICE_PACKAGE_NAME);
						capabilities.setCapability("app", DEVICE_PACKAGE_NAME);
						//appActivity key varies and it depends on java client we used
						capabilities.setCapability("appActivity", DEVICE_ACTIVITY_NAME);
						capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, DEVICE_TIME_OUT);
						//capabilities.setCapability(MobileCapabilityType.AUTO_WEBVIEW, "true");
						capabilities.setCapability("nativeWebTap", "true");
						System.out.println("Loaded capabilities : "+capabilities);
						URL url	=	new URL("http://127.0.0.1:"+BOOTSTRAP_PORT+"/wd/hub");
						androidDribverList.add(new AndroidDriver(url,capabilities));
						//driver	=	new AndroidDriver(url,capabilities);
						Thread.sleep(2000);
						//driver.get("http://www.google.com/");
					}if(EXECUTION_TYPE.equalsIgnoreCase("PARALLEL")){
						for(int rowCount=0;rowCount<Config.Device_Proiperties.size();rowCount++){
							getRequirdDeviceProperties(rowCount);
							if(PLATFORM_NAME.equalsIgnoreCase("Android")){
								AppiumServer as	=	new AppiumServer(EXECUTION_TYPE, BOOTSTRAP_PORT);
								Thread thread	=	new Thread(as);
								thread.start();
								//Add join condition here
								thread.join();
								Thread.sleep(1000);
								DesiredCapabilities capabilities	=	new DesiredCapabilities();
								capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PLATFORM_NAME);
								capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, DEVICE_VERSION);
								capabilities.setCapability("udid", DEVICE_SERIAL_NO);
								capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BROWSER_NAME);
								//appPackage key varies and it depends on java client we used
								//capabilities.setCapability("appPackage", Config.DEVICE_PACKAGE_NAME);
								capabilities.setCapability("app", DEVICE_PACKAGE_NAME);
								//appActivity key varies and it depends on java client we used
								capabilities.setCapability("appActivity", DEVICE_ACTIVITY_NAME);
								capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, DEVICE_TIME_OUT);
								//capabilities.setCapability(MobileCapabilityType.AUTO_WEBVIEW, "true");
								capabilities.setCapability("nativeWebTap", "true");
								System.out.println("Loaded capabilities : "+capabilities);
								URL url	=	new URL("http://127.0.0.1:"+BOOTSTRAP_PORT+"/wd/hub");
								//driver	=	new AndroidDriver(url,capabilities);
								androidDribverList.add(new AndroidDriver(url,capabilities));
								Thread.sleep(2000);
								//driver.get("http://www.google.com/");
							}
						}
					}
				}if(WHERE_TO_EXECUTE.equalsIgnoreCase("DEVICE") && EXECUTION_MODE.equalsIgnoreCase("MOBILE_APP")){
					if(EXECUTION_TYPE.equalsIgnoreCase("SEQUENTIAL")){
						//Even though a single device/multiple is configured/Connected
						//It works only single senario at a time.
						//Initiates appium server only once with default port, since execution type is sequential. 
						//Even though multiple devices are connected we take only the first device in the list.
						for(int rowCount=0;rowCount<Config.Device_Proiperties.size();rowCount++){
							getRequirdDeviceProperties(rowCount);
							if(PLATFORM_NAME.equalsIgnoreCase("Android")){
								break;
							}
						}
						AppiumServer as	=	new AppiumServer(EXECUTION_TYPE, DEFAULT_BOOTSTRAP_PORT);
						Thread thread	=	new Thread(as);
						thread.start();
						//Add join condition here
						thread.join();
						Thread.sleep(1000);
						DesiredCapabilities capabilities	=	new DesiredCapabilities();
						capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PLATFORM_NAME);
						capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, DEVICE_VERSION);
						capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, DEVICE_NAME);
//						if(Config.Device_Proiperties.size() == 1){
//							capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, Config.DEVICE_NAME);	
//						}else{
							capabilities.setCapability("udid", DEVICE_SERIAL_NO);
//						}
						//appPackage key varies and it depends on java client we used
						capabilities.setCapability("app", DEVICE_PACKAGE_NAME);
						//appActivity key varies and it depends on java client we used
						capabilities.setCapability("appActivity", DEVICE_ACTIVITY_NAME);
						capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, DEVICE_TIME_OUT);
						URL url	=	new URL("http://127.0.0.1:"+BOOTSTRAP_PORT+"/wd/hub");
						//driver	=	new AndroidDriver(url,capabilities);
						androidDribverList.add(new AndroidDriver(url,capabilities));
						Thread.sleep(2000);
					}
					if(EXECUTION_TYPE.equalsIgnoreCase("PARALLEL")){
						for(int rowCount=0;rowCount<Config.Device_Proiperties.size();rowCount++){
							getRequirdDeviceProperties(rowCount);
							if(PLATFORM_NAME.equalsIgnoreCase("Android")){
								AppiumServer as	=	new AppiumServer(EXECUTION_TYPE, BOOTSTRAP_PORT);
								Thread thread	=	new Thread(as);
								thread.start();
								//Add join condition here
								thread.join();
								Thread.sleep(1000);
								DesiredCapabilities capabilities	=	new DesiredCapabilities();
								capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PLATFORM_NAME);
								capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, DEVICE_VERSION);
								capabilities.setCapability("udid", DEVICE_SERIAL_NO);
								capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, DEVICE_NAME);
								//appPackage key varies and it depends on java client we used
								//capabilities.setCapability("appPackage", Config.DEVICE_PACKAGE_NAME);
								capabilities.setCapability("app", DEVICE_PACKAGE_NAME);
								//appActivity key varies and it depends on java client we used
								capabilities.setCapability("appActivity", DEVICE_ACTIVITY_NAME);
								capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, DEVICE_TIME_OUT);
								System.out.println("Loaded capabilities : "+capabilities);
								URL url	=	new URL("http://127.0.0.1:"+BOOTSTRAP_PORT+"/wd/hub");
								//driver	=	new AndroidDriver(url,capabilities);
								androidDribverList.add(new AndroidDriver(url,capabilities));
								LoadEnvironment.capabilities.add(capabilities);
								LoadEnvironment.url.add(url);
								Thread.sleep(2000);
							}
						}
					}
				}if(WHERE_TO_EXECUTE.equalsIgnoreCase("LOCAL") && EXECUTION_MODE.equalsIgnoreCase("BROWSER")){
					//this condition belongs to desktop
				}
			}else{
				System.out.println("else devices");
				//fOR MAC IOS , lly add the code for MAC Android
				if(WHERE_TO_EXECUTE.equalsIgnoreCase("DEVICE") && EXECUTION_MODE.equalsIgnoreCase("BROWSER") && AndroidDeviceConnectionStatus){
					if(EXECUTION_TYPE.equalsIgnoreCase("SEQUENTIAL")){
						for(int rowCount=0;rowCount<Config.Device_Proiperties.size();rowCount++){
							getRequirdDeviceProperties(rowCount);
							if(PLATFORM_NAME.equalsIgnoreCase("Android")){
								break;
							}
						}
						AppiumServer as	=	new AppiumServer(EXECUTION_TYPE, DEFAULT_BOOTSTRAP_PORT);
						Thread thread	=	new Thread(as);
						thread.start();
						//Add join condition here
						thread.join();
						Thread.sleep(1000);
						DesiredCapabilities capabilities	=	new DesiredCapabilities();
						capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PLATFORM_NAME);
						capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, DEVICE_VERSION);
						if(Config.Device_Proiperties.size() == 1){
							capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, DEVICE_NAME);	
						}else{
							capabilities.setCapability("udid", DEVICE_SERIAL_NO);
						}
						capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BROWSER_NAME);
						//appPackage key varies and it depends on java client we used
						//capabilities.setCapability("appPackage", Config.DEVICE_PACKAGE_NAME);
						capabilities.setCapability("app", DEVICE_PACKAGE_NAME);
						//appActivity key varies and it depends on java client we used
						capabilities.setCapability("appActivity", DEVICE_ACTIVITY_NAME);
						capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, DEVICE_TIME_OUT);
						capabilities.setCapability(MobileCapabilityType.LAUNCH_TIMEOUT, DEVICE_TIME_OUT);
						capabilities.setCapability(MobileCapabilityType.DEVICE_READY_TIMEOUT, DEVICE_TIME_OUT);
						capabilities.setCapability("noReset", true);
						System.out.println("Loaded capabilities : "+capabilities);
						try {
							//check for the command too
							  //executeCommand("bash -c sudo xcode-select -s /Applications/Xcode.app/Contents/Developer/");
							  //Check for default ip i think it should be 0.0.0.0:4725
							  iosDriver = new IOSDriver(new URL("http://127.0.0.1:"+BOOTSTRAP_PORT+"/wd/hub"),capabilities);
						  } catch (java.net.MalformedURLException e) {
						      	  e.printStackTrace();
						  }
					}if(EXECUTION_TYPE.equalsIgnoreCase("PARALLEL")){
						for(int rowCount=0;rowCount<Config.Device_Proiperties.size();rowCount++){
							getRequirdDeviceProperties(rowCount);
							if(PLATFORM_NAME.equalsIgnoreCase("Android")){
								AppiumServer as	=	new AppiumServer(EXECUTION_TYPE, BOOTSTRAP_PORT);
								Thread thread	=	new Thread(as);
								thread.start();
								//Add join condition here
								thread.join();
								Thread.sleep(1000);
								DesiredCapabilities capabilities	=	new DesiredCapabilities();
								capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PLATFORM_NAME);
								capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, DEVICE_VERSION);
								capabilities.setCapability("udid", DEVICE_SERIAL_NO);
								capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BROWSER_NAME);
								//appPackage key varies and it depends on java client we used
								//capabilities.setCapability("appPackage", Config.DEVICE_PACKAGE_NAME);
								capabilities.setCapability("app", DEVICE_PACKAGE_NAME);
								//appActivity key varies and it depends on java client we used
								capabilities.setCapability("appActivity", DEVICE_ACTIVITY_NAME);
								capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, DEVICE_TIME_OUT);
								capabilities.setCapability(MobileCapabilityType.LAUNCH_TIMEOUT, DEVICE_TIME_OUT);
								capabilities.setCapability(MobileCapabilityType.DEVICE_READY_TIMEOUT, DEVICE_TIME_OUT);
								capabilities.setCapability("noReset", true);
								System.out.println("Loaded capabilities : "+capabilities);
								try {
									//check for the command too
									 // executeCommand("bash -c sudo xcode-select -s /Applications/Xcode.app/Contents/Developer/");
									  //Check for default ip i think it should be 0.0.0.0:PORT
									  iosDriver = new IOSDriver(new URL("http://127.0.0.1:"+BOOTSTRAP_PORT+"/wd/hub"),capabilities);
								  } catch (java.net.MalformedURLException e) {
								      	  e.printStackTrace();
								  }
							}
						}
					}
				}
				if(WHERE_TO_EXECUTE.equalsIgnoreCase("DEVICE") && EXECUTION_MODE.equalsIgnoreCase("BROWSER") && IOSDeviceConnectionStatus){
					if(EXECUTION_TYPE.equalsIgnoreCase("SEQUENTIAL")){
						for(int rowCount=0;rowCount<Config.Device_Proiperties.size();rowCount++){
							getRequirdDeviceProperties(rowCount);
							if(PLATFORM_NAME.equalsIgnoreCase("IOS")){
								break;
							}
						}
						AppiumServer as	=	new AppiumServer(EXECUTION_TYPE, DEFAULT_BOOTSTRAP_PORT);
						Thread thread	=	new Thread(as);
						thread.start();
						//Add join condition here
						thread.join();
						Thread.sleep(1000);
						DesiredCapabilities capabilities	=	new DesiredCapabilities();
						capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PLATFORM_NAME);
						capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, DEVICE_VERSION);
						if(Config.Device_Proiperties.size() == 1){
							capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, DEVICE_NAME);	
						}else{
							capabilities.setCapability("udid", DEVICE_SERIAL_NO);
						}
						capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BROWSER_NAME);
						//appPackage key varies and it depends on java client we used
						//capabilities.setCapability("appPackage", Config.DEVICE_PACKAGE_NAME);
						capabilities.setCapability("app", DEVICE_PACKAGE_NAME);
						//appActivity key varies and it depends on java client we used
						capabilities.setCapability("appActivity", DEVICE_ACTIVITY_NAME);
						capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, DEVICE_TIME_OUT);
						capabilities.setCapability(MobileCapabilityType.LAUNCH_TIMEOUT, DEVICE_TIME_OUT);
						capabilities.setCapability(MobileCapabilityType.DEVICE_READY_TIMEOUT, DEVICE_TIME_OUT);
						capabilities.setCapability("noReset", true);
						System.out.println("Loaded capabilities : "+capabilities);
						try {
							//check for the command too
							  executeCommand("bash -c sudo xcode-select -s /Applications/Xcode.app/Contents/Developer/");
							  //Check for default ip i think it should be 0.0.0.0:4725
							  iosDriver = new IOSDriver(new URL("http://127.0.0.1:"+BOOTSTRAP_PORT+"/wd/hub"),capabilities);
						  } catch (java.net.MalformedURLException e) {
						      	  e.printStackTrace();
						  }
					}if(EXECUTION_TYPE.equalsIgnoreCase("PARALLEL")){
						for(int rowCount=0;rowCount<Config.Device_Proiperties.size();rowCount++){
							getRequirdDeviceProperties(rowCount);
							if(PLATFORM_NAME.equalsIgnoreCase("IOS")){
								AppiumServer as	=	new AppiumServer(EXECUTION_TYPE, BOOTSTRAP_PORT);
								Thread thread	=	new Thread(as);
								thread.start();
								//Add join condition here
								thread.join();
								Thread.sleep(1000);
								DesiredCapabilities capabilities	=	new DesiredCapabilities();
								capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PLATFORM_NAME);
								capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, DEVICE_VERSION);
								capabilities.setCapability("udid", DEVICE_SERIAL_NO);
								capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BROWSER_NAME);
								//appPackage key varies and it depends on java client we used
								//capabilities.setCapability("appPackage", Config.DEVICE_PACKAGE_NAME);
								capabilities.setCapability("app", DEVICE_PACKAGE_NAME);
								//appActivity key varies and it depends on java client we used
								capabilities.setCapability("appActivity", DEVICE_ACTIVITY_NAME);
								capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, DEVICE_TIME_OUT);
								capabilities.setCapability(MobileCapabilityType.LAUNCH_TIMEOUT, DEVICE_TIME_OUT);
								capabilities.setCapability(MobileCapabilityType.DEVICE_READY_TIMEOUT, DEVICE_TIME_OUT);
								capabilities.setCapability("noReset", true);
								System.out.println("Loaded capabilities : "+capabilities);
								try {
									//check for the command too
									  executeCommand("bash -c sudo xcode-select -s /Applications/Xcode.app/Contents/Developer/");
									  //Check for default ip i think it should be 0.0.0.0:PORT
									  iosDriver = new IOSDriver(new URL("http://127.0.0.1:"+BOOTSTRAP_PORT+"/wd/hub"),capabilities);
								  } catch (java.net.MalformedURLException e) {
								      	  e.printStackTrace();
								  }
							}
						}
					}
				}if(WHERE_TO_EXECUTE.equalsIgnoreCase("DEVICE") && EXECUTION_MODE.equalsIgnoreCase("MOBILE_APP") && IOSDeviceConnectionStatus){
					if(EXECUTION_TYPE.equalsIgnoreCase("SEQUENTIAL")){
						for(int rowCount=0;rowCount<Config.Device_Proiperties.size();rowCount++){
							getRequirdDeviceProperties(rowCount);
							if(PLATFORM_NAME.equalsIgnoreCase("IOS")){
								break;
							}
						}
						AppiumServer as	=	new AppiumServer(EXECUTION_TYPE, DEFAULT_BOOTSTRAP_PORT);
						Thread thread	=	new Thread(as);
						thread.start();
						//Add join condition here
						thread.join();
						Thread.sleep(1000);
						DesiredCapabilities capabilities	=	new DesiredCapabilities();
						capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PLATFORM_NAME);
						capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, DEVICE_VERSION);
						if(Config.Device_Proiperties.size() == 1){
							capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, DEVICE_NAME);	
						}else{
							capabilities.setCapability("udid", DEVICE_SERIAL_NO);
						}
						//appPackage key varies and it depends on java client we used
						capabilities.setCapability("app", DEVICE_PACKAGE_NAME);
						//appActivity key varies and it depends on java client we used
						capabilities.setCapability("appActivity", DEVICE_ACTIVITY_NAME);
						capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, DEVICE_TIME_OUT);
						capabilities.setCapability(MobileCapabilityType.LAUNCH_TIMEOUT, DEVICE_TIME_OUT);
						capabilities.setCapability(MobileCapabilityType.DEVICE_READY_TIMEOUT, DEVICE_TIME_OUT);
						capabilities.setCapability("noReset", true);
						System.out.println("Loaded capabilities : "+capabilities);
						try {
							//check for the command too
							  executeCommand("bash -c sudo xcode-select -s /Applications/Xcode.app/Contents/Developer/");
							  //Check for default ip i think it should be 0.0.0.0:PORT
							  iosDriver = new IOSDriver(new URL("http://127.0.0.1:"+BOOTSTRAP_PORT+"/wd/hub"),capabilities);
						  } catch (java.net.MalformedURLException e) {
						      	  e.printStackTrace();
						  }
					}if(EXECUTION_TYPE.equalsIgnoreCase("PARALLEL")){
						for(int rowCount=0;rowCount<Config.Device_Proiperties.size();rowCount++){
							getRequirdDeviceProperties(rowCount);
							if(PLATFORM_NAME.equalsIgnoreCase("IOS")){
								AppiumServer as	=	new AppiumServer(EXECUTION_TYPE, BOOTSTRAP_PORT);
								Thread thread	=	new Thread(as);
								thread.start();
								//Add join condition here
								thread.join();
								Thread.sleep(1000);
								DesiredCapabilities capabilities	=	new DesiredCapabilities();
								capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PLATFORM_NAME);
								capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, DEVICE_VERSION);
								capabilities.setCapability("udid", DEVICE_SERIAL_NO);
								capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BROWSER_NAME);
								//appPackage key varies and it depends on java client we used
								//capabilities.setCapability("appPackage", Config.DEVICE_PACKAGE_NAME);
								capabilities.setCapability("app", DEVICE_PACKAGE_NAME);
								//appActivity key varies and it depends on java client we used
								capabilities.setCapability("appActivity", DEVICE_ACTIVITY_NAME);
								capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, DEVICE_TIME_OUT);
								capabilities.setCapability(MobileCapabilityType.LAUNCH_TIMEOUT, DEVICE_TIME_OUT);
								capabilities.setCapability(MobileCapabilityType.DEVICE_READY_TIMEOUT, DEVICE_TIME_OUT);
								capabilities.setCapability("noReset", true);
								System.out.println("Loaded capabilities : "+capabilities);
								try {
									//check for the command too
									  executeCommand("bash -c sudo xcode-select -s /Applications/Xcode.app/Contents/Developer/");
									  //Check for default ip i think it should be 0.0.0.0:PORT
									  iosDriver = new IOSDriver(new URL("http://127.0.0.1:"+BOOTSTRAP_PORT+"/wd/hub"),capabilities);
								  } catch (java.net.MalformedURLException e) {
								      	  e.printStackTrace();
								  }
							}
						}
					}
				}if(WHERE_TO_EXECUTE.equalsIgnoreCase("DEVICE") && EXECUTION_MODE.equalsIgnoreCase("MOBILE_APP") && AndroidDeviceConnectionStatus){
					if(EXECUTION_TYPE.equalsIgnoreCase("SEQUENTIAL")){
						for(int rowCount=0;rowCount<Config.Device_Proiperties.size();rowCount++){
							getRequirdDeviceProperties(rowCount);
							if(PLATFORM_NAME.equalsIgnoreCase("Android")){
								break;
							}
						}
						AppiumServer as	=	new AppiumServer(EXECUTION_TYPE, DEFAULT_BOOTSTRAP_PORT);
						Thread thread	=	new Thread(as);
						thread.start();
						//Add join condition here
						thread.join();
						Thread.sleep(1000);
						DesiredCapabilities capabilities	=	new DesiredCapabilities();
						capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PLATFORM_NAME);
						capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, DEVICE_VERSION);
						if(Config.Device_Proiperties.size() == 1){
							capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, DEVICE_NAME);	
						}else{
							capabilities.setCapability("udid", DEVICE_SERIAL_NO);
						}
						//appPackage key varies and it depends on java client we used
						capabilities.setCapability("app", DEVICE_PACKAGE_NAME);
						//appActivity key varies and it depends on java client we used
						capabilities.setCapability("appActivity", DEVICE_ACTIVITY_NAME);
						capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, DEVICE_TIME_OUT);
						capabilities.setCapability(MobileCapabilityType.LAUNCH_TIMEOUT, DEVICE_TIME_OUT);
						capabilities.setCapability(MobileCapabilityType.DEVICE_READY_TIMEOUT, DEVICE_TIME_OUT);
						capabilities.setCapability("noReset", true);
						System.out.println("Loaded capabilities : "+capabilities);
						try {
							//check for the command too
							  executeCommand("bash -c sudo xcode-select -s /Applications/Xcode.app/Contents/Developer/");
							  //Check for default ip i think it should be 0.0.0.0:PORT
							  iosDriver = new IOSDriver(new URL("http://127.0.0.1:"+BOOTSTRAP_PORT+"/wd/hub"),capabilities);
						  } catch (java.net.MalformedURLException e) {
						      	  e.printStackTrace();
						  }
					}if(EXECUTION_TYPE.equalsIgnoreCase("PARALLEL")){
						for(int rowCount=0;rowCount<Config.Device_Proiperties.size();rowCount++){
							getRequirdDeviceProperties(rowCount);
							if(PLATFORM_NAME.equalsIgnoreCase("Android")){
								AppiumServer as	=	new AppiumServer(EXECUTION_TYPE, BOOTSTRAP_PORT);
								Thread thread	=	new Thread(as);
								thread.start();
								//Add join condition here
								thread.join();
								Thread.sleep(1000);
								DesiredCapabilities capabilities	=	new DesiredCapabilities();
								capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PLATFORM_NAME);
								capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, DEVICE_VERSION);
								capabilities.setCapability("udid", DEVICE_SERIAL_NO);
								capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BROWSER_NAME);
								//appPackage key varies and it depends on java client we used
								//capabilities.setCapability("appPackage", Config.DEVICE_PACKAGE_NAME);
								capabilities.setCapability("app", DEVICE_PACKAGE_NAME);
								//appActivity key varies and it depends on java client we used
								capabilities.setCapability("appActivity", DEVICE_ACTIVITY_NAME);
								capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, DEVICE_TIME_OUT);
								capabilities.setCapability(MobileCapabilityType.LAUNCH_TIMEOUT, DEVICE_TIME_OUT);
								capabilities.setCapability(MobileCapabilityType.DEVICE_READY_TIMEOUT, DEVICE_TIME_OUT);
								capabilities.setCapability("noReset", true);
								System.out.println("Loaded capabilities : "+capabilities);
								try {
									//check for the command too
									  executeCommand("bash -c sudo xcode-select -s /Applications/Xcode.app/Contents/Developer/");
									  //Check for default ip i think it should be 0.0.0.0:PORT
									  iosDriver = new IOSDriver(new URL("http://127.0.0.1:"+BOOTSTRAP_PORT+"/wd/hub"),capabilities);
								  } catch (java.net.MalformedURLException e) {
								      	  e.printStackTrace();
								  }
							}
						}
					}
				}if(WHERE_TO_EXECUTE.equalsIgnoreCase("LOCAL") && EXECUTION_MODE.equalsIgnoreCase("BROWSER")){
					//this condition belongs to desktop
				}
			}
		}catch(NullPointerException npe){
			System.out.println("Null Pointer Exception while Initiating Appium server and Desired capabiities "+npe.getLocalizedMessage());
			npe.printStackTrace();
		}catch(Exception e){
			System.out.println("Exception while Initiating Appium server and Desired capabiities "+e.getLocalizedMessage());
			e.printStackTrace();
		}
	}*/
}