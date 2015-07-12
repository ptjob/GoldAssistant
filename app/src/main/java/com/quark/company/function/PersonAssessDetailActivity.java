package com.quark.company.function;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.qingmu.jianzhidaren.R;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.http.image.CircularImage;
import com.quark.http.image.LoadImage;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.jianzhidaren.MainCompanyActivity;
import com.quark.model.CancelApply;
import com.quark.ui.widget.CustomDialogThree;

/**
 * 评价人员详情
 * 
 * @author Administrator
 * 
 */
public class PersonAssessDetailActivity extends BaseActivity {

	private RadioGroup radioGroup1;
	private RadioGroup radioGroup2;
	private Boolean changeedGroup = false;
	private String user_id, activity_id, url_ziliao, url_comtent, type_st,
			neirong, text, text2;
	private CancelApply cancelApply;
	private TextView cancel_name, age, note;
	private ImageView iconc, yan_img, cyj_img, sex_img;
	private Button gree_cancel;
	private RadioButton radioButton1, radioButton2;
	private EditText content;
	private int status;
	int[] heartImg = { R.id.xinyi_bt1, R.id.xinyi_bt2, R.id.xinyi_bt3,
			R.id.xinyi_bt4, R.id.xinyi_bt5, R.id.xinyi_bt6, R.id.xinyi_bt7,
			R.id.xinyi_bt8, R.id.xinyi_bt9, R.id.xinyi_bt10 };

	@Override
	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.company_evaluation_person);
		setBackButton();
		setTopTitle("人员评价");
		RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
		topLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));
		/**
		 * 实现RadioGroup单选互斥选择的逻辑，否则出现多选
		 */
		radioGroup1 = (RadioGroup) findViewById(R.id.orderBy1);
		radioGroup1
				.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
		radioGroup2 = (RadioGroup) findViewById(R.id.orderBy2);
		radioGroup2
				.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
		// ///////
		activity_id = getIntent().getStringExtra("activity_id");
		user_id = getIntent().getStringExtra("user_id");

		url_ziliao = Url.COMPANY_cancelApply + "?token="
				+ MainCompanyActivity.token;
		url_comtent = Url.COMPANY_commentRequirer + "?token="
				+ MainCompanyActivity.token;
		getData();
		// 评价
		gree_cancel = (Button) findViewById(R.id.gree_cancel);
		gree_cancel.setText("提交评论");

		content = (EditText) findViewById(R.id.cancel_neirong);
		gree_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				comfiyComment();
			}
		});
	}

	private void setComment() {
		radioButton1 = (RadioButton) findViewById(radioGroup1
				.getCheckedRadioButtonId());
		radioButton2 = (RadioButton) findViewById(radioGroup2
				.getCheckedRadioButtonId());
		if (radioButton1 != null) {
			text = radioButton1.getText().toString().replaceAll(" ", ""); // 去掉字符串空格
			type_st = text;

		}
		if (radioButton2 != null) {
			text2 = radioButton2.getText().toString().replaceAll(" ", "");
			type_st = text2;
		}
		if (type_st.equals("优秀")) {
			content.setText("做的很好，非常感谢你对本次活动的支持与配合。");
		} else if (type_st.equals("中评")) {
			content.setText("期待下次与你合作;");
		} else if (type_st.equals("差评")) {
			content.setText("其实你可以做的很好;");
		} else if (type_st.equals("放飞机")) {
			content.setText("如临时有事，希望提前告知。");
		}
	}

	private void comfiyComment() {
		radioButton1 = (RadioButton) findViewById(radioGroup1
				.getCheckedRadioButtonId());
		radioButton2 = (RadioButton) findViewById(radioGroup2
				.getCheckedRadioButtonId());
		if (radioButton1 != null) {
			text = radioButton1.getText().toString().replaceAll(" ", ""); // 去掉字符串空格
			type_st = text;

		}
		if (radioButton2 != null) {
			text2 = radioButton2.getText().toString().replaceAll(" ", "");
			type_st = text2;
		}

		neirong = content.getText().toString();
		if (neirong != null && neirong.length() > 0) {
			gree_cancel.setClickable(false);
			StringRequest stringRequest = new StringRequest(Method.POST,
					url_comtent, new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							try {
								JSONObject js = new JSONObject(response);
								JSONObject jss = js
										.getJSONObject("AgreeCancelApplyResponse");
								status = jss.getInt("status");
								if (status == 2) {
									String msg = jss.getString("msg");
									showAlertDialog2(msg, "温馨提示");
								} else {
									getResult();
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError arg0) {
							gree_cancel.setClickable(true);
							showToast("提交失败");
						}
					}) {
				@Override
				protected Map<String, String> getParams() {
					Map<String, String> map = new HashMap<String, String>();
					map.put("user_id", user_id);
					map.put("activity_id", activity_id);
					map.put("comment", type_st);
					map.put("remark", neirong + "");// 评语
					return map;
				}
			};
			queue.add(stringRequest);
			stringRequest.setRetryPolicy(new DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1,
					1.0f));

		} else {
			showToast("请输入评语");
		}
	}

	// 评论结果
	protected void getResult() {
		if (status == 1) {
			showToast("评价成功");
			PersonAssessDetailActivity.this.finish();
		} else {
			gree_cancel.setClickable(true);
			showToast("提交失败");
		}
	}

	// 异步加载数据
	private void getData() {
		showWait(true);
		StringRequest stringRequest = new StringRequest(Method.POST,
				url_ziliao, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js
									.getJSONObject("CancelApplyResponse");
							cancelApply = (CancelApply) JsonUtil.jsonToBean(
									jss, CancelApply.class);
							initView();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						showWait(false);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", user_id);
				map.put("activity_id", activity_id);
				return map;
			}
		};
		queue.add(stringRequest);
		stringRequest
				.setRetryPolicy(new DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1, 1.0f));

	}

	protected void initView() {
		cancel_name = (TextView) findViewById(R.id.cancel_name);
		cancel_name.setText(cancelApply.getName());
		sex_img = (ImageView) findViewById(R.id.item_sex_imv);
		if (cancelApply.getSex() == -1) {
			sex_img.setVisibility(View.INVISIBLE);
		} else if (cancelApply.getSex() == 0) {
			sex_img.setImageResource(R.drawable.my_women);
			sex_img.setVisibility(View.VISIBLE);
		} else {
			sex_img.setImageResource(R.drawable.my_men);
			sex_img.setVisibility(View.VISIBLE);
		}
		age = (TextView) findViewById(R.id.age);
		age.setText(cancelApply.getAge() + "岁");
		note = (TextView) findViewById(R.id.note);
		if (cancelApply.getNote() == null || cancelApply.getNote().equals("")) {
			note.setText("没有填写理由。");
		} else {
			note.setText(cancelApply.getNote());
		}
		yan_img = (ImageView) findViewById(R.id.yan_img);
		if (cancelApply.getCertification() == 2) {
			yan_img.setImageResource(R.drawable.my_certified);
		} else {
			yan_img.setImageResource(R.drawable.my_unauthorized);
		}
		cyj_img = (ImageView) findViewById(R.id.cyj_img);
		if (cancelApply.getEarnest_money() == 1) {
			cyj_img.setImageResource(R.drawable.my_margin);
		} else {
			cyj_img.setImageResource(R.drawable.my_ordinary);
		}
		CircularImage cover_user_photo = (CircularImage) findViewById(R.id.cover_user_photo);
		if (cancelApply.getPicture_1() == null
				|| cancelApply.getPicture_1().equals("")) {
		} else {

			// LoadImage.loadImage(Url.GETPIC + cancelApply.getPicture_1(),
			// cover_user_photo);
			checkPhotoExits(cancelApply.getPicture_1(), cover_user_photo);
		}
		addXinToView(cancelApply.getCreditworthiness());
	}

	class MyRadioGroupOnCheckedChangedListener implements
			OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (!changeedGroup) {
				changeedGroup = true;
				if (group == radioGroup1) {
					radioGroup2.clearCheck();
					setComment();
				} else if (group == radioGroup2) {
					radioGroup1.clearCheck();
					setComment();
					radioButton2 = (RadioButton) findViewById(radioGroup2
							.getCheckedRadioButtonId());
					text2 = radioButton2.getText().toString()
							.replaceAll(" ", "");
					if (text2.equals("放飞机")) {
						showAlertDialog("评价放飞机，该用户将会被封号处理，您确定TA未提前通知您吗？",
								"温馨提示");
					}
				}
				changeedGroup = false;
			}
		}
	}

	public void showAlertDialog2(String str, final String str2) {

		CustomDialogThree.Builder builder = new CustomDialogThree.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);
		builder.setPositiveButton("我知道了",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
		builder.create().show();
	}

	public void showAlertDialog(String str, final String str2) {

		CustomDialogThree.Builder builder = new CustomDialogThree.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);
		builder.setPositiveButton("点错了", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				RadioButton youxiu = (RadioButton) findViewById(R.id.youxiu);
				youxiu.setChecked(true);
			}
		});

		builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				RadioButton feiji = (RadioButton) findViewById(R.id.wind_type_wangongjie);
				feiji.setChecked(true);
			}
		});

		builder.create().show();
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
			}
		} else {
			for (int a = 0; a < 10; a++) {
				ImageView imageView = (ImageView) findViewById(heartImg[a]);
				imageView.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 判断本地是否存储了之前的照片
	 * 
	 */

	private void checkPhotoExits(String picName, ImageView iv) {
		File mePhotoFold = new File(Environment.getExternalStorageDirectory()
				+ "/" + "jzdr/" + "image");
		if (!mePhotoFold.exists()) {
			mePhotoFold.mkdirs();
		}
		File f = new File(Environment.getExternalStorageDirectory() + "/"
				+ "jzdr/" + "image/" + picName);
		if (f.exists()) {
			// Bitmap bb_bmp = MyResumeActivity.zoomImg(f, 300, 300);
			Bitmap bb_bmp = BitmapFactory.decodeFile(Environment
					.getExternalStorageDirectory()
					+ "/"
					+ "jzdr/"
					+ "image/"
					+ picName);
			if (bb_bmp != null) {
				iv.setImageBitmap(LoadImage.toRoundBitmap(bb_bmp));
			} else {
				loadpersonPic(picName, iv, 0);
			}

		} else {
			loadpersonPic(picName, iv, 0);
		}

	}

	/**
	 * @Description: 加载图片
	 * @author howe
	 * @date 2014-7-30 下午5:57:52
	 * 
	 */

	private void loadpersonPic(final String picName, final ImageView imageView,
			final int isRound) {
		ImageRequest imgRequest = new ImageRequest(Url.GETPIC + picName,
				new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap arg0) {
						if (isRound == 1) {
						} else {
							imageView.setImageBitmap(arg0);
							OutputStream output = null;
							try {
								File mePhotoFold = new File(
										Environment
												.getExternalStorageDirectory()
												+ "/" + "jzdr/" + "image");
								if (!mePhotoFold.exists()) {
									mePhotoFold.mkdirs();
								}
								output = new FileOutputStream(
										Environment
												.getExternalStorageDirectory()
												+ "/"
												+ "jzdr/"
												+ "image/"
												+ picName);
								arg0.compress(Bitmap.CompressFormat.JPEG, 100,
										output);
								output.flush();
								output.close();
							} catch (Exception e) {
								e.printStackTrace();
							}

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
