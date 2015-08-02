package com.parttime.addresslist.userdetail;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parttime.common.Image.ContactImageLoader;
import com.parttime.pojo.UserDetailVO;
import com.parttime.utils.IntentManager;
import com.parttime.utils.TimeUtils;
import com.parttime.widget.RankView;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.ApplicationControl;

import java.util.ArrayList;

/**
 *
 * Created by luhua on 2015/7/30.
 */
public class UserDetailViewHelper implements View.OnClickListener {

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
            friendContainer,        //好友容器
            evaluationContainer;    //评价容器

    public RankView reputationValueContainer;//信誉值容器


    public TextView nameTxt,
            sexTxt,
            ageTxt,
            educationTxt,
            heightTxt,
            otherTxt,
            threeDimensionalTxt,
            certificationTxt,
            pictureNum  //图片数量
                    ;


    public ImageView head;

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
    ArrayList<String> pictures;


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


        reputationValueContainer = (RankView)view.findViewById(R.id.reputation_value_container);
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
        pictureNum = (TextView)view.findViewById(R.id.picture_num);

        head = (ImageView)view.findViewById(R.id.head);

        if(initContent == InitContent.INIT_RESUME){//初始化简历
            resumeContainer.setVisibility(View.VISIBLE);
            resumeContentContainer = new ResumeContentContainer(userDetailFragment,userDetailPagerAdapter);
            resumeContentContainer.initView(view);
        }else if(initContent == InitContent.INIT_FRIEND){//初始化好友

            educationContainer.setVisibility(View.GONE);
            heightContainer.setVisibility(View.GONE);
            otherContainer.setVisibility(View.GONE);
            threeDimensionalContainer.setVisibility(View.GONE);

            friendContainer.setVisibility(View.VISIBLE);
            friendContentContainer = new FriendContentContainer(userDetailFragment,userDetailPagerAdapter);
            friendContentContainer.initView(friendContainer);
        }else if(initContent == InitContent.INIT_APPRAISE){//初始化评价

            educationContainer.setVisibility(View.GONE);
            heightContainer.setVisibility(View.GONE);
            otherContainer.setVisibility(View.GONE);
            threeDimensionalContainer.setVisibility(View.GONE);

            evaluationContainer.setVisibility(View.VISIBLE);
            appraiseContentContainer = new AppraiseContentContainer(userDetailFragment,userDetailPagerAdapter);
            appraiseContentContainer.initView(evaluationContainer);
        }

        setListener();
    }

    private void setListener() {
        head.setOnClickListener(this);
    }

    /**
     * 刷新数据
     * @param vo UserDetailVO
     */
    public void reflesh(UserDetailVO vo,InitContent initContent) {
        pictures = new ArrayList<>();
        String picture1 = vo.picture_1;
        if(!TextUtils.isEmpty(picture1)){
            ContactImageLoader.loadNativePhoto(userDetailFragment.userId,picture1,head, userDetailPagerAdapter.userDetailActivity.queue);
            pictures.add(picture1);
        }
        String picture2 = vo.picture_2;
        if(!TextUtils.isEmpty(picture2)){
            pictures.add(picture2);
        }
        String picture3 = vo.picture_3;
        if(!TextUtils.isEmpty(picture3)){
            pictures.add(picture3);
        }
        String picture4 = vo.picture_4;
        if(!TextUtils.isEmpty(picture4)){
            pictures.add(picture4);
        }
        String picture5 = vo.picture_5;
        if(!TextUtils.isEmpty(picture5)){
            pictures.add(picture5);
        }
        String picture6 = vo.picture_6;
        if(!TextUtils.isEmpty(picture6)){
            pictures.add(picture6);
        }
        pictureNum.setText(ApplicationControl.getInstance().getString(R.string.picture_num,pictures.size()));

        nameTxt.setText(vo.name);
        if(initContent == InitContent.INIT_FRIEND){
            if(vo.age > 0) {
                ageTxt.setText(String.valueOf(vo.age));
            }
        }else {
            String birthdate = vo.birthdate;
            if (!TextUtils.isEmpty(birthdate)) {
                long birthTime = TimeUtils.getTime(birthdate, TimeUtils.pattern1);
                int age = (int) (System.currentTimeMillis() - birthTime) / (1000 * 60 * 60 * 24);
                if (age > 0) {
                    ageTxt.setText(String.valueOf(age + 1));
                }
            }
        }
        sexTxt.setText(vo.sex == 0 ? "女": (vo.sex == 1 ? "男":"未知"));
        educationTxt.setText(vo.education);
        heightTxt.setText(String.valueOf(vo.height));
        String other;
        if(vo.health_record == 0){
            other = "无健康证";
        }else{
            other = "有健康证";
        }
        otherTxt.setText(other);
        threeDimensionalTxt.setText(vo.bbh);
        StringBuilder certificationStr = new StringBuilder();
        int certification = vo.certification;
        if(certification == 0){
            certificationStr.append("未认证");
        }else if(certification == 1){
            certificationStr.append("已提交认证");
        }else if(certification == 2){
            certificationStr.append("已实名认证");
        }else if(certification == 3){
            certificationStr.append("认证不通过");
        }
        if(certificationStr.length() > 0){
            certificationStr.append("/");
        }
        int earnestMoney = vo.earnest_money;
        if(earnestMoney == 0){
            certificationStr.append("未交诚意金");
        }else if(earnestMoney ==1){
            certificationStr.append("已交诚意金");
        }
        certificationTxt.setText(certificationStr.toString());

        //化信誉值
        //Utils.addStars(vo.creditworthiness, reputationValueContainer, userDetailPagerAdapter.userDetailActivity, R.drawable.ee_27);
        int num = (int)Math.round(vo.creditworthiness * 1.0 / 10);
        reputationValueContainer.rank(num);

        if(initContent == InitContent.INIT_RESUME){//初始化简历
            resumeContentContainer.reflesh(vo);
        }else if(initContent == InitContent.INIT_FRIEND){//初始化好友
            friendContentContainer.reflesh(vo);
        }else if(initContent == InitContent.INIT_APPRAISE){//初始化评价
            appraiseContentContainer.reflesh(vo);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head:
                ArrayList<String> userIds = new ArrayList<>();
                int size = pictures.size();
                for(int i = 0 ; i < size; i ++){
                    userIds.add(userDetailFragment.userId);
                }
                IntentManager.intentToImageShow(userDetailFragment.userDetailPagerAdapter.userDetailActivity,pictures,userIds);
                break;
        }
    }


    public static enum InitContent{
        INIT_RESUME,
        INIT_FRIEND,
        INIT_APPRAISE
   }

}
