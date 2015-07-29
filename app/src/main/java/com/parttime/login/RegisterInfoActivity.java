package com.parttime.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.carson.constant.JiaoyanUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnCompoundButtonCheckedChange;
import com.parttime.base.WithTitleActivity;
import com.parttime.widget.EditItem;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.AgreementActivity;

/**
 * Created by cjz on 2015/7/28.
 */
public class RegisterInfoActivity extends WithTitleActivity{
    public static final String EXTRA_TELEPHONE = "extra_telephone";
    public static final String EXTRA_CODE = "extra_code";

    public static final int PWD_AT_LEAT = 6;
    public static final int PWD_AT_MOST = 10;

    @ViewInject(R.id.ei_real_name)
    private EditItem eiRealName;
    @ViewInject(R.id.ei_pwd)
    private EditItem eiPwd;
    @ViewInject(R.id.cb_agree)
    private CheckBox cbAgree;
    @ViewInject(R.id.tv_agreement)
    private TextView tvAgreement;
    @ViewInject(R.id.btn_next)
    private Button btnNext;

    private String telephone;
    private String code;

    private String name;
    private String pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register_info);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
        getIntentData();
    }

    private void getIntentData(){
        Intent intent = getIntent();
        telephone = intent.getStringExtra(EXTRA_TELEPHONE);
        code = intent.getStringExtra(EXTRA_CODE);
    }

    @Override
    protected void initViews() {
        super.initViews();
        center(R.string.basic_info_1);
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent();
				intent.setClass(RegisterInfoActivity.this, AgreementActivity.class);
				startActivity(intent);
            }
        };
        CharSequence text = tvAgreement.getText();
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        ssb.setSpan(span, 7, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAgreement.setText(ssb);
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @OnClick(R.id.btn_next)
    public void next(View v){
        if(validate()){
            Intent intent = new Intent(this, SetGenderActivity.class);
            intent.putExtra(SetGenderActivity.EXTRA_TELEPHONE, telephone);
            intent.putExtra(SetGenderActivity.EXTRA_NAME, name);
            intent.putExtra(SetGenderActivity.EXTRA_CODE, code);
            intent.putExtra(SetGenderActivity.EXTRA_PWD_ENCODED, JiaoyanUtil.MD5(pwd));
            startActivity(intent);
        }

    }

    private boolean validate(){
        name = eiRealName.getValue().trim();
        pwd = eiPwd.getValue();
        if(name.length() <= 0){
            showToast(R.string.please_enter_real_name);
            return false;
        }
        if(pwd.length() <= 0){
            showToast(R.string.please_set_pwd);
            return false;
        }
        if(pwd .length() < PWD_AT_LEAT || pwd.length() > PWD_AT_MOST){
            showToast(R.string.pwd_should_range_from_6_to_10);
            return false;
        }
        if(!cbAgree.isChecked()){
            showToast(R.string.havent_read_the_agreement);
            return false;
        }
        return true;
    }

//    @OnCompoundButtonCheckedChange(R.id.cb_agree)
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//    }

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
