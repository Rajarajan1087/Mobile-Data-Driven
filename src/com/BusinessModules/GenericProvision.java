package com.BusinessModules;

import java.util.ArrayList;
import java.util.Iterator;

import com.Engine.Reporter;
import com.SharedModules.CPEGProvision;
import com.SharedModules.Constants;
import com.SharedModules.DbUtilities;
import com.SharedModules.EVGProvision;
import com.SharedModules.IPTVProvision;
import com.SharedModules.LLUProvision;
import com.SharedModules.NGAProvision;
import com.SharedModules.OrderDetailsBean;
import com.SharedModules.SMPFProvision;
import com.SharedModules.TVCPEGProvision;
import com.Stubs.StubFilePlacing;
import com.Stubs.StubFilePlacing.StubType;

public class GenericProvision implements Constants{

	public Reporter Report;
	public GenericProvision(Reporter report) {
		Report = report;
	}

	public void ChangePackage_GenericProvision(ArrayList<String> ProvCmd,String Str_CLI,String SourcePackage,String FibreInstallationMethod) throws Exception{

		Iterator<String> PRV=ProvCmd.iterator();
		DbUtilities DbU = new DbUtilities(Report);
		LLUProvision LLU = new LLUProvision(Report);
		NGAProvision NGA = new NGAProvision(Report);
		EVGProvision EVG = new EVGProvision(Report);
		CPEGProvision CPEG = new CPEGProvision(Report);
		IPTVProvision IPTV = new IPTVProvision(Report);
		SMPFProvision SMPF =  new SMPFProvision(Report);
		StubFilePlacing SF = new StubFilePlacing(Report);
		TVCPEGProvision TVCPEG = new TVCPEGProvision(Report);
		OrderDetailsBean ODB = new OrderDetailsBean();
		String CPWNRef = null;

		while(PRV.hasNext()){
			String PRVCMD = PRV.next();
			switch(PRVCMD){
			case "LLUMigrate":
				DbU.WaitforGwyCmdId(Str_CLI, "1", "LLUMigrate", true);
				LLU.LLUMigrate_SIM2(Constants.Completed, Str_CLI, false, false);
				break;
			case "NGAProvide":
				if (SourcePackage.equalsIgnoreCase("MPF") && FibreInstallationMethod.equalsIgnoreCase("engineerinstall")){ 
					DbU.WaitforGwyCmdId(Str_CLI, "1", "NGAProvide", true);
					NGA.NGAProvide(Constants.Completed, Str_CLI, "");
				}
				else if (SourcePackage.equalsIgnoreCase("MPF") && FibreInstallationMethod.equalsIgnoreCase("selfinstall")){ 
					DbU.WaitforGwyCmdId(Str_CLI, "1", "NGAProvide", true);
					NGA.NGAProvide_selfinstall(Constants.Completed, Str_CLI, "");
				}
				else{
					DbU.WaitforGwyCmdId(Str_CLI, "1", "NGAProvide", true);
					NGA.NGAProvide_SIM2(Constants.Completed, Str_CLI, "");
				}
				break;
			case "EVGConfirmAppointment":
				DbU.WaitforGwyCmdId(Str_CLI, "1", "EVGConfirmAppointment", true);
				EVG.EVGProvide(Constants.Completed, Str_CLI);
				break;
			case "SubmitCPEOrder" :
				DbU.WaitforGwyCmdId(Str_CLI, "1", "SubmitCPEOrder", true);
				CPEG.CPEGProvide(Constants.Completed, Str_CLI);
				break;
			case "IPTVProvide":
				DbU.WaitforGwyCmdId(Str_CLI, "1", "IPTVProvide", true);
				IPTV.IPTVProvide(Constants.Completed, Str_CLI);
				break;
			case "LLUModify":
				DbU.WaitforGwyCmdId(Str_CLI, "1", "LLUModify", true);
				LLU.LLUModify(Constants.Completed, Str_CLI, "");
				break;
			case "SMPFProvide":
				DbU.WaitforGwyCmdId(Str_CLI, "1", "SMPFProvide", true);
				SMPF.SMPFProvide(Constants.Completed, Str_CLI, "");
				break;
			case "SMPFMigrate":
				DbU.WaitforGwyCmdId(Str_CLI, "1", "SMPFMigrate", true);
				SMPF.SMPFMigrate(Constants.Completed, Str_CLI, "");
				break;
			case "IPTVModify":
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
			case "SubmitTVCPEOrder":
				DbU.WaitforGwyCmdId(Str_CLI, "1", "SubmitTVCPEOrder", true);
				TVCPEG.TVCPEGProvide(Constants.Completed, Str_CLI);
				break;
			default:
				break;
			}
		}
	}
	public void SaveRetain_GenericProvision(ArrayList<String> ProvCmd,String Str_CLI,String SourcePackage) throws Exception{

		Iterator<String> PRV=ProvCmd.iterator();
		DbUtilities DbU = new DbUtilities(Report);
		LLUProvision LLU = new LLUProvision(Report);
		NGAProvision NGA = new NGAProvision(Report);
		EVGProvision EVG = new EVGProvision(Report);
		CPEGProvision CPEG = new CPEGProvision(Report);
		IPTVProvision IPTV = new IPTVProvision(Report);
		SMPFProvision SMPF =  new SMPFProvision(Report);
		StubFilePlacing SF = new StubFilePlacing(Report);
		OrderDetailsBean ODB = new OrderDetailsBean();
		TVCPEGProvision TVCPEG = new TVCPEGProvision(Report);
		String CPWNRef = null;

		while(PRV.hasNext()){
			String PRVCMD = PRV.next();
			switch(PRVCMD){
			case "EVGConfirmAppointment":
				DbU.WaitforGwyCmdId(Str_CLI, "3", "EVGConfirmAppointment", true);
				EVG.EVGProvide(Constants.Completed, Str_CLI);
				break;
			case "SubmitCPEOrder" :
				DbU.WaitforGwyCmdId(Str_CLI, "3", "SubmitCPEOrder", true);
				CPEG.CPEGProvide(Constants.Completed, Str_CLI);
				break;
			case "IPTVProvide":
				DbU.WaitforGwyCmdId(Str_CLI, "3", "IPTVProvide", true);
				IPTV.IPTVProvide(Constants.Completed, Str_CLI);
				break;
			case "SubmitTVCPEOrder":
				DbU.WaitforGwyCmdId(Str_CLI, "3", "SubmitTVCPEOrder", true);
				TVCPEG.TVCPEGProvide(Constants.Completed, Str_CLI);
				break;
			case "LLUModify":
				DbU.WaitforGwyCmdId(Str_CLI, "3", "LLUModify", true);
				LLU.LLUModify(Constants.Completed, Str_CLI, "");
				break;
			case "SMPFProvide":
				DbU.WaitforGwyCmdId(Str_CLI, "3", "SMPFProvide", true);
				SMPF.SMPFProvide(Constants.Completed, Str_CLI, "");
				break;
			case "SMPFMigrate":
				DbU.WaitforGwyCmdId(Str_CLI, "3", "SMPFMigrate", true);
				SMPF.SMPFMigrate(Constants.Completed, Str_CLI, "");
				break;
			case "IPTVModify":
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
			default:
				break;
			}
		}
	}

	public void InitialSale_GenericProvision(ArrayList<String> ProvCmd,String Str_CLI,String SourcePackage) throws Exception{

		Iterator<String> PRV=ProvCmd.iterator();
		DbUtilities DbU = new DbUtilities(Report);
		LLUProvision LLU = new LLUProvision(Report);
		NGAProvision NGA = new NGAProvision(Report);
		EVGProvision EVG = new EVGProvision(Report);
		CPEGProvision CPEG = new CPEGProvision(Report);
		IPTVProvision IPTV = new IPTVProvision(Report);
		StubFilePlacing SF = new StubFilePlacing(Report);
		OrderDetailsBean ODB = new OrderDetailsBean();
		String CPWNRef = null;

		while(PRV.hasNext()){
			String PRVCMD = PRV.next();
			switch(PRVCMD){
			case "LLUMigrate":
				DbU.WaitforGwyCmdId(Str_CLI, "0", "LLUMigrate", true);
				LLU.LLUMigrate_SIM2(Constants.Completed, Str_CLI, false, true);
				break;
			case "NGAProvide":
				if (SourcePackage.equalsIgnoreCase("MPF")){ 
					DbU.WaitforGwyCmdId(Str_CLI, "0", "NGAProvide", true);
					NGA.NGAProvide(Constants.Completed, Str_CLI, "");
				}
				else{
					DbU.WaitforGwyCmdId(Str_CLI, "0", "NGAProvide", true);
					NGA.NGAProvide_SIM2(Constants.Completed, Str_CLI, "");
				}
				break;
			case "EVGConfirmAppointment":
				DbU.WaitforGwyCmdId(Str_CLI, "0", "EVGConfirmAppointment", true);
				EVG.EVGProvide(Constants.Completed, Str_CLI);
				break;
			case "SubmitCPEOrder" :
				DbU.WaitforGwyCmdId(Str_CLI, "0", "SubmitCPE", true);
				CPEG.CPEGProvide(Constants.Completed, Str_CLI);
				break;

			case "IPTVProvide":
				DbU.WaitforGwyCmdId(Str_CLI, "0", "IPTVProvide", true);
				IPTV.IPTVProvide(Constants.Completed, Str_CLI);
				break;
			case "LLUModify":
				DbU.WaitforGwyCmdId(Str_CLI, "0", "LLUModify", true);
				LLU.LLUModify(Constants.Completed, Str_CLI, "");
				break;
			case "IPTVModify":
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
			default:
				break;
			}
		}
	}

	public void NewlineSale_GenericProvision(ArrayList<String> ProvCmd,String RefNo, String Str_CLI, String TargetPackage) throws Exception{

		Iterator<String> PRV=ProvCmd.iterator();
		DbUtilities DbU = new DbUtilities(Report);
		LLUProvision LLU = new LLUProvision(Report);
		NGAProvision NGA = new NGAProvision(Report);
		EVGProvision EVG = new EVGProvision(Report);
		CPEGProvision CPEG = new CPEGProvision(Report);
		TVCPEGProvision TVCPEG = new TVCPEGProvision(Report);
		IPTVProvision IPTV = new IPTVProvision(Report);
		SMPFProvision SMPF =  new SMPFProvision(Report);

		while(PRV.hasNext()){
			String PRVCMD = PRV.next();
			switch(PRVCMD){
			case "LLUProvideNew":
				DbU.WaitforGwyCmdId(RefNo, "13", "LLUProvideNew", true);
				if (TargetPackage.contains("Fibre")){
					LLU.LLUProvideNew_SIM2(Completed, RefNo, Str_CLI, "");
				}
				else{
					LLU.LLUProvideNew(Constants.Completed,  RefNo, Str_CLI,"");
				}
				break;
			case "LLUProvideTakeover":
				DbU.WaitforGwyCmdId(RefNo, "13", "LLUProvideTakeover", true);
				LLU.LLUProvideTakeover(Constants.Completed,  RefNo, Str_CLI, "");
				break;
			case "LLUModify":
				DbU.WaitforGwyCmdId(Str_CLI, "13", "LLUModify", true);
				LLU.LLUModify(Constants.Completed, Str_CLI, "");
				break;

			case "NGAProvide":
				DbU.WaitforGwyCmdId(Str_CLI, "13", "NGAProvide", true);
				if (TargetPackage.contains("Fibre")){
					NGA.NGAProvide_SIM2(Constants.Completed, Str_CLI, "");
				}
				else{
					NGA.NGAProvide(Constants.Completed, Str_CLI, "");
				}
				break;
			case "NGAModify":
				DbU.WaitforGwyCmdId(Str_CLI, "13", "NGAModify", true);
				NGA.NGAModify(Constants.Completed, Str_CLI);
				break;	

			case "EVGConfirmAppointment":
				DbU.WaitforGwyCmdId(Str_CLI, "13", "EVGConfirmAppointment", true);
				EVG.EVGProvide(Constants.Completed, Str_CLI);
				break;

			case "SubmitCPEOrder":
				DbU.WaitforGwyCmdId(Str_CLI, "13", "SubmitCPEOrder", true);
				CPEG.CPEGProvide(Constants.Completed, Str_CLI);
				break;

			case "SubmitTVCPEOrder":
				DbU.WaitforGwyCmdId(Str_CLI, "13", "SubmitTVCPEOrder", true);
				TVCPEG.TVCPEGProvide(Constants.Completed, Str_CLI);
				break;	

			case "IPTVProvide":
				DbU.WaitforGwyCmdId(Str_CLI, "13", "IPTVProvide", true);
				IPTV.IPTVProvide(Constants.Completed, Str_CLI);
				break;

			case "SMPFProvide":
				DbU.WaitforGwyCmdId(Str_CLI, "13", "SMPFProvide", true);
				SMPF.SMPFProvide(Constants.Completed, Str_CLI, "");
				break;

			}
		}
	}

	public void HomeMove_GenericProvision(ArrayList<String> ProvCmd,String Str_CLI, String Str_NewCLI, String TargetPackage) throws Exception{

		Iterator<String> PRV=ProvCmd.iterator();
		DbUtilities DbU = new DbUtilities(Report);
		LLUProvision LLU = new LLUProvision(Report);
		NGAProvision NGA = new NGAProvision(Report);
		EVGProvision EVG = new EVGProvision(Report);
		CPEGProvision CPEG = new CPEGProvision(Report);
		TVCPEGProvision TVCPEG = new TVCPEGProvision(Report);
		IPTVProvision IPTV = new IPTVProvision(Report);
		SMPFProvision SMPF =  new SMPFProvision(Report);

		while(PRV.hasNext()){
			String PRVCMD = PRV.next();
			switch(PRVCMD){
			case "LLUCease":
				DbU.WaitforGwyCmdId(Str_CLI, "2", "LLUCease", true);
				LLU.LLUCease(Constants.Completed, Str_CLI);
				break;
			case "SMPFCease":
				DbU.WaitforGwyCmdId(Str_CLI, "2", "SMPFCease", true);
				SMPF.SMPFCease(Constants.Completed, Str_CLI);
				break;

			case "LLUProvideNew":
				DbU.WaitforGwyCmdId("HM"+Str_CLI, "13", "LLUProvideNew", true);
				if (TargetPackage.contains("FIBRE")){
					LLU.LLUProvideNew_SIM2(Constants.Completed, "HM"+Str_CLI, Str_NewCLI, "");
				}
				else{
					LLU.LLUProvideNew(Constants.Completed, "HM"+Str_CLI, Str_NewCLI, "");
				}
				break;
			case "LLUProvideTakeover":
				DbU.WaitforGwyCmdId("HM"+Str_CLI, "13", "LLUProvideTakeover", true);
				LLU.LLUProvideTakeover(Constants.Completed, "HM"+Str_CLI, Str_NewCLI, "");
				break;
			case "LLUModify":
				DbU.WaitforGwyCmdId(Str_CLI, "13", "LLUModify", true);
				LLU.LLUModify(Constants.Completed, Str_CLI, "");
				break;

			case "NGAProvide":
				DbU.WaitforGwyCmdId(Str_NewCLI, "13", "NGAProvide", true);
				if (TargetPackage.contains("FIBRE")){
					NGA.NGAProvide_SIM2(Constants.Completed, Str_NewCLI, "");
				}
				else{
					NGA.NGAProvide(Constants.Completed, Str_NewCLI, "");
				}
				break;
			case "NGAModify":
				DbU.WaitforGwyCmdId(Str_NewCLI, "13", "NGAModify", true);
				NGA.NGAModify(Constants.Completed, Str_NewCLI);
				break;	

			case "EVGConfirmAppointment":
				DbU.WaitforGwyCmdId(Str_NewCLI, "13", "EVGConfirmAppointment", true);
				EVG.EVGProvide(Constants.Completed, Str_NewCLI);
				break;

			case "SubmitCPEOrder":
				DbU.WaitforGwyCmdId(Str_NewCLI, "13", "SubmitCPEOrder", true);
				CPEG.CPEGProvide(Constants.Completed, Str_NewCLI);
				break;

			case "SubmitTVCPEOrder":
				DbU.WaitforGwyCmdId(Str_NewCLI, "13", "SubmitTVCPEOrder", true);
				TVCPEG.TVCPEGProvide(Constants.Completed, Str_NewCLI);
				break;	

			case "IPTVProvide":
				DbU.WaitforGwyCmdId(Str_NewCLI, "13", "IPTVProvide", true);
				IPTV.IPTVProvide(Constants.Completed, Str_NewCLI);
				break;

			case "SMPFProvide":
				DbU.WaitforGwyCmdId(Str_CLI, "13", "SMPFProvide", true);
				SMPF.SMPFProvide(Constants.Completed, Str_CLI, "");
				break;

			}
		}
	}

	public void ChangeFeature_GenericProvision(ArrayList<String> ProvCmd,String Str_CLI) throws Exception{

		Iterator<String> PRV=ProvCmd.iterator();
		DbUtilities DbU = new DbUtilities(Report);
		LLUProvision LLU = new LLUProvision(Report);
		NGAProvision NGA = new NGAProvision(Report);
		CPEGProvision CPEG = new CPEGProvision(Report);
		StubFilePlacing SF = new StubFilePlacing(Report);
		OrderDetailsBean ODB = new OrderDetailsBean();

		while(PRV.hasNext()){
			String PRVCMD = PRV.next();
			switch(PRVCMD){
			case "LLUModify":
				DbU.WaitforGwyCmdId(Str_CLI, "3", "LLUModify", true);
				LLU.LLUModify(Constants.Completed, Str_CLI, "");
				break;

			case "NGAModify":
				DbU.WaitforGwyCmdId(Str_CLI, "3", "NGAModify", true);
				NGA.NGAModify(Constants.Completed, Str_CLI);
				break;	

			case "SubmitCPE":
				DbU.WaitforGwyCmdId(Str_CLI, "3", "SubmitCPEOrder", true);
				CPEG.CPEGProvide(Constants.Completed, Str_CLI);
				break;

			case "IPTVModify":
				String CPWNRef = null;
				//DbU.WaitforGwyCmdId(Str_CLI, "3", "IPTVModify", true);
				DbU.getOrderDetails(Str_CLI, "IPTVModify", ODB);
				DbU.SM_SKID_CPWRef_CASR(ODB);
				CPWNRef = ODB.getCPWN();
				SF.PlaceFile(StubType.IPTVModify, CPWNRef);
				break;	

			}
		}
	}

}




