package com.example.kotaluwukcom.laporgorontalo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.kotaluwukcom.laporgorontalo.fragment.ProfileFragment;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN     = "IS_LOGIN";
    public static final String NAMA       = "NAMA";
    public static final String EMAIL      = "EMAIL";
    public static  final String PASSWORD = "PASSWORD";
    public static final String ID         = "ID";
    public static final String UMUR       = "UMUR";
    public static final String KERJA      = "PEKERJAAN";
    public static final String ALAMAT     = "ALAMAT";
    public static final String NO         = "NOTELP";
    public static final String FOTOMAS    = "FOTOMAS";


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String name, String email, String id, String umur, String pekerjaan, String alamat, String notelp,
                              String foto_mas){

        editor.putBoolean(LOGIN, true);
        editor.putString(NAMA, name);
        editor.putString(EMAIL, email);
        editor.putString(ID, id);
        editor.putString(UMUR, umur);
        editor.putString(KERJA, pekerjaan);
        editor.putString(ALAMAT, alamat);
        editor.putString(NO, notelp);
        editor.putString(FOTOMAS, foto_mas);

        editor.apply();

    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){

        if (!this.isLoggin()){
            Intent i = new Intent(context, Masuk.class);
            context.startActivity(i);
            ((MainActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){

        HashMap<String, String> user = new HashMap<>();
        user.put(NAMA, sharedPreferences.getString(NAMA, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(PASSWORD, sharedPreferences.getString(PASSWORD, null));
        user.put(ID, sharedPreferences.getString(ID, null));
        user.put(UMUR, sharedPreferences.getString(UMUR, null));
        user.put(KERJA, sharedPreferences.getString(KERJA, null));
        user.put(ALAMAT, sharedPreferences.getString(ALAMAT, null));
        user.put(NO, sharedPreferences.getString(NO, null));
        user.put(FOTOMAS, sharedPreferences.getString(FOTOMAS, null));

        return user;
    }

    public void logout(){

        editor.clear();
        editor.commit();
        Intent i = new Intent(context, Masuk.class);
        context.startActivity(i);
        //((ProfileFragment) context).finish();

    }

    public void createSession1(String id, String password) {
        editor.putString(ID, id);
        editor.putString(PASSWORD, password);
    }
}
