package com.candidcold.cacophony.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by davidmorant on 3/14/15.
 */
public class PhoneTone implements Parcelable, Serializable {
    String tonePath;
    String toneName;
    int checked; // Set as either 0 or 1 for ease of converting to a database

    public PhoneTone() {
        checked = 0;
    }

    public boolean equals(PhoneTone second) {
        return second.getToneName().equals(toneName) && second.getTonePath().equals(tonePath);
    }

    @Override
    public String toString() {
        return toneName;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public String getTonePath() {
        return tonePath;
    }

    public void setTonePath(String tonePath) {
        this.tonePath = tonePath;
    }

    public String getToneName() {
        return toneName;
    }

    public void setToneName(String toneName) {
        this.toneName = toneName;
    }

    private PhoneTone(Parcel in) {
        String [] nameAndPath = new String[2];
        in.readStringArray(nameAndPath);

        toneName = nameAndPath[0];
        tonePath = nameAndPath[1];
        checked = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(checked);
        String[] nameAndPath = {toneName, tonePath};
        dest.writeStringArray(nameAndPath);
    }

    public static final Parcelable.Creator<PhoneTone> CREATOR
            = new Parcelable.Creator<PhoneTone>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public PhoneTone createFromParcel(Parcel in) {
            return new PhoneTone(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public PhoneTone[] newArray(int size) {
            return new PhoneTone[size];
        }
    };
}
