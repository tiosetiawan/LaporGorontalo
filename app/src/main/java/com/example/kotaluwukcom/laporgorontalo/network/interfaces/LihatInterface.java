package com.example.kotaluwukcom.laporgorontalo.network.interfaces;

import com.example.kotaluwukcom.laporgorontalo.model.BaseResponse;
import com.example.kotaluwukcom.laporgorontalo.model.ListLapor;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LihatInterface {
    //membaca data
    @GET("read_laporan.php")
    Call<ListLapor> ambilDataLapor();
}
