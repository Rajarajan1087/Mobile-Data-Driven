package com.SharedModules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.EMS.MessageTester;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Engine.AppiumSetup;
import com.Utils.Reusables;

public class GenericProvisioning extends AppiumSetup implements Constants {
	public Reporter Report;

	/**
	 * @param report
	 */
	public GenericProvisioning(Reporter report) {
		Report = report;
	}
	public void ProvisionAll_AOD(String CLI)
	{
		DbUtilities DBU=new DbUtilities(Report);
	}
	public void CPEGProvide(String FinalState, String CLI) throws Exception{
		String [] state={Requested,Dispatched,"",Completed};
		int Num_State=4;
		if(FinalState.equalsIgnoreCase(Constants.Received))
		{
			state[2]=Received;
			Num_State=3;
		}
		else
		{
			state[2]=Delivered;
		}

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{
		
			String 	date =null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

			DBU.getOrderDetails(CLI, "SubmitCPE", ODB,false);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\CPEGProvision.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i =0; i<Num_State;i++){
				oldtext = template;
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				newtext = newtext.replaceAll("M_cmdInstID_CPE",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_CPE",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_serviceResellerId","569");
				newtext = newtext.replaceAll("M_STATE",state[i]);
				if(state[i].equalsIgnoreCase(Requested)){
					newtext = newtext.replaceAll("M_DISPATCHDATE","");	
				}else{
					newtext = newtext.replaceAll("M_DISPATCHDATE","<ns0:Attribute><ns0:name>dispatchDate</ns0:name><ns0:value>DateTime</ns0:value></ns0:Attribute>\n");
				}
				newtext = newtext.replaceAll("M_DateTime",date);
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CPEG_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CPEG_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);
				
				if(FinalState.equalsIgnoreCase(state[i]))
					break;
				
			}
			Report.fnReportPass("CPEG Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("CPEG file genration", "CPEG file generation error " + e.getMessage());

		}finally{

		}
	}
	
	public void CPEG_OrderProcess(String FinalState, String Str_CLI,String Str_State,String Dsp_date,String UID,boolean Boolean_TVCPEG) throws Exception{
		
		String Target="DISPATCHED";
		String [] State = {"DSP","RCV","RTB","RTS","DEL","FUL"};
		String [] state = {"DISPATCHED","RECEIVED","RETURN_TO_BASE","RETURN_TO_SENDER","DELIVERED","FULFILMENT_REQUESTED"};
		String CPEG_orderNo="";
		String orderlineid1="";
		String partcode;
		String Str_CurrentState="";
		int states_number=1;
		DbUtilities DbU = new DbUtilities(Report);
		String[] updatestates={"DSP",""};
		
		
		if(FinalState.equalsIgnoreCase("DSP"))
		{
		}
		else{
			updatestates[1]=FinalState;
			states_number=2;
		}
		for(int k=0;k<State.length;k++)
		{
			if(FinalState.equalsIgnoreCase(State[k]))
			{
				Target=state[k];
			}
			
		}
		
/****************************** To get the CPEG order id from CPEG database using cli *************/
		
		CPEG_orderNo=DbU.getResultCPEG1("select ORDER_ID from CPEGPROVISIONORDER where  CLI='"+Str_CLI+"' and rownum<=1 order by ORDER_ID desc","ORDER_ID");
		System.out.println(CPEG_orderNo);
		
		//CPEG_orderNo="700043450";
		
/****************************** To verify the CPE product is available under the correct fulfilment house *****/
		
		DbU.getResultCPEG1("select FULFILMENT_HOUSE_ID from PRODUCTPART where id in (select product_part_id from CPEGORDERLINE where order_id ='"+CPEG_orderNo+"')","");
		
/****************************** To verify the current status of the CPEG Order *******************************/
		
		Str_CurrentState=DbU.getResultCPEG1("select STATE from cpegorder where order_id  ='"+CPEG_orderNo+"'","STATE");
		System.out.println(Str_CurrentState);
		if(Str_CurrentState.equalsIgnoreCase("ACCEPTED")||Str_CurrentState.equalsIgnoreCase("VALIDATED"))
		{
//			LaunchBrowser("FIREFOX",LoadEnvironment.ANOVO_orderSender_URL);
			driver1.get(LoadEnvironment.ANOVO_updateProcessor_URL);
			Thread.sleep(3000);
			driver1.close();
			
			DbU.getResultCPEG1("Update cpegorder set state='FULFILMENT_REQUESTED' where order_id='"+CPEG_orderNo+"'","");
			String data=DbU.getResultCPEG1("select MAX(UPDATE_ID) UPDATE_ID,RESELLER_REFERENCE,TRANSACTION_ID,DELIVERY_METHOD from orderstatusupdate where order_id  ='"+CPEG_orderNo+"' group by ORDER_ID,RESELLER_REFERENCE,TRANSACTION_ID,DELIVERY_METHOD","UPDATE_ID,RESELLER_REFERENCE,TRANSACTION_ID,DELIVERY_METHOD");
			String []Data=data.split(",");
			Thread.sleep(4000);
			//DbU.getResultCPEG1("insert into orderstatusupdate(update_id,order_id,state,state_change_date,reseller_reference,tracking_number,transaction_id,serial_code,delivery_method,dispatch_date,error_code,error_message,fulfillhouse_stat_chng_dt) values ('"+String.valueOf((Integer.parseInt(Data[0])+1))+"','"+CPEG_orderNo+"','FULFILMENT_REQUESTED',SYSDATE,'"+Data[1]+"',null,'"+Data[1]+"',null,'"+Data[2]+"',null,null,null,null)","");
			
		}
		
/******************************* Verification of order status after running the Order Sender job **************/
		
		int j=0;
		Str_CurrentState=DbU.getResultCPEG1("select STATE from cpegorder where order_id  ='"+CPEG_orderNo+"'","STATE");
		while(j<50&&!Str_CurrentState.equalsIgnoreCase("FULFILMENT_REQUESTED"))
		{
			Str_CurrentState=DbU.getResultCPEG1("select STATE from cpegorder where order_id  ='"+CPEG_orderNo+"'","STATE");
			if(Str_CurrentState.equalsIgnoreCase("FULFILMENT_REQUESTED"))
			{
				j=50;
			}
			j++;
		}
		String date = Reusables.getdateFormat("dd-MMM-yy", -10)+" 08.17.06.000000000 AM";
		String date1 =Reusables.getdateFormat("dd-MMM-yy", -10)+" 08.17.06.000000000 AM +00:00";
		if(Str_CurrentState.equalsIgnoreCase("FULFILMENT_REQUESTED"))
		{
			//rs=DbU.getResultCPEG1("select count(*) as count from cpegorderline where order_id  ='"+CPEG_orderNo+"'");
			orderlineid1=DbU.getResultCPEG1("select ORDER_LINE_ID from cpegorderline where order_id  ='"+CPEG_orderNo+"'","ORDER_LINE_ID");
			partcode=DbU.getResultCPEG1("select PART_CODE from PRODUCTPART where id  =(select PRODUCT_PART_ID from cpegorderline where order_line_id = '"+orderlineid1+"')","PART_CODE");
			for(int i=0;i<states_number;i++)
			{
			//old cpeg update _Padma
//					DbU.getResultCPEG1("UPDATE ANOVOSTATUSUPDATE SET " + 
//                                    "TRANSACTION_ID = "+CPEG_orderNo+"," + 
//                                    "ORDER_LINE_ID = "+orderlineid1+"," + 
//                                    "STATE = 'PENDING'," + 
//                                    "TRACKING_NUMBER = 'T"+orderlineid1+"'," + 
//                                    "STATUS_CODE = '"+updatestates[i]+"'," + 
//                                    "SERIAL_CODE = 'SC"+orderlineid1+"'," + 
//                                    "INSERT_DATE = to_date('"+Dsp_date+"','dd/MM/yyyy hh24:mi:ss')," + 
//                                    "SENT_PART_CODE='"+partcode+"'," + 
//                                    "MAC_ADDRESS = 'MAC"+orderlineid1+"'," + 
//                                    "UPDATE_DATE = to_date('"+Dsp_date+"','dd/MM/yyyy hh24:mi:ss')," + 
//                                    "DISPATCH_DATE = to_date('"+Dsp_date+"','dd/MM/yyyy hh24:mi:ss') " + 
//                                    "WHERE STATUS_UPDATE_ID = "+UID,"");
					
				DbU.getResultCPEG1("UPDATE ANOVOSTATUSUPDATE SET " + 
								"TRANSACTION_ID = "+CPEG_orderNo+"," +
//								"UPDATE_DATE = to_date('"+Dsp_date+"','dd/MM/yyyy hh24:mi:ss')," + 
//								"DISPATCH_DATE = to_date('"+Dsp_date+"','dd/MM/yyyy hh24:mi:ss') " +
								"UPDATE_DATE = '"+date+"' ," + 
								"DISPATCH_DATE = '"+date+"' ," + 
								"STATUS_CODE = '"+updatestates[i]+"'," +
								"TRACKING_NUMBER ='" +  "'," + 
								"SERIAL_CODE = 'SC"+orderlineid1+"'," + 
								"SENT_PART_CODE='"+partcode+"'," + 
//								"INSERT_DATE = to_date('"+Dsp_date+"','dd/M/yyyy hh24:mi:ss')," + 
								"INSERT_DATE = '"+date1+"' ," + 
								"STATE = 'PENDING'," + 
								"ORDER_LINE_ID = "+orderlineid1+"," +                           
								"MAC_ADDRESS = '3B:sO:FF:i8:10' " +                          
								"WHERE STATUS_UPDATE_ID = "+UID,"");
					
//				LaunchBrowser("FIREFOX",LoadEnvironment.ANOVO_updateProcessor_URL);
				driver1.get(LoadEnvironment.ANOVO_updateProcessor_URL);
				Thread.sleep(3000);
				driver1.close();
					
//				LaunchBrowser("FIREFOX",LoadEnvironment.ANOVO_updateProcessor_URL);
				driver1.get(LoadEnvironment.ANOVO_updateProcessor_URL);
				Thread.sleep(3000);
				driver1.close();
			}
		}		
		
/******************************* verify the current status of the CPE order is Target state *****************/
			
			Str_CurrentState=DbU.getResultCPEG1("select STATE from cpegorder where order_id  ='"+CPEG_orderNo+"'","STATE");
			if(Str_CurrentState.equalsIgnoreCase(Target))
			{
				Report.fnReportPass("CPEG Orders are processed succesfully. DispatchedDate ->" + String.valueOf(date) + " SerialNumber->SC" + orderlineid1 +" TrackingNumber->T" + orderlineid1 + " TransactionID->" + String.valueOf(CPEG_orderNo) + " FinalState is ->" + Str_CurrentState);
			}
			else
			{
				Report.fnReportFailAndTerminateTest("CPEG Order Process","CPEG Orders are not processed succesfully. DispatchedDate ->" + String.valueOf(Dsp_date) + " SerialNumber->SC" + orderlineid1 +" TrackingNumber->T" + orderlineid1 + " TransactionID->" + String.valueOf(CPEG_orderNo) + " FinalState is ->" + Str_CurrentState);
			}
			
			if(Boolean_TVCPEG)
			{
				TVCPEGProvision TVCPEG = new TVCPEGProvision(Report);
				if(Target.equalsIgnoreCase("RECEIVED"))
				{
					TVCPEG.TVCPEGProvide(Constants.Received, Str_CLI);
				}
				else if(Target.equalsIgnoreCase("DELIVERED"))
				{
					TVCPEG.TVCPEGProvide(Constants.Completed, Str_CLI);
				}
			}
			else
			{
				GenericProvisioning CPEG = new GenericProvisioning(Report);
				if(Target.equalsIgnoreCase("RECEIVED"))
				{
					CPEG.CPEGProvide(Constants.Received, Str_CLI);
				}
				else if(Target.equalsIgnoreCase("DELIVERED"))
				{
					CPEG.CPEGProvide(Constants.Completed, Str_CLI);
				}
			}
		}
	
	
	public void CPEGCancel(String FinalState, String CLI) throws Exception{
		String [] state = {Completed};

		OrderDetailsBean ODB = new OrderDetailsBean();
		DbUtilities DBU = new DbUtilities(Report);

		try{
		
			String 	date =null;
			date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);

			DBU.getOrderDetails(CLI, "CancelCPE", ODB);
			
			File file = new File(System.getProperty("user.dir")+"\\ProvisioningTemplates\\CPECancel.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "",template="";
			while((line = reader.readLine()) != null)
				template += line + "\r\n";
			reader.close();
			
			for(int i =0; i<1;i++){
				oldtext = template;
				date = Reusables.getdateFormat("yyyy-MM-dd'T'HH:mm:ss", 0);
				String newtext = oldtext.replaceAll("M_env",LoadEnvironment.ENV );
				newtext = newtext.replaceAll("M_emm_hostname",LoadEnvironment.EMM_HOSTNAME);
				newtext = newtext.replaceAll("M_emm_port",LoadEnvironment.EMM_PORT );
				newtext = newtext.replaceAll("M_emm_username",LoadEnvironment.EMM_USERNAME );
				newtext = newtext.replaceAll("M_emm_password",LoadEnvironment.EMM_PASSWORD );
				newtext = newtext.replaceAll("M_cmdInstID_CPE",ODB.getPROVCMDINSTID());
				newtext = newtext.replaceAll("M_gwyCmdID_CPE",ODB.getPROVCMDGWYCMDID());
				newtext = newtext.replaceAll("M_CLI",CLI);
				newtext = newtext.replaceAll("M_serviceResellerId","569");
				newtext = newtext.replaceAll("M_STATE",state[i]);
				newtext = newtext.replaceAll("M_DateTime",date);
				FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CPEG_"+state[i]+".txt");
				writer.write(newtext);writer.flush();writer.close();
				MessageTester.MessageTester_test(System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+CLI+"_CPEG_"+state[i]+".txt");
				Thread.sleep(PROV_TIME);
				System.out.println("looped in " + i);
				if(FinalState.equalsIgnoreCase(state[i]))
					break;
				
			}
			Report.fnReportPass("CPEG Provisioning complete for "+ CLI);
		} catch (Exception e) {
			Report.fnReportFailAndTerminateTest("CPEG file genration", "CPEG file generation error " + e.getMessage());

		}finally{

		}
	}
	
}
	
	