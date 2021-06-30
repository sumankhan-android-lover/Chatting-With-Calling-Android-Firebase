package com.uaguria.chatingwithcalling.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.uaguria.chatingwithcalling.R;
import com.uaguria.chatingwithcalling.interfaces.InterfaceUserOnClick;
import com.uaguria.chatingwithcalling.model.UserModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = UserAdapter.class.getSimpleName();
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<UserModel> userList;
    private InterfaceUserOnClick userOnClick;

    public UserAdapter(ArrayList<UserModel> userList, Context context) {
        this.userList = userList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        userOnClick =(InterfaceUserOnClick)context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserAdapter.UserViewHolder(inflater.inflate(R.layout.item_home_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserModel model = userList.get(position);
        setupUser(model, (UserAdapter.UserViewHolder) holder);
    }

    private void setupUser(UserModel model, UserViewHolder holder) {
        holder.userName.setText(model.getName());
        Glide.with(context)
                .load(model.getPhoto())
                .thumbnail(0.1f)
                .fitCenter()
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .placeholder(R.drawable.default_photo)
                .error(R.drawable.default_photo)
                .into(holder.userPhoto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userOnClick.userClick(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.userPhoto)
        CircleImageView userPhoto;
        @BindView(R.id.userName)
        TextView userName;

        public UserViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
