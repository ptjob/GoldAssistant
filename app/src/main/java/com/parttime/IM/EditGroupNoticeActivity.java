package com.parttime.IM;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.parttime.common.head.ActivityHead;
import com.parttime.constants.ActivityExtraAndKeys;
import com.parttime.net.DefaultCallback;
import com.parttime.net.GroupSettingRequest;
import com.parttime.pojo.GroupDescription;
import com.qingmu.jianzhidaren.R;
import com.quark.volley.VolleySington;

import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class EditGroupNoticeActivity extends BaseActivity implements View.OnClickListener{

    private final String TAG = "EditGroupNoticeActivity";

    private static final int MAX_COUNT = 140;

    private EditText noticeContent;
    private TextView contentCount;
    private Button done;

    private GroupDescription groupNotice;
    private String groupId;

    protected RequestQueue queue = VolleySington.getInstance().getRequestQueue();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group_notice);

        initValue();

        bindData();

        setListener();
    }


    private void initValue() {
        //设置头部
        ActivityHead activityHead = new ActivityHead(this);
        activityHead.setCenterTxt1(R.string.group_notice);

        noticeContent = (EditText)findViewById(R.id.notice_content);
        contentCount = (TextView)findViewById(R.id.text_count);
        done = (Button)findViewById(R.id.done);

    }

    private void bindData() {
        groupId = getIntent().getStringExtra(ActivityExtraAndKeys.GroupSetting.GROUPID);
        String description = getIntent().getStringExtra(ActivityExtraAndKeys.ChatGroupNotice.GROUP_NOTICE_CONTENT);
        if(! TextUtils.isEmpty(description)) {
            try {
                description = URLDecoder.decode(description, "UTF-8");
                groupNotice = new Gson().fromJson(description, GroupDescription.class);
            } catch (IllegalStateException | JsonSyntaxException | UnsupportedEncodingException ignore) {
                Log.e(TAG, "description format is error , description = " + description);
            }
        }
        if(groupNotice != null) {
            noticeContent.setText(groupNotice.info);
            contentCount.setText(getString(R.string.content_text_count,groupNotice.info.length(),MAX_COUNT));
        }else{
            contentCount.setText(getString(R.string.content_text_count,0,MAX_COUNT));
        }

    }

    private void setListener(){
        noticeContent.addTextChangedListener(editChangeListener);
        done.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.done:
                String info = noticeContent.getText().toString();
                /*if(groupNotice != null) {
                    groupNotice.info = info;
                    String desc = new Gson().toJson(groupNotice);
                    try {
                        desc = URLEncoder.encode(desc,HTTP.UTF_8);*/
                        new GroupSettingRequest().updateGroupDescription(groupId, EMChatManager.getInstance().getCurrentUser(),info,null,queue, new DefaultCallback(){
                            @Override
                            public void success(Object obj) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            EMGroup group = EMGroupManager.getInstance().getGroupFromServer(groupId);
                                            EMGroupManager.getInstance().createOrUpdateLocalGroup(group);
                                        } catch (EaseMobException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();

                                Toast.makeText(EditGroupNoticeActivity.this,R.string.update_success , Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void failed(Object obj) {
                                Toast.makeText(EditGroupNoticeActivity.this,R.string.action_failed , Toast.LENGTH_SHORT).show();
                            }
                        });
                   /* } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }*/
        }
    }

    private TextWatcher editChangeListener = new TextWatcher() {
        private int editStart;//光标开始位置
        private int editEnd;//光标结束位置
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            int contentLength = start + count;
            if(contentLength > MAX_COUNT){
                Toast.makeText(EditGroupNoticeActivity.this, getString(R.string.group_notice_max_count_tip,MAX_COUNT),Toast.LENGTH_SHORT).show();
                return ;
            }

            contentCount.setText(getString(R.string.content_text_count,contentLength,MAX_COUNT));

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length() > MAX_COUNT) {
                editStart = noticeContent.getSelectionStart();
                editEnd = noticeContent.getSelectionEnd();
                s.delete(editStart - 1, editEnd);
                noticeContent.setText(s);
                noticeContent.setSelection(MAX_COUNT);
            }
        }
    };
}
