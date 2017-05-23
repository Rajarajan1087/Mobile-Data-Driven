package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.Engine.LoadEnvironment;
import com.Stubs.StubFilePlacing;
import com.Stubs.StubFilePlacing.StubType;
import com.Engine.Reporter;
import com.Utils.Reusables;


public class NGAProvision implements Constants {
	
	public Reporter Report;
	StubFilePlacing SFP;
	/**
	 * @param report
	 */
	public NGAProvision(Reporter report) {
		Report = report;
		SFP = new StubFilePlacing(Report);
	}

	
	public void NGAProvideDoubleMigration(String FinalState, String CLI,
			String InitialState) throws Exception {

		String[] state = { Accepted, Validated, Requested, Progressing,
				PlanningDelayed, Acknowledged, CoolingOff,
				ImplementationDelayed, PONR, Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();
		try {
			String CPWNRef = null;
			String date = null;
			if ((InitialState.equalsIgnoreCase(""))
					|| (InitialState.equalsIgnoreCase(Accepted))) {
				DBU.getOrderDetails(CLI, "NGAProvide", ODB);
				// DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
				// CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);
			} else {
				DBU.getOrderDetails(CLI, "NGAProvide", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}

			SFP.PlaceFile(StubType.IPTVProvide, CPWNRef);

			File file = new File(
					System.getProperty("user.dir")
							+ "\\ProvisioningTemplates\\NGAProvision_DoubleMigration.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "", template = "";
			while ((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for (int i = 0; i < 10; i++) {
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				if (!InitialState.equalsIgnoreCase("")) {
					if (!InitialState.equalsIgnoreCase(state[i])) {
						InitialState = "";
						continue;
					}
				}

				oldtext = template;

				String newtext = oldtext.replaceAll("M_env",
						LoadEnvironment.ENV);
				newtext = newtext.replaceAll("M_emm_hostname",
						LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",
						LoadEnvironment.EMM_PORT);
				newtext = newtext.replaceAll("M_emm_username",
						LoadEnvironment.EMM_USERNAME);
				newtext = newtext.replaceAll("M_emm_password",
						LoadEnvironment.EMM_PASSWORD);
				newtext = newtext.replaceAll("M_cmdInstID_NGA",
						ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_NGA",
						ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_CLI", CLI);
				newtext = newtext.replaceAll("M_serviceResellerId",
						ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef", CPWNRef);
				newtext = newtext.replaceAll("M_STATE", state[i]);
				newtext = newtext.replaceAll("M_LORN_NGA",
						ODB.getFIBRELINKEDORDERREF());
				newtext = newtext.replaceAll("M_DateTime", date);

				newtext = newtext.replaceAll(
						"M_CUSTOMERREQUESTED",
						"<ns0:Attribute><ns0:name>CustomerAgreedDate</ns0:name><ns0:value>"
								+ Reusables.getdateFormat(
										"yyyy-MM-dd'T'HH:mm:ss", 5)
								+ "</ns0:value></ns0:Attribute>");

				if ((state[i].equalsIgnoreCase(CoolingOff))
						|| (state[i].equalsIgnoreCase(PONR))
						| (state[i].equalsIgnoreCase(Completed))) {
					newtext = newtext.replaceAll(
							"M_COMMITTED",
							"<ns0:customerCommitedDate>"
									+ Reusables.getdateFormat(
											"yyyy-MM-dd'T'HH:mm:ss", 5)
									+ "</ns0:customerCommitedDate>");
					newtext = newtext
							.replaceAll("M_Cdatetime", Reusables.getdateFormat(
									"yyyy-MM-dd'T'HH:mm:ss", 5));
				} else {
					newtext = newtext.replaceAll("M_COMMITTED", "");
				}

				if (state[i].equalsIgnoreCase(CoolingOff)) {
					newtext = newtext.replaceAll("M_CLOSEDDATE", date);
				} else {
					newtext = newtext.replaceAll("M_CLOSEDDATE", "");
				}

				FileWriter writer = new FileWriter(
						System.getProperty("user.dir")
								+ "\\ProvisioningUpdates\\" + CLI
								+ "_NGAProvision_DoubleMigration_" + state[i]
								+ ".txt");
				writer.write(newtext);
				writer.flush();
				writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")
						+ "\\ProvisioningUpdates\\" + CLI
						+ "_NGAProvision_DoubleMigration_" + state[i] + ".txt");
				Thread.sleep(PROV_TIME);

				if (FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("NGAProvision_DoubleMigration Provisioning complete for "
					+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest(
					"NGAProvision_DoubleMigration file genration",
					"NGAProvision_DoubleMigration file generation error "
							+ e.getMessage());

		} finally {
		}
	}
	public void NGAProvide_SIM2(String FinalState, String CLI, String InitialState) throws Exception{

		String [] state = {Accepted,Validated,Requested,Progressing,PlanningDelayed,Acknowledged,CoolingOff,ImplementationDelayed,PONR,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();
		try {
			String CPWNRef = null ;
			String 	date = null;
			if((InitialState.equalsIgnoreCase(""))||(InitialState.equalsIgnoreCase(Accepted))){			
				DBU.getOrderDetails(CLI, "NGAProvide", ODB);
				DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);
			}else{
				DBU.getOrderDetails(CLI, "NGAProvide", ODB);
				DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}
			
			
			SFP.PlaceFile(StubType.IPTVProvide, CPWNRef);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGAProvide_SIM2.txt");
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
				newtext = newtext.replaceAll("M_cmdInstID_NGA",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_NGA",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_LORN_NGA",ODB.getFIBRELINKEDORDERREF());
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_CUSTOMERREQUESTED","<ns0:Attribute><ns0:name>CustomerAgreedDate</ns0:name><ns0:value>"+AB.getSLOTDATE()+"</ns0:value></ns0:Attribute>");
				//if((state[i].equalsIgnoreCase(CoolingOff))||(state[i].equalsIgnoreCase(PONR))|(state[i].equalsIgnoreCase(Completed))){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+AB.getSLOTDATE()+"</ns0:customerCommitedDate>");
					/*newtext = newtext.replaceAll("M_APPOINTMENTDETAILS", "<ns0:AppointmentDetails><ns1:appointmentStatus xmlns:ns1=" + "\n" + "http://xmlns.cpw.co.uk/CPW/CDM/Appointment" + "\n" + 
													        ">1</ns1:appointmentStatus><ns1:appointmentId xmlns:ns1=" + "\n" + "http://xmlns.cpw.co.uk/CPW/CDM/Appointment" + "\n" + 
													        ">"+AB.getAPPOINTMENTID()+"</ns1:appointmentId><ns1:appointmentDate xmlns:ns1=" + "\n" + "http://xmlns.cpw.co.uk/CPW/CDM/Appointment" + "\n" + 
													        ">"+AB.getSLOTDATE()+"</ns1:appointmentDate>" + 
													        "<ns1:appointmentSlotStartTime xmlns:ns1 =" + "\n" + "http://xmlns.cpw.co.uk/CPW/CDM/Appointment" + "\n" + 
													        ">"+AB.getSLOTENDTIME()+"</ns1:appointmentSlotStartTime><ns1:appointmentSlotEndTime xmlns:ns1 =" + "\n" + "http://xmlns.cpw.co.uk/CPW/CDM/Appointment" + "\n" + 
													        ">"+AB.getSLOTENDTIME()+"</ns1:appointmentSlotEndTime></ns0:AppointmentDetails>");	*/
					
					newtext = newtext.replaceAll("M_APPOINTMENTDETAILS","<ns0:AppointmentDetails><ns1:appointmentStatus xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">1</ns1:appointmentStatus>"
							+ "<ns1:appointmentId xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+AB.getAPPOINTMENTID()+"</ns1:appointmentId>"
							+ "<ns1:appointmentDate xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+AB.getSLOTDATE()+"</ns1:appointmentDate>"
							+ "<ns1:appointmentSlotStartTime xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+AB.getSLOTSTARTTIME()+"</ns1:appointmentSlotStartTime>"
							+ "<ns1:appointmentSlotEndTime xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+AB.getSLOTENDTIME()+"</ns1:appointmentSlotEndTime>"
							+ "<ns1:appointmentRequired xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">true</ns1:appointmentRequired></ns0:AppointmentDetails>");
					newtext = newtext.replaceAll("M_Cdatetime",Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 5));
				/*}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
					newtext = newtext.replaceAll("M_APPOINTMENTDETAILS", "");	
				}*/
				
				if(state[i].equalsIgnoreCase(CoolingOff)){
					newtext = newtext.replaceAll("M_CLOSEDDATE",date);
				}else{
					newtext = newtext.replaceAll("M_CLOSEDDATE","");
				}
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGAProvide_SIM2_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGAProvide_SIM2_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("NGAProvide_SIM2 Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("NGAProvide_SIM2 file genration", "NGAProvide_SIM2 file generation error " + e.getMessage());

		}finally{
		}
	}
	
	public void NGAProvide(String FinalState, String CLI, String InitialState) throws Exception{

		String [] state = {Accepted,Validated,Requested,Progressing,PlanningDelayed,Acknowledged,CoolingOff,ImplementationDelayed,PONR,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();
		try {
			String CPWNRef = null ;
			String 	date = null;
			if((InitialState.equalsIgnoreCase(""))||(InitialState.equalsIgnoreCase(Accepted))){			
				DBU.getOrderDetails(CLI, "NGAProvide", ODB);
				DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);
			}else{
				DBU.getOrderDetails(CLI, "NGAProvide", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}
			
			
			SFP.PlaceFile(StubType.IPTVProvide, CPWNRef);
			for(int i=0;i<10;i++){
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 14);

				if(!InitialState.equalsIgnoreCase("")){
					if(!InitialState.equalsIgnoreCase(state[i])){
						InitialState = "";
						continue;
					}
				}
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGAProvide.txt");
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
				
				newtext = newtext.replaceAll("M_cmdInstID_NGA",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_NGA",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_CUSTOMERREQUESTED","<ns0:Attribute><ns0:name>CustomerAgreedDate</ns0:name><ns0:value>"+AB.getSLOTDATE()+"</ns0:value></ns0:Attribute>");
				if((state[i].equalsIgnoreCase(CoolingOff))||(state[i].equalsIgnoreCase(PONR))){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+AB.getSLOTDATE()+"</ns0:customerCommitedDate>");
					newtext = newtext.replaceAll("M_APPOINTMENTDETAILS","<ns0:AppointmentDetails><ns1:appointmentStatus xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">1</ns1:appointmentStatus>"
							+ "<ns1:appointmentId xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+AB.getAPPOINTMENTID()+"</ns1:appointmentId>"
							+ "<ns1:appointmentDate xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+AB.getSLOTDATE()+"</ns1:appointmentDate>"
							+ "<ns1:appointmentSlotStartTime xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+AB.getSLOTSTARTTIME()+"</ns1:appointmentSlotStartTime>"
							+ "<ns1:appointmentSlotEndTime xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+AB.getSLOTENDTIME()+"</ns1:appointmentSlotEndTime>"
							+ "<ns1:appointmentRequired xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">true</ns1:appointmentRequired></ns0:AppointmentDetails>");
					newtext = newtext.replaceAll("M_Cdatetime",Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 5));
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
					newtext = newtext.replaceAll("M_APPOINTMENTDETAILS", "");	
				}
								
				if(state[i].equalsIgnoreCase(CoolingOff)){
					newtext = newtext.replaceAll("M_CLOSEDDATE","<ns0:Attribute><ns0:name>ClosedDate</ns0:name><ns0:value>+00:00</ns0:value></ns0:Attribute>");
				}else{
					newtext = newtext.replaceAll("M_CLOSEDDATE","");
				}
								FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGAProvide_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGAProvide_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("NGAProvide Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("NGAProvide file genration", "NGAProvide file generation error " + e.getMessage());

		}finally{
		}
	}
	
	public void NGAAmend(String FinalState, String CLI) throws Exception{

		String [] state = {Accepted,Validated,Requested,AmmendmentPending,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		OrderDetailsBean ODB1 = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String 	date = null;
			String CPWNRef = null ;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			DBU.getOrderDetails(CLI, "NGAAmend", ODB);
			DBU.getOrderDetails(CLI, "NGAProvide", ODB1);
			CPWNRef = NameGenerator.randomCPWNRef(6);
			for(int i=0;i<5;i++){	
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGAAmend.txt");
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

				newtext = newtext.replaceAll("M_gwyCmdID_tarCMD",ODB1.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_cmdInstID",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_cpwnReference",CPWNRef);

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGAAmend_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGAAmend_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("NGAAmend Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("NGAAmend file genration", "NGAAmend file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void NGAProvide_selfinstall(String FinalState, String CLI, String InitialState) throws Exception{

		String [] state = {Accepted,Validated,Requested,Progressing,PlanningDelayed,Acknowledged,CoolingOff,ImplementationDelayed,PONR,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();
		try {
			String CPWNRef = null ;
			String 	date = null;
			if((InitialState.equalsIgnoreCase(""))||(InitialState.equalsIgnoreCase(Accepted))){			
				DBU.getOrderDetails_selfinstall(CLI, "NGAProvide", ODB);
				//DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);
			}else{
				DBU.getOrderDetails(CLI, "NGAProvide", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}
			
			
			SFP.PlaceFile(StubType.IPTVProvide, CPWNRef);
			for(int i=0;i<10;i++){
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				if(!InitialState.equalsIgnoreCase("")){
					if(!InitialState.equalsIgnoreCase(state[i])){
						InitialState = "";
						continue;
					}
				}
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGAProvide_selfinstall.txt");
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
				
				newtext = newtext.replaceAll("M_cmdInstID_NGA",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_NGA",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_CUSTOMERREQUESTED","<ns0:Attribute><ns0:name>CustomerAgreedDate</ns0:name><ns0:value>"+AB.getSLOTDATE()+"</ns0:value></ns0:Attribute>");
								
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGAProvide_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGAProvide_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("NGAProvide Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("NGAProvide file genration", "NGAProvide file generation error " + e.getMessage());

		}finally{
		}
	}
	
	public void NGAProvide_KCI2(String FinalState, String CLI, String InitialState) throws Exception{

		String [] state = {Accepted,Validated,Requested,Progressing,PlanningDelayed,Acknowledged,CoolingOff,ImplementationDelayed,PONR,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();
		try {
			String CPWNRef = null ;
			String 	date = null;
			String 	date1 = null;
			String Appid = "1"+CLI.substring(5);
			date1 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 14);
			if((InitialState.equalsIgnoreCase(""))||(InitialState.equalsIgnoreCase(Accepted))){			
				DBU.getOrderDetails(CLI, "NGAProvide", ODB);
				DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);
			}else{
				DBU.getOrderDetails(CLI, "NGAProvide", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}
			
			
			SFP.PlaceFile(StubType.IPTVProvide, CPWNRef);
			for(int i=0;i<10;i++){
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				if(!InitialState.equalsIgnoreCase("")){
					if(!InitialState.equalsIgnoreCase(state[i])){
						InitialState = "";
						continue;
					}
				}
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGAProvide.txt");
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
				
				newtext = newtext.replaceAll("M_cmdInstID_NGA",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_NGA",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_CUSTOMERREQUESTED","<ns0:Attribute><ns0:name>CustomerAgreedDate</ns0:name><ns0:value>"+AB.getSLOTDATE()+"</ns0:value></ns0:Attribute>");
				if((state[i].equalsIgnoreCase(CoolingOff))||(state[i].equalsIgnoreCase(PONR))){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+date1+"</ns0:customerCommitedDate>");
					newtext = newtext.replaceAll("M_APPOINTMENTDETAILS","<ns0:AppointmentDetails><ns1:appointmentStatus xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">1</ns1:appointmentStatus>"
							+ "<ns1:appointmentId xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+Appid+"</ns1:appointmentId>"
							+ "<ns1:appointmentDate xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+date1+"</ns1:appointmentDate>"
							+ "<ns1:appointmentSlotStartTime xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+"08:00:00"+"</ns1:appointmentSlotStartTime>"
							+ "<ns1:appointmentSlotEndTime xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">"+"13:00:00"+"</ns1:appointmentSlotEndTime>"
							+ "<ns1:appointmentRequired xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Appointment\">true</ns1:appointmentRequired></ns0:AppointmentDetails>");
					newtext = newtext.replaceAll("M_Cdatetime",Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 5));
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
					newtext = newtext.replaceAll("M_APPOINTMENTDETAILS", "");	
				}
								
				if(state[i].equalsIgnoreCase(CoolingOff)){
					newtext = newtext.replaceAll("M_CLOSEDDATE","<ns0:Attribute><ns0:name>ClosedDate</ns0:name><ns0:value>+00:00</ns0:value></ns0:Attribute>");
				}else{
					newtext = newtext.replaceAll("M_CLOSEDDATE","");
				}
								FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGAProvide_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGAProvide_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("NGAProvide Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("NGAProvide file genration", "NGAProvide file generation error " + e.getMessage());

		}finally{
		}
	}
	
	public void NGAModify(String FinalState, String CLI) throws Exception{

		String [] state = {Accepted,Validated,Requested,Progressing,Acknowledged,CoolingOff,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		
		try {
			String CPWNRef = null ;
			String 	date = null;
			
			DBU.getOrderDetails(CLI, "NGAModify", ODB);
			//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
			CPWNRef = NameGenerator.randomCPWNRef(6);
			
			SFP.PlaceFile(StubType.IPTVModify, CPWNRef);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGAModify.txt");
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
				
				newtext = newtext.replaceAll("M_cmdInstID_NGA",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_NGA",ODB.getPROVCMDGWYCMDID() );
				
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				
				newtext = newtext.replaceAll("M_STATE",state[i]);
				
				newtext = newtext.replaceAll("M_DateTime",date);			
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGAModify_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGAModify_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("NGAModify Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("NGAModify file genration", "NGAModify file generation error " + e.getMessage());

		}finally{
		}
	}
	
	
	public void NGACancel(String FinalState, String CLI) throws Exception{

		String [] state = {Accepted,Validated,Requested,CancellationPending,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		
		try {
			String CPWNRef = null ;
			String 	date = null;
			
			DBU.getOrderDetails(CLI, "NGACancel", ODB);
					
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGACancel.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<5;i++){	
				
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_cmdInstID_NGA",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_NGA",ODB.getPROVCMDGWYCMDID() );
				
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				
				newtext = newtext.replaceAll("M_STATE",state[i]);
				
				newtext = newtext.replaceAll("M_DateTime",date);			
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGACancel_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGACancel_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("NGACancel Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("NGACancel file genration", "NGACancel file generation error " + e.getMessage());

		}finally{
		}
	}

	
	public void NGAProvide_AOD(String FinalState, String Str_CLI, String Str_Account, int InitialState) throws Exception{

		String [] state = {NGAProvide_Accepted, NGAProvide_Validated, NGAProvide_Requested, NGAProvide_Progressing, NGAProvide_ImplemenationDelayed, NGAProvide_Acknowledged, NGAProvide_Coolingoff, NGAProvide_PONR, NGAProvide_Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();

		try {
			String CPWNRef = null ;
			String 	date = null;
			if((InitialState==0)){			
				DBU.getOrderDetails(Str_CLI, "NGAProvide", ODB);
				DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);
			}else{
				DBU.getOrderDetails(Str_CLI, "NGAProvide", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}
			
//			SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);
			SFP.PlaceFile(StubType.IPTVModifyNew, CPWNRef);
			
			for(int i=InitialState;i<9;i++){
				
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 14);
				String CustomerRequestedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 3);
				String CUSTOMERAGREEDDATE = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 5);
				String CLOSEDDATE = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 6);
				String COMPLETEDDATE = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 7);
				
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGAProvide_"+state[i]+".txt");
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
				newtext = newtext.replaceAll("M_DATETIME",date);
				newtext = newtext.replaceAll("M_COMMANDID",ODB.getPROVCMDGWYCMDID());				
				newtext = newtext.replaceAll("M_RESELLERREFERENCE",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_CLI",Str_CLI );
				newtext = newtext.replaceAll("M_RESELLERACCOUNTREFERENCE", Str_Account);			
				newtext = newtext.replaceAll("M_CPWNREFERENCE",CPWNRef);
				System.out.println("AppID -> "+AB.getAPPOINTMENTID());
				newtext = newtext.replaceAll("M_APPOINTMENTID",AB.getAPPOINTMENTID());
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CUSTOMERREQUIREDBYDATE", CustomerRequestedDate);
				newtext = newtext.replaceAll("M_CUSTOMERAGREEDDATE", CUSTOMERAGREEDDATE);
				newtext = newtext.replaceAll("M_CLOSEDDATE", CLOSEDDATE);
				newtext = newtext.replaceAll("M_COMPLETEDDATE", COMPLETEDDATE);
	
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+Str_CLI+"_NGAProvide_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+Str_CLI+"_NGAProvide_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("NGAProvide Provisioning complete for "+ Str_CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("NGAProvide file genration", "NGAProvide file generation error " + e.getMessage());
		}finally{
		}
	}

	public void NGAAmend_AOD(String FinalState, String CLI, int InitialState) throws Exception{

		String [] state = {NGA_Amend_Accepted,NGA_Amend_Validated,NGA_Amend_Requested,NGA_Amend_AmendmentPending,NGA_Amend_Completed,NGA_Amend_Rejected};

		OrderDetailsBean ODB = new OrderDetailsBean();
		AppointmentBean AB = new AppointmentBean();
		OrderDetailsBean ODB1 = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String 	date = null;
			String 	date1 = null;
			String 	date2 = null;
			String CPWNRef = null;	
			String uniqueNumber = null;	
			
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			date1 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 3);
			date2 = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 5);
			DBU.getOrderDetails(CLI, "NGAAmend", ODB);
			DBU.getOrderDetails(CLI, "NGAProvide", ODB1);
			uniqueNumber = NameGenerator.randomCPWNRef(6);
			
			if((InitialState==0)){			
				DBU.getOrderDetails(CLI, "NGAAmend", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);

			}else{
				DBU.getOrderDetails(CLI, "NGAAmend", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}
			
			for(int i=InitialState;i<6;i++){	
				
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGA_AMEND_"+state[i]+".txt");
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
				
				newtext = newtext.replaceAll("M_ORDERID",ODB.getORDERID());
				System.out.println(ODB.getORDERID());
				newtext = newtext.replaceAll("M_STATEID",state[i]);
				newtext = newtext.replaceAll("M_COMMANDID",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_cmdInstId", ODB.getPROVCMDINSTID());
				System.out.println(ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_TARGETCOMMANDID",uniqueNumber);
				newtext = newtext.replaceAll("M_CPWNREF",CPWNRef);
				System.out.println(CPWNRef);
				newtext = newtext.replaceAll("M_CLI",CLI );
				System.out.println(CPWNRef);
				newtext = newtext.replaceAll("M_DATE1",date1);
				System.out.println(date1);
				newtext = newtext.replaceAll("M_APPOINTMENTID","10109387");
				//System.out.println(AB.getAPPOINTMENTID());
				newtext = newtext.replaceAll("M_DATE2",date2);
				
				newtext = newtext.replaceAll("M_DATE",date);
							
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGAAmend_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGAAmend_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("NGAAmend Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("NGAAmend file genration", "NGAAmend file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void NGAUNA(String CLI, String AppDate) throws Exception{

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();
		try {
			String CPWNRef = null ;
			String 	UNAREF = null;
			String 	date = null;

			DBU.getOrderDetails(CLI, "NGAProvide", ODB);
			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
			DBU.SM_SKID_CPWRef_CASR(ODB);
			CPWNRef = ODB.getCPWN();
			UNAREF = NameGenerator.randomCPWNRef(6);
							
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGA_UNA.txt");
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
				
				newtext = newtext.replaceAll("M_OrderID",ODB.getORDERID());
				System.out.println(ODB.getORDERID());
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_AppDate",AppDate);
				newtext = newtext.replaceAll("M_CommandID",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_UNAREF",UNAREF);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGA_UNA_"+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGA_UNA_"+".txt");
				Thread.sleep(PROV_TIME);


			Report.fnReportPass("NGA_UNA Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("NGA_UNA file genration", "NGA_UNA file generation error " + e.getMessage());

		}finally{
		}
	}
	
	public void NGA_Cancel_AOD(String FinalState, String CLI, String Error, int InitialState) throws Exception{

		String [] state = {NGA_Cancel_Accepted,NGA_Cancel_Validated,NGA_Cancel_Requested,NGA_Cancel_CancellationPending,NGA_Cancel_Completed,NGA_Cancel_Rejected};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		
		try {
			String CPWNRef = null;
			String 	date = null;
			String TargetCmdID = null;
			
			DBU.getOrderDetails(CLI, "NGACancel", ODB);
			CPWNRef = NameGenerator.randomCPWNRef(6);
			TargetCmdID = NameGenerator.randomCPWNRef(6);		
			
			for(int i=InitialState;i<6;i++){	
				
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGA_Cancel_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "",template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();
				
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss",0);
				
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_RESELLERREFERENCE",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_COMMANDID",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_OrderId",ODB.getORDERID());
			//	System.out.println(ODB.getORDERID());			
				
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_CPWNREFERENCE",CPWNRef);
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_TARGETCOMMANDID",TargetCmdID);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_ERROR",Error);
				newtext = newtext.replaceAll("M_DATETIME",date);			
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGA_Cancel_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGA_Cancel_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("NGA_Cancel_AOD Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("NGA_Cancel_AOD file genration", "NGA_Cancel_AOD file generation error " + e.getMessage());

		}finally{
		}
	}
	
	public void NGA_Modify_AOD(String FinalState, String CLI, String Account,int InitialState) throws Exception{

		String [] state = {NGA_Modify_Accepted,NGA_Modify_Validated,NGA_Modify_Requested,NGA_Modify_Progressing,NGA_Modify_Acknowledged,NGA_Modify_Committed,NGA_Modify_Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		
		try {
			String CPWNRef = null ;
			String 	date = null;
			String 	date2 = null;
			String date3 = null;
			
			
			DBU.getOrderDetails(CLI, "NGAModify", ODB);
			//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
			CPWNRef = NameGenerator.randomCPWNRef(6);
			
			SFP.PlaceFile(StubType.IPTVModify, CPWNRef);
			
			
			for(int i= InitialState;i<7;i++){	
				
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				date2= Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 5);
				date3= Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 6);

				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGA_Modify_"+state[i]+".txt");
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
				
				
				newtext = newtext.replaceAll("M_ORDERID",ODB.getORDERID());
				newtext = newtext.replaceAll("M_RESELLERREFERENCE",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_COMMANDID",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_RESELLERACCOUNTREFERENCE",Account);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_CPWNREFERENCE",CPWNRef);
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CUSTOMERREQUIREDBYDATE",date2);
				newtext = newtext.replaceAll("M_CUSTOMERAGREEDDATE",date2);
				newtext = newtext.replaceAll("M_COMPLETEDDATE",date3);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				
				newtext = newtext.replaceAll("M_DATETIME",date);			
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGA_Modify_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_NGA_Modify_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("NGA_Modify_AOD Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("NGA_Modify_AOD file genration", "NGA_Modify_AOD file generation error " + e.getMessage());

		}finally{
		}
	}
	
		public void NGAProvide_AOD_SIM2(String FinalState, String Str_CLI, String Str_Account, int InitialState) throws Exception{

		String [] state = {NGAProvide_Accepted, NGAProvide_Validated, NGAProvide_Requested, NGAProvide_Progressing, NGAProvide_ImplemenationDelayed, NGAProvide_Acknowledged, NGAProvide_LinkedOrderMatched_SIM2, NGAProvide_PlanningDelayed_SIM2, NGAProvide_Coolingoff, NGAProvide_PONR, NGAProvide_Completed, NGAProvide_AwaitingLinkedOrderUpdate_SIM2, NGAProvide_AwaitingLinkedOrderCompletion_SIM2};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();

		try {
			String CPWNRef = null ;
			String 	date = null;
			if((InitialState==0)){			
				DBU.getOrderDetails(Str_CLI, "NGAProvide", ODB);
				DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);
			}else{
				DBU.getOrderDetails(Str_CLI, "NGAProvide", ODB);
				DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
				
			}
			
			SFP.PlaceFile(StubType.IPTVProvideNew, CPWNRef);
			
			for(int i=InitialState;i<13;i++){
							
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 14);
				String CustomerRequestedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 3);
				String CUSTOMERAGREEDDATE = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 5);
				String CLOSEDDATE = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 6);
				String COMPLETEDDATE = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 7);
				
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGAProvide_"+state[i]+".txt");
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
				newtext = newtext.replaceAll("M_DATETIME",date);
				newtext = newtext.replaceAll("M_COMMANDID",ODB.getPROVCMDGWYCMDID());				
				newtext = newtext.replaceAll("M_RESELLERREFERENCE",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_CLI",Str_CLI );
				newtext = newtext.replaceAll("M_RESELLERACCOUNTREFERENCE", Str_Account);			
				newtext = newtext.replaceAll("M_CPWNREFERENCE",CPWNRef);
				System.out.println("M_APPOINTMENTID - > "+AB.getAPPOINTMENTID());
				newtext = newtext.replaceAll("M_APPOINTMENTID",AB.getAPPOINTMENTID());
				newtext = newtext.replaceAll("M_serviceResellerId","569");
				newtext = newtext.replaceAll("M_CUSTOMERREQUIREDBYDATE", CustomerRequestedDate);
				newtext = newtext.replaceAll("M_CUSTOMERAGREEDDATE", CUSTOMERAGREEDDATE);
				newtext = newtext.replaceAll("M_CLOSEDDATE", CLOSEDDATE);
				newtext = newtext.replaceAll("M_COMPLETEDDATE", COMPLETEDDATE);
				newtext = newtext.replaceAll("M_LORN",ODB.getFIBRELINKEDORDERREF());
								
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+Str_CLI+"_NGAProvide_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+Str_CLI+"_NGAProvide_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("NGAProvide Provisioning complete for "+ Str_CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("NGAProvide file genration", "NGAProvide file generation error " + e.getMessage());

		}finally{
		}
	}


	public void NGASuspend_AOD(String FinalState, String Str_CLI, int InitialState) throws Exception{

		String [] state = {NGASuspend_Accepted,NGASuspend_Validated,NGASuspend_Completed,NGASuspend_Rejected};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		
		try {
			String CPWNRef = null ;
			String 	date = null;
			String uniqueNumber = null;

			
			DBU.getOrderDetails(Str_CLI, "NGASuspend", ODB);

			if((InitialState==0)){			
				DBU.getOrderDetails(Str_CLI, "NGASuspend", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);
			}else{
				DBU.getOrderDetails(Str_CLI, "NGASuspend", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}

			
			for(int i=InitialState;i<4;i++){	

				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGASuspend_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "",template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				uniqueNumber = NameGenerator.randomCPWNRef(6);

				
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_ORDERID", ODB.getORDERID());		
				newtext = newtext.replaceAll("M_STATE",state[i]);			
				newtext = newtext.replaceAll("M_DATETIME",date);
				newtext = newtext.replaceAll("M_COMMANDID",ODB.getPROVCMDGWYCMDID() );				
				newtext = newtext.replaceAll("M_RESELLERREFERENCE",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_CLI",Str_CLI );
				newtext = newtext.replaceAll("M_CPWNREFERENCE",CPWNRef);
				newtext = newtext.replaceAll("M_TargetCommandID",uniqueNumber);
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());				
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+Str_CLI+"_NGASuspend_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+Str_CLI+"_NGASuspend_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;
			}
			Report.fnReportPass("NGASuspend Provisioning complete for "+ Str_CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("NGASuspend file genration", "NGASuspend file generation error " + e.getMessage());

		}finally{
		}
	}

	
	
}
