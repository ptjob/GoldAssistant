package com.parttime.mine;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.parttime.base.WithTitleActivity;
import com.parttime.common.adapters.SingleTextAdapter;
import com.parttime.widget.CountingEditText;
import com.qingmu.jianzhidaren.R;

/**
 * Created by cjz on 2015/7/12.
 */
public class EditMyIntroActivity extends WithTitleActivity {
    private FrameLayout flLeft;
    private FrameLayout flRight;
    private TextView tvCenter;

    private CountingEditText cetIntro;
    private GridView gv;
    private SingleTextAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_my_intro);
        super.onCreate(savedInstanceState);
        loadData();
    }

    @Override
    protected void initViews(){
        left(TextView.class, R.string.back);
        right(TextView.class, R.string.preview);
        center(R.string.edit_intro);
//        cetIntro = (CountingEditText) findViewById(R.id.cet_intro);
        gv = (GridView) findViewById(R.id.gv_types);
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

    protected void loadData(){

    }


}
