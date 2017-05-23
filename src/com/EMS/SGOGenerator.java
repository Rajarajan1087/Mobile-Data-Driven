package com.EMS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.SharedModules.DbUtilities;
import com.SharedModules.LLUProvision;
import com.SharedModules.NameGenerator;
import com.Stubs.StubFilePlacing;
import com.Stubs.StubFilePlacing.StubType;
import com.Utils.Reusables;


public class SGOGenerator {
	public Reporter Report;
	public SGOGenerator(Reporter report) {
		Report=report;
	}
	public SGOGenerator() {
	}
	public String generateSGO(String PackageID, String CoreProducts, String Orderlines, String FNAME, String SNAME, String CLI,
			boolean BookAppointment,boolean UPFrontPayment) throws Exception{
		
	String UPfront = "<ord:Attributes>"
			+ " <ord:Attribute> "
			+ " <ord:name>totalUpfrontPaymentAmount</ord:name> "
			+ " <ord:value>126.00</ord:value> "
			+ " </ord:Attribute>"
			+ " <ord:Attribute>"
			+ " <ord:name>upfrontPaymentAuthCode</ord:name>"
			+ " <ord:value>tst323</ord:value>"
			+ " </ord:Attribute>"
			+ " <ord:Attribute>"
			+ " <ord:name>networkResultsReference</ord:name>"
			+ " <ord:value>1400000000</ord:value>"
			+ " </ord:Attribute>"
			+ " </ord:Attributes>";
	String UPFront2 = "<ord:OrderLine>"
			+ " <ord:productId>18208</ord:productId> "
			+ " <ord:sgoProductType>PROPOSITION</ord:sgoProductType>"
			+ " <ord:Attributes> "
			+ " <ord:Attribute>"
			+ " <ord:name>upfrontPaymentTaken</ord:name>"
			+ " <ord:value>TRUE</ord:value>"
			+ " </ord:Attribute>"
			+ " </ord:Attributes>"
			+ " </ord:OrderLine>";
			
String SGO = null;


String SGO1 = FileReader(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SGO1.txt");
String SGO2 = FileReader(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SGO2.txt");
String SGO3 = FileReader(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SGO3.txt");
String Up = FileReader(System.getProperty("user.dir")+"\\ProvisioningTemplates\\Upfrontpayment.txt");

//System.out.println("SGO1-----------------------------------\n"+SGO1);
//System.out.println("SGO2-----------------------------------\n"+SGO2);
SGO=SGO1;

if(UPFrontPayment)
	SGO = SGO + Up ;

SGO = SGO + SGO2;

if(UPFrontPayment)
	SGO = SGO + UPfront ;

SGO = SGO +"</ord:OrderLine>";


for(String Proposition : Orderlines.split(",") ){
	
	SGO=SGO+"<ord:OrderLine>"
			+ "<ord:productId>"+Proposition+"</ord:productId>"
			+ "<ord:sgoProductType>PROPOSITION</ord:sgoProductType>"
			+ "</ord:OrderLine>";
	SGO = SGO + "\n";
}
for(String Proposition : CoreProducts.split(",") ){
	
	SGO=SGO+"<ord:OrderLine>"
			+ "<ord:productId>"+Proposition+"</ord:productId>"
			+ "<ord:sgoProductType>PROPOSITION</ord:sgoProductType>"
			+ "</ord:OrderLine>";
	SGO = SGO + "\n";
	
}
SGO = SGO +"</ord:OrderLines>";

if(UPFrontPayment)
	SGO = SGO + UPFront2;

SGO = SGO + SGO3;


SGO = SGO.replaceAll("M_CLI", CLI);
SGO = SGO.replaceAll("M_DATE1", Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0));
SGO = SGO.replaceAll("M_DATE2", Reusables.getdateFormat("yyyy-MM-dd", 10));
SGO = SGO.replaceAll("M_PACKAGEID", PackageID);
SGO = SGO.replaceAll("M_ORDERID", "N"+CLI.substring(6));
SGO = SGO.replaceAll("M_FNAME", FNAME);
SGO = SGO.replaceAll("M_SNAME", SNAME);
SGO = SGO.replaceAll("M_CORRID", "099"+Reusables.getdateFormat("yyyyMMddHHmmss", 0));
SGO = SGO.replaceAll(" <ord:primaryStbProductId>1</ord:primaryStbProductId>", "");

//System.out.println(SGO);
FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\SGO_"+CLI+".txt");
writer.write(SGO);writer.flush();writer.close();
return SGO;

}
	public String FileReader(String Path) throws Exception{
		File file = new File(Path);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "", SGO = "";
		while((line = reader.readLine()) != null)
			SGO += line + "\r\n";
		reader.close();
		return SGO;
	}
	
	public String generateTVSGO_InitialSale_MI(String CLI, int TVAppointmentgap) throws Exception{
		//File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\263SGO_InitialSale_MI.txt");
		File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\263SGOT13.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "", SGO = "";
		while((line = reader.readLine()) != null)
			SGO += line + "\r\n";
		reader.close();
		
	//	String CLI = NameGenerator.randomCLI(9);
		String Fname=NameGenerator.randomName(6);
		String Sname=NameGenerator.randomName(6);
		
		System.out.println("First Name is -> "+Fname+"  Last Name is -> "+Sname);
		
		SGO = SGO.replaceAll("M_env",LoadEnvironment.ENV );
		SGO = SGO.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
		SGO = SGO.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
		SGO = SGO.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
		SGO = SGO.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
		
		//String NLCLI = "NL"+CLI.substring(7);
		String OrderNO = "N"+CLI.substring(4);
		LLUProvision LLU=new LLUProvision(Report);
		String NRR=LLU.getNRR();
		
		SGO = SGO.replaceAll("M_CLI", CLI);
		SGO = SGO.replaceAll("M_SUPPLIERORDERID", OrderNO);
		SGO = SGO.replaceAll("M_NETWORKRESULTREFERNCE", NRR);
		SGO = SGO.replaceAll("M_DATETIME", Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss",0));
		SGO = SGO.replaceAll("M_DATE", Reusables.getdateFormat("yyyy-MM-dd",0));
		SGO = SGO.replaceAll("M_BTDATE", Reusables.getdateFormat("yyyy-MM-dd",2)+"T");
		SGO = SGO.replaceAll("M_FNAME", Fname);
		SGO = SGO.replaceAll("M_LNAME", Sname);
		
		//SGO = SGO.replaceAll("M_APPTID", "1"+NameGenerator.randomCLI(9).substring(0, 5));
		
		
		
		String response = BookAppointment.BookAppt(TVAppointmentgap);
		
	
		SGO = SGO.replaceAll("M_APPTID", Reusables.getXMLdata(response, "ns0:AppointmentDetails", "ns1:appointmentId"));
		SGO = SGO.replaceAll("M_APPDATE",  Reusables.getXMLdata(response, "ns0:AppointmentDetails", "ns1:appointmentDate"));
		SGO = SGO.replaceAll("M_SLOTSTART",  Reusables.getXMLdata(response, "ns0:AppointmentDetails", "ns1:appointmentSlotStartTime"));
		SGO = SGO.replaceAll("M_SLOTEND",  Reusables.getXMLdata(response, "ns0:AppointmentDetails", "ns1:appointmentSlotEndTime"));
	
		
		FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SGO"+".txt");
		writer.write(SGO);writer.flush();writer.close();
	//	MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SGO"+".txt");
		
		return SGO;
	}
	public String generateTVFibreSGO_NL_Porting(int TVAppointmentgap) throws Exception{
		File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\BSS3_NLTVFibreSISGO_Porting.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "", SGO = "";
		while((line = reader.readLine()) != null)
			SGO += line + "\r\n";
		reader.close();
		
		String CLI = NameGenerator.randomCLI(9);
		String Fname=NameGenerator.randomName(6);
		String Sname=NameGenerator.randomName(6);
		
		System.out.println("First Name is -> "+Fname+"  Last Name is -> "+Sname);
		
		SGO = SGO.replaceAll("M_env",LoadEnvironment.ENV );
		SGO = SGO.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
		SGO = SGO.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
		SGO = SGO.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
		SGO = SGO.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
		
		String NLCLI = "NL"+CLI.substring(7);
		String OrderNO = "N"+CLI.substring(4);
		LLUProvision LLU=new LLUProvision(Report);
		String NRR=LLU.getNRR();
		
		SGO = SGO.replaceAll("M_NLCLI", NLCLI);
		SGO = SGO.replaceAll("M_NRR", NRR);
		SGO = SGO.replaceAll("M_ORDERNO", OrderNO);
		SGO = SGO.replaceAll("M_PORTCLI", CLI);
		SGO = SGO.replaceAll("M_DATE", Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss",0));
		SGO = SGO.replaceAll("M_BTDATE", Reusables.getdateFormat("yyyy-MM-dd",2)+"T");
		SGO = SGO.replaceAll("M_FNAME", Fname);
		SGO = SGO.replaceAll("M_LNAME", Sname);
		
		SGO = SGO.replaceAll("M_BTAPPID", "1"+NameGenerator.randomCLI(9).substring(0, 5));
		
		String response = BookAppointment.BookAppt(TVAppointmentgap);
		
		SGO = SGO.replaceAll("M_TVAPPID", Reusables.getXMLdata(response, "ns0:AppointmentDetails", "ns1:appointmentId"));
		SGO = SGO.replaceAll("M_TVAPPDATE",  Reusables.getXMLdata(response, "ns0:AppointmentDetails", "ns1:appointmentDate"));
		SGO = SGO.replaceAll("M_SLOTSTART",  Reusables.getXMLdata(response, "ns0:AppointmentDetails", "ns1:appointmentSlotStartTime"));
		SGO = SGO.replaceAll("M_SLOTEND",  Reusables.getXMLdata(response, "ns0:AppointmentDetails", "ns1:appointmentSlotEndTime"));
	
		FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_TVFiberSGO"+".txt");
		writer.write(SGO);writer.flush();writer.close();
		MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_TVFiberSGO"+".txt");
		
		//return NLCLI+","+Fname+","+Sname;
		return NLCLI;
		
	}
	
	public String generateTVSGO_NL_Porting(int TVAppointmentgap) throws Exception{
		File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\TRIO14_NLTVSGO_Porting.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "", SGO = "";
		while((line = reader.readLine()) != null)
			SGO += line + "\r\n";
		reader.close();
		
		String CLI = NameGenerator.randomCLI(9);
		String Fname=NameGenerator.randomName(6);
		String Sname=NameGenerator.randomName(6);
		
		System.out.println("First Name is -> "+Fname+"  Last Name is -> "+Sname);
		
		SGO = SGO.replaceAll("M_env",LoadEnvironment.ENV );
		SGO = SGO.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
		SGO = SGO.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
		SGO = SGO.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
		SGO = SGO.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
		
		String NLCLI = "NL"+CLI.substring(7);
		String OrderNO = "N"+CLI.substring(4);
		LLUProvision LLU=new LLUProvision(Report);
		String NRR=LLU.getNRR();
		
		SGO = SGO.replaceAll("M_NLCLI", NLCLI);
		SGO = SGO.replaceAll("M_NRR", NRR);
		SGO = SGO.replaceAll("M_ORDERNO", OrderNO);
		SGO = SGO.replaceAll("M_PORTCLI", CLI);
		SGO = SGO.replaceAll("M_DATE", Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss",0));
		SGO = SGO.replaceAll("M_BTDATE", Reusables.getdateFormat("yyyy-MM-dd",2)+"T");
		SGO = SGO.replaceAll("M_FNAME", Fname);
		SGO = SGO.replaceAll("M_LNAME", Sname);
		
		SGO = SGO.replaceAll("M_BTAPPID", "1"+NameGenerator.randomCLI(9).substring(0, 5));
		
		String response = BookAppointment.BookAppt(TVAppointmentgap);
		
		SGO = SGO.replaceAll("M_TVAPPID", Reusables.getXMLdata(response, "ns0:AppointmentDetails", "ns1:appointmentId"));
		SGO = SGO.replaceAll("M_TVAPPDATE",  Reusables.getXMLdata(response, "ns0:AppointmentDetails", "ns1:appointmentDate"));
		SGO = SGO.replaceAll("M_SLOTSTART",  Reusables.getXMLdata(response, "ns0:AppointmentDetails", "ns1:appointmentSlotStartTime"));
		SGO = SGO.replaceAll("M_SLOTEND",  Reusables.getXMLdata(response, "ns0:AppointmentDetails", "ns1:appointmentSlotEndTime"));
	
		FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+NLCLI+"_SGOTVPorting"+".txt");
		writer.write(SGO);writer.flush();writer.close();
		MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+NLCLI+"_SGOTVPorting"+".txt");
		
		//return NLCLI+","+Fname+","+Sname;
		return NLCLI;
		
	}
	public String generate285MultipleSGO_NL_Porting(int TVAppointmentgap,int No_OfCustomer) throws Exception{
		String NLCLIs="";
		String Fname=NameGenerator.randomName(6);
		String Sname=NameGenerator.randomName(6);
		String Email=Fname+"."+Sname;
		for(int i=0;i<No_OfCustomer;i++)
		{
			Email=Email+i;
			System.out.println(Email);
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SGO_285Porting.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", SGO = "";
			while((line = reader.readLine()) != null)
				SGO += line + "\r\n";
			reader.close();
		
			System.out.println("First Name is -> "+Fname+"  Last Name is -> "+Sname);
		
			SGO = SGO.replaceAll("M_env",LoadEnvironment.ENV );
			SGO = SGO.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
			SGO = SGO.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
			SGO = SGO.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
			SGO = SGO.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
			
			String CLI = NameGenerator.randomCLI(9);
			String NLCLI = "NL"+CLI.substring(7);
			NLCLIs+=","+NLCLI;
			String OrderNO = "N"+CLI.substring(4);
			LLUProvision LLU=new LLUProvision(Report);
			String NRR=LLU.getNRR();
		
			SGO = SGO.replaceAll("M_NLCLI", NLCLI);
			System.out.println(NLCLI);
			SGO = SGO.replaceAll("M_NRR", NRR);
			SGO = SGO.replaceAll("M_ORDERNO", OrderNO);
			SGO = SGO.replaceAll("M_PORTCLI", CLI);
			SGO = SGO.replaceAll("M_DATE2", Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss",15));
			SGO = SGO.replaceAll("M_DATE1", Reusables.getdateFormat("yyyy-MM-dd",15));
			SGO = SGO.replaceAll("M_DATE", Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss",0));
			SGO = SGO.replaceAll("M_BTDATE", Reusables.getdateFormat("yyyy-MM-dd",15)+"T");
			SGO = SGO.replaceAll("M_FNAME", Fname);
			SGO = SGO.replaceAll("M_LNAME", Sname);
			SGO = SGO.replaceAll("M_EMAIL", Sname);
		
			SGO = SGO.replaceAll("M_BTAPPID", "1"+NameGenerator.randomCLI(9).substring(0, 5));
		
			FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SGO"+".txt");
			writer.write(SGO);writer.flush();writer.close();
			MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SGO"+".txt");
			//return NLCLI+","+Fname+","+Sname;
			DbUtilities DBU=new DbUtilities(Report);
			DBU.WaitforOrderStatus(NLCLI, "", "2", false);
		}
		return NLCLIs.substring(1);
		
	}
	public String generate285SGO_NL_Porting(int TVAppointmentgap,Boolean... AppTime) throws Exception{
		int CurrentDate=0;
		if(AppTime.length>0)
		{
			if(AppTime[0])
			{
				CurrentDate=CurrentDate-TVAppointmentgap-1;
			}
		}
		LLUProvision LLU=new LLUProvision(Report);
		String NRR=LLU.getNRR();
		DbUtilities d=new DbUtilities(Report);
		
		File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SGO_285Porting.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "", SGO = "";
		while((line = reader.readLine()) != null)
			SGO += line + "\r\n";
		reader.close();
		
		String CLI = NameGenerator.randomCLI(9);
		String Fname=NameGenerator.randomName(6);
		String Sname=NameGenerator.randomName(6);
		
		System.out.println("First Name is -> "+Fname+"  Last Name is -> "+Sname);
		
		SGO = SGO.replaceAll("M_env",LoadEnvironment.ENV );
		SGO = SGO.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
		SGO = SGO.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
		SGO = SGO.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
		SGO = SGO.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
		
		String NLCLI = "NL"+CLI.substring(7);
		String OrderNO = "N"+CLI.substring(4);
		
		SGO = SGO.replaceAll("M_NLCLI", NLCLI);
		SGO = SGO.replaceAll("M_NRR", NRR);
		SGO = SGO.replaceAll("M_ORDERNO", OrderNO);
		SGO = SGO.replaceAll("M_PORTCLI", CLI);
		SGO = SGO.replaceAll("M_DATE2", Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss",CurrentDate+TVAppointmentgap));
		SGO = SGO.replaceAll("M_DATE1", Reusables.getdateFormat("yyyy-MM-dd",CurrentDate+TVAppointmentgap));
		SGO = SGO.replaceAll("M_DATE", Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss",CurrentDate));
		SGO = SGO.replaceAll("M_BTDATE", Reusables.getdateFormat("yyyy-MM-dd",CurrentDate+TVAppointmentgap)+"T");
		SGO = SGO.replaceAll("M_FNAME", Fname);
		SGO = SGO.replaceAll("M_LNAME", Sname);
		
		SGO = SGO.replaceAll("M_BTAPPID", "1"+NameGenerator.randomCLI(9).substring(0, 5));
		
		FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SGO"+".txt");
		writer.write(SGO);writer.flush();writer.close();
		MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SGO"+".txt");
		
		//return NLCLI+","+Fname+","+Sname;
		return NLCLI;
//		return null;
		
	}
	
	public String generate287SGO_NL(int TVAppointmentgap) throws Exception{
		File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SGO_287NewLine.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "", SGO = "";
		while((line = reader.readLine()) != null)
			SGO += line + "\r\n";
		reader.close();
		String ALK="A00030177127";
		StubFilePlacing SF=new StubFilePlacing(Report);
		SF.PlaceFile(StubType.NPAC_LLU_ALK_BTLive, ALK);
		String CLI = NameGenerator.randomCLI(9);
		String Fname=NameGenerator.randomName(6);
		String Sname=NameGenerator.randomName(6);
		
//		String response = BookAppointment.BookAppt(TVAppointmentgap);
		
		System.out.println("First Name is -> "+Fname+"  Last Name is -> "+Sname);
		
		SGO = SGO.replaceAll("M_env",LoadEnvironment.ENV );
		SGO = SGO.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
		SGO = SGO.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
		SGO = SGO.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
		SGO = SGO.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
		
		String NLCLI = "NL"+CLI.substring(7);
		String OrderNO = "N"+CLI.substring(4);
		LLUProvision LLU=new LLUProvision(Report);
		String NRR=LLU.getNRR();
		
		SGO = SGO.replaceAll("M_NLCLI", NLCLI);
		SGO = SGO.replaceAll("M_NRR", NRR);
		SGO = SGO.replaceAll("M_ORDERNO", OrderNO);
		SGO = SGO.replaceAll("M_PORTCLI", CLI);
		SGO = SGO.replaceAll("M_DATE2", Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss",15));
		SGO = SGO.replaceAll("M_DATE1", Reusables.getdateFormat("yyyy-MM-dd",15));
		SGO = SGO.replaceAll("M_DATE3", Reusables.getdateFormat("yyyy-MM-dd",22));
		SGO = SGO.replaceAll("M_DATE", Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss",0));
		SGO = SGO.replaceAll("M_BTDATE", Reusables.getdateFormat("yyyy-MM-dd",23)+"T");
		SGO = SGO.replaceAll("M_FNAME", Fname);
		SGO = SGO.replaceAll("M_LNAME", Sname);
		SGO = SGO.replaceAll("M_ALK", ALK);
		
		SGO = SGO.replaceAll("M_BTAPPID", NameGenerator.randomNumber("9", 4));
//		SGO = SGO.replaceAll("M_BTAPPID", Reusables.getXMLdata(response, "ns0:AppointmentDetails", "ns1:appointmentId"));
//		SGO = SGO.replaceAll("M_BTDATE",  Reusables.getXMLdata(response, "ns0:AppointmentDetails", "ns1:appointmentDate"));
		
		FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+NLCLI+"_SGO"+".txt");
		writer.write(SGO);writer.flush();writer.close();
		MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+NLCLI+"_SGO"+".txt");
		
		//return NLCLI+","+Fname+","+Sname;
		return NLCLI;
		
	}
	
	public String generateTVSGO_InitialSale(String CLI, int TVAppointmentgap,boolean MI_SI) throws Exception{
		
		File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SGOTV263MI.txt");
		if(!MI_SI)
		{
			file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SGOTV263SI.txt");
		}
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "", SGO = "";
		while((line = reader.readLine()) != null)
			SGO += line + "\r\n";
		reader.close();
		
		String Fname=NameGenerator.randomName(6);
		String Sname=NameGenerator.randomName(6);
		String Streetname=NameGenerator.randomName(6);
		String H_Num=NameGenerator.randomCLI(3).substring(1);
		
		System.out.println("First Name is -> "+Fname+"  Last Name is -> "+Sname);
		LLUProvision LLU=new LLUProvision(Report);
		String NRR=LLU.getNRR();
		
		SGO = SGO.replaceAll("M_env",LoadEnvironment.ENV );
		SGO = SGO.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
		SGO = SGO.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
		SGO = SGO.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
		SGO = SGO.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
		
		SGO = SGO.replaceAll("M_cli", CLI);
		SGO = SGO.replaceAll("M_date1", Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0));
		SGO = SGO.replaceAll("M_H_NO", H_Num);
		SGO = SGO.replaceAll("M_crd", Reusables.getdateFormat("yyyy-MM-dd",16));
		SGO = SGO.replaceAll("M_addr_line", Streetname);
		SGO = SGO.replaceAll("M_town", "LONDON");
		SGO = SGO.replaceAll("M_pst1_pst2", "CV34 6TE");
		SGO = SGO.replaceAll("M_nrr", NRR);
		SGO = SGO.replaceAll("M_apptid", "1"+NameGenerator.randomCLI(9).substring(0, 4));
		SGO = SGO.replaceAll("M_appt_date", Reusables.getdateFormat("yyyy-MM-dd",23));
		SGO = SGO.replaceAll("M_first_name", Fname);
		SGO = SGO.replaceAll("M_last_name", Sname);
		SGO = SGO.replaceAll("M_email_id", Fname+Sname+"@cpwplc.com");
		SGO = SGO.replaceAll("M_dob", "1984-09-09");
	
		FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SGOTV263"+".txt");
		writer.write(SGO);writer.flush();writer.close();
		MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SGOTV263"+".txt");
		return SGO;
		
	}
	
public String generate285SGO_Switcher(String CLI,String... Postcode) throws Exception{
		String Str_Postcode;
		System.out.println("Switcher 285");
	if(Postcode.length<1)
	{
		Str_Postcode="W114AR";
	}
	else
	{
		Str_Postcode=Postcode[0];
	}
		File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SGO_285.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "", SGO = "";
		while((line = reader.readLine()) != null)
			SGO += line + "\r\n";
		reader.close();
		
		String OrderNO = "N"+CLI.substring(4);
		String Fname=NameGenerator.randomName(6);
		String Sname=NameGenerator.randomName(6);
		System.out.println("First Name is -> "+Fname+"  Last Name is -> "+Sname);
		LLUProvision LLU=new LLUProvision(Report);
		String NRR=LLU.getNRR();
		
		SGO = SGO.replaceAll("M_env",LoadEnvironment.ENV );
		SGO = SGO.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
		SGO = SGO.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
		SGO = SGO.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
		SGO = SGO.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
		
		SGO = SGO.replaceAll("M_CLI", CLI);
		SGO = SGO.replaceAll("M_PostCode", Str_Postcode);
		SGO = SGO.replaceAll("M_DATE", Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss",0));
		SGO = SGO.replaceAll("M_NRR", NRR);
		SGO = SGO.replaceAll("M_ORDERNO", OrderNO);
		SGO = SGO.replaceAll("M_FNAME", Fname);
		SGO = SGO.replaceAll("M_LNAME", Sname);
	
		FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SGOSwitcher286"+".txt");
		writer.write(SGO);writer.flush();writer.close();
		MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SGOSwitcher286"+".txt");
		return SGO;
		
	}

public String generate263SGO_SwitcherSI(String CLI,String... Postcode) throws Exception{
	String Str_Postcode;
	System.out.println("Switcher 263 Self Install");
if(Postcode.length<1)
{
	Str_Postcode="W114AR";
}
else
{
	Str_Postcode=Postcode[0];
}
	File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SGO_263SI.txt");
	BufferedReader reader = new BufferedReader(new FileReader(file));
	String line = "", SGO = "";
	while((line = reader.readLine()) != null)
		SGO += line + "\r\n";
	reader.close();
	
	String OrderNO = "N"+CLI.substring(4);
	String Fname=NameGenerator.randomName(6);
	String Sname=NameGenerator.randomName(6);
	System.out.println("First Name is -> "+Fname+"  Last Name is -> "+Sname);
	LLUProvision LLU=new LLUProvision(Report);
	String NRR=LLU.getNRR();
	
	SGO = SGO.replaceAll("M_env",LoadEnvironment.ENV );
	SGO = SGO.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
	SGO = SGO.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
	SGO = SGO.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
	SGO = SGO.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
	
	SGO = SGO.replaceAll("M_CLI", CLI);
	SGO = SGO.replaceAll("M_PostCode", Str_Postcode);
	SGO = SGO.replaceAll("M_DATE", Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss",0));
	SGO = SGO.replaceAll("M_NRR", NRR);
	SGO = SGO.replaceAll("M_ORDERNO", OrderNO);
	SGO = SGO.replaceAll("M_FNAME", Fname);
	SGO = SGO.replaceAll("M_LNAME", Sname);

	FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SGOTV263"+".txt");
	writer.write(SGO);writer.flush();writer.close();
	MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SGOTV263"+".txt");
	return SGO;
	
}

	public String generateMPFSGO_InitialSale(String CLI) throws Exception{

		File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SGOMPF.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "", SGO = "";
		while((line = reader.readLine()) != null)
			SGO += line + "\r\n";
		reader.close();

		String 	date = null;
		date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

		String Fname=NameGenerator.randomName(6);
		String Sname=NameGenerator.randomName(6);
//		String Streetname=NameGenerator.randomName(6);
//		String H_Num=NameGenerator.randomCLI(3).substring(1);

		System.out.println("First Name is -> "+Fname+"  Last Name is -> "+Sname);

		SGO = SGO.replaceAll("M_env",LoadEnvironment.ENV );
		SGO = SGO.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
		SGO = SGO.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
		SGO = SGO.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
		SGO = SGO.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

		SGO = SGO.replaceAll("M_CLI", CLI);
		SGO = SGO.replaceAll("M_date", date);
		SGO = SGO.replaceAll("M_first_name", Fname);
		SGO = SGO.replaceAll("M_last_name", Sname);		

//		SGO = SGO.replaceAll("M_H_NO", H_Num);
//		SGO = SGO.replaceAll("M_crd", Reusables.getdateFormat("yyyy-MM-dd",16));
//		SGO = SGO.replaceAll("M_addr_line", Streetname);
//		SGO = SGO.replaceAll("M_town", "LONDON");
//		SGO = SGO.replaceAll("M_pst1_pst2", "CV34 6TE");
//		SGO = SGO.replaceAll("M_nrr", NRR);
//		SGO = SGO.replaceAll("M_apptid", "1"+NameGenerator.randomCLI(9).substring(0, 4));
//		SGO = SGO.replaceAll("M_appt_date", Reusables.getdateFormat("yyyy-MM-dd",23));	
//		SGO = SGO.replaceAll("M_email_id", Fname+Sname+"@cpwplc.com");
//		SGO = SGO.replaceAll("M_dob", "1984-09-09");
//	
		FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"SGOMPF"+".txt");
		writer.write(SGO);writer.flush();writer.close();
		MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"SGOMPF"+".txt");
		return SGO;

	}
}
