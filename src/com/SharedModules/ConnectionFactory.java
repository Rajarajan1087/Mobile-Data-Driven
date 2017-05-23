package com.SharedModules;

import java.sql.*;

/*
 * Functions
 * connection factory establishes connection to the required database
 * closes the connection 
 */
public class ConnectionFactory {

	public static java.sql.Connection createConnection(String url, String dbuser, String dbpass) {
		// Use DRIVER and DBURL to create a connection
		// Recommend connection pool implementation/usage
		java.sql.Connection con=null;
		
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url,dbuser,dbpass);

		}catch (ClassNotFoundException ce) {

		}catch (SQLException se) {
			
			se.printStackTrace();
		}
		return con;
	}
	public static void closeConnection(java.sql.Connection con){
		try {
			con.close();
		} catch (SQLException e) {
		
		}
	}

}
