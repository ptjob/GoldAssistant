package com.quark.senab.us.image;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
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
import com.parttime.main.MainTabActivity;
import com.quark.model.MyResume;
import com.quark.utils.WaitDialog;
import com.quark.volley.VolleySington;

public class ImagePagerActivity extends FragmentActivity {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";

	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;
	private ImageView imageBtn;
	private ImageView image_btn_addImag;
	String url1 = "";
	String url2 = "";
	String url3 = "";
	String url4 = "";
	String url5 = "";
	String url6 = "";
	String[] imagesUrls = new String[] { "", "", "", "", "", "" };
	String[] totalpicUrls;// 真实的图片数组
	String userId;
	MyResume re = new MyResume();
	protected RequestQueue queue;
	Bundle savedInstanceStateTemp;
	int totalPic = 0;
	SharedPreferences sp;

	// String imageUri = "file:///mnt/sdcard/image.png"; // 加载 SD card图片

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);
		savedInstanceStateTemp = savedInstanceState;
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		userId = sp.getString("userId", "");
		queue = VolleySington.getInstance().getRequestQueue();
		getData();
		image_btn_addImag = (ImageView) findViewById(R.id.image_btn_add);
		image_btn_addImag.setOnClickListener(addPic);
		// 返回
		imageBtn = (ImageView) findViewById(R.id.image_close);
		imageBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ImagePagerActivity.this.finish();
			}
		});
	}

	/**
	 * 判断本地是否已经存在该图片,若存在则调用本地图片,反之则网络获取
	 * 
	 */
	private String checkPhotoExitsInNative(int position) {
		String picPath;
		switch (position) {
		case 0:
			picPath = re.getPicture_1();
			if (!"".equals(picPath)) {
				File f = new File(Environment.getExternalStorageDirectory()
						+ "/" + "jzdr/" + "image/" + picPath);
				if (f.exists()) {
					imagesUrls[position] = "file://"
							+ Environment.getExternalStorageDirectory() + "/"
							+ "jzdr/" + "image/" + picPath;
				} else {
					imagesUrls[position] = Url.GETPIC + picPath;
				}

			} else {
				imagesUrls[position] = "";
			}
			break;
		case 1:
			picPath = re.getPicture_2();
			if (!"".equals(picPath)) {
				File f = new File(Environment.getExternalStorageDirectory()
						+ "/" + "jzdr/" + "image/" + picPath);
				if (f.exists()) {
					imagesUrls[position] = "file://"
							+ Environment.getExternalStorageDirectory() + "/"
							+ "jzdr/" + "image/" + picPath;
				} else {
					imagesUrls[position] = Url.GETPIC + picPath;
				}

			} else {
				imagesUrls[position] = "";
			}
			break;
		case 2:
			picPath = re.getPicture_3();
			if (!"".equals(picPath)) {
				File f = new File(Environment.getExternalStorageDirectory()
						+ "/" + "jzdr/" + "image/" + picPath);
				if (f.exists()) {
					imagesUrls[position] = "file://"
							+ Environment.getExternalStorageDirectory() + "/"
							+ "jzdr/" + "image/" + picPath;
				} else {
					imagesUrls[position] = Url.GETPIC + picPath;
				}

			} else {
				imagesUrls[position] = "";
			}
			break;
		case 3:
			picPath = re.getPicture_4();
			if (!"".equals(picPath)) {
				File f = new File(Environment.getExternalStorageDirectory()
						+ "/" + "jzdr/" + "image/" + picPath);
				if (f.exists()) {
					imagesUrls[position] = "file://"
							+ Environment.getExternalStorageDirectory() + "/"
							+ "jzdr/" + "image/" + picPath;
				} else {
					imagesUrls[position] = Url.GETPIC + picPath;
				}

			} else {
				imagesUrls[position] = "";
			}
			break;
		case 4:
			picPath = re.getPicture_5();
			if (!"".equals(picPath)) {
				File f = new File(Environment.getExternalStorageDirectory()
						+ "/" + "jzdr/" + "image/" + picPath);
				if (f.exists()) {
					imagesUrls[position] = "file://"
							+ Environment.getExternalStorageDirectory() + "/"
							+ "jzdr/" + "image/" + picPath;
				} else {
					imagesUrls[position] = Url.GETPIC + picPath;
				}

			} else {
				imagesUrls[position] = "";
			}
			break;
		case 5:
			picPath = re.getPicture_6();
			if (!"".equals(picPath)) {
				File f = new File(Environment.getExternalStorageDirectory()
						+ "/" + "jzdr/" + "image/" + picPath);
				if (f.exists()) {
					imagesUrls[position] = "file://"
							+ Environment.getExternalStorageDirectory() + "/"
							+ "jzdr/" + "image/" + picPath;
				} else {
					imagesUrls[position] = Url.GETPIC + picPath;
				}

			} else {
				imagesUrls[position] = "";
			}
			break;
		default:
			break;
		}

		return imagesUrls[position];

	}

	/**
	 * 处理图片路径
	 * 
	 */
	public void deal() {
		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		// String[] urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);
		mPager = (HackyViewPager) findViewById(R.id.pager);
		for (int i = 0; i < imagesUrls.length; i++) {
			imagesUrls[i] = checkPhotoExitsInNative(i);
		}
		ImagePagerAdapter mAdapter = new ImagePagerAdapter(
				getSupportFragmentManager(), imagesUrls);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);

		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
				.getAdapter().getCount());
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
				CharSequence text = getString(R.string.viewpager_indicator,
						arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
				currentPage = arg0;
			}

		});
		if (savedInstanceStateTemp != null) {
			pagerPosition = savedInstanceStateTemp.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
	}

	int currentPage;

	public void getData() {
		totalPic = 0;
		// 每次更新图片后重新获取一次图片url
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST,
				Url.USER_jianli_show + "?token=" + MainTabActivity.token,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
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

								if (re.getPicture_1() != null
										&& !re.getPicture_1().equals("")) {
									url1 = Url.GETPIC + re.getPicture_1();
									imagesUrls[0] = url1;
									totalPic++;
								}
								if (re.getPicture_2() != null
										&& !re.getPicture_2().equals("")) {
									url2 = Url.GETPIC + re.getPicture_2();
									imagesUrls[1] = url2;
									totalPic++;
								}
								if (re.getPicture_3() != null
										&& !re.getPicture_3().equals("")) {
									url3 = Url.GETPIC + re.getPicture_3();
									imagesUrls[2] = url3;
									totalPic++;
								}
								if (re.getPicture_4() != null
										&& !re.getPicture_4().equals("")) {
									url4 = Url.GETPIC + re.getPicture_4();
									imagesUrls[3] = url4;
									totalPic++;
								}
								if (re.getPicture_5() != null
										&& !re.getPicture_5().equals("")) {
									url5 = Url.GETPIC + re.getPicture_5();
									imagesUrls[4] = url5;
									totalPic++;
								}
								if (re.getPicture_6() != null
										&& !re.getPicture_6().equals("")) {
									url6 = Url.GETPIC + re.getPicture_6();
									imagesUrls[5] = url6;
									totalPic++;
								}
								deal();
							} else {
								getNativePhoto();// 加载本地图片
								deal();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						getNativePhoto();
						deal();

					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", userId);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1, 1.0f));

	}

	/**
	 * 获取本地图片照片
	 * 
	 */
	private void getNativePhoto() {
		re.setPicture_1(sp.getString(userId + "pic_1", ""));
		re.setPicture_2(sp.getString(userId + "pic_2", ""));
		re.setPicture_3(sp.getString(userId + "pic_3", ""));
		re.setPicture_4(sp.getString(userId + "pic_4", ""));
		re.setPicture_5(sp.getString(userId + "pic_5", ""));
		re.setPicture_6(sp.getString(userId + "pic_6", ""));
	}

	OnClickListener addPic = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			int tempPositon = currentPage + 1;
			intent.putExtra("position", tempPositon + "");
			intent.putExtra("totalPic", totalPic + "");
			ImagePagerActivity.this.setResult(RESULT_OK, intent);
			ImagePagerActivity.this.finish();
		}
	};

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
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

	protected WaitDialog dialog;

	protected void showWait(boolean isShow) {
		if (isShow) {
			if (null == dialog) {
				dialog = new WaitDialog(this);
			}
			dialog.show();
		} else {
			if (null != dialog) {
				dialog.dismiss();
			}
		}
	}
}