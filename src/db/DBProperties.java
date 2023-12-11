package db;

import java.util.Properties;

public class DBProperties {
	private static Properties prop;

//	static {
//		try {
//			prop.load(DBProperties.class.getClassLoader().getResourceAsStream("dbControl.properties"));
//		} catch (IOException ioException) {
//			ioException.printStackTrace();
//		}
//	}

	public static void setProperties(Properties prop) {
		DBProperties.prop = prop;
	}

	public static String getDbHost() {
		return prop.get("db.host").toString();
	}

	public static String getDbPort() {
		return prop.get("db.port").toString();
	}

	public static String getUsername() {
		return prop.get("db.username").toString();
	}

	public static String getPassword() {
		return prop.get("db.password").toString();
	}

	public static String getDbOption() {
		return prop.get("db.options").toString();
	}

	public static String getDbName() {
		return prop.get("db.databaseName").toString();
	}

}
