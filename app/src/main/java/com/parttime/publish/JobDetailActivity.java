package com.parttime.publish;

import android.os.Bundle;

import com.parttime.common.head.ActivityHead;
import com.parttime.pojo.PartJob;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;

/**
 * 活动详情页
 * Created by wyw on 2015/7/20.
 */
public class JobDetailActivity extends BaseActivity {


    public static final String EXTRA_ID = "id";
    public static final String EXTRA_COMPANY_ID = "company_id";
    public static final String EXTRA_PART_JOB = "part_job";

    private int id;
    private int companyId;
    private PartJob partJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        initIntent();
        initControls();
        bindListener();
        bindData();
    }

    private void bindData() {
        if (partJob != null) {
            showToast(partJob.toString());
        }
    }

    private void bindListener() {

    }

    private void initControls() {
        ActivityHead activityHead = new ActivityHead();
        activityHead.initHead(this);
        if (partJob != null) {
            activityHead.setCenterTxt1(R.string.publish_job_preview_title);
        }
    }

    private void initIntent() {
        id = getIntent().getIntExtra(EXTRA_ID, -1);
        companyId = getIntent().getIntExtra(EXTRA_COMPANY_ID, -1);
        if (id == -1) {
            partJob = (PartJob) getIntent().getSerializableExtra(EXTRA_PART_JOB);
        }
    }

    @Override
    public void setBackButton() {

    }
}
