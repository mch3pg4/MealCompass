package com.example.mealcompass.Helpdesk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;

import org.w3c.dom.Text;

import java.util.List;

public class HelpdeskChatsAdapter extends RecyclerView.Adapter<HelpdeskChatsAdapter.HelpdeskChatsViewHolder> {
    private final List<HelpdeskChatsItem> mHelpdeskChatsItems;
    private final UserRepository userRepository;
    private final Context context;

    public static class HelpdeskChatsViewHolder extends RecyclerView.ViewHolder {
        public ImageView chatImage;
        public TextView chatName;
        public TextView chatMessage;
        public TextView chatTime;

        public HelpdeskChatsViewHolder(View view) {
            super(view);
            chatImage = view.findViewById(R.id.profileImageButton);
            chatName = view.findViewById(R.id.chatName);
            chatMessage = view.findViewById(R.id.chatContent);
            chatTime = view.findViewById(R.id.chatTime);
        }
    }

    public HelpdeskChatsAdapter(List<HelpdeskChatsItem> helpdeskChatsItems, UserRepository userRepository, Context context) {
        this.mHelpdeskChatsItems = helpdeskChatsItems;
        this.userRepository = userRepository;
        this.context = context;
    }

    @NonNull
    @Override
    public HelpdeskChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_chat_item, parent, false);
        return new HelpdeskChatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpdeskChatsViewHolder holder, int position) {
        HelpdeskChatsItem currentItem = mHelpdeskChatsItems.get(position);
        userRepository.loadUserProfileImage(currentItem.getSenderId(), holder.chatImage, context);
        holder.chatName.setText(currentItem.getChatName());
        holder.chatTime.setText(currentItem.getChatTime());
        holder.chatMessage.setText(currentItem.getChatMessage());

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("chatId", currentItem.getChatId());
            bundle.putString("chatName", currentItem.getChatName());
            bundle.putString("senderId", currentItem.getSenderId());
            Navigation.findNavController(v).navigate(R.id.action_helpdeskAdminFragment_to_helpdeskFragment, bundle );
        });
    }

    @Override
    public int getItemCount() {
        return mHelpdeskChatsItems.size();
    }
}
