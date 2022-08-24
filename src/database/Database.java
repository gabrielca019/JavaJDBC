package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
	
	private static Connection conn = null;
	
	public static Connection getConnection() {
		if(conn == null) {
			try {
				Properties properties = loadProperties();
				String url = properties.getProperty("dburl");
				conn = DriverManager.getConnection(url, properties);
			} catch(SQLException e) {
				throw new DatabaseException(e.getMessage());
			}
		}
		return conn;
	}
	
	public static void closeConnection() {
		if(conn != null) {
			try {
				conn.close();
			} catch(SQLException e) {
				throw new DatabaseException(e.getMessage());
			}
		}
	}
	
	private static Properties loadProperties() {
		try(FileInputStream fs = new FileInputStream("db.properties")) {
			Properties properties = new Properties();
			properties.load(fs);
			return properties;
		} catch(IOException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

}