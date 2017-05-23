package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Utils.Reusables;

public class TMIGProvision implements Constants{
	public Reporter Report;
	/**
	 * @param reporter
	 */
	public TMIGProvision(Reporter reporter) {
		Report = reporter;
	}
	
public void T_Mig_SMPFT3(String CLI, String BRANDID, String toPackID) throws Exception{
		
		try {
			DbUtilities DBU = new DbUtilities(Report);
			CustomerBean CB = new CustomerBean();
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			String 	Futuredate = Reusables.getdateFormat("yyyy-MM-dd", 12);
			
			DBU.getCustomerDetails(CLI, CB);
			DBU.getBranchAndExchangeCode_ByCLI(CLI, CB);
			
			if(CB.getBranchCode().isEmpty()||CB.getExchangeCode().isEmpty()||CB.getBranchCode()==null||CB.getExchangeCode()==null){
				DBU.updateBranchAndExchangeCode_ByCLI(CLI, "1231", "SSLCROC");
				CB.setBranchCode("1231");
				CB.setExchangeCode("SSLCROC");
			}
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\T-MigSMPFT3.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<1;i++){	

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				System.out.println(CB.getBranchCode());
				newtext = newtext.replaceAll("M_Branchcode",CB.getBranchCode());
				newtext = newtext.replaceAll("M_Exchangecode",CB.getExchangeCode());
				newtext = newtext.replaceAll("M_accountId",CB.getAccountNumber());
				newtext = newtext.replaceAll("M_customerId",CB.getCustomerNumber());
				
				newtext = newtext.replaceAll("M_BRANDID",BRANDID);
				newtext = newtext.replaceAll("M_CLI",CLI );
				
				newtext = newtext.replaceAll("M_T_PACKAGEID",toPackID );
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_Date",Futuredate );
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_T-MigSMPFT3_.txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_T-MigSMPFT3_.txt");
				
				Thread.sleep(PROV_TIME);

				
			}
			Report.fnReportPass("T-MigSMPFT3 Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("T-MigSMPFT3 file genration", "T-MigSMPFT3 file generation error " + e.getMessage());

		}finally{

		}
		
		
	}

public void T_Mig_Singleton_SMPFT3(String CLI, String BRANDID, String toPackID) throws Exception{
	
	try {
		DbUtilities DBU = new DbUtilities(Report);
		CustomerBean CB = new CustomerBean();
		String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
		String 	Futuredate = Reusables.getdateFormat("yyyy-MM-dd", 12);
		
		DBU.getCustomerDetails(CLI, CB);
		DBU.getBranchAndExchangeCode_ByCLI(CLI, CB);
		
		if(CB.getBranchCode().isEmpty()||CB.getExchangeCode().isEmpty()||CB.getBranchCode()==null||CB.getExchangeCode()==null){
			DBU.updateBranchAndExchangeCode_ByCLI(CLI, "1231", "SSLCROC");
			CB.setBranchCode("1231");
			CB.setExchangeCode("SSLCROC");
		}
		
		File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\T-Mig_Singleton_SMPFT3.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "", oldtext = "",template="";
		while((line = reader.readLine()) != null)
			template += line + "\r\n";
		reader.close();
		
		for(int i=0;i<1;i++){	

			oldtext=template;

			String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
			newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
			newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
			newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
			newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
			
			System.out.println(CB.getBranchCode());
			newtext = newtext.replaceAll("M_Branchcode",CB.getBranchCode());
			newtext = newtext.replaceAll("M_Exchangecode",CB.getExchangeCode());
			newtext = newtext.replaceAll("M_accountId",CB.getAccountNumber());
			newtext = newtext.replaceAll("M_customerId",CB.getCustomerNumber());
			
			newtext = newtext.replaceAll("M_BRANDID",BRANDID);
			newtext = newtext.replaceAll("M_CLI",CLI );
			
			newtext = newtext.replaceAll("M_T_PACKAGEID",toPackID );
			newtext = newtext.replaceAll("M_DateTime",date);
			newtext = newtext.replaceAll("M_Date",Futuredate );
			
			FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_T-Mig_Singleton_SMPFT3_.txt");
			writer.write(newtext);writer.flush();writer.close();
			MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_T-Mig_Singleton_SMPFT3_.txt");
			
			Thread.sleep(PROV_TIME);

			
		}
		Report.fnReportPass("T-MigSMPFT3Singleton Provisioning complete for "+ CLI);
	} catch (Exception e) {
		Report.fnReportFailAndTerminateTest("T-MigSMPFT3Singleton file genration", "T-MigSMPFT3Singleton file generation error " + e.getMessage());

	}finally{

	}
	
	
}

	public void T_Mig_LLU(String CLI, String BRANDID, String toPackID) throws Exception{
		
		try {
			DbUtilities DBU = new DbUtilities(Report);
			CustomerBean CB = new CustomerBean();
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			String 	Futuredate = Reusables.getdateFormat("yyyy-MM-dd", 12);
			
			DBU.getCustomerDetails(CLI, CB);
			DBU.getBranchAndExchangeCode_ByCLI(CLI, CB);
			
			if(CB.getBranchCode().isEmpty()||CB.getExchangeCode().isEmpty()||CB.getBranchCode()==null||CB.getExchangeCode()==null){
				DBU.updateBranchAndExchangeCode_ByCLI(CLI, "1231", "SSLCROC");
				CB.setBranchCode("1231");
				CB.setExchangeCode("SSLCROC");
			}
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\T-MigLLU.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<1;i++){	

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				System.out.println(CB.getBranchCode());
				newtext = newtext.replaceAll("M_Branchcode",CB.getBranchCode());
				newtext = newtext.replaceAll("M_Exchangecode",CB.getExchangeCode());
				newtext = newtext.replaceAll("M_accountId",CB.getAccountNumber());
				newtext = newtext.replaceAll("M_customerId",CB.getCustomerNumber());
				
				newtext = newtext.replaceAll("M_BRANDID",BRANDID);
				newtext = newtext.replaceAll("M_CLI",CLI );
				
				newtext = newtext.replaceAll("M_T_PACKAGEID",toPackID );
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_Date",Futuredate );
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_T-MigLLU_.txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_T-MigLLU_.txt");
				
				Thread.sleep(PROV_TIME);

				
			}
			Report.fnReportPass("T-MigLLU Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("T-MigLLU file genration", "T-MigLLU file generation error " + e.getMessage());

		}finally{

		}
		
		
	}

}
