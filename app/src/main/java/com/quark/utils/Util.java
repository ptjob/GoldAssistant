package com.quark.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Environment;

public class Util {
	/**
	 * @Description: 手机号码验证
	 * @author howe
	 * @date 2014-7-22 下午4:00:37
	 * 
	 */

	public static boolean isMobileNO(String mobiles) {
		if (mobiles == null || (mobiles.trim().equals(""))) {
			return false;
		}
		// 18几的字段全开 ("^((13[0-9])|(15[^4,\\D])|(18[0,1,2,3,5-9]))\\d{8}$");
		Pattern p = Pattern.compile("^((1))\\d{10}$");
		// Pattern p = Pattern
		// .compile("^((13[0-9])|(15[^4,\\D])|(18[^4,\\D]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	// 是否为数字
	public static boolean isNumeric(String str) {
		if (str == null || (str.trim().equals(""))) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	// 判断email格式是否正确
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 校验银行卡卡号
	 * 
	 * @param cardId
	 * @return
	 */
	public static boolean checkBankCard(String cardId) {
		char bit = getBankCardCheckCode(cardId
				.substring(0, cardId.length() - 1));
		if (bit == 'N') {
			return false;
		}
		return cardId.charAt(cardId.length() - 1) == bit;
	}

	/**
	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
	 * 
	 * @param nonCheckCodeCardId
	 * @return
	 */
	public static char getBankCardCheckCode(String nonCheckCodeCardId) {
		if (nonCheckCodeCardId == null
				|| nonCheckCodeCardId.trim().length() == 0
				|| !nonCheckCodeCardId.matches("\\d+")) {
			// 如果传的不是数据返回N
			return 'N';
		}
		char[] chs = nonCheckCodeCardId.trim().toCharArray();
		int luhmSum = 0;
		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
	}

	// 身高为140cm-220cm
	// public static boolean heightCheck(String heightStr){
	// if(heightStr==null||(heightStr.trim().equals(""))){
	// return false;
	// }
	// Pattern p = Pattern.compile("^1[4-9][0-9]|200cm\b$");
	// Matcher m = p.matcher(heightStr);
	// return m.matches();
	// }
	public static boolean heightCheck(String heightStr) {
		if (heightStr == null || (heightStr.trim().equals(""))) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[0-9]*$");
		if (pattern.matcher(heightStr).matches()) {
			int hei = Integer.valueOf(heightStr);
			if ((hei > 140) && (hei < 200)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean isUserName(String mobiles) {
		Pattern p = Pattern.compile("^[A-Za-z\u4e00-\u9fa5]{2,15}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 检查是否为空 为空返回false,不为空返回true
	 * 
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {
		if (str != null && (!str.trim().equals(""))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检查是否存在SDCard
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static String DateToString(Date date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(date);
		} else {
			return "";
		}
	}

	/**
	 * 通过年月日获取 年龄
	 * 
	 * @param brithday
	 * @return
	 */
	public static String getCurrentAgeByBirthdate(String brithday) {

		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = new Date();
		java.util.Date mydate = null;
		try {
			mydate = myFormatter.parse(brithday);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000)
				+ 1;

		String year = new java.text.DecimalFormat("#").format(day / 365f);

		return year;
	}

	public static boolean isIdCard(String idcard) {
		// 定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
		Pattern idNumPattern = Pattern
				.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
		// 通过Pattern获得Matcher
		Matcher m = idNumPattern.matcher(idcard);

		return m.matches();
	}

	// 工作内容 10-200字
	public static boolean isInfook(String str) {
		if (str != null && (!str.trim().equals("")) && (str.length() > 10)
				&& (str.length() < 1000)) {
			return true;
		} else {
			return false;
		}
	}

	// 详细地址 4-20字
	public static boolean isAddressDetail(String str) {
		if (str != null && (!str.trim().equals("")) && (str.length() > 3)
				&& (str.length() < 21)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isName(String str){
		Pattern p = Pattern.compile("^[\\u4E00-\\u9FFF]+$");
		Matcher matcher = p.matcher(str);
		return matcher.matches();
	}
}
