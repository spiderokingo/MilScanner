package com.infantry.milscanner.Config;


import com.infantry.milscanner.Models.BaseModel;
import com.infantry.milscanner.Models.UsersModel;
import com.infantry.milscanner.Models.WeaponModel;
import com.infantry.milscanner.Utils.ModelCaches;
import com.infantry.milscanner.Utils.MyCallback;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Mast3ro on 9/16/2015.
 */
public class ApiService {
    public static apiEndpointInterface apiEndpointInterface;

    public static apiEndpointInterface getApiEndpointInterface() {
        apiEndpointInterface = null;
        RestAdapter.Builder builderAdapter;
        builderAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.BASIC);

        builderAdapter.setEndpoint(ModelCaches.getInstance().getApiCompletePath() + "php");

        apiEndpointInterface = builderAdapter.build().create(apiEndpointInterface.class);

        return apiEndpointInterface;
    }

    public static apiEndpointInterface getTestInterface(String Path) {
        apiEndpointInterface = null;
        RestAdapter.Builder builderAdapter;
        builderAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.BASIC);
        builderAdapter.setEndpoint("http://" + Path + "/db/php");
        apiEndpointInterface = builderAdapter.build().create(apiEndpointInterface.class);

        return apiEndpointInterface;
    }

    public interface apiEndpointInterface {

        @FormUrlEncoded
        @POST("/loginValidate.php")
        void login(@Field("Username") String username, @Field("Password") String password, @Field("Mode") String mode, MyCallback<UsersModel> cb);

        @FormUrlEncoded
        @POST("/loginValidate.php")
        void apiCheck(@Field("Username") String username,Callback<UsersModel> cb);

        @FormUrlEncoded
        @POST("/getQR.php")
        void getQRDetails(@Field("Mode") String mode,@Field("IdentityID") String identityId, MyCallback<UsersModel> cb);

        @FormUrlEncoded
        @POST("/getQR.php")
        void getQRWeaponDetails(@Field("Mode") String mode,@Field("WeaponNumber") String WeaponNumber, MyCallback<WeaponModel> cb);

        @FormUrlEncoded
        @POST("/getDepositDetails.php")
        void getDepositWeapon(@Field("Mode") String mode,@Field("WeaponNumber") String WeaponNumber, MyCallback<WeaponModel> cb);

        @FormUrlEncoded
        @POST("/getQR.php")
        void submitWithdraw(@Field("Mode") String mode,
                            @Field("PersonalID") String PersonalID,
                            @Field("IdentityID") String IdentityID,
                            @Field("WeaponID") String WeaponID,
                            @Field("WeaponNumber") String WeaponNumber, MyCallback<BaseModel> cb);

    }
}
