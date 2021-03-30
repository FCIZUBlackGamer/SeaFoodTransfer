package com.ccis.seafoodtrans.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Container implements Parcelable {
    String SSCC;
    int Amount;

    public Container(String s, int amount){
        this.SSCC = s;
        this.Amount = amount;
    }

    protected Container(Parcel in) {
        SSCC = in.readString();
        Amount = in.readInt();
    }

    public static final Creator<Container> CREATOR = new Creator<Container>() {
        @Override
        public Container createFromParcel(Parcel in) {
            return new Container(in);
        }

        @Override
        public Container[] newArray(int size) {
            return new Container[size];
        }
    };

    public String getSSCC() {
        return SSCC;
    }

    public void setSSCC(String SSCC) {
        this.SSCC = SSCC;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SSCC);
        dest.writeInt(Amount);
    }
}
