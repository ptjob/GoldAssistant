package com.parttime.login;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.parttime.main.MainTabActivity;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;

/**
 * Created by cjz on 2015/7/30.
 */
public class ShowAnimActivity extends BaseActivity implements Runnable{
    @ViewInject(R.id.iv_anim)
    private ImageView ivAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_anim);
        ViewUtils.inject(this);
        AnimationDrawable anim = (AnimationDrawable) ivAnim.getBackground();
        anim.setOneShot(true);
        anim.start();
        ivAnim.postDelayed(this, 2010);
    }

    @Override
    public void run() {
        Intent intent = new Intent();
        intent.setClass(this,
                MainTabActivity.class);
        startActivity(intent);
        finish();
    }
}
