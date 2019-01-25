package com.example.kotaluwukcom.laporgorontalo.network.interfaces;

import com.example.kotaluwukcom.laporgorontalo.model.BaseResponse;
import com.example.kotaluwukcom.laporgorontalo.network.config.Config;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface DaftarInterface {

    @FormUrlEncoded
    @POST(Config.API_REGISTER)
    Call<BaseResponse> daftar(
            @Field("nama") String nama,
            @Field("umur") String umur,
            @Field("pekerjaan") String pekerjaan,
            @Field("alamat") String alamat,
            @Field("email") String email,
            @Field("password") String password);

}
