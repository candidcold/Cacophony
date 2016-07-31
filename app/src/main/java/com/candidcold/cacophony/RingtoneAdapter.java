package com.candidcold.cacophony;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.candidcold.cacophony.data.PhoneTone;

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
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.ringtone_textview, parent, false);
            holder = new ViewHolder();
            holder.ringtoneName = (TextView) convertView.findViewById(R.id.ringtone_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final PhoneTone tone = getItem(position);
        holder.ringtoneName.setText(tone.getToneName());

        return convertView;
    }

    private static class ViewHolder {
        TextView ringtoneName;
    }

}
