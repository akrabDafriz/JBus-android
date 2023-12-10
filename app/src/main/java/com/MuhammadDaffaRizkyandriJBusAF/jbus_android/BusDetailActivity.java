package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import static com.MuhammadDaffaRizkyandriJBusAF.jbus_android.LoginActivity.currentBus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Schedule;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.BaseApiService;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.UtilsApi;

import org.w3c.dom.Text;

import java.util.Collections;

public class BusDetailActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private TextView busName;
    private TextView facilities;
    private TextView price;
    private TextView capacity;
    private TextView busType;
    private TextView departure;
    private TextView arrival;
    private Spinner schedulesSpinner;
    public static Schedule selectedSchedule;
    private Button makeBookButton;
    private Button backButton;
    private TextView noSchedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);
        mContext = this;
        mApiService = UtilsApi.getApiService();

        busName = findViewById(R.id.bus_name_detail);
        facilities = findViewById(R.id.facilities_detail);
        price = findViewById(R.id.price_detail);
        capacity = findViewById(R.id.capacity_detail);
        busType = findViewById(R.id.bus_type_detail);
        departure = findViewById(R.id.departure_station_detail);
        arrival = findViewById(R.id.arrival_station_detail);
        schedulesSpinner = findViewById(R.id.schedule_spinner_detail);
        makeBookButton = findViewById(R.id.make_booking_button);
        backButton = findViewById(R.id.back_button);
        noSchedules = findViewById(R.id.scheduleIsEmpty);
        handleScheduleSpinner();

        busName.setText("Bus: "+currentBus.name);
        facilities.setText("Fasilitas: "+currentBus.facilities.toString());
        price.setText("Harga Tiket: "+ String.valueOf(currentBus.price.price));
        capacity.setText("Kapasitas Bus: "+String.valueOf(currentBus.capacity));
        busType.setText("Tipe Bus: " +currentBus.busType.toString());
        departure.setText("Stasiun Berangkat: "+currentBus.departure.stationName);
        arrival.setText("Stasiun Tujuan: "+currentBus.arrival.stationName);
        schedulesSpinner.setOnItemSelectedListener(setSchedule());
        if(currentBus.schedules==null){
            schedulesSpinner.setVisibility(View.GONE);
            makeBookButton.setVisibility(View.GONE);
        }
        else noSchedules.setVisibility(View.GONE);

        makeBookButton.setOnClickListener(v->{
            moveActivity(mContext, MakeBookingActivity.class);
        });
        backButton.setOnClickListener(v->{
            moveActivity(mContext, MainActivity.class);
        });
    }
    protected AdapterView.OnItemSelectedListener setSchedule(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSchedule = (Schedule) adapterView.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        };
    }
    private void handleScheduleSpinner(){
        ArrayAdapter schedBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1,
                currentBus.schedules);
        schedBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        schedulesSpinner.setAdapter(schedBus);
    }

    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    private void viewToast(Context ctx, String message){
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }

}