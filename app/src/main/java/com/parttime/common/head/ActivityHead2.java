package com.parttime.common.head;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;

/**
 * 左边图片返回，ImageView:left_back
 * 中间两个TextView: center_txt1, center_txt2
 * 右边两个ImageView container Id : container_right1_image, container_right2_image, 一个ProgressBar:progressBar
 * Created by luhua on 15/7/17.
 */
public class ActivityHead2 extends ActivityHead {

    public TextView centerTxt2;
    public RelativeLayout imgRightContainer1, imgRightContainer2;
    public ImageView imgRight1;
    public TextView txtRight2;
    public ProgressBar progressBar;

    public ActivityHead2(Activity activity){
        super(activity);
    }

    @Override
    public void initHead(Activity activity) {
        super.initHead(activity);
        centerTxt2 = (TextView)activity.findViewById(R.id.center_txt2);
        imgRightContainer1 = (RelativeLayout)activity.findViewById(R.id.container_right1_image);
        imgRightContainer2 = (RelativeLayout)activity.findViewById(R.id.container_right2_image);
        imgRight1 = (ImageView)activity.findViewById(R.id.img_right1);
        txtRight2 = (TextView)activity.findViewById(R.id.img_right2);
        progressBar = (ProgressBar)activity.findViewById(R.id.progressBar);

    }


    public void setCenterTxt2(int resStr){
        centerTxt2.setText(resStr);
        centerTxt2.setVisibility(View.VISIBLE);
    }

    public void setCenterTxt2(String text){
        centerTxt2.setText(text);
        centerTxt2.setVisibility(View.VISIBLE);
    }

    public void setImgRight1(int resDrawable){
        imgRight1.setImageResource(resDrawable);
        imgRightContainer1.setVisibility(View.VISIBLE);
    }

    public void setImgRight1Gone(){
        imgRightContainer1.setVisibility(View.GONE);
    }

    public void setTxtRight2(int resDrawable){
        txtRight2.setBackgroundResource(resDrawable);
        imgRightContainer2.setVisibility(View.VISIBLE);
    }

    public void setTxtRight2Text(int resStr){
        txtRight2.setText(resStr);
        imgRightContainer2.setVisibility(View.VISIBLE);
    }

    public void setImgRight2Gone(){
        imgRightContainer2.setVisibility(View.GONE);
    }

}
