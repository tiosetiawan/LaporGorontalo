package com.example.kotaluwukcom.laporgorontalo.network;

import android.content.Context;

import com.example.kotaluwukcom.laporgorontalo.network.config.RetrofitBuilder;
import com.example.kotaluwukcom.laporgorontalo.network.interfaces.LihatInterface;

import retrofit2.Retrofit;

public class LihatService {
    private LihatInterface lihatInterface;

    public LihatService(Context context){
        lihatInterface = RetrofitBuilder.builder(context)
                .create(LihatInterface.class);
    }

}
