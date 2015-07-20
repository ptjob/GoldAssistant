package com.parttime.mine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carson.constant.ConstantForSaveList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.WithTitleActivity;
import com.parttime.common.Image.ContactImageLoader;
import com.parttime.constants.SharedPreferenceConstants;
import com.parttime.main.MainTabActivity;
import com.parttime.type.AccountType;
import com.parttime.utils.SharePreferenceUtil;
import com.parttime.widget.CountingEditText;
import com.parttime.widget.SelectItem;
import com.parttime.widget.CommonShowItemLayout;
import com.parttime.widget.SelectLayout;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.image.UploadImg;
import com.quark.utils.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjz on 2015/7/12.
 */
public class EditMyIntroActivity extends WithTitleActivity {
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";

    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private FrameLayout flLeft;
    private FrameLayout flRight;
    private TextView tvCenter;

    @ViewInject(R.id.cet_intro)
    private CountingEditText cetIntro;
    @ViewInject(R.id.sl_work_types)
    private SelectLayout slWorkTypes;
    @ViewInject(R.id.iv_head_image)
    private ImageView ivHead;

    private Bitmap userPhotoBmp = null;
    private String userId;
    private int userType;
    private Bitmap head;
//    private List<Object> workTypes;
    private String[] workTypes;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocalData();
        setContentView(R.layout.activity_edit_my_intro);

        super.onCreate(savedInstanceState);
        loadData();
    }

    @Override
    protected void initViews(){
        left(TextView.class, R.string.back);
        right(TextView.class, R.string.preview);
        center(R.string.edit_intro);
        ViewUtils.inject(this);
        if(head != null) {
            ivHead.setImageBitmap(head);
        }
        if(userType == AccountType.AGENT){

            slWorkTypes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected ViewGroup getLeftWrapper() {
        return null;
    }

    @Override
    protected ViewGroup getRightWrapper() {
        return null;
    }

    @Override
    protected TextView getCenter() {
        return null;
    }

    protected void loadLocalData(){
        userId = SharePreferenceUtil.getInstance(this).loadStringSharedPreference("userId");
        userType = SharePreferenceUtil.getInstance(this).loadIntSharedPreference(SharedPreferenceConstants.USER_TYPE, -1);
        head = ContactImageLoader.get(userId);
    }

    protected void loadData(){
        if(userType == AccountType.AGENT){
            workTypes = getResources().getStringArray(R.array.work_types);

            for(int i = 0; i < workTypes.length; ++i){
                slWorkTypes.add(workTypes[i]);
            }

        }
    }

    @OnClick(R.id.iv_head_image)
    public void uploadPhoto(View v){
        showSheetPic();
    }

    public Dialog showSheetPic() {
        final Dialog dlg = new Dialog(this, R.style.ActionSheet);
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.actionsheet, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);

        TextView mContent = (TextView) layout.findViewById(R.id.content);//
        // 拍照上传
        TextView mCancel = (TextView) layout.findViewById(R.id.cancel);
        TextView mTitle = (TextView) layout.findViewById(R.id.title);// 相册中选择

        // 拍照上传
        mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFromCapture = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                if (Util.hasSdcard()) {
                    File s = Environment.getExternalStorageDirectory();
                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                            .fromFile(new File(Environment
                                    .getExternalStorageDirectory(),
                                    IMAGE_FILE_NAME)));
                } else {

                    Toast mToast = Toast.makeText(EditMyIntroActivity.this,
                            "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG);
                    mToast.setGravity(Gravity.CENTER, 0, 0);
                    mToast.show();
                }
                startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                dlg.dismiss();
            }
        });

        mTitle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*"); // 设置文件类型
                intentFromGallery.setAction(Intent.ACTION_PICK);
                startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                dlg.dismiss();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        dlg.onWindowAttributesChanged(lp);
        dlg.setCanceledOnTouchOutside(false);
        dlg.setContentView(layout);
        dlg.show();

        return dlg;
    }

    private void afterGetPhoto(Intent data){
        if (data.getData() != null) {
            startPhotoZoom(data.getData(), 300, 300);
        } else {
            Toast mToast = Toast.makeText(this, "获取图片失败。。。",
                    Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
        }
    }

    private void afterTakePhoto(Intent data){
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
                // tt_uri =
                // Uri.parse(MediaStore.Images.Media.insertImage(
                // getActivity().getContentResolver(),
                // Environment.getExternalStorageDirectory() + "/"
                // + IMAGE_FILE_NAME, null, null));
                tt_uri = Uri.parse(MediaStore.Images.Media.insertImage(
                        getContentResolver(),
                        userPhotoBmp, null, null));
            } catch (Exception e) {
                e.printStackTrace();
                tt_uri = Uri.fromFile(tempFile);
            }
            if (tt_uri != null) {
                startPhotoZoom(tt_uri, 300, 300);
            } else {
                Toast mToast = Toast.makeText(this,
                        "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();
            }
        } else {

            Toast mToast = Toast.makeText(this,
                    "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
        }
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return 图片的旋转角度
     */
    private int getBitmapDegree(String path) {
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
    private Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
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

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri, int x, int y) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode != Activity.RESULT_OK){
                return;
            }
            switch (requestCode){
                case IMAGE_REQUEST_CODE:
                    afterGetPhoto(data);
                    break;
                case CAMERA_REQUEST_CODE:
                    afterTakePhoto(data);
                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        Toast mToast = Toast.makeText(this, R.string.wait_while_uploading_avatar,
                                Toast.LENGTH_LONG);
                        mToast.setGravity(Gravity.CENTER, 0, 0);
                        mToast.show();
                        String uploadAvatarUrl = Url.COMPANY_upload_avatar + "?token="
                                + MainTabActivity.token;
                        UploadImg.getImageToView(this, data,
                                ivHead, uploadAvatarUrl, null, null,
                                null, "avatar", null, "company_id", userId,
                                null);
                    }
                    break;
            }
    }
}
