package db;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import models.Config;
import models.Log;

public class DBConnect {
	String url = "jdbc:mysql://" + DBProperties.getDbHost() + ":" + DBProperties.getDbPort() + "/"
			+ DBProperties.getDbName();
	String user = DBProperties.getUsername();
	String pass = DBProperties.getPassword();
	Connection connect;

	static DBConnect install;

	public DBConnect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect = DriverManager.getConnection(url, user, pass);
		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static DBConnect getInstance() {
		if (install == null)
			install = new DBConnect();
		return install;
	}

	public Statement getStatement() {
		if (connect == null)
			return null;
		try {
			return connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException e) {
			return null;
		}
	}

//	private void connect() throws SQLException, ClassNotFoundException {
//		if (connect == null || connect.isClosed()) {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			connect = DriverManager.getConnection(url, user, pass);
//		}
//	}

	public Connection get() {
//		try {
//			connect();
			return connect;
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException(e);
//		}
	}

//	public Config getConfigFrommDB(String description) {
//		try {
//			CallableStatement callableStatement = DBConnect.getInstance().get().prepareCall("{call getConfig(?,?,?)}",
//					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//			callableStatement.setString(1, "source to csv");
//			callableStatement.setInt(2, 1);
//			callableStatement.setString(3, description);
//			callableStatement.execute();
//			ResultSet rs = callableStatement.getResultSet();
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public int insertConfig(Config c) {
		int generatedId = -1;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		try {
			Connection connection = DBConnect.getInstance().get();
			String sql = "INSERT INTO `config` (phase,source, source_name, area, path_to_save, file_name_format, file_type, time_get_data,`interval`, create_date, update_date, description, status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, c.getPhase());
			statement.setString(2, c.getSource());
			statement.setString(3, c.getSourceName());
			statement.setString(4, c.getArea());
			statement.setString(5, c.getPathToSave());
			statement.setString(6, c.getFileNameFormat());
			statement.setString(7, c.getFileType());
			statement.setString(8, c.getTimeGetData().format(formatter));
			statement.setInt(9, c.getInterval());
			statement.setString(10, c.getCreateDate().format(formatter));
			statement.setString(11, c.getUpdateDate() != null ? c.getUpdateDate().format(formatter) : null);
			statement.setString(11, c.getDescription());
			statement.setInt(13, c.getStatus());
			statement.executeUpdate();

			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				generatedId = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return generatedId;
	}

	

	
}
