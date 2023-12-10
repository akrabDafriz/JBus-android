package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Bus;

public class ManageBusAdapter extends ArrayAdapter<Bus> {

    // invoke the suitable constructor of the ArrayAdapter class
    public ManageBusAdapter(@NonNull Context context, List<Bus> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.manage_bus_view, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Bus currentBus = getItem(position);

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView busText = currentItemView.findViewById(R.id.bus_name_busMainView);
        Button calendar = currentItemView.findViewById(R.id.calendar_button);
        TextView departure_actual_text = currentItemView.findViewById(R.id.departure_actual_text_main);
        TextView arrival_actual_text = currentItemView.findViewById(R.id.arrival_actual_text_main);
        assert currentBus != null;
        busText.setText(currentBus.name);
        departure_actual_text.setText(currentBus.departure.stationName.toString());
        arrival_actual_text.setText(currentBus.arrival.stationName.toString());
        calendar.setOnClickListener(v -> {
            // Handle the calendar button click for the specific item at the given position
            Bus clickedBus = getItem(position);
            if (clickedBus != null) {
                LoginActivity.currentBus = clickedBus;
                Context context = getContext();
                Intent intent = new Intent(context, BusScheduleActivity.class);
                intent.putExtra("busId", clickedBus.id); // Pass necessary data
                context.startActivity(intent);
            }
        });
        return currentItemView;
    }
}