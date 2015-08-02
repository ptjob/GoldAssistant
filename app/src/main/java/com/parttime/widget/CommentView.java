package com.parttime.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.ApplicationControl;

/**
 *
 * Created by dehua on 15/8/2.
 */
public class CommentView extends  LinearLayout{

    private boolean initExecuted;
    protected LayoutInflater inflater;
    protected Context context;

    private TextView commentStatus,
                groupName,
                remark;

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
        View view = inflater.inflate(R.layout.comment_item,this, false);
        commentStatus = (TextView)view.findViewById(R.id.comment_status);
        groupName = (TextView)view.findViewById(R.id.groupName);
        remark = (TextView)view.findViewById(R.id.remark);

        addView(view);

    }

    public void bindData(String comment, String groupName, String remark){
        commentStatus.setText(comment);
        ApplicationControl application = ApplicationControl.getInstance();
        if(application.getString(R.string.comment_excellent).equals(comment)){
            commentStatus.setBackgroundResource(R.color.comment_detail_execllent);
        }else if(application.getString(R.string.comment_good).equals(comment)){
            commentStatus.setBackgroundResource(R.color.comment_detail_good);
        }else if(application.getString(R.string.comment_bad).equals(comment)){
            commentStatus.setBackgroundResource(R.color.comment_detail_bad);
        }else if(application.getString(R.string.comment_fly).equals(comment)){
            commentStatus.setBackgroundResource(R.color.comment_detail_fly);
        }else{
            commentStatus.setBackgroundResource(R.color.comment_detail_fly);
        }
        this.groupName.setText(groupName);
        this.remark.setText(remark);
    }


}
