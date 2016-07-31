package com.candidcold.cacophony;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.candidcold.cacophony.data.PhoneTone;

import java.util.List;

public class SelectedTonesAdapter extends RecyclerView.Adapter<SelectedTonesAdapter.ToneHolder> {

    private List<PhoneTone> selectedTones;

    public SelectedTonesAdapter(List<PhoneTone> selectedTones) {
        this.selectedTones = selectedTones;
    }

    @Override
    public SelectedTonesAdapter.ToneHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ringtone_textview, parent, false);
        return new ToneHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SelectedTonesAdapter.ToneHolder holder, int position) {
        PhoneTone singleTone = selectedTones.get(position);

        holder.setRingtone(singleTone.getToneName());
        // TODO: Create a callback to play the selected tone

    }

    @Override
    public int getItemCount() {
        return selectedTones.size();
    }

    // TODO: Make the current ringtone the one that has a Card instead
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ToneHolder extends RecyclerView.ViewHolder {
        TextView ringtone;

        public ToneHolder(View itemView) {
            super(itemView);
            ringtone = (TextView) itemView.findViewById(R.id.ringtone_textview);
        }

        void setRingtone(String name) {
            if (ringtone != null) {
                ringtone.setText(name);
            }
        }
    }
}
