package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Bus;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.BaseApiService;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.UtilsApi;

import java.util.List;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageBusActivity extends AppCompatActivity {

    private BaseApiService mApiService;
    private Context mContext;
    private ImageButton manageBusAddButton;
    private List<Bus> listBus = new ArrayList<>();
    private ListView busListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus);
        mContext = this;
        mApiService = UtilsApi.getApiService();
        handleList();
        try{
            getSupportActionBar().hide();
        }catch(NullPointerException n){
            n.printStackTrace();
        }

        busListView = findViewById(R.id.manage_Bus_ListView);
        manageBusAddButton = findViewById(R.id.manageBus_addButton);
        manageBusAddButton.setOnClickListener(v->{
            moveActivity(mContext, AddBusActivity.class);
        });
    }
    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    private void viewToast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
    protected void handleList() {
        // handling empty field
        int idS = LoginActivity.loggedAccount.id;

        mApiService.getMyBus(idS).enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listBus = response.body();
                ArrayList<Bus> set = new ArrayList<>(listBus);
                BusRenterArrayAdapter busRenterAdptr = new BusRenterArrayAdapter(mContext, set);
                busListView.setAdapter(busRenterAdptr);
                // if success finish this activity (refresh page)
            }
            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}