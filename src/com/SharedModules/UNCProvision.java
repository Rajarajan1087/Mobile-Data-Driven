package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Utils.Reusables;

public class UNCProvision implements Constants{
	public Reporter Report;

	/**
	 * @param report
	 */
	public UNCProvision(Reporter report) {
		Report = report;
	}
	
	public void UNC_SMPF(String FinalState, String CLI,String serviceResellerId) throws Exception{
		String[] state = {Acknowledged,Committed,Completed};

		try {
			String UniqueNumber = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			UniqueNumber = Reusables.getdateFormat("ddhhmmss", 0);
			String LISPValue = NameGenerator.randomCLI(10);
			//Value = 
			//String LISPValue = "LISP"+ NameGenerator.randomCLI(10);
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\UNC_SMPF.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<3;i++){	
				
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_gwyCmdID_SMPF",UniqueNumber);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",serviceResellerId);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_value",LISPValue);
				if(state[i]==Committed){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:Attributes><ns0:Attribute><ns0:name>ServiceId</ns0:name><ns0:value>" + LISPValue + "</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>Username</ns0:name><ns0:value>" + CLI + "@talktalk.net</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>Password</ns0:name><ns0:value>pwd_" + CLI + "</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>SupplierReference</ns0:name><ns0:value>9999</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>DNAReference</ns0:name><ns0:value>12346579</ns0:value></ns0:Attribute></ns0:Attributes><ns0:Resource><ns0:resourceId>" + LISPValue + "</ns0:resourceId><ns0:resourceType>2</ns0:resourceType></ns0:Resource>");
				}else if(state[i]==Completed){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:Attributes><ns0:Attribute><ns0:name>ServiceId</ns0:name><ns0:value>" + CLI + "</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>Username</ns0:name><ns0:value>" + CLI + "@talktalk.net</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>Password</ns0:name><ns0:value>pwd_" + CLI + "</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>SupplierReference</ns0:name><ns0:value>9999</ns0:value></ns0:Attribute><ns0:Attribute><ns0:name>DNAReference</ns0:name><ns0:value>12346579</ns0:value></ns0:Attribute></ns0:Attributes><ns0:Resource><ns0:resourceId>" + CLI + "</ns0:resourceId><ns0:resourceType>2</ns0:resourceType></ns0:Resource>");
				}else{
					newtext = newtext.replaceAll("M_COMMITTED", "");
				}

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFProvide_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_SMPFProvide_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("SMPFProvide Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("SMPFProvide file genration", "SMPFProvide file generation error " + e.getMessage());

		}finally{

		}
	
	}

	public void UNC_IPS(String FinalState, String CLI, String ServiceResellerID) throws Exception{
		String [] state = {Acknowledged,CeasePending,Completed};
			
		try {
			//String UniqueNumber = null ;
			//String CPWNRef = null;
			//String NewCLI = null;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			String UniqueNumber = Reusables.getdateFormat("ddhhmmss", 0);
			String CPWNRef = NameGenerator.randomCPWNRef(6);
			String NewCLI = NameGenerator.randomCLI(9);
			
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\UNC_IPS.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<3;i++){	

				oldtext=template;
				
				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				newtext = newtext.replaceAll("M_gwyCmdID_IPS",UniqueNumber);
				//newtext = newtext.replaceAll("M_cmdInstID_IPS",UniqueNumber);
				newtext = newtext.replaceAll("M_CLI",CLI );				
				newtext = newtext.replaceAll("M_serviceResellerId",ServiceResellerID);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
							
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_IPS_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_IPS_"+state[i]+".txt");
				
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("UNC_IPS Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("UNC_IPS file genration", "UNC_IPS file generation error " + e.getMessage());

		}finally{

		}
	}
	

	public void UNC_WLR3(String FinalState, String CLI, String ServiceResellerID) throws Exception{

		//String [] state = {"57","20","24","60","12"};
		String [] state = {Pending,Acknowledged,Committed,Semicomplete,Completed};

		//DbUtilities DBU = new DbUtilities(Report);
		OrderDetailsBean ODB = new OrderDetailsBean();
		try {
			//String CPWNRef = null ;
			String UniqueNumber = null ;
			String date = null;
			UniqueNumber = Reusables.getdateFormat("ddhhmmss", -5);
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
	
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\UNC_WLR3.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<5;i++){	

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				newtext = newtext.replaceAll("M_cmdInstID_WLR3",UniqueNumber);
				newtext = newtext.replaceAll("M_gwyCmdID_WLR3",UniqueNumber);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ServiceResellerID);
				//newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				if(state[i]==Committed){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
				}

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_WLR3_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_WLR3_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("UNC_WLR3 Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("UNC_WLR3 file genration", "UNC_WLR3 file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void UNC_LLU_WLTO(String FinalState, String CLI, String ServiceResellerID,boolean Stopcontrolline) throws Exception{
		String [] state = {Acknowledged,CeasePending,Completed};
		
			
		try {
			String UniqueNumber = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			UniqueNumber = Reusables.getdateFormat("ddhhmmss", 30);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\UNC_LLU_WLTO.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			if(Stopcontrolline==true)
			{
				UniqueNumber = Reusables.getdateFormat("ddhhmmss", 30);
			}
			else
			{
				UniqueNumber = Reusables.getdateFormat("ddhhmmss", -5);
			}
			for(int i=0;i<3;i++){	

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				//newtext = newtext.replaceAll("M_cmdInstID_LLU",UniqueNumber);
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",UniqueNumber);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ServiceResellerID);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_LLU_WLTO_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				
				
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_LLU_WLTO_"+state[i]+".txt");
				System.out.println("file");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("UNC_LLU_WLTO Provisioning complete for "+ CLI);
			
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("UNC_LLU_WLTO file genration", "UNC_LLU_WLTO file generation error " + e.getMessage());


		}
	}

	public void UNC_WLR3_WLTO(String FinalState, String CLI, String ServiceResellerID,boolean Stopcontrolline) throws Exception{
		String [] state = {Acknowledged,CeasePending,Completed};
		
			
		try {
			String UniqueNumber = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			UniqueNumber = Reusables.getdateFormat("ddhhmmss", 30);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\UNC_WLR3_WLTO.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			if(Stopcontrolline==true)
			{
				UniqueNumber = Reusables.getdateFormat("ddhhmmss", 30);
			}
			else
			{
				UniqueNumber = Reusables.getdateFormat("ddhhmmss", -5);
			}
			for(int i=0;i<3;i++){	

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				//newtext = newtext.replaceAll("M_cmdInstID_LLU",UniqueNumber);
				newtext = newtext.replaceAll("M_gwyCmdID_WLR3",UniqueNumber);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_ServiceResellerID",ServiceResellerID);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_LLU_WLTO_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				
				
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_LLU_WLTO_"+state[i]+".txt");
				System.out.println("file");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("UNC_LLU_WLTO Provisioning complete for "+ CLI);
			
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("UNC_LLU_WLTO file genration", "UNC_LLU_WLTO file generation error " + e.getMessage());


		}
	}
	
	
	public void UNC_LLU(String FinalState, String CLI, String ServiceResellerID) throws Exception{
		String [] state = {Acknowledged,CeasePending,Completed};
		
			
		try {
			String UniqueNumber = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			UniqueNumber = Reusables.getdateFormat("ddhhmmss", 0);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\UNC_LLU.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<3;i++){	

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_cmdInstID_LLU",UniqueNumber);
				newtext = newtext.replaceAll("M_gwyCmdID_LLU",UniqueNumber);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ServiceResellerID);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_LLU_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_LLU_"+state[i]+".txt");
				
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("UNC_LLU Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("UNC_LLU file genration", "UNC_LLU file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void UNC_LLUNew(String FinalState, String CLI, String Account,String InitialState,String CeaseReason, boolean AdditionalAttribute, String add,String ServiceResellerId) throws Exception{
		String [] state = {BSS_UNC_Acknowledged,BSS_UNC_CeasePending,BSS_UNC_Completed};
		
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
			
		try {
			String CPWNRef = null ;
			String UniqueNumber = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			UniqueNumber = Reusables.getdateFormat("ddhhmmss", 0);
			System.out.println(UniqueNumber);
			String FutureDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			String CompletedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			
			if((InitialState.equalsIgnoreCase(""))||(InitialState.equalsIgnoreCase(BSS_UNC_Acknowledged))){			
				//DBU.getOrderDetails(CLI, "LLUUnsolicited%", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);

			}else{
				DBU.getOrderDetails(CLI, "LLUUnsolicited%", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}

			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\UNC_LLUNew.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<3;i++){	
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				if(!InitialState.equalsIgnoreCase("")){
					if(!InitialState.equalsIgnoreCase(state[i])){
						
						continue;
						
					}
					InitialState = "";
				}

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_CMD",UniqueNumber);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_CPWN",CPWNRef);
				newtext = newtext.replaceAll("M_REASON",CeaseReason);
				newtext = newtext.replaceAll("M_ACCOUNT",Account);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_ServiceResellerId",ServiceResellerId);		
				
				System.out.println(state[i]);
				
				if(AdditionalAttribute==true){
					newtext = newtext.replaceAll("M_add",add);	
				}
				else{
					newtext = newtext.replaceAll("M_add", "" );
				}
				
				if(state[i]==BSS_UNC_Acknowledged){
					newtext = newtext.replaceAll("M_ACCEPTED","<Properties><Name>StateClarification</Name><Value>LSPG0510Order Acknowledged</Value></Properties>");
					newtext = newtext.replaceAll("M_VALIDATED","");
					newtext = newtext.replaceAll("M_COMPLETED","");
				}
				else if(state[i]==BSS_UNC_CeasePending){
					newtext = newtext.replaceAll("M_VALIDATED","<Properties><Name>StateClarification</Name><Value>LSPG0520Order Committed</Value></Properties><Properties><Name>CustomerAgreedDate</Name><Value>"+FutureDate+"</Value></Properties><Properties><Name>PreviousStateID</Name><Value>2</Value></Properties>");
					newtext = newtext.replaceAll("M_ACCEPTED","");
					newtext = newtext.replaceAll("M_COMPLETED","");
				}
				else{
					newtext = newtext.replaceAll("M_COMPLETED","<Properties><Name>StateClarification</Name><Value>LSPG0530Order Completed</Value></Properties><Properties><Name>CustomerAgreedDate</Name><Value>"+FutureDate+"</Value></Properties><Properties><Name>CompletedDate</Name><Value>"+CompletedDate+"</Value></Properties><Properties><Name>ClosedDate</Name><Value>"+CompletedDate+"</Value></Properties><Properties><Name>PreviousStateID</Name><Value>3</Value></Properties>");
					newtext = newtext.replaceAll("M_ACCEPTED","");
					newtext = newtext.replaceAll("M_VALIDATED","");
				}
								
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_LLUNew_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_LLUNew_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			
				Report.fnReportPass("UNC_LLUNew Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("UNC_LLUNew file genration", "UNC_LLUNew file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void UNC_LLUNew_SMPF(String FinalState, String CLI, String Account,String InitialState,String CeaseReason, boolean AdditionalAttribute, String add,String ServiceResellerId) throws Exception{
		String [] state = {BSS_UNC_Acknowledged,BSS_UNC_CeasePending,BSS_UNC_Completed};
		
		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);
			
		try {
			String CPWNRef = null ;
			String UniqueNumber = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			UniqueNumber = Reusables.getdateFormat("ddhhmmss", 0);
			System.out.println(UniqueNumber);
			String FutureDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 07);
			String CompletedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 15);
			
			if((InitialState.equalsIgnoreCase(""))||(InitialState.equalsIgnoreCase(BSS_UNC_Acknowledged))){			
				//DBU.getOrderDetails(CLI, "LLUUnsolicited%", ODB);
				//CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);
				CPWNRef = NameGenerator.randomCPWNRef(6);

			}else{
				DBU.getOrderDetails(CLI, "LLUUnsolicited%", ODB);
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
			}

			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\UNC_LLUNew_SMPF.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<3;i++){	
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

				if(!InitialState.equalsIgnoreCase("")){
					if(!InitialState.equalsIgnoreCase(state[i])){
						
						continue;
						
					}
					InitialState = "";
				}

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_CMD",UniqueNumber);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_CPWN",CPWNRef);
				newtext = newtext.replaceAll("M_REASON",CeaseReason);
				newtext = newtext.replaceAll("M_ACCOUNT",Account);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_ServiceResellerId",ServiceResellerId);		
				
				System.out.println(state[i]);
				
				if(AdditionalAttribute==true){
					newtext = newtext.replaceAll("M_add",add);	
				}
				else{
					newtext = newtext.replaceAll("M_add", "" );
				}
				
				if(state[i]==BSS_UNC_Acknowledged){
					newtext = newtext.replaceAll("M_ACCEPTED","<Properties><Name>StateClarification</Name><Value>LSPG0510Order Acknowledged</Value></Properties>");
					newtext = newtext.replaceAll("M_VALIDATED","");
					newtext = newtext.replaceAll("M_COMPLETED","");
				}
				else if(state[i]==BSS_UNC_CeasePending){
					newtext = newtext.replaceAll("M_VALIDATED","<Properties><Name>StateClarification</Name><Value>LSPG0520Order Committed</Value></Properties><Properties><Name>CustomerAgreedDate</Name><Value>"+FutureDate+"</Value></Properties><Properties><Name>PreviousStateID</Name><Value>2</Value></Properties>");
					newtext = newtext.replaceAll("M_ACCEPTED","");
					newtext = newtext.replaceAll("M_COMPLETED","");
				}
				else{
					newtext = newtext.replaceAll("M_COMPLETED","<Properties><Name>StateClarification</Name><Value>LSPG0530Order Completed</Value></Properties><Properties><Name>CustomerAgreedDate</Name><Value>"+FutureDate+"</Value></Properties><Properties><Name>CompletedDate</Name><Value>"+CompletedDate+"</Value></Properties><Properties><Name>ClosedDate</Name><Value>"+CompletedDate+"</Value></Properties><Properties><Name>PreviousStateID</Name><Value>3</Value></Properties>");
					newtext = newtext.replaceAll("M_ACCEPTED","");
					newtext = newtext.replaceAll("M_VALIDATED","");
				}
								
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_LLUNew_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_LLUNew_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			
				Report.fnReportPass("UNC_LLUNew Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("UNC_LLUNew file genration", "UNC_LLUNew file generation error " + e.getMessage());

		}finally{

		}
	}

	public void UNC_Cancel(String FinalState, String CLI, String AccessMethod) throws Exception{
		String [] state = {Cancelled};

		String CmdText = "";
		if(AccessMethod.equalsIgnoreCase("T3SMPF"))
			CmdText = "SMPFT3";
		else
			CmdText = AccessMethod;

		try {
			String UniqueNumber = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			UniqueNumber = Reusables.getdateFormat("ddhhmmss", 0);
			DbUtilities DBU = new DbUtilities(Report);
			OrderDetailsBean ODB = new OrderDetailsBean();
			DBU.getOrderDetails(CLI, AccessMethod+"%Unsolicited%", ODB);


			for(int i=0;i<1;i++){	

				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\UNC_"+AccessMethod+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "";
				while((line = reader.readLine()) != null)
					oldtext += line + "\r\n";
				reader.close();

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_cmdInstID_"+CmdText,ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_"+CmdText,ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
								
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_"+AccessMethod+"_cancel_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_"+AccessMethod+"_cancel_"+state[i]+".txt");

				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("UNC_"+AccessMethod+" cancel Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("UNC_"+AccessMethod+" cancel file genration", "UNC_"+AccessMethod+" cancel file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void UNC_SMPF(String FinalState, String CLI, String ServiceResellerID,String InitialState) throws Exception{
		String [] state = {Acknowledged,Committed,Completed};
		
			
		try {
			String UniqueNumber=null;
			DbUtilities DBU = new DbUtilities(Report);
			OrderDetailsBean ODB = new OrderDetailsBean();
			//String SMPF_CPWNRef =null;
			String CPWNRef=null;
			System.out.println("Before if");
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			
			if(InitialState.equalsIgnoreCase(Acknowledged))
			{	
				System.out.println("Acknowledged");
				UniqueNumber = Reusables.getdateFormat("ddhhmmss", -5);
				CPWNRef = NameGenerator.randomCPWNRef(6);
				System.out.println(CPWNRef);
				
			}
			else 
			{
				System.out.println("!Acknowledged");
				DBU.getOrderDetails(CLI,"SMPFUnsolicitedCease%",ODB);
				UniqueNumber=ODB.getPROVCMDGWYCMDID();
				DBU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
				System.out.println(CPWNRef);
				String ORDER_ID=ODB.getORDERID();	
				
			}
						
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\UNC_SMPF.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i=0;i<3;i++){	
				
				

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_gwyCmdID_SMPF",UniqueNumber);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ServiceResellerID);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_value",CPWNRef);
				
				
				newtext = newtext.replaceAll("M_STATE",state[i]);
				if(state[i].equalsIgnoreCase(Committed)){
					newtext = newtext.replaceAll("M_COMMITTED", "<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");	
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
				}
				

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_SMPF_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_SMPF_"+state[i]+".txt");
				
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("UNC_SMPF Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("UNC_SMPF file genration", "UNC_SMPF file generation error " + e.getMessage());

		}finally{

		}
	}

	
//	public void UNC_LLU_WLTO(String FinalState, String CLI, String ServiceResellerID,boolean Stopcontrolline) throws Exception{
//		String [] state = {Acknowledged,CeasePending,Completed};
//
//
//		try {
//			String UniqueNumber = null ;
//			String updatetime = null ;
//			String line = "", oldtext = "",template="";
//
//			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 30);
//			UniqueNumber = Reusables.getdateFormat("ddhhmmss", 30);
//
//
//			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\UNC_LLU_WLTO.txt");
//			BufferedReader reader = new BufferedReader(new FileReader(file));
//
//			while((line = reader.readLine()) != null)
//				template += line + "\r\n";
//			reader.close();
//
//
//			for(int i=0;i<3;i++){	
//				oldtext=template;
//				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
//				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
//				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
//				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
//				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
//
//				//newtext = newtext.replaceAll("M_cmdInstID_LLU",UniqueNumber);
//				newtext = newtext.replaceAll("M_gwyCmdID_LLU",UniqueNumber);
//				newtext = newtext.replaceAll("M_CLI",CLI );
//				newtext = newtext.replaceAll("M_serviceResellerId",ServiceResellerID);
//				newtext = newtext.replaceAll("M_STATE",state[i]);
//				newtext = newtext.replaceAll("M_DateTime",date);
//
//				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_LLU_WLTO_"+state[i]+".txt");
//				writer.write(newtext);writer.flush();writer.close();
//
//
//				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_LLU_WLTO_"+state[i]+".txt");
//				System.out.println("file");
//				Thread.sleep(PROV_TIME);
//
//				if(FinalState.equalsIgnoreCase(state[i]))
//					break;
//
//			}
//			if(Stopcontrolline==true)
//			{
//				oldtext=template;
//				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
//
//				updatetime = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 30);
//				newtext = newtext.replaceAll("M_DateTime",updatetime);
//			}
//			else
//			{
//				oldtext=template;
//				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
//
//				updatetime = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", -5);
//				newtext = newtext.replaceAll("M_DateTime",updatetime);
//			}
//			Report.fnReportPass("UNC_LLU_WLTO Provisioning complete for "+ CLI);
//
//		} catch (Exception e) {
//			Report.fnReportFailAndTerminateTest("UNC_LLU_WLTO file genration", "UNC_LLU_WLTO file generation error " + e.getMessage());
//
//
//		}
//	} 
	
	public void UNC_LLU_WLTOCAN(String FinalState, String CLI, String ServiceResellerID,boolean Stopcontrolline,String AccessMethod ) throws Exception{
		String [] state = {Cancelled};


		try {
			String UniqueNumber = null ;
			String updatetime = null ;
			String line = "", oldtext = "",template="";
			OrderDetailsBean ODB = new OrderDetailsBean();

			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 30);	
			DbUtilities DBU = new DbUtilities(Report);
			DBU.getOrderDetails(CLI, "LLUUnsolicited%", ODB);

			for(int i=0;i<1;i++){
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\UNC_LLU_WLTO.txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));

				while((line = reader.readLine()) != null)
					oldtext += line + "\r\n";
				reader.close();

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

				newtext = newtext.replaceAll("M_cmdInstID_LLU",ODB.getPROVCMDINSTID() );
				newtext = newtext.replaceAll("M_gwyCmdID_LLU", ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ServiceResellerID);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_LLU_WLTO_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				System.out.println("file");

				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_LLU_WLTO_"+state[i]+".txt");
				System.out.println("file");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			if(Stopcontrolline==true)
			{
				oldtext=template;
				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );

				updatetime = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 30);
				newtext = newtext.replaceAll("M_DateTime",updatetime);
			}
			else
			{
				oldtext=template;
				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );

				updatetime = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", -5);
				newtext = newtext.replaceAll("M_DateTime",updatetime);
			}
			Report.fnReportPass("UNC_LLU_WLTO Provisioning complete for "+ CLI);

		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("UNC_LLU_WLTO file genration", "UNC_LLU_WLTO file generation error " + e.getMessage());


		}
	} 
	public void UNC_OBC(String FinalState, String CLI, String Account) throws Exception{
		String [] state = {OBC_UNC_Pending,OBC_UNC_Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try {
			String CPWNRef = null ;
			String UniqueNumber = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			UniqueNumber = Reusables.getdateFormat("ddhhmmss", 0);
			System.out.println(UniqueNumber);
			String FutureDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 7);
			String CompletedDate = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 15);
			CPWNRef = NameGenerator.randomCPWNRef(6);
			for(int i=0;i<2;i++){	
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\OBC_UNC_"+state[i]+".txt");
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "",template="";
				while((line = reader.readLine()) != null)
					template += line + "\r\n";
				reader.close();
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );	
				
				newtext = newtext.replaceAll("M_cmdInstID",UniqueNumber);
				newtext = newtext.replaceAll("M_gwyCmdID",UniqueNumber);
				
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_CPWN",CPWNRef );
//				newtext = newtext.replaceAll("M_REASON",CeaseReason);
				newtext = newtext.replaceAll("M_ACCOUNT",Account);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				System.out.println(state[i]);
				newtext = newtext.replaceAll("M_Future",FutureDate);
				System.out.println(FutureDate);
				newtext = newtext.replaceAll("M_ServiceResellerId","569");
				System.out.println("569");
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_OBC_UNC_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_OBC_UNC_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}

			Report.fnReportPass("UNC_OBC Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("UNC_OBC file generation", "UNC_OBC file generation error " + e.getMessage());

		}finally{

		}
	}

	public void UNC_SMPFT3(String FinalState, String CLI, String ServiceResellerID) throws Exception{
		String [] state = {Acknowledged,CeasePending,Completed};

		try {
			String UniqueNumber = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			UniqueNumber = Reusables.getdateFormat("ddhhmmss", 0);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\UNC_T3SMPF.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i=0;i<3;i++){	

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );

			//	newtext = newtext.replaceAll("M_cmdInstID_LLU",UniqueNumber);
				newtext = newtext.replaceAll("M_gwyCmdID_SMPFT3",UniqueNumber);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ServiceResellerID);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_SMPFT3_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_SMPFT3_"+state[i]+".txt");

				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("UNC_SMPFT3 Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("UNC_SMPFT3 file genration", "UNC_SMPFT3 file generation error " + e.getMessage());

		}finally{

		}
	} 

	
	public void UNC_CPS(String FinalState, String CLI, String ServiceResellerID) throws Exception{
		String [] state = {Pending,Completed};


		try {
			String UniqueNumber = null ;
			String 	date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
			UniqueNumber = Reusables.getdateFormat("ddhhmmss", 0);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\UNC_OBC.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();

			for(int i=0;i<3;i++){	

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				newtext = newtext.replaceAll("M_gwyCmdID_CPS",UniqueNumber);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ServiceResellerID);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				//newtext = newtext.replaceAll("M_cmdInstID_LLU",UniqueNumber);

				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_CPS_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_UNC_CPS_"+state[i]+".txt");

				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("UNC_CPS Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("UNC_CPS file genration", "UNC_CPS file generation error " + e.getMessage());

		}finally{

		}
	} 
}


