package com.parttime.publish;

import android.os.Bundle;

import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;

/**
 * 经纪人排行榜
 * Created by wyw on 2015/8/2.
 */
public class JobBrokerCharts extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_broker_rank);
    }

    @Override
     public void setBackButton() {
        super.setBackButton();
    }
}
