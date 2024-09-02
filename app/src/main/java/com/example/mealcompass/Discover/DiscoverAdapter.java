package com.example.mealcompass.Discover;

import android.app.AlertDialog;
import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealcompass.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.ViewHolder> {

    private final List<DiscoverItem> mDiscoverItems;
    private final boolean isAdmin;

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

    public DiscoverAdapter(List<DiscoverItem> discoverItems, boolean isAdmin) {
        this.mDiscoverItems = discoverItems;
        this.isAdmin = isAdmin;
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
        Glide.with(holder.discoverImage.getContext())
                .load(discoverItem.getDiscoverImage())
                .into(holder.discoverImage);
        holder.readMoreButton.setOnClickListener(v -> {
            // pass article data to DiscoverArticleFragment
            Bundle bundle = new Bundle();
            bundle.putString("discoverId", discoverItem.getDiscoverId());
            bundle.putString("authorName", discoverItem.getAuthorName());
            bundle.putString("timePosted", discoverItem.getTimePosted());
            bundle.putString("discoverArticleTitle", discoverItem.getDiscoverArticleTitle());
            bundle.putString("discoverArticleDescription", discoverItem.getDiscoverArticleDescription());
            bundle.putString("discoverImage", discoverItem.getDiscoverImage());
            Navigation.findNavController(holder.readMoreButton)
                    .navigate(R.id.action_discoverFragment_to_discoverArticleFragment, bundle);
        });

        // if user type is admin, show more options button
        holder.moreOptionsButton.setVisibility(isAdmin ? View.VISIBLE : View.GONE);

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
                    Bundle bundle = new Bundle();
                    bundle.putString("articleId", discoverItem.getDiscoverId());

                    // navigate to EditDiscoverArticleFragment
                    Navigation.findNavController(v)
                            .navigate(R.id.action_discoverFragment_to_addDiscoverArticleFragment, bundle);
                    return true;
                } else if (itemId == R.id.delete) {
                    // Show an alert dialog to confirm deletion
                    AlertDialog alertDialog = new AlertDialog.Builder(holder.moreOptionsButton.getContext())
                            .setTitle("Delete Article")
                            .setMessage("Are you sure you want to delete this article?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                // Delete the article
                                DiscoverRepository discoverRepository = new DiscoverRepository();
                                discoverRepository.deleteDiscover(discoverItem.getDiscoverId());
                                Toast.makeText(holder.moreOptionsButton.getContext(), "Article deleted", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                            .create();
                    alertDialog.show();
                    return true;
                }
                return false;
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
