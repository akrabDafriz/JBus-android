package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Account;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.BaseResponse;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.BaseApiService;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.UtilsApi;

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton = null;
    private BaseApiService mApiService;
    private Context mContext;
    private EditText name, email, password;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regisiter);

        mContext = this;
        mApiService = UtilsApi.getApiService();
// sesuaikan dengan ID yang kalian buat di layout
        name = findViewById(R.id.register_name);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        registerButton = findViewById(R.id.registerButton);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v->{
            moveActivity(mContext, LoginActivity.class);
        });

        registerButton.setOnClickListener(v->{
            handleRegister();
        });

        try {
            getSupportActionBar().hide();
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }

    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    private void viewToast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }

    protected void handleRegister() {
        // handling empty field
        String nameS = name.getText().toString();
        String emailS = email.getText().toString();
        String passwordS = password.getText().toString();
        if (nameS.isEmpty() || emailS.isEmpty() || passwordS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.register(nameS, emailS, passwordS).enqueue(new Callback<BaseResponse<Account>>() {
            @Override
            public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Account> res = response.body();
// if success finish this activity (back to login activity)
                if (res.success){
                    finish();
                    name.setText("");
                    email.setText("");
                    password.setText("");
                }
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
