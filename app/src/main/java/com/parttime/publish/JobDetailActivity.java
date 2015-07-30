package com.parttime.publish;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parttime.common.head.ActivityHead;
import com.parttime.net.DefaultCallback;
import com.parttime.net.PublishRequest;
import com.parttime.net.ResponseBaseCommonError;
import com.parttime.pojo.PartJob;
import com.parttime.pojo.SalaryUnit;
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
        REVIEW,
        DETAIL
    }

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_PART_JOB = "part_job";


    private TextView mTxtStatus, mTxtViewCount, mTxtHandCount;
    private TextView mTxtType, mTxtTitle, mTxtSalary, mTxtCompany, mTxtWorkArea, mTxtWorkTime;
    private TextView mTxtPayType, mTxtHeadSum, mTxtWorkAddress, mTxtWorkRequire;
    private TextView mTxtHeight, mTxtMeasurements, mTxtHealthProve, mTxtLanguage;
    private LinearLayout mLLDeclareContainer, mLLCompanyContainer, mLLMoreRequireContainer;
    private LinearLayout mLLHeightContainer, mLLMeasurementsContainer, mLLLanguageContainer, mLLHealthProveContainer;
    private ActivityHead activityHead;

    private int id;
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
        } else if (type == Type.DETAIL) {
            showWait(true);
            new PublishRequest().publishActivityDetail(id, queue, new DefaultCallback() {
                @Override
                public void success(Object obj) {
                    showWait(false);
                    partJob = (PartJob) obj;
                    bindWithPartJob();
                }

                @Override
                public void failed(Object obj) {
                    showWait(false);
                    ResponseBaseCommonError error = (ResponseBaseCommonError) obj;
                    showToast(error.msg);
                }
            });
        }
    }

    private void bindWithPartJob() {
        Logger.i(partJob.toString());

        mTxtType.setText(partJob.type);
        mTxtTitle.setText(partJob.title);
        if (partJob.salaryUnit == SalaryUnit.FACE_TO_FACE) {
            mTxtSalary.setText(R.string.publish_job_salary_unit_face_to_face);
        } else {
            String salaryUnit = "";
            if (partJob.salaryUnit != null) {
                switch (partJob.salaryUnit) {
                    case DAY:
                        salaryUnit = getString(R.string.publish_job_salary_unit_day);
                        break;
                    case HOUR:
                        salaryUnit = getString(R.string.publish_job_salary_unit_hour);
                        break;
                    case MONTH:
                        salaryUnit = getString(R.string.publish_job_salary_unit_month);
                        break;
                    case TIMES:
                        salaryUnit = getString(R.string.publish_job_salary_unit_times);
                        break;
                    case CASES:
                        salaryUnit = getString(R.string.publish_job_salary_unit_cases);
                        break;
                }
            }
            mTxtSalary.setText(partJob.salary + " " + salaryUnit);
        }

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
        if (type == Type.DETAIL) {
            mLLDeclareContainer.setVisibility(View.VISIBLE);
            String status = "";
            switch (partJob.jobAuthType) {
                case DELETE:
                case FAIL_TO_PASS:
                    status = getString(R.string.job_detail_status_fail);
                    break;
                case PASS:
                    if (partJob.isStart) {
                        status = getString(R.string.job_detail_status_start);
                    } else {
                        status = getString(R.string.job_detail_status_pass);
                    }
                    break;
                case READY_TO_PASS:
                    if (partJob.isStart) {
                        status = getString(R.string.job_detail_status_start);
                    } else {
                        status = getString(R.string.job_detail_status_ready);
                    }
                    break;
            }
            mTxtStatus.setText(status);

            mTxtViewCount.setText(getString(R.string.job_detail_view_count_format, partJob.viewCount));
            mTxtHandCount.setText(getString(R.string.job_detail_hand_count_format, partJob.handCount));
        } else {
            mLLDeclareContainer.setVisibility(View.GONE);
        }
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
        activityHead.setRightTxtOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });
    }



    private void initControls() {

        mTxtStatus = (TextView) findViewById(R.id.txt_job_status);
        mTxtViewCount = (TextView) findViewById(R.id.txt_view_count);
        mTxtHandCount = (TextView) findViewById(R.id.txt_hand_count);

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
        mLLDeclareContainer = (LinearLayout) findViewById(R.id.ll_job_declare_container);
        mLLCompanyContainer = (LinearLayout) findViewById(R.id.ll_company_container);
        mLLMoreRequireContainer = (LinearLayout) findViewById(R.id.ll_more_require_container);
        mLLHeightContainer = (LinearLayout) findViewById(R.id.ll_height_container);
        mLLMeasurementsContainer = (LinearLayout) findViewById(R.id.ll_measurements_container);
        mLLLanguageContainer = (LinearLayout) findViewById(R.id.ll_language_container);
        mLLHealthProveContainer = (LinearLayout) findViewById(R.id.ll_health_prove_container);

        activityHead = new ActivityHead(this);
        activityHead.initHead(this);
        if (type == Type.REVIEW) {
            activityHead.setCenterTxt1(R.string.publish_job_preview_title);
        } else if (type == Type.DETAIL) {
            activityHead.setCenterTxt1(R.string.publish_job_detail_title);
            activityHead.setRightTxt(R.string.share);
        }
    }

    private void initIntent() {
        id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id == -1) {
            partJob = (PartJob) getIntent().getSerializableExtra(EXTRA_PART_JOB);
            type = Type.REVIEW;
        } else {
            type = Type.DETAIL;
        }
    }

    @Override
    public void setBackButton() {

    }

    // 点击分享按钮触发
    private void share() {
        showToast("分享");
    }
}
