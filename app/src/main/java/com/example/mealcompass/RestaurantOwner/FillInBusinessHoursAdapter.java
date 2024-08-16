package com.example.mealcompass.RestaurantOwner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealcompass.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.List;

public class FillInBusinessHoursAdapter extends RecyclerView.Adapter<FillInBusinessHoursAdapter.ViewHolder> {

    private final List<FillInBusinessHoursItem> businessHoursItems;
    private final FragmentManager fragmentManager;



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dayTextView;
        public TextView dayOpeningTextView;
        public TextView dayClosingTextView;
        public CheckBox closedCheckBox;
        public CheckBox splitHoursCheckBox;
        public LinearLayout splitHoursLayout;
        public TextView splitDayOpeningTextView;
        public TextView splitDayClosingTextView;
        public MaterialButton openingHourButton, closingHourButton, splitOpeningHourButton, splitClosingHourButton;

        public ViewHolder(View view) {
            super(view);
            dayTextView = view.findViewById(R.id.dayTextView);
            dayOpeningTextView = view.findViewById(R.id.dayOpeningTextView);
            dayClosingTextView = view.findViewById(R.id.dayClosingTextView);
            closedCheckBox = view.findViewById(R.id.closedCheckBox);
            splitHoursCheckBox = view.findViewById(R.id.splitHoursCheckBox);
            splitHoursLayout = view.findViewById(R.id.splitHoursLayout);
            splitDayOpeningTextView = view.findViewById(R.id.splitDayOpeningTextView);
            splitDayClosingTextView = view.findViewById(R.id.splitDayClosingTextView);
            openingHourButton = view.findViewById(R.id.openingHourButton);
            closingHourButton = view.findViewById(R.id.closingHourButton);
            splitOpeningHourButton = view.findViewById(R.id.splitOpeningHourButton);
            splitClosingHourButton = view.findViewById(R.id.splitClosingHourButton);
        }
    }

    public FillInBusinessHoursAdapter(List<FillInBusinessHoursItem> businessHoursItems, FragmentManager fragmentManager) {
        this.businessHoursItems = businessHoursItems;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_hours_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FillInBusinessHoursItem item = businessHoursItems.get(position);

        holder.dayTextView.setText(item.getDay());
        holder.dayOpeningTextView.setText(item.getOpeningHour());
        holder.dayClosingTextView.setText(item.getClosingHour());
        holder.closedCheckBox.setChecked(item.isClosed());

        // Set visibility based on the item state
        holder.splitHoursLayout.setVisibility(item.isSplitHours() ? View.VISIBLE : View.GONE);

        // Handle checkboxes and buttons here, e.g., opening time pickers
        holder.closedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setClosed(isChecked);

            if (isChecked) {
                //grey out the opening and closing time
                holder.dayOpeningTextView.setEnabled(false);
                holder.dayClosingTextView.setEnabled(false);
                holder.splitDayOpeningTextView.setEnabled(false);
                holder.splitDayClosingTextView.setEnabled(false);
                holder.openingHourButton.setEnabled(false);
                holder.closingHourButton.setEnabled(false);
                holder.splitOpeningHourButton.setEnabled(false);
                holder.splitClosingHourButton.setEnabled(false);
                holder.dayOpeningTextView.setAlpha(0.5f); // 50% transparent
                holder.dayClosingTextView.setAlpha(0.5f);
                holder.splitDayOpeningTextView.setAlpha(0.5f);
                holder.splitDayClosingTextView.setAlpha(0.5f);
                holder.openingHourButton.setAlpha(0.5f);
                holder.closingHourButton.setAlpha(0.5f);
                holder.splitOpeningHourButton.setAlpha(0.5f);
                holder.splitClosingHourButton.setAlpha(0.5f);
            } else {
                holder.dayOpeningTextView.setEnabled(true);
                holder.dayClosingTextView.setEnabled(true);
                holder.splitDayOpeningTextView.setEnabled(true);
                holder.splitDayClosingTextView.setEnabled(true);
                holder.openingHourButton.setEnabled(true);
                holder.closingHourButton.setEnabled(true);
                holder.splitOpeningHourButton.setEnabled(true);
                holder.splitClosingHourButton.setEnabled(true);
                holder.dayOpeningTextView.setAlpha(1.0f); // 100% opaque
                holder.dayClosingTextView.setAlpha(1.0f);
                holder.splitDayOpeningTextView.setAlpha(1.0f);
                holder.splitDayClosingTextView.setAlpha(1.0f);
                holder.openingHourButton.setAlpha(1.0f);
                holder.closingHourButton.setAlpha(1.0f);
                holder.splitOpeningHourButton.setAlpha(1.0f);
                holder.splitClosingHourButton.setAlpha(1.0f);
            }
        });

       // Set up listeners for the buttons to open time pickers
        holder.openingHourButton.setOnClickListener(v -> {
            // Open time picker for the opening hour
            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(9)
                    .setMinute(0)
                    .setTitleText("Select Opening Hour")
                    .build();

            timePicker.show(fragmentManager, "tag");
        });

        holder.closingHourButton.setOnClickListener(v -> {
            // Open time picker for the closing hour
            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(18)
                    .setMinute(0)
                    .setTitleText("Select Closing Hour")
                    .build();

            timePicker.show(fragmentManager, "tag");
        });

        holder.splitOpeningHourButton.setOnClickListener(v -> {
            // Open time picker for the opening hour
            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(9)
                    .setMinute(0)
                    .setTitleText("Select Split Opening Hour")
                    .build();

            timePicker.show(fragmentManager, "tag");
        });

        holder.splitClosingHourButton.setOnClickListener(v -> {
            // Open time picker for the closing hour
            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(18)
                    .setMinute(0)
                    .setTitleText("Select Split Closing Hour")
                    .build();

            timePicker.show(fragmentManager, "tag");
        });


    }

    @Override
    public int getItemCount() {
        return businessHoursItems.size();
    }
}

