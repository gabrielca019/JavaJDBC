package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import database.Database;
import database.DatabaseException;
import database.DatabaseIntegrityException;

public class Main {

	public static void main(String[] args) {
		//createSeller();
		//readAllDepartment();
		//updateSellerSalary();
		//deleteDepartment();
		transation();
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
	
	public static void updateSellerSalary() {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		try {
			conn = Database.getConnection();
			preparedStatement = conn.prepareStatement("UPDATE seller "
													+ "SET BaseSalary = BaseSalary + ? "
													+ "WHERE (DepartmentId = ?)");
			preparedStatement.setDouble(1, 200.0);
			preparedStatement.setInt(2, 2);
			
			int rowsAffected = preparedStatement.executeUpdate();
			
			System.out.println("Rows affected: " + rowsAffected);
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			Database.closeStatement(preparedStatement);
			Database.closeConnection();
		}
		
	}
	
	public static void deleteDepartment() {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		try {
			conn = Database.getConnection();
			
			preparedStatement = conn.prepareStatement("DELETE FROM department"
													+ "WHERE Id = ?");
			preparedStatement.setInt(1, 2);
			
			int rowsAffected = preparedStatement.executeUpdate();
			
			System.out.println("Rows affected: " + rowsAffected);
		} catch(SQLException e) {
			throw new DatabaseIntegrityException(e.getMessage());
		} finally {
			Database.closeStatement(preparedStatement);
			Database.closeConnection();
		}
	}
	
	public static void transation() {
		Connection conn = null;
		Statement statement = null;
		
		try {
			conn = Database.getConnection();
			conn.setAutoCommit(false); //faz com que todos comandos sql sejam agrupados num "grupo", assim executando todos de uma unica vez posteriormente com o commit ou voltando atras com o rollback
			
			statement = conn.createStatement();
			
			int rows1 = statement.executeUpdate("UPDATE seller "
											  + "SET BaseSalary = 2090 "
											  + "WHERE DepartmentId = 1");

			int rows2 = statement.executeUpdate("UPDATE seller "
											  + "SET BaseSalary = 3090 "
											  + "WHERE DepartmentId = 2");
			
			conn.commit();
			System.out.println("rows 1: " + rows1);
			System.out.println("rows 2: " + rows2);
		} catch(SQLException e) {
			try {
				conn.rollback();
				throw new DatabaseException("Transaction rolled back! Caused by: " + e.getMessage());
			} catch (SQLException e1) {
				throw new DatabaseException("Error trying to rollback! Caused by: " + e1.getMessage());
			}
		} finally {
			Database.closeStatement(statement);
			Database.closeConnection();
		}
	}

}