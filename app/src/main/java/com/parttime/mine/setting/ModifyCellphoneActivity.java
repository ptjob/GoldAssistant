package com.parttime.mine.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carson.constant.ConstantForSaveList;
import com.carson.constant.JiaoyanUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.IntentManager;
import com.parttime.base.WithTitleActivity;
import com.parttime.constants.SharedPreferenceConstants;
import com.parttime.net.BaseRequest;
import com.parttime.net.Callback;
import com.parttime.net.ErrorHandler;
import com.parttime.utils.SharePreferenceUtil;
import com.parttime.widget.EditItem;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.ui.widget.CustomDialog;
import com.quark.utils.Util;
import com.quark.volley.VolleySington;

import org.w3c.dom.CDATASection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cjz on 2015/7/24.
 */
public class ModifyCellphoneActivity extends WithTitleActivity implements TextWatcher{

    private static final int CODE_LEN = 6;

    @ViewInject(R.id.ei_original_phone_number)
    private EditItem eiOriginalNumber;
    @ViewInject(R.id.ei_login_pwd)
    private EditItem eiLoginPwd;
    @ViewInject(R.id.ei_new_phone_number)
    private EditItem eiNewNumber;
    @ViewInject(R.id.ei_validation_code)
    private EditItem eiCode;

    @ViewInject(R.id.btn_get_code)
    private Button btnGetCode;
    @ViewInject(R.id.btn_next)
    private Button btnNext;
    @ViewInject(R.id.tv_failed_to_get_code)
    private TextView tvFailedToGet;

    @ViewInject(R.id.iv_code_ok)
    private ImageView ivVerifyStatus;

    private CustomDialog dlg;

    private String oldPhoneNum;
    private String code;
    private String pwd;
    private String newPhoneNum;

    private int lastLen;
    private boolean everReachLen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_modify_cellphone);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        super.initViews();
        left(TextView.class, R.string.back);
        center(R.string.modify_phone_number);
        eiCode.addTextChangeListener(this);
    }

//    private boolean validatePhoneNum(){
//        phoneNum = eiOriginalNumber.getValue().trim();
//        if(!Util.isMobileNO(o)){
//            showToast(R.string.old_phone_number_format_wrong);
//            return false;
//        }
//        return true;
//    }

    @OnClick(R.id.btn_get_code)
    public void getCode(View v){
        if(!validate()){
            return;
        }
        btnGetCode.setEnabled(false);
        showWait(true);
        Map<String, String> map = new HashMap<String, String>();
        map.put("telephone", newPhoneNum);
        new BaseRequest().request(Url.MESSAGE_MODIFY_TELEPHONE_NUM, map, VolleySington.getInstance().getRequestQueue(), new Callback() {
            @Override
            public void success(Object obj) {
                showWait(false);
                btnNext.setEnabled(true);
//                finish();
            }

            @Override
            public void failed(Object obj) {
                showWait(false);
                btnGetCode.setEnabled(true);
                new ErrorHandler(ModifyCellphoneActivity.this, obj).showToast();
            }
        });
    }

    private boolean validate(){
        oldPhoneNum = eiOriginalNumber.getValue().trim();
        pwd = eiLoginPwd.getValue().trim();
        newPhoneNum = eiNewNumber.getValue().trim();
        if(!Util.isMobileNO(oldPhoneNum)){
            showToast(R.string.old_phone_number_format_wrong);
            return false;
        }
        if(pwd.length() <= 0){
            showToast(R.string.please_enter_login_pwd);
            return false;
        }
        if(!Util.isMobileNO(newPhoneNum)){
            showToast(R.string.new_phone_number_format_wrong);
            return false;
        }

        return true;
    }

    private boolean validateCode(){
        code = eiCode.getValue().trim();
        if(code.length() == 0){
            showToast(R.string.please_enter_validate_code);
            return false;
        }
        return true;
    }

    private void toLogin(){
        SharePreferenceUtil spu = SharePreferenceUtil.getInstance(this);
        spu.removeAllKey();
        spu.saveSharedPreferences(SharedPreferenceConstants.REMEMBERED_TEL, newPhoneNum);
        finish();
        IntentManager.intentToLoginActivity(this);
    }

    @OnClick(R.id.btn_next)
    public void next(View v) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("telephone", oldPhoneNum);
        params.put("password", JiaoyanUtil.MD5(pwd));
        params.put("new_telephone", newPhoneNum);
        new BaseRequest().request(Url.COMPANY_MODIFY_TELEPHONE_NUM, params, VolleySington.getInstance().getRequestQueue(), new Callback() {
            @Override
            public void success(Object obj) {
                showWait(false);
                toLogin();
            }

            @Override
            public void failed(Object obj) {
                showWait(false);
                new ErrorHandler(ModifyCellphoneActivity.this, obj).showToast();
            }
        });
    }

    @OnClick(R.id.tv_failed_to_get_code)
    public void failToGetCode(View v){
        dlg = createDialog(getString(R.string.friendly_tips), getString(R.string.contact_customer_service_for_validaion_code),
                getString(R.string.contact_customer_service), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dlg.dismiss();
                        Intent intent = new Intent(
                                Intent.ACTION_CALL,
                                Uri.parse("tel:"
                                        + ConstantForSaveList.CARSON_CALL_NUMBER));
                        ModifyCellphoneActivity.this.startActivity(intent);
                    }
                }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null && dlg.isShowing()) {
                            dlg.dismiss();
                        }
                    }
                 });
        dlg.show();
    }

    @Override
    public void finish() {
        if(dlg != null && dlg.isShowing()){
            dlg.dismiss();
        }
        super.finish();
    }

    private void verifyCode(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("telephone", newPhoneNum);
        params.put("code", code);
        new BaseRequest().request(Url.MESSAGE_VALIDATE, params, VolleySington.getInstance().getRequestQueue(), new Callback() {
            @Override
            public void success(Object obj) {
                if(!everReachLen){
                    ivVerifyStatus.setVisibility(View.VISIBLE);
                    everReachLen = true;
                }
                ivVerifyStatus.setSelected(true);
                btnNext.setEnabled(true);
            }

            @Override
            public void failed(Object obj) {
                if(!everReachLen){
                    ivVerifyStatus.setVisibility(View.VISIBLE);
                    everReachLen = true;
                }
                ivVerifyStatus.setSelected(false);
            }
        });
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        code = eiCode.getValue();
        int length = s.length();
        if(length == CODE_LEN){
            if(lastLen < CODE_LEN){
                verifyCode();
            }
        }else {
            if(lastLen == CODE_LEN){
                ivVerifyStatus.setSelected(false);
                btnNext.setEnabled(false);
            }
        }
        lastLen = length;

    }
}
