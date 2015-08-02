package com.parttime.common.activity;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qingmu.jianzhidaren.R;

/**
 *
 * Created by dehua on 15/8/2.
 */
public class EditTextLimitChar {

    private Activity activity ;
    private int MAX_COUNT ;
    private TextView tipView;
    private EditText contentEdTxt;

    public EditTextLimitChar(Activity activity, int maxCount, String initContent,EditText contentEdTxt, TextView tipView){
        this.activity = activity;
        MAX_COUNT = maxCount;
        this.tipView = tipView;
        this.contentEdTxt = contentEdTxt;

        if(initContent == null || initContent.length() == 0){
            tipView.setText(activity.getString(R.string.content_text_count, 0, MAX_COUNT));
        }else{
            contentEdTxt.setText(initContent);
            tipView.setText(activity.getString(R.string.content_text_count, initContent.length(), MAX_COUNT));
        }

        contentEdTxt.addTextChangedListener(editChangeListener);
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
                Toast.makeText(activity,
                        activity.getString(R.string.group_notice_max_count_tip, MAX_COUNT),
                        Toast.LENGTH_SHORT).show();
                return ;
            }

            tipView.setText(activity.getString(R.string.content_text_count, contentLength, MAX_COUNT));

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length() > MAX_COUNT) {
                editStart = contentEdTxt.getSelectionStart();
                editEnd = contentEdTxt.getSelectionEnd();
                s.delete(editStart - 1, editEnd);
                contentEdTxt.setText(s);
                contentEdTxt.setSelection(MAX_COUNT);
            }
        }
    };
}
