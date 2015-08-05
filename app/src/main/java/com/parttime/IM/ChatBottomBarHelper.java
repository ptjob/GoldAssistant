package com.parttime.IM;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parttime.constants.ApplicationConstants;
import com.qingmu.jianzhidaren.R;

/**
 *
 * Created by dehua on 15/7/26.
 */
public class ChatBottomBarHelper implements View.OnClickListener{

    private ChatActivity activity;
    private LinearLayout normalBar, publicAccountBar;
    private Button switcherNormal;
    private FrameLayout switcherPublicAccount;
    private TextView callCustomerService,parttimeScchool;

    public ChatBottomBarHelper(ChatActivity activity){

        this.activity = activity;

        normalBar = (LinearLayout)activity.findViewById(R.id.rl_bottom);
        publicAccountBar = (LinearLayout)activity.findViewById(R.id.public_account_bar);
        switcherNormal = (Button)activity.findViewById(R.id.switcher_1);
        switcherPublicAccount = (FrameLayout)activity.findViewById(R.id.switcher_2);
        callCustomerService = (TextView)activity.findViewById(R.id.call_customer_service);
        parttimeScchool = (TextView)activity.findViewById(R.id.parttime_scchool);

        switcherNormal.setVisibility(View.VISIBLE);

        switcherNormal.setOnClickListener(this);
        switcherPublicAccount.setOnClickListener(this);
        callCustomerService.setOnClickListener(this);
        parttimeScchool.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switcher_1:
                normalBar.setVisibility(View.GONE);
                publicAccountBar.setVisibility(View.VISIBLE);
                break;
            case R.id.switcher_2:
                normalBar.setVisibility(View.VISIBLE);
                publicAccountBar.setVisibility(View.GONE);
                break;
            case R.id.call_customer_service:
                Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ ApplicationConstants.OFFICIAL_NUMBER));
                activity.startActivity(intent);
                break;
            case R.id.parttime_scchool:
                Toast.makeText(activity,"调用浏览器",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
