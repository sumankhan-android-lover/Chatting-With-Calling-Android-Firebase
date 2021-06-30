package com.uaguria.chatingwithcalling.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.uaguria.chatingwithcalling.MyApplication;
import com.uaguria.chatingwithcalling.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MainActivity activity;
    private SharedPreferences appPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        activity = this;
        bindActivity();
    }

    private void bindActivity() {
        appPrefs = ((MyApplication) getApplicationContext()).getAppPrefs();

        Log.e(TAG, "bindActivity: login type"+appPrefs.getBoolean("login", false) );
        if (appPrefs.getBoolean("login", false)){
            Intent intent = new Intent(activity, HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }


    @OnClick(R.id.login)
    void login(){
        startActivity(new Intent(activity,LoginActivity.class));
    }

    @OnClick(R.id.registration)
    void registration(){
        startActivity(new Intent(activity,RegistrationActivity.class));
    }
}