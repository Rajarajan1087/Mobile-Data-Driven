package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Utils.Reusables;

public class Rejection implements Constants{
	public Reporter Report;

	/**
	 * @param report
	 */
	public Rejection(Reporter report) {
		Report = report;
	}

	public void RejectionTrigger(String CLI, String AccessMethod, String CommandName, String RejectionCode,
			String RejectionDescription) throws Exception{

		String	State[] = {Rejected};

		try {
			
			String 	DateTime = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			OrderDetailsBean ODB = new OrderDetailsBean();
			DbUtilities DBU = new DbUtilities(Report);
			String gateWayID = null;
			String AM = null;

			DBU.getOrderDetails(CLI, CommandName, ODB);
		
			if((AccessMethod.equalsIgnoreCase("FTTP"))||(AccessMethod.equalsIgnoreCase("LLU"))){
				gateWayID = "0";
				AM = "3";
			}else if(AccessMethod.equalsIgnoreCase("OBC")){
				gateWayID = "0";
				AM = "1";
			}else if(AccessMethod.equalsIgnoreCase("T3SMPF")){
				gateWayID = "0";
				AM = "12";
			}else if(AccessMethod.equalsIgnoreCase("IPS")){
				gateWayID = "0";
				AM = "4";
			}else if(AccessMethod.equalsIgnoreCase("VM")){
				gateWayID = "0";
				AM = "8";
			}else if(AccessMethod.equalsIgnoreCase("WLR3")){
				gateWayID = "2";
				AM = "7";
			}else if(AccessMethod.equalsIgnoreCase("CPEG")){
				gateWayID = "12";
				AM = "11";
			}else if(AccessMethod.equalsIgnoreCase("NGA")){
				gateWayID = "13";
				AM = "15";
			}else if(AccessMethod.equalsIgnoreCase("IPTV")){
				gateWayID = "7";
				AM = "14";
			}else if(AccessMethod.equalsIgnoreCase("EVG")){
				gateWayID = "16";
				AM = "16";
			}else if(AccessMethod.equalsIgnoreCase("SMPF")){
				gateWayID = "3";
				AM = "9";
			}

			for(int i=0;i<1;i++){	

				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\Rejection.txt");
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
				newtext = newtext.replaceAll("M_STATE",State[i]);
				newtext = newtext.replaceAll("M_DateTime",DateTime);

				newtext = newtext.replaceAll("M_gateWayID",gateWayID);
				newtext = newtext.replaceAll("M_AM",AM);

				newtext = newtext.replaceAll("M_REJCODE",RejectionCode);
				newtext = newtext.replaceAll("M_REJDES",RejectionDescription);
				newtext = newtext.replaceAll("M_COMMITTED","");


				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_Rejection_"+CommandName+State[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_Rejection_"+CommandName+State[i]+".txt");
				Thread.sleep(PROV_TIME);

			}
			Report.fnReportPass("Rejection_ Provisioning complete - >  CLI "+ CLI + "  FOR Command-->" + CommandName);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("Rejection_ file genration", "Rejection_ file generation error " + e.getMessage());

		}finally{

		}
	}

	public void Rejections_AOL (String CLI, String RejectionCode,
			String RejectionDescription,String REJORDER) throws Exception
			{
		
		String	State[] = {Rejected};
		
		try 
		{		
		String 	DateTime = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		String gateWayID = null;
		String AM = null;

		DBU.getOrderDetails(CLI, REJORDER, ODB);

		for(int i=0;i<1;i++)
		{	
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\Rejections_AOL.txt");
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
			newtext = newtext.replaceAll("M_STATE",State[i]);
			newtext = newtext.replaceAll("M_DateTime",DateTime);

			newtext = newtext.replaceAll("M_gateWayID",gateWayID);
			newtext = newtext.replaceAll("M_AM",AM);

			newtext = newtext.replaceAll("M_REJCODE",RejectionCode);
			newtext = newtext.replaceAll("M_REJDES",RejectionDescription);
			

			
			if(REJORDER.startsWith("OBC"))
			{
				newtext = newtext.replaceAll("M_ACCESSMETHOD","1");
			}
			else if(REJORDER.startsWith("SMPF"))
			{
				newtext = newtext.replaceAll("M_ACCESSMETHOD","9");
			}
			else if(REJORDER.startsWith("IPS"))
			{
				newtext = newtext.replaceAll("M_ACCESSMETHOD","4");
			}

			
			FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_Rejections_AOL"+State[i]+".txt");
			writer.write(newtext);writer.flush();writer.close();
			

			MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_Rejections_AOL"+State[i]+".txt");
			Thread.sleep(PROV_TIME);
		}
		
		Report.fnReportPass("Rejections_AOL Provisioning complete - >  CLI "+ CLI);
			} 
	catch (Exception e) {
				Report.fnReportFailAndTerminateTest("Rejection_ file genration", "Rejection_ file generation error " + e.getMessage());

			}
	finally{

			}
	}
}
