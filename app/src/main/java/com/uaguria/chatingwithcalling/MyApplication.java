package com.uaguria.chatingwithcalling;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;

import com.androidnetworking.AndroidNetworking;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
    private SharedPreferences appPrefs;

    @Override
    public void onCreate() {
        super.onCreate();
        appPrefs = getSharedPreferences("AppPrefs",MODE_PRIVATE);
        AndroidNetworking.initialize(getApplicationContext());
        FirebaseApp.initializeApp(getApplicationContext());
    }

    public SharedPreferences getAppPrefs() {
        return appPrefs;
    }

    public void setAppPrefs(SharedPreferences appPrefs) {
        this.appPrefs = appPrefs;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

}
