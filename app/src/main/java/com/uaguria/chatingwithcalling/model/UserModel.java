package com.uaguria.chatingwithcalling.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {
    private String uid;
    private String name;
    private String email;
    private String phone;
    private String photo;
    private Boolean online;
    private int viewType;

    public UserModel(){}

    protected UserModel(Parcel in) {
        uid = in.readString();
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        photo = in.readString();
        byte tmpOnline = in.readByte();
        online = tmpOnline == 0 ? null : tmpOnline == 1;
        viewType = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(photo);
        dest.writeByte((byte) (online == null ? 0 : online ? 1 : 2));
        dest.writeInt(viewType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
