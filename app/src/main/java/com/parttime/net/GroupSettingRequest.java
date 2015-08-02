package com.parttime.net;

import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.carson.constant.ConstantForSaveList;
import com.parttime.pojo.BaseUser;
import com.quark.common.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by luhua on 15/7/18.
 */
public class GroupSettingRequest extends BaseRequest {

    /**
     * 查看报名人员列表
     * @param reqData String
     * @param queue RequestQueue
     * @param callback DefaultCallback
     */
    public void getUserList(final String reqData, RequestQueue queue , final DefaultCallback callback){
        Map<String, String> map = new HashMap<>();
        map.put("group_id", reqData);

        request(Url.COMPANY_ACTIVITYFACEBOOK,map, queue, new Callback() {
            @Override
            public void success(Object obj) {
                JSONObject js = null ;
                if(obj instanceof JSONObject){
                    js = (JSONObject)obj;
                }
                if(js == null){
                    callback.failed("");
                    return ;
                }
                try {
                    AppliantResult appliantResult = new AppliantResult();
                    appliantResult.approvedCount = js.getInt("approved_count");
                    appliantResult.unApprovedCount = js.getInt("unapproved_count");
                    appliantResult.isEnd = js.getInt("isEnd");
                    ArrayList<UserVO> userVOs = new ArrayList<>();
                    JSONArray jss = js.getJSONArray("userList");
                    for (int i = 0; i < jss.length(); i++) {
                        UserVO userVO = new UserVO();
                        JSONObject jsonObject = jss.getJSONObject(i);
                        userVO.userId = jsonObject.getInt("user_id");
                        userVO.creditworthiness = jsonObject.getString("creditworthiness");
                        userVO.picture = jsonObject.getString("picture_1");
                        userVO.name = jsonObject.getString("name");
                        userVO.sex = jsonObject.getInt("sex");
                        userVO.age = jsonObject.getInt("age");
                        userVO.telephone = jsonObject.getString("telephone");
                        userVO.apply = jsonObject.getInt("apply");
                        userVO.ableComment = jsonObject.getInt("ableComment");
                        userVO.isCommented = jsonObject.getInt("is_commented");
                        userVO.earnestMoney = jsonObject.getInt("earnest_money");
                        userVO.certification = jsonObject.getInt("certification");
                        if(userVO.apply != UserVO.APPLY_REJECT) {
                            userVOs.add(userVO);
                        }else{
                            appliantResult.unApprovedCount --;
                        }
                    }
                    appliantResult.userList = userVOs;
                    if (userVOs.size() > 0) {
                        ConstantForSaveList.groupAppliantCache.put(reqData,appliantResult);// 保存缓存
                    }
                    callback.success(appliantResult);
                } catch (JSONException e) {
                    callback.failed(e);
                }
            }

            @Override
            public void failed(Object obj) {
                callback.failed(obj);
            }
        }) ;
    }

    public static class AppliantResult{ //报名列表

        public static int NO_END = 0;
        public static int YES_END = 1;


        public List<UserVO> userList;
        public int approvedCount; //已确认人数
        public int unApprovedCount; //未确认人数
        public int isEnd; //：活动是否结束（0-否，1-是）
    }

    public static class UserVO extends BaseUser{

        public static int APPLY_OK = 1;
        public static int APPLY_UNLOOK = 0;
        public static int APPLY_LOOKED = 3;
        public static int APPLY_REJECT = 2;

        public static int ABLECOMMENT_NO = 0;
        public static int ABLECOMMENT_OK = 1;
        public static int ISCOMMENT_NO = 0;
        public static int ISCOMMENT_OK = 1;

        public int userId;  //环信ID
        public String creditworthiness; //信誉值  对10取整
        public String picture; //头像
        public String name;     //姓名
        public int sex;         //性别（0-女，1-男）
        public int age;         //年龄
        public String telephone;//电话
        public int apply ;      //录取状态（0-没查看，1-已录取，2-、已拒绝，3-已查看）
        public int ableComment; //是否可评价（0-否，1-是)
        public int isCommented; //评价状态（0-未评价，1-已评价）
        public int earnestMoney;    //诚意金
        public int certification;   //认证状态  0:未认证 1:已提交认证 2:认证通过 3:认证不通过

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UserVO)) return false;

            UserVO userVO = (UserVO) o;

            if (userId != userVO.userId) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return userId;
        }
    }

    /**
     *  录取人员
     * @param userIds List<Integer>
     * @param groupId String
     * @param queue RequestQueue
     * @param callback DefaultCallback
     */
    public void approve(List<Integer> userIds, String groupId,  RequestQueue queue , final DefaultCallback callback){
        StringBuilder stringBuilder = new StringBuilder();
        int size = userIds.size();
        for(int i= 0 ; i < size; i ++){
            int userId = userIds.get(i);
            if(i < size - 1){
                stringBuilder.append(userId).append(",");
            }else{
                stringBuilder.append(userId);
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("user_id", stringBuilder.toString());
        map.put("group_id", groupId);

        request(Url.COMPANY_APPROVEACTIVITY,map, queue, new Callback() {
            @Override
            public void success(Object obj) {
                callback.success(obj);
            }

            @Override
            public void failed(Object obj) {
                callback.failed(obj);
            }
        }) ;
    }


    /**
     * 取消录取
     * @param userIds List<Integer>
     * @param groupId String
     * @param queue RequestQueue
     * @param callback DefaultCallback
     */
    public void cancelResume(List<Object> userIds, String groupId,  RequestQueue queue , final DefaultCallback callback){
        StringBuilder stringBuilder = new StringBuilder();
        int size = userIds.size();
        for(int i= 0 ; i < size; i ++){
            Object userId = userIds.get(i);
            if(i < size - 1){
                stringBuilder.append(userId).append(",");
            }else{
                stringBuilder.append(userId);
            }
        }
        cancelResume(groupId, queue, callback, stringBuilder);
    }
/*

    public void cancelResume(List<String> userIds, String groupId,  RequestQueue queue , final DefaultCallback callback) {
        StringBuilder stringBuilder = new StringBuilder();
        int size = userIds.size();
        for (int i = 0; i < size; i++) {
            String userId = userIds.get(i);
            if (i < size - 1) {
                stringBuilder.append(userId).append(",");
            } else {
                stringBuilder.append(userId);
            }
        }
        cancelResume(groupId, queue, callback, stringBuilder);
    }
*/

    private void cancelResume(String groupId, RequestQueue queue, final DefaultCallback callback, StringBuilder stringBuilder) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", stringBuilder.toString());
        map.put("group_id", groupId);

        request(Url.COMPANY_CANCELAPPROVEACTIVITY,map, queue, new Callback() {
            @Override
            public void success(Object obj) {
                callback.success(obj);
            }

            @Override
            public void failed(Object obj) {
                callback.failed(obj);
            }
        }) ;
    }


    /**
     * 拒绝人员
     * @param userIds List<Integer>
     * @param groupId String
     * @param queue RequestQueue
     * @param callback DefaultCallback
     */
    public void reject(List<Integer> userIds, String groupId,  RequestQueue queue , final DefaultCallback callback){
        StringBuilder stringBuilder = new StringBuilder();
        int size = userIds.size();
        for(int i= 0 ; i < size; i ++){
            int userId = userIds.get(i);
            if(i < size - 1){
                stringBuilder.append(userId).append(",");
            }else{
                stringBuilder.append(userId);
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("user_id", stringBuilder.toString());
        map.put("group_id", groupId);

        request(Url.COMPANY_REJECTACTIVITY,map, queue, new Callback() {
            @Override
            public void success(Object obj) {
                callback.success(obj);
            }

            @Override
            public void failed(Object obj) {
                callback.failed(obj);
            }
        }) ;
    }

    /**
     * 发送录取人员到邮箱
     * @param email String
     * @param groupId String
     * @param groupName String
     * @param queue RequestQueue
     * @param callback DefaultCallback
     */
    public void sendAdmitUserToEmail(String email, String groupId, String groupName, RequestQueue queue , final DefaultCallback callback){

        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("group_id", groupId);
        map.put("group_name", groupName);

        request(Url.COMPANY_GETGROUPEXCEL,map, queue, new Callback() {
            @Override
            public void success(Object obj) {
                callback.success(obj);
            }

            @Override
            public void failed(Object obj) {
                callback.failed(obj);
            }
        }) ;
    }

    /**
     * 更新群通告
     * @param groupId  群组ID
     * @param updaterName 修改者姓名
     * @param info 通告内容
     * @param title 通告标题
     * @param queue RequestQueue
     * @param callback DefaultCallback
     */
    public void updateGroupDescription(String groupId, String updaterName,String info, String title ,RequestQueue queue , final DefaultCallback callback){
        Map<String, String> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("name", updaterName);
        map.put("info", info);
        if(!TextUtils.isEmpty(title)) {
            map.put("title", title);
        }

        request(Url.GROUP_MODIFYGROUPDESC,map, queue, new Callback() {
            @Override
            public void success(Object obj) {
                callback.success(obj);
            }

            @Override
            public void failed(Object obj) {
                callback.failed(obj);
            }
        }) ;
    }


}
