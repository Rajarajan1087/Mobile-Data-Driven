package com.SharedModules;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.Engine.LoadEnvironment;
import com.Engine.Reporter;

public class NewDatabase{
	public Reporter Report;
	public int limit=1;
	public long startTime=0;
	public long duration=0;
	public String FibreType="";
	public int TrialLimit=1;
	public int warningDuration=20;
	public int orderStatus=4181;
	private String Str_ErrorCLI="";
	private static Connection con;
	public NewDatabase(Reporter report) {
		Report=report;
	}
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
		String CLI_OMP = null;
		String CLI_CRM = "";
		String CLI_SV = "";
		
		searchValue=searchValue.replace(",", "','");
		packageID=packageID.replace(",", "','");
		
/***************************** Querying CRM DB **************************/
		
		TimeRunner();
		int j=0;
		while(j<TrialLimit)
		{
			System.out.println("Retreiving Data from CRM - - TRIAL "+(j+1));
			CLI_CRM=getCLI_CRM(packageID,search,searchValue,inContract);
			if(!CLI_CRM.equalsIgnoreCase(""))
			{
					TimeRunner();TimeRunner();
					System.out.println("Got Data from CRM in TRIAL "+(j+1));
			}
			else
			{
				j++;
				if(j==TrialLimit)
				{
					TimeRunner();TimeRunner();
					Report.fnReportFailAndTerminateTest("Data Not Found","Fail: NO DATA FROM CRM");
				}
				continue;
			}
			
		if(!CLI_CRM.equalsIgnoreCase(""))
		{
			for(int i=0;i<Str_CLI.length;i++)
			{
				CLI_CRM=removeCLI(CLI_CRM,Str_CLI[i]);
			}
//			CLI_CRM=shuffleCLI(CLI_CRM);
		}
		
/***************************** Querying OMP DB **************************/
		
			System.out.println("Retreiving Data from OMP - - TRIAL "+(j+1));
			CLI_OMP = getCLI_OMP(CLI_CRM);
			if(CLI_OMP!=null)
			{
				TimeRunner();TimeRunner();
				System.out.println("Got Data from OMP in TRIAL "+(j+1));
			}
			else
			{
				j++;
				if(j==TrialLimit)
				{
					TimeRunner();TimeRunner();
					Report.fnReportFailAndTerminateTest("Data Not Found","Fail: NO DATA FROM OMP");
				}
				continue;
//				CLI_CRM=shuffleCLI(CLI_CRM);
			}
			
		if(CLI_OMP!=null)
		{
//			CLI_OMP=shuffleCLI(CLI_OMP);
		}
		
/***************************** Querying SV DB **************************/
		
			System.out.println("Retreiving Data from SV - - TRIAL "+(j+1));
			CLI_SV = getCLI_SV(CLI_OMP,limit,creditClassCheck);
			if(!CLI_SV.equals(""))
			{
				TimeRunner();TimeRunner();
				System.out.println("Got Data from SV in TRIAL "+(j+1));
				j=TrialLimit;
			}
			else
			{
//				CLI_OMP=shuffleCLI(CLI_OMP);
				j++;
				if(j==TrialLimit)
				{
					TimeRunner();TimeRunner();
					Report.fnReportFailAndTerminateTest("Data Not Found","Fail: NO DATA FROM SV");
				}
				continue;
			}
		}
		return CLI_SV;
	} 
	public String getDiscountIDs() throws Exception {
		Connection con = null;
		PreparedStatement stm = null;
		String query = null;
		ResultSet rs = null;
		String returnData="";
		con = ConnectionFactory.createConnection("jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
		query ="select distinct propositionid from cblowner.portfolioitemdiscount p where NAME not in ('CC Calling Circle','Mobile Saver Boost','CC Calling Circle','Free Unlimited Usage','Free Mobile Extra','Free Mobile Saver Boost','12 Months Line Rental Discoun','Free Connection Fee','Free Line Rental for 3 months','Free Broadband Forever','Free Line Rental for 2 months','Calling Features Boost','Free Line Rental for 1 month','Free Talk Unlimited','Mobile Saver Boost')";
		try{
			stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			
			if(rs.next()){
				while(rs.next()){
					returnData=returnData+","+rs.getString(1);
				}
			}else{
				throw new RuntimeException("NO ROWS");
			}
		}catch(Exception e){
			if(e.getMessage().equalsIgnoreCase("NO ROWS")){
				Report.fnReportFailAndTerminateTest("SKID DB","NO CPWN FOUND");

			}
		}finally{
			ConnectionFactory.closeConnection(con);
		}
		return returnData.substring(1);
	} 
	public void TimeRunner() throws Exception
	{
		if(startTime==0)
		{
			startTime = System.nanoTime();
		}
		else
		{
			duration = System.nanoTime() - startTime;
			long misec=duration%1000000000;
			long sec=duration/1000000000;
			startTime=0;
			if(sec>warningDuration)
			{
				Report.fnReportWarning("Time Taken is "+sec+"."+misec+"seconds");
			}
			else
			{
				System.out.println("Time Taken is "+sec+"."+misec+"seconds");
			}
		}
	}
	
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
//			E.printStackTrace();
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
			con = ConnectionFactory.createConnection(ConnectionURL,LoadEnvironment.OMP_DBUSERNAME,LoadEnvironment.OMP_DBPASSWORD);
			String query;
			if(orderStatus==4181)
			{
			query = "select * from(select column_value CLI from table(sys.odcivarchar2list("+cLI_CRM+"))"
				+ " Minus"
				+ " select cli from "+LoadEnvironment.OMP_DBSCHEMA+".VW_CPWORDERSTATUS where ISORDERINFINALSTATE = 'N' and CLI in ("+cLI_CRM+"))  order by dbms_random.value";
			}
			else
			{
				query = " select cli from "+LoadEnvironment.OMP_DBSCHEMA+".VW_CPWORDERSTATUS where orderstatus='"+orderStatus+"' and CLI in ("+cLI_CRM+")  order by dbms_random.value";
			}
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
//			e.printStackTrace();
		}finally{
			ConnectionFactory.closeConnection(con);
		}
		System.out.println("Done with OMP");
		return omp;
	}

	public String getAcc_CLI(String CLI_ACC,boolean Boolean_CLI_ACC) {
		String ACC_CLI = "";
		String query=null;
		try{
			String ConnectionURL = "jdbc:oracle:thin:@"+LoadEnvironment.CRM_DBIP+":"+LoadEnvironment.CRM_DBPORT+":"+LoadEnvironment.CRM_DBNAME;
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
				if(ACC_CLI.equalsIgnoreCase("")){
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
			}catch(Exception e){
//				e.printStackTrace();
		}finally{
			ConnectionFactory.closeConnection(con);
		}
		return ACC_CLI;
	}
	
	/*private String[] executeQuery(String query,DBNames DBName,String[] Data,int...limit) throws Exception {
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
//			System.out.println(query);
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
//							System.out.println("Initial Data --> "+Datam[i]);
						}
						int count=0;
							while(rs.next()){
								for(int i=0;i<Data.length;i++)
								{
									Datam[i]=Datam[i]+","+rs.getString(Data[i]);
									if(i==Data.length-1)
									{
//										System.out.println((count+1)+" Data --> "+Datam[i]);
									}
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
//			e.printStackTrace();
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
	}*/
	private String getCLI_SV(String CLI_OMP,int limit,boolean creditClassCheck) {
		String ReturnData="";
		ResultSet rs=null;
		String ACC_OMP=",";
		ACC_OMP=getAcc_CLI(CLI_OMP, true);
		if(!ACC_OMP.equals(""))
		{
				try{
					String ConnectionURL = "jdbc:oracle:thin:@"+LoadEnvironment.SV_DBIP+":"+LoadEnvironment.SV_DBPORT + ":" + LoadEnvironment.SV_DBNAME;
					con = ConnectionFactory.createConnection(ConnectionURL,LoadEnvironment.SV_DBUSERNAME,LoadEnvironment.SV_DBPASSWORD);
					String query="select * from (select column_value ACCOUNT_NAME from table(sys.odcivarchar2list("+ACC_OMP+"))" +
					" INTERSECT" +
					" select ACCOUNT_NAME from "+LoadEnvironment.SV_DBSCHEMA+".account a, "+LoadEnvironment.SV_DBSCHEMA+".customer_node_da_array nda where nda.CUSTOMER_NODE_ID=a.CUSTOMER_NODE_ID" +
					" and nda.derived_attribute_id = '12000066'" +
					" AND SYSDATE BETWEEN nda.EFFECTIVE_START_DATE AND nda.EFFECTIVE_END_dATE and nda.INDEX2_VALUE = '0'" +
					" and ACCOUNT_NAME in (select column_value ACCOUNT_NAME from table(sys.odcivarchar2list("+ACC_OMP+")))";
					if(creditClassCheck)
					{
						query=query+" and a.ACCOUNT_BALANCE <= 0 ) where rownum<="+limit;
					}
					else
					{
						query=query+") where rownum<="+limit;
					}
					PreparedStatement stm=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					rs=stm.executeQuery();
					if(rs.next())
					{
						rs.beforeFirst();
						while(rs.next())
						{
							ReturnData+=","+rs.getString(1);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					ConnectionFactory.closeConnection(con);
				}
			}
		if(!ReturnData.equals(""))
		{
			ReturnData=getAcc_CLI(ReturnData.substring(1), false).replaceAll(",", "|")+","+ReturnData.substring(1).replaceAll(",", "|");
		}
		return ReturnData;
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
							+ " and P1.propositionid not in ('"+searchValue+"')"
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
							+ " and ct.terminationdate is null and rownum<=1000 group by cli having count(*)=1";
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
							+ " and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1 and rownum<=1000 group by cli having count(*)=1 ";
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
							+ " and ct.terminationdate is null and rownum<=1000 group by cli having count(*)=1";
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
							+ " and p.disconnectiondate is null and p.enddate is null and p.isvalid = 1 and rownum<=1000 group by cli having count(*)=1";
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
							+ " and ct.terminationdate is null and rownum<=1000 group by cli having count(*)=1";
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
							+ " and p.disconnectiondate is null and p.enddate is null and  p.isvalid = 1 and rownum<=1000 group by cli having count(*)=1";
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
			con = ConnectionFactory.createConnection(ConnectionURL,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
			query1= " select cli from CBLOWNER.v_customer_search where accountnumber in(" +
					" select accountnumber from CBLOWNER.v_customer_search where CUSTOMERNUMBER in (  select CUSTOMERNUMBER from CBLOWNER.v_customer_search where CLI in ("+query1+")" +
					" minus select CUSTOMERNUMBER from CBLOWNER.bar  minus  select CUSTOMERNUMBER from CBLOWNER.fraud )" +
					" minus select  accountnumber from CBLOWNER.v_customer_search	group by accountnumber having count(accountnumber)>1) AND LENGTH(CLI) = 11" +
					" minus select  cli from CBLOWNER.v_customer_search	group by cli having count(cli)>1";
			if(!FibreType.equals(""))
			{
				System.out.println("Two Click Data");
				System.out.println("\tData for Fibre "+FibreType);
				query1=query1+" intersect" +
					" select  Distinct cli from cblowner.portfoliosalespackage psp,CBLOWNER.portfolio_item_supp_service p where packageid in('"+packageID+"')" +
					" and psp.id = p.portfoliosalespackageid and p.name like '%"+FibreType+"%'";
			}
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
//			e.printStackTrace();
		}finally{
			ConnectionFactory.closeConnection(con);
		}
		CLI=CLI.replaceAll(",'$", "");
		CLI=removeCLI(CLI,Str_ErrorCLI);
//		System.out.println(CLI);
//		System.out.println("Done with CRM");
		return CLI;
	}
	
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
//			e.printStackTrace();
		}finally{
			ConnectionFactory.closeConnection(con);
		}
		return CLI;
	}
	
	/*private String shuffleCLI(String Str_CLI) throws Exception
	{
		System.out.println("Shuffling Data...");
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
	}*/
	
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
//			e.printStackTrace();
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
//			e.printStackTrace();
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
		try{
			con = ConnectionFactory.createConnection(ConnectionURL,LoadEnvironment.CRM_DBUSERNAME,LoadEnvironment.CRM_DBPASSWORD);
			query = "select CUSTOMERNUMBER,FIRSTNAME,FAMILYNAME from cblowner.v_blacklist_search where rownum=1";
			stm = con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stm.executeQuery();
			if(rs.next()){
				strCustInfo=rs.getString("FIRSTNAME") + "," + rs.getString("FAMILYNAME");
			}else{
				Report.fnReportFailAndTerminateTest("BLACKLIST DATA","NO BLACKLIST DATA IN CRM");
				strCustInfo = null;
			}
		}catch(Exception e){
//			e.printStackTrace();
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
				Report.fnReportFailAndTerminateTest("SV DB","NO Account with Invoice present in SV DB");
				strCustInfo = null;
			}
		}catch(Exception e){
//			e.printStackTrace();
		}finally{
			ConnectionFactory.closeConnection(con);
			return strCustInfo;
		}
	}
}

