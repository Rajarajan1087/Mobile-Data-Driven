package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Utils.Reusables;

public class CancelUpdate implements Constants {
	public Reporter Report;
	/**
	 * @param report
	 */
	public CancelUpdate(Reporter report) {
		Report = report;
	}

	/**
	 * @param FinalState
	 * @param CLI
	 * @param TargetCommand
	 * @throws Exception
	 */
	public void CancelUpdate_withoutAppointment(String FinalState, String CLI, String TargetCommand,String Serviceresellerid) throws Exception{

		String [] state = {Cancelled};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);


			DBU.getOrderDetails(CLI, TargetCommand, ODB);
			CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
			if(Serviceresellerid=="")
			{
				Serviceresellerid=ODB.getServiceresellerid();
			}
			for(int i=0;i<1;i++){	

				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\CancelUpdate.txt");
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
				newtext = newtext.replaceAll("M_gwyCmdID",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",Serviceresellerid);
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);

				if(TargetCommand.startsWith("LLU")){
					newtext = newtext.replaceAll("M_gwyID","0");
					newtext = newtext.replaceAll("M_accessMethod","3");
				}else if(TargetCommand.startsWith("SMPF")){
					newtext = newtext.replaceAll("M_gwyID","0");
					newtext = newtext.replaceAll("M_accessMethod","9");
				}else if(TargetCommand.startsWith("OBC")){
					newtext = newtext.replaceAll("M_gwyID","0");
					newtext = newtext.replaceAll("M_accessMethod","1");
				}else if(TargetCommand.startsWith("IPS")){
					newtext = newtext.replaceAll("M_gwyID","0");
					newtext = newtext.replaceAll("M_accessMethod","4");
				}else if(TargetCommand.startsWith("VM")){
					newtext = newtext.replaceAll("M_gwyID","0");
					newtext = newtext.replaceAll("M_accessMethod","8");
				}else if(TargetCommand.startsWith("WLR3")){
					newtext = newtext.replaceAll("M_gwyID","2");
					newtext = newtext.replaceAll("M_accessMethod","7");
				}else if(TargetCommand.contains("CPE")){
					newtext = newtext.replaceAll("M_gwyID","12");
					newtext = newtext.replaceAll("M_accessMethod","11");
				}else{
					throw new RuntimeException("NO COMMAND");
				}

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CancelUpdate_"+TargetCommand+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CancelUpdate_"+TargetCommand+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("CancelUpdate Provisioning complete - >  CLI "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("CancelUpdate file genration", "CancelUpdate file generation error " + e.getMessage());

		}finally{

		}
	}

	public void CancelUpdate_withAppointment(String FinalState, String CLI, String TargetCommand) throws Exception{

		String [] state = {Cancelled};

		OrderDetailsBean ODB = new OrderDetailsBean();
		AppointmentBean AB = new AppointmentBean();
		DbUtilities DBU = new DbUtilities(Report);
		try {
			String CPWNRef = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);


			DBU.getOrderDetails(CLI, TargetCommand, ODB);
			DBU.getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);
			CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);

			for(int i=0;i<1;i++){	

				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\CancelUpdateWithAppointment.txt");
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
				
				newtext = newtext.replaceAll("M_BT_appointmentId",AB.getAPPOINTMENTID());
				newtext = newtext.replaceAll("M_BT_appointmentDate",AB.getSLOTDATE());
				newtext = newtext.replaceAll("M_BT_appointmentSlotStartTime",AB.getSLOTSTARTTIME());
				newtext = newtext.replaceAll("M_BT_appointmentSlotEndTime",AB.getSLOTENDTIME());
				

				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CancelUpdateWithAppointment_"+TargetCommand+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CancelUpdateWithAppointment_"+TargetCommand+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("CancelUpdateWithAppointment Provisioning complete - >  CLI "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("CancelUpdateWithAppointment file genration", "CancelUpdateWithAppointment file generation error " + e.getMessage());

		}finally{

		}
	}

}


