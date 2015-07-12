package com.carson.constant;

import java.security.MessageDigest;

public class JiaoyanUtil {
	/**
	 * 校验验证码
	 * 
	 */
	public static boolean vertifyCode(String code) {
		int sum = 0;
		sum = getNum(code.substring(0, 1)) + 2 * getNum(code.substring(1, 2))
				+ 3 * getNum(code.substring(2, 3)) + 4
				* getNum(code.substring(3, 4)) + 5
				* getNum(code.substring(4, 5));
		int sixNum = getNum(code.substring(5, 6));
		if (sum % 9 == sixNum) {
			return true;
		}
		return false;
	}

	public static int getNum(String s) {
		if (s != null && !"".equals(s)) {
			int temp = 0;
			try {
				temp = Integer.parseInt(s);
				return temp;
			} catch (Exception e) {
				temp = 0;
			}
		}

		return 0;
	}

	public static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
