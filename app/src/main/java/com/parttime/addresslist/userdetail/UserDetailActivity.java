package com.parttime.addresslist.userdetail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.parttime.addresslist.GroupUpdateRemarkActivity;
import com.parttime.base.WithTitleActivity;
import com.parttime.constants.ActivityExtraAndKeys;
import com.qingmu.jianzhidaren.R;
import com.quark.volley.VolleySington;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class UserDetailActivity extends WithTitleActivity implements View.OnClickListener {

    public String groupId;
    public boolean isGroupOwner = false;
    public FromAndStatus fromAndStatus;
    public String userId;//当前显示的UserId
    //0:没有获取成功 1:禁言 2:非禁言
    private int forbiddenValue = 0;

    public ViewPager viewPager ;

    public UserDetailPagerAdapter adapter ;

    private LinkedHashSet<String> set;

    public RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_detail);
        super.onCreate(savedInstanceState);
        queue = VolleySington.getInstance().getRequestQueue();
        initView();

        bindData();
    }

    private void initView() {
        left(TextView.class, R.string.back);
        right(TextView.class, R.string.more ,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMore();
            }
        });
        center(R.string.user_detail);

        viewPager = (ViewPager)findViewById(R.id.viewPager);


    }

    private void bindData() {
        groupId = getIntent().getStringExtra(ActivityExtraAndKeys.GroupSetting.GROUPID);
        Serializable serializable = getIntent().getSerializableExtra(ActivityExtraAndKeys.UserDetail.FROM_AND_STATUS);
        userId = getIntent().getStringExtra(ActivityExtraAndKeys.UserDetail.SELECTED_USER_ID);
        isGroupOwner = getIntent().getBooleanExtra(ActivityExtraAndKeys.GroupSetting.GROUPOWNER,false);
        ArrayList<String> userIds = getIntent().getStringArrayListExtra(ActivityExtraAndKeys.USER_ID);

        if(isGroupOwner && userIds != null && userIds.size() > 0){
            rightWrapper.setVisibility(View.VISIBLE);
            //初始化更多的禁言管理的数据
            if(!TextUtils.isEmpty(userId)){
                initUserBlock(userId);
            }
        }else{
            rightWrapper.setVisibility(View.GONE);
        }

        if(serializable != null && serializable instanceof FromAndStatus){
            fromAndStatus = (FromAndStatus)serializable;
        }
        if(fromAndStatus == null){
            fromAndStatus = FromAndStatus.FROM_ACTIVITY_GROUP_VIEW_RESUME;
        }

        int viewPagerCurrentItem = -1;
        set = new LinkedHashSet<>();
        if(userIds != null && userIds.size() > 0){
            for(String userId : userIds){
                if(TextUtils.isEmpty(userId)){
                    continue;
                }
                set.add(userId);
            }
            viewPagerCurrentItem = userIds.indexOf(userId);
        }

        adapter = new UserDetailPagerAdapter(getSupportFragmentManager(),this);
        adapter.setData(set);
        adapter.fromAndStatus = fromAndStatus;
        viewPager.setAdapter(adapter);
        //设置当前页显示的user
        if(viewPagerCurrentItem > -1){
            viewPager.setCurrentItem(viewPagerCurrentItem);
        }


    }

    public void initUserBlock(final String userId){
        this.userId = userId;
        new Thread(new Runnable() {

            public void run() {
                try {
                    List<String> blockedList = EMGroupManager.getInstance()
                            .getBlockedUsers(groupId);
                    if (blockedList != null ) {
                        if(blockedList.contains(userId)){
                            forbiddenValue = 1;
                        }else{
                            forbiddenValue = 2;
                        }
                    }
                } catch (EaseMobException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "获取失败,请检查网络或稍后再试", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected ViewGroup getLeftWrapper() {return null;}

    @Override
    protected ViewGroup getRightWrapper() {return null;}

    @Override
    protected TextView getCenter() {return null;}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    private Dialog showMore() {
        final Dialog more = new Dialog(this, R.style.ActionSheet);
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(
                R.layout.activity_person_detail_more, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);

        //禁言
        final TextView forbiddenTable = (TextView) layout.findViewById(R.id.forbidden_talk);
        //取消禁言
        TextView cancelForbiddenTalk = (TextView) layout.findViewById(R.id.cancel_forbidden_talk);
        //更新群备注
        TextView updateGroupNotice = (TextView) layout.findViewById(R.id.update_group_notice);
        //取消
        TextView cancel = (TextView) layout.findViewById(R.id.cancel);

        forbiddenTable.setVisibility(View.GONE);
        cancelForbiddenTalk.setVisibility(View.GONE);
       if(forbiddenValue == 1){
           cancelForbiddenTalk.setVisibility(View.VISIBLE);
        }else if(forbiddenValue == 2){
           forbiddenTable.setVisibility(View.VISIBLE);
        }

        // 禁言
        forbiddenTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more.dismiss();
                showWait(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMGroupManager.getInstance().blockUser(groupId,
                                    userId);
                            forbiddenValue = 1;
                        } catch (EaseMobException e) {
                            e.printStackTrace();
                        }
                        showWait(false);
                    }
                }).start();


            }
        });

        //取消禁言
        cancelForbiddenTalk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                more.dismiss();
                showWait(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMGroupManager.getInstance().unblockUser(groupId,
                                    userId);
                            forbiddenValue = 2;
                        } catch (EaseMobException e) {
                            e.printStackTrace();
                        }
                        showWait(false);
                    }
                }).start();

            }
        });

        //更新群备注
        updateGroupNotice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(adapter.cache != null && adapter.cache.get(userId) != null) {
                    Intent intent = new Intent(UserDetailActivity.this, GroupUpdateRemarkActivity.class);
                    intent.putExtra(ActivityExtraAndKeys.GroupSetting.GROUPID, groupId);
                    intent.putExtra(ActivityExtraAndKeys.USER_ID, userId);
                    intent.putExtra(ActivityExtraAndKeys.GroupUpdateRemark.USER_NAME, adapter.cache.get(userId).name);
                    startActivity(intent);
                    more.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                more.dismiss();
            }
        });

        Window w = more.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        lp.y = -1000;
        lp.gravity = Gravity.BOTTOM;
        more.onWindowAttributesChanged(lp);
        more.setCanceledOnTouchOutside(true);
        more.setContentView(layout);
        more.show();

        return more;
    }

    public static enum FromAndStatus{
        FROM_NORMAL_GROUP_AND_FRIEND, //从普通群组过来 不是好友   //从普通群组过来 是好友
        FROM_ACTIVITY_GROUP_AND_NOT_FINISH, //从活动群过来 活动没有结束
        FROM_ACTIVITY_GROUP_AND_IS_FINISH, //从活动群过来 活动已结束
        FROM_ACTIVITY_GROUP_VIEW_RESUME,  //查看简历
    }
}
