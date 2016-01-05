package com.candidcold.cacophony;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by davidmorant on 3/16/15.
 */
public class RingtoneDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = RingtoneDatabaseHelper.class.getSimpleName();

    // Database information
    private static int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "RingtonesDb";

    // Table name
    public static final String TABLE_NAME = "Ringtones";

    // Column names
    public static final String KEY_ID = "_id";
    public static final String KEY_RINGTONE_URI = "Uri";
    public static final String KEY_RINGTONE_NAME = "Name";
    public static final String KEY_CHECKED = "Checked";

    // Ringtone stuff
    public RingtoneManager ringtoneManager;
    public Cursor ringtoneCursor;
    public Context applicationContext;
    public SQLiteDatabase db;

    // Auto-generated methods
    public RingtoneDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        applicationContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        // Create the table if it doesn't exist
        String createTable;
        createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_RINGTONE_URI + " TEXT,"
                + KEY_RINGTONE_NAME + " TEXT," + KEY_CHECKED + " INTEGER" + ")";

        db.execSQL(createTable);

        // Gets all the ringtones on the device.
        ringtoneManager = new RingtoneManager(applicationContext);
        ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);
        ringtoneCursor = ringtoneManager.getCursor();

        // Convert the cursor into tones and add them to the database.
        convertToTones(ringtoneCursor);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Recreate the table when it's upgraded
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Convert the cursor's content into tones and add it to the DB
    private void convertToTones(Cursor allRingtones) {
        while (allRingtones.moveToNext()) {
            int currentPosition = allRingtones.getPosition();
            Uri currentUri = ringtoneManager.getRingtoneUri(currentPosition);
            PhoneTone tone = new PhoneTone();
            Ringtone tempRingtone = RingtoneManager.getRingtone(
                    applicationContext, currentUri);
            tempRingtone.stop();
            tone.setToneName(tempRingtone.getTitle(applicationContext));
            tone.setTonePath(currentUri.toString());

            // Add this tone to the database
            addTone(tone);
        }
    }

    // Add the tone to the database
    public void addTone(PhoneTone tone) {

        ContentValues values = new ContentValues();
        values.put(KEY_RINGTONE_NAME, tone.getToneName());
        values.put(KEY_RINGTONE_URI, tone.getTonePath().toString());
        values.put(KEY_CHECKED, tone.getChecked());

        // Insert row
        db.insertOrThrow(TABLE_NAME, null, values);
    }

    // Get the IDs of tones that were selected
    public int[] getSelectedTones() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectionQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_CHECKED + " =" + 1;

        // Get all rows that are checked (meaning they were selected)
        Cursor selectedCursor = db.rawQuery(selectionQuery, null);

        int [] selectedPositions = new int[selectedCursor.getCount()];
        Log.d(TAG, " There are " + selectedPositions.length + " checked items");
        Log.d(TAG, " The cursor has " + selectedCursor.getCount() + " items");
        int counter = 0;

        // While you can move to the next cursor, record the positions of checked rows
        for (selectedCursor.moveToFirst(); !selectedCursor.isAfterLast(); selectedCursor.moveToNext()) {
            selectedPositions[counter] = selectedCursor.getInt(selectedCursor.getColumnIndex(KEY_ID)) - 1;
            counter++;
        }

        return selectedPositions;
    }

    // Reload the db
    public void reload() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Re-checks other checked items
    public void setPreviouslyCheckedItems(ArrayList<PhoneTone> formerChecked) {

        for (PhoneTone checked : formerChecked) {
            updateRow(checked);
        }
    }

    // Update the row in the database with the new value of the tone's checked state
    public void updateRow(PhoneTone tone) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CHECKED, tone.getChecked());

        Log.d(TAG, tone.getToneName() + " row updated");

        // Update the row that has the specific name of the one that was changed.
        db.update(TABLE_NAME, values, KEY_RINGTONE_NAME + " = ?", new String[]  {"" + tone.getToneName()});


    }

}
