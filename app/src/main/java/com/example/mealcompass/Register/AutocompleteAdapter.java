package com.example.mealcompass.Register;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AutocompleteAdapter extends ArrayAdapter<AutocompletePrediction> implements Filterable {

    private List<AutocompletePrediction> resultList;
    private final PlacesClient placesClient;

    public AutocompleteAdapter(Context context, PlacesClient placesClient) {
        super(context, android.R.layout.simple_dropdown_item_1line);
        this.placesClient = placesClient;
        resultList = new ArrayList<>();

    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public AutocompletePrediction getItem(int index) {
        return resultList.get(index);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }

        AutocompletePrediction item = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);

        // Set text for adapter
        textView.setText(item.getFullText(null));

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = getAutocomplete(constraint);
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    private List<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setCountries("MY")
                .setQuery(constraint.toString())
                .build();
        Task<FindAutocompletePredictionsResponse> results = placesClient.findAutocompletePredictions(request);
        try {
            Tasks.await(results);
            return new ArrayList<>(results.getResult().getAutocompletePredictions());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}
