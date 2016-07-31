package com.candidcold.cacophony.data;

import java.util.List;

public interface RingtoneTransaction {
    void update(PhoneTone[] after);
    List<PhoneTone> getSelectedTones();
}
