package com.parttime.mine.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.WithTitleActivity;
import com.parttime.widget.FormButton;
import com.parttime.widget.FormItem;
import com.qingmu.jianzhidaren.R;

/**
 * Created by cjz on 2015/7/16.
 */
public class SettingActivity extends WithTitleActivity{
    @ViewInject(R.id.fi_account_setting)
    private FormItem fiAccountSetting;

    @ViewInject(R.id.fi_clear_cache)
    private FormItem fiClearCache;

//    @ViewInject(R.id.fi_give_a_praise)
//    private FormItem fiGiveAPraise;

    @ViewInject(R.id.fi_about)
    private FormItem fiAbout;

    @ViewInject(R.id.fb_logout)
    private FormButton fbOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        super.initViews();
        ViewUtils.inject(this);
        center(R.string.setting);
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

    private void goToActivity(Class activityClz){
        Intent intent = new Intent(this, activityClz);
        startActivity(intent);
    }

    @OnClick(R.id.fi_account_setting)
    private void accountSetting(View v){
        goToActivity(AccountSettingActivity.class);
    }

    @OnClick(R.id.fi_clear_cache)
    private void clearCache(View v){

    }

    @OnClick(R.id.fi_about)
    private void about(View v){

    }

    @OnClick(R.id.fb_logout)
    private void logout(View v){

    }


}
