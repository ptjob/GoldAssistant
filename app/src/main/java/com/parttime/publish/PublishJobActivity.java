package com.parttime.publish;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.easemob.chatuidemo.activity.BaseActivity;
import com.parttime.common.head.ActivityHead;
import com.qingmu.jianzhidaren.R;

public class PublishJobActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private GridView mGridViewJobType;
    private String[] jobTypeArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_job);

        initControls();
        bindListener();
        bindData();
    }

    private void initControls() {
        ActivityHead activityHead = new ActivityHead();
        activityHead.initHead(this);
        activityHead.setCenterTxt(R.string.publish_job_title);
        mGridViewJobType = (GridView) findViewById(R.id.gridview_job_type);
    }

    private void bindListener() {
        mGridViewJobType.setOnItemClickListener(this);
    }

    private void bindData() {
        jobTypeArray = getResources().getStringArray(R.array.job_type);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(
                this,
                R.layout.item_job_type,
                R.id.txt_type_name,
                jobTypeArray
        );
        mGridViewJobType.setAdapter(stringArrayAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, jobTypeArray[i], Toast.LENGTH_SHORT).show();
    }
}
