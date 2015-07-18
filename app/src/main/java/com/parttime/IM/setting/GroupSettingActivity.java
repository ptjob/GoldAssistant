/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.parttime.IM.setting;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.activity.AlertDialog;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.easemob.chatuidemo.activity.EditActivity;
import com.easemob.chatuidemo.activity.ExportGroupListActivity;
import com.easemob.chatuidemo.activity.GroupBlacklistActivity;
import com.easemob.chatuidemo.activity.GroupPickContactsActivity;
import com.easemob.chatuidemo.widget.ExpandGridView;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.NetUtils;
import com.parttime.IM.ChatActivity;
import com.parttime.common.Image.ContactImageLoader;
import com.parttime.net.DefaultCallback;
import com.parttime.net.HuanXinRequest;
import com.parttime.utils.SharePreferenceUtil;
import com.qingmu.jianzhidaren.R;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.http.image.LoadImage;
import com.quark.image.UploadImg;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.model.HuanxinUser;
import com.quark.ui.widget.CustomDialog;
import com.quark.utils.NetWorkCheck;
import com.quark.volley.VolleySington;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: GroupSettingActivity
 * @Description: 群设置
 * @author howe
 * @date 2015-2-12 下午12:19:16
 * 
 */
public class GroupSettingActivity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = "GroupSettingActivity";

	public static GroupSettingActivity instance;

	protected RequestQueue queue;
	private SharePreferenceUtil sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_setting);
		sp = SharePreferenceUtil.getInstance(ApplicationControl.getInstance());

		queue = VolleySington.getInstance().getRequestQueue();
		instance = this;

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

    @Override
    public void onClick(View v) {

    }
}
