package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Utils.Reusables;

public class EVGProvision implements Constants{

	public Reporter Report;
	public EVGProvision(Reporter report) {
		Report = report;
	}
	public void EVGProvide(String FinalState, String CLI) throws Exception{
		
		String [] state = {Confirmed,Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();


		try{

			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

			DBU.getOrderDetails(CLI, "EVGConfirmAppointment", ODB);
			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\EVGProvision.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i =0; i<2;i++){

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );


				if(state[i].equalsIgnoreCase(Confirmed)){
					newtext = newtext.replaceAll("M_TARGETGWYCMDID","");	
					newtext = newtext.replaceAll("M_OTHERATTRIBUTES","");
				}else
				{
					newtext = newtext.replaceAll("M_TARGETGWYCMDID","\n<ns0:targetGatewayCommandId>M_gwyCmdID_EVG</ns0:targetGatewayCommandId>");
					newtext = newtext.replaceAll("M_OTHERATTRIBUTES","\n<ns0:Attribute><ns0:name>supplierReasonCode</ns0:name><ns0:value>000</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>supplierReasonDescription</ns0:name><ns0:value>Job Complete No issues</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>EVGReasonCode</ns0:name><ns0:value>EVG_REAS_0001</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>EVGReasonReasonDescription</ns0:name><ns0:value>Job Completed</ns0:value></ns0:Attribute>");
				}

				newtext = newtext.replaceAll("M_cmdInstID_EVG",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_EVG",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_serviceResellerId","569");
				newtext = newtext.replaceAll("M_STATE",state[i]);

				newtext = newtext.replaceAll("M_evg_appointmentId",AB.getAPPOINTMENTID());
				newtext = newtext.replaceAll("M_evg_appointmentDate", AB.getSLOTDATE());
				newtext = newtext.replaceAll("M_evg_apptStartTime",AB.getSLOTSTARTTIME());
				newtext = newtext.replaceAll("M_evg_apptEndTime",AB.getSLOTENDTIME());
				newtext = newtext.replaceAll("M_sup_visit_ref","123456");

				newtext = newtext.replaceAll("M_DateTime",date);

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_EVG_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_EVG_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("EVG Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("EVG file genration", "EVG file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void EVGCancel_BeforeConfirm(String CLI) throws Exception{

		String [] state = {Cancelled};


		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();


		try{

			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

			DBU.getOrderDetails(CLI, "EVGConfirmAppointment", ODB);
			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\EVGProvision.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i =0; i<1;i++){

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );



				newtext = newtext.replaceAll("M_TARGETGWYCMDID","\n<ns0:targetGatewayCommandId>M_gwyCmdID_EVG</ns0:targetGatewayCommandId>");
				newtext = newtext.replaceAll("M_OTHERATTRIBUTES","\n<ns0:Attribute><ns0:name>supplierReasonCode</ns0:name><ns0:value>000</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>supplierReasonDescription</ns0:name><ns0:value>Job Complete No issues</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>EVGReasonCode</ns0:name><ns0:value>EVG_REAS_0001</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>EVGReasonReasonDescription</ns0:name><ns0:value>Job Completed</ns0:value></ns0:Attribute>");


				newtext = newtext.replaceAll("M_cmdInstID_EVG",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_EVG",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_serviceResellerId","569");
				newtext = newtext.replaceAll("M_STATE",state[i]);

				newtext = newtext.replaceAll("M_evg_appointmentId",AB.getAPPOINTMENTID());
				newtext = newtext.replaceAll("M_evg_appointmentDate", AB.getSLOTDATE());
				newtext = newtext.replaceAll("M_evg_apptStartTime",AB.getSLOTSTARTTIME());
				newtext = newtext.replaceAll("M_evg_apptEndTime",AB.getSLOTENDTIME());
				newtext = newtext.replaceAll("M_sup_visit_ref","123456");

				newtext = newtext.replaceAll("M_DateTime",date);

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_EVG-CAN_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_EVG-CAN_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);



			}
			Report.fnReportPass("EVG Cancel Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("EVG file genration", "EVG file generation error " + e.getMessage());

		}finally{

		}
	}
	
	
	public void EVGCanProvide(String FinalState, String CLI,boolean CancelEVG) throws Exception{
		
		String [] state=new String[1];
		
		if(CancelEVG==true){
			
		 state[0] = Completed;
		}
		else
		{
	     state[0] = Cancelled;
		}
		OrderDetailsBean ODB = new OrderDetailsBean();
		OrderDetailsBean ODB1 = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();

		try{

			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

            DBU.getOrderDetails(CLI, "EVGConfirmAppointment", ODB);
			DBU.getOrderDetails_EVGCancel(CLI, "EVGCancelAppointment", ODB1);

			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\EVGProvision.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i =0; i<1;i++){

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_TARGETGWYCMDID","\n<ns0:targetGatewayCommandId>M_gwyCmdID_EVG</ns0:targetGatewayCommandId>");
				newtext = newtext.replaceAll("M_OTHERATTRIBUTES","\n<ns0:Attribute><ns0:name>supplierReasonCode</ns0:name><ns0:value>000</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>supplierReasonDescription</ns0:name><ns0:value>Job Complete No issues</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>EVGReasonCode</ns0:name><ns0:value>EVG_REAS_0001</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>EVGReasonReasonDescription</ns0:name><ns0:value>Job Completed</ns0:value></ns0:Attribute>");


				newtext = newtext.replaceAll("M_cmdInstID_EVG",ODB1.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_EVG",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_serviceResellerId","569");
			
			    newtext = newtext.replaceAll("M_STATE",state[i]);
				
			    newtext = newtext.replaceAll("M_evg_appointmentId",AB.getAPPOINTMENTID());
				newtext = newtext.replaceAll("M_evg_appointmentDate", AB.getSLOTDATE());
				newtext = newtext.replaceAll("M_evg_apptStartTime",AB.getSLOTSTARTTIME());
				newtext = newtext.replaceAll("M_evg_apptEndTime",AB.getSLOTENDTIME());
				newtext = newtext.replaceAll("M_sup_visit_ref","123456");

				newtext = newtext.replaceAll("M_DateTime",date);
				
				//newtext = newtext.replaceAll("M_TARGETGWYCMDID","\n<ns0:targetGatewayCommandId>M_gwyCmdID_EVG</ns0:targetGatewayCommandId>");
			//	newtext = newtext.replaceAll("M_OTHERATTRIBUTES","\n<ns0:Attribute><ns0:name>supplierReasonCode</ns0:name><ns0:value>000</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>supplierReasonDescription</ns0:name><ns0:value>Job Complete No issues</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>EVGReasonCode</ns0:name><ns0:value>EVG_REAS_0001</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>EVGReasonReasonDescription</ns0:name><ns0:value>Job Completed</ns0:value></ns0:Attribute>");

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_EVG-CAN_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_EVG-CAN_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("EVG Cancel Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("EVG file genration", "EVG file generation error " + e.getMessage());

		}finally{

		}
	}


	public void EVGCancel_AfterConfirm(String CLI) throws Exception{

		String [] state = {Cancelled};


		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();


		try{

			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

			DBU.getOrderDetails(CLI, "EVGConfirmAppointment", ODB);
			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\EVGCancelUpdate.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i =0; i<1;i++){

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );



				newtext = newtext.replaceAll("M_TARGETGWYCMDID","\n<ns0:targetGatewayCommandId>M_gwyCmdID_EVG</ns0:targetGatewayCommandId>");
				newtext = newtext.replaceAll("M_OTHERATTRIBUTES","\n<ns0:Attribute><ns0:name>supplierReasonCode</ns0:name><ns0:value>000</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>supplierReasonDescription</ns0:name><ns0:value>Job Complete No issues</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>EVGReasonCode</ns0:name><ns0:value>EVG_REAS_0001</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>EVGReasonReasonDescription</ns0:name><ns0:value>Job Completed</ns0:value></ns0:Attribute>");


				newtext = newtext.replaceAll("M_cmdInstID_EVG",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_EVG",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_serviceResellerId","569");
				newtext = newtext.replaceAll("M_STATE",state[i]);

				newtext = newtext.replaceAll("M_evg_appointmentId",AB.getAPPOINTMENTID());
				newtext = newtext.replaceAll("M_evg_appointmentDate", AB.getSLOTDATE());
				newtext = newtext.replaceAll("M_evg_apptStartTime",AB.getSLOTSTARTTIME());
				newtext = newtext.replaceAll("M_evg_apptEndTime",AB.getSLOTENDTIME());
				newtext = newtext.replaceAll("M_sup_visit_ref","123456");

				newtext = newtext.replaceAll("M_DateTime",date);

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_EVG-CAN_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_EVG-CAN_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

			}
			Report.fnReportPass("EVG Cancel Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("EVG file generation", "EVG file generation error " + e.getStackTrace());

		}finally{

		}
	}

	public void EVGProvide_SoftCancel(String FinalState, String CLI,boolean SoftCancel) throws Exception{
		String [] state=new String[1];
		
		if(SoftCancel)
		{
			 state[0] = SoftCancelled;
		}
		
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		AppointmentBean AB = new AppointmentBean();


		try{

			String 	date = null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

			DBU.getOrderDetails(CLI, "EVGConfirmAppointment", ODB);
			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\EVGProvision.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i =0; i<2;i++){

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );


				if(state[i].equalsIgnoreCase(Confirmed)){
					newtext = newtext.replaceAll("M_TARGETGWYCMDID","");	
					newtext = newtext.replaceAll("M_OTHERATTRIBUTES","");
				}else
				{
					newtext = newtext.replaceAll("M_TARGETGWYCMDID","\n<ns0:targetGatewayCommandId>M_gwyCmdID_EVG</ns0:targetGatewayCommandId>");
					newtext = newtext.replaceAll("M_OTHERATTRIBUTES","\n<ns0:Attribute><ns0:name>supplierReasonCode</ns0:name><ns0:value>000</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>supplierReasonDescription</ns0:name><ns0:value>Job Complete No issues</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>EVGReasonCode</ns0:name><ns0:value>EVG_REAS_0001</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>EVGReasonReasonDescription</ns0:name><ns0:value>Job Completed</ns0:value></ns0:Attribute>");
				}

				newtext = newtext.replaceAll("M_cmdInstID_EVG",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_EVG",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_serviceResellerId","569");
				newtext = newtext.replaceAll("M_STATE",state[i]);

				newtext = newtext.replaceAll("M_evg_appointmentId",AB.getAPPOINTMENTID());
				newtext = newtext.replaceAll("M_evg_appointmentDate", AB.getSLOTDATE());
				newtext = newtext.replaceAll("M_evg_apptStartTime",AB.getSLOTSTARTTIME());
				newtext = newtext.replaceAll("M_evg_apptEndTime",AB.getSLOTENDTIME());
				newtext = newtext.replaceAll("M_sup_visit_ref","123456");

				newtext = newtext.replaceAll("M_DateTime",date);

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_EVG_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_EVG_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("EVG Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("EVG file genration", "EVG file generation error " + e.getMessage());

		}finally{

		}
	}
public void EVGAppointmentRejection( String CLI) throws Exception{
		
		
	String [] state = {Rejected};


	OrderDetailsBean ODB = new OrderDetailsBean();
	DbUtilities DBU = new DbUtilities(Report);
	AppointmentBean AB = new AppointmentBean();


	try{

		String 	date = null;
		date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
		String SuppRef=null;
		String CPWNRef=null;
		SuppRef = Reusables.getdateFormat("mmss", 0); 
		CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
		
		
		DBU.getOrderDetails(CLI, "EVGConfirmAppointment", ODB);
		DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);

		File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\EVGRejection.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "", oldtext = "",template="";
		while((line = reader.readLine()) != null)
			template += line + "\r\n";
		reader.close();

		for(int i =0; i<1;i++){

			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

			oldtext=template;

			String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
			newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
			newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
			newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
			newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

			newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID());
			newtext = newtext.replaceAll("M_gwyCmdID_LLU",ODB.getPROVCMDGWYCMDID());
			newtext = newtext.replaceAll("M_CLI",CLI);
			newtext = newtext.replaceAll("M_serviceResellerId","569");
			newtext = newtext.replaceAll("M_STATE",state[i]);

			newtext = newtext.replaceAll("M_BT_appointmentId",AB.getAPPOINTMENTID());
			newtext = newtext.replaceAll("M_BT_appointmentDate", AB.getSLOTDATE());
			newtext = newtext.replaceAll("M_BT_appointmentSlotStartTime",AB.getSLOTSTARTTIME());
			newtext = newtext.replaceAll("M_BT_appointmentSlotEndTime",AB.getSLOTENDTIME());
			newtext = newtext.replaceAll("M_sup_visit_ref",SuppRef);
			newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);

			newtext = newtext.replaceAll("M_DateTime",date);

			FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_EVG_APP_REJ"+state[i]+".txt");
			writer.write(newtext);writer.flush();writer.close();
			MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_EVG_APP_REJ"+state[i]+".txt");
			Thread.sleep(PROV_TIME);


			}
			Report.fnReportPass("EVG Appointment Rejection Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("EVG file genration", "EVG file generation error " + e.getMessage());

		}finally{

		}
	}

}




