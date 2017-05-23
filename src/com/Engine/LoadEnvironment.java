package com.Engine;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.Config.Config;


public class LoadEnvironment {
	public static List<AndroidDriver> testDriver					=	new ArrayList<AndroidDriver>();
	public static List<AndroidDriver> androidDribverList			=	new ArrayList<AndroidDriver>();
	public static List<DesiredCapabilities> capabilities			=	new ArrayList<DesiredCapabilities>();
	public static List<URL>	 url									=	new ArrayList<URL>();
	public static IOSDriver iosDriver;
	
	public static String WHERE_TO_EXECUTE							=	"DEVICE";
	public static String EXECUTION_MODE								=	"MOBILE_APP";
	public static String EXECUTION_TYPE								=	"PARALLEL";
	
	//Device Connection Status
	public static boolean AndroidDeviceConnectionStatus				=	false;
	public static boolean IOSDeviceConnectionStatus					=	false;
	//System Properties
	public static String SYSTEM_OS_NAME;
	public static String SYSTEM_OS_VERSION;
	public static String SYSTEM_OS_ARCH;
	public static boolean SERVER_SATRTUP_STATUS	=	false;
	//Device Properties
	public static String DEVICE_NAME								=	"";
	public static String DEVICE_VERSION								=	"";
	public static String DEVICE_MODEL								=	"";
	public static String DEVICE_MANUFACTURER						=	"";
	public static String DEVICE_SERIAL_NO							=	"";
	public static String DEVICE_CONNECTION_STATUS					=	"";
	public static String DEVICE_PACKAGE_NAME						=	"com.talktalk.mytalktalk";
	public static String DEVICE_ACTIVITY_NAME						=	"MainActivity";
	public static String DEVICE_TIME_OUT							=	"1800000";
	public static String BROWSER_NAME								=	"";
	
	//SERVER PROPERTIES
		public static String PLATFORM_NAME								=	"";
		public static String SERVER_CONNECTION_STATUS					=	"";
		public static String DEFAULT_BOOTSTRAP_PORT						=	"4725";
		public static String BOOTSTRAP_PORT_RANGE						=	"";
		public static String BOOTSTRAP_PORT								=	"";
		public static String CHROME_DRIVER_PORT							=	"";
		public static String SELENORID_PORT								=	"";
		public static String APPIUM_TIME_OUT							=	"";
	
	// Down Stream Environment Properties	
	public static String ENV;
	public static String CRM_URL;
	public static String CRM_USERNAME;
	public static String CRM_PASSWORD;
	public static String BW_SERVERIP;
	public static String BW_SERVERPORT;
	// eMM variables
	public static String EMM_USERNAME;
	public static String EMM_PASSWORD;
	public static String EMM_HOSTNAME;
	public static String EMM_PORT;
	// CRM DB variables
	public static String CRM_DBNAME;
	public static String CRM_DBUSERNAME;
	public static String CRM_DBPASSWORD;
	public static String CRM_DBIP;
	public static String CRM_DBPORT;
	// SV DB variables
	public static String SV_DBNAME;
	public static String SV_DBUSERNAME;
	public static String SV_DBPASSWORD;
	public static String SV_DBSCHEMA;
	public static String SV_DBIP;
	public static String SV_DBPORT;
	// OMP DB variables
	public static String OMP_DBNAME;
	public static String OMP_DBUSERNAME;
	public static String OMP_DBPASSWORD;
	public static String OMP_DBSCHEMA;
	public static String OMP_DBIP;
	public static String OMP_DBPORT;
	// SKID DB variables
	public static String SKID_DBNAME;
	public static String SKID_DBUSERNAME;
	public static String SKID_DBPASSWORD;
	public static String SKID_DBIP;
	public static String SKID_DBPORT;
	// DDGS DB variables
	public static String DDGS_DBNAME;
	public static String DDGS_DBUSERNAME;
	public static String DDGS_DBPASSWORD;
	public static String DDGS_DBIP;
	public static String DDGS_DBPORT;
	//EV BD Variables
	public static  String EVG_DBNAME;
	public static  String EVG_DBUSERNAME;
	public static  String EVG_DBPASSWORD;
	public static  String EVG_DBIP;
	public static  String EVG_DBPORT;
	public static  String BootstrapPort;
	public static  String ChromeDriverPort;
	public static  String SelendroidPort;
	public static  String AppiumTimeout;
	public static  String NodeLocation;
	public static  String AppiumLocation;
	public static  String NRM_DBNAME;
	public static  String NRM_DBUSERNAME;
	public static  String NRM_DBPASSWORD;
	public static  String NRM_DBIP;
	public static  String NRM_DBPORT;
	// UP Stream Environment Properties	
	public static String MA_ONLINE_INSTANCE;
	public static String MAPassword;
	public static String MA_URL;
	public static String ONLINE_URL;
	public static String AGENT_URL;
	public static String MA_CSA_URL;
	public static String ONLINE_CSA_URL;


	public static  String SSO_DBNAME;
	public static  String SSO_DBUSERNAME;
	public static  String SSO_DBPASSWORD;
	public static  String SSO_DBIP;
	public static  String SSO_DBPORT;
	
	//Screen Data Properties
	public static String MenuData;
	public static String FooterDatum;
	public static String FooterData;
	public static String SmallPrintData;
	public static String Header_XPATH;
	public static String Header_MenuData;
	public static  String WCData_Modules;
	public static  String WC_NotDISPATCHED;
	public static  String WC_RDMHELPCheck;
	public static  String WC_YVHELPCheck;
	public static  String WC_BBGLNoHELPCheck;
	public static  String WC_BBGLBBHELPCheck;
	public static  String WC_BBGLFBHELPCheck;
	public static  String WC_TVGLHelpCheck;
	public static  String WC_PHBBText;
	public static  String WC_BBGLText;
	public static  String WC_BBGLBBText;
	public static  String WC_PHBBSupport;
	public static  String WC_STVText;
	public static  String WC_STVSupport;
	public static  String WC_SelectDate;
	public static  String WC_BillText;
	
	public static String CPEG_INSTANCE;
	
	public static  String ANOVO_CPEGPlaceOrder_URL;
	public static  String ANOVO_orderSender_URL;
	public static  String ANOVO_statusUpdate_URL;
	public static  String ANOVO_updateProcessor_URL;
	public static  String ANOVO_ReturnStatusupdate_URL;
	public static  String ANOVO_Unsolicitedorderprocessor_URL;

	public static  String NetLynk_CPEGPlaceOrder_URL;
	public static  String NetLynk_orderSender_URL;
	public static  String NetLynk_statusUpdate_URL;
	public static  String NetLynk_updateProcessor_URL;
			
	public static  String CPEG_DBNAME;
	public static  String CPEG_DBUSERNAME;
	public static  String CPEG_DBPASSWORD;
	public static  String CPEG_DBIP;
	public static  String CPEG_DBPORT;
	
	//XPath Properties
	public static String Class_Logo;
	public static String XP_Main;
	public static String WC_L30;
	public static String Header_Support;
	public static String XP_Menu;
	public static String XP_Back;
	public static String XP_Basket;
	public static String XP_LoadingIndicator;
	public static String ID_CopyRight;
	public static String XP_Logo;
	public static String ToolTip_ToClick;
	public static String ToolTip_ToVerify;
	public static String ToolTip_VerifyBanner;
	public static String ToolTip_Tooltip;
	public static String Main_Menu;
	public static String Main_MenuButton;
	public static  String WC_VerifyHomePage;
	public static  String WC_WelcomeMessagePHBB;
	public static  String WC_DAYSLIVE;
	public static  String WC_ModuleIcon;
	public static  String WC_Modules;
	public static  String WC_HEAD;
	public static  String WC_ModuleName;
	public static  String WC_RDM;
	public static  String WC_YV;
	public static  String WC_Date;
	public static  String WC_Help;
	public static  String WC_BBGL;
	public static  String WC_Details;
	public static  String WC_Summary;
	public static  String WC_RDMProceedType;
	public static  String WC_RDMNewType;
	public static  String WC_TextVerify;
	public static  String WC_Track;
	public static  String WC_RDMBanner;
	public static  String WC_YVBanner;
	public static  String WC_CloseHelp;
	public static  String WC_TVGLBanner;
	public static  String WC_TVGLHelp;
	public static  String WC_TVGL;
	public static  String WC_Rejection;
	public static  String WC_Cancel;
	public static  String WC_CancelPlaceAnotherOrder;
	public static  String WC_PHBBImageHead;
	public static  String WC_PFBBImage;
	public static  String WC_PHBBLearn;
	public static  String WC_PHBB;
	public static  String WC_STV;
	public static  String WC_Update;
	public static  String WC_Back;
	public static  String WC_Selected;
	public static  String WC_TimeSlot;
	public static  String WC_SelectedMonth;
	public static  String WC_SelectedDay;
	public static  String WC_DateConfirm;
	public static  String WC_UpdateDateSuccess;
	public static  String WC_GOBACKtoHOME;
	public static  String WC_ChangeAppointment;
	public static  String WC_MFB;
	public static  String WC_MFBLatest;
	public static  String WC_BillDetails;
		
	// Generic System Properties

	public static int timeOut;
	public static int waitTime;
	public static int justtimeOut;
	public static int custTimeOut; 
	public static int t = timeOut;
	public static String INPUTSHEET;
	public static String WB_Mobile_Standard;
	public static String WB_Test;

	public static String Sheet_Test;
	public static String Sheet_mSales;
	public static String Sheet_Service;
	public static String Sheet_Welcome;
	public static String Sheet_MA;
	
	public static String APKname;
	public static String mPackageName;
	//Loading Objects
	public static Properties prop = new Properties();
	public static InputStream input = null;
	public static String workingDir = System.getProperty("user.dir");
	public static String Slash="\\";
	public static void LoadGenericSystemProperties() throws Exception{
		try {
			
			input = new FileInputStream(workingDir+Slash+"PropertyFiles"+Slash+"GenericProperties"+Slash+"TestConfigGeneric.properties");
			prop.load(input);
			//get the property value and print it out
			timeOut =  (int) Double.parseDouble(prop.getProperty("timeOut"));
			waitTime =  (int) Double.parseDouble(prop.getProperty("waitTime"));
			justtimeOut =  (int) Double.parseDouble(prop.getProperty("justtimeOut"));
			MAPassword=prop.getProperty("MAPassword");
			BootstrapPort=prop.getProperty("BootstrapPort");
			ChromeDriverPort=prop.getProperty("ChromeDriverPort");
			SelendroidPort=prop.getProperty("SelendroidPort");
			AppiumTimeout=prop.getProperty("AppiumTimeout");
			NodeLocation=prop.getProperty("NodeLocation");
			AppiumLocation=prop.getProperty("AppiumLocation");
			
			custTimeOut =   (int) Double.parseDouble(prop.getProperty("custTimeOut"));
			INPUTSHEET =prop.getProperty("INPUTSHEET"); 
			WB_Mobile_Standard=workingDir+prop.getProperty("WB_Mobile_Standard");
			WB_Test=workingDir+prop.getProperty("WB_Test");
			Sheet_Test=prop.getProperty("Sheet_Test");
			Sheet_mSales=prop.getProperty("Sheet_mSales");
			Sheet_Service=prop.getProperty("Sheet_Service");
			Sheet_Welcome=prop.getProperty("Sheet_Welcome");
			Sheet_MA=prop.getProperty("Sheet_MA");
			mPackageName=prop.getProperty("mPackageName");
			APKname=prop.getProperty("APKname");
			System.out.println("*------- Generic Test Properties Loaded-------*");
		} catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
	
	public static String executeCommand(String requiredProperty){
		try{
			Process process	=	Runtime.getRuntime().exec(requiredProperty);
			process.waitFor();
		}catch(NullPointerException npe){
			System.out.println("Null Pointer Exception while fetching device properties using adb command"+npe.getLocalizedMessage());
		}catch(Exception e){
			System.out.println("Null Pointer Exception while fetching device properties using adb command : "+e.getLocalizedMessage());
		}
		return null;
	}
	
	public static void clearPreviousInstance(){
		try{
			System.out.println("************Started clearing Previous Instances***********");
			if(SYSTEM_OS_NAME.toUpperCase().contains("WINDOWS")){
				System.out.println("************Completed Previous Instances for WINDOWS***********");
				executeCommand("taskkill /f /im chromeDriver.exe");
				executeCommand("taskkill /f /im node.exe");
				executeCommand("taskkill /f /im adb.exe");
				executeCommand("adb shell pm clear "+DEVICE_PACKAGE_NAME);
			}else{
				
				//Add all the coMmands for MAC
				System.out.println("************Clearing previous instance for MAC ***********");
				executeCommand("pkill -9 chromedriver.exe");
				executeCommand("pkill -9 node.exe");
				executeCommand("pkill -9 adb.exe");
				
			}
			
		}catch(NullPointerException npe){
		}catch(Exception e){
		}
	}
	public static void loadSystemProperties(){
		try{
//			System.out.println("********Started Loading System Properties***********");
			SYSTEM_OS_NAME		=	System.getProperty("os.name");
			SYSTEM_OS_VERSION	=	System.getProperty("os.version");
			SYSTEM_OS_ARCH		=	System.getProperty("os.arch");
			
			System.out.println("******** Loading System Properties ***********");
			
			System.out.println("OS.Name "+ SYSTEM_OS_NAME + "OS Version " + SYSTEM_OS_VERSION +"system arch "+ SYSTEM_OS_ARCH);
			
			clearPreviousInstance();
		}catch(NullPointerException npe){
			System.out.println("Null Pointer Exception while loading system properties : "+npe.getLocalizedMessage());
		}catch(Exception e){
			System.out.println("Exception while loading system properties : "+e.getLocalizedMessage());
		}
	}
	public static void LoadScreenProperties() throws Exception{
		try {
			
			input = new FileInputStream(workingDir+Slash+"PropertyFiles"+Slash+"ScreenProperties"+Slash+"ScreenData.properties");
			//input = new FileInputStream(workingDir+Slash+"PropertyFiles\\ScreenProperties\\ScreenData.properties");
			prop.load(input);
			//get the property value and print it out
			MenuData = prop.getProperty("MenuData");
			FooterData = prop.getProperty("FooterData");
			FooterDatum = prop.getProperty("FooterDatum");
			SmallPrintData = prop.getProperty("SmallPrintData");
			Header_XPATH = prop.getProperty("Header_XPATH");
			Header_MenuData = prop.getProperty("Header_MenuData");
			WCData_Modules = prop.getProperty("WCData_Modules");
			WC_NotDISPATCHED = prop.getProperty("WC_NotDISPATCHED");
			WC_RDMHELPCheck = prop.getProperty("WC_RDMHELPCheck");
			WC_YVHELPCheck = prop.getProperty("WC_YVHELPCheck");
			WC_BBGLNoHELPCheck = prop.getProperty("WC_BBGLNoHELPCheck");
			WC_BBGLBBHELPCheck = prop.getProperty("WC_BBGLBBHELPCheck");
			WC_BBGLFBHELPCheck = prop.getProperty("WC_BBGLFBHELPCheck");
			WC_TVGLHelpCheck = prop.getProperty("WC_TVGLHelpCheck");
			WC_PHBBText = prop.getProperty("WC_PHBBText");
			WC_BBGLText = prop.getProperty("WC_BBGLText");
			WC_BBGLBBText = prop.getProperty("WC_BBGLBBText");
			WC_PHBBSupport = prop.getProperty("WC_PHBBSupport");
			WC_STVText = prop.getProperty("WC_STVText");
			WC_STVSupport = prop.getProperty("WC_STVSupport");
			WC_SelectDate = prop.getProperty("WC_SelectDate");
			WC_BillText = prop.getProperty("WC_BillText");
			System.out.println("*-------Screen Properties Loaded-------*");
		} catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
	public static void LoadXpathProperties() throws Exception{
		try {
			input = new FileInputStream(workingDir+Slash+"PropertyFiles"+Slash+"ScreenProperties"+Slash+"XPath.properties");
			//input = new FileInputStream(workingDir+"\\PropertyFiles\\ScreenProperties\\XPath.properties");
			prop.load(input);
			//get the property value and print it out
			Class_Logo = prop.getProperty("Class_Logo");
			XP_Menu = prop.getProperty("XP_Menu");
			XP_Back = prop.getProperty("XP_Back");
			XP_Basket = prop.getProperty("XP_Basket");
			ID_CopyRight = prop.getProperty("ID_CopyRight");
			XP_Logo = prop.getProperty("XP_Logo");
			XP_Main = prop.getProperty("XP_Main");
			XP_Logo = prop.getProperty("XP_Logo");
			XP_Main = prop.getProperty("XP_Main");
			WC_L30 = prop.getProperty("WC_L30");
			Header_Support = prop.getProperty("Header_Support");
			XP_LoadingIndicator = prop.getProperty("XP_LoadingIndicator");
			ToolTip_ToClick = prop.getProperty("ToolTip_ToClick");
			ToolTip_ToVerify = prop.getProperty("ToolTip_ToVerify");
			ToolTip_VerifyBanner = prop.getProperty("ToolTip_VerifyBanner");
			ToolTip_Tooltip = prop.getProperty("ToolTip_Tooltip");
			ToolTip_VerifyBanner = prop.getProperty("Main_Menu");
			ToolTip_Tooltip = prop.getProperty("Main_MenuButton");
			WC_VerifyHomePage = prop.getProperty("WC_HomePage");
			WC_WelcomeMessagePHBB = prop.getProperty("WC_WelcomeMessagePHBB");
			WC_DAYSLIVE = prop.getProperty("WC_DAYSLIVE");
			WC_ModuleIcon= prop.getProperty("WC_ModuleIcon");
			WC_Modules= prop.getProperty("WC_Modules");
			WC_HEAD= prop.getProperty("WC_HEAD");
			WC_ModuleName= prop.getProperty("WC_ModuleName");
			WC_RDM= prop.getProperty("WC_RDM");
			WC_YV= prop.getProperty("WC_YV");
			WC_Date= prop.getProperty("WC_Date");
			WC_Help = prop.getProperty("WC_Help");
			WC_BBGL= prop.getProperty("WC_BBGL");
			WC_Details= prop.getProperty("WC_Details");
			WC_Summary= prop.getProperty("WC_Summary");
			WC_RDMProceedType= prop.getProperty("WC_RDMProceedType");
			WC_RDMNewType= prop.getProperty("WC_RDMNewType");
			WC_TextVerify= prop.getProperty("WC_TextVerify");
			WC_Track= prop.getProperty("WC_Track");
			WC_RDMBanner= prop.getProperty("WC_RDMBanner");
			WC_YVBanner= prop.getProperty("WC_YVBanner");
			WC_CloseHelp= prop.getProperty("WC_CloseHelp");
			WC_TVGLHelp= prop.getProperty("WC_TVGLHelp");
			WC_TVGLBanner= prop.getProperty("WC_TVGLBanner");
			WC_TVGL= prop.getProperty("WC_TVGL");
			WC_Rejection= prop.getProperty("WC_Rejection");
			WC_Cancel= prop.getProperty("WC_Cancel");
			WC_CancelPlaceAnotherOrder= prop.getProperty("WC_CancelPlaceAnotherOrder");
			WC_PHBBImageHead= prop.getProperty("WC_PHBBImageHead");
			WC_PFBBImage= prop.getProperty("WC_PFBBImage");
			WC_PHBBLearn= prop.getProperty("WC_PHBBLearn");
			WC_PHBB= prop.getProperty("WC_PHBB");
			WC_STV= prop.getProperty("WC_STV");
			WC_Update = prop.getProperty("WC_Update");
			WC_Back = prop.getProperty("WC_Back");
			WC_Selected = prop.getProperty("WC_Selected");
			WC_TimeSlot = prop.getProperty("WC_TimeSlot");
			WC_SelectedMonth = prop.getProperty("WC_SelectedMonth");
			WC_SelectedDay = prop.getProperty("WC_SelectedDay");
			WC_DateConfirm = prop.getProperty("WC_DateConfirm");
			WC_UpdateDateSuccess = prop.getProperty("WC_UpdateDateSuccess");
			WC_GOBACKtoHOME = prop.getProperty("WC_GOBACKtoHOME");
			WC_ChangeAppointment = prop.getProperty("WC_ChangeAppointment");
			WC_MFB = prop.getProperty("WC_MFB");
			WC_MFBLatest = prop.getProperty("WC_MFBLatest");
			WC_BillDetails = prop.getProperty("WC_BillDetails");
			System.out.println("*-------XPath Properties Loaded-------*");
		} catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
	public static void LoadDownStreamENV(String env) throws Exception{

		Properties prop = new Properties();
		InputStream input = null;
		try {
			//input = new FileInputStream(workingDir+"\\PropertyFiles\\EnvironmentProperties\\"+env+".properties");
			input = new FileInputStream(workingDir+Slash+"PropertyFiles"+Slash+"EnvironmentProperties"+Slash+env+".properties");
			//load a properties file
			prop.load(input);

			CPEG_INSTANCE=prop.getProperty("CPEG_INSTANCE");
			
			ANOVO_CPEGPlaceOrder_URL=prop.getProperty("ANOVO_CPEGPlaceOrder_URL");
			ANOVO_orderSender_URL=prop.getProperty("ANOVO_orderSender_URL");
			ANOVO_statusUpdate_URL=prop.getProperty("ANOVO_statusUpdate_URL");
			ANOVO_updateProcessor_URL=prop.getProperty("ANOVO_updateProcessor_URL");
			ANOVO_ReturnStatusupdate_URL=prop.getProperty("ANOVO_ReturnStatusupdate_URL");
			ANOVO_Unsolicitedorderprocessor_URL=prop.getProperty("ANOVO_Unsolicitedorderprocessor_URL");

			NetLynk_CPEGPlaceOrder_URL=prop.getProperty("NetLynk_CPEGPlaceOrder_URL");
			NetLynk_orderSender_URL=prop.getProperty("NetLynk_orderSender_URL");
			NetLynk_statusUpdate_URL=prop.getProperty("NetLynk_statusUpdate_URL");
			NetLynk_updateProcessor_URL=prop.getProperty("NetLynk_updateProcessor_URL");
					
			CPEG_DBNAME=prop.getProperty("CPEG_DBNAME");
			CPEG_DBUSERNAME=prop.getProperty("CPEG_DBUSERNAME");
			CPEG_DBPASSWORD=prop.getProperty("CPEG_DBPASSWORD");
			CPEG_DBIP=prop.getProperty("CPEG_DBIP");
			CPEG_DBPORT=prop.getProperty("CPEG_DBPORT");
			
			//get the property value and print it out
			ENV = prop.getProperty("ENV");
			CRM_URL = prop.getProperty("CRM_URL");
			CRM_USERNAME = prop.getProperty("CRM_USERNAME");
			CRM_PASSWORD = prop.getProperty("CRM_PASSWORD");
			BW_SERVERIP=prop.getProperty("BW_SERVERIP");
			BW_SERVERPORT=prop.getProperty("BW_SERVERPORT");
			// eMM variables
			EMM_USERNAME=prop.getProperty("EMM_USERNAME");
			EMM_PASSWORD=prop.getProperty("EMM_PASSWORD");
			EMM_HOSTNAME=prop.getProperty("EMM_HOSTNAME");
			EMM_PORT=prop.getProperty("EMM_PORT");
			// CRM DB variables
			CRM_DBNAME=prop.getProperty("CRM_DBNAME");
			CRM_DBUSERNAME=prop.getProperty("CRM_DBUSERNAME");
			CRM_DBPASSWORD=prop.getProperty("CRM_DBPASSWORD");
			CRM_DBIP=prop.getProperty("CRM_DBIP");
			CRM_DBPORT=prop.getProperty("CRM_DBPORT");
			// SV DB variables
			SV_DBNAME=prop.getProperty("SV_DBNAME");
			SV_DBUSERNAME=prop.getProperty("SV_DBUSERNAME");
			SV_DBPASSWORD=prop.getProperty("SV_DBPASSWORD");
			SV_DBSCHEMA=prop.getProperty("SV_DBSCHEMA");
			SV_DBIP=prop.getProperty("SV_DBIP");
			SV_DBPORT=prop.getProperty("SV_DBPORT");
			// OMP DB variables
			OMP_DBNAME=prop.getProperty("OMP_DBNAME");
			OMP_DBUSERNAME=prop.getProperty("OMP_DBUSERNAME");
			OMP_DBPASSWORD=prop.getProperty("OMP_DBPASSWORD");
			OMP_DBSCHEMA=prop.getProperty("OMP_DBSCHEMA");
			OMP_DBIP=prop.getProperty("OMP_DBIP");
			OMP_DBPORT=prop.getProperty("OMP_DBPORT");
			// SKID DB variables
			SKID_DBNAME=prop.getProperty("SKID_DBNAME");
			SKID_DBUSERNAME=prop.getProperty("SKID_DBUSERNAME");
			SKID_DBPASSWORD=prop.getProperty("SKID_DBPASSWORD");
			SKID_DBIP=prop.getProperty("SKID_DBIP");
			SKID_DBPORT=prop.getProperty("SKID_DBPORT");
			// DDGS DB variables
			DDGS_DBNAME=prop.getProperty("DDGS_DBNAME");
			DDGS_DBUSERNAME=prop.getProperty("DDGS_DBUSERNAME");
			DDGS_DBPASSWORD=prop.getProperty("DDGS_DBPASSWORD");
			DDGS_DBIP=prop.getProperty("DDGS_DBIP");
			DDGS_DBPORT=prop.getProperty("DDGS_DBPORT");
			//EVG DB Variables
			EVG_DBNAME=prop.getProperty("EVG_DBNAME");
			EVG_DBUSERNAME=prop.getProperty("EVG_DBUSERNAME");
			EVG_DBPASSWORD=prop.getProperty("EVG_DBPASSWORD");
			EVG_DBIP=prop.getProperty("EVG_DBIP");
			EVG_DBPORT=prop.getProperty("EVG_DBPORT");
			System.out.println("*-------DownStream Env Properties Loaded-------*");
			//NRM DB Variables
			NRM_DBNAME=prop.getProperty("NRM_DBNAME");
			NRM_DBUSERNAME=prop.getProperty("NRM_DBUSERNAME");
			NRM_DBPASSWORD=prop.getProperty("NRM_DBPASSWORD");
			NRM_DBIP=prop.getProperty("NRM_DBIP");
			NRM_DBPORT=prop.getProperty("NRM_DBPORT");
			System.out.println("*-------DownStream Env Properties Loaded-------*");
		} catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		}

	}
	public static void loadDeviceProperties(){
		try{
//			System.out.println("************Started Loading Device Properties***********");
			////////////////////////////////////////////////////////////////////////////////////
			if(SYSTEM_OS_NAME.toUpperCase().contains("WINDOWS")){
				String deviceName					=	getDeviceProperties("adb devices");
				ArrayList<String> splitDeviceName	=	new ArrayList<String>();
				String[] splitDeviceList			=	deviceName.split(",");
//				System.out.println(splitDeviceList.length);
				for(int i=1;i<splitDeviceList.length;i++){
//					System.out.println(splitDeviceList[i]);
					if(splitDeviceList[i].contains("device")){
						String[] splitDupliDeviceName	=	splitDeviceList[i].split("device");
						splitDeviceName.add(splitDupliDeviceName[0].trim());
					}
				}
				//checking for android devices are connected, if connected array list size will not be zero else zero
				if(splitDeviceName.size() != 0){
					AndroidDeviceConnectionStatus	=	true;
				}
				splitDeviceName.clear();
			}
			// First priority is given to android devices if both android and ios devices are connected, even though we are executing MAC operating system
			if(AndroidDeviceConnectionStatus){
//				System.out.println("eneterd into android device properties inner loop method");
				String deviceName					=	getDeviceProperties("adb devices");
//				System.out.println("Tester :"+deviceName);
				ArrayList<String> splitDeviceName	=	new ArrayList<String>();
				String[] splitDeviceList			=	deviceName.split(",");
//				System.out.println(splitDeviceList.length);
				for(int i=1;i<splitDeviceList.length;i++){
					System.out.println(splitDeviceList[i]);
					if(splitDeviceList[i].contains("device")){
						String[] splitDupliDeviceName	=	splitDeviceList[i].split("device");
						splitDeviceName.add(splitDupliDeviceName[0].trim());
					}
				}
//				System.out.println("Devic eName List : "+splitDeviceName);
				/*System.out.println("DEVICE_NAME~"+splitDeviceName.get(0));
				System.out.println("DEVICE_NAME~"+splitDeviceName.get(1));*/
				getDeviceProperties("adb devices");
				Thread.sleep(2000);
				for(int deviceCount=0;deviceCount<splitDeviceName.size();deviceCount++){
					Config.Device_Proiperties.add(new ArrayList<String>());
					//System.out.println("DEVICE_NAME~"+splitDeviceName.get(0));
					Config.Device_Proiperties.get(deviceCount).add("DEVICE_NAME~"+splitDeviceName.get(deviceCount));
					String version 					=	getDeviceProperties("adb -s "+splitDeviceName.get(deviceCount)+" shell getprop ro.build.version.release");
					if(version.contains("daemon not running. starting it now on port 5037")){
						String[] deviceVersion	=	version.split(",");
						DEVICE_VERSION	=	deviceVersion[deviceVersion.length-1];
						Config.Device_Proiperties.get(deviceCount).add("DEVICE_VERSION~"+deviceVersion[deviceVersion.length-1]);	
					}else{
						DEVICE_VERSION	=	version;
						Config.Device_Proiperties.get(deviceCount).add("DEVICE_VERSION~"+DEVICE_VERSION);
					}
					DEVICE_MODEL				=	getDeviceProperties("adb -s "+splitDeviceName.get(deviceCount)+" shell getprop ro.product.model");	
					Config.Device_Proiperties.get(deviceCount).add("DEVICE_MODEL~"+DEVICE_MODEL);
					DEVICE_MANUFACTURER		=	getDeviceProperties("adb -s "+splitDeviceName.get(deviceCount)+" shell getprop ro.product.manufacturer");	
					Config.Device_Proiperties.get(deviceCount).add("DEVICE_MANUFACTURER~"+DEVICE_MANUFACTURER);
					DEVICE_SERIAL_NO			=	getDeviceProperties("adb -s "+splitDeviceName.get(deviceCount)+" shell getprop ro.boot.serialno");	
					Config.Device_Proiperties.get(deviceCount).add("DEVICE_SERIAL_NO~"+DEVICE_SERIAL_NO);
					if(splitDeviceName.size()==1){
						BOOTSTRAP_PORT		=	DEFAULT_BOOTSTRAP_PORT;
					}else{
						BOOTSTRAP_PORT		=	Integer.toString(Integer.parseInt(DEFAULT_BOOTSTRAP_PORT)+deviceCount);
					}
					Config.Device_Proiperties.get(deviceCount).add("BOOTSTRAP_PORT~"+BOOTSTRAP_PORT);
					PLATFORM_NAME			=	getDeviceProperties("adb -s "+splitDeviceName.get(deviceCount)+" shell getprop net.bt.name");	
					Config.Device_Proiperties.get(deviceCount).add("PLATFORM_NAME~"+PLATFORM_NAME);
				}
			}
			System.out.println("************ Loaded Device Properties ***********");
		}catch(NullPointerException npe){
			System.out.println("Null Pointer Exception while loading Device properties : "+npe.getLocalizedMessage());
			npe.printStackTrace();
		}catch(Exception e){
			System.out.println("Exception while loading Device properties : "+e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	public static String getDeviceProperties(String requiredProperty){
		String line	=	"";
		try{
			Process process	=	Runtime.getRuntime().exec(requiredProperty);
			InputStream in	=	process.getInputStream();
			BufferedReader br	=	new BufferedReader(new InputStreamReader(in));
			String commandOutputLine	=	null;
			while((commandOutputLine=br.readLine())!= null){
				if(!commandOutputLine.toUpperCase().equalsIgnoreCase("List of devices attached")){
					line	=	line+commandOutputLine+",";
				}
			}
			String splitLine	=	line.replaceAll(",$","").replaceAll(",$","");
			//System.out.println("Split line "+splitLine);
			return splitLine;
		}catch(NullPointerException npe){
			System.out.println("Null Pointer Exception while fetching device properties using instruments command : "+npe.getLocalizedMessage());
		}catch(Exception e){
			System.out.println("Null Pointer Exception while fetching device properties using instruments command : "+e.getLocalizedMessage());
		}
		return null;
	}
	
	public static void getRequirdDeviceProperties(int rowCount){
		try{
			System.out.println("Venky : "+Config.Device_Proiperties.size());
			for(int i=0;i<Config.Device_Proiperties.get(rowCount).size();i++){
				switch(i){
				case 0: String deviceName					=	Config.Device_Proiperties.get(rowCount).get(i);
						String[] splitDeviceName			=	deviceName.split("~");
						System.out.println("Config.DEVICE_NAME : "+splitDeviceName[1]);
						DEVICE_NAME							=	splitDeviceName[1];break;
				case 1: String deviceVersion				=	Config.Device_Proiperties.get(rowCount).get(i);
						String[] splitDeviceVersion			=	deviceVersion.split("~");
						System.out.println("Config.DEVICE_VERSION : "+splitDeviceVersion[1]);
						DEVICE_VERSION						=	splitDeviceVersion[1];break;
				case 2: String deviceModel					=	Config.Device_Proiperties.get(rowCount).get(i);
						String[] splitDeviceModel			=	deviceModel.split("~");
						System.out.println("Config.DEVICE_MODEL : "+splitDeviceModel[1]);
						DEVICE_MODEL						=	splitDeviceModel[1];break;
				case 3: String deviceManufacturer			=	Config.Device_Proiperties.get(rowCount).get(i);
						String[] splitDdeviceManufacturer	=	deviceManufacturer.split("~");
						System.out.println("Config.DEVICE_MANUFACTURER : "+splitDdeviceManufacturer[1]);
						DEVICE_MANUFACTURER					=	splitDdeviceManufacturer[1];break;
				case 4: String deviceSerailNo				=	Config.Device_Proiperties.get(rowCount).get(i);
						String[] splitdeviceSerailNo		=	deviceSerailNo.split("~");
						System.out.println("Config.DEVICE_SERIAL_NO : "+splitdeviceSerailNo[1]);
						DEVICE_SERIAL_NO					=	splitdeviceSerailNo[1];break;
				case 5: String deviceBootStrapPort			=	Config.Device_Proiperties.get(rowCount).get(i);
						String[] splitdeviceBootStrapPort	=	deviceBootStrapPort.split("~");
						System.out.println("Config.BOOTSTRAP_PORT : "+splitdeviceBootStrapPort[1]);
						BOOTSTRAP_PORT						=	splitdeviceBootStrapPort[1];break;
				case 6: String devicePlatFormName			=	Config.Device_Proiperties.get(rowCount).get(i);
						String[] splitdevicePlatFormName	=	devicePlatFormName.split("~");
						System.out.println("Config.PLATFORM_NAME : "+splitdevicePlatFormName[1]);
						PLATFORM_NAME						=	splitdevicePlatFormName[1];break;
				}
			}
		}catch(ArrayIndexOutOfBoundsException aioobe){
			System.out.println("Array out of Bounds exception while fetching required device properties from Device Properties Arraylist :"+aioobe.getLocalizedMessage());
			aioobe.printStackTrace();
		}catch(NullPointerException npe){
			System.out.println("Null Pointer Exception while fetching required device properties from Device Properties Arraylist :"+npe.getLocalizedMessage());
		}catch(Exception e){
			System.out.println("Exception while fetching required device properties from Device Properties Arraylist :"+e.getLocalizedMessage());
		}
	}
	public static void LoadUpStreamEnvironment(String env) throws Exception{

		Properties prop = new Properties();
		InputStream input = null;
		try {
			
			input = new FileInputStream(workingDir+Slash+"PropertyFiles"+Slash+"EnvironmentProperties"+Slash+env+".properties");
			//input = new FileInputStream(workingDir+"\\PropertyFiles\\EnvironmentProperties\\"+env+".properties");
			
			//load a properties file
			prop.load(input);

			//get the property value and print it out
			MA_ONLINE_INSTANCE=prop.getProperty("MA_ONLINE_INSTANCE");
			MA_URL = prop.getProperty("MA_URL");
			ONLINE_URL = prop.getProperty("ONLINE_URL");
			AGENT_URL = prop.getProperty("AGENT_URL");
			MA_CSA_URL = prop.getProperty("MA_CSA_URL");
			ONLINE_CSA_URL = prop.getProperty("ONLINE_CSA_URL");

			SSO_DBNAME=prop.getProperty("SSO_DBNAME");
			SSO_DBUSERNAME=prop.getProperty("SSO_DBUSERNAME");
			SSO_DBPASSWORD=prop.getProperty("SSO_DBPASSWORD");
			SSO_DBIP=prop.getProperty("SSO_DBIP");
			SSO_DBPORT=prop.getProperty("SSO_DBPORT");
			System.out.println("*-------UpStream Env Properties Loaded-------*");
		} catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		}

	}
}