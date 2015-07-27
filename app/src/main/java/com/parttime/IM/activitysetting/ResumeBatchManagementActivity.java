package com.parttime.IM.activitysetting;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.carson.constant.ConstantForSaveList;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.parttime.addresslist.UserDetailActivity;
import com.parttime.common.Image.ContactImageLoader;
import com.parttime.common.head.ActivityHead2;
import com.parttime.constants.ActivityExtraAndKeys;
import com.parttime.net.DefaultCallback;
import com.parttime.net.GroupSettingRequest;
import com.parttime.net.ResponseBaseCommonError;
import com.parttime.utils.SharePreferenceUtil;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.ui.widget.CustomDialog;
import com.quark.volley.VolleySington;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ResumeBatchManagementActivity extends BaseActivity implements View.OnClickListener {

    private ActivityHead2 headView;
    private ListView listView ;
    private Button pass, refused;

    private BatchAdapter adapter = new BatchAdapter();

    private ArrayList<BatchUserVO> data = new ArrayList<>();
    private HashMap<Integer,BatchUserVO> checkedMap = new HashMap<>();

    private String groupId;

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
        groupId = getIntent().getStringExtra(ActivityExtraAndKeys.GroupSetting.GROUPID);
        GroupSettingRequest.AppliantResult appliantResult = ConstantForSaveList.groupAppliantCache.get(groupId);
        if(appliantResult != null){
            for(GroupSettingRequest.UserVO userVO : appliantResult.userList){
                if(userVO == null){
                    continue;
                }
                if(userVO.apply == GroupSettingRequest.UserVO.APPLY_UNLOOK ||
                        userVO.apply == GroupSettingRequest.UserVO.APPLY_LOOKED){
                    BatchUserVO batchUserVO = new BatchUserVO(userVO);
                    data.add(batchUserVO);
                }
            }
            adapter.notifyDataSetChanged();
        }
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
                showAlertDialog(null , getString(R.string.reject_resume_or_not_and_remove_from_group), R.string.ok, R.string.cancel);
                break;
            case R.id.img_right2:
                //全选
                int size = data.size();
                int checkNum = checkedMap.size() ;
                if(size == checkNum) {
                    return;
                }
                for(int i = 0 ; i < size ; i ++ ){
                    BatchUserVO batchUserVO = data.get(i);
                    batchUserVO.checked = true;
                    checkedMap.put(i, batchUserVO);
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void pass() {

        //检查选中数量
        if(checkedMap.size() == 0){
            Toast.makeText(this,R.string.pls_check_first,Toast.LENGTH_LONG).show();
            return ;
        }

        final Collection<BatchUserVO> batchUserVOs = checkedMap.values();
        ArrayList<Integer> userIds = new ArrayList<>();
        for(BatchUserVO batchUserVO : batchUserVOs){
            if(batchUserVO == null){
                continue;
            }
            userIds.add(batchUserVO.userId);
        }

        showWait(true);
        //发送到服务器，调用通过接口
        new GroupSettingRequest().approve(userIds, groupId, queue, new DefaultCallback(){
            @Override
            public void success(Object obj) {
                //更新缓存
                GroupSettingRequest.AppliantResult appliantResult = ConstantForSaveList.groupAppliantCache.get(groupId);
                if(appliantResult != null) {
                    List<GroupSettingRequest.UserVO> userVOList = appliantResult.userList;
                    if(userVOList != null){
                        for(BatchUserVO batchUserVO : batchUserVOs){
                            if(userVOList.contains(batchUserVO)){
                                userVOList.remove(batchUserVO);
                            }
                        }
                    }
                    appliantResult.approvedCount = (appliantResult.approvedCount + batchUserVOs.size());
                    appliantResult.unApprovedCount = (appliantResult.unApprovedCount - batchUserVOs.size());
                }
                //成功之后，刷新列表
                data.removeAll(batchUserVOs);
                checkedMap.clear();
                adapter.notifyDataSetChanged();
                showWait(false);
            }

            @Override
            public void failed(Object obj) {
                if(obj instanceof ResponseBaseCommonError){
                    Toast.makeText(ResumeBatchManagementActivity.this, ((ResponseBaseCommonError)obj).msg, Toast.LENGTH_SHORT).show();
                }
                showWait(false);
            }
        });


    }

    public void showAlertDialog(String title, String message,
                                int positiveRes, int negativeRes) {

        CustomDialog.Builder builder = new CustomDialog.Builder(ResumeBatchManagementActivity.this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(positiveRes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                refused();
            }
        });

        builder.setNegativeButton(negativeRes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void refused() {

        //检查选中数量
        if(checkedMap.size() == 0){
            Toast.makeText(this,R.string.pls_check_first,Toast.LENGTH_LONG).show();
            return ;
        }

        final Collection<BatchUserVO> batchUserVOs = checkedMap.values();
        ArrayList<Integer> userIds = new ArrayList<>();
        for(BatchUserVO batchUserVO : batchUserVOs){
            if(batchUserVO == null){
                continue;
            }
            userIds.add(batchUserVO.userId);
        }

        showWait(true);
        //发送到服务器,提出群组(服务端处理)
        new GroupSettingRequest().reject(userIds, groupId, queue, new DefaultCallback(){
            @Override
            public void success(Object obj) {
                //更新缓存
                GroupSettingRequest.AppliantResult appliantResult = ConstantForSaveList.groupAppliantCache.get(groupId);
                if(appliantResult != null) {
                    List<GroupSettingRequest.UserVO> userVOList = appliantResult.userList;
                    if(userVOList != null){
                        for(BatchUserVO batchUserVO : batchUserVOs){
                            if(userVOList.contains(batchUserVO)){
                                userVOList.remove(batchUserVO);
                            }
                        }
                    }
                    appliantResult.unApprovedCount = (appliantResult.unApprovedCount - batchUserVOs.size());
                }
                checkedMap.clear();
                //成功之后，刷新列表
                data.removeAll(batchUserVOs);
                adapter.notifyDataSetChanged();
                showWait(false);
            }

            @Override
            public void failed(Object obj) {
                if(obj instanceof ResponseBaseCommonError){
                    Toast.makeText(ResumeBatchManagementActivity.this, ((ResponseBaseCommonError)obj).msg, Toast.LENGTH_SHORT).show();
                }
                showWait(false);
            }
        });

        //成功之后，刷新列表
        data.removeAll(batchUserVOs);
        adapter.notifyDataSetChanged();
    }


    private class BatchAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public BatchUserVO getItem(int position) {
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

                view.setTag(holder);
            }else{
                view = convertView;
                holder = (ViewHolder)convertView.getTag();
            }

            holder.checkBox.setOnClickListener(checkBoxClick);

            bindData(position, holder, view);

            return view;
        }

        private void bindData(int position, ViewHolder holder, View view) {
            BatchUserVO batchUserVO = getItem(position);
            if(batchUserVO == null){
                view.setVisibility(View.GONE);
            }else{
                view.setVisibility(View.VISIBLE);
            }

            //设置头像
            String head = batchUserVO.picture;
            if (! TextUtils.isEmpty(head)) {
                // 默认加载本地图片
                ContactImageLoader.loadNativePhoto(String.valueOf(batchUserVO.userId),
                        head, holder.head, queue);
                sp.loadStringSharedPreference(batchUserVO.userId
                        + "realname", batchUserVO.name);
            } else {
                holder.head.setImageResource(R.drawable.default_avatar);
            }

            holder.name.setText(batchUserVO.name);

            holder.head.setTag(batchUserVO);
            holder.checkBox.setTag(position);
            holder.checkBox.setVisibility(View.VISIBLE);

            //设置诚意金和认证
            StringBuilder moneyAndCertification = new StringBuilder();
            int moneyStatus = batchUserVO.earnestMoney;
            int accountStatus = batchUserVO.certification;
            if(moneyStatus == 0) {
                moneyAndCertification.append(getString(R.string.no_money));
            }else {
                moneyAndCertification.append(getString(R.string.had_money));
            }
            moneyAndCertification.append("/");

            if(accountStatus == 0){
                moneyAndCertification.append(getString(R.string.no_certification));
            }else if(accountStatus == 1){
                moneyAndCertification.append(getString(R.string.submit_certification));
            }else if(accountStatus == 2){
                moneyAndCertification.append(getString(R.string.had_certification));
            }else if(accountStatus == 3){
                moneyAndCertification.append(getString(R.string.reject_certification));
            }
            //这里与管理界面不一样需要注意，设置诚意金认证
            holder.resumeStatus.setText(moneyAndCertification.toString());

            //设置信誉
            String creditworthiness = batchUserVO.creditworthiness;
            addStars(creditworthiness, holder.reputationValueStar);

            if(checkedMap.get(position) != null){
                holder.checkBox.setChecked(true);
            }else{
                holder.checkBox.setChecked(false);
            }

            holder.head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BatchUserVO batchUserVO = (BatchUserVO)v.getTag();
                    Intent intent = new Intent(ResumeBatchManagementActivity.this, UserDetailActivity.class);
                    intent.putExtra(ActivityExtraAndKeys.GroupSetting.GROUPID , groupId);
                    if(batchUserVO != null) {
                        intent.putExtra(ActivityExtraAndKeys.USER_ID, String.valueOf(batchUserVO.userId));
                    }
                    EMGroup group = EMGroupManager.getInstance().getGroup(groupId);
                    if(EMChatManager.getInstance().getCurrentUser()
                            .equals(group.getOwner())){
                        intent.putExtra(ActivityExtraAndKeys.GroupSetting.GROUPOWNER, true);
                    }

                    startActivity(intent);
                }
            });
        }
    }

    private View.OnClickListener checkBoxClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int)v.getTag();
            BatchUserVO batchUserVO = adapter.getItem(position);
            if(v instanceof  CheckBox){
                CheckBox cb = (CheckBox)v;
                //点击checkBox时，checkBox点击之后的状态
                if(! cb.isChecked()){ //
                    checkedMap.remove(position);
                    batchUserVO.checked = false;
                }else{
                    checkedMap.put(position, batchUserVO);
                    batchUserVO.checked = true;
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


    private class BatchUserVO extends GroupSettingRequest.UserVO {
        boolean checked;

        public BatchUserVO(GroupSettingRequest.UserVO userVO) {

            BatchUserVO batchUserVO = this;
            batchUserVO.userId = userVO.userId;
            batchUserVO.apply = userVO.apply;
            batchUserVO.creditworthiness = userVO.creditworthiness;
            batchUserVO.picture = userVO.picture;
            batchUserVO.name = userVO.name;
            batchUserVO.sex = userVO.sex;
            batchUserVO.age = userVO.age;
            batchUserVO.telephone = userVO.telephone;
            batchUserVO.ableComment = userVO.ableComment;
            batchUserVO.isCommented = userVO.isCommented;
            batchUserVO.earnestMoney = userVO.earnestMoney;
            batchUserVO.certification = userVO.certification;
        }
    }

    public void addStars(String creditworthiness,LinearLayout container){
        int cre = Integer.valueOf(creditworthiness);
        container.removeAllViews();
        int num = cre / 10 ;
        for(int i = 0 ; i < num; i ++){
            container.addView(newStar());
        }

    }

    private ImageView newStar(){
        ImageView star = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        star.setLayoutParams(params);
        star.setImageResource(R.drawable.ee_27);
        return star;
    }

}
