package com.parttime.addresslist.userdetail;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;

/**
 *
 * Created by luhua on 2015/7/30.
 */
public class UserDetailViewHelper {

    public RelativeLayout nameContainer,
            sexContainer,
            ageContainer,
            educationContainer,
            heightContainer,
            otherContainer,
            threeDimensionalContainer,
            reputationContainer ,//信誉容器
            certificationContainer,

            resumeContainer; //简历容器

    public LinearLayout
            reputationValueContainer,//信誉值容器

            friendContainer,        //好友容器
            evaluationContainer;    //评价容器


    public TextView nameTxt,
            sexTxt,
            ageTxt,
            educationTxt,
            heightTxt,
            otherTxt,
            threeDimensionalTxt,
            certificationTxt;

    public ResumeContentContainer resumeContentContainer;
    public FriendContentContainer friendContentContainer;
    public AppraiseContentContainer appraiseContentContainer;

    UserDetailPagerAdapter.UserDetailFragment userDetailFragment;
    UserDetailPagerAdapter userDetailPagerAdapter;

    public UserDetailViewHelper(UserDetailPagerAdapter.UserDetailFragment userDetailFragment,UserDetailPagerAdapter userDetailPagerAdapter) {
        this.userDetailFragment = userDetailFragment;
        this.userDetailPagerAdapter = userDetailPagerAdapter;
    }

    //数据区域



    public void initView(View view,InitContent initContent){
        nameContainer = (RelativeLayout)view.findViewById(R.id.name_container);
        sexContainer = (RelativeLayout)view.findViewById(R.id.sex_container);
        ageContainer = (RelativeLayout)view.findViewById(R.id.age_container);
        educationContainer = (RelativeLayout)view.findViewById(R.id.education_container);
        heightContainer = (RelativeLayout)view.findViewById(R.id.height_container);
        otherContainer = (RelativeLayout)view.findViewById(R.id.other_container);
        threeDimensionalContainer = (RelativeLayout)view.findViewById(R.id.three_dimensional_container);
        reputationContainer = (RelativeLayout)view.findViewById(R.id.reputation_container);
        certificationContainer = (RelativeLayout)view.findViewById(R.id.certification_container);
        resumeContainer = (RelativeLayout)view.findViewById(R.id.resume_content_container);


        reputationValueContainer = (LinearLayout)view.findViewById(R.id.reputation_value_star_container);
        friendContainer = (LinearLayout)view.findViewById(R.id.friend_content_container);
        evaluationContainer = (LinearLayout)view.findViewById(R.id.evaluation_container);

        nameTxt = (TextView)view.findViewById(R.id.name);
        sexTxt = (TextView)view.findViewById(R.id.sex);
        ageTxt = (TextView)view.findViewById(R.id.age);
        educationTxt = (TextView)view.findViewById(R.id.education_value);
        heightTxt = (TextView)view.findViewById(R.id.height_value);
        otherTxt = (TextView)view.findViewById(R.id.other_value);
        threeDimensionalTxt = (TextView)view.findViewById(R.id.three_dimensional_value);
        certificationTxt = (TextView)view.findViewById(R.id.certification_value);


        if(initContent == InitContent.INIT_RESUME){//初始化简历
            resumeContainer.setVisibility(View.VISIBLE);
            resumeContentContainer = new ResumeContentContainer(userDetailFragment,userDetailPagerAdapter);
            resumeContentContainer.initView(resumeContainer);
        }else if(initContent == InitContent.INIT_FRIEND){//初始化好友
            friendContainer.setVisibility(View.VISIBLE);
            friendContentContainer = new FriendContentContainer(userDetailFragment,userDetailPagerAdapter);
            friendContentContainer.initView(friendContainer);
        }else if(initContent == InitContent.INIT_APPRAISE){//初始化评价
            evaluationContainer.setVisibility(View.VISIBLE);
            appraiseContentContainer = new AppraiseContentContainer(userDetailFragment,userDetailPagerAdapter);
            appraiseContentContainer.initView(evaluationContainer);
        }

    }

    public void initData(){

    }


    public static enum InitContent{
        INIT_RESUME,
        INIT_FRIEND,
        INIT_APPRAISE
   }

}
