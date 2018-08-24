package mxh.mileageplanner;

import java.util.Calendar;

public class Day {

    private MyCalendar myCal;
    private int miles;
    private long eventID;

    Day(MyCalendar myCalendar, Calendar date) {
        // store instance of calendar helper class
        myCal = myCalendar;

        // look for existing run in calendar
        MyCalendar.RunEntry run = myCalendar.findRun(date);
        if (run == null) {
            // create a new run event in the calendar if one doesn't exist
            miles = 0;
            eventID = myCalendar.saveRun(date, miles);
        } else {
            // use the existing calendar event
            miles = run.getMiles();
            eventID = run.getEventID();
        }
    }

    public int getMiles() {
        return miles;
    }

    public void setMiles(int miles) {
        this.miles = miles;

        // update the calendar event
        myCal.updateRun(this.eventID, miles);
    }
}
