package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import static com.MuhammadDaffaRizkyandriJBusAF.jbus_android.BusDetailActivity.selectedSchedule;
import static com.MuhammadDaffaRizkyandriJBusAF.jbus_android.LoginActivity.currentBus;
import static com.MuhammadDaffaRizkyandriJBusAF.jbus_android.LoginActivity.loggedAccount;
import static com.MuhammadDaffaRizkyandriJBusAF.jbus_android.LoginActivity.paymentList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Account;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.BaseResponse;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Bus;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Payment;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.BaseApiService;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeBookingActivity extends AppCompatActivity {
    private GridLayout gridLayout;
    int i;
    private Button backButton;
    private BaseApiService mApiService;
    private List<String> orderedSeat = new ArrayList<>();
    private Context mContext;
    private TextView selectedScheduleText;
    private TextView busCapacity;
    private TextView totalCost;
    private Button makeBookingButton;
    int amount = 0;
    int totalAmount = 0;
    int current_capacity = currentBus.capacity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_booking);

        mContext = this;
        mApiService = UtilsApi.getApiService();
        selectedScheduleText = findViewById(R.id.schedule_nya_makeBook);
        busCapacity = findViewById(R.id.capacity_makeBook);
        totalCost = findViewById(R.id.total_cost);
        makeBookingButton = findViewById(R.id.makeBookingBtn);
        gridLayout = findViewById(R.id.gridLayout);
        gridLayout.removeAllViews();

        selectedScheduleText.setText(selectedSchedule.toString());
        busCapacity.setText(String.valueOf(currentBus.capacity));
        totalAmount = (int) (currentBus.price.price * amount);
        totalCost.setText(String.valueOf(totalAmount));

        int row = gridLayout.getRowCount();
        int column = gridLayout.getColumnCount();
        System.out.println(currentBus);

        for (i = 0; i < currentBus.capacity; i++) {
            final int seatIndex = i;  // Create a final variable to capture the current value of i
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText("Seat " + (i+1));
            String check = ("AF" +String.format("%02d", i+1));
            if (Boolean.FALSE.equals(selectedSchedule.seatAvailability.get(check))){
                checkBox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                checkBox.setEnabled(false);
                current_capacity = current_capacity - 1;
                busCapacity.setText(String.valueOf(current_capacity));
            }

            // Adding the checkbox to the existing GridLayout
            gridLayout.addView(checkBox);

            // Setting an OnCheckedChangeListener for each checkbox
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Your logic when the checkbox is checked or unchecked
                    if (isChecked) {
                        orderedSeat.add("AF" + String.format("%02d", seatIndex+1));
                        System.out.println(orderedSeat);
                        current_capacity = current_capacity-1;
                        amount = amount + 1;
                        totalAmount = amount * (int) currentBus.price.price;
                        //viewToast(mContext,"Seat " + "AF" + String.format("%02d", seatIndex) + " selected");
                        busCapacity.setText(String.valueOf(current_capacity));
                        totalCost.setText(String.valueOf(totalAmount));
                    } else {
                        orderedSeat.remove("AF" + String.format("%02d", seatIndex+1));
                        System.out.println(orderedSeat);
                        amount = amount - 1;
                        current_capacity = current_capacity+1;
                        totalAmount = amount * (int) currentBus.price.price;
                        //viewToast(mContext,"Seat " + "AF" + String.format("%02d", seatIndex) + " unselected");
                        busCapacity.setText(String.valueOf(current_capacity));
                        totalCost.setText(String.valueOf(totalAmount));
                    }
                }
            });
        }

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v->{
            moveActivity(mContext, BusDetailActivity.class);
        });
        makeBookingButton.setOnClickListener(v->{
            handleMakeBooking();
            moveActivity(mContext, PaymentActivity.class);
        });
    }
    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    private void viewToast(Context ctx, String message){
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
    private void handleMakeBooking(){
        int buyerId = loggedAccount.id;
        int renterId = 0;
        int busId = currentBus.id;
        List<String> busSeats = orderedSeat;
        String departureDate = selectedSchedule.toString();

        mApiService.makeBooking(buyerId, renterId, busId, busSeats, departureDate).enqueue(new Callback<BaseResponse<Payment>>() {
            @Override
            public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Cannot get the Bus information" +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Payment> res = response.body();
                LoginActivity.paymentList.add(res.payload);
                finish();
                overridePendingTransition(0, 0);
                moveActivity(mContext, PaymentActivity.class);
                overridePendingTransition(0, 0);
                viewToast(mContext, res.message);
            }
            @Override
            public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}