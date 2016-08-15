package com.infantry.milscanner.Utils;

import com.google.gson.Gson;
import com.infantry.milscanner.Models.UsersModel;

/**
 * Created by MKinG on 1/29/2016.
 */
public class ModelCaches {
    private static ModelCaches ourInstance = new ModelCaches();
    public static ModelCaches getInstance() {
        return ourInstance;
    }

    /**
     * Cache of User Model
     */
    public UsersModel getUsersDetails() {
        if(Singleton.getInstance().getSharedPreferences().getString(Enum.SHARED_PREF_USER.getStringValue(),"").equals("")){
            return new UsersModel();
        }
        return new Gson().fromJson(Singleton.getInstance().getSharedPreferences().getString(Enum.SHARED_PREF_USER.getStringValue(),""), UsersModel.class);
    }
    public void setUsersDetails(UsersModel usersModel) {
        Singleton.getInstance().setSharedPrefString(Enum.SHARED_PREF_USER.getStringValue(), new Gson().toJson(usersModel));
    }

    public String getApiPath() {
        return Singleton.getInstance().getSharedPreferences().getString(
                Enum.SHARED_PREF_KEY_API_PATH.getStringValue(), Enum.API_PATH.getStringValue()
        );
    }

    public String getApiCompletePath(){
        return "http://" + ModelCaches.getInstance().getApiPath() + "/db/";
    }

    public void clearModelOnLogout(){
        setUsersDetails(new UsersModel());
    }
}
