package com.sdgvvk.v1.modal;

import java.util.List;

public class FcmNotificationModal {

    private String title;
    private String message;
    private String image;
    private String token;

    private String category;
    private String longtext;
    private String targetClass;
    private String notificationType;
    private String cpid;
    private String clientName;


    private String singleuser;
    private String filter;
    private String id;

    public FcmNotificationModal(String title, String message, String image, String category,
                                String longtext, String targetClass, String notificationType, String singleuser, String filter) {
        this.title = title;
        this.message = message;
        this.image = image;
        this.category = category;
        this.longtext = longtext;
        this.targetClass = targetClass;
        this.notificationType = notificationType;
        this.singleuser = singleuser;
        this.filter = filter;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    public String getNotificationtype() {
        return notificationType;
    }

    public void setNotificationtype(String notificationtype) {
        this.notificationType = notificationtype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCpid() {
        return cpid;
    }

    public void setCpid(String cpid) {
        this.cpid = cpid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSingleuser() {
        return singleuser;
    }

    public void setSingleuser(String singleuser) {
        this.singleuser = singleuser;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLongtext() {
        return longtext;
    }

    public void setLongtext(String longtext) {
        this.longtext = longtext;
    }
}
