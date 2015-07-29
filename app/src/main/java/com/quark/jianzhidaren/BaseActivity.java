package com.quark.jianzhidaren;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.qingmu.jianzhidaren.R;
import com.quark.common.ToastUtil;
import com.quark.common.ValidateHelper;
import com.quark.ui.widget.CustomDialog;
import com.quark.utils.WaitDialog;
import com.quark.volley.VolleySington;
import com.umeng.analytics.MobclickAgent;

/**
 *
 * Created by Administrator on 10/31 0031.
 */
public abstract class BaseActivity extends FragmentActivity {
	public static final String TAG = "com.quark";
	protected TextView title;
	protected Button right;
	protected ImageView back;
	private boolean isInit = false;
	protected RequestQueue queue;
	protected WaitDialog dialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		queue = VolleySington.getInstance().getRequestQueue();
	}

	/**
	 * 设置右边按钮
	 * 
	 * @param id int
	 *            按钮名称
	 * @param listener
	 *            按钮事件
	 */
	public void setRightImage(int id, View.OnClickListener listener) {
		if (id > 0) {
			ImageView right_img = (ImageView) findViewById(R.id.right);
			LinearLayout right_layout = (LinearLayout) findViewById(R.id.right_layout);
			// right_img.setImageResource(id);
			right_img.setVisibility(View.VISIBLE);
			if (null != listener) {
				right_layout.setOnClickListener(listener);
			} else {
				Log.e(TAG, "set right button error! listener is null");
			}
		} else {
			Log.e(TAG, "set right button error! name is null");
		}
	}

	public void setRight(int resId) {
		right.setText(resId);
	}

	public void setTopTitle(String titlestr) {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(titlestr);
	}

	/**
	 * 设置返回按钮
	 */
	public void setBackButton() {

		LinearLayout back_lay = (LinearLayout) findViewById(R.id.left);
		back_lay.setVisibility(View.VISIBLE);
		back_lay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void startActivityByClass(Class clazz, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, clazz);
		if (null != bundle) {
			intent.putExtras(bundle);
		}

		startActivity(intent);
	}

	/**
	 * 从intent获取bundle中得某个数据
	 * 
	 * @param key
	 * @return
	 */
	public Object getValue4Intent(String key) {
		if (ValidateHelper.isEmptyString(key)) {
			return null;
		}

		Bundle b = getIntent().getExtras();

		if (null == b) {
			return null;
		}

		return b.get(key);
	}

	/**
	 * Toast 方法
	 * 
	 * @param msg
	 */
	public void showToast(String msg) {
		if (ValidateHelper.isEmptyString(msg)) {
			return;
		}

		ToastUtil.showShortToast(msg);
	}

	public void showToast(int resid) {
		ToastUtil.showShortToast(resid);
	}

	protected void showWait(final boolean isShow) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    if (null == dialog) {
                        dialog = new WaitDialog(BaseActivity.this);
                    }
                    dialog.show();
                } else {
                    if (null != dialog) {
                        dialog.dismiss();
                    }
                }
            }
        });

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPause(this);// 友盟
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// 友盟
	}


	public CustomDialog createDialog(String title, final String msg, String positive, DialogInterface.OnClickListener positiveClick, String negative, DialogInterface.OnClickListener negativeClick) {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton(positive,
				positiveClick);
		builder.setNegativeButton(negative, negativeClick);
		return builder.create();
	}
}
