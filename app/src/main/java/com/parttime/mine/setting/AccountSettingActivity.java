package com.parttime.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.WithTitleActivity;
import com.parttime.widget.FormItem;
import com.qingmu.jianzhidaren.R;

/**
 * Created by cjz on 2015/7/16.
 */
public class AccountSettingActivity extends WithTitleActivity{
    @ViewInject(R.id.fi_modify_phone_number)
    private FormItem fiPhone;

    @ViewInject(R.id.fi_modify_pwd)
    private FormItem fiPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_account_setting);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        super.initViews();
        center(R.string.account_setting);
        left(TextView.class, R.string.back);
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

    @OnClick(R.id.fi_modify_phone_number)
    private void modifyPhone(View v){
        startActivity(new Intent(this, ModifyCellphoneActivity.class));
    }

    @OnClick(R.id.fi_modify_pwd)
    private void modifyPwd(View v){
        startActivity(new Intent(this, ModifyPwdActivity.class));
    }

}
