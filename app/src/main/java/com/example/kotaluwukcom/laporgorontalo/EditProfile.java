package com.example.kotaluwukcom.laporgorontalo;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.bumptech.glide.request.RequestOptions;
import com.example.kotaluwukcom.laporgorontalo.adapter.PagerAdapter;
import com.example.kotaluwukcom.laporgorontalo.fragment.ProfileFragment;
import com.example.kotaluwukcom.laporgorontalo.model.User;
import com.example.kotaluwukcom.laporgorontalo.network.config.AppController;
import com.example.kotaluwukcom.laporgorontalo.network.config.Config;
import com.example.kotaluwukcom.laporgorontalo.util.PrefUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private TextView id;
    private TextView nama;
    private TextView email;
    private TextView alamat;
    private TextView pekerjaan;
    private TextView umur;
    private TextView notelp;
   // private TextView password;
    TextView  batal, selesai;
    ImageView foto;
    ProgressDialog pd;
    SessionManager sessionManager;
    String getId;
    private Bitmap bitmap;

    private String urlmas = Config.API_URL_FOTO_MAS;

    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        pd = new ProgressDialog(EditProfile.this);

        id =  findViewById(R.id.edit_id);

        nama = findViewById(R.id.edit_nama);
        email = findViewById(R.id.edit_email);
        //password = findViewById(R.id.edit_pass);
        notelp = findViewById(R.id.edit_notelp);
        umur =  findViewById(R.id.edit_umur);
        alamat =  findViewById(R.id.edit_alamat);
        pekerjaan = findViewById(R.id.edit_pekerjaan);


        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);


        batal = (TextView) findViewById(R.id.editbatal);
        selesai = (TextView) findViewById(R.id.editselesai);

        foto = (ImageView) findViewById(R.id.foto);




        /*User user = PrefUtil.getUser(this, PrefUtil.USER_SESSION);
        id.setText(user.getData().getId());
        id.setVisibility(View.GONE);
        nama.setText(user.getData().getNama());
        email.setText(user.getData().getEmail());
        password.setText(user.getData().getPassword());
        notelp.setText(user.getData().getNotelp());
        umur.setText(user.getData().getUmur());
        pekerjaan.setText(user.getData().getPekerjaan());*/


        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               SaveEditDetail();

            }
        });


        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfile.this, MainActivity.class));


            }
        });


        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

    }


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Photo Gallery",
                "Camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                foto.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

            UploadPicture(getId, getStringImage(bitmap));

        }
    }

   private void UploadPicture(final String id, final String foto) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.API_UP_FOTO_MAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(AppController.TAG, response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(EditProfile.this, "Success!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditProfile.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfile.this, "Try Again!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("foto_mas", foto);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


    private void getUserDetail(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
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

                                   // String strId = object.getString("id").trim();
                                    String strName = object.getString("nama").trim();
                                    String strEmail = object.getString("email").trim();
                                    //String strPass = object.getString("password");
                                    String strUmur = object.getString("umur").trim();
                                    String strKerja = object.getString("pekerjaan").trim();
                                    String strAlamat = object.getString("alamat").trim();
                                    String strNotelp = object.getString("notelp").trim();
                                   String strFoto = object.getString("foto_mas").trim();

                                 //   id.setText(strId);
                                    nama.setText(strName);
                                    email.setText(strEmail);
                                   // password.setText(strPass);
                                    umur.setText(strUmur);
                                    pekerjaan.setText(strKerja);
                                    alamat.setText(strAlamat);
                                    notelp.setText(strNotelp);
                                    Glide.with(EditProfile.this)
                                            .load(urlmas+strFoto).apply(RequestOptions.circleCropTransform())
                                            .apply(new RequestOptions().placeholder(R.drawable.user).error(R.drawable.user))
                                            .into(foto);

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditProfile.this, "Error Reading Detail "+e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfile.this, "Error Reading Detail "+error.toString(), Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        requestQueue.add(stringRequest);

    }

    public void onResume() {
        super.onResume();
        getUserDetail();
    }



    private void SaveEditDetail() {

        final String name = this.nama.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
       // final String password = this.password.getText().toString().trim();
        final String umur = this.umur.getText().toString().trim();
        final String alamat = this.alamat.getText().toString().trim();
        final String pekerjaan = this.alamat.getText().toString().trim();
        final String notelp = this.notelp.getText().toString().trim();
        final String foto_mas = this.foto.toString().trim();
        final String id = getId;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.API_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(EditProfile.this, "Data Berhasil Di Edit", Toast.LENGTH_SHORT).show();
                                sessionManager.createSession(name, email, id, alamat, umur, pekerjaan, notelp, foto_mas);
                                startActivity( new Intent(EditProfile.this,MainActivity.class));
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditProfile.this, "Error "+ e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfile.this, "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", name);
                params.put("email", email);
               // params.put("password", password);
                params.put("umur", umur);
                params.put("alamat", alamat);
                params.put("pekerjaan", pekerjaan);
                params.put("notelp", notelp);
                //params.put("foto_mas", foto_mas);
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    public String getStringImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }



}
