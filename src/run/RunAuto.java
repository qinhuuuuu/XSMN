package run;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

import controller.ConfigController;
import controller.LogController;
import controller.LotteryResults;
import db.DBConnect;
import db.DBProperties;
import models.Config;
import models.Log;
import models.LotteryItem;

public class RunAuto {
	public static String formatDate(LocalDateTime date) {
		return date.getDayOfMonth() + "" + date.getMonthValue() + "" + date.getYear();
	}

	public static void main(String[] args) throws SQLException, IOException {
		Log log = new Log();
//		2. Lấy thông tin cấu hình database (Properties) từ file dbControl.properties
		Properties prop = new Properties();
		prop.load(DBProperties.class.getClassLoader().getResourceAsStream("dbControl.properties"));
		DBProperties.setProperties(prop);
//		3. Kết nối DB Control
		Connection connection = null;
		int retryCount = 0;
		while (retryCount < 3) {
			connection = DBConnect.getInstance().get();
			if (connection != null) {
//		4. Lấy config(phase="source to csv", description="GET_DATA_AUTO", status=1) trong config table
				Config config = ConfigController.getConfig(connection, Config.AUTO);
				System.out.println(config);
//		5. Ghi log bắt đầu lấy dữ liệu
				log.setTrackingDateTime(LocalDateTime.now());
				log.setSource(config.getSource());
				log.setConnectStatus(1);
				log.setDestination(config.getPathToSave());
				log.setPhase(config.getPhase());
				log.setResult("Thành công");
				log.setDetail("Bắt đầu lấy dữ liệu từ " + config.getSource());
				log.setDelete(false);
				LogController.insertLog(connection, log);
//		6. Lấy dữ liệu
				List<LotteryItem> lotteryItems = LotteryResults.getData(config);
//		4.7 Kiểm tra xem List có rỗng hay không
				if (lotteryItems.size() != 0) {
//			4.7.1 Ghi log lấy dữ liệu thành công
					log.setTrackingDateTime(LocalDateTime.now());
					log.setResult("Thành công");
					log.setDetail("Lấy dữ liệu ngày " + lotteryItems.get(0).getDate() + " thành công");
					log.setDelete(false);
					LogController.insertLog(connection, log);

//			5. Load dữ liệu vào file CSV
					LotteryResults.writeDataToCSV(lotteryItems, config.getPathToSave());
//			6. Ghi log lấy load file csv thành công
					log.setTrackingDateTime(LocalDateTime.now());
					log.setResult("Thành công");
					log.setDetail("Load file CSV ngày " + lotteryItems.get(0).getDate() + " thành công");
					log.setDelete(false);
					LogController.insertLog(connection, log);

				} else {
//			4.7.2 Ghi log lấy dữ liệu thất bại
					log.setTrackingDateTime(LocalDateTime.now());
					log.setConnectStatus(0);
					log.setResult("Lấy dữ liệu thất bại");
					log.setDetail("Lỗi không có dữ liệu để load file");
					log.setDelete(false);
					LogController.insertLog(connection, log);
				}
				break;
			}
//			 Ghi log kết nối DB thất bại
			log.setTrackingDateTime(LocalDateTime.now());
			log.setConnectStatus(0);
			log.setResult("Kết nối DB thất bại lần " + retryCount);
			log.setDetail("Lỗi không kết nối được đến data base lần thứ " + retryCount);
			log.setDelete(false);
			LogController.insertLog(connection, log);
			retryCount++;
			LogController.writeLogToCSV(log, "D://ErrorsDW", formatDate(log.getTrackingDateTime()), LotteryItem.AREA);
			System.out.println("Lỗi kết nối DB. Đang thử lại lần thứ " + retryCount);
//			 Ghi log đang thử kết nối lại
			log.setTrackingDateTime(LocalDateTime.now());
			log.setConnectStatus(0);
			log.setResult("Đang thử lại lần thứ " + retryCount);
			log.setDetail("Đang thử lại kết nối lần thứ " + retryCount);
			log.setDelete(false);
			LogController.writeLogToCSV(log, "D://ErrorsDW", formatDate(log.getTrackingDateTime()), LotteryItem.AREA);
		}

		if (connection == null) {
//			 Ghi log vào D://ErrorsDW không thể kết nối DB sau 3 lần
			log.setTrackingDateTime(LocalDateTime.now());
			log.setConnectStatus(0);
			log.setResult("Lỗi kết nối DB" + retryCount);
			log.setDetail("Không thể kết nối DB sau " + retryCount + " lần");
			log.setDelete(false);
			LogController.writeLogToCSV(log, "D://ErrorsDW", formatDate(log.getTrackingDateTime()), LotteryItem.AREA);
		}
	}
}
