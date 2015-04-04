package com.candidcold.cacophony;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by davidmorant on 3/14/15.
 *
 */
public class RingtoneFragment extends Fragment {
    public static final String TAG = MainActivity.class.getSimpleName();

    private TextView currentSong;
//    private ListView listView;
//    private Button startAlarm;
//    private Button cancelAlarm;
    private Cursor ringtonesCursor;
//    private RingtoneManager ringtoneManager;

    private ArrayList<PhoneTone> allRingtonesList;
    private ArrayList<PhoneTone> selectedRingtonesList;
    private RingtoneAdapter selectedRingtonesAdapter;
    private Queue<PhoneTone> tonesToUpdate;
    private AlarmManager alarmManager;
    private PendingIntent alarmPendingIntent;


    private boolean checked[];
    private String ringtoneNames[];
    private int counter = 0;

    private Ringtone playingRingtone;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setup() {
        RingtoneManager ringtoneManager = new RingtoneManager(getActivity().getApplicationContext());
        ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);
        ringtonesCursor = ringtoneManager.getCursor();
        int count = ringtonesCursor.getCount();

        checked = new boolean[count];
        ringtoneNames = new String[count];

        allRingtonesList = new ArrayList<>();
        selectedRingtonesList = new ArrayList<>();

        // Get an arraylist and an adapter for all the ringtones on the device
        while (ringtonesCursor.moveToNext()) {
            // Get the information of ringtone
            int currentPosition = ringtonesCursor.getPosition();
            Uri currentUri = ringtoneManager.getRingtoneUri(currentPosition);
            PhoneTone phoneTone = new PhoneTone();
            phoneTone.setTonePath(currentUri.toString());
            Ringtone tempRingtone = RingtoneManager.getRingtone(getActivity().getApplicationContext(), currentUri);
            tempRingtone.stop();
            phoneTone.setToneName(tempRingtone.getTitle(getActivity().getApplicationContext()));

            // For ease, get a list of the ringtone names as you're retrieving them
            ringtoneNames[counter] = phoneTone.getToneName();
            counter++;

            // Add the phoneTone to the list
            allRingtonesList.add(phoneTone);
        }
        selectedRingtonesAdapter = new RingtoneAdapter(getActivity().getApplicationContext(), selectedRingtonesList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ringtones, container, false);
        setup();
        currentSong = (TextView) rootView.findViewById(R.id.current_song);
        Button startAlarm = (Button) rootView.findViewById(R.id.alarmStarter);
        Button cancelAlarm = (Button) rootView.findViewById(R.id.alarmCanceler);
        Button refreshButton = (Button) rootView.findViewById(R.id.refreshApp);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);

        // Get the current ringtone
        playingRingtone = getCurrentDefaultRingtone();
        currentSong.setText(playingRingtone.getTitle(getActivity().getApplicationContext()));
        playingRingtone.stop();



        // Get the data to be shown in the listview and set it up
        getListOfSelected();
        listView.setAdapter(selectedRingtonesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Stop the ringtone playing before this one to prevent overlap
                playingRingtone.stop();
                playingRingtone = RingtoneManager.getRingtone(getActivity().getApplicationContext(),
                        Uri.parse(selectedRingtonesList.get(position).getTonePath()));
                playingRingtone.play();
                Log.d(TAG, playingRingtone.getTitle(getActivity().getApplicationContext()));
            }
        });

        startAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmManager = (AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getActivity().getApplicationContext(), AlarmReceiver.class);
                intent.putExtra("SelectedTones", selectedRingtonesList);
                alarmPendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime(),
                        AlarmManager.INTERVAL_DAY,
//                        1000 * 3, // For testing :]
                        alarmPendingIntent);
                Toast.makeText(getActivity().getApplicationContext(),
                        "Cycle started. Your ringtone will be set soon!",
                        Toast.LENGTH_LONG).show();
                Log.d(TAG, "Alarm has been started");
            }
        });

        // Button for canceling an alarm
        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AlarmReceiver.class);
                alarmManager = (AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                alarmPendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(alarmPendingIntent);
                Toast.makeText(getActivity().getApplicationContext(),
                        "Your cycle has been canceled",
                        Toast.LENGTH_LONG).show();
                Log.d(TAG, "Alarm has been canceled");
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshDialog().show();
            }
        });

        // Set up the fab and show the dialog
        FloatingActionButton fab;
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.attachToListView(listView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First get a list of the checked values in order to ensure the checked items
                // are up to date.
                createDialog();
            }
        });

        return rootView;
    }

    // Retrieve the arraylist of selected ringtones from the database
    public void getListOfSelected() {
        RingtoneDatabaseHelper helper = new RingtoneDatabaseHelper(getActivity().getApplicationContext());
        int [] selectedPositions = helper.getSelectedTones();

        // For each position that is selected, get the ringtone in that position in the list of
        // all ringtones
        for(int i = 0; i < selectedPositions.length; i++) {
            selectedRingtonesList.add(allRingtonesList.get(selectedPositions[i]));
            Log.d(TAG, allRingtonesList.get(selectedPositions[i]).getToneName());
        }
    }

    // Retrieve a list of boolean values to know which items should be pre-checked
    public void getListOfChecked() {
        RingtoneDatabaseHelper helper = new RingtoneDatabaseHelper(getActivity().getApplicationContext());
        int [] selectedPositions = helper.getSelectedTones();
//        Arrays.fill(checked, false);

        // For each position that is selected, check off that position in the boolean array
        for (int i = 0; i < selectedPositions.length; i++) {
            checked[selectedPositions[i]] = true;
        }
    }

    public AlertDialog createDialog() {
        // Create a queue of tones that need to be updated in the database after operations are done
        tonesToUpdate = new LinkedList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                getListOfChecked();
            }
        }).start();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.ringtone_dialog_title)
                .setMultiChoiceItems(ringtoneNames, checked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // Get which tone was changed
                        PhoneTone selectedTone = allRingtonesList.get(which);

                        if (isChecked) {
                            // If the item is checked, add it to the selected items, update db
                            selectedTone.setChecked(1); // 1 means checked, 0 means not checked
                            Log.d(TAG, "" + selectedTone.getChecked());
                            selectedRingtonesList.add(selectedTone);

                        } else {
                            // If the item isn't checked, remove it from added items if it's there
                            selectedTone.setChecked(0);
                            Log.d(TAG, "" + selectedTone.getChecked());
                            selectedRingtonesList.remove(selectedTone);

                        }
                        checked[which] = isChecked;
                        Log.d(TAG, " boolean flipped on item " + which);
                        Log.d(TAG, selectedTone.getToneName() + " has been modified");

                        // Notify the adapter there was a change in data
                        selectedRingtonesAdapter.notifyDataSetChanged();

                        // Add this tone to a queue of items that need to be updated
                        tonesToUpdate.add(selectedTone);
                    }
                })
                .setNeutralButton(R.string.neutral_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        // Start a new thread that updates the database in the background
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (!tonesToUpdate.isEmpty()) {
                                    RingtoneDatabaseHelper helper = new RingtoneDatabaseHelper(getActivity().getApplicationContext());
                                    helper.updateRow(tonesToUpdate.peek());
                                    Log.d(TAG, tonesToUpdate.peek().getToneName() + " updated");
                                    tonesToUpdate.remove();
                                }
                            }
                        }).start();
                    }
                });

        return builder.show();
    }

    // Retrieve the current ringtone
    private Ringtone getCurrentDefaultRingtone() {
        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(
                getActivity().getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
        Ringtone current = RingtoneManager.getRingtone(getActivity().getApplicationContext(), defaultRingtoneUri);
        current.stop();
        return current;
    }

    @Override
    public void onResume() {
        super.onResume();
        Ringtone temp = getCurrentDefaultRingtone();
        currentSong.setText(temp.getTitle(getActivity().getApplicationContext()));
        temp.stop();
    }

    private AlertDialog.Builder refreshDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: make method in db helper to get you the URIs of selected, store them
                        // TODO: Call method to update DB version
                        // TODO: Set the rows with matching URIs to checked, then restart the app

                        RingtoneDatabaseHelper helper = new RingtoneDatabaseHelper(getActivity().getApplicationContext());
                        helper.reload();
                        helper.setPreviouslyCheckedItems(selectedRingtonesList);
                        getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setTitle("Are you sure?")
                .setMessage(R.string.refresh_dialog_message);

        return builder;
    }

    @Override
    public void onStop() {
        super.onStop();
        // Release ringtones
        playingRingtone.stop();

    }
}
