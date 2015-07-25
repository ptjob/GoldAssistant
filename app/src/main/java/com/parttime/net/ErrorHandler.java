package com.parttime.net;

import com.android.volley.VolleyError;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;

import org.json.JSONException;

/**
 * Created by cjz on 2015/7/25.
 */
public class ErrorHandler {
    private BaseActivity activity;
    private Object error;

    public ErrorHandler(BaseActivity activity, Object error) {
        this.activity = activity;
        this.error = error;
    }

    private String getErrorMsg(){
        if(error == null){
            return activity.getString(R.string.operation_success);
        }
        if(error instanceof ResponseBaseCommonError){
            ResponseBaseCommonError err = (ResponseBaseCommonError) error;
            return err.msg;
//            return
        }
//        if(error instanceof JSONException || error instanceof VolleyError){
            return activity.getString(R.string.error_operation_fail);
//        }

    }

    public void showToast(){
        if(activity != null){
            activity.showToast(getErrorMsg());
        }
    }
}
