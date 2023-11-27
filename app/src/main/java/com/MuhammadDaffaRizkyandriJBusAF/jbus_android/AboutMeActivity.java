package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView registerRenter, renterNotRegistered, renterRegistered;
    private ImageView textBG1, textBG2;
    private Button topUpButton = null;
    private Button manageBus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        mContext = this;
        mApiService = UtilsApi.getApiService();

        topUpButton = findViewById(R.id.topUpButton);
        topUpAmount = findViewById(R.id.topUpAmount);
        registerRenter = findViewById(R.id.register_renter);
        renterRegistered = findViewById(R.id.renter_status);
        renterNotRegistered = findViewById(R.id.renter_not_registered);
        textBG1 = findViewById(R.id.textBG1);
        textBG2 = findViewById(R.id.textBG2);
        manageBus = findViewById(R.id.manage_Bus);

        TextView initial = findViewById(R.id.initial);
        initial.setText(""+LoginActivity.loggedAccount.name.charAt(0));

        TextView accountUsername = findViewById(R.id.acc_username);
        accountUsername.setText(LoginActivity.loggedAccount.name);

        TextView accountEmail = findViewById(R.id.acc_email);
        accountEmail.setText(LoginActivity.loggedAccount.email);

        TextView accountBalance = findViewById(R.id.acc_balance);
        accountBalance.setText(String.valueOf(LoginActivity.loggedAccount.balance));

        if(LoginActivity.loggedAccount.company==null){
            textBG1.setVisibility(View.GONE);
            renterRegistered.setVisibility(View.GONE);
            manageBus.setVisibility(View.GONE);
        }
        else{
            textBG2.setVisibility(View.GONE);
            registerRenter.setVisibility(View.GONE);
            renterNotRegistered.setVisibility(View.GONE);
        }

        topUpButton.setOnClickListener(v->{
            handleTopUp();
        });
        registerRenter.setOnClickListener(v->{
            moveActivity(mContext, RegisterRenterActivity.class);
        });
        manageBus.setOnClickListener(v->{
            moveActivity(mContext, ManageBusActivity.class);
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
            Toast.makeText(mContext, "Fill the amount with numbers", Toast.LENGTH_SHORT).show();
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

