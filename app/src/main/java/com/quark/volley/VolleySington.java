package com.quark.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 11/4 0004. 创建一个单利来管理我们额RequestQueue保证
 * App中只有一个RequestQueue 在 Application 初始化context/
 */
public class VolleySington {

	private static VolleySington mInstance;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private Context mContext;

	private static final byte[] lock = new byte[0];
	private static final byte[] imageLock = new byte[0];

	private VolleySington() {

	}

	public static VolleySington getInstance() {
		synchronized (lock) {
			if (mInstance == null) {
				mInstance = new VolleySington();
			}
		}
		return mInstance;
	}

	/**
	 * 这个方法不要再其它地方调用,避免引起内存泄漏
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		// mRequestQueue = getRequestQueue();
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			// getApplicationContext()是关键, 它会避免
			// Activity或者BroadcastReceiver带来的缺点.
			mRequestQueue = Volley.newRequestQueue(mContext);
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req) {
		getRequestQueue().add(req);
	}

	private void initmImageLoader() {
		if (mRequestQueue == null) {
			getRequestQueue();
		}
		mImageLoader = new ImageLoader(mRequestQueue,
				new ImageLoader.ImageCache() {
					private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(
							20);

					@Override
					public Bitmap getBitmap(String url) {
						return cache.get(url);
					}

					@Override
					public void putBitmap(String url, Bitmap bitmap) {
						cache.put(url, bitmap);
					}
				});
	}

	public ImageLoader getImageLoader() {
		synchronized (imageLock) {
			if (null == mImageLoader) {
				initmImageLoader();
			}
		}
		return mImageLoader;
	}

}
