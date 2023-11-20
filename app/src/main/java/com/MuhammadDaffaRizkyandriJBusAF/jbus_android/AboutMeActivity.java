package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        TextView accountUsername = findViewById(R.id.acc_username);
        accountUsername.setText("Muhammad Daffa Rizkyandri");

        TextView accountEmail = findViewById(R.id.acc_email);
        accountEmail.setText("dafrizz@gmail.com");

        TextView accountBalance = findViewById(R.id.acc_balance);
        accountBalance.setText("150000");
    }
}