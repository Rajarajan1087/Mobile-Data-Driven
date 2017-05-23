package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Utils.Reusables;

public class TVCPEGProvision implements Constants {
	public Reporter Report;

	/**
	 * @param report
	 */
	public TVCPEGProvision(Reporter report) {
		Report = report;
	}

	public void TVCPEGProvide(String FinalState, String CLI) throws Exception{
		String [] state={Requested,Dispatched,"",Completed};
		int Num_State=4;
		if(FinalState.equalsIgnoreCase(Constants.Received))
		{
			state[2]=Received;
			Num_State=3;
		}
		else
		{
			state[2]=Delivered;
		}
		
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{
		
			String 	date =null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

			DBU.getOrderDetails(CLI, "SubmitTVCPE", ODB);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\TVCPEGProvision.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i =0; i<Num_State;i++){
				oldtext = template;
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				newtext = newtext.replaceAll("M_cmdInstID_CPE",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_CPE",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_serviceResellerId","569");
				newtext = newtext.replaceAll("M_STATE",state[i]);
				//newtext=newtext.replaceAll("M_Transaction_id","254126");
				if(state[i].equalsIgnoreCase(Requested)){
					newtext = newtext.replaceAll("M_DISPATCHDATE","");	
				}else{
					newtext = newtext.replaceAll("M_DISPATCHDATE","<ns0:Attribute><ns0:name>dispatchDate</ns0:name><ns0:value>M_DateTime</ns0:value></ns0:Attribute>\n");
				}
				newtext = newtext.replaceAll("M_DateTime",date);
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_TVCPEG_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_TVCPEG_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);
				
				if(FinalState.equalsIgnoreCase(state[i]))
					break;
				
			}
			Report.fnReportPass("TVCPEG Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("TVCPEG file genration", "TVCPEG file generation error " + e.getMessage());

		}finally{

		}
	}
	
}



