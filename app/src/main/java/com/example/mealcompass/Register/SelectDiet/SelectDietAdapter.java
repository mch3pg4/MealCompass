package com.example.mealcompass.Register.SelectDiet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mealcompass.R;

import java.util.ArrayList;

public class SelectDietAdapter extends ArrayAdapter<SelectDietItem> {

    public SelectDietAdapter(@NonNull Context context, ArrayList<SelectDietItem> dietItemArrayList){

        super(context, 0, dietItemArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.diet_list_item, parent, false);
        }

        SelectDietItem currentItem = getItem(position);

        TextView dietName = convertView.findViewById(R.id.dietType);
        dietName.setText(currentItem.getDietName());

        CheckBox checkBox = convertView.findViewById(R.id.checkbox);
        checkBox.setChecked(currentItem.isChecked());

        // handle check box click
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> currentItem.setChecked(isChecked));

        return convertView;
    }
}
