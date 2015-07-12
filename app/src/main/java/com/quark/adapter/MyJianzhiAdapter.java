package com.quark.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.carson.loadpic.CarsonLoadPic;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.qingmu.jianzhidaren.R;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.company.function.FullStarffedActivity;
import com.quark.company.function.PersonAssessActivity;
import com.quark.guanli.BaomingListActivity;
import com.quark.guanli.MyJianzhiDetailActivity;
import com.parttime.main.MainTabActivity;
import com.quark.model.MyJianzhi;
import com.quark.ui.widget.CustomDialog;
import com.quark.ui.widget.CustomDialogThree;
import com.quark.utils.NetWorkCheck;
import com.quark.volley.VolleySington;
import com.thirdparty.alipay.RechargeActivity;

/**
 * 
 * @ClassName: MyJianzhiAdapter
 * @Description: TODO
 * @author howe
 * @date 2015-1-22 下午8:40:37
 * 
 */
public class MyJianzhiAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<MyJianzhi> list;
	private Context context;

	private ImageView qrImgImageView;
	Bitmap mBitmap;
	public int status = 0;
	// 图片宽度的一般
	private static final int IMAGE_HALFWIDTH = 50;
	private String sign_url, previewRefreshUrl, refreshUrl;// 签到URL、预刷新、刷新
	private String company_id;
	private SharedPreferences sp;
	protected RequestQueue queue;
	private MyJianzhi jianzhi;

	public MyJianzhiAdapter(Context context, List<MyJianzhi> list) {
		this.list = list;
		this.context = context;
		sp = context.getSharedPreferences("jrdr.setting", context.MODE_PRIVATE);
		company_id = sp.getString("userId", "");
		queue = VolleySington.getInstance().getRequestQueue();
		refreshUrl = Url.COMPANY_MyJianzhi_reflesh + "?token="
				+ MainTabActivity.token;
		previewRefreshUrl = Url.COMPANY_MyJianzhi_previewReflesh + "?token="
				+ MainTabActivity.token;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int i, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_myjianzhi_2, null);
			holder.titleTv = (TextView) convertView.findViewById(R.id.title);
			holder.statusTv = (TextView) convertView
					.findViewById(R.id.item_status_tv);
			holder.addressTv = (TextView) convertView
					.findViewById(R.id.item_address_tv);
			holder.dateTimeTv = (TextView) convertView
					.findViewById(R.id.item_datetime_tv);
			holder.scanTv = (TextView) convertView
					.findViewById(R.id.item_scans_tv);
			holder.introTv = (TextView) convertView
					.findViewById(R.id.item_intro_tv);
			holder.confirmedTv = (TextView) convertView
					.findViewById(R.id.item_confirmed_people_tv);
			holder.totalTv = (TextView) convertView
					.findViewById(R.id.item_all_people_tv);
			holder.weiChuliTv = (TextView) convertView
					.findViewById(R.id.item_unconfirmed_number_tv);
			holder.weiPingjiaImv = (ImageView) convertView
					.findViewById(R.id.item_uncomment_imv);
			holder.item_photo_layout = (LinearLayout) convertView
					.findViewById(R.id.item_photo_layout);
			holder.rosterLayout = (RelativeLayout) convertView
					.findViewById(R.id.item_roster_layout);
			holder.pingjiaLayout = (RelativeLayout) convertView
					.findViewById(R.id.item_pingjia_layout);
			holder.img1 = (ImageView) convertView
					.findViewById(R.id.item_photo_1);
			holder.img2 = (ImageView) convertView
					.findViewById(R.id.item_photo_2);
			holder.img3 = (ImageView) convertView
					.findViewById(R.id.item_photo_3);
			holder.img4 = (ImageView) convertView
					.findViewById(R.id.item_photo_4);
			holder.img5 = (ImageView) convertView
					.findViewById(R.id.item_photo_5);
			holder.reFreshBtn = (Button) convertView
					.findViewById(R.id.item_refresh_btn);
			holder.signBtn = (Button) convertView
					.findViewById(R.id.item_sign_btn);
			holder.button1 = (Button) convertView
					.findViewById(R.id.item_button_01);
			holder.button2 = (Button) convertView
					.findViewById(R.id.item_button_02);
			holder.button3 = (Button) convertView
					.findViewById(R.id.item_button_03);
			holder.item_topLayout = (LinearLayout) convertView
					.findViewById(R.id.item_top_layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.titleTv.setText(list.get(i).getTitle());// titile

		// 活动是否是已取缔
		if (list.get(i).getActivity_status() == 4) {
			holder.statusTv.setText("被冻结");
			holder.statusTv.setTextColor(context.getResources().getColor(
					R.color.guanli_beiqudi_color));
		} else {
			if (list.get(i).getStatus() == 1) {
				// 待审核
				holder.statusTv.setText("待审核");
				holder.statusTv.setTextColor(context.getResources().getColor(
						R.color.guanli_shenhe_color));
			} else if (list.get(i).getStatus() == 2) {
				// 审核通过招人中
				int hc = list.get(i).getHead_count();
				int cc = list.get(i).getConfirmed_count();
				if (hc == cc) {
					// 已招满
					holder.statusTv.setText("已招满");
					holder.statusTv.setTextColor(context.getResources()
							.getColor(R.color.guanli_yimanyuan_color));
				} else {
					holder.statusTv.setText("招人中");
					holder.statusTv.setTextColor(context.getResources()
							.getColor(R.color.guanli_zhaorenzhong_color));

				}
			} else if (list.get(i).getStatus() == 3) {// 审核不通过
				holder.statusTv.setText("未通过");
				holder.statusTv.setTextColor(context.getResources().getColor(
						R.color.guanli_weitongguo_color));
			} else if (list.get(i).getStatus() == 4) {// 已下架
				holder.statusTv.setText("已下架");
				holder.statusTv.setTextColor(context.getResources().getColor(
						R.color.guanli_yixiajia_color));
			}
		}

		holder.addressTv.setText(list.get(i).getCounty());// 发布地点

		if (list.get(i).getPublish_time() != null
				&& !"".equals(list.get(i).getPublish_time())) {

			if (list.get(i).getPublish_time().length() > 5) {
				if (list.get(i).getNow() != null
						&& !"".equals(list.get(i).getNow())) {
					String cNow = list.get(i).getNow();
					String pubTime = list.get(i).getPublish_time();
					// 2015-05-10 21:15:12
					if (cNow.length() > 5) {
						cNow = cNow.substring(5, 10);
						pubTime = pubTime.substring(5, 10);
						if (cNow.equals(pubTime)) {
							holder.dateTimeTv.setText("今天"
									+ list.get(i).getPublish_time()
											.substring(11, 16));// 发布时间
						} else {
							holder.dateTimeTv.setText(pubTime);// 发布时间
						}
					} else {
						holder.dateTimeTv.setText(list.get(i).getPublish_time()
								.substring(5));// 发布时间
					}

				} else {
					holder.dateTimeTv.setText(list.get(i).getPublish_time()
							.substring(5));// 发布时间
				}

			} else
				holder.dateTimeTv.setText(list.get(i).getPublish_time());// 发布时间

		}
		holder.scanTv.setText(list.get(i).getView_count() + "次");// 浏览次数
		holder.introTv.setText(list.get(i).getRequire_info());// 活动介绍
		holder.confirmedTv.setText(list.get(i).getConfirmed_count() + "");

		if (list.get(i).getUncheck_count() > 0) {
			holder.weiChuliTv.setText((list.get(i).getUncheck_count() > 99 ? 99
					: list.get(i).getUncheck_count()) + "");// 未处理人数
			holder.weiChuliTv.setVisibility(View.VISIBLE);
			holder.button1.setVisibility(View.VISIBLE);
		} else {
			holder.button1.setVisibility(View.GONE);
			holder.button2.setVisibility(View.GONE);
			holder.weiChuliTv.setVisibility(View.INVISIBLE);
		}

		if (sp.getBoolean(ConstantForSaveList.userId
				+ list.get(i).getActivity_id(), false)) {

			holder.weiPingjiaImv.setVisibility(View.VISIBLE);// 未评价数(录取后又取消报名)
		} else {
			holder.weiPingjiaImv.setVisibility(View.INVISIBLE);
		}

		// 点击进入活动详情
		holder.item_topLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				jianzhi = list.get(i);
				if (jianzhi.getActivity_status() == 4) {
					ToastUtil.showShortToast("您的活动已被冻结,请联系客服");
				} else {
					Intent intent = new Intent();
					intent.setClass(context, MyJianzhiDetailActivity.class);
					intent.putExtra("activity_id", jianzhi.getActivity_id()
							+ "");
					context.startActivity(intent);
				}
			}
		});
		jianzhi = list.get(i);

		// 显示最新报名的5个人员头像
		JSONArray jsArray = null;
		jsArray = jianzhi.getApply_list();
		// 如果没有报名人员和已录取人员,则头像栏隐藏
		if (jsArray != null && jianzhi != null) {
			if (jsArray.length() > 0 || jianzhi.getConfirmed_count() > 0) {
				holder.item_photo_layout.setVisibility(View.VISIBLE);
				holder.totalTv.setText("/" + jsArray.length());// 总报名数
				if (jsArray.length() == 1) {
					holder.img1.setVisibility(View.VISIBLE);
					holder.img2.setVisibility(View.INVISIBLE);
					holder.img3.setVisibility(View.INVISIBLE);
					holder.img4.setVisibility(View.INVISIBLE);
					holder.img5.setVisibility(View.INVISIBLE);
				} else if (jsArray.length() == 2) {
					holder.img1.setVisibility(View.VISIBLE);
					holder.img2.setVisibility(View.VISIBLE);
					holder.img3.setVisibility(View.INVISIBLE);
					holder.img4.setVisibility(View.INVISIBLE);
					holder.img5.setVisibility(View.INVISIBLE);
				} else if (jsArray.length() == 3) {
					holder.img1.setVisibility(View.VISIBLE);
					holder.img2.setVisibility(View.VISIBLE);
					holder.img3.setVisibility(View.VISIBLE);
					holder.img4.setVisibility(View.INVISIBLE);
					holder.img5.setVisibility(View.INVISIBLE);
				} else if (jsArray.length() == 4) {
					holder.img1.setVisibility(View.VISIBLE);
					holder.img2.setVisibility(View.VISIBLE);
					holder.img3.setVisibility(View.VISIBLE);
					holder.img4.setVisibility(View.VISIBLE);
					holder.img5.setVisibility(View.INVISIBLE);
				} else if (jsArray.length() >= 5) {
					holder.img1.setVisibility(View.VISIBLE);
					holder.img2.setVisibility(View.VISIBLE);
					holder.img3.setVisibility(View.VISIBLE);
					holder.img4.setVisibility(View.VISIBLE);
					holder.img5.setVisibility(View.VISIBLE);
				}
				int pic_user_id = 0;
				String pic_name = null;
				for (int j = 0; j < jsArray.length(); j++) {
					if (j >= 5) {
						break;
					}
					JSONObject info;
					try {
						info = jsArray.getJSONObject(j);
						pic_user_id = info.getInt("user_id");
						pic_name = info.getString("picture_1");
						switch (j) {
						case 0:
							if (pic_user_id != 0 && pic_name != null
									&& !"".equals(pic_name)) {
								checkPhotoExits(pic_user_id, pic_name,
										holder.img1);
							} else {
								holder.img1
										.setImageBitmap(CarsonLoadPic.centerSquareScaleBitmap(
												BitmapFactory.decodeResource(
														context.getResources(),
														R.drawable.broker_ic_launcher),
												130));
							}
							break;
						case 1:
							if (pic_user_id != 0 && pic_name != null
									&& !"".equals(pic_name)) {

								checkPhotoExits(pic_user_id, pic_name,
										holder.img2);
							} else {
								holder.img2
										.setImageBitmap(CarsonLoadPic.centerSquareScaleBitmap(
												BitmapFactory.decodeResource(
														context.getResources(),
														R.drawable.broker_ic_launcher),
												130));
							}
							break;
						case 2:
							if (pic_user_id != 0 && pic_name != null
									&& !"".equals(pic_name)) {

								checkPhotoExits(pic_user_id, pic_name,
										holder.img3);
							} else {
								holder.img3
										.setImageBitmap(CarsonLoadPic.centerSquareScaleBitmap(
												BitmapFactory.decodeResource(
														context.getResources(),
														R.drawable.broker_ic_launcher),
												130));
							}
							break;
						case 3:
							if (pic_user_id != 0 && pic_name != null
									&& !"".equals(pic_name)) {

								checkPhotoExits(pic_user_id, pic_name,
										holder.img4);
							} else {
								holder.img4
										.setImageBitmap(CarsonLoadPic.centerSquareScaleBitmap(
												BitmapFactory.decodeResource(
														context.getResources(),
														R.drawable.broker_ic_launcher),
												130));
							}
							break;
						case 4:
							if (pic_user_id != 0 && pic_name != null
									&& !"".equals(pic_name)) {

								checkPhotoExits(pic_user_id, pic_name,
										holder.img5);
							} else {
								holder.img5
										.setImageBitmap(CarsonLoadPic.centerSquareScaleBitmap(
												BitmapFactory.decodeResource(
														context.getResources(),
														R.drawable.broker_ic_launcher),
												130));
							}
							break;
						default:
							break;
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

			} else {
				holder.item_photo_layout.setVisibility(View.GONE);
			}
		} else {
			holder.item_photo_layout.setVisibility(View.GONE);
		}
		// 是否显示花名册、评价人员
		if (jianzhi != null) {
			if (jianzhi.getConfirmed_count() > 0) {
				holder.rosterLayout.setVisibility(View.VISIBLE);
				holder.pingjiaLayout.setVisibility(View.VISIBLE);
				holder.button1.setVisibility(View.VISIBLE);
				holder.button2.setVisibility(View.VISIBLE);
				holder.button3.setVisibility(View.VISIBLE);
			} else {
				holder.rosterLayout.setVisibility(View.GONE);
				if (sp.getBoolean(ConstantForSaveList.userId
						+ list.get(i).getActivity_id(), false)) {
					holder.pingjiaLayout.setVisibility(View.VISIBLE);
				} else {
					holder.button1.setVisibility(View.GONE);
					holder.button2.setVisibility(View.GONE);
					holder.pingjiaLayout.setVisibility(View.GONE);
				}
			}

		}

		holder.item_photo_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				jianzhi = list.get(i);
				if (jianzhi.getActivity_status() == 4) {
					ToastUtil.showShortToast("您的活动已被冻结,请联系客服");
				} else {

					if (list.get(i).getUncheck_count() > 0) {
						// 跳转到未处理人员列表
						if (NetWorkCheck.isOpenNetwork(context)) {
							if (jianzhi != null) {
								Intent intent = new Intent(context,
										BaomingListActivity.class);
								intent.putExtra("activity_id",
										jianzhi.getActivity_id() + "");
								intent.putExtra("title", jianzhi.getTitle());
								intent.putExtra("female_count", String
										.valueOf(jianzhi.getFemale_count()));
								intent.putExtra("male_count",
										String.valueOf(jianzhi.getMale_count()));
								intent.putExtra("fromNotification", true);
								context.startActivity(intent);
							} else {
								ToastUtil.showShortToast("网络不好,请检查网络设置。");
							}
						} else {
							ToastUtil.showShortToast("网络不好,请检查网络设置。");
						}

					} else {
						// 跳转到全部人员列表
						if (NetWorkCheck.isOpenNetwork(context)) {
							if (jianzhi != null) {
								Intent intent = new Intent(context,
										BaomingListActivity.class);
								intent.putExtra("activity_id",
										jianzhi.getActivity_id() + "");
								intent.putExtra("title", jianzhi.getTitle());
								intent.putExtra("female_count", String
										.valueOf(jianzhi.getFemale_count()));
								intent.putExtra("male_count",
										String.valueOf(jianzhi.getMale_count()));
								intent.putExtra("fromNotification", false);
								context.startActivity(intent);
							} else {
								ToastUtil.showShortToast("网络不好,请检查网络设置。");
							}
						} else {
							ToastUtil.showShortToast("网络不好,请检查网络设置。");
						}
					}
				}

			}
		});
		holder.rosterLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				jianzhi = list.get(i);
				if (jianzhi.getActivity_status() == 4) {
					ToastUtil.showShortToast("您的活动已被冻结,请联系客服");
				} else {
					// 跳转到花名册
					Intent intent = new Intent();
					intent.setClass(context, FullStarffedActivity.class);
					intent.putExtra("activity_id", jianzhi.getActivity_id()
							+ "");
					intent.putExtra("activity_name", jianzhi.getTitle());
					intent.putExtra("total_num", jianzhi.getHead_count() + "");
					context.startActivity(intent);
				}

			}
		});
		holder.pingjiaLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				holder.weiPingjiaImv.setVisibility(View.INVISIBLE);
				// 跳转到活动评价列表
				jianzhi = list.get(i);
				if (jianzhi.getActivity_status() == 4) {
					ToastUtil.showShortToast("您的活动已被冻结,请联系客服");
				} else {
					Editor edt = sp.edit();
					edt.putBoolean(ConstantForSaveList.userId
							+ list.get(i).getActivity_id(), false);
					edt.commit();
					Intent intent = new Intent();
					intent.setClass(context, PersonAssessActivity.class);
					intent.putExtra("activity_id", jianzhi.getActivity_id()
							+ "");
					intent.putExtra("total_num", jianzhi.getConfirmed_count()
							+ "");
					context.startActivity(intent);
				}
			}
		});
		// 置顶
		holder.reFreshBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 获取当前list值
				jianzhi = list.get(i);
				if (jianzhi.getActivity_status() == 4) {
					ToastUtil.showShortToast("您的活动已被冻结,请联系客服");
				} else {
					if (NetWorkCheck.isOpenNetwork(context)) {
						if (jianzhi != null) {
							if (jianzhi.getStatus() == 3) {
								// 审核未通过不能刷新
								ToastUtil.showShortToast("审核未通过不能进行置顶");
							} else if (jianzhi.getStatus() == 4) {
								// 已下架的活动不能刷新
								ToastUtil.showShortToast("该活动已下架不能进行置顶");

							} else {
								if (jianzhi.getStatus() != 1) {
									// 弹框提示是否要刷新
									previewRefreshJianZhi(String
											.valueOf(jianzhi.getActivity_id()));
									// 点击刷选窗口关闭
								} else {
									ToastUtil.showShortToast("正在审核的兼职不能置顶");
								}
							}
						} else {
							ToastUtil.showShortToast("当前网络状况不好,请检查网络环境。");
						}

					} else {
						ToastUtil.showShortToast("当前网络状况不好,请检查网络环境。");
					}
				}
			}
		});
		holder.signBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				jianzhi = list.get(i);
				if (jianzhi.getActivity_status() == 4) {
					ToastUtil.showShortToast("您的活动已被冻结,请联系客服");
				} else {
					try {
						sign_url = Url.COMPANY_sign + "?token="
								+ MainTabActivity.token + "&company_id="
								+ company_id + "&activity_id="
								+ list.get(i).getActivity_id();
						initPopWindow(list.get(i).getActivity_id() + "");
					} catch (WriterException e) {
						e.printStackTrace();
					}
				}
			}
		});

		return convertView;
	}

	private static class ViewHolder {

		TextView titleTv, statusTv, addressTv, dateTimeTv, scanTv, introTv,
				confirmedTv, totalTv, weiChuliTv;
		ImageView img1, img2, img3, img4, img5, weiPingjiaImv;
		LinearLayout item_photo_layout, item_topLayout;
		RelativeLayout rosterLayout, pingjiaLayout;
		Button reFreshBtn, signBtn;// 刷新、签到
		Button button1, button2, button3;// 花名册上中下3条线
	}

	/**
	 * 判断本地是否存储了之前的照片
	 * 
	 */

	private void checkPhotoExits(int pic_user_id, String picName, ImageView iv) {
		File mePhotoFold = new File(Environment.getExternalStorageDirectory()
				+ "/" + "jzdr/" + "image");
		if (!mePhotoFold.exists()) {
			mePhotoFold.mkdirs();
		}
		File f = new File(Environment.getExternalStorageDirectory() + "/"
				+ "jzdr/" + "image/" + picName);
		if (f.exists()) {
			// Bitmap bb_bmp = MyResumeActivity.zoomImg(f, 150, 150);
			Bitmap bb_bmp = BitmapFactory.decodeFile(Environment
					.getExternalStorageDirectory()
					+ "/"
					+ "jzdr/"
					+ "image/"
					+ picName);
			if (bb_bmp != null) {
				iv.setImageBitmap(CarsonLoadPic.centerSquareScaleBitmap(bb_bmp,
						130));
			} else {
				loadpersonPic(pic_user_id, picName, iv, 0);
			}

		} else {
			loadpersonPic(pic_user_id, picName, iv, 0);
		}

	}

	/**
	 * @Description: 加载图片
	 * @author howe
	 * @date 2014-7-30 下午5:57:52
	 * 
	 */

	private void loadpersonPic(final int pic_user_id, final String picName,
			final ImageView imageView, final int isRound) {
		ImageRequest imgRequest = new ImageRequest(Url.GETPIC + picName,
				new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap arg0) {
						if (isRound == 1) {
						} else {
							imageView.setImageBitmap(CarsonLoadPic
									.centerSquareScaleBitmap(arg0, 130));
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
								Editor edt = sp.edit();
								edt.putString(pic_user_id + "_photo", picName);
								edt.commit();
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

	/**
	 * 刷新前先判断钱是否够用
	 * 
	 */
	public void previewRefreshJianZhi(final String activity_id) {
		StringRequest request = new StringRequest(Request.Method.POST,
				previewRefreshUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js
									.getJSONObject("PreRefleshResponse");
							int status = jss.getInt("status");
							String money = jss.getString("money");
							String msg = jss.getString("alert_msg");
							String titile = jss.getString("alert_title");
							String other = jss.getString("alert_other");
							String cancle = jss.getString("alert_cancle");
							if (status == 1) {
								// 状态1是当前可以免费刷新一次

								// showAlertDialog(
								// "您已成功刷新兼职，要记得把活动分享出去哦，让更多的人来报名吧",
								// "刷新成功", "我知道了");
								// true 付费 false 免费
								// 1付费 0 免费
								showRefreshAlertDialog(msg, titile, other,
										cancle, String.valueOf(status),
										activity_id);// msg,titile,button
							} else if (status == 2) {
								// 状态2是当前免费刷新次数用完,余额不足
								// showAlertDialog(
								// "您今日的免费刷新次数已使用完，如需刷新此条兼职需要付费3元",
								// "刷新失败", "立即充值");
								showFeeRefreshAlertDialog(msg, titile, other,
										cancle, money, String.valueOf(status),
										activity_id);
							} else if (status == 3) {
								// 状态3表示当前免费刷新次数用完,有可用余额
								// showAlertDialog(
								// "活动刷新成功，将活动分享给他人，可以帮你更快的完成人员招聘哦",
								// "刷新成功", "我知道了");
								showFeeRefreshAlertDialog(msg, titile, other,
										cancle, money, String.valueOf(status),
										activity_id);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("company_id", company_id);
				map.put("activity_id", activity_id);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	/**
	 * 刷新付费活动时弹框
	 * 
	 */

	public void showFeeRefreshAlertDialog(String str, final String str2,
			final String str3, final String str4, String money,
			final String flag, final String activity_id) {

		CustomDialogThree.Builder builder = new CustomDialogThree.Builder(
				context);
		builder.setTitle(str2);
		builder.setMessage(str);
		builder.setMoney("(帐号余额:" + money + "元)");
		builder.setPositiveButton(str3, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 2是立即充值,其它是刷新
				if ("2".equals(flag)) {
					Intent intent = new Intent();
					intent.setClass(context, RechargeActivity.class);
					context.startActivity(intent);
				} else {
					refreshJianZhi(flag, activity_id);

				}
			}
		});

		builder.setNegativeButton(str4, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	/**
	 * 刷新活动时弹框
	 * 
	 */

	public void showRefreshAlertDialog(String str, final String str2,
			final String str3, final String str4, final String flag,
			final String activity_id) {

		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setTitle(str2);
		builder.setMessage(str);
		builder.setPositiveButton(str3, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 2是立即充值,其它是刷新
				if ("2".equals(flag)) {
					Intent intent = new Intent();
					intent.setClass(context, RechargeActivity.class);
					context.startActivity(intent);
				} else {
					refreshJianZhi(flag, activity_id);

				}
			}
		});

		builder.setNegativeButton(str4, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	public void showAlertDialog(String str, final String str2, String str3) {

		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setTitle(str2);
		builder.setMessage(str);
		builder.setPositiveButton(str3, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (str2.equals("刷新失败")) {
					Intent intent = new Intent();
					intent.setClass(context, RechargeActivity.class);
					context.startActivity(intent);
				} else if (str2.equals("刷新成功")) {
					// getData();// 刷新界面
				}
			}
		});
		if ("立即充值".equals(str3)) {
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							dialog.dismiss();
						}
					});
		}
		builder.create().show();
	}

	/**
	 * 刷新兼职信息
	 * 
	 */
	private void refreshJianZhi(final String flag, final String activity_id) {
		StringRequest request = new StringRequest(Request.Method.POST,
				refreshUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js
									.getJSONObject("RefleshResponse");
							int status = jss.getInt("status");
							if (status == 1) {
								// 状态1是当前可以免费刷新一次
								// ToastUtil.showShortToast("刷新成功");
								showAlertDialog(
										"您已成功置顶兼职，要记得把活动分享出去哦，让更多的人来报名吧",
										"置顶成功", "我知道了");
							} else if (status == 2) {
								// 状态2是当前免费刷新次数用完,余额不足

							} else if (status == 3) {
								// 状态3表示当前免费刷新次数用完,有可用余额
								ToastUtil.showShortToast("置顶成功");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						ToastUtil.showShortToast("置顶失败");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("company_id", company_id);
				map.put("activity_id", activity_id);
				map.put("isPay", flag);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	/**
	 * 弹出二维码扫描码
	 * 
	 */
	private void initPopWindow(String activity_id) throws WriterException {
		// 加载popupWindow的布局文件
		final View contentView = LayoutInflater.from(context).inflate(
				R.layout.alert_dialog_code, null);
		qrImgImageView = (ImageView) contentView
				.findViewById(R.id.qiandao_code);
		Resources r = context.getResources();
		// 生成二维码
		// res= getResources();activity的方法上下文获取
		mBitmap = BitmapFactory.decodeResource(r, R.drawable.ic_launcher);
		// 缩放图片
		Matrix m = new Matrix();
		float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
		float sy = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getHeight();
		m.setScale(sx, sy);
		// 重新构造一个40*40的图片
		mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
				mBitmap.getHeight(), m, false);

		// mBitmap = EncodingHandler.createQRCode(url, 420);//生成二维码
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		try {
			int width = wm.getDefaultDisplay().getWidth();// 屏幕
			int height = wm.getDefaultDisplay().getHeight();
			mBitmap = cretaeBitmap(new String(activity_id.getBytes(),
					"ISO-8859-1"), width * 3 / 4);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		qrImgImageView.setImageBitmap(mBitmap);

		// 声明一个弹出框
		final PopupWindow popupWindow = new PopupWindow(
				contentView.findViewById(R.id.pop_layout),
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		// 为弹出框设定自定义的布局
		popupWindow.setContentView(contentView);
		//
		// Button button_sure = (Button)
		// contentView.findViewById(R.id.btn_cancel);
		// button_sure.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// popupWindow.dismiss();
		// status = 1;
		// // signStaff();
		// }
		// });

		contentView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				int height = contentView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y > height) {// 把小于号 改为大于号，轻触其他地方都可以退出二维码
						popupWindow.dismiss();
						status = 1;
					}
				}
				return true;
			}
		});

		// 设置SelectPicPopupWindow弹出窗体可点击
		popupWindow.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		popupWindow.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		popupWindow.setBackgroundDrawable(dw);

		popupWindow.showAtLocation(
				LayoutInflater.from(context).inflate(
						R.layout.fragment_home_company, null), Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0);

	}

	/**
	 * 生成二维码
	 * 
	 * @param 字符串
	 * @return Bitmap
	 * @throws WriterException
	 */
	public Bitmap cretaeBitmap(String str, int widthAndHeight)
			throws WriterException {

		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		// BitMatrix matrix= new MultiFormatWriter().encode(str,
		// BarcodeFormat.QR_CODE, 300, 300);
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight, hints);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了,logo位于中间
		int halfW = width / 2;
		int halfH = height / 2;
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
						&& y > halfH - IMAGE_HALFWIDTH
						&& y < halfH + IMAGE_HALFWIDTH) {
					pixels[y * width + x] = mBitmap.getPixel(x - halfW
							+ IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
				} else {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					} else { // 无信息设置像素点为白色
						pixels[y * width + x] = 0xffffffff;
					}
				}

			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

		return bitmap;
	}

}
