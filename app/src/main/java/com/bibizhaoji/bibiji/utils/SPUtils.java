package com.bibizhaoji.bibiji.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.speech.utils.ContextUtils;

import java.util.Map;

public class SPUtils {

    /**
     * 保存在手机里面的文件名o
     */
    private static final String FILE_NAME = "share_data";
    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */

    public static void put(String key, Object object) {
        put(ContextUtils.getApplicationContext(), key, object);
    }

    public static void put(Context context, String key, Object object) {
        put(FILE_NAME, context, key, object);
    }

    public static void put(String fileName, Context context, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.apply();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */

    public static Object get(String key, Object defaultObject) {
        return get(ContextUtils.getApplicationContext(), key, defaultObject);
    }

    public static Object get(Context context, String key, Object defaultObject) {
        return get(FILE_NAME, context, key, defaultObject);
    }

    public static Object get(String fileName, Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else if (null == defaultObject) {
            return sp.getString(key, null);
        }

        return null;
    }

    public static String getString(String fileName, Context context, String key, String defValue) {
        return (String) get(fileName, context, key, defValue);
    }

    public static int getInt(String fileName, Context context, String key, int defValue) {
        return (int) get(fileName, context, key, defValue);
    }


    public static long getLong(String fileName, Context context, String key, long defValue) {
        return (long) get(fileName, context, key, defValue);
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(String key) {
        remove(ContextUtils.getApplicationContext(), key);
    }

    public static void remove(Context context, String key) {
        remove(FILE_NAME, context, key);
    }

    public static void remove(String fileName, Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public static void clear(Context context, String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 查询某个key是否已经存在
     */

    public static boolean contains(String key) {
        return contains(ContextUtils.getApplicationContext(), key);
    }

    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }


    public static boolean contains(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAll(Context context) {
        return getAll(context, FILE_NAME);
    }

    public static Map<String, ?> getAll(Context context, String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    public static boolean clearKey(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().remove(key).apply();
        return true;
    }

}
