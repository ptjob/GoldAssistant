package com.parttime.publish;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.parttime.common.activity.ChooseListActivity;
import com.parttime.common.head.ActivityHead;
import com.parttime.utils.IntentManager;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;

public class WriteJobActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_TYPE = "type";
    private static final int REQUEST_TXT_SELECT = 0x1001;

    private String type;
    private TextView mTxtMoreRequire;
    private TextView  mTxtBeginTime, mTxtEndTime, mTxtPayType, mTxtWorkArea;
    private TextView mTxtSalaryUnit, mTxtLanguage, mTxtHeight, mTxtMeasurements;
    private EditText mEditJobTitle;
    private LinearLayout mLLSexContainer,  mLLMoreRequireContainer;
    private RadioButton mRadioSexUnlimited, mRadioSexLimited;

    private TextView mTxtSelectTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_job);
        initIntent();
        initControls();
        bindListener();
    }

    private void bindListener() {
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
    }



    private void initIntent() {
        type = getIntent().getStringExtra(EXTRA_TYPE);
    }

    private void initControls() {
        ActivityHead activityHead = new ActivityHead();
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

        mEditJobTitle = (EditText) findViewById(R.id.edittxt_job_title);

        mLLSexContainer = (LinearLayout) findViewById(R.id.ll_sex_container);
        mLLMoreRequireContainer = (LinearLayout) findViewById(R.id.ll_more_require_container);
        mRadioSexUnlimited = (RadioButton) findViewById(R.id.radio_sex_unlimited);
        mRadioSexLimited = (RadioButton) findViewById(R.id.radio_sex_limited);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_begin_time:
                break;
            case R.id.txt_end_time:
                break;
            case R.id.txt_pay_type:
                mTxtSelectTemp = mTxtPayType;
                String[] payTypeArray = getResources().getStringArray(R.array.pay_type);
                IntentManager.openChoooseListActivity(WriteJobActivity.this,
                        getString(R.string.publish_job_label_pay_type),
                        payTypeArray, REQUEST_TXT_SELECT);
                break;
            case R.id.txt_work_area:
                break;
            case R.id.txt_salary_unit:
                break;
            case R.id.txt_language:
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
                        mTxtSelectTemp.setText(result);
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
            mLLSexContainer.setVisibility(View.GONE);
        } else if (mRadioSexLimited.isChecked()) {
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


}