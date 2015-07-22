package com.parttime.publish;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parttime.common.head.ActivityHead;
import com.parttime.pojo.PartJob;
import com.parttime.utils.CheckUtils;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.utils.Logger;

/**
 * 活动详情页
 * Created by wyw on 2015/7/20.
 */
public class JobDetailActivity extends BaseActivity {

    enum Type {
        REVIEW
    }

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_COMPANY_ID = "company_id";
    public static final String EXTRA_PART_JOB = "part_job";


    private TextView mTxtType, mTxtTitle, mTxtSalary, mTxtCompany, mTxtWorkArea, mTxtWorkTime;
    private TextView mTxtPayType, mTxtHeadSum, mTxtWorkAddress, mTxtWorkRequire;
    private TextView mTxtHeight, mTxtMeasurements, mTxtHealthProve, mTxtLanguage;
    private LinearLayout mLLCompanyContainer, mLLMoreRequireContainer;
    private LinearLayout mLLHeightContainer, mLLMeasurementsContainer, mLLLanguageContainer, mLLHealthProveContainer;

    private int id;
    private int companyId;
    private PartJob partJob;
    private Type type;


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
        if (type == Type.REVIEW) {
            bindWithPartJob();
        }
    }

    private void bindWithPartJob() {
        Logger.i(partJob.toString());

        mTxtType.setText(partJob.type);
        mTxtTitle.setText(partJob.title);
        mTxtSalary.setText("" + partJob.salary);
        mTxtCompany.setText(partJob.companyName);
        mTxtWorkArea.setText(partJob.area);
        mTxtWorkTime.setText(getString(R.string.job_detail_work_time_format, partJob.beginTime, partJob.endTime));
        mTxtPayType.setText(partJob.payType);
        if (partJob.apartSex) {
            mTxtHeadSum.setText(getString(R.string.job_detail_apart_sex_format, partJob.maleNum, partJob.femaleNum));
        } else {
            mTxtHeadSum.setText(getString(R.string.job_detail_head_sum_format, partJob.headSum));
        }
        mTxtWorkAddress.setText(partJob.address);
        if (partJob.isHasMoreRequire()) {
            mLLMoreRequireContainer.setVisibility(View.VISIBLE);
            if (partJob.height != null) {
                mTxtHeight.setText(getString(R.string.job_detail_height_format, partJob.height));
                mLLHeightContainer.setVisibility(View.VISIBLE);
            } else {
                mLLHeightContainer.setVisibility(View.GONE);
            }

            if (partJob.isHasMeasurements()) {
                mTxtMeasurements.setText(getString(R.string.job_detail_measurements_format, partJob.bust, partJob.beltline, partJob.hipline));
                mLLMeasurementsContainer.setVisibility(View.VISIBLE);
            } else {
                mLLMeasurementsContainer.setVisibility(View.GONE);
            }

            if (partJob.healthProve != null) {
                mTxtHealthProve.setText(partJob.healthProve ? getString(R.string.need) : getString(R.string.unneed));
                mLLHealthProveContainer.setVisibility(View.VISIBLE);
            } else {
                mLLHealthProveContainer.setVisibility(View.GONE);
            }
            if (!CheckUtils.isEmpty(partJob.language)) {
                mTxtLanguage.setText(partJob.language);
                mLLLanguageContainer.setVisibility(View.VISIBLE);
            } else {
                mLLLanguageContainer.setVisibility(View.GONE);
            }

        } else {
            mLLMoreRequireContainer.setVisibility(View.GONE);
        }

        mTxtWorkRequire.setText(partJob.workRequire);
    }

    private void bindListener() {

    }

    private void initControls() {

        mTxtType = (TextView) findViewById(R.id.txt_type);
        mTxtTitle = (TextView) findViewById(R.id.txt_title);
        mTxtSalary = (TextView) findViewById(R.id.txt_salary);
        mTxtCompany = (TextView) findViewById(R.id.txt_company);
        mTxtWorkArea = (TextView) findViewById(R.id.txt_work_area);
        mTxtWorkTime = (TextView) findViewById(R.id.txt_work_time);
        mTxtPayType = (TextView) findViewById(R.id.txt_pay_type);
        mTxtHeadSum = (TextView) findViewById(R.id.txt_head_sum);
        mTxtWorkAddress = (TextView) findViewById(R.id.txt_work_address);
        mTxtWorkRequire = (TextView) findViewById(R.id.txt_work_require);
        mTxtHeight = (TextView) findViewById(R.id.txt_height);
        mTxtMeasurements = (TextView) findViewById(R.id.txt_measurements);
        mTxtHealthProve = (TextView) findViewById(R.id.txt_health_prove);
        mTxtLanguage = (TextView) findViewById(R.id.txt_language);
        mLLCompanyContainer = (LinearLayout) findViewById(R.id.ll_company_container);
        mLLMoreRequireContainer = (LinearLayout) findViewById(R.id.ll_more_require_container);
        mLLHeightContainer = (LinearLayout) findViewById(R.id.ll_height_container);
        mLLMeasurementsContainer = (LinearLayout) findViewById(R.id.ll_measurements_container);
        mLLLanguageContainer = (LinearLayout) findViewById(R.id.ll_language_container);
        mLLHealthProveContainer = (LinearLayout) findViewById(R.id.ll_health_prove_container);

        ActivityHead activityHead = new ActivityHead(this);
        activityHead.initHead(this);
        if (type == Type.REVIEW) {
            activityHead.setCenterTxt1(R.string.publish_job_preview_title);
        }
    }

    private void initIntent() {
        id = getIntent().getIntExtra(EXTRA_ID, -1);
        companyId = getIntent().getIntExtra(EXTRA_COMPANY_ID, -1);
        if (id == -1) {
            partJob = (PartJob) getIntent().getSerializableExtra(EXTRA_PART_JOB);
            type = Type.REVIEW;
        }
    }

    @Override
    public void setBackButton() {

    }
}
