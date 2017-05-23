package com.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.IOException;
import java.io.InputStreamReader;

public class SampleTest {
	public static void main(String[] args) throws Exception {
		   try {
			   sendGet("http://sales-dev.por.replatformapis.infosys-18.nec.talkdev.co.uk/sales-api/boost/?product=tvplus&boost=SKYSPORTS&portalId=Talktalk");
		   }
		   catch( IOException e ) {
		      System.out.println(e);
		      System.exit(0);
		   }
		}
	
	public static void postRequest() throws IOException{
		try {

			String url	=	"http://sales-dev.por.replatformapis.infosys-18.nec.talkdev.co.uk/sales-api/boost/?product=tvplus&boost=SKYSPORTS&portalId=Talktalk";
			System.out.println(url);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			System.out.println(url);
			String line	=	"";
			// add reuqest header
			con.setReadTimeout(10000);
			con.setConnectTimeout(15000);
			con.setAllowUserInteraction(false);
			con.setRequestMethod("GET");
			con.setRequestProperty("Remote Address", "10.103.2.77:80");
			con.setRequestProperty("GET", "HTTP/1.1");
			con.setRequestProperty("Host", "sales-dev.por.replatformapis.infosys-18.nec.talkdev.co.uk");
			con.setRequestProperty("Connection", "keep-alive");
			con.setRequestProperty("Cache-Control", "max-age=0");
			con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36");
			con.setRequestProperty("Access-Control-Allow-Credentials", "true");
			con.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Content-Length", "73");
//			String urlParameters = getAPIParamValues("product,boost,portalId","tvplus,SKYSPORTS,Talktalk");
			
//			System.out.println(urlParameters);
			// Send post request
			con.setDoInput(true);
			con.setDoOutput(true);
			///////////////////////
			
			
			OutputStream os = con.getOutputStream();
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//			wr.write(urlParameters);
			System.out.println("wr : "+wr.toString());
			wr.flush();
			wr.close();
		
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
				System.out.println("Writing = "+response);
			}
			in.close();

			String responseString = response.toString();
			System.out.println(responseString);

			

		} catch (Exception e) {
			/*Report.fnReportFailAndTerminateTest("Register Fail",
					exMsg.getLocalizedMessage());*/
			e.getLocalizedMessage();
			e.printStackTrace();
		}
		
	}
	public static String sendGet(String Url) throws Exception {

		URL obj = new URL(Url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + Url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
//		System.out.println(response.toString());
		return response.toString();
	}
	
	private static String getAPIParamValues(String param,String value) throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;
	    String[] splitParam	=	param.split(",");	
	    String[] splitValue	=	value.split(",");
	    for (int count=0;count<splitParam.length;count++)
	    {
	        if (first){
	            first = false;
	        }
	        else{
	            result.append("&");
	        }
	        result.append(URLEncoder.encode(splitParam[count], "UTF-8"));
	        result.append("=");
	        result.append(URLEncoder.encode(splitValue[count], "UTF-8"));
	    }
	    return result.toString();
	}
	
	public static void getResponse() throws MalformedURLException,IOException {
	 
	//Code to make a webservice HTTP request
	String responseString = "";
	String outputString = "";
	String wsURL = "https://sales-dev.por.replatformapis.infosys-18.nec.talkdev.co.uk/sales-api/boost";
	URL obj = new URL(wsURL);
	HttpURLConnection httpConn = (HttpURLConnection) obj.openConnection();
	ByteArrayOutputStream bout = new ByteArrayOutputStream();
	String xmlInput = "?category=product";
	/*" <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://litwinconsulting.com/webservices/\">\n" +
	" <soapenv:Header/>\n" +
	" <soapenv:Body>\n" +
	" <web:smallprint>\n" +
	//" <!--Optional:-->\n" +
	//" <web:category>" + "product" + "</web:category>\n" +
	" </web:smallprint>\n" +
	" </soapenv:Body>\n" +
	" </soapenv:Envelope>";*/
	 
	byte[] buffer = new byte[xmlInput.length()];
	buffer = xmlInput.getBytes();
	bout.write(buffer);
	byte[] b = bout.toByteArray();
	//String SOAPAction ="http://sales-dev.por.replatformapis.infosys-18.nec.talkdev.co.uk/sales-api/smallprint/";
	// Set the appropriate HTTP parameters.
	httpConn.setRequestProperty("Content-Length",String.valueOf(b.length));
	httpConn.setRequestProperty("Content-Type", "application/json");
	//httpConn.setRequestProperty("SOAPAction", SOAPAction);
	httpConn.setRequestMethod("POST");
	httpConn.setDoOutput(true);
	httpConn.setDoInput(true);
	OutputStream out = httpConn.getOutputStream();
	//Write the content of the request to the outputstream of the HTTP Connection.
	out.write(b);
	System.out.println(out.toString());
	out.close();
	//Ready with sending the request.
	 
	//Read the response.
	InputStreamReader isr =new InputStreamReader(httpConn.getInputStream());
	BufferedReader in = new BufferedReader(isr);
	 
	//Write the SOAP message response to a String.
	while ((responseString = in.readLine()) != null) {
	outputString = outputString + responseString;
	}
	//Parse the String output to a org.w3c.dom.Document and be able to reach every node with the org.w3c.dom API.
	System.out.println("New Response : "+responseString);
	}
	
	/*public static void latestCodePostResponse() throws ClientProtocolException, IOException, JSONException{
		  HttpClient client = new DefaultHttpClient();
		  HttpPost post = new HttpPost("http://sales-dev.por.replatformapis.infosys-18.nec.talkdev.co.uk/sales-api/smallprint/?");
		  JSONObject  json = new JSONObject ();
		  json.put("category", "product");
		  System.out.println(json.toString());
		  StringEntity input = new StringEntity( json.toString());
		  input.setContentType("application/json");
		  input.setContentEncoding("/?category=product");
		  post.setEntity(input);
		  HttpResponse response = client.execute(post);
		  BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		  String line = "";
		  while ((line = rd.readLine()) != null) {
		   System.out.println(line);
		  }
	}*/
}
