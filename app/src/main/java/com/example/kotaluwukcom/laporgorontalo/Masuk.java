package com.example.kotaluwukcom.laporgorontalo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kotaluwukcom.laporgorontalo.fragment.ProfileFragment;
import com.example.kotaluwukcom.laporgorontalo.model.BaseResponse;
import com.example.kotaluwukcom.laporgorontalo.model.User;
import com.example.kotaluwukcom.laporgorontalo.network.MasukService;
import com.example.kotaluwukcom.laporgorontalo.network.config.Config;
import com.example.kotaluwukcom.laporgorontalo.util.PrefUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Masuk extends AppCompatActivity {

    Intent intent;
    SessionManager sessionManager;
    Button btn_register;
    private ProgressBar loading;
    private EditText emailText;
    private EditText passwordText;
    private Button btnMasuk;
    private Button btnDaftar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masuk);

       loading = findViewById(R.id.loading);
        emailText = (EditText) findViewById(R.id.txt_email);
        passwordText = (EditText) findViewById(R.id.txt_password);
        btnMasuk = (Button) findViewById(R.id.btn_login);
        btnDaftar = (Button) findViewById(R.id.btn_register);

        sessionManager = new SessionManager(this);

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = emailText.getText().toString().trim();
                String mPass = passwordText.getText().toString().trim();

                if (!mEmail.isEmpty() || !mPass.isEmpty()) {
                    Login(mEmail, mPass);
                } else {
                    emailText.setError("Email Tidak Boleh Kosong");
                    passwordText.setError("Password Tidak Boleh Kosong");
                }
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Masuk.this, Daftar.class));
            }
        });

    }

    private void Login(final String email, final String password) {

        loading.setVisibility(View.VISIBLE);
        btnMasuk.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.API_LOGIN,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            //String error = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String name = object.getString("nama").trim();
                                    String email = object.getString("email").trim();
                                   // String password = object.getString("password").trim();
                                    String id = object.getString("id").trim();
                                    String umur = object.getString("umur").trim();
                                    String pekerjaan = object.getString("pekerjaan").trim();
                                    String alamat = object.getString("alamat").trim();
                                    String notelp = object.getString("notelp").trim();
                                    String foto_mas = object.getString("foto_mas").trim();

                                    sessionManager.createSession(name, email, id, umur, pekerjaan, alamat, notelp, foto_mas);

                                    Intent intent = new Intent(Masuk.this, MainActivity.class);
                                    intent.putExtra("nama", name);
                                    intent.putExtra("email", email);
                                   // intent.putExtra("password", password);
                                    intent.putExtra("umur", umur);
                                    intent.putExtra("pekerjaan", pekerjaan);
                                    intent.putExtra("alamat", alamat);
                                    intent.putExtra("notelp", notelp);
                                    intent.putExtra("foto_mas", foto_mas);
                                    startActivity(intent);
                                    finish();

                                   loading.setVisibility(View.GONE);


                                }

                            }else {
                                loading.setVisibility(View.GONE);
                                AlertDialog.Builder builder = new AlertDialog.Builder(Masuk.this);
                                builder.setMessage("Password Salah")
                                        .setNegativeButton("Ulangi", null).create().show();
                                //Toast.makeText(Masuk.this, "Password Salah" , Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                           loading.setVisibility(View.GONE);
                            btnMasuk.setVisibility(View.VISIBLE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(Masuk.this);
                            builder.setMessage("Email Salah")
                                    .setNegativeButton("Ulangi", null).create().show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       loading.setVisibility(View.GONE);
                        btnMasuk.setVisibility(View.VISIBLE);
                        Toast.makeText(Masuk.this, "Error " +error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}