package com.parttime.addresslist.userdetail;

import android.view.View;
import android.widget.Button;

import com.parttime.pojo.UserDetailVO;
import com.qingmu.jianzhidaren.R;

/**
 * 好友容器
 */
public class FriendContentContainer{

    public Button sendAddFriend, //发送添加好友
                toMsg,  //发起会话
                deleteFriend;//删除好友

    //数据部分
    UserDetailPagerAdapter.UserDetailFragment userDetailFragment;
    UserDetailPagerAdapter userDetailPagerAdapter;

    public FriendContentContainer(UserDetailPagerAdapter.UserDetailFragment userDetailFragment,
                                  UserDetailPagerAdapter userDetailPagerAdapter) {
        this.userDetailFragment = userDetailFragment;
        this.userDetailPagerAdapter = userDetailPagerAdapter;
    }

    public void initView(View view){

        sendAddFriend = (Button)view.findViewById(R.id.send_add_friend);
        toMsg = (Button)view.findViewById(R.id.to_msg);
        deleteFriend = (Button)view.findViewById(R.id.delete_friend);

    }

    public void reflesh(UserDetailVO vo) {

    }
}