package run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

import controller.LotteryResults;
import db.DBConnect;
import db.DBProperties;
import models.Config;
import models.Log;
import models.LotteryItem;

public class RunByManualDateTyping {

//	public static void main(String[] args) throws SQLException, IOException {
////		2. Lấy thông tin cấu hình database (Properties) 
//		Properties prop = new Properties();
//		prop.load(DBProperties.class.getClassLoader().getResourceAsStream("dbControl.properties"));
//		DBProperties.setProperties(prop);
//		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//		System.out.println("Nhap ngay xo so (FORMAT: DD-MM-YYYY): ");
//		String date = reader.readLine();
//		System.out.println(date);
////		1. Kết nối DB Control
//		DBConnect connection = DBConnect.getInstance();
////		2. Lấy config(phase=source to csv, status=1) trong config table
//		Config config = connection.getConfig("Config lấy dữ liệu thủ công theo ngày");
//		config.setSource(config.getSource().replace("dd-mm-yyyy", date));;
//		Log log = new Log();
////		3. Ghi log bắt đầu lấy data
//		log.setTrackingDateTime(LocalDateTime.now());
//		log.setSource(config.getSource());
//		log.setConnectStatus(1);
//		log.setDestination(config.getPathToSave());
//		log.setPhase(config.getPhase());
//		log.setResult("Thành công");
//		log.setDetail("Bắt đầu lấy dữ liệu từ " + config.getSource());
//		log.setDelete(false);
//		connection.insertLog(log);
////		4. Lấy dữ liệu
//		List<LotteryItem> lotteryItems = LotteryResults.getData(config);
////		4.7 Kiểm tra xem List có rỗng hay không
//		if (lotteryItems.size() != 0) {
////			4.7.1 Ghi log lấy dữ liệu thành công
//			log.setTrackingDateTime(LocalDateTime.now());
//			log.setResult("Thành công");
//			log.setDetail("Lấy dữ liệu ngày " + lotteryItems.get(0).getDate() + " thành công");
//			log.setDelete(false);
//			connection.insertLog(log);
//
////			5. Load dữ liệu vào file CSV
//			LotteryResults.writeDataToCSV(lotteryItems, config.getPathToSave());
////			6. Ghi log lấy load file csv thành công
//			log.setTrackingDateTime(LocalDateTime.now());
//			log.setResult("Thành công");
//			log.setDetail("Load file CSV ngày " + lotteryItems.get(0).getDate() + " thành công");
//			log.setDelete(false);
//			connection.insertLog(log);
//
//		} else {
////			4.7.2 Ghi log lấy dữ liệu thất bại
//			log.setTrackingDateTime(LocalDateTime.now());
//			log.setConnectStatus(0);
//			log.setResult("Lấy dữ liệu thất bại");
//			log.setDetail("Lỗi không có dữ liệu để load file");
//			log.setDelete(false);
//			connection.insertLog(log);
//
//		}
//	}
}
