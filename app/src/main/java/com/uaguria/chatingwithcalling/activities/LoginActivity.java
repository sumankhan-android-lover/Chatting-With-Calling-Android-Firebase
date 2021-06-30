package com.uaguria.chatingwithcalling.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uaguria.chatingwithcalling.MyApplication;
import com.uaguria.chatingwithcalling.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private LoginActivity activity;
    private SharedPreferences appPrefs;
    private FirebaseAuth mAuth;
    private Dialog dialog;

    @BindView(R.id.email)
    TextInputLayout email;
    @BindView(R.id.password)
    TextInputLayout password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        activity = this;
        bindActivity();
    }

    private void bindActivity() {
        appPrefs = ((MyApplication) getApplicationContext()).getAppPrefs();
        mAuth = FirebaseAuth.getInstance();

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.item_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @OnClick(R.id.login)
    void login(){
        dialog.show();
        FirebaseUser user = mAuth.getCurrentUser();
        if (validateLogin()) {
            onLogin();
        }
    }

    private boolean validateLogin() {
        if (TextUtils.isEmpty(email.getEditText().getText().toString())) {
            dialog.dismiss();
            //email.setError("Please enter your validate email");
            Toast.makeText(activity, "Please enter valid email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(password.getEditText().getText().toString())) {
            dialog.dismiss();
            //password.setError("Please enter your validate password");
            Toast.makeText(activity, "Please enter valid password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void onLogin() {
       mAuth.signInWithEmailAndPassword(email.getEditText().getText().toString(), password.getEditText().getText().toString())
               .addOnCompleteListener(task -> {
                  if (task.isSuccessful()){
                      Log.e(TAG, "user: " + mAuth.getCurrentUser());
                      appPrefs.edit()
                              .putString("user_id", mAuth.getCurrentUser().getUid())
                              .putString("email", mAuth.getCurrentUser().getEmail())
                              .putBoolean("login", true)
                              .apply();

                      dialog.dismiss();
                      Intent intent = new Intent(activity, HomeActivity.class);
                      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                      startActivity(intent);
                      finish();
                  } else {
                      dialog.dismiss();
                      Log.e(TAG, "refer error: " + task.getException().getMessage());
                      Toast.makeText(activity, "Error message : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                  }
               });
    }
}