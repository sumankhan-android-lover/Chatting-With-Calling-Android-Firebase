package com.uaguria.chatingwithcalling.helpers;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserStatus {
    private FirebaseAuth mAuth;
    private Context context;

    public UserStatus(FirebaseAuth mAuth, Context context) {
        this.mAuth = mAuth;
        this.context = context;
    }

    public void setOnLine(){
        FirebaseDatabase  database = FirebaseDatabase.getInstance();
        DatabaseReference userReference = database.getReference();
        userReference
                .child("Users")
                .child(mAuth.getCurrentUser().getUid())
                .child("online")
                .setValue(true);
    }

    public void aetOffLine(){
        FirebaseDatabase  database = FirebaseDatabase.getInstance();
        DatabaseReference userReference = database.getReference();
        userReference
                .child("Users")
                .child(mAuth.getCurrentUser().getUid())
                .child("online")
                .setValue(false);
    }

}
