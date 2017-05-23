package com.EMS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.Engine.LoadEnvironment;
import com.Engine.Reporter;



public class AvailabilityChecker{

	public Reporter Report;
	public AvailabilityChecker(Reporter report) {
		Report = report;
	}

	public String SMPF_AC_Check(String CLI) throws Exception{

		File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SMPF_AC_CHECK.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "", AC_CHECK = "";
		while((line = reader.readLine()) != null)
			AC_CHECK += line + "\r\n";
		reader.close();


		//AC_CHECK = AC_CHECK.replaceAll("M_DAA", Date);
		AC_CHECK = AC_CHECK.replaceAll("M_ENV", LoadEnvironment.ENV);
		AC_CHECK = AC_CHECK.replaceAll("M_env",LoadEnvironment.ENV );
		AC_CHECK = AC_CHECK.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
		AC_CHECK = AC_CHECK.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
		AC_CHECK = AC_CHECK.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
		AC_CHECK = AC_CHECK.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
		AC_CHECK = AC_CHECK.replaceAll("M_CLI",CLI);

		Report.fnReportPass("Request Sent IS : - >"+AC_CHECK);
		FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\SMPF_AC_CHECK.txt");
		writer.write(AC_CHECK);writer.flush();writer.close();

		String response = MessageTester.MessageTester_test_Synchronous(System.getProperty("user.dir")+"\\ProvisioningUpdates\\SMPF_AC_CHECK.txt");
		System.out.println("sent sucessfully");


		return response;
	}
}
