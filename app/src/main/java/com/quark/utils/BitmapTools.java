package com.quark.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by Administrator on 11/1 0001.
 */
public class BitmapTools {

    public static Bitmap scaleBitmap(Bitmap bitmap, int vwidth, int vheight) {
        int bwidth = bitmap.getWidth();
        int bheight = bitmap.getHeight();

        //x需要放大的倍数  view 的 width
        float scalex = (float) vwidth / bwidth;
        //y需要放大的倍数
        float scaley = (float) vheight / bheight;

        //取大的倍数， 保证视图被填充
        float scale = Math.max(scalex, scaley) * 1.1f;
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bg = Bitmap.createBitmap(vwidth, vheight, config);
        Canvas c = new Canvas(bg);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scale, scale);
        c.drawBitmap(bitmap, matrix, paint);
        return bg;
    }
}
