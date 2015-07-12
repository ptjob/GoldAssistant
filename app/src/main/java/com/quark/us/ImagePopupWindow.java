package com.quark.us;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.qingmu.jianzhidaren.R;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.jianzhidaren.MainCompanyActivity;
import com.quark.model.MyResume;
import com.quark.senab.us.image.HackyViewPager;
import com.quark.senab.us.image.ImageDetailFragment;
import com.quark.volley.VolleySington;

public class ImagePopupWindow extends PopupWindow {

	private Button btn_cancel;
	private View mMenuView;
	// //////读取图片参数、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、
	int totalPic = 0;
	MyResume re = new MyResume();
	String url1 = "";
	String url2 = "";
	String url3 = "";
	String url4 = "";
	String url5 = "";
	String url6 = "";
	String[] imagesUrls = new String[] { "", "", "", "", "", "" };
	protected RequestQueue queue;
	private int pagerPosition, position;
	private String user_id;
	private HackyViewPager mPager;
	private TextView indicator;
	private Context context;

	public ImagePopupWindow(Activity context, String[] urls, int position,
			String userId) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.image_detail_pager, null);
		queue = VolleySington.getInstance().getRequestQueue();
		user_id = userId;
		this.position = position;
		this.context = context;
		getData();

		// ////////弹窗必须/////////////////////////////////////////////////
		/*
		 * btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel); //
		 * 取消按钮 btn_cancel.setOnClickListener(new OnClickListener() {
		 * 
		 * public void onClick(View v) { // 销毁弹出框 dismiss(); } });
		 */
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
	}

	// ////////弹窗必须/////////////////////////////////////////////////

	int currentPage;

	private void getData() {
		totalPic = 0;
		// 每次更新图片后重新获取一次图片url
		// showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST,
				Url.USER_jianli_show + "?token=" + MainCompanyActivity.token,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// showWait(false);
						Log.e("mytag", "获得的json：" + response);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject statusjs = js
									.getJSONObject("ResponseStatus");
							int status = statusjs.getInt("status");
							if (status == 2) {
								JSONObject myResumeJson = js
										.getJSONObject("MyResumeResponse");
								re = (MyResume) JsonUtil.jsonToBean(
										myResumeJson, MyResume.class);
								Log.e("mytag", "获得的bean：" + re.toString());

								if (re.getPicture_1() != null) {
									url1 = Url.GETPIC + re.getPicture_1();
									imagesUrls[0] = url1;
									totalPic++;
								}
								if (re.getPicture_2() != null) {
									url2 = Url.GETPIC + re.getPicture_2();
									imagesUrls[1] = url2;
									totalPic++;
								}
								if (re.getPicture_3() != null) {
									url3 = Url.GETPIC + re.getPicture_3();
									imagesUrls[2] = url3;
									totalPic++;
								}
								if (re.getPicture_4() != null) {
									url4 = Url.GETPIC + re.getPicture_4();
									imagesUrls[3] = url4;
									totalPic++;
								}
								if (re.getPicture_5() != null) {
									url5 = Url.GETPIC + re.getPicture_5();
									imagesUrls[4] = url5;
									totalPic++;
								}
								if (re.getPicture_6() != null) {
									url6 = Url.GETPIC + re.getPicture_6();
									imagesUrls[5] = url6;
									totalPic++;
								}
								deal();
							} else {
								// Toast.makeText(ImagePagerActivity.this,
								// "获取图片失败！",Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						// showWait(false);
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", user_id);

				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1, 1.0f));

	}

	public void deal() {
		pagerPosition = position;

		mPager = (HackyViewPager) mMenuView.findViewById(R.id.pager);
		// ImagePagerAdapter mAdapter = new
		// ImagePagerAdapter(getSupportFragmentManager(), imagesUrls);
		// mPager.setAdapter(mAdapter);
		indicator = (TextView) mMenuView.findViewById(R.id.indicator);

		CharSequence text = context.getString(R.string.viewpager_indicator, 1,
				mPager.getAdapter().getCount());
		indicator.setText(text);
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = context.getString(
						R.string.viewpager_indicator, arg0 + 1, mPager
								.getAdapter().getCount());
				indicator.setText(text);
				currentPage = arg0;
				Log.e("erros", "当前页=" + arg0);
			}

		});
		/*
		 * if (savedInstanceStateTemp != null) { pagerPosition =
		 * contextsavedInstanceStateTemp.getInt(STATE_POSITION); }
		 */

		mPager.setCurrentItem(pagerPosition);
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public String[] fileList;

		public ImagePagerAdapter(FragmentManager fm, String[] fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.length;
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList[position];
			return ImageDetailFragment.newInstance(url);
		}

	}

}
