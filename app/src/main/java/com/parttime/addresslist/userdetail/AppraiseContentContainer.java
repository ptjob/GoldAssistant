package com.parttime.addresslist.userdetail;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;

/**
 * 评价容器
 */
public class AppraiseContentContainer{

    public ImageView star1, star2, star3, star4;
    public LinearLayout appraiseDetailContainer; //评价容器
    public EditText appraiseContent;
    public TextView appraiseTextCountTip;
    public Button submit;

    //数据部分
    UserDetailPagerAdapter.UserDetailFragment userDetailFragment;
    UserDetailPagerAdapter userDetailPagerAdapter;

    public AppraiseContentContainer(UserDetailPagerAdapter.UserDetailFragment userDetailFragment,
                                  UserDetailPagerAdapter userDetailPagerAdapter) {
        this.userDetailFragment = userDetailFragment;
        this.userDetailPagerAdapter = userDetailPagerAdapter;
    }

    public void initView(View view){
        star1 = (ImageView)view.findViewById(R.id.star1);
        star2 = (ImageView)view.findViewById(R.id.star2);
        star3 = (ImageView)view.findViewById(R.id.star3);
        star4 = (ImageView)view.findViewById(R.id.star4);

        appraiseDetailContainer = (LinearLayout)view.findViewById(R.id.appraise_detail_container);

        appraiseContent = (EditText)view.findViewById(R.id.appraise_content);

        appraiseTextCountTip = (TextView)view.findViewById(R.id.appraise_text_count_tip);

        submit = (Button)view.findViewById(R.id.submit);
    }
}