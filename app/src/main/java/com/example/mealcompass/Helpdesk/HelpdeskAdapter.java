package com.example.mealcompass.Helpdesk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealcompass.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HelpdeskAdapter extends RecyclerView.Adapter<HelpdeskAdapter.ViewHolder> {
    private List<Helpdesk> helpdeskList;
    private final String userId;
    private static final int SENDER_VIEW = 1;
    private static final int RECEIVER_VIEW = 2;

    // ViewHolder class with cached views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView, timeTextView;

        public ViewHolder(View view, int viewType) {
            super(view);
            if (viewType == SENDER_VIEW) {
                messageTextView = view.findViewById(R.id.senderMessage);
                timeTextView = view.findViewById(R.id.timeMessage);
            } else {
                messageTextView = view.findViewById(R.id.receiverMessage);
                timeTextView = view.findViewById(R.id.timeMessage);
            }
        }

        public void bindMessage(Helpdesk helpdesk) {
            messageTextView.setText(helpdesk.getMessage());
            // convert time to string
            Date sendTime = helpdesk.getSendTime();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
            timeTextView.setText(sdf.format(sendTime));
        }
    }

    public HelpdeskAdapter(List<Helpdesk> helpdeskList, String userId) {
        this.helpdeskList = helpdeskList;
        this.userId = userId;
    }

    @Override
    public int getItemViewType(int position) {
        Helpdesk helpdesk = helpdeskList.get(position);
        return helpdesk.getSenderId().equals(userId) ? SENDER_VIEW : RECEIVER_VIEW;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == SENDER_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.helpdesk_chat_sender, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.helpdesk_chat_receiver, parent, false);
        }
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Helpdesk helpdesk = helpdeskList.get(position);
        holder.bindMessage(helpdesk);
    }

    @Override
    public int getItemCount() {
        return helpdeskList.size();
    }

    // Method to update the list when new messages are added
    public void updateMessages(List<Helpdesk> newMessages, RecyclerView recyclerView) {
        this.helpdeskList = newMessages;
        notifyItemChanged(newMessages.size()); // or optimize using notifyItemRangeInserted()
        recyclerView.scrollToPosition(newMessages.size() - 1); // scroll to the last message


    }
}

