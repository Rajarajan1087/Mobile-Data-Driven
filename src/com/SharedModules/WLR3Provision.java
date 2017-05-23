package com.SharedModules;

import java.io.BufferedReader;
import com.Engine.Reporter;
import com.Utils.Reusables;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.Engine.LoadEnvironment;

public class WLR3Provision implements Constants{
	public Reporter Report;

	/**
	 * @param report
	 */
	public WLR3Provision(Reporter report) {
		Report = report;
	}

	/**
	 * @param FinalState
	 * @param CLI
	 * @throws Exception
	 */
	public void WLR3Modify(String FinalState, String CLI) throws Exception{

		String [] state = {Pending,Acknowledged,Committed,Semicomplete,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{
			String 	date = null;

			DBU.getOrderDetails(CLI, "WLR3UpdateInstallation", ODB);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\WLR3Modify.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i =0; i<5;i++){

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				newtext = newtext.replaceAll("M_DateTime",date);
				
				newtext = newtext.replaceAll("M_cmdInstID_WLR3",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_WLR3",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());	

				if(state[i].equalsIgnoreCase(Committed)){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
				}
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_WLR3Modify_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_WLR3Modify_"+state[i]+".txt");
				
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("WLR3Cease Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("WLR3Cease file genration", "WLR3Cease file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void WLR3Cease(String FinalState, String CLI) throws Exception{

		String [] state = {Pending,Acknowledged,Committed,Semicomplete,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{
			String CPWNRef = null ;
			String 	date = null;

			DBU.getOrderDetails(CLI, "WLR3Cease", ODB);
			CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\WLR3Cease.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i =0; i<5;i++){

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				newtext = newtext.replaceAll("M_cmdInstID_WLR3",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_WLR3",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);

				if(state[i].equalsIgnoreCase(Committed)){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
				}
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_WLR3Cease_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_WLR3Cease_"+state[i]+".txt");
				
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("WLR3Cease Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("WLR3Cease file genration", "WLR3Cease file generation error " + e.getMessage());

		}finally{

		}
	}
	
	/**
	 * @param FinalState
	 * @param CLI
	 * @throws Exception
	 */
	public void WLR3Suspend(String FinalState, String CLI) throws Exception{

		String [] state = {Pending,Acknowledged,Committed,Semicomplete,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{
			String CPWNRef = null ;
			String 	date = null;

			DBU.getOrderDetails(CLI, "WLR3UpdateDebtManagementFeatures", ODB);
			CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\WLR3Suspend.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i =0; i<5;i++){

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_cmdInstID_WLR3",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_WLR3",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);

				if(state[i].equalsIgnoreCase(Committed)){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
				}
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_WLR3Suspend_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_WLR3Suspend_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("WLR3Suspend Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("WLR3Suspend file genration", "WLR3Suspend file generation error " + e.getMessage());

		}finally{

		}
	}

	public void WLR3Transfer(String FinalState, String CLI, String InitialState) throws Exception{
		
		String [] state = {Pending,Acknowledged,Committed,Semicomplete,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{
			String CPWNRef = null ;
			String 	date = null;

			DBU.getOrderDetails(CLI, "WLR3Transfer", ODB);
			CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\WLR3Transfer.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i =0; i<5;i++){
				
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				
				
				if(!InitialState.equalsIgnoreCase("")){
					if(!InitialState.equalsIgnoreCase(state[i])){
						InitialState="";
						continue;
					}
				}

				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				
				newtext = newtext.replaceAll("M_cmdInstID_WLR3",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_WLR3",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_CPWNRef",CPWNRef);
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);

				if(state[i].equalsIgnoreCase(Committed)){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
				}
				
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_WLR3Transfer_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_WLR3Transfer_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("WLR3Transfer Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("WLR3Transfer file genration", "WLR3Transfer file generation error " + e.getMessage());

		}finally{

		}
	}

	public void WLR3Renumber(String FinalState, String CLI,String NewCLI,String InitialState) throws Exception{

		String [] state = {Pending,Acknowledged,Committed,Semicomplete,Completed };

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{
			String CPWNRef = null ;
			String 	date = null;
			if(InitialState.equalsIgnoreCase("Pending"))
			{
				DBU.getOrderDetails(CLI, "WLR3Renumber%", ODB);
			}
			else
			{
				DBU.getOrderDetails(CLI, "WLR3Renumber%", ODB);	
			}
			
			String SM_OrderID=ODB.getORDERID();
			CPWNRef = Reusables.getdateFormat("yyhhmmss", 0);

			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\WLR3Cease.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i =0; i<5;i++){

				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				
				oldtext=template;

				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				newtext = newtext.replaceAll("M_cmdInstID_WLR3",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_WLR3",ODB.getPROVCMDGWYCMDID() );
				newtext = newtext.replaceAll("M_CLI",CLI );
				newtext = newtext.replaceAll("M_serviceResellerId",ODB.getServiceresellerid());
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				newtext = newtext.replaceAll("M_NewCLI",NewCLI);

				if(state[i].equalsIgnoreCase(Committed)){
					newtext = newtext.replaceAll("M_COMMITTED","<ns0:customerCommitedDate>"+date+"</ns0:customerCommitedDate>");
				}else{
					newtext = newtext.replaceAll("M_COMMITTED","");
				}
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_WLR3Renumber_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_WLR3Renumber_"+state[i]+".txt");
				
				Thread.sleep(PROV_TIME);

				if(FinalState.equalsIgnoreCase(state[i]))
					break;

			}
			Report.fnReportPass("WLR3Cease Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("WLR3Cease file genration", "WLR3Cease file generation error " + e.getMessage());

		}finally{

		}
	}
	
}




