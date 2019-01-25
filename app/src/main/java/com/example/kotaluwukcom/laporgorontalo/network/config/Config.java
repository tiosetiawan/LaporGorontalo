package com.example.kotaluwukcom.laporgorontalo.network.config;

public class Config {


    public static final String BASE_URL = "http://192.168.1.15"; // Your Local IP Address

    public static final String API_URL_FOTO_MAS = BASE_URL + "/laporgorontalorev/";

    public static final String API_URL = BASE_URL + "/laporgorontalorev";

    public static final String API_URL_FOTO_LAP = API_URL + "/fotolap/";

    public static final String API_UP_FOTO_MAS = API_URL + "/uploadfotomas.php";
    public static final String API_LOGIN = API_URL + "/masuk.php";
    public static final String API_REGISTER = API_URL + "/daftar.php";
    public static final String API_UPDATE = API_URL + "/profileedit.php";
    public static final String API_UPDATE_PASS = API_URL + "/editpass.php";
    public static final String API_READ_MAS = API_URL + "/read_mas.php";

    public static final String KEY_EMP_ID            ="id";
    public static final String KEY_EMP_NAMA          ="nama";
    public static final String KEY_EMP_UMUR          ="umur";
    public static final String KEY_EMP_KERJA         ="pekerjaan";
    public static final String KEY_EMP_ALAMAT        ="alamat";
    public static final String KEY_EMP_NO            ="notelp";
    public static final String KEY_EMP_EMAIl         ="email";

}
