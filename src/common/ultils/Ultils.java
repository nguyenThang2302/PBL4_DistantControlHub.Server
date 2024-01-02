package common.ultils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class Ultils {
	public static java.sql.Date convertStringToDate(String dateString) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date utilDate = dateFormat.parse(dateString);
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		return sqlDate;
	}
	
	public String generateVerifyCode() {
		DecimalFormat df = new DecimalFormat("000000");
		Random ran = new Random();
		String code = df.format(ran.nextInt(1000000));
		return code;
	}
}
