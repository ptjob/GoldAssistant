package com.parttime.IM.activitysetting;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.Constant;

/**
 * Created by dehua on 15/8/5.
 */
public class ChatSendMsgHelper {

    public void sendShareActivity(String groupId,
                                  String activityId, String activityJobPlace,
                                  String activityTitle, String activitySalary){
        //这里是扩展自文本消息，如果这个自定义的消息需要用到语音或者图片等，可以扩展自语音、图片消息，亦或是位置消息。
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        TextMessageBody txtBody = new TextMessageBody("");
        message.addBody(txtBody);

        /*activityId:活动id
        activityJobPlace:工作区域
        activityTitle:活动标题
        activitySalary:薪资(如:100元/天)*/

        // 增加自己特定的属性,目前sdk支持int,boolean,String这三种属性，可以设置多个扩展属性
        message.setAttribute("activityId", activityId);
        message.setAttribute("activityJobPlace", activityJobPlace);
        message.setAttribute("activityTitle", activityTitle);
        message.setAttribute("activitySalary", activitySalary);
        message.setAttribute(Constant.MESSAGE_SHARE_JOB, "1");

        message.setReceipt(groupId);
        EMChatManager.getInstance()
                .getConversation(groupId).addMessage(message);
        //发送消息
        EMChatManager.getInstance().sendMessage(message,new EMCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

}
