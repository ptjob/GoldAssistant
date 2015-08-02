package com.parttime.net;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.parttime.pojo.UserDetailVO;
import com.quark.common.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by luhua on 2015/7/31.
 */
public class UserDetailRequest extends BaseRequest {

    /**
     * 获取联系人详情
     * @param userId  String 联系人Id
     * @param groupId String 群Id
     * @param queue RequestQueue
     * @param callback DefaultCallback
     */
    public void getUserDetail(String userId,String groupId,
                              RequestQueue queue ,
                              final DefaultCallback callback){
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("group_id", groupId);

        request(Url.USER_DETAIL_INFO,map, queue, new Callback() {

            @Override
            public void success(Object obj) throws JSONException {
                JSONObject js = null ;
                if(obj instanceof JSONObject){
                    js = (JSONObject)obj;
                }
                if(js == null){
                    callback.failed("");
                    return ;
                }
                try {
                    JSONObject userInfo = js.getJSONObject("userInfo");
                    String ui = userInfo.toString();
                    UserDetailVO userDetailVO = new Gson().fromJson(ui, UserDetailVO.class);
                    callback.success(userDetailVO);
                }catch (JSONException e){
                    callback.failed(e);
                }
            }

            @Override
            public void failed(Object obj) {
                callback.failed(obj);
            }
        });
    }

    /**
     * 评论人员
     * @param userId String
     * @param groupId String
     * @param comment String 评价等级（优秀，良好，差评，放飞机）
     * @param remark String 评语
     * @param queue RequestQueue
     * @param callback DefaultCallback
     */
    public void comment(String userId,String groupId,
                        String comment, String remark,
                        RequestQueue queue ,
                        final DefaultCallback callback){
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("group_id", groupId);
        map.put("comment", comment);
        map.put("remark", remark);

        request(Url.COMMENT_REQUIRE,map, queue, new Callback() {

            @Override
            public void success(Object obj) throws JSONException {
                callback.success(obj);
            }

            @Override
            public void failed(Object obj) {
                callback.failed(obj);
            }
        });
    }
}
