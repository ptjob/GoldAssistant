package com.parttime.publish;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.parttime.common.Image.ContactImageLoader;
import com.parttime.net.DefaultCallback;
import com.parttime.net.ErrorHandler;
import com.parttime.net.PublishRequest;
import com.parttime.net.ResponseBaseCommonError;
import com.parttime.net.UserDetailRequest;
import com.parttime.pojo.AccountInfo;
import com.parttime.publish.adapter.JobManageListAdapter;
import com.parttime.publish.vo.PublishActivityListVo;
import com.parttime.utils.CheckUtils;
import com.parttime.utils.IntentManager;
import com.parttime.widget.BaseXListView;
import com.parttime.widget.RankView;
import com.qingmu.jianzhidaren.R;
import com.quark.fragment.company.BaseSupportFragment;
import com.quark.http.image.CircularImage;
import com.quark.utils.Logger;

import me.maxwin.view.XListView;

/**
 * 经纪人-详情 Fragment
 * Created by wyw on 2015/8/4.
 */
public class JobBrokerDetailFragment extends BaseSupportFragment implements AdapterView.OnItemClickListener, XListView.IXListViewListener {

    public static final int PAGE_COUNT = 20;

    public static final String KEY_COMPANY_ID = "companyId";

    private static final int TAB_JOB = 0;
    private static final int TAB_INTRO = 1;


    private int mCompanyId;
    private CircularImage mImgViHead;
    private ImageView mImgViAuth;
    private TextView mTxtCompanyName, mTxtHireType, mTxtIntro;
    private RankView mRankViPoint;
    private BaseXListView mListJob;
    private TextView mTxtTabIntro, mTxtTabJob;
    private ImageView mImgViTabIntro, mImgViTabJob;


    private AccountInfo mCompanyInfo;
    private PublishActivityListVo mCurrentVo;
    private JobManageListAdapter mAdapterMain;

    private DefaultCallback showCompanyCallback = new DefaultCallback() {
        @Override
        public void success(Object obj) {
            if (obj instanceof AccountInfo) {
                showWait(false);
                mCompanyInfo = (AccountInfo) obj;
                if (CheckUtils.isSafe(getActivity())) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bindCompanyInfo();
                        }
                    });
                }
            } else {
                showToast(R.string.error_date_and_refresh);
            }
        }

        @Override
        public void failed(Object obj) {
            showWait(false);
            if (obj instanceof ResponseBaseCommonError) {
                ResponseBaseCommonError error = (ResponseBaseCommonError) obj;
                showToast(error.msg);
            }
        }
    };

    private DefaultCallback jobListCallback = new DefaultCallback() {
        @Override
        public void success(Object obj) {
            showWait(false);
            mCurrentVo = (PublishActivityListVo) obj;
            if (CheckUtils.isSafe(getActivity())) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mCurrentVo.pageNumber == 1) {
                            mAdapterMain.setAll(mCurrentVo.jobManageListVoList);
                            mListJob.updateRefreshTime();
                        } else {
                            mAdapterMain.addAll(mCurrentVo.jobManageListVoList);
                        }

                        mListJob.setLoadOver(mCurrentVo.jobManageListVoList.size(), PAGE_COUNT);
                        mListJob.stopRefresh();
                        mListJob.stopLoadMore();
                    }
                });
            }

        }

        @Override
        public void failed(Object obj) {
            showWait(false);
            new ErrorHandler((com.quark.jianzhidaren.BaseActivity) getActivity(), obj).showToast();
        }
    };

    public JobBrokerDetailFragment() {
    }

    public static JobBrokerDetailFragment newInstance(int companyId) {
        JobBrokerDetailFragment jobBrokerMeFragment = new JobBrokerDetailFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_COMPANY_ID, companyId);
        jobBrokerMeFragment.setArguments(args);
        return jobBrokerMeFragment;
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
        View view = inflater.inflate(R.layout.fragment_broker_detail, null);
        // arguments
        this.mCompanyId = getArguments().getInt(KEY_COMPANY_ID);

        // controls
        mImgViHead = (CircularImage) view.findViewById(R.id.imgvi_head);
        mImgViAuth = (ImageView) view.findViewById(R.id.imgvi_auth);
        mTxtCompanyName = (TextView) view.findViewById(R.id.txt_company_name);
        mTxtHireType = (TextView) view.findViewById(R.id.txt_company_type);
        mTxtIntro = (TextView) view.findViewById(R.id.txt_intro);
        mRankViPoint = (RankView) view.findViewById(R.id.rankvi_point);
        mListJob = (BaseXListView) view.findViewById(R.id.listview_job);
        mTxtTabIntro = (TextView) view.findViewById(R.id.txt_tab_intro);
        mTxtTabJob = (TextView) view.findViewById(R.id.txt_tab_job);
        mImgViTabIntro = (ImageView) view.findViewById(R.id.imgvi_tab_intro);
        mImgViTabJob = (ImageView) view.findViewById(R.id.imgvi_tab_job);

        mListJob.setPullRefreshEnable(false);

        // listener
        mTxtTabIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabClick(TAB_INTRO);
            }
        });

        mTxtTabJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabClick(TAB_JOB);
            }
        });

        mListJob.setOnItemClickListener(this);
        mListJob.setXListViewListener(this);

        // adapter
        mAdapterMain = new JobManageListAdapter(getActivity());
        mListJob.setAdapter(mAdapterMain);

        tabClick(TAB_INTRO);

        return view;
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

    private void bindData() {
        showWait(true);
        new UserDetailRequest().showCompany(this.mCompanyId, queue, showCompanyCallback);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void tabClick(int tabIndex) {
        switch (tabIndex) {
            case TAB_JOB:
                mTxtTabJob.setTextColor(getResources().getColor(R.color.common_gray_5));
                mImgViTabJob.setVisibility(View.VISIBLE);

                mTxtTabIntro.setTextColor(getResources().getColor(R.color.common_gray_4));
                mImgViTabIntro.setVisibility(View.GONE);

                mListJob.setVisibility(View.VISIBLE);
                mTxtIntro.setVisibility(View.GONE);

                refreshFirstPageJob();
                break;
            case TAB_INTRO:
                mTxtTabIntro.setTextColor(getResources().getColor(R.color.common_gray_5));
                mImgViTabIntro.setVisibility(View.VISIBLE);

                mTxtTabJob.setTextColor(getResources().getColor(R.color.common_gray_4));
                mImgViTabJob.setVisibility(View.GONE);

                mTxtIntro.setVisibility(View.VISIBLE);
                mListJob.setVisibility(View.GONE);

                break;
        }
    }

    private void bindCompanyInfo() {
        if (mCompanyInfo == null) {
            Logger.w("[bindCompanyInfo]mCompanyInfo is null!!!");
            return ;
        }

        mTxtCompanyName.setText(mCompanyInfo.name);
        mTxtHireType.setText(getString(R.string.job_broker_detail_hire_type_format,
                CheckUtils.isEmpty(mCompanyInfo.hire_type) || mCompanyInfo.hire_type.equals("null") ?
                        getString(R.string.none) : mCompanyInfo.hire_type ));
        mTxtIntro.setText(mCompanyInfo.introduction);
        ContactImageLoader.loadNativePhoto(String.valueOf(mCompanyInfo.id), mCompanyInfo.avatar, mImgViHead, queue);
        // 认证
        mImgViAuth.setVisibility(View.VISIBLE);
        mRankViPoint.rank(mCompanyInfo.point);

    }

    // 加载第一页兼职列表
    private void refreshFirstPageJob() {
        if (mCurrentVo == null) {
            showWait(true);
        }
        mCurrentVo = null;
        refreshListView();
    }

    private void refreshListView() {
        new PublishRequest().publishActivityList(this.mCompanyId, getNextPageNumber(), PAGE_COUNT, PublishRequest.PUBLISH_ACTIVITY_LIST_TYPE_RECRUIT, queue, jobListCallback);
    }

    // 获取下一页页码
    private int getNextPageNumber() {
        return mCurrentVo == null ? 1 : mCurrentVo.pageNumber + 1;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int position = i - 1;
        if (position < mAdapterMain.getCount()) {
            long jobId = mAdapterMain.getItemId(position);
            IntentManager.openJobDetailActivity(getActivity(), (int) jobId, "");
        } else {
            showToast(R.string.error_date_and_refresh);
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        loadMore();
    }

    private void loadMore() {
        refreshListView();
    }

}
