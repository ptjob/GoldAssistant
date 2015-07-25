package com.parttime.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parttime.utils.AndroidUtils;
import com.parttime.widget.wheel.OnWheelChangedListener;
import com.parttime.widget.wheel.WheelView;
import com.parttime.widget.wheel.adapters.ArrayWheelAdapter;
import com.qingmu.jianzhidaren.R;

/**
 * 滚轮选择对话框
 * Created by wyw on 2015/4/21.
 */
public class WheelDialog extends Dialog implements View.OnClickListener, OnWheelChangedListener {

    private LinearLayout llWheelHouse;
    protected WheelView[] wheels;
    private TextView tvOk;
    private TextView tvCanel;
    private TextView tvTitle;
    private boolean isCycle = false;

    private OnSubmitListener onSubmitListener;
    private OnClickListener onWheelCancelListener;

    public WheelDialog(Context context, String[]...txts) {
        super(context, R.style.Dialog);
        Window window = getWindow();
        // 设置显示动画
        if (Build.VERSION.SDK_INT >= 14) {
            window.setDimAmount(0.5f);
        }

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
//        WindowManager.LayoutParams wl = window.getAttributes();
//
//        wl.x = 0;
//        wl.y = dm.heightPixels;
//        // 设置显示位置
//        onWindowAttributesChanged(wl);
        // 设置点击外围解散
        setCanceledOnTouchOutside(true);

        LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(getContext());
        View view = (LinearLayout) inflater.inflate(R.layout.dialog_wheel_template, null);

        initViews(view);

        setContentView(view);

        attachWheels(txts);

    }

    /*public WheelDialog(Context context, int[]...txtResIds) {

    }*/

    protected void attachWheels(String[]...labelss){
        int count = labelss.length;
        int index = 0;
        if(count > 0) {
            wheels = new WheelView[count];
            for (String[] labels : labelss) {
                wheels[index++] = addWheel(labels);
            }
        }
    }

    protected void attachWheels(Object[] tags, String[]...labelss){
        int count = labelss.length;
        int index = 0;
        llWheelHouse.removeAllViews();
        if(count > 0) {
            wheels = new WheelView[count];
            for (String[] labels : labelss) {
                WheelView wheelView = addWheel(labels);
                wheelView.setTag(tags[index]);
                wheels[index++] = wheelView;
            }
        }
    }

    protected void resetWheels(Object[] tags, String[]...labelss) {
        int childCount = llWheelHouse.getChildCount();
        int j = 0;
        Resources res = getContext().getResources();
        for (int i = 0; i < childCount; i++) {
            WheelView childAt = (WheelView) llWheelHouse.getChildAt(i);
            if (childAt.getTag() == tags[j]) {
                ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(getContext(), labelss[j]);
                adapter.setTextSize(res.getDimensionPixelSize(R.dimen.time_picker_time_item_text_size));
                childAt.setViewAdapter(adapter);
                j++;

                if (j >= tags.length) {
                    break;
                }
            }
        }
    }

    protected WheelView addWheel(String[] labels){
        WheelView wheelView = new WheelView(getContext());
        Resources res = getContext().getResources();
        LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(0, res.getDimensionPixelSize(R.dimen.time_picker_time_area_height));
        lllp.weight = 1;
        llWheelHouse.addView(wheelView, lllp);
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(getContext(), labels);
        adapter.setTextSize(res.getDimensionPixelSize(R.dimen.time_picker_time_item_text_size));
        wheelView.setViewAdapter(adapter);
        wheelView.setCyclic(isCycle);
        return wheelView;
    }

    protected void initViews(View root){
        llWheelHouse = (LinearLayout) root.findViewById(R.id.ll_wheel_container);
        tvCanel = (TextView) root.findViewById(R.id.txt_cancel);
        tvOk = (TextView) root.findViewById(R.id.txt_ok);
        tvTitle = (TextView) root.findViewById(R.id.tv_title);

        tvCanel.setOnClickListener(this);
        tvOk.setOnClickListener(this);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_cancel:
                onCancel();
                break;
            case R.id.txt_ok:
                onOk();
                break;

        }
    }

    protected void onOk(){
        int count = wheels.length;
        int[] pos = new int[count];
        for(int i = 0 ; i < count; ++i){
            pos[i] = wheels[i].getCurrentItem();
        }
        if(onSubmitListener != null){
            onSubmitListener.onSubmit(pos);
        }
        dismiss();
    }

    protected void onCancel() {
        dismiss();
        if (onWheelCancelListener != null) {
            onWheelCancelListener.onClick(this, 0);
        }
    }


    public void setTitle(String title){
        if(tvTitle != null){
            tvTitle.setText(title);
        }
    }

    public void setTitle(int title){
        if(tvTitle != null){
            tvTitle.setText(title);
        }
    }

    public void setLeftBtnTxt(String txt){
        if(tvCanel != null){
            tvCanel.setText(txt);
        }
    }

    public void setLeftBtnTxt(int txtId){
        if(tvCanel != null){
            tvCanel.setText(txtId);
        }
    }

    public void setRightBtnTxt(String txt){
        if(tvOk != null){
            tvOk.setText(txt);
        }
    }

    public void setRightBtnTxt(int txtId){
        if(tvOk != null){
            tvOk.setText(txtId);
        }
    }

    public void setOnSubmitListener(OnSubmitListener onSubmitListener){
        this.onSubmitListener = onSubmitListener;
    }

    public void setOnWheelCancelListener(OnClickListener onWheelCancelListener) {
        this.onWheelCancelListener = onWheelCancelListener;
    }

    public void setCurrentPosition(int...pos){
        for(int i = 0; i < wheels.length && i < pos.length; ++i){
            wheels[i].setCurrentItem(pos[i], false);
        }
    }

    public static interface OnSubmitListener {
        void onSubmit(int... pos);
    }

    public void setCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }
}
