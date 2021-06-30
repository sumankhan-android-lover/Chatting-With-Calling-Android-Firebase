package com.uaguria.chatingwithcalling.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uaguria.chatingwithcalling.R;
import com.uaguria.chatingwithcalling.model.ChatModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = ChatAdapter.class.getSimpleName();
    public static final int TEXT_LEFT = 1;
    public static final int TEXT_RIGHT = 2;
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<ChatModel> chatList;

    public ChatAdapter(ArrayList<ChatModel> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return chatList.get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TEXT_LEFT:
                return new TextViewHolder(inflater.inflate(R.layout.item_left, parent, false));
            case TEXT_RIGHT:
                return new TextViewHolder(inflater.inflate(R.layout.item_right, parent, false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatModel model = chatList.get(position);
        switch (model.getViewType()) {
            case TEXT_RIGHT:
            case TEXT_LEFT:
                setupTextLeft((TextViewHolder) holder, model, position);
                break;
        }
    }

    private void setupTextLeft(TextViewHolder holder, ChatModel model, int position) {
        int previousPos = position-1;
        if (position>0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (chatList.get(previousPos).getSender().equals(model.getSender())) {
                params.setMargins(0, 4, 0, 0);
                holder.bubble.setVisibility(View.INVISIBLE);
            } else {
                params.setMargins(0, 24, 0, 0);
                holder.bubble.setVisibility(View.VISIBLE);
            }
            holder.chatArea.setLayoutParams(params);
        }
        holder.textView.setText(model.getMessage());
        holder.dateTime.setText(model.getFormatted_time());


    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text)
        TextView textView;
        @BindView(R.id.dateTime)
        TextView dateTime;
        @BindView(R.id.bubble)
        ImageView bubble;
        @BindView(R.id.chat_area)
        LinearLayout chatArea;

        public TextViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
