package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Utils.Reusables;

public class CPSProvision implements Constants{
	public Reporter Report;
	
	/**
	 * @param report
	 */
	public CPSProvision(Reporter report) {
		Report = report;
	}

	public void OBCCease(String FinalState, String CLI) throws Exception{
		String [] state = {Accepted,Validated,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{
		
			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			DBU.getOrderDetails(CLI, "OBCCease", ODB);
			String CPWNRef = Reusables.getdateFormat("hhmmss", 0);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\CPSSuspend.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i =0; i<3;i++){
				
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
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CPS_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CPS_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("CPS Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("CPS file genration", "CPS file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void OBCUSM(String FinalState, String CLI,String ServiceResellerID,String USMType) throws Exception{
		String [] state = {Pending,Suspended,Completed};

		try{
		
			String 	date = null;
			String UniqueNum = Reusables.getdateFormat("hhmmss", 0);
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\USM_OBC.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i =0; i<3;i++){
				
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				oldtext=template;
				
				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_gwyCmdID_CPS",UniqueNum);
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_serviceResellerId",ServiceResellerID);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				
				newtext = newtext.replaceAll("M_DateTime",date);
				
				if(USMType.equalsIgnoreCase("CPSRetain"))
				{
					newtext = newtext.replaceAll("M_USM_TYPE",CPSRetain);
				}
				else if(USMType.equalsIgnoreCase("ChangeOfAddressSameCLI"))
				{
					newtext = newtext.replaceAll("M_USM_TYPE",ChangeOfAddressSameCLI);
				}
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CPS_USM_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CPS_USM_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("OBC USM Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("OBC USM file genration", "CPS file generation error " + e.getMessage());

		}finally{

		}
	}
	
	
	public void CPSSuspend(String FinalState, String CLI) throws Exception{
		String [] state = {Accepted,Validated,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{
		
			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			DBU.getOrderDetails(CLI, "OBCSuspend", ODB);
			String CPWNRef = Reusables.getdateFormat("hhmmss", 0);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\CPSSuspend.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i =0; i<3;i++){
				
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
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CPS_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CPS_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("CPS Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("CPS file genration", "CPS file generation error " + e.getMessage());

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
		
}


