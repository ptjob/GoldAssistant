package com.quark.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.carson.loadpic.SwipeAdapter;
import com.carson.loadpic.SwipeLayout;
import com.qingmu.jianzhidaren.R;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.jianzhidaren.MainCompanyActivity;
import com.quark.model.GuangchangModle;
import com.quark.volley.VolleySington;

public class MyJianZhiCollectedAdapter extends SwipeAdapter {

	private ViewHolder holder;
	private List<GuangchangModle> list;
	private Context context;
	private String cancelCollectUrl;
	private RequestQueue queue;
	private SharedPreferences sp;
	private String user_id;
	private boolean isFromShareFlag;
	private SwipeLayout swipeLayout;

	public MyJianZhiCollectedAdapter(Context context,
			List<GuangchangModle> list, boolean isFromShareFlag) {
		this.list = list;
		this.context = context;
		this.isFromShareFlag = isFromShareFlag;
		cancelCollectUrl = Url.USER_CANCEL_COLLECT + "?token="
				+ MainCompanyActivity.token;
		sp = context.getSharedPreferences("jrdr.setting", Context.MODE_PRIVATE);
		user_id = sp.getString("userId", "");
	}

	private static class ViewHolder {
		TextView type;
		TextView title;
		TextView date;
		TextView addreess;
		TextView dates;
		TextView freeNumber;
		TextView salary;
		ImageView baozhengjinImv;
		ImageView chaojiImv;
		RelativeLayout deleteLayout, starLayout;// 左滑删除收藏,分享
	}

	/**
	 * 取消收藏
	 */
	private void cancelCollectJianzhi(final String activity_id) {
		queue = VolleySington.getInstance().getRequestQueue();
		StringRequest request = new StringRequest(Request.Method.POST,
				cancelCollectUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js.getJSONObject("ResponseStatus");
							String status = jss.getString("status");
							if ("1".equals(status)) {
								// collectedImv
								// .setImageResource(R.drawable.activity_un_collected);
								// ToastUtil.showShortToast("取消收藏成功");
								// jianzhi.setCollected(0);
							} else {
								// ToastUtil.showShortToast("网络不好,取消收藏失败");
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
				map.put("user_id", user_id);
				map.put("activity_id", activity_id);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	@Override
	public int getSwipeLayoutResourceId(int position) {
		return R.id.swipeLayout;
	}

	@Override
	public View generateView(int position, ViewGroup parent) {
		View v = LayoutInflater.from(context).inflate(R.layout.item_collected,
				null);
		swipeLayout = (SwipeLayout) v
				.findViewById(getSwipeLayoutResourceId(position));
		swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {

			@Override
			public void onUpdate(SwipeLayout layout, int leftOffset,
					int topOffset) {

			}

			@Override
			public void onOpen(SwipeLayout layout) {
				// YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
			}

			@Override
			public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

			}

			@Override
			public void onClose(SwipeLayout layout) {

			}
		});
		return v;
	}

	@Override
	public void fillValues(final int i, View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.type = (TextView) convertView.findViewById(R.id.type);
		holder.title = (TextView) convertView.findViewById(R.id.title);
		holder.date = (TextView) convertView.findViewById(R.id.date);
		holder.addreess = (TextView) convertView.findViewById(R.id.addreess);
		holder.dates = (TextView) convertView.findViewById(R.id.dates);
		holder.freeNumber = (TextView) convertView
				.findViewById(R.id.freeNumber);
		holder.salary = (TextView) convertView.findViewById(R.id.salary);
		holder.baozhengjinImv = (ImageView) convertView
				.findViewById(R.id.baozhengjin_icon_img);
		holder.chaojiImv = (ImageView) convertView
				.findViewById(R.id.chaoji_icon_img);
		holder.deleteLayout = (RelativeLayout) convertView
				.findViewById(R.id.delete_button);
		holder.starLayout = (RelativeLayout) convertView
				.findViewById(R.id.star_button);

		// 如果是分享过来的则分享显示,删除隐藏
		if (isFromShareFlag) {
			holder.starLayout.setVisibility(View.GONE);
			holder.deleteLayout.setVisibility(View.GONE);
		} else {
			holder.starLayout.setVisibility(View.GONE);
			holder.deleteLayout.setVisibility(View.VISIBLE);
		}

		// 分享兼职
		holder.starLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 回调接口到chatactivity
				Intent intent = new Intent(); // Itent就是我们要发送的内容
				intent.setAction("com.carson.share.jianzhi"); // 设置你这个广播的action
				intent.putExtra("activity_id", list.get(i).getActivity_id()
						+ "");
				intent.putExtra("title", list.get(i).getTitle());
				intent.putExtra("pay", list.get(i).getPay());
				intent.putExtra("pay_type", list.get(i).getPay_type());
				intent.putExtra("job_place", list.get(i).getCounty());
				intent.putExtra("start_time", list.get(i).getStart_time());
				intent.putExtra("left_count", list.get(i).getLeft_count());
				context.sendBroadcast(intent); // 发送广播
				ToastUtil.showShortToast("活动分享成功^_^");
				((Activity) context).finish();// 销毁收藏界面

			}
		});
		holder.deleteLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 删除收藏列表
				cancelCollectJianzhi(list.get(i).getActivity_id() + "");
				list.remove(i);
				notifyDataSetChanged();

			}
		});

		String typestr = list.get(i).getType();

		holder.type.setText(typestr);
		if (typestr.equals("派发")) {
			holder.type.setBackgroundResource(R.color.type_paifa);
		}
		if (typestr.equals("促销")) {
			holder.type.setBackgroundResource(R.color.type_chuxiao);
		}
		if (typestr.equals("其他")) {
			holder.type.setBackgroundResource(R.color.type_qita);
		}
		if (typestr.equals("家教")) {
			holder.type.setBackgroundResource(R.color.type_jiajiao);
		}
		if (typestr.equals("服务员")) {
			holder.type.setBackgroundResource(R.color.type_fuwuyuan);
			holder.type.setText("服务");
		}
		if (typestr.equals("礼仪")) {
			holder.type.setBackgroundResource(R.color.type_liyi);
		}
		if (typestr.equals("安保人员")) {
			holder.type.setText("安保");
			holder.type.setBackgroundResource(R.color.type_baoanrenyuan);
		}
		if (typestr.equals("模特")) {
			holder.type.setBackgroundResource(R.color.type_mote);

		}
		if (typestr.equals("主持")) {
			holder.type.setBackgroundResource(R.color.type_zhuchi);
		}
		if (typestr.equals("翻译")) {
			holder.type.setBackgroundResource(R.color.type_fanyi);
		}
		if (typestr.equals("工作人员")) {
			holder.type.setText("工作");
			holder.type.setBackgroundResource(R.color.type_gongzuorenyuan);
		}
		if (typestr.equals("话务")) {
			holder.type.setBackgroundResource(R.color.type_huawu);
		}
		if (typestr.equals("充场")) {
			holder.type.setBackgroundResource(R.color.type_chongchang);
		}
		if (typestr.equals("演艺")) {
			holder.type.setBackgroundResource(R.color.type_yanyi);
		}
		if (typestr.equals("访谈")) {
			holder.type.setBackgroundResource(R.color.type_fangtan);
		}

		holder.title.setText(list.get(i).getTitle());
		String datestr = list.get(i).getStart_time();
		if (datestr.length() > 5) {
			datestr = datestr.substring(5, datestr.length());
		}
		holder.date.setText(datestr);
		holder.addreess.setText(list.get(i).getCounty());
		holder.dates.setText(datestr + "(" + list.get(i).getDays() + "天)");
		holder.freeNumber.setText(list.get(i).getLeft_count() + "人");
		if (list.get(i).getPay_type() == 1) {
			holder.salary.setText(list.get(i).getPay() + "元/时");
		} else {
			holder.salary.setText(list.get(i).getPay() + "元/天");
		}
		if (list.get(i).getApply() == 2) {

		} else if (list.get(i).getApply() == 1) {

		} else {

		}
		// 是否显示保证金，超级标签 0表示没有,1表示有
		if (list.get(i).getGuarantee() == 0) {
			holder.baozhengjinImv.setVisibility(View.GONE);
		} else {
			holder.baozhengjinImv.setVisibility(View.VISIBLE);
		}
		if (list.get(i).getSuperJob() == 0) {
			holder.chaojiImv.setVisibility(View.GONE);
		} else {
			holder.chaojiImv.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public int getCount() {
		return list.size() == 0 ? 0 : list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return swipeLayout;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
