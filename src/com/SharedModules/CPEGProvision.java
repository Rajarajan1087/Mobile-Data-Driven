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

public class CPEGProvision extends AppiumSetup implements Constants {
	public Reporter Report;
	public String tid="";
	/**
	 * @param report
	 */
	public CPEGProvision(Reporter report) {
		Report = report;
	}
	public void CPEGProvide(String FinalState, String CLI) throws Exception{
		String [] state={Requested,Dispatched,"",Completed};
		int Num_State=4;
		if(FinalState.equalsIgnoreCase(Received))
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
	
	/**
	 * @param FinalState
	 * @param Str_CLI
	 * @param Str_State
	 * @param Dsp_date
	 * @param UID
	 * @param Boolean_TVCPEG
	 * @throws Exception
	 */
	public void CPEG_OrderProcess(String FinalState, String Str_CLI,String RouterType) throws Exception{
//		String UID="44418303";
		String UID="44432485";
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
		String DeliveryCheck="and delivery_method='NORMPOST'";
		if(!RouterType.equalsIgnoreCase(""))
		{
			DeliveryCheck="and delivery_method='COURIER'";
		}
		
/****************************** To get the CPEG order id from CPEG database using cli *************/

		do
		{
			CPEG_orderNo=DbU.getResultCPEG1("select ORDER_ID from CPEGPROVISIONORDER where  CLI='"+Str_CLI+"' "+DeliveryCheck+" and rownum<=1 order by ORDER_ID desc","ORDER_ID");
			System.out.println(CPEG_orderNo);
		}while(CPEG_orderNo.equals("ORDER_ID"));
//		CPEG_orderNo="303420400";
		
/****************************** To verify the CPE product is available under the correct fulfilment house *****/
		
		DbU.getResultCPEG1("select FULFILMENT_HOUSE_ID from PRODUCTPART where id in (select product_part_id from CPEGORDERLINE where order_id ='"+CPEG_orderNo+"')","");
		
/****************************** To verify the current status of the CPEG Order *******************************/
		
		Str_CurrentState=DbU.getResultCPEG1("select STATE from cpegorder where order_id  ='"+CPEG_orderNo+"'","STATE");
		System.out.println(Str_CurrentState);
		if(Str_CurrentState.equalsIgnoreCase("ACCEPTED")||Str_CurrentState.equalsIgnoreCase("VALIDATED"))
		{
			sendGet(LoadEnvironment.ANOVO_orderSender_URL);
			sendGet(LoadEnvironment.ANOVO_updateProcessor_URL);

			DbU.getResultCPEG1("Update cpegorder set state='FULFILMENT_REQUESTED' where order_id='"+CPEG_orderNo+"'","");
			String data=DbU.getResultCPEG1("select MAX(UPDATE_ID) UPDATE_ID,RESELLER_REFERENCE,TRANSACTION_ID,DELIVERY_METHOD from orderstatusupdate where order_id  ='"+CPEG_orderNo+"' group by ORDER_ID,RESELLER_REFERENCE,TRANSACTION_ID,DELIVERY_METHOD","UPDATE_ID,RESELLER_REFERENCE,TRANSACTION_ID,DELIVERY_METHOD");
			String []Data=data.split(",");
			sendGet(LoadEnvironment.ANOVO_updateProcessor_URL);
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
		String date="to_timestamp(sysdate-10)";
		String date1=date;
		if(Str_CurrentState.equalsIgnoreCase("FULFILMENT_REQUESTED"))
		{
			orderlineid1=DbU.getResultCPEG1("select ORDER_LINE_ID from cpegorderline where order_id  ='"+CPEG_orderNo+"'","ORDER_LINE_ID");
			partcode=DbU.getResultCPEG1("select PART_CODE from PRODUCTPART where id  =(select PRODUCT_PART_ID from cpegorderline where order_line_id = '"+orderlineid1+"')","PART_CODE");
			for(int i=0;i<states_number;i++)
			{
					if(tid.equals(""))
					{
						DbU.getResultCPEG1("UPDATE ANOVOSTATUSUPDATE SET " + 
								"TRANSACTION_ID = "+CPEG_orderNo+"," +
								"UPDATE_DATE = "+date+" ," + 
								"DISPATCH_DATE = "+date+" ," + 
								"STATUS_CODE = '"+updatestates[i]+"'," +
								"TRACKING_NUMBER ='" +  "'," + 
								"SERIAL_CODE = 'SC"+orderlineid1+"'," + 
								"SENT_PART_CODE='"+partcode+"'," + 
								"INSERT_DATE = "+date1+" ," + 
								"STATE = 'PENDING'," + 
								"ORDER_LINE_ID = "+orderlineid1+"," +                           
								"MAC_ADDRESS = '3B:sO:FF:i8:10' " +                          
								"WHERE STATUS_UPDATE_ID = "+UID,"");
					}
					else{
						DbU.getResultCPEG1("UPDATE ANOVOSTATUSUPDATE SET " + 
								"TRANSACTION_ID = "+CPEG_orderNo+"," +
								"UPDATE_DATE = "+date+" ," + 
								"DISPATCH_DATE = "+date+" ," + 
								"STATUS_CODE = '"+updatestates[i]+"'," +
								"TRACKING_NUMBER ='T"+orderlineid1+"'," + 
								"SERIAL_CODE = 'SC"+orderlineid1+"'," + 
								"SENT_PART_CODE='"+partcode+"'," + 
								"INSERT_DATE = "+date1+" ," + 
								"STATE = 'PENDING'," + 
								"ORDER_LINE_ID = "+orderlineid1+"," +                           
								"MAC_ADDRESS = '3B:sO:FF:i8:10' " +                          
								"WHERE STATUS_UPDATE_ID = "+UID,"");
					}
					sendGet(LoadEnvironment.ANOVO_updateProcessor_URL);
					sendGet(LoadEnvironment.ANOVO_updateProcessor_URL);
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
				Report.fnReportFailAndTerminateTest("CPEG Order Process","CPEG Orders are not processed succesfully. SerialNumber->SC" + orderlineid1 +" TrackingNumber->T" + orderlineid1 + " TransactionID->" + String.valueOf(CPEG_orderNo) + " FinalState is ->" + Str_CurrentState);
			}
			
			/*if(Boolean_TVCPEG)
			{
				TVCPEGProvision TVCPEG = new TVCPEGProvision(Report);
				if(Target.equalsIgnoreCase("RECEIVED"))
				{
					TVCPEG.TVCPEGProvide(Received, Str_CLI);
				}
				else if(Target.equalsIgnoreCase("DELIVERED"))
				{
					TVCPEG.TVCPEGProvide(Completed, Str_CLI);
				}
			}
			else
			{
				CPEGProvision CPEG = new CPEGProvision(Report);
				if(Target.equalsIgnoreCase("RECEIVED"))
				{
					CPEG.CPEGProvide(Received, Str_CLI,"");
				}
				else if(Target.equalsIgnoreCase("DELIVERED"))
				{
					CPEG.CPEGProvide(Completed, Str_CLI,"");
				}
			}*/
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
	
	