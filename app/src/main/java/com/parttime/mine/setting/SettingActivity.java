package com.parttime.mine.setting;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carson.constant.ConstantForSaveList;
import com.easemob.EMCallBack;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.IntentManager;
import com.parttime.base.WithTitleActivity;
import com.parttime.login.FindPJLoginActivity;
import com.parttime.main.MainTabActivity;
import com.parttime.widget.FormButton;
import com.parttime.widget.FormItem;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.model.HuanxinUser;
import com.quark.ui.widget.CustomDialog;

import java.util.ArrayList;

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

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        super.onCreate(savedInstanceState);
        init();
    }

    protected void init(){
        sp = getSharedPreferences("jrdr.setting",
                Context.MODE_PRIVATE);
    }

    @Override
    protected void initViews() {
        super.initViews();
        ViewUtils.inject(this);
        center(R.string.setting);
        left(TextView.class, R.string.back);
    }


    public void showAlertDialog() {

        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage(R.string.logout_prompt_content);
        builder.setTitle(R.string.logout_tips_title);
        builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ApplicationControl.getInstance().logout(new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences.Editor edit = sp.edit();
                                edit.putString("userId", "");
                                edit.putString("token", "");
                                edit.clear();
                                edit.commit();
                                ConstantForSaveList.usersNick = new ArrayList<HuanxinUser>();//
                                finish();
                                // carson 点击退出账号时，关闭之前的界面

								/*if (FindPJLoginActivity.instance != null) {
									FindPJLoginActivity.instance.finish();
								}*/
                                IntentManager.intentToLoginActivity(SettingActivity.this);
                            }

                        });
                    }

                    @Override
                    public void onProgress(int arg0, String arg1) {

                    }

                    @Override
                    public void onError(int arg0, String arg1) {

                    }
                });

            }
        });

        builder.setPositiveButton(R.string.wrong_click, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.create().show();
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
        showAlertDialog();
    }


}
