package com.example.kotaluwukcom.laporgorontalo.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.kotaluwukcom.laporgorontalo.EditPass;
import com.example.kotaluwukcom.laporgorontalo.EditProfile;
import com.example.kotaluwukcom.laporgorontalo.InputLapor;
import com.example.kotaluwukcom.laporgorontalo.MainActivity;
import com.example.kotaluwukcom.laporgorontalo.Masuk;
import com.example.kotaluwukcom.laporgorontalo.R;
import com.example.kotaluwukcom.laporgorontalo.SessionManager;
import com.example.kotaluwukcom.laporgorontalo.adapter.PagerAdapter;
import com.example.kotaluwukcom.laporgorontalo.model.LaporData;
import com.example.kotaluwukcom.laporgorontalo.model.ListLapor;
import com.example.kotaluwukcom.laporgorontalo.model.User;
import com.example.kotaluwukcom.laporgorontalo.model.UserData;
import com.example.kotaluwukcom.laporgorontalo.network.config.AppController;
import com.example.kotaluwukcom.laporgorontalo.network.config.Config;
import com.example.kotaluwukcom.laporgorontalo.network.config.RetrofitBuilder;
import com.example.kotaluwukcom.laporgorontalo.network.interfaces.LihatInterface;
import com.example.kotaluwukcom.laporgorontalo.util.PrefUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {


    SharedPreferences sharedPreferences;

    //private SwipeRefreshLayout swipeRefreshLayout;
    SessionManager sessionManager;
    private TextView id;
    private TextView nama;
    private TextView email;
    private TextView alamat;
    private TextView pekerjaan;
    private TextView umur;
    private ImageView foto;
    private TextView notelp;
    private Button btnLogout, editprof, editpass;
    private String urlmas = Config.API_URL_FOTO_MAS;
    PrefUtil prefUtil;
    UserData userData;
    String getId;

    public static void start(Context context) {
        Intent intent = new Intent(context, ProfileFragment.class);
        context.startActivity(intent);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view_profile = inflater.inflate(R.layout.fragment_profile, container, false);
        //swipeRefreshLayout = (SwipeRefreshLayout) view_profile.findViewById(R.id.refreshprof);
        id = (TextView) view_profile.findViewById(R.id.idmas);
        nama = (TextView) view_profile.findViewById(R.id.nama);
        alamat = (TextView) view_profile.findViewById(R.id.alamat);
        umur = (TextView) view_profile.findViewById(R.id.umur);
        pekerjaan = (TextView) view_profile.findViewById(R.id.kerja);
        email = (TextView) view_profile.findViewById(R.id.email);
        notelp = (TextView) view_profile.findViewById(R.id.notelpon);
        foto = (ImageView)view_profile.findViewById(R.id.foto);


        sessionManager = new SessionManager(getContext());
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);

        btnLogout = (Button) view_profile.findViewById(R.id.btn_logout);
        editpass = (Button) view_profile.findViewById(R.id.editpass);
        editprof =  (Button) view_profile.findViewById(R.id.editprof);

       /* User user = PrefUtil.getUser(getContext(), PrefUtil.USER_SESSION);
        id.setText(user.getData().getId());
        nama.setText(user.getData().getNama());
        email.setText(user.getData().getEmail());
        alamat.setText(user.getData().getAlamat());
        umur.setText(user.getData().getUmur());
        pekerjaan.setText(user.getData().getPekerjaan());
        notelp.setText(user.getData().getNotelp());
        Glide.with(ProfileFragment.this)
        .load(urlmas+user.getData().getFoto_mas()).apply(RequestOptions.circleCropTransform())
                .into(foto);*/





     /* swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                // Handler digunakan untuk menjalankan jeda selama 5 detik
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Berhenti berputar/refreshing
                        swipeRefreshLayout.setRefreshing(false);



                        //Berganti Text Setelah Layar di Refresh


                    }
                },2000); //4000 millisecond = 4 detik
            }
        });*/


        editprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent goPindah = new Intent(getActivity(), EditProfile.class);
                startActivity(goPindah);
            }
        });

        editpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent Pindah = new Intent(getActivity(), EditPass.class);
                startActivity(Pindah);
            }
        });



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogKeluar();

            }
        });

        return view_profile;
        

    }


    private void showDialogKeluar(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        // set title dialog
        alertDialogBuilder.setTitle("Keluar dari aplikasi?");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Klik Ya untuk keluar!")
                .setIcon(R.mipmap.ic_lapor)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        sessionManager.logout();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }


    private void getUserDetail(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.API_READ_MAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(AppController.TAG, response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")){

                                for (int i =0; i < jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strName = object.getString("nama").trim();
                                    String strEmail = object.getString("email").trim();
                                    String strUmur = object.getString("umur").trim();
                                    String strKerja = object.getString("pekerjaan").trim();
                                    String strAlamat = object.getString("alamat").trim();
                                    String strNotelp = object.getString("notelp").trim();
                                     String strFoto = object.getString("foto_mas").trim();

                                    nama.setText(strName);
                                    email.setText(strEmail);
                                    umur.setText(strUmur);
                                    pekerjaan.setText(strKerja);
                                    alamat.setText(strAlamat);
                                    notelp.setText(strNotelp);
                                    //foto.glide
                                    Glide.with(ProfileFragment.this)
                                            .load(urlmas+strFoto).apply(RequestOptions.circleCropTransform())
                                            .apply(new RequestOptions().placeholder(R.drawable.user).error(R.drawable.user))
                                            .into(foto);

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Error Reading Detail "+e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Error Reading Detail "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new HashMap<>();
                params.put("id", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    public void onResume() {
        super.onResume();
        getUserDetail();
    }


    /*private void refreshContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);

    }*/


    private void finish() {
    }

    void logoutAct() { PrefUtil.clear(getContext());
    }


}
