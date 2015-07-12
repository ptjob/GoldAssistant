package com.quark.jianzhidaren;


import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.qingmu.jianzhidaren.R;

public class AgreementActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agreement);
		setBackButton();
		setTopTitle("注册协议");
		WebView webView = (WebView) findViewById(R.id.set);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.requestFocus();
		webView.setHorizontalScrollBarEnabled(false);
		webView.setVerticalScrollBarEnabled(false);
		webView.loadUrl("file:///android_asset/agreement.html");
	}
}
