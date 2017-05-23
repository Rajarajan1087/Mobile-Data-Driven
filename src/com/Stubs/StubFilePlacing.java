package com.Stubs;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.SharedModules.Constants;
import com.SharedModules.NameGenerator;
import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.Utils.Reusables;
import com.jcraft.jsch.*;


public class StubFilePlacing implements Constants {
	public Reporter Report;

	/**
	 * @param report
	 */
	public StubFilePlacing(Reporter report) {
		Report = report;
	}

	public StubFilePlacing() {
		// TODO Auto-generated constructor stub
	}
	public enum StubType{
		NPAC_ACErrorResponseSky,NPAC_ACErrorResponseVirgin,NPAC_ExchangeStatus,NPAC_LLU_ALK_BTLive,NPAC_LLU_ALK_BTStop,NPAC_LLU_ALK_Newline,Switcher,LLU_ALK_Newline,NGAv2CLI,NGAv2CLI_new,IPTVProvideNew,IPTVSuspendNew,IPTVModifyNew,IPTVM,IPTVU,QS3,Check_Nominated_CLI,Check_CLI_Poratability,Working_Error_Response,IPTVProvide,IPTVModify,
		IPTVCease,IPTVSuspend,LLUv5,QS4,LLU_ALK_BTStop,ExchangeStatus,PostCode_GoldAddress,NGA_ALK,NGAALK_new,NGAALK_new_SpeedLT5,NGAGEA,LLU_CPS_LowSpeed,
		LLU_ALK_LowSpeed,CreditCheck_TotalAccept,CreditCheck_ConditionalAccept,CreitCheck_TotalDecline,CreditCheck_Referral,
		LLU_CPS_PORT_RH_Virgin,ACErrorResponseVirgin,ACErrorResponseBT,LLU_ALK_BTLive,ACErrorResponseSky,MatchAddressResponse,
		GetCPEResponse,IPS,Low_ExchangeStatus,SMPF,LLU_ALK_BTStop_Unicast,RetainNumber,Check_Nominated_CLI1,Check_Nominated_CLI2,
		NGAGEA_SINO_MILT5,LLU_ALK_BTStop_LLU,NGAv2CLI_new_SI_LE5mbps,NGAALK_new_LE5Mbps,NGAALK_SINO_MIYES,LLU_ALK_BTLive_Unicast,Check_Nominated_CLI_Sales,SPA
	}

	public enum SFTPStubType{
		DERBY,AUTOMATION,DERBY1
	}

	public enum AppoitnmentStubType{
		BT,EVG
	}


	private File StubFileGenerate(String Str_CLI,String Str_TempFile,String Str_OutFileName) throws Exception{
		System.out.println(Str_CLI);

		BufferedReader reader;
		if(Str_TempFile.endsWith(".xml"))
		{
			reader = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(Str_TempFile), "UTF8"));
		}
		else
		{
			File file = new File(Str_TempFile);		
			reader = new BufferedReader(new FileReader(file));
		}
		String line = "", oldtext = "";
		while((line = reader.readLine()) != null)
			oldtext += line + "\r\n";
		reader.close();

		String newtext = oldtext.replaceAll("replaceMe",Str_CLI );
		newtext = newtext.replaceAll("xxxxx", Str_CLI);

		//for IPTV Stubs

		newtext = newtext.replaceAll("MYBOOKEDDATE", Reusables.getdateFormat("yyyy-MM-dd", 15));
		newtext = newtext.replaceAll("M_ALI", NameGenerator.randomNumber("C", 11));
		File newfile = new File(OUTPUTFILE_DIR+Str_OutFileName);
		FileWriter writer = new FileWriter(newfile);
		writer.write(newtext);
		writer.flush();
		writer.close();
		//		System.out.println(newfile);
		return newfile;
	}

	private void StubFileCopy(File File_Source,String Str_Destination) throws Exception{
		if(File_Source.exists()){
			System.out.println("source is there");
		}

		File File_Dest = new File(Str_Destination);
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(File_Source);
			os = new FileOutputStream(File_Dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
				//System.out.println("copying file");
			}
		}catch(Exception e){
			throw e;
		}finally {
			is.close();
			os.close();
			System.out.println("file Copy Complete to "+Str_Destination);
			if(Report!=null){
				Report.fnReportPass("file Copy Complete to "+Str_Destination);
			}
		}
	}

	public void StubFileSFTP(SFTPStubType SftpType,File File_Source,String Str_Destination) throws Exception{

		JSch jsch=new JSch();
		Session session=null;
		Channel channel=null;
		ChannelSftp channelSftp=null;
		try {
			switch(SftpType){
			case AUTOMATION:
				session = jsch.getSession(SFTPSTUB_auth, SFTPSTUB_host, 22);
				session.setPassword(SFTPSTUB_pass);
				break;
			case DERBY:
				session = jsch.getSession(DERBYSTUB_auth, DERBYSTUB_host, 22);
				session.setPassword(DERBYSTUB_pass);
				break;
			case DERBY1:
				session = jsch.getSession(DERBYSTUB_auth1, DERBYSTUB_host, 22);
				session.setPassword(DERBYSTUB_pass1);
			default:
				break;
			}

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();

			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(Str_Destination);
			try{
				channelSftp.rm(File_Source.getName());
			}catch(Exception e){
				// Do Nothing
			}
			channelSftp.put(new FileInputStream(File_Source), File_Source.getName());

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			channelSftp.exit();
			session.disconnect();
			System.out.println("file SFTP Complete to  "+Str_Destination);
			if(Report!=null){
				Report.fnReportPass("file SFTP Complete to "+Str_Destination);
			}
		}

	}

	public void PlaceFile(StubType Stub,String Str_CLI) throws Exception{
		File File_Source = null;
		switch(Stub){
		case Switcher:
			PlaceFile(StubType.NGAv2CLI_new, Str_CLI);
			PlaceFile(StubType.IPTVM, Str_CLI);
			File_Source = StubFileGenerate(Str_CLI,Template_Switcher_NPAC,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_NPAC);
			break;
		case LLU_ALK_Newline:
			File_Source = StubFileGenerate(Str_CLI,Template_LLU_ALK_Newline,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUALK);
			break;
		case SPA:
			File_Source = StubFileGenerate(Str_CLI,Template_SPA,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_SPA);
			break;
		case IPTVU:			
			File_Source = StubFileGenerate(Str_CLI,Template_IPTVU,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY,File_Source, Stub_LLUCLI);

			//			File_Source = StubFileGenerate(Str_CLI,Template_IPTVU,Str_CLI+".txt");
			//			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUCLI);
			break;
		case IPTVM:
			File_Source = StubFileGenerate(Str_CLI,Template_IPTVM,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY,File_Source, Stub_LLUCLI);

			//			File_Source = StubFileGenerate(Str_CLI,Template_IPTVM,Str_CLI+".txt");
			//			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUCLI);
			break;
		case NGAv2CLI:
			File_Source = StubFileGenerate(Str_CLI,Template_NGAv2CLI,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY,File_Source, Stub_NGAv2CLI_new);
			break;
		case NGAv2CLI_new:
			File_Source = StubFileGenerate(Str_CLI,Template_NGAv2CLI_new,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY,File_Source, Stub_NGAv2CLI_new);
			break;
		case IPTVSuspendNew:
			File_Source = StubFileGenerate(Str_CLI,Template_IPTVSuspendStubsNew,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_IPTVSuspendStubs);
			break;
		case IPTVModifyNew:
			File_Source = StubFileGenerate(Str_CLI,Template_IPTVModifyStubsNew,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_IPTVModifyStubsNew);
			break;
		case QS3:
			File_Source = StubFileGenerate(Str_CLI,Template_IPS,Str_CLI+".txt");
			StubFileCopy(File_Source, Stub_IPSCLI+Str_CLI+".txt");

			File_Source = StubFileGenerate(Str_CLI,Template_QS3_V5,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY,  File_Source, Stub_LLUCLI);
			break;
		case Check_CLI_Poratability:
			File_Source = StubFileGenerate(Str_CLI,Template_Check_CLI_Poratability,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY1, File_Source, Stub_Check_CLI_Poratability);
			break;
		case IPTVProvideNew:
			File_Source = StubFileGenerate(Str_CLI,Template_IPTVProvideStubsNew,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_IPTVProvideStubsNew);
			break;
		case Check_Nominated_CLI:
			File_Source = StubFileGenerate(Str_CLI,Template_Check_Nominated_CLI,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_Check_Nominated_CLI);
			break;
		case Check_Nominated_CLI1:
			File_Source = StubFileGenerate(Str_CLI,Template_Check_Nominated_CLI1,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_Check_Nominated_CLI);
			break;
		case Check_Nominated_CLI2:
			File_Source = StubFileGenerate(Str_CLI,Template_Check_Nominated_CLI2,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_Check_Nominated_CLI);
			break;
		case Check_Nominated_CLI_Sales:
			File_Source = StubFileGenerate(Str_CLI,Template_Check_Nominated_CLI_Sales,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_Check_Nominated_CLI);
			break;
		case Working_Error_Response:
			File_Source = StubFileGenerate(Str_CLI,Template_Working_Error_Response,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUCLI);
			break;
		case NPAC_LLU_ALK_BTLive:
			File_Source = StubFileGenerate(Str_CLI,Template_NPAC_LLUALK_BTLive,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUALK);
			break;
		case NPAC_ExchangeStatus:
//			Str_CLI=Str_CLI.replaceAll("\\s+", "");
			File_Source = StubFileGenerate(Str_CLI,Template_NPACExchangeStatus,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_NPAC);
			break;
		case NPAC_LLU_ALK_Newline:
			File_Source = StubFileGenerate(Str_CLI, Template_NPAC_LLU_ALK_Newline, Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_NPAC);
			break;
		case NPAC_LLU_ALK_BTStop:
			File_Source = StubFileGenerate(Str_CLI,Template_NPAC_LLUALK_BTStop,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_NPAC);
			break;
		case IPTVCease:
			File_Source = StubFileGenerate(Str_CLI,Template_IPTVCeaseStubs,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUCLI);
			break;
		case IPTVModify:
			File_Source = StubFileGenerate(Str_CLI,Template_IPTVModifyStubs,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUCLI);
			break;
		case IPTVProvide:
			File_Source = StubFileGenerate(Str_CLI,Template_IPTVProvideStubs,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_IPTVProvideStubs);
			break;
		case IPTVSuspend:
			File_Source = StubFileGenerate(Str_CLI,Template_IPTVSuspendStubs,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_IPTVSuspendStubs);
			break;
		case LLUv5:
			File_Source = StubFileGenerate(Str_CLI,Template_LLUv5,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY,  File_Source, Stub_LLUCLI);
			break;
		case QS4:
			File_Source = StubFileGenerate(Str_CLI,Template_IPS,Str_CLI+".txt");
			StubFileCopy(File_Source, Stub_IPSCLI+Str_CLI+".txt");

			File_Source = StubFileGenerate(Str_CLI,Template_QS3_V5,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY,  File_Source, Stub_LLUCLI);
			break;
		case ExchangeStatus:
			//			File_Source = StubFileGenerate(Str_CLI,Template_ExchangeStatus,Str_CLI+".txt");
			//			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUPostcode);

			File_Source = StubFileGenerate(Str_CLI,Template_ExchangeStatus,Str_CLI+".txt");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUPostcode);
			break;
		case LLU_ALK_BTStop:
			File_Source = StubFileGenerate(Str_CLI,Template_LLUALK_BTStop,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUALK);
		case LLU_ALK_BTLive:
			File_Source = StubFileGenerate(Str_CLI,Template_LLUALK_BTLive,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUALK);
			break;
		case LLU_ALK_BTStop_LLU:
			File_Source = StubFileGenerate(Str_CLI,Template_LLUALK_BTStop_LLU,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUALK);
		case LLU_ALK_BTStop_Unicast:
			File_Source = StubFileGenerate(Str_CLI,Template_LLU_ALK_BTStop_IPTVUnicast,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUALK);
			break;
		case PostCode_GoldAddress:
			break;
		case NGA_ALK:
			File_Source = StubFileGenerate(Str_CLI,Template_NGA_ALK_Live_TRIO14,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY,File_Source, Stub_NGAALK_new);
			break;

		case NGAALK_new:
			File_Source = StubFileGenerate(Str_CLI,Template_NGA_ALK_Live_TRIO14,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY,File_Source, Stub_NGAALK_new);
			break;
		case NGAALK_new_LE5Mbps:
			File_Source = StubFileGenerate(Str_CLI,Template_NGA_ALK_LE5Mbps,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY,File_Source, Stub_NGAALK_new);
			break;

		case NGAALK_new_SpeedLT5:
			File_Source = StubFileGenerate(Str_CLI,Template_NGAALK_new_SpeedLT5,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY,File_Source, Stub_NGAALK_new);
			break;
		case NGAGEA:
			File_Source = StubFileGenerate(Str_CLI,Template_NGAGEA,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY,File_Source, Stub_NGAv2CLI_new);
			break;

		case NGAGEA_SINO_MILT5:
			File_Source = StubFileGenerate(Str_CLI,Template_NGAGEA_SINO_MILT5,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY,File_Source, Stub_NGAv2CLI_new);
			break;
		case CreditCheck_ConditionalAccept:
			File_Source = StubFileGenerate(Str_CLI,Template_CreditCheck_ConditionalAccept,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_CreditCheck);
			break;
		case CreditCheck_Referral:
			File_Source = StubFileGenerate(Str_CLI,Template_CreditCheck_Referral,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_CreditCheck);
			break;
		case CreditCheck_TotalAccept:
			File_Source = StubFileGenerate(Str_CLI,Template_CreditCheck_TotalAccept,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_CreditCheck);
			break;
		case CreitCheck_TotalDecline:
			File_Source = StubFileGenerate(Str_CLI,Template_CreditCheck_TotalDecline,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_CreditCheck);
			break;
		case LLU_ALK_LowSpeed:
			File_Source = StubFileGenerate(Str_CLI,Template_LLU_ALK_LowSpeed,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUALK);
			break;
		case LLU_CPS_LowSpeed:
			File_Source = StubFileGenerate(Str_CLI,Template_LLU_CPS_LowSpeed,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY,  File_Source, Stub_LLUCLI);
			break;
		case LLU_CPS_PORT_RH_Virgin:
			File_Source = StubFileGenerate(Str_CLI,Template_LLU_CPS_PORT_RH_Virgin,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUCLI);
			break;
		case ACErrorResponseVirgin:
			File_Source = StubFileGenerate(Str_CLI,Template_ACErrorResponseVirgin,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUCLI);
			break;
		case ACErrorResponseSky:
			File_Source = StubFileGenerate(Str_CLI,Template_ACErrorResponseSky,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUCLI);
			break;
		case NPAC_ACErrorResponseSky:
			File_Source = StubFileGenerate(Str_CLI,Template_NPAC_ACErrorResponseSKY,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_NPAC);
			break;
		case NPAC_ACErrorResponseVirgin:
			File_Source = StubFileGenerate(Str_CLI,Template_NPAC_ACErrorResponseVirgin,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_NPAC);
			break;
		case LLU_ALK_BTLive_Unicast:
			File_Source = StubFileGenerate(Str_CLI,Template_LLUALK_BTLive_Unicast,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUALK);
			break;
		case MatchAddressResponse:
			File_Source = StubFileGenerate(Str_CLI,Template_MatchAddressResponse,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_MatchAddressResponse);
			break;
		case ACErrorResponseBT:
			File_Source = StubFileGenerate(Str_CLI,Template_ACErrorResponseBT,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUCLI);
			break;
		case GetCPEResponse:
			File_Source = StubFileGenerate(Str_CLI,Template_GetCPEResponse,Str_CLI+"_Resp.txt");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_GetCPEResponsse);
			break;
		case IPS:
			File_Source = StubFileGenerate(Str_CLI,Template_IPS,Str_CLI+".txt");
			StubFileCopy(File_Source, Stub_IPSCLI+Str_CLI+".txt");
			break;
		case Low_ExchangeStatus:
			File_Source = StubFileGenerate(Str_CLI,Template_ExchangeStatus_Low,Str_CLI+"_Resp.xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_LLUALK);
			break;
		case SMPF:
			File_Source = StubFileGenerate(Str_CLI,Template_SMPF,Str_CLI+".txt");
			StubFileCopy(File_Source, Stub_SMPFCLI+Str_CLI+".txt");
			break;
		case RetainNumber:
			File_Source = StubFileGenerate(Str_CLI,Template_Retain_Number,Str_CLI+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_Retain_Number);
			break;	
		default:
			break;
		}
	}

	public int Stub_PostCodeAddress(String Str_Alk,String Str_PostCode,String Str_Qualifier) throws Exception{
		//		File Stub_Postcode = new File(Stub_AddressMatching+Str_PostCode+".txt");
		//		String Str_Target=System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+Str_PostCode+".txt";
		//		StubFileCopy(new File(Template_AddressMatching),Str_Target);
		//		PostcodeGenerate(Str_Target,Str_Alk,Str_PostCode,Str_Qualifier);
		//		StubFileCopy(new File(Str_Target),Stub_AddressMatching+Str_PostCode+".txt");
		Random rand = new Random();
		int Rface = 100 + rand.nextInt((1000 - 1) + 1) + 1;
		String Stub_Postcode =  Stub_AddressMatching+"/"+Str_PostCode+"_Response.xml";
		String Str_Target=System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+Str_PostCode+"_Response.xml";

		GetFile(Stub_Postcode, Str_Target,DERBYSTUB_host,DERBYSTUB_auth,DERBYSTUB_pass);

		PostcodeGenerate_withBuildingNumber(Str_Target,Str_Alk,Str_PostCode,Str_Qualifier,Rface);

		PutFile(Str_Target,Stub_Postcode, DERBYSTUB_host,DERBYSTUB_auth,DERBYSTUB_pass);
		return Rface;
	}

	public void PostcodeGenerate(String Str_Target,String Str_Alk,String Str_PostCode,String Str_Qualifier) throws Exception{
		//String Str_Alk="A"+Str_CLI;
		String line = "", oldtext = "";
		int Int_LineCounter = 0;
		int Int_InsertCounter=0;
		int Int_InsertPosition = 0;
		String Str_Address="";
		File file = new File(Str_Target);
		BufferedReader reader = new BufferedReader(new FileReader(file));

		while((line = reader.readLine()) != null)Int_LineCounter++;
		reader.close();

		Int_InsertPosition=Int_LineCounter - 5;
		if(Str_Qualifier.equalsIgnoreCase("GOLD")){
			Str_Address="<Address> \n\t\t"
					+ "<BuildingNumber>"+Int_InsertPosition+"</BuildingNumber>\n\t\t"
					+ "<Street>Worton Way</Street> \n\t\t"
					+ "<PostTown>ISLEWORTHy</PostTown> \n\t\t"
					+ "<County>Middlesex</County> \n\t\t"
					+ "<PostCode>"+Str_PostCode+"</PostCode> \n\t\t"
					+ "<ALK>"+Str_Alk+"</ALK> \n\t\t"
					+ "<CSSDistrictCode>TH</CSSDistrictCode> \n\t\t"
					+ "<Qualifier>Gold</Qualifier> \n"
					+ "</Address> \n";
		}else{
			Str_Address="<Address> \n\t\t"
					+ "<BuildingNumber>"+Int_InsertPosition+"</BuildingNumber>\n\t\t"
					+ "<Street>Worton Way</Street> \n\t\t"
					+ "<PostTown>ISLEWORTHy</PostTown> \n\t\t"
					+ "<County>Middlesex</County> \n\t\t"
					+ "<PostCode>"+Str_PostCode+"</PostCode> \n\t\t"
					+ "<CSSDistrictCode>TH</CSSDistrictCode> \n\t\t"
					+ "<Qualifier>Silver</Qualifier> \n"
					+ "</Address> \n";
		}
		line="";
		BufferedReader reader1 = new BufferedReader(new FileReader(file));
		while((line = reader1.readLine()) != null){
			oldtext += line + "\r\n";
			if(Int_InsertCounter==Int_InsertPosition){	
				//System.out.println("Matched");
				oldtext += Str_Address + "\n";
			}
			Int_InsertCounter++;
		}
		reader1.close();

		FileWriter writer = new FileWriter(Str_Target);
		writer.write(oldtext);writer.flush();writer.close();
	}

	//	public void Stub_PostCodeAddress_withBuildingNumber(String Str_Alk,String Str_PostCode,String Str_Qualifier,int BuildingNumber) throws Exception{
	//		File Stub_Postcode = new File(Stub_AddressMatching+Str_PostCode+".txt");
	//		String Str_Target=System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+Str_PostCode+".txt";
	//
	//		//StubFileCopy(new File(Template_AddressMatching),Str_Target);
	//		StubFileCopy(new File(Stub_AddressMatching+Str_PostCode+".txt"),Str_Target);
	//
	//		PostcodeGenerate_withBuildingNumber(Str_Target,Str_Alk,Str_PostCode,Str_Qualifier,BuildingNumber);
	//		//System.out.println(Stub_AddressMatching+Str_PostCode+".txt");
	//		StubFileCopy(new File(Str_Target),Stub_AddressMatching+Str_PostCode+".txt");
	//		//System.out.println(Stub_AddressMatching+Str_PostCode+".txt");
	//	}
	public void Stub_PostCodeAddress_withBuildingNumber(String Str_Alk,String Str_PostCode,String Str_Qualifier,int BuildingNumber) throws Exception{
		String Stub_Postcode =  Stub_AddressMatchingNew+"/"+Str_PostCode+"_Response.xml";
		String Str_Target=System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+Str_PostCode+"_Response.xml";

		GetFile(Stub_Postcode, Str_Target,DERBYSTUB_host,DERBYSTUB_auth,DERBYSTUB_pass);

		PostcodeGenerate_withBuildingNumber(Str_Target,Str_Alk,Str_PostCode,Str_Qualifier,BuildingNumber);

		PutFile(Str_Target,Stub_Postcode, DERBYSTUB_host,DERBYSTUB_auth,DERBYSTUB_pass);

	}
	public void Stub_IssueAddressKey(String Str_Alk,String Str_PostCode,String Str_Qualifier,int BuildingNumber) throws Exception{

		//File Stub_Postcode = new File(Stub_IssueAddressKey+Str_PostCode+".txt");
		String Str_Target=System.getProperty("user.dir")+"\\ProvisioningUpdates\\"+Str_PostCode+".txt";
		StubFileCopy(new File(Template_IssueAddressKey),Str_Target);

		IssueAddressKeyGenerate(Str_Target,Str_Alk,Str_PostCode,Str_Qualifier,BuildingNumber);
		StubFileCopy(new File(Str_Target),Stub_IssueAddressKey+Str_PostCode+".txt");
	} 

	public void PostcodeGenerate_withBuildingNumber(String Str_Target,String Str_Alk,String Str_PostCode,String Str_Qualifier,int BuildingNumber) throws Exception{
		//String Str_Alk="A"+Str_CLI;
		String line = "", oldtext = "";
		int Int_LineCounter = 0;
		int Int_InsertCounter=0;
		int Int_InsertPosition = 0;
		String Str_Address="";
		File file = new File(Str_Target);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		while((line = reader.readLine()) != null)Int_LineCounter++;
		reader.close();
		Int_InsertPosition=Int_LineCounter - 3;

		Str_Address="\t\t\t<ns0:PostCodeValidatedSite>"+ "\r\n"
				+ "\t\t\t\t<ns0:Address>"+ "\r\n"
				+ "\t\t\t\t\t<ns0:ALK>"+Str_Alk+"</ns0:ALK>"+ "\r\n"
				+ "\t\t\t\t\t<ns0:BuildingName>White City</ns0:BuildingName>"+ "\r\n"
				+ "\t\t\t\t\t<ns0:BuildingNumber>"+BuildingNumber+"</ns0:BuildingNumber>"+ "\r\n"
				+ "\t\t\t\t\t<ns0:CSSDistrictCode>LC</ns0:CSSDistrictCode>"+ "\r\n"
				+ "\t\t\t\t\t<ns0:ExchangeCode>LCROC</ns0:ExchangeCode>"+ "\r\n"
				+ "\t\t\t\t\t<ns0:IsPostCodeValid>true</ns0:IsPostCodeValid>"+ "\r\n"
				+ "\t\t\t\t\t<ns0:Locality>IRLAM</ns0:Locality>"+ "\r\n"
				+ "\t\t\t\t\t<ns0:OrganisationName>E TUPLING SON LTD</ns0:OrganisationName>"+ "\r\n"
				+ "\t\t\t\t\t<ns0:PostCode>"+Str_PostCode+"</ns0:PostCode>"+ "\r\n"
				+ "\t\t\t\t\t<ns0:PostTown>MANCHESTER</ns0:PostTown>"+ "\r\n"
				+ "\t\t\t\t\t<ns0:Qualifier>Gold</ns0:Qualifier>"+ "\r\n"
				+ "\t\t\t\t\t<ns0:Street>SIEMENS ROAD</ns0:Street>"+ "\r\n"
				+ "\t\t\t\t\t<ns0:SubBuilding>2</ns0:SubBuilding>"+ "\r\n"
				+ "\t\t\t\t\t<ns0:Technologies>"+ "\r\n"
				+ "\t\t\t\t\t\t<ns0:Technology>"+ "\r\n"
				+ "\t\t\t\t\t\t\t<ns0:IsAssociated>false</ns0:IsAssociated>"+ "\r\n"
				+ "\t\t\t\t\t\t\t<ns0:IsRestricted>false</ns0:IsRestricted>"+ "\r\n"
				+ "\t\t\t\t\t\t\t<ns0:Name>TTG</ns0:Name>"+ "\r\n"
				+ "\t\t\t\t\t\t</ns0:Technology>"+ "\r\n"
				+ "\t\t\t\t\t</ns0:Technologies>"+ "\r\n"
				+ "\t\t\t\t</ns0:Address>"+ "\r\n"
				+ "\t\t\t</ns0:PostCodeValidatedSite>\n";
		/*if(Str_Qualifier.equalsIgnoreCase("GOLD")){
			System.out.println("in Gold");
			Str_Address="\t<Address>"+ "\r\n"
					+ "\t<BuildingName>"+BuildingNumber+"</BuildingName>"+ "\r\n"
					+ "\t<Street>White City</Street>"+ "\r\n"
					+ "\t<PostTown>White City</PostTown>"+ "\r\n"
					+ "\t<County>London</County>"+ "\r\n"
					+ "\t<PostCode>"+Str_PostCode+"</PostCode>"+ "\r\n"
					+"\t<ALK>"+Str_Alk+"</ALK>"+ "\r\n"
					+ "\t<CSSDistrictCode>LC</CSSDistrictCode>"+ "\r\n"
					+ "\t<ExchangeCode>LCROC</ExchangeCode>"+ "\r\n"
					+ "\t<Qualifier>Gold</Qualifier>"+ "\r\n"
					+ "\t</Address>\r\n";
		}else{

			Str_Address="\t<Address>"+ "\r\n"
					+ "\t<BuildingName>"+BuildingNumber+"</BuildingName>"+ "\r\n"
					+ "\t<Street>White City</Street>"+ "\r\n"
					+ "\t<PostTown>White City</PostTown>"+ "\r\n"
					+ "\t<County>London</County>"+ "\r\n"
					+ "\t<PostCode>"+Str_PostCode+"</PostCode>"+ "\r\n"
					+ "\t<CSSDistrictCode>LC</CSSDistrictCode>"+ "\r\n"
					+ "\t<ExchangeCode>LCROC</ExchangeCode>"+ "\r\n"
					+ "\t<Qualifier>Silver</Qualifier>"+ "\r\n"
					+ "\t</Address>"+ "\r\n";
		}*/
		System.out.println(Str_Address);

		line="";
		BufferedReader reader1 = new BufferedReader(new FileReader(file));
		while((line = reader1.readLine()) != null){
			if(Int_InsertCounter==Int_InsertPosition){	
				//System.out.println("Matched");
				oldtext += Str_Address;
			}
			oldtext += line + "\r\n";
			Int_InsertCounter++;
		}
		reader1.close();

		FileWriter writer = new FileWriter(Str_Target);
		writer.write(oldtext);writer.flush();writer.close();
	}
	public void IssueAddressKeyGenerate(String Str_Target,String Str_Alk,String Str_PostCode,String Str_Qualifier,int BuildingNumber) throws Exception{
		//String Str_Alk="A"+Str_CLI;

		String line = "", oldtext = "";
		int Int_LineCounter = 0;
		int Int_InsertCounter=0;
		int Int_InsertPosition = 0;
		String Str_Address="";
		File file = new File(Str_Target);
		BufferedReader reader = new BufferedReader(new FileReader(file));

		while((line = reader.readLine()) != null)Int_LineCounter++;
		reader.close();

		Int_InsertPosition=Int_LineCounter - 5;
		Str_Address="<BuildingNumber>"+BuildingNumber+"</BuildingNumber>\n\t\t"
				+ "<Street>Worton Way</Street>\n\t\t "
				+ "<Locality>Twickenham</Locality>"
				+ "<PostTown>White City</PostTown>"
				+ "<PostCode>"+Str_PostCode+"</PostCode>"
				+ "<ALK>"+Str_Alk+"</ALK>"
				+ "<CSSDistrictCode>SS</CSSDistrictCode>"
				+ "<ExchangeCode>LCROC</ExchangeCode>"
				+ "<Qualifier>"+Str_Qualifier+"</Qualifier>";

		line="";
		BufferedReader reader1 = new BufferedReader(new FileReader(file));
		while((line = reader1.readLine()) != null){
			oldtext += line + "\r\n";
			if(Int_InsertCounter==Int_InsertPosition){	
				//System.out.println("Matched");
				oldtext += Str_Address + "\n";
			}
			Int_InsertCounter++;
		}
		reader1.close();

		FileWriter writer = new FileWriter(Str_Target);
		writer.write(oldtext);writer.flush();writer.close();
	}


	public void AppointmentStub(AppoitnmentStubType AppStubType, boolean SlotAvailability ) throws Exception{
		String DATE = null;
		String SLOT = null;
		File File_Source = null;
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
		switch(AppStubType){
		case BT:

			String BT1 = 	"<?xml version=\"1.0\" encoding=\"utf-16\"?>"
					+ "\n<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
					+ "\n<soap:Body>"
					+ "\n<GetAvailableAppointmentsResponse xmlns=\"https://webservices.opalonline.co.uk/AppointingService\">"
					+ "\n<GetAvailableAppointmentsResult>"
					+ "\n<Status>"
					+ "\n<HasErrors>false</HasErrors>"
					+ "\n</Status>"
					+ "<ListOfAppointments>";

			String BT2 = "\n</ListOfAppointments>"
					+ "\n</GetAvailableAppointmentsResult>"
					+ "\n</GetAvailableAppointmentsResponse>"
					+ "\n</soap:Body>"
					+ "\n</soap:Envelope>";

			for(int i=0;i<90;i++){
				Calendar cs = Calendar.getInstance();
				cs.add(Calendar.DATE, i);
				int dayOfWeek = cs.get(Calendar.DAY_OF_WEEK);
				DATE = formatter.format(cs.getTime());
				if((dayOfWeek==7)||(dayOfWeek==1)){
				}else{
					BT1 = BT1 + "\n<AppointmentSlotInfo><AppointmentDate>"+DATE+"T10:20:00-05:00</AppointmentDate><AppointmentTimeSlot>AM</AppointmentTimeSlot></AppointmentSlotInfo>";
					BT1 = BT1 + "\n<AppointmentSlotInfo><AppointmentDate>"+DATE+"T10:20:00-05:00</AppointmentDate><AppointmentTimeSlot>PM</AppointmentTimeSlot></AppointmentSlotInfo>";
					BT1 = BT1 + "\n<AppointmentSlotInfo><AppointmentDate>"+DATE+"T10:20:00-05:00</AppointmentDate><AppointmentTimeSlot>EV</AppointmentTimeSlot></AppointmentSlotInfo>";

				}
			}		
			BT1 = BT1 + BT2;

			FileWriter writer1 = new FileWriter(OUTPUTFILE_DIR+BTAPPOINTMENT_FILE_NAME+".txt");
			writer1.write(BT1);writer1.flush();writer1.close();
			File_Source = new File(OUTPUTFILE_DIR+BTAPPOINTMENT_FILE_NAME+".txt");
			StubFileCopy(File_Source, Stub_BT_APPOINTMENT+BTAPPOINTMENT_FILE_NAME+".txt");

			break;

		case EVG:

			String EVG1 = 	"<AvailabilityResponse>"
					+ "\n<Response> "
					+ "\n<Code>2147483647</Code> "
					+ "\n<Message>String content</Message> "
					+ "\n</Response> "
					+ "\n<Success>true</Success>";
			String EVG2 = "\n</AvailabilityResponse>";

			if(SlotAvailability){
				SLOT = "3000";
			}else{
				SLOT="0";
			}
			for(int i=0;i<90;i++){
				Calendar cs = Calendar.getInstance();
				cs.add(Calendar.DATE, i);
				int dayOfWeek = cs.get(Calendar.DAY_OF_WEEK);
				DATE = formatter.format(cs.getTime());
				if((dayOfWeek==7)||(dayOfWeek==1)){
				}else{
					EVG1 = EVG1 + "\n<Slot VisitDate=\""+DATE+"\" VisitWindowStart=\"09:00:00.000\" VisitWindowEnd=\"11:00:00.000\" Availability=\"High\" EngineerCount=\""+SLOT+"\"/>";
					EVG1 = EVG1 + "\n<Slot VisitDate=\""+DATE+"\" VisitWindowStart=\"15:00:00.000\" VisitWindowEnd=\"18:00:00.000\" Availability=\"High\" EngineerCount=\""+SLOT+"\"/>";
				}
			}		
			EVG1 = EVG1 +EVG2;
			FileWriter writer = new FileWriter(OUTPUTFILE_DIR+EVGAPPOINTMENT_FILE_NAME+".xml");
			writer.write(EVG1);writer.flush();writer.close();
			File_Source = new File(OUTPUTFILE_DIR+EVGAPPOINTMENT_FILE_NAME+".xml");
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_EVG_Query_Slot_Qube);
			StubFileSFTP(SFTPStubType.DERBY, File_Source, Stub_EVG_Query_Slot_Qube1);
			break;

		default:
			break;
		}
	}
	public void  Fetch_RC_DB_Details(){

		URL url;
		HttpURLConnection connection = null;  
		StringBuffer response = null;
		String responseString= null;
		String RCdetails = null;

		try{
			url = new URL(EVG_RC_URL);

			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			//Send request
			DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
			wr.writeBytes ("Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
					+ "Accept-Encoding:gzip,deflate,sdch"
					+ "Accept-Language:en-US,en;q=0.8"
					+ "Cache-Control:max-age=0"
					+ "Connection:keep-alive"
					+ "Host:10.155.38.169");
			wr.flush ();
			wr.close ();
			//Get Response    
			InputStream is ;
			if(connection.getResponseCode()<=400){
				is=connection.getInputStream();

			}else{
				is = connection.getErrorStream();
			} 
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			response = new StringBuffer(); 
			while((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			responseString = response.toString();
		} catch (Exception e) {
			//	return null;
			e.printStackTrace();
		} finally {
			if(connection != null) {
				connection.disconnect(); 
			}
		}	
		String html =responseString ;
		Document doc = Jsoup.parse(html);
		Elements tables = doc.select("table");
		Elements myTdsstudent = tables.select("tr");
		int j = 0;
		for (Element myTdsstudentIterator: myTdsstudent) {
			RCdetails = myTdsstudentIterator.select("td:eq(2)").text();
			if(myTdsstudentIterator.select("td:eq(3)").text().contains(LoadEnvironment.ENV)){
				//  System.out.println(myTdsstudentIterator.select("td:eq(2)").text());
				RCdetails = myTdsstudentIterator.select("td:eq(2)").text();
				if(j>0){
					if((RCdetails.contains("evg"))||(RCdetails.contains("EVG")))
						break;
				}else{
					j++;
				}
			}
		}
		String [] details = new String[3];
		int i=0;
		for(String RC : RCdetails.split("/")){
			details[i]=RC;
			i++;
		}
		//System.out.println("EVG Properties set to: EVG IP: PORT "+details[0]+" EVG DBNAME "+details[1]+" DBUSERNAME and DBPASSWORD "+details[2]);
		if((RCdetails.contains("evg"))||(RCdetails.contains("EVG"))){
			LoadEnvironment.EVG_DBPORT = details[0].substring(details[0].indexOf(':')+1);
			LoadEnvironment.EVG_DBIP =details[0].substring(0, details[0].indexOf(':'));
			LoadEnvironment.EVG_DBNAME = details[1];
			if(details[2]==null){
				LoadEnvironment.EVG_DBUSERNAME="evg";	
				LoadEnvironment.EVG_DBPASSWORD="evg";
			}else{
				LoadEnvironment.EVG_DBUSERNAME = details[2];
				LoadEnvironment.EVG_DBPASSWORD = details[2];
			}
			System.out.println("EVG Properties set to: EVG IP - > "+LoadEnvironment.EVG_DBIP+" PORT - > "+LoadEnvironment.EVG_DBPORT+" EVG DBNAME - > "+LoadEnvironment.EVG_DBNAME+" DBUSERNAME - > "+LoadEnvironment.EVG_DBUSERNAME+" DBPASSWORD - >"+LoadEnvironment.EVG_DBPASSWORD);
		}else{
			System.out.println("NO EVG Instances Pointed to "+LoadEnvironment.ENV);
		}
	}


	public void GetFile(String Str_Source,String File_Dest,String BW_ServerIP,String BW_UserName,String BW_Password) throws Exception{

		JSch jsch=new JSch();
		Session session=null;
		Channel channel=null;
		ChannelSftp channelSftp=null;
		try {

			session = jsch.getSession(BW_UserName, BW_ServerIP, 22);
			session.setPassword(BW_Password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			System.out.println("------------session connected----------------");
			channel = session.openChannel("sftp");
			channel.connect();
			System.out.println("------------channel connected----------------");
			channelSftp = (ChannelSftp) channel;
			channelSftp.get(Str_Source,File_Dest);

		}finally{
			channelSftp.exit();
			session.disconnect();
			System.out.println("file SFTP Complete to  "+File_Dest);

		}

	}

	public void PutFile(String Str_Source,String File_Dest,String BW_ServerIP,String BW_UserName,String BW_Password) throws Exception{
		//		deleteFileSFTP(SFTPStubType.DERBY, File_Name, File_Dest);
		JSch jsch=new JSch();
		Session session=null;
		Channel channel=null;
		ChannelSftp channelSftp=null;
		try {

			session = jsch.getSession(BW_UserName, BW_ServerIP, 22);
			session.setPassword(BW_Password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			System.out.println("------------session Disconnected----------------");
			channel = session.openChannel("sftp");
			channel.connect();
			System.out.println("------------session Disconnected----------------");
			channelSftp = (ChannelSftp) channel;
			channelSftp.put(Str_Source,File_Dest,ChannelSftp.OVERWRITE);


		}finally{
			channelSftp.exit();
			session.disconnect();
			System.out.println("file SFTP Complete to  "+File_Dest);

		}

	}

}

