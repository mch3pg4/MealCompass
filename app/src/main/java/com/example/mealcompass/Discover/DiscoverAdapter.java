package com.example.mealcompass.Discover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealcompass.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.ViewHolder> {

    private final List<DiscoverItem> mDiscoverItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView authorName, timePosted, discoverArticleTitle, discoverArticleDescription;
        public ImageView discoverImage;
        public MaterialButton readMoreButton;
        public ImageButton moreOptionsButton;

        public ViewHolder(View view) {
            super(view);
            authorName = view.findViewById(R.id.authorName);
            timePosted = view.findViewById(R.id.timePosted);
            discoverArticleTitle = view.findViewById(R.id.discoverArticleTitle);
            discoverArticleDescription = view.findViewById(R.id.discoverArticleDescription);
            discoverImage = view.findViewById(R.id.discoverImage);
            readMoreButton = view.findViewById(R.id.readMoreButton);
            moreOptionsButton = view.findViewById(R.id.moreButton);
        }

    }

    public DiscoverAdapter(List<DiscoverItem> discoverItems) {
        this.mDiscoverItems = discoverItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DiscoverItem discoverItem = mDiscoverItems.get(position);
        holder.authorName.setText(discoverItem.getAuthorName());
        holder.timePosted.setText(discoverItem.getTimePosted());
        holder.discoverArticleTitle.setText(discoverItem.getDiscoverArticleTitle());
        holder.discoverArticleDescription.setText(discoverItem.getDiscoverArticleDescription());
        holder.discoverImage.setImageResource(discoverItem.getDiscoverImage());
        holder.readMoreButton.setOnClickListener(v -> {
            // Navigate to the DiscoverDetailsFragment
            Navigation.findNavController(holder.readMoreButton)
                    .navigate(R.id.action_discoverFragment_to_discoverArticleFragment);
        });

        holder.moreOptionsButton.setOnClickListener(v -> {
            // Create a PopupMenu
            PopupMenu popupMenu = new PopupMenu(holder.moreOptionsButton.getContext(), holder.moreOptionsButton);
            // Inflate the menu resource
            popupMenu.getMenuInflater().inflate(R.menu.discover_card_menu, popupMenu.getMenu());

            // Handle menu item clicks
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                // Handle Delete action
                if (itemId == R.id.edit) {
                    // Handle Edit action
                    return true;
                } else return itemId == R.id.delete;
            });
            // Show the popup menu
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return mDiscoverItems.size();
    }
}
