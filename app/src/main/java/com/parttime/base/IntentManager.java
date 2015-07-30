package com.parttime.base;

import android.app.Activity;
import android.content.Intent;

import com.parttime.login.FindPJLoginActivity;
import com.parttime.main.MainTabActivity;
import com.parttime.mine.EnterpriseCertSubmitActivity;
import com.parttime.mine.EnterpriseCertedActivity;
import com.parttime.mine.PersonalCertSubmitActivity;
import com.parttime.mine.PersonalCertedActivity;
import com.quark.jianzhidaren.BaseActivity;

/**
 * Created by cjz on 2015/7/26.
 */
public class IntentManager {
    public static void intentToLoginActivity(BaseActivity activity){
        Intent intent = new Intent(activity, FindPJLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);

        if (MainTabActivity.instens != null) {
            MainTabActivity.instens.finish();
        }
    }

    public static void intentToPersonalCertSubmitActivity(BaseActivity activity, int certStatus, int isAgent){
        Intent intent = new Intent(activity, PersonalCertSubmitActivity.class);
        intent.putExtra(PersonalCertSubmitActivity.EXTRA_CERT_STATUS, certStatus);
        intent.putExtra(PersonalCertSubmitActivity.EXTRA_IS_AGENT, isAgent);
        activity.startActivity(intent);
    }

    public static void intentToEnterpriseCertSubmitActivity(BaseActivity activity, int certStatus, int isAgent){
        Intent intent = new Intent(activity, EnterpriseCertSubmitActivity.class);
        intent.putExtra(EnterpriseCertSubmitActivity.EXTRA_CERT_STATUS, certStatus);
        intent.putExtra(EnterpriseCertSubmitActivity.EXTRA_IS_AGENT, isAgent);
        activity.startActivity(intent);
    }

    public static void intentToPersonalCertedActivity(BaseActivity activity, int isAgent){
        Intent intent = new Intent(activity, PersonalCertedActivity.class);
        intent.putExtra(PersonalCertedActivity.EXTRA_IS_AGENT, isAgent);
        activity.startActivity(intent);
    }

    public static void intentToEnterpriseCertedActivity(BaseActivity activity, int isAgent){
        Intent intent = new Intent(activity, EnterpriseCertedActivity.class);
        intent.putExtra(EnterpriseCertedActivity.EXTRA_IS_AGENT, isAgent);
        activity.startActivity(intent);
    }

}
