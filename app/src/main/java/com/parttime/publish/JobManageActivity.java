package com.parttime.publish;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;

import com.android.volley.VolleyError;
import com.parttime.common.head.ActivityHead;
import com.parttime.net.DefaultCallback;
import com.parttime.net.PublishRequest;
import com.parttime.net.ResponseBaseCommonError;
import com.parttime.publish.adapter.JobManageListAdapter;
import com.parttime.publish.vo.PublishActivityListVo;
import com.parttime.utils.IntentManager;
import com.parttime.widget.BaseXListView;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;

import me.maxwin.view.XListView;

/**
 * 兼职管理界面
 * Created by wyw on 2015/7/26.
 */
public class JobManageActivity extends BaseActivity implements AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, XListView.IXListViewListener {


    public static final int PAGE_COUNT = 20;

    private BaseXListView mListViewMain;
    private RadioButton mRadioRecruit;
    private RadioButton mRadioAuditing;
    private RadioButton mRadioUndercarriage;
    private JobManageListAdapter mAdapterMain;
    private PublishActivityListVo mCurrentVo;

    private DefaultCallback mDefaultCallback = new DefaultCallback() {
        @Override
        public void success(Object obj) {
            showWait(false);
            mCurrentVo = (PublishActivityListVo) obj;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mCurrentVo.pageNumber == 1) {
                        mAdapterMain.setAll(mCurrentVo.jobManageListVoList);
                        mListViewMain.updateRefreshTime();
                    } else {
                        mAdapterMain.addAll(mCurrentVo.jobManageListVoList);
                    }

                    mListViewMain.setLoadOver(mCurrentVo.jobManageListVoList.size(), PAGE_COUNT);
                    mListViewMain.stopRefresh();
                    mListViewMain.stopLoadMore();
                }
            });
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_manage);
        initControls();
        bindListener();
        bindData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void bindData() {
        mAdapterMain = new JobManageListAdapter(this);
        mListViewMain.setAdapter(mAdapterMain);

        mRadioRecruit.setChecked(true);
        refreshFirstPage();
    }

    private void bindListener() {
        mListViewMain.setOnItemClickListener(this);
        mRadioRecruit.setOnCheckedChangeListener(this);
        mRadioAuditing.setOnCheckedChangeListener(this);
        mRadioUndercarriage.setOnCheckedChangeListener(this);

        mListViewMain.setXListViewListener(this);
    }

    private void initControls() {
        mListViewMain = (BaseXListView) findViewById(R.id.listview_main);
        mRadioRecruit = (RadioButton) findViewById(R.id.radio_recruit);
        mRadioAuditing = (RadioButton) findViewById(R.id.radio_auditing);
        mRadioUndercarriage = (RadioButton) findViewById(R.id.radio_undercarriage);

        ActivityHead activityHead = new ActivityHead(this);
        activityHead.setCenterTxt1(R.string.job_manage_title);

    }

    @Override
    public void setBackButton() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int position = i - 1;
        if (position < mAdapterMain.getCount()) {
            long jobId = mAdapterMain.getItemId(position);
            IntentManager.openJobDetailActivity(this, (int) jobId, "");
        } else {
            showToast(R.string.error_date_and_refresh);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        refreshFirstPage();
    }

    // 刷新第一页
    private void refreshFirstPage() {
        mCurrentVo = null;
        showWait(true);
        refreshListView();
    }

    private void loadMore() {
        refreshListView();
    }

    private void refreshListView() {
        if (mRadioRecruit.isChecked()) {
            // 招人中
            new PublishRequest().publishActivityList(getNextPageNumber(), PAGE_COUNT, PublishRequest.PUBLISH_ACTIVITY_LIST_TYPE_RECRUIT, queue, mDefaultCallback);
        } else if (mRadioAuditing.isChecked()) {
            // 待审核
            new PublishRequest().publishActivityList(getNextPageNumber(), PAGE_COUNT, PublishRequest.PUBLISH_ACTIVITY_LIST_TYPE_AUDITING, queue, mDefaultCallback);
        } else {
            // 已下架
            new PublishRequest().publishActivityList(getNextPageNumber(), PAGE_COUNT, PublishRequest.PUBLISH_ACTIVITY_LIST_TYPE_UNDERCARRIAGE, queue, mDefaultCallback);
        }
    }

    // 获取下一页页码
    private int getNextPageNumber() {
        return mCurrentVo == null ? 1 : mCurrentVo.pageNumber + 1;
    }

    @Override
    public void onRefresh() {
        refreshFirstPage();
    }

    @Override
    public void onLoadMore() {
        loadMore();
    }


}
