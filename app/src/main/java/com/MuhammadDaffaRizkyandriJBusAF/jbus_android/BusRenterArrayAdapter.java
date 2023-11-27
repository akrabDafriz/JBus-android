package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Bus;

public class BusRenterArrayAdapter extends ArrayAdapter<Bus> {

    // invoke the suitable constructor of the ArrayAdapter class
    public BusRenterArrayAdapter(@NonNull Context context, List<Bus> list) {
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
        TextView busText = currentItemView.findViewById(R.id.bus_text_view);
        ImageView calendar = currentItemView.findViewById(R.id.calendar_button);
        busText.setText(currentBus.name);

        return currentItemView;
    }
}