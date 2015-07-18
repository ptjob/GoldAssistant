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
 * Created by cjz on 2015/7/12.
 */
public class FormItem extends FrameLayout{

    protected ImageView ivIcon;
    protected TextView tvTitle;
    protected TextView tvValue;
    protected ImageView ivArrow;

    protected LayoutInflater inflater;
    protected Context context;
    private boolean initExecuted;


    private int dividerLayoutWidth = LayoutParams.MATCH_PARENT;
    private int dividerLayoutHeight ;

    protected boolean topDividerShown;
    protected boolean bottomDividerShown;

    public FormItem(Context context) {
        super(context);
        if(!initExecuted){
            initExecuted = true;
            init(context, null);
        }
    }

    public FormItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    public FormItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FormItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    protected void init(Context context, AttributeSet attrs){
        this.context = context;
        inflater = LayoutInflater.from(context);
        View content = inflater.inflate(R.layout.view_form_item, this, false);
        ivIcon = (ImageView) content.findViewById(R.id.form_item_icon);
        tvTitle = (TextView) content.findViewById(R.id.form_item_title);
        tvValue = (TextView) content.findViewById(R.id.form_item_value);
        ivArrow = (ImageView) content.findViewById(R.id.form_item_arrow);
        ivArrow.setImageResource(R.drawable.btn_go);

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
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FormItem);
        if(typedArray != null){
            Drawable drawable = typedArray.getDrawable(R.styleable.FormItem_form_icon);
            if(drawable != null){
                ivIcon.setImageDrawable(drawable);
            }else {
                //set default
                ivIcon.setImageResource(R.drawable.ic_launcher);
            }

            String string = typedArray.getString(R.styleable.FormItem_form_title);
            if(string != null){
                tvTitle.setText(string);
            }

            string = typedArray.getString(R.styleable.FormItem_form_value);
            if(string != null){
                tvValue.setText(string);
            }

            string = typedArray.getString(R.styleable.FormItem_form_tips);
            if(string != null){
                tvValue.setHint(string);
            }

            topDividerShown = typedArray.getBoolean(R.styleable.FormItem_topLine_shown, true);
            bottomDividerShown = typedArray.getBoolean(R.styleable.FormItem_bottomLine_shown, true);
        }
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

    public void setValue(String value){
        if(tvValue != null){
            tvValue.setText(value);
        }
    }

    public void setValue(int txtId){
        if(tvValue != null){
            tvValue.setText(txtId);
        }
    }
}
