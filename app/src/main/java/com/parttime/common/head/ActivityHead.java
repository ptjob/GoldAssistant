package com.parttime.common.head;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;

import org.w3c.dom.Text;

/**
 * 左边图片返回， left_back
 * 中间文字 center_txt1
 * Created by luhua on 15/7/13.
 */
public class ActivityHead{

    public View leftBack;
    public TextView centerTxt1;
    public TextView rightTxt;

    public ActivityHead(Activity activity){
        initHead(activity);
    }

    public void initHead(final Activity activity){
        centerTxt1 = (TextView)activity.findViewById(R.id.center_txt1);
        leftBack = activity.findViewById(R.id.left_back);
        leftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        rightTxt = (TextView) activity.findViewById(R.id.right_txt);
    }

    public void setCenterTxt1(int resStr){
        centerTxt1.setText(resStr);
        centerTxt1.setVisibility(View.VISIBLE);
    }

    public void setCenterTxt1(String text){
        centerTxt1.setText(text);
        centerTxt1.setVisibility(View.VISIBLE);
    }

    public void setRightTxt(int resStr) {
        rightTxt.setText(resStr);
        rightTxt.setVisibility(View.VISIBLE);
    }

    public void setRightTxt(String text) {
        rightTxt.setText(text);
        rightTxt.setVisibility(View.VISIBLE);
    }

    public void setRightTxtOnClickListener(View.OnClickListener onClickListener){
        rightTxt.setOnClickListener(onClickListener);
    }
}
