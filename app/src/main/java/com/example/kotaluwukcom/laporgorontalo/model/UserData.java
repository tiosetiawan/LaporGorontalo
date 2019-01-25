package com.example.kotaluwukcom.laporgorontalo.model;

import android.content.SharedPreferences;

import java.util.HashMap;

public class UserData {

    SharedPreferences sharedPreferences;

    private String nama;
    private String email;
    private String umur;
    private String alamat;
    private String foto_mas;
    private String pekerjaan;
    private String notelp;
    private String id;
    private String password;

    public UserData() {
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUmur() { return umur; }

    public void setUmur(String umur) { this.umur = umur; }

    public String getAlamat() { return alamat; }

    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getFoto_mas() { return foto_mas; }

    public void setFoto_mas(String foto_mas) { this.foto_mas = foto_mas; }

    public String getPekerjaan() { return pekerjaan; }

    public void setPekerjaan(String pekerjaan) { this.pekerjaan = pekerjaan; }

    public String getNotelp() {
        return notelp;
    }

    public void setNotelp(String notelp) {
        this.notelp = notelp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HashMap<String, String> getUserDetail(){

        HashMap<String, String> user = new HashMap<>();
        user.put(nama, sharedPreferences.getString(email, null));
        user.put(email, sharedPreferences.getString(email, null));
        user.put(id, sharedPreferences.getString(id, null));

        return user;
    }
}



