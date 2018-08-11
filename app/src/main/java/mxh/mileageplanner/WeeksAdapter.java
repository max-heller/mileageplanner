package mxh.mileageplanner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class WeeksAdapter extends RecyclerView.Adapter<WeeksAdapter.ViewHolder> {

    private List<Week> mWeeks;

    public WeeksAdapter(List<Week> weeks) {
        mWeeks = weeks;
    }

    @Override
    public WeeksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View weekView = inflater.inflate(R.layout.item_week, parent, false);

        ViewHolder viewHolder = new ViewHolder(weekView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeeksAdapter.ViewHolder viewHolder, int position) {
        Week week = mWeeks.get(position);

        viewHolder.mondayTextView.setText(String.valueOf(week.getDailyMiles()[0]));
        viewHolder.tuesdayTextView.setText(String.valueOf(week.getDailyMiles()[1]));
        viewHolder.wednesdayTextView.setText(String.valueOf(week.getDailyMiles()[2]));
        viewHolder.thursdayTextView.setText(String.valueOf(week.getDailyMiles()[3]));
        viewHolder.fridayTextView.setText(String.valueOf(week.getDailyMiles()[4]));
        viewHolder.saturdayTextView.setText(String.valueOf(week.getDailyMiles()[5]));
        viewHolder.sundayTextView.setText(String.valueOf(week.getDailyMiles()[6]));
    }

    @Override
    public int getItemCount() {
        return mWeeks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mondayTextView;
        public TextView tuesdayTextView;
        public TextView wednesdayTextView;
        public TextView thursdayTextView;
        public TextView fridayTextView;
        public TextView saturdayTextView;
        public TextView sundayTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mondayTextView = itemView.findViewById(R.id.tvMonday);
            tuesdayTextView = itemView.findViewById(R.id.tvTuesday);
            wednesdayTextView = itemView.findViewById(R.id.tvWednesday);
            thursdayTextView = itemView.findViewById(R.id.tvThursday);
            fridayTextView = itemView.findViewById(R.id.tvFriday);
            saturdayTextView = itemView.findViewById(R.id.tvSaturday);
            sundayTextView = itemView.findViewById(R.id.tvSunday);
        }
    }
}
