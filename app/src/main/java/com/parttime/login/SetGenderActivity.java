package com.parttime.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droid.carson.Activity01;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.IntentManager;
import com.parttime.base.WithTitleActivity;
import com.parttime.net.BaseRequest;
import com.parttime.net.Callback;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.volley.VolleySington;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cjz on 2015/7/29.
 */
public class SetGenderActivity extends WithTitleActivity{
    public static final String EXTRA_TELEPHONE = "extra_telephone";
    public static  final String EXTRA_CODE = "extra_code";
    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_PWD_ENCODED = "extra_pwd_encoded";

    private  String telephone;
    private String code;
    private String name;
    private String pwdEncoded;

    private String gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_gender);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
        getIntentData();
    }

    private void getIntentData(){
        Intent intent = getIntent();
        telephone = intent.getStringExtra(EXTRA_TELEPHONE);
        code = intent.getStringExtra(EXTRA_CODE);
        name = intent.getStringExtra(EXTRA_NAME);
        pwdEncoded = intent.getStringExtra(EXTRA_PWD_ENCODED);
    }

    @Override
    protected void initViews() {
        super.initViews();
        center(R.string.set_gender);

    }

    @OnClick(R.id.fl_male)
    public void male(View v){
//        showToast("male");
//        startActivity(new Intent(this, Activity01.class));
        gender = "1";
        register();
    }

    @OnClick(R.id.fl_female)
    public void female(View v){
//        showToast("female");
//        startActivity(new Intent(this, Activity01.class));
        gender = "0";
        register();
    }

    private void register(){
        showWait(true);
        Map<String, String> params = new HashMap<>();
        params.put("telephone", telephone);
        params.put("name", name);
        params.put("password", pwdEncoded);
        params.put("code", code);
        params.put("city", "深圳");
        params.put("sex", gender);
        new BaseRequest().request(Url.COMPANY_REGISTER, params, VolleySington.getInstance().getRequestQueue(), new Callback() {
            @Override
            public void success(Object obj) throws JSONException {
                showWait(false);
                IntentManager.intentToLoginActivity(SetGenderActivity.this);
            }

            @Override
            public void failed(Object obj) {
                showWait(false);
                IntentManager.intentToLoginActivity(SetGenderActivity.this);
            }
        });
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
}
