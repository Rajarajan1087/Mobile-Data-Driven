package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Utils.Reusables;

public class SMPFT3Provision implements Constants {

	public Reporter Report;

	/**
	 * @param report
	 */
	public SMPFT3Provision(Reporter report) {
		Report = report;
	}
	
	public void SMPFT3_Provide_AOD(String FinalState, String CLI, int InitialState, String LLUType) throws Exception{

		String [] state = {SMPFT3_Provide_Accepted,SMPFT3_Provide_Validated,SMPFT3_Provide_Requested,SMPFT3_Provide_Acknowledged,
				SMPFT3_Provide_Coolingoff,SMPFT3_Provide_Completed};
		
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		CustomerBean CB = new CustomerBean();
		DBU.getCustomerDetails(CLI, CB);
		
		DBU.getOrderDetails(CLI, "T3SMPFProvide", ODB);
	
		try {
			String CPWNRef = null ;
			String 	date = null,date2=null;
			CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);			
			
			for(int i=InitialState;i<6;i++){	
				
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SMPFT3_PROVIDE_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "",template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				date2 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 15);
				
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				
				newtext = newtext.replaceAll("M_ORDERID",ODB.getORDERID());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DATE",date);
				newtext = newtext.replaceAll("M_COMMANDID",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CMDINSTID",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_ACCOUNTNUMBER",CB.getAccountNumber());
				newtext = newtext.replaceAll("M_LLUTYPE",LLUType);
				newtext = newtext.replaceAll("M_CRDDATE",date2);
				newtext = newtext.replaceAll("M_ServiceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_CPWREF",CPWNRef);
							
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFT3_PROVIDE_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFT3_PROVIDE_"+state[i]+".txt");
				
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("_SMPFT3_PROVIDE_AOD Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("_SMPFT3_PROVIDE_ file genration", "_SMPFT3_PROVIDE_AOD file generation error " + e.getMessage());

		}finally{

		}
	}
		

	public void SMPFT3Cancel(String FinalState, String CLI) throws Exception{
		String[] state = {Accepted,Validated,Requested,CancellationPending,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String date = null;
			String CustomerAgreedDate = Reusables.getdateFormat("yyyy-MM-dd'T'hh:mm:ss.0ss", 8);

			DBU.getOrderDetails(CLI, "T3SMPFCancel", ODB);

			DBU.SM_SKID_CPWRef_CASR(ODB);
			CPWNRef = ODB.getCPWN();

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SMPFT3Cease.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i=0;i<5;i++){	

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", -5);

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_cmdInstID_SMPFT3",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_SMPFT3",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_customerCommitedDate",CustomerAgreedDate);
				newtext = newtext.replaceAll("M_CustomerAgreedDate",CustomerAgreedDate);

				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
                
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFT3Cancel_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFT3Cancel_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("SMPFT3Cancel Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("SMPFT3Cancel file generation", "SMPFT3Cancel file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void SMPFT3Suspend(String FinalState, String CLI) throws Exception{
		String[] state = {Accepted,Validated,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String date = null;
			
			DBU.getOrderDetails(CLI, "T3SMPFSuspend", ODB);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SMPFT3Suspend.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i=0;i<3;i++){	

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", -5);

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_cmdInstID_SMPFT3",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_SMPFT3",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());

				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
                
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFT3Suspend_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFT3Suspend_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("SMPFT3Suspend Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("SMPFT3Suspend file generation", "SMPFT3Suspend file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void SMPFT3Cease(String FinalState, String CLI) throws Exception{
		String[] state = {Accepted,Validated,Requested,Progressing,Acknowledged,CeasePending,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String date = null;
			String CustomerAgreedDate = Reusables.getdateFormat("yyyy-MM-dd'T'hh:mm:ss.0ss", 8);

			DBU.getOrderDetails(CLI, "T3SMPFCease", ODB);

			DBU.SM_SKID_CPWRef_CASR(ODB);
			CPWNRef = ODB.getCPWN();

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SMPFT3Cease.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i=0;i<7;i++){	

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_cmdInstID_SMPFT3",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_SMPFT3",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_customerCommitedDate",CustomerAgreedDate);
				newtext = newtext.replaceAll("M_CustomerAgreedDate",CustomerAgreedDate);

				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
                
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFT3Cease_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFT3Cease_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("SMPFT3Cease Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("SMPFT3Cease file generation", "SMPFT3Cease file generation error " + e.getMessage());

		}finally{

		}
	}

	public void SMPFT3Migrate(String FinalState, String CLI) throws Exception{
		String[] state = {Accepted,Validated,Requested,Progressing,Acknowledged,CoolingOff,PONR,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String date = null;
			String CustomerAgreedDate = Reusables.getdateFormat("yyyy-MM-dd'T'hh:mm:ss.0ss", 8);

			DBU.getOrderDetails(CLI, "T3SMPFMigrate", ODB);
			//DBU.SM_Insert_T_SK(ODB.getCASR());
			//DBU.SM_Insert_T_NETWORK_KEYS(ODB.getCASR(),T_NETWORK_KEYS_NK_ID.SMPF_T3);
			
			DBU.SM_SKID_CPWRef_CASR(ODB);
			CPWNRef = ODB.getCPWN();
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SMPFT3Migrate.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i=0;i<8;i++){	

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_cmdInstID_SMPFT3",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_SMPFT3",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_customerCommitedDate",CustomerAgreedDate);
				newtext = newtext.replaceAll("M_CustomerAgreedDate",CustomerAgreedDate);

				newtext = newtext.replaceAll("M_STATE",state[i]);

				if(state[i].equalsIgnoreCase("CoolingOff"))
				{
					newtext = newtext.replaceAll("M_CLOSEDDATE","<ns0:Attribute><ns0:name>ClosedDate</ns0:name><ns0:value>${DateTime}</ns0:value></ns0:Attribute>");
				}
				else
				{
					newtext = newtext.replaceAll("M_CLOSEDDATE","");
				}

				newtext = newtext.replaceAll("M_DateTime",date);

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFT3Migrate_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFT3Migrate_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
		//	}
			Report.fnReportPass("SMPFT3Migrate Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("SMPFT3Migrate file generation", "SMPFT3Migrate file generation error " + e.getMessage());

		}finally{

		}
}

	public void SMPFT3Renumber(String FinalState, String CLI, String NEWCLI) throws Exception{
		String [] state = {Accepted,Validated,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			//String CPWNRef = null ;
			String 	date =null;


			DBU.getOrderDetails(CLI, "T3SMPFRenumber", ODB);
			//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
			//CPWNRef = NameGenerator.randomCPWNRef(6);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SMPFT3Renumber.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i=0;i<3;i++){	

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				newtext = newtext.replaceAll("M_cmdInstID_SMPFT3",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_SMPFT3",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				//newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_NEWCLI",NEWCLI);
				newtext = newtext.replaceAll("M_DateTime",date);


				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFT3Renumber_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFT3Renumber_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("SMPFT3Renumber Provisioning complete - >  CLI "+ CLI+" New CLI - > "+NEWCLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("SMPFT3Renumber file genration", "SMPFT3Renumber file generation error " + e.getMessage());

		}finally{

		}

	} 
}	
