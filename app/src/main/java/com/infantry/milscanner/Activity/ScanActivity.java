package com.infantry.milscanner.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.infantry.milscanner.Config.ApiService;
import com.infantry.milscanner.Fragment.DepositFragment;
import com.infantry.milscanner.Fragment.WithdrawFragment;
import com.infantry.milscanner.Models.UsersModel;
import com.infantry.milscanner.Models.WeaponModel;
import com.infantry.milscanner.R;
import com.infantry.milscanner.Utils.Enum;
import com.infantry.milscanner.Utils.MyCallback;
import com.infantry.milscanner.Utils.Singleton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ScanActivity extends AppCompatActivity {

    @Bind(R.id.my_toolbar)
    Toolbar myToolbar;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.tabs)
    TabLayout tabs;

    private ActionBar mActionBar;
    WithdrawFragment withdrawFragment;
    DepositFragment depositFragment;

    private final String User = "USER";
    private final String Weapon = "WEAPON";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        setToolbar("อาวุธ");

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
            if(result.getContents() != null) {
                Singleton.toast(getApplicationContext(),result.getContents(),Toast.LENGTH_LONG);
                if(tabs.getSelectedTabPosition() == 0)
                    getQrDetailsFromServer(result.getContents());
                else
                    getDepositDetails(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void getQrDetailsFromServer(final String text) {
        switch (withdrawFragment.scanState){
            case User:
                ApiService.getApiEndpointInterface().getQRDetails(Enum.MODE_USER.getStringValue(), text, new MyCallback<UsersModel>() {
                    @Override
                    public void good(UsersModel model) {
                        if(model != null){
                            if(model.result) {
                                model.IdentityID = text;
                                withdrawFragment.showPersonUI(model);
                                withdrawFragment.scanState = Enum.MODE_WEAPON.getStringValue();
                            }else{
                                Singleton.toast(getApplicationContext(), model.message, Toast.LENGTH_LONG);
                            }
                        }
                    }
                });
                break;
            case Weapon:
                ApiService.getApiEndpointInterface().getQRWeaponDetails(Enum.MODE_WEAPON.getStringValue(), text, new MyCallback<WeaponModel>() {
                    @Override
                    public void good(WeaponModel model) {
                        if (model != null) {
                            if (model.result) {
                                withdrawFragment.showWeaponUI(model);
                            } else {
                                Singleton.toast(getApplicationContext(), model.message, Toast.LENGTH_LONG);
                            }
                        }
                    }
                });
                break;
        }
    }

    public void getDepositDetails(final String text) {
        switch (depositFragment.scanState){
            case Weapon:
                ApiService.getApiEndpointInterface().getDepositWeapon(Enum.MODE_WEAPON.getStringValue(), text, new MyCallback<WeaponModel>() {
                    @Override
                    public void good(WeaponModel model) {
                        if (model != null) {
                            if (model.result) {
                                depositFragment.showWeaponUI(model);
                                depositFragment.scanState = Enum.MODE_USER.getStringValue();
                            } else {
                                Singleton.toast(getApplicationContext(), model.message, Toast.LENGTH_LONG);
                            }
                        }
                    }
                });
                break;
            case User:
                ApiService.getApiEndpointInterface().getQRDetails(Enum.MODE_USER.getStringValue(), text, new MyCallback<UsersModel>() {
                    @Override
                    public void good(UsersModel model) {
                        if (model != null) {
                            if (model.result) {
                                model.IdentityID = text;
                                depositFragment.showPersonUI(model);
                            } else {
                                Singleton.toast(getApplicationContext(), model.message, Toast.LENGTH_LONG);
                            }
                        }
                    }
                });
                break;
        }
    }


    public void setToolbar(@NonNull String title) {
        setSupportActionBar(myToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(" " + title);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewPager(ViewPager viewPager) {
        withdrawFragment = new WithdrawFragment();
        depositFragment = new DepositFragment();
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(withdrawFragment, Enum.TITLE_TAB_WITHDRAW.getStringValue());
        adapter.addFragment(depositFragment, Enum.TITLE_TAB_DEPOSIT.getStringValue());
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
