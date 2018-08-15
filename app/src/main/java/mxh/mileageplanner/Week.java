package mxh.mileageplanner;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Week {
    private int[] mDailyMiles;

    public Week(int[] dailyMiles) {
        mDailyMiles = dailyMiles;
    }

    public static ArrayList<Week> createMileageTimeline(int numWeeks) {
        ArrayList<Week> weeks = new ArrayList<Week>();
        Random r = new Random();

        for (int i = 0; i < numWeeks; i++) {
            int[] dailyMiles = new int[7];
            for (int j = 0; j < 7; j++) {
                dailyMiles[j] = r.nextInt(10);
            }

            weeks.add(new Week(dailyMiles));
        }

        return weeks;
    }

    public int[] getDailyMiles() {
        return mDailyMiles;
    }

    public int getTotalMiles() {
        return Arrays.stream(mDailyMiles).sum();
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
        mDailyMiles[idx] = newVal;
    }
}
