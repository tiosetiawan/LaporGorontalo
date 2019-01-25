package com.example.kotaluwukcom.laporgorontalo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.bumptech.glide.request.RequestOptions;
import com.example.kotaluwukcom.laporgorontalo.R;
import com.example.kotaluwukcom.laporgorontalo.network.config.AppController;
import com.example.kotaluwukcom.laporgorontalo.network.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditPass extends AppCompatActivity {

    private TextView id;
    private TextView password;
  //  private TextView confirm_pass;

    TextView  batal, selesai;

    SessionManager sessionManager;
    String getId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pass);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        id =  findViewById(R.id.edit_id);

        password = findViewById(R.id.edit_pass);
    //    confirm_pass = findViewById(R.id.edit_confirm_pass);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);


        batal = (TextView) findViewById(R.id.editbatal);
        selesai = (TextView) findViewById(R.id.editselesai);


        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SaveEditPass();

            }
        });


        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditPass.this, MainActivity.class));


            }
        });


    }




    private void SaveEditPass() {


        final String password = this.password.getText().toString().trim();
        final String id = getId;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.API_UPDATE_PASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(EditPass.this, "Data Berhasil Di Edit", Toast.LENGTH_SHORT).show();
                                sessionManager.createSession1(id, password);
                                startActivity( new Intent(EditPass.this,MainActivity.class));
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditPass.this, "Error "+ e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(EditPass.this, "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("password", password);
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }



}
