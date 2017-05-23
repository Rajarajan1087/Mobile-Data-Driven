package com.Engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import com.Engine.AppiumSetup;

public class iOSWebKit implements Runnable {

	public String getDeviceID = "";
	public String IOSWebkitDebugProxy="";
	AppiumSetup appiumSetUp = new AppiumSetup();

	@Override
	public void run() {
	try{
		
		
		//String ios_web_lit_proxy_runner="/Applications/Appium3.app/Contents/Resources/node_modules/appium/bin/ios-webkit-debug-proxy-launcher.js";
		
		
		
		//String webkitRunner= "ios_webkit_debug_proxy"+ " -c " + udid + ":27753" + " -d";
		//String webkitRunner= ios_web_lit_proxy_runner+ " -c " + "67773889b7def4f5888e81b63a3914ba00427350" + ":27753" + " -d";
		//String webkitRunner= ios_web_lit_proxy_runner+ " -c " + "5aef5ba4cfdc74ccb2caf0e4d8cbf364c6feb0f3" + ":27753" + " -d";
		/*
		Process process = Runtime.getRuntime().exec(webkitRunner);
		if(process != null){
			System.out.println("Started WebKit proxy on device " + "27753" + " and with port number " );
		}else{
			System.out.println("Not ******* Started WebKit proxy on device 27753 and with port number " );
		}
		*/
		
		/*
		CommandLine command = new CommandLine("/Applications/Appium4.app/Contents/Resources/node_modules/appium/bin/ios-webkit-debug-proxy-launcher.js -c 5aef5ba4cfdc74ccb2caf0e4d8cbf364c6feb0f3:27753 -d");
		System.out.println(command);
		
		PipedOutputStream outputStream = new PipedOutputStream();
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
		executor.setStreamHandler(streamHandler);
		executor.setExitValue(1);
		executor.execute(command, resultHandler);
		Scanner sc = new Scanner (new PipedInputStream(outputStream));

		while (sc.hasNextLine()) {
			String Out=sc.nextLine();
			System.out.println("printout... "+Out);
		}
		*/
		
		
		getDeviceID =appiumSetUp.deviceID;
		IOSWebkitDebugProxy =appiumSetUp.IOSWebKitDebugProxy;
		
		System.out.println("Getting device details for webkit debug proxy "+ IOSWebkitDebugProxy +"device id... " +getDeviceID);
		//Process process = Runtime.getRuntime().exec("/Applications/Appium.app/Contents/Resources/node_modules/appium/bin/ios-webkit-debug-proxy-launcher.js -c " +getDeviceID+":"+IOSWebkitDebugProxy+ " -d");
		Process process = Runtime.getRuntime().exec("/Applications/Appium.app/Contents/Resources/node_modules/appium/bin/ios-webkit-debug-proxy-launcher.js -c 67773889b7def4f5888e81b63a3914ba00427350:27753 -d");
		//Process process = Runtime.getRuntime().exec("/Applications/Appium.app/Contents/Resources/node_modules/appium/bin/ios-webkit-debug-proxy-launcher.js -c 5aef5ba4cfdc74ccb2caf0e4d8cbf364c6feb0f3:27753 -d");
		 System.out.println("the output stream is "+process.getOutputStream());
	        BufferedReader reader=new BufferedReader( new InputStreamReader(process.getInputStream()));
	      
	        String s; 
	        while ((s = reader.readLine()) != null){
	         //  System.out.println("The input stream is " + s);
			
	        }
	      
	        
	}
	
	catch(Exception E)
	{
		System.out.println("IOS WEBKIT EXCEPTION "+E);
		E.printStackTrace();
	}
		
		
	}

}
