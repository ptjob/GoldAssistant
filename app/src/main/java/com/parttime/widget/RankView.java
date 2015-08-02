package com.parttime.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qingmu.jianzhidaren.R;

/**
 * Created by cjz on 2015/7/21.
 */
public class RankView extends LinearLayout{

    private Context context;

    private boolean initExecuted;
    private int emptyResId;
    private int fullResId;
    private int totalScore = 5;
    private int innerMargin;
    private int itemWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int itemHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

    private ImageView[] ivs;

    public RankView(Context context) {
        super(context);
        if(!initExecuted){
            initExecuted = true;
            init(context, null);
        }
    }

    public RankView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    public RankView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RankView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    private void init(Context context, AttributeSet attrs){
        this.context = context;
        setOrientation(LinearLayout.HORIZONTAL);
        innerMargin = context.getResources().getDimensionPixelSize(R.dimen.rank_view_inner_margin_default);
        if(attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RankView);
            if(typedArray != null){
                emptyResId = typedArray.getResourceId(R.styleable.RankView_rv_empty_img, 0);
                fullResId = typedArray.getResourceId(R.styleable.RankView_rv_full_img, 0);
                totalScore = typedArray.getInt(R.styleable.RankView_rv_total_score, totalScore);
                innerMargin = typedArray.getDimensionPixelSize(R.styleable.RankView_rv_inner_margin, innerMargin);
                itemWidth = typedArray.getDimensionPixelSize(R.styleable.RankView_rv_item_width, itemWidth);
                itemHeight = typedArray.getDimensionPixelSize(R.styleable.RankView_rv_item_height, itemHeight);
            }
        }
        if(emptyResId <= 0){
            emptyResId = R.drawable.redstar_empty;
        }
        if(fullResId <= 0){
            fullResId = R.drawable.redstar_fill;
        }

        establishIvs();
    }

    private void establishIvs(){
        removeAllViews();
        ivs = new ImageView[totalScore];
        ImageView iv;
        if(totalScore > 0){
            iv = makeIv();
            addView(iv, makeLayoutParams());
            ivs[indexOfChild(iv)] = iv;

            for(int i = 1; i < totalScore; ++i){
                iv = makeIv();
                LayoutParams layoutParams = makeLayoutParams(innerMargin);
                addView(iv, layoutParams);
                ivs[indexOfChild(iv)] = iv;
            }
        }
    }

    private ImageView makeIv(){
        ImageView iv = new ImageView(context);
        return iv;
    }

    private LinearLayout.LayoutParams makeLayoutParams(){
        return makeLayoutParams(0);
    }

    private LinearLayout.LayoutParams makeLayoutParams(int marginLeft){
        LayoutParams lp = new LayoutParams(itemWidth, itemHeight);
        lp.gravity = Gravity.CENTER_VERTICAL;
        lp.leftMargin = marginLeft;
        return lp;
    }

    public void rank(int score){
        int i = 0;
        for(; i < score && i < ivs.length; ++i){
            ivs[i].setImageResource(fullResId);
        }
        for(; i < ivs.length; ++i){
            ivs[i].setImageResource(emptyResId);
        }
    }

    public void setTotalScore(int totalScore){
        this.totalScore = totalScore;
        establishIvs();
    }
}
