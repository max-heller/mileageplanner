package mxh.mileageplanner;

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
                dailyMiles[j] = r.nextInt(15);
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
}
