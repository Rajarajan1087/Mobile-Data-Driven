package com.EMS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.Engine.LoadEnvironment;
import com.Engine.Reporter;

public class ValidateBankAccount{
	public Reporter  Report;
	
	/**
	 * @param report
	 */
	public ValidateBankAccount(Reporter report) {
		Report = report;
	}

	public String sendValidateBankAccount(String AccountNumber, String SortCode) throws Exception{
		
		String response = null;
		File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\ValidateBankAccount.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "", oldtext = "";
		while((line = reader.readLine()) != null)
			oldtext += line + "\r\n";
		reader.close();
			
		FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\ValidateBankAccount.txt");
		writer.write(oldtext);writer.flush();writer.close();
		
		SOAPRequester SOAP = new SOAPRequester(Report);
		response = SOAP.setSoapMsg("http://"+LoadEnvironment.BW_SERVERIP+":10042/BusinessServices/WebGateway/Payment/BankingServices.serviceagent/UseSOAP",
				"/BusinessServices/WebGateway/Payment/BankingServices.serviceagent/UseSOAP/ValidateBankAccount", oldtext);
						
		return response;
			
	}

}
