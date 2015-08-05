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
public class PersonalCertSubmitActivity extends UpLoadPicActivity implements ActionSheet.OnActionSheetSelected {
    public static final String EXTRA_CERT_STATUS = "extra_cert_status";
    public static final String EXTRA_IS_AGENT = "extra_is_agent";

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
//    @ViewInject(R.id.ll_front_text)
//    private LinearLayout llFrontText;
//    @ViewInject(R.id.ll_back_text)
    private LinearLayout llBackText;


    private Bitmap userPhotoBmp = null;//
    private int option = 1;//
    private String uploadidUrl;

    private boolean idFrontUploaded, idBackUploaded;

    UploadImg.OnUploadListener frontUploadListener = new UploadImg.OnUploadListener() {
        @Override
        public void success() {
//            llFrontText.setVisibility(View.GONE);
            idFrontUploaded = true;
        }

        @Override
        public void fail() {

        }
    };

    UploadImg.OnUploadListener backUploadListener = new UploadImg.OnUploadListener() {
        @Override
        public void success() {
            llBackText.setVisibility(View.GONE);
            idBackUploaded = true;
        }

        @Override
        public void fail() {

        }
    };

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

    @Override
    protected int getOption(View clicked) {
        if(clicked == flIdFront){
            return 1;
        }else if(clicked == flIdBack){
            return 2;
        }
        return 0;
    }

    @Override
    protected String getUploadUrl(int option) {
        if(option == 1){
            return Url.COMPANY_uploadIdcard_zheng;
        }else if(option == 2){
            return Url.COMPANY_uploadIdcard_fan;
        }
        return null;
    }


    @Override
    protected ImageView getImageViewToShowUploadPic(int option) {
        ImageView iv = null;
        switch (option){
            case 1:
                iv = ivIdFront;
                break;
            case 2:
                iv = ivIdBack;
                break;
            default:

        }

        return iv;
    }

    @Override
    protected UploadImg.OnUploadListener getUploadListener(int option) {
        UploadImg.OnUploadListener listener;
        switch (option){
            case 1:
                listener = frontUploadListener;
                break;
            case 2:
                listener = backUploadListener;
                break;
            default:
                listener = null;
        }
        return listener;
    }
}
