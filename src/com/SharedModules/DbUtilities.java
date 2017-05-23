package com.SharedModules;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Stubs.StubFilePlacing;
import com.Stubs.StubFilePlacing.StubType;
import com.Utils.Reusables;

public class DbUtilities implements Constants {

	public Reporter Report;

	public enum INSTALLATION_METHOD_CONTROL{
		Managed_Install,No_STB,CTD
	}
	public enum MANAGED_INSTALL_CONTROL{
		Change_Package,Change_Package_Retentions,Homemover,Initial_Sale,My_Account_Change_Package,My_Account_Manage_Appointment,New_Line_Sale
	}
	public enum ACCESSIBILITYAUTHENTICATED{
		Zero,one
	}
	public enum ACCESSIBILITYREQUESTED{
		Zero,one
	}
	public enum General10{
		Yes,No
	}
	public enum CommsLevel {
		OrderLevel,AccountLevel,Any
	}
	public enum T_NETWORK_KEYS_NK_ID{
		IPS,MPF,WLR2,WLR3,OBC,SMPF,DNA,IPTV,NGA,SMPF_T3,LLU
	}
	private Connection con=null;
	private PreparedStatement stm = null;

	


	public DbUtilities(Reporter report) {
		Report = report;
	}

	/**
	 * @param CLI
	 * @param provand
	 * @param ODB
	 * @throws Exception
	 */

	public void doProvision(String Str_CLI,String provCMD) throws Exception{

		DbUtilities DbU = new DbUtilities(Report);
		LLUProvision LLU = new LLUProvision(Report);
		NGAProvision NGA = new NGAProvision(Report);
		EVGProvision EVG = new EVGProvision(Report);
		CPEGProvision CPEG = new CPEGProvision(Report);
		TVCPEGProvision TVCPEG = new TVCPEGProvision(Report);
		IPTVProvision IPTV = new IPTVProvision(Report);
		SMPFProvision SMPF =  new SMPFProvision(Report);
		StubFilePlacing SF = new StubFilePlacing(Report);
		OrderDetailsBean ODB = new OrderDetailsBean();
		FTTPProvision FTTP = new FTTPProvision(Report);
		SMPFT3Provision SMPFT3 =  new SMPFT3Provision(Report);		
		WLR3Provision WLR3 = new WLR3Provision(Report);
		IPSProvision IPS = new IPSProvision(Report);
		OBCProvision OBC = new OBCProvision(Report);
		WLR3Provision WLR = new WLR3Provision(Report);
		CPSProvision CPS = new CPSProvision(Report);
		VMProvision VM = new VMProvision(Report);
		NewDatabase ND=new NewDatabase(Report);
		
		String LLUType="MPF";
		String Error="";
		boolean Porting=true;
		boolean IPTVreq=false;
		boolean newCLI_used=false;
		int InitialState=0;
		String CancelReason="Cancelled By Customer";
		String Str_NewCLI=NameGenerator.randomCLI(9);
		String Str_Account=ND.getAcc_CLI(Str_CLI, true);
		for(String PRVCMD:provCMD.split(",")){
			if(newCLI_used)
			{
				Str_CLI=Str_NewCLI;
			}
			switch(PRVCMD){
			//LLU Provisioning Commands
			case "LLUModify":
				DbU.WaitforGwyCmdId(Str_CLI, "3", "LLUModify", true);
				LLU.LLUModify(Constants.LLU_Modify_Completed, Str_CLI, "0");
				break;
				
			case "LLUModify_AOD":
				DbU.WaitforGwyCmdId(Str_CLI, "3", "LLUModify_AOD", true);
				LLU.LLU_Modify_AOD(Constants.LLU_Modify_Completed, Str_CLI, Str_Account, LLUType);
				break;
				
			case "LLUSuspend":
				DbU.WaitforGwyCmdId(Str_CLI, "10", "LLUSuspend", true);
				LLU.LLUSuspend(Constants.LLU_Suspend_Completed, Str_CLI, "0");
				break;
				
			case "LLUSuspend_AOD":
				DbU.WaitforGwyCmdId(Str_CLI, "10", "LLUSuspend_AOD", true);
				LLU.LLUSuspend_AOD(Constants.LLU_Suspend_Completed, Str_CLI, Str_Account, LLUType,InitialState);
				break;
				
			case "LLURenumber_AOD":
				DbU.WaitforGwyCmdId(Str_CLI, "4", "LLURenumber", true);
				LLU.LLU_Renumber_AOD(Constants.LLU_Renumber_Completed, Str_CLI, Str_NewCLI, LLUType, Str_Account);
				newCLI_used=true;
				break;
				
			case "LLUProvideTakeover":
				DbU.WaitforGwyCmdId("HM"+Str_CLI, "13", "LLUProvideTakeover", true);
				LLU.LLUProvideTakeover(Constants.LLU_PRT_Completed, Str_CLI, Str_NewCLI, "0");
				newCLI_used=true;
				break;	
				
			case "LLU_PRT_AOD":
				DbU.WaitforGwyCmdId("HM"+Str_CLI, "13", "LLUPRT_AOD", true);
				LLU.LLU_PRT_AOD(Constants.LLU_PRT_Completed, "HM"+Str_CLI, Str_NewCLI, Str_Account, LLUType,InitialState);
				newCLI_used=true;
				break;	
				
			case "LLUProvideTakeover_KCI2":
				DbU.WaitforGwyCmdId("HM"+Str_CLI, "13", "LLUProvideTakeover_KCI2", true);
				LLU.LLUProvideTakeover_KCI2(Constants.LLU_PRT_Completed, "HM"+Str_CLI, Str_NewCLI,"0");
				newCLI_used=true;
				break;
				
			case "LLUProvideTakeover_SIM2":
				DbU.WaitforGwyCmdId("HM"+Str_CLI, "13", "LLUProvideTakeover_SIM2", true);
				LLU.LLUProvideTakeover_SIM2(Constants.LLU_PRT_Completed, "HM"+Str_CLI, Str_NewCLI,"0");
				newCLI_used=true;
				break;
				
			case "LLUProvideTakeover_SIM2_KCI2":
				DbU.WaitforGwyCmdId("HM"+Str_CLI, "13", "LLUProvideTakeover_SIM2_KCI2", true);
				LLU.LLUProvideTakeover_SIM2_KCI2(Constants.LLU_PRT_Completed, "HM"+Str_CLI, Str_NewCLI,"0");
				newCLI_used=true;
				break;
				
			case "LLUMigrate":
				DbU.WaitforGwyCmdId(Str_CLI, "1", "LLUMigrate", true);
				LLU.LLUMigrate(Constants.LLU_Migrate_Completed, Str_CLI, "0");
				break;
				
			case "LLUMigrate_AOD":
				DbU.WaitforGwyCmdId(Str_CLI, "1", "LLUMigrate_AOD", true);
				LLU.LLU_Migrate_AOD(Constants.LLU_Migrate_Completed, Str_CLI, Str_Account, LLUType, InitialState);
				break;	
				
			case "LLUMigrate_SIM2":
				DbU.WaitforGwyCmdId(Str_CLI, "1", "LLUMigrate_SIM2", true);
				LLU.LLUMigrate_SIM2(Constants.LLU_Migrate_Completed, Str_CLI,Porting,IPTVreq);
				break;
				
			case "LLUCancel":
				DbU.WaitforGwyCmdId(Str_CLI, "0", "LLUCancel", true);
				LLU.LLUCancel(Constants.LLU_Cancel_Completed, Str_CLI);
				break;
				
			case "LLUCancel_AOD":
				DbU.WaitforGwyCmdId(Str_CLI, "0", "LLUCancel_AOD", true);
				LLU.LLU_Cancel_AOD(Constants.LLU_Cancel_Completed, Str_CLI, CancelReason,LLUType);
				break;
				
			case "LLUCease":
				DbU.WaitforGwyCmdId(Str_CLI, "2", "LLUCease", true);
				LLU.LLUCease(Constants.LLU_Cease_Completed, Str_CLI);
				break;
				
			case "LLUCease_AOD":
				DbU.WaitforGwyCmdId(Str_CLI, "2", "LLUCease_AOD", true);
				LLU.LLUCease_AOD(Constants.LLU_Cease_Completed, Str_CLI, Str_Account, LLUType, 0);
				break;
				
			case "LLUAmend":
				DbU.WaitforGwyCmdId(Str_CLI, "A", "LLUAmend", true);
				LLU.LLUAmend(Constants.LLU_Amend_Completed, Str_CLI);
				break;
				
			case "LLUAmend_AOD":
				DbU.WaitforGwyCmdId(Str_CLI, "A", "LLUAmend_AOD", true);
				LLU.LLUAmend_AOD(Constants.LLU_Cease_Completed, Str_CLI, Str_Account, LLUType, 0);
				break;	
				
			case "LLUAmend_UpdateOrder":
				DbU.WaitforGwyCmdId(Str_CLI, "A", "LLUAmend_UpdateOrder", true);
				LLU.LLUAmend_UpdateOrder(Constants.LLU_Cease_Completed, Str_CLI);
				break;
				
			case "LLUProvideNew":
				DbU.WaitforGwyCmdId("HM"+Str_CLI, "13", "LLUProvideNew", true);
				LLU.LLUProvideNew(Constants.LLU_PRN_Completed, Str_CLI, Str_NewCLI,"0");
				newCLI_used=true;
				break;
				
			case "LLUProvideNew_AOD":
				DbU.WaitforGwyCmdId("HM"+Str_CLI, "13", "LLUProvideNew_AOD", true);
				LLU.LLU_PRN_AOD(Constants.LLU_PRN_Completed, "HM"+Str_CLI, InitialState, Str_Account, Str_NewCLI);
				newCLI_used=true;
				break;
				
			case "LLUProvideNew_RetainNo":
				DbU.WaitforGwyCmdId("HM"+Str_CLI, "13", "LLUProvideNew_RetainNo", true);
				LLU.LLUProvideNew_RetainNo(Constants.LLU_PRN_Completed, Str_CLI, Str_NewCLI,"0");
				newCLI_used=true;
				break;
				
			case "LLUProvideNew_SIM2":
				DbU.WaitforGwyCmdId("HM"+Str_CLI, "13", "LLUProvideNew_RetainNo", true);
				LLU.LLUProvideNew_SIM2(Constants.LLU_PRN_Completed, Str_CLI, Str_NewCLI,"0");
				newCLI_used=true;
				break;
				
			case "LLURenumber":
				DbU.WaitforGwyCmdId(Str_CLI, "4", "LLURenumber", true);
				LLU.LLURenumber(Constants.LLU_Renumber_Completed, Str_CLI, Str_NewCLI);
				newCLI_used=true;
				break;
				
			
				//NGA Provisioning Commands
				
			case "NGAProvide":
//				if (FibreInstallationMethod.equalsIgnoreCase("selfinstall")){ 
//					DbU.WaitforGwyCmdId(Str_CLI, "13", "NGAProvide", true);
//					NGA.NGAProvide_selfinstall(Constants.Completed, Str_CLI, "");
//				}
//				else{
					DbU.WaitforGwyCmdId(Str_CLI, "13", "NGAProvide", true);
					NGA.NGAProvide_AOD(Constants.NGAProvide_Completed, Str_CLI, Str_Account, InitialState);
//				}
				break;
				
			case "NGAProvide_SIM2":
					DbU.WaitforGwyCmdId(Str_CLI, "13", "NGAProvide_SIM2", true);
					NGA.NGAProvide_SIM2(Constants.NGAProvide_Completed, Str_CLI,"0");
				break;
				
			case "NGAProvide_AOD_SIM2":
				DbU.WaitforGwyCmdId(Str_CLI, "13", "NGAProvide_AOD_SIM2", true);
				NGA.NGAProvide_AOD_SIM2(Constants.NGAProvide_Completed, Str_CLI, Str_Account, InitialState);
			break;
				
			case "NGAProvide_AOD":
				DbU.WaitforGwyCmdId(Str_CLI, "13", "NGAProvide_AOD", true);
				NGA.NGAProvide_AOD(Constants.NGAProvide_Completed, Str_CLI, Str_Account, InitialState);
			break;
				
			case "NGAProvideDoubleMigration":
					DbU.WaitforGwyCmdId(Str_CLI, "13", "NGAProvideDoubleMigration", true);
					NGA.NGAProvideDoubleMigration(Constants.NGAProvide_Completed, Str_CLI,"0");
				break;
				
			case "NGAAmend_AOD":
				DbU.WaitforGwyCmdId(Str_CLI, "A", "NGAAmend_AOD", true);
				NGA.NGAAmend_AOD(Constants.NGA_Amend_Completed, Str_CLI, InitialState);
			break;
			
			case "NGAProvide_SelfInstall":
				DbU.WaitforGwyCmdId(Str_CLI, "13", "NGAProvide_SelfInstall", true);
				NGA.NGAProvide_selfinstall(Constants.NGAProvide_Completed, Str_CLI,"0");
			break;
			
			case "NGAProvide_KCI2":
				DbU.WaitforGwyCmdId(Str_CLI, "13", "NGAProvide_KCI2", true);
				NGA.NGAProvide_KCI2(Constants.NGAProvide_Completed, Str_CLI, "0");
			break;
				
			case "NGAModify":
				DbU.WaitforGwyCmdId(Str_CLI, "3", "NGAModify", true);
				NGA.NGAModify(Constants.NGA_Modify_Completed, Str_CLI);
				break;
				
			case "NGAModify_AOD":
				DbU.WaitforGwyCmdId(Str_CLI, "3", "NGAModify_AOD", true);
				NGA.NGA_Modify_AOD(Constants.NGA_Modify_Completed, Str_CLI, Str_Account, 0);
				break;	

			case "NGACancel":
				DbU.WaitforGwyCmdId(Str_CLI, "0", "NGACancel", true);
				NGA.NGACancel(Constants.NGA_Modify_Completed,Str_CLI);
				break;
				
			case "NGACancel_AOD":
				DbU.WaitforGwyCmdId(Str_CLI, "0", "NGACancel_AOD", true);
				NGA.NGA_Cancel_AOD(Constants.NGA_Modify_Completed,Str_CLI,Error,InitialState);
				break;
				
			case "NGASuspend_AOD":
				DbU.WaitforGwyCmdId(Str_CLI, "10", "NGASuspend_AOD", true);
				NGA.NGASuspend_AOD(Constants.NGASuspend_Completed, Str_CLI, InitialState);
			break;
			
				//IPTV Provisioning Commands
				
			case "IPTVProvide":
				DbU.WaitforGwyCmdId(Str_CLI, "13", "IPTVProvide", true);
				IPTV.IPTVProvide(Constants.Completed, Str_CLI);
				break;

			case "IPTVModify":
				String CPWNRef = null;
				//DbU.WaitforGwyCmdId(Str_CLI, "3", "IPTVModify", true);
				DbU.getOrderDetails(Str_CLI, "IPTVModify", ODB);
				DbU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
				SF.PlaceFile(StubType.IPTVModify, CPWNRef);
				break;	
				
			case "IPTVCease":
				//DbU.WaitforGwyCmdId(Str_CLI, "3", "IPTVModify", true);
				DbU.getOrderDetails(Str_CLI, "IPTVCease", ODB);
				DbU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
				SF.PlaceFile(StubType.IPTVCease, CPWNRef);
				break;	
				
			case "SubmitCPEOrder" :
				DbU.WaitforGwyCmdId(Str_CLI, "13", "SubmitCPEOrder", true);
				CPEG.CPEGProvide(Constants.Completed, Str_CLI);
				break;
				
			case "SubmitTVCPEOrder":
				DbU.WaitforGwyCmdId(Str_CLI, "13", "SubmitTVCPEOrder", true);
				TVCPEG.TVCPEGProvide(Constants.Completed, Str_CLI);
				break;
				
				//FTTP Provisioning Commands	
				
			case "FTTPProvide":
				DbU.WaitforGwyCmdId(Str_CLI, "13", "FTTPProvide", true);
				FTTP.FTTPProvide_(Constants.FTTP_Provide_Completed, Str_CLI, Str_Account);
				break;
				
			case "FTTPAmend":
				DbU.WaitforGwyCmdId(Str_CLI, "A", "FTTPAmend", true);
				FTTP.FTTPAmend(Constants.FTTP_Amend_Completed,Str_CLI,Str_Account,InitialState);
				break;
				
			case "FTTPCancel":
				DbU.WaitforGwyCmdId(Str_CLI, "0", "FTTPCancel", true);
				FTTP.FTTPCancel(Constants.FTTPCancel_Completed,Str_CLI, Str_Account, CancelReason, InitialState);
				break;
				
			case "FTTPSuspend":
				DbU.WaitforGwyCmdId(Str_CLI, "10", "FTTPSuspend", true);
				FTTP.FTTPSuspend_(Constants.FTTP_Suspend_Completed,Str_CLI, Str_Account,InitialState);
				break;
				
			case "FTTPModify":
				DbU.WaitforGwyCmdId(Str_CLI, "3", "FTTPModify", true);
				FTTP.FTTPModify_(Constants.FTTP_Modify_Completed,Str_CLI, Str_Account,InitialState);
				break;
				
			case "FTTPCease":
				DbU.WaitforGwyCmdId(Str_CLI, "2", "FTTPCease", true);
				FTTP.FTTPCease_(Constants.FTTP_Cease_Completed,Str_CLI, Str_Account,InitialState);
				break;
				
				// EVG Provisioning commnds
				
			case "EVGConfirmAppointment":
				DbU.WaitforGwyCmdId(Str_CLI, "1", "EVGConfirmAppointment", true);
				EVG.EVGProvide(Constants.Completed, Str_CLI);
				break;
			
				// SMPF Provisioning Commands
//			case "SMPFProvide":
//				DbU.WaitforGwyCmdId(Str_CLI, "1", "SMPFProvide", true);
//				SMPF.SMPFProvide(Constants.Completed, Str_CLI, "");
//				break;
//				
//			case "SMPFMigrate":
//				DbU.WaitforGwyCmdId(Str_CLI, "1", "SMPFMigrate", true);
//				SMPF.SMPFMigrate(Constants.Completed, Str_CLI, "");
//				break;
//			
//			case "SMPFModify":
//				DbU.WaitforGwyCmdId(Str_CLI, "1", "SMPFModify", true);
//				SMPF.SMPFModify(Constants.Completed, Str_CLI, "");
//				break;
//			
//			case "SMPFCease":
//				DbU.WaitforGwyCmdId(Str_CLI, "2", "SMPFCease", true);
//				SMPF.SMPFCease(Constants.Completed, Str_CLI);
//				break;
//
//			case "SMPFT3Cease":
//				DbU.WaitforGwyCmdId(Str_CLI, "2", "SMPFT3Cease", true);
//				SMPFT3.SMPFT3Cease(Constants.Completed, Str_CLI);
//				break;
//				
//			case "SMPFT3Renumber":
//				DbU.WaitforGwyCmdId(Str_CLI, "4", "SMPFT3Renumber", true);
//				SMPFT3.SMPFT3Renumber(Constants.Completed, Str_CLI, Str_NewCLI);
//				break;
//				
//				// IPS PRovisioning Commands
//			
//				
//			case "IPSModify":
//				DbU.WaitforGwyCmdId(Str_CLI, "2", "IPSModify", true);
//				IPS.IPSModify(Constants.Completed, Str_CLI);
//				break;
//				
//			case "IPSCease":
//				DbU.WaitforGwyCmdId(Str_CLI, "2", "IPSCease", true);
//				IPS.IPSCease(Constants.Completed, Str_CLI);
//				break;
//				
//				
//			case "IPSSuspend" :
//				DbU.WaitforGwyCmdId(Str_CLI, "10", "IPSSuspend", true);
//				IPS.IPSSuspend("Completed", Str_CLI);
//				break;
//				
//			case "IPSProvide" :
//				DbU.WaitforGwyCmdId(Str_CLI, "10", "IPSProvide", true);
//				IPS.IPSProvide(Constants.IPS_Provide_Completed,Str_CLI,"0");
//				break;
//				
//			case "IPSProvide_AOD" :
//				DbU.WaitforGwyCmdId(Str_CLI, "10", "IPSProvide_AOD", true);
//				IPS.IPSProvide_AOD(Constants.IPS_Provide_Completed,Str_CLI, Str_Account,InitialState);
//				break;
//				
//			case "IPSMigrate_AOD" :
//				DbU.WaitforGwyCmdId(Str_CLI, "10", "IPSMigrate_AOD", true);
//				IPS.IPSMigrate_AOD(Constants.IPS_Provide_Completed,Str_CLI);
//				break;
//				
//			case "IPSRenumber":
//				DbU.WaitforGwyCmdId(Str_CLI, "4", "IPSRenumber", true);
//				IPS.IPSRenumber(Constants.Completed, Str_CLI, Str_NewCLI);
//				break;
//			
//			case "IPSUNCRENO":
//				DbU.WaitforGwyCmdId(Str_CLI, "4", "IPSUNCRENO", true);
//				IPS.IPSUNCRENO(Constants.Completed, Str_CLI, Str_NewCLI,ServiceResellerid);
//				break;
//				
//				// OBC PRovisioning Commands	
//				
//			case "OBCCease":
//				DbU.WaitforGwyCmdId(Str_CLI, "2", "OBCCease", true);
//				OBC.OBCCease(Constants.OBCCease_Completed, Str_CLI);
//				break;
//				
//			case "OBCCease_AOD":
//				DbU.WaitforGwyCmdId(Str_CLI, "2", "OBCCease_AOD", true);
//				OBC.OBCCease_AOD(Constants.OBCCease_Completed, Str_CLI, Str_Account, InitialState);
//				break;
//				
//			case "OBCSuspend_AOD":
//				DbU.WaitforGwyCmdId(Str_CLI, "10", "OBCSuspend_AOD", true);
//				OBC.OBC_Suspend_AOD(Constants.OBC_Suspend_Completed, Str_CLI,"0",Str_Account);
//				break;
//				
//			case "OBCCancel_AOD":
//				DbU.WaitforGwyCmdId(Str_CLI, "10", "OBCCancel_AOD", true);
//				OBC.OBC_Cancel_AOD(Constants.OBC_Cancel_Completed, Str_CLI,CancelReason);
//				break;
//				
			// WLR3 Provisioning Command
				
			case "WLR3Cease":
				DbU.WaitforGwyCmdId(Str_CLI, "2", "WLR3Cease", true);
				WLR3.WLR3Cease(Constants.Completed, Str_CLI);
				break;
				
			case "WLR3Modify":
				DbU.WaitforGwyCmdId(Str_CLI, "3", "WLR3Modify", true);
				WLR3.WLR3Modify(Constants.Completed, Str_CLI);
				break;
				
			case "WLR3Suspend":
				DbU.WaitforGwyCmdId(Str_CLI, "10", "WLR3Suspend", true);
				WLR3.WLR3Suspend(Constants.Completed, Str_CLI);
				break;
				
			case "WLR3Trasfer":
				DbU.WaitforGwyCmdId(Str_CLI, "2", "WLR3Trasfer", true);
				WLR3.WLR3Transfer(Constants.Completed, Str_CLI,"0");
				break;
				
			case "WLR3UpdateDebtManagementFeatures":
				DbU.WaitforGwyCmdId(Str_CLI, "10", "WLR3UpdateDebtManagementFeatures", true);
				WLR.WLR3Suspend("Completed", Str_CLI);
				break;
			
			case "WLR3Renumber":
				DbU.WaitforGwyCmdId(Str_CLI, "4", "WLR3Renumber", true);
				WLR3.WLR3Renumber(Constants.Completed, Str_CLI, Str_NewCLI, "");
				newCLI_used=true;
				break;
	
//			case "VMRenumber":
//				DbU.WaitforGwyCmdId(Str_CLI, "4", "VMRenumber", true);
//				VM.VMRenumber_AOD(Constants.Completed, Str_CLI, Str_NewCLI, 0);
//				break;
	
			}
		}
	}
	
	public boolean GenericProvision(String CLI) throws Exception
	{
		boolean CompleteProvision=true;
		String OrderID="";
		while(!OrderID.equalsIgnoreCase("COMPLETED"))
		{
			OrderID=getOrderIDProv("02077200422");
			if(OrderID.equalsIgnoreCase("INVALID"))
			{
				CompleteProvision=false;
				break;
			}
			else if(OrderID.equalsIgnoreCase("COMPLETED"))
			{
				break;
			}
			else
			{
				String cmd=getProvCmd(OrderID);
				if(!cmd.equals(""))
				{
					doProvision(CLI,getProvCmd(OrderID));
				}
				else
				{
					continue;
				}
			}
		}
		return CompleteProvision;
	}
	
	public String getOrderIDProv(String CLI) throws Exception
	{
		String query;
		String ORDERID="";
		ResultSet rs = null;
	    con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);
		
	    query = " select status,orderid from (Select max(s.orderstatus) status,s.orderid from OM_BW_USER.cpworderstatus s where orderid in (" +
	    		" Select o.orderid from OM_BW_USER.cpworder o where o.cli='"+CLI+"'" +
	    		" ) group by s.orderid order by status) where status not like '4'";
	
		 try{
			PreparedStatement stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

			rs=stm.executeQuery();

			if(rs.next()){
				rs.beforeFirst();

				while(rs.next()){
					if(Integer.valueOf(rs.getString("status"))>4)
					{
						ORDERID="INVALID";
						break;
					}
					if(Integer.valueOf(rs.getString("status"))==2)
					{
						ORDERID+=rs.getString("orderid")+",";
					}
				}
			}else{
				ORDERID="COMPLETED";
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionFactory.closeConnection(con);
		}
		return ORDERID.replaceAll("(,)*$", "");
	}
	public String getProvCmd(String ORDERID) throws Exception
	{
		String query;
		String ProvCmd="";
		ResultSet rs = null;
	    con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);
		
	    query = " select o.PROVCMDNAME, o.provcmdinstid, o.provcmdgwycmdid from OM_BW_USER.cpworder c,OM_BW_USER.cpwprovcmd o where o.ORDERID=c.ORDERID " +
	    		" and o.orderid='"+ORDERID+"' and o.INVALIDCOMMAND is null and o.PROVCMDGWYCMDERROR is null and o.provcmdgwycmdid is not null and o.PROVCMDINSTID is not null and o.provcmdgwycmdid not in ('0') and o.PROVCMDINSTID not in ('0')";
	
		 try{
			PreparedStatement stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

			rs=stm.executeQuery();

			if(rs.next()){
				rs.beforeFirst();

				while(rs.next()){
					ProvCmd+=rs.getString(1)+",";
				}
			}else{
				ProvCmd="NONE";
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionFactory.closeConnection(con);
		}
		return ProvCmd.replaceAll("(,)*$", "");
	}
	public void ManageAdjustments_DbVerify(String account, String adjReason, String adjAmount, String adjType, String adjComments) throws Exception {
		String query;
			ResultSet rs = null;
		    con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SV_DBIP+":"+LoadEnvironment.SV_DBPORT+":"+LoadEnvironment.SV_DBNAME,LoadEnvironment.SV_DBUSERNAME,LoadEnvironment.SV_DBPASSWORD);
			
		    query = "select * from "+LoadEnvironment.SV_DBSCHEMA+".adjustment where to_account_id in(select account_id from "+LoadEnvironment.SV_DBSCHEMA+".ACCOUNT acc "+"where acc.account_name in '"+account+"') order by last_modified desc";
		
			 try{
				PreparedStatement stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

				rs=stm.executeQuery();

				if(rs.next()){
					rs.beforeFirst();

					while(rs.next()){
						
						if((rs.getString("AMOUNT").equals(adjAmount))&&(rs.getString("DESCRIPTION").equalsIgnoreCase(adjComments))){
							Report.fnReportPass("Adjustment details are updated successfully in SV db for Account : " + account + "");
							String adjID = rs.getString("ADJUSTMENT_ID");
							System.out.println("ADJUSTMENT_ID : " + adjID);
							Report.fnReportPass("Adjustment ID in SV db for Account : " + account + " is " + adjID+ "");	
							Report.fnReportPass("Adjustment Amount in SV db for Account : " + account + " is " + adjAmount+ "");	
						}else{
							//Report.fnReportFail("Package status in SV db for Account : " + account + " is " + StatusName + " Where as the expected is ");
						}
									
						rs.afterLast();

					}
				}else{

					throw new RuntimeException("Package status in SV db for Account : " + account + "Failed : No Records found");
				}
			}catch(RuntimeException e){
				if(e.getMessage().equals("Failed : No Records found")){

					Report.fnReportFailAndTerminateTest("Get details","Failed : No Records found" );
				}
			}finally{
				ConnectionFactory.closeConnection(con);
			}
		}	
	public String UPRN(String Str_CLI) throws Exception{
		String query = null;
		ResultSet rs = null;
		String UPRN = null;

		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);
		try{
			query = "select o.UPRN from OM_BW_USER.cpwaddress o inner join OM_BW_USER.cpwprovcmd c on o.ORDERID=c.ORDERID and c.CLI = '"+Str_CLI+"' order by c.LASTUPDATED desc";
                
        	stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			System.out.println("Query Executed");

			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){

					UPRN = rs.getString("UPRN");
					System.out.println(UPRN);
					rs.afterLast();
				}
			}else{
				throw new RuntimeException("NO ROWS");
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("NO ROWS")){
				Report.fnReportFail("UPRN is not present for  : " +Str_CLI);
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
		return UPRN;
	 } 
	public void MarketingPref_dbVerify_CRM(String Str_CLI,String String_Literature,String String_Email,String String_TeleMarketing,String String_DirectMailing,String String_Online,String String_CallCenter) throws Exception {
		String query;
			ResultSet rs = null;
			String[] Data_rsValues={"MARKETINGLITERATUREFLAG","EMAILFLAG","TELEMARKETINGFLAG","DIRECTMAILINGFLAG","ONLINEFLAG","CALLCENTREXSELLUPSELLFLAG"};
			String[] Data_Flags={String_Literature,String_Email,String_TeleMarketing,String_DirectMailing,String_Online,String_CallCenter};
		    con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
			
		    query = "select * from CBLOWNER.CPWCUSTOMER where partyroleid in(Select partyroleid from cblowner.cpwpartyroleaccount where accountid in(select accountid from cblowner.portfoliosalespackage where cli='"+Str_CLI+"'))";
		
			 try{
				PreparedStatement stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

				rs=stm.executeQuery();

				if(rs.next()){
					rs.beforeFirst();

					while(rs.next()){
						
						for(int i=0;i<Data_rsValues.length;i++)
						{
							if(Data_Flags[i]!="")
							{
								if(Data_Flags[i].equalsIgnoreCase(rs.getString(Data_rsValues[i])))
								{
									Report.fnReportPass("The "+Data_rsValues[i]+" is "+Data_Flags[i]);
								}
								else
								{
									Report.fnReportFail("The "+Data_rsValues[i]+" is "+rs.getString(Data_rsValues[i]) +"Not "+Data_Flags[i]);
								}
							}
						}
									
						rs.afterLast();

					}
				}else{

					throw new RuntimeException("Customer Preferences are not updated in CRM db for CLI : " + Str_CLI + "Failed : No Records found");
				}
			}catch(RuntimeException e){
				if(e.getMessage().equals("Failed : No Records found")){

					Report.fnReportFailAndTerminateTest("Get details","Failed : No Records found" );
				}
			}finally{
				ConnectionFactory.closeConnection(con);
			}
		}	
	
	public String OMP_ColtID(String OrderID,String ByRefcoltid1) throws Exception{
		String query = null;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);
		
        query = "select OM_ORDPROCDIRID from OM_OrderProcDirective where  orderid ='" + OrderID + "'";
		try{
			stm=con.prepareStatement(query);
			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){
					
					ByRefcoltid1 = rs.getString("OM_ORDPROCDIRID");
					System.out.println(ByRefcoltid1);
					Report.fnReportPass("COLT is present for  : " + OrderID);
					rs.afterLast();
				}
			}else{
				throw new RuntimeException("NO ROWS");
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("NO ROWS")){
				Report.fnReportFail("COLT is not present for  : " + OrderID);
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
		return ByRefcoltid1;
	 }
	
	public void EVGDB_SalesPackageVerification(String Str_CLI,String PackageID,String... Str_ProvCmd) throws Exception{
		String query = null;
		ResultSet rs = null;
		
		OrderDetailsBean ODB = new OrderDetailsBean();
		AppointmentBean AB = new AppointmentBean();
		if(Str_ProvCmd.length>0)
		{
			getOrderDetails(Str_CLI, Str_ProvCmd[0], ODB);
		}
		else
		{
			getOrderDetails(Str_CLI, "EVGConfirmAppointment", ODB);
		}
		getAppointmentDetails(ODB.getPROVCMDINSTID(), AB);

		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.EVG_DBIP+":"+LoadEnvironment.EVG_DBPORT+":"+LoadEnvironment.EVG_DBNAME,LoadEnvironment.EVG_DBUSERNAME,LoadEnvironment.EVG_DBPASSWORD);
		
        query = "select * from sls_pckg where sls_pckg_id in (select sales_package_id from confirmed_visit where visit_ref_id='"+AB.getAPPOINTMENTID()+"')";
		try{
			stm=con.prepareStatement(query);
			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();
				int i=0;
				while(rs.next()){
					if(rs.getString("SLS_PCKG_ID")==PackageID)
					{
						Report.fnReportPass("The Sales Package id updated correctly as : " + rs.getString("SLS_PCKG_ID"));
						i=1;
					}
					rs.afterLast();
				}
				if(i==0)
				{
					Report.fnReportFail("The Sales Package id NOT updated correctly as : " + rs.getString("SLS_PCKG_ID"));
				}
			}else{
				throw new RuntimeException("NO ROWS");
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("NO ROWS")){
				Report.fnReportFail("EVGDB is not Updated... No Command found");
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	 }
	
	
	public String CPW_ColtID(String OrderID, String ByRefcoltid) throws Exception{
		String query = null;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);

        query = "select COLTRESULTREFERENCE from CPW_OrderProcessingDirective where  orderid ='" + OrderID + "'";
		try{
			stm=con.prepareStatement(query);
			rs=stm.executeQuery();

			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){

					Report.fnReportFail("COLT is not present for  : " + OrderID);
					rs.afterLast();
				}
			}else{
				throw new RuntimeException("NO ROWS");
			}

			ByRefcoltid = rs.getString("COLTRESULTREFERENCE");
			System.out.println(ByRefcoltid);

		}catch(RuntimeException e){

			if(e.getMessage().equals("NO ROWS")){
			Report.fnReportPass("COLT is present for  : " + OrderID);
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	 return ByRefcoltid;
	 }
	
	public String CPW_ColtID_neg(String OrderID, String ByRefcoltid) throws Exception{
		String query = null;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);
		
        query = "select COLTRESULTREFERENCE from CPW_OrderProcessingDirective where  orderid ='" + OrderID + "'";
		try{
			stm=con.prepareStatement(query);
			rs=stm.executeQuery();
			
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){
					
					ByRefcoltid = rs.getString("COLTRESULTREFERENCE");
					System.out.println(ByRefcoltid);
					
					Report.fnReportPass("COLT is not present for  : " + OrderID);
					rs.afterLast();
				}
			}else{
				throw new RuntimeException("NO ROWS");
			}
		}catch(RuntimeException e){

			if(e.getMessage().equals("NO ROWS")){
			Report.fnReportFail("COLT is present for  : " + OrderID);
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	 return ByRefcoltid;
	 }
	
	public String getResultCPEG1(String query,String data) throws Exception {

		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CPEG_DBIP+":"+LoadEnvironment.CPEG_DBPORT+":"+LoadEnvironment.CPEG_DBNAME,LoadEnvironment.CPEG_DBUSERNAME,LoadEnvironment.CPEG_DBPASSWORD);
		int j=0;
		try{
			PreparedStatement stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			System.out.println(query);
			if(query.toUpperCase().startsWith("UPDATE"))
			{
				j=stm.executeUpdate();
				if(j==0)
				{
					Report.fnReportFailAndTerminateTest("Update Query", "Table not updated");
				}
			}
			else
			{
				rs=stm.executeQuery();
			}
			if(query.startsWith("select"))
			{
				if(rs.next())
				{
					if(data!="")
					{
						String[] Data=data.split(",");
						data=rs.getString(Data[0]);
						for(int i=1;i<Data.length;i++)
						{
							data=data+","+rs.getString(Data[i]);
						}
					}
					else
					{
						data="";
					}
				}else{
					throw new RuntimeException("NO AND FOUND");
				}
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("NO COMMAND FOUND")){

				Report.fnReportFailAndTerminateTest("Get Command details","NO COMMAND FOUND" );
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
		return data;
	}
	
	public void getOrderDetails(String CLI, String provand, OrderDetailsBean ODB) throws Exception {
		String query;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);

		query="select c.PROVCMDNAME,o.BRANDID,o.ORDERID,c.PROVCMDGWYCMDID,c.FIBRELINKEDORDERREF,c.PROVCMDINSTID,o.CASR,C.OMAPPOINTMENTID, "
				+ " c.serviceresellerid,c.resourceid,c.resourcetype,o.activityid,o.ordertype from "+LoadEnvironment.OMP_DBSCHEMA+".cpworder "
				+ " o inner join "+LoadEnvironment.OMP_DBSCHEMA+".cpwprovcmd c on o.ORDERID=c.ORDERID and o.CLI='"+CLI+"'"
				+ " and c.PROVCMDNAME like '"+provand+"%' "
				+ " order by c.LASTUPDATED desc";
		/* deleted 07- Feb - 2014
		 * 	+ " and c.PROVCMDGWYCMDERROR is null " 
		 *	+ " and c.INVALIDAND is null "
		 */
		System.out.println(query);
		try{
			PreparedStatement stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();

				while(rs.next()){
					ODB.setPROVCMDNAME(rs.getString("PROVCMDNAME"));
					ODB.setBRANDID(rs.getString("BRANDID"));
					ODB.setORDERID(rs.getString("ORDERID"));
					ODB.setPROVCMDGWYCMDID(rs.getString("PROVCMDGWYCMDID"));
					ODB.setFIBRELINKEDORDERREF(rs.getString("FIBRELINKEDORDERREF"));
					ODB.setPROVCMDINSTID(rs.getString("PROVCMDINSTID"));
					ODB.setCASR(rs.getString("CASR"));
					ODB.setOMAPPOINTMENTID(rs.getString("OMAPPOINTMENTID"));
					ODB.setServiceresellerid(rs.getString("serviceresellerid"));
					ODB.setResourceid(rs.getString("resourceid"));
					ODB.setResourcetype(rs.getString("resourcetype"));
					ODB.setActivityid(rs.getString("activityid"));
					ODB.setOrdertype(rs.getString("ordertype"));
					rs.afterLast();

				}

				if((ODB.getPROVCMDGWYCMDID().equalsIgnoreCase(""))||(ODB.getPROVCMDINSTID().equalsIgnoreCase("0"))
						||(ODB.getPROVCMDGWYCMDID().equalsIgnoreCase("0"))||(ODB.getPROVCMDINSTID().equalsIgnoreCase("0"))){

					throw new RuntimeException(" gwycmdid or cmdinstid not found in the OMP db or '0'");
				}else{
					//	fnReportPass("Provision "+provand+" found with gatewayID  "+ODB.getPROVCMDGWYCMDID());
				}


			}else{

				throw new RuntimeException("NO AND FOUND");
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("NO AND FOUND")){

				Report.fnReportFailAndTerminateTest("Getand details","NO AND FOUND" );
			}else{
				Report.fnReportFailAndTerminateTest("Getand details"," gwycmdid or cmdinstid not found in the OMP db or '0'");
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}
	
	public void getOrderDetails(String CLI, String provCommand, OrderDetailsBean ODB,boolean verifyAll) throws Exception {
		String query;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);

		query="select c.PROVCMDNAME,o.BRANDID,o.ORDERID,c.PROVCMDGWYCMDID,c.FIBRELINKEDORDERREF,c.PROVCMDINSTID,o.CASR,C.OMAPPOINTMENTID, "
				+ " c.serviceresellerid,c.resourceid,c.resourcetype,o.activityid,o.ordertype from "+LoadEnvironment.OMP_DBSCHEMA+".cpworder "
				+ " o inner join "+LoadEnvironment.OMP_DBSCHEMA+".cpwprovcmd c on o.ORDERID=c.ORDERID and o.CLI='"+CLI+"'"
				+ " and c.PROVCMDNAME like '"+provCommand+"%' "
				+ " order by c.LASTUPDATED desc";
		/* deleted 07- Feb - 2014
		 * 	+ " and c.PROVCMDGWYCMDERROR is null " 
		 *	+ " and c.INVALIDCOMMAND is null "
		 */
		System.out.println(query);
		try{
			PreparedStatement stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

			rs=stm.executeQuery();

			if(rs.next()){
				rs.beforeFirst();

				while(rs.next()){
					ODB.setPROVCMDNAME(rs.getString("PROVCMDNAME"));
					ODB.setBRANDID(rs.getString("BRANDID"));
					ODB.setORDERID(rs.getString("ORDERID"));
					ODB.setPROVCMDGWYCMDID(rs.getString("PROVCMDGWYCMDID"));
					ODB.setFIBRELINKEDORDERREF(rs.getString("FIBRELINKEDORDERREF"));
					ODB.setPROVCMDINSTID(rs.getString("PROVCMDINSTID"));
					ODB.setCASR(rs.getString("CASR"));
					ODB.setOMAPPOINTMENTID(rs.getString("OMAPPOINTMENTID"));
					ODB.setServiceresellerid(rs.getString("serviceresellerid"));
					ODB.setResourceid(rs.getString("resourceid"));
					ODB.setResourcetype(rs.getString("resourcetype"));
					ODB.setActivityid(rs.getString("activityid"));
					ODB.setOrdertype(rs.getString("ordertype"));
					rs.afterLast();

				}
				if(verifyAll)
				{
					if((ODB.getPROVCMDGWYCMDID().equalsIgnoreCase(""))||(ODB.getPROVCMDINSTID().equalsIgnoreCase("0"))
							||(ODB.getPROVCMDGWYCMDID().equalsIgnoreCase("0"))||(ODB.getPROVCMDINSTID().equalsIgnoreCase("0"))){

						throw new RuntimeException(" gwycmdid or cmdinstid not found in the OMP db or '0'");
					}else{
						//	fnReportPass("Provision "+provCommand+" found with gatewayID  "+ODB.getPROVCMDGWYCMDID());
					}
				}		

			}else{

				throw new RuntimeException("NO COMMAND FOUND");
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("NO COMMAND FOUND")){

				Report.fnReportFailAndTerminateTest("GetCommand details","NO COMMAND FOUND" );
			}else{
				Report.fnReportFailAndTerminateTest("GetCommand details"," gwycmdid or cmdinstid not found in the OMP db or '0'");
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}
	
	public void getOrderDetails_EVGCancel(String CLI, String provand, OrderDetailsBean ODB) throws Exception {
		String query;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);

		query="select c.PROVCMDNAME,o.BRANDID,o.ORDERID,c.FIBRELINKEDORDERREF,c.PROVCMDINSTID,o.CASR,C.OMAPPOINTMENTID, "
				+ " c.serviceresellerid,c.resourceid,c.resourcetype,o.activityid,o.ordertype from "+LoadEnvironment.OMP_DBSCHEMA+".cpworder "
				+ " o inner join "+LoadEnvironment.OMP_DBSCHEMA+".cpwprovcmd c on o.ORDERID=c.ORDERID and o.CLI='"+CLI+"'"
				+ " and c.PROVCMDNAME like '"+provand+"%' "
				+ " order by c.LASTUPDATED desc";
		/* deleted 07- Feb - 2014
		 * 	+ " and c.PROVCMDGWYCMDERROR is null " 
		 *	+ " and c.INVALIDAND is null "
		 */
		System.out.println(query);
		try{
			PreparedStatement stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

			rs=stm.executeQuery();

			if(rs.next()){
				rs.beforeFirst();

				while(rs.next()){
					ODB.setPROVCMDNAME(rs.getString("PROVCMDNAME"));
					ODB.setBRANDID(rs.getString("BRANDID"));
					ODB.setORDERID(rs.getString("ORDERID"));
					ODB.setPROVCMDGWYCMDID(rs.getString("PROVCMDINSTID"));
					ODB.setFIBRELINKEDORDERREF(rs.getString("FIBRELINKEDORDERREF"));
					ODB.setPROVCMDINSTID(rs.getString("PROVCMDINSTID"));
					ODB.setCASR(rs.getString("CASR"));
					ODB.setOMAPPOINTMENTID(rs.getString("OMAPPOINTMENTID"));
					ODB.setServiceresellerid(rs.getString("serviceresellerid"));
					ODB.setResourceid(rs.getString("resourceid"));
					ODB.setResourcetype(rs.getString("resourcetype"));
					ODB.setActivityid(rs.getString("activityid"));
					ODB.setOrdertype(rs.getString("ordertype"));
					rs.afterLast();

				}

				if((ODB.getPROVCMDGWYCMDID().equalsIgnoreCase(""))||(ODB.getPROVCMDINSTID().equalsIgnoreCase("0"))
						||(ODB.getPROVCMDGWYCMDID().equalsIgnoreCase("0"))||(ODB.getPROVCMDINSTID().equalsIgnoreCase("0"))){

					throw new RuntimeException("cmdinstid not found in the OMP db or '0'");
				}else{
					//	fnReportPass("Provision "+provand+" found with gatewayID  "+ODB.getPROVCMDGWYCMDID());
				}


			}else{

				throw new RuntimeException("NO AND FOUND");
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("NO AND FOUND")){

				Report.fnReportFailAndTerminateTest("Getand details","NO AND FOUND" );
			}else{
				Report.fnReportFailAndTerminateTest("Getand details"," gwycmdid or cmdinstid not found in the OMP db or '0'");
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}
	public void getCRD(String CLI, String provCommand, OrderDetailsBean ODB) throws Exception {
		String query;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);

		query="select o.CUSTOMERREQUIREDDATE,c.PROVCMDINSTID,c.PROVCMDGWYCMDID,c.serviceresellerid from "+LoadEnvironment.OMP_DBSCHEMA+".cpworder "
				+ " o inner join "+LoadEnvironment.OMP_DBSCHEMA+".cpwprovcmd c on o.ORDERID=c.ORDERID and o.CLI='"+CLI+"'"
				+ " and c.PROVCMDNAME like '"+provCommand+"%' "
				+ " order by c.LASTUPDATED desc";

		System.out.println(query);
		try{
			PreparedStatement stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

			rs=stm.executeQuery();

			if(rs.next()){
				rs.beforeFirst();

				while(rs.next()){
					ODB.setPROVCMDINSTID(rs.getString("PROVCMDINSTID"));
					ODB.setPROVCMDGWYCMDID(rs.getString("PROVCMDGWYCMDID"));
					ODB.setServiceresellerid(rs.getString("serviceresellerid"));
					ODB.setCRD(rs.getString("CUSTOMERREQUIREDDATE"));
					rs.afterLast();

				}

			}else{

				throw new RuntimeException("NO COMMAND FOUND");
			}
		}catch(RuntimeException e){


			Report.fnReportFailAndTerminateTest("GetCommand details","NO COMMAND FOUND" );

		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}

	/**
	 * @param provCommandID
	 * @param AB
	 * @throws Exception
	 */
	public void getAppointmentDetails(String provCommandID, AppointmentBean AB) throws Exception{
		String query;
		ResultSet rs = null;
		//Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);

		query="select a.APPOINTMENTID, a.SLOTDATE, a.SLOTSTARTTIME, a.SLOTENDTIME from "+LoadEnvironment.OMP_DBSCHEMA+".cpwappointment a "
				+ "where a.PROVCMDINSTID ='"+provCommandID+"' order by a.APPOINTMENTID desc";
		System.out.println(query);
		try{
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){
					AB.setAPPOINTMENTID(rs.getString(1));
					String date1 = formatter.format(rs.getDate("SLOTDATE"));
					AB.setSLOTDATE(date1);
					AB.setSLOTSTARTTIME(rs.getString(3));
					AB.setSLOTENDTIME(rs.getString(4));

					rs.afterLast();
				}
			}else{

				throw new RuntimeException("NO APPOINTMENT FOUND");

			}
		}catch(RuntimeException e){

			if(e.getMessage().equals("NO APPOINTMENT FOUND")){

				System.out.println("NO APPOINTMENT FOUND for"+provCommandID);
			}

		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}

	/**
	 * @param account
	 * @param PackageName
	 * @throws Exception
	 */
	public void CP_SVdbVerify(String account, String PackageName) throws Exception{
		String query = null;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SV_DBIP+":"+LoadEnvironment.SV_DBPORT+":"+LoadEnvironment.SV_DBNAME,LoadEnvironment.SV_DBUSERNAME,LoadEnvironment.SV_DBPASSWORD);
		/*query="select (SELECT ABBREVIATION FROM SV_DBSCHEMA.REFERENCE_CODE WHERE REFERENCE_TYPE_ID = 9000160 AND REFERENCE_CODE = general_1) "
				+ "Package_name from "+LoadEnvironment.SV_DBSCHEMA+".product_instance_history where PRODUCT_INSTANCE_STATUS_CODE = 3 "
				+ "and base_product_instance_id is null"
				+ "and SYSDATE BETWEEN EFFECTIVE_START_DATE AND EFFECTIVE_END_DATE and customer_node_id in (Select acc.CUSTOMER_NODE_ID"
				+ " from "+LoadEnvironment.SV_DBSCHEMA+".ACCOUNT acc where acc.account_name = '"+account+"')";*/

		query="select (SELECT ABBREVIATION FROM "+LoadEnvironment.SV_DBSCHEMA+".REFERENCE_CODE WHERE REFERENCE_TYPE_ID = 9000160 AND REFERENCE_CODE = general_1) Package_name "
				+ " from "+LoadEnvironment.SV_DBSCHEMA+".product_instance_history "
				+ " where PRODUCT_INSTANCE_STATUS_CODE = 3"
				+ " and base_product_instance_id is null "
				+ " and SYSDATE BETWEEN EFFECTIVE_START_DATE AND EFFECTIVE_END_DATE "
				+ " and customer_node_id in (Select acc.CUSTOMER_NODE_ID from "+LoadEnvironment.SV_DBSCHEMA+".ACCOUNT acc"
				+ " where acc.account_name = '"+account+"')";
		try{
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){
					if(rs.getString("PACKAGE_NAME").equalsIgnoreCase(PackageName)) {
						Report.fnReportPass("Package Name in SV is " + rs.getString("PACKAGE_NAME"));
					}else{
						Report.fnReportFail("Package Name in SV is " + rs.getString("PACKAGE_NAME"));
					}
				}
			}else{

				throw new RuntimeException("NO ROWS");
			}
		}catch(RuntimeException e){

			if(e.getMessage().equals("No ROWS")){

				Report.fnReportFail(" No Records found in SV DB for " + account);
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}

	}

	/**
	 * @param CLI
	 * @param OrderStatus
	 * @throws Exception
	 */
	public void OrderStatus_OMPdbVerify(String CLI, String OrderStatus) throws Exception{
		String query = null;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);
		query="Select max(s.orderstatus) ORDERSTATUS from "+LoadEnvironment.OMP_DBSCHEMA+".cpworderstatus s where s.orderid in"
				+ " (Select o.orderid from "+LoadEnvironment.OMP_DBSCHEMA+".cpworder o where o.cli='"+CLI+"') order by"
				+ " s.lastupdated desc nulls last";
		try{
			stm=con.prepareStatement(query);
			rs=stm.executeQuery();

			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){

					if(rs.getString("ORDERSTATUS").equalsIgnoreCase(OrderStatus)) {
						Report.fnReportPass("The Order Status is " + rs.getString("ORDERSTATUS"));
					}else{
						Report.fnReportFail("The Order Status is " + rs.getString("ORDERSTATUS"));
					}
					rs.afterLast();
				}
			}else{

				throw new RuntimeException("NO ROWS");
			}
		}catch(RuntimeException e){

			if(e.getMessage().equals("No ROWS")){

				Report.fnReportFail(" No Records found in OMP DB DB for " + CLI);
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}

	/**
	 * @param Account
	 * @throws Exception
	 */
	public void UNC_SV_PackageStatus(String Account) throws Exception{
		String query = null;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SV_DBIP+":"+LoadEnvironment.SV_DBPORT+":"+LoadEnvironment.SV_DBNAME,LoadEnvironment.SV_DBUSERNAME,LoadEnvironment.SV_DBPASSWORD);
		query = "select PRODUCT_INSTANCE_STATUS_CODE,(SELECT ABBREVIATION FROM "+LoadEnvironment.SV_DBSCHEMA+".REFERENCE_CODE WHERE"
				+ " REFERENCE_TYPE_ID = 9000160 AND REFERENCE_CODE = general_1) Package_name from "+LoadEnvironment.SV_DBSCHEMA+".product_instance_history"
				+ " where PRODUCT_INSTANCE_STATUS_CODE = 9 and base_product_instance_id is null and SYSDATE+1 "
				+ " BETWEEN EFFECTIVE_START_DATE AND EFFECTIVE_END_DATE and customer_node_id in"
				+ " (Select acc.CUSTOMER_NODE_ID from "+LoadEnvironment.SV_DBSCHEMA+".ACCOUNT acc where acc.account_name = '"+Account+"')";
				try{
					stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					rs=stm.executeQuery();
					if(rs.next()){
						rs.beforeFirst();
						while(rs.next()){
							Report.fnReportPass("The status of the Package  " + rs.getString("Package_name") + " in SV is " + rs.getString("PRODUCT_INSTANCE_STATUS_CODE"));
						}
					}else{

						throw new RuntimeException ("NO ROWS");
					}
				}catch(RuntimeException e){

					if(e.getMessage().equals("No ROWS")){
						Report.fnReportFail(" No Records found in OMP DB DB for " + Account);
					}
				}finally{
					ConnectionFactory.closeConnection(con);
				}
	}

	public void UNC_Cancel_SV_PackageStatus(String Account) throws Exception{
		String query = null;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SV_DBIP+":"+LoadEnvironment.SV_DBPORT+":"+LoadEnvironment.SV_DBNAME,LoadEnvironment.SV_DBUSERNAME,LoadEnvironment.SV_DBPASSWORD);
		query = "select PRODUCT_INSTANCE_STATUS_CODE,(SELECT ABBREVIATION FROM "+LoadEnvironment.SV_DBSCHEMA+".REFERENCE_CODE WHERE"
				+ " REFERENCE_TYPE_ID = 9000160 AND REFERENCE_CODE = general_1) Package_name from "+LoadEnvironment.SV_DBSCHEMA+".product_instance_history"
				+ " where PRODUCT_INSTANCE_STATUS_CODE = 3 and base_product_instance_id is null and SYSDATE+1 "
				+ " BETWEEN EFFECTIVE_START_DATE AND EFFECTIVE_END_DATE and customer_node_id in"
				+ " (Select acc.CUSTOMER_NODE_ID from "+LoadEnvironment.SV_DBSCHEMA+".ACCOUNT acc where acc.account_name = '"+Account+"')";
				try{
					stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					rs=stm.executeQuery();
					if(rs.next()){
						rs.beforeFirst();
						while(rs.next()){
							Report.fnReportPass("The status of the Package  " + rs.getString("Package_name") + " in SV is " + rs.getString("PRODUCT_INSTANCE_STATUS_CODE"));
						}
					}else{

						throw new RuntimeException ("NO ROWS");
					}
				}catch(RuntimeException e){

					if(e.getMessage().equals("No ROWS")){
						Report.fnReportFail(" No Records found in OMP DB DB for " + Account);
					}
				}finally{
					ConnectionFactory.closeConnection(con);
				}
	}
	/**
	 * @param OrderID
	 * @param RejCode
	 * @param RejQueue
	 * @param BrandID
	 * @throws Exception
	 */

	// NRM verification for Rejection.
	/*public void NRM_dbVerification(String OrderID, String RejCode, String RejQueue, String BrandID) throws Exception{
		String query = null;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.NRM_DBIP+":"+LoadEnvironment.NRM_DBPORT+":"+LoadEnvironment.NRM_DBNAME,LoadEnvironment.NRM_DBUSERNAME,LoadEnvironment.NRM_DBPASSWORD);
		//con = ConnectionFactory.createConnection("jdbc:oracle:thin:@10.155.53.89:1541:NRMNPT01","rjmadmin","rjmadmin");
		query = "select r.ROUTETOTARGETQUEUE, r.BRANDID from rjmadmin.t_routingmatrix r where"
				+ " r.Deletedon is null and r.routematrixid in (select wr.routematrixid from rjmadmin.t_workitem_route wr where wr.wiid in"
				+ "(select w.wiid from rjmadmin.t_workitem w where w.orderid='"+OrderID+"' and w.rejectioncode='"+RejCode+"'))";
		try{
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){
					if((rs.getString("ROUTETOTARGETQUEUE").equals(RejQueue))&&(rs.getString("BRANDID").equals(BrandID))){
						Report.fnReportPass("Rejected order updated queue is  : " + rs.getString("ROUTETOTARGETQUEUE"));
					}else{
						Report.fnReportFail("Rejected order updated queue is  : " + rs.getString("ROUTETOTARGETQUEUE"));
					}
				}
			}else{

				throw new RuntimeException("NO ROWS");
			}
		}catch(RuntimeException e){

			if(e.getMessage().equals("NO ROWS")){

				Report.fnReportFail(" No NRM Records found in OMP DB DB for " + OrderID + "with Rejection Code"+RejCode+" with Rejection Queue "+RejQueue);
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}
	//NRM verification for WIM
	public void NRM_dbVerification_WIM(String OrderID, String Str_CLI, String Str_OrderType, String Str_WIMQUE,String Str_WIMCMDTYPE) throws Exception{
		String query = null;
		ResultSet rs = null;
		int i=0;

		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.NRM_DBIP+":"+LoadEnvironment.NRM_DBPORT+":"+LoadEnvironment.NRM_DBNAME,LoadEnvironment.NRM_DBUSERNAME,LoadEnvironment.NRM_DBPASSWORD);
		
		query = "select r.routetotargetqueue,r.provisioningcommandtype,r.subcategoryid from rjmadmin.t_routingmatrix r where r.Deletedon is null and r.routematrixid in" + 
				"(select wr.routematrixid from rjmadmin.t_workitem_route wr where wr.wiid in" + 
				"(select w.wiid from rjmadmin.t_workitem w where w.orderid='"+OrderID+"'and w.cli='"+Str_CLI+"'and w.provisioningcommandtype='"+Str_OrderType+"'))order by r.createdon desc";
System.out.println(query);

		try{
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				System.out.println("good");
				rs.beforeFirst();
				while(rs.next()){
					System.out.println(rs.getString("PROVISIONINGCOMMANDTYPE"));
					System.out.println(rs.getString("ROUTETOTARGETQUEUE"));
					if((rs.getString("ROUTETOTARGETQUEUE").equals(Str_WIMQUE))&&(rs.getString("PROVISIONINGCOMMANDTYPE").equals(Str_WIMCMDTYPE))){
						Report.fnReportPass("Updated Target queue is  : " + rs.getString("ROUTETOTARGETQUEUE"));
						i++;
					}	
				}
				if(i==0)
				{
					Report.fnReportFail("Target Queue is not updated : " + rs.getString("ROUTETOTARGETQUEUE"));
				}
			}else{
				throw new RuntimeException("NO ROWS");
			}
		}catch(RuntimeException e){

			if(e.getMessage().equals("NO ROWS")){

				Report.fnReportFail(" No NRM Records found in OMP DB DB for " + OrderID + "the workitem "+Str_WIMQUE+" with the workitem type "+Str_WIMCMDTYPE);
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}*/


	/**
	 * @param CLI
	 * @param Account
	 * @param Status
	 * @throws Exception
	 */
	public void TariffPlanStatus_SV(String CLI, String Account, String Status) throws Exception{
		String query = null;
		ResultSet rs = null;
		String StatusName = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SV_DBIP+":"+LoadEnvironment.SV_DBPORT+":"+LoadEnvironment.SV_DBNAME,LoadEnvironment.SV_DBUSERNAME,LoadEnvironment.SV_DBPASSWORD);
		query =  "SELECT PIH.EFFECTIVE_START_DATE, PIH.EFFECTIVE_END_DATE, PIH.PRODUCT_ID,PIH.GENERAL_1, CASE WHEN"
				+ " PRODUCT_ID in ('22240043'/*IPTV*/,'9000097'/*CPS*/,'9000166'/*WLR*/,'9000095'/*BB*/) THEN (SELECT ABBREVIATION FROM "
				+ ""+LoadEnvironment.SV_DBSCHEMA+".REFERENCE_CODE WHERE REFERENCE_TYPE_ID = 9000229 AND REFERENCE_CODE = PIH.GENERAL_2)"
				+ " END TARIFF_PLAN, PRODUCT_INSTANCE_STATUS_CODE FROM "+LoadEnvironment.SV_DBSCHEMA+".PRODUCT_INSTANCE_HISTORY PIH,"
				+ " "+LoadEnvironment.SV_DBSCHEMA+".CUSTOMER_NODE_HISTORY CNH WHERE PIH.BASE_PRODUCT_INSTANCE_ID IS NOT NULL"
				+ " AND CNH.CUSTOMER_NODE_ID = pih.CUSTOMER_NODE_ID AND SYSDATE + 1 BETWEEN CNH.EFFECTIVE_START_DATE"
				+ " AND CNH.EFFECTIVE_END_DATE AND SYSDATE + 1 BETWEEN PIH.EFFECTIVE_START_DATE AND PIH.EFFECTIVE_END_DATE and"
				+ " PIH.effective_start_date > sysdate-1 AND PIH.CUSTOMER_NODE_ID in (Select acc.CUSTOMER_NODE_ID"
				+ " from "+LoadEnvironment.SV_DBSCHEMA+".ACCOUNT acc where acc.account_name = '"+Account+"')And pih.general_1  like '%"+CLI+"'";
		try{
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){

					if(rs.getString("PRODUCT_INSTANCE_STATUS_CODE").equals("3")){
						StatusName = "ACTIVE";
					}else if(rs.getString("PRODUCT_INSTANCE_STATUS_CODE").equals("9")){
						StatusName = "CANCELLED";
					}else{
						StatusName = rs.getString("PRODUCT_INSTANCE_STATUS_CODE");
					}

					if(rs.getString("PRODUCT_INSTANCE_STATUS_CODE").equals(Status)){
						Report.fnReportPass("Product status in SV db: for CLI : " + CLI + " and for Product: " + rs.getString("TARIFF_PLAN") + " is : " + StatusName);
					}else{
						Report.fnReportFail("Product status in SV db: for CLI : " + CLI + " and for Product: " + rs.getString("TARIFF_PLAN") + " is : " + StatusName + " Where as the expected is " + Status);
					}
				}
			}else{

				throw new RuntimeException("NO ROWS");
			}
		}catch(RuntimeException e){

			if(e.getMessage().equals("NO ROWS")){

				Report.fnReportFail(" No  Records found for " + CLI);
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}


	/**
	 * @param AD_ADDED_PROP_SV
	 * @param AD_REMOVED_PROP_SV
	 * @param AD_ACCOUNT
	 * @throws Exception
	 */
	public void CF_SVdbVerify(String AD_ADDED_PROP_SV , String AD_REMOVED_PROP_SV, String AD_ACCOUNT) throws Exception{

		Calendar currentDate = Calendar.getInstance();
		currentDate.add(Calendar.DATE, 1);  
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
		String date = formatter.format(currentDate.getTime());


		String query = null;
		ResultSet rs = null;
		String proposition = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SV_DBIP+":"+LoadEnvironment.SV_DBPORT+":"+LoadEnvironment.SV_DBNAME,LoadEnvironment.SV_DBUSERNAME,LoadEnvironment.SV_DBPASSWORD);
		query = "SELECT PIH.EFFECTIVE_START_DATE, PIH.EFFECTIVE_END_DATE, PIH.PRODUCT_ID, CASE WHEN "
				+ " PRODUCT_ID in ('22240043'/*IPTV*/,'9000097'/*CPS*/,'9000166'/*WLR*/,'9000095'/*BB*/) THEN "
				+ " (SELECT ABBREVIATION FROM "+LoadEnvironment.SV_DBSCHEMA+".REFERENCE_CODE WHERE REFERENCE_TYPE_ID = 9000229 AND REFERENCE_CODE = PIH.GENERAL_2) "
				+ " WHEN PRODUCT_ID = 9000103 /*Bundle,*/ THEN "
				+ " (SELECT ABBREVIATION FROM "+LoadEnvironment.SV_DBSCHEMA+".REFERENCE_CODE WHERE REFERENCE_TYPE_ID = 9000154 AND REFERENCE_CODE = PIH.GENERAL_2) "
				+ " WHEN PRODUCT_ID = 9000102 /*Discount,*/ THEN "
				+ " (SELECT ABBREVIATION FROM "+LoadEnvironment.SV_DBSCHEMA+".REFERENCE_CODE WHERE REFERENCE_TYPE_ID = 9000153 AND REFERENCE_CODE = PIH.GENERAL_2) "
				+ " WHEN PRODUCT_ID = 9000101 /*Supplementary Service,*/ THEN "
				+ " (SELECT ABBREVIATION FROM "+LoadEnvironment.SV_DBSCHEMA+".REFERENCE_CODE WHERE REFERENCE_TYPE_ID = 9000152 AND REFERENCE_CODE = PIH.GENERAL_2) "
				+ " END TARIFF_PLAN, PRODUCT_INSTANCE_STATUS_CODE "
				+ " FROM "+LoadEnvironment.SV_DBSCHEMA+".PRODUCT_INSTANCE_HISTORY PIH, "+LoadEnvironment.SV_DBSCHEMA+".CUSTOMER_NODE_HISTORY CNH WHERE"
				+ " PIH.BASE_PRODUCT_INSTANCE_ID Is Not Null AND CNH.CUSTOMER_NODE_ID = pih.CUSTOMER_NODE_ID AND"
				+ " SYSDATE + 1 BETWEEN CNH.EFFECTIVE_START_DATE AND CNH.EFFECTIVE_END_DATE AND"
				+ " SYSDATE + 1 BETWEEN PIH.EFFECTIVE_START_DATE AND PIH.EFFECTIVE_END_DATE "
				+ "And PIH.effective_start_date > sysdate AND PIH.CUSTOMER_NODE_ID in "
				+ "(Select acc.CUSTOMER_NODE_ID from  "+LoadEnvironment.SV_DBSCHEMA+".ACCOUNT acc where acc.account_name = '"+AD_ACCOUNT+"')";

		try{
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();


				if(AD_REMOVED_PROP_SV.equals("NONE")){
					proposition = AD_ADDED_PROP_SV;

					for(String prop : proposition.split(",")){
						int errorFlag  = 1;
						rs.beforeFirst();
						while(rs.next()){
							String date1 = formatter.format(rs.getDate("EFFECTIVE_START_DATE"));
							if((date1.equals(date))&&(rs.getString("PRODUCT_INSTANCE_STATUS_CODE").equals("3"))
									&&(rs.getString("TARIFF_PLAN").equals(prop))){
								errorFlag = 0;
								break;
							}else{
								errorFlag = 1;
							}
						}
						if(errorFlag==0){
							Report.fnReportPass("Proposition "+prop+" was  found with status code 3 in SV DB");
						}else{

							Report.fnReportFail("Proposition "+prop+" was not found with status code 3 in SV DB");
						}
					}
				}

				if(AD_ADDED_PROP_SV.equals("NONE")){
					proposition = AD_ADDED_PROP_SV;

					for(String prop : proposition.split(",")){
						int errorFlag  = 1;
						rs.beforeFirst();
						while(rs.next()){
							String date1 = formatter.format(rs.getDate("EFFECTIVE_START_DATE"));
							if((date1.equals(date))&&(rs.getString("PRODUCT_INSTANCE_STATUS_CODE").equals("9"))
									&&(rs.getString("TARIFF_PLAN").equals(prop))){
								errorFlag = 0;
								break;
							}else{
								errorFlag = 1;
							}
						}
						if(errorFlag==0){
							Report.fnReportPass("Proposition "+prop+" was  found with status code 9 in SV DB");
						}else{
							Report.fnReportFail("Proposition "+prop+" was not found with status code 9 in SV DB");							
						}
					}
				}
			}else{
				throw new RuntimeException("NO ROWS");
			}
		}catch(RuntimeException e){

			if(e.getMessage().equals("NO ROWS")){

				Report.fnReportFail(" No  Records found for " + AD_ACCOUNT);
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}



	}

	public void CommsVerify(String Account, String COMMS) throws Exception{
		String query = null;
		ResultSet rs = null;

		Calendar currentDate = Calendar.getInstance();
		currentDate.add(Calendar.DATE, 0);  
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
		String date = formatter.format(currentDate.getTime());
		//String date1 = formatter.format(rs.getDate("EFFECTIVE_START_DATE"));
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);

		try{

			query = "Select  EVENTTYPE,CREATEDATE,CHANNEL from cblowner.communication where"
					+ " accountnumber = '"+Account+"' order by lastupdateddate desc";
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();

			if(!rs.next()){
				throw new RuntimeException("No Records");
			}
			COMMS=StringUtils.removeEnd(COMMS, "|");
			COMMS=COMMS.replace("|", ",");
			Report.fnReportPass("Communications that has to be checked are "+COMMS);
			for(String COMMS1: COMMS.split(",")){
				int errorFlag=1;
				String channel = null;
				rs.beforeFirst();
				if(rs.next()){
					rs.beforeFirst();
					while(rs.next()){
						String date1 = formatter.format(rs.getDate("CREATEDATE"));
						System.out.println("date1 "+date1);
						if((date1.equals(date))&&(rs.getString("EVENTTYPE").equals(COMMS1))){
							errorFlag=0;
							channel=rs.getString("CHANNEL");
							break;
						}else{
							errorFlag=1;

						}

					}
					if(errorFlag==0){

						Report.fnReportPass("Communication :" + COMMS1 + " using Channel " + channel + " was sent to the customer");
					}else{

						Report.fnReportFail("Communication :" + COMMS1 + " was not sent to the customer");
					}
				}
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("No Records")){

				Report.fnReportFail(" No  Comms sent Records found for " + Account);
			}

		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}

	@SuppressWarnings("null")
	public String[] getComms(String COMMS) throws Exception{
		String query = null;
		ResultSet rs = null;
		String[] Account_Number=null;
		String[] COMMS1=COMMS.split(",");
		Calendar currentDate = Calendar.getInstance();
		currentDate.add(Calendar.DATE, 0);  
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
		String date = formatter.format(currentDate.getTime());
		//String date1 = formatter.format(rs.getDate("EFFECTIVE_START_DATE"));
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);

		try{
			COMMS=StringUtils.removeEnd(COMMS, "|");
			COMMS=COMMS.replace("|", ",");
			Report.fnReportPass("Communications that has to be checked are "+COMMS);
			for(int i=0;i<COMMS1.length;i++ ){
				query = "Select  accountnumber,CREATEDATE,CHANNEL from cblowner.communication where"
						+ " EVENTTYPE = '"+COMMS1[i]+"' order by lastupdateddate desc";
				stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs=stm.executeQuery();
				if(!rs.next()){
					throw new RuntimeException("No Records");
				}
				int errorFlag=1;
				String channel = null;
				rs.beforeFirst();
				if(rs.next()){
					rs.beforeFirst();
					
					for(int j=0;rs.next()&&j<3;j++){
						String date1 = formatter.format(rs.getDate("CREATEDATE"));
						System.out.println("date1 "+date1);
						if(date1.equals(date)){
							Account_Number[i]=rs.getString("accountnumber")+",";
							errorFlag=0;
							break;
						}else{
							errorFlag=1;

						}
					}
					if(errorFlag==0){

						Report.fnReportPass("Communication :" + COMMS1 + " using Channel " + channel + " was sent to the customer");
					}else{

						Report.fnReportFail("Communication :" + COMMS1 + " was not sent to the customer");
					}
				}
				Account_Number[i]=Account_Number[i].substring(0, Account_Number[i].length()-1);
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("No Records")){

				Report.fnReportFail(" No Data found for COMM" + COMMS);
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
		return Account_Number;
	}
	
	public void SM_packageStatus_SV(String CLI, String Status) throws Exception{

		String query = null;
		ResultSet rs = null;
		String StatusName = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SV_DBIP+":"+LoadEnvironment.SV_DBPORT+":"+LoadEnvironment.SV_DBNAME,LoadEnvironment.SV_DBUSERNAME,LoadEnvironment.SV_DBPASSWORD);
		query = "select SERVICE_STATUS_CODE from "+LoadEnvironment.SV_DBSCHEMA+".service_history sh where sh.service_name like '%"+CLI+"' and "
				+ "sh.service_type_id in (select st.service_type_id from "+LoadEnvironment.SV_DBSCHEMA+".service_type"
				+ " st where st.service_type_name Like 'Fixed Line CLI%') AND SYSDATE + 1 BETWEEN sh.EFFECTIVE_START_DATE"
				+ " AND sh.EFFECTIVE_END_DATE order by sh.SERVICE_ID desc";
		try{
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){

					if(rs.getString("SERVICE_STATUS_CODE").equalsIgnoreCase("3")){
						StatusName = "ACTIVE";
					}else if(rs.getString("SERVICE_STATUS_CODE").equals("9")){
						StatusName = "CANCELLED";
					}else{
						StatusName = rs.getString("SERVICE_STATUS_CODE");
					}

					if(rs.getString("SERVICE_STATUS_CODE").equalsIgnoreCase(Status)){
						Report.fnReportPass("Package status in SV db for CLI : " + CLI + " is " + StatusName);
					}else{
						Report.fnReportFail("Package status in SV db for CLI :  " + CLI + " is " + rs.getString("SERVICE_STATUS_CODE") + " - : " + StatusName + " Where as the expected is " + Status);
					}
					break;
				}
			}else{

				throw new RuntimeException("NO ROWS");
			}
		}catch(RuntimeException e){

			if(e.getMessage().equals("NO ROWS")){

				Report.fnReportFail(" No  Records found for " + CLI);
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}

	public void SM_SKID_CPWRef_CASR(OrderDetailsBean oDB) throws Exception {
		String query = null;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SKID_DBIP+":"+LoadEnvironment.SKID_DBPORT+":"+LoadEnvironment.SKID_DBNAME,LoadEnvironment.SKID_DBUSERNAME,LoadEnvironment.SKID_DBPASSWORD);
		query ="select NK_VALUE from T_NETWORK_KEYS where NK_CASR = '"+oDB.getCASR()+"' order by NK_UPDATEDON desc";
		try{
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){
					oDB.setCPWN(rs.getString("NK_VALUE"));
					rs.afterLast();
				}
			}else{
				throw new RuntimeException("NO ROWS");
			}
		}catch(Exception e){
			if(e.getMessage().equalsIgnoreCase("NO ROWS")){
				Report.fnReportFailAndTerminateTest("SKID DB", "NO CPWN FOUND");

			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}

	}

	/*public void WaitforOrderStatus(String Str_CLI, String Str_OrderType,
			String Str_OrderStatus, boolean TerminateTest) throws Exception {

		// final String query = null;
		int localTimeOut = OrderTimeOut / 5;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"
				+ LoadEnvironment.OMP_DBIP + ":" + LoadEnvironment.OMP_DBPORT
				+ ":" + LoadEnvironment.OMP_DBNAME,
				LoadEnvironment.OMP_DBUSERNAME, LoadEnvironment.OMP_DBPASSWORD);
		
		 * String
		 * query="Select max(s.orderstatus) ORDERSTATUS from "+LoadEnvironment
		 * .OMP_DBSCHEMA+".cpworderstatus s where s.orderid in" +
		 * " (Select o.orderid from "
		 * +LoadEnvironment.OMP_DBSCHEMA+".cpworder o where o.cli='"
		 * +Str_CLI+"' and o.ordertype="+Str_OrderType+") order by" +
		 * " s.lastupdated desc nulls last";
		 
		String orderQuery=" and o.ordertype='"+Str_OrderType + "'";
		if(Str_OrderType.equals(""))
		{
			orderQuery="";
		}
		String query = "Select max(s.orderstatus) ORDERSTATUS,s.orderid from "
				+ LoadEnvironment.OMP_DBSCHEMA
				+ ".cpworderstatus s where s.orderid in "
				+ "(Select o.orderid from " + LoadEnvironment.OMP_DBSCHEMA
				+ ".cpworder o where o.cli='" + Str_CLI + "'" +orderQuery
				+ ") "
				+ "group by s.orderid order by s.orderid desc nulls last";

		System.out.println("WaitforOrderStatus Query - "+query);
		ResultSet rs;
		String Str_capturedOrderStatus = "";
		boolean StatusReached = false;
		try {
			for (int Second = 1;; Second++) {
				try {
					stm = con.prepareStatement(query,
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
					rs = stm.executeQuery();
					if (rs.next()) {
//						System.out.println("Inside IF Wait");
						rs.beforeFirst();
						while (rs.next()) {
//							System.out.println("Inside While Wait");
							Str_capturedOrderStatus = rs
									.getString("ORDERSTATUS");
							if (Str_capturedOrderStatus
									.equalsIgnoreCase(Str_OrderStatus)) {
								StatusReached = true;
								break;
							} else {
//								System.out.println("Orderstatus is:"
//										+ rs.getString("ORDERSTATUS"));
////								throw new Exception("NOT MATCHED");
							}
						}
					}
					if (StatusReached) {

						break;
					}
					// System.out.println("NO ROWS");
//					throw new Exception("NO ROWS");
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
					if (Second == localTimeOut) {
						if (e.getMessage().equals("NO ROWS")) {
							if (TerminateTest) {
								Report.fnReportFailAndTerminateTest(
										"WaitforOrderStatus",
										" No Records found in OMP DB DB for "
												+ Str_CLI);
							} else {
								Report.fnReportFail(" No Records found in OMP DB DB for "
										+ Str_CLI);
							}
						}
						if (e.getMessage().equals("NOT MATCHED")) {
							System.out.println("FAILED");
							if (TerminateTest) {
								Report.fnReportFailAndTerminateTest(
										"WaitforOrderStatus",
										" Order Status in OMP DB DB for "
												+ Str_CLI
												+ ". Does not reach expected value:"
												+ Str_OrderStatus
												+ ":Actual value is:"
												+ Str_capturedOrderStatus);
							} else {
								Report.fnReportFail(" Order Status in OMP DB DB for "
										+ Str_CLI
										+ ". Does not reach expected value:"
										+ Str_OrderStatus
										+ ":Actual value is:"
										+ Str_capturedOrderStatus);
							}
						}
						break;
					} else {
						Thread.sleep(5000);
					}
				}
				if (StatusReached) {
					Report.fnReportPass("The Order Status has reached the state "
							+ Str_capturedOrderStatus);
				}
			}
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}*/
	public void WaitforOrderStatus(String Str_CLI, String Str_OrderType,
			String Str_OrderStatus, boolean TerminateTest) throws Exception {

		// final String query = null;
		int localTimeOut = 150;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"
				+ LoadEnvironment.OMP_DBIP + ":" + LoadEnvironment.OMP_DBPORT
				+ ":" + LoadEnvironment.OMP_DBNAME,
				LoadEnvironment.OMP_DBUSERNAME, LoadEnvironment.OMP_DBPASSWORD);
		/*
		 * String
		 * query="Select max(s.orderstatus) ORDERSTATUS from "+LoadEnvironment
		 * .OMP_DBSCHEMA+".cpworderstatus s where s.orderid in" +
		 * " (Select o.orderid from "
		 * +LoadEnvironment.OMP_DBSCHEMA+".cpworder o where o.cli='"
		 * +Str_CLI+"' and o.ordertype="+Str_OrderType+") order by" +
		 * " s.lastupdated desc nulls last";
		 */
		String orderQuery=" and o.ordertype='"+Str_OrderType + "'";
		if(Str_OrderType.equals(""))
		{
			orderQuery="";
		}
		String query = "Select max(s.orderstatus) ORDERSTATUS,s.orderid from "
				+ LoadEnvironment.OMP_DBSCHEMA
				+ ".cpworderstatus s where s.orderid in "
				+ "(Select o.orderid from " + LoadEnvironment.OMP_DBSCHEMA
				+ ".cpworder o where o.cli='" + Str_CLI + "'" +orderQuery
				+ ") "
				+ "group by s.orderid order by s.orderid desc nulls last";

		System.out.println("WaitforOrderStatus Query - "+query);

		String Str_capturedOrderStatus = "";
		boolean StatusReached = false;
		try {
			for (int Second = 1;; Second++) {
				try {
					stm = con.prepareStatement(query,
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
					ResultSet rs = stm.executeQuery();
					if (rs.next()) {
						System.out.println("Inside IF Wait");
						rs.beforeFirst();
						while (rs.next()) {
							System.out.println("Inside While Wait");
							Str_capturedOrderStatus = rs
									.getString("ORDERSTATUS");
							if (Str_capturedOrderStatus
									.equalsIgnoreCase(Str_OrderStatus)) {
								System.out.println("PASSED");
								StatusReached = true;
								break;
							} else {
								System.out.println("Orderstatus is:"
										+ rs.getString("ORDERSTATUS"));
//								throw new Exception("NOT MATCHED");
							}
						}
					}
					rs.close();
					if (StatusReached) {

						break;
					}
					// System.out.println("NO ROWS");
//					throw new Exception("NO ROWS");
				} catch(SQLException s)
				{
					s.printStackTrace();
					Report.fnReportFailAndTerminateTest("SQL Exception", "SQL Exception happened");
				}
				catch (Exception e) {
					e.printStackTrace();
					if (Second == localTimeOut) {
						if (e.getMessage().equals("NO ROWS")) {
							if (TerminateTest) {
								Report.fnReportFailAndTerminateTest(
										"WaitforOrderStatus",
										" No Records found in OMP DB DB for "
												+ Str_CLI);
							} else {
								Report.fnReportFail(" No Records found in OMP DB DB for "
										+ Str_CLI);
							}
						}
						if (e.getMessage().equals("NOT MATCHED")) {
							System.out.println("FAILED");
							if (TerminateTest) {
								Report.fnReportFailAndTerminateTest(
										"WaitforOrderStatus",
										" Order Status in OMP DB DB for "
												+ Str_CLI
												+ ". Does not reach expected value:"
												+ Str_OrderStatus
												+ ":Actual value is:"
												+ Str_capturedOrderStatus);
							} else {
								Report.fnReportFail(" Order Status in OMP DB DB for "
										+ Str_CLI
										+ ". Does not reach expected value:"
										+ Str_OrderStatus
										+ ":Actual value is:"
										+ Str_capturedOrderStatus);
							}
						}
						break;
					} else {
						Thread.sleep(5000);
					}
				}
				finally{
					Thread.sleep(1000);
				}
//				if (StatusReached) {
//					Report.fnReportPass("The Order Status has reached the state "
//							+ Str_capturedOrderStatus);
//				}
			}
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}
	public void WaitforGwyCmdId(String Str_CLI,String Str_OrderType,String Str_provCommand,boolean TerminateTest) throws Exception{

		//final String query = null;
		int localTimeOut = CommandTimeOut/5;
		String query;
		ResultSet rs = null;
		boolean verify=true;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);

		query="select c.PROVCMDNAME,o.BRANDID,o.ORDERID,c.PROVCMDGWYCMDID,c.FIBRELINKEDORDERREF,c.PROVCMDINSTID,o.CASR,C.OMAPPOINTMENTID,"
				+ "c.serviceresellerid,c.resourceid,c.resourcetype,o.activityid,o.ordertype from "+LoadEnvironment.OMP_DBSCHEMA+".cpworder"
				+ " o inner join "+LoadEnvironment.OMP_DBSCHEMA+".cpwprovcmd c on o.ORDERID=c.ORDERID and o.CLI='"+Str_CLI+"'"
				+ " and o.ordertype="+Str_OrderType
				+ " and c.PROVCMDNAME like '"+Str_provCommand+"%' "
				+ " and c.PROVCMDGWYCMDERROR is null " 
				+ " and c.INVALIDCOMMAND is null "
				+ "order by c.LASTUPDATED desc";
		String Str_capturedPROVCMDGWYCMDID="";
		String Str_capturedPROVCMDINSTID="";

		try{
			PreparedStatement stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			for(int Second=1;;Second++){
				try{
					rs=stm.executeQuery();
					if(rs.next()){
						rs.beforeFirst();
						while(rs.next()){
							Str_capturedPROVCMDGWYCMDID=rs.getString("PROVCMDGWYCMDID");
							Str_capturedPROVCMDINSTID=rs.getString("PROVCMDINSTID");
							rs.afterLast();
						}

							if(verify)
							{
								if((Str_capturedPROVCMDGWYCMDID.equalsIgnoreCase(""))||(Str_capturedPROVCMDINSTID.equalsIgnoreCase("0"))
								||(Str_capturedPROVCMDGWYCMDID.equalsIgnoreCase("0"))||(Str_capturedPROVCMDINSTID.equalsIgnoreCase("0"))){
							throw new Exception("PROVCMDGWYCMDID NOT RECEIVED");
							}else{
								Report.fnReportPass("Provision Command "+Str_provCommand+" has received PROVCMDGWYCMDID : "+Str_capturedPROVCMDGWYCMDID);
								break;
							}
						}

					}else{
						throw new Exception("NO COMMAND FOUND");
					}
				}catch(Exception e){
					if(Second==localTimeOut){
						if(e.getMessage().equals("NO COMMAND FOUND")){
							if(TerminateTest){
								Report.fnReportFailAndTerminateTest("WaitforGwyCmdId", "No Records found in OMP DB DB for " + Str_CLI + " and " + Str_provCommand);
							}else{
								Report.fnReportFail("No Records found in OMP DB DB for " + Str_CLI + " and " + Str_provCommand);
							}
						}
						if(e.getMessage().equals("PROVCMDGWYCMDID NOT RECEIVED")){
							if(TerminateTest){
								Report.fnReportFailAndTerminateTest("WaitforGwyCmdId", "Provision Command "+Str_provCommand+" has not received a valid PROVCMDGWYCMDID : " 
										+ Str_capturedPROVCMDGWYCMDID + " Or a valid PROVCMDINSTID:"+Str_capturedPROVCMDINSTID);
							}else{
								Report.fnReportFail("Provision Command "+Str_provCommand+" has not received a valid PROVCMDGWYCMDID : " 
										+ Str_capturedPROVCMDGWYCMDID + " Or a valid PROVCMDINSTID:"+Str_capturedPROVCMDINSTID);
							}						
						}
						break;
					}else{
						Thread.sleep(5000);
					}
				}

			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}

	@SuppressWarnings("unused")
	public ArrayList<String> WaitforProvCmd(String Str_CLI,String Str_OrderType,boolean TerminateTest) throws Exception{

		//final String query = null;
		int localTimeOut = CommandTimeOut/5;
		String query;
		ResultSet rs = null;
		ArrayList<String> Str_capturedPROVCMDNAME = new ArrayList<String>();
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);

		query="select c.PROVCMDNAME,o.BRANDID,o.ORDERID,c.PROVCMDGWYCMDID,c.FIBRELINKEDORDERREF,c.PROVCMDINSTID,o.CASR,C.OMAPPOINTMENTID,"
				+ "c.serviceresellerid,c.resourceid,c.resourcetype,o.activityid,o.ordertype from "+LoadEnvironment.OMP_DBSCHEMA+".cpworder"
				+ " o inner join "+LoadEnvironment.OMP_DBSCHEMA+".cpwprovcmd c on o.ORDERID=c.ORDERID and o.CLI='"+Str_CLI+"'"
				+ " and o.ordertype="+Str_OrderType
				+ " and c.PROVCMDGWYCMDERROR is null " 
				+ " and c.INVALIDCOMMAND is null "
				+ "order by c.LASTUPDATED desc";

		//String Str_capturedPROVCMDNAME[]="";
		String Str_capturedPROVCMDINSTID="";
		String Str_capturedPROVCMDGWYCMDID="";

		try{
			PreparedStatement stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			//for(int Second=1;;Second++){
				//try{
					rs=stm.executeQuery();
					if(rs.next()){
						rs.beforeFirst();

						while(rs.next()){
							//(String[]) rowValues.toArray(new String[rowValues.size()]);
							System.out.println("Provisioning commands: "+rs.getString("PROVCMDNAME"));
							Str_capturedPROVCMDNAME.add(rs.getString("PROVCMDNAME"));
							Str_capturedPROVCMDGWYCMDID=rs.getString("PROVCMDGWYCMDID");
							Str_capturedPROVCMDINSTID=rs.getString("PROVCMDINSTID");

						}
						
						Iterator<String> itr=Str_capturedPROVCMDNAME.iterator(); 
						while(itr.hasNext()){ 
							System.out.println(itr.next());
							
						}

					}else{
						throw new Exception("NO COMMAND FOUND");
					}
				}
		catch(Exception e){
//					if(Second==localTimeOut){
						if(e.getMessage().equals("NO COMMAND FOUND")){
							if(TerminateTest){
								Report.fnReportFail("No Records found in OMP DB DB for " + Str_CLI + " and " + Str_capturedPROVCMDNAME);
							}
						}

		}finally{
			ConnectionFactory.closeConnection(con);
		}
		return Str_capturedPROVCMDNAME;
	}
	public void getCustomerDetails(String CLI, CustomerBean CB) throws Exception{
		String query = null;
		ResultSet rs = null;

		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);

		try{

			query = "select * from CBLOWNER.v_customer_search where CLI = '"+CLI+"'";
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();

			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){

					CB.setAccountNumber(rs.getString("ACCOUNTNUMBER"));
					CB.setCustomerNumber(rs.getString("CUSTOMERNUMBER"));
					rs.afterLast();
				}
			}else{
				throw new RuntimeException("No Records");
			}


		}catch(RuntimeException e){
			if(e.getMessage().equals("No Records")){

				Report.fnReportFail(" No  customer details Records found for " + CLI);
			}

		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}
	public void getBranchAndExchangeCode_ByCLI(String CLI, CustomerBean CB) throws Exception{

		String query = null;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);

		try{

			query = "select BRANCHCODE, EXCHANGECODE from CBLOWNER.PORTFOLIOSALESPACKAGE P where P.CLI='"+CLI+"'";
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();

			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){

					CB.setBranchCode(rs.getString("BRANCHCODE"));
					CB.setExchangeCode(rs.getString("EXCHANGECODE"));
					rs.afterLast();
				}
			}else{
				throw new RuntimeException("No Records");
			}


		}catch(RuntimeException e){
			if(e.getMessage().equals("No Records")){

				Report.fnReportFail(" No  Exchange  details Records found for " + CLI);
			}

		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}
	public void updateBranchAndExchangeCode_ByCLI(String CLI, String BRANCHCODE,String EXCHANGECODE) throws Exception{

		String query = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,"pduser","mappings");
		try{
			query = "Update CBLOWNER.PORTFOLIOSALESPACKAGE set BRANCHCODE = '"+BRANCHCODE+"',EXCHANGECODE = '"+EXCHANGECODE+"' where CLI in '"+CLI+"'";
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			stm.executeUpdate();
			con.commit();
		}catch(RuntimeException e){
			Report.fnReportFail(" No  Exchange  details Records found for " + CLI);
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}
	
	
	
	public String[] getCustomerDetails(String Str_CLI)throws Exception{
		String query = null;
		ResultSet rs = null;
		String name= null;
		String []  det=new String[6];
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
		try{
			query = "select * from CBLOWNER.v_customer_search where CLI = '"+Str_CLI+"'";

		System.out.println(query);
	
						stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
						System.out.println("After con");
						rs=stm.executeQuery();
						System.out.println("After execute");
						if(rs.next()){
							System.out.println("in rs");
									rs.beforeFirst();
									while(rs.next())
									{	
										
										det[0]=rs.getString("TITLE");
										det[1]=rs.getString("FIRSTNAME");
										det[2]=rs.getString("LASTNAME");
										det[3]=rs.getString("ACCOUNTNUMBER");
										det[4]=rs.getString("CUSTOMERNUMBER");
										det[5]=rs.getString("POSTCODE");
																		
																		
							
											if((det[0]!=null)||(det[1]!=null)||(det[2]!=null)||(det[3]!=null)||(det[4]!=null)||(det[5]!=null)||(det[6]!=null))
											{
											Report.fnReportPass("CLI of the Customer is"+det[0]+"; Title of the Customer is"+det[1]+"; First name of the customer is "+det[2]+"; Last name of the customer is"+det[3]+"; AccountNumber of the customer is "+det[4]+"; CustomerNumber of the customer is"+det[5]+"; PostCode of the customer is"+det[6]);
											}
											else
											{
													Report.fnReportFail("The details are found null"+"TITLE :"+det[0]+"FIRSTNAME :"+det[1]+"LASTNAME :"+det[2]+"ACCOUNTNUMBER :"+det[3]+"CUSTOMERNUMBER :"+det[4]+"POSTCODE :"+det[5]);       
											}									
									}
						}
						else
						{
							throw new RuntimeException ("NO ROWS");
						}
				}
				catch(RuntimeException e){

					if(e.getMessage().equals("No ROWS")){
						Report.fnReportFail(" No Records found in DB for " + Str_CLI);
					}
				}finally{
					ConnectionFactory.closeConnection(con);
				}
		
		
		return det;
	
	}

	public void update_INSTALLATION_METHOD_CONTROL(INSTALLATION_METHOD_CONTROL METHOD_CONTROL, boolean INSTALLATION_METHOD_CONTROL_Availability,boolean Pre_Registration ) throws Exception{
		String query = null;
		String Str_INSTALLATION_METHOD_CONTROL_Availability = "ENABLED";
		String Pre_Registration_availability = "ENABLED";

		if(INSTALLATION_METHOD_CONTROL_Availability){
			Str_INSTALLATION_METHOD_CONTROL_Availability = "ENABLED";
		}else{
			Str_INSTALLATION_METHOD_CONTROL_Availability = "DISABLED";
		}
		if(Pre_Registration){
			Pre_Registration_availability = "ENABLED";
		}else{
			Pre_Registration_availability = "DISABLED";
		}

		switch(METHOD_CONTROL){
		case Managed_Install:
			query = "update INSTALLATION_METHOD_CONTROL set INSTAL_METHOD_STATUS_CODE='"+Str_INSTALLATION_METHOD_CONTROL_Availability+"'"
					+ ", PRE_REGISTRATION_STATUS_CODE='" + Pre_Registration_availability + "'" 
					+ " where INSTALLATION_METHOD_ID in "
					+ " (select INSTALLATION_METHOD_ID from INSTALLATION_METHOD where INSTALLATION_METHOD_DESC='Managed Install')";
			break;
		case No_STB:
			query = "update INSTALLATION_METHOD_CONTROL set INSTAL_METHOD_STATUS_CODE='"+Str_INSTALLATION_METHOD_CONTROL_Availability+"'"
					+ ", PRE_REGISTRATION_STATUS_CODE='" + Pre_Registration_availability + "'" 
					+ " where INSTALLATION_METHOD_ID in "
					+ " (select INSTALLATION_METHOD_ID from INSTALLATION_METHOD where INSTALLATION_METHOD_DESC='No STB')";
			break;
		case CTD:
			query = "update INSTALLATION_METHOD_CONTROL set INSTAL_METHOD_STATUS_CODE='"+Str_INSTALLATION_METHOD_CONTROL_Availability+"'"
					+ ", PRE_REGISTRATION_STATUS_CODE='" + Pre_Registration_availability + "'" 
					+ " where INSTALLATION_METHOD_ID in "
					+ " (select INSTALLATION_METHOD_ID from INSTALLATION_METHOD where INSTALLATION_METHOD_DESC='Self Install')";
			break;
		default:
			break;

		}
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.EVG_DBIP+":"+LoadEnvironment.EVG_DBPORT+":"+LoadEnvironment.EVG_DBNAME,LoadEnvironment.EVG_DBUSERNAME,LoadEnvironment.EVG_DBPASSWORD);
		try{

			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			stm.executeUpdate();
			con.commit();
			Report.fnReportPass(METHOD_CONTROL.name() + " is set to " + Str_INSTALLATION_METHOD_CONTROL_Availability + " Pre-REg status set to "+Pre_Registration_availability);
		}catch(RuntimeException e){
			Report.fnReportFail("update_INSTALLATION_METHOD_CONTROL FAILED");
		}finally{
			ConnectionFactory.closeConnection(con);
		}

	}	

	public void update_MANAGED_INSTALL_CONTROL(MANAGED_INSTALL_CONTROL METHOD_CONTROL, boolean RESOURCE_CHECK_STATUS_CODE_Availability,boolean Pre_Registration) throws Exception{
		String query = null;
		String Pre_Registration_availability = "ENABLED";
		String Str_RESOURCE_CHECK_STATUS_CODE_Availability = "AVAILABLE";

		if(RESOURCE_CHECK_STATUS_CODE_Availability){
			Str_RESOURCE_CHECK_STATUS_CODE_Availability = "AVAILABLE";
		}else{
			Str_RESOURCE_CHECK_STATUS_CODE_Availability = "UNAVAILABLE";
		}
		if(Pre_Registration){
			Pre_Registration_availability = "ENABLED";
		}else{
			Pre_Registration_availability = "DISABLED";
		}

		switch(METHOD_CONTROL){
		case Change_Package:
			query = "update MANAGED_INSTALL_CONTROL set RESOURCE_CHECK_STATUS_CODE='"+Str_RESOURCE_CHECK_STATUS_CODE_Availability+"',PRE_REGISTRATION_STATUS_CODE='"+Pre_Registration_availability+"' "
					+ "where REGION_CODE = '"+REGION+"'"
					+ " and "
					+ "VISIT_TYPE_ACTIVITY_ID in "
					+ "(select VISIT_TYPE_ACTIVITY_ID from VISIT_TYPE_ACTIVITY where VISIT_TYPE_ACTIVITY_DESC like '%Change Package%')";
			break;
		case Change_Package_Retentions:
			query = "update MANAGED_INSTALL_CONTROL set RESOURCE_CHECK_STATUS_CODE='"+Str_RESOURCE_CHECK_STATUS_CODE_Availability+"',PRE_REGISTRATION_STATUS_CODE='"+Pre_Registration_availability+"' "
					+ "where REGION_CODE = '"+REGION+"' and "
					+ "VISIT_TYPE_ACTIVITY_ID in "
					+ "(select VISIT_TYPE_ACTIVITY_ID from VISIT_TYPE_ACTIVITY where VISIT_TYPE_ACTIVITY_DESC like '%Change Package%')";
			break;
		case Homemover:
			query = "update MANAGED_INSTALL_CONTROL set RESOURCE_CHECK_STATUS_CODE='"+Str_RESOURCE_CHECK_STATUS_CODE_Availability+"',PRE_REGISTRATION_STATUS_CODE='"+Pre_Registration_availability+"' "
					+ "where REGION_CODE = '"+REGION+"' and "
					+ "VISIT_TYPE_ACTIVITY_ID in "
					+ "(select VISIT_TYPE_ACTIVITY_ID from VISIT_TYPE_ACTIVITY where VISIT_TYPE_ACTIVITY_DESC like '%Homemover%')";
			break;
		case Initial_Sale:
			query = "update MANAGED_INSTALL_CONTROL set RESOURCE_CHECK_STATUS_CODE='"+Str_RESOURCE_CHECK_STATUS_CODE_Availability+"',PRE_REGISTRATION_STATUS_CODE='"+Pre_Registration_availability+"' "
					+ "where REGION_CODE = '"+REGION+"' and "
					+ "VISIT_TYPE_ACTIVITY_ID in "
					+ "(select VISIT_TYPE_ACTIVITY_ID from VISIT_TYPE_ACTIVITY where VISIT_TYPE_ACTIVITY_DESC like '%Initial Sale%')";
			break;
		case My_Account_Change_Package:
			query = "update MANAGED_INSTALL_CONTROL set RESOURCE_CHECK_STATUS_CODE='"+Str_RESOURCE_CHECK_STATUS_CODE_Availability+"',PRE_REGISTRATION_STATUS_CODE='"+Pre_Registration_availability+"' "
					+ "where REGION_CODE = '"+REGION+"' and "
					+ "VISIT_TYPE_ACTIVITY_ID in "
					+ "(select VISIT_TYPE_ACTIVITY_ID from VISIT_TYPE_ACTIVITY where VISIT_TYPE_ACTIVITY_DESC like '%Change Package%')";
			break;
		case My_Account_Manage_Appointment:
			query = "update MANAGED_INSTALL_CONTROL set RESOURCE_CHECK_STATUS_CODE='"+Str_RESOURCE_CHECK_STATUS_CODE_Availability+"',PRE_REGISTRATION_STATUS_CODE='"+Pre_Registration_availability+"' "
					+ "where REGION_CODE = '"+REGION+"' and "
					+ "VISIT_TYPE_ACTIVITY_ID in "
					+ "(select VISIT_TYPE_ACTIVITY_ID from VISIT_TYPE_ACTIVITY where VISIT_TYPE_ACTIVITY_DESC like '%My Account Manage Appointment%')";
			break;
		case New_Line_Sale:
			query = "update MANAGED_INSTALL_CONTROL set RESOURCE_CHECK_STATUS_CODE='"+Str_RESOURCE_CHECK_STATUS_CODE_Availability+"',PRE_REGISTRATION_STATUS_CODE='"+Pre_Registration_availability+"' "
					+ "where REGION_CODE = '"+REGION+"' and "
					+ "VISIT_TYPE_ACTIVITY_ID in "
					+ "(select VISIT_TYPE_ACTIVITY_ID from VISIT_TYPE_ACTIVITY where VISIT_TYPE_ACTIVITY_DESC like '%New Line Sale%')";
			break;
		default:
			break;


		}
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.EVG_DBIP+":"+LoadEnvironment.EVG_DBPORT+":"+LoadEnvironment.EVG_DBNAME,LoadEnvironment.EVG_DBUSERNAME,LoadEnvironment.EVG_DBPASSWORD);
		try{

			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			stm.executeUpdate();
			con.commit();
			Report.fnReportPass(METHOD_CONTROL.name() + " is set to " + Str_RESOURCE_CHECK_STATUS_CODE_Availability + " Pre-REg status set to "+Pre_Registration_availability);
		}catch(RuntimeException e){
			Report.fnReportFail("update_INSTALLATION_METHOD_CONTROL FAILED");
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}



	public void Update_CPE_Availability(String CPE, boolean CPE_Availability) throws Exception{
		String query = null;
		String Str_CPE_Availability = "ENABLED";


		if(CPE_Availability){
			Str_CPE_Availability = "AVAILABLE";
		}else{
			Str_CPE_Availability = "UNAVAILABLE";
		}
		query = "Update CPE_PRODUCT_CONTROL set CPE_RESOURCE_CHECK_STATUS_CODE = '"+Str_CPE_Availability+"'  "
				+ "where CPE_PRODUCT_ID  in (select CPE_PRODUCT_ID from CPE_PRODUCT where CPE_PRODUCT_DESC like '%"+CPE+"%')";

		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.EVG_DBIP+":"+LoadEnvironment.EVG_DBPORT+":"+LoadEnvironment.EVG_DBNAME,LoadEnvironment.EVG_DBUSERNAME,LoadEnvironment.EVG_DBPASSWORD);
		try{

			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			stm.executeUpdate();
			con.commit();
			Report.fnReportPass(CPE + " is set to " + Str_CPE_Availability);
		}catch(RuntimeException e){
			Report.fnReportFail("Update CPE_Availability FAILED");
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}

	public void Update_Installation_Address(String Str_CLI) throws Exception{

		String query = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,"pduser","mappings");
		try{
			query = "UPDATE ccsowner.postaladdress"
					+ " SET POSTALCODETEXT = '"+POSTCODE+"'"
					+ " WHERE contactinfoid IN"
					+ " (SELECT INSTALLATIONADDRESSID FROM cblowner.portfoliosalespackage WHERE cli='"+Str_CLI+"')";
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			stm.executeUpdate();
			con.commit();
		}catch(RuntimeException e){
			Report.fnReportFail("update_Installation_Address Failed for CLI " + Str_CLI);
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}

	public void Update_Correspondance_Address(String Str_CLI,String Str_PostCode) throws Exception{

		String query = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,"pduser","mappings");
		try{
			query = "update CCSOWNER.POSTALADDRESS POST "
					+ " set POST.POSTALCODETEXT = '"+Str_PostCode+"'"
					+ " where POST.CONTACTINFOID in (select contactinfoid from ccsowner.PARTYCONTACTINFO where partyid in"
					+ " (select partyid from CBLOWNER.V_CUSTOMER_SEARCH where cli='"+Str_CLI+"') and contacttypecode='ALTERNATE1')" ;
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			stm.executeUpdate();
			con.commit();
		}catch(RuntimeException e){
			Report.fnReportFail("Update_Correspondance_Address Failed for CLI " + Str_CLI);
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}

	public void WaitforRejectionRetry(String Str_CLI, String Str_provCommand,OrderDetailsBean oDB,boolean TerminateTest) throws Exception{
		//final String query = null;
		int localTimeOut = CommandTimeOut/5;
		String query;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);

		query="select c.PROVCMDNAME,o.BRANDID,o.ORDERID,c.PROVCMDGWYCMDID,c.FIBRELINKEDORDERREF,c.PROVCMDINSTID,o.CASR,C.OMAPPOINTMENTID,"
				+ "c.serviceresellerid,c.resourceid,c.resourcetype,o.activityid,o.ordertype from "+LoadEnvironment.OMP_DBSCHEMA+".cpworder"
				+ " o inner join "+LoadEnvironment.OMP_DBSCHEMA+".cpwprovcmd c on o.ORDERID=c.ORDERID and o.CLI='"+Str_CLI+"'"
				+ " and o.ordertype="+oDB.getOrdertype()
				+ " and c.PROVCMDNAME like '"+Str_provCommand+"%' "
				+ " and c.PROVCMDGWYCMDERROR is null " 
				+ " and c.INVALIDCOMMAND is null "
				+ " and TO_CHAR(GWCMD_SUBDATE,'DD-MM-YYYY')=TO_CHAR(SYSDATE,'DD-MM-YYYY') "
				+ " and o.ORDERID="+oDB.getORDERID() +""
				+ " order by c.LASTUPDATED desc";

		String Str_capturedPROVCMDGWYCMDID="";
		String Str_capturedPROVCMDINSTID="";

		try{
			PreparedStatement stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			for(int Second=1;;Second++){
				try{
					rs=stm.executeQuery();
					if(rs.next()){
						rs.beforeFirst();

						while(rs.next()){
							Str_capturedPROVCMDGWYCMDID=rs.getString("PROVCMDGWYCMDID");
							Str_capturedPROVCMDINSTID=rs.getString("PROVCMDINSTID");
							rs.afterLast();
						}

						if((Str_capturedPROVCMDGWYCMDID.equalsIgnoreCase(""))||(Str_capturedPROVCMDINSTID.equalsIgnoreCase("0"))
								||(Str_capturedPROVCMDGWYCMDID.equalsIgnoreCase("0"))||(Str_capturedPROVCMDINSTID.equalsIgnoreCase("0"))){
							throw new Exception("PROVCMDGWYCMDID NOT RECEIVED");
						}else{
							Report.fnReportPass("WaitforRejectionRetry says : Provision Command "+Str_provCommand+" has received PROVCMDGWYCMDID : "+Str_capturedPROVCMDGWYCMDID);
							break;
						}

					}else{
						throw new Exception("NO COMMAND FOUND");
					}
				}catch(Exception e){
					if(Second==localTimeOut){
						if(e.getMessage().equals("NO COMMAND FOUND")){
							if(TerminateTest){
								Report.fnReportFailAndTerminateTest("WaitforGwyCmdId", "WaitforRejectionRetry says :No Records found in OMP DB DB for " + Str_CLI + " and " + Str_provCommand);
							}else{
								Report.fnReportFail("WaitforRejectionRetry says : No Records found in OMP DB DB for " + Str_CLI + " and " + Str_provCommand);
							}
						}
						if(e.getMessage().equals("PROVCMDGWYCMDID NOT RECEIVED")){
							if(TerminateTest){
								Report.fnReportFailAndTerminateTest("WaitforGwyCmdId", "Provision Command "+Str_provCommand+" has not received a valid PROVCMDGWYCMDID : " 
										+ Str_capturedPROVCMDGWYCMDID + " Or a valid PROVCMDINSTID:"+Str_capturedPROVCMDINSTID);
							}else{
								Report.fnReportFail("Provision Command "+Str_provCommand+" has not received a valid PROVCMDGWYCMDID : " 
										+ Str_capturedPROVCMDGWYCMDID + " Or a valid PROVCMDINSTID:"+Str_capturedPROVCMDINSTID);
							}						
						}
						break;
					}else{
						Thread.sleep(5000);
					}
				}

			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}
	public void Verify_ManageAccessibilityDetails_CRM(String Str_CLI, ACCESSIBILITYREQUESTED AAR,ACCESSIBILITYAUTHENTICATED AAU) throws Exception{

		String query = null;
		ResultSet rs = null;
		String Str_ACCESSIBILITYAUTHENTICATED = "";
		String Str_ACCESSIBILITYREQUESTED = "";
		switch(AAU){
		case Zero:
			Str_ACCESSIBILITYAUTHENTICATED="0";
			break;
		case one:
			Str_ACCESSIBILITYAUTHENTICATED="1";
			break;
		default:
			break;
		}
		switch(AAR){
		case Zero:
			Str_ACCESSIBILITYREQUESTED="0";
			break;
		case one:
			Str_ACCESSIBILITYREQUESTED="1";
			break;
		default:
			break;

		}

		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
		try{
			query = "select ACCESSIBILITYAUTHENTICATED,ACCESSIBILITYREQUESTED from cblowner.cpwcustomer where partyroleid in "
					+ "(select PARTYROLEID from CBLOWNER.V_CUSTOMER_SEARCH where cli='"+Str_CLI+"')";
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){
					if(rs.getString("ACCESSIBILITYAUTHENTICATED").equalsIgnoreCase(Str_ACCESSIBILITYAUTHENTICATED) &&
							rs.getString("ACCESSIBILITYREQUESTED").equalsIgnoreCase(Str_ACCESSIBILITYREQUESTED)){
						Report.fnReportPass("ACCESSIBILITY details updated in CRM DB, the values in DB are ACCESSIBILITYREQUESTED:"+ rs.getString("ACCESSIBILITYREQUESTED")
								+ " ACCESSIBILITYAUTHENTICATED:" + rs.getString("ACCESSIBILITYAUTHENTICATED") );
					}else{
						Report.fnReportFail("ACCESSIBILITY details not updated in CRM DB, the values in DB are ACCESSIBILITYREQUESTED:"+ rs.getString("ACCESSIBILITYREQUESTED")
								+ " ACCESSIBILITYAUTHENTICATED:" + rs.getString("ACCESSIBILITYAUTHENTICATED") );
					}
					rs.afterLast();
				}
			}else{
				throw new RuntimeException("No Records");
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("No Records")){
				Report.fnReportFail(" No  ACCESSIBILITY details Records found for " + Str_CLI+" in CRM");
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}
	public void Verify_ManageAccessibilityDetails_SV(String Str_Account, General10 General_10) throws Exception{

		String query = null;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SV_DBIP+":"+LoadEnvironment.SV_DBPORT+":"+LoadEnvironment.SV_DBNAME,LoadEnvironment.SV_DBUSERNAME,LoadEnvironment.SV_DBPASSWORD);
		try{
			query = "select CUSTOMER_NODE_ID,GENERAL_10 from "+LoadEnvironment.SV_DBSCHEMA+".customer_node_history where customer_node_id "
					+ "in (Select acc.CUSTOMER_NODE_ID from "+LoadEnvironment.SV_DBSCHEMA+".ACCOUNT acc where acc.account_name = '"+Str_Account+"') and "
					+ "SYSDATE between EFFECTIVE_START_DATE and EFFECTIVE_END_DATE";
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){

					if(rs.getString("GENERAL_10").equalsIgnoreCase(General_10.name())){
						Report.fnReportPass("ACCESSIBILITY detail updated in SV DB, Value in DB is:" + rs.getString("GENERAL_10"));
					}else{
						Report.fnReportFail("ACCESSIBILITY detail not updated in SV DB, Value in DB is:" + rs.getString("GENERAL_10"));
					}
					rs.afterLast();
				}
			}else{
				throw new RuntimeException("No Records");
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("No Records")){
				Report.fnReportFail(" No  ACCESSIBILITY details Records found for " + Str_Account+" in SV");
			}
		}catch(Exception e){
			Report.fnReportFail(" Unable to verify  ACCESSIBILITY details Records found for " + Str_Account+" in SV");
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}
	public void Update_ManageAccessibilityDetails_CRM(String Str_CLI,ACCESSIBILITYREQUESTED AAR, ACCESSIBILITYAUTHENTICATED AAU) throws Exception{

		String Str_ACCESSIBILITYAUTHENTICATED = "";
		String Str_ACCESSIBILITYREQUESTED = "";
		switch(AAU){
		case Zero:
			Str_ACCESSIBILITYAUTHENTICATED="0";
			break;
		case one:
			Str_ACCESSIBILITYAUTHENTICATED="1";
			break;
		default:
			break;
		}
		switch(AAR){
		case Zero:
			Str_ACCESSIBILITYREQUESTED="0";
			break;
		case one:
			Str_ACCESSIBILITYREQUESTED="1";
			break;
		default:
			break;

		}
		String query = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,"pduser","mappings");
		try{
			query = "update  cblowner.cpwcustomer set ACCESSIBILITYAUTHENTICATED ='"+Str_ACCESSIBILITYAUTHENTICATED+"',"
					+ " ACCESSIBILITYREQUESTED='"+Str_ACCESSIBILITYREQUESTED+"'"
					+ " where partyroleid in "
					+ " (select PARTYROLEID from CBLOWNER.V_CUSTOMER_SEARCH where cli='"+Str_CLI+"')";
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			stm.executeUpdate();
			con.commit();
			Report.fnReportPass("ACCESSIBILITY details Records updated for " + Str_CLI+" in CRM");
		}catch(Exception e){
			Report.fnReportFail("ACCESSIBILITY details Records Not updated for " + Str_CLI+" in CRM");
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}
	public void Update_ManageAccessibilityDetails_SV(String Str_Account, General10 General_10) throws Exception{
		String Str_General_10 = "";
		switch(General_10){
		case No:
			Str_General_10="No";
			break;
		case Yes:
			Str_General_10 = "Yes";
			break;
		default:
			break;
		}
		String query = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SV_DBIP+":"+LoadEnvironment.SV_DBPORT+":"+LoadEnvironment.SV_DBNAME,LoadEnvironment.SV_DBUSERNAME,LoadEnvironment.SV_DBPASSWORD);
		try{
			query = "update "+LoadEnvironment.SV_DBSCHEMA+".customer_node_history set GENERAL_10 ='"+Str_General_10+"' where customer_node_id "
					+ "in (Select acc.CUSTOMER_NODE_ID from "+LoadEnvironment.SV_DBSCHEMA+".ACCOUNT acc where acc.account_name = '"+Str_Account+"') and "
					+ "SYSDATE between EFFECTIVE_START_DATE and EFFECTIVE_END_DATE";
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			stm.executeUpdate();
			con.commit();
			Report.fnReportPass("ACCESSIBILITY details Records updated for " + Str_Account+" in SV");
		}catch(Exception e){
			Report.fnReportFail("ACCESSIBILITY details Records Not updated for " + Str_Account+" in SV");
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}

	public String getCRMCommsRefByCLI(CommsLevel CL,String strCustomerNumber,String strCLI,
			String strAccount,String strEventType,String strChannel,String strBrandId) throws Exception{
		String query = null;
		ResultSet rs = null;
		String strCommsRefId = "";
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"
				+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
		try{
			switch(CL){
			case AccountLevel:
				query="select COMMUNICATIONDISPATCHREF from cblowner.communication_dispatch  where lastupdateddate= " + 
						"(Select max(lastupdateddate) from cblowner.communication_dispatch where communicationid in " + 
						"(select ID from cblowner.communication where accountnumber= '"+ strAccount + "'and channel= '"+ strChannel +"' "
						+ " and eventtype= '"+ strEventType +"'and brandid= '"+ strBrandId +"'))";
				break;
			case OrderLevel:
				query="select COMMUNICATIONDISPATCHREF from cblowner.communication_dispatch  where lastupdateddate= " + 
						"(Select max(lastupdateddate) from cblowner.communication_dispatch where communicationid in " + 
						"(select ID from cblowner.communication where accountnumber= '" + strAccount + "' and cli= '" + strCLI + "' " + 
						"and channel= '" + strChannel + "' and eventtype= '" + strEventType + "'and brandid= '" + strBrandId + "'))" ;
				break;
			case Any:
				query="select COMMUNICATIONDISPATCHREF from cblowner.communication_dispatch  where lastupdateddate= " + 
						"(Select max(lastupdateddate) from cblowner.communication_dispatch where communicationid in " + 
						"(select ID from cblowner.communication where customernumber='" + strCustomerNumber + "' and channel= '" + strChannel + 
						"' and eventtype= '" + strEventType + "'and brandid= '" + strBrandId + "'))";

				break;
			}
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){
					strCommsRefId=rs.getString("COMMUNICATIONDISPATCHREF");
					rs.afterLast();
				}
			}else{
				throw new RuntimeException("No Records");
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("No Records")){
				Report.fnReportFail(" No  Communication Records found for " + strCLI+" in CRM DB");
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
		return strCommsRefId;
	}

	public void SM_SKID_CPWReference(String CLI, String AMID,String ByRefCPWNRefNo) throws Exception {
		String query = null;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SKID_DBIP+":"+LoadEnvironment.SKID_DBPORT+":"+LoadEnvironment.SKID_DBNAME,LoadEnvironment.SKID_DBUSERNAME,LoadEnvironment.SKID_DBPASSWORD);
		Connection 	con1 = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
		query = "select pdt.* from CBLOWNER.portfolioitemproduct pdt where pdt.Name = 'Broadband' and "
				+ "pdt.portfoliosalespackageid in (select id from cblowner.portfoliosalespackage where cli = '"+CLI+"')";
		try{
			stm=con1.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){
					if(rs.getString("NEWRESOURCEID") != null){
						Report.fnReportPass("CASR value for CLI "+CLI+" is "+rs.getString("NEWRESOURCEID"));
						try{
							String query1 = "select NK_VALUE from T_NETWORK_KEYS where NK_CASR = '"+rs.getString("NEWRESOURCEID")+"' and NK_KY_ID = '"+AMID+"' order by NK_UPDATEDON desc";
							System.out.println(query1);
							ResultSet rs1=null;
							stm=con1.prepareStatement(query1,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
							rs1=stm.executeQuery();
							if(rs1.next()){
								rs1.beforeFirst();
								while(rs1.next()){
									ByRefCPWNRefNo = rs1.getString("NK_VALUE");
									Report.fnReportPass("CPWN number Set is "+ByRefCPWNRefNo);
									rs1.afterLast();
								}
							}else{
								throw new RuntimeException("NO ROWS");
							}
						}catch(Exception e){
							if(e.getMessage().equalsIgnoreCase("NO ROWS")){
								Report.fnReportFailAndTerminateTest("SKID DB", "NP CPWN FOUND");
							}
						}finally{
							ConnectionFactory.closeConnection(con1);
						}
					}else{
						throw new RuntimeException("NO CPWN");	
					}
					rs.afterLast();
				}
			}else{
				throw new RuntimeException("NO ROWS");
			}
		}catch(Exception e){
			if(e.getMessage().equalsIgnoreCase("NO ROWS")){
				Report.fnReportFailAndTerminateTest("CRM DB", "NO PDt found");
			}else if(e.getMessage().equalsIgnoreCase("NO CPWN")){
				Report.fnReportFailAndTerminateTest("CRM DB", " NEWRESOURCEID was null");
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}

	}
	public void SM_Insert_T_SK(String CASR) throws Exception {
		String query = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SKID_DBIP+":"+LoadEnvironment.SKID_DBPORT+":"+LoadEnvironment.SKID_DBNAME,LoadEnvironment.SKID_DBUSERNAME,LoadEnvironment.SKID_DBPASSWORD);
		try{
			query = "INSERT INTO SKROOT.T_SK (SK_CASR, SK_CREATEDON) VALUES ('"+CASR+"', TO_TIMESTAMP('"+Reusables.getdateFormat("dd-MMM-yy", 0)+" 23.52.43.747720000', 'DD-MON-RR HH24.MI.SS.FF'))";
			System.out.println(query);
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			stm.executeUpdate();
			con.commit();
		}catch(Exception e){
			Report.fnReportFailAndTerminateTest("SKID DB", e.getMessage());

		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}
	public void SM_Insert_T_NETWORK_KEYS(String CASR,T_NETWORK_KEYS_NK_ID NK_ID) throws Exception {
		String query = null;
		String Str_NK_ID="";
		switch(NK_ID){
		case DNA:
			Str_NK_ID="7";
			break;
		case IPS:
			Str_NK_ID="1";
			break;
		case IPTV:
			Str_NK_ID="8";
			break;
		case MPF:
			Str_NK_ID="2";
			break;
		case NGA:
			Str_NK_ID="9";
			break;
		case OBC:
			Str_NK_ID="5";
			break;
		case SMPF:
			Str_NK_ID="6";
			break;
		case SMPF_T3:
			Str_NK_ID="10";
			break;
		case WLR2:
			Str_NK_ID="3";
			break;
		case WLR3:
			Str_NK_ID="4";
			break;
		case LLU:
			Str_NK_ID="2";
		default:
			break;

		}
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SKID_DBIP+":"+LoadEnvironment.SKID_DBPORT+":"+LoadEnvironment.SKID_DBNAME,LoadEnvironment.SKID_DBUSERNAME,LoadEnvironment.SKID_DBPASSWORD);
		try{
			query = "INSERT INTO SKROOT.T_NETWORK_KEYS (NK_CASR, NK_KY_ID, NK_VALUE, NK_CREATEDON) VALUES "
					+ "('"+CASR+"', '"+Str_NK_ID+"', '"+NameGenerator.randomCPWNRef(7)+"', TO_TIMESTAMP('"+Reusables.getdateFormat("dd-MMM-yy", 0)+" 01.07.24.890832000', 'DD-MON-RR HH24.MI.SS.FF'))";
			System.out.println(query);
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			stm.executeUpdate();
			con.commit();
		}catch(Exception e){
			Report.fnReportFailAndTerminateTest("SKID DB", e.getMessage());

		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}
	public void Validate_SKID_CPWRef_CASR(String CLI,String ProvCommand,T_NETWORK_KEYS_NK_ID AccessMethod) throws Exception {
		String query = null;
		ResultSet rs = null;

		OrderDetailsBean oDB = new OrderDetailsBean();
		getOrderDetails(CLI, ProvCommand, oDB);
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SKID_DBIP+":"+LoadEnvironment.SKID_DBPORT+":"+LoadEnvironment.SKID_DBNAME,LoadEnvironment.SKID_DBUSERNAME,LoadEnvironment.SKID_DBPASSWORD);
		query ="select NK_VALUE from T_NETWORK_KEYS where NK_CASR = '"+oDB.getCASR()+"' order by NK_UPDATEDON desc";
		try{
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				System.out.println("CPWN Reference Number is found");	
			}else{
				if(oDB.getCASR()!=null){
					System.out.println("CPWN Reference Number is not found -- thus adding a new one");
					SM_Insert_T_SK(oDB.getCASR());
					SM_Insert_T_NETWORK_KEYS(oDB.getCASR(),AccessMethod);
				}else{
					Report.fnReportFailAndTerminateTest("SKID DB", "NP CPWN FOUND");
				}
			}
		}catch(Exception e){
			if(e.getMessage().equalsIgnoreCase("NO ROWS")){
				Report.fnReportFailAndTerminateTest("SKID DB", "NP CPWN FOUND");
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}

	}	

	public void Verify_ManageComplaints_CRM(String strComplaintRef, String strComplaintStatus,String strBrand) throws Exception{

		String query = null;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
		try{
			query = "select COMPLAINTNUMBER,STATUS,BRAND_ID from cblowner.complaint where complaintnumber='"+ strComplaintRef +"' order by lastupdateddate desc";

			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){
					if(rs.getString("STATUS").equalsIgnoreCase(strComplaintStatus) || rs.getString("BRAND_ID").equalsIgnoreCase(strBrand)){
						Report.fnReportPass("Complaint detail updated in CRM DB, Status in DB is:" + rs.getString("STATUS"));
					}else{
						Report.fnReportFail("Complaint detail not updated in CRM DB, Status in DB is:" + rs.getString("STATUS") + " ,Expected status is : "+ strComplaintStatus );
					}
					rs.afterLast();
				}
			}else{
				throw new RuntimeException("No Records");
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("No Records")){
				Report.fnReportFail(" No  Complaint details Records found for " + strComplaintRef +"in CRM DB");
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}

	public void Update_Billing_Address(String Str_Account,String Str_PostCode) throws Exception{

		String query = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SV_DBIP+":"+LoadEnvironment.SV_DBPORT+":"+LoadEnvironment.SV_DBNAME,LoadEnvironment.SV_DBUSERNAME,LoadEnvironment.SV_DBPASSWORD);
		try{
			query = "UPDATE "+LoadEnvironment.SV_DBSCHEMA+".address_history ah "
					+ "	set POST_CODE='"+Str_PostCode+"' "
					+ " where ah.address_id in "
					+ "	(select cnh.postal_address_id from "+LoadEnvironment.SV_DBSCHEMA+".customer_node_history cnh where cnh.customer_node_id in "
					+ " (select a.customer_node_id from "+LoadEnvironment.SV_DBSCHEMA+".account a where a.account_name='"+Str_Account+"')) "
					+ " and SYSDATE between EFFECTIVE_START_DATE and EFFECTIVE_END_DATE" ;
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			stm.executeUpdate();
			con.commit();
		}catch(RuntimeException e){
			Report.fnReportFail("Update_Billing_Address Failed for Account " + Str_Account);
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}

	public void Verify_Billing_Address_SV(String str_Account,String str_LINE1,String str_LINE2,String str_CITY,String str_PostCode) throws Exception{

		String query = null;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SV_DBIP+":"+LoadEnvironment.SV_DBPORT+":"+LoadEnvironment.SV_DBNAME,LoadEnvironment.SV_DBUSERNAME,LoadEnvironment.SV_DBPASSWORD);
		try{
			query = "select LINE_1,DECODE(LINE_2, NULL,'-1',LINE_2) as LINE_2,CITY,POST_CODE from "+LoadEnvironment.SV_DBSCHEMA+".address_history ah "
					+ " where ah.address_id in "
					+ "	(select cnh.postal_address_id from "+LoadEnvironment.SV_DBSCHEMA+".customer_node_history cnh where cnh.customer_node_id in "
					+ " (select a.customer_node_id from "+LoadEnvironment.SV_DBSCHEMA+".account a where a.account_name='"+str_Account+"')) "
					+ " and SYSDATE between EFFECTIVE_START_DATE and EFFECTIVE_END_DATE" ;
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){
					if(rs.getString("LINE_1").equalsIgnoreCase(str_LINE1) && rs.getString("LINE_2").equalsIgnoreCase(str_LINE2)
							&& rs.getString("CITY").equalsIgnoreCase(str_CITY) && rs.getString("POST_CODE").equalsIgnoreCase(str_PostCode)){
						Report.fnReportPass("Billing Address updated in SV DB, Address in DB is:" + rs.getString("LINE_1") + ";" +
								rs.getString("LINE_2") +";" + rs.getString("CITY") +";" + rs.getString("POST_CODE"));
					}else{
						Report.fnReportFail("Billing Address not updated in SV DB, Address in DB is:" + rs.getString("LINE_1") + ";" +
								rs.getString("LINE_2") +";" + rs.getString("CITY") +";" + rs.getString("POST_CODE"));
					}
					rs.afterLast();
				}
			}else{
				throw new RuntimeException("No Records");
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("No Records")){
				Report.fnReportFail(" No  Billing address details found for " + str_Account +"in SV DB");
			}else{
				Report.fnReportFail("Verify_Billing_Address_SV Failed for Account " + str_Account);
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}


	public void Verify_Installation_Address(String Str_CLI, String Str_AddressLine1, String Str_AddressLine2, String Str_PostalTown, String Str_County, String Str_PostCode) throws Exception{
		String query = null;
		ResultSet rs = null;

		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
		try{
			query = "select * from ccsowner.postaladdress where contactinfoid in (select INSTALLATIONADDRESSID from cblowner.portfoliosalespackage where cli='"+Str_CLI+"')";
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){
					System.out.println(rs.getString("ADDRESS1TEXT"));
					System.out.println(rs.getString("ADDRESS2TEXT"));
					System.out.println(rs.getString("CITYNAME"));
//					System.out.println(rs.getString("County"));
					System.out.println(rs.getString("POSTALCODETEXT"));
					

					if(rs.getString("ADDRESS1TEXT").equalsIgnoreCase(Str_AddressLine1) && rs.getString("ADDRESS2TEXT").equalsIgnoreCase(Str_AddressLine2) && rs.getString("CITYNAME").equalsIgnoreCase(Str_PostalTown) && /*rs.getString("County").equalsIgnoreCase(Str_County) &&*/ rs.getString("POSTALCODETEXT").equalsIgnoreCase(Str_PostCode)){
						System.out.println("UYBRFut gUFVGUITrb YR");
Report.fnReportPass("Installation address updated in CRM DB");
					}else{
						Report.fnReportFail("Installation address not updated in CRM DB");
					}
					rs.afterLast();
				}
			}else{
				throw new RuntimeException("No Records");
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("No Records")){
				Report.fnReportFail(" No Records found in CRM DB");
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}

	}

	public void Verify_Billing_Preferences_SV(String Str_Account,String Str_InvoiceFormat, String Str_PaperBill, String Str_eBill) throws Exception{

		String query = null;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SV_DBIP+":"+LoadEnvironment.SV_DBPORT+":"+LoadEnvironment.SV_DBNAME,LoadEnvironment.SV_DBUSERNAME,LoadEnvironment.SV_DBPASSWORD);
		try{
			query = "select index1_value,"
					+ "(SELECT ABBREVIATION FROM " +LoadEnvironment.SV_DBSCHEMA+ ".REFERENCE_CODE WHERE REFERENCE_TYPE_ID = 9000175 AND REFERENCE_CODE = RESULT1_VALUE) INV_MEDIA, "
					+ "(SELECT ABBREVIATION FROM " +LoadEnvironment.SV_DBSCHEMA+ ".REFERENCE_CODE WHERE REFERENCE_TYPE_ID = 9000176 AND REFERENCE_CODE = RESULT2_VALUE) INV_LAYOUT, "
					+ "(SELECT ABBREVIATION FROM " +LoadEnvironment.SV_DBSCHEMA+ ".REFERENCE_CODE WHERE REFERENCE_TYPE_ID = 9000177 AND REFERENCE_CODE = RESULT3_VALUE) INV_ITEMISATION, "
					+ "(SELECT ABBREVIATION FROM " +LoadEnvironment.SV_DBSCHEMA+ ".REFERENCE_CODE WHERE REFERENCE_TYPE_ID = 9000178 AND REFERENCE_CODE = RESULT4_VALUE) INV_MASKING "
					+ "from " +LoadEnvironment.SV_DBSCHEMA+ ".customer_node_da_array where derived_attribute_id = 9000098 "
					+ "AND SYSDATE BETWEEN EFFECTIVE_START_DATE AND EFFECTIVE_END_dATE "
					+ "AND CUSTOMER_NODE_ID in (select acc.CUSTOMER_NODE_ID from " +LoadEnvironment.SV_DBSCHEMA+ ".account acc where acc.account_name='"+Str_Account+"')";

			System.out.println("query executed");

			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){
					if(rs.getString("INV_LAYOUT").equalsIgnoreCase(Str_InvoiceFormat)){
						Report.fnReportPass("The invoice format is "+Str_InvoiceFormat+";");
					}else{
						Report.fnReportFail("The invoice format is "+Str_InvoiceFormat+";");
					}
					if(rs.getString("INV_MEDIA").equalsIgnoreCase("Paper")){
						Report.fnReportPass("The bill method is Paper");
					}else if(rs.getString("INV_MEDIA").equalsIgnoreCase("Email")){
						Report.fnReportPass("The bill method is Email");
					}else{
						Report.fnReportFail("The bill method is not Email or Paper");
					}
					rs.afterLast();
				}
			}else{
				throw new RuntimeException("No Records");
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("No Records")){
				Report.fnReportFail(" No Billing preferences details found for " + Str_Account +"in SV DB");
			}else{
				Report.fnReportFail("Verify_Billing_Preferences_SV Failed for Account " + Str_Account);
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}
	
	
	
	public void Confirm_Fraud_Status(String Account) throws Exception{
		String query = null;
		ResultSet rs = null;
		@SuppressWarnings("unused")
		String  invSuppressReason=null;
		String effStartDate = null;
		String effEndDate = null;
		
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SV_DBIP+":"+LoadEnvironment.SV_DBPORT+":"+LoadEnvironment.SV_DBNAME,LoadEnvironment.SV_DBUSERNAME,LoadEnvironment.SV_DBPASSWORD);
		try{	
		query="select customer_node_id,effective_start_date,effective_end_date,GENERAL_3 as INV_SUPPRESS_REASON,GENERAL_6 as ConfirmedFraud_Flag" + 
                " from " + LoadEnvironment.SV_DBSCHEMA + ".customer_node_history "
                + " where CUSTOMER_NODE_ID in (Select acc.CUSTOMER_NODE_ID from " + LoadEnvironment.SV_DBSCHEMA  + ".ACCOUNT acc where acc.account_name = '"+Account+"')" + 
                " and sysdate between effective_start_date and effective_end_date";
				
		System.out.println(query);
	
						stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
						rs=stm.executeQuery();
						if(rs.next()){
							System.out.println("in rs");
									rs.beforeFirst();
									while(rs.next())
									{	
										System.out.println("Got the data to next ");
											if(rs.getString("ConfirmedFraud_Flag").equalsIgnoreCase("Yes"))
											{
												invSuppressReason = rs.getString("INV_SUPPRESS_REASON");
											            effStartDate = rs.getString("EFFECTIVE_START_DATE");
											            effEndDate = rs.getString("EFFECTIVE_END_DATE");
											            System.out.println("Before report making ");
											Report.fnReportPass("Confirmed flag is set to YES in DB for account" +Account+"; Effective start Date"+effStartDate+"; Effective End Date"+effEndDate);
											}
											else
											{
													Report.fnReportFail("Confirmed Flag is NOT set to YES in SV db for account : " + Account + "; Actual : Confirmed flag in SV db : "
															+ rs.getString("ConfirmedFraud_Flag") +
														"; Effective Start date : " + effStartDate + "; Effective End date : " + effEndDate);
											        
											}
										
										
									}
						}
						else
						{
	
							throw new RuntimeException ("NO ROWS");
						}
				}
				catch(RuntimeException e){

					if(e.getMessage().equals("No ROWS")){
						Report.fnReportFail(" No Records found in OMP DB DB for " + Account);
					}
				}finally{
					ConnectionFactory.closeConnection(con);
				}
	}
	

	public String FName_LNameByCLI(String Str_CLI) throws Exception{
		String query = null;
		ResultSet rs = null;
		String name= null;
		String Fname = null;
		String Lname = null;
		
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
		try{	
		query="select FIRSTNAME, LASTNAME  from CBLOWNER.v_customer_search cs where cs.cli in " + 
                    "(select cli from CBLOWNER.portfoliosalespackage psp where psp.cli = '"+Str_CLI+"') order by cs.pspactivationdate desc";
		System.out.println(query);
	
						stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
						System.out.println("After con");
						rs=stm.executeQuery();
						System.out.println("After execute");
						if(rs.next()){
							System.out.println("in rs");
									rs.beforeFirst();
									while(rs.next())
									{	
										Fname=rs.getString("FIRSTNAME");
										Lname=rs.getString("LASTNAME");
										name=Fname+" "+Lname;
							
											if((Fname!=null)&&(Lname!=null))
											{
									
											Report.fnReportPass("The first name of the customer is "+Fname+"; Last name of the customer is"+Lname);
											}
											else
											{
													Report.fnReportFail("the first name and last name are found null");
											        
											}									
									}
						}
						else
						{
							throw new RuntimeException ("NO ROWS");
						}
				}
				catch(RuntimeException e){

					if(e.getMessage().equals("No ROWS")){
						Report.fnReportFail(" No Records found in DB for " + Str_CLI);
					}
				}finally{
					ConnectionFactory.closeConnection(con);
				}
		return name;
	}
	
	public void CommsVerifyNeg(String Account, String COMMS) throws Exception{
		String query = null;
		ResultSet rs = null;

		Calendar currentDate = Calendar.getInstance();
		currentDate.add(Calendar.DATE, 0);  
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
		String date = formatter.format(currentDate.getTime());
		//String date1 = formatter.format(rs.getDate("EFFECTIVE_START_DATE"));
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);

		try{

			query = "Select  EVENTTYPE,CREATEDATE,CHANNEL from cblowner.communication where"
					+ " accountnumber = '"+Account+"' order by lastupdateddate desc";
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();

			if(!rs.next()){
				throw new RuntimeException("No Records");
			}

			for(String COMMS1: COMMS.split(",")){
				int errorFlag=1;
				String channel = null;
				rs.beforeFirst();
				if(rs.next()){
					rs.beforeFirst();
					while(rs.next()){
						String date1 = formatter.format(rs.getDate("CREATEDATE"));
						if((date1.equals(date))&&(rs.getString("EVENTTYPE").equals(COMMS1))){
							errorFlag=0;
							channel=rs.getString("CHANNEL");
							break;
						}else{
							errorFlag=1;

						}

					}
					if(errorFlag==1){

						Report.fnReportPass("Communication :" + COMMS1 + " using Channel " + channel + " was not sent to the customer");
					}else{

						Report.fnReportFail("Communication :" + COMMS1 + " was  sent to the customer");
					}
				}
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("No Records")){

				Report.fnReportFail(" No  Comms sent Records found for " + Account);
			}

		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}

	/*public void NRM_DbVerfication_WIM_CheckStatus(String OrderId,String WIMCMDTYPE,String Checkstatus) throws Exception{
	
	
		
		String query = null;
		String query1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		int n=5;
		int n1=32;
		
	con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.NRM_DBIP+":"+LoadEnvironment.NRM_DBPORT+":"+LoadEnvironment.NRM_DBNAME,LoadEnvironment.NRM_DBUSERNAME,LoadEnvironment.NRM_DBPASSWORD);

		query = "select w.WIID,w.PROVISIONINGCOMMANDTYPE from rjmadmin.t_workitem w where w.orderid='"+OrderId+"'order by w.createdon desc";


		try{
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			  if(rs.next()){
				rs.beforeFirst();
				while(rs.next()){
					
						if((!(rs.getString("WIID").equals(null)))&&(rs.getString("PROVISIONINGCOMMANDTYPE").equals(WIMCMDTYPE))){
							String WIID=rs.getString("WIID");
							Report.fnReportPass("Work item triggered is  :  " + rs.getString("WIID"));
						}else{
							Report.fnReportFail("Work item is not triggered   : " + rs.getString("WIID"));
						}
				}
			  }
				else{
					
				throw new RuntimeException("No records for WIID");
					}
						
				String WIID=rs.getString("WIID");
				if(Checkstatus.equalsIgnoreCase("yes"))
				{
					query1 = "select s.SID,s.SSID from rjmadmin.t_workitem_status s where s.wiid='"+WIID+"' order by s.createdon desc";
				
					stm=con.prepareStatement(query1,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					 rs1=stm.executeQuery();
					 if(rs1.next())
					 {
							rs1.beforeFirst();
							while(rs1.next())
							{
								if(rs1.getString("SID").equals(n) &&rs1.getString("SSID").equals(n1))
								{
									Report.fnReportPass("NRM DB Work item state is autoclosed. State : 5 Substate :32");
								}
								else
								{
									Report.fnReportFail("Work item is not closed.State : "+rs1.getString("SID")+" Substate: "+rs1.getString("SSID"));
								}
							}
					 }
					 else
					 {
						 throw new RuntimeException("No Records in NRM DB");
					 }
							
				}
				
		}
					catch(RuntimeException e)
					{
						if(e.getMessage().equals("No Records in NRM DB")){
			
							Report.fnReportFail(" No Record in NRM DB");
						}
		
				     }
				
				
				finally{
			ConnectionFactory.closeConnection(con);
				}
			
	}			
				
	public void GetInstallationStatus(String CLI) throws Exception{
		String query = null;
		ResultSet rs = null;

		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.NRM_DBIP+":"+LoadEnvironment.NRM_DBPORT+":"+LoadEnvironment.NRM_DBNAME,LoadEnvironment.NRM_DBUSERNAME,LoadEnvironment.NRM_DBPASSWORD);
		
			query = "select ENA.ATTRIBUTE_NAME,ENA.ATTRIBUTE_VALUE  from CBLOWNER.EXTERNAL_NETWORK_ATTRIBUTE  ENA where ENA.PORTFOLIOITEMPRODUCTID in" + 
             "(select PFP.ID from CBLOWNER.PORTFOLIOITEMPRODUCT PFP where PFP.PORTFOLIOSALESPACKAGEID in" + 
             "(select PSP.ID from CBLOWNER.PORTFOLIOSALESPACKAGE PSP where PSP.cli = '"+CLI+"' ) and PFP.NAME = 'Broadband')and ENA.ATTRIBUTE_NAME = 'fibreInstallationMethodId' order by ENA.LASTUPDATEDDATE asc";
			System.out.println(query);
			try{
				stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs=stm.executeQuery();

				System.out.println("query executed");
				if(rs.next()){
							rs.beforeFirst();
							while(rs.next())
							{	
								System.out.println("inside while");
								   String Attribute_Name =rs.getString("ATTRIBUTE_NAME");
								   String Attribute_value =rs.getString("ATTRIBUTE_VALUE");
									
									if(Attribute_Name.equalsIgnoreCase("fibreInstallationMethodId"))
									{
										if(Attribute_value.equalsIgnoreCase("Engineer Install"))
										{
											Report.fnReportPass("Fibre Installation Method is Engineer Install.");
										}
										else if(Attribute_value.equalsIgnoreCase("Self Install"))
										{
											Report.fnReportPass("Fibre Installation Method is Self Install.");
										}
										
									}
									else
									{
										Report.fnReportFail("No installation method is odtained from db");
									}
							}
							
				}
				else
				{
					throw new RuntimeException();
				}
			}catch(RuntimeException e){
				Report.fnReportFail("No Records found in the db");
			}
			finally{
				ConnectionFactory.closeConnection(con);
			}			
			
	}
	
	public void GetInstallationStatus(String CLI,INSTALLATION_METHOD_CONTROL IMC ) throws Exception{
		String query = null;
		ResultSet rs = null;

		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.NRM_DBIP+":"+LoadEnvironment.NRM_DBPORT+":"+LoadEnvironment.NRM_DBNAME,LoadEnvironment.NRM_DBUSERNAME,LoadEnvironment.NRM_DBPASSWORD);
		
			query = "select ENA.ATTRIBUTE_NAME,ENA.ATTRIBUTE_VALUE  from CBLOWNER.EXTERNAL_NETWORK_ATTRIBUTE  ENA where ENA.PORTFOLIOITEMPRODUCTID in" + 
             "(select PFP.ID from CBLOWNER.PORTFOLIOITEMPRODUCT PFP where PFP.PORTFOLIOSALESPACKAGEID in" + 
             "(select PSP.ID from CBLOWNER.PORTFOLIOSALESPACKAGE PSP where PSP.cli = '"+CLI+"' ) and PFP.NAME = 'Broadband')and ENA.ATTRIBUTE_NAME = 'fibreInstallationMethodId' order by ENA.LASTUPDATEDDATE asc";
			System.out.println(query);
			try{
				stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs=stm.executeQuery();

				System.out.println("query executed");
				if(rs.next()){
							rs.beforeFirst();
							while(rs.next())
							{	
								System.out.println("inside while");
								   String Attribute_Name =rs.getString("ATTRIBUTE_NAME");
								   String Attribute_value =rs.getString("ATTRIBUTE_VALUE");
									
									if(Attribute_Name.equalsIgnoreCase("fibreInstallationMethodId"))
									{
										if(Attribute_value.equalsIgnoreCase(IMC.name()))
										{
											Report.fnReportPass("Fibre Installation Method is "+IMC.name()+".");
										}
										else if(Attribute_value.equalsIgnoreCase("Self Install"))
										{
											Report.fnReportFail("Fibre Installation Method is "+Attribute_value+".");
										}
										
									}
									else
									{
										Report.fnReportFail("No installation method is odtained from db");
									}
							}
							
				}
				else
				{
					throw new RuntimeException();
				}
			}catch(RuntimeException e){
				Report.fnReportFail("No Records found in the db");
			}
			finally{
				ConnectionFactory.closeConnection(con);
			}			
			
	}
	
	public String NRM_WIM_DB_Verification_WIID(String OrderID,String WIMCMDTYPE) throws Exception{
		String query = null;
		ResultSet rs = null;
		String WIID=null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.NRM_DBIP+":"+LoadEnvironment.NRM_DBPORT+":"+LoadEnvironment.NRM_DBNAME,LoadEnvironment.NRM_DBUSERNAME,LoadEnvironment.NRM_DBPASSWORD);
		query ="select w.WIID,w.PROVISIONINGCOMMANDTYPE from rjmadmin.t_workitem w where w.orderid='"+OrderID+"'order by w.createdon desc";
		try{
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();

			System.out.println("query executed");
			if(rs.next()){
						rs.beforeFirst();
						while(rs.next())
						{	
							if(!rs.getString("WIID").equalsIgnoreCase(null))
							{
								WIID=rs.getString("WIID");
								Report.fnReportPass("NRM DB Workitem triggered is: "+WIID);
								
							}
							else
							{
								Report.fnReportFail("Work Item is not Updated");
							}
							
						
						}
			}
			else
			{
				throw new RuntimeException();
			}
		}catch(RuntimeException e)
		{
			Report.fnReportFail("No Records found in the db");
		}
		finally{
			ConnectionFactory.closeConnection(con);
		}
		return WIID;
	}
	
	public String NRM_WIM_MERGETOWIID_DB_Verification_WIID(String CLI,String OrderID) throws Exception{
		String query = null;
		ResultSet rs = null;
		String WIMLIST=null;
		String mergetowiid=null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
		query ="select w.mergedtowiid from rjmadmin.t_workitem w where  w.orderid='"+OrderID+"'and w.cli='"+CLI+"' order by createdon desc";
		try{
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();

			System.out.println("query executed");
			if(rs.next()){
						rs.beforeFirst();
						while(rs.next())
						{	
							if(!rs.getString("MERGEDTOWIID").equalsIgnoreCase(null))
							{
								mergetowiid=rs.getString("MERGEDTOWIID");
								Report.fnReportPass("NRM DB Updated Target queue: "+mergetowiid);
								
							}
							else
							{
								Report.fnReportFail("Rejected order updated queue is : "+rs.getString("ROUTETOTARGETQUEUE"));
							}
							
						}
						
					}
		}
		catch(RuntimeException e)
		{
			Report.fnReportFail("No Records found in the db");
		}
		finally{
			ConnectionFactory.closeConnection(con);
		}
		return mergetowiid;
		
	}*/
	
	public String[] SM_AccountAndCustomerNumber_ByCLI(String CLI) throws Exception{
		
		String query = null;
		ResultSet rs = null;
	
		String [] Cust_det = new String[2];

		
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
		try{	
		query="select customernumber, accountnumber from CBLOWNER.v_customer_search cs where cs.cli in"+
		"(select cli from CBLOWNER.portfoliosalespackage psp where psp.cli = '"+CLI+"') order by cs.pspactivationdate desc";
		
		System.out.println(query);
	
						stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
						System.out.println("After con");
						rs=stm.executeQuery();
						System.out.println("After execute");
						if(rs.next()){
							System.out.println("in rs");
									rs.beforeFirst();
									while(rs.next())
									{	
										Cust_det[0]=rs.getString("ACCOUNTNUMBER");
										Cust_det[1]=rs.getString("CUSTOMERNUMBER");
									
							
											if((Cust_det[0]!=null)&&(Cust_det[1]!=null))
											{
									
											Report.fnReportPass("Account Number of the customer is "+Cust_det[0]+"; Customer Number of the customer is"+Cust_det[1]);
											}
											else
											{
													Report.fnReportFail("the Account Number and Customer Number are found null");
											        
											}									
									}
						}
						else
						{
							throw new RuntimeException ("NO ROWS");
						}
				}
				catch(RuntimeException e){

					if(e.getMessage().equals("No ROWS")){
						Report.fnReportFail(" No Records found in DB for " + CLI);
					}
				}finally{
					ConnectionFactory.closeConnection(con);
				}
		return Cust_det;
	}
	
	public String get_VerifyCRD(String CLI) throws Exception{
		String query = null;
		ResultSet rs = null;	
		String CRD=null;		
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);
		try{	
		query="select p.CUSTOMERREQUIREDDATE from"+LoadEnvironment.OMP_DBSCHEMA+".CPWORDER p where p.CLI = '"+CLI+"' order by lastupdated desc";		
		System.out.println(query);
	
						stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
						System.out.println("After con");
						rs=stm.executeQuery();
						System.out.println("After execute");
						if(rs.next()){
							System.out.println("in rs");
									rs.beforeFirst();
									while(rs.next())
									{	
										if(rs.getString("CUSTOMERREQUIREDDATE").equalsIgnoreCase(null)||rs.getString("CUSTOMERREQUIREDDATE").equalsIgnoreCase("0"))
										{
											Report.fnReportFail("The customer Requred ddate is null or 0 in DB");

										}
											CRD=rs.getString("CUSTOMERREQUIREDDATE");
																	
									}
						}
						else
						{
							throw new RuntimeException ("NO ROWS");
						}
				}
				catch(RuntimeException e){

					if(e.getMessage().equals("No ROWS")){
						Report.fnReportFail(" No Records found for the provisioning command in DB");
					}
				}finally{
					ConnectionFactory.closeConnection(con);
				}
		return CRD;
	
	}
	
	public String get_gatewayID(String CLI,String ProvCMD) throws Exception{
		String query = null;
		ResultSet rs = null;	
		String gatewayID=null;		
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);
		try{	
	    query = "select c.PROVCMDNAME,o.BRANDID,o.ORDERID,c.PROVCMDGWYCMDID,c.PROVCMDINSTID,o.CASR,"+
				"C.OMAPPOINTMENTID,c.serviceresellerid,c.resourceid," +
                "c.resourcetype,o.activityid,o.ordertype from "+LoadEnvironment.OMP_DBSCHEMA+
                ".cpworder o inner join "+LoadEnvironment.OMP_DBSCHEMA+".cpwprovcmd c " + 
                "on o.ORDERID=c.ORDERID and o.CLI='"+CLI+"' and"+" c.PROVCMDNAME like '"+ProvCMD+"' order by c.LASTUPDATED desc";		
		
		System.out.println(query);
	
						stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
						System.out.println("After con");
						rs=stm.executeQuery();
						System.out.println("After execute");
						if(rs.next()){
							System.out.println("in rs");
									rs.beforeFirst();
									while(rs.next())
									{	
										if(rs.getString("PROVCMDGWYCMDID").equalsIgnoreCase(null)||rs.getString("PROVCMDGWYCMDID").equalsIgnoreCase("0"))
										{
											Report.fnReportFail("The PROVCMDGWYCMDID is null or 0 in DB");

										}
										gatewayID=rs.getString("PROVCMDGWYCMDID");
										Report.fnReportPass("The PROVCMDGWYCMDID for "+ProvCMD+" is:"+gatewayID);							
									}
						}
						else
						{
							throw new RuntimeException ("NO ROWS");
						}
				}
				catch(RuntimeException e){

					if(e.getMessage().equals("No ROWS")){
						Report.fnReportFail(" No Records found for the provisioning command in DB");
					}
				}finally{
					ConnectionFactory.closeConnection(con);
				}
		return gatewayID;
	
	}
	public void getOrderDetails_selfinstall(String CLI, String provCommand, OrderDetailsBean ODB) throws Exception {
		String query;
		ResultSet rs = null;
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);

		query="select c.PROVCMDNAME,o.BRANDID,o.ORDERID,c.PROVCMDGWYCMDID,c.FIBRELINKEDORDERREF,c.PROVCMDINSTID,o.CASR, "
				+ " c.serviceresellerid,c.resourceid,c.resourcetype,o.activityid,o.ordertype from "+LoadEnvironment.OMP_DBSCHEMA+".cpworder "
				+ " o inner join "+LoadEnvironment.OMP_DBSCHEMA+".cpwprovcmd c on o.ORDERID=c.ORDERID and o.CLI='"+CLI+"'"
				+ " and c.PROVCMDNAME like '"+provCommand+"%' "
				+ " order by c.LASTUPDATED desc";
		/* deleted 07- Feb - 2014
		 * 	+ " and c.PROVCMDGWYCMDERROR is null " 
		 *	+ " and c.INVALIDCOMMAND is null "
		 */
		System.out.println(query);
		try{
			PreparedStatement stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

			rs=stm.executeQuery();

			if(rs.next()){
				rs.beforeFirst();

				while(rs.next()){
					ODB.setPROVCMDNAME(rs.getString("PROVCMDNAME"));
					ODB.setBRANDID(rs.getString("BRANDID"));
					ODB.setORDERID(rs.getString("ORDERID"));
					ODB.setPROVCMDGWYCMDID(rs.getString("PROVCMDGWYCMDID"));
					ODB.setFIBRELINKEDORDERREF(rs.getString("FIBRELINKEDORDERREF"));
					ODB.setPROVCMDINSTID(rs.getString("PROVCMDINSTID"));
					ODB.setCASR(rs.getString("CASR"));
					ODB.setServiceresellerid(rs.getString("serviceresellerid"));
					ODB.setResourceid(rs.getString("resourceid"));
					ODB.setResourcetype(rs.getString("resourcetype"));
					ODB.setActivityid(rs.getString("activityid"));
					ODB.setOrdertype(rs.getString("ordertype"));
					rs.afterLast();

				}

				if((ODB.getPROVCMDGWYCMDID().equalsIgnoreCase(""))||(ODB.getPROVCMDINSTID().equalsIgnoreCase("0"))
						||(ODB.getPROVCMDGWYCMDID().equalsIgnoreCase("0"))||(ODB.getPROVCMDINSTID().equalsIgnoreCase("0"))){

					throw new RuntimeException(" gwycmdid or cmdinstid not found in the OMP db or '0'");
				}else{
					//	fnReportPass("Provision "+provCommand+" found with gatewayID  "+ODB.getPROVCMDGWYCMDID());
				}


			}else{

				throw new RuntimeException("NO COMMAND FOUND");
			}
		}catch(RuntimeException e){
			if(e.getMessage().equals("NO COMMAND FOUND")){

				Report.fnReportFailAndTerminateTest("GetCommand details","NO COMMAND FOUND" );
			}else{
				Report.fnReportFailAndTerminateTest("GetCommand details"," gwycmdid or cmdinstid not found in the OMP db or '0'");
			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}

	public void Verify_MarketingPreferences(String Str_CLI, int Email, int SMS,
			int Phone, int Post) throws Exception {
		String query = "";
		ResultSet rs = null;
		try {
			con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"
					+ LoadEnvironment.CRM_DBIP + ":"
					+ LoadEnvironment.CRM_DBPORT + ":"
					+ LoadEnvironment.CRM_DBNAME,
					LoadEnvironment.CRM_DBUSERNAME,
					LoadEnvironment.CRM_DBPASSWORD);
			query = "select MARKETINGPREFEMAILFLAG,MARKETINGPREFSMSFLAG,MARKETINGPREFLETTERFLAG,MARKETINGPREFVOICEFLAG from CBLOWNER.CPWCUSTOMER where"
					+ " partyroleid in (select prl.id from ccsowner.partyrole prl,ccsowner.partyaccountrole par,cblowner.portfoliosalespackage psp"
					+ " where prl.partyid = par.partyid and par.accountid = psp.accountid and psp.cli='"
					+ Str_CLI + "')";

			stm = con.prepareStatement(query,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = stm.executeQuery();
			if (rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					if ((rs.getInt("MARKETINGPREFEMAILFLAG") == Email)
							&& (rs.getInt("MARKETINGPREFSMSFLAG") == SMS)
							&& (rs.getInt("MARKETINGPREFLETTERFLAG") == Post)
							&& (rs.getInt("MARKETINGPREFVOICEFLAG") == Phone)) {
						Report.fnReportPass("Marketing Preferences updated Correctly in CRM DB");
					} else {
						Report.fnReportPass("Marketing Preferences updated Incorrectly in CRM DB");
					}
					rs.afterLast();
				}
			} else {
				throw new RuntimeException("No Records");
			}
		} catch (RuntimeException e) {
			if (e.getMessage().equals("No Records")) {

				Report.fnReportFail(" No  Marketing Prference related record available for "
						+ Str_CLI);
			}

		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}	
}



