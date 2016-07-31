package com.candidcold.cacophony.data;

import android.content.Context;

import java.util.List;

public class RingtoneInteractor implements RingtoneTransaction {
    private RingtoneDatabaseHelper helper;

    public RingtoneInteractor(Context context) {
        helper = new RingtoneDatabaseHelper(context);
    }

    @Override
    public void update(PhoneTone[] after) {

        // TODO: Check for what has changed, and call the method needed

        // TODO: Or I could get what's in the DB already and then just add what's not there etc.
    }

    @Override
    public List<PhoneTone> getSelectedTones() {
        return helper.getTones();
    }

    private void addTone(PhoneTone tone) {

    }

    private void removeTone(PhoneTone tone) {

    }
}
