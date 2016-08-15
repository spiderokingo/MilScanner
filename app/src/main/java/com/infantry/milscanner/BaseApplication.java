package com.infantry.milscanner;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.infantry.milscanner.Utils.Singleton;

import timber.log.Timber;

/**
 * Created by MKinG on 1/26/2016.
 */
public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks{

    public static int resume = 0, pause = 0;

    @Override
    public void onCreate() {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());

        //initial shared pref
        Singleton.getInstance().initSharedPrefs(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        registerActivityLifecycleCallbacks(this);
    }

    public static boolean isAppForeground() {
        return resume > pause;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        Singleton.getInstance().setCurrentContext(activity);
        resume++;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        pause++;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
