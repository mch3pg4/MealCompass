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


        holder.openingHourButton.setOnClickListener(v -> showTimePicker(9, "Select Opening Hour", fragmentManager, holder.dayOpeningTextView, position, true, false, false, false));

        holder.closingHourButton.setOnClickListener(v -> showTimePicker(18, "Select Closing Hour", fragmentManager, holder.dayClosingTextView, position, false, true, false, false));

        holder.splitOpeningHourButton.setOnClickListener(v -> showTimePicker(9, "Select Split Opening Hour", fragmentManager, holder.splitDayOpeningTextView, position, false, false, true, false));

        holder.splitClosingHourButton.setOnClickListener(v -> showTimePicker(18, "Select Split Closing Hour", fragmentManager, holder.splitDayClosingTextView, position, false, false, false, true));

    }

    // function to show time picker and set the selected time to the target TextView
    private void showTimePicker(int initialHour, String title, FragmentManager fragmentManager, TextView targetTextView, int position, boolean isSetOpeningHour, boolean isSetClosingHour, boolean isSetSplitOpeningHour, boolean isSetSplitClosingHour){
        // Open time picker for the given parameters
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(initialHour)
                .setMinute(0)
                .setTitleText(title)
                .build();

        timePicker.addOnPositiveButtonClickListener(view -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            // Format the hour in 12-hour format
            String formattedHour = (hour == 0 || hour == 12) ? "12" : String.valueOf(hour % 12);

            // Format the minutes with leading zero if needed
            String formattedMinute = minute < 10 ? "0" + minute : String.valueOf(minute);

            // Determine AM or PM
            String amPm = (hour >= 12) ? "PM" : "AM";

            // Combine to form the final time string
            String formattedTime = formattedHour + ":" + formattedMinute + " " + amPm;

            // Set the formatted time to the target TextView
            targetTextView.setText(formattedTime);

            // Update the corresponding item in businessHoursItems
            FillInBusinessHoursItem item = businessHoursItems.get(position);
            if (isSetOpeningHour) {
                item.setOpeningHour(formattedTime);
            } else if (isSetClosingHour) {
                item.setClosingHour(formattedTime);
            } else if (isSetSplitOpeningHour) {
                item.setSplitOpeningHour(formattedTime);
            } else if (isSetSplitClosingHour) {
                item.setSplitClosingHour(formattedTime);
            }
        });

        timePicker.show(fragmentManager, "tag");
    }

    @Override
    public int getItemCount() {
        return businessHoursItems.size();
    }
}

