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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carson.constant.ConstantForSaveList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.net.BaseRequest;
import com.parttime.net.Callback;
import com.parttime.widget.EditItem;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.image.UploadImg;
import com.quark.ui.widget.ActionSheet;
import com.quark.utils.Util;
import com.quark.volley.VolleySington;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cjz on 2015/7/26.
 */
public class PersonalCertSubmitActivity extends UpLoadPicActivity implements ActionSheet.OnActionSheetSelected, DialogInterface.OnCancelListener{
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    private static final String IMAGE_FILE_NAME = "faceImage.jpg";//

    @ViewInject(R.id.ei_boss_name)
    private EditItem eiBossName;
    @ViewInject(R.id.ei_boss_id_card)
    private EditItem eiBossIdCard;
    @ViewInject(R.id.fl_id_front)
    private FrameLayout flIdFront;
    @ViewInject(R.id.fl_id_back)
    private FrameLayout flIdBack;
    @ViewInject(R.id.iv_id_front)
    private ImageView ivIdFront;
    @ViewInject(R.id.iv_id_back)
    private ImageView ivIdBack;
    @ViewInject(R.id.btn_submit)
    private Button btnSummit;
    @ViewInject(R.id.ll_front_text)
    private LinearLayout llFrontText;
    @ViewInject(R.id.ll_back_text)
    private LinearLayout llBackText;


    private Bitmap userPhotoBmp = null;//
    private int option = 1;//
    private String uploadidUrl;

    private boolean idFrontUploaded, idBackUploaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_cert_submit);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        super.initViews();
        center(R.string.personal_cert);
        left(TextView.class, R.string.back);
    }

    @OnClick(R.id.fl_id_front)
    public void idFrontCilck(View v){
        option = 1;
        uploadidUrl = Url.COMPANY_uploadIdcard_zheng;
        UploadImg.showSheetPic(this, this, this, this);
    }

    @OnClick(R.id.fl_id_back)
    public void idBackClick(View v){
        option = 2;
        uploadidUrl = Url.COMPANY_uploadIdcard_fan;
        UploadImg.showSheetPic(this, this, this, this);
    }

    @OnClick(R.id.btn_submit)
    public void submit(View v){
        if(!validate()){
            return;
        }
        showWait(true);
        Map<String, String> params = new HashMap<>();
        params.put("company_id", getCompanyId());
        params.put("name", eiBossName.getValue().trim());
        params.put("identity", eiBossIdCard.getValue().trim());
        params.put("type", "" + 1);
        new BaseRequest().request(Url.COMPANY_shenheSubmit, params, VolleySington.getInstance().getRequestQueue(), new Callback() {
            @Override
            public void success(Object obj) {
                showWait(false);
            }

            @Override
            public void failed(Object obj) {
                showWait(false);
            }
        });
    }

    private boolean validate(){
        String bossName = eiBossName.getValue().trim();
        if(bossName.length() <= 0){
            showToast(R.string.please_enter_name);
            return false;
        }
        if(!Util.isName(bossName)){
            showToast(R.string.name_should_be_chinese);
            return false;
        }

        String bossIdCard = eiBossIdCard.getValue().trim();
        if(bossIdCard.length() <= 0){
            showToast(R.string.please_enter_name);
            return false;
        }
        if(!Util.isIdCard(bossIdCard)){
            showToast(R.string.please_enter_id_in_format);
            return false;
        }

        if(!idFrontUploaded || !idBackUploaded){
            showToast(R.string.please_upload_id_card);
            return false;
        }
        return true;
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

    @Override
    public void onClick(int whichButton) {

    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri, int x, int y) {
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
                                    data, ivIdFront, uploadidUrl,
                                    null, "company_id", getCompanyId(),
                                    "identity_front", null, null, null, null, new UploadImg.OnUploadListener() {
                                        @Override
                                        public void success() {
                                            llFrontText.setVisibility(View.GONE);
                                            idFrontUploaded = true;
                                        }

                                        @Override
                                        public void fail() {

                                        }
                                    });
                        } else if (option == 2) {
//                            proBar2.setVisibility(View.VISIBLE);
                            UploadImg.getImageToView(this,
                                    data, ivIdBack, uploadidUrl,
                                    null, "company_id", getCompanyId(),
                                    "identity_verso", null, null, null, null, new UploadImg.OnUploadListener() {
                                        @Override
                                        public void success() {
                                            llBackText.setVisibility(View.GONE);
                                            idBackUploaded = true;
                                        }

                                        @Override
                                        public void fail() {

                                        }
                                    });
                        }

                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
