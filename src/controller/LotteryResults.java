package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import db.DBConnect;
import models.Config;
import models.Log;
import models.LotteryItem;

public class LotteryResults {
	public static final int G8 = 0;
	public static final int G7 = 4;
	public static final int G6 = 8;
	public static final int G5 = 12;
	public static final int G4 = 16;
	public static final int G3 = 20;
	public static final int G2 = 24;
	public static final int G1 = 28;
	public static final int GDB = 32;

	public static void setPrize(int index, String prize, LotteryItem lotteryItem) {
		switch (index) {
		case G8: {
			lotteryItem.setPrize_eight(prize.substring(1, prize.length()));
			break;
		}
		case G7: {
			lotteryItem.setPrize_seven(prize.substring(1, prize.length()));
			break;
		}
		case G6: {
			lotteryItem.setPrize_six(prize.substring(1, prize.length()));
			break;
		}
		case G5: {
			lotteryItem.setPrize_five(prize.substring(1, prize.length()));
			break;
		}
		case G4: {
			lotteryItem.setPrize_four(prize.substring(1, prize.length()));
			break;
		}
		case G3: {
			lotteryItem.setPrize_three(prize.substring(1, prize.length()));
			break;
		}
		case G2: {
			lotteryItem.setPrize_two(prize.substring(1, prize.length()));
			break;
		}
		case G1: {
			lotteryItem.setPrize_one(prize.substring(1, prize.length()));
			break;
		}
		case GDB: {
			lotteryItem.setPrize_special(prize.substring(1, prize.length()));
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + index);
		}
	}

	public static List<LotteryItem> getData(Config config) throws IOException {
//		6.1 Tạo List LotteryItem rỗng
		List<LotteryItem> lotteryItemList = new ArrayList<LotteryItem>();
		String source = config.getSource();
		Log logConnect = new Log();
		try {
//			6.2 Kết nối với source bằng JSoup
			Response response = Jsoup.connect(source).execute();
//			6.3 Kiểm tra trạng thái kết nối
			if (response.statusCode() == 200) {
				System.out.println("Kết nối thành công. Mã trạng thái HTTP: " + response.statusCode());
//				6.4.1 Ghi log kết nối source thành công
				logConnect.setTrackingDateTime(LocalDateTime.now());
				logConnect.setSource(config.getSource());
				logConnect.setConnectStatus(1);
				logConnect.setDestination(config.getPathToSave());
				logConnect.setPhase(config.getPhase());
				logConnect.setResult("Thành công");
				logConnect.setDetail("Kết nối trang web thành công");
				logConnect.setDelete(false);
				LogController.insertLog(DBConnect.getInstance().get(), logConnect);
//				6.5 Lấy đoạn HTML chứa dữ liệu cần lấy từ source
				Element divCommon = response.parse().selectFirst("#load_kq_mn_0");
//	    		6.6 Extract data từ HTML vào List
				Elements provinceElements = divCommon.select("a");
				for (Element e : provinceElements) {
					LotteryItem lotteryItem = new LotteryItem();
					lotteryItem
							.setProvince(new String(e.text().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
					lotteryItemList.add(lotteryItem);
				}
				Elements rewards = divCommon.select("td");
				Element date = divCommon.selectXpath("//*[@id=\"content\"]/div[1]/div[1]/div[3]/div[1]/div/a[3]")
						.get(0);
				for (int i = 0; i < (lotteryItemList.size() + 1) * 9; i += 4) {
					for (int j = 1; j <= lotteryItemList.size(); j++) {
						String prize = "";
						prize = rewards.get(j + i).text();
						setPrize(i, prize, lotteryItemList.get(j - 1));
						lotteryItemList.get(j - 1).setDate(date.text().substring(10));
					}
				}
			}
		} catch (HttpStatusException e) {
			System.out.println("Lỗi kết nối. Mã trạng thái HTTP: " + e.getStatusCode());
//			6.4.2 Ghi log lỗi kết nối source
			logConnect.setTrackingDateTime(LocalDateTime.now());
			logConnect.setSource(config.getSource());
			logConnect.setConnectStatus(0);
			logConnect.setDestination(config.getPathToSave());
			logConnect.setPhase("source to csv");
			logConnect.setResult("Thất bại");
			logConnect.setDetail("Kết nối đến trang web thất bại. Lỗi " + e.getStatusCode());
			logConnect.setDelete(false);
			LogController.insertLog(DBConnect.getInstance().get(), logConnect);
			
		}

		return lotteryItemList;

	}

	public static void writeDataToCSV(List<LotteryItem> lotteryItemList, String csvFilePath) {
		List<String[]> data = new ArrayList<String[]>();
		String[] title = { "province", "prize_eight", "prize_seven", "prize_six", "prize_five", "prize_four",
				"prize_three", "prize_two", "prize_one", "prize_special", "date" };
		data.add(title);
		for (int i = 0; i < lotteryItemList.size(); i++) {
			data.add(lotteryItemList.get(i).toArray());
		}
		String ddmmyyyy = lotteryItemList.get(0).getDate().replaceAll("-", "");
		String area = "xsmn";
		String path = csvFilePath + "\\" + ddmmyyyy + "_" + area + ".csv";

		try (FileWriter fileWriter = new FileWriter(path)) {
			for (String[] row : data) {
				for (int i = 0; i < row.length; i++) {
					fileWriter.append(row[i]);
					if (i < row.length - 1) {
						fileWriter.append(",");
					}
				}
				fileWriter.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
