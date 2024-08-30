package com.example.mealcompass.Register;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;

public class PlacesAutoCompleteAdapter extends ArrayAdapter<AutocompletePrediction> implements Filterable {

    private final PlacesClient placesClient;

    public PlacesAutoCompleteAdapter(@NonNull Context context, @NonNull PlacesClient placesClient) {
        super(context, android.R.layout.simple_dropdown_item_1line);
        this.placesClient = placesClient;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }

        AutocompletePrediction prediction = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(prediction != null ? prediction.getPrimaryText(null).toString() : "");

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    List<AutocompletePrediction> predictions = getPredictions(constraint.toString());
                    results.values = predictions;
                    results.count = predictions.size();
                } else {
                    results.values = new ArrayList<>();
                    results.count = 0;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                @SuppressWarnings("unchecked")
                List<AutocompletePrediction> predictions = (List<AutocompletePrediction>) results.values;
                clear();
                if (predictions != null) {
                    addAll(predictions);
                }
                notifyDataSetChanged();
            }

            private List<AutocompletePrediction> getPredictions(String query) {
                List<AutocompletePrediction> predictions = new ArrayList<>();
                // Implement prediction fetching logic from Places API
                // Create a request for autocomplete predictions
                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                        .setQuery(query)
                        .build();
                placesClient.findAutocompletePredictions(request)
                        .addOnSuccessListener(response -> {
                            predictions.addAll(response.getAutocompletePredictions());
                            notifyDataSetChanged();
                        })
                        .addOnFailureListener(exception -> {
                            Log.e("PlacesAutoCompleteAdapter", "Failed to fetch predictions", exception);
                        });
                return predictions;
            }
        };
    }
}


