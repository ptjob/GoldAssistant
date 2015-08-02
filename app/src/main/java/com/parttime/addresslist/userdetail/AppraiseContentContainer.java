package com.parttime.addresslist.userdetail;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parttime.common.activity.EditTextLimitChar;
import com.parttime.constants.ConstantForSaveListHelper;
import com.parttime.net.DefaultCallback;
import com.parttime.net.UserDetailRequest;
import com.parttime.pojo.UserDetailVO;
import com.qingmu.jianzhidaren.BuildConfig;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.ApplicationControl;

/**
 * 评价容器
 */
public class AppraiseContentContainer implements CompoundButton.OnCheckedChangeListener, View.OnClickListener{

    public CheckBox star1, star2, star3, star4;
    public LinearLayout appraiseDetailContainer; //评价容器
    public EditText appraiseContent;
    public TextView appraiseTextCountTip,appraiseRemark,starDescription;
    public Button submit;
    public FrameLayout editContainer;

    //数据部分
    UserDetailPagerAdapter.UserDetailFragment userDetailFragment;
    UserDetailPagerAdapter userDetailPagerAdapter;
    UserDetailActivity activity;
    UserDetailVO userDetailVO;

    private final int TIP_MAX_SIZE = 50;

    public AppraiseContentContainer(UserDetailPagerAdapter.UserDetailFragment userDetailFragment,
                                  UserDetailPagerAdapter userDetailPagerAdapter) {
        this.userDetailFragment = userDetailFragment;
        this.userDetailPagerAdapter = userDetailPagerAdapter;
        this.activity = userDetailPagerAdapter.userDetailActivity;
    }

    public void initView(View view){
        star1 = (CheckBox)view.findViewById(R.id.star1);
        star2 = (CheckBox)view.findViewById(R.id.star2);
        star3 = (CheckBox)view.findViewById(R.id.star3);
        star4 = (CheckBox)view.findViewById(R.id.star4);

        appraiseDetailContainer = (LinearLayout)view.findViewById(R.id.appraise_detail_container);

        appraiseContent = (EditText)view.findViewById(R.id.appraise_content);

        appraiseTextCountTip = (TextView)view.findViewById(R.id.appraise_text_count_tip);
        appraiseRemark = (TextView)view.findViewById(R.id.appraise_remark);
        starDescription = (TextView)view.findViewById(R.id.star_description);

        submit = (Button)view.findViewById(R.id.submit);
        editContainer = (FrameLayout) view.findViewById(R.id.edit_container);

        //进来不显示，点击星评价之后，如果小于四颗星显示
        appraiseDetailContainer.setVisibility(View.GONE);
        appraiseRemark.setVisibility(View.GONE);
        starDescription.setVisibility(View.GONE);

        star1.setOnCheckedChangeListener(this);
        star2.setOnCheckedChangeListener(this);
        star3.setOnCheckedChangeListener(this);
        star4.setOnCheckedChangeListener(this);

        submit.setOnClickListener(this);


        new EditTextLimitChar(activity,TIP_MAX_SIZE,null,appraiseContent,appraiseTextCountTip);
    }

    public void reflesh(UserDetailVO vo) {
        userDetailVO = vo;
        String common = vo.comment;
        if(! TextUtils.isEmpty(common)){
            starDescription.setVisibility(View.VISIBLE);

            ApplicationControl application = ApplicationControl.getInstance();
            if(application.getString(R.string.comment_excellent).equals(common)){
                star4.setChecked(true);
                star3.setChecked(true);
                star2.setChecked(true);
                star1.setChecked(true);
                starDescription.setText(R.string.comment_excellent);
            }else if(application.getString(R.string.comment_good).equals(common)){
                star3.setChecked(true);
                star2.setChecked(true);
                star1.setChecked(true);
                starDescription.setText(R.string.comment_good);
            }else if(application.getString(R.string.comment_bad).equals(common)){
                star2.setChecked(true);
                star1.setChecked(true);
                starDescription.setText(R.string.comment_bad);
            }else if(application.getString(R.string.comment_fly).equals(common)){
                star1.setChecked(true);
                starDescription.setText(R.string.comment_fly);
            }else{
                star4.setChecked(true);
                star3.setChecked(true);
                star2.setChecked(true);
                star1.setChecked(true);
            }

            star1.setOnCheckedChangeListener(null);
            star2.setOnCheckedChangeListener(null);
            star3.setOnCheckedChangeListener(null);
            star4.setOnCheckedChangeListener(null);
            star1.setEnabled(false);
            star2.setEnabled(false);
            star3.setEnabled(false);
            star4.setEnabled(false);

            String remark = vo.remark;
            if(!TextUtils.isEmpty(remark)) {
                appraiseRemark.setText(remark);
                appraiseRemark.setVisibility(View.VISIBLE);
            }
            submit.setEnabled(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int viewId = buttonView.getId();
        switch (viewId){
            case R.id.star1:

                appraiseDetailContainer.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                starDescription.setVisibility(isChecked ? View.VISIBLE : View.GONE);

                if(!isChecked) {
                    star3.setChecked(false);
                    star4.setChecked(false);
                    star2.setChecked(false);
                }else{
                    starDescription.setText(R.string.comment_fly);
                    if(! star2.isChecked() && ! star3.isChecked() && ! star4.isChecked()){
                        appraiseContent.setHint(R.string.comment_remark_hint);
                    }
                }
                break;
            case R.id.star2:
                if(!isChecked){
                    star3.setChecked(false);
                    star4.setChecked(false);
                    starDescription.setText(R.string.comment_fly);
                }else{
                    star1.setChecked(true);
                    starDescription.setText(R.string.comment_bad);
                }
                break;
            case R.id.star3:
                if(!isChecked){
                    star4.setChecked(false);
                    starDescription.setText(R.string.comment_bad);
                }else{
                    star1.setChecked(true);
                    star2.setChecked(true);
                    starDescription.setText(R.string.comment_good);
                }
                break;
            case R.id.star4:
                if(isChecked) {
                    star1.setChecked(true);
                    star2.setChecked(true);
                    star3.setChecked(true);
                    appraiseDetailContainer.setVisibility(View.GONE);
                    starDescription.setText(R.string.comment_excellent);
                }else{
                    appraiseDetailContainer.setVisibility(View.VISIBLE);
                    starDescription.setText(R.string.comment_good);
                }
                break;
        }
    }

    public void checkStarStatus(){}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                submit();
                break;
        }
    }

    private void submit() {
        ApplicationControl application = ApplicationControl.getInstance();
        String comment = application.getString(R.string.comment_excellent);
        if(star4.isChecked()){
            comment = application.getString(R.string.comment_excellent);
        }else if(star3.isChecked()){
            comment = application.getString(R.string.comment_good);
        }else if(star2.isChecked()){
            comment = application.getString(R.string.comment_bad);
        }else if(star1.isChecked()){
            comment = application.getString(R.string.comment_fly);
        }

        final String remark = appraiseContent.getText().toString();

        if(application.getString(R.string.comment_fly).equals(comment) && remark.length() < 10){
            Toast.makeText(activity,R.string.comment_fly_tips, Toast.LENGTH_SHORT).show();
            return ;
        }

        final String comment2 =comment;
        activity.showWait(true);
        new UserDetailRequest().comment(userDetailFragment.userId,activity.groupId,
                comment,remark,
                activity.queue,
                new DefaultCallback(){
                    @Override
                    public void success(Object obj) {
                        userDetailVO.comment = comment2;
                        userDetailVO.remark = remark;
                        new ConstantForSaveListHelper().updateGroupAppliantCacheIsComment(
                                activity.groupId,
                                userDetailFragment.userId);
                        activity.showWait(false);
                    }

                    @Override
                    public void failed(Object obj) {

                        activity.showWait(false);
                    }
                });
    }
}