package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Utils.Reusables;

public class IPSProvision implements Constants{
	
	/**
	 * @param report
	 */
	public IPSProvision(Reporter report) {
		Report = report;
	}

	public Reporter Report;
	
	public void IPSRenumber(String FinalState, String CLI) throws Exception{

		String [] state = {Accepted,Validated,Completed };
		
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		DBU.getOrderDetails(CLI, "IPSRenumber", ODB);
	
		try {
			String 	date = null;
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPSRenumber.txt");
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
			
				newtext = newtext.replaceAll("M_gwyCmdID_IPS",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPSRenumber_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPSRenumber_"+state[i]+".txt");
				
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			
			Report.fnReportPass("IPSRenumber Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("IPSRenumber file genration", "IPSRenumber file generation error " + e.getMessage());

		}finally{

		}
		
	}
	
	public void IPSModify(String FinalState, String CLI) throws Exception{
		String [] state = {Accepted,Validated,Requested,Progressing,
				Acknowledged,Assigned,CoolingOff,PONR,Installed,Completed };
		
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		DBU.getOrderDetails(CLI, "IPSModify", ODB);
	
		try {
			String CPWNRef = null ;
			String 	date = null;
			date = null;
			CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPSModify.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<10;i++){	

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_cmdInstID_IPS",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_IPS",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				
				if(state[i]==CoolingOff){
					newtext = newtext.replaceAll("M_COOLINGOFF","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
				}else{
					newtext = newtext.replaceAll("M_COOLINGOFF","");
				}


				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPSModify_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPSModify_"+state[i]+".txt");
				
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("IPSModify Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("IPSModify file genration", "IPSModify file generation error " + e.getMessage());

		}finally{

		}

	}

	public void IPSSuspend(String FinalState, String CLI) throws Exception{

		String [] state = {Accepted,Validated,Completed };
		
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		DBU.getOrderDetails(CLI, "IPSSuspend", ODB);
	
		try {
			String CPWNRef = null ;
			String 	date = null;
			CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPSSuspend.txt");
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
				
				newtext = newtext.replaceAll("M_cmdInstID_IPS",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_IPS",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPSSuspend_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPSSuspend_"+state[i]+".txt");
				
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("IPSSuspend Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("IPSSuspend file genration", "IPSSuspend file generation error " + e.getMessage());

		}finally{

		}
		
	}
	
	public void IPSProvide_AOD(String FinalState, String CLI, String Account, int InitialState) throws Exception{

		String [] state = {IPS_Provide_Accepted,IPS_Provide_Acknowledged,IPS_Provide_Coolingoff,IPS_Provide_Completed};
		
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
//		CustomerBean CB = new CustomerBean();
//		DBU.getCustomerDetails(CLI, CB);
//		DBU.getOrderDetails(CLI, "IPSProvide", ODB);
	
		try {
			String CPWNRef = null ;
			String 	date = null,date2=null;
			CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
			
			if((InitialState==0)){			
				DBU.getOrderDetails(CLI, "IPSProvide", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);

			}else{
				DBU.getOrderDetails(CLI, "IPSProvide", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}
			
			
			for(int i=InitialState;i<4;i++){	
				
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPS_PROVIDE_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "",template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				date2 = Reusables.getdateFormat("yyyy-MM-dd", 15);
				
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_CMDINSTID",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_COMMANDID",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_ORDERID",ODB.getORDERID());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_CPWREF",CPWNRef);
				newtext = newtext.replaceAll("M_DATE",date);
				newtext = newtext.replaceAll("M_CRDDATE",date2);
				newtext = newtext.replaceAll("M_ACCOUNTNUMBER",Account);
				
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPS_PROVIDE_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPS_PROVIDE_"+state[i]+".txt");
				
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("IPS PROVIDE AOD Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("IPS PROVIDE AOD file genration", "IPS PROVIDE AOD file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void IPSMigrate_AOD(String FinalState, String CLI) throws Exception{

		String [] state = {IPS_MIGRATE_Accepted,IPS_MIGRATE_Requested,IPS_MIGRATE_Acknowledged,
				IPS_MIGRATE_Coolingoff,IPS_MIGRATE_Completed};
		
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		DBU.getOrderDetails(CLI, "IPSMigrate", ODB);
	
		try {
			String CPWNRef = null ;
			String 	date = null,date2=null;
			CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
			
			for(int i=0;i<5;i++){	
				
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPS_MIGRATE_"+state[i]+".txt");
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
				
				newtext = newtext.replaceAll("M_CMDINSTID",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_COMMANDID",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_ORDERID",ODB.getORDERID());
				newtext = newtext.replaceAll("M_CPWREF",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DATE",date);
				newtext = newtext.replaceAll("M_CRDDATE",date2);
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPS_MIGRATE_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPS_MIGRATE_"+state[i]+".txt");
				
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("IPSMigrate Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("IPSMigrate file genration", "IPSMigrate file generation error " + e.getMessage());

		}finally{

		}
		}

	public void IPSProvide(String FinalState, String CLI, String InitialState) throws Exception{
		String [] state = {Accepted,Validated,Requested,Progressing,Acknowledged,Assigned,
				CoolingOff,PONR,Installed,Completed };
				
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		DBU.getOrderDetails(CLI, "IPSProvide", ODB);
	
		try {
			
			String 	date = null;
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPSProvide.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<10;i++){	
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);;

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_cmdInstID_IPS",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_IPS",ODB.getPROVCMDGWYCMDID() );
				
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_orderID_IPS",ODB.getORDERID());
				
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				
				if(state[i].equalsIgnoreCase(CoolingOff)){
					newtext = newtext.replaceAll("M_COOLINGOFF","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
				}else{
					newtext = newtext.replaceAll("M_COOLINGOFF","");
				}
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPSProvide_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPSProvide_"+state[i]+".txt");
				
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("IPSProvide Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("IPSProvide file genration", "IPSProvide file generation error " + e.getMessage());

		}finally{

		}
		
	}

	public void IPSUNCRENO(String FinalState, String CLI, String NEWCLI,String ServiceResellerid) throws Exception{
		String [] state = {Completed};
				
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		String CPWNRef="";
		String 	date = null;
		String UniqueID=Reusables.getdateFormat("yyyyMMdd", 0);
		try {
			DBU.SM_SKID_CPWReference(CLI, "1",CPWNRef );
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPSUNCReno.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "";
			while((line = reader.readLine()) != null)
				oldtext += line + "\r\n";
			reader.close();
							
			for(int i=0;i<1;i++){	
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_cmdInstID_IPS",UniqueID);
				newtext = newtext.replaceAll("M_gwyCmdIDIPSUNCReno",UniqueID);
				
				newtext = newtext.replaceAll("M_serviceResellerId",ServiceResellerid);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_NEWCLI",NEWCLI);
				
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				
				newtext = newtext.replaceAll("M_RESOURCEID",CPWNRef);
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPSUNCReno_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPSUNCReno_"+state[i]+".txt");
				
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("IPSUNCReno Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("IPSUNCReno file genration", "IPSUNCReno file generation error " + e.getMessage());

		}finally{

		}	
	}
	
	public void IPSCease(String FinalState, String CLI) throws Exception{
		String [] state = {Accepted,Validated,Requested,Progressing,CeasePending,PONR,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{

			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			DBU.getOrderDetails(CLI, "IPSCease", ODB);
			String CPWNRef = Reusables.getdateFormat("hhmmss", 0);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPSCease.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i =0; i<7;i++){

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_cmdInstID_IPS",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_IPS",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_serviceResellerId","569");
				newtext = newtext.replaceAll("M_STATE",state[i]);

				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPS_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPS_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("IPS Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("IPS file genration", "OBC file generation error " + e.getMessage());

		}finally{

		}
	}  
	
	public void IPSRenumber(String FinalState, String CLI, String NEWCLI) throws Exception{
		String [] state = {Accepted,Validated,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			//String CPWNRef = null ;
			String 	date =null;


			DBU.getOrderDetails(CLI, "IPSRenumber", ODB);
			//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
			//CPWNRef = NameGenerator.randomCPWNRef(6);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPSRenumber.txt");
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
				newtext = newtext.replaceAll("M_gwyCmdID_IPS",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);

				//newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				//newtext = newtext.replaceAll("M_cmdInstID_IPS",ODB.getPROVCMDINSTID());
				//newtext = newtext.replaceAll("M_NEWCLI",NEWCLI);


				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"IPSRenumber"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"IPSRenumber"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("IPSRenumber Provisioning complete - >  CLI "+ CLI+" New CLI - > "+NEWCLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("IPSRenumber file genration", "IPSRenumber file generation error " + e.getMessage());

		}finally{

		}

	}
}
