package com.parttime.publish;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingmu.jianzhidaren.R;
import com.quark.fragment.company.BaseSupportFragment;

/**
 * 经纪人-我的详情
 * Created by wyw on 2015/8/4.
 */
public class JobBrokerMeFragment extends BaseSupportFragment {

    public static JobBrokerMeFragment newInstance() {
        JobBrokerMeFragment jobBrokerMeFragment = new JobBrokerMeFragment();
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
        View view = inflater.inflate(R.layout.fragment_broker_me, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
