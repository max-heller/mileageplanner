package mxh.mileageplanner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WeeksAdapter extends RecyclerView.Adapter<WeeksAdapter.ViewHolder> {

    private List<Week> mWeeks;
    private RecyclerView mRecyclerView;

    WeeksAdapter(List<Week> weeks) {
        mWeeks = weeks;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public WeeksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View weekView = inflater.inflate(R.layout.item_week, parent, false);

        return new ViewHolder(weekView);
    }

    @Override
    public void onBindViewHolder(final WeeksAdapter.ViewHolder viewHolder, final int position) {
        // get week for bound ViewHolder and update views based on its contents
        final Week week = mWeeks.get(position);
        viewHolder.setWeek(week, position);
    }

    @Override
    public int getItemCount() {
        return mWeeks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ArrayList<NumberPicker> numberPickers = new ArrayList<>();

        TextView weekTextView;
        TextView totalTextView;
        TextView changeTextView;

        Week week;
        int position;

        ViewHolder(View itemView) {
            super(itemView);

            // find all of the number pickers and add them to ArrayList
            numberPickers.add((NumberPicker) itemView.findViewById(R.id.npMonday));
            numberPickers.add((NumberPicker) itemView.findViewById(R.id.npTuesday));
            numberPickers.add((NumberPicker) itemView.findViewById(R.id.npWednesday));
            numberPickers.add((NumberPicker) itemView.findViewById(R.id.npThursday));
            numberPickers.add((NumberPicker) itemView.findViewById(R.id.npFriday));
            numberPickers.add((NumberPicker) itemView.findViewById(R.id.npSaturday));
            numberPickers.add((NumberPicker) itemView.findViewById(R.id.npSunday));

            // find textViews to be updated
            weekTextView = itemView.findViewById(R.id.tvWeek);
            totalTextView = itemView.findViewById(R.id.tvTotal);
            changeTextView = itemView.findViewById(R.id.tvChange);

            for (int i = 0; i < 7; i++) {
                final int dayIdx = i;

                // set some basic options for the number picker
                NumberPicker np = numberPickers.get(i);
                np.setMinValue(0);
                np.setMaxValue(150);
                np.setWrapSelectorWheel(false);

                // add a listener to handle updating the week and RecyclerView when a change is made
                np.setOnScrollListener(new NumberPicker.OnScrollListener() {
                    @Override
                    public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
                        if (scrollState == SCROLL_STATE_IDLE) {
                            week.updateDay(dayIdx, numberPicker.getValue());

                            // ask the recyclerView to update when it has time
                            // necessary to avoid errors when notifyItemChanged ends up running
                            // while a ViewHolder is already being bound
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    notifyItemRangeChanged(position, position + 1);
                                }
                            });
                        }
                    }
                });
            }
        }

        void setWeek(Week week, int position) {
            /* Updates the view based on the attributes of its corresponding Week */
            this.week = week;
            this.position = position;

            // get the miles of each day
            final int[] dailyMiles = week.getDailyMiles();

            // display total miles for the week
            totalTextView.setText(String.format("Total: %dmi", week.getTotalMiles()));

            // handle different behavior for first (current) week
            if (position == 0) {
                weekTextView.setText("This Week");
                changeTextView.setText("Change: N/A");
            } else {
                // display percent change from last week
                Week lastWeek = mWeeks.get(position - 1);
                String percentChange = week.getPercentChange(lastWeek);
                changeTextView.setText(String.format("Change: %s", percentChange));

                // set week label based on its relation to the current week
                if (position == 1) {
                    weekTextView.setText("Next Week");
                } else {
                    weekTextView.setText(String.format("%d Weeks from Now", position));
                }
            }

            // update the number pickers with the miles for each day
            // TODO: doesn't update when events are first loaded from calendar?
            for (int i = 0; i < 7; i++) {
                numberPickers.get(i).setValue(dailyMiles[i]);
            }
        }
    }
}