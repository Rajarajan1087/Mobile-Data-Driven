package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Utils.Reusables;

public class SMPFProvision implements Constants {
	
	public Reporter Report;

	/**
	 * @param report
	 */
	public SMPFProvision(Reporter report) {
		Report = report;
	}

	public void SMPFCease(String FinalState, String CLI) throws Exception{
		String[] state = {Progressing,Committed,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String 	date = null;

			DBU.getOrderDetails(CLI, "SMPFCease", ODB);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SMPFCease.txt");
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
				newtext = newtext.replaceAll("M_cmdInstID_SMPF",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_SMPF",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				if(state[i]==Committed){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
				}


				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFCease_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFCease_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("SMPFCease Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("SMPFCease file genration", "SMPFCease file generation error " + e.getMessage());

		}finally{

		}
	}	
		
	public void SMPFProvide(String FinalState, String CLI,String prov_initialstate) throws Exception{
		String[] state = {Progressing,Committed,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String 	date = null;

			DBU.getOrderDetails(CLI, "SMPFProvide", ODB);
			String LISPValue = "LISP"+ NameGenerator.randomCLI(10);
			//Value = 
			//String LISPValue = "LISP"+ NameGenerator.randomCLI(10);
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SMPFProvide.txt");
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
				newtext = newtext.replaceAll("M_gwyCmdID_SMPF",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_Value",LISPValue);
				if(state[i]==Committed){
					newtext = newtext.replaceAll("M_CCD","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:Attributes><ns0:Attribute><ns0:name>ServiceId</ns0:name><ns0:value>" + LISPValue + "</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>Username</ns0:name><ns0:value>" + CLI + "@talktalk.net</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>Password</ns0:name><ns0:value>pwd_" + CLI + "</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>SupplierReference</ns0:name><ns0:value>9999</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>DNAReference</ns0:name><ns0:value>12346579</ns0:value></ns0:Attribute></ns0:Attributes><ns0:Resource><ns0:resourceId>" + LISPValue + "</ns0:resourceId><ns0:resourceType>2</ns0:resourceType></ns0:Resource>");
				}else if(state[i]==Completed){
					newtext = newtext.replaceAll("M_CCD","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:Attributes><ns0:Attribute><ns0:name>ServiceId</ns0:name><ns0:value>" + CLI + "</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>Username</ns0:name><ns0:value>" + CLI + "@talktalk.net</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>Password</ns0:name><ns0:value>pwd_" + CLI + "</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>SupplierReference</ns0:name><ns0:value>9999</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>DNAReference</ns0:name><ns0:value>12346579</ns0:value></ns0:Attribute></ns0:Attributes><ns0:Resource><ns0:resourceId>" + CLI + "</ns0:resourceId><ns0:resourceType>2</ns0:resourceType></ns0:Resource>");
				}else{
					newtext = newtext.replaceAll("M_CCD", "");
					newtext = newtext.replaceAll("M_COMMITTED", "");
				}
				if(state[i]==Progressing){
					newtext= newtext.replaceAll("M_PROGRESSING","<ns0:Attributes/>");
				}else{
					newtext = newtext.replaceAll("M_PROGRESSING", "");
				}

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFProvide_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFProvide_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("SMPFProvide Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("SMPFProvide file genration", "SMPFProvide file generation error " + e.getMessage());

		}finally{

		}
	
	}
	
	public void SMPFMigrate(String FinalState, String CLI,String prov_initialstate) throws Exception{
		String[] state = {Progressing,Committed,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String 	date = null;

			DBU.getOrderDetails(CLI, "SMPFMigrate", ODB);
			//Value = 
			String LISPValue = NameGenerator.randomCLI(10);
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SMPFMigrate.txt");
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
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_cmdInstID_SMPF",ODB.getPROVCMDINSTID() );
				newtext = newtext.replaceAll("M_gwyCmdID_SMPF",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_Value",LISPValue);
				if(state[i]==Committed){
					newtext = newtext.replaceAll("M_CCD","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:Attributes><ns0:Attribute><ns0:name>ServiceId</ns0:name><ns0:value>" + "LISP"+LISPValue + "</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>Username</ns0:name><ns0:value>" + CLI + "@talktalk.net</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>Password</ns0:name><ns0:value>pwd_" + CLI + "</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>SupplierReference</ns0:name><ns0:value>9999</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>DNAReference</ns0:name><ns0:value>12346579</ns0:value></ns0:Attribute></ns0:Attributes><ns0:Resource><ns0:resourceId>" + "LISP"+LISPValue + "</ns0:resourceId><ns0:resourceType>2</ns0:resourceType></ns0:Resource>");
				}else if(state[i]==Completed){
					newtext = newtext.replaceAll("M_CCD","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:Attributes><ns0:Attribute><ns0:name>ServiceId</ns0:name><ns0:value>LSIP" + CLI + "</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>Username</ns0:name><ns0:value>" + CLI + "@talktalk.net</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>Password</ns0:name><ns0:value>pwd_" + CLI + "</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>SupplierReference</ns0:name><ns0:value>9999</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>DNAReference</ns0:name><ns0:value>12346579</ns0:value></ns0:Attribute></ns0:Attributes><ns0:Resource><ns0:resourceId>LSIP" + CLI + "</ns0:resourceId><ns0:resourceType>2</ns0:resourceType></ns0:Resource>");
				}else{
					newtext = newtext.replaceAll("M_CCD", "");
					newtext = newtext.replaceAll("M_COMMITTED", "");
				}
				if(state[i]==Progressing){
					newtext= newtext.replaceAll("M_PROGRESSING","<ns0:Attributes/>");
				}else{
					newtext = newtext.replaceAll("M_PROGRESSING", "");
				}
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFMigrate_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFMigrate_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("SMPFMigrate Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("SMPFMigrate file genration", "SMPFMigrate file generation error " + e.getMessage());

		}finally{

		}
	
	}
	public void SMPFModify(String FinalState, String CLI,String prov_initialstate) throws Exception{
		String[] state = {Progressing,Committed,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String 	date = null;

			DBU.getOrderDetails(CLI, "SMPFModify", ODB);
			//Value = 
			String LISPValue = "LISP"+ NameGenerator.randomCLI(10);
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SMPFModify.txt");
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
				newtext = newtext.replaceAll("M_gwyCmdID_SMPF",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_Value",LISPValue);
				if(state[i]==Committed){
					newtext = newtext.replaceAll("M_CCD","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:Attributes><ns0:Attribute><ns0:name>ServiceId</ns0:name><ns0:value>" + LISPValue + "</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>Username</ns0:name><ns0:value>" + CLI + "@talktalk.net</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>Password</ns0:name><ns0:value>pwd_" + CLI + "</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>SupplierReference</ns0:name><ns0:value>9999</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>DNAReference</ns0:name><ns0:value>12346579</ns0:value></ns0:Attribute></ns0:Attributes><ns0:Resource><ns0:resourceId>" + LISPValue + "</ns0:resourceId><ns0:resourceType>2</ns0:resourceType></ns0:Resource>");
				}else if(state[i]==Completed){
					newtext = newtext.replaceAll("M_CCD","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:Attributes><ns0:Attribute><ns0:name>ServiceId</ns0:name><ns0:value>" + CLI + "</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>Username</ns0:name><ns0:value>" + CLI + "@talktalk.net</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>Password</ns0:name><ns0:value>pwd_" + CLI + "</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>SupplierReference</ns0:name><ns0:value>9999</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>DNAReference</ns0:name><ns0:value>12346579</ns0:value></ns0:Attribute></ns0:Attributes><ns0:Resource><ns0:resourceId>" + CLI + "</ns0:resourceId><ns0:resourceType>2</ns0:resourceType></ns0:Resource>");
				}else{
					newtext = newtext.replaceAll("M_CCD", "");
					newtext = newtext.replaceAll("M_COMMITTED", "");
				}
				if(state[i]==Progressing){
					newtext= newtext.replaceAll("M_PROGRESSING","<ns0:Attributes/>");
				}else{
					newtext = newtext.replaceAll("M_PROGRESSING", "");
				}

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFModify_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFModify_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("SMPFModify Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("SMPFModify file genration", "SMPFModify file generation error " + e.getMessage());

		}finally{

		}
	
	}
	
}
