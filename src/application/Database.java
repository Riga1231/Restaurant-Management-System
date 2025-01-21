package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

	public static Connection getConnection() {
	    try {
	        return DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "root", "redmercy44");
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
}
