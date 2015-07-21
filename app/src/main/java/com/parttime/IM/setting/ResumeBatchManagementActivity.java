package com.parttime.IM.setting;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.parttime.common.Image.ContactImageLoader;
import com.parttime.common.head.ActivityHead2;
import com.parttime.net.GroupSettingRequest;
import com.parttime.utils.SharePreferenceUtil;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.utils.NetWorkCheck;
import com.quark.volley.VolleySington;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ResumeBatchManagementActivity extends BaseActivity implements View.OnClickListener {

    private ActivityHead2 headView;
    private ListView listView ;
    private Button pass, refused;

    private BatchAdapter adapter = new BatchAdapter();

    private ArrayList<GroupSettingRequest.UserVO> data = new ArrayList<>();
    private HashMap<Integer,GroupSettingRequest.UserVO> checkedMap = new HashMap<>();

    protected RequestQueue queue = VolleySington.getInstance().getRequestQueue();
    private SharePreferenceUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_batch_management);

        sp = SharePreferenceUtil.getInstance(ApplicationControl.getInstance());

        initView() ;

        bindData() ;
    }

    private void initView() {
        headView = new ActivityHead2(this);
        headView.setCenterTxt1(R.string.group_setting_batch);
        headView.setImgRight2(R.drawable.settings_selected);
        headView.imgRight2.setOnClickListener(this);
        pass = (Button)findViewById(R.id.pass);
        refused = (Button)findViewById(R.id.refused);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        pass.setOnClickListener(this);
        refused.setOnClickListener(this);
    }

    private void bindData() {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pass:
                pass();
                break;
            case R.id.refused:
                refused();
                break;
            case R.id.img_right2:
                //全选
                int size = data.size();
                for(int i = 0 ; i < size ; i ++ ){
                    GroupSettingRequest.UserVO userVO = data.get(i);
                    checkedMap.put(i, userVO);
                }
                break;
        }
    }

    private void pass() {
        //检查网络
        if (! NetWorkCheck.isOpenNetwork(this)) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_net_tip), Toast.LENGTH_SHORT).show();
        }

        //检查选中数量
        if(checkedMap.size() == 0){
            Toast.makeText(this,R.string.pls_check_first,Toast.LENGTH_LONG).show();
            return ;
        }

        Collection<GroupSettingRequest.UserVO> userVOs = checkedMap.values();

        //发送到服务器，调用通过接口

        //成功之后，刷新列表
        data.removeAll(userVOs);
        adapter.notifyDataSetChanged();
    }

    private void refused() {
        //检查网络
        if (! NetWorkCheck.isOpenNetwork(this)) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_net_tip), Toast.LENGTH_SHORT).show();
        }

        //检查选中数量
        if(checkedMap.size() == 0){
            Toast.makeText(this,R.string.pls_check_first,Toast.LENGTH_LONG).show();
            return ;
        }

        Collection<GroupSettingRequest.UserVO> userVOs = checkedMap.values();

        //发送到服务器,提出群组

        //成功之后，刷新列表
        data.removeAll(userVOs);
        adapter.notifyDataSetChanged();
    }


    private class BatchAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public GroupSettingRequest.UserVO getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view ;
            ViewHolder holder;
            if(convertView == null){
                view = getLayoutInflater().inflate(R.layout.activity_group_setting_item,parent,false);

                holder = new ViewHolder();
                holder.head = (ImageView) view.findViewById(R.id.head);
                holder.name = (TextView) view.findViewById(R.id.name);
                holder.resumeStatus = (TextView) view.findViewById(R.id.resume_status);
                holder.moneyAccountStatus = (TextView) view.findViewById(R.id.money_account_status);
                holder.reputationValueStar = (LinearLayout) view.findViewById(R.id.reputation_value_star_container);
                holder.checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                holder.checkBox.setOnClickListener(checkBoxClick);

            }else{
                view = convertView;
                holder = (ViewHolder)convertView.getTag();
            }

            bindData(position, holder, view);

            return view;
        }

        private void bindData(int position, ViewHolder holder, View view) {
            GroupSettingRequest.UserVO userVO = getItem(position);
            if(userVO == null){
                view.setVisibility(View.GONE);
            }else{
                view.setVisibility(View.VISIBLE);
            }

            //设置头像
            String head = userVO.picture;
            if (! TextUtils.isEmpty(head)) {
                // 默认加载本地图片
                ContactImageLoader.loadNativePhoto(String.valueOf(userVO.userId),
                        head, holder.head, queue);
                sp.loadStringSharedPreference(userVO.userId
                        + "realname", userVO.name);
            } else {
                holder.head.setImageResource(R.drawable.default_avatar);
            }

            holder.name.setText(userVO.name);

            holder.checkBox.setTag(position);
            //int moneyStatus = userVO.moneyStatus;
            //int accountStatus = userVO.accountStatus;

            String creditworthiness = userVO.creditworthiness;

        }
    }

    private View.OnClickListener checkBoxClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int)v.getTag();
            GroupSettingRequest.UserVO userVO = adapter.getItem(position);
            if(v instanceof  CheckBox){
                CheckBox cb = (CheckBox)v;
                if(cb.isChecked()){
                    checkedMap.remove(position);
                }else{
                    checkedMap.put(position, userVO);
                }
            }
        }
    };

    private class ViewHolder{
        public ImageView head; //头像
        public TextView name, //名字
                resumeStatus, //简历状态
                moneyAccountStatus; // 诚意金/实名认证
        public LinearLayout reputationValueStar; //信誉值
        public CheckBox checkBox;

    }

}
