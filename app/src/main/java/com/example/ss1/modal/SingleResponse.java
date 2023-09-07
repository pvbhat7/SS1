package com.example.ss1.modal;

import com.google.gson.annotations.SerializedName;

public class SingleResponse {

    @SerializedName("result")
    public String result;

    public SingleResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
