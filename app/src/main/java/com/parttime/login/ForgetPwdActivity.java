package com.parttime.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.carson.constant.ConstantForSaveList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.LocalInitActivity;
import com.parttime.net.BaseRequest;
import com.parttime.net.Callback;
import com.parttime.widget.EditItem;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.ui.widget.CustomDialog;
import com.quark.utils.Util;
import com.quark.volley.VolleySington;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cjz on 2015/7/24.
 */
public class ForgetPwdActivity extends LocalInitActivity{

    @ViewInject(R.id.ei_phone_num)
    private EditItem eiPhone;
    @ViewInject(R.id.ei_code)
    private EditItem eiCode;

    @ViewInject(R.id.btn_get_code)
    private Button btnGetCode;
    @ViewInject(R.id.btn_next)
    private Button btnNext;

    @ViewInject(R.id.tv_failed_to_get_code)
    private TextView tvFailToGetCode;

    private CustomDialog dlg;
    private String validateCode;
    private String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forget_pwd);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void initViews() {
        super.initViews();
        center(R.string.forgetPass);
        left(TextView.class, R.string.back);
    }

    private boolean validatePhoneNum(){
        if(!Util.isMobileNO(eiPhone.getValue().trim())){
            showToast(R.string.phone_number_format_wrong);
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_get_code)
    public void getCode(View v){
        if(!validatePhoneNum()){
            return;
        }
        phoneNum = eiPhone.getValue().trim();
        Map<String, String> params = new HashMap<String, String>();
        params.put("telephone", phoneNum);
        new BaseRequest().request(Url.COMPANY_FORGET_PWD, params, VolleySington.getInstance().getRequestQueue(), new Callback() {
            @Override
            public void success(Object obj) {
                showToast("ok");
            }

            @Override
            public void failed(Object obj) {

            }
        });
    }

    private boolean validateCode(){
        String code = eiCode.getValue().trim();
        if(code.length() <= 0){
            showToast(R.string.please_enter_validate_code);
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_next)
    public void next(View v){
        if(!validateCode()){
            return;
        }
        showWait(true);
        validateCode = eiCode.getValue().trim();
        Map<String, String> params = new HashMap<String, String>();
        params.put("telephone", phoneNum);
        params.put("code", validateCode);
        new BaseRequest().request(Url.MESSAGE_VALIDATE, params, VolleySington.getInstance().getRequestQueue(), new Callback() {
            @Override
            public void success(Object obj) {
                showWait(false);
                Intent intent = new Intent(ForgetPwdActivity.this, SetPwdActivity.class);
                intent.putExtra(SetPwdActivity.EXTRA_PHONE_NUM, phoneNum);
                intent.putExtra(SetPwdActivity.EXTRA_VALIDATE_CODE, validateCode);
                startActivity(intent);
            }

            @Override
            public void failed(Object obj) {
                showWait(false);
            }
        });
    }

    @OnClick(R.id.tv_failed_to_get_code)
    public void failToGet(View v){
        dlg = createDialog(getString(R.string.friendly_tips), getString(R.string.contact_customer_service_for_validaion_code),
                getString(R.string.contact_customer_service), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dlg.dismiss();
                        Intent intent = new Intent(
                                Intent.ACTION_CALL,
                                Uri.parse("tel:"
                                        + ConstantForSaveList.CARSON_CALL_NUMBER));
                        ForgetPwdActivity.this.startActivity(intent);
                    }
                }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(dialog != null && dlg.isShowing()){
                            dlg.dismiss();
                        }
                    }
                });
        dlg.show();
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
