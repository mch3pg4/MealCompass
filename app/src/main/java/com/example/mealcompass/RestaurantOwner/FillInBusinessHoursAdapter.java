package com.example.mealcompass.RestaurantOwner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealcompass.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class FillInBusinessHoursAdapter extends RecyclerView.Adapter<FillInBusinessHoursAdapter.ViewHolder> {

    private final List<FillInBusinessHoursItem> businessHoursItems;



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

    public FillInBusinessHoursAdapter(List<FillInBusinessHoursItem> businessHoursItems) {
        this.businessHoursItems = businessHoursItems;
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

//        // Set visibility based on the item state
        holder.splitHoursLayout.setVisibility(item.isSplitHours() ? View.VISIBLE : View.GONE);
//
//        holder.splitHoursCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            item.setSplitHours(isChecked);
//            // Update the visibility of split hours layout
//            holder.splitHoursLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
//
//        });

//        holder.splitHoursCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            item.setSplitHours(isChecked);
//            if (isChecked) {
//                holder.splitHoursLayout.setVisibility(View.VISIBLE);
//            } else {
//                holder.splitHoursLayout.setVisibility(View.GONE);
//            }
////            if (item.isSplitHours()) {
////                holder.splitHoursLayout.setVisibility(View.VISIBLE);
////                holder.splitDayOpeningTextView.setText(item.getSplitOpeningHour());
////                holder.splitDayClosingTextView.setText(item.getSplitClosingHour());
////            } else {
////                holder.splitHoursLayout.setVisibility(View.GONE);
////            }
//        });



        // Handle checkboxes and buttons here, e.g., opening time pickers
        holder.closedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setClosed(isChecked);

            if (isChecked) {
                //grey out the opening and closing time
                holder.dayOpeningTextView.setEnabled(false);
                holder.dayClosingTextView.setEnabled(false);
                holder.openingHourButton.setEnabled(false);
                holder.closingHourButton.setEnabled(false);
                holder.dayOpeningTextView.setAlpha(0.5f); // 50% transparent
                holder.dayClosingTextView.setAlpha(0.5f);
                holder.openingHourButton.setAlpha(0.5f);
                holder.closingHourButton.setAlpha(0.5f);
            } else {
                holder.dayOpeningTextView.setEnabled(true);
                holder.dayClosingTextView.setEnabled(true);
                holder.openingHourButton.setEnabled(true);
                holder.closingHourButton.setEnabled(true);
                holder.dayOpeningTextView.setAlpha(1.0f); // 100% opaque
                holder.dayClosingTextView.setAlpha(1.0f);
                holder.openingHourButton.setAlpha(1.0f);
                holder.closingHourButton.setAlpha(1.0f);
            }
        });


        // Set up listeners for the buttons to open time pickers


    }

    @Override
    public int getItemCount() {
        return businessHoursItems.size();
    }
}

