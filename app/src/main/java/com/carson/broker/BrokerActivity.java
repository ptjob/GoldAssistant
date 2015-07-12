package com.carson.broker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.qingmu.jianzhidaren.R;
import com.quark.adapter.BrokerAdapter;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.http.image.CircularImage;
import com.quark.http.image.LoadImage;
import com.quark.jianzhidaren.BaseActivity;
import com.parttime.main.MainTabActivity;
import com.quark.model.BrokerBean;
import com.quark.volley.VolleySington;

public class BrokerActivity extends BaseActivity implements IXListViewListener {
	ArrayList<BrokerBean> lists = new ArrayList<BrokerBean>();
	XListView list;
	private BrokerAdapter adpter;
	private String dataUrl;
	int pageNumber = 1;
	int currentCount = 1;
	int page_size = 5;
	private String user_id;
	private SharedPreferences sp;
	private RequestQueue queue;
	private ImageView nodata_img;
	private CircularImage cover_user_photo;
	private String city;
	// 当前经纪人个人信息
	private String avatar, name;// 头像
	private int rank, fans;// 排名、粉丝数
	private TextView my_broker_tv, my_funs_tv, name_tv, content_tv;// 排行榜、粉丝数、名字、描述、干得漂亮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_broker_paihangbang);
		cover_user_photo = (CircularImage) findViewById(R.id.cover_user_photo);
		name_tv = (TextView) findViewById(R.id.broker_name_tv);
		TextPaint tp = name_tv.getPaint();
		tp.setFakeBoldText(true);
		my_broker_tv = (TextView) findViewById(R.id.broker_paiming_tv);
		// 排行榜设置粗体
		TextPaint tp2 = my_broker_tv.getPaint();
		tp2.setFakeBoldText(true);
		my_funs_tv = (TextView) findViewById(R.id.broker_fans_num_tv);
		content_tv = (TextView) findViewById(R.id.broker_top_content_tv);
		nodata_img = (ImageView) findViewById(R.id.nodata_img);
		queue = VolleySington.getInstance().getRequestQueue();
		setTopTitle("金牌助理排行榜");
		setBackButton();
		dataUrl = Url.BROKER_LIST + "?token=" + MainTabActivity.token;
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		city = sp.getString("city", "");
		list = (XListView) findViewById(R.id.my_broker_list);
		list.setPullLoadEnable(true);
		adpter = new BrokerAdapter(BrokerActivity.this, lists);
		list.setAdapter(adpter);
		// list.setPullRefreshEnable(true);
		list.setXListViewListener(this);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

			}
		});
		// 头部显示灰色
		RelativeLayout reLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
		reLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));

	}

	/**
	 * 清空数据链表
	 */
	private void init_lists() {
		lists = new ArrayList<BrokerBean>();
	}

	@Override
	protected void onResume() {
		super.onResume();
		init_lists();// 置空数据链表
		pageNumber = 1;
		getData();
		adpter = new BrokerAdapter(BrokerActivity.this, lists);
		list.setAdapter(adpter);

	}

	public void resetData() {
		init_lists();// 置空数据链表
		pageNumber = 1;
		getData();
	}

	// ========================xlist=================================================
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (lists != null && lists.size() > 0) {
				nodata_img.setVisibility(View.GONE);
				list.setLoadOver(currentCount, page_size);// 用于是否加载完了
				adpter.notifyDataSetChanged();
				onLoad();
			} else {
				nodata_img.setVisibility(View.VISIBLE);
				list.setVisibility(View.GONE);
			}
			if (name == null || "".equals(name)) {
			} else {
				name_tv.setText(name);
			}
			if (avatar == null || "".equals(avatar)) {
				cover_user_photo
						.setImageResource(R.drawable.default_avatar_business);
			} else {
				checkPhotoExits(avatar, cover_user_photo);
			}
			if (rank > 0) {
				my_broker_tv.setText("No." + rank);
				if (rank > 3) {
					content_tv.setText("做得不错,继续加油。");
				} else {
					content_tv.setText("干得漂亮,继续保持!");
				}
			}
			if (fans > 0)
				my_funs_tv.setText(String.valueOf(fans) + "粉丝");
		};
	};

	private void onLoad() {
		list.stopRefresh();
		list.stopLoadMore();
		list.setRefreshTime("刚刚");
	}

	/**
	 * 分享兼职活动
	 */
	public void setTopTitle(String titlestr) {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(titlestr);
	}

	/**
	 * 设置返回按钮
	 */
	public void setBackButton() {

		LinearLayout back_lay = (LinearLayout) findViewById(R.id.left);
		back_lay.setVisibility(View.VISIBLE);
		back_lay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onRefresh() {
		pageNumber = 1;
		lists.clear();
		getData();
		adpter = new BrokerAdapter(BrokerActivity.this, lists);
		list.setAdapter(adpter);
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getData();
	}

	// ======xlist end============

	public void getData() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST, dataUrl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss2 = js
									.getJSONObject("responseStatus");
							int status = jss2.getInt("status");
							// status 1表示获取成功
							if (status == 1) {
								// 经纪人个人信息
								rank = jss2.getInt("rank");// 排名
								fans = jss2.getInt("fans");// 粉丝数
								name = jss2.getString("name");// 商家自己名字
								JSONObject jss = jss2.getJSONObject("company");
								avatar = jss.getString("avatar");// 获取个人头像
								JSONObject jsss = jss2
										.getJSONObject("agentPage");
								// 其它经纪人信息列表
								JSONArray ls = jsss.getJSONArray("list");
								if (ls.length() > 0) {
									for (int i = 0; i < ls.length(); i++) {
										BrokerBean brokerBean = (BrokerBean) JsonUtil
												.jsonToBean(
														ls.getJSONObject(i),
														BrokerBean.class);
										lists.add(brokerBean);
									}
								}
								currentCount = ls.length();
								Message msg = handler.obtainMessage();
								msg.what = 20;
								handler.sendMessage(msg);
							} else {
								Message msg = handler.obtainMessage();
								msg.what = 20;
								handler.sendMessage(msg);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Message msg = handler.obtainMessage();
							msg.what = 20;
							handler.sendMessage(msg);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						Message msg = handler.obtainMessage();
						msg.what = 20;
						handler.sendMessage(msg);
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("company_id", user_id);
				map.put("city", city);
				map.put("page_size", page_size + "");
				map.put("pn", pageNumber + "");
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

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
		imgRequest.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}
}
