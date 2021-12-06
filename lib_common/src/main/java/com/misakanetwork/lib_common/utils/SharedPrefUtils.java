package com.misakanetwork.lib_common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By：Misaka10085
 * on：2021/4/14
 * package：com.misakanetwork.lib_common.utils
 * class name：SharedPrefUtils
 * desc：SharedPreferences
 */
public class SharedPrefUtils {
    public static final String NAME = "MVPProjectStructure";
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private final SharedPreferences preferences;

    private SharedPrefUtils(String preferenceName) {
        preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        SharedPrefUtils.context = context.getApplicationContext();
    }

    public static SharedPrefUtils open(String name) {
        synchronized (SharedPrefUtils.class) {
            if (SharedPrefUtils.context == null) {
                throw new RuntimeException("Must call init(Context context) at first");
            }
            return new SharedPrefUtils(name);
        }
    }

    /**
     * 存放String
     */
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 获取String
     * 默认返回null
     */
    public String getString(String key) {
        return this.preferences.getString(key, null);
    }

    /**
     * 存放Int
     */
    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 获取Int
     * 默认返回0
     */
    public int getInt(String key) {
        return this.preferences.getInt(key, 0);
    }

    /**
     * 获取Int
     * 默认返回0
     */
    public int getInt(String key, int defaultValue) {
        return this.preferences.getInt(key, defaultValue);
    }

    /**
     * 存放布尔值
     */
    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 获取布尔值
     * 默认返回false
     */
    public boolean getBoolean(String key) {
        return this.preferences.getBoolean(key, false);
    }

    /**
     * 存放对象
     */
    public void putEntity(String key, Object value) {
        Gson gson = new Gson();
        String strJson = gson.toJson(value);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, strJson);
        editor.apply();
    }

    public void putSerializableEntity(String key, Object value) {
        if (value instanceof Serializable) {
            try {
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bs);
                oos.writeObject(value);
                oos.flush();
                oos.close();
                byte[] data = bs.toByteArray();
                String base64Str = Base64.encodeToString(data, 2);
                this.putString(key, base64Str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取对象
     * 默认返回null
     */
    public <T> Object getEntity(String key, Class<T> classBean) {
        String result = this.preferences.getString(key, null);
        if (result == null) {
            return null;
        }
        try {
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            return gson.fromJson(jsonObject, classBean);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getSerializableEntity(String key) {
        try {
            String s = this.getString(key);
            if (TextUtils.isEmpty(s)) {
                return null;
            }
            byte[] data = Base64.decode(s, 2);
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object o = ois.readObject();
            ois.close();
            return o;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 移除对象
     */
    public void remove(String key) {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 存储List
     */
    public <T> void putDataList(String key, List<T> dataList) {
        Gson gson = new Gson();
        String strJson;
        if (dataList == null) {
            strJson = null;
        } else if (dataList.size() == 0) {
            strJson = null;
        } else {
            strJson = gson.toJson(dataList);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, strJson);
        editor.apply();
    }

    /**
     * 获取存储List
     * 默认返回null
     */
    public <T> List<T> getDataList(String key, Class<T> classBean) {
        String result = this.preferences.getString(key, null);
        if (result == null) {
            return null;
        }
        try {
            Gson gson = new Gson();
            JsonArray jsonArray = new JsonParser().parse(result).getAsJsonArray();
            List<T> dataList = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                dataList.add(gson.fromJson(element, classBean));
            }
            return dataList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
