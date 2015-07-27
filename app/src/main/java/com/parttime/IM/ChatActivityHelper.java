package com.parttime.IM;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.parttime.constants.ActivityExtraAndKeys;
import com.parttime.pojo.GroupDescription;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.ApplicationControl;

import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 *
 * Created by luhua on 15/7/12.
 */
public class ChatActivityHelper {

    private final String TAG = "ChatActivityHelper";

    /**
     * 群聊通知
     * @param activity ChatActivity
     */
    public void showGroupNotice(final ChatActivity activity, View view){
        View popView = activity.getLayoutInflater().inflate(R.layout.activity_chat_group_notice_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        EMGroup group = EMGroupManager.getInstance().getGroup(activity.toChatUsername);
        final String description = group.getDescription();
        View edit = popView.findViewById(R.id.edit);
        String currentUser = EMChatManager.getInstance().getCurrentUser();
        if(currentUser != null && currentUser.equals(group.getOwner())) {
            edit.setVisibility(View.VISIBLE);
            edit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ApplicationControl.getInstance(), EditGroupNoticeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(ActivityExtraAndKeys.ChatGroupNotice.GROUP_NOTICE_CONTENT, description);
                    intent.putExtra(ActivityExtraAndKeys.GroupSetting.GROUPID, activity.toChatUsername);
                    ApplicationControl.getInstance().startActivity(intent);
                    popupWindow.dismiss();
                }
            });
        }



        TextView content = (TextView)popView.findViewById(R.id.group_notice_content);
        if(description != null) {
            try {
                String desc = URLDecoder.decode(description, HTTP.UTF_8);
                GroupDescription gd = new Gson().fromJson(desc, GroupDescription.class);
                if(gd != null){
                    content.setText(gd.info);
                }
            } catch (IllegalStateException | JsonSyntaxException | UnsupportedEncodingException ignore) {
                Log.e(TAG, "description format is error , description = " + description);
            }

        }
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //设置popwindow出现和消失动画
        //popupWindow.setAnimationStyle(R.style.popwin_anim_style_2);

        //设置popwindow显示位置
        popupWindow.showAsDropDown(view);
        //获取popwindow焦点
        popupWindow.setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();

    }

}
