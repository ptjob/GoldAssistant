package com.parttime.common.Image;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.carson.constant.ConstantForSaveList;
import com.parttime.constants.ActivityExtraAndKeys;
import com.parttime.utils.SharePreferenceUtil;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.http.image.LoadImage;
import com.quark.jianzhidaren.ApplicationControl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * Created by dehua on 15/7/17.
 */
public class ContactImageLoader {

    private static final String TAG = "ContactImageLoader";

    public static final String APP_PATH = Environment.getExternalStorageDirectory() + "/"
            + "jzdr";

    public static final String Image_Path = Environment.getExternalStorageDirectory() + "/"
            + "jzdr/" + "image";

    public static Bitmap get(String id){
        return get(id, null);
    }

    public static Bitmap get(String id, String imgName){
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (! sdCardExist){
            return null;
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

        File picture = null;
        if(imgName != null){
            picture = new File(Image_Path, imgName);
        }else {
            picture = new File(Image_Path,
                    sp.loadStringSharedPreference(id + "_photo", "c"));
        }
        if (! picture.isDirectory() && picture.exists()) {
            // 加载本地图片
            return  BitmapFactory.decodeFile(picture.getAbsolutePath());
        }
        return null;
    }

    /**
     * 加载图片， 先判断本地，本地不存在，获取网络图片
     * @param id String
     * @param avatarUrl String
     * @param avatar ImageView
     * @param queue RequestQueue
     */
    public static void loadNativePhoto(final String id, final String avatarUrl,
                                 final ImageView avatar, RequestQueue queue ) {

        loadNativePhoto(id, avatarUrl, avatar, R.drawable.default_avatar, queue);

    }

    public static void loadNativePhoto(final String id, final String avatarUrl,
                                       final ImageView avatar, int defaultImgRes, RequestQueue queue ){
        Bitmap bitmap = get(id, avatarUrl);
        if(bitmap == null){
            if(defaultImgRes > 0) {
                avatar.setBackgroundResource(defaultImgRes);
            }
            loadpersonPic(queue, id, avatarUrl, avatar, 1);
        }else{
            avatar.setBackgroundDrawable(bitmapToDrawable(bitmap));
        }
    }

    /**
     * 加载图片
     * @param queue RequestQueue
     * @param id String
     * @param url String
     * @param imageView ImageView
     * @param isRound int
     */
    public static void loadpersonPic(RequestQueue queue , final String id, final String url,
                               final ImageView imageView, final int isRound) {
        ImageRequest imgRequest = new ImageRequest(Url.GETPIC + url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap arg0) {
                        String picture = null;
                        Object obj = imageView.getTag(R.id.picture);
                        if(obj instanceof String) {
                            picture = (String)obj;
                        }
                        if(url.equals(picture) || TextUtils.isEmpty(picture)) {
                            imageView.setBackgroundDrawable(bitmapToDrawable(arg0));
                        }

                        boolean sdCardExist = Environment.getExternalStorageState()
                                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
                        if (! sdCardExist){
                            return;
                        }
                        OutputStream output = null;
                        try {
                            File mePhotoFold = new File(Image_Path);
                            if (!mePhotoFold.exists()) {
                                mePhotoFold.mkdirs();
                            }
                            output = new FileOutputStream(new File(Image_Path,  url));
                            arg0.compress(Bitmap.CompressFormat.JPEG, 100,
                                    output);
                            output.flush();
                            SharePreferenceUtil sp = SharePreferenceUtil.getInstance(ApplicationControl.getInstance());
                            sp.saveSharedPreferences(id + "_photo", url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally{
                            if(output != null){
                                try {
                                    output.close();
                                } catch (IOException ignore) {
                                }
                            }
                        }

                    }
                }, 300, 200, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {

            }
        });
        queue.add(imgRequest);
        imgRequest.setRetryPolicy(new DefaultRetryPolicy(
                ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

    }

    public static long getCacheSize(){
        if(!Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED)) {
            return 0;
        }
        File mePhotoFold = new File(Image_Path);
        if(mePhotoFold.isDirectory() && mePhotoFold.exists()){
            long total = 0;
            File[] files = mePhotoFold.listFiles();
            if(files != null){
                for(File f : files){
                    if(f.exists() && !f.isDirectory()){
                        total += f.length();
                    }
                }
            }
            return total;
        }
        return 0;
    }

    public static void clearCache(){
        if(!Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED)) {
            return ;
        }
        File mePhotoFold = new File(Image_Path);
        if(mePhotoFold.isDirectory() && mePhotoFold.exists()){
            File[] files = mePhotoFold.listFiles();
            if(files != null){
                for(File file : files){
                    if(file.exists() && !file.isDirectory()){
                        file.delete();
                    }
                }
            }
        }
    }

    public static Drawable bitmapToDrawable(Bitmap bitmap){
        return new BitmapDrawable(ApplicationControl.getInstance().getResources(),  bitmap);
    }

}
