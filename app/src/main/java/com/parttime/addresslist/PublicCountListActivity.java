package com.parttime.addresslist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chatuidemo.activity.BaseActivity;
import com.easemob.chatuidemo.domain.User;
import com.parttime.IM.ChatActivity;
import com.parttime.common.head.ActivityHead;
import com.qingmu.jianzhidaren.R;
import com.quark.http.image.CircularImage;

import java.util.ArrayList;

public class PublicCountListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ActivityHead head;
    private ListView listView;
    private PublicCountAdapter adapter;
    private ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_count_list);
        head = new ActivityHead(this);
        head.setCenterTxt1(R.string.public_count);
        adapter = new PublicCountAdapter();
        buildData();
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }

    private void buildData() {

        User kefu = new User();
        kefu.setUsername(getString(R.string.kefu));
        users.add(kefu);

        User caiwy = new User();
        caiwy.setUsername(getString(R.string.caiwu));
        users.add(caiwy);

        User dingyue = new User();
        dingyue.setUsername(getString(R.string.dingyue));
        users.add(dingyue);

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        User user = users.get(position);

        startActivity(new Intent(this,
                ChatActivity.class).putExtra("userId",
                user.getUsername()));
    }

    class PublicCountAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public User getItem(int position) {
            return users.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view;
            ViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new ViewHolder();
                view = getLayoutInflater().inflate(R.layout.row_contact,parent,false);
                viewHolder.picture = (CircularImage) view.findViewById(R.id.avatar);
                viewHolder.name = (TextView) view.findViewById(R.id.name);
                view.setTag(viewHolder);
            }else{
                view = convertView;
                viewHolder = (ViewHolder)view.getTag();
            }


            User user = getItem(position);
            if(getString(R.string.kefu).equals(user.getUsername())){
                viewHolder.name.setText(getString(R.string.kefu_value));
                Drawable draw1 = getResources().getDrawable(
                        R.drawable.custom_kefu);
                BitmapDrawable bd = (BitmapDrawable) draw1;
                Bitmap bitmap = bd.getBitmap();
                viewHolder.picture.setImageBitmap(bitmap);
            }else if(getString(R.string.caiwu).equals(user.getUsername())){
                viewHolder.name.setText(getString(R.string.caiwu_value));
                Drawable draw1 = getResources().getDrawable(
                        R.drawable.custom_caiwu);
                BitmapDrawable bd = (BitmapDrawable) draw1;
                Bitmap bitmap = bd.getBitmap();
                viewHolder.picture.setImageBitmap(bitmap);
            }else if(getString(R.string.dingyue).equals(user.getUsername())){
                viewHolder.name.setText(getString(R.string.dingyue_value));
                Drawable draw1 = getResources().getDrawable(
                        R.drawable.custom_xiaozhushou);
                BitmapDrawable bd = (BitmapDrawable) draw1;
                Bitmap bitmap = bd.getBitmap();
                viewHolder.picture.setImageBitmap(bitmap);
            }


            return view;
        }

        class ViewHolder{
            CircularImage picture;
            TextView name;
        }
    }

}
