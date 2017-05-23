/*Test Scenario 		: m.Sales
 *Test Case Names 		: SBBNewlinePostCodeOnly
 *Package				: m.Sales 
 *Created By			: Raja
 *Created on			: 18-05-2015
 */

package com.Utils;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.Engine.LoadEnvironment;
import com.Engine.AppiumSetup;
import com.SharedModules.Constants;
import com.SharedModules.DbUtilities;
import com.Support.Support;
import com.WebActions.WebActions;

public class Sample extends AppiumSetup implements Constants{
	
	/**Inputsheet Variable**/
	String InputSheet;

	/** Test Pre Processing	Q
	 * @param method
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@BeforeMethod(alwaysRun=true)
	public void myBeforeTestMethod(Method method) throws Exception {
		System.out.println(RunType);
		Report.testtype = RunType;				//Test type 
		Report.rownumber = 0;						//Start row in the Excel Report
		Report.testPassed = true;					// Default test result
		Report.result = true;						//Default test result
		Report.testname = this.getClass().toString();//Test class name
		Report.methodname = method.getName();		//Test method name
		//Report path name
		Report.filename = Report.methodname;

		strOutputFilePath = Report.absolutepath + "/Outputfiles/" + "/"
				+ Report.testtype + "/" + Report.filename;
		System.out.println(strOutputFilePath);
		//Screenshot path name
		Report.strOutputScreenShotsFilePath = Report.absolutepath
				+ "/Outputfiles" + "/" + Report.testtype + "/Screenshots/"
				+ Report.methodname;

		System.out.println("-------------------------------------[ START OF TEST ]-----------------------------------------");
	}

	@DataProvider(name="TS01")//Data Provider name
	public Object[][] DATA() throws Exception {
		Object[][] retObjArr = null ;
		try{
			System.out.println(LoadEnvironment.WB_Mobile_Standard);
			System.out.println(LoadEnvironment.Sheet_mSales);
			System.out.println("TS01");

			DataProviderExcelReader DXR = new DataProviderExcelReader();;	//Excel Reader 
			retObjArr= DXR.getExcelData(LoadEnvironment.WB_Mobile_Standard,LoadEnvironment.Sheet_mSales,"TS01");	//Get data object order = WB,SHT,SCRIP_ID
		}catch(Exception e){
			e.printStackTrace();
		}
		return retObjArr;
	}

	/** Test Methods
	 * @param Script_ID
	 * @param ROW
	 * @throws Exception
	 */
	@Test(groups = { "Automation" },dataProvider="TS01")//Data Provider name should be the same
	 
	public void TC01SBBNewlinePostCodeOnly(String Script_ID, String ROW) throws Exception {

		if(TestPreProcessing(ROW,LoadEnvironment.WB_Mobile_Standard,LoadEnvironment.Sheet_mSales))
		{
			ReadExcelSheet RX = new ReadExcelSheet(Report);
			RX.WriteToExcelWithRows(System.getProperty("user.dir")+"\\DataProvider\\DataSheet_Mobile_Standard.xls", "Good", "CLI", "2", "waste");
/************************* Declaring Variables *****************************************/
		
			/*String Str_SalesType=RX.ReadFromExcelWithRows(LoadEnvironment.WB_Mobile_Standard,LoadEnvironment.Sheet_mSales, ROW, "SALESTYPE");
			String Str_PostCode=RX.ReadFromExcelWithRows(LoadEnvironment.WB_Mobile_Standard,LoadEnvironment.Sheet_mSales, ROW, "POSTCODE");
			String Str_Qualifier=RX.ReadFromExcelWithRows(LoadEnvironment.WB_Mobile_Standard,LoadEnvironment.Sheet_mSales, ROW, "QUALIFIER");
			String PrivacyFeatures=RX.ReadFromExcelWithRows(LoadEnvironment.WB_Mobile_Standard,LoadEnvironment.Sheet_mSales, ROW, "PRIVACYFEATURES");
			String CallBoosts=RX.ReadFromExcelWithRows(LoadEnvironment.WB_Mobile_Standard,LoadEnvironment.Sheet_mSales, ROW, "CALLBOOSTS");
			String TVBoosts=RX.ReadFromExcelWithRows(LoadEnvironment.WB_Mobile_Standard,LoadEnvironment.Sheet_mSales, ROW, "TVBOOSTS");
			String Str_PackageName=RX.ReadFromExcelWithRows(LoadEnvironment.WB_Mobile_Standard,LoadEnvironment.Sheet_mSales, ROW, "PACKAGENAME");
			String Str_SIMType=RX.ReadFromExcelWithRows(LoadEnvironment.WB_Mobile_Standard,LoadEnvironment.Sheet_mSales, ROW, "SIMTYPE");
			String Str_SIMSize=RX.ReadFromExcelWithRows(LoadEnvironment.WB_Mobile_Standard,LoadEnvironment.Sheet_mSales, ROW, "SIMSIZE");
			WebActions.Package=Str_PackageName;
			String []PersonalDetails=new String[7];
			String NLCLI="";
			FibreType FT=FibreType.none;
			if(Str_PackageName.contains("Fibre"))
			{
				if(RX.ReadFromExcelWithRows(LoadEnvironment.WB_Mobile_Standard,LoadEnvironment.Sheet_mSales, ROW, "FIBRETYPE").equalsIgnoreCase("Large"))
					{FT=FibreType.Large;}
				else{FT=FibreType.Medium;}
			}
			
*//************************* Instantiating Classes *****************************************//*
		
			Support ST=new Support(driver,Report);
			mSales CS=new mSales(driver,Report);
			DbUtilities DbU=new DbUtilities(Report);
		
*//************************* Stubbing New AddressKey *****************************************//*
			
			String[] Addresskey=ST.getNewAddressKey(Str_SalesType,Str_PostCode, Str_Qualifier);

*//************************* Main Test *****************************************//*
		
			CS.MainScreen();
			CS.getPackageScreen();
			CS.PostCodeScreen(Str_PostCode);
			CS.SelectAddressScreen(Addresskey[1],true,InternetServiceProvider.none, true, 7);
			CS.GreatNewsScreen(FT,Str_SalesType);
			CS.SIMProvideScreen(Str_SIMType, Str_SIMSize);
			CS.PackageDetailsScreenBasic(PrivacyFeatures, CallBoosts,TVBoosts,false);
			PersonalDetails=CS.PersonalDetailsScreen(Addresskey[1]+",");
			CS.PaymentScreen();
			CS.HomeSafeScreen();
			NLCLI="NL"+CS.ConfirmationScreen(PersonalDetails[3], (PrivacyFeatures+","+CallBoosts).split(","));
			
			DbU.WaitforOrderStatus(NLCLI, "13", "2",true);*/
			
/*************************END of Main Test*****************************************/
			Report.reportTest();
		}
	}
}