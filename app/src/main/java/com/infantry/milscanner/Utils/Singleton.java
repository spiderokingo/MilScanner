package com.infantry.milscanner.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.infantry.milscanner.Activity.LoginActivity;

/**
 * Created by Mast3ro on 9/19/2015.
 */
public class Singleton {

    private Context mContext;
    private static SharedPreferences sharedPreferences;
    private static String APP_TOKEN;
    private static Toast toast;

    private static Singleton ourInstance = new Singleton();
    public static Singleton getInstance() {
        return ourInstance;
    }

    //get Token of Application
    public String getAppToken() {
        return APP_TOKEN;
    }

    //get shared preference
    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    //initial shared preference
    public void initSharedPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(Enum.SHARED_PREF_NAME.getStringValue(), Context.MODE_PRIVATE);
        APP_TOKEN = sharedPreferences.getString(Enum.SHARED_PREF_KEY_TOKEN.getStringValue(), null);
    }

    public void setSharedPrefString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
        if (key.equals(Enum.SHARED_PREF_KEY_TOKEN.getStringValue()))
            APP_TOKEN = value;
    }

    public static void toast(Context context, String message, int length) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(context, message, length);
        toast.show();
    }

    public Context getCurrentContext() {
        return mContext;
    }

    public void setCurrentContext(Context mContext) {
        this.mContext = mContext;
    }

    public void goLogin(Context context) {
        Singleton.getInstance().setSharedPrefString(Enum.SHARED_PREF_USER.getStringValue(), "");
        Intent login = new Intent(context, LoginActivity.class);
        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(login);

        //Clear Shared Pref data
        ModelCaches.getInstance().clearModelOnLogout();
    }

    /**
     * hide Keyboard in any Activity
     */
    public void hideKeyboard(Activity activity){
        try{
            //hide Keyboard
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }
}
