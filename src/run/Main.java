package run;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import controller.DBConnect;
import controller.LotteryResults;
import models.Config;
import models.Log;
import models.LotteryItem;

public class Main {
	public static void main(String[] args) throws SQLException, IOException {
//		1. Kết nối DB Control
		DBConnect connection = DBConnect.getInstance();
//		2. Lấy dữ liệu trong config table
		Config config = connection.getConfig();

		Log log = new Log();
//		3. Ghi log bắt đầu lấy data
		log.setTrackingDateTime(LocalDateTime.now());
		log.setSource(config.getSource());
		log.setConnectStatus(1);
		log.setDestination(config.getPathToSave());
		log.setPhase("source to csv");
		log.setResult("Thành công");
		log.setDetail("Bắt đầu lấy dữ liệu từ source");
		log.setDelete(false);
		connection.insertLog(log);
//		4. Lấy dữ liệu
		List<LotteryItem> lotteryItems = LotteryResults.getData(config);
//		4.7 Kiểm tra xem List có rỗng hay không
		if (lotteryItems.size() != 0) {
//			4.7.1 Ghi log lấy dữ liệu thành công
			log.setTrackingDateTime(LocalDateTime.now());
			log.setSource(config.getSource());
			log.setConnectStatus(1);
			log.setDestination(config.getPathToSave());
			log.setPhase("source to csv");
			log.setResult("Thành công");
			log.setDetail("Lấy dữ liệu ngày " + lotteryItems.get(0).getDate()+ " thành công");
			log.setDelete(false);
			connection.insertLog(log);

//			5. Load dữ liệu vào file CSV
			LotteryResults.writeDataToCSV(lotteryItems, config.getPathToSave());
//			6. Ghi log lấy load file csv thành công
			log.setTrackingDateTime(LocalDateTime.now());
			log.setSource(config.getSource());
			log.setConnectStatus(1);
			log.setDestination(config.getPathToSave());
			log.setPhase("source to csv");
			log.setResult("Thành công");
			log.setDetail("Load file CSV ngày " + lotteryItems.get(0).getDate() + " thành công");
			log.setDelete(false);
			connection.insertLog(log);

		} else {
//			4.7.2 Ghi log lấy dữ liệu thất bại
			log.setTrackingDateTime(LocalDateTime.now());
			log.setSource(config.getSource());
			log.setConnectStatus(0);
			log.setDestination(config.getPathToSave());
			log.setPhase("source to csv");
			log.setResult("Lấy dữ liệu thất bại");
			log.setDetail("Lỗi không có dữ liệu để load file");
			log.setDelete(false);
			connection.insertLog(log);

		}
	}
}
