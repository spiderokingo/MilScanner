package com.infantry.milscanner.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.infantry.milscanner.Config.ApiService;
import com.infantry.milscanner.Models.UsersModel;
import com.infantry.milscanner.R;
import com.infantry.milscanner.Utils.*;
import com.infantry.milscanner.Utils.Enum;
import com.squareup.okhttp.Call;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.et_login_username)
    EditText etLoginUsername;
    @Bind(R.id.et_login_password)
    EditText etLoginPassword;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.rootLayout)
    RelativeLayout rootLayout;
    @Bind(R.id.btn_setting)
    ImageButton btnSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setOnClick();

    }

    /**
     * >>>>>>>>>>>>>>>> LOGIN LOGIC <<<<<<<<<<<<<<<<
     */

    private void setOnClick() {
        btnLogin.setOnClickListener(loginOnClicked);
        btnSetting.setOnClickListener(loginOnClicked);
    }

    /**
     * OnClicked class of Login Activity
     */
    View.OnClickListener loginOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Singleton.getInstance().hideKeyboard(LoginActivity.this);
            switch (view.getId()) {
                case R.id.btn_login:
                    loginApi();
                    break;
                case R.id.btn_setting:
                    showSettingDialog();

                    break;
            }
        }
    };

    /**
     * Call Login api
     */
    private void loginApi() {
        ApiService.getApiEndpointInterface().login(etLoginUsername.getText().toString(),
            etLoginPassword.getText().toString(), Enum.MODE_LOGIN.getStringValue(), new MyCallback<UsersModel>() {
                @Override
                public void good(UsersModel model) {

                    if(model != null){
                        if (model.result) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            ModelCaches.getInstance().setUsersDetails(model);
                        }else{
                            Singleton.toast(getApplicationContext(), model.message, Toast.LENGTH_SHORT);
                        }
                    }

                }
            });
    }

    public void showSettingDialog(){
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Setting Server Path")
                .positiveText("Save")
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .titleColor(getResources().getColor(R.color.colorPrimaryDark))
                .negativeText("Cancel")
                .negativeColor(getResources().getColor(R.color.gray))
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Server Path.....", ModelCaches.getInstance().getApiPath(), false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(final MaterialDialog dialog, final CharSequence input) {
                        //Update Local
                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(dialog.getView().getWindowToken(), 0);

                            ApiService.getTestInterface(input.toString()).apiCheck("", new Callback<UsersModel>() {
                                @Override
                                public void success(UsersModel usersModel, Response response) {

                                    Singleton.getInstance().setSharedPrefString(Enum.SHARED_PREF_KEY_API_PATH.getStringValue(), input.toString());
                                    Snackbar.make(rootLayout, "Server Path Saved", Snackbar.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Singleton.toast(getBaseContext(), "Please Check Your Server Path", Toast.LENGTH_SHORT);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .build();
        dialog.getBuilder().autoDismiss(false);
        dialog.show();
    }
}
