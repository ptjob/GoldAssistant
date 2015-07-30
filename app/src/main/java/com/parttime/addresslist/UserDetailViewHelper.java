package com.parttime.addresslist;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
            reputationContainer,
            certificationContainer,
            appraiseContainer, //评价title容器

            resumeContainer; //简历容器
    public LinearLayout appraiseValueContainer,//评价容器
            reputationValueContainer,//信誉容器
            resumeBottomContainer, //招聘底部容器
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

    public void initView(View view){

    }

    /**
     * 简历容器
     */
    public static class ResumeContentContainer{
        public void initView(View view){

        }
    }
    /**
     * 好友容器
     */
    public static class FriendContentContainer{
        public void initView(View view){

        }
    }
    /**
     * 评价容器
     */
    public static class AppraiseContentContainer{
        public void initView(View view){

        }
    }


}
