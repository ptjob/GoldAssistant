package com.parttime.addresslist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingmu.jianzhidaren.R;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 *
 * Created by dehua on 15/7/28.
 */
public class UserDetailPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> userIds;

    public UserDetailPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(LinkedHashSet<String> userIds){

        this.userIds = new ArrayList<>(userIds);

    }

    @Override
    public Fragment getItem(int position) {
        return UserDetailFragment.newInstance(userIds.get(position));
    }

    @Override
    public int getCount() {
        return userIds.size();
    }

    public static class UserDetailFragment extends Fragment{
        private static final String ARG_USER_ID = "userId";
        private String userId;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static UserDetailFragment newInstance(String userId) {
            UserDetailFragment fragment = new UserDetailFragment();
            Bundle args = new Bundle();
            args.putString(ARG_USER_ID, userId);
            fragment.setArguments(args);
            return fragment;
        }

        public UserDetailFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_user_detail_item, container, false);
            UserDetailViewHelper helper = new UserDetailViewHelper();
            helper.initView(rootView);
            return rootView;
        }

    }

}
