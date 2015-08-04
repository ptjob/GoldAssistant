package com.parttime.publish;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.VolleyError;
import com.parttime.net.DefaultCallback;
import com.parttime.net.PublishRequest;
import com.parttime.net.ResponseBaseCommonError;
import com.parttime.publish.adapter.JobBrokerListAdapter;
import com.parttime.publish.vo.JobBrokerChartsFragmentVo;
import com.parttime.utils.CheckUtils;
import com.parttime.widget.BaseXListView;
import com.qingmu.jianzhidaren.R;
import com.quark.fragment.company.BaseSupportFragment;

import me.maxwin.view.XListView;

/**
 * 经纪人-排行列表
 * Created by wyw on 2015/8/4.
 */
public class JobBrokerChartsFragment extends BaseSupportFragment implements AdapterView.OnItemClickListener, XListView.IXListViewListener {

    public static final int PAGE_COUNT = 20;

    private BaseXListView mListViewMain;
    private JobBrokerListAdapter mAdapterMain;
    private JobBrokerChartsFragmentVo mCurrentVo;

    private DefaultCallback mDefaultCallback = new DefaultCallback() {
        @Override
        public void success(Object obj) {
            showWait(false);
            mCurrentVo = (JobBrokerChartsFragmentVo) obj;

            if (CheckUtils.isSafe(getActivity())) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mCurrentVo.pageNumber == 1) {
                            mAdapterMain.setAll(mCurrentVo.jobBrokerListVos);
                            mListViewMain.updateRefreshTime();
                        } else {
                            mAdapterMain.addAll(mCurrentVo.jobBrokerListVos);
                        }

                        mListViewMain.setLoadOver(mCurrentVo.jobBrokerListVos.size(), PAGE_COUNT);
                        mListViewMain.stopRefresh();
                        mListViewMain.stopLoadMore();
                    }
                });
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
    };

    public static JobBrokerChartsFragment newInstance() {
        return new JobBrokerChartsFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_broker_charts, null);
        mListViewMain = (BaseXListView) view.findViewById(R.id.listview_main);
        bindListener();
        return view;
    }

    private void bindListener() {
        mListViewMain.setOnItemClickListener(this);

        mListViewMain.setXListViewListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        bindData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void bindData() {
        mAdapterMain = new JobBrokerListAdapter(getActivity());
        mListViewMain.setAdapter(mAdapterMain);

        refreshFirstPage();
    }

    // 刷新第一页
    private void refreshFirstPage() {
        mCurrentVo = null;
        showWait(true);
        refreshListView();
    }

    private void refreshListView() {
        new PublishRequest().agentList(getNextPageNumber(), PAGE_COUNT, PublishRequest.PUBLISH_ACTIVITY_LIST_TYPE_UNDERCARRIAGE, queue, mDefaultCallback);
    }

    private void loadMore() {
        refreshListView();
    }

    // 获取下一页页码
    private int getNextPageNumber() {
        return mCurrentVo == null ? 1 : mCurrentVo.pageNumber + 1;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
