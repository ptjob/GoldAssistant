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
import com.parttime.pojo.CertVo;
import com.parttime.type.AccountType;
import com.qingmu.jianzhidaren.R;

/**
 * Created by cjz on 2015/7/14.
 */
public class RealNameCertSelectActivity extends WithTitleActivity{
//    public static final String EXTRA_IS_AGENT = "extra_is_agent";
    public static final String EXTRA_CERT_VO = "extra_cert_vo";

    @ViewInject(R.id.btn_personal_cert)
    private Button btnPersonalCert;
    @ViewInject(R.id.btn_enterprise_cert)
    private Button btnEnterpriseCert;

    private CertVo certVo;

    public static RealNameCertSelectActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntentData();
        setContentView(R.layout.activity_real_name_cert);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);

        instance = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    private void getIntentData(){
        Intent intent = getIntent();
        certVo = intent.getParcelableExtra(EXTRA_CERT_VO);
        if(certVo == null){
            finish();
            return;
        }
    }

    @Override
    protected void initViews() {
        super.initViews();
        left(TextView.class, R.string.back);
        center(R.string.real_name_cert);
    }

    private void realNameCert(){
        Intent intent = new Intent(this, BeforeCertedActivity.class);
        intent.putExtra(BeforeCertedActivity.EXTRA_CERT_VO, certVo);
        startActivity(intent);
    }

    @OnClick(R.id.btn_personal_cert)
    public void personalCert(View v){
        certVo.accountType = AccountType.PERSONAL;
        realNameCert();
    }

    @OnClick(R.id.btn_enterprise_cert)
    public void enterpriseCert(View v){
        certVo.accountType = AccountType.ENTERPRISE;
        realNameCert();
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
