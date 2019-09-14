package com.example.personator.shared.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.personator.shared.config.Common;

public class SharedPreferencesHelper {

    private SharedPreferences mShared;
    private SharedPreferences.Editor mEditor;

    @SuppressLint("CommitPrefEdits")
    public SharedPreferencesHelper(Context context) {
        mShared = context.getSharedPreferences("aboutNews", Context.MODE_PRIVATE);
        mEditor = mShared.edit();
    }

    public void addIntSharedPreferences(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public void addStringSharedPreferences(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void addFloatSharedPreferences(String key, float value) {
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    public void addBooleanSharedPreferences(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public int getIntSharedPreferences(String key) {
        return mShared.getInt(key, 0);
    }

    public String getStringSharedPreferences(String key) {
        return mShared.getString(key, null);
    }

    public float getFloatSharedPreferences(String key) {
        return mShared.getFloat(key, 0);
    }

    public boolean getBooleanSharedPreferences(String key) {
        return mShared.getBoolean(key, false);
    }

    public void clearSharedPreferences(){
        mEditor.clear();
        mEditor.commit();
    }
}
