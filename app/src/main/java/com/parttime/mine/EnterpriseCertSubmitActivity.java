package com.parttime.mine;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.net.BaseRequest;
import com.parttime.net.Callback;
import com.parttime.widget.EditItem;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.image.UploadImg;
import com.quark.utils.Util;
import com.quark.volley.VolleySington;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cjz on 2015/7/27.
 */
public class EnterpriseCertSubmitActivity extends UpLoadPicActivity{
    public static final String EXTRA_CERT_STATUS = "extra_cert_status";
    public static final String EXTRA_IS_AGENT = "extra_is_agent";


    @ViewInject(R.id.ei_boss_name)
    private EditItem eiBossName;
    @ViewInject(R.id.ei_boss_id_card)
    private EditItem eiBossIdCard;
    @ViewInject(R.id.ei_enterprise_reg_id)
    private EditItem eiRegId;
    @ViewInject(R.id.fl_id_front)
    private FrameLayout flFront;
    @ViewInject(R.id.fl_id_back)
    private FrameLayout flBack;
    @ViewInject(R.id.fl_enterprise_reg_id)
    private FrameLayout flRegId;

    @ViewInject(R.id.iv_id_front)
    private ImageView ivIdFront;
    @ViewInject(R.id.iv_id_back)
    private ImageView ivIdBack;
    @ViewInject(R.id.iv_enterprise_reg_id)
    private ImageView ivRegId;
    @ViewInject(R.id.btn_submit)
    private Button btnSummit;
    @ViewInject(R.id.ll_front_text)
    private LinearLayout llFrontText;
    @ViewInject(R.id.ll_back_text)
    private LinearLayout llBackText;
    @ViewInject(R.id.ll_enterprise_reg_id)
    private LinearLayout llRegIdText;

    private boolean idFrontUploaded, idBackUploaded, idRegIdUploaded;


    UploadImg.OnUploadListener frontUploadListener = new UploadImg.OnUploadListener() {
        @Override
        public void success() {
            llFrontText.setVisibility(View.GONE);
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

    UploadImg.OnUploadListener regIdUploadListener = new UploadImg.OnUploadListener() {

        @Override
        public void success() {
            llRegIdText.setVisibility(View.GONE);
            idRegIdUploaded = true;
        }

        @Override
        public void fail() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_enterprise_cert_submit);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        super.initViews();
        center(R.string.enterprise_cert);
        left(TextView.class, R.string.back);
    }

//    @OnClick(R.id.fl_id_front)
//    public void uploadFront(View v){
//
//    }
//
//    @OnClick(R.id.fl_id_back)
//    public void uploadBack(View v){
//
//    }

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
        params.put("company_code", eiRegId.getValue().trim());
        params.put("type", "" + accountType);
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

        String regId = eiRegId.getValue().trim();
        if(Util.isEmpty(regId)){
            showToast(R.string.please_enter_reg_id);
            return false;
        }


        if(!idFrontUploaded || !idBackUploaded){
            showToast(R.string.please_upload_id_card);
            return false;
        }
        return true;
    }


    @OnClick(R.id.fl_enterprise_reg_id)
    public void uploadRegId(View v){
        option = 3;
        UploadImg.showSheetPic(this, this, this, this);
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
    protected int getOption(View clicked) {
        if(clicked == flFront){
            return 1;
        }else if(clicked == flBack){
            return 2;
        }else if(clicked == flRegId){
            return 3;
        }
        return 0;
    }

    @Override
    protected String getUploadUrl(int option) {
        if(option == 1){
            return Url.COMPANY_uploadIdcard_zheng;
        }else if(option == 2){
            return Url.COMPANY_uploadIdcard_fan;
        }else if(option == 3){
            return Url.COMPANY_uploadyinyzz;
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
            case 3:
                iv = ivRegId;
            default:

        }

        return iv;
    }

    @Override
    protected UploadImg.OnUploadListener getUploadListener(int option) {
        return null;
    }
}
