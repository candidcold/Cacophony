package com.android.candidcold.cacophony;

import android.net.Uri;

/**
 * Created by davidmorant on 3/14/15.
 */
public class PhoneTone {
    Uri tonePath;
    String toneName;

    public PhoneTone() {
    }

    @Override
    public String toString() {
        return toneName;
    }

    public Uri getTonePath() {
        return tonePath;
    }

    public void setTonePath(Uri tonePath) {
        this.tonePath = tonePath;
    }

    public String getToneName() {
        return toneName;
    }

    public void setToneName(String toneName) {
        this.toneName = toneName;
    }
}
