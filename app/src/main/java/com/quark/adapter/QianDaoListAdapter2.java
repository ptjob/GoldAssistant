package com.quark.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.model.QianDaoListBean;

public class QianDaoListAdapter2 extends BaseAdapter {

	private Context context;
	private ArrayList<QianDaoListBean> qiandaoList;
	private int all_count;// 发起签到总次数
	private ArrayList<ImageView> imvList = new ArrayList<ImageView>();
	private ArrayList<LinearLayout> layoutList = new ArrayList<LinearLayout>();
	private ArrayList<TextView> tvList = new ArrayList<TextView>();

	public QianDaoListAdapter2(Context context,
			ArrayList<QianDaoListBean> qiandaoList, int all_count) {
		this.context = context;
		this.qiandaoList = qiandaoList;
		this.all_count = all_count;
	}

	@Override
	public int getCount() {
		return qiandaoList == null ? 0 : qiandaoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return qiandaoList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_qiandaolist2, null);
			viewHolder.layout1 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout1);
			viewHolder.layout2 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout2);
			viewHolder.layout3 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout3);
			viewHolder.layout4 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout4);
			viewHolder.layout5 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout5);
			viewHolder.layout6 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout6);
			viewHolder.layout7 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout7);
			viewHolder.layout8 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout8);
			viewHolder.layout9 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout9);
			viewHolder.layout10 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout10);
			viewHolder.layout11 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout11);
			viewHolder.layout12 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout12);
			viewHolder.layout13 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout13);
			viewHolder.layout14 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout14);
			viewHolder.layout15 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout15);
			viewHolder.layout16 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout16);
			viewHolder.layout17 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout17);
			viewHolder.layout18 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout18);
			viewHolder.layout19 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout19);
			viewHolder.layout20 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout20);
			viewHolder.layout21 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout21);
			viewHolder.layout22 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout22);
			viewHolder.layout23 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout23);
			viewHolder.layout24 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout24);
			viewHolder.layout25 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout25);
			viewHolder.layout26 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout26);
			viewHolder.layout27 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout27);
			viewHolder.layout28 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout28);
			viewHolder.layout29 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout29);
			viewHolder.layout30 = (LinearLayout) convertView
					.findViewById(R.id.item_linearlayout30);

			// 初始化图标
			viewHolder.imv1 = (ImageView) convertView
					.findViewById(R.id.item_img1);
			viewHolder.imv2 = (ImageView) convertView
					.findViewById(R.id.item_img2);
			viewHolder.imv3 = (ImageView) convertView
					.findViewById(R.id.item_img3);
			viewHolder.imv4 = (ImageView) convertView
					.findViewById(R.id.item_img4);
			viewHolder.imv5 = (ImageView) convertView
					.findViewById(R.id.item_img5);
			viewHolder.imv6 = (ImageView) convertView
					.findViewById(R.id.item_img6);
			viewHolder.imv7 = (ImageView) convertView
					.findViewById(R.id.item_img7);
			viewHolder.imv8 = (ImageView) convertView
					.findViewById(R.id.item_img8);
			viewHolder.imv9 = (ImageView) convertView
					.findViewById(R.id.item_img9);
			viewHolder.imv10 = (ImageView) convertView
					.findViewById(R.id.item_img10);
			viewHolder.imv11 = (ImageView) convertView
					.findViewById(R.id.item_img11);
			viewHolder.imv12 = (ImageView) convertView
					.findViewById(R.id.item_img12);
			viewHolder.imv13 = (ImageView) convertView
					.findViewById(R.id.item_img13);
			viewHolder.imv14 = (ImageView) convertView
					.findViewById(R.id.item_img14);
			viewHolder.imv15 = (ImageView) convertView
					.findViewById(R.id.item_img15);
			viewHolder.imv16 = (ImageView) convertView
					.findViewById(R.id.item_img16);
			viewHolder.imv17 = (ImageView) convertView
					.findViewById(R.id.item_img17);
			viewHolder.imv18 = (ImageView) convertView
					.findViewById(R.id.item_img18);
			viewHolder.imv19 = (ImageView) convertView
					.findViewById(R.id.item_img19);
			viewHolder.imv20 = (ImageView) convertView
					.findViewById(R.id.item_img20);
			viewHolder.imv21 = (ImageView) convertView
					.findViewById(R.id.item_img21);
			viewHolder.imv22 = (ImageView) convertView
					.findViewById(R.id.item_img22);
			viewHolder.imv23 = (ImageView) convertView
					.findViewById(R.id.item_img23);
			viewHolder.imv24 = (ImageView) convertView
					.findViewById(R.id.item_img24);
			viewHolder.imv25 = (ImageView) convertView
					.findViewById(R.id.item_img25);
			viewHolder.imv26 = (ImageView) convertView
					.findViewById(R.id.item_img26);
			viewHolder.imv27 = (ImageView) convertView
					.findViewById(R.id.item_img27);
			viewHolder.imv28 = (ImageView) convertView
					.findViewById(R.id.item_img28);
			viewHolder.imv29 = (ImageView) convertView
					.findViewById(R.id.item_img29);
			viewHolder.imv30 = (ImageView) convertView
					.findViewById(R.id.item_img30);

			// 初始化tv
			viewHolder.tv1 = (TextView) convertView.findViewById(R.id.item_tv1);
			viewHolder.tv2 = (TextView) convertView.findViewById(R.id.item_tv2);
			viewHolder.tv3 = (TextView) convertView.findViewById(R.id.item_tv3);
			viewHolder.tv4 = (TextView) convertView.findViewById(R.id.item_tv4);
			viewHolder.tv5 = (TextView) convertView.findViewById(R.id.item_tv5);
			viewHolder.tv6 = (TextView) convertView.findViewById(R.id.item_tv6);
			viewHolder.tv7 = (TextView) convertView.findViewById(R.id.item_tv7);
			viewHolder.tv8 = (TextView) convertView.findViewById(R.id.item_tv8);
			viewHolder.tv9 = (TextView) convertView.findViewById(R.id.item_tv9);
			viewHolder.tv10 = (TextView) convertView
					.findViewById(R.id.item_tv10);
			viewHolder.tv11 = (TextView) convertView
					.findViewById(R.id.item_tv11);
			viewHolder.tv12 = (TextView) convertView
					.findViewById(R.id.item_tv12);
			viewHolder.tv13 = (TextView) convertView
					.findViewById(R.id.item_tv13);
			viewHolder.tv14 = (TextView) convertView
					.findViewById(R.id.item_tv14);
			viewHolder.tv15 = (TextView) convertView
					.findViewById(R.id.item_tv15);
			viewHolder.tv16 = (TextView) convertView
					.findViewById(R.id.item_tv16);
			viewHolder.tv17 = (TextView) convertView
					.findViewById(R.id.item_tv17);
			viewHolder.tv18 = (TextView) convertView
					.findViewById(R.id.item_tv18);
			viewHolder.tv19 = (TextView) convertView
					.findViewById(R.id.item_tv19);
			viewHolder.tv20 = (TextView) convertView
					.findViewById(R.id.item_tv20);
			viewHolder.tv21 = (TextView) convertView
					.findViewById(R.id.item_tv21);
			viewHolder.tv22 = (TextView) convertView
					.findViewById(R.id.item_tv22);
			viewHolder.tv23 = (TextView) convertView
					.findViewById(R.id.item_tv23);
			viewHolder.tv24 = (TextView) convertView
					.findViewById(R.id.item_tv24);
			viewHolder.tv25 = (TextView) convertView
					.findViewById(R.id.item_tv25);
			viewHolder.tv26 = (TextView) convertView
					.findViewById(R.id.item_tv26);
			viewHolder.tv27 = (TextView) convertView
					.findViewById(R.id.item_tv27);
			viewHolder.tv28 = (TextView) convertView
					.findViewById(R.id.item_tv28);
			viewHolder.tv29 = (TextView) convertView
					.findViewById(R.id.item_tv29);
			viewHolder.tv30 = (TextView) convertView
					.findViewById(R.id.item_tv30);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (arg0 == 0) {
			// 表头
			if (all_count < 8) {
				// 至少8列
				viewHolder.layout1.setVisibility(View.VISIBLE);
				viewHolder.layout2.setVisibility(View.VISIBLE);
				viewHolder.layout3.setVisibility(View.VISIBLE);
				viewHolder.layout4.setVisibility(View.VISIBLE);
				viewHolder.layout5.setVisibility(View.VISIBLE);
				viewHolder.layout6.setVisibility(View.VISIBLE);
				viewHolder.layout7.setVisibility(View.VISIBLE);
				viewHolder.layout8.setVisibility(View.VISIBLE);
				viewHolder.layout9.setVisibility(View.GONE);
				viewHolder.layout10.setVisibility(View.GONE);
				viewHolder.layout11.setVisibility(View.GONE);
				viewHolder.layout12.setVisibility(View.GONE);
				viewHolder.layout13.setVisibility(View.GONE);
				viewHolder.layout14.setVisibility(View.GONE);
				viewHolder.layout15.setVisibility(View.GONE);
				viewHolder.layout16.setVisibility(View.GONE);
				viewHolder.layout17.setVisibility(View.GONE);
				viewHolder.layout18.setVisibility(View.GONE);
				viewHolder.layout19.setVisibility(View.GONE);
				viewHolder.layout20.setVisibility(View.GONE);
				viewHolder.layout21.setVisibility(View.GONE);
				viewHolder.layout22.setVisibility(View.GONE);
				viewHolder.layout23.setVisibility(View.GONE);
				viewHolder.layout24.setVisibility(View.GONE);
				viewHolder.layout25.setVisibility(View.GONE);
				viewHolder.layout26.setVisibility(View.GONE);
				viewHolder.layout27.setVisibility(View.GONE);
				viewHolder.layout28.setVisibility(View.GONE);
				viewHolder.layout29.setVisibility(View.GONE);
				viewHolder.layout30.setVisibility(View.GONE);

				viewHolder.imv1.setVisibility(View.GONE);
				viewHolder.imv2.setVisibility(View.GONE);
				viewHolder.imv3.setVisibility(View.GONE);
				viewHolder.imv4.setVisibility(View.GONE);
				viewHolder.imv5.setVisibility(View.GONE);
				viewHolder.imv6.setVisibility(View.GONE);
				viewHolder.imv7.setVisibility(View.GONE);
				viewHolder.imv8.setVisibility(View.GONE);

				viewHolder.tv1.setVisibility(View.VISIBLE);
				viewHolder.tv2.setVisibility(View.VISIBLE);
				viewHolder.tv3.setVisibility(View.VISIBLE);
				viewHolder.tv4.setVisibility(View.VISIBLE);
				viewHolder.tv5.setVisibility(View.VISIBLE);
				viewHolder.tv6.setVisibility(View.VISIBLE);
				viewHolder.tv7.setVisibility(View.VISIBLE);
				viewHolder.tv8.setVisibility(View.VISIBLE);
			} else {
				// 发起签到次数大于8列 显示all_count 列数
				imvList.clear();
				imvList.add(viewHolder.imv1);
				imvList.add(viewHolder.imv2);
				imvList.add(viewHolder.imv3);
				imvList.add(viewHolder.imv4);
				imvList.add(viewHolder.imv5);
				imvList.add(viewHolder.imv6);
				imvList.add(viewHolder.imv7);
				imvList.add(viewHolder.imv8);
				imvList.add(viewHolder.imv9);
				imvList.add(viewHolder.imv10);
				imvList.add(viewHolder.imv11);
				imvList.add(viewHolder.imv12);
				imvList.add(viewHolder.imv13);
				imvList.add(viewHolder.imv14);
				imvList.add(viewHolder.imv15);
				imvList.add(viewHolder.imv16);
				imvList.add(viewHolder.imv17);
				imvList.add(viewHolder.imv18);
				imvList.add(viewHolder.imv19);
				imvList.add(viewHolder.imv20);
				imvList.add(viewHolder.imv21);
				imvList.add(viewHolder.imv22);
				imvList.add(viewHolder.imv23);
				imvList.add(viewHolder.imv24);
				imvList.add(viewHolder.imv25);
				imvList.add(viewHolder.imv26);
				imvList.add(viewHolder.imv27);
				imvList.add(viewHolder.imv28);
				imvList.add(viewHolder.imv29);
				imvList.add(viewHolder.imv30);

				layoutList.clear();
				layoutList.add(viewHolder.layout1);
				layoutList.add(viewHolder.layout2);
				layoutList.add(viewHolder.layout3);
				layoutList.add(viewHolder.layout4);
				layoutList.add(viewHolder.layout5);
				layoutList.add(viewHolder.layout6);
				layoutList.add(viewHolder.layout7);
				layoutList.add(viewHolder.layout8);
				layoutList.add(viewHolder.layout9);
				layoutList.add(viewHolder.layout10);
				layoutList.add(viewHolder.layout11);
				layoutList.add(viewHolder.layout12);
				layoutList.add(viewHolder.layout13);
				layoutList.add(viewHolder.layout14);
				layoutList.add(viewHolder.layout15);
				layoutList.add(viewHolder.layout16);
				layoutList.add(viewHolder.layout17);
				layoutList.add(viewHolder.layout18);
				layoutList.add(viewHolder.layout19);
				layoutList.add(viewHolder.layout20);
				layoutList.add(viewHolder.layout21);
				layoutList.add(viewHolder.layout22);
				layoutList.add(viewHolder.layout23);
				layoutList.add(viewHolder.layout24);
				layoutList.add(viewHolder.layout25);
				layoutList.add(viewHolder.layout26);
				layoutList.add(viewHolder.layout27);
				layoutList.add(viewHolder.layout28);
				layoutList.add(viewHolder.layout29);
				layoutList.add(viewHolder.layout30);

				tvList.clear();
				tvList.add(viewHolder.tv1);
				tvList.add(viewHolder.tv2);
				tvList.add(viewHolder.tv3);
				tvList.add(viewHolder.tv4);
				tvList.add(viewHolder.tv5);
				tvList.add(viewHolder.tv6);
				tvList.add(viewHolder.tv7);
				tvList.add(viewHolder.tv8);
				tvList.add(viewHolder.tv9);
				tvList.add(viewHolder.tv10);
				tvList.add(viewHolder.tv11);
				tvList.add(viewHolder.tv12);
				tvList.add(viewHolder.tv13);
				tvList.add(viewHolder.tv14);
				tvList.add(viewHolder.tv15);
				tvList.add(viewHolder.tv16);
				tvList.add(viewHolder.tv17);
				tvList.add(viewHolder.tv18);
				tvList.add(viewHolder.tv19);
				tvList.add(viewHolder.tv20);
				tvList.add(viewHolder.tv21);
				tvList.add(viewHolder.tv22);
				tvList.add(viewHolder.tv23);
				tvList.add(viewHolder.tv24);
				tvList.add(viewHolder.tv25);
				tvList.add(viewHolder.tv26);
				tvList.add(viewHolder.tv27);
				tvList.add(viewHolder.tv28);
				tvList.add(viewHolder.tv29);
				tvList.add(viewHolder.tv30);
				// 显示all_count 列数
				for (int i = 0; i < all_count; i++) {
					if (i == 30) {
						break;
					}
					layoutList.get(i).setVisibility(View.VISIBLE);
					tvList.get(i).setVisibility(View.VISIBLE);
					imvList.get(i).setVisibility(View.GONE);
				}
				// 隐藏all_count到30的列数
				for (int i = 29; i >= all_count; i--) {
					layoutList.get(i).setVisibility(View.GONE);
				}

			}

		} else {
			// 真实的签到记录显示
			if (all_count < 8) {
				imvList.clear();
				imvList.add(viewHolder.imv1);
				imvList.add(viewHolder.imv2);
				imvList.add(viewHolder.imv3);
				imvList.add(viewHolder.imv4);
				imvList.add(viewHolder.imv5);
				imvList.add(viewHolder.imv6);
				imvList.add(viewHolder.imv7);
				imvList.add(viewHolder.imv8);
				JSONObject qiandaoJsonObject = qiandaoList.get(arg0)
						.getSignMap();

				for (int i = 0; i < 8; i++) {
					if (i < all_count) {
						// 在发起签到次数以内
						if (qiandaoJsonObject != null) {
							try {
								if (qiandaoJsonObject
										.get(String.valueOf(i + 1)) != null) {
									imvList.get(i).setImageResource(
											R.drawable.vertify_yes);
								} else {
									imvList.get(i).setImageResource(
											R.drawable.vertify_no);
								}
							} catch (JSONException e) {
								e.printStackTrace();
								imvList.get(i).setImageResource(
										R.drawable.vertify_no);
							}

						} else {
							imvList.get(i).setImageResource(
									R.drawable.vertify_no);
						}

					} else {
						imvList.get(i).setVisibility(View.INVISIBLE);
					}
				}

			} else {
				// all_count >8的情况
				imvList.clear();
				imvList.add(viewHolder.imv1);
				imvList.add(viewHolder.imv2);
				imvList.add(viewHolder.imv3);
				imvList.add(viewHolder.imv4);
				imvList.add(viewHolder.imv5);
				imvList.add(viewHolder.imv6);
				imvList.add(viewHolder.imv7);
				imvList.add(viewHolder.imv8);
				imvList.add(viewHolder.imv9);
				imvList.add(viewHolder.imv10);
				imvList.add(viewHolder.imv11);
				imvList.add(viewHolder.imv12);
				imvList.add(viewHolder.imv13);
				imvList.add(viewHolder.imv14);
				imvList.add(viewHolder.imv15);
				imvList.add(viewHolder.imv16);
				imvList.add(viewHolder.imv17);
				imvList.add(viewHolder.imv18);
				imvList.add(viewHolder.imv19);
				imvList.add(viewHolder.imv20);
				imvList.add(viewHolder.imv21);
				imvList.add(viewHolder.imv22);
				imvList.add(viewHolder.imv23);
				imvList.add(viewHolder.imv24);
				imvList.add(viewHolder.imv25);
				imvList.add(viewHolder.imv26);
				imvList.add(viewHolder.imv27);
				imvList.add(viewHolder.imv28);
				imvList.add(viewHolder.imv29);
				imvList.add(viewHolder.imv30);

				JSONObject qiandaoJsonObject = qiandaoList.get(arg0)
						.getSignMap();
				for (int i = 0; i < all_count; i++) {
					if (i == 30) {
						break;
					}
					if (qiandaoJsonObject != null) {
						try {
							if (qiandaoJsonObject.get(String.valueOf(i + 1)) != null) {
								imvList.get(i).setImageResource(
										R.drawable.vertify_yes);
							} else {
								imvList.get(i).setImageResource(
										R.drawable.vertify_no);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							imvList.get(i).setImageResource(
									R.drawable.vertify_no);
						}

					} else {
						imvList.get(i).setImageResource(R.drawable.vertify_no);
					}

				}

			}

		}

		return convertView;
	}

	class ViewHolder {
		LinearLayout layout1, layout2, layout3, layout4, layout5, layout6,
				layout7, layout8, layout9, layout10, layout11, layout12,
				layout13, layout14, layout15, layout16, layout17, layout18,
				layout19, layout20, layout21, layout22, layout23, layout24,
				layout25, layout26, layout27, layout28, layout29, layout30;
		ImageView imv1, imv2, imv3, imv4, imv5, imv6, imv7, imv8, imv9, imv10,
				imv11, imv12, imv13, imv14, imv15, imv16, imv17, imv18, imv19,
				imv20, imv21, imv22, imv23, imv24, imv25, imv26, imv27, imv28,
				imv29, imv30;
		TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12,
				tv13, tv14, tv15, tv16, tv17, tv18, tv19, tv20, tv21, tv22,
				tv23, tv24, tv25, tv26, tv27, tv28, tv29, tv30;
	}

}
