package com.infantry.milscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.infantry.milscanner.Activity.LoginActivity;
import com.infantry.milscanner.Activity.MainActivity;
import com.infantry.milscanner.Utils.ModelCaches;
import com.infantry.milscanner.Utils.Singleton;

import timber.log.Timber;

/**
 * Created by MKinG on 2/15/2016.
 */
public class Launcher extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }else {
            if (ModelCaches.getInstance().getUsersDetails().Username == null) {
                startActivity(new Intent(Launcher.this, LoginActivity.class));
            }else{
                startActivity(new Intent(Launcher.this, MainActivity.class));
            }
            //else just close the window will find Behind Activity
            finish();

        }
    }
}
