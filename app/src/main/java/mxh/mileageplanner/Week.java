package mxh.mileageplanner;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Week {

    private ArrayList<Day> mDays;

    private Week(MyCalendar myCalendar, Calendar startDay) {
        mDays = new ArrayList<>(7);

        // first day of week
        Calendar day = startDay;

        // loop over the week's days, instantiating objects
        for (int i = 0; i < 7; i++) {
            mDays.add(new Day(myCalendar, day));
            day.add(Calendar.DATE, 1);
        }
    }

    public static ArrayList<Week> createMileageTimeline(MyCalendar myCalendar, int numWeeks) {
        /* Returns an ArrayList of numWeeks*Week objects */
        ArrayList<Week> weeks = new ArrayList<>(numWeeks);

        // get beginning of first day of current week
        Calendar day = Calendar.getInstance();
        day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);

        // add the specified number of weeks to the ArrayList
        for (int i = 0; i < numWeeks; i++) {
            // even though day isn't incremented, this seems to have the desired effect
            // when set to increment by 7 days each loop, results in every other week
            // it's possible that day is modified inside of the Week constructor and, somehow,
            // its value here is updated
            weeks.add(new Week(myCalendar, day));
        }
        return weeks;
    }

    public int[] getDailyMiles() {
        /* Returns array of miles for each day in the week */
        int[] dailyMiles = new int[7];
        for (int i = 0, j = mDays.size(); i < j; i++) {
            // get miles for each Day object in the Week
            dailyMiles[i] = mDays.get(i).getMiles();
        }
        return dailyMiles;
    }

    public int getTotalMiles() {
        /* Calculates total miles for the week */
        return Arrays.stream(this.getDailyMiles()).sum();
    }

    public String getPercentChange(Week otherWeek) {
        /* Calculates the percent increase/decrease in total miles from another Week object */
        // get change as ratio
        float otherWeekTotal = (float) otherWeek.getTotalMiles();
        float thisWeekTotal = (float) this.getTotalMiles();
        float change = (thisWeekTotal - otherWeekTotal) / otherWeekTotal;

        // convert change to a string containing percent change
        NumberFormat defaultFormat = NumberFormat.getPercentInstance();
        defaultFormat.setMinimumFractionDigits(1);
        defaultFormat.setMaximumFractionDigits(1);
        return defaultFormat.format(change);
    }

    public void updateDay(int idx, int newVal) {
        mDays.get(idx).setMiles(newVal);
    }
}