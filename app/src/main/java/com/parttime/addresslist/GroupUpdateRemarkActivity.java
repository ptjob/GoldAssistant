package com.parttime.addresslist;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parttime.common.head.ActivityHead;
import com.parttime.constants.ActivityExtraAndKeys;
import com.parttime.net.DefaultCallback;
import com.parttime.net.UserDetailRequest;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;

public class GroupUpdateRemarkActivity extends BaseActivity {

    private String groupId , userId;
    private String oldName;


    private EditText name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_update_remark);
        ActivityHead headView = new ActivityHead(this);
        headView.setCenterTxt1(R.string.update_group_remark);

        name = (EditText)findViewById(R.id.name);
        findViewById(R.id.done).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String newName = name.getText().toString();
                if(! newName.equals(oldName)){
                    new UserDetailRequest().updateUserRemark(groupId,userId,newName,
                            GroupUpdateRemarkActivity.this.queue , new DefaultCallback(){
                        @Override
                        public void success(Object obj) {
                            GroupUpdateRemarkActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            });
                        }

                        @Override
                        public void failed(Object obj) {
                            Toast.makeText(GroupUpdateRemarkActivity.this, getString(R.string.action_failed), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }else{
                    finish();
                }

            }
        });

        bindData();

    }

    private void bindData() {
        groupId = getIntent().getStringExtra(ActivityExtraAndKeys.GroupSetting.GROUPID);
        userId = getIntent().getStringExtra(ActivityExtraAndKeys.USER_ID);
        oldName = getIntent().getStringExtra(ActivityExtraAndKeys.GroupUpdateRemark.USER_NAME);

        name.setText(oldName);

    }


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
}
