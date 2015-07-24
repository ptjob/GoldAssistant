package com.parttime.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alipay.sdk.encrypt.MD5;
import com.carson.constant.JiaoyanUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.WithTitleActivity;
import com.parttime.main.MainTabActivity;
import com.parttime.net.BaseRequest;
import com.parttime.net.Callback;
import com.parttime.widget.EditItem;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.volley.VolleySington;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cjz on 2015/7/24.
 */
public class SetPwdActivity extends WithTitleActivity implements Callback{

    public static final String EXTRA_PHONE_NUM = "extra_phone_num";
    public static final String EXTRA_VALIDATE_CODE = "extra_validate_code";

    @ViewInject(R.id.ei_new_pwd)
    private EditItem eiNewPwd;
    @ViewInject(R.id.btn_finish)
    private Button btnFinish;

    private String phoneNum;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_pwd);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
        getIntentData();
    }

    private void getIntentData(){
        Intent intent = getIntent();
        if(intent != null){
            phoneNum = intent.getStringExtra(EXTRA_PHONE_NUM);
            code = intent.getStringExtra(EXTRA_VALIDATE_CODE);
        }
    }

    @Override
    protected void initViews() {
        super.initViews();
        center(R.string.change_pwd);
    }

    @OnClick(R.id.btn_finish)
    public void submit(View v){
        showWait(true);
        Map<String, String> params  = new HashMap<String, String>();
        params.put("code", code);
        params.put("telephone", phoneNum);
        params.put("new_password", JiaoyanUtil.MD5(eiNewPwd.getValue().trim())/*eiNewPwd.getValue().trim()*/);
        new BaseRequest().request(Url.FORGET_PWD, params, VolleySington.getInstance().getRequestQueue(), this);
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
    public void success(Object obj) {
        showWait(false);
        Intent intent = new Intent(this, FindPJLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        if (MainTabActivity.instens != null) {
            MainTabActivity.instens.finish();
        }
    }

    @Override
    public void failed(Object obj) {
        showWait(false);
    }
}
