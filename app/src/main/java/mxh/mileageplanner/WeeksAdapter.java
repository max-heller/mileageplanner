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

    public WeeksAdapter(List<Week> weeks) {
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

        ViewHolder viewHolder = new ViewHolder(weekView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final WeeksAdapter.ViewHolder viewHolder, final int position) {
        final Week week = mWeeks.get(position);
        viewHolder.setWeek(week, position);
    }

    @Override
    public int getItemCount() {
        return mWeeks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ArrayList<NumberPicker> numberPickers = new ArrayList<>();

        public TextView weekTextView;
        public TextView totalTextView;
        public TextView changeTextView;

        Week week;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);

            numberPickers.add((NumberPicker) itemView.findViewById(R.id.npMonday));
            numberPickers.add((NumberPicker) itemView.findViewById(R.id.npTuesday));
            numberPickers.add((NumberPicker) itemView.findViewById(R.id.npWednesday));
            numberPickers.add((NumberPicker) itemView.findViewById(R.id.npThursday));
            numberPickers.add((NumberPicker) itemView.findViewById(R.id.npFriday));
            numberPickers.add((NumberPicker) itemView.findViewById(R.id.npSaturday));
            numberPickers.add((NumberPicker) itemView.findViewById(R.id.npSunday));

            weekTextView = itemView.findViewById(R.id.tvWeek);
            totalTextView = itemView.findViewById(R.id.tvTotal);
            changeTextView = itemView.findViewById(R.id.tvChange);

            for (int i = 0; i < 7; i++) {
                final int dayIdx = i;
                NumberPicker np = numberPickers.get(i);
                np.setMinValue(0);
                np.setMaxValue(150);
                np.setWrapSelectorWheel(false);
                np.setOnScrollListener(new NumberPicker.OnScrollListener() {
                    @Override
                    public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
                        if (scrollState == SCROLL_STATE_IDLE) {
                            week.updateDay(dayIdx, numberPicker.getValue());
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

        public void setWeek(Week week, int position) {
            this.week = week;
            this.position = position;

            final int[] dailyMiles = week.getDailyMiles();

            weekTextView.setText(String.format("Week %d", position));
            totalTextView.setText(String.format("Total: %dmi", week.getTotalMiles()));

            if (position == 0) {
                changeTextView.setText("Change: N/A");
            } else {
                Week lastWeek = mWeeks.get(position - 1);
                String percentChange = week.getPercentChange(lastWeek);
                changeTextView.setText(String.format("Change: %s", percentChange));
            }

            for (int i = 0; i < 7; i++) {
                numberPickers.get(i).setValue(dailyMiles[i]);
            }
        }
    }
}