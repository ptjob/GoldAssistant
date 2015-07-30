package com.parttime.base;

import android.os.Bundle;

import com.parttime.constants.SharedPreferenceConstants;
import com.parttime.type.AccountType;
import com.parttime.utils.SharePreferenceUtil;

/**
 * Created by cjz on 2015/7/23.
 */
public abstract class LocalInitActivity extends WithTitleActivity{
    protected String cId;
    protected int userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocalData();
        super.onCreate(savedInstanceState);
    }

    protected void loadLocalData(){
        SharePreferenceUtil spu = SharePreferenceUtil.getInstance(this);
        cId = spu.loadStringSharedPreference(SharedPreferenceConstants.COMPANY_ID);
        userType = spu.loadIntSharedPreference(SharedPreferenceConstants.USER_TYPE, AccountType.INIT);
    }

    protected String getCompanyId(){
        return cId;
    }

    protected int getUserType(){
        return userType;
    }
}
