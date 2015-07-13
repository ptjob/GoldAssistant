package com.parttime.IM;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.ApplicationControl;

/**
 *
 * Created by luhua on 15/7/12.
 */
public class ChatActivityHelper {

    /**
     * 群聊通知
     * @param activity ChatActivity
     */
    public void showGroupNotice(ChatActivity activity, View view){
        View popView = activity.getLayoutInflater().inflate(R.layout.activity_chat_group_notice_popup, null);
        PopupWindow popupWindow = new PopupWindow(popView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        popView.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplicationControl.getInstance(),EditGroupNoticeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ApplicationControl.getInstance().startActivity(intent);
            }
        });

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
