package com.parttime.addresslist;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.easemob.chatuidemo.activity.BaseActivity;
import com.parttime.common.head.ActivityHead;
import com.parttime.constants.ActivityExtraAndKeys;
import com.qingmu.jianzhidaren.R;

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

                }
                finish();
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
