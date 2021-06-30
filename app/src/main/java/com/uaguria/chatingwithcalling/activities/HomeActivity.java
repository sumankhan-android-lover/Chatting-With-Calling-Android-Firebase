package com.uaguria.chatingwithcalling.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uaguria.chatingwithcalling.MyApplication;
import com.uaguria.chatingwithcalling.R;
import com.uaguria.chatingwithcalling.adapters.UserAdapter;
import com.uaguria.chatingwithcalling.helpers.UserStatus;
import com.uaguria.chatingwithcalling.interfaces.InterfaceUserOnClick;
import com.uaguria.chatingwithcalling.model.UserModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements InterfaceUserOnClick {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private HomeActivity activity;
    private SharedPreferences appPrefs;
    private Dialog dialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mReferenceUser;
    private UserModel userModel;

    private UserStatus userStatus;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private ArrayList<UserModel> userList;
    private UserAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        activity = this;
        bindActivity();
        setOnLine();
        getUser();
    }

    private void bindActivity() {
        appPrefs = ((MyApplication) getApplicationContext()).getAppPrefs();
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();     // call firebase authentication instance
        mUser = mAuth.getCurrentUser();

        userStatus = new UserStatus(mAuth,activity);

        mReferenceUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());
        mReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userModel = snapshot.getValue(UserModel.class);
                    if (userModel != null) {
                        if (!activity.isFinishing()) {
                            Glide.with(activity)
                                    .load(userModel.getPhoto())
                                    .thumbnail(0.1f)
                                    .fitCenter()
                                    .error(R.drawable.default_photo)
                                    .placeholder(R.drawable.default_photo)
                                    .into(photo);
                        }
                    }else {
                        Log.e(TAG, "bindActivity: user model home activity some issue");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.getDetails());
            }
        });

        //customize loading
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.item_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(activity));
        userList = new ArrayList<>();
    }

    private void setOnLine() {
        userStatus.setOnLine();
    }

    private void getUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userList.clear();
                    for (DataSnapshot shot : snapshot.getChildren()) {
                        UserModel userModel = shot.getValue(UserModel.class);
                        if (userModel != null) {
                            if (!userModel.getUid().equals(mUser.getUid())) {
                                userList.add(userModel);
                            }
                        }
                    }
                    Log.e(TAG, "onDataChange: array list size" + userList.size());
                    adapter = new UserAdapter(userList, activity);
                    recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.getDetails());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            logout();
        }
        return true;
    }


    private void logout() {
        dialog.show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuth.signOut();     // firebase auth signout
                appPrefs.edit().clear().apply();    // share preference clear all data
                dialog.dismiss();

                Intent intent = new Intent(activity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }, 1500);

    }

    @Override
    public void userClick(UserModel model) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("user",model);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        userStatus.aetOffLine();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userStatus.setOnLine();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!this.isDestroyed()) {
            Glide.with(activity).pauseRequests();
        }
    }
}