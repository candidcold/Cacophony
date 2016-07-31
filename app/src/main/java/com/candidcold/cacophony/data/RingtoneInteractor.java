package com.candidcold.cacophony.data;

import android.content.Context;

import java.util.ArrayList;

public class RingtoneInteractor implements RingtoneTransaction {
    private RingtoneDatabaseHelper helper;

    public RingtoneInteractor(Context context) {
        helper = new RingtoneDatabaseHelper(context);
    }

    @Override
    public void update(PhoneTone[] after) {
        ArrayList<PhoneTone> tonesInDb = getSelectedTones();

        observeAdditions(tonesInDb, after);
        observeSubstractions(tonesInDb, after);

    }

    @Override
    public ArrayList<PhoneTone> getSelectedTones() {
        return helper.getTones();
    }

    private void observeAdditions(ArrayList<PhoneTone> inDb, PhoneTone[] checked) {
        for (PhoneTone checking : checked) {
            boolean existing = false;

            for (PhoneTone toMatch : inDb) {
                if (checking.equalTo(toMatch)) {
                    existing = true;
                    break;
                }
            }

            if (!existing) {
                addTone(checking);
            }
        }

    }

    private void observeSubstractions(ArrayList<PhoneTone> inDb, PhoneTone[] checked) {
        for (PhoneTone checking : inDb) {
            boolean existing = false;

            for (PhoneTone toMatch : checked) {
                if (checking.equalTo(toMatch)) {
                    existing = true;
                    break;
                }
            }

            if (!existing) {
                removeTone(checking);
            }
        }

    }

    private void addTone(PhoneTone tone) {
        helper.addTone(tone);
    }

    private void removeTone(PhoneTone tone) {
        helper.removeTone(tone);
    }
}
