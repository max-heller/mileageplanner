package mxh.mileageplanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Week> weeks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvWeeks = findViewById(R.id.rvWeeks);

        weeks = Week.createMileageTimeline(50);
        WeeksAdapter adapter = new WeeksAdapter(weeks);
        rvWeeks.setAdapter(adapter);

        rvWeeks.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvWeeks.addItemDecoration(itemDecoration);
    }
}