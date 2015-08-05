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

    protected View topDivider;
    protected View bottomDivider;

    protected LayoutInflater inflater;
    protected Context context;
    private boolean initExecuted;


    private int dividerLayoutWidth = LayoutParams.MATCH_PARENT;
    private int dividerLayoutHeight ;

    protected boolean topDividerShown;
    protected boolean bottomDividerShown;

    protected boolean topDividerLeftIndent;
    protected boolean topDividerRightIndent;
    protected boolean bottomDividerLeftIndent;
    protected boolean bottomDividerRightIndent;
    protected int topDividerLeftIndentValue;
    protected int topDividerRightIndentValue;
    protected int bottomDividerLeftIndentValue;
    protected int bottomDividerRightIndentValue;

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

    public void setTopDividerIndent(boolean topLeft, boolean topRight){
        topDividerLeftIndent = topLeft;
        topDividerRightIndent = topRight;
        updateTopDivider();
    }

    public void setBottomDividerIndent(boolean bottomLeft, boolean bottomRight){
        bottomDividerLeftIndent = bottomLeft;
        bottomDividerRightIndent = bottomRight;
        updateBottomDivider();
    }

    protected void updateTopDivider(){
        if(topDivider != null){
            FrameLayout.LayoutParams fllp = (LayoutParams) topDivider.getLayoutParams();
            if(topDividerLeftIndent){
                fllp.leftMargin = topDividerLeftIndentValue;
            }
            if(topDividerRightIndent){
                fllp.rightMargin = topDividerRightIndentValue;
            }
        }
    }

    protected void updateBottomDivider(){
        if(bottomDivider != null){
            FrameLayout.LayoutParams fllp = (LayoutParams) bottomDivider.getLayoutParams();
            if(bottomDividerLeftIndent){
                fllp.leftMargin = bottomDividerLeftIndentValue;
            }
            if(bottomDividerRightIndent){
                fllp.rightMargin = bottomDividerRightIndentValue;
            }
        }
    }

    public void showTopDivider(){
        if(topDivider == null){
            topDivider = makeTopDivider();
            addView(topDivider);
        }else {
            topDivider.setVisibility(View.VISIBLE);
        }

    }

    public void hideTopDivider(){
        if(topDivider != null){
            topDivider.setVisibility(View.GONE);
        }
    }

    public void showBottomDivider(){
        if(bottomDivider == null){
            bottomDivider = makeBotttomDidider();
            addView(bottomDivider);
        }else {
            bottomDivider.setVisibility(View.VISIBLE);
        }
    }

    public void hideBottomDivider(){
        if(bottomDivider != null){
            bottomDivider.setVisibility(View.GONE);
        }
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
        topDividerLeftIndentValue = topDividerRightIndentValue = bottomDividerLeftIndentValue = bottomDividerRightIndentValue = context.getResources().getDimensionPixelSize(R.dimen.form_item_margin_left);
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

            topDividerLeftIndent = typedArray.getBoolean(R.styleable.FormItem_topLine_leftIndent, topDividerLeftIndent);
            topDividerRightIndent = typedArray.getBoolean(R.styleable.FormItem_topLine_rightIndent, topDividerRightIndent);
            bottomDividerLeftIndent = typedArray.getBoolean(R.styleable.FormItem_bottomLine_leftIndent, bottomDividerLeftIndent);
            bottomDividerRightIndent = typedArray.getBoolean(R.styleable.FormItem_bottomLine_rightIndent, bottomDividerRightIndent);

            topDividerLeftIndentValue = topDividerRightIndentValue
                    = bottomDividerLeftIndentValue = bottomDividerRightIndentValue = typedArray.getDimensionPixelSize(R.styleable.FormItem_indentValue, topDividerLeftIndentValue);
            topDividerLeftIndentValue = typedArray.getDimensionPixelSize(R.styleable.FormItem_topLine_leftIndentValue, topDividerLeftIndentValue);
            topDividerRightIndentValue = typedArray.getDimensionPixelSize(R.styleable.FormItem_topLine_rightIndentValue, topDividerRightIndentValue);
            bottomDividerLeftIndentValue = typedArray.getDimensionPixelSize(R.styleable.FormItem_bottomLine_leftIndentValue, bottomDividerLeftIndentValue);
            bottomDividerRightIndentValue = typedArray.getDimensionPixelSize(R.styleable.FormItem_bottomLine_rightIndentValue, bottomDividerRightIndentValue);

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
        if(topDividerLeftIndent){
            fllp.leftMargin = topDividerLeftIndentValue;
        }
        if(topDividerRightIndent){
            fllp.rightMargin = topDividerRightIndentValue;
        }
        divider.setLayoutParams(fllp);
        return divider;
    }

    private View makeBotttomDidider(){
        View divider = makeDivider();
        FrameLayout.LayoutParams fllp = new FrameLayout.LayoutParams(dividerLayoutWidth, dividerLayoutHeight);
        fllp.gravity = Gravity.BOTTOM;
        if(bottomDividerLeftIndent){
            fllp.leftMargin = bottomDividerLeftIndentValue;
        }
        if(bottomDividerRightIndent){
            fllp.rightMargin = bottomDividerRightIndentValue;
        }
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
