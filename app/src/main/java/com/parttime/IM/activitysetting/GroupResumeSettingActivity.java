/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.parttime.IM.activitysetting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.carson.constant.ConstantForSaveList;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.parttime.addresslist.UserDetailActivity;
import com.parttime.common.Image.ContactImageLoader;
import com.parttime.common.head.ActivityHead2;
import com.parttime.constants.ActivityExtraAndKeys;
import com.parttime.net.DefaultCallback;
import com.parttime.net.GroupSettingRequest;
import com.parttime.net.HuanXinRequest;
import com.parttime.utils.SharePreferenceUtil;
import com.parttimejob.swipe.SwipeListView;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.model.HuanxinUser;
import com.quark.ui.widget.CustomDialog;
import com.quark.volley.VolleySington;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @ClassName: GroupResumeSettingActivity
 * @Description: 群管理
 *
 */
public class GroupResumeSettingActivity extends BaseActivity implements
		View.OnClickListener {
	private static final String TAG = "GroupResumeSettingActivity";

	public static GroupResumeSettingActivity instance;

    private ActivityHead2 headView;
    private TextView tip; //显示已录取和待处理的人数
    private SwipeListView listView ;
    private Dialog more;

    private SettingAdapter adapter = new SettingAdapter();;

    protected RequestQueue queue = VolleySington.getInstance().getRequestQueue();
	private SharePreferenceUtil sp;

    private ArrayList<GroupSettingRequest.UserVO> data = new ArrayList<>();
    private String groupId;
    private EMGroup group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_setting);
		sp = SharePreferenceUtil.getInstance(ApplicationControl.getInstance());

        Intent intent = getIntent();
        groupId = intent.getStringExtra(ActivityExtraAndKeys.GroupSetting.GROUPID);

        initView();

		instance = this;

	}

    private void initView() {
        headView = new ActivityHead2(this);
        headView.setCenterTxt1(R.string.group_member);
        headView.setImgRight2(R.drawable.settings_selected);
        headView.progressBar.setVisibility(View.GONE);
        headView.imgRight2.setOnClickListener(this);

        tip = (TextView) findViewById(R.id.tip);
        listView = (SwipeListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    private void bindView(){

        if(groupId != null) {
            group = EMGroupManager.getInstance().getGroup(groupId);
            List<String> count = group.getMembers();
            headView.setCenterTxt2(getString(R.string.group_member_number, count != null ? count.size() : 1));
        }
        GroupSettingRequest.AppliantResult appliantResult = ConstantForSaveList.groupAppliantCache.get(groupId);
        if(appliantResult != null){
            tip.setText(getString(R.string.admitted_pending_tip,appliantResult.approvedCount,appliantResult.unApprovedCount));
            data.addAll(appliantResult.userList);
            adapter.notifyDataSetChanged();
        }
        getGroupApliantResult(groupId);
        // 保证每次进详情看到的都是最新的group
        //updateGroup();
    }

    /**
     * 更新已录取，和待处理的数量
     */
    public void updateTip(){
        GroupSettingRequest.AppliantResult appliantResult = ConstantForSaveList.groupAppliantCache.get(groupId);
        if(appliantResult != null) {
            tip.setText(getString(R.string.admitted_pending_tip, appliantResult.approvedCount, appliantResult.unApprovedCount));
        }
    }

    public void getGroupApliantResult(String id){
        new GroupSettingRequest().getUserList(id, queue, new DefaultCallback() {
            @Override
            public void success(Object obj) {
                super.success(obj);
                if (obj instanceof GroupSettingRequest.AppliantResult) {
                    GroupSettingRequest.AppliantResult result = (GroupSettingRequest.AppliantResult) obj;
                    tip.setText(getString(R.string.admitted_pending_tip, result.approvedCount, result.unApprovedCount));
                    List<GroupSettingRequest.UserVO> userVOs = result.userList;
                    if(result.userList != null) {
                        data.clear();
                        data.addAll(userVOs);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    protected void updateGroup() {
        headView.progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMGroup returnGroup = EMGroupManager.getInstance()
                            .getGroupFromServer(groupId);
                    // 更新本地数据
                    EMGroupManager.getInstance().createOrUpdateLocalGroup(
                            returnGroup);

                    List<String> members = group.getMembers();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            headView.setCenterTxt1(group.getGroupName());
                            headView.setCenterTxt2("(" + group.getAffiliationsCount() + "人)");
                            headView.progressBar.setVisibility(View.INVISIBLE);

                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            headView.progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        },"huanxinGroup").start();
    }

    private void getNick(List<String> members){
        StringBuilder contactIdsStr = new StringBuilder("");
        int size = members.size();
        for (int i = 0; i < size; i++) {
            if(i < size -1 ) {
                contactIdsStr.append(members.get(i)).append(",");
            }else{
                contactIdsStr.append(members.get(i));
            }
        }


        new HuanXinRequest().getHuanxinUserList(contactIdsStr.toString(), queue, new DefaultCallback(){
            @Override
            public void success(Object obj) {
                super.success(obj);
                if(obj instanceof ArrayList){
                    @SuppressLint("Unchecked")
                    ArrayList<HuanxinUser> list = (ArrayList<HuanxinUser>)obj;

                    if (list.size() > 0) {
                        ConstantForSaveList.usersNick = list;// 保存缓存

                    }

                }
            }
        });
    }

    @Override
	protected void onResume() {
		super.onResume();
        bindView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_right2:
                showMore();
                break;
        }
    }


    public Dialog showMore() {
        if(more != null){
            if(more.isShowing()){
                more.dismiss();
            }else{
                more.show();
            }
            return more;
        }
        more = new Dialog(this, R.style.ActionSheet);
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(
                R.layout.activity_group_setting_more, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);

        //批量处理
        TextView batch = (TextView) layout.findViewById(R.id.batch);
        //发送到邮箱
        TextView email = (TextView) layout.findViewById(R.id.email);
        //群设置
        TextView setting = (TextView) layout.findViewById(R.id.setting);
        //取消
        TextView cancel = (TextView) layout.findViewById(R.id.cancel);

        // 批量处理
        batch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupResumeSettingActivity.this, ResumeBatchManagementActivity.class);
                intent.putExtra(ActivityExtraAndKeys.GroupSetting.GROUPID,groupId);
                startActivity(intent);
                more.dismiss();
            }
        });

        email.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GroupResumeSettingActivity.this, SendAdmitToEmailActivity.class);
                intent.putExtra(ActivityExtraAndKeys.GroupSetting.GROUPID,groupId);
                startActivity(intent);
                more.dismiss();
            }
        });

        setting.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GroupResumeSettingActivity.this, GroupSettingActivity.class);
                    intent.putExtra(ActivityExtraAndKeys.GroupSetting.GROUPID,groupId);
                    startActivity(intent);
                    more.dismiss();
                }
            });

        cancel.setOnClickListener(new OnClickListener() {

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
        more.setCanceledOnTouchOutside(false);
        more.setContentView(layout);
        more.show();

        return more;
    }

    private class SettingAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public GroupSettingRequest.UserVO getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            GroupSettingRequest.UserVO userVO = getItem(position);
            if(userVO.apply == GroupSettingRequest.UserVO.APPLY_OK){
                return 0 ;
            }else{
                return 1;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view ;
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                if(getItem(position).apply == GroupSettingRequest.UserVO.APPLY_OK) {
                    view = getLayoutInflater().inflate(R.layout.activity_group_setting_item_swip, parent, false);
                    holder.reject = (Button)view.findViewById(R.id.reject);
                }else{
                    view = getLayoutInflater().inflate(R.layout.activity_group_setting_item_swip2, parent, false);
                }

                holder.head = (ImageView) view.findViewById(R.id.head);
                holder.name = (TextView) view.findViewById(R.id.name);
                holder.resumeStatus = (TextView) view.findViewById(R.id.resume_status);
                holder.moneyAccountStatus = (TextView) view.findViewById(R.id.money_account_status);
                holder.reputationValueStar = (LinearLayout) view.findViewById(R.id.reputation_value_star_container);
                holder.resumeButton = (Button) view.findViewById(R.id.button);

                view.setTag(holder);
            }else{
                view = convertView;
                holder = (ViewHolder)convertView.getTag();
            }

            bindData(position, holder, view);


            return view;
        }


        private void bindData(int position, ViewHolder holder, View view) {
            GroupSettingRequest.UserVO userVO = getItem(position);
            if(userVO == null){
                view.setVisibility(View.GONE);
            }else{
                view.setVisibility(View.VISIBLE);
            }

            //设置头像
            String head = userVO.picture;
            if (! TextUtils.isEmpty(head)) {
                // 默认加载本地图片
                holder.head.setTag(R.id.picture,head);
                ContactImageLoader.loadNativePhoto(String.valueOf(userVO.userId),
                        head, holder.head,queue);
                sp.loadStringSharedPreference(userVO.userId+ "realname", userVO.name);
            } else {
                holder.head.setImageResource(R.drawable.default_avatar);
            }

            holder.name.setText(userVO.name);

            holder.resumeButton.setVisibility(View.VISIBLE);
            //待处理
            int apply = userVO.apply;
            if(apply == GroupSettingRequest.UserVO.APPLY_OK){
                holder.resumeStatus.setText(R.string.already_resume);
                holder.resumeButton.setText(R.string.cancel_resume);
            }else if(apply == GroupSettingRequest.UserVO.APPLY_UNLOOK || apply == GroupSettingRequest.UserVO.APPLY_LOOKED){
                holder.resumeStatus.setText(R.string.unresume);
                holder.resumeButton.setText(R.string.resume);
            }
            holder.resumeButton.setTag(userVO);
            holder.head.setTag(userVO);

            //设置诚意金和认证
            StringBuilder moneyAndCertification = new StringBuilder();
            int moneyStatus = userVO.earnestMoney;
            int accountStatus = userVO.certification;
            if(moneyStatus == 0) {
                moneyAndCertification.append(getString(R.string.no_money));
            }else {
                moneyAndCertification.append(getString(R.string.had_money));
            }
            moneyAndCertification.append("/");

            if(accountStatus == 0){
                moneyAndCertification.append(getString(R.string.no_certification));
            }else if(accountStatus == 1){
                moneyAndCertification.append(getString(R.string.submit_certification));
            }else if(accountStatus == 2){
                moneyAndCertification.append(getString(R.string.had_certification));
            }else if(accountStatus == 3){
                moneyAndCertification.append(getString(R.string.reject_certification));
            }
            holder.moneyAccountStatus.setText(moneyAndCertification.toString());

            //设置信誉
            String creditworthiness = userVO.creditworthiness;
            addStars(creditworthiness, holder.reputationValueStar);


            setListener(holder);
        }


        private void setListener(ViewHolder holder) {
            //录取和取消录取点击事件
            holder.resumeButton.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(v instanceof Button) {
                        GroupSettingRequest.UserVO userVO = (GroupSettingRequest.UserVO)v.getTag();
                        int apply = userVO.apply;
                        if (apply == GroupSettingRequest.UserVO.APPLY_OK) {
                            //取消录取  已录用的人员，点击取消录用，弹窗提示“确认取消录用改用，取消后该用户将被移除聊天群组”——取消，确认
                            showAlertDialog( null , getString(R.string.cacel_resume_or_not_and_remove_from_group), Action.UNRESUME, userVO ,
                                    R.string.ok, R.string.cancel);
                        } else if (apply == GroupSettingRequest.UserVO.APPLY_UNLOOK || apply == GroupSettingRequest.UserVO.APPLY_LOOKED) {
                            //录取   确认后可取消录用该用户，信息中心会提醒用户‘已被商家取消录用’。同时该用户也将被移除聊天群组
                            showAlertDialog( null , getString(R.string.cacel_resume_or_not), Action.RESUME, userVO,
                                    R.string.yes , R.string.no);
                        }
                    }
                }
            });

            holder.head.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    GroupSettingRequest.UserVO userVO = (GroupSettingRequest.UserVO)v.getTag();
                    Intent intent = new Intent(GroupResumeSettingActivity.this, UserDetailActivity.class);
                    intent.putExtra(ActivityExtraAndKeys.GroupSetting.GROUPID , groupId);
                    if(userVO != null) {
                        intent.putExtra(ActivityExtraAndKeys.USER_ID, String.valueOf(userVO.userId));
                    }
                    if(EMChatManager.getInstance().getCurrentUser()
                            .equals(group.getOwner())){
                        intent.putExtra(ActivityExtraAndKeys.GroupSetting.GROUPOWNER, true);
                    }
                    startActivity(intent);
                }
            });
        }


        public void showAlertDialog(String title, String message,final Action action, final GroupSettingRequest.UserVO userVO,
                                    int positiveRes, int negativeRes) {

            CustomDialog.Builder builder = new CustomDialog.Builder(GroupResumeSettingActivity.this);
            builder.setMessage(message);
            builder.setTitle(title);
            builder.setPositiveButton(positiveRes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    showWait(true);
                    if(action == Action.UNRESUME || action == Action.REJECT){
                        ArrayList<Integer> userIds = new ArrayList<>();
                        userIds.add(userVO.userId);
                        new GroupSettingRequest().cancelResume(userIds , groupId, queue, new DefaultCallback(){
                            @Override
                            public void success(Object obj) {
                                super.success(obj);
                                new Thread(new Runnable() {
                                    public void run() {
                                        try {
                                            /*EMGroupManager.getInstance()
                                                    .removeUserFromGroup(groupId,
                                                            String.valueOf(userVO.userId));*/
                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    data.remove(userVO);
                                                    GroupSettingRequest.AppliantResult appliantResult = ConstantForSaveList.groupAppliantCache.get(groupId);
                                                    if(appliantResult != null){
                                                        appliantResult.userList.remove(userVO);
                                                        appliantResult.approvedCount  --;
                                                    }
                                                    updateTip();
                                                    adapter.notifyDataSetChanged();
                                                    showWait(false);
                                                }
                                            });
                                        } catch (final Exception e) {
                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    showWait(false);
                                                    Toast.makeText(getApplicationContext(),
                                                            "退出群聊失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }
                                }).start();
                            }

                            @Override
                            public void failed(Object obj) {
                                super.failed(obj);
                                showWait(false);
                                Toast.makeText(GroupResumeSettingActivity.this, getString(R.string.action_failed) , Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else if(action == Action.RESUME){
                        ArrayList<Integer> userIds = new ArrayList<>();
                        userIds.add(userVO.userId);
                        new GroupSettingRequest().approve(userIds , groupId, queue, new DefaultCallback(){
                            @Override
                            public void success(Object obj) {
                                super.success(obj);
                                userVO.apply = GroupSettingRequest.UserVO.APPLY_OK;
                                updateTip();
                                notifyDataSetChanged();
                                showWait(false);
                            }

                            @Override
                            public void failed(Object obj) {
                                super.failed(obj);
                                showWait(false);
                                Toast.makeText(GroupResumeSettingActivity.this, getString(R.string.action_failed) , Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });

            builder.setNegativeButton(negativeRes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            builder.create().show();
        }


    }

    private static enum  Action{
        RESUME,
        UNRESUME , REJECT
    }

    private class ViewHolder{
        public ImageView head; //头像
        public TextView name, //名字
                resumeStatus, //简历状态
                moneyAccountStatus; // 诚意金/实名认证
        public LinearLayout reputationValueStar; //信誉值
        public Button resumeButton;
        public Button reject;//拒绝

    }

    public void addStars(String creditworthiness,LinearLayout container){
        int cre = Integer.valueOf(creditworthiness);
        container.removeAllViews();
        int num = cre / 10 ;
        for(int i = 0 ; i < num; i ++){
            container.addView(newStar());
        }

    }

    private ImageView newStar(){
        ImageView star = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        star.setLayoutParams(params);
        star.setImageResource(R.drawable.ee_27);
        return star;
    }

}
