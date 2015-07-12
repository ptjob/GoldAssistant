package com.quark.citylistview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sortlistview.R;
import com.quark.citylistview.SideBar.OnTouchingLetterChangedListener;

public class ChooseCityActivity extends Activity {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private TextView dingweiCityTv, hotCityOne, hotCityTwo, hotCityThree,
			hotCityFour, hotCityFive, hotCitySix, hotCitySeven, hotCityEight,
			hotCityNine;// 热门城市

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	private String dingweicityStr;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.city_list);
		// dingweicityStr = getIntent().getExtras().getString("citylist_city",
		// "定位失败");
		initViews();
		ImageButton back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initViews() {
		// init 热门城市
		dingweiCityTv = (TextView) findViewById(R.id.dingweicity);
		dingweiCityTv.setText(dingweicityStr);
		dingweiCityTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!dingweiCityTv.getText().toString().equals("定位失败")) {
					Toast.makeText(getApplication(),
							"成功切换到:" + dingweiCityTv.getText().toString(),
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					// intent.putExtra("province", province);
					intent.putExtra("city", dingweiCityTv.getText().toString());
					ChooseCityActivity.this.setResult(RESULT_OK, intent);
					ChooseCityActivity.this.finish();
				}
			}
		});
		hotCityOne = (TextView) findViewById(R.id.hot1);
		hotCityOne.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplication(),
						"成功切换到:" + hotCityOne.getText().toString(),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				// intent.putExtra("province", province);
				intent.putExtra("city", hotCityOne.getText().toString());
				ChooseCityActivity.this.setResult(RESULT_OK, intent);
				ChooseCityActivity.this.finish();
			}
		});
		hotCityTwo = (TextView) findViewById(R.id.hot2);
		hotCityTwo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplication(),
						"成功切换到:" + hotCityTwo.getText().toString(),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				// intent.putExtra("province", province);
				intent.putExtra("city", hotCityTwo.getText().toString());
				ChooseCityActivity.this.setResult(RESULT_OK, intent);
				ChooseCityActivity.this.finish();
			}
		});
		hotCityThree = (TextView) findViewById(R.id.hot3);
		hotCityThree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplication(),
						"成功切换到:" + hotCityThree.getText().toString(),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				// intent.putExtra("province", province);
				intent.putExtra("city", hotCityThree.getText().toString());
				ChooseCityActivity.this.setResult(RESULT_OK, intent);
				ChooseCityActivity.this.finish();

			}
		});
		hotCityFour = (TextView) findViewById(R.id.hot4);
		hotCityFour.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplication(),
						"成功切换到:" + hotCityFour.getText().toString(),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				// intent.putExtra("province", province);
				intent.putExtra("city", hotCityFour.getText().toString());
				ChooseCityActivity.this.setResult(RESULT_OK, intent);
				ChooseCityActivity.this.finish();
			}
		});
		hotCityFive = (TextView) findViewById(R.id.hot5);
		hotCityFive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplication(),
						"成功切换到:" + hotCityFive.getText().toString(),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				// intent.putExtra("province", province);
				intent.putExtra("city", hotCityFive.getText().toString());
				ChooseCityActivity.this.setResult(RESULT_OK, intent);
				ChooseCityActivity.this.finish();
			}
		});
		hotCitySix = (TextView) findViewById(R.id.hot6);
		hotCitySix.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplication(),
						"成功切换到:" + hotCitySix.getText().toString(),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				// intent.putExtra("province", province);
				intent.putExtra("city", hotCitySix.getText().toString());
				ChooseCityActivity.this.setResult(RESULT_OK, intent);
				ChooseCityActivity.this.finish();
			}
		});
		hotCitySeven = (TextView) findViewById(R.id.hot7);
		hotCitySeven.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplication(),
						"成功切换到:" + hotCitySeven.getText().toString(),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				// intent.putExtra("province", province);
				intent.putExtra("city", hotCitySeven.getText().toString());
				ChooseCityActivity.this.setResult(RESULT_OK, intent);
				ChooseCityActivity.this.finish();
			}
		});
		hotCityEight = (TextView) findViewById(R.id.hot8);
		hotCityEight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplication(),
						"成功切换到:" + hotCityEight.getText().toString(),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				// intent.putExtra("province", province);
				intent.putExtra("city", hotCityEight.getText().toString());
				ChooseCityActivity.this.setResult(RESULT_OK, intent);
				ChooseCityActivity.this.finish();
			}
		});
		hotCityNine = (TextView) findViewById(R.id.hot9);
		hotCityNine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplication(),
						"成功切换到:" + hotCityNine.getText().toString(),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				// intent.putExtra("province", province);
				intent.putExtra("city", hotCityNine.getText().toString());
				ChooseCityActivity.this.setResult(RESULT_OK, intent);
				ChooseCityActivity.this.finish();
			}
		});

		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Toast.makeText(
						getApplication(),
						"成功切换到城市:"
								+ ((SortModel) adapter.getItem(position))
										.getName(), Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();

				// intent.putExtra("province", province);
				intent.putExtra("city",
						((SortModel) adapter.getItem(position)).getName());

				ChooseCityActivity.this.setResult(RESULT_OK, intent);
				ChooseCityActivity.this.finish();
			}
		});

		SourceDateList = filledData(getResources().getStringArray(R.array.date));

		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);
		// 设置scrollview内的listview
		// Utils.setListViewHeightBasedOnChildren(sortListView);
		setListViewHeightBasedOnChildren(sortListView);
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 解决ScrollView与ListView合用(正确计算Listview的高度)的问题解决
	 * 
	 * @param listView
	 */
	private void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenHeigh = dm.heightPixels;
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + screenHeigh;
		listView.setLayoutParams(params);
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String[] date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

}
