package com.parttime.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.LocalInitActivity;
import com.parttime.net.BaseRequest;
import com.parttime.net.Callback;
import com.parttime.pojo.WalletItem;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.volley.VolleySington;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;

/**
 * Created by cjz on 2015/7/14.
 */
public class MyWalletActivity extends LocalInitActivity implements XListView.IXListViewListener{
    private static final int PAGE_SIZE = 10;
    @ViewInject(R.id.tv_balance)
    private TextView tvBalance;
    @ViewInject(R.id.tv_recharge)
    private TextView tvRecharge;
    @ViewInject(R.id.xlv)
    private XListView xlv;

    private int pageIndex = 1;
    private int totalRecord;

    private double balance;
    private List<WalletItem> walletItems = new ArrayList<WalletItem>();
    private MoneyAdapter adapter;


    private Callback cbAdd = new Callback() {
        @Override
        public void success(Object obj) {
            JSONObject json = (JSONObject) obj;
            try {
                balance = json.getDouble("company_money");
                JSONObject billPage = json.getJSONObject("billPage");
                if(billPage != null){
                    pageIndex = billPage.getInt("pageNumber") + 1;
                    int pageSize = billPage.getInt("pageSize");
                    int totalPage = billPage.getInt("totalPage");
                    totalRecord = billPage.getInt("totalRow");
                    JSONArray bills = billPage.getJSONArray("list");
                    if(bills != null){
                        Gson gson = new Gson();
                        String s;
                        WalletItem wi;
                        List<WalletItem> wis = new ArrayList<WalletItem>();
                        for(int i = 0; i < bills.length(); ++i){
                            s = bills.get(i).toString();
                            wi = gson.fromJson(s, WalletItem.class);
                            wis.add(wi);
                        }

                        setDatas(wis, false);
                        updateViews();


                    }
                    int resultLen = bills != null ? billPage.length() : 0;
                    xlv.setLoadOver(resultLen, PAGE_SIZE);
                    xlv.stopRefresh();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                showWait(false);
            }
        }

        @Override
        public void failed(Object obj) {

        }
    };

    private Callback cbAppend = new Callback() {
        @Override
        public void success(Object obj) {
            JSONObject json = (JSONObject) obj;
            try {
                balance = json.getDouble("company_money");
                JSONObject billPage = json.getJSONObject("billPage");
                if(billPage != null){
                    pageIndex = billPage.getInt("pageNumber") + 1;
                    int pageSize = billPage.getInt("pageSize");
                    int totalPage = billPage.getInt("totalPage");
                    totalRecord = billPage.getInt("totalRow");
                    JSONArray bills = billPage.getJSONArray("list");
                    if(bills != null){
                        Gson gson = new Gson();
                        String s;
                        WalletItem wi;
                        List<WalletItem> wis = new ArrayList<WalletItem>();
                        for(int i = 0; i < bills.length(); ++i){
                            s = bills.get(i).toString();
                            wi = gson.fromJson(s, WalletItem.class);
                            wis.add(wi);
                        }
                        setDatas(wis, true);

                        updateViews();
                    }
                    int resultLen = bills != null ? bills.length() : 0;
                    xlv.setLoadOver(resultLen, PAGE_SIZE);
                    xlv.stopLoadMore();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void failed(Object obj) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.acitity_my_wallet);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
        loadLocalData();
        loadData();
    }


    private void setDatas(List<WalletItem> datas,  boolean append){
        if(!append){
            walletItems.clear();
        }
        walletItems.addAll(datas);
        adapter.notifyDataSetChanged();
    }

    private void loadData(){
        showWait(true);
        load(cbAdd);
    }

    private void load(Callback cb){
        Map<String, String> params = new HashMap<String, String>();
        params.put("company_id", cId);
        params.put("pn", pageIndex + "");
        params.put("page_size", PAGE_SIZE + "");
        new BaseRequest().request(Url.MY_BALANCE, params, VolleySington.getInstance().getRequestQueue(), cb);
    }

    private void updateViews(){
        tvBalance.setText(balance + "");
    }


    @OnClick(R.id.tv_recharge)
    public void recharget(View v){
        startActivity(new Intent(this, RechargeActivity.class));
    }

    @Override
    protected void initViews() {
        super.initViews();
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        left(TextView.class, R.string.back);
        center(R.string.myWallet);

        xlv.setXListViewListener(this);
        xlv.setPullLoadEnable(true);
        adapter = new MoneyAdapter(this, walletItems);
        xlv.setAdapter(adapter);
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
        pageIndex = 1;
        load(cbAdd);
    }

    @Override
    public void onLoadMore() {
        load(cbAppend);
    }

    private class MoneyAdapter extends BaseAdapter {
        private Context context;
        private List<WalletItem> datas;
        private LayoutInflater inflater;

        public MoneyAdapter(Context context, List<WalletItem> datas) {
            this.context = context;
            this.datas = datas;
            inflater = LayoutInflater.from(context);
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
                convertView = inflater.inflate(R.layout.item_my_wallet, xlv, false);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
//                holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            WalletItem wi = (WalletItem) getItem(position);
            holder.tvTitle.setText(wi.title);
            if(wi.pay_time != null){
                String[] split = wi.pay_time.split("\\s+");
                if(split != null && split.length > 0){
                    holder.tvTime.setText(split[0]);
                }else {
                    holder.tvTime.setText("");
                }
            }else {
                holder.tvTime.setText("");
            }
            holder.tvType.setText(wi.type == 1 ?  String.format("+%.2f", wi.money) : String.format("-%.2f", wi.money));
            holder.tvType.setSelected(wi.type != 1);
//            holder.tvType.
//            holder.tvType.setText(wi.type);
            return convertView;
        }

        private class ViewHolder {
            public TextView tvTitle;
            public TextView tvType;
            public TextView tvTime;
//            public TextView tvStatus;
        }
    }
}
