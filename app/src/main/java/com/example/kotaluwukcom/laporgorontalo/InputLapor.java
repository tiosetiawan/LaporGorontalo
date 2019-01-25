package com.example.kotaluwukcom.laporgorontalo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kotaluwukcom.laporgorontalo.fragment.ProfileFragment;
import com.example.kotaluwukcom.laporgorontalo.model.User;
import com.example.kotaluwukcom.laporgorontalo.network.config.AppController;
import com.example.kotaluwukcom.laporgorontalo.network.config.Config;
import com.example.kotaluwukcom.laporgorontalo.util.PrefUtil;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.msoftworks.easynotify.EasyNotify;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class InputLapor extends AppCompatActivity {

    Button buttonUpload, btnmaps;
    Toolbar toolbar;
    TextView statusmaps, textid;
    ImageView imageView;
    EditText deskripsilap, tanggal, api_key, title, topic, sound;
    Bitmap bitmap, decoded;

    int success;
    private static final int RC_MAPS = 4;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100
   // String id;
    String getId;

    private static final String TAG = InputLapor.class.getSimpleName();


    private String UPLOAD_URL = "http://192.168.1.15/laporgorontalorev/inputlapor.php";

    public static final String API_KEY = "AIzaSyDg5lJkzm5OipN02BGTxxY6gqkte-swtcI";
    public static final String TittlePol = "Polres Gorontalo Kota";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String KEY_IMAGE = "foto";
    private String KEY_NAME = "deskripsi";
    private String KEY_LAT =  "latitude_laporan";
    private String KEY_LNG =  "longtitude_laporan";
    private String KEY_ALMT = "alamat";
    private String KEY_TGL  = "date";
    private String KEY_ID = "id";

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    SessionManager sessionManager;

    String tag_json_obj = "json_obj_req";
    SharedPreferences sharedpreferences;
    public static void start(Context context) {
        Intent intent = new Intent(context, InputLapor.class);
        context.startActivity(intent);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_lapor);

       // toolbar = (Toolbar) findViewById(R.id.toolbar);
       //setSupportActionBar(toolbar);

        myCalendar = Calendar.getInstance();
        Context context = getApplicationContext();

        imageView = (ImageView) findViewById(R.id.imageView);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);

        deskripsilap = (EditText) findViewById(R.id.deskripsi);

        tanggal = (EditText) findViewById(R.id.tanggal);

        btnmaps = (Button)findViewById(R.id.btn_maps);

        statusmaps     = (TextView)findViewById(R.id.status_maps);
        textid         = (TextView)findViewById(R.id.textid);

        api_key = (EditText) findViewById(R.id.api_key);
        topic= (EditText) findViewById(R.id.ntopic);
        title= (EditText) findViewById(R.id.ntitle);
        sound= (EditText) findViewById(R.id.nsound);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();


        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);


        api_key.setText(API_KEY);
        topic.setText("kecelakaan");
        sound.setText("police");
        title.setText(TittlePol);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });


        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel();
            }
        };

        tanggal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(InputLapor.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        buttonUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String deskripsi = deskripsilap.getText().toString().trim();


                if (!deskripsi.isEmpty() ) {
                    uploadImage();
                }else {
                    deskripsilap.setError("Deskripsi Tidak Boleh Kosong");
                }


            }
        });

        btnmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(InputLapor.this), RC_MAPS);
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "EEEE, dd MMMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        tanggal.setText(sdf.format(myCalendar.getTime()));
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

                                    String strId = object.getString("id").trim();

                                    textid.setText(strId);

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(InputLapor.this, "Error Reading Detail "+e.toString(), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(InputLapor.this, MainActivity.class);
                            startActivity(intent);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(InputLapor.this, "Error Reading Detail "+error.toString(), Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(InputLapor.this);
        requestQueue.add(stringRequest);

    }

    public void onResume() {
        super.onResume();
        getUserDetail();
    }





    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size,  baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;

    }

    private void uploadImage() {
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.e("v Add", jObj.toString());

                                Toast.makeText(InputLapor.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(InputLapor.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callEasyNotify();
                        //menghilangkan progress dialog
                        loading.dismiss();
                        Intent intent = new Intent(InputLapor.this, MainActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();

                        //menampilkan toast
                        AlertDialog.Builder builder = new AlertDialog.Builder(InputLapor.this);
                        builder.setMessage("Silahkan upload foto kejadian demi keaslian pelaporan kecelakaan.")
                                .setNegativeButton("Ok", null).create().show();
                        Log.e(TAG, "Anda belum memilih foto");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters

                Map<String, String> params = new HashMap<String, String>();
                //menambah parameter yang di kirim ke web servis
                params.put(KEY_IMAGE, getStringImage(decoded));
                params.put(KEY_NAME, deskripsilap.getText().toString().trim());
                params.put(KEY_ALMT, statusmaps.getText().toString().trim());
                params.put(KEY_TGL, tanggal.getText().toString().trim());
                params.put(KEY_LAT, lat );
                params.put(KEY_LNG, lng );
                params.put("id", textid.getText().toString().trim());


                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);

    }

    private void callEasyNotify() {
        EasyNotify easyNotify = new EasyNotify(api_key.getText().toString());
        easyNotify.setSendBy(EasyNotify.TOPIC);
        easyNotify.setTopic(topic.getText().toString());
        easyNotify.setTitle(title.getText().toString());
        easyNotify.setBody(deskripsilap.getText().toString());
        easyNotify.setSound(sound.getText().toString());
        easyNotify.nPush();
        easyNotify.setEasyNotifyListener(new EasyNotify.EasyNotifyListener() {
            @Override
            public void onNotifySuccess(String s) {
                Toast.makeText(InputLapor.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNotifyError(String s) {
                Toast.makeText(InputLapor.this, s, Toast.LENGTH_SHORT).show();
            }

        });
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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



    Geocoder geocoder = new Geocoder(this, Locale.getDefault());


    String lat, lng;
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
                 }else if (requestCode == RC_MAPS && resultCode  == RESULT_OK){
                Place place = PlacePicker.getPlace(InputLapor.this, data);
                String alamat = String.format("Lokasi Kecelakaan : %s", place.getAddress());
                lat = String.valueOf(place.getLatLng().latitude);
                lng = String.valueOf(place.getLatLng().longitude);

                statusmaps.setText(alamat);
            }
        }


    private void kosong() {
        imageView.setImageResource(0);
        deskripsilap.setText(null);
        statusmaps.setText(null);
        textid.setText(null);



    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imageView.setImageBitmap(decoded);
    }

    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
