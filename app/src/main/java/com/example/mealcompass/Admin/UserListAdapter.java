package com.example.mealcompass.Admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealcompass.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    private final List<UserListItem> mUserListItems;

    public static class UserListViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public ImageView userProfileImage;

        public UserListViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.userName);
            userProfileImage = view.findViewById(R.id.userProfileImage);

        }

    }

    public UserListAdapter(List<UserListItem> userListItems) {
        this.mUserListItems = userListItems;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new UserListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserListViewHolder holder, int position) {
        UserListItem userListItems = mUserListItems.get(position);
        holder.userName.setText(userListItems.getUserName());
        holder.userProfileImage.setImageResource(R.drawable.placeholder);
    }

    @Override
    public int getItemCount() {
        return mUserListItems.size();
    }
}

