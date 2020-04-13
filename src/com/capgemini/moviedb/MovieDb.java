package com.capgemini.moviedb;

import java.sql.Connection;
import java.sql.DriverManager;

public class MovieDb {
	public static Connection getConnection() throws Exception{
		 String driverName = "oracle.jdbc.driver.OracleDriver";
		  Class.forName(driverName);
		  Connection conn = DriverManager.getConnection(
		             "jdbc:oracle:thin:@localhost:1521:XE","movie","movie123");
		
		return conn;
	}
}
