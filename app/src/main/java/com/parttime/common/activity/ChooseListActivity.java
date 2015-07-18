package com.parttime.common.activity;

import android.os.Bundle;

import com.easemob.chatuidemo.activity.BaseActivity;
import com.qingmu.jianzhidaren.R;

/**
 * 公共选择列表界面
 * 用于传入一个字符串列表
 * 选择后返回一个字符结果
 * Created by wyw on 2015/7/17.
 */
public class ChooseListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_choose_list);
    }
}
