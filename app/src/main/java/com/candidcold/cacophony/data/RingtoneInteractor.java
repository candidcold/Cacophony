package com.candidcold.cacophony.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class RingtoneInteractor implements RingtoneTransaction {
    private RingtoneDatabaseHelper helper;

    public RingtoneInteractor(Context context) {
        helper = new RingtoneDatabaseHelper(context);
    }

    @Override
    public void update(PhoneTone[] after) {

        // TODO: Check for what has changed, and call the method needed
        List<PhoneTone> tonesInDb = getSelectedTones();



    }

    @Override
    public ArrayList<PhoneTone> getSelectedTones() {
        return helper.getTones();
    }

    private void addTone(PhoneTone tone) {
        helper.addTone(tone);
    }

    private void removeTone(PhoneTone tone) {
        helper.removeTone(tone);
    }
}
