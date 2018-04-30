package org.kinecosystem.kinit.repository;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesStore implements DataStore {

    private static final String UTF8_CHARSET = "UTF-8";
    private final String storageSection;
    private final Context applicationContext;

    public SharedPreferencesStore(Context context, String storage) {
        applicationContext = context.getApplicationContext();
        storageSection = storage;
    }

    public boolean getBoolean(String key) {
        SharedPreferences sp = applicationContext.getSharedPreferences(storageSection, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean value) {
        SharedPreferences sp = applicationContext.getSharedPreferences(storageSection, Context.MODE_PRIVATE);
        return sp.getBoolean(key, value);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences sp = applicationContext.getSharedPreferences(storageSection, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public int getInt(String key, Integer defaultValue) {
        SharedPreferences sp = applicationContext.getSharedPreferences(storageSection, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue!=null? defaultValue.intValue(): 0);
    }

    public void putInt(String key, int value) {
        SharedPreferences sp = applicationContext.getSharedPreferences(storageSection, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public long getLong(String key, Long defaultValue) {
        SharedPreferences sp = applicationContext.getSharedPreferences(storageSection, Context.MODE_PRIVATE);
        return sp.getLong(key, defaultValue!=null? defaultValue.longValue(): 0);
    }

    public void putLong(String key, long value) {
        SharedPreferences sp = applicationContext.getSharedPreferences(storageSection, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        SharedPreferences sp = applicationContext.getSharedPreferences(storageSection, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public List<String> getStringList(String key, List<String> defaultValue) {
        SharedPreferences sp = applicationContext.getSharedPreferences(storageSection, Context.MODE_PRIVATE);
        Set<String> set = sp.getStringSet(key, null);
        return set == null ? defaultValue : new ArrayList<>(set);
    }

    public void putString(String key, String value) {
        SharedPreferences sp = applicationContext.getSharedPreferences(storageSection, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putStringList(String key, List<String> value) {
        if (value != null) {
            SharedPreferences sp = applicationContext.getSharedPreferences(storageSection, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            Set<String> temp = new HashSet<>(value);
            editor.putStringSet(key, temp);
            editor.apply();
        }
    }

    public Map<String, String> getAll() {
        SharedPreferences sp = applicationContext.getSharedPreferences(storageSection, Context.MODE_PRIVATE);
        return (Map<String, String>)sp.getAll();
    }

    public void clear(String key) {
        SharedPreferences sp = applicationContext.getSharedPreferences(storageSection, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public void clearAll() {
        SharedPreferences sp = applicationContext.getSharedPreferences(storageSection, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    private <T> T loadFromResources(int id, Class<T> classOfT) {
        try {
            InputStream inputStream = applicationContext.getResources().openRawResource(id);
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, UTF8_CHARSET));
            Gson gson = new Gson();
            return gson.fromJson(reader, classOfT);
        } catch (UnsupportedEncodingException e) {
            // unsupported utf-8 - that shouldn't happen...
            e.printStackTrace();
        }
        return null;
    }
}
