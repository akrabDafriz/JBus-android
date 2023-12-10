package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Bus;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Payment;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.BaseApiService;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.UtilsApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentListAdapter extends ArrayAdapter<Payment> {
    private BaseApiService mApiService;
    private Bus currentBus;
    private String busSeats;
    public PaymentListAdapter(@NonNull Context context, List<Payment> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;
        mApiService = UtilsApi.getApiService();
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.booking_status_list_view, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Payment currentNumberPosition = getItem(position);

        assert currentNumberPosition != null;
        TextView dept = currentItemView.findViewById(R.id.dept_mOrder_view);
        TextView arr = currentItemView.findViewById(R.id.arr_mOrder_view);

        TextView busName = currentItemView.findViewById(R.id.bus_name_mOrder);
        mApiService.getBusById(currentNumberPosition.getBusId()).enqueue(new Callback<Bus>() {
                 @Override
                 public void onResponse(Call<Bus> call, Response<Bus> response) {
                     if (!response.isSuccessful()) {
                         return;
                     }
                     currentBus = response.body();
                     busName.setText(currentBus.name);
                     dept.setText(currentBus.departure.stationName);
                     arr.setText(currentBus.arrival.stationName);
                 }
                 @Override
                 public void onFailure(Call<Bus> call, Throwable t) {
                 }
             }
        );

        TextView busSeat = currentItemView.findViewById(R.id.bus_seat_text_mOrder);
        StringBuilder stringBuilder = new StringBuilder();
        for (String element : currentNumberPosition.busSeats) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(element);
        }
        busSeat.setText(stringBuilder.toString());

        TextView departureDate = currentItemView.findViewById(R.id.departure_date_mOrder);
        departureDate.setText(currentNumberPosition.getDepartureTime());

        TextView bookingStatus = currentItemView.findViewById(R.id.mOrder_status_view);
        bookingStatus.setText(currentNumberPosition.status.toString());

        return currentItemView;
    }
}
