package com.parttime.publish;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parttime.net.DefaultCallback;
import com.parttime.net.ResponseBaseCommonError;
import com.parttime.net.UserDetailRequest;
import com.parttime.pojo.AccountInfo;
import com.qingmu.jianzhidaren.R;
import com.quark.fragment.company.BaseSupportFragment;

/**
 * 经纪人-详情
 * Created by wyw on 2015/8/4.
 */
public class JobBrokerDetailFragment extends BaseSupportFragment {

    public static final String KEY_COMPANY_ID = "companyId";
    private int mCompanyId;

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
        this.mCompanyId = getArguments().getInt(KEY_COMPANY_ID);

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
        new UserDetailRequest().showCompany(this.mCompanyId, queue, new DefaultCallback() {
            @Override
            public void success(Object obj) {
                if (obj instanceof AccountInfo) {
                    showWait(false);
                    AccountInfo accountInfo = (AccountInfo) obj;
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
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
