package com.parttime.publish;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.parttime.common.activity.ChooseListActivity;
import com.parttime.common.head.ActivityHead;
import com.parttime.net.BaseRequest;
import com.parttime.net.Callback;
import com.parttime.pojo.PartJob;
import com.parttime.pojo.SalaryUnit;
import com.parttime.utils.CheckUtils;
import com.parttime.utils.FormatUtils;
import com.parttime.utils.IntentManager;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.ui.widget.ActionSheet;
import com.quark.volley.VolleySington;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

/**
 * 发布兼职页
 */
public class WriteJobActivity extends BaseActivity implements
        View.OnClickListener, DialogInterface.OnCancelListener,
        ActionSheet.OnActionSheetSelected {

    public static final String EXTRA_TYPE = "type";
    private static final int REQUEST_TXT_SELECT = 0x1001;

    private String type;
    private ActivityHead activityHead;
    private TextView mTxtMoreRequire;
    private TextView mTxtBeginTime, mTxtEndTime, mTxtPayType, mTxtWorkArea;
    private TextView mTxtSalaryUnit, mTxtLanguage, mTxtHeight, mTxtMeasurements;
    private TextView mTxtWorkRequireTip;
    private EditText mEditJobTitle, mEditWorkAddress, mEditSalary;
    private EditText mEditHeadSum, mEditMaleNum, mEditFemaleNum;
    private EditText mEditWorkRequire;
    private LinearLayout mLLHeadSumContainer, mLLSexContainer, mLLMoreRequireContainer;
    private RadioButton mRadioSexUnlimited, mRadioSexLimited;
    private RadioButton mRadioUnShowTel, mRadioShowTel;
    private RadioButton mRadioNeedHealthProve, mRadioUnNeedHealthProve;
    private Button mBtnPublish;

    private TextView mTxtSelectTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_job);
        initIntent();
        initControls();
        bindListener();
        bindData();
    }

    private void bindData() {
    }

    private void bindListener() {
        activityHead.setRightTxtOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyForm()) {
                    preview();
                }
            }
        });

        mTxtBeginTime.setOnClickListener(this);
        mTxtEndTime.setOnClickListener(this);
        mTxtPayType.setOnClickListener(this);
        mTxtWorkArea.setOnClickListener(this);
        mTxtSalaryUnit.setOnClickListener(this);
        mTxtLanguage.setOnClickListener(this);
        mTxtHeight.setOnClickListener(this);
        mTxtMeasurements.setOnClickListener(this);

        mTxtMoreRequire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMoreRequire();
            }
        });

        mEditWorkRequire.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 改变工作要求右下角的字数提示
                mTxtWorkRequireTip.setText(getString(R.string.publish_job_tip_work_require, editable.length()));
            }
        });

        mRadioSexUnlimited.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toggleSex();
            }
        });

        mRadioSexLimited.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        mBtnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyForm()) {
                    publish();
                }
            }
        });
    }


    private void initIntent() {
        type = getIntent().getStringExtra(EXTRA_TYPE);
    }

    private void initControls() {
        activityHead = new ActivityHead();
        activityHead.initHead(this);
        activityHead.setRightTxt(R.string.preview);
        activityHead.setCenterTxt1(type);

        mTxtMoreRequire = (TextView) findViewById(R.id.txt_more_require);

        mTxtBeginTime = (TextView) findViewById(R.id.txt_begin_time);
        mTxtEndTime = (TextView) findViewById(R.id.txt_end_time);
        mTxtPayType = (TextView) findViewById(R.id.txt_pay_type);
        mTxtWorkArea = (TextView) findViewById(R.id.txt_work_area);
        mTxtSalaryUnit = (TextView) findViewById(R.id.txt_salary_unit);
        mTxtLanguage = (TextView) findViewById(R.id.txt_language);
        mTxtHeight = (TextView) findViewById(R.id.txt_height);
        mTxtMeasurements = (TextView) findViewById(R.id.txt_measurements);
        mTxtWorkRequireTip = (TextView) findViewById(R.id.edittxt_work_require_tip);

        mEditJobTitle = (EditText) findViewById(R.id.edittxt_job_title);
        mEditWorkAddress = (EditText) findViewById(R.id.edittxt_work_address);
        mEditSalary = (EditText) findViewById(R.id.edittxt_salary);
        mEditHeadSum = (EditText) findViewById(R.id.edittxt_head_sum);
        mEditMaleNum = (EditText) findViewById(R.id.edittxt_male_num);
        mEditFemaleNum = (EditText) findViewById(R.id.edittxt_female_num);
        mEditWorkRequire = (EditText) findViewById(R.id.edittxt_work_require_content);

        mLLHeadSumContainer = (LinearLayout) findViewById(R.id.ll_head_sum_container);
        mLLSexContainer = (LinearLayout) findViewById(R.id.ll_sex_container);
        mLLMoreRequireContainer = (LinearLayout) findViewById(R.id.ll_more_require_container);

        mRadioSexUnlimited = (RadioButton) findViewById(R.id.radio_sex_unlimited);
        mRadioSexLimited = (RadioButton) findViewById(R.id.radio_sex_limited);
        mRadioUnShowTel = (RadioButton) findViewById(R.id.radio_unshow_tel);
        mRadioShowTel = (RadioButton) findViewById(R.id.radio_show_tel);
        mRadioNeedHealthProve = (RadioButton) findViewById(R.id.radio_need_health_prove);
        mRadioUnNeedHealthProve = (RadioButton) findViewById(R.id.radio_unneed_health_prove);

        mBtnPublish = (Button) findViewById(R.id.btn_publish);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_begin_time:
                ActionSheet.showSheetTime4(this, this, this,
                        mTxtBeginTime);
                break;
            case R.id.txt_end_time:
                ActionSheet.showSheetTime4(this, this, this,
                        mTxtEndTime);
                break;
            case R.id.txt_pay_type:
                mTxtSelectTemp = mTxtPayType;
                String[] payTypeArray = getResources().getStringArray(R.array.pay_type);
                IntentManager.openChoooseListActivity(WriteJobActivity.this,
                        getString(R.string.publish_job_label_pay_type),
                        payTypeArray, REQUEST_TXT_SELECT);
                break;
            case R.id.txt_work_area:
                mTxtSelectTemp = mTxtWorkArea;
                String[] workAreaArray = new String[]{"南山区", "罗湖区", "福田区", "宝安区"};
                IntentManager.openChoooseListActivity(WriteJobActivity.this,
                        getString(R.string.publish_job_label_work_area),
                        workAreaArray, REQUEST_TXT_SELECT);
                break;
            case R.id.txt_salary_unit:
                mTxtSelectTemp = mTxtSalaryUnit;
                String[] salaryUnitArray = getResources().getStringArray(R.array.salary_unit);
                IntentManager.openChoooseListActivity(WriteJobActivity.this,
                        getString(R.string.publish_job_label_salary_unit),
                        salaryUnitArray, REQUEST_TXT_SELECT);
                break;
            case R.id.txt_language:
                ActionSheet.showSheetLanguage(this, this, this,
                        mTxtLanguage);
                break;
            case R.id.txt_height:
                break;
            case R.id.txt_measurements:
                break;
            default:
                break;
        }
    }

    @Override
    public void setBackButton() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TXT_SELECT:
                    if (data != null) {
                        String result = data.getStringExtra(ChooseListActivity.EXTRA_RESULT);
                        if (mTxtSelectTemp != null) {
                            mTxtSelectTemp.setText(result);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 切换是否指定性别
     */
    private void toggleSex() {
        if (mRadioSexUnlimited.isChecked()) {
            mLLHeadSumContainer.setVisibility(View.VISIBLE);
            mLLSexContainer.setVisibility(View.GONE);
        } else if (mRadioSexLimited.isChecked()) {
            mLLHeadSumContainer.setVisibility(View.GONE);
            mLLSexContainer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 切换更多要求
     */
    private void toggleMoreRequire() {
        if (mLLMoreRequireContainer.getVisibility() == View.GONE) {
            mLLMoreRequireContainer.setVisibility(View.VISIBLE);
            Drawable drawable = getResources().getDrawable(
                    R.drawable.other_btn_off);
            mTxtMoreRequire.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

        } else {
            mLLMoreRequireContainer.setVisibility(View.GONE);
            Drawable drawable = getResources().getDrawable(
                    R.drawable.other_btn_on);
            mTxtMoreRequire.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
    }


    @Override
    public void onCancel(DialogInterface dialogInterface) {

    }

    @Override
    public void onClick(int whichButton) {

    }

    private boolean verifyForm() {
        String verifyStr;
        verifyStr = mEditJobTitle.getText().toString();
        if (CheckUtils.isEmpty(verifyStr)) {
            showToast(getString(R.string.publish_job_form_uninput_warn_format, getString(R.string.publish_job_label_job_title)));
            return false;
        }

        String beginTime;
        beginTime = verifyStr = mTxtBeginTime.getText().toString();
        if (CheckUtils.isEmpty(verifyStr)) {
            showToast(getString(R.string.publish_job_form_uninput_warn_format, getString(R.string.publish_job_label_begin_time)));
            return false;
        }

        String endTime;
        endTime = verifyStr = mTxtEndTime.getText().toString();
        if (CheckUtils.isEmpty(verifyStr)) {
            showToast(getString(R.string.publish_job_form_uninput_warn_format, getString(R.string.publish_job_label_begin_time)));
            return false;
        }

        verifyStr = mTxtPayType.getText().toString();
        if (CheckUtils.isEmpty(verifyStr)) {
            showToast(getString(R.string.publish_job_form_uninput_warn_format, getString(R.string.publish_job_label_pay_type)));
            return false;
        }

        verifyStr = mTxtWorkArea.getText().toString();
        if (CheckUtils.isEmpty(verifyStr)) {
            showToast(getString(R.string.publish_job_form_uninput_warn_format, getString(R.string.publish_job_label_work_area)));
            return false;
        }

        verifyStr = mEditWorkAddress.getText().toString();
        if (CheckUtils.isEmpty(verifyStr) ||
                verifyStr.length() < getResources().getInteger(R.integer.work_address_min_length)) {
            showToast(R.string.publish_job_hint_work_address);
            return false;
        }

        verifyStr = mEditSalary.getText().toString();
        if (CheckUtils.isEmpty(verifyStr)) {
            showToast(getString(R.string.publish_job_form_uninput_warn_format, getString(R.string.publish_job_label_salary)));
            return false;
        } else {
            try {
                Integer.parseInt(verifyStr);
            } catch (NumberFormatException e) {
                showToast(getString(R.string.publish_job_form_salary_format_error));
                return false;
            }
        }

        verifyStr = mTxtSalaryUnit.getText().toString();
        if (CheckUtils.isEmpty(verifyStr)) {
            showToast(getString(R.string.publish_job_form_uninput_warn_format, getString(R.string.publish_job_label_salary_unit)));
            return false;
        }

        verifyStr = mEditWorkRequire.getText().toString();
        if (CheckUtils.isEmpty(verifyStr) ||
                verifyStr.length() < getResources().getInteger(R.integer.work_require_min_length)) {
            showToast(R.string.publish_job_hint_work_require);
            return false;
        }

        return true;
    }

    private void publish() {
       
    }

    private void preview() {
        PartJob partJob = new PartJob();
        partJob.type = type;
        partJob.title = FormatUtils.formatStr(mEditJobTitle.getText().toString());
        partJob.beginTime = FormatUtils.formatStr(mTxtBeginTime.getText().toString());
        partJob.endTime = FormatUtils.formatStr(mTxtEndTime.getText().toString());
        partJob.payType = FormatUtils.formatStr(mTxtPayType.getText().toString());
        partJob.area = FormatUtils.formatStr(mTxtWorkArea.getText().toString());
        partJob.address = FormatUtils.formatStr(mEditWorkAddress.getText().toString());
        partJob.salary = FormatUtils.parseToInt(mEditSalary.getText().toString());
        partJob.salaryUnit = SalaryUnit.parse(FormatUtils.formatStr(mTxtSalaryUnit.getText().toString()));
        partJob.apartSex = mRadioSexLimited.isChecked();
        if (partJob.apartSex) {
            String maleNumStr = mEditMaleNum.getText().toString();
            partJob.maleNum = CheckUtils.isEmpty(maleNumStr) ? 0 : FormatUtils.parseToInt(maleNumStr);

            String femaleNumStr = mEditFemaleNum.getText().toString();
            partJob.femaleNum = CheckUtils.isEmpty(femaleNumStr) ? 0 : FormatUtils.parseToInt(femaleNumStr);
        } else {
            String headSumStr = mEditHeadSum.getText().toString();
            partJob.headSum = CheckUtils.isEmpty(headSumStr) ? 0 : FormatUtils.parseToInt(headSumStr);
        }

        partJob.workRequire =  FormatUtils.formatStr(mEditWorkRequire.getText().toString());
        partJob.isShowTel = mRadioShowTel.isChecked();

        if (mRadioNeedHealthProve.isChecked() || mRadioUnNeedHealthProve.isChecked()) {
            // 选择了是否要健康证
            partJob.healthProve = mRadioNeedHealthProve.isChecked();
        }

        String language = mTxtLanguage.getText().toString();
        if (!CheckUtils.isEmpty(language)) {
            // 设置了擅长的语言
            partJob.language = language;
        }

        IntentManager.openJobDetailActivity(this, partJob);
    }
}