package com.parttime.mine;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.parttime.base.WithTitleActivity;
import com.parttime.pojo.Fans;
import com.parttime.widget.RankView;
import com.qingmu.jianzhidaren.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.maxwin.view.XListView;

/**
 * Created by cjz on 2015/7/16.
 */
public class MyFansActivity extends WithTitleActivity implements XListView.IXListViewListener{
    @ViewInject(R.id.xlv_my_fans)
    private XListView lv;

    private MyFansAdapter adapter;
    private List<Fans> fanses = new ArrayList<Fans>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_fans);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);

        loadData();
    }


    @Override
    protected void initViews() {
        super.initViews();
        left(TextView.class, R.string.back);
        center(R.string.my_fans);

        lv.setPullLoadEnable(true);
        lv.setXListViewListener(this);
        adapter = new MyFansAdapter(this, fanses);
        lv.setAdapter(adapter);
    }

    private void updateViews(List<Fans> fs){
        fanses.clear();
        if(fs != null){
            fanses.addAll(fs);
        }
        adapter.notifyDataSetChanged();
    }

    private void loadData(){
        showWait(true);
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Fans> fs = new ArrayList<Fans>();
                        Fans f;
                        Random random = new Random();
                        for(int i = 0; i < 20; ++i){
                            f = new Fans();
                            f.user_name = "User-" + i;
                            f.sex = random.nextInt(2);
                            f.earnest_money = random.nextInt(2);
                            f.certification = random.nextInt(4);
                            f.creditworthiness = random.nextInt(60);
                            fs.add(f);
                        }
                        showWait(false);
                        updateViews(fs);
                    }
                });
            }
        }.start();
    }

    @Override
    protected ViewGroup getLeftWrapper() {
        return null;
    }

    @Override
    protected ViewGroup getRightWrapper() {
        return null;
    }

    @Override
    protected TextView getCenter() {
        return null;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    private class MyFansAdapter extends BaseAdapter {

        private Context context;
        private List<Fans> datas;
        private LayoutInflater inflater;
        private String male, female;

        public MyFansAdapter(Context context, List<Fans> datas) {
            this.context = context;
            this.datas = datas;
            inflater = LayoutInflater.from(context);
            male = "(" + context.getString(R.string.male) + ")";
            female = "(" + context.getString(R.string.female) + ")";
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_my_fans, lv, false);
                holder.ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvSincereMoney = (TextView) convertView.findViewById(R.id.tv_sincere_moneys);
                holder.tvCertStatus = (TextView) convertView.findViewById(R.id.tv_cert_status);
                holder.rvCredit = (RankView) convertView.findViewById(R.id.rv_credit);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            Fans fans = (Fans) getItem(position);
            holder.ivHead.setImageResource(R.drawable.ic_launcher);
            holder.tvName.setText(fans.user_name + " " + (fans.sex == 1 ? male : female));
            holder.tvSincereMoney.setText(context.getString(fans.earnest_money == 1 ? R.string.sincere_money_paid : R.string.sincere_money_not_paid));
            holder.tvCertStatus.setText(context.getString(fans.certification == 2 ? R.string.real_name_certed : R.string.real_name_not_certed));
            holder.rvCredit.rank(fans.creditworthiness / 10);
            return convertView;
        }

        private class ViewHolder {
            public ImageView ivHead;
            public TextView tvName;
            public TextView tvSincereMoney;
            public TextView tvCertStatus;
            public TextView tvCredit;
            public RankView rvCredit;
        }
    }
}
