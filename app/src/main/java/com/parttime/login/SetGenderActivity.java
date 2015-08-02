package com.parttime.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droid.carson.Activity01;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.CitySelectActivity;
import com.parttime.base.IntentManager;
import com.parttime.base.WithTitleActivity;
import com.parttime.constants.SharedPreferenceConstants;
import com.parttime.net.BaseRequest;
import com.parttime.net.Callback;
import com.parttime.net.ErrorHandler;
import com.parttime.utils.SharePreferenceUtil;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.volley.VolleySington;

import org.json.JSONException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cjz on 2015/7/29.
 */
public class SetGenderActivity extends WithTitleActivity{
    public static final String EXTRA_TELEPHONE = "extra_telephone";
    public static  final String EXTRA_CODE = "extra_code";
    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_PWD_ENCODED = "extra_pwd_encoded";
    public static final String DEF_LOCATION_FAIL = "定位失败";

    private static final int REQUEST_CODE_LOCATION = 10001;

    private  String telephone;
    private String code;
    private String name;
    private String pwdEncoded;

    private String gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_gender);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
        getIntentData();
    }

    private void getIntentData(){
        Intent intent = getIntent();
        telephone = intent.getStringExtra(EXTRA_TELEPHONE);
        code = intent.getStringExtra(EXTRA_CODE);
        name = intent.getStringExtra(EXTRA_NAME);
        pwdEncoded = intent.getStringExtra(EXTRA_PWD_ENCODED);
    }

    @Override
    protected void initViews() {
        super.initViews();
        center(R.string.set_gender);

    }

    @OnClick(R.id.fl_male)
    public void male(View v){
//        showToast("male");
//        startActivity(new Intent(this, Activity01.class));
        gender = "1";
        register();
    }

    @OnClick(R.id.fl_female)
    public void female(View v){
//        showToast("female");
//        startActivity(new Intent(this, Activity01.class));
        gender = "0";
        register();
    }

    private void register(){
        /*showWait(true);
        Map<String, String> params = new HashMap<>();
        params.put("telephone", telephone);
        params.put("name", name);
        params.put("password", pwdEncoded);
        params.put("code", code);
        params.put("city", "深圳");
        params.put("sex", gender);
        new BaseRequest().request(Url.COMPANY_REGISTER, params, VolleySington.getInstance().getRequestQueue(), new Callback() {
            @Override
            public void success(Object obj) throws JSONException {
                showWait(false);
                IntentManager.intentToLoginActivity(SetGenderActivity.this);
            }

            @Override
            public void failed(Object obj) {
                showWait(false);
                IntentManager.intentToLoginActivity(SetGenderActivity.this);
            }
        });*/
        Intent intent = new Intent();
        // 传值当前定位城市
        intent.putExtra(Activity01.EXTRA_CITYLIST_CITY,
                SharePreferenceUtil.getInstance(this).loadStringSharedPreference(
                        SharedPreferenceConstants.DINGWEICITY, DEF_LOCATION_FAIL));
        intent.setClass(this, CitySelectActivity.class);
//        startActivityForResult(intent, REQUEST_CODE_LOCATION);
        intent.putExtra(CitySelectActivity.EXTRA_DIY_ACTION, new SelectCityAction());
        intent.putExtra(CitySelectActivity.EXTRA_ACITON_EXTRA, new RegParams(telephone, code, name, pwdEncoded, gender));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_LOCATION:
                    String string = data.getExtras().getString(Activity01.EXTRA_CITY);
                    showToast(string);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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

    public static class RegParams implements Serializable {
        public  String telephone;
        public String code;
        public String name;
        public String pwdEncoded;

        public String gender;

        public RegParams(String telephone, String code, String name, String pwdEncoded, String gender) {
            this.telephone = telephone;
            this.code = code;
            this.name = name;
            this.pwdEncoded = pwdEncoded;
            this.gender = gender;
        }
    }

    public static class SelectCityAction implements Activity01.DiyAction {

        @Override
        public void clicked(int index, String city, Serializable extra, final BaseActivity activity) {
            RegParams regParams = (RegParams) extra;
            activity.showWait(true);
            Map<String, String> params = new HashMap<>();
            params.put("telephone", regParams.telephone);
            params.put("name", regParams.name);
            params.put("password", regParams.pwdEncoded);
            params.put("code", regParams.code);
            params.put("city", city);
            params.put("sex", regParams.gender);
            new BaseRequest().request(Url.COMPANY_REGISTER, params, VolleySington.getInstance().getRequestQueue(), new Callback() {
                @Override
                public void success(Object obj) throws JSONException {
                    activity.showWait(false);
                    IntentManager.intentToLoginActivity(activity);
                    activity.finish();
                }

                @Override
                public void failed(Object obj) {
                    activity.showWait(false);
                    new ErrorHandler(activity, obj).showToast();
//                    IntentManager.intentToLoginActivity(activity);
                }
            });

        }
    }
}
