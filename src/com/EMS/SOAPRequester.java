package com.EMS;

import java.net.HttpURLConnection; 
import java.net.URL;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream; 
import java.io.InputStreamReader;

import com.Engine.Reporter;


public class SOAPRequester{

	/**
	 * @param report
	 */
	public SOAPRequester(Reporter report) {

		Report = report;
	}
	public Reporter Report ;
	/**
	 * @param targetURL = SOAP URL
	 * @param urlParameters = XML CONTENT TO BE SENT
	 * @return 
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("finally")
	public String setSoapMsg(String targetURL, String SoapAction, String urlParameters) throws Exception{

		URL url;
		HttpURLConnection connection = null;  
		StringBuffer response = null;
		try {
			//Create connection
			url = new URL(targetURL);

			// for not trusted site (https)
			// _FakeX509TrustManager.allowAllSSL();
			// System.setProperty("javax.net.debug","all");

			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("SOAPAction", SoapAction);
			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			//Send request
			DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
			wr.writeBytes (urlParameters);
			wr.flush ();
			wr.close ();

			//Get Response    
			InputStream is ;

			if(connection.getResponseCode()<=400){
				is=connection.getInputStream();
				Report.fnReportPass("Soap Request sent sucessfully" + urlParameters );
			}else{
				/* error from server */
				is = connection.getErrorStream();
			} 

			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			response = new StringBuffer(); 
			while((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();

		} catch (Exception e) {
			//	return null;
			e.printStackTrace();
			Report.fnReportFailAndTerminateTest("Soap sender", "Soap Request not sent");

		} finally {
			if(connection != null) {
				connection.disconnect(); 
			}
			return response.toString();
		}
	}
}
