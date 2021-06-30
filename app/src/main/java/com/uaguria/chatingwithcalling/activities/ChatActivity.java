package com.uaguria.chatingwithcalling.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.uaguria.chatingwithcalling.adapters.ChatAdapter;
import com.uaguria.chatingwithcalling.helpers.UserStatus;
import com.uaguria.chatingwithcalling.model.ChatModel;
import com.uaguria.chatingwithcalling.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity{
    private static final String TAG = ChatActivity.class.getSimpleName();
    public static final int TEXT_LEFT = 1;
    public static final int TEXT_RIGHT = 2;
    private ChatActivity activity;
    private SharedPreferences appPrefs;
    private Dialog dialog;
    private DatabaseReference mReferenceUser;
    private FirebaseUser mUser;
    private String receiverId;
    private FirebaseAuth mAuth;
    private UserStatus userStatus;
    private Date dateCalender;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private SimpleDateFormat sdf;

    private UserModel userModel;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private ChatAdapter adapter;
    private ArrayList<ChatModel> chatList;
    @BindView(R.id.chat_text)
    EditText chatMessage;
    @BindView(R.id.containerGroup)
    Group containerGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        activity = this;
        if (getIntent().hasExtra("user")) {
            userModel = getIntent().getParcelableExtra("user");
            bindActivity();
            getMessage();
        }else {
            activity.finish();
        }
    }

    private void bindActivity() {
        appPrefs = ((MyApplication) getApplicationContext()).getAppPrefs();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //customize loading
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.item_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dateCalender = Calendar.getInstance().getTime();
        sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault());
        dateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
        timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        //date.setText(sdf.format(dateCalender));
        Log.e(TAG, "bindActivity: date time => "+sdf.format(dateCalender));
        Log.e(TAG, "bindActivity: date=> "+dateFormat.format(dateCalender)+" time=> "+timeFormat.format(dateCalender));

        mAuth = FirebaseAuth.getInstance();     // call firebase authentication instance
        userStatus = new UserStatus(mAuth,activity);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Glide.with(activity)
                .load(userModel.getPhoto())
                .thumbnail(0.1f)
                .fitCenter()
                .error(R.drawable.default_photo)
                .placeholder(R.drawable.default_photo)
                .into(photo);
        userName.setText(userModel.getName());
        receiverId = userModel.getUid();

        chatList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(layoutManager);

    }

    private void getMessage() {
        mReferenceUser = FirebaseDatabase.getInstance().getReference("Chats");
        mReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chats");
                Log.e(TAG, "onDataChange: get data reference" + reference);
                chatList.clear();

                for (DataSnapshot shot : snapshot.getChildren()) {
                    ChatModel chat = shot.getValue(ChatModel.class);
                    if (chat != null) {
                        if (chat.getReceiver().equals(mUser.getUid()) && chat.getSender().equals(receiverId) ||
                                chat.getReceiver().equals(receiverId) && chat.getSender().equals(mUser.getUid())) {
                            Log.e(TAG, "onDataChange: receiver id " + chat.getReceiver());
                            Log.e(TAG, "onDataChange: shared preference receiver id " + appPrefs.getString("user_id", ""));
                            if (chat.getSender().equals(appPrefs.getString("user_id", ""))) {
                                chat.setViewType(TEXT_RIGHT);
                            } else {
                                chat.setViewType(TEXT_LEFT);
                            }
                            chatList.add(chat);
                        }
                    }
                }
                adapter = new ChatAdapter(chatList, activity);
                recycler.setAdapter(adapter);
                recycler.scrollToPosition(chatList.size() - 1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: "+error.getMessage() );
            }
        });
    }

    @OnClick(R.id.attachFile)
    void attachFile(View view){
        if (view.getTag().equals("1")){
            view.setTag("2");
            containerGroup.setVisibility(View.VISIBLE);
        }else {
            view.setTag("1");
            containerGroup.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.files)
    void setFiles(){

    }

    @OnClick(R.id.galleries)
    void setGalleries(){

    }

    @OnClick(R.id.photos)
    void setCamera(){

    }

    @OnClick(R.id.chat_button)
    void submitButton(){
        if (!TextUtils.isEmpty(chatMessage.getText().toString().trim())) {
            sendMessage();
        }
    }

    private void sendMessage() {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
        String messageKey = mReference.push().getKey();
        HashMap<String, Object> map = new HashMap<>();
        map.put("sender", mUser.getUid());
        map.put("receiver", receiverId);
        map.put("message", chatMessage.getText().toString());
        map.put("message_id", messageKey);
        map.put("type", "text");
        map.put("date_time", sdf.format(dateCalender));
        map.put("formatted_date", dateFormat.format(dateCalender));
        map.put("formatted_time", timeFormat.format(dateCalender));
        map.put("isseen", false);

        mReference.child("Chats").push().setValue(map);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(mUser.getUid())
                .child(receiverId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    reference.child("id").setValue(receiverId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chatMessage.getText().clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.video :
                //video calling one to one channel (please define method and continue code)
                break;
        }
        return true;
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

    @OnClick(R.id.rootLayout)
    void rootContainerClick(){
        if (containerGroup.getVisibility()==View.VISIBLE){
            containerGroup.setVisibility(View.GONE);
        }
        MyApplication.hideSoftKeyboard(activity);
    }
}