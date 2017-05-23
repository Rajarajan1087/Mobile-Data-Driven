package com.SharedModules;

public interface Constants {
	//Provisioning State Contants
	//InputSheet Path
	public static final String InputSheet = System.getProperty("user.dir")+"\\LocalJARs\\InputSheet.xls";
	public static final String MyAccountSheetName = "MyAccount";
	public static final String OnlineSheetName = "Online";
	public static final String Stub_GetCPEResponsse = "/opt/app/tibco/Stubs/EAIOpal/GetCPE";
	public static final String Stub_SPA = "/opt/app/tibco/Stubs/SPA/Response";
	public static final String Stub_CreditCheck = "/opt/app/tibco/Stubs/Equifax/Response";
	public static final String Stub_MatchAddressResponse = "/opt/app/tibco/Stubs/Equifax/AddressResponse/Match";
	public static final String Template_NGAV2_SI_LE5Mbps=System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGAV2_SI_LE5Mbps.xml";
	public static final String Template_NGAGEA_SINO_MILT5=System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGAGEA_SINO_MILT5.xml";
	public static final String Template_NGAGEA=System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGAGEA.xml";
	public static final String Template_LLU_ALK_Newline = System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_ALK_Newline.txt";
	public static final String Template_Switcher_NPAC = System.getProperty("user.dir")+"\\ProvisioningTemplates\\Switcher_NPAC.xml";
	public static final String Template_SPA=System.getProperty("user.dir")+"\\ProvisioningTemplates\\SPA.xml";
	public static final String Template_Check_Nominated_CLI_Sales = System.getProperty("user.dir")+"\\ProvisioningTemplates\\CheckNominateCLI_Sales.txt";
	public static final String Template_Check_Nominated_CLI1 = System.getProperty("user.dir")+"\\ProvisioningTemplates\\CheckNominateCLI_1.txt";
	public static final String Template_Check_Nominated_CLI2 = System.getProperty("user.dir")+"\\ProvisioningTemplates\\CheckNominateCLI_2.txt";
	public static final String Template_LLUALK_BTStop_LLU = System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_ALK_BTStop_LLU.txt";
	public static final String Template_LLU_ALK_BTStop_IPTVUnicast = System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_ALK_BTStop_IPTVUnicast.txt";
	public static final String Template_ExchangeStatus_Low = System.getProperty("user.dir")+"\\ProvisioningTemplates\\Low_exchangestatus.txt";
	public static final String Template_NGA_ALK_LE5Mbps = System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGA_ALKNew_SpeedLT5.xml";
	public static final String Template_NGAALK_SINO_MIYES = System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGA_ALK_SI_NO.xml";
	public static final String Template_NGAALK_new_SpeedLT5 = System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGA_ALKNew_SpeedLT5.xml";
	public static final String Template_LLU_ALK_LowSpeed = System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_ALK_LowSpeed.txt";
	public static final String Template_LLU_CPS_LowSpeed = System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_CPS_LowSpeed.txt";
	public static final String Template_CreditCheck_ConditionalAccept = System.getProperty("user.dir")+"\\ProvisioningTemplates\\CreditCheck_ConditionalAccept.txt";
	public static final String Template_CreditCheck_Referral = System.getProperty("user.dir")+"\\ProvisioningTemplates\\CreditCheck_Referral.txt";
	public static final String Template_CreditCheck_TotalAccept = System.getProperty("user.dir")+"\\ProvisioningTemplates\\CreditCheck_TotalAccept.txt";
	public static final String Template_CreditCheck_TotalDecline = System.getProperty("user.dir")+"\\ProvisioningTemplates\\CreditCheck_TotalDecline.txt";
	public static final String Template_LLU_CPS_PORT_RH_Virgin=System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_CPS_PORT_RH_Virgin.txt";
	public static final String Template_ACErrorResponseVirgin=System.getProperty("user.dir")+"\\ProvisioningTemplates\\ACErrorResponseVirgin.txt";
	public static final String Template_ACErrorResponseSky=System.getProperty("user.dir")+"\\ProvisioningTemplates\\ACErrorResponseSky.txt";
	public static final String Template_NPAC_ACErrorResponseSKY=System.getProperty("user.dir")+"\\ProvisioningTemplates\\NPAC_ACErrorResponseSKY.xml";
	public static final String Template_NPAC_ACErrorResponseVirgin=System.getProperty("user.dir")+"\\ProvisioningTemplates\\NPAC_ACErrorResponseVirgin.xml";
	public static final String Template_ACErrorResponseBT=System.getProperty("user.dir")+"\\ProvisioningTemplates\\ACErrorResponseBT.txt";
	public static final String Template_LLUALK_BTLive_Unicast = System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_ALK_BTLive_Unicast.txt";
	public static final String Template_MatchAddressResponse = System.getProperty("user.dir")+"\\ProvisioningTemplates\\MatchAddressResponse.txt";
	public static final String Template_GetCPEResponse = System.getProperty("user.dir")+"\\ProvisioningTemplates\\GetCPEResponse.txt";
	
	public static final String POSTCODE1 = "W11 4AR";

	public static String CommonAddressPart = "White City ";
	public static final String  Accepted = "0";
	public static final String  Validated = "2";
	public static final String  Requested = "6";
	public static final String  Progressing = "19";
	public static final String  CeasePending = "27";
	public static final String  Assigned = "65";
	public static final String  Acknowledged = "20";
	public static final String  CoolingOff = "9";
	public static final String  Committed = "24";
	public static final String  PONR = "23";
	public static final String  Completed = "12";
	public static final String  PlanningDelayed = "21";;
	public static final String  ImplementationDelayed = "22";
	public static final String  LinkedOrderMatched = "71";
	public static final String  Pending = "57";
	public static final String  Semicomplete = "60";
	public static final String  Dispatched = "75";
	public static final String  Installed = "59";
	public static final String  Confirmed = "89";
	public static final String  Delivered = "81";
	public static final String  CancellationPending = "25";
	public static final String  Suspended = "58";
	public static final String  Cancelled = "10";
	public static final String  Received = "93";
	public static final String  prov_AOL = "821";
	public static final String  prov_TTK = "569";
	public static final String  ChangeOfAddressSameCLI = "95";
	public static final String  CPSRetain = "94";
	public static final String AutoGNPCompletionRequired = "63";
	public static final String AutoGNPRequired ="64";
	public static final String Rejected ="11";
	public static final String AmmendmentPending = "26";
	public static final String SoftCancelled = "98";
	
	//Provisioning State Constants for BSS_Unsolicited Cease
		public static final String BSS_UNC_Acknowledged = "2";
		public static final String BSS_UNC_CeasePending = "3";
		public static final String BSS_UNC_Completed = "7";

		//Provisioning State Constants for FTTPAmend

		public static final String FTTP_Amend_Accepted = "1";
		public static final String FTTP_Amend_Validated = "2";
		public static final String FTTP_Amend_Requested = "3";
		public static final String FTTP_Amend_Completed = "4";

		//Provisioning State Constants for FTTPCancel

		public static final String FTTPCancel_Accepted = "1";
		public static final String FTTPCancel_Validated = "2";
		public static final String FTTPCancel_Requested = "3";
		public static final String FTTPCancel_Completed = "4";

	//Provisioning State Constants for FTTP_Provide
		
		public static final String FTTP_Provide_Accepted = "1";
		public static final String FTTP_Provide_Requested = "3";
		public static final String FTTP_Provide_Acknowledged = "5";
		public static final String FTTP_Provide_Coolingoff = "6";
		public static final String FTTP_Provide_Completed = "12";
		
		//Provisioning State Constants for FTTP_Cease
		
		public static final String FTTP_Cease_Accepted = "1";
		public static final String FTTP_Cease_Validated = "2";
		public static final String FTTP_Cease_CeasePending ="3";
		public static final String FTTP_Cease_Completed = "5";
				
		//Provisioning State Constants for FTTP_Modify
				
		public static final String FTTP_Modify_Accepted = "1";
		public static final String FTTP_Modify_Validated = "2";
		public static final String FTTP_Modify_ModifyPending = "3";
		public static final String FTTP_Modify_Completed = "4";
		
		
		//Provisioning State Constants for FTTP_Suspend
		
		public static final String FTTP_Suspend_Accepted = "1";
		public static final String FTTP_Suspend_Validated = "2";
		public static final String FTTP_Suspend_Completed = "3";
		
		
		//Provisioning State Constants for FTTP_UNC

		public static final String FTTP_UNC_Acknowledged = "1";
		public static final String FTTP_UNC_CeasePending = "1";
		public static final String FTTP_UNC_Completed = "1";

		
		
		//Provisioning State Constants for IPS_Migrate_AOD	
		
		public static final String IPS_MIGRATE_Accepted = "1";
		public static final String IPS_MIGRATE_Requested = "2";
		public static final String IPS_MIGRATE_Acknowledged = "8";
		public static final String IPS_MIGRATE_Coolingoff = "10";
		public static final String IPS_MIGRATE_Completed = "14";
		
		//Provisioning State Constants for IPS_Provide_AOD	
		
		public static final String IPS_Provide_Accepted = "1";
		public static final String IPS_Provide_Acknowledged = "8";
		public static final String IPS_Provide_Coolingoff = "10";
		public static final String IPS_Provide_Completed = "14";
		

		//Provisioning State Constants for LLU_Amend

		public static final String LLU_Amend_Accepted = "1";
		public static final String LLU_Amend_Validated = "2";
		public static final String LLU_Amend_Requested = "5";
		public static final String LLU_Amend_AmendmentPending = "6";
		public static final String LLU_Amend_Completed = "7";

		//Provisioning State Constants for LLU_Cancel
		
		public static final String LLU_Cancel_Accepted = "1";
		public static final String LLU_Cancel_Validated = "2";
		public static final String LLU_Cancel_Requested = "5";
		public static final String LLU_Cancel_CancellationPending = "6";
		public static final String LLU_Cancel_Completed = "7";

		//Provisioning State Constants for LLU_Cease

		public static final String LLU_Cease_Accepted = "1";
		public static final String LLU_Cease_Validated = "2";
		public static final String LLU_Cease_Requested = "5";
		public static final String LLU_Cease_Progressing = "6";
		public static final String LLU_Cease_Acknowledged = "7";
		public static final String LLU_Cease_CeasePending = "9";
		public static final String LLU_Cease_Completed = "13";
		
		//Provisioning State Constants for LLU_Migrate_AOD
		
		public static final String LLU_Migrate_Accepted = "1";
		public static final String LLU_Migrate_Validated = "2";
		public static final String LLU_Migrate_Requested = "5";
		public static final String LLU_Migrate_Progressing = "6";
		public static final String LLU_Migrate_Acknowledged = "7";
		public static final String LLU_Migrate_CoolingOff = "10";
		public static final String LLU_Migrate_PONR = "12";
		public static final String LLU_Migrate_Completed = "14";
		
		//Provisioning State Constants for LLU ProvideNew				
		public static final String LLU_PRN_Accepted = "1";
		public static final String LLU_PRN_Validated = "2";
		public static final String LLU_PRN_Requested = "5";
		public static final String LLU_PRN_Progressing = "6";
		public static final String LLU_PRN_Acknowledged = "7";
		public static final String LLU_PRN_Coolingoff = "9";
		public static final String LLU_PRN_ImplementationDelayed = "10";
		public static final String LLU_PRN_PONR = "11";
		public static final String LLU_PRN_Completed = "13";
	
		//Provisioning State Constants for LLU_Suspend
		
		public static final String LLU_Suspend_Accepted = "1";
		public static final String LLU_Suspend_Validated = "4";
		public static final String LLU_Suspend_Completed = "5";

		
		//Provisioning State Constants for LLU_Renumber
		public static final String LLU_Renumber_Accepted = "1";
		public static final String LLU_Renumber_Validated = "2";
		public static final String LLU_Renumber_Completed = "3";

	    //Provisioning State Constants for LLU_Modify
		
		public static final String LLU_Modify_Accepted = "1";
		public static final String LLU_Modify_Validated = "2";
		public static final String LLU_Modify_Completed = "11";
		
		//Provisioning State Constants for LLU_PRT
		
		public static final String LLU_PRT_Accepted = "1";
		public static final String LLU_PRT_Validated = "2";
		public static final String LLU_PRT_Requested = "5";
		public static final String LLU_PRT_Progressing = "6";
		public static final String LLU_PRT_Acknowledged = "7";
		public static final String LLU_PRT_Coolingoff = "9";
		public static final String LLU_PRT_PONR = "11";
		public static final String LLU_PRT_Completed = "13";
				
		//Provisioning State Constants for NGA_Amend
		
		public static final String NGA_Amend_Accepted = "1";
		public static final String NGA_Amend_Validated = "2";
		public static final String NGA_Amend_Requested = "5";
		public static final String NGA_Amend_AmendmentPending = "6";
		public static final String NGA_Amend_Completed = "7";
		public static final String NGA_Amend_Rejected = "8";
		
		//Provisioning State Constants for NGA_Suspend

		public static final String NGASuspend_Accepted = "1";
		public static final String NGASuspend_Validated = "2";
		public static final String NGASuspend_Rejected = "3";
		public static final String NGASuspend_Completed = "4";
		
		//Provisioning State Constants for NGA Cancel				
		public static final String NGA_Cancel_Accepted = "1";
		public static final String NGA_Cancel_Validated = "2";
		public static final String NGA_Cancel_Requested = "5";
		public static final String NGA_Cancel_CancellationPending = "6";
		public static final String NGA_Cancel_Completed = "8";
		public static final String NGA_Cancel_Rejected = "7";
		
		//Provisioning State Constants for NGA Modify				
		public static final String NGA_Modify_Accepted = "1";
		public static final String NGA_Modify_Validated = "2";
		public static final String NGA_Modify_Requested = "5";
		public static final String NGA_Modify_Progressing = "6";
		public static final String NGA_Modify_Acknowledged = "7";
		public static final String  NGA_Modify_Committed = "8";
		public static final String  NGA_Modify_Completed = "11";
		
		//Provisioning State Constants for NGA_Provide

		public static final String NGAProvide_Accepted = "1";
		public static final String NGAProvide_Validated = "2";
		public static final String NGAProvide_Requested = "5";
		public static final String NGAProvide_Progressing = "6";
		public static final String NGAProvide_Acknowledged = "7";
		public static final String NGAProvide_LinkedOrderMatched_SIM2 = "8";
		public static final String NGAProvide_PlanningDelayed_SIM2 = "9";
		public static final String NGAProvide_Cancelled = "10";
		public static final String NGAProvide_Coolingoff = "11";
		public static final String NGAProvide_ImplemenationDelayed = "12";
		public static final String NGAProvide_PONR = "13";
		public static final String NGAProvide_Rejected_SIM2= "14";
		public static final String NGAProvide_Completed = "15";
		public static final String NGAProvide_AwaitingLinkedOrderUpdate_SIM2 = "16";
		public static final String NGAProvide_AwaitingLinkedOrderCompletion_SIM2 = "17";
		
		//Provisioning State Constants for OBC Cancel
		
		public static final String OBC_Cancel_Accepted = "1";
		public static final String OBC_Cancel_Validated = "3";
		public static final String OBC_Cancel_Requested = "5";
		public static final String OBC_Cancel_Completed = "7";
		
		//Provisioning State Constants for OBC Modify
		
		public static final String OBC_Modify_Accepted = "1";
		public static final String OBC_Modify_Validated = "2";
		public static final String OBC_Modify_Rejected = "7";
		public static final String OBC_Modify_Completed = "5";
		
		//Provisioning State Constants for OBC_Cease
		
		public static final String OBCCease_Accepted = "1";
		public static final String OBCCease_Validated = "3";
		public static final String OBCCease_Requested = "5";		
		public static final String OBCCease_Pending = "7";
		public static final String OBCCease_Completed = "10";
		public static final String OBCCease_PONR = "16";
		
		//Provisioning State Constants for OBC_Provide
		
		public static final String OBC_Provide_Accepted = "1";
		public static final String OBC_Provide_Validated = "3";
		public static final String OBC_Provide_Requested = "5";
		public static final String OBC_Provide_CoolingOff = "7";
		public static final String OBC_Provide_PONR = "18";
		public static final String OBC_Provide_Completed = "11";
		public static final String OBC_Provide_Cancelled = "12";
		public static final String OBC_Provide_Rejected = "13";
		
		//Provisioning State Constants for OBC Suspend

		public static final String OBC_Suspend_Accepted = "1";
		public static final String OBC_Suspend_Validated = "2";
		public static final String OBC_Suspend_Rejected = "4";
		public static final String OBC_Suspend_Completed = "3"; 
		
		//Provisioning State Constants for OBC UNC
		
		public static final String OBC_UNC_Pending = "1";
		public static final String  OBC_UNC_Completed = "2";
				
		//Provisioning State Constants for VM_Cease
				
		public static final String VM_Cease_Accepted = "1";
		public static final String VM_Cease_Validated = "2";
		public static final String VM_Cease_Completed = "7";
		
		//Provisioning State Constants for VM_Provide
		
		public static final String VM_Provide_Accepted = "1";
		public static final String VM_Provide_Validated = "2";
		public static final String VM_Provide_Completed = "7";
		
		//Provisioning State Constants for VM_Renumber
		
		public static final String VM_Renumber_Accepted = "1";
		public static final String VM_Renumber_Validated = "2";
		public static final String VM_Renumber_Completed = "7";

		//Provisioning State Constants for SMPFT3_Provide	
				
		public static final String SMPFT3_Provide_Accepted = "1";
		public static final String SMPFT3_Provide_Validated = "2";
		public static final String SMPFT3_Provide_Requested = "6";
		public static final String SMPFT3_Provide_Acknowledged = "7";
		public static final String SMPFT3_Provide_Coolingoff = "11";
		public static final String SMPFT3_Provide_Completed = "15";

		
	public static final String  Accepted_AOD = "1";
	public static final String  Validated_AOD = "2";
	public static final String  Requested_AOD = "5";
	public static final String  Progressing_AOD = "6";
	public static final String  CeasePending_AOD = "9";
//	public static final String  Assigned_AOD = "65";
	public static final String  Acknowledged_AOD = "7";
	public static final String  CoolingOff_AOD = "10";
	public static final String  Committed_AOD = "8";
	public static final String  PONR_AOD = "12";
	public static final String  Completed_AOD = "14";
	public static final String  PlanningDelayed_AOD = "8";;
	public static final String  ImplementationDelayed_AOD = "11";
	public static final String  LinkedOrderMatched_AOD= "8";
	public static final String  Pending_AOD = "7";
//	public static final String  Semicomplete_AOD = "60";
	public static final String  Dispatched_AOD = "75";
	public static final String  Installed_AOD = "59";
	public static final String  Confirmed_AOD = "89";
	public static final String  Delivered_AOD ="81";
	public static final String  CancellationPending_AOD = "25";	
	public static final String  Suspended_AOD = "58";
	public static final String  Cancelled_AOD = "10";
	public static final String  Received_AOD = "93";

	public static final String  ChangeOfAddressSameCLI_AOD = "95";
	public static final String  CPSRetain_AOD = "94";
	public static final String AutoGNPCompletionRequired_AOD = "63";
	public static final String AutoGNPRequired_AOD ="64";
	public static final String Rejected_AOD ="11";
	public static final String AmmendmentPending_AOD = "26";
	public static final String SoftCancelled_AOD = "98";

	
	
	//Generic Constants
	public static final String OUTPUTFILE_DIR = System.getProperty("user.dir")+"\\ProvisioningUpdates\\";

	//Appoint Stub file Name Constants  
	public static final String BTAPPOINTMENT_FILE_NAME = "Success_11";
	public static final String EVGAPPOINTMENT_FILE_NAME = "Resp";


	// EVG RC URL 
	public String EVG_RC_URL = "http://10.155.38.169/cgi-bin/evg_instance.pl";
	
	//Business Function Constants
	public static final String HomeMoveType_WithLineRental = "Capture Homemove With Line Rental";
	public static final String HomeMoveType = "Capture Homemove(BB only without Line Rental)";
	
	//Stub File Destination Location
	public static final String Stub_NGAv2CLI = "\\\\10.155.53.190\\Data\\CLI\\";
	public static final String Stub_NGAv2CLI_new = "/opt/app/tibco/Stubs/EAIOpal/GetNGA/xmls";
	public static final String Stub_NGA_GEA = "\\\\10.155.53.190\\Data\\CLI\\";
	public static final String Stub_IPSCLI = "\\\\10.155.53.187\\IPS\\";
	public static final String Stub_SMPFCLI= "\\\\10.155.53.187\\SMPF\\";
//	public static final String Stub_LLUCLI = "/opt/LineCharacteristicsWSV5/GetLineCharacteristics/TelephoneNumber";
	public static final String Stub_LLUCLI = "/opt/app/tibco/Stubs/EAIOpal/GetLLULineCharacteristics";
	public static final String Stub_LLUALK = "/opt/app/tibco/Stubs/EAIOpal/GetLLULineCharacteristics/";
	public static final String Stub_NPAC = "/opt/app/tibco/Stubs/EAIOpal/NetworkProductAvailability/xmls/";
//	public static final String Stub_LLUALK = "/opt/LineCharacteristicsWSV5/GetLineCharacteristics/GoldAddressKey";
	public static final String Stub_LLUPostcode = "/opt/app/tibco/Stubs/EAIOpal/GetLLULineCharacteristics/";
//	public static final String Stub_LLUPostcode = "/opt/LineCharacteristicsWSV5/GetLineCharacteristics/Postcode";
	public static final String Stub_Check_Nominated_CLI = "/opt/app/tibco/Stubs/CheckNominatedCLI";
	public static final String Stub_Check_CLI_Poratability = "/opt/app/tibco/Stubs/CheckCLIPortability";
	public static final String Stub_Retain_Number = "/opt/app/tibco/Stubs/RetainNumber";
	public static final String Stub_Working_Error_Response = "/opt/LineCharacteristicsWSV5/GetLineCharacteristics/TelephoneNumber";
	public static final String Stub_AddressMatching = "\\\\10.155.53.186\\GetAvailableAddresses\\Data\\";
	public static final String Stub_AddressMatchingNew = "/opt/app/tibco/Stubs/CPS";
	public static final String Stub_NGAALK = "\\\\10.155.53.190\\Data\\GoldKey\\";
	public static final String Stub_EVG_Query_Slot_Qube = "/opt/app/tibco/Stubs/Qube/querySlot";
	public static final String Stub_EVG_Query_Slot_Qube1 = "/opt/app/tibco/Stubs/Qube-1/querySlot";
	public static final String Stub_BT_APPOINTMENT = "\\\\10.155.53.186\\GetAvailableAppointments\\Data\\";
	public static final String Stub_IssueAddressKey = "\\\\10.155.53.186\\IssueAddressKey\\Data\\";
	public static final String Stub_NGAALK_new = "/opt/app/tibco/Stubs/EAIOpal/GetNGA/xmls";
		
	public static final String Stub_IPTVProvideStubs = "/opt/IPTVExchangeInterfaceService/confirmOrder";
	public static final String Stub_IPTVProvideStubsNew = "/opt/app/tibco/Stubs/IPTV/ProvideIPTV";
	public static final String Stub_IPTVSuspendStubs = "/opt/IPTVExchangeInterfaceService/setUsageStatus";
	public static final String Stub_IPTVSuspendStubsNew = "/opt/app/tibco/Stubs/IPTV/SuspendIPTV";
	public static final String Stub_IPTVModifyStubs = "/opt/IPTVExchangeInterfaceService/updateEntitlements";	
	public static final String Stub_IPTVModifyStubsNew ="/opt/app/tibco/Stubs/IPTV/ModifyIPTV";
	//public static final String Stub_IPTVCeaseStubs = "/opt/IPTVExchangeInterfaceService/terminateAccount";
	public static final String Stub_IPTVCeaseStubs = "/opt/app/tibco/Stubs/IPTV/CeaseIPTV";
	public static final String Stub_getCPEAvailability_TV = "/opt/app/tibco/Stubs/EAIOpal/GetCPE";
	public static final String Stub_getCPEAvailability_Fibre = "/opt/app/tibco/Stubs/EAIOpal/GetCPE";
	public static final String Stub_getCPEAvailability_TV_Fibre = "/opt/app/tibco/Stubs/EAIOpal/GetCPE";
	public static final String Stub_getCPEAvailability = "/opt/app/tibco/Stubs/EAIOpal/GetCPE";
	
	//SFTP Stub Details
	public static final String SFTPSTUB_host="10.180.20.4";
	public static final String SFTPSTUB_auth="automation";
	public static final String SFTPSTUB_pass="Stub@123";

	//DERBY Stub Details
	public static final String DERBYSTUB_host="10.180.20.90";
//	public static final String DERBYSTUB_auth="varadhb";
//	public static final String DERBYSTUB_pass="welcome123";
	public static final String DERBYSTUB_auth="movvasr";
	public static final String DERBYSTUB_pass="cobal@123";
	public static final String DERBYSTUB_auth1="movvasr";
	public static final String DERBYSTUB_pass1="cobal@123";

	//Stub File Template file Location
	    public static final String Template_NGAv2CLI_new=System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGAV2_new.xml";
	    public static final String Template_NGAv2CLI_OverRide=System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGAv2_OverRide.xml"; 
	    public static final String Template_NGAv2CLI=System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGAV2.txt";
		public static final String Template_NGA_GEA=System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGA_GEA.txt";
		public static final String Template_IPTVM=System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPTV_MC.txt";
		public static final String Template_IPTVU=System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPTV_UC.txt";
		public static final String Template_Check_Nominated_CLI = System.getProperty("user.dir")+"\\ProvisioningTemplates\\CheckNominateCLI.txt";
		public static final String Template_Retain_Number = System.getProperty("user.dir")+"\\ProvisioningTemplates\\RetainNumber.txt";
		public static final String Template_Check_CLI_Poratability = System.getProperty("user.dir")+"\\ProvisioningTemplates\\CheckCLIPortability.txt";
		public static final String Template_Working_Error_Response = System.getProperty("user.dir")+"\\ProvisioningTemplates\\Working_error_response.txt";
		public static final String Template_NPAC_LLUALK_BTLive = System.getProperty("user.dir")+"\\ProvisioningTemplates\\NPAC_LLU_ALK_BTLive.xml";
		public static final String Template_NPACExchangeStatus = System.getProperty("user.dir")+"\\ProvisioningTemplates\\NPAC_ExchangeStatus.xml";
		public static final String Template_NPAC_LLUALK_BTStop = System.getProperty("user.dir")+"\\ProvisioningTemplates\\NPAC_LLU_ALK_BTStop.xml";
		public static final String Template_NPAC_LLU_ALK_Newline = System.getProperty("user.dir")+"\\ProvisioningTemplates\\NPAC_LLU_ALK_Newline.xml";
		public static final String Template_QS3_V5 = System.getProperty("user.dir")+"\\ProvisioningTemplates\\QS3_V5.txt";
		public static final String Template_IPS = System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPS.txt";
		public static final String Template_LLUv5 = System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_CPS_V5.txt";
		public static final String Template_LLUALK_BTStop = System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_ALK_BTStop.txt";
		public static final String Template_LLUALK_BTLive = System.getProperty("user.dir")+"\\ProvisioningTemplates\\LLU_ALK_BTLive.txt";
		public static final String Template_ExchangeStatus = System.getProperty("user.dir")+"\\ProvisioningTemplates\\exchangestatus.txt";
		public static final String Template_AddressMatching = System.getProperty("user.dir")+"\\ProvisioningTemplates\\postcodetemplate.txt";
		public static final String Template_AddressMatchingNew = System.getProperty("user.dir")+"\\ProvisioningTemplates\\postcodetemplateNew.xml";
		public static final String Template_NGA_ALK_Live = System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGA_ALK_Live.txt";
		public static final String Template_NGA_ALK_Live_TRIO14 = System.getProperty("user.dir")+"\\ProvisioningTemplates\\NGA_ALK_Live_TRIO14.xml";
		public static final String Template_SMPF = System.getProperty("user.dir")+"\\ProvisioningTemplates\\SMPF.txt";
		public static final String Template_IssueAddressKey = System.getProperty("user.dir")+"\\ProvisioningTemplates\\IssueAddressKey.txt";
		public static final String Template_getCPEAvailability_TV = System.getProperty("user.dir")+"\\ProvisioningTemplates\\getCPEAvailability_TV.xml";
		public static final String Template_getCPEAvailability_Fibre = System.getProperty("user.dir")+"\\ProvisioningTemplates\\getCPEAvailability_Fibre.xml";
		public static final String Template_getCPEAvailability_TV_Fibre = System.getProperty("user.dir")+"\\ProvisioningTemplates\\getCPEAvailability_TV_Fibre.xml";
		public static final String Template_getCPEAvailability = System.getProperty("user.dir")+"\\ProvisioningTemplates\\getCPEAvailability.xml";		
		
		public static final String Template_IPTVProvideStubs = System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPTVProvideStubs.txt";
		public static final String Template_IPTVProvideStubsNew = System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPTVProvideStubsNew.xml"; 
		public static final String Template_IPTVModifyStubs = System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPTVModifyStubs.txt";
		public static final String Template_IPTVModifyStubsNew = System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPTVModifyStubsNew.xml";
		public static final String Template_IPTVCeaseStubs = System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPTVCeaseStubs.txt";
		public static final String Template_IPTVCeaseStubsNew = System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPTVCeaseStubsNew.xml";
		public static final String Template_IPTVSuspendStubs = System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPTVSuspendStubs.txt";
		public static final String Template_IPTVSuspendStubsNew = System.getProperty("user.dir")+"\\ProvisioningTemplates\\IPTVSuspendStubsNew.xml";

	//Provisioning Delay Time Constants

	public static final int PROV_TIME = 1000;
	public static final int OrderTimeOut=120;
	public static final int CommandTimeOut=120;
	//SOAP Constants
	public static final String  SOAPURL_PART= "/BusinessServices/WebGateway/Order/OrderEntryServices.serviceagent/UseSOAP";
	
	//PostCode and Region Constants 
	
	public static final String REGION = "MIDLANDS - North Yorkshire and Humberside";
//	public static final String REGION = "LONDON - Somerset and Swindon";
	
//	public static final String POSTCODE = "YO61 3PL";
//	public static final String POSTCODE = "BA13 3BN";
//	public static final String AddressKey = "A13424991620";
//	public static final String POSTCODE_ENG="BA13 3BN";
//	public static final String POSTCODE = "W3 6AA";
//	public static final String AddressKey = "A90024003999";
//	public static final String POSTCODE = "HA0 4TY";
//	public static final String AddressKey = "A01013548559";
//	public static final String AddressKey = "A01190905148";
//	public static final String POSTCODE = "W11 4AR";
//	public static final String AddressKey = "A01510357506";
//	public static final String AddressKey = "A14976706545";
//	public static final String AddressKey1 = "A10025222444";
//	public static final String AddressKey = "A17484550939";
	public static final String POSTCODE = "W3 6AA";
//	public static final String AddressKey = "A20003000100";
	//CPE Constants 

	public static final String YVPlusBoxCourier = "YV+ Box - Courier";
	public static final String PowerlineAdaptorCourier = "Powerline Adaptor - Courier";
	public static final String YVPlusBoxEngineer = "YV+ Box - Engineer";
	public static final String PowerlineAdaptorEngineer = "Powerline Adaptor - Engineer";
	public static final String PowerlineAdaptor2Courier = "Powerline Adaptor 2 - Courier";
	public static final String PowerlineAdaptor2Engineer = "Powerline Adaptor 2 - Engineer";
	public static final String PowerlineSinglePortBBCourier = "Powerline Single Port - BB - Courier";
	public static final String PowerlineDualPortBBCourier =  "Powerline Dual Port - BB - Courier";
	public static final String YVBoxEngineer = "YV Box - Engineer";
	public static final String YVBoxCourier = "YV Box - Courier";

	public static final String TRIO_ACC_URL = "http://10.180.18.223:25546";
	public static final String TRIO_ACC_UTILS_XPATH="//div[@id='nav']//a[contains(text(),'Utils')]";
	public static final String TRIO_ACC_MA_LINK_XPATH="//div[@class='sidebar']//a[contains(text(),'My Account Registration')]";
	public static final String TRIO_ACC_MA_SERVLET = "http://10.180.18.223:8080/MAREG/MAREGServlet";
	
	//My Account Constants

		public static final String STR_PASSWORD = "password1";

		//products
		public static final String MONTHLYLRCOST="�16.70";
		public static final String MONTHLYVLRCOST="�180.36";
		
		//Direct Debit Constants
		public static final String bankAccNumber = "61043676";
		public static final String branchSortCodeOne = "40";
		public static final String branchSortCodeTwo = "40";
		public static final String branchSortCodeThree = "45";

		//CreditCheck DOBS
		
		public static final String ConditionalAccept = "05051981";
		public static final String Total_Accept = "03021971";
		public static final String Total_Decline = "04021971";
		public static final String Referral = "05021971";
		public static final String commonCreditcheck = "02021971";

}

