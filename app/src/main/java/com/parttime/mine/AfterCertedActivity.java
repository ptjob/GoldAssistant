package com.parttime.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.IntentManager;
import com.parttime.base.WithTitleActivity;
import com.parttime.common.Image.ContactImageLoader;
import com.parttime.pojo.CertVo;
import com.parttime.type.AccountType;
import com.parttime.type.CertStatus;
import com.parttime.widget.EditItem;
import com.qingmu.jianzhidaren.R;

/**
 * Created by cjz on 2015/7/29.
 */
public class AfterCertedActivity extends WithTitleActivity{
//    public static final String EXTRA_IS_AGENT = "extra_is_agent";
    public static final String EXTRA_CERT_VO = "extra_cert_vo";

    @ViewInject(R.id.ei_boss_name)
    private EditItem eiName;

    @ViewInject(R.id.ei_boss_id_card)
    private EditItem eiIdNum;

    @ViewInject(R.id.ei_enterprise_reg_id)
    private EditItem eiRegId;

    @ViewInject(R.id.btn_update)
    private Button btnUpdate;

    private CertVo certVo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntentData();
        if(certVo == null){
            return;
        }
        if(certVo.accountType == AccountType.PERSONAL ){
            setContentView(R.layout.activity_personal_certed);
        }else if(certVo.accountType == AccountType.ENTERPRISE){
            setContentView(R.layout.activity_enterprise_certed);
        }else if(certVo.accountType == AccountType.AGENT){
            if(!TextUtils.isEmpty(certVo.regId)){
                setContentView(R.layout.activity_enterprise_certed);
            }else {
                setContentView(R.layout.activity_personal_certed);
            }
        }
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
    }

    protected void getIntentData(){
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
        if(certVo.accountType == AccountType.PERSONAL){
            center(R.string.personal_certed);
        }else if(certVo.accountType == AccountType.ENTERPRISE) {
            center(R.string.enterprise_certed);
        }else {
            center(R.string.agent_certed);
        }
        left(TextView.class, R.string.back);

        initViewByStatus();
    }

    private void initViewByStatus(){
        eiName.setValue(certVo.name);
        eiIdNum.setValue(certVo.idNum);
        eiIdNum.setEnabled(false);
        eiName.setEnabled(false);
        if(certVo.accountType == AccountType.ENTERPRISE || !TextUtils.isEmpty(certVo.regId)){
            eiRegId.setValue(certVo.regId);
            eiRegId.setEnabled(false);
        }else {

        }
    }

    @OnClick(R.id.btn_update)
    public void update(View v){
        certVo.accountType = AccountType.ENTERPRISE;
        certVo.certStatus = CertStatus.NO_CERT;
        IntentManager.intentToBeforeCertedActivity(this, certVo);
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
