package com.quark.fragment.company;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingmu.jianzhidaren.R;

/**
 * 商家  圈子
 * @author howe
 *
 */
public class ShareFragment extends BaseFragment  {

    private static final String TAG = "ShareFragment";

    public static ShareFragment newInstance(String param1, String param2) {
        ShareFragment fragment = new ShareFragment();
        return fragment;
    }

    public ShareFragment() {
        
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        setTitle(view, R.string.quanzi);
       
        return view;
    }

   


}
