package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.BaseResponse;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Bus;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Schedule;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.BaseApiService;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.UtilsApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusScheduleActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    public EditText date;
    public EditText time;
    private List<Schedule> scheduleList;
    private ListView scheduleListView;
    private Button addScheduleButton;
    private Bus selectedBus;
    private Button backButton;
    private Button refreshButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_schedule);

        mContext = this;
        mApiService = UtilsApi.getApiService();

        date = findViewById(R.id.dateEdit);
        time = findViewById(R.id.timeEdit);
        scheduleListView = findViewById(R.id.schedule_list);
        addScheduleButton = findViewById(R.id.add_schedule_button);
        time.setInputType(InputType.TYPE_NULL);
        date.setInputType(InputType.TYPE_NULL);
        selectedBus = LoginActivity.currentBus;

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v->{
            moveActivity(mContext, ManageBusActivity.class);
        });

        date.setOnClickListener(v-> showDateDialog(date));
        time.setOnClickListener(v->showTimeDialog(time));
        getAllMySchedule();
        addScheduleButton.setOnClickListener(v->{
            handleAddSchedule();
        });
        refreshButton = findViewById(R.id.refreshButton_schedule);
        refreshButton.setOnClickListener(v->{
            recreate();
            overridePendingTransition(0, 0);
            overridePendingTransition(0, 0);
        });
    }

    private void showDateDialog(final EditText date_in) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                date_in.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new DatePickerDialog(mContext,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimeDialog(final EditText time_in) {
        final Calendar calendar=Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener= (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
            calendar.set(Calendar.MINUTE,minute);
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:00");
            time_in.setText(simpleDateFormat.format(calendar.getTime()));
        };
        new TimePickerDialog(mContext,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
    }
    protected void handleAddSchedule() {
        // handling empty field
        String dateS = date.getText().toString();
        String timeS = time.getText().toString();
        String dateTimeS = dateS + " " + timeS;

        if (dateS.isEmpty() || timeS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.addSchedule(LoginActivity.currentBus.id, dateTimeS).enqueue(new Callback<BaseResponse<Bus>>() {
            @Override
            public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Bus> res = response.body();
                if (res.success) {
                    finish();
                    overridePendingTransition(0, 0);
                    selectedBus = res.payload;
                    getAllMySchedule();
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                // if success refresh this activity
                viewToast(mContext, res.message);
            }
            @Override
            public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void viewToast(Context ctx, String message){
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
    protected void getAllMySchedule(){
        if(!selectedBus.schedules.isEmpty()) {
            scheduleListView.setVisibility(View.VISIBLE);
            ArrayList<Schedule> setList = new ArrayList<>(selectedBus.schedules);
            ScheduleArrayAdapter pageList = new ScheduleArrayAdapter(mContext, setList);
            scheduleListView.setAdapter(pageList);
        }
    }
    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
}