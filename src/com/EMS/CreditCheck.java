package com.EMS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Utils.Reusables;


public class CreditCheck{
	/**
	 * @param report
	 */
	public CreditCheck(Reporter report) {
		Report = report;
	}
	public Reporter Report;
	public String SCC() throws Exception{

		String response = "";
		
		File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\CreditCheck.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "", StCreditCheck = "";
		while((line = reader.readLine()) != null)
			StCreditCheck += line + "\r\n";
		reader.close();
		
		FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\CreditCheck.txt");
		writer.write(StCreditCheck);writer.flush();writer.close();
		
		SOAPRequester SOAP = new SOAPRequester(Report);
		response = SOAP.setSoapMsg("http://"+LoadEnvironment.BW_SERVERIP+":10014/BusinessServices/WebGateway/Customer/CreditCheckServices.serviceagent/UseSOAP",
				"/BusinessServices/WebGateway/Customer/CreditCheckServices.serviceagent/UseSOAP/CheckCreditProfile", StCreditCheck);
		
		return response;
	}

}
