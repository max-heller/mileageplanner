package mxh.mileageplanner;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Week {

    private ArrayList<Day> mDays;

    private Week(MyCalendar myCalendar) {
        Calendar day = Calendar.getInstance();
        day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        day.set(Calendar.HOUR_OF_DAY, 0);
        day.clear(Calendar.MINUTE);
        day.clear(Calendar.SECOND);
        day.clear(Calendar.MILLISECOND);
        mDays = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            mDays.add(new Day(myCalendar, day));
            day.add(Calendar.DATE, 1);
        }
    }

    public static ArrayList<Week> createMileageTimeline(MyCalendar myCalendar, int numWeeks) {
        ArrayList<Week> weeks = new ArrayList<>(numWeeks);
        for (int i = 0; i < numWeeks; i++) {
            weeks.add(new Week(myCalendar));
        }
        return weeks;
    }

    public int[] getDailyMiles() {
        int[] dailyMiles = new int[7];
        for (int i = 0, j = mDays.size(); i < j; i++) {
            dailyMiles[i] = mDays.get(i).getMiles();
        }
        return dailyMiles;
    }

    public int getTotalMiles() {
        return Arrays.stream(this.getDailyMiles()).sum();
    }

    public String getPercentChange(Week otherWeek) {
        float otherWeekTotal = (float) otherWeek.getTotalMiles();
        float thisWeekTotal = (float) this.getTotalMiles();
        float change = (thisWeekTotal - otherWeekTotal) / otherWeekTotal;

        NumberFormat defaultFormat = NumberFormat.getPercentInstance();
        defaultFormat.setMinimumFractionDigits(1);
        defaultFormat.setMaximumFractionDigits(1);
        return defaultFormat.format(change);
    }

    public void updateDay(int idx, int newVal) {
        mDays.get(idx).setMiles(newVal);
    }
}