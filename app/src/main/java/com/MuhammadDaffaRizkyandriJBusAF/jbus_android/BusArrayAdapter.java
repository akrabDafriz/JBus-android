package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Bus;

public class BusArrayAdapter extends ArrayAdapter<Bus> {

    // invoke the suitable constructor of the ArrayAdapter class
    public BusArrayAdapter(@NonNull Context context, List<Bus> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.bus_main_view, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Bus currentBus = getItem(position);

        assert currentBus != null;

        try {
            // then according to the position of the view assign the desired TextView 1 for the same
            TextView busText = currentItemView.findViewById(R.id.bus_name_busMainView);
            busText.setText(currentBus.name);

            TextView departure = currentItemView.findViewById(R.id.departure_actual_text_main);
            departure.setText(currentBus.departure.stationName.toString());

            TextView arrival = currentItemView.findViewById(R.id.arrival_actual_text_main);
            arrival.setText(currentBus.arrival.stationName.toString());
        }
        catch (NullPointerException n){
            return null;
        }
        return currentItemView;
    }
}