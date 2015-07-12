package com.quark.utils;

/*
 * 版       权:  Royal.k.peng@gmail.com, All rights reserved
 * 作       者:  Royal
 * 座 右  铭:  Never give up, adhere to in the end.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.qingmu.jianzhidaren.R;

/**
 * 日志管理类 主要功能， 1.直接log工具查看，不用打印详细的时间，第几行，写入到sdcard的日志才加这些东西。<br>
 * 2.写入日志文件的只有警告和error,才会写入到日志，注意异常信息的log需要写入到文件，
 *
 * @author Royal
 */
public class Logger {

    public static final String KEY_TAG = "TAG";
    public static final String KEY_ISLOG = "ISLOG";
    public static final String KEY_WRITE_SDCARD = "writesdcard";
    public static final String KEY_LOG_LEVEL = "loglevel";
    public static final String KEY_LOG_PATH = "logpath";
    public static final String KEY_LOG_FILENAME = "fileName";

    private static String TAG = "Logger";
    private static boolean ISLOG = true;
    private static boolean WRITE_SDCARD_LOG = true;
    @SuppressLint("SdCardPath")
    private static String LOG_PATH = "/data/data/com.mdj.mdjmeijia/";
    private static String LOG_FILENAME = "log.log";

    // log 等级的分配，完全和 android.util.Log类中的完全
    private static int LOG_LEVEL = Log.INFO;

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss ms");

    /**
     * 一般在程序的appliction中调用这个方法，来初始化一下，日志管理类。
     * 初始化:1.默认TAG,2.是否打印log，3.是否写日志到sdcard，4.日志等级。
     * 封装在 Bundle中传递.
     */

    private static void initLogger(Bundle bundle) {
        String tag = bundle.getString(KEY_TAG);
        String logPath = bundle.getString(KEY_LOG_PATH);
        String filename = bundle.getString(KEY_LOG_FILENAME);
        TAG = EmptyUtil.isStringEmpty(tag) ? TAG : tag;
        LOG_PATH = EmptyUtil.isStringEmpty(logPath) ? LOG_PATH : logPath;
        LOG_FILENAME = EmptyUtil.isStringEmpty(filename) ? LOG_FILENAME
                : filename;
        LOG_LEVEL = bundle.getInt(KEY_LOG_LEVEL);
        ISLOG = bundle.getBoolean(KEY_ISLOG);
        WRITE_SDCARD_LOG = bundle.getBoolean(KEY_WRITE_SDCARD);
    }

    public static void initProperties(Context context) {
        Properties properties = new Properties();
        InputStream in = null;
        try {

            in = context.getResources().openRawResource(R.raw.setting);
            properties.load(in);
            Bundle bundle = new Bundle();
            String tag = properties.getProperty(Logger.KEY_TAG);
            bundle.putString(Logger.KEY_TAG, tag);
            boolean isLog = getPropertieBoolean(properties,Logger.KEY_ISLOG);
            bundle.putBoolean(Logger.KEY_ISLOG, isLog);
            boolean write2Sdcard = getPropertieBoolean(properties,Logger.KEY_WRITE_SDCARD);
            bundle.putBoolean(Logger.KEY_WRITE_SDCARD, write2Sdcard);
            int logLevel = getPropertieInt(properties,Logger.KEY_LOG_LEVEL);
            bundle.putInt(Logger.KEY_LOG_LEVEL, logLevel);
            String fileName = properties.getProperty(Logger.KEY_LOG_FILENAME);
            bundle.putString(Logger.KEY_LOG_FILENAME, fileName);
            String logPath = context.getFilesDir().toString();
            bundle.putString(Logger.KEY_LOG_PATH, logPath);
            Log.i(TAG, "logconfig=[tag=" + tag + ",islog=" + isLog
                    + ",writeTosdCard=" + write2Sdcard + ",logLevel="
                    + logLevel + ",fileName=" + fileName + ",logPath="
                    + logPath);
            // init the logger.
            Logger.initLogger(bundle);

        } catch (FileNotFoundException e) {
            Log.w(TAG,"can't get logger file");
        } catch (IOException e) {
            Log.w(TAG, "READ file fiald");
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean getPropertieBoolean(Properties properties ,String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    private static int getPropertieInt(Properties properties ,String key) {
        int i = Log.INFO;
        try {
            String str = properties.getProperty(key);
            i = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            Log.v(TAG, "pasre propertieInt error! messge = " + e.toString());
            return i;
        }
        return i;
    }

    // 异常日志大小限制 5M
    private static final float ERR_LOG_SIZE = 5 * 1024.0f;

    /**
     * 只有在写入文件的时候才去调用这个方法来构建消息
     */
    private static String buildMsg(String msg) {
        StringBuilder buffer = new StringBuilder();
        Date date = new Date();
        String time = sdf.format(date);

        buffer.append(time);
        final StackTraceElement stackTraceElement = Thread.currentThread()
                .getStackTrace()[4];
        buffer.append(" [");
        buffer.append(Thread.currentThread().getName());
        buffer.append(":");
        buffer.append(stackTraceElement.getLineNumber());
        buffer.append(":");
        buffer.append(stackTraceElement.getMethodName());
        buffer.append("()] ");
        buffer.append(msg);
        buffer.append("\n");
        return buffer.toString();
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void v(String tag, String msg) {
        if (ISLOG && LOG_LEVEL >= Log.VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (ISLOG && LOG_LEVEL >= Log.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (ISLOG && LOG_LEVEL >= Log.INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void w(String tag, String msg) {
        if (ISLOG && LOG_LEVEL >= Log.WARN) {
            Log.w(tag, msg);
            if (WRITE_SDCARD_LOG) {
                String info = buildMsg(msg);
                writeFileToSD(info);
            }
        }
    }

    public static void w(String msg, Exception e) {
        w(TAG, msg, e);
    }

    public static void w(String tag, String msg, Exception e) {
        if (ISLOG && LOG_LEVEL >= Log.WARN) {
            Log.w(TAG, msg, e);
            if (WRITE_SDCARD_LOG) {
                String info = buildMsg(msg);
                writeFileToSD(info + getStackTrace(e));
            }
        }
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (ISLOG && LOG_LEVEL >= Log.ERROR) {
            Log.e(tag, msg);
            if (WRITE_SDCARD_LOG) {
                String info = buildMsg(msg);
                writeFileToSD(info);
            }
        }
    }

    public static void e(String msg, Exception e) {
        e(TAG, msg, e);
    }

    public static void e(String tag, String msg, Exception e) {
        if (ISLOG && LOG_LEVEL >= Log.ERROR) {
            Log.e(tag, msg, e);
            if (WRITE_SDCARD_LOG) {
                String info = buildMsg(msg);
                writeFileToSD(info + getStackTrace(e));
            }
        }
    }

    @SuppressLint("SdCardPath")
    private static void writeFileToSD(String context) {
        RandomAccessFile raf = null;
        if (LOG_PATH.startsWith("/sdcard") || LOG_PATH.startsWith("/mnt")) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                Log.d(TAG, "SD card is not avaiable right now.");
                return;
            }
        }
        try {
            String pathName = LOG_PATH;
            String fileName = LOG_FILENAME;
            File path = new File(pathName);
            File file = new File(pathName + fileName);
            if (!path.exists()) {
                Log.d(TAG, "Create the path:" + pathName);
                path.mkdir();
            }
            if (!file.exists()) {
                Log.d(TAG, "Create the file:" + fileName);
                file.createNewFile();
            }

            raf = new RandomAccessFile(file, "rw");
            if (file.length() > ERR_LOG_SIZE) {
                raf.seek(0);
            } else {
                raf.seek(file.length());
            }

            raf.write(context.getBytes());
        } catch (Exception e) {
            Log.e(TAG, "Error to write SD card.");
        } finally {
            if (null != raf) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取堆栈信息
     *
     * @param t
     * @return
     */
    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

}
