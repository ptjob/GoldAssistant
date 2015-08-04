package com.parttime.publish;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parttime.utils.ApplicationUtils;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;

import java.util.ArrayList;

/**
 * 经纪人排行榜
 * Created by wyw on 2015/8/2.
 */
public class JobBrokerChartsActivity extends BaseActivity {
    private ArrayList<Fragment> pageViews;
    private Fragment jobBrokerChartsFragment;
    private Fragment jobBrokerMeFragment;
    private RelativeLayout topRelativeLayout;
    private TextView msgTv;
    private TextView contactsTv;
    private TextView mTxtShare;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_broker_rank);

        intiControls();
        bindListener();
    }

    private void bindListener() {
        msgTv.setOnClickListener(new TabButtonClickListener(0));
        contactsTv.setOnClickListener(new TabButtonClickListener(1));
    }

    private void intiControls() {

        pageViews = new ArrayList<>();
        jobBrokerChartsFragment = JobBrokerChartsFragment.newInstance();
        jobBrokerMeFragment = JobBrokerDetailFragment.newInstance(ApplicationUtils.getLoginId());
        pageViews.add(jobBrokerChartsFragment);
        pageViews.add(jobBrokerMeFragment);

        topRelativeLayout = (RelativeLayout) findViewById(R.id.quanzi_top_relayout);
        msgTv = (TextView) findViewById(R.id.fragment_quanzi_msg_tv);
        contactsTv = (TextView) findViewById(R.id.fragment_quanzi_contacts_tv);
        mTxtShare = (TextView) findViewById(R.id.right_txt);

        msgTv.setTextColor(getResources().getColor(R.color.guanli_common_color));

        viewPager = (ViewPager) findViewById(R.id.guidePages);
        viewPager.setAdapter(new GuidePageAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new GuidePageChangeListener());
    }

    @Override
    public void setBackButton() {
        super.setBackButton();
    }

    private class TabButtonClickListener implements View.OnClickListener {
        private int index;

        public TabButtonClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            if (index == 0) {
                if (mTxtShare != null) {
                    mTxtShare.setVisibility(View.GONE);
                }
            } else if (index == 1) {
                if (mTxtShare != null) {
                    mTxtShare.setVisibility(View.VISIBLE);
                }
            }
            viewPager.setCurrentItem(index, true);
        }
    }

    private class GuidePageAdapter extends FragmentPagerAdapter {
        public GuidePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return pageViews.get(i);
        }

        @Override
        public int getCount() {
            return pageViews.size();
        }
    }

    private class GuidePageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            setBackStatus(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    private void setBackStatus(int position) {
        if (position == 0) {
            msgTv.setBackgroundDrawable(getResources()
                    .getDrawable(R.drawable.quanzi_btn_bar_left_on));

            msgTv.setTextColor(getResources().getColor(
                    R.color.guanli_common_color));

            contactsTv.setBackgroundDrawable(getResources()
                    .getDrawable(R.drawable.quanzi_btn_bar_right_off));
            contactsTv.setTextColor(getResources().getColor(
                    R.color.body_color));
            mTxtShare.setVisibility(View.GONE);
        } else if (position == 1) {
            msgTv.setBackgroundDrawable(getResources()
                    .getDrawable(R.drawable.quanzi_btn_bar_left_off));
            msgTv.setTextColor(getResources().getColor(
                    R.color.body_color));
            contactsTv.setBackgroundDrawable(getResources()
                    .getDrawable(R.drawable.quanzi_btn_bar_right_on));
            contactsTv.setTextColor(getResources().getColor(
                    R.color.guanli_common_color));
            mTxtShare.setVisibility(View.VISIBLE);
        }

    }
}
