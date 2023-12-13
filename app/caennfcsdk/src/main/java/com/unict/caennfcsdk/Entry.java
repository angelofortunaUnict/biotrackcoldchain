package com.unict.caennfcsdk;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;

public class Entry extends BaseEntry implements Parcelable {
    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        public Entry createFromParcel(Parcel parcel) {
            return new Entry(parcel);
        }

        public Entry[] newArray(int i) {
            return new Entry[i];
        }
    };

    /* renamed from: x */
    private float f71x = 0.0f;

    public int describeContents() {
        return 0;
    }

    public Entry() {
    }

    public Entry(float f, float f2) {
        super(f2);
        this.f71x = f;
    }

    public Entry(float f, float f2, Object obj) {
        super(f2, obj);
        this.f71x = f;
    }

    public Entry(float f, float f2, Drawable drawable) {
        super(f2, drawable);
        this.f71x = f;
    }

    public Entry(float f, float f2, Drawable drawable, Object obj) {
        super(f2, drawable, obj);
        this.f71x = f;
    }

    public float getX() {
        return this.f71x;
    }

    public void setX(float f) {
        this.f71x = f;
    }

    public Entry copy() {
        return new Entry(this.f71x, getY(), getData());
    }

    public boolean equalTo(Entry entry) {
        if (entry != null && entry.getData() == getData() && Math.abs(entry.f71x - this.f71x) <= Float.intBitsToFloat(1) && Math.abs(entry.getY() - getY()) <= Float.intBitsToFloat(1)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "Entry, x: " + this.f71x + " y: " + getY();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(this.f71x);
        parcel.writeFloat(getY());
        if (getData() == null) {
            parcel.writeInt(0);
        } else if (getData() instanceof Parcelable) {
            parcel.writeInt(1);
            parcel.writeParcelable((Parcelable) getData(), i);
        } else {
            throw new ParcelFormatException("Cannot parcel an Entry with non-parcelable data");
        }
    }

    protected Entry(Parcel parcel) {
        this.f71x = parcel.readFloat();
        setY(parcel.readFloat());
        if (parcel.readInt() == 1) {
            setData(parcel.readParcelable(Object.class.getClassLoader()));
        }
    }
}
