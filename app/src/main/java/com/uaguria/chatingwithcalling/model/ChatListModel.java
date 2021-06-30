package com.uaguria.chatingwithcalling.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatListModel implements Parcelable {
    private int id;

    public ChatListModel(){}

    protected ChatListModel(Parcel in) {
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChatListModel> CREATOR = new Creator<ChatListModel>() {
        @Override
        public ChatListModel createFromParcel(Parcel in) {
            return new ChatListModel(in);
        }

        @Override
        public ChatListModel[] newArray(int size) {
            return new ChatListModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
