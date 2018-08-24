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

        // find RecyclerView in layout
        RecyclerView rvWeeks = findViewById(R.id.rvWeeks);

        // instantiate class used to interact with calendar provider
        MyCalendar myCalendar = new MyCalendar(getContentResolver(),
                "heller.max1@gmail.com", "Routine");

        // load a set number of weeks
        // TODO: dynamic loading as you scroll
        weeks = Week.createMileageTimeline(myCalendar, 5);

        // set up the RecyclerView's adapter
        WeeksAdapter adapter = new WeeksAdapter(weeks);
        rvWeeks.setAdapter(adapter);

        // use basic linear layout of week views
        rvWeeks.setLayoutManager(new LinearLayoutManager(this));

        // set dividers to be displayed between weeks
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvWeeks.addItemDecoration(itemDecoration);

        // enable snapping to weeks when scrolling
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvWeeks);
    }
}