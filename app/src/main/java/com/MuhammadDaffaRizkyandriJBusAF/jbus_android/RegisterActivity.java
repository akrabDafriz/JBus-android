package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regisiter);
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v->{
            moveActivity(this, LoginActivity.class);
        });
        try{
            getSupportActionBar().hide();
        }catch(NullPointerException n){
            n.printStackTrace();
        }
    }
    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    private void Toast(Context ctx, String message){
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
}