package com.example.kotaluwukcom.laporgorontalo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListLapor {

    @SerializedName("lapor")
    @Expose
    private ArrayList<LaporData> lapor = new ArrayList<>();

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("success")
    @Expose
    private Boolean success;


    public ArrayList<LaporData> getLapor() {
        return lapor;
    }

    public void setLapor(ArrayList<LaporData> lapor) {
        this.lapor = lapor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
