package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Utils.Reusables;


public class USMProvision  implements Constants{
	public Reporter Report;

	public USMProvision(Reporter report) {
		Report = report;
	}

	public void USM_OBC(String Str_FinalState, String Str_CLI, String Str_ServiceResellerID,String Str_USMType) throws Exception{
		String [] state = {Pending,Suspended,Completed};
		String date="";
		try {
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\USM_OBC.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",oldtext1 = "";
			while((line = reader.readLine()) != null)
				oldtext1 += line + "\r\n";
			reader.close();

			String UniqueNumber = Reusables.getdateFormat("yyyyMMdd", 0);
			for(int i=0;i<7;i++){	
				oldtext=oldtext1;
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );


				newtext = newtext.replaceAll("M_gwyCmdID_CPS",UniqueNumber);
				newtext = newtext.replaceAll("M_CLI",Str_CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",Str_ServiceResellerID);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);

				if(Str_USMType.equalsIgnoreCase("CPSRetain")){
					newtext = newtext.replaceAll("M_USM_TYPE","CPSRetain");
				}else if(Str_USMType.equalsIgnoreCase("ChangeOfAddressSameCLI")){
					newtext = newtext.replaceAll("M_USM_TYPE","95");
				}

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+Str_CLI+"_USM_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+Str_CLI+"_USM_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(Str_FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("USM Provisioning complete - >  CLI "+ Str_CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("USM file genration", "LLUCease file generation error " + e.getMessage());

		}finally{

		}

	}




}
