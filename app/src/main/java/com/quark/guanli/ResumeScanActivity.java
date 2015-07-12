package com.quark.guanli;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.quark.model.BaomingList;
import com.quark.model.ResumeToCompany;
import com.quark.model.UserCommentModle;
import com.quark.senab.us.image.ImagePagerActivity;
import com.quark.senab.us.image.ImagePagerScanActivity;
import com.quark.ui.widget.CustomDialog;
import com.quark.ui.widget.ListViewForScrollView;
import com.quark.us.MyResumeActivity;
import com.quark.utils.Util;

/**
 * 
 * @ClassName: ResumeScanActivity
 * @Description: 管理 简历详细 查看报名 人员详细资料
 * @author howe
 * @date 2015-1-29 下午4:06:04
 * 
 */
public class ResumeScanActivity extends BaseActivity {
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
	@ViewInject(R.id.my_scan_liuy)
	TextView my_scan_liuy;

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
	@ViewInject(R.id.head_number)
	TextView head_number;
	@ViewInject(R.id.head_tnumber)
	TextView head_tnumber;

	// 留言
	@ViewInject(R.id.liuyan_layout)
	LinearLayout liuyan_layout;
	// 底部操作
	@ViewInject(R.id.bootm_layout)
	LinearLayout bootm_layout;
	//
	@ViewInject(R.id.op_refuse)
	TextView op_refuse;
	@ViewInject(R.id.op_pass)
	TextView op_pass;

	@ViewInject(R.id.nextone)
	LinearLayout nextone;
	@ViewInject(R.id.backone)
	LinearLayout backone;
	@ViewInject(R.id.nextone_img)
	ImageView nextone_img;
	@ViewInject(R.id.backone_img)
	ImageView backone_img;

	@ViewInject(R.id.op_shangjia)
	LinearLayout op_shangjia;

	ListViewForScrollView comment_list;
	ResumeToCompany re = new ResumeToCompany();
	private String userId;
	private String activity_id;
	private String navUrl;// 诚意金
	private String dataUrl;
	private String approveActivityUrl;
	private String rejectActivityUrl;
	private String commentUrl;// 评论列表
	private int creditworthiness;// 信誉值：步长为：10为一个心，5为半个心
	private int certification;// 实名认证，0-未认证，1-已提交认证，2-认证通过,3-认证不通过
	private int earnest_money;// 诚意金,0-未交，1-已交
	int[] heartImg = { R.id.heart_img1, R.id.heart_img2, R.id.heart_img3,
			R.id.heart_img4, R.id.heart_img5, R.id.heart_img6, R.id.heart_img7,
			R.id.heart_img8, R.id.heart_img9, R.id.heart_img10 };;
	private String title;
	ArrayList<BaomingList> Idlists = new ArrayList<BaomingList>();
	private int currentPosition;
	private int all_num;// 已录取总人数
	private int man_num;// 已录取男的数量
	private int women_num;// 已录取女的数量
	private SharedPreferences sp;
	String female_count, male_count;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.me_my_resume_scan);

		setBackButton();
		setTopTitle("人员资料");
		ViewUtils.inject(this);
		title = (String) getValue4Intent("title");
		female_count = (String) getValue4Intent("female_count");
		male_count = (String) getValue4Intent("male_count");
		navUrl = Url.USER_jianli_scan_nav + "?token="
				+ MainCompanyActivity.token;
		commentUrl = Url.USER_jianli_scan_comment + "?token="
				+ MainCompanyActivity.token;
		dataUrl = Url.COMPANY_applicantInfo + "?token="
				+ MainCompanyActivity.token;
		approveActivityUrl = Url.COMPANY_approveActivity + "?token="
				+ MainCompanyActivity.token;
		rejectActivityUrl = Url.COMPANY_rejectActivity + "?token="
				+ MainCompanyActivity.token;

		picbottom.getBackground().setAlpha(100);
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
		topLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));
		// userId = sp.getString("userId", "");
		userId = getIntent().getStringExtra("userId");
		activity_id = getIntent().getStringExtra("activity_id");
		all_num = getIntent().getIntExtra("passpersonsize", 0);
		man_num = getIntent().getIntExtra("man_num", 0);
		women_num = all_num - man_num;
		// Log.e("all---man", all_num + "?" + man_num);
		Idlists = (ArrayList<BaomingList>) getIntent().getSerializableExtra(
				"userIds");
		String currentPositionStr = getIntent().getStringExtra(
				"currentPosition");
		if (currentPositionStr != null) {
			currentPosition = Integer.valueOf(currentPositionStr);
		}

		op_shangjia.setVisibility(View.VISIBLE);
		bootm_layout.setVisibility(View.VISIBLE);
		liuyan_layout.setVisibility(View.VISIBLE);

		fushu_layout.setVisibility(View.GONE);
		my_scan_img.setBackgroundResource(R.drawable.other_btn_off);

		comment_list = (ListViewForScrollView) findViewById(R.id.comment_list);
		LinearLayout right_layout = (LinearLayout) findViewById(R.id.right_layout);
		right_layout.setOnClickListener(backlin);
		getDatas();
	}

	OnClickListener backlin = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	public void init() {
		op_shangjia.setVisibility(View.VISIBLE);
		bootm_layout.setVisibility(View.VISIBLE);
		liuyan_layout.setVisibility(View.VISIBLE);
		// 不显示向前或者向后：nextone 向前，backone：向后
		if (currentPosition == 0) {
			// backone.setVisibility(View.INVISIBLE);
			backone_img.setImageResource(R.drawable.check_btn_back_null);
			backone.setClickable(false);
		}
		if (currentPosition == Idlists.size() - 1) {
			// nextone.setVisibility(View.INVISIBLE);
			nextone_img.setImageResource(R.drawable.check_btn_next_null);
			nextone.setClickable(false);
		}
		if (currentPosition < Idlists.size() - 1) {
			nextone.setVisibility(View.VISIBLE);
			nextone_img.setImageResource(R.drawable.check_btn_next);
			nextone.setClickable(true);
		}
		if (currentPosition > 0) {
			backone.setVisibility(View.VISIBLE);
			backone_img.setImageResource(R.drawable.check_btn_back);
			backone.setClickable(true);
		}
		op_refuse.setVisibility(View.VISIBLE);
		op_pass.setVisibility(View.VISIBLE);

		if (re.getApply() == 1) {// 确认
			op_refuse.setVisibility(View.GONE);
			op_pass.setClickable(false);
			op_pass.setText("已通过");
		} else if (re.getApply() == 2) { // 拒绝
			op_pass.setVisibility(View.GONE);
			op_refuse.setClickable(false);
			op_refuse.setText("已拒绝");
		}
		head_tnumber.setText(man_num + "");// 已确认男
		head_number.setText(women_num + "");// 已确认女
		// re.getChecked_count()=已确认人数

		if (re.getName() != null) {
			my_scan_name.setText(re.getName());
		} else {
			my_scan_name.setText("");
		}
		if (re.getSex() == 0) {
			my_scan_sex.setText("女性");
		} else {
			my_scan_sex.setText("男性");
		}
		if (re.getBirthdate() != null) {
			my_scan_age
					.setText(Util.getCurrentAgeByBirthdate(re.getBirthdate()));
		} else {
			my_scan_age.setText("");
		}
		if (re.getHeight() != -1) {
			my_scan_shengao.setText(re.getHeight() + "cm");
		} else {
			my_scan_shengao.setText("");
		}
		if (re.getEducation() != null) {
			my_scan_xueli.setText(re.getEducation());
		} else {
			my_scan_xueli.setText("");
		}
		if (re.getGraduate() != null) {
			my_scan_school.setText(re.getGraduate());
		} else {
			my_scan_school.setText("");
		}
		if (re.getSummary() != null) {
			my_scan_jinyan.setText(re.getSummary());
		} else {
			my_scan_jinyan.setText("");
		}
		if (re.getNote() != null) {
			my_scan_liuy.setText(re.getNote());
		} else {
			my_scan_liuy.setText("");
		}

		if (re.getBbh() != null) {
			my_scan_sangwei.setText(re.getBbh());
		} else {
			my_scan_sangwei.setText("");
		}

		if ((re.getCloth_weight() != null)
				&& (!re.getCloth_weight().equals("-1"))) {
			my_scan_yifucima.setText(re.getCloth_weight());
		} else {
			my_scan_yifucima.setText("");
		}
		if (re.getShoe_weight() != 0) {
			my_scan_shoesm.setText(re.getShoe_weight() + "");
		} else {
			my_scan_shoesm.setText("");
		}
		if (re.getHealth_record() != -1) {
			if (re.getHealth_record() == 0) {
				my_scan_jinakz.setText("无");
			} else {
				my_scan_jinakz.setText("有");
			}
		} else {
			my_scan_jinakz.setText("");
		}
		if ((re.getLanguage() != null) && (!re.getLanguage().equals("-1"))) {
			my_scan_yuyan.setText(re.getLanguage());
		} else {
			my_scan_yuyan.setText("");
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

	public void getData() {
		showWait(true);
		op_refuse.setVisibility(View.VISIBLE);
		op_pass.setVisibility(View.VISIBLE);
		op_pass.setClickable(true);
		op_pass.setText("通过");
		op_refuse.setClickable(true);
		op_refuse.setText("拒绝");

		StringRequest request = new StringRequest(Request.Method.POST, dataUrl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject myResumeJson = js
									.getJSONObject("ApplicantInfoResponse");
							re = (ResumeToCompany) JsonUtil.jsonToBean(
									myResumeJson, ResumeToCompany.class);
							init();
							Log.e("mytag", "获得的MyResume bean：" + re.toString());
							url1 = url2 = url3 = url4 = url5 = url6 = "";
							imagesUrls[0] = imagesUrls[1] = imagesUrls[2] = imagesUrls[3] = imagesUrls[4] = imagesUrls[5] = "";

							if (re.getPicture_1() != null
									&& !re.getPicture_1().equals("")) {
								url1 = Url.GETPIC + re.getPicture_1();
								imagesUrls[0] = url1;
								// 如果本地有头像，则加载本地头像,反之获取网络头像并存于本地
								loadNativePhoto("u" + userId,
										re.getPicture_1(), head_previe);
								Editor edt = sp.edit();
								edt.putString("u" + userId + "_photo",
										re.getPicture_1());
								edt.commit();
								// loadpersonPic(Url.GETPIC + re.getPicture_1(),
								// head_previe, 0);
							} else {
								head_previe
										.setImageResource(R.drawable.pop_share_btn_jz);
							}
							if (re.getPicture_2() != null
									&& !re.getPicture_2().equals("")) {
								url2 = Url.GETPIC + re.getPicture_2();
								imagesUrls[1] = url2;
							}
							if (re.getPicture_3() != null
									&& !re.getPicture_3().equals("")) {
								url3 = Url.GETPIC + re.getPicture_3();
								imagesUrls[2] = url3;
							}
							if (re.getPicture_4() != null
									&& !re.getPicture_4().equals("")) {
								url4 = Url.GETPIC + re.getPicture_4();
								imagesUrls[3] = url4;
							}
							if (re.getPicture_5() != null
									&& !re.getPicture_5().equals("")) {
								url5 = Url.GETPIC + re.getPicture_5();
								imagesUrls[4] = url5;
							}
							if (re.getPicture_6() != null
									&& !re.getPicture_6().equals("")) {
								url6 = Url.GETPIC + re.getPicture_6();
								imagesUrls[5] = url6;
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
				map.put("activity_id", activity_id);

				return map;
			}
		};
		queue.add(request);
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
	}

	public void getCommentData() {
		list.removeAll(list);
		adapter1 = new JianliScanAdapter(ResumeScanActivity.this, list);
		comment_list.setAdapter(adapter1);
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
								adapter1 = new JianliScanAdapter(
										ResumeScanActivity.this, list);
								comment_list.setAdapter(adapter1);
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
	}

	/**
	 * 信誉值
	 * 
	 * @param xin
	 * @param convertView
	 */
	private void addXinToView(int xin) {
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
		intent.setClass(ResumeScanActivity.this, ImagePagerScanActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
	}

	// /*************************carson add on
	// 4-9-19:47****************************************
	/**
	 * 加载本地头像和名字
	 */
	private void loadNativePhoto(final String id, final String avatarUrl,
			final ImageView avatar) {
		// 先获取本地名字和头像
		File mePhotoFold = new File(Environment.getExternalStorageDirectory()
				+ "/" + "jzdr/" + "image");
		if (!mePhotoFold.exists()) {
			mePhotoFold.mkdirs();
		}
		File picture_1 = new File(Environment.getExternalStorageDirectory()
				+ "/" + "jzdr/" + "image/" + sp.getString(id + "_photo", "c"));

		if (picture_1.exists()) {
			// 加载本地图片
			Bitmap bb_bmp = MyResumeActivity.zoomImg(picture_1, 300, 300);
			if (bb_bmp != null) {
				avatar.setImageBitmap(bb_bmp);
			} else {
				loadpersonPic(avatarUrl, avatar, 1);
			}
		} else {
			loadpersonPic(avatarUrl, avatar, 1);
		}

	}

	/**
	 * @Description: 加载图片
	 * @author howe
	 * @date 2014-7-30 下午5:57:52
	 * 
	 */
	public void loadpersonPic(final String url, final ImageView imageView,
			final int isRound) {
		ImageRequest imgRequest = new ImageRequest(Url.GETPIC + url,
				new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap arg0) {
						String picName = url;
						imageView.setImageBitmap(arg0);
						OutputStream output = null;
						try {
							File mePhotoFold = new File(
									Environment.getExternalStorageDirectory()
											+ "/" + "jzdr/" + "image");
							if (!mePhotoFold.exists()) {
								mePhotoFold.mkdirs();
							}
							output = new FileOutputStream(
									Environment.getExternalStorageDirectory()
											+ "/" + "jzdr/" + "image/"
											+ picName);
							arg0.compress(Bitmap.CompressFormat.JPEG, 100,
									output);
							output.flush();
							output.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, 300, 200, Config.ARGB_8888, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});
		queue.add(imgRequest);
		imgRequest.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	@OnClick(R.id.op_pass)
	public void pass(View v) {
		int requiredMale, requiredFemale;
		try {
			requiredMale = Integer.parseInt(male_count);
		} catch (Exception e) {
			requiredMale = 0;
		}
		try {
			requiredFemale = Integer.parseInt(female_count);
		} catch (Exception e) {
			requiredFemale = 0;
		}
		if (all_num >= re.getHead_count()) {
			showAlertDialog("本次活动人员已招募完成，如有人员变动请在活动详情中修改活动", "温馨提示", "我知道了");

		} else {
			// sex ==1表示男
			if (re.getSex() == 1) {
				// 男
				if (requiredMale == 0) {
					// 没有男性限制
					showAlertDialog("您确定录用TA?", "通过提示", "确定");
				} else {
					if (man_num >= requiredMale) {
						// 已录取男性大于等于限制人数弹框是否继续
						// 弹框
						showAlertDialog2("您要招募的男性人员已满，确认继续招募男性？", "温馨提示",
								"只看女性");
					} else {
						showAlertDialog("您确定录用TA?", "通过提示", "确定");
					}
				}
			} else {
				// 女
				if (requiredFemale == 0) {
					// 没有女性限制
					showAlertDialog("您确定录用TA?", "通过提示", "确定");
				} else {
					if ((women_num) >= requiredFemale) {
						// 已录取女性大于等于限制人数弹框是否继续
						// 弹框
						showAlertDialog2("您要招募的女性人员已满，确认继续招募女性？", "温馨提示",
								"只看男性");
					} else {
						showAlertDialog("您确定录用TA?", "通过提示", "确定");
					}
				}
			}
		}

	}

	@OnClick(R.id.op_refuse)
	public void refuse(View v) {
		showAlertDialog("您确定拒绝录用TA?", "拒绝提示", "确定");
	}

	public void opApprove(final ResumeToCompany re, String url, final int flag) {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							if (flag == 1) {// 通过approveActivityResponse
								JSONObject js = new JSONObject(response);
								JSONObject jss = js
										.getJSONObject("approveActivityResponse");
								Log.e("jss", jss.toString());
								int status = jss.getInt("status");
								// status: 1正常2所有的招满3女满4男满else操作异常
								Log.e("status==", status + "::");
								if (status == 1) {
									man_num = jss.getInt("total_male");
									women_num = jss.getInt("total_female");
									all_num = man_num + women_num;
									// 处理了一条报名信息,本地值减一
									int currentTodo = sp
											.getInt(ConstantForSaveList.userId
													+ "todo", 0);
									if (currentTodo > 0) {
										Editor edt = sp.edit();
										edt.putInt(ConstantForSaveList.userId
												+ "todo", currentTodo - 1);
										edt.commit();
									}
									if (all_num < 2) {
										showAlertDialog("通过后系统将自动将报名人员拉进"
												+ title + "聊天群，同时也为您创建了"
												+ title + "花名册", "已建立花名册",
												"我知道了");
									}
									getData(); // 刷新页面
								} else if (status == 2) {
									// 所有人招满
									man_num = jss.getInt("total_male");
									women_num = jss.getInt("total_female");
									all_num = man_num + women_num;
									String msg = jss.getString("msg");
									showAlertDialog(msg, "温馨提示", "我知道了");
									getData();
								} else if (status == 3) {
									// 女满
									man_num = jss.getInt("total_male");
									women_num = jss.getInt("total_female");
									all_num = man_num + women_num;
									String msg = jss.getString("msg");
									showAlertDialog(msg, "温馨提示", "我知道了");
									getData();
								} else if (status == 4) {
									// 男满
									man_num = jss.getInt("total_male");
									women_num = jss.getInt("total_female");
									all_num = man_num + women_num;
									String msg = jss.getString("msg");
									getData();
									showAlertDialog(msg, "温馨提示", "我知道了");
								} else {
									String msg = jss.getString("msg");
									showToast(msg);
								}
							} else {// 拒绝 rejectActivityResponse
								JSONObject js = new JSONObject(response);
								JSONObject jss = js
										.getJSONObject("rejectActivityResponse");
								int status = jss.getInt("status");
								if (status == 1) {
									if (re.getSex() == 1) {
										// man_num--;
									}
									// all_num--;
									// 处理了一条报名信息,本地值减一
									int currentTodo = sp
											.getInt(ConstantForSaveList.userId
													+ "todo", 0);
									if (currentTodo > 0) {
										Editor edt = sp.edit();
										edt.putInt(ConstantForSaveList.userId
												+ "todo", currentTodo - 1);
										edt.commit();
									}
									showToast("操作成功");
									getData();
								} else {
									showToast("操作失败，请重试");
								}
							}

						} catch (JSONException e) {
							showWait(false);
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						showToast("操作失败，请重试");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", userId);
				map.put("activity_id", activity_id);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	/**
	 * 弹窗提示
	 * 
	 * @param str
	 * @param str2
	 *            男性或女性人员已招满时弹框
	 */
	public void showAlertDialog2(String str, final String str2, String str3) {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);
		builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showAlertDialog("您确定录用TA?", "通过提示", "确定");
			}
		});

		builder.setNegativeButton(str3, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	/**
	 * 弹窗提示
	 * 
	 * @param str
	 * @param str2
	 */
	public void showAlertDialog(String str, final String str2, String str3) {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);

		builder.setPositiveButton(str3, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (str2.equals("通过提示")) {
					opApprove(re, approveActivityUrl, 1);
				}
				if (str2.equals("拒绝提示")) {
					opApprove(re, rejectActivityUrl, 2);
				}
			}
		});

		if (str2.equals("通过提示") || str2.equals("拒绝提示")) {
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
		}
		builder.create().show();
	}

	public void getDatas() {
		getData();
		getNav();
		getCommentData();
	}

	@OnClick(R.id.nextone)
	public void nextoneClick(View v) {
		currentPosition++;
		if (Idlists != null) {
			if (Idlists.size() > currentPosition) {
				userId = String.valueOf(Idlists.get(currentPosition)
						.getUser_id());
				getDatas();
			}
		}
	}

	@OnClick(R.id.backone)
	public void backoneClick(View v) {
		currentPosition--;
		if (Idlists != null) {
			if (currentPosition >= 0 && Idlists.size() > currentPosition) {
				userId = String.valueOf(Idlists.get(currentPosition)
						.getUser_id());
				getDatas();
			}
		}
	}
}
