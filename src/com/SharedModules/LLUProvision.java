package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.SharedModules.OrderDetailsBean;
import com.SharedModules.DbUtilities;
import com.SharedModules.Constants;
import com.Stubs.StubFilePlacing;
import com.Stubs.StubFilePlacing.StubType;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Utils.Reusables;

public class LLUProvision implements Constants {
	public Reporter Report;
	public StubFilePlacing	 SFP;
	
	public LLUProvision(Reporter report) {
		Report = report;
		SFP = new StubFilePlacing(Report);
	}

	/**
	 * @param FinalState
	 * @param CLI
	 * @param InitialState
	 * @throws Exception
	 */
	public void LLUModify(String FinalState, String CLI, String InitialState) throws Exception{


		//String [] state = {"0","2","6","19","20","24","12"};
		String [] state = {Accepted,Validated,Requested,Progressing,Acknowledged,Committed,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

			if((InitialState.equalsIgnoreCase(""))||(InitialState.equalsIgnoreCase(Accepted))){			
				DBU.getOrderDetails(CLI, "LLUModify", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				
				CPWNRef = NameGenerator.randomCPWNRef(6);
			}else{
				DBU.getOrderDetails(CLI, "LLUModify", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}


			SFP.PlaceFile(StubType.IPTVModifyNew, CPWNRef);

			SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);

			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLUModify.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<7;i++){	

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
				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				if(state[i]==Committed){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
				}

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUModify_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUModify_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("LLUModify Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLUModify file genration", "LLUModify file generation error " + e.getMessage());

		}finally{

		}
	}

	/**
	 * @param FinalState
	 * @param CLI
	 * @param NEWCLI
	 * @return 
	 * @throws Exception
	 */
	
	public String getNRR() throws Exception{
		//String [] state = {"0","2","6","19","20","24","12"};
String NRR="";
		try {
			String 	date = null;
			//date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\CTP_SGO.txt");
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
				newtext = newtext.replaceAll("M_DateTime",date);
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+"CTP_NRR"+".txt");
				writer.write(newtext);writer.flush();writer.close();
				String asds=MessageTester.MessageTester_test_Synchronous(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+"CTP_NRR"+".txt");
				int start=asds.indexOf("networkResultsReference>")+"networkResultsReference>".length();
				NRR=asds.substring(start,asds.indexOf("</",start));
				Thread.sleep(PROV_TIME);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLU Suspend file genration", "LLUModify file generation error " + e.getMessage());

		}finally{

		}
		return NRR;
	}
	
	public void LLUSuspend(String FinalState, String CLI, String InitialState) throws Exception{


		//String [] state = {"0","2","6","19","20","24","12"};
		String [] state = {Accepted,Validated,Requested,Progressing,Acknowledged,Committed,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String 	date = null;
			//date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

			if((InitialState.equalsIgnoreCase(""))||(InitialState.equalsIgnoreCase(Accepted))){			
				DBU.getOrderDetails(CLI, "LLUSuspend", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				
				CPWNRef = NameGenerator.randomCPWNRef(6);
			}else{
				DBU.getOrderDetails(CLI, "LLUSuspend", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}

			//SFP.PlaceFile(StubType.IPTVSuspendNew, CPWNRef);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLUModify.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<7;i++){	

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
				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				if(state[i]==Committed){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
				}

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUModify_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUModify_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("LLU Suspend Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLU Suspend file genration", "LLUModify file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void LLURenumber(String FinalState, String CLI, String NEWCLI) throws Exception{
		String [] state = {Accepted,Validated,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String 	date =null;


			DBU.getOrderDetails(CLI, "LLURenumber", ODB);
			//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
			CPWNRef = NameGenerator.randomCPWNRef(6);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLURenumber.txt");
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
				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_NEWCLI",NEWCLI);

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLURenumber_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLURenumber_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("LLURenumber Provisioning complete - >  CLI "+ CLI+" New CLI - > "+NEWCLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLURenumber file genration", "LLURenumber file generation error " + e.getMessage());

		}finally{

		}

	}

	/**
	 * @param FinalState
	 * @param CLI
	 * @param NEWCLI
	 * @param InitialState
	 * @throws Exception
	 */
	public void LLUProvideTakeover(String FinalState, String CLI, String NEWCLI, String InitialState) throws Exception {

		String [] state = {Accepted,Validated,Requested,Progressing,PlanningDelayed,
				Acknowledged,CoolingOff,PONR,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String 	date = null;
			String FutureDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 15);

			if((InitialState.equalsIgnoreCase(""))||(InitialState.equalsIgnoreCase(Accepted))){			
				DBU.getOrderDetails(CLI, "LLUProvideTakeover", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);

			}else{
				DBU.getOrderDetails(CLI, "LLUProvideTakeover", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}

			SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLUProvideTakeover.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<9;i++){	
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				if(!InitialState.equalsIgnoreCase("")){
					if(!InitialState.equalsIgnoreCase(state[i])){
						
						continue;
						
					}
					InitialState = "";
				}

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_NEWCLI",NEWCLI);

				if(state[i]==CoolingOff){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+FutureDate+"</ns0:customerCommitedDate>");
					newtext = newtext.replaceAll("M_CAD","<ns0:Attribute><ns0:name>CustomerAgreedDate</ns0:name><ns0:value>"+FutureDate+"</ns0:value></ns0:Attribute>");

				}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
					newtext = newtext.replaceAll("M_CAD","");
				}


				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_ProvideTakeOver_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_ProvideTakeOver_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("LLU_ProvideTakeOver Provisioning complete for "+CLI+" New CLI "+NEWCLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLU_ProvideTakeOver file genration", "LLU_ProvideTakeOver file generation error " + e.getMessage());

		}finally{

		}	
	}
	
	public void LLUProvideTakeover_KCI2(String FinalState, String CLI, String NEWCLI, String InitialState) throws Exception {

		String [] state = {Accepted,Validated,Requested,Progressing,PlanningDelayed,
				Acknowledged,CoolingOff,PONR,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		
		try {
			String CPWNRef = null ;
			String 	date = null;
			String CRD = null;
			String Appid = "1"+CLI.substring(5);
			//String FutureDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 15);

			if(InitialState.equalsIgnoreCase(Acknowledged))
			{			
				DBU.getCRD(CLI, "LLUProvideTakeover", ODB);
				
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);

			}else{
				DBU.getCRD(CLI, "LLUProvideTakeover", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}
			
			CRD = ODB.getCRD();
			System.out.println(CRD);
			CRD = Reusables.getdateFormat(CRD, "M/d/yyyy H:m:s", "yyyy-MM-dd'T'HH:mm:ss");
			System.out.println(CRD);
			SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLUProvideTakeoverKCI2.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			
			for(int i=0;i<9;i++){	
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				
				if(!InitialState.equalsIgnoreCase("")){
					if(!InitialState.equalsIgnoreCase(state[i])){
						
						continue;
					}
					InitialState = "";
				}

				
				oldtext=template;
				
				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_CRDDateTime",CRD);
				newtext = newtext.replaceAll("M_NEWCLI",NEWCLI);
				
				System.out.println("replaced all file");
				

				if(state[i]==CoolingOff){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+CRD+"</ns0:customerCommitedDate>");
					newtext = newtext.replaceAll("M_CAD","<ns0:Attribute><ns0:name>CustomerAgreedDate</ns0:name><ns0:value>"+CRD+"</ns0:value></ns0:Attribute>");
					newtext = newtext.replaceAll("M_APPOINTMENTDETAILS","<ns0:AppointmentDetails><ns1:appointmentStatus xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">1</ns1:appointmentStatus>"
							+ "<ns1:appointmentId xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+Appid+"</ns1:appointmentId>"
							+ "<ns1:appointmentDate xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+CRD+"</ns1:appointmentDate>"
							+ "<ns1:appointmentSlotStartTime xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+"08:00:00"+"</ns1:appointmentSlotStartTime>"
							+ "<ns1:appointmentSlotEndTime xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+"13:00:00"+"</ns1:appointmentSlotEndTime>"
							+ "<ns1:appointmentRequired xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">true</ns1:appointmentRequired></ns0:AppointmentDetails>");
					
					System.out.println("CRD is added");
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
					newtext = newtext.replaceAll("M_CAD","");
					newtext = newtext.replaceAll("M_APPOINTMENTDETAILS", "");
				}

				System.out.println("output file");
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_ProvideTakeOver_KCI2_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_ProvideTakeOver_KCI2_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("LLU_ProvideTakeOverKCI2 Provisioning complete for HM"+CLI+" New CLI "+NEWCLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLU_ProvideTakeOverKCI2 file genration", "LLU_ProvideTakeOverKCI2 file generation error " + e.getMessage());

		}finally{

		}	
	}

	public void LLUProvideTakeover_SIM2(String FinalState, String CLI, String NEWCLI, String InitialState) throws Exception {

		String [] state = {Accepted,Validated,Requested,Progressing,Acknowledged,CoolingOff,PONR,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String date = null;
			String FutureDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 15);

			if((InitialState.equalsIgnoreCase(""))||(InitialState.equalsIgnoreCase(Accepted))){			
				DBU.getOrderDetails(CLI, "LLUProvideTakeover", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);

			}else{
				DBU.getOrderDetails(CLI, "LLUProvideTakeover", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}

			SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLUProvideTakeover_SIM2.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<9;i++){	
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				if(!InitialState.equalsIgnoreCase("")){
					if(!InitialState.equalsIgnoreCase(state[i])){
						
						continue;
						
					}
					InitialState = "";
				}

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_NEWCLI",NEWCLI);
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_LORN_LLU","FIBRELINKEDORDERREF");
				
				if(state[i]==Validated){
					newtext = newtext.replaceAll("M_AR","<ns0:Attribute><ns0:name>AppointmentRequired</ns0:name><ns0:value>false</ns0:value></ns0:Attribute>");
				}else{
					newtext = newtext.replaceAll("M_AR","");
				}
				
				
				if(state[i]==CoolingOff){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+FutureDate+"</ns0:customerCommitedDate>");
					newtext = newtext.replaceAll("M_CAD","<ns0:Attribute><ns0:name>CustomerAgreedDate</ns0:name><ns0:value>"+FutureDate+"</ns0:value></ns0:Attribute>");
					newtext = newtext.replaceAll("M_ESD","<ns0:expectedServiceActivationDate>"+FutureDate+"</ns0:expectedServiceActivationDate>");
					
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
					newtext = newtext.replaceAll("M_CAD","");
					newtext = newtext.replaceAll("M_ESD","");
				}


				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_ProvideTakeOver_SIM2_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_ProvideTakeOver_SIM2_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("LLU_ProvideTakeOver_SIM2 Provisioning complete for HM"+CLI+" New CLI "+NEWCLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLU_ProvideTakeOver_SIM2 file genration", "LLU_ProvideTakeOver_SIM2 file generation error " + e.getMessage());

		}finally{

		}	
	}
	
	public void LLUProvideTakeover_SIM2_KCI2(String FinalState, String CLI, String NEWCLI, String InitialState) throws Exception {

		String [] state = {Accepted,Validated,Requested,Progressing,Acknowledged,CoolingOff,PONR,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String date = null;
			String FutureDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 15);

			if((InitialState.equalsIgnoreCase(""))||(InitialState.equalsIgnoreCase(Accepted))){			
				DBU.getOrderDetails(CLI, "LLUProvideTakeover", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);

			}else{
				DBU.getOrderDetails(CLI, "LLUProvideTakeover", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}

			SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLUProvideTakeover_SIM2.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<9;i++){	
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				if(!InitialState.equalsIgnoreCase("")){
					if(!InitialState.equalsIgnoreCase(state[i])){
						
						continue;
						
					}
					InitialState = "";
				}

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_NEWCLI",NEWCLI);
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_LORN_LLU","FIBRELINKEDORDERREF");
				
				if(state[i]==Validated){
					newtext = newtext.replaceAll("M_AR","<ns0:Attribute><ns0:name>AppointmentRequired</ns0:name><ns0:value>false</ns0:value></ns0:Attribute>");
				}else{
					newtext = newtext.replaceAll("M_AR","");
				}
				
				
				if(state[i]==CoolingOff){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+FutureDate+"</ns0:customerCommitedDate>");
					newtext = newtext.replaceAll("M_CAD","<ns0:Attribute><ns0:name>CustomerAgreedDate</ns0:name><ns0:value>"+FutureDate+"</ns0:value></ns0:Attribute>");
					newtext = newtext.replaceAll("M_ESD","<ns0:expectedServiceActivationDate>"+FutureDate+"</ns0:expectedServiceActivationDate>");
					
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
					newtext = newtext.replaceAll("M_CAD","");
					newtext = newtext.replaceAll("M_ESD","");
				}


				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_ProvideTakeOver_SIM2_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_ProvideTakeOver_SIM2_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("LLU_ProvideTakeOver_SIM2 Provisioning complete for HM"+CLI+" New CLI "+NEWCLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLU_ProvideTakeOver_SIM2 file genration", "LLU_ProvideTakeOver_SIM2 file generation error " + e.getMessage());

		}finally{

		}	
	}
	
	/**
	 * @param FinalState
	 * @param CLI
	 * @param Porting
	 * @param IPTVReq
	 * @throws Exception
	 */
	public void LLUMigrate_SIM2(String FinalState, String CLI, boolean Porting, boolean IPTVReq) throws Exception{

		String state[] = {Accepted,Validated,Requested,Progressing,Acknowledged,CoolingOff,PONR,Completed};
		int x = 8;
		String	statePorting[] = {Accepted,Validated,Requested,Progressing,Acknowledged,AutoGNPRequired,CoolingOff,PONR,AutoGNPCompletionRequired,Completed};
		if(Porting){
			x=10;
		}
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			String FutureDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 13);
			DBU.getOrderDetails(CLI, "LLUMigrate", ODB);
			CPWNRef = NameGenerator.randomCPWNRef(6);
			SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLUMigrate_SIM2.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<x;i++){	

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss",0 );
				
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_DateTime2",FutureDate);
				if(Porting){
					newtext = newtext.replaceAll("M_STATE",statePorting[i]);
				}else{
					newtext = newtext.replaceAll("M_STATE",state[i]);
				}
				newtext = newtext.replaceAll("M_DateTime",date);

				if(state[i]==CoolingOff){
					newtext = newtext.replaceAll("M_CoolingOff","<ns0:customerCommitedDate>"+FutureDate+"</ns0:customerCommitedDate>");
				}else{
					newtext = newtext.replaceAll("M_CoolingOff","");
				}


				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUMigrate_SIM2_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUMigrate_SIM2_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("LLUMigrate_SIM2 Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLUMigrate_SIM2 file genration", "LLUMigrate_SIM2 file generation error " + e.getMessage());

		}finally{

		}
	}

	/**
	 * @param FinalState
	 * @param CLI
	 * @throws Exception
	 */
	public void LLUCancel(String FinalState, String CLI) throws Exception{

		String [] state = {Accepted,Validated,CancellationPending,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String 	date = null;

			DBU.getOrderDetails(CLI, "LLUCancel", ODB);
			//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
			CPWNRef = NameGenerator.randomCPWNRef(6);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLUCancel.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<4;i++){	

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_cmdInstID",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUCancel_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUCancel_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("LLUCancel Provisioning complete - >  CLI "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLUCancel file genration", "LLUCancel file generation error " + e.getMessage());

		}finally{

		}
	}
	
	
	
	/**
	 * @param FinalState
	 * @param CLI
	 * @throws Exception
	 */
	public void LLU_Cancel_AOD(String FinalState, String CLI, String CancelReason, String LLUTYPE) throws Exception{

			String [] state = {LLU_Cancel_Accepted,LLU_Cancel_Validated,LLU_Cancel_Requested,LLU_Cancel_CancellationPending,LLU_Cancel_Completed};

			OrderDetailsBean ODB = new OrderDetailsBean();
			DbUtilities DBU = new DbUtilities(Report);
			try {
				String CPWNRef = null ;
				String 	date = null;
				
				DBU.getOrderDetails(CLI, "LLUCancel", ODB);
				CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				System.out.println(CPWNRef);
				
				String OrderID = ODB.getORDERID();
				String ORDERId = OrderID.substring(0, 7);
				
				//CPWNRef = NameGenerator.randomCPWNRef(6);
				
				for(int i=0;i<5;i++){	
					
					File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_Cancel_"+state[i]+".txt");
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
					
					newtext = newtext.replaceAll("M_ORDERID",ORDERId);
					newtext = newtext.replaceAll("M_STATE",state[i]);
					newtext = newtext.replaceAll("M_DateTime",date);
					newtext = newtext.replaceAll("M_gwyCmdID",ODB.getPROVCMDGWYCMDID());	

			
					newtext = newtext.replaceAll("M_cmdInstID",ODB.getPROVCMDINSTID());
					newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);	
					newtext = newtext.replaceAll("M_CLI",CLI );
					newtext = newtext.replaceAll("M_REASON",CancelReason);
					System.out.println(CancelReason);
					newtext = newtext.replaceAll("M_LLUTYPE",LLUTYPE);
					newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
						
					FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUCancel_"+state[i]+".txt");
					writer.write(newtext);writer.flush();writer.close();
					MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUCancel_"+state[i]+".txt");
					Thread.sleep(PROV_TIME);

					if(FinalState.equalsIgnoreCase(state[i]))
						break;

				}
				Report.fnReportPass("LLUCancel_AOD Provisioning complete - >  CLI "+ CLI);
			} catch (Exception e) {
				Report.fnReportFailAndTerminateTest("LLUCancel_AOD file genration","LLUCancel_AOD file generation error " + e.getMessage());

			}finally{

			}
		}

		/**
	 * @param FinalState
	 * @param CLI
	 * @throws Exception
	 */
	public void LLUCease(String FinalState, String CLI) throws Exception{

		String [] state = {Accepted,Validated,Requested,Progressing,Acknowledged,CeasePending,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String 	date = null;
			String CustomerAgreedDate = Reusables.getdateFormat("yyyy-MM-dd'T'hh:mm:ss.0ss", 14);

			DBU.getOrderDetails(CLI, "LLUCease", ODB);
			
			DBU.SM_SKID_CPWRef_CASR(ODB);
			
			CPWNRef = ODB.getCPWN();

			SFP.PlaceFile(StubType.IPTVCease, CPWNRef);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLUCease.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<7;i++){	
				
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 7);

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				
				newtext = newtext.replaceAll("M_STATE",state[i]);
				
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_customerCommitedDate",CustomerAgreedDate);
				newtext = newtext.replaceAll("M_CustomerAgreedDate",CustomerAgreedDate);
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUCease_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUCease_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("LLUCease Provisioning complete - >  CLI "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLUCease file genration", "LLUCease file generation error " + e.getMessage());

		}finally{

		}
	}
	
	/**
	 * @param CLI
	 * @param InitialState
	 * @throws Exception
	 */
	public void LLUAmend(String FinalState, String CLI) throws Exception{

		String [] state = {Accepted,Validated,Requested,AmmendmentPending,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		OrderDetailsBean ODB1 = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			DBU.getOrderDetails(CLI, "LLUAmend", ODB);
			DBU.getOrderDetails(CLI, "LLUProvide", ODB1);

			for(int i=0;i<5;i++){	
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLUAmend.txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "";
				while((line = reader.readLine()) != null)
					oldtext += line + "\r\n";
				reader.close();
				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_cmdInstID",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_gwyCmdtarCMD",ODB1.getPROVCMDGWYCMDID());

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUAmend_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUAmend_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("LLUAmend Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLUAmend file genration", "LLUAmend file generation error " + e.getMessage());

		}finally{

		}
	}

	
	public void LLUAmend_UpdateOrder(String FinalState, String CLI) throws Exception{

		String [] state = {Accepted,Validated,Requested,AmmendmentPending,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		OrderDetailsBean ODB1 = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String 	date = null;
			String 	CRD = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			CRD = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 21);
			DBU.getOrderDetails(CLI, "LLUAmend", ODB);
			DBU.getOrderDetails(CLI, "LLUProvide", ODB1);

			for(int i=0;i<5;i++){	
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLUAmend.txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "";
				while((line = reader.readLine()) != null)
					oldtext += line + "\r\n";
				reader.close();
				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_cmdInstID",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_CRDDateTime",CRD);
				newtext = newtext.replaceAll("M_gwyCmdtarCMD",ODB1.getPROVCMDGWYCMDID());

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUAmend_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUAmend_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("LLUAmend Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLUAmend file genration", "LLUAmend file generation error " + e.getMessage());

		}finally{

		}
	}
	
	
	/**
	 * @param FinalState
	 * @param CLI
	 * @param NEWCLI
	 * @param InitialState
	 * @throws Exception
	 */
	public void LLUProvideNew(String FinalState, String CLI, String NEWCLI, String InitialState) throws Exception{
		String [] state = {Accepted,Validated,Requested,Progressing,PlanningDelayed,Acknowledged,CoolingOff,ImplementationDelayed,PONR,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();
		
		try {
			String CPWNRef = null ;
			String 	date = null;
	//		String FutureDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 15);

			if((InitialState.equalsIgnoreCase(""))||(InitialState.equalsIgnoreCase(Accepted))){			
				DBU.getOrderDetails(CLI, "LLUProvideNew", ODB, false);
				DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);				
			//	CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);

			}else{
				DBU.getOrderDetails(CLI, "LLUProvideNew", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
				DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);				
			}

			SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLUProvideNew.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<10;i++){	
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				if(!InitialState.equalsIgnoreCase("")){
					if(!InitialState.equalsIgnoreCase(state[i])){
						InitialState = "";
						continue;
					}
				}

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );
				
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_NEWCLI",NEWCLI);
				
				if((state[i].equalsIgnoreCase(Accepted))||(state[i].equalsIgnoreCase(Validated))||(state[i].equalsIgnoreCase(Progressing))){
					newtext = newtext.replaceAll("M_COMMITTED","");
					newtext = newtext.replaceAll("M_CAD","");
					newtext = newtext.replaceAll("M_APPOINTMENTDETAILS","");
					
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+AB.getSLOTDATE()+"</ns0:customerCommitedDate>");
					newtext = newtext.replaceAll("M_CAD","<ns0:Attribute><ns0:name>CustomerAgreedDate</ns0:name><ns0:value>"+AB.getSLOTDATE()+"</ns0:value></ns0:Attribute>");
					newtext = newtext.replaceAll("M_APPOINTMENTDETAILS","<ns0:AppointmentDetails><ns1:appointmentStatus xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">1"
							+ "</ns1:appointmentStatus><ns1:appointmentId xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+AB.getAPPOINTMENTID()+"</ns1:appointmentId>"
							+ "<ns1:appointmentDate xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+AB.getSLOTDATE()+"</ns1:appointmentDate>"
							+ "<ns1:appointmentSlotStartTime xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+AB.getSLOTSTARTTIME()+"</ns1:appointmentSlotStartTime>"
							+ "<ns1:appointmentSlotEndTime xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+AB.getSLOTENDTIME()+"</ns1:appointmentSlotEndTime><ns1:appointmentRequired xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">true</ns1:appointmentRequired></ns0:AppointmentDetails>");
				}

		
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUProvideNew_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUProvideNew_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("LLUProvideNew Provisioning complete for "+CLI+" New CLI "+NEWCLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLUProvideNew file genration", "LLUProvideNew file generation error " + e.getMessage());
		}finally{

		}	
	}
	
	public String UNA_LLU(String CLI, String AppDate, String ConfirmationReq, String AppId, String LLUType) throws Exception{

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
			String 	date2 = null;
		    String ServiceActivationDate = null;
		    
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			date2 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss",4); 
			ServiceActivationDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 8);
			AppDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss",9); 
			
			//AppDate = Reusables.getDaysFrom(AppDate, "yyyy-MM-dd'T'HH:mm:ss", 0);
			//custAggreeDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 2);
			//SuppRef = Reusables.getdateFormat("mmss", 0); 
			
			CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
			DBU.getOrderDetails(CLI, "LLUProvideNew%", ODB);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_UNAProvision.txt");
			
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
				
				newtext = newtext.replaceAll("M_ORDERID",ODB.getORDERID());
				newtext = newtext.replaceAll("M_STATE",state);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_gwyCmdID_UNA",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_ServiceActivationDate",ServiceActivationDate);
				newtext = newtext.replaceAll("M_CAD",date2);
				newtext = newtext.replaceAll("M_DelayReason", "NotSet");
				newtext = newtext.replaceAll("M_Confirmationrequired", ConfirmationReq );
				newtext = newtext.replaceAll("M_UNA_appointmentDate",AppDate);
				System.out.println(AppDate);
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_LLUTYPE",LLUType);
				System.out.println(LLUType);
				newtext = newtext.replaceAll("M_SERVICERESELLERID",ODB.getServiceresellerid());
							
//				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
//				newtext = newtext.replaceAll("M_UNA_appointmentId",AppId );
//			    newtext = newtext.replaceAll("M_UNA_apptStartTime", AppSlotStartTime ); 
//				newtext = newtext.replaceAll("M_UNA_apptEndTime", AppSlotEndTime );
//			    newtext = newtext.replaceAll("M_serviceResellerId","569");
//			    newtext = newtext.replaceAll("M_sup_visit_ref", SuppRef );
//				newtext = newtext.replaceAll("M_CustomerAgreedDate", custAggreeDate );
//				newtext = newtext.replaceAll("M_StateClarification","StateClarification");
//				newtext = newtext.replaceAll("M_AmendReason", "Re-synchronisation");
				
				String outputcontent = newtext;
				
//				if(UNCWLTO == "yes"){
//				updateTime = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", -5);
//			    outputcontent = outputcontent.replaceAll("M_DateTime", updateTime );
//				}else{				
//			    outputcontent = outputcontent.replaceAll("M_DateTime", AppDate );
//				}

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNA_"+state+".txt");
				System.out.println("After writing file");
				writer.write(newtext);
				writer.flush();
				writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNA_"+state+".txt");
				Thread.sleep(PROV_TIME);
			
			Report.fnReportPass("UNA_LLU Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("UNA_LLU file genration", "UNA_LLU file generation error " + e.getMessage());

		}finally{

		}
		return AppDate;
	}
	
	
	public void LLUProvideNew_RetainNo(String FinalState, String CLI, String NEWCLI, String InitialState) throws Exception{
		String [] state = {Accepted,Validated,Requested,Progressing,PlanningDelayed,Acknowledged,CoolingOff,ImplementationDelayed,PONR,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();
		
		try {
			String CPWNRef = null ;
			String 	date = null;
	//		String FutureDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 15);

			if((InitialState.equalsIgnoreCase(""))||(InitialState.equalsIgnoreCase(Accepted))){			
				DBU.getOrderDetails(CLI, "LLUProvideNew", ODB);
				DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);				
			//	CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);

			}else{
				DBU.getOrderDetails(CLI, "LLUProvideNew", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
				DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);				
			}

			SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLUProvideNew.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<10;i++){	
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 7);

				if(!InitialState.equalsIgnoreCase("")){
					if(!InitialState.equalsIgnoreCase(state[i])){
						InitialState = "";
						continue;
					}
				}

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );
				
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_NEWCLI",NEWCLI);
				
				if((state[i].equalsIgnoreCase(Accepted))||(state[i].equalsIgnoreCase(Validated))||(state[i].equalsIgnoreCase(Progressing))){
					newtext = newtext.replaceAll("M_COMMITTED","");
					newtext = newtext.replaceAll("M_CAD","");
					newtext = newtext.replaceAll("M_APPOINTMENTDETAILS","");
					
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+AB.getSLOTDATE()+"</ns0:customerCommitedDate>");
					newtext = newtext.replaceAll("M_CAD","<ns0:Attribute><ns0:name>CustomerAgreedDate</ns0:name><ns0:value>"+AB.getSLOTDATE()+"</ns0:value></ns0:Attribute>");
					newtext = newtext.replaceAll("M_APPOINTMENTDETAILS","<ns0:AppointmentDetails><ns1:appointmentStatus xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">1"
							+ "</ns1:appointmentStatus><ns1:appointmentId xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+AB.getAPPOINTMENTID()+"</ns1:appointmentId>"
							+ "<ns1:appointmentDate xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+AB.getSLOTDATE()+"</ns1:appointmentDate>"
							+ "<ns1:appointmentSlotStartTime xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+AB.getSLOTSTARTTIME()+"</ns1:appointmentSlotStartTime>"
							+ "<ns1:appointmentSlotEndTime xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+AB.getSLOTENDTIME()+"</ns1:appointmentSlotEndTime><ns1:appointmentRequired xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">true</ns1:appointmentRequired></ns0:AppointmentDetails>");
				}

		
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUProvideNew_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUProvideNew_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("LLUProvideNew Provisioning complete for "+CLI+" New CLI "+NEWCLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLUProvideNew file genration", "LLUProvideNew file generation error " + e.getMessage());
		}finally{

		}	
	}
//	public void LLU_Suspend_AOD(String FinalState, String CLI, String Account, String LLUTYPE, String CPWNRef) throws Exception{
//	
//		String [] state = {LLU_Suspend_Accepted,LLU_Suspend_Validated,LLU_Suspend_Completed};
//	
//		OrderDetailsBean ODB = new OrderDetailsBean();
//		OrderDetailsBean ODB1 = new OrderDetailsBean();
//		DbUtilities DBU = new DbUtilities(Report);
//		try {
//			String 	date = null;
//			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
//			DBU.getOrderDetails(CLI, "LLUSuspend", ODB);
//					
//			for(int i=0;i<3;i++){	
//				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
//				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_Suspend_"+state[i]+".txt");
//				BufferedReader reader = new BufferedReader(new FileReader(file));
//				String line = "", oldtext = "";
//				while((line = reader.readLine()) != null)
//					oldtext += line + "\r\n";
//				reader.close();
//				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
//				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
//				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
//				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
//				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
//	
//				newtext = newtext.replaceAll("M_ORDERID",ODB.getORDERID());
//				newtext = newtext.replaceAll("M_STATE",state[i]);
//				newtext = newtext.replaceAll("M_DateTime",date);
//				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );
//				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
//				newtext = newtext.replaceAll("M_CLI",CLI);
//				newtext = newtext.replaceAll("M_ACCOUNT",Account);
//				newtext = newtext.replaceAll("M_LLUTYPE",LLUTYPE);
//				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);	
//				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
//				
//				
//				//newtext = newtext.replaceAll("M_gwyCmdtarCMD",ODB1.getPROVCMDGWYCMDID());
//	
//				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_Suspend_"+state[i]+".txt");
//				writer.write(newtext);writer.flush();writer.close();
//				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_Suspend_"+state[i]+".txt");
//				Thread.sleep(PROV_TIME);
//	
//				if(FinalState.equalsIgnoreCase(state[i]))
//					break;
//			}
//			Report.fnReportPass("LLUSuspend Provisioning complete for "+ CLI);
//		} catch (Exception e) {
//			Report.fnReportFailAndTerminateTest("LLUSuspend file genration", "LLUSupend file generation error " + e.getMessage());
//	    
//		}finally{
//	
//		}
//	}

	/**
	 * @param FinalState
	 * @param CLI
	 * @param InitialState
	 * @throws Exception
	 */
	public void LLUMigrate(String FinalState, String CLI, String InitialState) throws Exception{


		//String [] state = {"0","2","6","19","20","24","12"};
		String [] state = {Accepted,Validated,Requested,Progressing,Acknowledged,CoolingOff,Committed,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

			if((InitialState.equalsIgnoreCase(""))||(InitialState.equalsIgnoreCase(Accepted))){			
				DBU.getOrderDetails(CLI, "LLUMigrate", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);
			}else{
				DBU.getOrderDetails(CLI, "LLUMigrate", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}

			SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLUMigrate.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<8;i++){	
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
				
				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );
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


				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUMigrate_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUMigrate_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("LLUMigrate Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLUMigrate file genration", "LLUMigrate file generation error " + e.getMessage());

		}finally{

		}
	}

	public void LLUProvideNew_SIM2(String FinalState, String CLI, String NEWCLI, String InitialState) throws Exception{
		String [] state = {Accepted,Validated,Requested,Progressing,PlanningDelayed,Acknowledged,LinkedOrderMatched,CoolingOff,PONR,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();

		try {
			String CPWNRef = null ;
			String 	date = null;
			//		String FutureDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 15);

			if((InitialState.equalsIgnoreCase(""))||(InitialState.equalsIgnoreCase(Accepted))){			
				DBU.getOrderDetails(CLI, "LLUProvideNew", ODB);
				CPWNRef = NameGenerator.randomCPWNRef(6);

			}else{
				DBU.getOrderDetails(CLI, "LLUProvideNew", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
				DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);				
			}

			SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);
			String Fdate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 13);
			for(int i=0;i<10;i++){	
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				if(!InitialState.equalsIgnoreCase("")){
					if(!InitialState.equalsIgnoreCase(state[i])){
						InitialState = "";
						continue;
					}
				}

				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLUProvideNew_SIM2.txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "";
				while((line = reader.readLine()) != null)
					oldtext += line + "\r\n";
				reader.close();

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );

				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_NEWCLI",NEWCLI);
				newtext = newtext.replaceAll("M_LORN_LLU",ODB.getFIBRELINKEDORDERREF());

				if((state[i].equalsIgnoreCase(CoolingOff))||(state[i].equalsIgnoreCase(PONR))||(state[i].equalsIgnoreCase(Completed))){
					newtext = newtext.replaceAll("M_CCD","<ns0:customerCommitedDate>"+Fdate+"</ns0:customerCommitedDate>");
					newtext = newtext.replaceAll("M_ServiceAD","<ns0:expectedServiceActivationDate>"+Fdate+"</ns0:expectedServiceActivationDate>");
				}else{
					newtext = newtext.replaceAll("M_CCD","");
					newtext = newtext.replaceAll("M_ServiceAD","");
				}
				
				
				

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUProvideNew_SIM2_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUProvideNew_SIM2_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("LLUProvideNew_SIM2 Provisioning complete for "+CLI+" New CLI "+NEWCLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLUProvideNew_SIM2 file genration", "LLUProvideNew_SIM2 file generation error " + e.getMessage());

		}finally{

		}	
	}
	
	public void LLUSuspend(String FinalState, String CLI) throws Exception{

		String [] state = {Accepted,Validated,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		OrderDetailsBean ODB1 = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			DBU.getOrderDetails(CLI, "LLUSuspend", ODB);
			
			for(int i=0;i<3;i++){	
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLUSuspend.txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "";
				while((line = reader.readLine()) != null)
					oldtext += line + "\r\n";
				reader.close();
				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				//newtext = newtext.replaceAll("M_gwyCmdtarCMD",ODB1.getPROVCMDGWYCMDID());

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUSuspend_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUSuspend_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("LLUSuspend Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLUSuspend file genration", "LLUSupend file generation error " + e.getMessage());

		}finally{

		}
	}
	
	
	public void LLUAmend_AOD(String FinalState, String CLI, String Str_Account, String LLUType, int InitialState) throws Exception{

		String [] state = {LLU_Amend_Accepted,LLU_Amend_Validated,LLU_Amend_Requested,LLU_Amend_AmendmentPending,LLU_Amend_Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		AppointmentBean AB = new AppointmentBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
		String CPWNRef = null;
		String date = null;
		 
		date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
		DBU.getOrderDetails(CLI, "LLUAmend", ODB);
		 
		DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
		 
		// DBU.getOrderDetails(CLI, "LLUProvide", ODB1);
		// String CustomerRequiredDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 2);
		// String AppointmentDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 3);
		// String ServiceActivationDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 6);
		// String CustomerAgreedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 7);
		//
		if((InitialState==0)){
		DBU.getOrderDetails(CLI, "LLUAmend", ODB);
		//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
		CPWNRef = NameGenerator.randomCPWNRef(6);

		}else{
		DBU.getOrderDetails(CLI, "LLUAmend", ODB);
		DBU.SM_SKID_CPWRef_CASR(ODB);
		CPWNRef = ODB.getCPWN();
		}
		 
		for(int i=InitialState;i<5;i++){
		date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
		System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_Amend_"+state[i]+".txt");
		File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_Amend_"+state[i]+".txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "", oldtext = "";
		while((line = reader.readLine()) != null)
		oldtext += line + "\r\n";
		reader.close();
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
		newtext = newtext.replaceAll("M_ACCOUNT", Str_Account);
		newtext = newtext.replaceAll("M_CLI",CLI);
		newtext = newtext.replaceAll("M_LLUType",LLUType);
		newtext = newtext.replaceAll("M_customerRequiredDate",AB.getSLOTDATE());
		newtext = newtext.replaceAll("M_AppointmentDate",AB.getSLOTDATE());
		newtext = newtext.replaceAll("M_ServiceActivationDate",AB.getSLOTDATE());
		newtext = newtext.replaceAll("M_CustomerAgreedDate",AB.getSLOTDATE());
		newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);

		FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUAmend_"+state[i]+".txt");
		writer.write(newtext);writer.flush();writer.close();
		MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUAmend_"+state[i]+".txt");
		Thread.sleep(PROV_TIME);

		if(FinalState.equalsIgnoreCase(state[i]))
		break;
		}
		Report.fnReportPass("LLUAmend Provisioning complete for "+ CLI);
		} catch (Exception e) {
		Report.fnReportFailAndTerminateTest("LLUAmend file genration", "LLUAmend file generation error " + e.getMessage());

		}finally{

		}
		}
	public void LLU_Renumber_AOD(String FinalState, String CLI, String NEWCLI, String LLUTYPE, String Account ) throws Exception{
		String [] state = {LLU_Renumber_Accepted,LLU_Renumber_Validated,LLU_Renumber_Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String 	date =null;


			DBU.getOrderDetails(CLI, "LLURenumber", ODB);
			//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
			CPWNRef = NameGenerator.randomCPWNRef(6);

			
			
			for(int i=0;i<3;i++){	
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_Renumber_"+state[i]+".txt");
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
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_LLUTYPE",LLUTYPE);
				newtext = newtext.replaceAll("M_ACCOUNT",Account);
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
			
				
				newtext = newtext.replaceAll("M_NEWCLI",NEWCLI);

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_Renumber_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_Renumber_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("LLURenumber Provisioning complete - >  CLI "+ CLI+" New CLI - > "+NEWCLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLURenumber file genration", "LLURenumber file generation error " + e.getMessage());

		}finally{

		}

	}
	
	public void LLU_PRN_AOD(String FinalState, String CLI,int InitialState, String Account,String NewCLI) throws Exception{
		String [] state = {LLU_PRN_Accepted,LLU_PRN_Validated,LLU_PRN_Requested,LLU_PRN_Progressing,LLU_PRN_Acknowledged,LLU_PRN_Coolingoff,LLU_PRN_ImplementationDelayed,LLU_PRN_PONR,LLU_PRN_Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();
		
		try {
			String CPWNRef = null ;
			String 	date = null;
			String date2 = null;
			String date3 = null;
			CPWNRef = NameGenerator.randomCPWNRef(6);
			String Str_NewCLI=NameGenerator.randomCLI(9);
			String AppID="";
	//		String FutureDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 15);
			
			DBU.getOrderDetails(CLI, "LLUProvideNew", ODB, false);
			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
			AppID=AB.getAPPOINTMENTID();
			System.out.println(AppID);
			if(AppID==null){
				AppID="";
				System.out.println("No Appointment");
			}
			else{
				System.out.println("Got Appointment");
			}
			
			SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			System.out.println(AB.getSLOTDATE());
			date2 = AB.getSLOTDATE();
			date3 = Reusables.getDaysFrom(date2, "yyyy-MM-dd'T'HH:mm:ss", 1);
			for(int i=InitialState;i<9;i++)
			{	
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_PRN_"+state[i]+".txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_PRN_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "",template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				//System.out.println(newtext);
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_ORDERID",ODB.getORDERID());
				newtext = newtext.replaceAll("M_RESELLERREFERENCE",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_COMMANDID",ODB.getPROVCMDGWYCMDID());

				newtext = newtext.replaceAll("M_APPOINTMENTID",AppID);
				newtext = newtext.replaceAll("M_CLI",NewCLI );
				newtext = newtext.replaceAll("M_RESELLERACCOUNTREFERENCE",Account);
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNREFERENCE",CPWNRef);
				newtext = newtext.replaceAll("M_STATEID",state[i]);
				newtext = newtext.replaceAll("M_DATETIME",date);
				System.out.println(date2);
				newtext = newtext.replaceAll("M_CUSTOMERREQUIREDBYDATE",date2);
				newtext = newtext.replaceAll("M_CUSTOMERAGREEDDATE",date2);
				newtext = newtext.replaceAll("M_COMPLETEDDATE",date3);
				newtext = newtext.replaceAll("M_CLOSEDDATE",date3);
				newtext = newtext.replaceAll("M_EXPECTEDSERVICEACTIVATIONDATE",date3);
				newtext = newtext.replaceAll("M_ALLOCATEDCLI",NewCLI);				
						
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_PRN_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_PRN_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("LLUProvideNew Provisioning complete for "+CLI+" New CLI "+NewCLI);
		} catch (Exception e) {
			e.printStackTrace();
			Report.fnReportFailAndTerminateTest("LLUProvideNew file genration", "LLUProvideNew file generation error " + e.getMessage());
		}finally{

		}	
	}
	
	public void LLU_PRN_AOD_Cancelled(String CLI,String Account) throws Exception{

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();
		
		try {
			String CPWNRef = null ;
			String 	date = null;
			String date2 = null;
			String date3 = null;
			CPWNRef = NameGenerator.randomCPWNRef(6);
			String Str_NewCLI=NameGenerator.randomCLI(9);
			String AppID="";
	//		String FutureDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 15);
			
			DBU.getOrderDetails(CLI, "LLUProvideNew", ODB, false);
			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
			AppID=AB.getAPPOINTMENTID();
			System.out.println(AppID);
			if(AppID==null){
				AppID="";
				System.out.println("No Appointment");
			}
			else{
				System.out.println("Got Appointment");
			}
			
			SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			System.out.println(AB.getSLOTDATE());
			date2 = AB.getSLOTDATE();
			date3 = Reusables.getDaysFrom(date2, "yyyy-MM-dd'T'HH:mm:ss", 1);
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_PRN_7.txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_PRN_7.txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "",template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				//System.out.println(newtext);
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_ORDERID",ODB.getORDERID());
				newtext = newtext.replaceAll("M_RESELLERREFERENCE",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_COMMANDID",ODB.getPROVCMDGWYCMDID());

				newtext = newtext.replaceAll("M_APPOINTMENTID",AppID);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_RESELLERACCOUNTREFERENCE",Account);
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNREFERENCE",CPWNRef);
				newtext = newtext.replaceAll("M_STATEID","8");
				newtext = newtext.replaceAll("M_DATETIME",date);
				newtext = newtext.replaceAll("M_CUSTOMERREQUIREDBYDATE",date2);
				newtext = newtext.replaceAll("M_CUSTOMERAGREEDDATE",date2);
				newtext = newtext.replaceAll("M_COMPLETEDDATE",date3);
				newtext = newtext.replaceAll("M_CLOSEDDATE",date3);
				newtext = newtext.replaceAll("M_EXPECTEDSERVICEACTIVATIONDATE",date3);
				newtext = newtext.replaceAll("M_ALLOCATEDCLI",CLI);				
						
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_PRN_8.txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_PRN_8.txt");
				Thread.sleep(PROV_TIME);
			Report.fnReportPass("LLUProvideNew Provisioning complete for "+CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLUProvideNew file genration", "LLUProvideNew file generation error " + e.getMessage());
		}finally{

		}	
	}
	public void LLUCease_AOD(String FinalState, String CLI, String Str_Account, String LLUType, int InitialState) throws Exception{

		String [] state = {LLU_Cease_Accepted,LLU_Cease_Validated,LLU_Cease_Requested,LLU_Cease_Progressing,LLU_Cease_Acknowledged,LLU_Cease_CeasePending,LLU_Cease_Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null;
			String 	date = null;

			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
//			String CustomerRequiredDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 3);
//			String CustomerAgreedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 4);
//			String CompletedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 6);
//			String ClosedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 7);
			
			String CustomerRequiredDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			String CustomerAgreedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			String CompletedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			String ClosedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			
//			DBU.SM_SKID_CPWRef_CASR(ODB);
			
			if((InitialState==0)){			
				DBU.getOrderDetails(CLI, "LLUCease", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);

			}else{
				DBU.getOrderDetails(CLI, "LLUCease", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}
									
			for(int i=InitialState;i<7;i++){	
								
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_Cease_"+state[i]+".txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_Cease_"+state[i]+".txt");
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
				newtext = newtext.replaceAll("M_ACCOUNT", Str_Account);
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_LLUType",LLUType);
				newtext = newtext.replaceAll("M_customerRequiredDate",CustomerRequiredDate);
				newtext = newtext.replaceAll("M_CustomerAgreedDate",CustomerAgreedDate);
				newtext = newtext.replaceAll("M_CompletedDate",CompletedDate);
				newtext = newtext.replaceAll("M_ClosedDate",ClosedDate);
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
						
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUCease_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUCease_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
				
			}
			Report.fnReportPass("LLUCease_AOD Provisioning complete - >  CLI "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLUCease_AOD file genration", "LLUCease_AOD file generation error " + e.getMessage());

		}finally{

		}
	}	

   public void LLU_Migrate_AOD(String FinalState, String CLI, String Account, String LLUType, int InitialState) throws Exception{
		
		String [] state = {LLU_Migrate_Accepted,LLU_Migrate_Validated,LLU_Migrate_Requested,LLU_Migrate_Progressing,LLU_Migrate_Acknowledged,LLU_Migrate_CoolingOff,LLU_Migrate_PONR,LLU_Migrate_Completed};
		  
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();

		try{	
			String 	date =null;
			String 	date2 =null;
			String 	date3 =null;
			String 	Activationdate =null;
			String 	Completeddate =null;
			String 	Closeddate =null;
			String 	ServiceActivationDate =null;
			String CPWNRef = null;
			
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			date2 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 3);
			date3 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 5);
			Activationdate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 7);
			Completeddate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 10);
			Closeddate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 14);
			ServiceActivationDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 16);
			
			if((InitialState==0)){			
				DBU.getOrderDetails(CLI, "LLUMigrate", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);

			}else{
				DBU.getOrderDetails(CLI, "LLUMigrate", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}
			NewDatabase ND=new NewDatabase(Report);
			Account=ND.getAcc_CLI(CLI, true);
			
			//String CPWNRef = NameGenerator.randomCPWNRef(6);
			
			DBU.getOrderDetails(CLI, "LLUMigrate", ODB);
			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);

			SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);

			for(int i =InitialState; i<8;i++){
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_Migrate_"+state[i]+".txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_Migrate_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();
				
//				if(!(InitialState==0)){
//					if(!(InitialState==(state[i]))){
//						
//						continue;
//						
//					}
//					InitialState = "";
//				}
							
				String newtext = template.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME);
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD);
				
				newtext = newtext.replaceAll("M_ORDERID", ODB.getORDERID());
				System.out.println("ORDERID : "+ ODB.getORDERID());
				newtext = newtext.replaceAll("M_STATEID",state[i]);
				newtext = newtext.replaceAll("M_DATE",date);
				newtext = newtext.replaceAll("M_gwyCmdID",ODB.getPROVCMDGWYCMDID());
				System.out.println("GatewayCommandId : "+ ODB.getPROVCMDGWYCMDID());	
				newtext = newtext.replaceAll("M_cmdInstID",ODB.getPROVCMDINSTID());
				System.out.println("CommandInstantId : "+ ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_ACCOUNT",Account);
				newtext = newtext.replaceAll("M_LLUTYPE",LLUType);
				newtext = newtext.replaceAll("M_CRD",date2);
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_SERVICERESELLERID",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWN",CPWNRef);
				newtext = newtext.replaceAll("M_CAD",date3);
				newtext = newtext.replaceAll("M_ExpectedActivationDate",Activationdate);
				newtext = newtext.replaceAll("M_CompletedDate",Completeddate);
				newtext = newtext.replaceAll("M_ClosedDate",Closeddate);
				newtext = newtext.replaceAll("M_ServiceActivationDate",ServiceActivationDate);
								
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_Migrate_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_Migrate_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);
				
				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			  }
			
			Report.fnReportPass("LLU_Migrate_AOD Provide Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLU_Migrate_AOD Provide generation", "LLU_Migrate_AOD file generation error " + e.getMessage());

		}finally{

		}
	}	
	
	
	
	public void LLUMigrate_AOD(String FinalState, String CLI, String InitialState) throws Exception{


		//String [] state = {"0","2","6","19","20","24","12"};
		String [] state = {Accepted_AOD,Validated_AOD,Requested_AOD,Progressing_AOD,Acknowledged_AOD,Committed_AOD,Completed_AOD };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

			if((InitialState.equalsIgnoreCase(""))||(InitialState.equalsIgnoreCase(Accepted))){			
				DBU.getOrderDetails(CLI, "LLUMigrate", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);
			}else{
				DBU.getOrderDetails(CLI, "LLUMigrate", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}

			SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLUMigrate_AOD.xml");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<7;i++){	
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
		//		newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_AgentID", prov_TTK);
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUMigrate_"+state[i]+".xml");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUMigrate_"+state[i]+".xml");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("LLUMigrate Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLUMigrate file genration", "LLUMigrate file generation error " + e.getMessage());

		}finally{

		}
	}

	public void LLU_PRT_AOD(String FinalState, String CLI, String Str_NewCLI, String Str_Account, String LLUType, int InitialState) throws Exception{

		String [] state = {LLU_PRT_Accepted,LLU_PRT_Validated,LLU_PRT_Requested,LLU_PRT_Progressing,LLU_PRT_Acknowledged,LLU_PRT_Coolingoff,LLU_PRT_PONR,LLU_PRT_Completed};
		 
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		CustomerBean CB = new CustomerBean();
		 
		//DBU.getCustomerDetails(CLI, CB);
		try {
		String CPWNRef = null ;
		String date = null,date2=null;
		 
		if(InitialState==(0)){
		DBU.getOrderDetails(CLI, "LLUProvideTakeover", ODB);
		CPWNRef = NameGenerator.randomCPWNRef(6);

		}else{
		DBU.getOrderDetails(CLI, "LLUProvideTakeover", ODB);
		DBU.SM_SKID_CPWRef_CASR(ODB);
		CPWNRef = ODB.getCPWN();
		System.out.println(CPWNRef);
		 
		}
		 
		SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);
		// SFP.PlaceFile(StubType.IPTVModifyNew, CPWNRef);

		for(int i=InitialState;i<8;i++){
		File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_PRT_"+state[i]+".txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "", oldtext = "",template="";
		while((line = reader.readLine()) != null)
		template += line + "\r\n";
		reader.close();

		date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
		date2 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 15);
		String CustomerRequiredDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 3);
		String CustomerAgreedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 4);
		String CompletedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 6);
		String ClosedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 7);
		String ExpecServiceActivatedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 7);
		String ServiceActivatedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 8);
		 
		oldtext=template;

		String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
		newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
		newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
		newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
		newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
		 
		newtext = newtext.replaceAll("M_ORDERID", ODB.getORDERID());
		System.out.println(ODB.getORDERID());
		newtext = newtext.replaceAll("M_STATE",state[i]);
		newtext = newtext.replaceAll("M_DateTime",date);
		newtext = newtext.replaceAll("M_CRDDATE",date2);
		newtext = newtext.replaceAll("M_gwyCmdID",ODB.getPROVCMDGWYCMDID());
		newtext = newtext.replaceAll("M_cmdInstID",ODB.getPROVCMDINSTID());
		 
		newtext = newtext.replaceAll("M_ACCOUNT",Str_Account);
		newtext = newtext.replaceAll("M_CLI",CLI);
		newtext = newtext.replaceAll("M_NewCLI",Str_NewCLI);
		newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
		newtext = newtext.replaceAll("M_LLUType",LLUType);
		newtext = newtext.replaceAll("M_customerRequiredDate",CustomerRequiredDate);
		newtext = newtext.replaceAll("M_CustomerAgreedDate",CustomerAgreedDate);
		newtext = newtext.replaceAll("M_CompletedDate",CompletedDate);
		newtext = newtext.replaceAll("M_ClosedDate",ClosedDate);
		newtext = newtext.replaceAll("M_ExpServActdDate",ExpecServiceActivatedDate);
		newtext = newtext.replaceAll("M_ServActdDate",ServiceActivatedDate);
		 
		FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_PRT_"+state[i]+".txt");
		writer.write(newtext);writer.flush();writer.close();
		MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_PRT_"+state[i]+".txt");
		 
		Thread.sleep(PROV_TIME);

		if(FinalState.equalsIgnoreCase(state[i]))
		break;

		}
		Report.fnReportPass("LLU Prt AOD Provisioning complete for "+ CLI);
		} catch (Exception e) {
		Report.fnReportFailAndTerminateTest("LLU Prt AOD file genration", "LLU Prt PROVIDE AOD file generation error " + e.getMessage());

		}finally{

		}
		}

public void LLU_Modify_AOD(String FinalState, String CLI, String Account, String LLUType) throws Exception{
		
		String [] state = {LLU_Modify_Accepted,LLU_Modify_Validated,LLU_Modify_Completed};
		  
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();

		try{	
			String 	date =null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
//			date2 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 2);
//			Leaddate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 4);
			
			String CPWNRef = NameGenerator.randomCPWNRef(6);
			
			DBU.getOrderDetails(CLI, "LLUModify", ODB);
			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
			
			SFP.PlaceFile(StubType.IPTVModifyNew, CPWNRef);
			SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);

			for(int i =0; i<3;i++){
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_Modify_"+state[i]+".txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_Modify_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();
							
				String newtext = template.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME);
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD);
				
				newtext = newtext.replaceAll("M_ORDERID", ODB.getORDERID());
//				System.out.println("ORDERID : "+ ODB.getORDERID());
				newtext = newtext.replaceAll("M_STATEID",state[i]);
				newtext = newtext.replaceAll("M_DATE",date);
				newtext = newtext.replaceAll("M_gwyCmdID",ODB.getPROVCMDGWYCMDID());
//				System.out.println("GatewayCommandId : "+ ODB.getPROVCMDGWYCMDID());	
				newtext = newtext.replaceAll("M_cmdInstID",ODB.getPROVCMDINSTID());
//				System.out.println("CommandInstantId : "+ ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_ACCOUNT",Account);
				newtext = newtext.replaceAll("M_LLUTYPE",LLUType);
				newtext = newtext.replaceAll("M_CLI",CLI);
//				newtext = newtext.replaceAll("M_SERVICERESELLERID",ResellerID);
				newtext = newtext.replaceAll("M_CPWN",CPWNRef);
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_Modify_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLU_Modify_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);
				
				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			  }
			
			Report.fnReportPass("LLU_Modify_AOD Provide Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLU_Modify_AOD Provide genration", "LLU_Modify_AOD file generation error " + e.getMessage());

		}finally{

		}
	}

	
	public void LLUSuspend_AOD(String FinalState, String CLI, String Account, String LLUTYPE, int InitialState) throws Exception{

		String [] state = {LLU_Suspend_Accepted,LLU_Suspend_Validated,LLU_Suspend_Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		OrderDetailsBean ODB1 = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
//			DBU.getOrderDetails(CLI, "LLUSuspend", ODB);
			
			if(InitialState==(0)){			
				DBU.getOrderDetails(CLI, "LLUSuspend", ODB);
				CPWNRef = NameGenerator.randomCPWNRef(6);

			}else{
				DBU.getOrderDetails(CLI, "LLUSuspend", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
				System.out.println(CPWNRef);
				//DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);				
			}	
			
			for(int i =0; i<3;i++){
				System.out.println(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_Suspend_"+state[i]+".txt");
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_Suspend_"+state[i]+".txt");
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
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_ACCOUNT",Account);
				newtext = newtext.replaceAll("M_LLUTYPE",LLUTYPE);
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);	
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				
				
				//newtext = newtext.replaceAll("M_gwyCmdtarCMD",ODB1.getPROVCMDGWYCMDID());

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUSuspend_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_LLUSuspend_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("LLUSuspend Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("LLUSuspend file genration", "LLUSupend file generation error " + e.getMessage());

		}finally{

		}
	}

	
}