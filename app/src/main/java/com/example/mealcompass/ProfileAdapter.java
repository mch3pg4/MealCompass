package com.example.mealcompass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private final List<ProfileItem> mProfileItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView icon;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            icon = view.findViewById(R.id.icon);
        }
    }

    public ProfileAdapter(List<ProfileItem> profileItems) {
        this.mProfileItems = profileItems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfileItem profileItem = mProfileItems.get(position);
        holder.title.setText(profileItem.getTitle());
        holder.icon.setImageResource(profileItem.getIcon());
    }

    @Override
    public int getItemCount() {
        return mProfileItems.size();
    }
}
