package com.parttime.base;

import android.content.Intent;

import com.parttime.login.FindPJLoginActivity;
import com.parttime.main.MainTabActivity;
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
}
