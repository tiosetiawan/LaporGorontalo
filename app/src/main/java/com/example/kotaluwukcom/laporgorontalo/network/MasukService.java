package com.example.kotaluwukcom.laporgorontalo.network;

import android.content.Context;

import com.example.kotaluwukcom.laporgorontalo.network.config.RetrofitBuilder;
import com.example.kotaluwukcom.laporgorontalo.network.interfaces.MasukInterface;


import retrofit2.Callback;


public class MasukService {

    private MasukInterface masukInterface;

    public MasukService(Context context){
        masukInterface = RetrofitBuilder.builder(context)
                .create(MasukInterface.class);
    }
    public void doMasuk(String email, String password, Callback callback){
        masukInterface.masuk(email, password).enqueue(callback);
    }

}
