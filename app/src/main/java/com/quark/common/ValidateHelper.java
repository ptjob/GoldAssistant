package com.quark.common;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 校验帮助类
 * 
 * @author dengjialuo
 * 
 */
public final class ValidateHelper {
	private ValidateHelper() {
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 *            字符串
	 * @return 是否为空
	 */
	public static boolean isEmptyString(String str) {
		return str == null || str.trim().length() == 0 || "null".equals(str);
	}

	/**
	 * 判断字符串是否不为空
	 * 
	 * @param str
	 *            字符串
	 * @return 是否不为空
	 */
	public static boolean isNotEmptyString(String str) {
		return !isEmptyString(str);
	}

	/**
	 * 判断是否为空集合
	 * 
	 * @param collection
	 *            集合
	 * @return 是否为空集合
	 */
	public static boolean isEmptyCollection(Collection collection) {
		return null == collection || collection.isEmpty();
	}

	/**
	 * 判断是否不为空集合
	 * 
	 * @param collection
	 *            集合
	 * @return 是否不为空集合
	 */
	public static boolean isNotEmptyCollection(Collection collection) {
		return !isEmptyCollection(collection);
	}

	/**
	 * 判断是否为空Map
	 * 
	 * @param map
	 *            Map
	 * @return 是否为空Map
	 */
	public static boolean isEmptyMap(Map map) {
		return null == map || map.isEmpty();
	}

	/**
	 * 判断是否不为空Map
	 * 
	 * @param map
	 *            Map
	 * @return 是否不为空Map
	 */
	public static boolean isNotEmptyMap(Map map) {
		return !isEmptyMap(map);
	}

	/**
	 * 判断是否为空数组
	 * 
	 * @param arrayObj
	 *            数组
	 * @return 是否为空数组
	 */
	public static boolean isEmptyArray(Object arrayObj) {
		if (null == arrayObj) {
			return true;
		}

		Class clazz = arrayObj.getClass();

		if (!clazz.isArray()) {
			return true;
		}

		if (Array.getLength(arrayObj) == 0) {
			return true;
		}

		return false;
	}

	/**
	 * 去掉空格 trim
	 * 
	 * @param text
	 *            要操作的字符串
	 * @return String 去掉两边多余空格的字符串。当text为null的时候返回null
	 */
	public static String trim(String text) {
		return text == null ? null : text.trim();
	}

	/**
	 * 比较两字符串，在忽略空格的时候，是否相同
	 * 
	 * @param str1
	 * @param str2
	 * @return boolean 当去掉空格后相等则返回true，其他返回false.如果两个字符串都为null时，也返回true
	 */
	public static boolean isEqualsIgnoreBlank(String str1, String str2) {
		if (str1 == null) {
			return str2 == null ? true : false;
		} else {
			return str1.trim().equals(trim(str2));
		}
	}

	/**
	 * 是否为URI字符串(即是否以content://开头)
	 * 
	 * @param str
	 * @return boolean true表示是URI字符串
	 */
	public static boolean isURI(String str) {
		if (isEmptyString(str)) {
			return false;
		}

		return str.startsWith("content://");
	}

	/**
	 * 从一个字符串中获取文件名 获取原则如果: http://xdfd/xfds/sfsd/sfsd.xxx ==> sfsd
	 * 
	 * @param str
	 * @return null 不符合文件名的规范如: http://xdfd/xfds/sfsd/sfsd ,否则 返回 文件名 String
	 */
	public static String getFileName(String str) {
		if (isEmptyString(str)) {
			return null;
		}

		int start = str.lastIndexOf('/');
		start++;

		int end = str.lastIndexOf('.');

		if (end <= start) {
			return null;
		}

		return str.substring(start, end);
	}

	/**
	 * 获取系统时间的日志文本
	 * 
	 * @return String
	 */
	public static String getSystemTimeLogText() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"[yyyy-MM-dd HH:mm:ss.SSS]");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		return formatter.format(curDate);
	}

	/**
	 * 获取系统时间的文本
	 * 
	 * @return String
	 */
	public static String getSystemTimeText() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		return formatter.format(curDate);
	}

	/**
	 * 获取某整数的数字个数
	 */
	public static int getNumCount(int number) {
		if (number < 0) {
			return 0;
		} else if (number <= 9) {
			return 1;
		}

		int result = 1;

		int tempNumber = number;

		while (tempNumber >= 10) {
			result++;

			// 大于0继续
			tempNumber /= 10;
		}

		return result;
	}

	/**
	 * 获取某整数的格式化字符串
	 */
	public static String getFormatString(int number, int length) {
		int size = getNumCount(number);

		if (size >= length) {
			return String.valueOf(number);
		} else {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < length - size; i++) {
				sb.append("0");
			}

			sb.append(String.valueOf(number));

			return sb.toString();
		}
	}

	/**
	 * 根据显示宽度 获取最大显示字符串
	 * 
	 * @param title
	 *            应用全名称
	 * @param showWidth
	 *            最大显示的宽度
	 * @param titleTextSize
	 *            显示字体大小
	 * @return 返回最大显示字符串，从0开始截取
	 */
	public static String getCutTitleName(String title, int showWidth,
			float titleTextSize) {
		int titleLen = title.length();
		Rect rect = new Rect();
		Paint paint = new Paint();
		paint.setTextSize(titleTextSize);

		// 每次去掉最后一个字符，检测是否超出最大显示宽度
		String str = null;
		for (int i = 0; i < titleLen; i++) {
			str = title.substring(0, titleLen - i);
			paint.getTextBounds(str, 0, str.length(), rect);
			if (rect.width() < showWidth) {
				return str;
			}
		}
		return "NotName";
	}

	/**
	 * 获取字符串所在矩形，得到宽高
	 * 
	 * @param text
	 *            字符串
	 * @param paint
	 *            画笔
	 * @param textSize
	 *            字体大小
	 * */
	public static Rect getTextRec(String text, Paint paint, float textSize) {
		Rect rect = new Rect();
		if (paint != null && text != null) {
			if (0 < textSize) {
				paint.setTextSize(textSize);
			}
			paint.getTextBounds(text, 0, text.length(), rect);
		}
		return rect;
	}

	/**
	 * 如果内容为null，则赋值为""
	 * 
	 * @param content
	 * @return
	 */
	public static String getContentText(String content) {
		if (isNotEmptyString(content)) {
			return content;
		}
		return "";
	}

	/**
	 * 匹配手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 判断网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;

	}
	/**
	 * 判断GPS是否开启
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isGPS(Context context)
	{
		LocationManager lmLocationManager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		
		Boolean isGPS=lmLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		
		return isGPS;
	}

	/**
	 * 判断文件夹不存在便创建
	 */
	public static boolean isExistDir(String path) {
		File file = new File(path);

		if (file.exists()) {
			return true;
		}

		file.mkdirs();
		return false;
	}
}
