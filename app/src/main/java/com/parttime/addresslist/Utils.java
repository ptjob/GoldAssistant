package com.parttime.addresslist;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by dehua on 15/7/31.
 */
public class Utils {

    public static void addStars(String creditworthiness,LinearLayout container, Activity activity, int drawableResId){
        int cre = Integer.valueOf(creditworthiness);
        addStars(cre, container, activity, drawableResId);

    }

    public static void addStars(int cre, LinearLayout container, Activity activity, int drawableResId) {
        container.removeAllViews();
        int num = (int)Math.round(cre * 1.0 / 10);
        for(int i = 0 ; i < num; i ++){
            container.addView(newStar(activity,drawableResId));
        }
    }


    private static ImageView newStar(Activity activity, int drawableResId){
        ImageView star = new ImageView(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        star.setLayoutParams(params);
        star.setImageResource(drawableResId);
        return star;
    }
}
