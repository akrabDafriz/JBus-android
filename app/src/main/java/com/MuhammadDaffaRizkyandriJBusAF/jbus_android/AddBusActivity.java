package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Account;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.BaseResponse;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Bus;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.BusType;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Facility;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Station;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.BaseApiService;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class AddBusActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private BusType[] busType = BusType.values();
    private BusType selectedBusType;
    private Spinner busTypeSpinner;
    private Spinner departureSpinner;
    private Spinner arrivalSpinner;
    private List<Station> stationList = new ArrayList<>();
    private int selectedDeptStationID;
    private int selectedArrStationID;
    private CheckBox acCheckBox, wifiCheckBox, toiletCheckBox, lcdCheckBox;
    private CheckBox coolboxCheckBox, lunchCheckBox, baggageCheckBox, electricCheckBox;
    private EditText busName, busCapacity, busPrice;
    private List<Facility> selectedFacilities = new ArrayList<>();
    private Button addBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);
        try{
            getSupportActionBar().hide();
        }catch(NullPointerException n){
            n.printStackTrace();
        }
        mContext = this;
        mApiService = UtilsApi.getApiService();

        busName = findViewById(R.id.bus_name);
        busCapacity = findViewById(R.id.bus_capacity);
        busPrice = findViewById(R.id.bus_price);
        addBus = findViewById(R.id.busAddButton);


        busTypeSpinner = this.findViewById(R.id.spinner);
        departureSpinner = findViewById(R.id.spinner2);
        arrivalSpinner = findViewById(R.id.spinner3);
        acCheckBox = findViewById(R.id.ac_checkbox);
        wifiCheckBox = findViewById(R.id.wifi_checkbox);
        toiletCheckBox = findViewById(R.id.toilet_checkbox);
        lcdCheckBox = findViewById(R.id.lcd_tv_checkbox);
        coolboxCheckBox = findViewById(R.id.coolbox_checkbox);
        lunchCheckBox = findViewById(R.id.lunch_checkbox);
        baggageCheckBox = findViewById(R.id.large_baggage_checkbox);
        electricCheckBox = findViewById(R.id.electric_socket_checkbox);


        ArrayAdapter adBus = new ArrayAdapter(this, android.R.layout.simple_list_item_1, busType);
        adBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        busTypeSpinner.setAdapter(adBus);
        //menambahkan OISL (OnItemSelectedListener) untuk spinner
        busTypeSpinner.setOnItemSelectedListener(busTypeOISL);
        handleDepartureArrival();
        addBus.setOnClickListener(v->{
            handleCheckBox();
            handleCreate();
        });
    }

    AdapterView.OnItemSelectedListener busTypeOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            // mengisi field selectedBusType sesuai dengan item yang dipilih
            selectedBusType = busType[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    protected void handleDepartureArrival() {
        mApiService.getAllStation().enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                stationList = response.body(); //simpan response body ke listStation
                ArrayList<String> stationNameList = new ArrayList<>();
                for(Station station : stationList){
                    stationNameList.add(station.stationName);
                }

                AdapterView.OnItemSelectedListener deptOISL = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                        // mengisi field selectedBusType sesuai dengan item yang dipilih
                        selectedDeptStationID = stationList.get(position).id;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                ArrayAdapter deptBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, stationNameList);
                deptBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                departureSpinner.setAdapter(deptBus);
                departureSpinner.setOnItemSelectedListener(deptOISL);

                AdapterView.OnItemSelectedListener arrOISL = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                        // mengisi field selectedBusType sesuai dengan item yang dipilih
                        selectedArrStationID = stationList.get(position).id;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };

                ArrayAdapter arrBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, stationNameList);
                arrBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                arrivalSpinner.setAdapter(arrBus);
                arrivalSpinner.setOnItemSelectedListener(arrOISL);
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                viewToast(mContext, "Problem with server");
            }
        });
    }
    protected void handleCheckBox(){
        selectedFacilities.clear(); // Clear the list before updating
        if (acCheckBox.isChecked()) { selectedFacilities.add(Facility.AC);}
        if (wifiCheckBox.isChecked()) { selectedFacilities.add(Facility.WIFI);}
        if (toiletCheckBox.isChecked()){ selectedFacilities.add(Facility.TOILET);}
        if (lcdCheckBox.isChecked()) { selectedFacilities.add(Facility.LCD_TV);}
        if (coolboxCheckBox.isChecked()) { selectedFacilities.add(Facility.COOL_BOX);}
        if (lunchCheckBox.isChecked()) { selectedFacilities.add(Facility.LUNCH);}
        if (baggageCheckBox.isChecked()){ selectedFacilities.add(Facility.LARGE_BAGGAGE);}
        if (electricCheckBox.isChecked()){ selectedFacilities.add(Facility.ELECTRIC_SOCKET);}
    }
    protected void handleCreate() {
        // handling empty field

        int accountId = LoginActivity.loggedAccount.id;
        String name = busName.getText().toString();
        int capacity = Integer.valueOf(busCapacity.getText().toString());
        List<Facility> facilities = selectedFacilities;
        BusType busType = selectedBusType;
        int price = Integer.valueOf(busPrice.getText().toString());
        int stationDepartureId = selectedDeptStationID;
        int stationArrivalId = selectedArrStationID;

        if (name.isEmpty() || busCapacity.getText().toString().isEmpty() || busPrice.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        mApiService.create(accountId, name, capacity, facilities, busType,
                price, stationDepartureId, stationArrivalId).enqueue(new Callback<BaseResponse<Bus>>() {
            @Override
            public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Bus> res = response.body();
                // if success finish this activity (back to login activity)
                if (res.success) {
                    moveActivity(mContext, ManageBusActivity.class);
                    overridePendingTransition(0,0);
                    moveActivity(getApplicationContext(), ManageBusActivity.class);
                    overridePendingTransition(0,0);
                }
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    private void viewToast(Context ctx, String message){
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
}