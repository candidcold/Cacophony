package com.android.candidcold.cacophony;

import java.io.Serializable;

/**
 * Created by davidmorant on 3/14/15.
 */
public class PhoneTone implements Serializable {
    String tonePath;
    String toneName;
    int checked; // Set as either 0 or 1 for ease of converting to a database

    public PhoneTone() {
        checked = 0;
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
}
