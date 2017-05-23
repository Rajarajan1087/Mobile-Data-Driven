package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Utils.Reusables;

public class IPTVProvision implements Constants{
	
	public Reporter Report;



	/**
	 * @param report
	 */
	public IPTVProvision(Reporter report) {
		Report = report;
	}

public void IPTVHorizon(String FinalState, String CLI,String CPWNRef) throws Exception{
		
		String [] state = {Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		
		try{

			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPTVHorizontal.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i =0; i<1;i++){

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				 newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				 newtext = newtext.replaceAll("M_STATE",state[i]);

				newtext = newtext.replaceAll("M_DateTime",date);

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPTV_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPTV_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("IPTV Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("IPTV file genration", "IPTV file generation error " + e.getMessage());

		}finally{

		}
	}

public void IPTVHorizonFTTP(String FinalState, String CLI,String CPWNRef) throws Exception{
	
	String [] state = {Completed};

	OrderDetailsBean ODB = new OrderDetailsBean();
	DbUtilities DBU = new DbUtilities(Report);
	
	try{

		String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
		String CPWN = NameGenerator.randomCPWNRef(6);

		File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPTVHorizontalFTTP.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "", oldtext = "",template="";
		while((line = reader.readLine()) != null)
			template += line + "\r\n";
		reader.close();

		for(int i =0; i<1;i++){

			oldtext=template;

			String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
			newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
			newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
			newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
			newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
			newtext = newtext.replaceAll("M_CPWN",CPWN );
			 newtext = newtext.replaceAll("M_CASRValue",CPWNRef);
			 newtext = newtext.replaceAll("M_STATE",state[i]);

			newtext = newtext.replaceAll("M_DateTime",date);

			FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPTV_"+state[i]+".txt");
			writer.write(newtext);writer.flush();writer.close();
			MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPTV_"+state[i]+".txt");
			Thread.sleep(PROV_TIME);

			if(FinalState.equalsIgnoreCase(state[i]))
				break;

		}
		Report.fnReportPass("IPTV Provisioning complete for "+ CLI);
	} catch (Exception e) {
		Report.fnReportFailAndTerminateTest("IPTV file genration", "IPTV file generation error " + e.getMessage());

	}finally{

	}
}

	public void IPTVProvide(String FinalState, String CLI) throws Exception{
		String [] state = {Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
		
		try{

			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

			DBU.getOrderDetails(CLI, "IPTVProvide", ODB);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPTVProvide.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i =0; i<1;i++){

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				 newtext = newtext.replaceAll("M_cmdInstID_CPS",ODB.getPROVCMDINSTID());
				 newtext = newtext.replaceAll("M_gwyCmdID_IPTV",ODB.getPROVCMDGWYCMDID());
				 newtext = newtext.replaceAll("M_CLI",CLI);
				 newtext = newtext.replaceAll("M_serviceResellerId","569");
				 newtext = newtext.replaceAll("M_STATE",state[i]);

				newtext = newtext.replaceAll("M_DateTime",date);

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPTV_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_IPTV_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("IPTV Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("IPTV file genration", "IPTV file generation error " + e.getMessage());

		}finally{

		}
	}
}
