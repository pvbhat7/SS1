package com.sdgvvk.v1.modal;

public class FcmTokenModal {

    private String id;
    private String cpid;
    private String token;
    private String deviceid;

    private String appversion;



    public FcmTokenModal(String cpid, String token,String deviceid,String appversion) {
        this.cpid = cpid;
        this.token = token;
        this.deviceid = deviceid;
        this.appversion = appversion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCpid() {
        return cpid;
    }

    public void setCpid(String cpid) {
        this.cpid = cpid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }
}
