package com.quark.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import com.quark.image.ImageCache;

/**
 * @author Royal
 * @Email Royal.k.peng@gmail.com
 */
public final class ConfigDataUtil {

	// 配置一个参数表示是否测试，如果是测试，条件不满进行跳转。
	public static final boolean inTest = true;
	private static final String TAG = "ConfigDataUtil";
	private static final String IMAGE_CACHE_DIR = "thumbs";
	// 饿汉模式，默认初始化，
	private static ConfigDataUtil instance = new ConfigDataUtil();

	// bitmapfun
	private ImageCache.ImageCacheParams cacheParams;
	private ImageCache mImageCache;

	private static final byte[] lock = new byte[0];
	private DisplayMetrics metrics;

	/**
	 * 使用私有构造器，不让外部对象初始化对象
	 */
	private ConfigDataUtil() {

	}

	public void setDisplayMetrics(DisplayMetrics dm) {
		this.metrics = dm;
	}

	public DisplayMetrics getMetrics() {
		return metrics;
	}

	public int getWindowsWidth() {
		if (null == metrics) {
			return -1;
		}
		return metrics.widthPixels;

	}

	public static ConfigDataUtil getInstance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new ConfigDataUtil();
			}
		}
		return instance;
	}

	public void init(Context context) {
		// initImageCache(context);
	}

	/**
	 * 初始化 bitmapfun 框架中的配置参数
	 * 
	 * @param context
	 */
	private void initImageCache(Context context) {

		cacheParams = new ImageCache.ImageCacheParams(IMAGE_CACHE_DIR);
		// cacheParams.memCacheSize = 1024 * 1024 *
		// Utils.getMemoryClass(context) / 3;
		mImageCache = new ImageCache(context, cacheParams);
	}

	public ImageCache getmImageCache(Context context) {
		if (mImageCache == null) {
			initImageCache(context);
		}
		return mImageCache;
	}

	//
	public void clearCache(Context context) {
		getmImageCache(context).clearCaches();
		// DiskLruCache.clearCache(context, ImageFetcher.HTTP_CACHE_DIR);
	}

}
