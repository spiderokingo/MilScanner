package com.infantry.milscanner.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.infantry.milscanner.Config.ApiService;
import com.infantry.milscanner.Fragment.DepositFragment;
import com.infantry.milscanner.Fragment.MainFragment;
import com.infantry.milscanner.Fragment.SettingFragment;
import com.infantry.milscanner.Fragment.WithdrawFragment;
import com.infantry.milscanner.Models.UsersModel;
import com.infantry.milscanner.R;
import com.infantry.milscanner.Utils.Enum;
import com.infantry.milscanner.Utils.ModelCaches;
import com.infantry.milscanner.Utils.MyCallback;
import com.infantry.milscanner.Utils.Singleton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ScanActivity extends AppCompatActivity {

    @Bind(R.id.my_toolbar)
    Toolbar myToolbar;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.tabs)
    TabLayout tabs;

    private ActionBar mActionBar;
    WithdrawFragment homeFragment;
    DepositFragment activitiesFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        setToolbar("Weapons");

        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        tabs.setupWithViewPager(viewPager);

    }

    /**
     * >>>>>>>>>>>>>>>> LOGIN LOGIC <<<<<<<<<<<<<<<<
     */

    // Get the results:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void setToolbar(@NonNull String title) {
        setSupportActionBar(myToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(" " + title);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewPager(ViewPager viewPager) {
        homeFragment = new WithdrawFragment();
        activitiesFragment = new DepositFragment();
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(homeFragment, Enum.TITLE_TAB_WITHDRAW.getStringValue());
        adapter.addFragment(activitiesFragment, Enum.TITLE_TAB_DEPOSIT.getStringValue());
        viewPager.setAdapter(adapter);
    }

    class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }



        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
