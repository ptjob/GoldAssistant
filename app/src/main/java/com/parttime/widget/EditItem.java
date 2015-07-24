package com.parttime.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;

/**
 * Created by cjz on 2015/7/24.
 */
public class EditItem extends FrameLayout{

    private Context context;
    private LayoutInflater inflater;

    private TextView tvName;
    private EditText etValue;

    private String name;
    private String hint;
    private String value;

    private boolean topDividerShown;
    private boolean bottomDividerShown;

    private int dividerLayoutWidth = LayoutParams.MATCH_PARENT;
    private int dividerLayoutHeight ;

    private boolean initExecuted;
    public EditItem(Context context) {
        super(context);
        if(!initExecuted){
            initExecuted = true;
            init(context, null);
        }
    }

    public EditItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    public EditItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EditItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    private void init(Context context, AttributeSet attrs){
        this.context = context;
        inflater = LayoutInflater.from(context);

        View root = inflater.inflate(R.layout.view_edit_item, this, false);
        tvName = (TextView) root.findViewById(R.id.edit_item_name);
        etValue = (EditText) root.findViewById(R.id.edit_item_value);

        dividerLayoutHeight = context.getResources().getDimensionPixelSize(R.dimen.divider_width);
        topDividerShown = bottomDividerShown = true;

        if(attrs != null){
            assignXmlAttrs(attrs);
        }

        if(name != null){
            tvName.setText(name);
        }
        if(hint != null){
            etValue.setHint(hint);
        }
        if(value != null){
            etValue.setText(value);
        }

        checkDividers();

        addView(root);

    }

    private void assignXmlAttrs(AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditItem);
        if(typedArray != null){
            name = typedArray.getString(R.styleable.EditItem_ei_name);
            hint = typedArray.getString(R.styleable.EditItem_ei_hint);
            value = typedArray.getString(R.styleable.EditItem_ei_value);
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

    public void setName(String txt){
        if(tvName != null){
            tvName.setText(txt);
        }
    }

    public void setName(int txtId){
        if(tvName != null){
            tvName.setText(txtId);
        }
    }

    public void setValue(String value){
        if(etValue != null){
            etValue.setText(value);
        }
    }

    public String getValue(){
        return etValue != null ? etValue.getText().toString() : "";
    }

}
