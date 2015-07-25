package com.parttime.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.LocalInitActivity;
import com.parttime.base.WithTitleActivity;
import com.parttime.net.BaseRequest;
import com.parttime.net.Callback;
import com.parttime.net.ErrorHandler;
import com.parttime.widget.CountingEditText;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.volley.VolleySington;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cjz on 2015/7/14.
 */
public class SuggestionActivity extends LocalInitActivity implements Callback, TextWatcher{

    @ViewInject(R.id.cet_suggestion)
    private CountingEditText cetSuggest;
    @ViewInject(R.id.btn_send)
    private Button btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_suggestion);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        super.initViews();
        center(R.string.suggestion);
        left(TextView.class, R.string.back);

        cetSuggest.addTextChangedListener(this);
    }

    @OnClick(R.id.btn_send)
    public void send(View v){
        showWait(true);
        Map<String, String> params = new HashMap<String, String>();
        params.put("company_id", getCompanyId());
        params.put("content", cetSuggest.getText().toString());
        new BaseRequest().request(Url.SUGGEST, params, VolleySington.getInstance().getRequestQueue(), this);
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

    @Override
    public void success(Object obj) {
        showWait(false);
        finish();
    }

    @Override
    public void failed(Object obj) {
        showWait(false);
        new ErrorHandler(SuggestionActivity.this, obj).showToast();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length() > 0){
            btnSend.setEnabled(true);
        }else {
            btnSend.setEnabled(false);
        }
    }
}
