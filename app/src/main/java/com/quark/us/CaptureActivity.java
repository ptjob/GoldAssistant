package com.quark.us;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.parttime.main.MainTabActivity;
import com.quark.sweep.camera.CameraManager;
import com.quark.sweep.decoding.CaptureActivityHandler;
import com.quark.sweep.decoding.InactivityTimer;
import com.quark.sweep.view.ViewfinderView;
import com.quark.ui.widget.CustomDialog;
import com.quark.utils.WaitDialog;
import com.quark.volley.VolleySington;

/**
 * Initial the camera
 * 
 * @author C罗 扫一扫
 */
public class CaptureActivity extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private Button cancelScanButton;
	private String signUp_url, user_id, activity_id;
	protected WaitDialog dialog;
	protected RequestQueue queue = VolleySington.getInstance()
			.getRequestQueue();
	private int status;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
		SharedPreferences sp = getSharedPreferences("jrdr.setting",
				Activity.MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		signUp_url = Url.USE_signUp + "?token=" + MainTabActivity.token;
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

		// quit the scan view
		cancelScanButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CaptureActivity.this.finish();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * Handler scan result
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(final Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();

		// 扫描结果
		if (resultString == null || resultString.equals("")) {
			Toast.makeText(CaptureActivity.this, "签到失败!", Toast.LENGTH_SHORT)
					.show();
		} else {
			activity_id = resultString;
			int temp = 0;
			try {
				temp = Integer.parseInt(resultString);
			} catch (Exception e) {
				temp = 0;
			}
			if (temp > 0) {
				signUp();
			} else {
				Toast.makeText(CaptureActivity.this,
						"您扫描了无效二维码，需要找商家扫描活动二维码才可以成功签到哦", Toast.LENGTH_SHORT)
						.show();
			}
		}

	}

	// 签到
	private void signUp() {
		showWait(true);
		StringRequest stringRequest = new StringRequest(Method.POST,
				signUp_url, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js.getJSONObject("responseStatus");
							status = jss.getInt("status");
							String msg = jss.getString("msg");
							String title = jss.getString("title");
							String confirm = jss.getString("confirm");
							getSignUp(msg, title, confirm);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						showWait(false);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("activity_id", activity_id);
				map.put("user_id", user_id);
				return map;
			}
		};
		queue.add(stringRequest);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	// 签到结果
	protected void getSignUp(String msg, String title, String confirm) {
		// TODO Auto-generated method stub
		if (status == 1) {
			showAlertDialog(msg, title, confirm);
		} else {
			showAlertDialog2(msg, title, confirm);
		}
	}

	// 弹窗
	public void showAlertDialog(String str, final String str2, String str3) {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);

		builder.setPositiveButton(str3, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				CaptureActivity.this.finish();
			}
		});
		builder.create().show();
	}

	public void showAlertDialog2(String str, final String str2, String str3) {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);

		builder.setPositiveButton(str3, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				CaptureActivity.this.finish();
			}
		});
		builder.create().show();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	protected void showWait(boolean isShow) {
		if (isShow) {
			if (null == dialog) {
				dialog = new WaitDialog(CaptureActivity.this);
			}
			dialog.show();
		} else {
			if (null != dialog) {
				dialog.dismiss();
			}
		}
	}

}