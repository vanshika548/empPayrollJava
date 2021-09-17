package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {
	private PreparedStatement employeePayrollDataStatement;
	private static EmployeePayrollDBService employeePayrollDBService;
	private EmployeePayrollDBService() {
	}
	
	public static EmployeePayrollDBService getInstance() {
		if(employeePayrollDBService == null)
			employeePayrollDBService = new EmployeePayrollDBService();
		return employeePayrollDBService;
	}

	public List<EmployeePayrollData> readData() {
		String sql = "Select * from employee_payroll";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try(Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				int id = result.getInt("ID");
				String name = result.getString("Name");
				double salary = result.getDouble("BASIC_PAY");
				LocalDate startDate = result.getDate("Start_date").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id,name,salary,startDate));
	        }
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_database?useSSL=FALSE";
		String userName = "root";
		String password = "Vanshika@123";
		Connection con;
		System.out.println("Connecting to database"+ jdbcURL);
		con = DriverManager.getConnection(jdbcURL , userName , password);
		System.out.println("Connection is successful!!!!! "+con);
		return con;
	}

	public int updateEmployeeData(String name, double salary) {
		String sql = String.format("update employee_payroll set salary = %.2f where name = '%s':",salary,name);
		try(Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private int updateEmployeeDataUsingPreparedStatement(String name, double salary) {
		String sql = String.format("update employee_payroll set salary = %.2f where name = '%s':",salary,name);
		try(Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<EmployeePayrollData> getEmployeePayrollData(String name) {
		List<EmployeePayrollData> employeePayrollList = null;
		if(this.employeePayrollDataStatement == null)
			this.preparedStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1, name);
			ResultSet result = employeePayrollDataStatement.executeQuery();
			employeePayrollList = this.getEmployeePayrollData(result);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet result) {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try {
			while(result.next()) {
				int id = result.getInt("ID");
				String name = result.getString("Name");
				double salary = result.getDouble("BASIC_PAY");
				LocalDate startDate = result.getDate("Start_date").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id,name,salary,startDate));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private void preparedStatementForEmployeeData() {
		try {
			Connection connection = this.getConnection();
			String sql = "Select * from employee_payroll where name= ?";
			employeePayrollDataStatement = connection.prepareStatement(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
}
