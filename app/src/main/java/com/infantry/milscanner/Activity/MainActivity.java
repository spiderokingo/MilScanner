package com.infantry.milscanner.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.infantry.milscanner.Config.ApiService;
import com.infantry.milscanner.Fragment.MainFragment;
import com.infantry.milscanner.Fragment.SettingFragment;
import com.infantry.milscanner.Models.UsersModel;
import com.infantry.milscanner.R;
import com.infantry.milscanner.Utils.*;
import com.infantry.milscanner.Utils.Enum;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.infantry.milscanner.Utils.Enum.*;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_QR_SCAN = 0;
    private AccountHeader headerResult;
    public Drawer drawer;
    private ActionBar mActionBar;

    @Bind(R.id.my_toolbar)
    Toolbar myToolbar;
    private MainFragment mainFragment;
    private SettingFragment settingFragment;
    int currentTab = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setToolbar("หน้าหลัก");

        mainFragment = new MainFragment();
        settingFragment = new SettingFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commit();

        initNavDrawer();


    }

    ///// LOGIC BEGIN /////

    public void setToolbar(@NonNull String title) {
        setSupportActionBar(myToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(" " + title);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initNavDrawer(){
        final int NAV_MAIN = 1;
        final int NAV_SETTING = 2;
        final int NAV_LOGOUT = 3;

        //below line is for loading profile image from url
        DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.clear(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx) {
                return null;
            }

        });


        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName(MENU_MAIN.getStringValue()).withIdentifier(NAV_MAIN);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName(MENU_SETTING.getStringValue()).withIdentifier(NAV_SETTING);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withName(MENU_LOGOUT.getStringValue()).withIdentifier(NAV_LOGOUT);

        String imagePath = ModelCaches.getInstance().getApiCompletePath() + ModelCaches.getInstance().getUsersDetails().ImageFullPath;
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionListEnabled(false)
                .withHeaderBackground(R.drawable.nav_header)
                .addProfiles(new ProfileDrawerItem()
                                .withName(ModelCaches.getInstance().getUsersDetails().TitleName
                                        + " " + ModelCaches.getInstance().getUsersDetails().FirstName
                                        + " " + ModelCaches.getInstance().getUsersDetails().LastName)
                                .withEmail(ModelCaches.getInstance().getUsersDetails().Permission)
                                .withIcon(imagePath)
                )

                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(myToolbar)
                .withTranslucentStatusBar(false)
                .withDisplayBelowStatusBar(false)
                .withActionBarDrawerToggleAnimated(true)
                .withStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark))
                .addDrawerItems(item1, item2, item5)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                        drawer.closeDrawer();
                        switch (i) {
                            case NAV_MAIN:
                                myToolbar.setTitle(Enum.MENU_MAIN.getStringValue());
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commit();
                                currentTab = 1;
                                return true;
                            case NAV_SETTING:
                                myToolbar.setTitle(Enum.MENU_SETTING.getStringValue());
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, settingFragment).commit();
                                currentTab = 2;
                                return true;
                            case 3:
                                onLogout();
                            default:
                        }
                        return false;
                    }
                })
                .build();
    }


    private void onLogout(){
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(Enum.TITLE_CONFIRM_LOGOUT.getStringValue())
                .positiveText(Enum.EXIT.getStringValue())
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .titleColor(getResources().getColor(R.color.colorPrimary))
                .content(Enum.CONTENT_CONFIRM_LOGOUT.getStringValue())
                .negativeText(Enum.CANCEL.getStringValue())
                .negativeColor(getResources().getColor(R.color.gray))
                .build();

        dialog.getBuilder().callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
                Singleton.getInstance().goLogin(MainActivity.this);
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                super.onNegative(dialog);
                drawer.setSelection(currentTab);
            }
        });
        dialog.show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
