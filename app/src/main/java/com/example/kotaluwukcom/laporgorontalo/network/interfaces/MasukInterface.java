package com.example.kotaluwukcom.laporgorontalo.network.interfaces;

import com.example.kotaluwukcom.laporgorontalo.model.User;
import com.example.kotaluwukcom.laporgorontalo.network.config.Config;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface MasukInterface {

    @FormUrlEncoded
    @POST(Config.API_LOGIN)
    Call<User>masuk(
            @Field("email") String email,
            @Field("password") String password);

}
