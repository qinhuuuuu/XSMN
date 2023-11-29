package controller;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import models.Config;
import models.Log;

public class DBConnect {
	String url = "jdbc:mysql://localhost:3306/control";
	String user = "root";
	String pass = "";
	Connection connect;

	static DBConnect install;

	// 1. kết nối với MySQL
	private DBConnect() {
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

	// tạo đối tượng statement
	public Statement getStatement() {
		if (connect == null)
			return null;
		try {
			return connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException e) {
			return null;
		}
	}

	private void connect() throws SQLException, ClassNotFoundException {
		if (connect == null || connect.isClosed()) {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect = DriverManager.getConnection(url, user, pass);
		}
	}

	public Connection get() {
		try {
			connect();
			return connect;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}


	public int insertConfig(Config c) {
		int generatedId = -1;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		try {
			Connection connection = DBConnect.getInstance().get();
			String sql = "INSERT INTO `config` (source, source_name, area, path_to_save, file_name_format, file_type, time_get_data,`interval`, create_date, update_date, description, status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, c.getSource());
			statement.setString(2, c.getSourceName());
			statement.setString(3, c.getArea());
			statement.setString(4, c.getPathToSave());
			statement.setString(5, c.getFileNameFormat());
			statement.setString(6, c.getFileType());
			statement.setString(7, c.getTimeGetData().format(formatter));
			statement.setInt(8, c.getInterval());
			statement.setString(9, c.getCreateDate().format(formatter));
			statement.setString(10, c.getUpdateDate() != null ? c.getUpdateDate().format(formatter) : null);
			statement.setString(11, c.getDescription());
			statement.setInt(12, c.getStatus());
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

	public Config getConfig() throws SQLException {
		Config res = null;
		String sql = "SELECT * FROM `config` WHERE status=?";
		try {
			Connection connection = DBConnect.getInstance().get();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, 1);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				res = new Config();
				res.setId(rs.getInt(1));
				res.setSource(rs.getString(2));
				res.setSourceName(rs.getString(3));
				res.setArea(rs.getString(4));
				res.setPathToSave(rs.getString(5));
				res.setFileNameFormat(rs.getString(6));
				res.setFileType(rs.getString(7));
				res.setTimeGetData(
						LocalDateTime.parse(rs.getString(8), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				res.setInterval(rs.getInt(9));
				res.setCreateDate(
						LocalDateTime.parse(rs.getString(10), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				res.setUpdateDate(rs.getString(11) != null
						? LocalDateTime.parse(rs.getString(11), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
						: null);
				res.setDescription(rs.getString(12));
				res.setStatus(rs.getInt(13));
				break;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return res;
	}
	public int insertLog(Log log) {
		int generatedId = -1;
		DateTimeFormatter  formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		try {
			Connection connection = DBConnect.getInstance().get();
			String sql = "INSERT INTO `log` ( tracking_date_time, source, connect_status, destination, phase, result, detail, is_delete) VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, log.getTrackingDateTime() != null ? log.getTrackingDateTime().format(formatterDateTime) : null);
			statement.setString(2, log.getSource());
			statement.setInt(3, log.getConnectStatus());
			statement.setString(4, log.getDestination());
			statement.setString(5, log.getPhase());
			statement.setString(6, log.getResult());
			statement.setString(7, log.getDetail());
			statement.setBoolean(8, log.isDelete());
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
