package com.quark.us;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingmu.jianzhidaren.R;
import com.quark.adapter.JianliScanAdapter;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.jianzhidaren.MainCompanyActivity;
import com.quark.model.MyResume;
import com.quark.model.UserCommentModle;
import com.quark.senab.us.image.ImagePagerActivity;
import com.quark.senab.us.image.ImagePagerScanActivity;
import com.quark.ui.widget.ListViewForScrollView;
import com.quark.utils.Util;

/**
 * 我的简历 预览
 * 
 * @author C罗
 * 
 */
public class MyResumeScanActivity extends BaseActivity {
	String url1 = "";
	String url2 = "";
	String url3 = "";
	String url4 = "";
	String url5 = "";
	String url6 = "";
	String[] imagesUrls = new String[] { "", "", "", "", "", "" };// 直接初始化

	public JianliScanAdapter adapter1;
	ArrayList<UserCommentModle> list = new ArrayList<UserCommentModle>();

	@ViewInject(R.id.my_scan_name)
	TextView my_scan_name;
	@ViewInject(R.id.my_scan_sex)
	TextView my_scan_sex;
	@ViewInject(R.id.my_scan_age)
	TextView my_scan_age;
	@ViewInject(R.id.my_scan_shengao)
	TextView my_scan_shengao;
	@ViewInject(R.id.my_scan_xueli)
	TextView my_scan_xueli;
	@ViewInject(R.id.my_scan_school)
	TextView my_scan_school;

	boolean zliaoShow = true;
	// 头像
	@ViewInject(R.id.head_previe)
	ImageView head_previe;

	// 附属资料头
	@ViewInject(R.id.my_scan_zliao_relayout)
	RelativeLayout my_scan_zliao_relayout;
	@ViewInject(R.id.my_scan_img)
	ImageView my_scan_img;

	@ViewInject(R.id.my_scan_sangwei)
	TextView my_scan_sangwei;

	/*
	 * @ViewInject(R.id.my_scan_xiongwei) TextView my_scan_xiongwei;
	 * 
	 * @ViewInject(R.id.my_scan_yaowei) TextView my_scan_yaowei;
	 * 
	 * @ViewInject(R.id.my_scan_tunwei) TextView my_scan_tunwei;
	 */
	@ViewInject(R.id.my_scan_yifucima)
	TextView my_scan_yifucima;
	@ViewInject(R.id.my_scan_shoesm)
	TextView my_scan_shoesm;
	@ViewInject(R.id.my_scan_jinakz)
	TextView my_scan_jinakz;
	@ViewInject(R.id.my_scan_yuyan)
	TextView my_scan_yuyan;

	// 资料内容
	@ViewInject(R.id.my_scan_jinyan_llayout)
	LinearLayout my_scan_jinyan_llayout;
	@ViewInject(R.id.my_scan_jinyan)
	TextView my_scan_jinyan;
	boolean commentShow = true;
	// 列表头
	@ViewInject(R.id.my_scan_comment_rlayout)
	RelativeLayout my_scan_comment_rlayout;
	@ViewInject(R.id.my_scan_comment_img)
	ImageView my_scan_comment_img;

	@ViewInject(R.id.fushu_layout)
	LinearLayout fushu_layout;

	@ViewInject(R.id.picbottom)
	LinearLayout picbottom;

	@ViewInject(R.id.cyj_img)
	ImageView cyj_img;
	@ViewInject(R.id.yan_img)
	// 验
	ImageView yan_img;

	ListViewForScrollView comment_list;
	MyResume re;
	private String userId;
	private String navUrl;// 诚意金
	private String commentUrl;// 评论列表
	private int creditworthiness;// 信誉值：步长为：10为一个心，5为半个心
	private int certification;// 实名认证，0-未认证，1-已提交认证，2-认证通过,3-认证不通过
	private int earnest_money;// 诚意金,0-未交，1-已交
	int[] heartImg = { R.id.heart_img1, R.id.heart_img2, R.id.heart_img3,
			R.id.heart_img4, R.id.heart_img5, R.id.heart_img6, R.id.heart_img7,
			R.id.heart_img8, R.id.heart_img9, R.id.heart_img10 };;
	private LinearLayout topTitileLayout;
	private SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.me_my_resume_scan);
		topTitileLayout = (LinearLayout) findViewById(R.id.me_my_resume_scan_top);
		setBackButton();
		ViewUtils.inject(this);
		navUrl = Url.USER_jianli_scan_nav + "?token=" + MainCompanyActivity.token;
		commentUrl = Url.USER_jianli_scan_comment + "?token="
				+ MainCompanyActivity.token;

		picbottom.getBackground().setAlpha(80);
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		userId = sp.getString("userId", "");
		getPicData();
		re = (MyResume) getIntent().getSerializableExtra("myResume");
		boolean flag = getIntent().getExtras().getBoolean("showtitile");
		if (flag) {
			topTitileLayout.setVisibility(View.VISIBLE);
		} else {
			topTitileLayout.setVisibility(View.GONE);
		}
		init();
		getNav();
		getCommentData();

		JianliScanAdapter adapter1 = new JianliScanAdapter(this, list);
		comment_list = (ListViewForScrollView) findViewById(R.id.comment_list);
		comment_list.setAdapter(adapter1);
	}

	public void init() {

		if (re.getName() != null) {
			my_scan_name.setText(re.getName());
		}
		if (re.getSex() == 0) {
			my_scan_sex.setText("女性");
		} else if (re.getSex() == 1) {
			my_scan_sex.setText("男性");
		} else {
			my_scan_sex.setText("未填写");
		}
		if (re.getBirthdate() != null) {
			my_scan_age
					.setText(Util.getCurrentAgeByBirthdate(re.getBirthdate()));
		}
		if (re.getHeight() != -1) {
			my_scan_shengao.setText(re.getHeight() + "cm");
		}
		if (re.getEducation() != null) {
			my_scan_xueli.setText(re.getEducation());
		}
		if (re.getGraduate() != null) {
			my_scan_school.setText(re.getGraduate());
		}
		if (re.getSummary() != null) {
			my_scan_jinyan.setText(re.getSummary());
		}
		if (re.getBbh() != null) {
			my_scan_sangwei.setText(re.getBbh());
		}
		if ((re.getCloth_weight() != null)
				&& (!re.getCloth_weight().equals("-1"))) {
			my_scan_yifucima.setText(re.getCloth_weight());
		}
		if ((re.getShoe_weight() != -1) && (re.getShoe_weight() != 0)) {
			my_scan_shoesm.setText(re.getShoe_weight() + "");
		}
		if (re.getHealth_record() != -1) {
			if (re.getHealth_record() == 0) {
				my_scan_jinakz.setText("无");
			} else {
				my_scan_jinakz.setText("有");
			}
		}
		if ((re.getLanguage() != null) && (!re.getLanguage().equals("-1"))) {
			my_scan_yuyan.setText(re.getLanguage());
		}
	}

	@OnClick({ R.id.my_scan_zliao_relayout, R.id.my_scan_comment_rlayout })
	public void ziliaoOnclick(View view) {
		switch (view.getId()) {
		case R.id.my_scan_zliao_relayout:
			if (zliaoShow) {
				zliaoShow = false;
				fushu_layout.setVisibility(View.GONE);
				my_scan_img.setBackgroundResource(R.drawable.other_btn_off);
			} else {
				zliaoShow = true;
				fushu_layout.setVisibility(View.VISIBLE);
				my_scan_img.setBackgroundResource(R.drawable.other_btn_on);
			}
			break;
		case R.id.my_scan_comment_rlayout:
			if (commentShow) {
				commentShow = false;
				comment_list.setVisibility(View.GONE);
				my_scan_comment_img
						.setBackgroundResource(R.drawable.other_btn_off);
			} else {
				commentShow = true;
				comment_list.setVisibility(View.VISIBLE);
				my_scan_comment_img
						.setBackgroundResource(R.drawable.other_btn_on);
			}
			break;
		default:
			break;
		}
	}

	public void getPicData() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST,
				Url.USER_jianli_show + "?token=" + MainCompanyActivity.token,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
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
									loadpersonPic(
											Url.GETPIC + re.getPicture_1(),
											head_previe, 0);
								}
								if (re.getPicture_2() != null) {
									url2 = Url.GETPIC + re.getPicture_2();
									imagesUrls[1] = url2;
								}
								if (re.getPicture_3() != null) {
									url3 = Url.GETPIC + re.getPicture_3();
									imagesUrls[2] = url3;
								}
								if (re.getPicture_4() != null) {
									url4 = Url.GETPIC + re.getPicture_4();
									imagesUrls[3] = url4;
								}
								if (re.getPicture_5() != null) {
									url5 = Url.GETPIC + re.getPicture_5();
									imagesUrls[4] = url5;
								}
								if (re.getPicture_6() != null) {
									url6 = Url.GETPIC + re.getPicture_6();
									imagesUrls[5] = url6;
								}
							} else {
								Toast.makeText(MyResumeScanActivity.this,
										"获取图片失败！", Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
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

	public void getNav() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST, navUrl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject statusjs = js
									.getJSONObject("SnapshotResponse");

							creditworthiness = statusjs
									.getInt("creditworthiness");// 信誉值：步长为：10为一个心，5为半个心
							certification = statusjs.getInt("certification");// 实名认证，0-未认证，1-已提交认证，2-认证通过,3-认证不通过
							earnest_money = statusjs.getInt("earnest_money");
							if (earnest_money == 1) {
								cyj_img.setImageResource(R.drawable.scan_resume_cyj);
							} else {
								cyj_img.setImageResource(R.drawable.scan_resume_putong);
							}
							if (certification == 2) {
								yan_img.setImageResource(R.drawable.scan_resume_yrz);
							} else {
								yan_img.setImageResource(R.drawable.scan_resume_wrz);
							}
							addXinToView(creditworthiness);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
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

	public void getCommentData() {

		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST,
				commentUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							JSONArray jsa = js
									.getJSONArray("UserCommentsResponse");
							for (int i = 0; i < jsa.length(); i++) {
								UserCommentModle newComment = (UserCommentModle) JsonUtil
										.jsonToBean(jsa.getJSONObject(i),
												UserCommentModle.class);
								list.add(newComment);
							}
							showWait(false);
						} catch (JSONException e) {
							showWait(false);
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
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

	public void addXinToView(int xin) {
		if (xin > 0) {
			int heartCount = xin / 10;
			int heartHeart = xin % 10;
			int j = 0;
			if (heartCount > 9) {
				ImageView imageView = (ImageView) findViewById(heartImg[0]);
				imageView.setVisibility(View.VISIBLE);
				imageView.setImageResource(R.drawable.icon_heart_ten);
			} else {
				for (int i = 0; i < heartCount; i++) {
					ImageView imageView = (ImageView) findViewById(heartImg[i]);
					imageView.setVisibility(View.VISIBLE);
					imageView.setImageResource(R.drawable.icon_heart);
					j = i;
				}
				if (heartHeart == 5) {
					ImageView imageView = (ImageView) findViewById(heartImg[j + 1]);
					imageView.setImageResource(R.drawable.icon_heart_half);
					imageView.setVisibility(View.VISIBLE);
				}
				// 用于刷新UI
				if (heartCount < 9) {
					for (int ii = j + 2; ii < 10; ii++) {
						ImageView imageView = (ImageView) findViewById(heartImg[ii]);
						imageView.setVisibility(View.GONE);
					}
				}
				if (heartCount == 5 && heartHeart == 5) {
					LinearLayout ly = (LinearLayout) findViewById(R.id.icon_heart_two);
					ly.setVisibility(View.VISIBLE);
				} else if (heartCount <= 5) {
					LinearLayout ly = (LinearLayout) findViewById(R.id.icon_heart_two);
					ly.setVisibility(View.GONE);
					LinearLayout lyone = (LinearLayout) findViewById(R.id.icon_heart_one);
					lyone.setPadding(0, 20, 0, 0);
				} else {
					LinearLayout ly = (LinearLayout) findViewById(R.id.icon_heart_two);
					ly.setVisibility(View.VISIBLE);
				}
			}
		} else {

			for (int a = 0; a < 10; a++) {
				ImageView imageView = (ImageView) findViewById(heartImg[a]);
				imageView.setVisibility(View.GONE);
			}

		}
	}

	/**
	 * 点击头像
	 */
	@OnClick({ R.id.head_previe })
	public void touxiangOnClick(View view) {
		imageBrower(0, imagesUrls);
	}

	// 已做缓存处理
	private void imageBrower(int position, String[] urls) {
		Intent intent = new Intent();
		intent.setClass(MyResumeScanActivity.this, ImagePagerScanActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
	}

	/**
	 * @Description: 加载图片
	 * @author howe
	 * @date 2014-7-30 下午5:57:52
	 * 
	 */
	public void loadpersonPic(String url, final ImageView imageView,
			final int isRound) {
		ImageRequest imgRequest = new ImageRequest(url,
				new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap arg0) {
						if (isRound == 1) {
							// Bitmap output=Util.toRoundCorner(arg0, 2);
							// imageView.setImageBitmap(output);
						} else {
							// LayoutParams params = new
							// LayoutParams(width-30,(width-30)*380/640);
							// params.setMargins(15, 0, 0, 0);
							// imageView.setLayoutParams(params );
							imageView.setImageBitmap(arg0);
						}
					}
				}, 300, 200, Config.ARGB_8888, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});
		queue.add(imgRequest);
		imgRequest.setRetryPolicy(new DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1, 1.0f));

	}
}
