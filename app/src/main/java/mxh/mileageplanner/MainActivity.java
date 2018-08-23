package mxh.mileageplanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Week> weeks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvWeeks = findViewById(R.id.rvWeeks);

        MyCalendar myCalendar = new MyCalendar(getContentResolver(),
                "heller.max1@gmail.com", "Routine");

        weeks = Week.createMileageTimeline(myCalendar, 5);
        WeeksAdapter adapter = new WeeksAdapter(weeks);
        rvWeeks.setAdapter(adapter);

        rvWeeks.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvWeeks.addItemDecoration(itemDecoration);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvWeeks);
    }
}