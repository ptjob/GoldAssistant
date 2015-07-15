package com.quark.jianzhidaren;

import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.TextView;

import com.android.volley.VolleyLog;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.easemob.EMCallBack;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.domain.User;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.qingmu.jianzhidaren.R;
import com.quark.model.LoginResponse;
import com.quark.utils.ConfigDataUtil;
import com.quark.utils.Logger;
import com.quark.volley.VolleySington;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 10/30 0030.
 */
public class ApplicationControl extends Application {

	public LocationClient mLocationClient;
	public static ApplicationControl instance;

	public static Context applicationContext;
	// login user name
	public final String PREF_USERNAME = "username";

	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

	// 用户登入后不为null
	public LoginResponse loginResponse;// 全局用户对象

	public boolean isLogin() {
		if (null == loginResponse) {
			return false;
		}
		return true;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		MobclickAgent.updateOnlineConfig(this);// 友盟数据统计
		applicationContext = this;
		instance = this;
		ConfigDataUtil.getInstance().init(this);
		VolleySington.getInstance().init(this);
		Logger.initProperties(this);
		VolleyLog.DEBUG = false;// get请求会打印用户的密码
		// SDKInitializer.initialize(getApplicationContext());

		// 图片缓存处理，ImageLoader初始化
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.empty_photo)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.empty_photo)
				// 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)
				// 设置下载的图片是否缓存在SD卡中
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				// .showStubImage(R.drawable.empty_photo) // 设置图片下载期间显示的图片
				.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.memoryCache(new WeakMemoryCache())
				// // carson add
				// .memoryCacheSize(2 * 1024 * 1024)
				// carson add 缓存到内存最大数据
				.discCacheSize(50 * 1024 * 1024)
				// 缓存到文件最大数据
				.discCacheFileCount(100)
				// 缓存一百张图片
				.writeDebugLogs().defaultDisplayImageOptions(defaultOptions)
				.build();
		ImageLoader.getInstance().init(config);
		hxSDKHelper.onInit(applicationContext);
	}

	public static ApplicationControl getInstance() {
		return instance;
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		if (ConfigDataUtil.getInstance() != null)
			ConfigDataUtil.getInstance().clearCache(this);
	}

	public interface ReceiveLocation {
		void onReceiveLocation(BDLocation location);
	}

	// 环信需要

	/**
	 * 获取内存中好友user list
	 * 
	 * @return Map<String, User>
	 */
	public Map<String, User> getContactList() {
		return hxSDKHelper.getContactList();
	}

	/**
	 * 设置好友user list到内存中
	 * 
	 * @param contactList Map<String, User>
	 */
	public void setContactList(Map<String, User> contactList) {
		hxSDKHelper.setContactList(contactList);
	}

	/**
	 * 获取当前登陆用户名
	 * 
	 * @return String
	 */
	public String getUserName() {
		return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 * 
	 * @return String
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 * 
	 * @param username String
	 */
	public void setUserName(String username) {
		hxSDKHelper.setHXId(username);
	}

	/**
	 * 内部的自动登录需要的密码，已经加密存储了
	 * @param pwd String
	 */
	public void setPassword(String pwd) {
		hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
		if (hxSDKHelper != null) {
			hxSDKHelper.logout(emCallBack);
		}
	}

}
