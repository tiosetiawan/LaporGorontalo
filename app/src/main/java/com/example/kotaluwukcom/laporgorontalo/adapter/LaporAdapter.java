package com.example.kotaluwukcom.laporgorontalo.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.example.kotaluwukcom.laporgorontalo.MainActivity;
import com.example.kotaluwukcom.laporgorontalo.model.ListLapor;
import com.example.kotaluwukcom.laporgorontalo.network.config.Config;
import com.squareup.picasso.Picasso;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import com.example.kotaluwukcom.laporgorontalo.R;
import com.example.kotaluwukcom.laporgorontalo.model.LaporData;

import java.util.ArrayList;

public class LaporAdapter extends RecyclerView.Adapter<LaporAdapter.MyHolder> {

    private ArrayList<LaporData> listData;
    private Context context;
    RequestManager glide;
    private String urllap = Config.API_URL_FOTO_LAP;
    private String urlmas = Config.API_URL_FOTO_MAS;
    Button petunjuk;


    public LaporAdapter(ArrayList<LaporData> listData, Context context ){

        this.listData = listData;
        this.context = context;
        glide = Glide.with(context);

    }

    public LaporAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_lapor, parent, false);
        MyHolder holder = new MyHolder(v);
        return holder;


    }

    public void onBindViewHolder (LaporAdapter.MyHolder holder, final int position){
        final LaporData laporData = listData.get(position);

        glide.load(urlmas+listData.get(position).getFoto_mas()).apply(RequestOptions.circleCropTransform())
                .into(holder.imgView);

        holder.tv_name.setText(listData.get(position).getNama());
        holder.tv_time.setText(listData.get(position).getJamlapor());
        holder.tv_status.setText(listData.get(position).getDeskripsi());


        holder.petunjuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + listData.get(position).getLatitudeLapor() + "," + listData.get(position).getLongtitudeLapor() + "");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });


        if (laporData.getFoto() == null) {
            holder.imgPost.setVisibility(View.GONE);
        } else {
            holder.imgPost.setVisibility(View.VISIBLE);
            glide.load(urlmas+listData.get(position).getFoto())
                    .into(holder.imgPost);
        }


    }

    public int getItemCount(){ return listData.size();}




    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imgView, imgPost;
        TextView tv_name, tv_time, tv_status;
        Button petunjuk;


        public MyHolder(View itemView) {
            super(itemView);

            imgPost = (ImageView) itemView.findViewById(R.id.imgView_postPic);
            imgView = (ImageView) itemView.findViewById(R.id.imgView_proPic);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_status =(TextView) itemView.findViewById(R.id.tv_status);
            petunjuk = (Button) itemView.findViewById(R.id.petunjuk);

        }
    }




}
