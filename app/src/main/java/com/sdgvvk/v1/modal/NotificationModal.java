package com.sdgvvk.v1.modal;

public class NotificationModal {

    private int id;
    private String cpid;
    private String vcpid;
    private String action_id;
    private String is_viewed;
    private String updateDate;

    private String photo;
    private String title;
    private String time;
    public NotificationModal(String cpid , String vcpid , String action_id) {
        this.cpid = cpid;
        this.vcpid = vcpid;
        this.action_id = action_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCpid() {
        return cpid;
    }

    public void setCpid(String cpid) {
        this.cpid = cpid;
    }

    public String getVcpid() {
        return vcpid;
    }

    public void setVcpid(String vcpid) {
        this.vcpid = vcpid;
    }

    public String getAction_id() {
        return action_id;
    }

    public void setAction_id(String action_id) {
        this.action_id = action_id;
    }

    public String getIs_viewed() {
        return is_viewed;
    }

    public void setIs_viewed(String is_viewed) {
        this.is_viewed = is_viewed;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
