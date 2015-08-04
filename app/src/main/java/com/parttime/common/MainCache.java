package com.parttime.common;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MainCache {

    public static LruCache<String,Bitmap> generateHeadDrawableCache = new LruCache<>(512 * 1024);

}