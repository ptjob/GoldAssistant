package com.parttime.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
import java.util.Map;

/**
 * SharePreferenceUtil工具类
 */
public class SharePreferenceUtil {

    public static final String DEFAULT_FILE = "jrdr.setting";
    public static final int MODE = Context.MODE_PRIVATE;
    private static SharePreferenceUtil mInstance;
    private final SharedPreferences mSharedpreferences;

    private SharePreferenceUtil(Context context) {
        mSharedpreferences = context.getSharedPreferences(DEFAULT_FILE, MODE);
    }

    private SharePreferenceUtil(Context context, String fileName) {
        mSharedpreferences = context.getSharedPreferences(fileName, MODE);
    }

    public static SharePreferenceUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SharePreferenceUtil.class) {
                if (mInstance == null) {
                    mInstance = new SharePreferenceUtil(context);
                }
            }
        }

        return mInstance;
    }

    public static SharePreferenceUtil getInstance(Context context, String fileName) {
        if (mInstance == null) {
            synchronized (SharePreferenceUtil.class) {
                if (mInstance == null) {
                    mInstance = new SharePreferenceUtil(context, fileName);
                }
            }
        }

        return mInstance;
    }

    public boolean saveSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = mSharedpreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public String loadStringSharedPreference(String key) {
        String str = null;
        str = mSharedpreferences.getString(key, null);
        return str;
    }

    public String loadStringSharedPreference(String key, String defVal) {
        String str = null;
        str = mSharedpreferences.getString(key, defVal);
        return str;
    }

    public boolean saveSharedPreferences(String key, int value) {
        SharedPreferences.Editor editor = mSharedpreferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public int loadIntSharedPreference(String key) {
        return mSharedpreferences.getInt(key, 0);
    }

    public boolean saveSharedPreferences(String key, float value) {
        SharedPreferences.Editor editor = mSharedpreferences.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public float loadFloatSharedPreference(String key) {
        return mSharedpreferences.getFloat(key, 0f);
    }

    public boolean saveSharedPreferences(String key, long value) {
        SharedPreferences.Editor editor = mSharedpreferences.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public long loadLongSharedPreference(String key) {
        return mSharedpreferences.getLong(key, 0l);
    }

    public int loadIntSharedPreference(String key, int defaultValue) {
        return mSharedpreferences.getInt(key, defaultValue);
    }

    public boolean saveSharedPreferences(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedpreferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public boolean loadBooleanSharedPreference(String key) {
        return mSharedpreferences.getBoolean(key, false);
    }

    public boolean loadBooleanSharedPreference(String key, boolean b) {
        return mSharedpreferences.getBoolean(key, b);
    }

    public boolean saveAllSharePreference(String keyName, List<?> list) {
        int size = list.size();
        if (size < 1) {
            return false;
        }
        SharedPreferences.Editor editor = mSharedpreferences.edit();
        if (list.get(0) instanceof String) {
            for (int i = 0; i < size; i++) {
                editor.putString(keyName + i, (String) list.get(i));
            }
        } else if (list.get(0) instanceof Long) {
            for (int i = 0; i < size; i++) {
                editor.putLong(keyName + i, (Long) list.get(i));
            }
        } else if (list.get(0) instanceof Float) {
            for (int i = 0; i < size; i++) {
                editor.putFloat(keyName + i, (Float) list.get(i));
            }
        } else if (list.get(0) instanceof Integer) {
            for (int i = 0; i < size; i++) {
                editor.putLong(keyName + i, (Integer) list.get(i));
            }
        } else if (list.get(0) instanceof Boolean) {
            for (int i = 0; i < size; i++) {
                editor.putBoolean(keyName + i, (Boolean) list.get(i));
            }
        }
        return editor.commit();
    }

    public Map<String, ?> loadAllSharePreference(String key) {
        return mSharedpreferences.getAll();
    }

    public boolean removeKey(String key) {
        SharedPreferences.Editor editor = mSharedpreferences.edit();
        editor.remove(key);
        return editor.commit();
    }

    public boolean removeAllKey() {
        SharedPreferences.Editor editor = mSharedpreferences.edit();
        editor.clear();
        return editor.commit();
    }

}