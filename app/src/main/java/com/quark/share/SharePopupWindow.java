package com.quark.share;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

import com.parttime.addresslist.GroupsActivity;
import com.qingmu.jianzhidaren.R;
import com.quark.common.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * TODO<分享工具>
 * 
 * @data: 2014-7-21 下午2:45:38
 * @version: V1.0
 */

public class SharePopupWindow extends PopupWindow {

	private Context context;
	private PlatformActionListener platformActionListener;
	private ShareParams shareParams;
	private int type = 0;// 0:活动分享，1：兼职成就分享
	private String activityId, activityTitle, job_place, startTime;
	private int pay, pay_type, leftcount;
	private boolean shareFlag;// 是否可以分享 2、4的时候表示审核通过可以分享

	public SharePopupWindow(Context cx, boolean shareFlag) {
		this.context = cx;
		this.shareFlag = shareFlag;
	}

	public PlatformActionListener getPlatformActionListener() {
		return platformActionListener;
	}

	public void setPlatformActionListener(
			PlatformActionListener platformActionListener) {
		this.platformActionListener = platformActionListener;
	}

	TextView title;

	public void showShareWindow() {
		View view = LayoutInflater.from(context).inflate(R.layout.share_layout,
				null);
		SharedPreferences sp = context.getSharedPreferences("jrdr.setting",
				Context.MODE_PRIVATE);
			RelativeLayout topLayout = (RelativeLayout) view
					.findViewById(R.id.top_title_layout);
			topLayout.setBackgroundColor(context.getResources().getColor(
					R.color.guanli_common_color));
		title = (TextView) view.findViewById(R.id.title);
		GridView gridView = (GridView) view.findViewById(R.id.share_gridview);
		ShareAdapter adapter = new ShareAdapter(context);
		gridView.setAdapter(adapter);
		// 弹窗背景颜色
		gridView.setBackgroundColor(Color.WHITE);

		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		// 取消按钮
		btn_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 销毁弹出框
				dismiss();
			}
		});

		// 设置SelectPicPopupWindow的View
		this.setContentView(view);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);

		gridView.setOnItemClickListener(new ShareItemClickListener(this));
	}

	public void setTitle(String titlestr) {
		title.setText(titlestr);
	}

	private class ShareItemClickListener implements OnItemClickListener {
		private PopupWindow pop;

		public ShareItemClickListener(PopupWindow pop) {
			this.pop = pop;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			share(position);
			pop.dismiss();

		}
	}

	/**
	 * 分享
	 * 
	 * @param position
	 */
	private void share(int position) {
		if (position == 0) {
			MobclickAgent.onEvent(context, "onclick4", "微信分享");
			weixin();
		} else if (position == 1) {
			MobclickAgent.onEvent(context, "onclick5", "qq分享");
			qq();
		} else if (position == 2) {
			MobclickAgent.onEvent(context, "onclick6", "微博分享");
			weibo();
		} else if (position == 3) {
			MobclickAgent.onEvent(context, "onclick7", "朋友圈分享");
			WechatMoments();
		} else if (position == 4) {
			MobclickAgent.onEvent(context, "onclick8", "qq空间分享");
			qzone();
		} else if (position == 5) {
			// shortMessage();
			MobclickAgent.onEvent(context, "onclick9", "群组分享");
			if (shareFlag) {
				shareToGroup();
			} else {
				ToastUtil.showShortToast("活动审核未通过,请稍候。。。");
			}
		} else {
			Platform plat = null;
			plat = ShareSDK.getPlatform(context, getPlatform(position));
			if (platformActionListener != null) {
				plat.setPlatformActionListener(platformActionListener);
			}

			plat.share(shareParams);
		}
	}

	/**
	 * 传递活动详细信息到share
	 * 
	 */
	public void shareDataFromActivity(String activityId, String activityTitle,
			int pay, int pay_type, String job_place, String startTime,
			int leftcount) {
		this.activityId = activityId;
		this.activityTitle = activityTitle;
		this.pay = pay;
		this.pay_type = pay_type;
		this.job_place = job_place;
		this.startTime = startTime;
		this.leftcount = leftcount;
	}

	/**
	 * 初始化分享参数
	 * 
	 * @param shareModel
	 */
	public void initShareParams(ShareModel shareModel, int i) {
		type = i;
		if (shareModel != null) {
			ShareParams sp = new ShareParams();
			sp.setShareType(Platform.SHARE_TEXT);
			sp.setShareType(Platform.SHARE_WEBPAGE);

			sp.setTitle(shareModel.getTitle());
			sp.setText(shareModel.getText());
			sp.setUrl(shareModel.getUrl());
			sp.setImageUrl(shareModel.getImageUrl());
			shareParams = sp;
		}
	}

	/**
	 * 获取平台
	 * 
	 * @param position
	 * @return
	 */
	private String getPlatform(int position) {
		String platform = "";
		switch (position) {
		case 0:
			platform = "Wechat";
			break;
		case 1:
			platform = "QQ";
			break;
		case 2:
			platform = "SinaWeibo";
			break;
		case 3:
			platform = "WechatMoments";
			break;
		case 4:
			platform = "QZone";
			break;
		case 5:
			platform = "ShortMessage";
			break;
		}
		return platform;
	}

	private void WechatMoments() {
		ShareParams sp = new ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);
		if (type == 0) {
			String[] sourceStrArray = shareParams.getTitle().split(";");
			sp.setTitle("我最近报名了" + sourceStrArray[0] + "兼职，"
					+ sourceStrArray[1] + ",一起呗！" + shareParams.getUrl());
			sp.setText("我最近报名了" + sourceStrArray[0] + "兼职，" + sourceStrArray[1]
					+ ",一起呗！" + shareParams.getUrl());
		} else {
			sp.setTitle(shareParams.getTitle());
			sp.setText("每一份收入，都是对自己最好的证明。");
		}
		sp.setUrl(shareParams.getUrl());
		sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
		sp.setImageUrl(shareParams.getImageUrl());
		sp.setComment("我对此分享内容的评论");
		sp.setSite("兼职达人");
		sp.setSiteUrl(shareParams.getUrl());
		Platform weibo = ShareSDK.getPlatform(context, "WechatMoments");
		if (weibo != null) {
			weibo.setPlatformActionListener(platformActionListener); // 设置分享事件回调
																		// //
																		// 执行图文分享
			weibo.share(sp);
		}
	}

	/**
	 * 分享到微信
	 */
	private void weixin() {
		ShareParams sp = new ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);
		sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
		if (type == 0) {
			String[] titleArray = shareParams.getTitle().split(";");
			sp.setTitle(titleArray[0] + "  " + titleArray[1]);
			String[] sourceStrArray = shareParams.getText().split(";");
			sp.setText(sourceStrArray[0] + "\n" + sourceStrArray[1]);
		} else {
			sp.setTitle(shareParams.getTitle());
			sp.setText("每一份收入，都是对自己最好的证明。");
		}

		sp.setImageUrl(shareParams.getImageUrl());
		sp.setUrl(shareParams.getUrl());
		sp.setComment("我对此分享内容的评论");
		sp.setSite("兼职达人");
		sp.setSiteUrl(shareParams.getUrl());
		Platform weixin = ShareSDK.getPlatform(context, "Wechat");

		if (weixin != null) {
			weixin.setPlatformActionListener(platformActionListener); // 设置分享事件回调
																		// //
																		// 执行图文分享
			weixin.share(sp);
		}
	}

	/**
	 * 分享到微博
	 */
	private void weibo() {
		ShareParams sp = new ShareParams();
		if (type == 0) {
			String[] sourceStrArray = shareParams.getTitle().split(";");
			sp.setText("我最近报名了" + sourceStrArray[0] + "兼职，" + sourceStrArray[1]
					+ ",一起呗！" + shareParams.getUrl());
		} else {
			sp.setTitle(shareParams.getTitle());
			sp.setText("每一份收入，都是对自己最好的证明。");
		}

		sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
		sp.setImageUrl(shareParams.getImageUrl());
		sp.setComment("我对此分享内容的评论");
		sp.setSite("兼职达人");
		sp.setSiteUrl(shareParams.getUrl());
		Platform weibo = ShareSDK.getPlatform(context, SinaWeibo.NAME);
		if (weibo != null) {
			weibo.setPlatformActionListener(platformActionListener); // 设置分享事件回调
																		// //
																		// 执行图文分享
			weibo.share(sp);
		}
	}

	/**
	 * 分享到QQ空间
	 */
	private void qzone() {
		ShareParams sp = new ShareParams();
		sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
		if (type == 0) {
			String[] titleArray = shareParams.getTitle().split(";");
			sp.setTitle(titleArray[0] + " | " + titleArray[1]);
			String[] sourceStrArray = shareParams.getText().split(";");
			sp.setText(sourceStrArray[0] + "\n" + sourceStrArray[1]);
		} else {
			sp.setTitle(shareParams.getTitle());
			sp.setText("每一份收入，都是对自己最好的证明。");
		}
		sp.setImageUrl(shareParams.getImageUrl());
		sp.setComment("我对此分享内容的评论");
		sp.setSite("兼职达人");
		sp.setSiteUrl(shareParams.getUrl());

		Platform qq = ShareSDK.getPlatform(context, "QZone");
		if (qq != null) {
			qq.setPlatformActionListener(platformActionListener);
			qq.share(sp);
		}
	}

	private void qq() {
		ShareParams sp = new ShareParams();
		sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
		if (type == 0) {
			String[] titleArray = shareParams.getTitle().split(";");
			sp.setTitle(titleArray[0] + " | " + titleArray[1]);
			String[] sourceStrArray = shareParams.getText().split(";");
			sp.setText(sourceStrArray[0] + "\n" + sourceStrArray[1]);
		} else {
			sp.setTitle(shareParams.getTitle());
			sp.setText("每一份收入，都是对自己最好的证明。");
		}
		sp.setImageUrl(shareParams.getImageUrl());
		// sp.setImageData(BitmapFactory.decodeResource(context.getResources(),R.drawable.pic_default_user));
		// //只对微信有用的 其他的平台 用不了的
		sp.setComment("我对此分享内容的评论");
		sp.setSite("兼职达人");
		sp.setSiteUrl(shareParams.getUrl());

		Platform qq = ShareSDK.getPlatform(context, "QQ");
		if (qq != null) {
			qq.setPlatformActionListener(platformActionListener);
			qq.share(sp);
		}
	}

	/**
	 * 分享活动到我的群
	 * 
	 */
	private void shareToGroup() {
		// 进入群聊列表页面
		Intent intent = new Intent(context, GroupsActivity.class);
		intent.putExtra("isFromShare", true);
		intent.putExtra("activityId", activityId);
		intent.putExtra("title", activityTitle);
		intent.putExtra("pay", pay);
		intent.putExtra("pay_type", pay_type);
		intent.putExtra("job_place", job_place);
		intent.putExtra("start_time", startTime);
		intent.putExtra("left_count", leftcount);
		context.startActivity(intent);

	}

	/**
	 * 分享到短信
	 */
	private void shortMessage() {
		ShareParams sp = new ShareParams();
		sp.setAddress("");
		sp.setText("我最近报名了" + shareParams.getTitle() + "的兼职，一起呗！"
				+ shareParams.getUrl());

		Platform circle = ShareSDK.getPlatform(context, "ShortMessage");
		circle.setPlatformActionListener(platformActionListener); // 设置分享事件回调
		// 执行图文分享
		circle.share(sp);
	}

}
