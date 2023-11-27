package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.BaseResponse;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Renter;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.BaseApiService;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterRenterActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private EditText companyName, address, phoneNumber;
    private Button registerRenterButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_renter);
        mContext = this;
        mApiService = UtilsApi.getApiService();

        companyName = findViewById(R.id.companyName);
        address = findViewById(R.id.address);
        phoneNumber = findViewById(R.id.phoneNumber);
        registerRenterButton = findViewById(R.id.registerRenterButton);

        registerRenterButton.setOnClickListener(v->{
            handleRegisterRenter();
        });
    }
    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    private void viewToast(Context ctx, String message){
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
    protected void handleRegisterRenter() {
        // handling empty field
        String companyNameS = companyName.toString();
        String addressS = address.toString();
        String phoneNumberS = phoneNumber.toString();

        if (companyNameS.isEmpty() | addressS.isEmpty() | phoneNumberS.isEmpty()) {
            viewToast(mContext, "Field cannot be empty");
            return;
        }
        mApiService.registerRenter(LoginActivity.loggedAccount.id,companyNameS, addressS, phoneNumberS).enqueue(new Callback<BaseResponse<Renter>>() {
            @Override
            public void onResponse(Call<BaseResponse<Renter>> call, Response<BaseResponse<Renter>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    viewToast(mContext, "Application error " + response.code());
                    return;
                }
                BaseResponse<Renter> res = response.body();
                if(res.success) {
                    LoginActivity.loggedAccount.company = res.payload;
                    finish();
                }
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<BaseResponse<Renter>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}