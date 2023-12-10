package com.MuhammadDaffaRizkyandriJBusAF.jbus_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.model.Bus;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.BaseApiService;
import com.MuhammadDaffaRizkyandriJBusAF.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private BusArrayAdapter busAdapter;
    private ListView busListV;
    private Button[] btns;
    private int currentPage = 0;
    private int pageSize = 10;
    private int listSize;
    private int noOfPages;
    private List<Bus> listBus = new ArrayList<>();
    private Button prevButton = null;
    private Button nextButton = null;
    private HorizontalScrollView pageScroll = null;
    private Button refreshButton;
    private Button backButton;
    @Override
      protected void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mApiService = UtilsApi.getApiService();

        prevButton = findViewById(R.id.prev_page);
        nextButton = findViewById(R.id.next_page);
        pageScroll = findViewById(R.id.page_number_scroll);
        refreshButton = findViewById(R.id.refreshButton_schedule);

        busListV = findViewById(R.id.busListView);
        handleBusList();

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v->{
            moveActivity(mContext, LoginActivity.class);
        });
        busListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> busListView, View view, int i, long l) {
                LoginActivity.currentBus = (Bus) busListView.getItemAtPosition(i);
                if(LoginActivity.currentBus != null) {
                    moveActivity(mContext, BusDetailActivity.class);
                }
            }
        });
        // construct the footer
        paginationFooter();
        goToPage(currentPage);

        // listener untuk button prev dan button
        prevButton.setOnClickListener(v -> {
            currentPage = currentPage != 0? currentPage-1 : 0;
            goToPage(currentPage);
        });
        nextButton.setOnClickListener(v -> {
            currentPage = currentPage != noOfPages -1? currentPage+1 : currentPage;
            goToPage(currentPage);
        });}
        catch(Exception e){
            e.printStackTrace();
        }
        refreshButton.setOnClickListener(v->{
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try{
            if(item.getItemId() == R.id.account_button){
                moveActivity(mContext, AboutMeActivity.class);
                return true;
            }
            if(item.getItemId() == R.id.payment_button){
                moveActivity(mContext, PaymentActivity.class);
                return true;
            }
        }
        catch(Exception e){
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }
    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    private void viewToast(Context ctx, String message){
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
    protected void handleBusList() {
        mApiService.getAllBus().enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Cannot get the Bus information" +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listBus = response.body();
                listSize = listBus.size();
                ArrayList<Bus> set = new ArrayList<>(listBus);
                busAdapter = new BusArrayAdapter(mContext, set);
                busListV.setAdapter(busAdapter);
                // if success finish this activity (refresh page)
            }
            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void paginationFooter() {
        int val = listSize % pageSize;
        val = val == 0 ? 0:1;
        noOfPages = listSize / pageSize + val;
        LinearLayout ll = findViewById(R.id.btn_layout);
        btns = new Button[noOfPages];
        if (noOfPages <= 6) {
            ((FrameLayout.LayoutParams) ll.getLayoutParams()).gravity = Gravity.CENTER;
        }
        for (int i = 0; i < noOfPages; i++) {
            btns[i]=new Button(this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText(""+(i+1));
// ganti dengan warna yang kalian mau
            btns[i].setTextColor(getResources().getColor(R.color.black));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100,
                    100);
            ll.addView(btns[i], lp);
            final int j = i;
            btns[j].setOnClickListener(v -> {
                currentPage = j;
                goToPage(j);
            });
        }
    }

    private void goToPage(int index) {
        for (int i = 0; i< noOfPages; i++) {
            if (i == index) {
                btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
                scrollToItem(btns[index]);
                viewPaginatedList(listBus, currentPage);
            } else {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }
    private void scrollToItem(Button item) {
        int scrollX = item.getLeft() - (pageScroll.getWidth() - item.getWidth()) /
                2;
        pageScroll.smoothScrollTo(scrollX, 0);
    }
    private void viewPaginatedList(List<Bus> listBus, int page) {
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, listBus.size());
        List<Bus> paginatedList = listBus.subList(startIndex, endIndex);
        BusArrayAdapter paginatedAdapter = new BusArrayAdapter(this, paginatedList);
        busListV = findViewById(R.id.busListView);
        busListV.setAdapter(paginatedAdapter);
        // Tampilkan paginatedList ke listview
    }

}