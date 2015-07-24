package com.parttime.mine.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.carson.constant.ConstantForSaveList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.WithTitleActivity;
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
public class ModifyCellphoneActivity extends WithTitleActivity{

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

    private CustomDialog dlg;
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
    }

    private boolean validate(){

        if(!Util.isMobileNO(eiOriginalNumber.getValue().trim())){
            showToast(R.string.phone_number_format_wrong);
            return false;
        }
        return false;
    }

    @OnClick(R.id.btn_get_code)
    public void getCode(View v){
//        if(!validate()){
//            return;
//        }
        btnGetCode.setEnabled(false);
        showWait(true);
        Map<String, String> map = new HashMap<String, String>();
//        map.put("telephone", eiOriginalNumber.getValue().trim());
//        map.put("code", code);
        map.put("telephone", eiNewNumber.getValue().trim());
        new BaseRequest().request(Url.COMPANY_SEND_TEL_CODE, map, VolleySington.getInstance().getRequestQueue(), new Callback() {
            @Override
            public void success(Object obj) {
                showWait(false);
                finish();
            }

            @Override
            public void failed(Object obj) {
                showWait(false);
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
                        if(dialog != null && dlg.isShowing()){
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
