package com.quark.us;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.carson.https.HttpsUtils;
import com.qingmu.jianzhidaren.R;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.localslideshowview.LocalSlideShowView;
import com.quark.ui.widget.CommonWidget;
import com.quark.ui.widget.CustomDialog;
import com.thirdparty.alipay.RechargeCYJActivity;

/**
 * 关于信誉值和诚意金
 * 
 * @author cluo
 * 
 */
public class LocalCarouselActivity extends BaseActivity {

	private ImageButton imageBtn;
	private LocalSlideShowView mSlideShowView;
	private Button liaojie;
	private TextView title;
	private String type;
	private String user_id;
	private final static String number = "0755-23742220";
	private SharedPreferences sp;
	private Button jiaonaCyjBtn, quhuiCyjBtn;// 缴纳取回诚意金
	private String take_money_url;// 取回诚意金

	@Override
	protected void onResume() {
		super.onResume();
		// sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		// user_id = sp.getString("userId", "");
		// if (jiaonaCyjBtn != null && quhuiCyjBtn != null) {
		// // 先判断是否已交诚意金
		// if (!sp.getBoolean(user_id + "haschengyijin", false)) {
		// // 还没有缴纳诚意金
		// jiaonaCyjBtn.setVisibility(View.VISIBLE);
		// quhuiCyjBtn.setVisibility(View.GONE);
		//
		// } else {
		// jiaonaCyjBtn.setVisibility(View.GONE);
		// quhuiCyjBtn.setVisibility(View.VISIBLE);
		// }
		// }
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_re_reput_authority);
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		take_money_url = Url.TAKE_EARNEST_MONEY;
		title = (TextView) findViewById(R.id.title);
		// 返回
		liaojie = (Button) findViewById(R.id.liaojie);
		liaojie.setOnClickListener(linstener);
		// 返回
		imageBtn = (ImageButton) findViewById(R.id.back);
		imageBtn.setOnClickListener(linstener);
		jiaonaCyjBtn = (Button) findViewById(R.id.jiaonachengyijin);
		quhuiCyjBtn = (Button) findViewById(R.id.quhuichengyijin);
		List<Integer> imageUris = new ArrayList<Integer>();
		type = getIntent().getStringExtra("type");
		if (type.equals("1")) {
			title.setText(R.string.done_job);
			/**
			 * 设置图片主要是传入图片路径，可以是int类型的，也 可以像博客中的一样，传入String类型的
			 */
			imageUris.add(R.drawable.intruduction_finishtask_1);
			imageUris.add(R.drawable.intruduction_finishtask_2);
			imageUris.add(R.drawable.intruduction_finishtask_3);
		} else if (type.equals("2")) {
			title.setText(R.string.go_shiming);
			imageUris.add(R.drawable.intruduction_realname_1);
			imageUris.add(R.drawable.intruduction_realname_2);
			imageUris.add(R.drawable.intruduction_realname_3);
			imageUris.add(R.drawable.intruduction_realname_4);
		} else {
			title.setText(R.string.jiaona);
			imageUris.add(R.drawable.intruduction_money_1);
			imageUris.add(R.drawable.intruduction_money_2);
			imageUris.add(R.drawable.intruduction_money_3);
			imageUris.add(R.drawable.intruduction_money_4);
			// 先判断是否已交诚意金
			if (!sp.getBoolean(user_id + "haschengyijin", false)) {
				// 还没有缴纳诚意金
				jiaonaCyjBtn.setVisibility(View.VISIBLE);
				quhuiCyjBtn.setVisibility(View.GONE);
			} else {
				jiaonaCyjBtn.setVisibility(View.GONE);
				quhuiCyjBtn.setVisibility(View.VISIBLE);
			}
			// 缴纳诚意金
			jiaonaCyjBtn.setOnClickListener(cyjOnclick);
			// 打电话
			quhuiCyjBtn.setOnClickListener(callOnclick);

		}

		/**
		 * 获取控件
		 */
		mSlideShowView = (LocalSlideShowView) findViewById(R.id.slideshowView);
		/**
		 * 为控件设置图片
		 */
		mSlideShowView.setImageUris(imageUris);
	}

	OnClickListener linstener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};
	/**
	 * 诚意金充值
	 * 
	 * @param v
	 */
	OnClickListener cyjOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (user_id.equals("")) {
				CommonWidget.showAlertDialog(LocalCarouselActivity.this,
						LocalCarouselActivity.this, "您还没有登陆，注册登陆后才可以查看哦！",
						"温馨提示", "随便看看");
			} else {
				// 判断是否是诚意金用户

				if (!sp.getBoolean(user_id + "haschengyijin", false)) {
					Intent intent = new Intent();
					intent.setClass(LocalCarouselActivity.this,
							RechargeCYJActivity.class);
					startActivity(intent);
				} else {
					ToastUtil.showShortToast("您已是诚意金用户,无需再次缴纳诚意金。");
				}
			}
		}
	};
	/**
	 * 打电话联系客服 取回诚意金 取回诚意金直接将钱充值入钱包，并生成一条充值记录
	 * 
	 * @param v
	 */
	OnClickListener callOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 取回诚意金时先判断是否已认证

			showAlertTakeCyjDialog("取回的诚意金系统将会自动转款至我的钱包。", "取回诚意金");

			// Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
			// + number));
			// LocalCarouselActivity.this.startActivity(intent);
		}
	};

	/**
	 * 取回诚意金弹框提示
	 * 
	 */

	private void showAlertTakeCyjDialog(String str, final String str2) {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);
		builder.setPositiveButton("确定取回",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// take_earnest_money();
					}
				});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				showWait(true);
				break;
			case 1:
				showWait(false);
			case 2:
				jiaonaCyjBtn.setVisibility(View.VISIBLE);
				quhuiCyjBtn.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		};
	};
}
