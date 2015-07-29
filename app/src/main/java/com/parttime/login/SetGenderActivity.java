package com.parttime.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.WithTitleActivity;
import com.qingmu.jianzhidaren.R;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_gender);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
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
        showToast("male");
    }

    @OnClick(R.id.fl_female)
    public void female(View v){
        showToast("female");
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
