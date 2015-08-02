package com.parttime.base;

import android.content.Intent;

import com.parttime.login.FindPJLoginActivity;
import com.parttime.main.MainTabActivity;
import com.parttime.mine.BeforeCertedActivity;
import com.parttime.mine.AfterCertedActivity;
import com.parttime.mine.PersonalCertedActivity;
import com.parttime.mine.RealNameCertSelectActivity;
import com.parttime.pojo.CertVo;
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

    public static void intentToRealNameSelectActivity(BaseActivity activity, CertVo vo){
        Intent intent = new Intent(activity, RealNameCertSelectActivity.class);
        intent.putExtra(RealNameCertSelectActivity.EXTRA_CERT_VO, vo);
        activity.startActivity(intent);
    }

    public static void intentToBeforeCertedActivity(BaseActivity activity, CertVo vo){
        Intent intent = new Intent(activity, BeforeCertedActivity.class);
        intent.putExtra(BeforeCertedActivity.EXTRA_CERT_VO, vo);
        activity.startActivity(intent);
    }


    public static void intentToAfterCertedActivity(BaseActivity activity, CertVo vo){
        Intent intent = new Intent(activity, AfterCertedActivity.class);
        intent.putExtra(AfterCertedActivity.EXTRA_CERT_VO, vo);
        activity.startActivity(intent);
    }

}
