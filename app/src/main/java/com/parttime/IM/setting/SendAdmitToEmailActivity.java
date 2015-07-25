package com.parttime.IM.setting;

import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.parttime.common.head.ActivityHead;
import com.parttime.constants.ActivityExtraAndKeys;
import com.parttime.net.DefaultCallback;
import com.parttime.net.GroupSettingRequest;
import com.parttime.utils.SharePreferenceUtil;
import com.parttime.utils.ValidateUtils;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.utils.NetWorkCheck;
import com.quark.volley.VolleySington;

public class SendAdmitToEmailActivity extends BaseActivity implements View.OnClickListener {


    private final String EMAIL_KEY = "send_to_email";

    private EditText email;
    private Button done;

    private String groupId;

    private SharePreferenceUtil sp = SharePreferenceUtil.getInstance(ApplicationControl.getInstance());

    protected RequestQueue queue = VolleySington.getInstance().getRequestQueue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_admit_to_email);
        ActivityHead headView = new ActivityHead(this);
        headView.setCenterTxt1(R.string.send_approve_username_to_email);

        email = (EditText)findViewById(R.id.email);
        done = (Button)findViewById(R.id.done);

        groupId = getIntent().getStringExtra(ActivityExtraAndKeys.GroupSetting.GROUPID);

        String emailValue = sp.loadStringSharedPreference(EMAIL_KEY);
        if(! TextUtils.isEmpty(emailValue)){
            email.setText(emailValue);
        }

        done.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.done:
                Editable emailStr = email.getText();
                sendAdmitUserToEmail(emailStr.toString());
                break;
        }
    }

    private void sendAdmitUserToEmail(String emailStr) {


        if(! ValidateUtils.isEmail(emailStr)){
            Toast.makeText(getApplicationContext(), getString(R.string.format_error), Toast.LENGTH_SHORT).show();
            return ;
        }

        sp.saveSharedPreferences(EMAIL_KEY,emailStr);
        EMGroup emGroup = EMGroupManager.getInstance().getGroup(groupId);
        String groupName ;
        if(emGroup != null){
            groupName = emGroup.getGroupName();
        }else {
            groupName = "temp";
        }


        new GroupSettingRequest().sendAdmitUserToEmail(emailStr,groupId,groupName, queue , new DefaultCallback(){
            @Override
            public void success(Object obj) {
                super.success(obj);
                Toast.makeText(SendAdmitToEmailActivity.this, R.string.already_send_to_email,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failed(Object obj) {
                super.failed(obj);
                Toast.makeText(SendAdmitToEmailActivity.this, R.string.send_to_email_failed ,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
