package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Bus;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Payment;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.BaseApiService;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageOrderActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private List<Payment> listPayment;
    private ListView paymentListView;
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order);
        mContext = this;
        mApiService = UtilsApi.getApiService();

        paymentListView = findViewById(R.id.manage_order_list);
        handleList();

        backButton = findViewById(R.id.backButton_mOrder);
        backButton.setOnClickListener(v->{
            moveActivity(mContext, AboutMeActivity.class);
        });

        paymentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> busListView, View view, int i, long l) {
                moveActivity(mContext, AboutMeActivity.class);
            }
        });
    }
    protected void handleList() {
        // handling empty field
        int buyerId = LoginActivity.loggedAccount.id;
        mApiService.getAllPayment(buyerId).enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listPayment = response.body();
                ArrayList<Payment> set = new ArrayList<>(listPayment);
                PaymentListAdapter paymentListAdapter = new PaymentListAdapter(mContext, set);
                paymentListView.setAdapter(paymentListAdapter);
                // if success finish this activity (refresh page)
            }
            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {
                Toast.makeText(mContext, "Yes, still Problem with the server",
                        Toast.LENGTH_SHORT).show();
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