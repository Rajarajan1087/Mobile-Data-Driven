/*package com.Engine;

import java.io.File;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.ServerSocket;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import com.Support.Support.ProcessType;

public class AppiumServer implements Runnable{

public AppiumServer(){
		
	}
	public AppiumServer(String executionType, String port){
		if(executionType.equalsIgnoreCase("SEQUENTIAL")){
			LoadEnvironment.BOOTSTRAP_PORT	=	LoadEnvironment.DEFAULT_BOOTSTRAP_PORT;
		}else{
			LoadEnvironment.BOOTSTRAP_PORT	=	port;
		}
	}
	public static void getPort() throws Exception
	{
		try{
			if(LoadEnvironment.EXECUTION_TYPE.equalsIgnoreCase("SEQUENTIAL")){
				ServerSocket socket = new ServerSocket(0);
				socket.setReuseAddress(true);
				String port = Integer.toString(socket.getLocalPort()); 
				socket.close();
				LoadEnvironment.BOOTSTRAP_PORT	=	port;
			}
		}catch(NullPointerException npe){}
		catch(Exception e){}
	} 
	@Override
	public void run() {
		try{
			PipedOutputStream outputStream = new PipedOutputStream();

			CommandLine command = new CommandLine(LoadEnvironment.NodeLocation);
			command.addArgument(LoadEnvironment.AppiumLocation, false);
			command.addArgument("--automation-name", false);
			command.addArgument("Appium");
			command.addArgument("--address", false);
			command.addArgument("127.0.0.1");
			command.addArgument("--port", false);
			//LoadEnvironment.BOOTSTRAP_PORT	=	getPort();
			command.addArgument(LoadEnvironment.BOOTSTRAP_PORT);
			command.addArgument("--no-reset", false);
			System.out.println(command);
			System.out.println("Initializing Appium Server");
			DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
			DefaultExecutor executor = new DefaultExecutor();
			PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
			executor.setStreamHandler(streamHandler);
			executor.setExitValue(1);
			executor.execute(command, resultHandler);
			Scanner sc = new Scanner (new PipedInputStream(outputStream));
			long startTime = System.currentTimeMillis();
			long elapsedTime = 0;
			int i=0;
			while (sc.hasNextLine()) {
				String Out=sc.nextLine();
				System.out.println(Out);
				if(Out.contains("debug")){
					LoadEnvironment.SERVER_SATRTUP_STATUS	=	true;
					break;
				}else{
					elapsedTime = (new Date()).getTime() - startTime;
					if(elapsedTime >50000)
					{
						LoadEnvironment.SERVER_SATRTUP_STATUS	=	true;
						break;
					}
					
				}
				
			}
		}catch(Exception e){
			System.out.println("Exception while starting appium setup");
		}
		
	}
}
*/

package com.Engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import com.Config.Config;
import com.Support.Support.ProcessType;
import com.SharedModules.NameGenerator;
import com.Engine.AppiumSetup;



public class AppiumServer implements Runnable{
	private String CanStart="Checking", addDevicePort ="100",addedDevicePort;
	private String BootstrapPort=getPort();
	private String ChromeDriverPort=getPort();
	private String iOSWebkitDebugProxy=getPort();
	private String port=getPort();
	AppiumSetup appiumSetup = new AppiumSetup();
	String getRandomGenerateName =	NameGenerator.randomName(7);
	String getInstrumentsURL ="/Users/tcs/Desktop/UIInstruments/"+getRandomGenerateName;
	String getDeviceId= appiumSetup.deviceID;
	
	public String getCanStart()
	{
		
		return CanStart;
	}
	
	public String getBootstrapPort()
	{
		return BootstrapPort;
	}
	public String getDefaultPort()
	{
		return port;
	}

	public String getChromeDriverPort()
	{
		return ChromeDriverPort;
	}
	
	public String getiOSWebkitDebugProxy()
	{
		return iOSWebkitDebugProxy;
	}
	
	public String getPort()
	{
		String port="";
		try {
			ServerSocket socket = new ServerSocket(0);
			socket.setReuseAddress(true);
			port = Integer.toString(socket.getLocalPort()); 
			socket.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return port;
	}
	
	
	
	public void run()
	{
		System.out.println("Instruments Temp location "+getInstrumentsURL);
		try
		{
			int q=0;
			File f = new File(LoadEnvironment.AppiumLocation);
			//System.out.println("Get appium location "+f);
			if(!(f.exists() && !f.isDirectory())) { 
				CanStart="Appium";
				q=1;
			}
			f = new File(LoadEnvironment.NodeLocation);
			//System.out.println("Get node location "+f);
			if(!(f.exists() && !f.isDirectory())) { 
				CanStart="Node";
				q=1;
			}
			if(q==0)
			{
				
				int getValue = Integer.parseInt(addDevicePort)+Integer.parseInt(BootstrapPort);
				addedDevicePort = Integer.toString(getValue);
				System.out.println("Increased BootStrapPort for device"+addedDevicePort);
				

				CommandLine command = new CommandLine(LoadEnvironment.NodeLocation);
				command.addArgument(LoadEnvironment.AppiumLocation, false);
				command.addArgument("--automation-name", false);
				command.addArgument("Appium");
				command.addArgument("--address", false);
				command.addArgument("127.0.0.1");
				command.addArgument("--port", false);
				command.addArgument(port);
				command.addArgument("--no-reset", false);
				
				if(getDeviceId.length() < 40){
					command.addArgument("-bp", false);
					command.addArgument(BootstrapPort);
					
					command.addArgument("--chromedriver-port", false);
					command.addArgument(ChromeDriverPort);
				}
				
				command.addArgument("-U", false);
				command.addArgument(getDeviceId);
				
				
				//command.addArgument("--debug-log-spacing", true);
				//command.addArgument("--show-ios-log", true);
				//command.addArgument("--native-instruments-lib", true);
				//command.addArgument("--session-override", true);
				//command.addArgument("--trace-dir", false);
				//command.addArgument(getInstrumentsURL);
				
			//	command.addArgument("--webkit-debug-proxy-port", false);
			//	command.addArgument(iOSWebkitDebugProxy);
				
				System.out.println(command);
				System.out.println("Initializing Appium Server");
				
				PipedOutputStream outputStream = new PipedOutputStream();
				DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
				DefaultExecutor executor = new DefaultExecutor();
				PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
				executor.setStreamHandler(streamHandler);
				executor.setExitValue(1);
				executor.execute(command, resultHandler);
				Scanner sc = new Scanner (new PipedInputStream(outputStream));
				
				long startTime = System.currentTimeMillis();
				long elapsedTime = 0;
				int i=0;
				
				while (sc.hasNextLine()) {
					String Out=sc.nextLine();
					//System.out.println("printout... "+Out);
					if(!Out.contains("Console LogLevel: debug"))
					{
						if(!(elapsedTime < 15000))
						{
							i=1;
							break;
						}
						elapsedTime = (new Date()).getTime() - startTime;
					}
					else
					{	
						break;
					}
				}
				
				if(i==0)
				{
					System.out.println("Appium Server Started");
					System.out.println("Time Taken : "+elapsedTime);
					CanStart="";
				}
				else
				{
					CanStart="Quit";
					System.out.println("Elapsed Time is "+elapsedTime/1000+" seconds");
				}
				
				while (sc.hasNextLine()) {
					String printIt=sc.nextLine();
					System.out.println("Console windows..."+port+"...."+printIt);
					if(printIt.contains("Adb command timed out after ") &&printIt.contains("Driver info: chromedriver"))
						
					{
						System.out.println("Calling ADB to Bypass Appium to Trigger Chrome . . .");
						AppiumSetup.ExecuteCommand("", ProcessType.Kill, "adb shell am start com.android.chrome/com.google.android.apps.chrome.Main");
						AppiumSetup.ExecuteCommand("", ProcessType.Kill, "adb shell am start com.android.chrome/com.google.android.apps.chrome.Main");
					
					}
				}
				
			}
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
		
	}
}