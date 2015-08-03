package com.parttime.publish;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;

import java.util.ArrayList;

/**
 * 经纪人排行榜
 * Created by wyw on 2015/8/2.
 */
public class JobBrokerCharts extends BaseActivity {
    private ArrayList<Object> pageViews;
    private View page1;
    private View page2;
    private RelativeLayout topRelativeLayout;
    private TextView msgTv;
    private TextView contactsTv;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_broker_rank);

        intiControls();
        bindData();
    }

    private void bindData() {
        initMessageView();

        initAddressView();
    }

    private void initAddressView() {

    }

    private void initMessageView() {

    }

    private void intiControls() {
        LayoutInflater inflater = getLayoutInflater();
        pageViews = new ArrayList<>();
        page1 = inflater.inflate(R.layout.fragment_conversation_history, null);
        page2 = inflater.inflate(R.layout.fragment_contact_list, null);
        pageViews.add(page1);
        pageViews.add(page2);
        topRelativeLayout = (RelativeLayout) findViewById(R.id.quanzi_top_relayout);
        msgTv = (TextView) findViewById(R.id.fragment_quanzi_msg_tv);
        contactsTv = (TextView) findViewById(R.id.fragment_quanzi_contacts_tv);
        msgTv.setTextColor(getResources().getColor(
                R.color.guanli_common_color));
        msgTv.setOnClickListener(new TabButtonClickListener(0));
        contactsTv.setOnClickListener(new TabButtonClickListener(1));
        viewPager = (ViewPager) findViewById(R.id.guidePages);
        viewPager.setAdapter(new GuidePageAdapter());
        viewPager.setOnPageChangeListener(new GuidePageChangeListener());
    }

    @Override
    public void setBackButton() {
        super.setBackButton();
    }

    private class TabButtonClickListener implements View.OnClickListener {
        public TabButtonClickListener(int i) {

        }

        @Override
        public void onClick(View view) {

        }
    }

    private class GuidePageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return false;
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
            msgTv.setBackgroundDrawable( getResources()
                    .getDrawable(R.drawable.quanzi_btn_bar_left_on));

            msgTv.setTextColor( getResources().getColor(
                    R.color.guanli_common_color));

            contactsTv.setBackgroundDrawable( getResources()
                    .getDrawable(R.drawable.quanzi_btn_bar_right_off));
            contactsTv.setTextColor( getResources().getColor(
                    R.color.body_color));
        } else if (position == 1) {
            msgTv.setBackgroundDrawable( getResources()
                    .getDrawable(R.drawable.quanzi_btn_bar_left_off));
            msgTv.setTextColor( getResources().getColor(
                    R.color.body_color));
            contactsTv.setBackgroundDrawable( getResources()
                    .getDrawable(R.drawable.quanzi_btn_bar_right_on));
            contactsTv.setTextColor( getResources().getColor(
                    R.color.guanli_common_color));

        }
        
    }
}
