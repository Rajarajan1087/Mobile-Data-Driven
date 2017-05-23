/**
 * 
 */
package com.SharedModules;

import java.util.Random;

import com.Engine.AppiumSetup;
import com.Engine.Reporter;
import com.Stubs.StubFilePlacing;
import com.Stubs.StubFilePlacing.StubType;

/**
 * @author 661317
 *
 */
public class NameGenerator extends AppiumSetup{
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		int i;
		for ( i=0;i<=0;i++){
			String fname = randomName(6);
			String lname = randomName(6);
			String CLI = randomCLI(9);
			System.out.println("Firstname-->"+fname+"  Lastname-->"+lname+"   CLI-->"+CLI);
		}

	}
	public static String randomName( int len ) {

		final String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder( len );

		for( int i = 0; i < len; i++ ) 
			sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

		return sb.toString();
	}

	public static String randomCLI( int len ) throws Exception {
		NewDatabase db = new NewDatabase(Report);
		int validate = 1;
		final String AB = "0123456789";
		Random rnd = new Random();
		//String returnCLI = null;
		StringBuilder sb  = new StringBuilder( len );
		sb.append( "01" );
		for( int i = 0; i < len; i++ ) 
			sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

		String CLI = sb.toString();
		validate = db.getCLIValidate(CLI);
		System.out.println("NewCLI --> "+CLI);
		StubFilePlacing SF = new StubFilePlacing();
		SF.PlaceFile(StubType.Switcher, CLI);
		if(validate==1){
			return CLI;
		}else{
			randomCLI(9);
			return CLI;
		}
	}
	public static String randomCPWNRef( int len ) throws Exception {
		int validate = 1;
		final String AB = "0123456789";
		NewDatabase db = new NewDatabase(Report);
		Random rnd = new Random();
		//String returnCLI = null;
		StringBuilder sb  = new StringBuilder( len );
		sb.append( '1' );
		for( int i = 0; i < len; i++ ) 
			sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

		String CPWN = sb.toString();
		try {
			validate = db.Get_CPWNRef(CPWN);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(validate==1){
			return CPWN;
		}else{
			randomCLI(len);
			return CPWN;
		}
	}

	public static String randomNumber( String StartingNumber, int Balancelength ) {

		final String AB = "0123456789";
		Random rnd = new Random();

		StringBuilder sb  = new StringBuilder( Balancelength );
		sb.append( StartingNumber );
		for( int i = 0; i < Balancelength; i++ ) 
			sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

		String CPWN = sb.toString();

		return CPWN;
	}

}




