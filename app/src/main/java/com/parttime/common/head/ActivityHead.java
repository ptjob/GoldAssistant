package com.parttime.common.head;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;

/**
 * 左边图片返回，
 * 中间文字
 * Created by luhua on 15/7/13.
 */
public class ActivityHead{

    public LinearLayout leftBack;
    public TextView centerTxt;

    public void initHead(final Activity activity){
        centerTxt = (TextView)activity.findViewById(R.id.center_txt);
        leftBack = (LinearLayout)activity.findViewById(R.id.left_back);
        leftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    public void setCenterTxt(int resStr){
        centerTxt.setText(resStr);
    }

}
