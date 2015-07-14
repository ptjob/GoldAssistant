package com.parttime.mine;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parttime.base.WithTitleActivity;
import com.qingmu.jianzhidaren.R;

/**
 * Created by cjz on 2015/7/14.
 */
public class RealNameCertActivity extends WithTitleActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_real_name_cert);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        super.initViews();
        left(TextView.class, R.string.back);
        center(R.string.real_name_cert);
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
