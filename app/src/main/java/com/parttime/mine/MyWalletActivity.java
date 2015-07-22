package com.parttime.mine;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.parttime.base.WithTitleActivity;
import com.parttime.constants.SharedPreferenceConstants;
import com.parttime.net.BaseRequest;
import com.parttime.net.Callback;
import com.parttime.pojo.WalletItem;
import com.parttime.utils.SharePreferenceUtil;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.volley.VolleySington;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;

/**
 * Created by cjz on 2015/7/14.
 */
public class MyWalletActivity extends WithTitleActivity implements XListView.IXListViewListener{
    private static final int PAGE_SIZE = 10;
    @ViewInject(R.id.tv_balance)
    private TextView tvBalance;
    @ViewInject(R.id.tv_recharge)
    private TextView tvRecharge;
    @ViewInject(R.id.xlv)
    private XListView xlv;

    private String cId;
    private int pageIndex = 1;


    private Callback cbAdd = new Callback() {
        @Override
        public void success(Object obj) {

        }

        @Override
        public void failed(Object obj) {

        }
    };

    private Callback cbAppend = new Callback() {
        @Override
        public void success(Object obj) {

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



    private void loadLocalData(){
        cId = SharePreferenceUtil.getInstance(this).loadStringSharedPreference(SharedPreferenceConstants.COMPANY_ID);
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

    @Override
    protected void initViews() {
        super.initViews();
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        left(TextView.class, R.string.back);
        center(R.string.myWallet);

        xlv.setXListViewListener(this);
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
            return null;
        }

        private class ViewHolder {
            public TextView tvTitle;
            public TextView tvType;
            public TextView tvTime;
            public TextView tvStatus;
        }
    }
}
