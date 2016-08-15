package com.infantry.milscanner.Utils;

import android.widget.Toast;

import com.google.gson.Gson;
import com.infantry.milscanner.Models.BaseModel;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by MKinG on 1/21/2016.
 */
public abstract class MyCallback<T extends BaseModel> implements Callback<T> {
    @Override
    public void success(T model, Response response) {
        good(model);
        Timber.d("res: " + new Gson().toJson(model));
    }

    public abstract void good(T model);
    @Override
    public void failure(RetrofitError error) {
        try{
            if(error.getResponse() == null){
                Singleton.toast(Singleton.getInstance().getCurrentContext(), "404 Not Found", Toast.LENGTH_SHORT);
            }else if(error.isNetworkError()){
                Singleton.toast(Singleton.getInstance().getCurrentContext(), "Network Error", Toast.LENGTH_SHORT);
            }
            error.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
