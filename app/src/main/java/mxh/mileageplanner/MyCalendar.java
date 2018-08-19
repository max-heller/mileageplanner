package mxh.mileageplanner;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MyCalendar {

    private long calID;
    private ContentResolver cr;

    MyCalendar(ContentResolver cr, String accountName, String displayName) {
        this.cr = cr;
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
        };
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " = ?))";
        String[] selectionArgs = new String[]{accountName, displayName};
        Cursor cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

        cur.moveToNext();
        this.calID = cur.getLong(cur.getColumnIndex(CalendarContract.Calendars._ID));
        cur.close();
    }

    private String formatTitle(int miles) {
        String title;
        if (miles > 0) {
            title = String.format("%dmi Run", miles);
        } else {
            title = "Rest Day!";
        }
        return title;
    }

    public long saveRun(Calendar day, int distance) {
        long startMillis = day.getTimeInMillis();
        long endMillis = startMillis + TimeUnit.DAYS.toMillis(1);

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.ALL_DAY, 1);
        values.put(CalendarContract.Events.TITLE, formatTitle(distance));
        values.put(CalendarContract.Events.DESCRIPTION, "Mileage Planner");
        values.put(CalendarContract.Events.CALENDAR_ID, this.calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "UTC");
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        return Long.parseLong(uri.getLastPathSegment());
    }

    public void updateRun(long eventID, int distance) {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, formatTitle(distance));
        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        this.cr.update(updateUri, values, null, null);
    }

    public RunEntry findRun(Calendar day) {
        String[] mProjection = new String[]{
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
        };

        Uri uri = CalendarContract.Events.CONTENT_URI;
        String selection = "((" + CalendarContract.Events.DESCRIPTION + " = ?) AND ("
                + CalendarContract.Events.DTSTART + " = ?))";
        String[] selectionArgs = new String[]{"Mileage Planner", String.valueOf(day.getTimeInMillis())};
        Cursor cur = this.cr.query(uri, mProjection, selection, selectionArgs, null);

        try {
            cur.moveToNext();
            long eventID = cur.getLong(cur.getColumnIndex(CalendarContract.Events._ID));
            int miles;
            try {
                miles = Integer.parseInt(cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE)));
            } catch (NumberFormatException err) {
                miles = 0;
            }
            return new RunEntry(eventID, miles);
        } catch (CursorIndexOutOfBoundsException err) {
            return null;
        }
    }

    public class RunEntry {

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
