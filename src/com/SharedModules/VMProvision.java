package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Utils.Reusables;

public class VMProvision implements Constants {
	
	public Reporter Report;
	/**
	 * @param report
	 */
	public VMProvision(Reporter report) {
		Report = report;
	}

	public void VMCease(String FinalState,String CLI) throws Exception{
		String [] state = {Accepted,Validated,Requested,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{
		//	String CPWNRef = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

			DBU.getOrderDetails(CLI, "VMCease", ODB);
		//	CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\VMCease.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i =0; i<4;i++){

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				newtext = newtext.replaceAll("M_cmdInstID_VM",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_VM",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
     		 // newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
			//	newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);

				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_VMCease_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_VMCease_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("VMCease Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("VMCease file genration", "VMCease file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void VMRenumber_AOD(String FinalState, String CLI,String NewCLI, int InitialState)throws Exception{
		String [] state = {VM_Renumber_Accepted,VM_Renumber_Validated,VM_Renumber_Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{
			String CPWNRef = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			String 	ActualProvisionedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 3);

			DBU.getOrderDetails(CLI, "VMRenumber%", ODB);
					
			for(int i =InitialState; i<3;i++){
				
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\VM_Renumber_"+state[i]+".txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\VM_Renumber_"+state[i]+".txt");
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
				
				newtext = newtext.replaceAll("M_ORDERID_VM",ODB.getORDERID());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_gwyCmdID_VM",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_NewCLI",NewCLI );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_ProvisionedDate",ActualProvisionedDate);
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_VMRenumber_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_VMRenumber_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("VMProvide Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("VMProvide file genration", "VMProvide file generation error " + e.getMessage());

		}finally{

		}
}
	
	public void VMProvide_AOD(String FinalState, String CLI) throws Exception{
		String [] state = {VM_Provide_Accepted,VM_Provide_Validated,VM_Provide_Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{
			String CPWNRef = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			String 	date2 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 2);
			String 	ActualProvisionedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 3);
			
			DBU.getOrderDetails(CLI, "VMProvide", ODB);
			CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);

			for(int i =0; i<3;i++){
				
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\VM_Provide_"+state[i]+".txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\VM_Provide_"+state[i]+".txt");
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
				
				newtext = newtext.replaceAll("M_ORDERID_VM",ODB.getORDERID());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_gwyCmdID_VM",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_Appointment",date2);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_ProvisionedDate",ActualProvisionedDate);
				
//				newtext = newtext.replaceAll("M_cmdInstID_VM",ODB.getPROVCMDINSTID());
//			    newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
//				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
//				newtext = newtext.replaceAll("M_STATE",state[i]);
//				newtext = newtext.replaceAll("M_DateTime",date);
		
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_VMProvide_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_VMProvide_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("VMProvide_AOD Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("VMProvide_AOD file genration", "VMProvide_AOD file generation error " + e.getMessage());

		}finally{

		}
		
	}
	
	public void VMCease_AOD(String FinalState,String CLI) throws Exception{
		String [] state = {VM_Cease_Accepted,VM_Cease_Validated,VM_Cease_Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{
		//	String CPWNRef = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			String 	date1 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 7);
			String 	ActualProvisionedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 3);
						
			DBU.getOrderDetails(CLI, "VMCease", ODB);
		//	CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
		
			for(int i =0; i<3;i++){
				
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\VM_Cease_"+state[i]+".txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\VM_Cease_"+state[i]+".txt");
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
				newtext = newtext.replaceAll("M_ORDERID_VM",ODB.getORDERID());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_gwyCmdID_VM",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_AppointmentDate",date1);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_AppointmentDate",date1);
				newtext = newtext.replaceAll("M_ProvisionedDate",ActualProvisionedDate);
						
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_VMCease_AOD_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_VMCease_AOD_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("VMCease_AOD Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("VMCease_AOD file genration", "VMCease_AOD file generation error " + e.getMessage());

		}finally{

		}
	}



	public void VMProvide(String FinalState, String CLI) throws Exception{
		String [] state = {Accepted,Validated,Requested,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{
			String CPWNRef = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

			DBU.getOrderDetails(CLI, "VMProvide", ODB);
			CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\VMProvide.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i =0; i<4;i++){

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_cmdInstID_VM",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_VM",ODB.getPROVCMDGWYCMDID() );
				
				newtext = newtext.replaceAll("M_CLI",CLI );
     		    newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);

				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_VMProvide_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_VMProvide_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("VMProvide Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("VMProvide file genration", "VMProvide file generation error " + e.getMessage());

		}finally{

		}
		
	}

	public void VMRenumber(String FinalState, String CLI,String NewCLI) throws Exception{
		String [] state = {Accepted,Validated,Requested,CoolingOff,PONR,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{
			String CPWNRef = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

			DBU.getOrderDetails(CLI, "VMRenumber%", ODB);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\VMProvide.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i =0; i<4;i++){

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_cmdInstID_VM",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_VM",ODB.getPROVCMDGWYCMDID() );
				
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_NewCLI",NewCLI );
     		    newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);

				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_VMRenumber_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_VMRenumber_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("VMProvide Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("VMProvide file genration", "VMProvide file generation error " + e.getMessage());

		}finally{

		}
}
}
