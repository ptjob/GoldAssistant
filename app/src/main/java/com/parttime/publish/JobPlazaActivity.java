package com.parttime.publish;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.VolleyError;
import com.parttime.common.head.ActivityHead;
import com.parttime.net.DefaultCallback;
import com.parttime.net.PublishRequest;
import com.parttime.net.ResponseBaseCommonError;
import com.parttime.publish.adapter.JobPlazaListAdapter;
import com.parttime.publish.vo.JobPlazaActivityListVo;
import com.parttime.utils.IntentManager;
import com.parttime.widget.BaseXListView;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;

import me.maxwin.view.XListView;

/**
 * 兼职广场
 * Created by wyw on 2015/8/2.
 */
public class JobPlazaActivity extends BaseActivity implements AdapterView.OnItemClickListener, XListView.IXListViewListener {

    public static final int PAGE_COUNT = 20;

    private BaseXListView mListViewMain;
    private JobPlazaListAdapter mAdapterMain;
    private JobPlazaActivityListVo mCurrentVo;

    private DefaultCallback mDefaultCallback = new DefaultCallback() {
        @Override
        public void success(Object obj) {
            showWait(false);
            mCurrentVo = (JobPlazaActivityListVo) obj;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mCurrentVo.pageNumber == 1) {
                        mAdapterMain.setAll(mCurrentVo.jobPlazaListVoList);
                        mListViewMain.updateRefreshTime();
                    } else {
                        mAdapterMain.addAll(mCurrentVo.jobPlazaListVoList);
                    }

                    mListViewMain.setLoadOver(mCurrentVo.jobPlazaListVoList.size(), PAGE_COUNT);
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
        setContentView(R.layout.activity_job_plaza);
        initControls();
        bindListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindData();
    }

    private void bindData() {
        mAdapterMain = new JobPlazaListAdapter(this);
        mListViewMain.setAdapter(mAdapterMain);

        refreshFirstPage();
    }

    private void bindListener() {
        mListViewMain.setOnItemClickListener(this);

        mListViewMain.setXListViewListener(this);
    }

    private void initControls() {
        mListViewMain = (BaseXListView) findViewById(R.id.listview_main);
        ActivityHead activityHead = new ActivityHead(this);
        activityHead.setCenterTxt1(R.string.job_plaza_title);

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
        new PublishRequest().plazaList(getNextPageNumber(), PAGE_COUNT, PublishRequest.PUBLISH_ACTIVITY_LIST_TYPE_UNDERCARRIAGE, queue, mDefaultCallback);
    }

    // 获取下一页页码
    private int getNextPageNumber() {
        return mCurrentVo == null ? 1 : mCurrentVo.pageNumber + 1;
    }

    @Override
    public void setBackButton() {
        super.setBackButton();
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
    public void onRefresh() {
        refreshFirstPage();
    }

    @Override
    public void onLoadMore() {
        loadMore();
    }

}
