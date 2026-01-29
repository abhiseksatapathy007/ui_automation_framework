package com.automation.framework.Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper {

	private Connection connection;
	private String dbType;
	private String connectionUrl;
	private String username;
	private String password;

	/**
	 * Constructor to initialize database connection parameters
	 * 
	 * @param dbType      Database type: "postgresql", "mysql", "sqlserver", "oracle"
	 * @param host        Database host/ip
	 * @param port        Database port
	 * @param database    Database name
	 * @param username    Database username
	 * @param password    Database password
	 */
	public DatabaseHelper(String dbType, String host, String port, String database, String username, String password) {
		this.dbType = dbType.toLowerCase();
		this.username = username;
		this.password = password;
		this.connectionUrl = buildConnectionUrl(host, port, database);
	}

	/**
	 * Builds the JDBC connection URL based on database type
	 * 
	 * @param host     Database host
	 * @param port     Database port
	 * @param database Database name
	 * @return JDBC connection URL
	 */
	private String buildConnectionUrl(String host, String port, String database) {
		switch (dbType) {
		case "postgresql":
			return "jdbc:postgresql://" + host + ":" + port + "/" + database;
		case "mysql":
			return "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";
		case "sqlserver":
			return "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + database + ";encrypt=false";
		case "oracle":
			return "jdbc:oracle:thin:@" + host + ":" + port + ":" + database;
		default:
			throw new IllegalArgumentException("Unsupported database type: " + dbType);
		}
	}

	/**
	 * Establishes connection to the database
	 * 
	 * @throws SQLException if connection fails
	 */
	public void connect() throws SQLException {
		try {
			switch (dbType) {
			case "postgresql":
				Class.forName("org.postgresql.Driver");
				break;
			case "mysql":
				Class.forName("com.mysql.cj.jdbc.Driver");
				break;
			case "sqlserver":
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				break;
			case "oracle":
				Class.forName("oracle.jdbc.driver.OracleDriver");
				break;
			}
			connection = DriverManager.getConnection(connectionUrl, username, password);
			System.out.println("Database connection established successfully");
		} catch (ClassNotFoundException e) {
			throw new SQLException("Database driver not found: " + e.getMessage());
		}
	}

	/**
	 * Closes the database connection
	 * 
	 * @throws SQLException if closing connection fails
	 */
	public void disconnect() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
			System.out.println("Database connection closed");
		}
	}

	/**
	 * Executes a SELECT query and returns results as List of Maps
	 * 
	 * @param query SQL SELECT query
	 * @return List of Maps where each Map represents a row with column names as keys
	 * @throws SQLException if query execution fails
	 */
	public List<Map<String, Object>> executeQuery(String query) throws SQLException {
		List<Map<String, Object>> results = new ArrayList<>();
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (resultSet.next()) {
				Map<String, Object> row = new HashMap<>();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metaData.getColumnName(i);
					Object value = resultSet.getObject(i);
					row.put(columnName, value);
				}
				results.add(row);
			}
		}
		return results;
	}

	/**
	 * Executes an UPDATE, INSERT, or DELETE query
	 * 
	 * @param query SQL UPDATE/INSERT/DELETE query
	 * @return Number of rows affected
	 * @throws SQLException if query execution fails
	 */
	public int executeUpdate(String query) throws SQLException {
		try (Statement statement = connection.createStatement()) {
			return statement.executeUpdate(query);
		}
	}

	/**
	 * Executes a prepared statement with parameters
	 * 
	 * @param query      SQL query with placeholders (?)
	 * @param parameters Array of parameter values
	 * @return Number of rows affected for UPDATE/INSERT/DELETE, or ResultSet for SELECT
	 * @throws SQLException if query execution fails
	 */
	public Object executePreparedStatement(String query, Object... parameters) throws SQLException {
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			for (int i = 0; i < parameters.length; i++) {
				preparedStatement.setObject(i + 1, parameters[i]);
			}

			if (query.trim().toUpperCase().startsWith("SELECT")) {
				return preparedStatement.executeQuery();
			} else {
				return preparedStatement.executeUpdate();
			}
		}
	}

	/**
	 * Executes a SELECT query and returns a single value
	 * 
	 * @param query SQL SELECT query that returns a single value
	 * @return Single value from the query result
	 * @throws SQLException if query execution fails
	 */
	public Object executeScalar(String query) throws SQLException {
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {

			if (resultSet.next()) {
				return resultSet.getObject(1);
			}
			return null;
		}
	}

	/**
	 * Executes a SELECT query and returns results as List of Lists (for data providers)
	 * 
	 * @param query SQL SELECT query
	 * @return List of Lists where inner list represents a row
	 * @throws SQLException if query execution fails
	 */
	public List<List<Object>> executeQueryAsList(String query) throws SQLException {
		List<List<Object>> results = new ArrayList<>();
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (resultSet.next()) {
				List<Object> row = new ArrayList<>();
				for (int i = 1; i <= columnCount; i++) {
					row.add(resultSet.getObject(i));
				}
				results.add(row);
			}
		}
		return results;
	}

	/**
	 * Verifies if a record exists in the database
	 * 
	 * @param query SQL SELECT query with WHERE clause
	 * @return true if record exists, false otherwise
	 * @throws SQLException if query execution fails
	 */
	public boolean recordExists(String query) throws SQLException {
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			return resultSet.next();
		}
	}

	/**
	 * Gets the count of records matching the query
	 * 
	 * @param query SQL SELECT COUNT query
	 * @return Count of records
	 * @throws SQLException if query execution fails
	 */
	public int getRecordCount(String query) throws SQLException {
		Object result = executeScalar(query);
		if (result instanceof Number) {
			return ((Number) result).intValue();
		}
		return 0;
	}

	/**
	 * Checks if the database connection is active
	 * 
	 * @return true if connection is active, false otherwise
	 */
	public boolean isConnected() {
		try {
			return connection != null && !connection.isClosed() && connection.isValid(5);
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Gets the connection object (use with caution)
	 * 
	 * @return Connection object
	 */
	public Connection getConnection() {
		return connection;
	}
}

