package com.sdgvvk.v1.modal;

public class Level_1_cardModal {

    private String profileId;
    private String firstname;
    private String lastname;

    private String mobile;

    private String gender;
    private String dob;
    private String age;

    private String isShortlisted;

    private String isLiked;

    private String isInterestsent;

    private String isViewed;

    private String profilephotoaddress;

    private String isAdmin;


    public Level_1_cardModal(String profileId, String firstname, String lastname, String dob, String age, String profilephotoaddress, String isShortlisted, String isInterestsent, String isLiked,String isAdmin) {
        this.profileId = profileId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.age = age;
        this.profilephotoaddress = profilephotoaddress;
        this.isShortlisted = isShortlisted;
        this.isInterestsent = isInterestsent;
        this.isLiked = isLiked;
        this.isAdmin = isAdmin;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getIsShortlisted() {
        return isShortlisted;
    }

    public void setIsShortlisted(String isShortlisted) {
        this.isShortlisted = isShortlisted;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public String getIsInterestsent() {
        return isInterestsent;
    }

    public void setIsInterestsent(String isInterestsent) {
        this.isInterestsent = isInterestsent;
    }

    public String getProfilephotoaddress() {
        return profilephotoaddress;
    }

    public void setProfilephotoaddress(String profilephotoaddress) {
        this.profilephotoaddress = profilephotoaddress;
    }

    public String getIsViewed() {
        return isViewed;
    }

    public void setIsViewed(String isViewed) {
        this.isViewed = isViewed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIsAdmin() {
        return isAdmin ;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }
}
