package models;


public class LotteryItem {
	private String province;
	private String prize_eight;
	private String prize_seven;
	private String prize_six;
	private String prize_five;
	private String prize_four;
	private String prize_three;
	private String prize_two;
	private String prize_one;
	private String prize_special;
	private String date;

	public LotteryItem(String province, String prize_eight, String prize_seven, String prize_six, String prize_five,
			String prize_four, String prize_three, String prize_two, String prize_one, String prize_special,
			String date) {
		super();
		this.province = province;
		this.prize_eight = prize_eight;
		this.prize_seven = prize_seven;
		this.prize_six = prize_six;
		this.prize_five = prize_five;
		this.prize_four = prize_four;
		this.prize_three = prize_three;
		this.prize_two = prize_two;
		this.prize_one = prize_one;
		this.prize_special = prize_special;
		this.date = date;
	}

	public LotteryItem() {
		super();
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPrize_eight() {
		return prize_eight;
	}

	public void setPrize_eight(String prize_eight) {
		this.prize_eight = prize_eight;
	}

	public String getPrize_seven() {
		return prize_seven;
	}

	public void setPrize_seven(String prize_seven) {
		this.prize_seven = prize_seven;
	}

	public String getPrize_six() {
		return prize_six;
	}

	public void setPrize_six(String prize_six) {
		this.prize_six = prize_six;
	}

	public String getPrize_five() {
		return prize_five;
	}

	public void setPrize_five(String prize_five) {
		this.prize_five = prize_five;
	}

	public String getPrize_four() {
		return prize_four;
	}

	public void setPrize_four(String prize_four) {
		this.prize_four = prize_four;
	}

	public String getPrize_three() {
		return prize_three;
	}

	public void setPrize_three(String prize_three) {
		this.prize_three = prize_three;
	}

	public String getPrize_two() {
		return prize_two;
	}

	public void setPrize_two(String prize_two) {
		this.prize_two = prize_two;
	}

	public String getPrize_one() {
		return prize_one;
	}

	public void setPrize_one(String prize_one) {
		this.prize_one = prize_one;
	}

	public String getPrize_special() {
		return prize_special;
	}

	public void setPrize_special(String prize_special) {
		this.prize_special = prize_special;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return province + "," + prize_eight + "," + prize_seven + "," + prize_six + "," + prize_five + "," + prize_four
				+ "," + prize_three + "," + prize_two + "," + prize_one + "," + prize_special + "," + date;
	}

	public String[] toArray() {
		return new String[] { province, prize_eight, prize_seven, prize_six, prize_five, prize_four, prize_three,
				prize_two, prize_one, prize_special, date };
	}

}
