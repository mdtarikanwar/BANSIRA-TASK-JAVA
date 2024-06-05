package com.library;

import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcConnectivityUtil {

	 private static String jdbcUrl = "jdbc:mysql://localhost:3306/library";
     private static String username = "root";
     private static String password = "root"; 
     
     public static Connection getConnection() {    	
    	 Connection connection = null;
    	 try {
    		 
    		 Class.forName("com.mysql.cj.jdbc.Driver");
    		 
    		 connection = DriverManager.getConnection(jdbcUrl, username, password);
    		 return connection;
    	 } catch(Exception e) {
    		 e.printStackTrace();
    		 return null;
    	 }
     }
     
}
