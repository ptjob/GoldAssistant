package com.parttime.common.Image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.parttime.utils.SharePreferenceUtil;
import com.quark.jianzhidaren.ApplicationControl;

import java.io.File;

/**
 *
 * Created by dehua on 15/7/17.
 */
public class ContactImageLoader {

    private static final String TAG = "ContactImageLoader";

    public static final String Image_Path = Environment.getExternalStorageDirectory() + "/"
            + "jzdr/" + "image";

    public static Bitmap get(String name){
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (! sdCardExist){
            return null;//获取跟目录
        }

        File mePhotoFold = new File(Image_Path);
        if (!mePhotoFold.exists()) {
            boolean result = mePhotoFold.mkdirs();
            if(result){
                Log.i(TAG, "create image path success");
            }
        }
        SharePreferenceUtil sp = SharePreferenceUtil.getInstance(ApplicationControl.getInstance());
        // 当前聊天对象的头像更改,要先联网验证头像路径是否更改
        File picture = new File(Image_Path,
                sp.loadStringSharedPreference(name + "_photo", "c"));
        if (picture.exists()) {
            // 加载本地图片
            return  BitmapFactory.decodeFile(picture.getAbsolutePath());
        }
        return null;
    }

}
