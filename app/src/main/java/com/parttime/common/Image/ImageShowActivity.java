package com.parttime.common.Image;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.parttime.addresslist.userdetail.UserDetailPagerAdapter;
import com.parttime.base.WithTitleActivity;
import com.parttime.constants.ActivityExtraAndKeys;
import com.qingmu.jianzhidaren.R;

import java.util.ArrayList;

public class ImageShowActivity extends WithTitleActivity {

    private ViewPager viewPager ;

    private ImageShowPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_image_show);
        super.onCreate(savedInstanceState);
        initView();

        bindData();

    }

    private void initView() {
        viewPager = (ViewPager)findViewById(R.id.viewPager);

    }

    private void bindData() {
        ArrayList<String> pictures = getIntent().getStringArrayListExtra(ActivityExtraAndKeys.ImageShow.PICTURES);
        ArrayList<String> userIds = getIntent().getStringArrayListExtra(ActivityExtraAndKeys.USER_ID);

        adapter = new ImageShowPagerAdapter(getSupportFragmentManager());
        adapter.setData(pictures,userIds);
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cache.clear();
    }

    @Override
    protected ViewGroup getLeftWrapper() {
        return null;
    }

    @Override
    protected ViewGroup getRightWrapper() {
        return null;
    }

    @Override
    protected TextView getCenter() {
        return null;
    }

}
