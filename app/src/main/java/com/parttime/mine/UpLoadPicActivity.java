package com.parttime.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.carson.constant.ConstantForSaveList;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.LocalInitActivity;
import com.qingmu.jianzhidaren.R;
import com.quark.image.UploadImg;
import com.quark.ui.widget.ActionSheet;
import com.quark.utils.Util;

import java.io.File;
import java.io.IOException;

/**
 * Created by cjz on 2015/7/26.
 */
public abstract class UpLoadPicActivity extends LocalInitActivity implements ActionSheet.OnActionSheetSelected, DialogInterface.OnCancelListener{
    public static  final String EXTRA_ACCOUNT_TYPE = "extra_account_type";
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    private static final String IMAGE_FILE_NAME = "faceImage.jpg";//



    protected Bitmap userPhotoBmp = null;//
    protected int option = 1;//
    protected String uploadidUrl;


//    protected int accountType;
    /*
    1:待审核  2：审核通过 3：审核不通过
0:尚未提交审核
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri, int x, int y) {
        ConstantForSaveList.uploadUri = uri;// 暂时存储uri 如htc不能保存uri
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", x);
        intent.putExtra("aspectY", y);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", x);
        intent.putExtra("outputY", y);
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", false);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return 图片的旋转角度
     */
    protected int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm
     *            需要旋转的图片
     * @param degree
     *            旋转角度
     * @return 旋转后的图片
     */
    protected Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    // 位置固定 只能固定大小
                    if (data.getData() != null) {
                        startPhotoZoom(data.getData(), 360, 224);
                    } else {
                        showToast("获取图片失败...");
                    }
                    break;
                case CAMERA_REQUEST_CODE:
                    if (Util.hasSdcard()) {
                        File tempFile = new File(
                                Environment.getExternalStorageDirectory() + "/"
                                        + IMAGE_FILE_NAME);
                        //
                        BitmapFactory.Options opt = new BitmapFactory.Options();
                        opt.inJustDecodeBounds = true;
                        userPhotoBmp = BitmapFactory.decodeFile(
                                Environment.getExternalStorageDirectory() + "/"
                                        + IMAGE_FILE_NAME, opt);
                        // 获取到这个图片的原始宽度和高度
                        int picWidth = opt.outWidth;
                        int picHeight = opt.outHeight;

                        // 获取屏的宽度和高度
                        WindowManager windowManager = getWindowManager();
                        Display display = windowManager.getDefaultDisplay();
                        int screenWidth = display.getWidth();
                        int screenHeight = display.getHeight();
                        opt.inSampleSize = 2;
                        if (picWidth > picHeight) {
                            if (picWidth > screenWidth)
                                opt.inSampleSize = picWidth / screenWidth;
                        } else {
                            if (picHeight > screenHeight)

                                opt.inSampleSize = picHeight / screenHeight;
                        }
                        int degree = getBitmapDegree(Environment
                                .getExternalStorageDirectory()
                                + "/"
                                + IMAGE_FILE_NAME);
                        opt.inJustDecodeBounds = false;
                        userPhotoBmp = BitmapFactory.decodeFile(
                                Environment.getExternalStorageDirectory() + "/"
                                        + IMAGE_FILE_NAME, opt);
                        userPhotoBmp = rotateBitmapByDegree(userPhotoBmp, degree);

                        Uri tt_uri = null;
                        try {
                            tt_uri = Uri
                                    .parse(MediaStore.Images.Media.insertImage(
                                            getContentResolver(), userPhotoBmp,
                                            null, null));
                        } catch (Exception e) {
                            e.printStackTrace();
                            tt_uri = Uri.fromFile(tempFile);
                        }
                        if (tt_uri != null) {
                            startPhotoZoom(tt_uri, 360, 224);
                        } else {
                            showToast("未找到存储卡，无法存储照片");
                        }
                    } else {
                        showToast("未找到存储卡，无法存储照片");
                    }
                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        // 企业
                        if (option == 1) {
//                            proBar1.setVisibility(View.VISIBLE);
                            UploadImg.getImageToView(this,
                                    data, getImageViewToShowUploadPic(option), getUploadUrl(1),
                                    null, "company_id", getCompanyId(),
                                    "identity_front", null, null, null, null, getUploadListener(option));
                        } else if (option == 2) {
//                            proBar2.setVisibility(View.VISIBLE);
                            UploadImg.getImageToView(this,
                                    data, getImageViewToShowUploadPic(option), getUploadUrl(2),
                                    null, "company_id", getCompanyId(),
                                    "identity_verso", null, null, null, null, getUploadListener(option));
                        } else if(option == 3){
                            UploadImg.getImageToView(this,
                                    data, getImageViewToShowUploadPic(option), getUploadUrl(3),
                                    null, "company_id", getCompanyId(),
                                    "company_picture", null, null, null, null, getUploadListener(option));
                        }

                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.fl_id_front)
    public void idFrontCilck(View v){
        option = getOption(v);
        UploadImg.showSheetPic(this, this, this, this);
    }

    @OnClick(R.id.fl_id_back)
    public void idBackClick(View v){
        option = getOption(v);
        UploadImg.showSheetPic(this, this, this, this);
    }

    @Override
    public void onClick(int whichButton) {

    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    protected abstract int getOption(View clicked);
    protected abstract String getUploadUrl(int option);
    protected abstract ImageView getImageViewToShowUploadPic(int option);
    protected abstract UploadImg.OnUploadListener getUploadListener(int option);
}
