package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.EMS.MessageTester;
import com.jcraft.jsch.Random;
import com.Utils.ReadExcelSheet;

public class FetchALK implements Constants{
	public static Reporter Report;
	public FetchALK(Reporter report)
	{
		report=Report;
	}

	public static String GETALK_SAD_MNAREQ(String Postcode) throws Exception
	{
		int EndRow = 1;//Row Number -1

		String WorkbookLocation	= System.getProperty("user.dir")+"\\DataProvider\\InputSheet.xls";
		System.out.println(System.getProperty("user.dir")+"\\DataProvider\\InputSheet.xls");
		ReadExcelSheet RX = new ReadExcelSheet(Report);
		String SheetName = "POSTCODE";
		String line = "", oldtext = "",template="",template1="",template2="";
		FileWriter writer;
		String address="";
		String ALK="";
		/***************************** COMMON VARIABLES INIT***************************/
		String EMMSheetName = "EMM";
		String EMM_HOST=LoadEnvironment.EMM_HOSTNAME;
		String EMM_PORT=LoadEnvironment.EMM_PORT;
		String EMM_USER=LoadEnvironment.EMM_USERNAME;
		String EMM_PASS=LoadEnvironment.EMM_PASSWORD;
		String ENV    =LoadEnvironment.ENV;
		//		String BuildingNo=RX.ReadFromExcelWithRows(WorkbookLocation, SheetName, "1", "BuildingNo");
		//		String ThoroughFare=RX.ReadFromExcelWithRows(WorkbookLocation, SheetName, "1", "ThoroughFare");




		/*********************Get ALKS**********************************************************/

		String GETALK = "POSTCODE,ALK";
		//Read Template

		File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\SearchAddressDetails.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));

		while((line = reader.readLine()) != null)
			template += line + "\r\n";
		reader.close();

		oldtext = template;

		String POSTCODE=Postcode;
		oldtext= oldtext.replaceAll("M_ENV", ENV);
		oldtext= oldtext.replaceAll("M_emm_hostname", EMM_HOST);
		oldtext= oldtext.replaceAll("M_emm_port", EMM_PORT);
		oldtext= oldtext.replaceAll("M_emm_username", EMM_USER);
		oldtext= oldtext.replaceAll("M_emm_password", EMM_PASS);

		oldtext= oldtext.replaceAll("M_POSTCODE", POSTCODE);

		writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\SearchAddress"+POSTCODE+".txt");
		writer.write(oldtext);writer.flush();writer.close();

		String response = MessageTester.MessageTester_test_Synchronous(System.getProperty("user.dir")+"\\ProvisioningUpdates\\SearchAddress"+POSTCODE+".txt");
		writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\SearchAddress"+POSTCODE+"_RESP.txt");
		writer.write(response);writer.flush();writer.close();
		//addressDetails[buildingNumber='"+BuildingNo+"']/uprn
		while(ALK.equals(""))
		{
			try {
				address=XMLVerify_Modified(System.getProperty("user.dir")+"\\ProvisioningUpdates\\SearchAddress"+POSTCODE+"_RESP.txt","//addressDetails");
				address=address.replace("#text:", "");
				address=address.replaceAll("                            ", "");
				System.out.println(address);
				writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\AddressDetails.txt");
				writer.write(address);writer.flush();writer.close();

				System.out.println("POSTCODE COMPLETE -->"+POSTCODE);
				//			System.out.println(uprn);

				/********************Match Network Address*********************************************/
				//Read Template LLUAC 
				template="";
				file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\MatchNetworkAddress.txt");
				reader = new BufferedReader(new FileReader(file));

				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();

				//			String[] result1 = line.split(",");	
				oldtext = template;
				POSTCODE=Postcode;
				//			
				oldtext= oldtext.replaceAll("M_ENV", ENV);
				oldtext= oldtext.replaceAll("M_emm_hostname", EMM_HOST);
				oldtext= oldtext.replaceAll("M_emm_port", EMM_PORT);
				oldtext= oldtext.replaceAll("M_emm_username", EMM_USER);
				oldtext= oldtext.replaceAll("M_emm_password", EMM_PASS);

				oldtext= oldtext.replaceAll("M_UPRN", "<ns1:uprn xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Address\">"+FilterDetails("uprn")+"</ns1:uprn>");
				if(address.contains("buildingName"))
					oldtext= oldtext.replaceAll("M_buildingName", "<ns1:buildingName xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Address\">"+FilterDetails("buildingName")+"</ns1:buildingName>");
				else
					oldtext= oldtext.replaceAll("M_buildingName","");
				if(address.contains("buildingNumber"))
					oldtext= oldtext.replaceAll("M_buildingNumber", "<ns1:buildingNumber xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Address\">"+FilterDetails("buildingNumber")+"</ns1:buildingNumber>");					
				else
					oldtext= oldtext.replaceAll("M_buildingNumber","");
				oldtext= oldtext.replaceAll("M_thoroughfareName", "<ns1:thoroughfareName xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Address\">"+FilterDetails("thoroughfareName")+"</ns1:thoroughfareName>");
				oldtext= oldtext.replaceAll("M_postcode",  "<ns1:postcode xmlns:ns1=\"http://xmlns.cpw.co.uk/CPW/CDM/Address\">"+FilterDetails("postcode")+"</ns1:postcode>");

				writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\MNA_REQ"+FilterDetails("uprn")+".txt");
				writer.write(oldtext);writer.flush();writer.close();

				response = MessageTester.MessageTester_test_Synchronous(System.getProperty("user.dir")+"\\ProvisioningUpdates\\MNA_REQ"+FilterDetails("uprn")+".txt");
				//MNA_RESP"+FilterDetails("uprn")
				writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\MNA_RESP"+FilterDetails("uprn")+".xml");
				writer.write(response);writer.flush();writer.close();	

				ALK=XMLVerify_Modified2(System.getProperty("user.dir")+"\\ProvisioningUpdates\\MNA_RESP"+FilterDetails("uprn")+".xml","//*[local-name()='alk']");
			} catch (Exception e) {
				if(ALK==null)
				{
					ALK="";
				}
				else{
					ALK="ads";
					throw e;
				}
			}
		}
		System.out.println("Alk is "+ALK);
		return ALK+":"+FilterDetails("buildingNumber")+":"+FilterDetails("buildingName")+":"+FilterDetails("thoroughfareName");
	}


	public static String FilterDetails(String fieldname) throws Exception
	{
		String value="";
		String[] linesplit=new String[2]; 
		File file = new File(System.getProperty("user.dir")+"\\ProvisioningUpdates\\AddressDetails.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line="";
		try{
			while((line = reader.readLine()) != null)
			{
				linesplit=line.split(":");
				if(linesplit[0].equals(fieldname))
				{
					value=linesplit[1];
					System.out.println(value);
				}
			}

		}	
		catch(Exception e)
		{
			System.out.println(e.getLocalizedMessage());
		}

		return value;

	}
	@SuppressWarnings("finally")
	public static String  getXMLdata1(String XMLContent, String NodeName, String TAG,String ALK) throws Exception{
		int ReturnXMLvalue = 0;
		String CeasePending = "";
		try{
			java.io.FileWriter fw = new java.io.FileWriter(System.getProperty("user.dir")+"\\Output\\LLU_ALK_RESP.xml");
			fw.write(XMLContent);
			fw.close();

			File fXmlFile = new File(System.getProperty("user.dir")+"\\Output\\LLU_ALK_RESP.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();
			XPath xPath =  XPathFactory.newInstance().newXPath();

			NodeList nList = doc.getElementsByTagName(NodeName);

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					ReturnXMLvalue = ReturnXMLvalue+eElement.getElementsByTagName(TAG).getLength();


				}
			}
			if(TAG.equalsIgnoreCase("ns0:WorkingLineDetails")){
				nList = doc.getElementsByTagName("ns0:WorkingLineDetails");
				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);
					Element eElement = (Element) nNode;
					CeasePending=CeasePending+ALK+","+eElement.getElementsByTagName("ns0:linePendingCeaseOrder").item(0).getTextContent()+"M_";
				}
			}



		}catch(Exception e){
		}finally{
			return Integer.toString(ReturnXMLvalue)+"|"+CeasePending;
		}
	}

	@SuppressWarnings("finally")
	public static String  getXMLdata2(String XMLContent, String NodeName, String TAG,String ToCheck) throws Exception{
		int ReturnXMLvalue = 0;
		String CeasePending = "";

		try{
			java.io.FileWriter fw = new java.io.FileWriter(System.getProperty("user.dir")+"\\Output\\NGA_ALK_RESP.xml");
			fw.write(XMLContent);
			fw.close();

			File fXmlFile = new File(System.getProperty("user.dir")+"\\Output\\NGA_ALK_RESP.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();
			XPath xPath =  XPathFactory.newInstance().newXPath();

			NodeList nList = doc.getElementsByTagName(NodeName);

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					if(eElement.getElementsByTagName(TAG).item(0).getTextContent().equalsIgnoreCase(ToCheck)){

						//System.out.println("TO CHECK was ->"+ToCheck+" "+eElement.getElementsByTagName(TAG).item(0).getTextContent());
						ReturnXMLvalue = ReturnXMLvalue+eElement.getElementsByTagName(TAG).getLength();
					}

				}
			}
			//System.out.println(ReturnXMLvalue);

		}catch(Exception e){
		}finally{
			return Integer.toString(ReturnXMLvalue)+"|"+CeasePending;
		}
	}
	@SuppressWarnings("finally")
	public static String getXMLdata3(String XMLContent, String NodeName, String TAG) throws Exception{
		String ReturnXMLvalue = "";

		try{
			java.io.FileWriter fw = new java.io.FileWriter(System.getProperty("user.dir")+"\\Output\\LLU_ALK_RESP.xml");
			fw.write(XMLContent);
			fw.close();

			File fXmlFile = new File(System.getProperty("user.dir")+"\\Output\\LLU_ALK_RESP.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();


			NodeList nList = doc.getElementsByTagName(NodeName);
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					ReturnXMLvalue = ReturnXMLvalue+eElement.getElementsByTagName(TAG).item(0).getTextContent()+"-"
							+eElement.getElementsByTagName("ns0:accessLineStatus").item(0).getTextContent()+"|";

				}
			}
		}catch(Exception e){

		}finally{
			return ReturnXMLvalue;
		}

	}

	public static String XMLVerify_Modified(String LogFilePath,String TargetNodes) throws Exception{
		File file = new File(LogFilePath);
		DocumentBuilderFactory dbFactory 
		= DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		String temp="";
		String value="";
		dBuilder = dbFactory.newDocumentBuilder();
		try{
			Document doc = dBuilder.parse(file);   

			doc.getDocumentElement().normalize();
			XPath xPath =  XPathFactory.newInstance().newXPath();
			String ToCheck=TargetNodes;
			NodeList nl =(NodeList) xPath.compile(ToCheck).evaluate(doc, XPathConstants.NODE);
			int n=nl.getLength();
			System.out.println(n);
			java.util.Random r= new java.util.Random();
			n=r.nextInt(n);
			while(n==0)
			{
				n=r.nextInt(n);
			}

			System.out.println(n);
			ToCheck="//addressDetails["+n+"]";

			Node nList = (Node) xPath.compile(ToCheck).evaluate(doc, XPathConstants.NODE);
			NodeList nodelist =nList.getChildNodes();
			System.out.println(nodelist.getLength());
			for(int i=1;i<=nodelist.getLength();i++)
			{
				Node node=nodelist.item(i);
				//				System.out.println(i);
				//				System.out.println(nodelist.item(i).getTextContent());
				temp=temp+node.getNodeName()+":"+node.getTextContent()+"\r\n"; 	
				//				 System.out.println(temp);
			}	
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());

		}
		return temp;

	}
	public static String XMLVerify_Modified2(String LogFilePath,String TargetNodes) throws Exception{
		File file = new File(LogFilePath);
		DocumentBuilderFactory dbFactory 
		= DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		String value="";
		dBuilder = dbFactory.newDocumentBuilder();
		try{
			Document doc = dBuilder.parse(file);   
			doc.getDocumentElement().normalize();
			XPath xPath =  XPathFactory.newInstance().newXPath();
			String ToCheck=TargetNodes;

			Node nList = (Node) xPath.compile(ToCheck).evaluate(doc, XPathConstants.NODE);
			value=nList.getTextContent();
		}
		catch(Exception e)
		{
			e.printStackTrace();    	
		}
		return value;

	}
}

