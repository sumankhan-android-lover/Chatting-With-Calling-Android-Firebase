package com.uaguria.chatingwithcalling.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatModel implements Parcelable {
    private String sender;
    private String receiver;
    private String message;
    private String message_id;
    private String type;
    private String date_time;
    private String formatted_date;
    private String formatted_time;
    private boolean isseen;
    private int viewType;

    public ChatModel(){
        isseen = false;
    }

    protected ChatModel(Parcel in) {
        sender = in.readString();
        receiver = in.readString();
        message = in.readString();
        message_id = in.readString();
        type = in.readString();
        date_time = in.readString();
        formatted_date = in.readString();
        formatted_time = in.readString();
        isseen = in.readByte() != 0;
        viewType = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sender);
        dest.writeString(receiver);
        dest.writeString(message);
        dest.writeString(message_id);
        dest.writeString(type);
        dest.writeString(date_time);
        dest.writeString(formatted_date);
        dest.writeString(formatted_time);
        dest.writeByte((byte) (isseen ? 1 : 0));
        dest.writeInt(viewType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChatModel> CREATOR = new Creator<ChatModel>() {
        @Override
        public ChatModel createFromParcel(Parcel in) {
            return new ChatModel(in);
        }

        @Override
        public ChatModel[] newArray(int size) {
            return new ChatModel[size];
        }
    };

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getFormatted_date() {
        return formatted_date;
    }

    public void setFormatted_date(String formatted_date) {
        this.formatted_date = formatted_date;
    }

    public String getFormatted_time() {
        return formatted_time;
    }

    public void setFormatted_time(String formatted_time) {
        this.formatted_time = formatted_time;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
