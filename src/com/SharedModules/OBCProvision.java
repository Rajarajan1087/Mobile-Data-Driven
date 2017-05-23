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

public class OBCProvision  implements Constants{
	public Reporter Report;

	public OBCProvision(Reporter report) {
		Report = report;
		}
	
	public void OBCCease(String FinalState, String CLI) throws Exception{
		String [] state = {Accepted,Validated,Requested,Pending,PONR,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{

			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			DBU.getOrderDetails(CLI, "OBCCease", ODB);
			String CPWNRef = Reusables.getdateFormat("hhmmss", 0);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\OBCCease.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i =0; i<6;i++){

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_cmdInstID_CPS",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_CPS",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_serviceResellerId","569");
				newtext = newtext.replaceAll("M_STATE",state[i]);

				newtext = newtext.replaceAll("M_DateTime",date);

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CPS_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CPS_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("OBC Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("OBC file genration", "OBC file generation error " + e.getMessage());

		}finally{

		}
	}  

	
//	public void OBCCease_AOD(String FinalState, String CLI, String Account, int InitialState) throws Exception{
//		String [] state = {OBCCease_Accepted,OBCCease_Validated,OBCCease_Requested,OBCCease_Pending,OBCCease_Completed,OBCCease_PONR};
//
//		OrderDetailsBean ODB = new OrderDetailsBean();
//		DbUtilities DBU = new DbUtilities(Report);
//
//		try{
//
//			String 	date = null;
//			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
//			DBU.getOrderDetails(CLI, "OBCCease", ODB);
//			String CPWNRef = Reusables.getdateFormat("hhmmss", 0);
////			DBU.SM_SKID_CPWRef_CASR(ODB);
//						
//			if((InitialState==0)){			
//				DBU.getOrderDetails(CLI, "OBCCease", ODB);
//				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
//				CPWNRef = NameGenerator.randomCPWNRef(6);
//
//			}else{
//				DBU.getOrderDetails(CLI, "OBCCease", ODB);
//				DBU.SM_SKID_CPWRef_CASR(ODB);
//				CPWNRef = ODB.getCPWN();
//			}
//
//			
//			for(int i =0; i<6;i++){
//
//				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
//				String CustomerRequestedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 3);
//				String GoLiveDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 4);
//				String ActualProvisionedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 5);
//				
//				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_Cease_"+state[i]+".txt");
//				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\OBCCease_"+state[i]+".txt");
//				BufferedReader reader = new BufferedReader(new FileReader(file));
//				String line = "", oldtext = "",template="";
//				while((line = reader.readLine()) != null)
//					template += line + "\r\n";
//				reader.close();
//
//				oldtext=template;
//
//				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
//				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
//				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
//				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
//				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
//
//				newtext = newtext.replaceAll("M_ORDERID", ODB.getORDERID());
//				newtext = newtext.replaceAll("M_STATE",state[i]);
//				newtext = newtext.replaceAll("M_DateTime",date);
//				newtext = newtext.replaceAll("M_gwyCmdID",ODB.getPROVCMDGWYCMDID() );
//				newtext = newtext.replaceAll("M_cmdInstID",ODB.getPROVCMDINSTID());
//				newtext = newtext.replaceAll("M_ACCOUNT", Account);
//				newtext = newtext.replaceAll("M_CLI",CLI);
//				newtext = newtext.replaceAll("M_customerRequestedDate",CustomerRequestedDate);
//				newtext = newtext.replaceAll("M_goLiveDate",GoLiveDate);
//				newtext = newtext.replaceAll("M_ActualProvisionedDate",ActualProvisionedDate);
//				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
////				newtext = newtext.replaceAll("M_serviceResellerId","569");
//
//				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CPS_"+state[i]+".txt");
//				writer.write(newtext);writer.flush();writer.close();
//				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CPS_"+state[i]+".txt");
//				Thread.sleep(PROV_TIME);
//
//				if(FinalState.equalsIgnoreCase(state[i]))
//					break;
//
//			}
//			Report.fnReportPass("OBC Provisioning complete for "+ CLI);
//		} catch (Exception e) {
//			Report.fnReportFailAndTerminateTest("OBC file genration", "OBC file generation error " + e.getMessage());
//
//		}finally{
//
//		}
//	}  

	public void OBCProvide_AOD(String FinalState, String CLI, int InitialState, String Account) throws Exception{
		String [] state = {OBC_Provide_Accepted,OBC_Provide_Validated,OBC_Provide_Requested,OBC_Provide_CoolingOff,OBC_Provide_PONR,OBC_Provide_Completed,OBC_Provide_Cancelled,OBC_Provide_Rejected};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{

			String 	date = null;
			String date2 = null;
			String date3 = null;
			String date4 = null;
			String date5 = null;
			String CPWNRef = NameGenerator.randomCPWNRef(6);
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			date2 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 3);
			date3 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 5);
			date4 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 10);
			date5 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 14);
			
			if((InitialState==0)){			
				DBU.getOrderDetails(CLI, "OBCProvide", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);

			}else{
				DBU.getOrderDetails(CLI, "OBCProvide", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}
			
			DBU.getOrderDetails(CLI, "OBCProvide", ODB);

			for(int i =InitialState; i<8;i++){
				
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\OBC_Provide_"+state[i]+".txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\OBC_Provide_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", template="";
				while((line = reader.readLine()) != null)
				template += line + "\r\n";
				reader.close();				
				
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

//				if(!(InitialState==0)){
//					if(!(InitialState==(state[i]))){
//						
//						continue;
//						
//					}
//					InitialState = "";
//				}

				System.out.println(template);

				String newtext = template.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_OrderId",ODB.getORDERID());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_gwyCmdID_OBC",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_ACCOUNT", Account);
				newtext = newtext.replaceAll("M_CPWN",CPWNRef);
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_cmdInstID_OBC",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_CRD",date2);
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CustReqstdDate",date3);
				newtext = newtext.replaceAll("M_GoLiveDate",date4);
				newtext = newtext.replaceAll("M_ActualprovisionedDate",date5);
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_OBCProvide_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_OBCProvide_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("OBCProvide_AOD Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("OBCProvide_AOD file genration", "OBCProvide_AOD file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void OBCProvide(String FinalState, String CLI, String InitialState) throws Exception{
		String [] state = {Accepted,Validated,Requested,CoolingOff,PONR,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{

			String 	date = null;

			DBU.getOrderDetails(CLI, "OBCProvide", ODB);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\OBCProvide.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i =0; i<6;i++){

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				if(!InitialState.equalsIgnoreCase("")){
					if(!InitialState.equalsIgnoreCase(state[i])){
						InitialState="";
						continue;
					}
				}

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_cmdInstID_CPS",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_CPS",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_serviceResellerId","569");
				newtext = newtext.replaceAll("M_STATE",state[i]);

				newtext = newtext.replaceAll("M_DateTime",date);


				if(state[i].equalsIgnoreCase(CoolingOff)){
					newtext = newtext.replaceAll("M_COOLINGOFF","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
				}else{
					newtext = newtext.replaceAll("M_COOLINGOFF","");
				}



				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_OBCProvide_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_OBCProvide_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("OBCProvide Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("OBCProvide file genration", "OBCProvide file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void OBC_Modify_AOD(String FinalState, String CLI, String InitialState, String Account) throws Exception{


		//String [] state = {"0","2","6","19","20","24","12"};
		String [] state = {Accepted,Validated,Rejected,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		StubFilePlacing SFP = new StubFilePlacing();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String 	date = null;
			String 	date2 = null;
			String 	date3 = null;
			
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			date2 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 5);
			date3 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 1);

			/*if((InitialState.equalsIgnoreCase(""))||(InitialState.equalsIgnoreCase(Accepted))){			
				DBU.getOrderDetails(CLI, "OBC_Modify", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				
				CPWNRef = NameGenerator.randomCPWNRef(6);
			}else{
				DBU.getOrderDetails(CLI, "OBC_Modify", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}
*/
			SFP.PlaceFile(StubType.IPTVModify, CPWNRef);
			
			
			
			for(int i=0;i<4;i++){	

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				date2 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 5);
				date3 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 1);
				
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\OBC_Modify."+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "",template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();
				/*if(!InitialState.equalsIgnoreCase("")){
					if(!InitialState.equalsIgnoreCase(state[i])){
						InitialState="";
						continue;
					}
				}
*/
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_ORDERID",ODB.getORDERID());
				newtext = newtext.replaceAll("M_cmdInstID",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_ACCOUNT",Account);
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_DateTime2",date2);
				newtext = newtext.replaceAll("M_DateTime3",date3);
				
				if(state[i]==Committed){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
				}

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_OBC_Modify_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_OBC_Modify_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("OBC_Modify Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("OBC_Modify file genration", "OBC_Modify file generation error " + e.getMessage());

		}finally{

		}
	}
	
public void OBC_Suspend_AOD(String FinalState, String CLI, String InitialState, String Account) throws Exception{


		
		String [] state = {OBC_Suspend_Accepted,OBC_Suspend_Validated,OBC_Suspend_Completed,OBC_Suspend_Rejected};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		StubFilePlacing SFP = new StubFilePlacing(Report);
		try {
			String CPWNRef = null ;
			String 	date = null;
			//date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

					
			CPWNRef = NameGenerator.randomCPWNRef(6);
			
			SFP.PlaceFile(StubType.IPTVSuspend, CPWNRef);
			
		
			
			for(int i=0;i<4;i++){	
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\OBC_Suspend_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "",template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
						
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_ORDERID",ODB.getORDERID());
				newtext = newtext.replaceAll("M_cmdInstID",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_ACCOUNT",Account);
				newtext = newtext.replaceAll("M_DateTime",date);
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_OBC_Suspend_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_OBC_Suspend_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("OBC_Suspend Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("OBC_Suspend file generation", "OBC_Suspend file generation error " + e.getMessage());

		}finally{

		}
	}


	public void OBC_Cancel_AOD(String FinalState, String CLI, String CancelReason) throws Exception{

		String [] state = {OBC_Cancel_Accepted,OBC_Cancel_Validated,OBC_Cancel_Requested,OBC_Cancel_Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String 	date = null;

			DBU.getOrderDetails(CLI, "OBC_Cancel", ODB);
			//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
			CPWNRef = NameGenerator.randomCPWNRef(6);

			
			
			for(int i=0;i<4;i++){	
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\OBC_Cancel_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "",template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_ORDERID",ODB.getORDERID());
				newtext = newtext.replaceAll("M_cmdInstID",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_REASON",CancelReason);
				System.out.println(CancelReason);
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_OBC_Cancel_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_OBC_Cancel_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("OBCCancel Provisioning complete - >  CLI "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("OBCCancel file generation", "OBCCancel file generation error " + e.getMessage());

		}finally{

		}
	}
		
	public void OBCCease_AOD(String FinalState, String CLI, String Account, int InitialState) throws Exception{
		String [] state = {OBCCease_Accepted,OBCCease_Validated,OBCCease_Requested,OBCCease_Pending,OBCCease_PONR,OBCCease_Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{

			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			DBU.getOrderDetails(CLI, "OBCCease", ODB);
			String CPWNRef = Reusables.getdateFormat("hhmmss", 0);
//			DBU.SM_SKID_CPWRef_CASR(ODB);
						
			if((InitialState==0)){			
				DBU.getOrderDetails(CLI, "OBCCease", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);

			}else{
				DBU.getOrderDetails(CLI, "OBCCease", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}

			
			for(int i=InitialState; i<6;i++){

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				String CustomerRequestedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 3);
				String GoLiveDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 4);
				String ActualProvisionedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 5);
				
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_Cease_"+state[i]+".txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\OBCCease_"+state[i]+".txt");
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

				newtext = newtext.replaceAll("M_ORDERID", ODB.getORDERID());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_gwyCmdID",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_cmdInstID",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_ACCOUNT", Account);
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_customerRequestedDate",CustomerRequestedDate);
				newtext = newtext.replaceAll("M_goLiveDate",GoLiveDate);
				newtext = newtext.replaceAll("M_ActualProvisionedDate",ActualProvisionedDate);
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
//				newtext = newtext.replaceAll("M_serviceResellerId","569");

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CPS_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CPS_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("OBC Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("OBC file genration", "OBC file generation error " + e.getMessage());

		}finally{

		}
	}  

	
	
}