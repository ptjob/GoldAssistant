package com.parttime.addresslist.userdetail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;
import com.easemob.chatuidemo.domain.User;
import com.parttime.IM.ChatActivity;
import com.parttime.pojo.UserDetailVO;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.ui.widget.EditDialog;

import java.util.Map;
import java.util.Set;

/**
 * 好友容器
 */
public class FriendContentContainer implements View.OnClickListener{

    public Button sendAddFriend, //发送添加好友
                toMsg,  //发起会话
                deleteFriend;//删除好友

    LinearLayout friendContainer;

    UserDetailActivity activity;

    //数据部分
    UserDetailPagerAdapter.UserDetailFragment userDetailFragment;
    UserDetailPagerAdapter userDetailPagerAdapter;


    public FriendContentContainer(UserDetailPagerAdapter.UserDetailFragment userDetailFragment,
                                  UserDetailPagerAdapter userDetailPagerAdapter) {
        this.userDetailFragment = userDetailFragment;
        this.userDetailPagerAdapter = userDetailPagerAdapter;
        activity = userDetailPagerAdapter.userDetailActivity;
    }

    public void initView(View view){

        sendAddFriend = (Button)view.findViewById(R.id.send_add_friend);
        toMsg = (Button)view.findViewById(R.id.to_msg);
        deleteFriend = (Button)view.findViewById(R.id.delete_friend);

        friendContainer = (LinearLayout)view.findViewById(R.id.friend_container);

        toMsg.setOnClickListener(this);
        sendAddFriend.setOnClickListener(this);

    }

    public void reflesh(UserDetailVO vo) {
        Map<String,User> contactList = ApplicationControl.getInstance().getContactList();
        if(contactList != null && vo != null){
            String userId = vo.userId;
            boolean contain = false;
            Set<String> keys = contactList.keySet();
            for(String key : keys){
                if(key.contains(userId)){
                    contain = true;
                    break;
                }
            }
            if(contain){
                //初始化好友界面
                toMsg.setVisibility(View.VISIBLE);
                deleteFriend.setVisibility(View.GONE);
                friendContainer.setVisibility(View.VISIBLE);
            }else{
                //初始化加入好友界面
                sendAddFriend.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_add_friend:
                sendAddFriend("", "请在下方表明您的身份", userDetailFragment.userId);
                break;
            case R.id.to_msg:
                toMsg();
                break;
            case R.id.delete_friend:
                deleteFriend();
                break;
        }
    }

    private void sendAddFriend(String str, final String str2, final String userId) {

            final EditDialog.Builder builder = new EditDialog.Builder(activity);
            builder.setMessage(str);
            builder.setTitle(str2);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    final String content = builder.getContent();

                    dialog.dismiss();
                    final ProgressDialog progressDialog = new ProgressDialog(activity);
                    progressDialog.setMessage("正在发送请求...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                String sendstr ;
                                // demo写死了个reason，实际应该让用户手动填入
                                if (content.isEmpty()) {
                                    sendstr = "邀请你为好友";
                                } else {
                                    sendstr = content;
                                }
                                EMContactManager.getInstance().addContact(
                                        userId, sendstr);
                                activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(activity, "发送请求成功,等待对方验证", Toast.LENGTH_LONG)
                                                .show();
                                    }
                                });
                            } catch (final Exception e) {
                                activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(activity,
                                                "请求添加好友失败:" + e.getMessage(), Toast.LENGTH_LONG)
                                                .show();
                                    }
                                });
                            }
                        }
                    }).start();

                }
            });
            builder.create().show();
    }

    private void toMsg() {
        activity.startActivity(new Intent(activity,
                ChatActivity.class).putExtra("userId",
                userDetailFragment.userId));
    }

    private void deleteFriend() {


    }
}