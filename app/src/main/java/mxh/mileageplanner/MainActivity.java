package mxh.mileageplanner;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ArrayList<Week> weeks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvWeeks = findViewById(R.id.rvWeeks);

        // calendar stuff
        final String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
        };
        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_DISPLAY_NAME_INDEX = 2;

        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " = ?))";
        String[] selectionArgs = new String[]{"heller.max1@gmail.com", "Routine"};
        Cursor cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

        cur.moveToNext();
        long calID = cur.getLong(PROJECTION_ID_INDEX);
        Log.i("calendar display name", cur.getString(PROJECTION_DISPLAY_NAME_INDEX));
        Log.i("calendar id", String.valueOf(cur.getLong(PROJECTION_ID_INDEX)));
        cur.close();

        // just testing, shouldn't actually be here
        Calendar beginTime = Calendar.getInstance();
        beginTime.clear();
        beginTime.set(2018, 7, 15);
        long startMillis = beginTime.getTimeInMillis();
        long endMillis = startMillis + TimeUnit.DAYS.toMillis(1);

        cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.ALL_DAY, 1);
        values.put(CalendarContract.Events.TITLE, "6mi Run");
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "UTC");
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
        uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        long eventID = Long.parseLong(uri.getLastPathSegment());
        Log.i("event id", String.valueOf(eventID));
        // end calendar stuff

        weeks = Week.createMileageTimeline(25);
        WeeksAdapter adapter = new WeeksAdapter(weeks, calID);
        rvWeeks.setAdapter(adapter);

        rvWeeks.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvWeeks.addItemDecoration(itemDecoration);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvWeeks);
    }
}