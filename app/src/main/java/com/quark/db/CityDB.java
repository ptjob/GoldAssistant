package com.quark.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CityDB {

	public static ArrayList<String> getCitys(Context context, String city) {
		SQLiteDatabase mDbCity;
		ArrayList<String> mProvinces;// 城市列表
		ArrayList<String> mCities;
		ArrayList<String> mCountries;
		int tempCityId = 1;
		mDbCity = CityDatabase.openDatabase(context);
		// 查找 省级
		mProvinces = new ArrayList<String>();
		String sql = "select * from area where area_parent_id =0";
		Cursor cursor = mDbCity.rawQuery(sql, null);
		// 遍历Cursor
		while (cursor.moveToNext()) {
			String area_name = cursor.getString(cursor
					.getColumnIndex("area_name"));
			// Log.e("tran", "privence area_name=" + area_name);
			mProvinces.add(area_name);
		}

		// 查找 市级
		// mCities = new ArrayList<String>();
		// sql = "select * from area where area_name like '%?%'";
		// cursor = mDbCity.rawQuery(sql, new String[] { city});
		// // 遍历Cursor
		// while (cursor.moveToNext()) {
		// String area_name =
		// cursor.getString(cursor.getColumnIndex("area_name"));
		// //Log.i("tran1", "city area_name=" + area_name);
		// mCities.add(area_name);
		// }

		// 查找 区县级
		mCountries = new ArrayList<String>();
		// 接口要求 去掉市字
		String tempCity = city;
		if (city.endsWith("市")) {
			tempCity = city.substring(0, city.length() - 1);
		}
		mCountries.add("全" + tempCity);

		// ********************** 获取市的编号********************************
		// **********北京、上海、重庆、天津4个直辖市要特殊处理******************************

		if ("北京".equals(city) || "上海".equals(city) || "重庆".equals(city)
				|| "天津".equals(city)) {
			String carson_city = city + "市";
			sql = "select * from area where area_name like '%" + carson_city
					+ "%'";
			cursor = mDbCity.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				tempCityId = cursor.getInt(cursor.getColumnIndex("area_id"));
				// Log.e("ewai_city", "citypicker area_id=" + tempCityId);
			}
		} else {
			sql = "select * from area where area_name like '%" + city + "%'";
			cursor = mDbCity.rawQuery(sql, null);
			if (cursor.getCount() > 0) {
				// 必须使用moveToFirst方法将记录指针移动到第1条记录的位置
				cursor.moveToFirst();
				tempCityId = cursor.getInt(cursor.getColumnIndex("area_id"));
				// Log.e("tran", "citypicker area_id=" + tempCityId);
			}
		}
		// **********根据parent_id 获取所有的区**************
		sql = "select * from area where area_parent_id =? and area_parent_id!=0";
		cursor = mDbCity.rawQuery(sql, new String[] { "" + tempCityId });
		// 遍历Cursor
		while (cursor.moveToNext()) {
			String area_name = cursor.getString(cursor
					.getColumnIndex("area_name"));
			if (!"其他".equals(area_name))
				mCountries.add(area_name);
		}
		return mCountries;
	}
}
