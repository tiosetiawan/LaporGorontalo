package com.example.kotaluwukcom.laporgorontalo.model;

import java.util.List;

public class BaseResponse {

    private boolean error;
    private String message;
    String value;
    List<Result>result;

    public BaseResponse() {
    }

    public String getValue() {
        return value;
    }

    public List<Result> getResult() {
        return result;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
