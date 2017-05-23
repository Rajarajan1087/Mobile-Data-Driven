package com.SharedModules;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.Engine.LoadEnvironment;
import com.Engine.Reporter;
import com.WebActions.WebActions;

public class CopyOfNewDatabase{
	public Reporter Report;
	public CopyOfNewDatabase(Reporter report) {
		Report=report;
	}
	private String Str_ErrorCLI="";
	private static Connection con=null;
	
	public enum DBNames{
		CRM(LoadEnvironment.CRM_DBIP,LoadEnvironment.CRM_DBPORT,LoadEnvironment.CRM_DBNAME,"",LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD),
		SV(LoadEnvironment.SV_DBIP,LoadEnvironment.SV_DBPORT,LoadEnvironment.SV_DBNAME,LoadEnvironment.SV_DBSCHEMA,LoadEnvironment.SV_DBUSERNAME,LoadEnvironment.SV_DBPASSWORD),
		OMP(LoadEnvironment.OMP_DBIP,LoadEnvironment.OMP_DBPORT,LoadEnvironment.OMP_DBNAME,LoadEnvironment.OMP_DBSCHEMA,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD),
		EVG(LoadEnvironment.EVG_DBIP,LoadEnvironment.EVG_DBPORT,LoadEnvironment.EVG_DBNAME,"",LoadEnvironment.EVG_DBUSERNAME,LoadEnvironment.EVG_DBPASSWORD);
		
		private final String DB_IP;
	    private final String DB_PORT;
	    private final String DB_Name;
	    private final String DB_SCHEMA;
	    private final String DB_UName;
	    private final String DB_Password;
	    
	    private DBNames(String DB_IP,String DB_PORT,String DB_Name,String DB_SCHEMA,String DB_UName,String DB_Password) {
		        this.DB_IP = DB_IP;
		        this.DB_PORT = DB_PORT;
		        this.DB_Name = DB_Name; 
		        this.DB_SCHEMA = DB_SCHEMA;
		        this.DB_UName = DB_UName;
		        this.DB_Password = DB_Password;
		    }
		    
		    public String[] getDBDetails() {
		    	String[] DB_Details={DB_IP,DB_PORT,DB_Name,DB_SCHEMA,DB_UName,DB_Password};
		        return DB_Details;
		    }
	}
	public enum searchByData{
		Proposition,Bundle,Discount,blank
	}

	public String getDataNEW(String packageID, searchByData search, String searchValue, boolean inContract, boolean creditClassCheck,String... Str_CLI) throws Exception {
		//WebActions WA=new WebActions(null,Report);
//		WA.TimeRunner();
		String CLI_OMP = null;
		String CLI_CRM = "";
		String CLI_SV = ",";
		int limit=10;
		
		searchValue=searchValue.replace(",", "','");
		packageID=packageID.replace(",", "','");
		
/***************************** Querying CRM DB **************************/
		
		int j=0;
		while(j<3)
		{
			if(j>0)
			{
				System.out.println("Retreiving Data from CRM - - TRIAL "+(j+1));
			}
			CLI_CRM=getCLI_CRM(packageID,search,searchValue,inContract);
			if(!CLI_CRM.equalsIgnoreCase(""))
			{
				if(j>0)
				{
					System.out.println("Got Data from CRM in TRIAL "+(j+1));
				}
				j=3;
			}
			j++;
		}
		if(!CLI_CRM.equalsIgnoreCase(""))
		{
			for(int i=0;i<Str_CLI.length;i++)
			{
				CLI_CRM=removeCLI(CLI_CRM,Str_CLI[i]);
			}
//			CLI_CRM=shuffleCLI(CLI_CRM);
//			System.out.println(CLI_CRM);
		}
		else{
			Report.fnReportFailAndTerminateTest("Fail", "NO DATA FROM CRM");
		}
//		WA.TimeRunner();WA.TimeRunner();
/***************************** Querying OMP DB **************************/
		
		j=0;
		while(j<3)
		{
			if(j>0)
			{
				System.out.println("Retreiving Data from OMP - - TRIAL "+(j+1));
			}
			CLI_OMP = getCLI_OMP(CLI_CRM);
			if(CLI_OMP!=null)
			{
				if(j>0)
				{
					System.out.println("Got Data from OMP in TRIAL "+(j+1));
				}
				j=3;
			}
			else
			{
//				CLI_CRM=shuffleCLI(CLI_CRM);
				j++;
			}
		}
		if(CLI_OMP!=null)
		{
//			CLI_OMP=shuffleCLI(CLI_OMP);
//			System.out.println(CLI_OMP);
		}
		else{
//			System.out.println("No data from OMP");
			Report.fnReportFailAndTerminateTest("Fail", "NO DATA FROM OMP");
		}
//		WA.TimeRunner();WA.TimeRunner();
/***************************** Querying SV DB **************************/
		
		j=0;
		while(j<3)
		{
			if(j>0)
			{
				System.out.println("Retreiving Data from SV - - TRIAL "+(j+1));
			}
			CLI_SV = getCLI_SV(CLI_OMP,limit,creditClassCheck);
			if(!CLI_SV.equalsIgnoreCase(","))
			{
				if(j>0)
				{
					System.out.println("Got Data from SV in TRIAL "+(j+1));
				}
				j=3;
			}
			else
			{
//				CLI_OMP=shuffleCLI(CLI_OMP);
				j++;
			}
		}
		if(!CLI_SV.equalsIgnoreCase(","))
		{
//			System.out.println(CLI_SV);
		}
		else{
//			System.out.println("No data from SV");
			Report.fnReportFailAndTerminateTest("Fail", "NO DATA FROM SV");
		}
	//	WA.TimeRunner();
		return CLI_SV;
	} 
	/*public String getDataNEW(String packageID, searchByData search, String searchValue, boolean inContract, boolean creditClassCheck,String... Str_CLI) throws Exception {
		String CLI_OMP = null;
		String CLI_CRM = "";
		String CLI_SV = ",";
		int limit=1;
		searchValue=searchValue.replace(",", "','");
		packageID=packageID.replace(",", "','");
		//CLI_CRM=Data_GetCLI_CRM(packageID,search,searchValue,inContract);
		int j=0;
		while(j<3)
		{
			System.out.println("Retreiving Data from CRM - - TRIAL "+(j+1));
			CLI_CRM=getCLI_CRM(packageID,search,searchValue,inContract);
			if(!CLI_CRM.equalsIgnoreCase(""))
			{
				System.out.println("Got Data from CRM in TRIAL "+(j+1));
				j=3;
			}
			j++;
		}
		if(!CLI_CRM.equalsIgnoreCase(""))
		{
			for(int i=0;i<Str_CLI.length;i++)
			{
				CLI_CRM=removeCLI(CLI_CRM,Str_CLI[i]);
				System.out.println(CLI_CRM);
			}
			CLI_CRM=shuffleCLI(CLI_CRM);
			System.out.println(CLI_CRM);
		}
		else{
			Report.fnReportFailAndTerminateTest("Fail", "NO DATA FROM CRM");
		}

		j=0;
		while(j<3)
		{
			System.out.println("Retreiving Data from OMP - - TRIAL "+(j+1));
			CLI_OMP = getCLI_OMP(CLI_CRM);
			if(CLI_OMP!=null)
			{
				System.out.println("Got Data from OMP in TRIAL "+(j+1));
				j=3;
			}
			else
			{
				CLI_CRM=shuffleCLI(CLI_CRM);
				j++;
			}
		}
		if(CLI_OMP!=null)
		{
			CLI_OMP=shuffleCLI(CLI_OMP);
			System.out.println(CLI_OMP);
		}//,01614860859,01403266914,02920851226,01505842606,01162387453,01912210602,01924892771,01536510065,01952252751,01422244582,01803842044,01243574019,01622758176,01964543915,01829260703,01302711268,01908319368,01507600391,01226745807,01616436886,01913886737,01492547115,02920591828,01246432655,01395568627,01375675727,01580715291,01483578227,02085206735,01926400459,01223263723,01485544161,02077041117,01922419193,01480463946,01752261278,01495774145,01772430008,02087767914,01422885284,01932873209,02087852270,01179605040,01620823835,02086801449,01458832632,01270569744,02084462339,01555664804,02085989336,01316636907,01922610898,01514243313,01603611995,01473830359,01872279263,01902734970,01614800374,01752227783,01473682672,01419541688,01491682111,01787475128,01465713828,01283790647,01044190893,01509672152,01724732810,01443773487,02034174408,01280841751,01189884392,01732823336,01489893221,01706221048,01684593466,02076371688,01316540094,01743790982,01259218751,01563525659,01282618089,01343541338,01772717600,0146076214,01706216642,0140446949,02083634197,01229466095,01442211373,01639637636,01628605592,01915126453,01733204569,01513555556,01652636715,02089594290,01275472201,01576202776,01444628721,01284766232,01224313342,01373826124,01474566515,02380554980,01254885650,01823274660,01376322429,0172669232,02084445563,01372726554,01179104733,01536269256,01516539631,01708225302,01623628592,01502740114,01535658420,01224743600,01480464324,01299403064,01983760244,01902340224,01926419833,01269842102,01513571688,01179734611,01244543938,01495750560,02084559391,01416392525,01330822844,01527832884,01273586031,01614318134,01344488638,01207270730,01619806502,01302856365,01617362140,01505613384,01302727264,01484681392,01924894667,01706868765,01279435949,01132865298,01773821633,01225466358,01937587400,01227277330,01452414500,01343843233,01162772576,01634253689,01162717773,01932848649,02838832476,01224478815,01529461612,01873850938,01945772849,02083686956,01765690945,01622859325,01332671678,02085614972,02083645800,01604378312,01634710415,01458447382,01214201828,01223846399,01932560775,01413323428,01132374089,01256353629,01908610541,01270625980,01495764758,01584810707,01226753250,01773718284,01736757793,01246411110,01579342026,01283534887,01342713854,01428723312,01535631454,02088741774,01388762171,01889579700,01473313133,01535647384,02072633635,01213548767,01953452888,01323430441,01273731759,01226714915,01743364062,01442905298,01303812808,01442823748,02083007431,01443431659,01924438420,01744731938,01413284061,01273478285,01253733654,01752404888,01865515426,01858469061,01322862706,01344779971,01873811752,01618653977,01913845157,01225895689,01417705078,01992471702,01509234124,01483426071,01217534094,01303257786,01142577191,01162779884,0152466388,01962714346,01483763060,01932785093,01913734308,01233732362,02089086048,02077003800,0129827041,01325281440,01934624418,01614785574,02086726818,01582593321,01913780694,01295669168,01244678722,01275332138,01684574825,01502581632,01332553287,01522810602,02088807273,01263825332,01752702853,01935428760,02086585063,01159305445,02088539054,01422240725,01915369681,01567289345,01444454303,02380262291,01382221969,02087734911,01472812773,02089406022,02820741149,02085710228,02085777575,01228818754,02380644812,01422881974,01637872192,01928567005,02085921372,01603305777,01874625280,01622730227,01963362374,02077363825,02086690489,01912688981,01702203367,01935850645,01745339309,02075812619,01803843944,01543676778,01425474173,01283716693,02036095791,01400281597,01142645196,02085532056,02085752543,01621773214,01207281622,01515237298,01524825450,01260252968,01209211080,01536628771,01553776424,01924872424,01189820021,01555812515,01424214257,01782642066,02085024174,01924263835,01322272244,01986111972,01702545207,01416387120,01785256992,01708764882,01233612327,01573224457,01989567451,01162740183,02076070399,01226747794,01179554424,01745339458,01214405408,02083682957,01723354735,01407741281,01226281281,02085955787,01903237995,01262671787,02920861060,01253624316,01446775958,01277652554,02838329509,01202854625,01329845421,01612271730,02870320952,01217474395,01612244726,01978264992,01912578329,01509880087,01923826870,01422883803,01905353820,01284727725,02084714486,01067583025,01617907877,01200427156,01207591808,01424722752,01244534249,01284700417,02897510106,01977515204,01214454722,01952670570,02871883126,01217433404,01803522661,02877764915,01416441990,01548561514,01516775837,01536722081,01202604357,01227280926,01636679760,01908550191,01493780777,01726843770,0172667138,01928566309,01522704449,02476362187,01284764195,01246861337,0190524554,01582597675,01384873102,01902791646,01253735008,01803671866,02380734707,01708458977,01200428742,01215804713,01798874770,01443431562,01636893356,01480472841,01626859049,01246276758,01472872324,01983873809,01333425249,01303240857,02088531933,01983866189,01269592325,01342326838,01502508120,01226725467,02086468185,02825892023,02084284942,01582566859,01204885700,01623516269,01572755197,02088430306,01274869245,01803553509,01933350976,01189585366,01926930682,01425674540,01226766527,01246416740,01283222017,01792579130,01226790712,01305788889,01268540152,01903776620,01558823689,01217704151,01249657728,02076364150,01418101204,01246410732,01708553495,0129720481,01215576648,01782633537,01382580527,014434765,02890452245,01275392118,01283740669,01381620100,01419424154,01173291592,01304842585,01670364672,01505612888,01952240730,02084290792,01914134519,01670518388,01935850602,01902454858,01553767797,01706230592,01925221268,01923225772,02088962663,01325319083,02920228332,01514932785,02032223021,01934876831,01142685387,02077368101,01912677471,01704214201,01413377078,02089791269,01422845628,01189789623,01332841518,01697321530,01744757812,01617907554,01895631018,01354656411,01708753456,01695422841,01695572485,02077350118,01388663324,01903742804,01323895711,01522702008,01579384134,01432273305,02088934190,01249822889,01606594554,01805625898,01202822569,01427877960,02380735557,02089927021,01908394765,02392384793,01616523824,01732780848,01904799025,01202721872,02380321249,01407765723,01489584691,01758613382,02885439210,02074392527,01900815937,01908372053,01923239578,01793772869,01625430725,02078400535,01889586225,01273582313,01226792774,01865244819
		j=0;
		while(j<3)
		{
			System.out.println("Retreiving Data from SV - - TRIAL "+(j+1));
			CLI_SV = getCLI_SV(CLI_OMP,limit);
			if(!CLI_SV.equalsIgnoreCase(","))
			{
				System.out.println("Got Data from SV in TRIAL "+(j+1));
				j=3;
			}
			else
			{
				CLI_OMP=shuffleCLI(CLI_OMP);
				j++;
			}
		}
		if(!CLI_SV.equalsIgnoreCase(","))
		{
			System.out.println(CLI_SV);
		}
		else{
			System.out.println("No data from SV");
			Report.fnReportFailAndTerminateTest("Fail", "NO DATA FROM SV");
		}
		if(creditClassCheck)
		{
			Update_CreditClassCreditTier(CLI_SV.substring(CLI_SV.indexOf(",")),"E1","1");
		}
		return CLI_SV;
	} */
/*	public String getDataNEW_Modified(String packageID, searchByData search, String searchValue, boolean inContract, boolean creditClassCheck,String Str_NOT_CLI) {

		String CLI_OMP = null;
		String CLI_CRM = null;
		String CLI_final = null;
		
		CLI_CRM=Data_GetCLI_CRM(packageID,search,searchValue,inContract);
		
		if(CLI_CRM!=null){
			CLI_OMP  = getCLI_OMP(CLI_CRM);
		}else{
			System.out.println("NO DATA FROM OMP");
		}
		if(CLI_OMP != null){
			System.out.println("CLIs From OMP_DB for "+packageID+" are "+CLI_OMP);
			CLI_final = getCLI_fineTune(CLI_OMP,inContract,creditClassCheck);
		}else{
			System.out.println("NO DATA to fine tune");
		}
		return CLI_final;
		
	}*/
	public String[] getCLI_fineTune_Modified(String cLI_OMP, boolean inContract,boolean creditClassCheck)
	{
		String AccNo = "";
		String query;
		ResultSet rs = null;
		PreparedStatement stm =null;
		try{
			String ConnectionURL = "jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":" +LoadEnvironment.CRM_DBNAME;
			//	System.out.println(ConnectionURL);
			con = ConnectionFactory.createConnection(ConnectionURL,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
//			System.out.println("Connection established");
			cLI_OMP=cLI_OMP.replaceAll(",", "','");
			query ="select * from(select CUSTOMERNUMBER from CBLOWNER.v_customer_search where CLI in ('"+cLI_OMP+"'))" +
					" minus" +
					" select CUSTOMERNUMBER from CBLOWNER.bar" +
					" minus" +
					" select CUSTOMERNUMBER from CBLOWNER.fraud" +
					" intersect" +
					" select customernumber from CBLOWNER.v_customer_search group by customernumber having count(*)=1";
			stm=con.prepareStatement(query);
//			System.out.println(query);
			rs=stm.executeQuery();
			int count = 0;
			while(rs.next()){
//				System.out.println(count);
				AccNo=AccNo+rs.getString(1);
				AccNo=AccNo+"','";
				if(count==998)
					break;
				count++;

			}
			AccNo=AccNo.replaceAll("','$", "");
//			System.out.println(AccNo);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
		String[] returnData={AccNo,cLI_OMP};
		return returnData;
	}
	

	/*private String Data_GetCLI_CRM(String packageID, searchByData search,String searchValue,boolean inContract) throws Exception
	{
		String CLI_CRM0 = null;
		String CLI_CRM1 = null;
		String[] SearchValue;
		String Result="";
		boolean Boolean_Search=true;
		boolean Boolean_Search_NOT=false;
		if(searchValue!=""&&search.name()!="blank")
		{
			SearchValue=Data_SearchValue(searchValue);
			SearchValue[0]=SearchValue[0].replace(",", "','");
			SearchValue[1]=SearchValue[1].replace(",", "','");
			packageID=packageID.replace(",", "','");
			if(SearchValue[0].equalsIgnoreCase(""))
			{
				Boolean_Search=false;
			}
			else
			{
				CLI_CRM0=getCLI_CRM(packageID,search,SearchValue[0],inContract);
				CLI_CRM0=CLI_CRM0.replace("'", "");
			}
			if(SearchValue[1].equalsIgnoreCase(""))
			{
				Boolean_Search_NOT=false;
			}
			else
			{
				CLI_CRM1= getCLI_CRM(packageID,search,"NOT_"+SearchValue[1],inContract);
				CLI_CRM1=CLI_CRM1.replace("'", "");
			}
			if(Boolean_Search&&Boolean_Search_NOT)
			{
				Result=filterCLI(CLI_CRM0,CLI_CRM1,false);
			}
			else if(Boolean_Search)
			{
				Result=CLI_CRM0;
			}
			else
			{
				Result=CLI_CRM1;
			}
			return Result;
		}
		else
		{
			return getCLI_CRM(packageID,search,searchValue,inContract);
		}
	}*/
	
	/*@SuppressWarnings({ "unchecked", "rawtypes" })
	private String filterCLI(String Data1,String Data2,Boolean Minus_DIFF) throws Exception
	{
		String[] CLI0=Data1.split(",");
		String[] CLI1=Data2.split(",");
		List<String> baseList=Arrays.asList(CLI1);
		List<String> compareList=new ArrayList(Arrays.asList(CLI0));
		if(CLI0.length>CLI1.length)
		{
			compareList = new ArrayList(Arrays.asList(CLI0));
			baseList = Arrays.asList(CLI1);
		}
		else
		{
			if(Minus_DIFF)
			{
				Report.fnReportFailAndTerminateTest("DB Comparison", "Invalid Data for Comparison");
			}else
			{
				compareList = new ArrayList(Arrays.asList(CLI1));
				baseList = Arrays.asList(CLI0);
			}
		}
		try
		{
			compareList.retainAll(baseList);
		}
		catch(Exception e)
		{
			Report.fnReportFailAndTerminateTest("Retain Function", "Retaining the String is not done properly");
		}
		return StringUtils.join(compareList,",");
	}
	private String[] Data_SearchValue(String SearchValue)
	{
		StringBuffer SEARCHVALUE = new StringBuffer(110);
		StringBuffer NOT_SEARCHVALUE = new StringBuffer(110);
		String [] Search=SearchValue.split("','");
		
		for(int i=0;i<Search.length;i++)
		{
			if(Search[i].contains("NOT"))
			{
				NOT_SEARCHVALUE.append(Search[i].substring(4)+",");
			}
			else
			{
				SEARCHVALUE.append(Search[i]+",");
			}
		}
		
		String [] a={StringUtils.removeEnd(SEARCHVALUE.toString(), ","),StringUtils.removeEnd(NOT_SEARCHVALUE.toString().toString(), ",")};
		return a;
	}*/
	
	public void Update_CreditClassCreditTier(String Str_Account,String Str_CreditClass,String Str_CreditTier) throws Exception{

		String query = null;
		PreparedStatement stm = null;
		String ConnectionURL = "jdbc:oracle:thin:@"+LoadEnvironment.SV_DBIP+":"+LoadEnvironment.SV_DBPORT+":"+LoadEnvironment.SV_DBNAME;
		con = ConnectionFactory.createConnection(ConnectionURL,LoadEnvironment.SV_DBUSERNAME,LoadEnvironment.SV_DBPASSWORD);
		try{
			query = "UPDATE "+LoadEnvironment.SV_DBSCHEMA+".customer_node_history CNH"
					+ " SET CNH.CREDIT_RATING_CODE = (SELECT REFERENCE_CODE FROM "+LoadEnvironment.SV_DBSCHEMA+".REFERENCE_CODE"
					+ " WHERE REFERENCE_TYPE_ID = 9000173  AND ABBREVIATION='"+Str_CreditClass+"')"
					+ " WHERE CNH.EFFECTIVE_END_DATE > SYSDATE and CNH.CUSTOMER_NODE_ID in"
					+ " (select acc.CUSTOMER_NODE_ID from "+LoadEnvironment.SV_DBSCHEMA+".account acc where acc.account_name ='"+Str_Account+"')";
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			stm.executeUpdate();
			con.commit();

			query = "UPDATE "+LoadEnvironment.SV_DBSCHEMA+".customer_node_da_array"
					+ " SET INDEX1_VALUE = "+Str_CreditTier +" "
					+ " WHERE derived_attribute_id = 23000140"
					+ " AND SYSDATE BETWEEN EFFECTIVE_START_DATE AND EFFECTIVE_END_dATE"
					+ "  --and INDEX2_VALUE = 1"
					+ " AND CUSTOMER_NODE_ID  in"
					+ " (Select acc.CUSTOMER_NODE_ID from "+LoadEnvironment.SV_DBSCHEMA+".ACCOUNT acc where acc.account_name = '"+Str_Account+"')";
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			System.out.println("Updating Credit Class for Account Number "+ Str_Account);
			stm.executeUpdate();
			con.commit();
		}finally{
			ConnectionFactory.closeConnection(con);
		}
	}

	private String getCLI_OMP(String cLI_CRM) {
		String omp = null;
		try{
			String ConnectionURL = "jdbc:oracle:thin:@"+LoadEnvironment.OMP_DBIP+":"+LoadEnvironment.OMP_DBPORT+":"+LoadEnvironment.OMP_DBNAME;
			//	System.out.println(ConnectionURL);
			
			con = ConnectionFactory.createConnection(ConnectionURL,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);
			String query = "select * from(select column_value CLI from table(sys.odcivarchar2list("+cLI_CRM+"))"
				+ " Minus"
				+ " select cli from "+LoadEnvironment.OMP_DBSCHEMA+".VW_CPWORDERSTATUS where ISORDERINFINALSTATE = 'N' and CLI in ("+cLI_CRM+"))  order by dbms_random.value";
			System.out.println(query);
			PreparedStatement stm=con.prepareStatement(query);
			ResultSet rs=stm.executeQuery();
			while(rs.next()){
				if(omp == null){
					omp=rs.getString(1);
					omp=omp+",";
				}else{

					omp=omp+rs.getString(1);
					omp=omp+",";
				}
			}
			if(omp!=null){
				omp = omp.substring(0, omp.length() - 1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionFactory.closeConnection(con);
		}
		System.out.println("CLIs From OMP_DB are "+omp);
		System.out.println("Done with OMP");
		return omp;
	}

	public String getAcc_CLI(String CLI_ACC,boolean Boolean_CLI_ACC) {
		String ACC_CLI = null;
		int i=1;
		String query=null;
		do
		{
		try{
			String ConnectionURL = "jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME;
//				System.out.println(ConnectionURL);
			
			con = ConnectionFactory.createConnection(ConnectionURL,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
			CLI_ACC=CLI_ACC.replaceAll(",", "','");
			if(Boolean_CLI_ACC)
			{
				query = "select ACCOUNTNUMBER from CBLOWNER.v_customer_search where CLI in ('"+CLI_ACC+"')";
			}
			else
			{
				query = "select CLI from CBLOWNER.v_customer_search where ACCOUNTNUMBER in ('"+CLI_ACC+"')";
			}
//			System.out.println(query);
			PreparedStatement stm=con.prepareStatement(query);
			ResultSet rs=stm.executeQuery();
			int count=0;
			while(rs.next()){
				if(ACC_CLI == null){
					ACC_CLI=rs.getString(1);
					ACC_CLI=ACC_CLI+",";
				}else{

					ACC_CLI=ACC_CLI+rs.getString(1);
					ACC_CLI=ACC_CLI+",";
				}
				if(count==998)
					break;
			}
			ACC_CLI = ACC_CLI.substring(0, ACC_CLI.length() - 1);
//			System.out.println(ACC_CLI);
			}catch(Exception e){
				i=0;
			e.printStackTrace();
		}finally{
			ConnectionFactory.closeConnection(con);
		}
		}while(i==0);
		return ACC_CLI;
	}
	
	private String[] executeQuery(String query,DBNames DBName,String[] Data,int...limit) throws Exception {
		String[] Return=Data;
		if(!(limit.length>0))
		{
			limit[0]=998;
		}
		String[] Datam=new String[limit[0]];
		try{
			DBNames g = DBName;
			String[] DB_Details=g.getDBDetails();
			String ConnectionURL = "jdbc:oracle:thin:@"+DB_Details[0]+":"+DB_Details[1]+":"+DB_Details[2];
			con = ConnectionFactory.createConnection(ConnectionURL,DB_Details[4],DB_Details[5]);
			PreparedStatement stm=con.prepareStatement(query);
			System.out.println(query);
			ResultSet rs=stm.executeQuery();
			
			if(query.trim().startsWith("select"))
			{
				if(rs.next())
				{
					if(Data.length>0)
					{
						for(int i=0;i<Data.length;i++)
						{
							Datam[i]=rs.getString(Data[i]);
							System.out.println("Initial Data --> "+Datam[i]);
						}
						int count=0;
							while(rs.next()){
								for(int i=0;i<Data.length;i++)
								{
									Datam[i]=Datam[i]+","+rs.getString(Data[i]);
									System.out.println(count+" Data --> "+Datam[i]);
								}
								if(count==limit[0])
									break;
								count++;
							}
					}
				}else{
					Datam=Return;
				}
			}
		}catch(RuntimeException e){
			e.printStackTrace();
			Datam=Return;
//			if(e.getMessage().equals("NO Data FOUND")){
//
//				Report.fnReportFailAndTerminateTest("Data Not Available","NO Data FOUND" );
//			}
		}
			finally{
			ConnectionFactory.closeConnection(con);
		}
		return Datam;
	}
	private String getCLI_SV(String CLI_OMP,int limit,boolean creditClassCheck) {
		String Data[]={"ACCOUNT_NAME"};
		String ACC_OMP="";
		try{
			ACC_OMP=getAcc_CLI(CLI_OMP, true);
			ACC_OMP=shuffleCLI(ACC_OMP);
			ACC_OMP=ACC_OMP.replaceAll(",", "','");
			System.out.println(ACC_OMP);
				String query = " select ACCOUNT_NAME from "+LoadEnvironment.SV_DBSCHEMA+".account a, "+LoadEnvironment.SV_DBSCHEMA+".customer_node_da_array nda where nda.CUSTOMER_NODE_ID=a.CUSTOMER_NODE_ID" +
						" and nda.derived_attribute_id = '12000066'" +
						" AND SYSDATE BETWEEN nda.EFFECTIVE_START_DATE AND nda.EFFECTIVE_END_dATE" +
						" and nda.INDEX2_VALUE = '0'" +
						" and ACCOUNT_NAME in ('"+ACC_OMP+"') and rownum<="+limit;
				if(creditClassCheck)
				{
					query=query+" and a.ACCOUNT_BALANCE <= 0 ";
				}
				query=query+" order by dbms_random.value";
				Data=executeQuery(query,DBNames.SV,Data,limit);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionFactory.closeConnection(con);
		}
//		System.out.println(Data[0]);
		return getAcc_CLI(Data[0], false).replaceAll(",", "|")+","+Data[0].replaceAll(",", "|");
	}
	
	/**
	 * @param packageID
	 * @param search
	 * @param searchValue
	 * @return
	 */
	private String getCLI_CRM(String packageID, searchByData search,
			String searchValue,boolean inContract) {

		String CLI = null ;	
		ResultSet rs = null;
		String query1 = null; 

		switch (search){
		case Proposition:
			if(inContract){
				if(searchValue.contains("NOT")){
					searchValue = searchValue.substring(4);
					query1 =  "select  cli from cblowner.portfoliosalespackage psp,CBLOWNER.contract ct where packageid in('"+packageID+"') "
							+ " and psp.id = ct.portfoliosalespackageid "
							+ " and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null"
							+ " and psp.isvalid = 1 "
							+ "	and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "
							+ "	and ct.terminationdate is null and rownum<=1000 "
							+ " MINUS "
							+ " Select P.CLI from cblowner.PORTFOLIO_ITEM_SUPP_SERVICE P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"')"
							+ " and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null) "
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and "
							+ " sysdate >= p1.activationdate and sysdate < p1.disconnectiondate) "
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and  p1.enddate is not null and"
							+ " sysdate < p1.activationdate)) "
							+ " and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1 ";

				}else {

					query1 ="Select P.CLI from cblowner.PORTFOLIO_ITEM_SUPP_SERVICE P1,cblowner.portfoliosalespackage P,CBLOWNER.contract ct "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P.id = ct.portfoliosalespackageid "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1"
							+ " and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "
							+ " and ct.terminationdate is null and rownum<=1000 group by cli having(count(*)=1";
				}
			}else{
				if(searchValue.contains("NOT")){

					searchValue = searchValue.substring(4);
					query1 = "select  cli from cblowner.portfoliosalespackage psp where packageid in('"+packageID+"') "
							+ " and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null"
							+ " and psp.isvalid = 1 and rownum<=1000 "
							+ " MINUS "
							+ " Select P.CLI from cblowner.PORTFOLIO_ITEM_SUPP_SERVICE P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"')"
							+ " and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null) "
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and "
							+ " sysdate >= p1.activationdate and sysdate < p1.disconnectiondate) "
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and  p1.enddate is not null and"
							+ " sysdate < p1.activationdate)) "
							+ " and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1 "; 

				}else {
					query1 ="Select P.CLI from cblowner.PORTFOLIO_ITEM_SUPP_SERVICE P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1 and rownum<=1000 group by cli having(count(*)=1 ";
				}
			}
			break;
		case Discount:
			if(inContract){
				if(searchValue.contains("NOT")){
					searchValue = searchValue.substring(4);
					query1 =  "select  cli from cblowner.portfoliosalespackage psp,CBLOWNER.contract ct where packageid in('"+packageID+"') "
							+ " and psp.id = ct.portfoliosalespackageid "
							+ " and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null"
							+ " and psp.isvalid = 1 "
							+ "	and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "
							+ "	and ct.terminationdate is null and rownum<=1000 "
							+ " MINUS "
							+ " Select P.CLI from cblowner.portfolioitemdiscount P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"')"
							+ " and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null) "
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and "
							+ " sysdate >= p1.activationdate and sysdate < p1.disconnectiondate) "
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and  p1.enddate is not null and"
							+ " sysdate < p1.activationdate)) "
							+ " and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1 "; 

				}else {

					query1 ="Select P.CLI from cblowner.portfolioitemdiscount P1,cblowner.portfoliosalespackage P,CBLOWNER.contract ct "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P.id = ct.portfoliosalespackageid "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1"
							+ " and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "
							+ " and ct.terminationdate is null and rownum<=1000 group by cli having(count(*)=1";
				}
			}else{
				if(searchValue.contains("NOT")){
					searchValue = searchValue.substring(4);
					query1 = "select  cli from cblowner.portfoliosalespackage psp where packageid in('"+packageID+"') "
							+ " and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null and  psp.isvalid = 1 and rownum<=1000 "
							+ " MINUS "
							+ " Select P.CLI from cblowner.portfolioitemdiscount P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"')"
							+ " and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null) "
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and "
							+ " sysdate >= p1.activationdate and sysdate < p1.disconnectiondate) "
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and  p1.enddate is not null and"
							+ " sysdate < p1.activationdate)) "
							+ " and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and  p.isvalid = 1  "; 
				}else{

					query1 ="Select Distinct P.CLI from cblowner.portfolioitemdiscount P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1 and rownum<=1000 group by cli having(count(*)=1";
				}
			}
			break;
		case Bundle:
			if(inContract){
				if(searchValue.contains("NOT")){
					searchValue = searchValue.substring(4);
					query1 =  "select  cli from cblowner.portfoliosalespackage psp,CBLOWNER.contract ct where packageid in('"+packageID+"') "
							+ " and psp.id = ct.portfoliosalespackageid "
							+ " and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null"
							+ " and psp.isvalid = 1 "
							+ "	and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "
							+ "	and ct.terminationdate is null and rownum<=1000 "
							+ " MINUS "
							+ " Select P.CLI from cblowner.portfolioitembundle P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"')"
							+ " and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null) "
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and "
							+ " sysdate >= p1.activationdate and sysdate < p1.disconnectiondate) "
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and  p1.enddate is not null and"
							+ " sysdate < p1.activationdate)) "
							+ " and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1 "; 

				}else {

					query1 ="Select Distinct P.CLI from cblowner.portfolioitembundle P1,cblowner.portfoliosalespackage P,CBLOWNER.contract ct "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P.id = ct.portfoliosalespackageid "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1"
							+ " and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "
							+ " and ct.terminationdate is null and rownum<=1000 group by cli having(count(*)=1";
				}
			}else{
				if(searchValue.contains("NOT")){
					searchValue = searchValue.substring(4);
					query1 = "select  cli from cblowner.portfoliosalespackage psp where packageid in('"+packageID+"') "
							+ " and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null and  psp.isvalid = 1 and rownum<=1000 group by cli"
							+ " MINUS "
							+ " Select P.CLI from cblowner.portfolioitembundle P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"')"
							+ " and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null) "
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and "
							+ " sysdate >= p1.activationdate and sysdate < p1.disconnectiondate) "
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and  p1.enddate is not null and"
							+ " sysdate < p1.activationdate)) "
							+ " and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and  p.isvalid = 1 "; 
				}else {

					query1 ="Select Distinct P.CLI from cblowner.portfolioitembundle P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and  p.isvalid = 1 and rownum<=1000 group by cli having(count(*)=1";
				}
			}
			break;
		case blank:
			/*query1 = "select CLI from CBLOWNER.portfoliosalespackage p where packageid in ('"+packageID+"') and"
					    + " ((p.activationdate is not null and p.disconnectiondate is null and p.enddate is null)"
						+ "	or (p.activationdate is not null and p.disconnectiondate is not null and p.enddate is not null and"
						+ "	sysdate >= p.activationdate and sysdate < p.disconnectiondate)"
						+ "	or (p.activationdate is not null and p.disconnectiondate is not null and p.enddate is not null and"
						+ "	sysdate < p.activationdate)) and rownum <=1000";*/
			if(inContract){
				query1 = "select  cli from cblowner.portfoliosalespackage psp LEFT OUTER Join CBLOWNER.contract ct on "+
						"	psp.id = ct.portfoliosalespackageid where "+
						"	packageid in('"+packageID+"') "+
						"	and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null "+
						"	and psp.isvalid = 1 "+
						"	and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "+
						"	and ct.terminationdate is null and rownum<=1000 "+
						"	MINUS "+
						"	Select P.CLI from CBLOWNER.portfolioitemproduct P1,cblowner.portfoliosalespackage P "+ 
						"	where p1.portfoliosalespackageid = P.id "+
						"	and p1.tariffid in ('317') "+
						"	and p.packageid in ('"+packageID+"') "+
						"	and p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null "+ 
						"	and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1 "+
						"   ";	
			}else{
				query1 = "select  cli from cblowner.portfoliosalespackage psp"+
						"	where "+
						"	packageid in('"+packageID+"') "+
						"	and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null "+
						"	and psp.isvalid = 1 and rownum<=1000 "+
						"	MINUS "+
						"	Select P.CLI from CBLOWNER.portfolioitemproduct P1,cblowner.portfoliosalespackage P "+ 
						"	where p1.portfoliosalespackageid = P.id "+
						"	and p1.tariffid in ('317') "+
						"	and p.packageid in ('"+packageID+"') "+
						"	and p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null "+ 
						"	and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1 "+
						"   ";	
			}
			break;	
		}

		try{
			String ConnectionURL = "jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT + ":" + LoadEnvironment.CRM_DBNAME;
			//	System.out.println(ConnectionURL);
			query1= " select cli from CBLOWNER.v_customer_search where accountnumber in(" +
					" select accountnumber from CBLOWNER.v_customer_search where CUSTOMERNUMBER in (  select CUSTOMERNUMBER from CBLOWNER.v_customer_search where CLI in ("+query1+")" +
					" minus select CUSTOMERNUMBER from CBLOWNER.bar  minus  select CUSTOMERNUMBER from CBLOWNER.fraud )" +
					" minus select  accountnumber from CBLOWNER.v_customer_search	group by accountnumber having count(accountnumber)>1) AND LENGTH(CLI) = 11" +
					" minus select  cli from CBLOWNER.v_customer_search	group by cli having count(cli)>1";
			System.out.println(query1);
			con = ConnectionFactory.createConnection(ConnectionURL,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
			PreparedStatement stm=con.prepareStatement(query1,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next())
			{
				rs.beforeFirst();
				CLI = "'";
				int count = 0;
				while(rs.next()){
					CLI=CLI+rs.getString(1);
					CLI=CLI+"','";
					if(count==998)
						break;
					count++;
				}
			}
			else
			{
				CLI="";
			}
			if(CLI.equalsIgnoreCase("'"))
			{
				CLI="";
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionFactory.closeConnection(con);
		}
		CLI=CLI.replaceAll(",'$", "");
		CLI=removeCLI(CLI,Str_ErrorCLI);
		System.out.println("Done with CRM");
		return CLI;
	}
	
	/*private String getCLI_CRM_Modified(String packageID, searchByData search,
			String searchValue,boolean inContract) {

		String CLI = null ;	
		ResultSet rs = null;
			String queryPI ="Select Distinct P.CLI from cblowner.PORTFOLIO_ITEM_SUPP_SERVICE P1,cblowner.portfoliosalespackage P,CBLOWNER.contract ct "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P.id = ct.portfoliosalespackageid "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1"
							+ " and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "
							+ " and ct.terminationdate is null";

			String 	queryPO ="Select Distinct P.CLI from cblowner.PORTFOLIO_ITEM_SUPP_SERVICE P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1";

			String queryDI ="Select Distinct P.CLI from cblowner.portfolioitemdiscount P1,cblowner.portfoliosalespackage P,CBLOWNER.contract ct "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P.id = ct.portfoliosalespackageid "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1"
							+ " and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "
							+ " and ct.terminationdate is null ";

			String queryDO ="Select Distinct P.CLI from cblowner.portfolioitemdiscount P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1";
			String queryBI ="Select Distinct P.CLI from cblowner.portfolioitembundle P1,cblowner.portfoliosalespackage P,CBLOWNER.contract ct "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P.id = ct.portfoliosalespackageid "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1"
							+ " and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "
							+ " and ct.terminationdate is null ";
			String queryBO ="Select Distinct P.CLI from cblowner.portfolioitembundle P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and  p.isvalid = 1";
			query1 = "select CLI from CBLOWNER.portfoliosalespackage p where packageid in ('"+packageID+"') and"
					    + " ((p.activationdate is not null and p.disconnectiondate is null and p.enddate is null)"
						+ "	or (p.activationdate is not null and p.disconnectiondate is not null and p.enddate is not null and"
						+ "	sysdate >= p.activationdate and sysdate < p.disconnectiondate)"
						+ "	or (p.activationdate is not null and p.disconnectiondate is not null and p.enddate is not null and"
						+ "	sysdate < p.activationdate)) and rownum <=1000";
			String queryI = "select  cli from cblowner.portfoliosalespackage psp LEFT OUTER Join CBLOWNER.contract ct on "+
						"	psp.id = ct.portfoliosalespackageid where "+
						"	packageid in('"+packageID+"') "+
						"	and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null "+
						"	and psp.isvalid = 1 "+
						"	and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "+
						"	and ct.terminationdate is null "+
						"	MINUS "+
						"	Select P.CLI from CBLOWNER.portfolioitemproduct P1,cblowner.portfoliosalespackage P "+ 
						"	where p1.portfoliosalespackageid = P.id "+
						"	and p1.tariffid in ('317') "+
						"	and p.packageid in ('"+packageID+"') "+
						"	and p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null "+ 
						"	and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1 "+
						"   ";
			String queryO = "select  cli from cblowner.portfoliosalespackage psp"+
						"	where "+
						"	packageid in('"+packageID+"') "+
						"	and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null "+
						"	and psp.isvalid = 1 "+
						"	MINUS "+
						"	Select P.CLI from CBLOWNER.portfolioitemproduct P1,cblowner.portfoliosalespackage P "+ 
						"	where p1.portfoliosalespackageid = P.id "+
						"	and p1.tariffid in ('317') "+
						"	and p.packageid in ('"+packageID+"') "+
						"	and p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null "+ 
						"	and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1 "+
						"   ";	
		try{
			System.out.println(queryPI);
			String ConnectionURL = "jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT + ":" + LoadEnvironment.CRM_DBNAME;
			//	System.out.println(ConnectionURL);
			con = ConnectionFactory.createConnection(ConnectionURL,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
			PreparedStatement stm=con.prepareStatement(queryPI);
			rs=stm.executeQuery();
			if(rs.next())
			{
				CLI = "'";
				int count = 0;
				while(rs.next()){
					CLI=CLI+rs.getString(1);
					CLI=CLI+"','";
					if(count==998)
						break;
					count++;

				}
			}
			else
			{
				CLI="";
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionFactory.closeConnection(con);
		}
		//System.out.println("CLI FROM CRM " + CLI);
		CLI=removeCLI(CLI,Str_ErrorCLI);
		return CLI;
	}*/
	
	private String removeCLI(String CLI_CRM,String Str_ErrorCLI)
	{
		if(!Str_ErrorCLI.equalsIgnoreCase(""))
		{
		String[] Str_CLI=Str_ErrorCLI.split(",");
		for(int i=0;i<Str_CLI.length;i++)
		{
			CLI_CRM = CLI_CRM.replace(Str_CLI[i]+",","");
			CLI_CRM = CLI_CRM.replace(Str_CLI[i]+"','","");
			CLI_CRM = CLI_CRM.replace(Str_CLI[i],"");
			CLI_CRM = CLI_CRM.replaceAll(",'$", "");
			CLI_CRM = CLI_CRM.replaceAll(",$", "");
		}
		}
		return CLI_CRM;
	}
	
	@SuppressWarnings("unused")
	private String getCLI_CRM_Combined(String packageID, searchByData search,
			String searchValue,boolean inContract) {

		String CLI = null ;	
		ResultSet rs = null;
		String query1 = null; 

		switch (search){
		case Proposition:
			if(inContract){
				if(searchValue.contains("NOT")){
					searchValue = searchValue.substring(4);
					query1 =  "select  cli from cblowner.portfoliosalespackage psp,CBLOWNER.contract ct where packageid in('"+packageID+"') "
							+ " and psp.id = ct.portfoliosalespackageid "
							+ " and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null"
							+ " and psp.isvalid = 1 "
							+ "	and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "
							+ "	and ct.terminationdate is null "
							+ " MINUS "
							+ " Select P.CLI from cblowner.PORTFOLIO_ITEM_SUPP_SERVICE P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"')"
							+ " and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null) "
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and "
							+ " sysdate >= p1.activationdate and sysdate < p1.disconnectiondate) "
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and  p1.enddate is not null and"
							+ " sysdate < p1.activationdate)) "
							+ " and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1";

				}else {

					query1 ="Select Distinct P.CLI from cblowner.PORTFOLIO_ITEM_SUPP_SERVICE P1,cblowner.portfoliosalespackage P,CBLOWNER.contract ct "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P.id = ct.portfoliosalespackageid "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1"
							+ " and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "
							+ " and ct.terminationdate is null";
				}
			}else{
				if(searchValue.contains("NOT")){

					searchValue = searchValue.substring(4);
					query1 = "select  cli from cblowner.portfoliosalespackage psp where packageid in('"+packageID+"') "
							+ " and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null"
							+ " and psp.isvalid = 1 "
							+ " MINUS "
							+ " Select P.CLI from cblowner.PORTFOLIO_ITEM_SUPP_SERVICE P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"')"
							+ " and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null) "
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and "
							+ " sysdate >= p1.activationdate and sysdate < p1.disconnectiondate) "
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and  p1.enddate is not null and"
							+ " sysdate < p1.activationdate)) "
							+ " and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1"; 

				}else {

					query1 ="Select Distinct P.CLI from cblowner.PORTFOLIO_ITEM_SUPP_SERVICE P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1";
				}
			}
			break;
		case Discount:
			if(inContract){
				if(searchValue.contains("NOT")){
					searchValue = searchValue.substring(4);
					query1 =  "select  cli from cblowner.portfoliosalespackage psp,CBLOWNER.contract ct where packageid in('"+packageID+"') "
							+ " and psp.id = ct.portfoliosalespackageid "
							+ " and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null"
							+ " and psp.isvalid = 1 "
							+ "	and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "
							+ "	and ct.terminationdate is null "
							+ " MINUS "
							+ " Select P.CLI from cblowner.portfolioitemdiscount P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"')"
							+ " and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null) "
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and "
							+ " sysdate >= p1.activationdate and sysdate < p1.disconnectiondate) "
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and  p1.enddate is not null and"
							+ " sysdate < p1.activationdate)) "
							+ " and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1"; 

				}else {

					query1 ="Select Distinct P.CLI from cblowner.portfolioitemdiscount P1,cblowner.portfoliosalespackage P,CBLOWNER.contract ct "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P.id = ct.portfoliosalespackageid "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1"
							+ " and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "
							+ " and ct.terminationdate is null ";
				}
			}else{
				if(searchValue.contains("NOT")){
					searchValue = searchValue.substring(4);
					query1 = "select  cli from cblowner.portfoliosalespackage psp where packageid in('"+packageID+"') "
							+ " and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null and  psp.isvalid = 1 "
							+ " MINUS "
							+ " Select P.CLI from cblowner.portfolioitemdiscount P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"')"
							+ " and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null) "
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and "
							+ " sysdate >= p1.activationdate and sysdate < p1.disconnectiondate) "
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and  p1.enddate is not null and"
							+ " sysdate < p1.activationdate)) "
							+ " and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and  p.isvalid = 1"; 
				}else{

					query1 ="Select Distinct P.CLI from cblowner.portfolioitemdiscount P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1";
				}
			}
			break;
		case Bundle:
			if(inContract){
				if(searchValue.contains("NOT")){
					searchValue = searchValue.substring(4);
					query1 =  "select  cli from cblowner.portfoliosalespackage psp,CBLOWNER.contract ct where packageid in('"+packageID+"') "
							+ " and psp.id = ct.portfoliosalespackageid "
							+ " and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null"
							+ " and psp.isvalid = 1 "
							+ "	and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "
							+ "	and ct.terminationdate is null "
							+ " MINUS "
							+ " Select P.CLI from cblowner.portfolioitembundle P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"')"
							+ " and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null) "
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and "
							+ " sysdate >= p1.activationdate and sysdate < p1.disconnectiondate) "
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and  p1.enddate is not null and"
							+ " sysdate < p1.activationdate)) "
							+ " and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1"; 

				}else {

					query1 ="Select Distinct P.CLI from cblowner.portfolioitembundle P1,cblowner.portfoliosalespackage P,CBLOWNER.contract ct "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P.id = ct.portfoliosalespackageid "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1"
							+ " and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "
							+ " and ct.terminationdate is null ";
				}
			}else{
				if(searchValue.contains("NOT")){
					searchValue = searchValue.substring(4);
					query1 = "select  cli from cblowner.portfoliosalespackage psp where packageid in('"+packageID+"') "
							+ " and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null and  psp.isvalid = 1 "
							+ " MINUS "
							+ " Select P.CLI from cblowner.portfolioitembundle P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"')"
							+ " and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null) "
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and "
							+ " sysdate >= p1.activationdate and sysdate < p1.disconnectiondate) "
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and  p1.enddate is not null and"
							+ " sysdate < p1.activationdate)) "
							+ " and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and  p.isvalid = 1"; 
				}else {

					query1 ="Select Distinct P.CLI from cblowner.portfolioitembundle P1,cblowner.portfoliosalespackage P "
							+ " where p1.portfoliosalespackageid = P.id "
							+ " and P1.propositionid in ('"+searchValue+"') "
							+ "	and p.packageid in('"+packageID+"') "
							+ " and ((p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null)"
							+ " or (p1.activationdate is not null and p1.disconnectiondate is not null and p1.enddate is not null and"
							+ " sysdate >= p1.activationdate and"
							+ " sysdate < p1.disconnectiondate)"
							+ " or (p1.activationdate is not null and"
							+ " p1.disconnectiondate is not null and"
							+ "	p1.enddate is not null and"
							+ "	sysdate < p1.activationdate))"
							+ "	and p.activationdate is not null "
							+ " and p.disconnectiondate is null and p.enddate is null and  p.isvalid = 1";
				}
			}
			break;
		case blank:
			/*query1 = "select CLI from CBLOWNER.portfoliosalespackage p where packageid in ('"+packageID+"') and"
					    + " ((p.activationdate is not null and p.disconnectiondate is null and p.enddate is null)"
						+ "	or (p.activationdate is not null and p.disconnectiondate is not null and p.enddate is not null and"
						+ "	sysdate >= p.activationdate and sysdate < p.disconnectiondate)"
						+ "	or (p.activationdate is not null and p.disconnectiondate is not null and p.enddate is not null and"
						+ "	sysdate < p.activationdate)) and rownum <=1000";*/
			if(inContract){
				query1 = "select  cli from cblowner.portfoliosalespackage psp LEFT OUTER Join CBLOWNER.contract ct on "+
						"	psp.id = ct.portfoliosalespackageid where "+
						"	packageid in('"+packageID+"') "+
						"	and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null "+
						"	and psp.isvalid = 1 "+
						"	and SYSDATE between ct.STARTDATE+15 and ct.STARTDATE+(ct.CONTRACTTERM*30)-1 "+
						"	and ct.terminationdate is null "+
						"	MINUS "+
						"	Select P.CLI from CBLOWNER.portfolioitemproduct P1,cblowner.portfoliosalespackage P "+ 
						"	where p1.portfoliosalespackageid = P.id "+
						"	and p1.tariffid in ('317') "+
						"	and p.packageid in ('"+packageID+"') "+
						"	and p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null "+ 
						"	and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1 "+
						"   ";	
			}else{
				query1 = "select  cli from cblowner.portfoliosalespackage psp"+
						"	where "+
						"	packageid in('"+packageID+"') "+
						"	and psp.activationdate is not null and psp.disconnectiondate is null and psp.enddate is null "+
						"	and psp.isvalid = 1 "+
						"	MINUS "+
						"	Select P.CLI from CBLOWNER.portfolioitemproduct P1,cblowner.portfoliosalespackage P "+ 
						"	where p1.portfoliosalespackageid = P.id "+
						"	and p1.tariffid in ('317') "+
						"	and p.packageid in ('"+packageID+"') "+
						"	and p1.activationdate is not null and p1.disconnectiondate is null and p1.enddate is null "+ 
						"	and p.activationdate is not null and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1 "+
						"   ";	
			}
			break;	
		}


		try{
			String ConnectionURL = "jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT + ":" + LoadEnvironment.CRM_DBNAME;
			//	System.out.println(ConnectionURL);
			con = ConnectionFactory.createConnection(ConnectionURL,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
			PreparedStatement stm=con.prepareStatement(query1,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			CLI = "'";
			int count = 0;
			if(rs.next())
			{
				rs.beforeFirst();
			while(rs.next()){
				CLI=CLI+rs.getString(1);
				CLI=CLI+"','";
				if(count==998)
					break;
				count++;

			}
			}
			else
			{
				CLI="";
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionFactory.closeConnection(con);
		}
		//System.out.println("CLI FROM CRM " + CLI);
		return CLI;
	}
	
	private String shuffleCLI(String Str_CLI) throws Exception
	{
		//WebActions WA=new WebActions();
//		System.out.println("Shuffling Data...");
		List<String> CLIs = Arrays.asList(Str_CLI.split(","));
		Collections.shuffle(CLIs);
		StringBuilder sb = new StringBuilder();
		
		for (String w : CLIs) 
		{
			sb.append(w);
		    sb.append(',');
		}
		Str_CLI = sb.toString().trim().replaceAll(",$", "");
		return Str_CLI;
	}
	
	/**
	 * @param cLI
	 * @return
	 */
	@SuppressWarnings("finally")
	public int getCLIValidate(String cLI) {

		ResultSet rs = null;
		int returnval = 1;
		String query1 = null; 

		query1 = "select CLI from cblowner.portfoliosalespackage where CLI in ('"+cLI+"')";

		try{
			String ConnectionURL = "jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT + ":" + LoadEnvironment.CRM_DBNAME;
			con = ConnectionFactory.createConnection(ConnectionURL,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
			PreparedStatement stm=con.prepareStatement(query1);
			rs=stm.executeQuery();

			if(rs.next()){
				returnval = 0;
			}else{
				returnval = 1;
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionFactory.closeConnection(con);
			return returnval;
		}

	}
	
	@SuppressWarnings("finally")
	public int Get_CPWNRef(String cPWN) throws Exception {
		String query = null;
		ResultSet rs = null;
		PreparedStatement stm;
		int returnval = 1;
		
		
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.SKID_DBIP+":"+LoadEnvironment.SKID_DBPORT+":"+LoadEnvironment.SKID_DBNAME,LoadEnvironment.SKID_DBUSERNAME,LoadEnvironment.SKID_DBPASSWORD);
		query ="select NK_VALUE from T_NETWORK_KEYS where NK_VALUE = '"+cPWN+"'";
		try{
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				returnval = 0;
			}else{
				returnval = 1;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionFactory.closeConnection(con);
			System.out.println("CPWN REF is -->"+ cPWN);
			return returnval;
		}
	}
	
	@SuppressWarnings("finally")
	public String Get_BlacklistCustomer(){
		String query = null;
		ResultSet rs = null;
		PreparedStatement stm;
		String strCustInfo = "";
		
		
		String ConnectionURL = "jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT + ":" + LoadEnvironment.CRM_DBNAME;
		//	System.out.println(ConnectionURL);
		try{
			con = ConnectionFactory.createConnection(ConnectionURL,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
			query = "select CUSTOMERNUMBER,FIRSTNAME,FAMILYNAME from cblowner.v_blacklist_search where rownum=1";
			stm = con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				strCustInfo=rs.getString("FIRSTNAME") + "," + rs.getString("FAMILYNAME");
			}else{
				Report.fnReportFail("NO BLACKLIST DATA IN CRM");
				strCustInfo = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionFactory.closeConnection(con);
			return strCustInfo;
		}
	}

	@SuppressWarnings("finally")
	public String getAccountWithInvoice(){
		String query = null;
		ResultSet rs = null;
		PreparedStatement stm;
		String strCustInfo = "";
		
		String ConnectionURL = "jdbc:oracle:thin:@"+LoadEnvironment.SV_DBIP+":"+LoadEnvironment.SV_DBPORT + ":" + LoadEnvironment.SV_DBNAME;
		//	System.out.println(ConnectionURL);
		try{
			con = ConnectionFactory.createConnection(ConnectionURL,LoadEnvironment.SV_DBUSERNAME,LoadEnvironment.SV_DBPASSWORD);
			query = "select distinct inv.account_id,acc.account_name from ops$svwpor1b.invoice inv,ops$svwpor1b.account acc where "
						+ " acc.account_id=inv.account_id and "
						+ " acc.account_name like '4%'";
			stm = con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				strCustInfo=rs.getString("account_name");
			}else{
				Report.fnReportFail("NO Account with Invoice present in SV DB");
				strCustInfo = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionFactory.closeConnection(con);
			return strCustInfo;
		}
	}
}

