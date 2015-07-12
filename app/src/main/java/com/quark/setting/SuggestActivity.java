package com.quark.setting;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.jianzhidaren.MainCompanyActivity;
import com.quark.ui.widget.CommonWidget;
import com.quark.ui.widget.CustomDialog;

/**
 * 反馈 建议
 * 
 * @author Administrator
 * 
 */
public class SuggestActivity extends BaseActivity {

	private ImageButton imageBtn;
	private TextView zi_number;
	EditText editText;
	String url;
	String content;
	String user_id;
	String roleIdStr;
	private Button submitBtn;//

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
		setContentView(R.layout.setting_suggest);
		submitBtn = (Button) findViewById(R.id.regin);
		ViewUtils.inject(this);
		SharedPreferences sp = getSharedPreferences("jrdr.setting",
				MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		url = Url.COMPANY_coment + "?token=" + MainCompanyActivity.token;
		roleIdStr = "company_id";
		RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
		topLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));
		imageBtn = (ImageButton) findViewById(R.id.back);
		CommonWidget.back(imageBtn, this);
		zi_number = (TextView) findViewById(R.id.zi_number);
		editText = (EditText) findViewById(R.id.content);
		editText.addTextChangedListener(textWatcher);
	}

	public void showAlertDialog(String str, final String str2) {
		CustomDialog.Builder builder = new CustomDialog.Builder(
				SuggestActivity.this);
		builder.setMessage(str);
		builder.setTitle(str2);
		if (str2.equals("发送成功")) {
			builder.setPositiveButton("加油吧",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							if (str2.equals("发送成功")) {
								finish();
							}
						}
					});
		} else {
			builder.setPositiveButton("确 定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							submitBtn.setClickable(true);
						}
					});
		}
		builder.create().show();
	}

	@OnClick(R.id.regin)
	public void sbOnclick(View v) {
		content = editText.getText().toString();
		if (check()) {
			submitBtn.setClickable(false);
			StringRequest requestss = new StringRequest(Request.Method.POST,
					url, new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							try {
								JSONObject sjonds = new JSONObject(response);
								JSONObject sjond = sjonds
										.getJSONObject("ResponseStatus");
								int status = sjond.getInt("status");
								if (status == 2) {
									showAlertDialog(
											"非常感谢您对兼职达人团队提供的宝贵意见，我们会不断努力的！",
											"发送成功");
								} else {
									showToast("您的账户不存在或者被列为黑户");
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							showAlertDialog("您的网络不够给力，再试一次吧！", "提交失败");
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					Map<String, String> map = new HashMap<String, String>();
					map.put(roleIdStr, user_id);
					map.put("content", content);
					map.put("os", "android(" + android.os.Build.VERSION.RELEASE
							+ ")");
					map.put("phone_type", android.os.Build.MODEL + "");
					return map;
				}
			};
			queue.add(requestss);
			requestss.setRetryPolicy(new DefaultRetryPolicy(
					ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

		}

	}

	//
	TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			zi_number.setText(editText.getText().toString().length() + "/200");
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};

	public boolean check() {
		if (content == null || content.trim().equals("")) {
			showToast("请输入评论内容");
			return false;
		}
		return true;
	}
}
