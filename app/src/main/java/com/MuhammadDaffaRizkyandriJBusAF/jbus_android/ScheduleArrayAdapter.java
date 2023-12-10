package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import static com.MuhammadDaffaRizkyandriJBusAF.jbus_android.LoginActivity.currentBus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Bus;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleArrayAdapter extends ArrayAdapter<Schedule> {
    public ScheduleArrayAdapter(@NonNull Context context, ArrayList<Schedule> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.schedules_view, parent, false);
        }
        // get the position of the view from the ArrayAdapter
        Schedule currentNumberPosition = getItem(position);

        assert currentNumberPosition != null;

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView scheduleText = currentItemView.findViewById(R.id.schedule_view);
        scheduleText.setText(currentNumberPosition.printDate()+" "+currentNumberPosition.printTime());


        // then return the recyclable view
        return currentItemView;
    }
}
