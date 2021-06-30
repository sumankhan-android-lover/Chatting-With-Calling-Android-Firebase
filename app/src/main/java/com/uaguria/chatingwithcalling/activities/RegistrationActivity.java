package com.uaguria.chatingwithcalling.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uaguria.chatingwithcalling.MyApplication;
import com.uaguria.chatingwithcalling.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = RegistrationActivity.class.getSimpleName();
    private SharedPreferences appPrefs;
    private FirebaseAuth mAuth;
    private RegistrationActivity activity;
    private DatabaseReference referenceUser;
    private String firebaseUserId;
    private Dialog dialog;

    @BindView(R.id.name)
    TextInputLayout name;
    @BindView(R.id.email)
    TextInputLayout email;
    @BindView(R.id.phone)
    TextInputLayout phone;
    @BindView(R.id.password)
    TextInputLayout password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
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

    @OnClick(R.id.registration)
    void registration(){
        dialog.show();
        FirebaseUser user = mAuth.getCurrentUser();
            if (validateRegistration()) {
                onRegistration();
            }
    }

    private boolean validateRegistration() {
        if (TextUtils.isEmpty(name.getEditText().getText().toString())) {
            dialog.dismiss();
            //email.setError("Please enter your validate email");
            Toast.makeText(activity, "Please enter name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(email.getEditText().getText().toString())) {
            dialog.dismiss();
            //password.setError("Please enter your validate password");
            Toast.makeText(activity, "Please enter email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(phone.getEditText().getText().toString())) {
            dialog.dismiss();
            //password.setError("Please enter your validate password");
            Toast.makeText(activity, "Please enter phone", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(password.getEditText().getText().toString())) {
            dialog.dismiss();
            //password.setError("Please enter your validate password");
            Toast.makeText(activity, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void onRegistration() {
        mAuth.createUserWithEmailAndPassword(email.getEditText().getText().toString(),password.getEditText().getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        firebaseUserId = mAuth.getCurrentUser().getUid();

                        referenceUser = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUserId);

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("uid", firebaseUserId);
                        map.put("name", name.getEditText().getText().toString());
                        map.put("email", mAuth.getCurrentUser().getEmail());
                        map.put("phone", phone.getEditText().getText().toString());
                        map.put("photo", "null");
                        map.put("online", true);
                        map.put("password", password.getEditText().getText().toString());

                        referenceUser.updateChildren(map).addOnCompleteListener(refer -> {
                            if (refer.isSuccessful()) {
                                appPrefs.edit()
                                        .putString("user_id", firebaseUserId)
                                        .putString("email", email.getEditText().getText().toString())
                                        .putString("password", password.getEditText().getText().toString())
                                        .putBoolean("login", true)
                                        .apply();

                                dialog.dismiss();
                                Intent intent = new Intent(activity, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                dialog.dismiss();
                                Log.e(TAG, "refer error: " + refer.getException().getMessage());
                                Toast.makeText(activity, "Error message : " + refer.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        dialog.dismiss();
                        Log.e(TAG, "onRegistration: task exception => "+ task.getException().getMessage());
                        Toast.makeText(activity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}