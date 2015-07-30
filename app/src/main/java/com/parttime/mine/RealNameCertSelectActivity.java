package com.parttime.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.WithTitleActivity;
import com.qingmu.jianzhidaren.R;

/**
 * Created by cjz on 2015/7/14.
 */
public class RealNameCertSelectActivity extends WithTitleActivity{
    public static final String EXTRA_IS_AGENT = "extra_is_agent";

    @ViewInject(R.id.btn_personal_cert)
    private Button btnPersonalCert;
    @ViewInject(R.id.btn_enterprise_cert)
    private Button btnEnterpriseCert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_real_name_cert);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        super.initViews();
        left(TextView.class, R.string.back);
        center(R.string.real_name_cert);
    }

    @OnClick(R.id.btn_personal_cert)
    public void personalCert(View v){
        startActivity(new Intent(this, PersonalCertSubmitActivity.class));
    }

    @OnClick(R.id.btn_enterprise_cert)
    public void enterpriseCert(View v){
        startActivity(new Intent(this, EnterpriseCertSubmitActivity.class));
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
