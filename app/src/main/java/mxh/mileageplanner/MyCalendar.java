package mxh.mileageplanner;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MyCalendar {

    private long calID;
    private ContentResolver cr;

    MyCalendar(ContentResolver cr, String accountName, String displayName) {
        /* Instantiates object with the correct calendar ID to use */
        this.cr = cr;
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
        };

        // filter calendars by passed in params
        // TODO: hardcoded in MainActivity, make dynamic
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " = ?))";
        String[] selectionArgs = new String[]{accountName, displayName};
        Cursor cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

        // get ID of the first, and presumably only, calendar
        assert cur != null;
        cur.moveToNext();
        this.calID = cur.getLong(cur.getColumnIndex(CalendarContract.Calendars._ID));
        cur.close();
    }

    private String formatTitle(int miles) {
        /* Generates an appropriate title for a calendar event */
        String title;
        if (miles > 0) {
            title = String.format("%dmi Run", miles);
        } else {
            title = "Rest Day!";
        }
        return title;
    }

    public long saveRun(Calendar day, int distance) {
        /* Saves a run to the calendar */
        // get start and end times in milliseconds
        long startMillis = day.getTimeInMillis();
        long endMillis = startMillis + TimeUnit.DAYS.toMillis(1);
        Log.d("Day", "Saving at time: " + String.valueOf(startMillis));

        // set values for the event
        // IMPORTANT: EVENT_TIMEZONE must be UTC for all day events
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.ALL_DAY, 1);
        values.put(CalendarContract.Events.TITLE, formatTitle(distance));
        values.put(CalendarContract.Events.DESCRIPTION, "Mileage Planner");
        values.put(CalendarContract.Events.CALENDAR_ID, this.calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "UTC");
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);

        // save event to the calendar
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        // return event ID
        return Long.parseLong(uri.getLastPathSegment());
    }

    public void updateRun(long eventID, int distance) {
        /* Updates a run already stored in the calendar */
        ContentValues values = new ContentValues();
        // set the new title appropriately
        values.put(CalendarContract.Events.TITLE, formatTitle(distance));
        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        this.cr.update(updateUri, values, null, null);
    }

    public RunEntry findRun(Calendar day) {
        /* Gets the run saved at some day if it exists */
        // get start and end times
        // IMPORTANT: must be in UTC, as noted in saveRun
        long startMillis = day.getTimeInMillis();
        TimeZone tz = day.getTimeZone();
        long offsetFromUTC = tz.getOffset(startMillis);
        startMillis += offsetFromUTC;
        long endMillis = startMillis + TimeUnit.DAYS.toMillis(1);
        Log.d("Day", "Finding event at time: " + String.valueOf(startMillis));

        Uri uri = CalendarContract.Events.CONTENT_URI;
        String[] mProjection = new String[]{
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART
        };

        // filter by known values
        String selection = "(("
                + CalendarContract.Events.DESCRIPTION + " = ?) AND ("
                + CalendarContract.Events.CALENDAR_ID + " = ?) AND ("
                + CalendarContract.Events.DTSTART + " = ?) AND ("
                + CalendarContract.Events.DTEND + " = ?))";
        String[] selectionArgs = new String[]{
                "Mileage Planner",
                String.valueOf(calID),
                String.valueOf(startMillis),
                String.valueOf(endMillis)};

        Cursor cur = cr.query(uri, mProjection, selection, selectionArgs, null);

        // process and return event if it's found, return null if it isn't
        try {
            cur.moveToNext();
            long eventID = cur.getLong(cur.getColumnIndex(CalendarContract.Events._ID));
            Log.d("MyCalendar", "Found event with id: " + String.valueOf(eventID));

            // handle case of rest days, where title won't contain the number of miles (0)
            int miles;
            try {
                miles = Integer.parseInt(cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE)));
            } catch (NumberFormatException err) {
                miles = 0;
            }

            cur.close();
            return new RunEntry(eventID, miles);
        } catch (CursorIndexOutOfBoundsException err) {
            Log.d("MyCalendar", "No search results: " + err);
            cur.close();
            return null;
        }
    }

    public class RunEntry {
        /* Provides a simple structure for storing information about a run event in calendar */

        long eventID;
        int miles;

        RunEntry(long eventID, int miles) {
            this.eventID = eventID;
            this.miles = miles;
        }

        public int getMiles() {
            return miles;
        }

        public long getEventID() {
            return eventID;
        }
    }
}
