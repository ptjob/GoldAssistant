/*
 * 文  件  名:  GsonUtil.java
 * 创建日期:  2014-11-4/下午11:54:09
 * 版       权:  Royal.k.peng@gmail.com, All rights reserved
 * 作       者:  Royal
 * 座 右  铭:  Never give up, adhere to in the end.
 */
package com.quark.utils;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * @author Royal
 * @Ctime 2014-11-4/下午11:54:09
 * @DESC 用来把接口中的JSONObject对象转换为Java中的对象
 */
public class GsonUtil {

	public static <T> T getObj4String(String gsonStr, TypeToken<T> typeToken,
			FieldNamingStrategy fieldNamingStrategy) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		if (null != fieldNamingStrategy) {
			gsonBuilder.setFieldNamingStrategy(fieldNamingStrategy);
		}
		Gson gson = gsonBuilder.create();
		T response = null;
		try {
			response = gson.fromJson(gsonStr, typeToken.getType());
		} catch (JsonSyntaxException exception) {
			/*
			 * Logger.e("has a exception during parse. msg = " +
			 * exception.toString());
			 */
		}
		return response;
	}

	/**
	 * {url} /app/Banner/info
	 * 
	 * @param jsonStr
	 * @return
	 */
	// public static List<Bannder> bannerInfo(String jsonStr) {
	// ObjectListResponse<Bannder> response = getObj4String(jsonStr, new
	// TypeToken<ObjectListResponse<Bannder>>() {
	// }, new ObjectListResponseNamingStategy("banner"));
	// return response.getList();
	// }
	//
	// /**
	// * 获取专题名称
	// *
	// * @param jsonStr
	// * @return
	// */
	// public static Bannder bannerTopic(String jsonStr) {
	// ObjectOneObjectResponse<Bannder> response = getObj4String(jsonStr, new
	// TypeToken<ObjectOneObjectResponse<Bannder>>() {
	// }, new ObjectOneObjectResponseNamingStategy("topic"));
	// return response.getObj();
	// }
	//
	// /*1.1.2 公司简介及电话信息
	// *
	// * */
	// public static Company companInfo(String jsonStr) {
	// ObjectOneObjectResponse<Company> response = getObj4String(jsonStr, new
	// TypeToken<ObjectOneObjectResponse<Company>>() {
	// }, new ObjectOneObjectResponseNamingStategy("company"));
	// return response.getObj();
	// }

}
