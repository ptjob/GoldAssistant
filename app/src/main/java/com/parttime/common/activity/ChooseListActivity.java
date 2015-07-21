package com.parttime.common.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chatuidemo.activity.BaseActivity;
import com.parttime.common.head.ActivityHead;
import com.parttime.utils.CheckUtils;
import com.qingmu.jianzhidaren.R;

/**
 * 公共选择列表界面
 * 用于传入一个字符串列表
 * 选择后返回一个字符结果
 * Created by wyw on 2015/7/17.
 */
public class ChooseListActivity extends BaseActivity {
    public static final String EXTRA_DATA = "data";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_RESULT = "result";

    private String[] data;
    private String title;
    private ListView mListViewMain;
    private ActivityHead activityHead;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_choose_list);

        initIntent();
        initControls();
        bindListener();
        bindData();
    }

    private void bindListener() {
        mListViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!CheckUtils.isEmpty(data)) {
                    getIntent().putExtra(EXTRA_RESULT, data[i]);
                    setResult(RESULT_OK, getIntent());
                    finish();
                }
            }
        });
    }

    private void bindData() {
        activityHead.setCenterTxt1(title);

        ChooseListAdapter adapter = new ChooseListAdapter();
        adapter.setAll(this.data);
        mListViewMain.setAdapter(adapter);
    }

    private void initControls() {
        activityHead = new ActivityHead(this);
        mListViewMain = (ListView) findViewById(R.id.listview_main);

    }

    private void initIntent() {
        this.title = getIntent().getStringExtra(EXTRA_TITLE);
        this.data = getIntent().getStringArrayExtra(EXTRA_DATA);
    }

    public class ChooseListAdapter extends BaseAdapter {
        private String[] entities;

        public void setAll(String[] entities) {
            this.entities = entities;
        }

        @Override
        public int getCount() {
            return this.entities != null ? this.entities.length : 0;
        }

        @Override
        public Object getItem(int i) {
            return entities[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = getLayoutInflater().inflate(R.layout.item_choose_list, null);
                viewHolder.mTxtItem = (TextView) view.findViewById(R.id.txt_item);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.mTxtItem.setText(entities[i]);

            return view;
        }

    }

    public class ViewHolder {
        public TextView mTxtItem;
    }
}
