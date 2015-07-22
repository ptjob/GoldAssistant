package com.easemob.chatuidemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parttime.pojo.MessageSet;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * Created by luhua on 15/7/15.
 */
public class MessageSetDao {
    public static final String TABLE_NAME = "message_set";
    public static final String ID = "id";
    //群组或者联系人的name 也是唯一的
    public static final String COLUMN_NAME = "name";
    //区分群聊或者联系人
    public static final String COLUMN_TYPE = "type";
    //是否置顶 1:置顶；0: 不置顶
    public static final String COLUMN_TOP = "is_top";
    //创建时间 用来做置顶排序
    public static final String COLUMN_TIME = "create_time";

    private DbOpenHelper dbHelper;

    public MessageSetDao(Context context) {
        dbHelper = DbOpenHelper.getInstance(context);
    }

    /**
     * 保存消息列表的设置
     *
     * @param messageSet MessageSet
     */
    public long save(MessageSet messageSet) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, messageSet.name);
            values.put(COLUMN_TYPE, messageSet.type);
            values.put(COLUMN_TOP, messageSet.isTop ? 1 : 0);
            values.put(COLUMN_TIME, messageSet.createTime);

            return db.replace(TABLE_NAME, null, values);
        }
        return 0L;
    }

    /**
     * 删除一个消息置顶设置
     * @param name String
     * @param type String
     */
    public void delete(String name, String type){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            db.delete(TABLE_NAME,
                    COLUMN_NAME + " = ? and " + COLUMN_TYPE + " = ? " ,
                    new String[]{name, type});
        }
    }


    /**
     * 获取消息置顶设置list
     *
     * @return Map<String, MessageSet>
     */
    public Map<String, MessageSet> getMessageSetList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //保证存放元素的顺序
        Map<String, MessageSet> messageSetMap = new LinkedHashMap<>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " order by " + COLUMN_TIME, null);
            try {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(ID));
                    String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                    int isTop = cursor.getInt(cursor.getColumnIndex(COLUMN_TOP));
                    long createTime = cursor.getLong(cursor.getColumnIndex(COLUMN_TIME));
                    MessageSet messageSet = new MessageSet();
                    messageSet.id = id;
                    messageSet.name = name;
                    messageSet.type = type;
                    messageSet.isTop = isTop == 1;
                    messageSet.createTime = createTime;

                    messageSetMap.put(name, messageSet);
                }
            } finally {
               cursor.close();
            }
        }
        return messageSetMap;
    }


    /**
     * 获取消息置顶设置list
     *
     * @return Map<String, MessageSet>
     */
    public MessageSet getMessageSet(String name, String type) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_NAME + " = ? and " + COLUMN_TYPE + " = ? ",
                    new String[]{name, type});
            try {
                if (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(ID));
                    String n = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    String t = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                    int isTop = cursor.getInt(cursor.getColumnIndex(COLUMN_TOP));
                    long createTime = cursor.getLong(cursor.getColumnIndex(COLUMN_TIME));
                    MessageSet messageSet = new MessageSet();
                    messageSet.id = id;
                    messageSet.name = n;
                    messageSet.type = t;
                    messageSet.isTop = isTop == 1;
                    messageSet.createTime = createTime;

                    return messageSet;
                }
            } finally {
                cursor.close();
            }
        }
        return null;
    }


}
