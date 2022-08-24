package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import database.Database;

public class Main {

	public static void main(String[] args) {
		createSeller();
		//readAllDepartment();
	}
	
	public static void readAllDepartment() {
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			conn = Database.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery("select * from department");
			
			while(resultSet.next()) {
				System.out.println(resultSet.getInt("Id") + ", " + resultSet.getString("Name"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			Database.closeStatement(statement);
			Database.closeResultSet(resultSet);
			Database.closeConnection();
		}
	}
	
	public static void createSeller() {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			conn = Database.getConnection();
			preparedStatement = conn.prepareStatement("INSERT INTO seller "
													+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
													+ "VALUES "
													+ "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			preparedStatement.setString(1, "Gabriel Carvalho");
			preparedStatement.setString(2, "gabriel@gmail.com");
			preparedStatement.setDate(3, new java.sql.Date(simpleDateFormat.parse("19/01/2000").getTime()));
			preparedStatement.setDouble(4, 3000.0);
			preparedStatement.setInt(5, 4);
			
			int rowsAffected = preparedStatement.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				while(resultSet.next()) {
					int id = resultSet.getInt(1);
					System.out.println("Id = " + id);
				}
			} else {
				System.out.println("No rows affected");
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			Database.closeStatement(preparedStatement);
			Database.closeConnection();
		}
	}

}