package com.quark.fragment.company;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.parttime.login.SetGenderActivity;
import com.qingmu.jianzhidaren.R;
import com.quark.common.ToastUtil;
import com.quark.common.ValidateHelper;
import com.quark.image.ImageWorker;
import com.quark.utils.WaitDialog;
import com.quark.volley.VolleySington;

/**
 * 除了首页的4个fragemnt 其他Fragment 不要继承一BaseFragmetn. 如果要统一化，非要继承 修改 onAttach 中的，
 * mActivity = (MainActivity) activity;在其他页面也要做相应修改 A simple {@link android.app.Fragment}
 * subclass.
 */
public abstract class BaseSupportFragment extends Fragment {

    // ImageWorker 初始化如果不自己传入参数，将使用这个默认参数
    protected static final int DEFAULT_IMAGE_WIDTH = 128;
    protected static final int DEFAULT_IMAGE_HIGHT = 128;
    // protected static final int DEFAULT_IMAGE_RESID =
    // R.drawable.default_avatar;
    protected Resources res;
    protected WaitDialog dialog;
    protected RequestQueue queue = VolleySington.getInstance()
            .getRequestQueue();
    protected ImageWorker mImageWorker;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        res = activity.getResources();
    }

    protected void setTitle(View view, String title) {
        ((TextView) view.findViewById(R.id.title)).setText(title);
    }

    protected void setTitle(View view, int titleId) {
        String title = res.getString(titleId);
        setTitle(view, title);
    }

    protected void toastLong(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    protected void toastLong(int resId) {
        String text = res.getString(resId);
        toastLong(text);
    }

    protected void toastShort(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    protected void toastShort(int resId) {
        String text = res.getString(resId);
        toastShort(text);
    }

    protected void showWait(boolean isShow) {
        if (isShow) {
            if (null == dialog) {
                dialog = new WaitDialog(getActivity());
            }
            dialog.show();
        } else {
            if (null != dialog) {
                dialog.dismiss();
            }
        }
    }

    /**
     * Toast 方法
     *
     * @param msg
     */
    public SetGenderActivity.RegParams showToast(String msg) {
        if (ValidateHelper.isEmptyString(msg)) {
            return null;
        }

        ToastUtil.showShortToast(msg);
        return null;
    }

    public void showToast(int resid) {
        ToastUtil.showShortToast(resid);
    }
}
