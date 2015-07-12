package com.droid.carson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droid.carson.Activity02.LocateIn;
import com.droid.carson.MyLetterListView.OnTouchingLetterChangedListener;
import com.qingmu.jianzhidaren.R;

public class Activity01 extends Activity {
    private BaseAdapter adapter;
    private ListView personList;
    private TextView overlay; // 对话框首字母textview
    private MyLetterListView letterListView; // A-Z listview
    private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private String[] sections;// 存放存在的汉语拼音首字母
    private Handler handler;
    private OverlayThread overlayThread; // 显示首字母对话框
    private ArrayList<City> allCity_lists; // 所有城市列表
    private ArrayList<City> city_lists;// 城市列表
    ListAdapter.TopViewHolder topViewHolder;
    private String lngCityName = "定位失败";
    private ImageButton backImb;// 返回
    private ClearEditText clearEdt;
    WindowManager windowManager;
    private RelativeLayout topLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_main);
        topLayout = (RelativeLayout) findViewById(R.id.title);
        topLayout.setBackgroundColor(getResources().getColor(
                R.color.guanli_common_color));
        lngCityName = getIntent().getExtras()
                .getString("citylist_city", "定位失败");
        backImb = (ImageButton) findViewById(R.id.backImb);
        backImb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        clearEdt = (ClearEditText) findViewById(R.id.filter_edit);
        clearEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                filterData(arg0.toString());
            }
        });
        personList = (ListView) findViewById(R.id.list_view);
        allCity_lists = new ArrayList<City>();
        letterListView = (MyLetterListView) findViewById(R.id.MyLetterListView01);
        letterListView
                .setOnTouchingLetterChangedListener(new LetterListViewListener());
        // Activity02.setLocateIn(new GetCityName());
        alphaIndexer = new HashMap<String, Integer>();
        handler = new Handler();
        overlayThread = new OverlayThread();
        personList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String currentCity = allCity_lists.get(arg2).getName();
                if (!"".equals(currentCity)) {
                    // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
                    Toast.makeText(getApplication(),
                            "成功切换到城市:" + allCity_lists.get(arg2).getName(),
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("city", allCity_lists.get(arg2).getName());
                    Activity01.this.setResult(RESULT_OK, intent);
                    Activity01.this.finish();
                }

            }
        });
        personList.setAdapter(adapter);
        initOverlay();
        hotCityInit();
        setAdapter(allCity_lists);
    }

    /**
     * 热门城市
     */
    public void hotCityInit() {
        City city = new City("", "-");
        // allCity_lists.add(city);
        // city = new City("", "-");
        allCity_lists.add(city);
        city = new City("上海", "");
        allCity_lists.add(city);
        city = new City("北京", "");
        allCity_lists.add(city);
        city = new City("广州", "");
        allCity_lists.add(city);
        city = new City("深圳", "");
        allCity_lists.add(city);
        city = new City("武汉", "");
        allCity_lists.add(city);
        city = new City("天津", "");
        allCity_lists.add(city);
        city = new City("西安", "");
        allCity_lists.add(city);
        city = new City("南京", "");
        allCity_lists.add(city);
        city = new City("杭州", "");
        allCity_lists.add(city);
        city = new City("成都", "");
        allCity_lists.add(city);
        city = new City("重庆", "");
        allCity_lists.add(city);
        city_lists = getCityList();
        allCity_lists.addAll(city_lists);
    }

    private ArrayList<City> getCityList(String ccity) {
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<City> list = new ArrayList<City>();
        try {
            dbHelper.createDataBase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from city ", null);
            City city;
            while (cursor.moveToNext()) {
                city = new City(cursor.getString(1), cursor.getString(2));
                if (city.name.contains(ccity)) {
                    Log.e("city", city.name);
                    list.add(city);
                }
            }
            if (list.size() > 0) {
                list.add(new City("", "-"));
            }
            cursor.close();
            db.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(list, comparator);
        return list;
    }

    private ArrayList<City> getCityList() {
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<City> list = new ArrayList<City>();
        try {
            dbHelper.createDataBase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from city", null);
            City city;
            while (cursor.moveToNext()) {
                city = new City(cursor.getString(1), cursor.getString(2));
                list.add(city);
            }
            cursor.close();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(list, comparator);
        return list;
    }

    /**
     * a-z排序
     */
    Comparator comparator = new Comparator<City>() {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyi().substring(0, 1);
            String b = rhs.getPinyi().substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }

        }
    };

    private void setAdapter(List<City> list) {
        adapter = new ListAdapter(this, list);
        personList.setAdapter(adapter);
    }

    public class ListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<City> list;
        final int VIEW_TYPE = 2;

        public ListAdapter(Context context, List<City> list) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
            alphaIndexer = new HashMap<String, Integer>();
            sections = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                // 当前汉语拼音首字母
                String currentStr = getAlpha(list.get(i).getPinyi());
                // 上一个汉语拼音首字母，如果不存在为“ ”
                String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
                        .getPinyi()) : " ";
                if (!previewStr.equals(currentStr)) {
                    String name = getAlpha(list.get(i).getPinyi());
                    alphaIndexer.put(name, i);
                    sections[i] = name;
                }
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            // TODO Auto-generated method stub
            int type = 0;
            // if (position == 0) {
            // type = 2;
            // } else if (position == 1) {
            // type = 1;
            // }
            if (position == 0)
                type = 1;
            return type;
        }

        @Override
        public int getViewTypeCount() {// 这里需要返回需要集中布局类型，总大小为类型的种数的下标
            return VIEW_TYPE;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            int viewType = getItemViewType(position);
            if (viewType == 1) {
                if (convertView == null) {
                    topViewHolder = new TopViewHolder();
                    convertView = inflater.inflate(R.layout.frist_list_item,
                            null);
                    topViewHolder.alpha = (TextView) convertView
                            .findViewById(R.id.alpha);
                    topViewHolder.name = (TextView) convertView
                            .findViewById(R.id.lng_city);
                    convertView.setTag(topViewHolder);
                } else {
                    topViewHolder = (TopViewHolder) convertView.getTag();
                }

                topViewHolder.name.setText(lngCityName);
                topViewHolder.alpha.setVisibility(View.VISIBLE);
                topViewHolder.alpha.setText("定位城市");
                topViewHolder.name.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (!"定位失败".equals(lngCityName)) {
                            // ToastUtil.showLongToast(lngCityName + "");
                            topViewHolder.name.setClickable(false);
                            Toast.makeText(getApplication(),
                                    "成功切换到城市:" + lngCityName,
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            // intent.putExtra("province", province);
                            intent.putExtra("city", lngCityName);
                            Activity01.this.setResult(RESULT_OK, intent);
                            Activity01.this.finish();
                        }
                    }
                });
            }
            // else if (viewType == 2) {
            // final ShViewHolder shViewHolder;
            // if (convertView == null) {
            // shViewHolder = new ShViewHolder();
            // convertView = inflater.inflate(R.layout.search_item, null);
            // shViewHolder.editText = (ClearEditText) convertView
            // .findViewById(R.id.sh);
            // convertView.setTag(shViewHolder);
            // } else {
            // shViewHolder = (ShViewHolder) convertView.getTag();
            // }

            else {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.list_item, null);
                    holder = new ViewHolder();
                    holder.alpha = (TextView) convertView
                            .findViewById(R.id.alpha);
                    holder.name = (TextView) convertView
                            .findViewById(R.id.name);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (position >= 0) {
                    holder.name.setText(list.get(position).getName());
                    String currentStr = getAlpha(list.get(position).getPinyi());
                    String previewStr = (position - 1) >= 0 ? getAlpha(list
                            .get(position - 1).getPinyi()) : " ";
                    if (!previewStr.equals(currentStr)) {
                        holder.alpha.setVisibility(View.VISIBLE);
                        if (currentStr.equals("#")) {
                            currentStr = "热门城市";
                        }
                        holder.alpha.setText(currentStr);
                    } else {
                        holder.alpha.setVisibility(View.GONE);
                    }
                }
            }
            return convertView;
        }

        private class ViewHolder {
            TextView alpha; // 首字母标题
            TextView name; // 城市名字
        }

        private class TopViewHolder {
            TextView alpha; // 首字母标题
            TextView name; // 城市名字
        }

        private class ShViewHolder {
            ClearEditText editText;

        }
    }

    // 初始化汉语拼音首字母弹出提示框
    private void initOverlay() {
        LayoutInflater inflater = LayoutInflater.from(this);
        overlay = (TextView) inflater.inflate(R.layout.overlay, null);
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (overlay != null && windowManager != null) {
            windowManager.removeView(overlay);
        }
    }

    private class LetterListViewListener implements
            OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(final String s) {
            if (alphaIndexer.get(s) != null) {
                int position = alphaIndexer.get(s);
                personList.setSelection(position);
                overlay.setText(sections[position]);
                overlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                // 延迟一秒后执行，让overlay为不可见
                handler.postDelayed(overlayThread, 1500);
            }
        }

    }

    // 设置overlay不可见
    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }

    }

    // 获得汉语拼音首字母
    private String getAlpha(String str) {

        if (str.equals("-")) {
            return "&";
        }
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else {
            return "#";
        }
    }

    private class GetCityName implements LocateIn {
        @Override
        public void getCityName(String name) {
            System.out.println(name);
            if (topViewHolder.name != null) {
                lngCityName = name;
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {

        Log.e("tag", "filter data");
        if (TextUtils.isEmpty(filterStr)) {
            allCity_lists.clear();
            hotCityInit();
            setAdapter(allCity_lists);

        } else {
            allCity_lists.clear();
            city_lists = getCityList(filterStr);
            allCity_lists.addAll(city_lists);
            setAdapter(allCity_lists);
            // filterDateList.clear();
            // for (SortModel sortModel : SourceDateList) {
            // String name = sortModel.getName();
            // if (name.indexOf(filterStr.toString()) != -1
            // || characterParser.getSelling(name).startsWith(
            // filterStr.toString())) {
            // filterDateList.add(sortModel);
            // }
            // }
        }

    }

}