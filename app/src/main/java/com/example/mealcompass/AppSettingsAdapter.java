package com.example.mealcompass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppSettingsAdapter extends RecyclerView.Adapter<AppSettingsAdapter.ViewHolder> {

    private final List<AppSettingsItem> mAppItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView icon;


        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            icon = view.findViewById(R.id.icon);
        }
    }

    public AppSettingsAdapter(List<AppSettingsItem> appItems) {
        this.mAppItems = appItems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppSettingsItem appSettingsItem = mAppItems.get(position);
        holder.title.setText(appSettingsItem.getTitle());
        holder.icon.setImageResource(appSettingsItem.getIcon());
    }

    @Override
    public int getItemCount() {
        return mAppItems.size();
    }
}
