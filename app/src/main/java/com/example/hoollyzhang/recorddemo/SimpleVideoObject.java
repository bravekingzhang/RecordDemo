package com.example.hoollyzhang.recorddemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hoollyzhang on 16/11/22.
 */

public class SimpleVideoObject implements Parcelable {


    public SimpleVideoObject(String videoPaht) {
        this.videoPaht = videoPaht;
    }

    public String getVideoPath() {
        return videoPaht;
    }

    public void setVideoPaht(String videoPaht) {
        this.videoPaht = videoPaht;
    }

    private String videoPaht;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.videoPaht);
    }

    public SimpleVideoObject() {
    }

    protected SimpleVideoObject(Parcel in) {
        this.videoPaht = in.readString();
    }

    public static final Creator<SimpleVideoObject> CREATOR = new Creator<SimpleVideoObject>() {
        @Override
        public SimpleVideoObject createFromParcel(Parcel source) {
            return new SimpleVideoObject(source);
        }

        @Override
        public SimpleVideoObject[] newArray(int size) {
            return new SimpleVideoObject[size];
        }
    };
}
