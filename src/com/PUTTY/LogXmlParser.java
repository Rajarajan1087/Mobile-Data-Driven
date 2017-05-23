package com.PUTTY;

//package com.LRshellScripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class LogXmlParser {

	public Session CreateJschSession(String User,String Pass,String Host) throws JSchException{
		JSch jsch = new JSch();
		Session session = null;
		try{

			session=jsch.getSession(User,Host, 22);
			session.setPassword(Pass);

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);

			session.setConfig("PreferredAuthentications",
					"publickey,keyboard-interactive,password");
			session.connect();
			System.out.println("------------Session Connected----------------");
		}catch(Exception e){

		}
		return session;
	}

	public Channel CreateChannelforExecution(Session session, String Mode) throws Exception{
		Channel channel = null;
		try{		
			channel = session.openChannel(Mode);
			channel.connect();
			System.out.println("------------Channel Connected----------------");

		}catch(Exception e){

		}
		return channel;
	}

	public PrintStream CreateCommander(Channel channel)throws Exception {
		PrintStream commander =null;
		try{
			OutputStream inputstream_for_the_channel = channel.getOutputStream();
			commander = new PrintStream(inputstream_for_the_channel,true);
			//			commander.println(Command);	
			//			System.out.println("Command Executed -- "+Command);
			//			commander.close();

		}catch(Exception e){

		}
		return commander;
	}
	public void CommandSender(PrintStream commander,String Command) throws Exception {

		try{

			commander.println(Command);	
			System.out.println("Command Executed -- "+Command);


		}catch(Exception e){

		}
	}
	public void CloseCommandSender(PrintStream commander) throws Exception {

		try{

			commander.close();


		}catch(Exception e){

		}
	}
	public void CloseChannel(Channel channel) throws Exception {
		try{
			channel.disconnect();
			System.out.println("------------Channel Disconnected----------------");

		}catch(Exception e){

		}
	}
	public void CloseSession(Session session) throws Exception {
		try{
			session.disconnect();
			System.out.println("------------Session Disconnected----------------");
		}catch(Exception e){

		}
	}
	public void DisplayShell(Channel channel) throws Exception{

		InputStream outputstream_from_the_channel = channel.getInputStream();
		System.out.println("good");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				outputstream_from_the_channel));
		System.out.println("good");
		String line;

		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}

	public void GetFile(String Str_Source,String File_Dest) throws Exception{
System.out.println(Str_Source);
System.out.println(File_Dest);
		JSch jsch=new JSch();
		Session session=null;
		Channel channel=null;
		ChannelSftp channelSftp=null;
		try {

			session = jsch.getSession("ramakrs", "10.180.20.117", 22);
			session.setPassword("Secret007");
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			System.out.println("------------session Disconnected----------------");
			channel = session.openChannel("sftp");
			channel.connect();
			System.out.println("------------session Disconnected----------------");
			channelSftp = (ChannelSftp) channel;
			channelSftp.get(Str_Source,File_Dest);
			
		}finally{
			channelSftp.exit();
			session.disconnect();
			System.out.println("file SFTP Complete to  "+File_Dest);

		}

	}
	public void PutFile(String Str_Source,String File_Dest) throws Exception{

		JSch jsch=new JSch();
		Session session=null;
		Channel channel=null;
		ChannelSftp channelSftp=null;
		try {

			session = jsch.getSession("ramakrs", "10.180.20.176", 22);
			session.setPassword("Secret007");
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			System.out.println("------------session Disconnected----------------");
			channel = session.openChannel("sftp");
			channel.connect();
			System.out.println("------------session Disconnected----------------");
			channelSftp = (ChannelSftp) channel;
			channelSftp.put(Str_Source,File_Dest);
			
			
		}finally{
			channelSftp.exit();
			session.disconnect();
			System.out.println("file SFTP Complete to  "+File_Dest);

		}

	}
	
	@SuppressWarnings("finally")
	public static String getXMLdata(String XMLContent, String NodeName, String TAG) throws Exception{
		String ReturnXMLvalue = null;
		
		try{
		

		File fXmlFile = new File("D:\\K_CMDRequest_01656577001.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();


		NodeList nList = doc.getElementsByTagName(NodeName);
		//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				ReturnXMLvalue = eElement.getElementsByTagName(TAG).item(0).getTextContent();
				break;
			}
		}
		}catch(Exception e){
			
		}finally{
			return ReturnXMLvalue;
		}
		
	}




}
