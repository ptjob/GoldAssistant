package com.parttime.publish;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.parttime.common.head.ActivityHead;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;

/**
 * 经纪人详情页面
 * Created by wyw on 2015/8/6.
 */
public class JobBrokerDetailActivity extends BaseActivity {

    public static final String EXTRA_COMPANY_ID = "company_id";

    private int mCompanyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_broker_detail);

        initIntent();
        initControls();


    }

    private void initControls() {

        ActivityHead activityHead = new ActivityHead(this);
        activityHead.setCenterTxt1(R.string.job_broker_detail_title);
        activityHead.setRightTxt(R.string.share);
        activityHead.setRightTxtOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        JobBrokerDetailFragment jobBrokerDetailFragment = JobBrokerDetailFragment.newInstance(mCompanyId);
        fragmentTransaction.add(R.id.ll_main, jobBrokerDetailFragment);
        fragmentTransaction.commit();
    }

    private void share() {

    }

    private void initIntent() {
        mCompanyId = getIntent().getIntExtra(EXTRA_COMPANY_ID, -1);
    }

    @Override
    public void setBackButton() {
        super.setBackButton();
    }
}
