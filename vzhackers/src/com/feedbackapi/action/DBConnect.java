package com.feedbackapi.action;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {
	public static Connection connection;

	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			if (connection == null) {
				connection = DriverManager
						.getConnection("jdbc:mysql://localhost:3306/feedback?"
								+ "user=root&password=root");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;

	}
}
