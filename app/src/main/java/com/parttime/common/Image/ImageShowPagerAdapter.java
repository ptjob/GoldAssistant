package com.parttime.common.Image;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.parttime.net.DefaultCallback;
import com.parttime.net.UserDetailRequest;
import com.parttime.pojo.UserDetailVO;
import com.qingmu.jianzhidaren.R;
import com.quark.volley.VolleySington;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Created by dehua on 15/7/28.
 */
public class ImageShowPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> pictures;
    private ArrayList<String> userIds;
    protected HashMap<String, Bitmap> cache = new HashMap<>();
    protected RequestQueue queue = VolleySington.getInstance().getRequestQueue();

    public ImageShowPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(ArrayList<String> pictures,ArrayList<String> userIds){

        this.pictures = new ArrayList<>(pictures);
        this.userIds = new ArrayList<>(userIds);

    }

    @Override
    public Fragment getItem(int position) {
        String picture = pictures.get(position);
        String userId = userIds.get(position);
        UserDetailFragment fragment = UserDetailFragment.newInstance(picture,userId);
        fragment.imageShowPagerAdapter = this;
        return fragment;
    }

    @Override
    public int getCount() {
        return pictures.size();
    }

    public static class UserDetailFragment extends Fragment{
        private static final String ARG_PICTURE = "picture";
        private static final String ARG_USER_ID = "userId";
        protected String picture;
        protected String userId;
        ImageShowPagerAdapter imageShowPagerAdapter;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static UserDetailFragment newInstance(String picture,String userId) {
            UserDetailFragment fragment = new UserDetailFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PICTURE, picture);
            args.putString(ARG_USER_ID, userId);
            fragment.setArguments(args);
            return fragment;
        }

        public UserDetailFragment() {

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            picture = getArguments().getString(ARG_PICTURE);
            userId = getArguments().getString(ARG_USER_ID);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ImageView rootView = (ImageView)inflater.inflate(R.layout.image_show_item, container, false);

            Bitmap bitmap = imageShowPagerAdapter.cache.get(picture);
            if(bitmap != null){
                rootView.setImageBitmap(bitmap);
            }else {
                ContactImageLoader.loadNativePhoto(userId,picture,rootView, imageShowPagerAdapter.queue);
            }
            return rootView;
        }

    }

}
