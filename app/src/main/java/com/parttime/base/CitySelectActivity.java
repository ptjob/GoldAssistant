package com.parttime.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.droid.carson.Activity01;

import java.io.Serializable;

/**
 * Created by cjz on 2015/8/2.
 */
public class CitySelectActivity extends Activity01{
    public static final String EXTRA_DIY_ACTION = "extra_diy_action";
    public static final String EXTRA_ACITON_EXTRA = "extra_action_extra";
    private DiyAction diyAction;
    private Serializable actionExtra;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if(intent != null) {
            diyAction = (DiyAction) intent.getSerializableExtra(EXTRA_DIY_ACTION);
            actionExtra = intent.getSerializableExtra(EXTRA_ACITON_EXTRA);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(diyAction != null){
            String currentCity = allCity_lists.get(position).getName();
            diyAction.clicked(position - ((ListView)parent).getHeaderViewsCount(), currentCity, actionExtra, this);
        }else {
            super.onItemClick(parent, view, position, id);
        }
    }
}
