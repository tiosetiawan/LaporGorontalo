package com.example.kotaluwukcom.laporgorontalo.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kotaluwukcom.laporgorontalo.InputLapor;
import com.example.kotaluwukcom.laporgorontalo.R;
import com.example.kotaluwukcom.laporgorontalo.adapter.LaporAdapter;
import com.example.kotaluwukcom.laporgorontalo.model.LaporData;
import com.example.kotaluwukcom.laporgorontalo.model.ListLapor;
import com.example.kotaluwukcom.laporgorontalo.network.LihatService;
import com.example.kotaluwukcom.laporgorontalo.network.config.RetrofitBuilder;
import com.example.kotaluwukcom.laporgorontalo.network.interfaces.LihatInterface;



import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class HomeFragment extends Fragment {


    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ArrayList<LaporData> listData;
    Context context;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar pd;
    private Button lapor;

    public HomeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



       View view_home = inflater.inflate(R.layout.fragment_home, container, false);
       recyclerView = (RecyclerView)view_home.findViewById(R.id.recyclerLapor);
        swipeRefreshLayout = (SwipeRefreshLayout) view_home.findViewById(R.id.refreshlap);


        lapor = (Button)view_home.findViewById(R.id.lapor);

        lapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goPindah = new Intent(getActivity(), InputLapor.class);
                startActivity(goPindah);
            }
        });



       int gridColumnCount = getResources().getInteger(R.integer.grid_column_count);

       pd = (ProgressBar) view_home.findViewById(R.id.pd);

       listData = new ArrayList<>();
       ambilData();

       recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumnCount));

       int swipeDirs;
       if (gridColumnCount > 1){
           swipeDirs = 0;
       } else {
           swipeDirs = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
       }

       return view_home;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                refreshContent();
            }
        });


    }


    private void refreshContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listData.clear();
                ambilData();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);

    }

    private void ambilData() {
        pd.setVisibility(View.VISIBLE);


        LihatInterface api = RetrofitBuilder.getLihatService();
        retrofit2.Call<ListLapor> call = api.ambilDataLapor();
            call.enqueue(new Callback<ListLapor>() {
                @Override
                public void onResponse(retrofit2.Call<ListLapor> call, Response<ListLapor> response) {
                    pd.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().toString().equals("true")) {
                            listData = response.body().getLapor();
                            LaporAdapter adapter = new LaporAdapter(listData, getActivity());
                            recyclerView.setAdapter(adapter);

                            //ngetes data di log
                            for (int i = 0; i < listData.size(); i++) {
                                Log.d(TAG, "onResponse: " + listData.get(i).getFoto());
                            }
                        } else {
                            Toast.makeText(getActivity(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Respones is Not Succesfull", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ListLapor> call, Throwable t) {
                    Toast.makeText(getActivity(), "Response Failure", Toast.LENGTH_SHORT).show();
                }


            });


        }

    }

