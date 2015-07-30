package com.parttime.mine;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.parttime.base.WithTitleActivity;
import com.qingmu.jianzhidaren.R;

/**
 * Created by cjz on 2015/7/29.
 */
public class PersonalCertedActivity extends WithTitleActivity{
    public static final String EXTRA_IS_AGENT = "extra_is_agent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_certed);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        super.initViews();
        center(R.string.personal_certed);
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
}
