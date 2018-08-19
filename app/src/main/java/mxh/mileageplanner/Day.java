package mxh.mileageplanner;

import java.util.Calendar;

public class Day {

    private MyCalendar myCal;
    private int miles;
    private long eventID;

    Day(MyCalendar myCalendar, Calendar date) {
        myCal = myCalendar;
        MyCalendar.RunEntry run = myCalendar.findRun(date);
        if (run == null) {
            miles = 0;
            eventID = myCalendar.saveRun(date, miles);
        } else {
            miles = run.getMiles();
            eventID = run.getEventID();
        }
    }

    public int getMiles() {
        return miles;
    }

    public void setMiles(int miles) {
        this.miles = miles;
        myCal.updateRun(this.eventID, miles);
    }
}
