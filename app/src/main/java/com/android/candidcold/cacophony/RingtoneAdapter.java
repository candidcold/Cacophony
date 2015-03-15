package com.android.candidcold.cacophony;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by davidmorant on 3/14/15.
 */
public class RingtoneAdapter extends ArrayAdapter<PhoneTone> {

    public RingtoneAdapter(Context context, ArrayList<PhoneTone> tones) {
        super(context, 0, tones);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        PhoneTone tone = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.ringtone_textview, null);

        TextView ringtoneName = (TextView) convertView.findViewById(R.id.ringtone_textview);
        ringtoneName.setText(position + getItem(position).getToneName());
//        Log.e(RingtoneFragment.TAG, "David  " + ringtoneName.getText());

        return super.getView(position, convertView, parent);
    }
}
