package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.SharedModules.OrderDetailsBean;
import com.SharedModules.DbUtilities;
import com.SharedModules.Constants;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Utils.Reusables;
import com.Stubs.StubFilePlacing;


public class UNAProvision implements Constants {
	public Reporter Report;
	public StubFilePlacing	 SFP;
	
	public UNAProvision(Reporter report) {
		Report = report;
		SFP = new StubFilePlacing(Report);
	}

	/**
	 * @param FinalState
	 * @param CLI
	 * @param InitialState
	 * @throws Exception
	 */
	public void UNA(String CLI, String AppDate, String AppSlotStartTime, String AppSlotEndTime, String ConfirmationReq, String AppId, String UNCWLTO) throws Exception{


		//String state = "12";
		String state = Completed;

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String updateTime = null;
			String custAggreeDate = null;
			String SuppRef = null;
			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			AppDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss",9); 
			//AppDate = Reusables.getDaysFrom(AppDate, "yyyy-MM-dd'T'HH:mm:ss", 0);
			custAggreeDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 2);
			SuppRef = Reusables.getdateFormat("mmss", 0); 
			
			CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
			DBU.getOrderDetails(CLI, "LLUProvideNew%", ODB);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\UNAProvision.txt");
			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			System.out.println("Before replacing");
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
			
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				//newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_UNA",ODB.getPROVCMDGWYCMDID());
				
				newtext = newtext.replaceAll("M_UNA_appointmentId",AppId );
			
				newtext = newtext.replaceAll("M_UNA_appointmentDate",AppDate );
			
				newtext = newtext.replaceAll("M_UNA_apptStartTime", AppSlotStartTime ); 
				
				newtext = newtext.replaceAll("M_UNA_apptEndTime", AppSlotEndTime );
				
				newtext = newtext.replaceAll("M_CLI",CLI );
				
				newtext = newtext.replaceAll("M_serviceResellerId","569");
			
				newtext = newtext.replaceAll("M_sup_visit_ref", SuppRef );
				
				newtext = newtext.replaceAll("M_CustomerAgreedDate", custAggreeDate );
				
				newtext = newtext.replaceAll("M_Confirmationrequired", ConfirmationReq );
				
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				
				newtext = newtext.replaceAll("M_StateClarification","StateClarification");
				
				newtext = newtext.replaceAll("M_DelayReason", "NotSet");
				
				newtext = newtext.replaceAll("M_AmendReason", "Re-synchronisation");
				
				newtext = newtext.replaceAll("M_DateTime",date);
				
				
				
				String outputcontent = newtext;
				
				if(UNCWLTO == "yes"){
				updateTime = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", -5);
			    outputcontent = outputcontent.replaceAll("M_DateTime", updateTime );
				}else{				
			    outputcontent = outputcontent.replaceAll("M_DateTime", AppDate );
				}
			

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNA_"+state+".txt");
				System.out.println("After writing file");
				writer.write(newtext);
				writer.flush();
				writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNA_"+state+".txt");
				Thread.sleep(PROV_TIME);
			
			Report.fnReportPass("UNA Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("UNA file genration", "UNA file generation error " + e.getMessage());

		}finally{

		}
	}
	
	
	public String  UNAPro(String CLI, String AppDate, String AppSlotStartTime, String AppSlotEndTime, String ConfirmationReq, String AppId, String UNCWLTO) throws Exception{


		//String state = "12";
		String state = Completed;

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		String Str_appointmentDate = Reusables.getDaysFrom(AppDate, "dd/MM/yyyy", 0);	
		System.out.println(Str_appointmentDate);
		String Str_apptDateforReplacement =  Reusables.getdateFormat(Str_appointmentDate, "dd/MM/yyyy", "yyyy-MM-dd'T'HH:mm:ss");
		try {
			String CPWNRef = null ;
			String updateTime = null;
			String custAggreeDate = null;
			String SuppRef = null;
			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);				
			custAggreeDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 2);
			SuppRef = Reusables.getdateFormat("mmss", 0); 		
			CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
			if(UNCWLTO.equalsIgnoreCase("yes"))
			{
				DBU.getOrderDetails(CLI, "LLUUnsolicited%", ODB);
			}
			else
			{
			DBU.getOrderDetails(CLI, "LLUProvideNew%", ODB);
			}
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\UNAProvision.txt");
			
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
				
				newtext = newtext.replaceAll("M_gwyCmdID_UNA",ODB.getPROVCMDGWYCMDID());
				
				newtext = newtext.replaceAll("M_UNA_appointmentId",AppId );
			
				newtext = newtext.replaceAll("M_UNA_appointmentDate",Str_apptDateforReplacement );
			
				newtext = newtext.replaceAll("M_UNA_apptStartTime", AppSlotStartTime ); 
				
				newtext = newtext.replaceAll("M_UNA_apptEndTime", AppSlotEndTime );
				
				newtext = newtext.replaceAll("M_CLI",CLI );
				
				newtext = newtext.replaceAll("M_serviceResellerId","569");
			
				newtext = newtext.replaceAll("M_sup_visit_ref", SuppRef );
				
				newtext = newtext.replaceAll("M_CustomerAgreedDate", custAggreeDate );
				
				newtext = newtext.replaceAll("M_Confirmationrequired", ConfirmationReq );
				
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				
				newtext = newtext.replaceAll("M_StateClarification","StateClarification");
				
				newtext = newtext.replaceAll("M_DelayReason", "NotSet");
				
				newtext = newtext.replaceAll("M_AmendReason", "Re-synchronisation");
				
				newtext = newtext.replaceAll("M_DateTime",date);
				
				
				
				String outputcontent = newtext;
				
				if(UNCWLTO == "yes"){
				updateTime = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", -5);
			    outputcontent = outputcontent.replaceAll("M_DateTime", updateTime );
				}else{				
			    outputcontent = outputcontent.replaceAll("M_DateTime", AppDate );
				}
			

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNA_"+state+".txt");
			
				writer.write(newtext);
				writer.flush();
				writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNA_"+state+".txt");
				Thread.sleep(PROV_TIME);
			
			Report.fnReportPass("UNA Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("UNA file genration", "UNA file generation error " + e.getMessage());

		}finally{

		}
		return Str_appointmentDate;
	}

	public String  UNAPro_NGA(String CLI, String AppDate, String AppSlotStartTime, String AppSlotEndTime, String ConfirmationReq, String AppId, String UNCWLTO) throws Exception{


		//String state = "12";
		String state = Completed;

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		String Str_appointmentDate = Reusables.getDaysFrom(AppDate, "dd/MM/yyyy", 0);	
		System.out.println(Str_appointmentDate);
		String Str_apptDateforReplacement =  Reusables.getdateFormat(Str_appointmentDate, "dd/MM/yyyy", "yyyy-MM-dd'T'HH:mm:ss");
		try {
			String CPWNRef = null ;
			String updateTime = null;
			String custAggreeDate = null;
			String SuppRef = null;
			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);				
			custAggreeDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 2);
			SuppRef = Reusables.getdateFormat("mmss", 0); 		
			CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
			DBU.getOrderDetails(CLI, "NGAProvide%", ODB);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\UNAProvision_NGA.txt");
			
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
				if(ConfirmationReq.equals("yes"))
				{
				newtext = newtext.replaceAll("M_gwyCmdID_UNA",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_cmdInstID_UNA",ODB.getPROVCMDINSTID());
				}
				else
				{
					newtext = newtext.replaceAll("M_gwyCmdID_UNA",ODB.getPROVCMDGWYCMDID());	
				}
				
				newtext = newtext.replaceAll("M_UNA_appointmentId",AppId );
			
				newtext = newtext.replaceAll("M_UNA_appointmentDate",Str_apptDateforReplacement );
			
				newtext = newtext.replaceAll("M_UNA_apptStartTime", AppSlotStartTime ); 
				
				newtext = newtext.replaceAll("M_UNA_apptEndTime", AppSlotEndTime );
				
				newtext = newtext.replaceAll("M_CLI",CLI );
				
				newtext = newtext.replaceAll("M_serviceResellerId","569");
			
				newtext = newtext.replaceAll("M_sup_visit_ref", SuppRef );
				
				newtext = newtext.replaceAll("M_CustomerAgreedDate", custAggreeDate );
				
				newtext = newtext.replaceAll("M_Confirmationrequired", ConfirmationReq );
				
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				
				newtext = newtext.replaceAll("M_StateClarification","StateClarification");
				
				newtext = newtext.replaceAll("M_DelayReason", "NotSet");
				
				newtext = newtext.replaceAll("M_AmendReason", "Re-synchronisation");
				
				newtext = newtext.replaceAll("M_DateTime",date);
				
				newtext = newtext.replaceAll("M_access_method","15");
				
				
				
				String outputcontent = newtext;
				
				if(UNCWLTO == "yes"){
				updateTime = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", -5);
			    outputcontent = outputcontent.replaceAll("M_DateTime", updateTime );
				}else{				
			    outputcontent = outputcontent.replaceAll("M_DateTime", AppDate );
				}
			

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNA_"+state+".txt");
			
				writer.write(newtext);
				writer.flush();
				writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNA_"+state+".txt");
				Thread.sleep(PROV_TIME);
			
			Report.fnReportPass("UNA Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("UNA file genration", "UNA file generation error " + e.getMessage());

		}finally{

		}
		return Str_appointmentDate;
	}
}	


	