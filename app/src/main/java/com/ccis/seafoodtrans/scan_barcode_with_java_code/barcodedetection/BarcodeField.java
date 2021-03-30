package com.ccis.seafoodtrans.scan_barcode_with_java_code.barcodedetection;


import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import kotlinx.android.parcel.Parcelize;

@Parcelize
public final class BarcodeField implements Parcelable {
    private final String label;
    private final String value;

    protected BarcodeField(Parcel in) {
        label = in.readString();
        value = in.readString();
    }

    public static final Creator<BarcodeField> CREATOR = new Creator<BarcodeField>() {
        @Override
        public BarcodeField createFromParcel(Parcel in) {
            return new BarcodeField(in);
        }

        @Override
        public BarcodeField[] newArray(int size) {
            return new BarcodeField[size];
        }
    };

    @NotNull
    public final String getLabel() {
        return this.label;
    }

    @NotNull
    public final String getValue() {
        return this.value;
    }

    public BarcodeField(@NotNull String label, @NotNull String value) {
        super();
        this.label = label;
        this.value = value;
    }


    @NotNull
    public String toString() {
        return "BarcodeField(label=" + this.label + ", value=" + this.value + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BarcodeField that = (BarcodeField) o;
        return label.equals(that.label) &&
                value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, value);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(@NotNull Parcel parcel, int flags) {
        parcel.writeString(this.label);
        parcel.writeString(this.value);
    }


}
