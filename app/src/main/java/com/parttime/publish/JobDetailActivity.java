package com.parttime.publish;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.parttime.common.head.ActivityHead;
import com.parttime.net.DefaultCallback;
import com.parttime.net.PublishRequest;
import com.parttime.net.ResponseBaseCommonError;
import com.parttime.pojo.PartJob;
import com.parttime.pojo.SalaryUnit;
import com.parttime.utils.CheckUtils;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.ui.widget.CustomDialog;
import com.quark.ui.widget.CustomDialogThree;
import com.quark.utils.Logger;
import com.thirdparty.alipay.RechargeActivity;

import org.json.JSONException;
import org.json.JSONObject;

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
    private TextView mTxtRefreshOrExpedited;

    private ImageView mImgViRefreshOrExpedited;

    private LinearLayout mLLDeclareContainer, mLLCompanyContainer, mLLMoreRequireContainer, mLLActionContainer;
    private LinearLayout mLLHeightContainer, mLLMeasurementsContainer, mLLLanguageContainer, mLLHealthProveContainer;
    private LinearLayout mLLJobRefreshOrExpedited, mLLJobModify, mLLJobShelves;

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
                    if (obj instanceof ResponseBaseCommonError) {
                        ResponseBaseCommonError error = (ResponseBaseCommonError) obj;
                        showToast(error.msg);
                    } else if (obj instanceof VolleyError) {
                        VolleyError error = (VolleyError) obj;
                        showToast(error.getMessage());
                    }
                }
            });
        }
    }


    private void bindListener() {
        activityHead.setRightTxtOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });
        mLLJobRefreshOrExpedited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTxtRefreshOrExpedited.getText().equals(getString(R.string.refresh))) {
                    jobRefresh();
                } else {
                    jobExpedited();
                }
            }
        });

        mLLMoreRequireContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jobModify();
            }
        });

        mLLJobShelves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jobShelves();
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
        mTxtRefreshOrExpedited = (TextView) findViewById(R.id.txt_refresh_or_expedited);
        mImgViRefreshOrExpedited = (ImageView) findViewById(R.id.imgvi_refresh_or_expedited);
        mLLDeclareContainer = (LinearLayout) findViewById(R.id.ll_job_declare_container);
        mLLCompanyContainer = (LinearLayout) findViewById(R.id.ll_company_container);
        mLLMoreRequireContainer = (LinearLayout) findViewById(R.id.ll_more_require_container);
        mLLActionContainer = (LinearLayout) findViewById(R.id.ll_job_action_container);
        mLLHeightContainer = (LinearLayout) findViewById(R.id.ll_height_container);
        mLLMeasurementsContainer = (LinearLayout) findViewById(R.id.ll_measurements_container);
        mLLLanguageContainer = (LinearLayout) findViewById(R.id.ll_language_container);
        mLLHealthProveContainer = (LinearLayout) findViewById(R.id.ll_health_prove_container);
        mLLJobRefreshOrExpedited = (LinearLayout) findViewById(R.id.ll_job_refresh_or_expedited);
        mLLJobModify = (LinearLayout) findViewById(R.id.ll_job_mofify);
        mLLJobShelves = (LinearLayout) findViewById(R.id.ll_job_shelves);

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
            // 显示 活动信息框 和 下面操作框
            mLLDeclareContainer.setVisibility(View.VISIBLE);
            mLLActionContainer.setVisibility(View.VISIBLE);

            String status = "";
            switch (partJob.jobAuthType) {
                case DELETE:
                case FAIL_TO_PASS:
                    status = getString(R.string.job_detail_status_fail);
                    mLLJobShelves.setEnabled(false);

                    // 如果活动结束，则按钮显示为加急，且为不可按状态
                    swtichRefreshOrExpedited(false, false);
                    switchModify(true);
                    switchShelve(false);
                    break;
                case PASS:
//                    if (partJob.isStart) {
//                        status = getString(R.string.job_detail_status_start);
//                    } else {
//                        status = getString(R.string.job_detail_status_pass);
//                    }
                    status = getString(R.string.job_detail_status_pass);
                    if (partJob.isStart) {
                        // 如果活动已经开始，刷新按钮则为加急按钮。
                        swtichRefreshOrExpedited(false, true);
                    } else {
                        // 如果活动未开始，则为刷新按钮。
                        swtichRefreshOrExpedited(true, true);
                    }
                    switchModify(true);
                    switchShelve(true);
                    break;
                case READY_TO_PASS:
                    status = getString(R.string.job_detail_status_ready);
                    if (partJob.isStart) {
                        // 如果活动已经开始，刷新按钮则为加急按钮，不可点击。
                        swtichRefreshOrExpedited(false, false);
                    } else {
                        // 如果活动未开始，则为刷新按钮，不可点击。
                        swtichRefreshOrExpedited(true, false);
                    }
                    switchModify(false);
                    switchShelve(false);
                    break;
            }
            mTxtStatus.setText(status);
            mTxtViewCount.setText(getString(R.string.job_detail_view_count_format, partJob.viewCount));
            mTxtHandCount.setText(getString(R.string.job_detail_hand_count_format, partJob.handCount));

        } else {
            // 同时隐藏活动信息和下面操作框
            mLLDeclareContainer.setVisibility(View.GONE);
            mLLActionContainer.setVisibility(View.GONE);
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

    private void swtichRefreshOrExpedited(boolean isRefresh, boolean isEnable) {
        if (isRefresh) {
            mTxtRefreshOrExpedited.setText(R.string.refresh);
            mImgViRefreshOrExpedited.setImageDrawable(getResources().getDrawable(R.drawable.refresh));
        } else {
            mTxtRefreshOrExpedited.setText(R.string.expedited);
            mImgViRefreshOrExpedited.setImageDrawable(getResources().getDrawable(R.drawable.expired));
        }

        mLLJobRefreshOrExpedited.setEnabled(isEnable);
    }

    private void switchShelve(boolean isEnable) {
        mLLJobShelves.setEnabled(isEnable);
    }

    private void switchModify(boolean isEnable) {
        mLLJobModify.setEnabled(isEnable);
    }

    // 点击分享按钮触发
    private void share() {
        showToast("分享");
    }

    private void jobRefresh() {
        showWait(true);
        new PublishRequest().preRefresh(partJob.id, queue, new DefaultCallback() {
            @Override
            public void success(Object obj) {
                showWait(false);
                try {
                    JSONObject jss = (JSONObject) obj;
                    int status = jss.getInt("status");
                    String msg = jss.getString("msg");
                    String titile = jss.getString("title");
                    String other = jss.getString("confirm");
                    String cancle = jss.getString("cancel");

                    if (status == 1) {
                        // 状态1是当前可以免费刷新一次
                        refreshNow();
                    } else if (status == 2) {
                        String money = jss.getString("money");
                        // 状态2是当前免费刷新次数用完,余额不足
                        showFeeRefreshAlertDialog(msg, titile, other,
                                cancle, money, String.valueOf(status));
                    } else if (status == 3) {
                        String money = jss.getString("money");
                        // 状态3表示当前免费刷新次数用完,有可用余额
                        showFeeRefreshAlertDialog(msg, titile, other,
                                cancle, money, String.valueOf(status));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failed(Object obj) {
                showWait(false);
                if (obj instanceof ResponseBaseCommonError) {
                    ResponseBaseCommonError error = (ResponseBaseCommonError) obj;
                    showToast(error.msg);
                } else if (obj instanceof VolleyError) {
                    VolleyError error = (VolleyError) obj;
                    showToast(error.getMessage());
                }
            }
        });
    }

    private void jobExpedited() {
        showWait(true);
        new PublishRequest().preUrgent(partJob.id, queue, new DefaultCallback() {
            @Override
            public void success(Object obj) {
                showWait(false);
                try {
                    JSONObject jss = (JSONObject) obj;
                    int status = jss.getInt("status");

                    String msg = jss.getString("msg");
                    String titile = jss.getString("title");
                    String other = jss.getString("confirm");
                    String cancle = jss.getString("cancel");
                    if (status == 1) {
                        // 状态1是当前可以免费加急一次
                        expeditedNow();
                    } else if (status == 2) {
                        String money = jss.getString("money");
                        // 状态2是当前免费加急次数用完,余额不足
                        showFeeExpeditedAlertDialog(msg, titile, other,
                                cancle, money, String.valueOf(status));
                    } else if (status == 3) {
                        String money = jss.getString("money");
                        // 状态3表示当前免费加急次数用完,有可用余额
                        showFeeExpeditedAlertDialog(msg, titile, other,
                                cancle, money, String.valueOf(status));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failed(Object obj) {
                showWait(false);
                if (obj instanceof ResponseBaseCommonError) {
                    ResponseBaseCommonError error = (ResponseBaseCommonError) obj;
                    showToast(error.msg);
                } else if (obj instanceof VolleyError) {
                    VolleyError error = (VolleyError) obj;
                    showToast(error.getMessage());
                }
            }
        });
    }

    private void jobModify() {

    }

    private void jobShelves() {
        showWait(true);
        new PublishRequest().shelve(partJob.id, queue, new DefaultCallback() {
            @Override
            public void success(Object obj) {
                showWait(false);
                showToast("下架成功");
                bindData();
            }

            @Override
            public void failed(Object obj) {
                showWait(false);
                if (obj instanceof ResponseBaseCommonError) {
                    ResponseBaseCommonError error = (ResponseBaseCommonError) obj;
                    showToast(error.msg);
                } else if (obj instanceof VolleyError) {
                    VolleyError error = (VolleyError) obj;
                    showToast(error.getMessage());
                }
            }
        });
    }


    /**
     * 刷新活动时弹框
     */
    public void showRefreshAlertDialog(String str, final String str2,
                                       final String str3, final String str4, final String flag) {

        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle(str2);
        builder.setMessage(str);
        builder.setPositiveButton(str3, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 2是立即充值,其它是刷新
                if ("2".equals(flag)) {
                    Intent intent = new Intent();
                    intent.setClass(JobDetailActivity.this,
                            RechargeActivity.class);
                    startActivity(intent);
                } else {
                    refreshNow();
                }
            }
        });

        builder.setNegativeButton(str4, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    /**
     * 刷新付费活动时弹框
     */

    public void showFeeRefreshAlertDialog(String message, final String title,
                                          final String positive, final String negative, String money,
                                          final String flag) {

        CustomDialogThree.Builder builder = new CustomDialogThree.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setMoney("(帐号余额:" + money + "元)");
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 2是立即充值,其它是刷新
                if ("2".equals(flag)) {
                    Intent intent = new Intent();
                    intent.setClass(JobDetailActivity.this,
                            RechargeActivity.class);
                    startActivity(intent);
                } else {
                    refreshNow();
                }
            }
        });

        builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    /**
     * 加急付费活动时弹框
     */

    public void showFeeExpeditedAlertDialog(String message, final String title,
                                          final String positive, final String negative, String money,
                                          final String flag) {

        CustomDialogThree.Builder builder = new CustomDialogThree.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setMoney("(帐号余额:" + money + "元)");
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 2是立即充值,其它是加急
                if ("2".equals(flag)) {
                    Intent intent = new Intent();
                    intent.setClass(JobDetailActivity.this,
                            RechargeActivity.class);
                    startActivity(intent);
                } else {
                    expeditedNow();
                }
            }
        });

        builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    /**
     * 刷新兼职信息
     */
    private void refreshNow() {
        showWait(true);
        new PublishRequest().refresh(partJob.id, queue, new DefaultCallback() {
            @Override
            public void success(Object obj) {
                showWait(false);
                try {
                    JSONObject js = (JSONObject) obj;
                    int status = js.getInt("status");
                    if (status == 1) {
                        // 状态1是当前可以免费刷新一次
                        showAlertDialog(
                                "您已成功刷新兼职，要记得把活动分享出去哦，让更多的人来报名吧",
                                "刷新成功", "我知道了");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Object obj) {
                showWait(false);
                if (obj instanceof ResponseBaseCommonError) {
                    ResponseBaseCommonError error = (ResponseBaseCommonError) obj;
                    showToast(error.msg);
                } else if (obj instanceof VolleyError) {
                    VolleyError error = (VolleyError) obj;
                    showToast(error.getMessage());
                }
            }
        });
    }

    /**
     * 进入加急流程
     */
    private void expeditedNow() {
        Intent intent = new Intent(this, JobExpeditedActivity.class);
        startActivity(intent);
    }

    /**
     * 弹出选择矿
     */
    public void showAlertDialog(String str, final String str2, String str3) {

        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle(str2);
        builder.setMessage(str);
        builder.setPositiveButton(str3, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (str2.equals("刷新失败")) {
                    Intent intent = new Intent();
                    intent.setClass(JobDetailActivity.this,
                            RechargeActivity.class);
                    startActivity(intent);
                }
            }
        });
        if ("立即充值".equals(str3)) {
            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
        }
        builder.create().show();
    }
}
