package com.quark.quanzi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.easemob.chat.EMContactManager;
import com.easemob.chatuidemo.Constant;
import com.parttime.IM.ChatActivity;
import com.easemob.chatuidemo.domain.User;
import com.lidroid.xutils.ViewUtils;
import com.parttime.constants.ApplicationConstants;
import com.qingmu.jianzhidaren.R;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.http.image.CircularImage;
import com.quark.http.image.LoadImage;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.model.HuanxingUserInfo;
import com.quark.ui.widget.EditDialog;

/**
 * 联系人列表页
 * 
 */
public class UserInfo extends BaseActivity {
	private String dataUrl;
	HuanxingUserInfo hxus;
	String hxId;
	int[] heartImg = { R.id.xinyi_bt1, R.id.xinyi_bt2, R.id.xinyi_bt3,
			R.id.xinyi_bt4, R.id.xinyi_bt5, R.id.xinyi_bt6, R.id.xinyi_bt7,
			R.id.xinyi_bt8, R.id.xinyi_bt9, R.id.xinyi_bt10 };
	private int creditworthiness;// 信誉值：步长为：10为一个心，5为半个心

	private boolean friendFlag = false;// 判断是否是好友
	private Button add_friend_btn;// 添加好友or发送消息
	private ProgressDialog progressDialog;
	private SharedPreferences sp;

	CircularImage cover_user_photo;// 头像
	private ImageView my_sex_imv, yan_img_imv, cyj_imv;// 性别
	private TextView nameTv;// 姓名
	private LinearLayout xinyu_linearlayout;

	/**
	 * 发送好友弹框
	 */
	public void showAlertDialog(String str, final String str2) {

		final EditDialog.Builder builder = new EditDialog.Builder(UserInfo.this);
		builder.setMessage(str);
		builder.setTitle(str2);

		builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				final String content = builder.getContent();
				dialog.dismiss();
				progressDialog = new ProgressDialog(UserInfo.this);
				progressDialog.setMessage("正在发送请求...");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();

				new Thread(new Runnable() {
					public void run() {
						try {
							String sendstr = "";
							// demo写死了个reason，实际应该让用户手动填入
							if (content.isEmpty()) {
								sendstr = "邀请你为好友";
							} else {
								sendstr = content;
							}
							EMContactManager.getInstance().addContact(hxId,
									sendstr);
							UserInfo.this.runOnUiThread(new Runnable() {
								public void run() {
									progressDialog.dismiss();
									Toast.makeText(UserInfo.this,
											"发送请求成功,等待对方验证", 1).show();
									add_friend_btn.setText("等待对方验证中...");
									add_friend_btn.setEnabled(false);
								}
							});
						} catch (final Exception e) {
							UserInfo.this.runOnUiThread(new Runnable() {
								public void run() {
									progressDialog.dismiss();
									Toast.makeText(UserInfo.this,
											"请求添加好友失败:" + e.getMessage(), 1)
											.show();
								}
							});
						}
					}
				}).start();
			}
		});
		builder.create().show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.huanxing_user_info);
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		ImageView back = (ImageView) findViewById(R.id.left);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserInfo.this.finish();
			}
		});
		// 头像
		cover_user_photo = (CircularImage) findViewById(R.id.cover_user_photo);
		// 姓名
		nameTv = (TextView) findViewById(R.id.name);
		// 性别
		my_sex_imv = (ImageView) findViewById(R.id.my_sex_imv);
		yan_img_imv = (ImageView) findViewById(R.id.yan_img);
		cyj_imv = (ImageView) findViewById(R.id.cyj_img);
		xinyu_linearlayout = (LinearLayout) findViewById(R.id.xinyu_linearlayout);

		add_friend_btn = (Button) findViewById(R.id.add_friend_btn);
		add_friend_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 如果是好友关系则跳转到发送消息界面,反之则发送好友请求
				if (friendFlag) {
					UserInfo.this.finish();
					if (!ApplicationConstants.JZDR.equals(hxId)) {
						startActivity(new Intent(UserInfo.this,
								ChatActivity.class).putExtra("userId", hxId));
					}
				} else {
					showAlertDialog("留言", "留言");
				}

			}
		});
		ViewUtils.inject(this);
		dataUrl = Url.HUANXIN_user_info;
		hxId = getIntent().getStringExtra("hxId");
		// 如果是商家,hxId以C开头,取消显示信誉值
		if (hxId != null && !"".equals(hxId)) {
			String userId = sp.getString("userId", "");
			if (!"".equals(userId)) {
				if (hxId.contains(userId)) {
					add_friend_btn.setVisibility(View.GONE);
				}
			}
			if (hxId.contains("c")) {
				xinyu_linearlayout.setVisibility(View.GONE);
				cyj_imv.setVisibility(View.GONE);
			}

		}
		// 判断是否是好友关系
		getFriendList();
		getData();
	}

	/**
	 * 获取当前用户好友列表
	 */
	private void getFriendList() {
		List<User> friendList = new ArrayList<User>();// 好友列表
		try {
			// friendList =
			// EMContactManager.getInstance().getContactUserNames();
			// 获取本地好友列表
			Map<String, User> users = ApplicationControl.getInstance()
					.getContactList();
			Iterator<Entry<String, User>> iterator = users.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, User> entry = iterator.next();
				if (!entry.getKey().equals(Constant.NEW_FRIENDS_USERNAME)
						&& !entry.getKey().equals(Constant.GROUP_USERNAME)
						&& !entry.getKey().equals(Constant.PUBLIC_COUNT)
                        ) {
					// 这里有bug，会有好友列表有uid,没有名字的情况
					if (!entry.getKey().equals(ApplicationConstants.JZDR)) {
						// userName ==nick 都是u661或者c221之类的
						// head 是u或者c
						friendList.add(entry.getValue());
					}
				}
			}
		} catch (Exception e) {
			friendList = new ArrayList<User>();
			e.printStackTrace();
		}
		if (friendList.size() > 0) {
			for (int i = 0; i < friendList.size(); i++) {
				if (friendList.get(i).getUsername().equals(hxId)) {
					friendFlag = true;
					return;
				}
			}
		}

	}

	public void getData() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST, dataUrl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jstatu = js
									.getJSONObject("ResponseStatus");
							JSONObject jinfo = js.getJSONObject("info");
							int status = jstatu.getInt("status");
							if (status == 2) {
								Toast.makeText(getApplicationContext(), "无此用户",
										0).show();
							}
							hxus = (HuanxingUserInfo) JsonUtil.jsonToBean(
									jinfo, HuanxingUserInfo.class);
							Editor edt = sp.edit();
							edt.putString(hxId + "_photo", hxus.getAvatar());
							edt.putString(hxId + "realname", hxus.getName());
							edt.commit();
							initView();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						hxus = new HuanxingUserInfo();
						hxus.setAvatar(sp.getString(hxId + "_photo", ""));
						hxus.setName(sp.getString(hxId + "realname", ""));
						hxus.setSex(-1);
						initView();
						Toast.makeText(getApplicationContext(), "哎呀,网络太糟糕了", 0)
								.show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", hxId + "");
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1, 1.0f));

	}

	/**
	 * 加载本地头像和名字
	 */
	private void loadNativePhoto(final String id, final ImageView avatar) {
		// 先获取本地名字和头像
		File mePhotoFold = new File(Environment.getExternalStorageDirectory()
				+ "/" + "jzdr/" + "image");
		if (!mePhotoFold.exists()) {
			mePhotoFold.mkdirs();
		}
		// 当前聊天对象的头像更改,要先联网验证头像路径是否更改
		File picture_1 = new File(Environment.getExternalStorageDirectory()
				+ "/" + "jzdr/" + "image/" + sp.getString(id + "_photo", "c"));
		if (picture_1.exists()) {
			// 加载本地图片
			// Bitmap bb_bmp = MyResumeActivity.zoomImg(picture_1, 300, 300);
			Bitmap bb_bmp = BitmapFactory.decodeFile(Environment
					.getExternalStorageDirectory()
					+ "/"
					+ "jzdr/"
					+ "image/"
					+ sp.getString(id + "_photo", "c"));
			if (bb_bmp != null) {
				avatar.setImageBitmap(LoadImage.toRoundBitmap(bb_bmp));
			} else {
				loadpersonPic(hxus.getAvatar(), avatar, 1);
			}
		} else {
			loadpersonPic(hxus.getAvatar(), avatar, 1);

		}

	}

	public void initView() {
		if ((hxus.getAvatar() != null) && (!hxus.getAvatar().equals(""))) {
			loadNativePhoto(hxId, cover_user_photo);
			// loadpersonPic(Url.GETPIC + hxus.getAvatar(), head, 1);
		} else {
			cover_user_photo.setImageResource(R.drawable.default_avatar);
		}
		creditworthiness = hxus.getCertification();
		nameTv.setText(hxus.getName());
		if (hxus.getSex() == -1) {
			my_sex_imv.setVisibility(View.GONE);
		} else if (hxus.getSex() == 1) {
			my_sex_imv.setVisibility(View.VISIBLE);
			my_sex_imv.setImageResource(R.drawable.my_men);
		} else if (hxus.getSex() == 0) {
			my_sex_imv.setVisibility(View.VISIBLE);
			my_sex_imv.setImageResource(R.drawable.my_women);
		}

		if (hxus.getCertification() == 2) {
			yan_img_imv.setImageResource(R.drawable.my_certified);
		} else {
			yan_img_imv.setImageResource(R.drawable.my_unauthorized);
		}
		if (hxus.getEarnest_money() == 1) {
			cyj_imv.setImageResource(R.drawable.my_margin);
		} else {
			cyj_imv.setImageResource(R.drawable.my_ordinary);
		}
		addXinToView(hxus.getCreditworthiness());
		if (friendFlag) {
			// message_title_tv.setText("查看好友");
			add_friend_btn.setText("发消息");
		} else {
			// message_title_tv.setText("添加好友");
			add_friend_btn.setText("发送好友申请");
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
						// Bitmap bit = UploadImg.toRoundCorner(arg0, 2);
						// imageView.setImageBitmap(UploadImg.toRoundCorner(arg0,
						// 2));
						String picName = url;
						imageView.setImageBitmap(LoadImage.toRoundBitmap(arg0));
						OutputStream output = null;
						try {
							File mePhotoFold = new File(Environment
									.getExternalStorageDirectory()
									+ "/"
									+ "jzdr/" + "image");
							if (!mePhotoFold.exists()) {
								mePhotoFold.mkdirs();
							}
							output = new FileOutputStream(Environment
									.getExternalStorageDirectory()
									+ "/"
									+ "jzdr/" + "image/" + picName);
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
		imgRequest.setRetryPolicy(new DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1, 1.0f));

	}

	public void addXinToView(int xin) {
		if (xin > 0) {
			// 如 60 是整10的
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

			}
		} else {
			for (int a = 0; a < 10; a++) {
				ImageView imageView = (ImageView) findViewById(heartImg[a]);
				imageView.setVisibility(View.GONE);
			}

		}
	}
}
