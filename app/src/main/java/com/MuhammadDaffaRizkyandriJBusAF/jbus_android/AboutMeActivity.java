package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Account;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.BaseResponse;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.BaseApiService;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutMeActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private EditText topUpAmount;
    private Button topUpButton = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        mContext = this;
        mApiService = UtilsApi.getApiService();

        topUpButton = findViewById(R.id.topUpButton);
        topUpAmount = findViewById(R.id.topUpAmount);

        TextView accountUsername = findViewById(R.id.acc_username);
        accountUsername.setText(LoginActivity.loggedAccount.name);

        TextView accountEmail = findViewById(R.id.acc_email);
        accountEmail.setText(LoginActivity.loggedAccount.email);

        TextView accountBalance = findViewById(R.id.acc_balance);
        accountBalance.setText(String.valueOf(LoginActivity.loggedAccount.balance));

        topUpButton.setOnClickListener(v->{
            handleTopUp();
        });
    }

    protected void handleTopUp() {
        // handling empty field
        String topUpAmountS = topUpAmount.getText().toString();

        if (topUpAmountS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!topUpAmountS.matches("\\d+")){
            Toast.makeText(mContext, "Isi nominal dengan angka", Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.topUp(LoginActivity.loggedAccount.id,Double.valueOf(topUpAmountS)).enqueue(new Callback<BaseResponse<Double>>() {
            @Override
            public void onResponse(Call<BaseResponse<Double>> call, Response<BaseResponse<Double>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Double> res = response.body();
                if(res.success) {
                    finish();
                    overridePendingTransition(0, 0);

                    LoginActivity.loggedAccount.balance += res.payload.doubleValue();

                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<BaseResponse<Double>> call, Throwable t) {
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

