package com.parttime.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.parttime.base.WithTitleActivity;
import com.parttime.common.Image.ContactImageLoader;
import com.parttime.pojo.AccountInfo;
import com.parttime.type.AccountType;
import com.parttime.type.CertStatus;
import com.parttime.widget.RankView;
import com.qingmu.jianzhidaren.R;

/**
 * Created by cjz on 2015/7/30.
 */
public class PreviewMyIntroActivity extends WithTitleActivity {
    public static final String EXTRA_ACCOUNT_INFO = "extra_account_info";
    private AccountInfo accountInfo;


    @ViewInject(R.id.iv_head)
    private ImageView ivHead;
    @ViewInject(R.id.tv_name)
    private TextView tvName;
    @ViewInject(R.id.rv_rank)
    private RankView rvRank;
    @ViewInject(R.id.certed)
    private ImageView ivCerted;
    @ViewInject(R.id.tv_work_types)
    private TextView tvWorkTypes;
//    @ViewInject(R.id.tv_work_types_intro)
//    private TextView tvWorkTypeIntro;
    @ViewInject(R.id.tv_main_intro)
    private TextView tvMainIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_preview_my_intro);
        ViewUtils.inject(this);
        getIntentData();
        super.onCreate(savedInstanceState);
    }

    private void getIntentData(){
        Intent intent = getIntent();
        accountInfo = intent.getParcelableExtra(EXTRA_ACCOUNT_INFO);
    }

    @Override
    protected void initViews() {
        super.initViews();
        center(R.string.preview_intro);
        left(TextView.class, R.string.back);

        bindDataToUi();
    }

    private void bindDataToUi(){
        if(accountInfo == null){
            return;
        }
        Bitmap bitmap = ContactImageLoader.get(accountInfo.id + "");
        ivHead.setImageBitmap(bitmap);

        tvName.setText(accountInfo.name);
        rvRank.rank(accountInfo.point);
        if(accountInfo.status == CertStatus.CERT_PASSED){
            ivCerted.setVisibility(View.VISIBLE);
        }
        if(accountInfo.type != AccountType.AGENT){
            tvWorkTypes.setVisibility(View.GONE);
//            tvWorkTypeIntro.setVisibility(View.GONE);
        }else {
            tvWorkTypes.setText(accountInfo.hire_type);
//            tvWorkTypeIntro.setText(getString());
        }
        tvMainIntro.setText(accountInfo.introduction);

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
}
