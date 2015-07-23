package com.parttime.base;

import android.os.Bundle;

import com.parttime.constants.SharedPreferenceConstants;
import com.parttime.utils.SharePreferenceUtil;

/**
 * Created by cjz on 2015/7/23.
 */
public abstract class LocalInitActivity extends WithTitleActivity{
    protected String cId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocalData();
    }

    protected void loadLocalData(){
        cId = SharePreferenceUtil.getInstance(this).loadStringSharedPreference(SharedPreferenceConstants.COMPANY_ID);
    }

    protected String getCompanyId(){
        return cId;
    }
}
