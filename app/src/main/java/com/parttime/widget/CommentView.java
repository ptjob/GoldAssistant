package com.parttime.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.qingmu.jianzhidaren.R;

/**
 * Created by dehua on 15/8/2.
 */
public class CommentView extends  LinearLayout{

    private boolean initExecuted;
    protected LayoutInflater inflater;
    protected Context context;

    public CommentView(Context context) {
        super(context);
        if(!initExecuted){
            initExecuted = true;
            init(context, null);
        }
    }

    public CommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    public CommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    protected void init(Context context, AttributeSet attrs){
        this.context = context;
        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.comment_item,null, false);
    }

}
