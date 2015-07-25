package com.parttime.net;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.google.gson.Gson;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.utils.NetWorkCheck;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 *
 * Created by luhua on 15/7/16.
 */
public class BaseRequest {

    /**
     * 请求公共方法，
     * @param reqParams Map<String,String> 请求参数
     * @param queue RequestQueue 请求队列
     * @param callback 回调
     *                 success回调：obj 是 JSONObject
     *                 failed 回调：obj 是 ResponseBaseCommonError or JSONException or VolleyError
     */
    public void request(String url, final Map<String,String> reqParams, RequestQueue queue , final Callback callback){
        //检查网络状态
        if (! NetWorkCheck.isOpenNetwork(ApplicationControl.getInstance())) {
            Toast.makeText(ApplicationControl.getInstance(), ApplicationControl.getInstance().getString(R.string.no_net_tip), Toast.LENGTH_SHORT).show();
            return ;
        }
        StringRequest request = new StringRequest(Request.Method.POST,url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // showWait(false);
                try {
                    JSONObject js = new JSONObject(response);
                    int status = js.getInt("status");
                    if(BaseResponse.STATUS_SUCCESS == status) {
                        callback.success(js);
                    }else{
                        ResponseBaseCommonError error = new Gson().fromJson(response, ResponseBaseCommonError.class);
                        callback.failed(error);
                    }

                } catch (JSONException e) {
                    callback.failed(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.failed(volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return reqParams;
            }
        };
        queue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
    }

}
