package com.ccis.seafoodtrans.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Quantity  implements Parcelable {
    String Name;
    int Amount;

    public Quantity(String name, int amount) {
        Name = name;
        Amount = amount;
    }

    protected Quantity(Parcel in) {
        Name = in.readString();
        Amount = in.readInt();
    }

    public static final Creator<Quantity> CREATOR = new Creator<Quantity>() {
        @Override
        public Quantity createFromParcel(Parcel in) {
            return new Quantity(in);
        }

        @Override
        public Quantity[] newArray(int size) {
            return new Quantity[size];
        }
    };

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
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
        dest.writeString(Name);
        dest.writeInt(Amount);
    }
}
