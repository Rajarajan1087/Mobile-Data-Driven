
package com.PUTTY;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Engine.AppiumSetup;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

public class LrShell extends AppiumSetup {

	public LrShell(Reporter report) {
		Report=report;
	}

	public String LrShell_getLogfile(String TargetPattern,String LogFileName,String StartPattern_X,String EndPattern_Y,String Str_CLI,boolean XY) throws Exception{
		/*****************************************************************************************************/	
		String StartPattern=StartPattern_X;
		String EndPattern=EndPattern_Y;
		
		if(XY)
		{
			StartPattern = "<root xmlns:jms1="+StartPattern_X+" xmlns="+EndPattern_Y+">";
			EndPattern= "</root>";
		}

		String LogFile = "/opt/tibco/tra/domain/TTG" + LoadEnvironment.ENV + "/application/logs/"+LogFileName;
		String OutputFilename = "LogXML_"+Str_CLI+".xml";

		System.out.println("Log--------------"+LogFile);
		System.out.println("Start Pattern------------------"+StartPattern);

		LogXmlParser Logs = new LogXmlParser();

		Session session = Logs.CreateJschSession("ramakrs", "Secret007", LoadEnvironment.BW_SERVERIP);
		Channel channel = Logs.CreateChannelforExecution(session, "shell");
		PrintStream commander = Logs.CreateCommander(channel);
		Logs.CommandSender(commander, "cd /home/ramakrs");
		Logs.CommandSender(commander, "./Sample_xml_parser_param.csh "+LogFile+" '"+StartPattern+"' '"+EndPattern+"' '"+TargetPattern+"' '"+OutputFilename+"'");
		Logs.CommandSender(commander, "cp output.txt "+OutputFilename);
		Logs.CommandSender(commander, "sed 's/.*\\(<root>*\\)/\\1/' output.txt > "+OutputFilename);
		Logs.CommandSender(commander, "exit");
		Logs.CloseCommandSender(commander);
		Logs.DisplayShell(channel);
		
		Logs.CloseChannel(channel);
		Logs.CloseSession(session);

		System.out.println("/home/ramakrs/" + OutputFilename);
		Logs.GetFile("/home/ramakrs/" + OutputFilename,System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+OutputFilename);
		return OutputFilename;
		
		/*************************END of Main Test*****************************************//*
		Report.reportTest();*/


			}
	
	public void LrShell_XMLVerify(String LogFilePath,String TargetNodes,String TargetValues,boolean EmailbyTemplate_NameValue) throws Exception{
		/*****************************************************************************************************/	
				String Str_XmlLog="";
				String line = "";
				int Str_Index=0;
				int i=0;
				String []TargetNode=TargetNodes.split(",");
				String []TargetValue=TargetValues.split(",");
		/********************************** Reading XMLFile **************************************************/	
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+LogFilePath);
				BufferedReader reader = new BufferedReader(new FileReader(file));
				while((line = reader.readLine()) != null)
					Str_XmlLog += line + "\r\n";
				reader.close();
				String Str_Temp=Str_XmlLog;
		/********************************** Getting the Index values of Target Nodes and Values ********************/	

				if(EmailbyTemplate_NameValue)
				{
					Str_Index=14;                    // 14 represents </name><value> exists between node and value
				}
				else
				{
					for(int j=0;j<TargetNode.length;j++)
					{
						TargetNode[j]="<"+TargetNode[j]+">";
					}
				}
				for(int j=0;j<TargetNode.length;j++)
				{
					Str_Temp=Str_XmlLog;
					if(EmailbyTemplate_NameValue)
					{
						Str_Index=14;                    // 14 represents </name><value> exists between node and value
					}
					i=1;
					while(i==1)
					{
						i=1;
						if(Str_Temp.contains(TargetNode[j]))
						{
							if(Str_Temp.contains(TargetValue[j]))
							{
								String asd=Str_Temp.substring(Str_Temp.indexOf(TargetNode[j])+TargetNode[j].length(), Str_Temp.indexOf(TargetValue[j],Str_Temp.indexOf(TargetNode[j])));
								for(int k = 0; k < asd.length(); k++)
								{
									if(Character.isWhitespace(asd.charAt(k)))
								    {
										Str_Index++;
								    }
								 }
								int Index_Node=Str_Temp.indexOf(TargetNode[j])+TargetNode[j].length()+Str_Index;
								int Index_Value=Str_Temp.indexOf(TargetValue[j],Str_Temp.indexOf(TargetNode[j]));
								if(Index_Node==Index_Value)
								{
									Report.fnReportPass("XML Verification Passed : The "+TargetValue[j]+" is found as the Value for the Node "+TargetNode[j]);
									i=0;
								}
								else
								{
									Str_Temp=Str_Temp.substring(Str_Temp.indexOf(TargetValue[j],Str_Temp.indexOf(TargetNode[j]))+TargetValue[j].length(),Str_Temp.length()-1);
								}
							}
							else
							{
								Report.fnReportFail("XML Verification Failed : The Target Value : "+TargetValue[j]+" is Not found");
								i=0;
							}
						}
						else
						{
							Report.fnReportFail("XML Verification Failed : The Target Node : "+TargetNode[j]+" is Not found");
							i=0;
						}
					}	
				}
		}
	
}
 