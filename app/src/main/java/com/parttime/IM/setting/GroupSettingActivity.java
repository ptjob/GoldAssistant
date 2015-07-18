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
package com.parttime.IM.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.carson.constant.ConstantForSaveList;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.parttime.common.Image.ContactImageLoader;
import com.parttime.common.head.ActivityHead2;
import com.parttime.constants.ActivityExtraAndKeys;
import com.parttime.utils.SharePreferenceUtil;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.volley.VolleySington;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @ClassName: GroupSettingActivity
 * @Description: 群设置
 * @author howe
 * @date 2015-2-12 下午12:19:16
 * 
 */
public class GroupSettingActivity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = "GroupSettingActivity";

	public static GroupSettingActivity instance;

    private ActivityHead2 headView;
    private TextView tip; //显示已录取和待处理的人数
    private ListView listView ;

    private SettingAdapter adapter;

    protected RequestQueue queue;
	private SharePreferenceUtil sp;

    private ArrayList<UserVO> data = new ArrayList<>();
    private int admitted; //已录取
    private int pending; //待处理
    private String groupId;
    private EMGroup group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_setting);
		sp = SharePreferenceUtil.getInstance(ApplicationControl.getInstance());

        Intent intent = getIntent();
        admitted = intent.getIntExtra(ActivityExtraAndKeys.GroupSetting.ADMITTED,0);
        pending = intent.getIntExtra(ActivityExtraAndKeys.GroupSetting.PENDING,0);
        groupId = intent.getStringExtra(ActivityExtraAndKeys.GroupSetting.GROUPID);

        initView();

        bindView();

		queue = VolleySington.getInstance().getRequestQueue();
		instance = this;

	}

    private void initView() {
        headView = new ActivityHead2();
        headView.initHead(this);
        tip = (TextView) findViewById(R.id.tip);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }


    private void bindView(){
        tip.setText(getString(R.string.admitted_pending_tip,admitted,pending));
        if(groupId != null) {
            group = EMGroupManager.getInstance().getGroup(groupId);
        }
        // 保证每次进详情看到的都是最新的group
        updateGroup();
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
                            // 显示消息免打扰或者非免打扰
                            boolean flag = sp.loadBooleanSharedPreference(
                                    ConstantForSaveList.userId + groupId
                                            + "pingbi", false);
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

    @Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

    @Override
    public void onClick(View v) {

    }

    private class SettingAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public UserVO getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view ;
            ViewHolder holder;
            if(convertView == null){
                view = getLayoutInflater().inflate(R.layout.activity_group_setting_item,parent,false);

                holder = new ViewHolder();
                holder.head = (ImageView) view.findViewById(R.id.head);
                holder.name = (TextView) view.findViewById(R.id.name);
                holder.resumeStatus = (TextView) view.findViewById(R.id.resume_status);
                holder.moneyAccountStatus = (TextView) view.findViewById(R.id.money_account_status);
                holder.reputationValueStar = (LinearLayout) view.findViewById(R.id.reputation_value_star_container);

            }else{
                view = convertView;
                holder = (ViewHolder)convertView.getTag();
            }

            bindData(position, holder, view);

            return view;
        }

        private void bindData(int position, ViewHolder holder, View view) {
            UserVO userVO = getItem(position);
            if(userVO == null){
                view.setVisibility(View.GONE);
            }else{
                view.setVisibility(View.VISIBLE);
            }

            //设置头像
            String head = userVO.head;
            if (! TextUtils.isEmpty(head)) {
                // 默认加载本地图片
                ContactImageLoader.loadNativePhoto(userVO.uid,
                        head, holder.head,queue);
                sp.loadStringSharedPreference(userVO.uid
                        + "realname", userVO.name);
            } else {
                holder.head.setImageResource(R.drawable.default_avatar);
            }

            holder.name.setText(userVO.name);

            int resumeStatus = userVO.resumeStatus;
            //if()

            int moneyStatus = userVO.moneyStatus;
            int accountStatus = userVO.accountStatus;

            int reputationValue = userVO.reputationValue;

        }
    }

    private class ViewHolder{
        public ImageView head; //头像
        public TextView name, //名字
                resumeStatus, //简历状态
                moneyAccountStatus; // 诚意金/实名认证
        public LinearLayout reputationValueStar; //信誉值

    }

    class UserVO{
        String uid;
        String head; //user avatar
        String name;
        int resumeStatus;
        int moneyStatus;
        int accountStatus;
        int reputationValue;
    }

}
