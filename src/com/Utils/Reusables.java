package com.Utils;

/* Script Name: I_RedboxInstant.Reusables
 * Script Author: Srinivas Sanduri
 * Date Created: 10-15-2012
 * Modified By:
 * Date Modified: 10-15-2012
 * Nature of Modification: NA
 * Areas covered in the script 
 * 		1. create New Invitation
 * 		2. get Unique Credit Card
 * 		3. ReadMyExcel
 * 		4. writeMyExcel
 * 		5. Reporter
 * 		6. ResultToExcel
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.Engine.AppiumSetup;

@SuppressWarnings("unused")
public class Reusables extends AppiumSetup{


	public static String getdateFormat(String hint,int days) throws Exception {
		String nextday = null;

		try{
			Date dNow = new Date(); 
			SimpleDateFormat ft =  new SimpleDateFormat (hint);
			String today = ft.format(dNow);
			Date dtoday = ft.parse(today);
			Calendar c = Calendar.getInstance();
			c.setTime(dNow);
			c.add(Calendar.DATE, days);  // number of days to add
			nextday = ft.format(c.getTime());
		}catch (Exception localException)
		{
			System.out.println(localException.getMessage());
			return nextday;
		}
		return nextday;
	}
	
	public static String getdateFormat(String date,String in_format,String out_format) throws Exception {
		String out_date = null;

		try{
			SimpleDateFormat ft =  new SimpleDateFormat (in_format);
			SimpleDateFormat ft_out =  new SimpleDateFormat (out_format);
			Date dNow = ft.parse(date);
			out_date = ft_out.format(dNow);
		}catch (Exception localException)
		{
			System.out.println(localException.getMessage());
			return out_date;
		}
		return out_date;
	}
	
	public static String getDaysFrom(String date,String format,int days) throws Exception{
		String nextday=null;
		try{
 
			SimpleDateFormat ft =  new SimpleDateFormat (format);
			Date dNow = ft.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(dNow);
			c.add(Calendar.DATE, days);  // number of days to add
			nextday = ft.format(c.getTime());
		}catch (Exception localException)
		{
			System.out.println(localException.getMessage());
			return nextday;
		}
		return nextday;
	}
	
	public static String getYearsFrom(String date,String format,int Years) throws Exception{
		String nextday=null;
		try{
 
			SimpleDateFormat ft =  new SimpleDateFormat (format);
			Date dNow = ft.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(dNow);
			c.add(Calendar.YEAR, Years);  // number of Years to add
			nextday = ft.format(c.getTime());
		}catch (Exception localException)
		{
			System.out.println(localException.getMessage());
			return nextday;
		}
		return nextday;
	}
	
	@SuppressWarnings("finally")
	public static String getXMLdata(String XMLContent, String NodeName, String TAG) throws Exception{
		String ReturnXMLvalue = null;
		
		try{
		java.io.FileWriter fw = new java.io.FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\MYXML.xml");
		fw.write(XMLContent);
		fw.close();

		File fXmlFile = new File(System.getProperty("user.dir")+"\\ProvisioningUpdates\\MYXML.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();


		NodeList nList = doc.getElementsByTagName(NodeName);
//		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				ReturnXMLvalue = eElement.getElementsByTagName(TAG).item(0).getTextContent();
				break;
			}
		}
//		if(ReturnXMLvalue==null)
//		{
//			Report.fnReportFail("Got Negative Response with error Message \""+getXMLdata(XMLContent,"<ns1:ErrorDetails>","<ns1:message>")+"\"");
//		}
		}catch(Exception e){
			
		}finally{
			return ReturnXMLvalue;
		}
		
	}

}

