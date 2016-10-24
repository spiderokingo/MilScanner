package com.infantry.milscanner.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.infantry.milscanner.Config.ApiService;
import com.infantry.milscanner.Models.BaseModel;
import com.infantry.milscanner.Models.UsersModel;
import com.infantry.milscanner.R;
import com.infantry.milscanner.Utils.Enum;
import com.infantry.milscanner.Utils.GPSTracker;
import com.infantry.milscanner.Utils.ModelCaches;
import com.infantry.milscanner.Utils.MyCallback;
import com.infantry.milscanner.Utils.Singleton;
import com.infantry.milscanner.ViewHolder.MainFragmentAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.RetrofitError;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class TrackingActivity extends AppCompatActivity {


    @Bind(R.id.my_toolbar)
    Toolbar myToolbar;
    @Bind(R.id.listView)
    ListView listView;

    Handler worker;
    Runnable task;
    GPSTracker gpsTracker;
    Calendar now;
    ArrayList<String> list;
    MainFragmentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        ButterKnife.bind(this);
        initialize();
        timerSetup();
        setToolbar("Tracking");

    }

    @Override
    protected void onResume() {
        super.onResume();
        isGPSTurnOn();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        worker.removeCallbacks(task);
    }

    /**
     * >>>>>>>>>>>>>>>> LOGIN LOGIC <<<<<<<<<<<<<<<<
     */

    private void initialize(){
        gpsTracker = new GPSTracker(this);
        now = Calendar.getInstance();

        list = new ArrayList<String>();

        adapter = new MainFragmentAdapter(TrackingActivity.this,
                android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }

    private void refreshList(){

    }

    /**
     * Check GPS Status
     */
    private void isGPSTurnOn() {
        if (gpsTracker.canGetLocation()) {
            worker.post(task);
        } else {
            gpsTracker.showSettingsAlert();
        }
    }
    /**
     * Set Polling function
     */
    private void timerSetup() {
        // Retrieve a PendingIntent that will perform a broadcast
        worker = new Handler();
        task = new Runnable() {
            public void run() {
                postTrackingApi();
//                Singleton.toast(TrackingActivity.this, "Running", Toast.LENGTH_SHORT);
            }
        };
    }

    /**
     * Call Login api
     */
    private void postTrackingApi() {
        ApiService.getApiEndpointInterface().trackingGPS(Enum.MODE_INSERT.getStringValue(),
                ModelCaches.getInstance().getUsersDetails().PersonalID,
                new SimpleDateFormat("dd-MM-yyyy, hh:mm:ss", Locale.getDefault()).format(new Date()),
                gpsTracker.getLatitude()+","+gpsTracker.getLongitude(),
                new MyCallback<BaseModel>() {
                    @Override
                    public void good(BaseModel model) {

                        if(model.result){
                            list.add(new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(new Date()) + ">Lat: " + gpsTracker.getLatitude() + ",Long: " + gpsTracker.getLongitude());
                        }else{
                            list.add("FAILED");
                        }
                        adapter = new MainFragmentAdapter(TrackingActivity.this,
                                android.R.layout.simple_list_item_1, list);
                        listView.setAdapter(adapter);
                        worker.postDelayed(task, 10000);

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        super.failure(error);
                        Singleton.toast(TrackingActivity.this, "Internet Failed", Toast.LENGTH_SHORT );
                        worker.postDelayed(task, 10000);
                    }
                });
    }

    public void setToolbar(@NonNull String title) {
        setSupportActionBar(myToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(" " + title);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
