package com.parttime.constants;

import android.text.TextUtils;

import com.carson.constant.ConstantForSaveList;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.google.gson.Gson;
import com.parttime.net.GroupSettingRequest;
import com.parttime.pojo.GroupDescription;

import java.util.List;
import java.util.Map;

/**
 * Created by dehua on 15/8/2.
 */
public class ConstantForSaveListHelper {

    public void updateGroupAppliantCacheIsComment(String groupId, String userId){
        if(ConstantForSaveList.groupAppliantCache != null) {
            GroupSettingRequest.AppliantResult appliantResult = ConstantForSaveList.groupAppliantCache.get(groupId);
            if(appliantResult != null && appliantResult.userList != null){
                for(GroupSettingRequest.UserVO userVO : appliantResult.userList){
                    if(userVO == null){
                        continue;
                    }
                    if(userId.equals(userVO.userId)){
                        userVO.isCommented = GroupSettingRequest.UserVO.ISCOMMENT_OK;
                        break;
                    }
                }

            }
        }
    }

    public static void cacheGroupType(){
        Map<String,GroupDescription> cache = ConstantForSaveList.groupDescriptionMapCache;
        List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();
        if(groups != null){
            Gson gson = new Gson();
            for (EMGroup group : groups){
                if(group != null){
                    String groupId = group.getGroupId();
                    if(cache.get(groupId) != null){
                        continue;
                    }
                    String description = group.getDescription();
                    if(!TextUtils.isEmpty(description)){
                        try {
                            GroupDescription groupDescription = gson.fromJson(description, GroupDescription.class);
                            if (groupDescription == null) {
                                continue;
                            }
                            cache.put(groupId, groupDescription);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
