package com.example.kotaluwukcom.laporgorontalo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LaporData {

    @SerializedName("id_laporan")
    @Expose
    private String idLaporan;


    @SerializedName("id")
    @Expose
    private  String id;

    @SerializedName("nama")
    @Expose
    private  String nama;

    @SerializedName("foto_mas")
    @Expose
    private String foto_mas;

    @SerializedName("foto")
    @Expose
    private String foto;

    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;

    @SerializedName("jamlapor")
    @Expose
    private String jamlapor;

    @SerializedName("latitude_laporan")
    @Expose
    private String latitudeLapor;

    @SerializedName("longtitude_laporan")
    @Expose
    private String longtitudeLapor;


    public String getFoto_mas() {
        return foto_mas;
    }

    public void setFoto_mas(String foto_mas) {
        this.foto_mas = foto_mas;
    }

    public String getJamlapor() {
        return jamlapor;
    }

    public void setJamlapor(String jamlapor) {
        this.jamlapor = jamlapor;
    }

    public String getIdLaporan() {
        return idLaporan;
    }

    public void setIdLaporan(String idLaporan) {
        this.idLaporan = idLaporan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public  String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getLatitudeLapor() {
        return latitudeLapor;
    }

    public void setLatitudeLapor(String latitudeLapor) {
        this.latitudeLapor = latitudeLapor;
    }

    public String getLongtitudeLapor() {
        return longtitudeLapor;
    }

    public void setLongtitudeLapor(String longtitudeLapor) {
        this.longtitudeLapor = longtitudeLapor;
    }



}
