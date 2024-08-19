package com.example.mealcompass.Register.SelectAllergy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mealcompass.R;

import java.util.ArrayList;

public class SelectAllergyAdapter extends ArrayAdapter<SelectAllergyItem> {

    public SelectAllergyAdapter(@NonNull Context context, ArrayList<SelectAllergyItem> allergyItemArrayList){
        super(context, 0, allergyItemArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.allergy_list_item, parent, false);
        }

        SelectAllergyItem currentItem = getItem(position);

        TextView allergyName = convertView.findViewById(R.id.allergyType);
        allergyName.setText(currentItem.getAllergyName());

        ImageView allergyImage = convertView.findViewById(R.id.allergyImage);
        allergyImage.setImageResource(currentItem.getAllergyImage());

        CheckBox checkBox = convertView.findViewById(R.id.checkbox);
        checkBox.setChecked(currentItem.isChecked());

        // handle check box click
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> currentItem.setChecked(isChecked));

        return convertView;
    }
}
