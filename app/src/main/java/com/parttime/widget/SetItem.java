package com.parttime.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;

/**
 *
 * Created by luhua on 15/7/19.
 */
public class SetItem extends FrameLayout {

    protected TextView tvTitle;
    protected ImageView rightImage;

    protected LayoutInflater inflater;
    protected Context context;


    private int dividerLayoutWidth = LayoutParams.MATCH_PARENT;
    private int dividerLayoutHeight ;

    protected boolean topDividerShown;
    protected boolean bottomDividerShown;


    public SetItem(Context context) {
        super(context);
    }

    public SetItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            init(context, attrs);
        }
    }


    protected void init(Context context, AttributeSet attrs){
        this.context = context;
        inflater = LayoutInflater.from(context);
        View content = inflater.inflate(R.layout.widget_set_item, this, false);
        tvTitle = (TextView) content.findViewById(R.id.form_item_title);
        rightImage = (ImageView) content.findViewById(R.id.form_item_off);
        rightImage.setImageResource(R.drawable.settings_btn_switch_off);

        dividerLayoutHeight = context.getResources().getDimensionPixelSize(R.dimen.divider_width);

        assignXmlAttrs(attrs);
        checkDividers();

        addView(content);
        setClickable(true);
    }

    private void checkDividers(){
        if(topDividerShown){
            addView(makeTopDivider());
        }
        if(bottomDividerShown){
            addView(makeBotttomDidider());
        }
    }

    private void assignXmlAttrs(AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FormSetting);
        if(typedArray != null){

            String string = typedArray.getString(R.styleable.FormSetting_form_left_txt);
            if(string != null){
                tvTitle.setText(string);
            }

            Drawable drawable = typedArray.getDrawable(R.styleable.FormSetting_form_Right_Img);
            if(drawable != null){
                rightImage.setImageDrawable(drawable);
            }else {
                //set default
                rightImage.setImageResource(R.drawable.settings_btn_switch_off);
            }

            topDividerShown = typedArray.getBoolean(R.styleable.FormSetting_set_topLine_shown, true);
            bottomDividerShown = typedArray.getBoolean(R.styleable.FormSetting_set_bottomLine_shown, true);

            typedArray.recycle();
        }

    }

    public void setRightImage(int resId){
        rightImage.setImageResource(resId);
    }

    private View makeDivider(){
        View divider = new View(context);
        divider.setBackgroundColor(context.getResources().getColor(R.color.divider_color));
        return divider;
    }

    private View makeTopDivider(){
        View divider = makeDivider();
        FrameLayout.LayoutParams fllp = new FrameLayout.LayoutParams(dividerLayoutWidth, dividerLayoutHeight);
        fllp.gravity = Gravity.TOP;
        divider.setLayoutParams(fllp);
        return divider;
    }

    private View makeBotttomDidider(){
        View divider = makeDivider();
        FrameLayout.LayoutParams fllp = new FrameLayout.LayoutParams(dividerLayoutWidth, dividerLayoutHeight);
        fllp.gravity = Gravity.BOTTOM;
        divider.setLayoutParams(fllp);
        return divider;
    }
}
