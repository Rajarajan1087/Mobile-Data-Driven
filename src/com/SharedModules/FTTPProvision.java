package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Stubs.StubFilePlacing;
import com.Stubs.StubFilePlacing.StubType;
import com.Utils.Reusables;

public class FTTPProvision implements Constants {
	public Reporter Report;

	/**
	 * @param report
	 */
	public FTTPProvision(Reporter report) {
		Report = report;
	}

	public void FTTPAmend (String FinalState, String Str_CLI, String Str_Account, int InitialState) throws Exception{

		String [] state = {FTTP_Amend_Accepted,FTTP_Amend_Validated,FTTP_Amend_Requested,FTTP_Amend_Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();

		try{

			String 	date =null;
			String uniqueNumber = null;

			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			uniqueNumber = NameGenerator.randomCPWNRef(6);
			
			DBU.getOrderDetails(Str_CLI, "FTTPAmend", ODB);
			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);

			for(int i =InitialState; i<4;i++){

				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\FTTPAmend_"+state[i]+".txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\FTTPAmend_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "",template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();
				oldtext=template;

				String newtext = template.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_OrderID",ODB.getORDERID());
				newtext = newtext.replaceAll("M_StateID",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_CommandID",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_ResellerReference",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_ResellerAccountReference",Str_Account);
				newtext = newtext.replaceAll("M_TargetCommandID",uniqueNumber);

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+Str_CLI+"_FTTPAmend_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+Str_CLI+"_FTTPAmend_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			  }

			Report.fnReportPass("FTTP Amend Provisioning complete for "+ Str_CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("FTTP Amend genration", "FTTP Amend file generation error " + e.getMessage());

		}finally{

		}
	}


	public void FTTPCancel (String FinalState, String Str_CLI, String Str_Account, String Reason, int InitialState) throws Exception{
		String [] state = {FTTPCancel_Accepted,FTTPCancel_Validated,FTTPCancel_Requested,FTTPCancel_Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{

			String 	date = null;
			String uniqueNumber = null;

			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			uniqueNumber = NameGenerator.randomCPWNRef(6);

			DBU.getOrderDetails(Str_CLI, "FTTPCancel", ODB);
			String CPWNRef = Reusables.getdateFormat("hhmmss", 0);
//			DBU.SM_SKID_CPWRef_CASR(ODB);
						
			if((InitialState==0)){			
				DBU.getOrderDetails(Str_CLI, "FTTPCancel", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);

			}else{
				DBU.getOrderDetails(Str_CLI, "FTTPCancel", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}
			
			for(int i =0; i<4;i++){

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\FTTPCancel_"+state[i]+".txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\FTTPCancel_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "",template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_STATEID",state[i]);
				newtext = newtext.replaceAll("M_DATETIME",date);
				newtext = newtext.replaceAll("M_COMMANDID",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_RESELLERREFERENCE",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_RESELLERACCOUNTREFERENCE", Str_Account);
				newtext = newtext.replaceAll("M_TargetCommandID",uniqueNumber);
				newtext = newtext.replaceAll("M_REASON",Reason);
				newtext = newtext.replaceAll("M_ServiceResellerId",ODB.getServiceresellerid());
				

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+Str_CLI+"_FTTPCancel_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+Str_CLI+"_FTTPCancel_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("FTTPCancel_ Provisioning complete for "+ Str_CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("FTTPCancel_ file genration", "FTTPCancel_ file generation error " + e.getMessage());

		}finally{

		}
	}  
	
public void FTTPSuspend_(String FinalState, String CLI, String Account, int InitialState) throws Exception{
		
		String [] state = {FTTP_Suspend_Accepted,FTTP_Suspend_Validated,FTTP_Suspend_Completed};
		  
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();
		String CASR="FTTP"+NameGenerator.randomCPWNRef(6);

		try{
		
			String 	date =null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
					
			DBU.getOrderDetails(CLI, "FTTPSuspend", ODB);
			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
			
			StubFilePlacing SF = new StubFilePlacing();
			SF.PlaceFile(StubType.IPTVSuspendNew,CASR);
			
			for(int i =InitialState; i<3;i++){
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\FTTP_Suspend_"+state[i]+".txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\FTTP_Suspend_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();				
				
				System.out.println(template);
				
				String newtext = template.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_OrderID",ODB.getORDERID());
				System.out.println("Order Id :" +ODB.getORDERID());
				newtext = newtext.replaceAll("M_STATEID",state[i]);
				System.out.println("State Id :" +state[i]);
				newtext = newtext.replaceAll("M_DATE",date);
				newtext = newtext.replaceAll("M_CommandID",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_ResellerReference",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_ResellerAccountReference",Account);
				newtext = newtext.replaceAll("M_CASR",CASR);
				System.out.println("CASR Value :" +ODB.getCASR());
				newtext = newtext.replaceAll("M_ServiceResellerId",ODB.getServiceresellerid());
				System.out.println("Service Reseller Id :" +ODB.getServiceresellerid());
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_FTTP_Suspend_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_FTTP_Suspend_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);
				
				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			  }
			
			Report.fnReportPass("FTTP Suspend Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("FTTP Suspend genration", "FTTP Suspend file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void FTTPModify_(String FinalState, String CLI, String Account, int InitialState) throws Exception{
		
		String [] state = {FTTP_Modify_Accepted,FTTP_Modify_Validated,FTTP_Modify_ModifyPending,FTTP_Modify_Completed};
		  
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();
		StubFilePlacing SFP = new StubFilePlacing(Report);

		try{
		
			String 	date =null;
			String 	date1 =null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			date1 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 3);
					
			DBU.getOrderDetails(CLI, "FTTPModify", ODB);
			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
			
			SFP.PlaceFile(StubType.IPTVModifyNew, ODB.getCASR());
			
			for(int i =InitialState; i<4;i++){
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\FTTP_Modify_"+state[i]+".txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\FTTP_Modify_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();				
				
				String newtext = template.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_ORDERID",ODB.getORDERID());
				System.out.println("Order Id :" +ODB.getORDERID());
				newtext = newtext.replaceAll("M_STATEID",state[i]);
				System.out.println("State Id :" +state[i]);
				newtext = newtext.replaceAll("M_DATE",date);
				newtext = newtext.replaceAll("M_COMMANDID",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_RESELLERREFERENCE",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_RESELLERACCOUNTREFERENCE",Account);
				newtext = newtext.replaceAll("M_CASR",ODB.getCASR());
				System.out.println("CASR Value :" +ODB.getCASR());
				newtext = newtext.replaceAll("M_MODIFYDATE",date1);
				newtext = newtext.replaceAll("M_ServiceResellerId",ODB.getServiceresellerid());
				System.out.println("Service Reseller Id :" +ODB.getServiceresellerid());			
				
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_FTTP_Modify_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_FTTP_Modify_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);
				
				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			  }
			
			Report.fnReportPass("FTTP Modify Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("FTTP Modify genration", "FTTP Modify file generation error " + e.getMessage());

		}finally{

		}
	}



	public void FTTPCease_(String FinalState, String CLI, String Str_Account, int InitialState) throws Exception{
		
		String [] state = {FTTP_Cease_Accepted,FTTP_Cease_Validated,FTTP_Cease_CeasePending,FTTP_Cease_Completed};
		  
		OrderDetailsBean ODB = new OrderDetailsBean();
		CustomerBean CB = new CustomerBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();
		
		String uprn= DBU.UPRN(CLI);

		try{
		
			String 	date =null;
			String 	date1 =null;
			String 	date2 =null;
			String UniqueNumber = null;
			date = Reusables.getdateFormat("yyyy-MM-dd", 0);
			date1 = Reusables.getdateFormat("yyyy-MM-dd", 5);
			date2 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			UniqueNumber = Reusables.getdateFormat("yyhhmmss", 0);
					
			DBU.getOrderDetails(CLI, "FTTPCease", ODB);
			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
			
			for(int i =InitialState; i<4;i++){
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\FTTP_Cease_"+state[i]+".txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\FTTP_Cease_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();				
				
				//System.out.println(template);
				
				String newtext = template.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_ORDERID",ODB.getORDERID());
				System.out.println("Order Id :" +ODB.getORDERID());
				newtext = newtext.replaceAll("M_STATEID",state[i]);
				System.out.println("State Id :" +state[i]);
				newtext = newtext.replaceAll("M_STATEDATETIME",date2);
				newtext = newtext.replaceAll("M_COMMANDID",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_RESELLERREFERENCE",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_RESELLERACCOUNTREFERENCE",Str_Account);
				newtext = newtext.replaceAll("M_ALLOCATEDCLI",CLI);
				newtext = newtext.replaceAll("M_CASR",ODB.getCASR());
				System.out.println("CASR Value :" +ODB.getCASR());
				newtext = newtext.replaceAll("M_CUSTOMERAGREEDDATE",date1);
				newtext = newtext.replaceAll("M_EXPECTEDSERVICEACTIVATIONDATE",date1);
				newtext = newtext.replaceAll("M_ORIGINALLEADTIMEDATE",date1);
				newtext = newtext.replaceAll("M_CUSTOMERREQUIREDBYDATE",date1);
				newtext = newtext.replaceAll("M_ORIGINALLEADTIMEDATE",date1);
				newtext = newtext.replaceAll("M_CUSTOMERREQUIREDBYDATE",date1);
				
				System.out.println("over");
				newtext = newtext.replaceAll("M_UPRN",uprn);
				System.out.println(uprn);
				newtext = newtext.replaceAll("M_APPOINTMENTID",UniqueNumber);	
							
				newtext = newtext.replaceAll("M_ServiceResellerId",ODB.getServiceresellerid());
				System.out.println("Service Reseller Id :" +ODB.getServiceresellerid());			
				
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_FTTP_Cease_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_FTTP_Cease_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);
				
				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			  }
			
			Report.fnReportPass("FTTP Cease Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("FTTP Cease genration", "FTTP Cease file generation error " + e.getMessage());

		}finally{

		}
	}

	
	
	public void FTTPProvide_(String FinalState, String CLI, String Account) throws Exception{
		
		String [] state = {FTTP_Provide_Accepted,FTTP_Provide_Requested,FTTP_Provide_Acknowledged,FTTP_Provide_Coolingoff,FTTP_Provide_Completed};
		  
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();
		CustomerBean CB = new CustomerBean();
		StubFilePlacing SFP = new StubFilePlacing(Report);		

		try{
		
			String 	date =null,date2=null,Leaddate=null,uprn = null,CASR=null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			date2 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 2);
			Leaddate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 4);
			CASR="FTTP"+NameGenerator.randomCPWNRef(4);
			
			uprn = DBU.UPRN(CLI);
			System.out.println("uprn --> "+uprn);
			
			DBU.getOrderDetails(CLI, "FTTPProvide", ODB);
			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
			
			SFP.PlaceFile(StubType.IPTVProvideNew, ODB.getCASR());
			
			for(int i =0; i<5;i++){
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\FTTP_Provide_"+state[i]+".txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\FTTP_Provide_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();
				
				
		 String newtext = template.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_APPOINTMENTID",AB.getAPPOINTMENTID());
				newtext = newtext.replaceAll("M_APPOINTMENTSLOTENDTIME",AB.getSLOTENDTIME());
				newtext = newtext.replaceAll("M_APPOINTMENTSLOTSTARTTIME",AB.getSLOTSTARTTIME());
				newtext = newtext.replaceAll("M_COMMANDID",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_DATE2",date2);
				newtext = newtext.replaceAll("M_DATE",date);
				newtext = newtext.replaceAll("M_FNAME",NameGenerator.randomName(6));
				newtext = newtext.replaceAll("M_SNAME",NameGenerator.randomName(6));
				newtext = newtext.replaceAll("M_ORGINALLEADTIME",Leaddate);
				newtext = newtext.replaceAll("M_STATEID",state[i]);
				newtext = newtext.replaceAll("M_Resellerreference",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_ResellerAccountreference",Account);
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_UPRN",uprn);			
				System.out.println("uprn --> "+uprn);
				newtext = newtext.replaceAll("M_CASR",CASR);				
				
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_FTTP_Provide_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_FTTP_Provide_"+state[i]+".txt");
			Thread.sleep(PROV_TIME);
				
				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			  }
			
			Report.fnReportPass("FTTP Provide Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("FTTP Provide genration", "FTTP Provide file generation error " + e.getMessage());

		}finally{

		}
	}
	
public void FTTPProvide_(String CLI, String Account, String StatusCode, String ClarificationCode) throws Exception{
				  
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();
		CustomerBean CB = new CustomerBean();
		StubFilePlacing SFP = new StubFilePlacing(Report);		

		try{
		
			String 	date =null,date2=null,Leaddate=null,uprn = null,CASR=null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			date2 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 2);
			Leaddate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 4);
	 //		CASR="FTTP"+NameGenerator.randomCPWNRef(4);
			
			uprn = DBU.UPRN(CLI);
			System.out.println("uprn --> "+uprn);
			
			DBU.getOrderDetails(CLI, "FTTPProvide", ODB);
			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
			CASR=ODB.getCASR();
			System.out.println("CASR VALUE--------->"+CASR);
			
		//	SFP.PlaceFile(StubType.IPTVProvideNew, ODB.getCASR());
			
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\FTTPProvide_Completed.txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\FTTPProvide_Completed.txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();
				
				
		 String newtext = template.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_APPOINTMENTID",AB.getAPPOINTMENTID());
				newtext = newtext.replaceAll("M_APPOINTMENTSLOTENDTIME",AB.getSLOTENDTIME());
				newtext = newtext.replaceAll("M_APPOINTMENTSLOTSTARTTIME",AB.getSLOTSTARTTIME());
				newtext = newtext.replaceAll("M_COMMANDID",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_DATE2",date2);
				newtext = newtext.replaceAll("M_DATE",date);
				newtext = newtext.replaceAll("M_FNAME",NameGenerator.randomName(6));
				newtext = newtext.replaceAll("M_SNAME",NameGenerator.randomName(6));
				newtext = newtext.replaceAll("M_ORGINALLEADTIME",Leaddate);
				newtext = newtext.replaceAll("M_Resellerreference",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_ResellerAccountreference",Account);
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_UPRN",uprn);			
				System.out.println("uprn --> "+uprn);
				newtext = newtext.replaceAll("M_CASR",CASR);		
				newtext = newtext.replaceAll("M_StatusCode",StatusCode);
				newtext = newtext.replaceAll("M_ClarificationCode",ClarificationCode);
								
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"FTTPProvide_Completed.txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"FTTPProvide_Completed.txt");
			Thread.sleep(PROV_TIME);
				
			Report.fnReportPass("FTTP Provide Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("FTTP Provide generation", "FTTP Provide file generation error " + e.getMessage());

		}finally{

		}
	}
	
	
	public void FTTP_UNC_AOD(String FinalState, String CLI, String Str_Account, int InitialState) throws Exception{
		
		String [] state = {FTTP_UNC_Acknowledged, FTTP_UNC_CeasePending, FTTP_UNC_Completed};
		  
		OrderDetailsBean ODB = new OrderDetailsBean();
		CustomerBean CB = new CustomerBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();
		
		//String uprn= DBU.UPRN(CLI);

		try{
		
			String 	date =null;
			String 	date1 =null;
			String 	date2 =null;
			String UniqueNumber = null;
			date = Reusables.getdateFormat("yyyy-MM-dd", 0);
			date1 = Reusables.getdateFormat("yyyy-MM-dd", 5);
			date2 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			UniqueNumber = Reusables.getdateFormat("yyhhmmss", 0);
					
//			DBU.getOrderDetails(CLI, "FTTPCease", ODB);
			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
			
			for(int i =InitialState; i<3;i++){
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\FTTP_UNC_"+state[i]+".txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\FTTP_UNC_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();				
				
				//System.out.println(template);
				
				String newtext = template.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_ORDERID",ODB.getORDERID());
				System.out.println("Order Id :" +ODB.getORDERID());
				newtext = newtext.replaceAll("M_STATEID",state[i]);
				System.out.println("State Id :" +state[i]);
				newtext = newtext.replaceAll("M_STATEDATETIME",date2);
				newtext = newtext.replaceAll("M_COMMANDID",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_RESELLERREFERENCE",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_RESELLERACCOUNTREFERENCE",Str_Account);
				newtext = newtext.replaceAll("M_ALLOCATEDCLI",CLI);
				newtext = newtext.replaceAll("M_CASR",ODB.getCASR());
				System.out.println("CASR Value :" +ODB.getCASR());
				newtext = newtext.replaceAll("M_CUSTOMERAGREEDDATE",date1);
				newtext = newtext.replaceAll("M_EXPECTEDSERVICEACTIVATIONDATE",date1);
				newtext = newtext.replaceAll("M_ORIGINALLEADTIMEDATE",date1);
				newtext = newtext.replaceAll("M_CUSTOMERREQUIREDBYDATE",date1);
				newtext = newtext.replaceAll("M_ORIGINALLEADTIMEDATE",date1);
				newtext = newtext.replaceAll("M_CUSTOMERREQUIREDBYDATE",date1);
				
				System.out.println("over");
//				newtext = newtext.replaceAll("M_UPRN",uprn);
//				System.out.println(uprn);
				newtext = newtext.replaceAll("M_APPOINTMENTID",UniqueNumber);
							
				newtext = newtext.replaceAll("M_ServiceResellerId",ODB.getServiceresellerid());
				System.out.println("Service Reseller Id :" +ODB.getServiceresellerid());			
								
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_FTTP_Cease_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_FTTP_Cease_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);
				
				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			  }
			
			Report.fnReportPass("FTTP Cease Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("FTTP Cease genration", "FTTP Cease file generation error " + e.getMessage());

		}finally{

		}
	}

	
	
}


