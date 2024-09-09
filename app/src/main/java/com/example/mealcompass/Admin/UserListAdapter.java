package com.example.mealcompass.Admin;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    private final List<UserListItem> mUserListItems;
    private final UserRepository userRepository;
    private final Context context;

    public static class UserListViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public ImageView userProfileImage;

        public UserListViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.userName);
            userProfileImage = view.findViewById(R.id.userProfileImage);
        }
    }

    public UserListAdapter(List<UserListItem> userListItems, UserRepository userRepository, Context context) {
        this.mUserListItems = userListItems;
        this.userRepository = userRepository;
        this.context = context;

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
        userRepository.loadUserProfileImage(userListItems.getUserId(), holder.userProfileImage, context);
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("userId", userListItems.getUserId());
            bundle.putString("userName", userListItems.getUserName());
            // if current page is showAllUsersFragment, navigate to userDetailsFragment
            // else if current page is adminFragment, navigate to userDetailsFragment
            if (context.getClass().getSimpleName().equals("ShowAllUsersFragment") ) {
                Navigation.findNavController(v).navigate(R.id.action_showAllUsersFragment_to_userDetailsFragment, bundle);
            } else {
                Navigation.findNavController(v).navigate(R.id.action_adminFragment_to_userDetailsFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserListItems.size();
    }
}

