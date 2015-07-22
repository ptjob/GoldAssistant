package com.parttime.IM;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chatuidemo.activity.BaseActivity;
import com.parttime.common.head.ActivityHead;
import com.qingmu.jianzhidaren.R;

public class EditGroupNoticeActivity extends BaseActivity implements View.OnClickListener{

    private static final int MAX_COUNT = 10;

    private EditText noticeContent;
    private TextView contentCount;
    private Button done;

    private String groupNotice = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group_notice);

        initValue();

        setValue();

        setListener();
    }


    private void initValue() {
        //设置头部
        ActivityHead activityHead = new ActivityHead(this);
        activityHead.setCenterTxt1(R.string.group_notice);

        noticeContent = (EditText)findViewById(R.id.notice_content);
        contentCount = (TextView)findViewById(R.id.text_count);
        done = (Button)findViewById(R.id.done);

    }

    private void setValue() {
        groupNotice = "";
        noticeContent.setText(groupNotice);
        contentCount.setText(getString(R.string.content_text_count,groupNotice.length(),MAX_COUNT));
    }

    private void setListener(){
        noticeContent.addTextChangedListener(editChangeListener);
        done.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.done:
                Toast.makeText(EditGroupNoticeActivity.this,"commit",Toast.LENGTH_SHORT).show();
                ;
        }
    }

    private TextWatcher editChangeListener = new TextWatcher() {
        private int editStart;//光标开始位置
        private int editEnd;//光标结束位置
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            int contentLength = start + count;
            if(contentLength > MAX_COUNT){
                Toast.makeText(EditGroupNoticeActivity.this, getString(R.string.group_notice_max_count_tip,MAX_COUNT),Toast.LENGTH_SHORT).show();
                return ;
            }

            contentCount.setText(getString(R.string.content_text_count,contentLength,MAX_COUNT));

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length() > MAX_COUNT) {
                editStart = noticeContent.getSelectionStart();
                editEnd = noticeContent.getSelectionEnd();
                s.delete(editStart - 1, editEnd);
                noticeContent.setText(s);
                noticeContent.setSelection(MAX_COUNT);
            }
        }
    };
}
