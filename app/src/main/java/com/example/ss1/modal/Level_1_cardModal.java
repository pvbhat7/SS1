package com.example.ss1.modal;

public class Level_1_cardModal {

    private String profileId;
    private String firstName;
    private String lastName;
    private String dob;
    private String age;

    private String isShortlisted;

    private String isLiked;

    private String isInterestsent;

    private String profilePhotoAddress;
    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getProfilePhotoAddress() {
        return profilePhotoAddress;
    }

    public void setProfilePhotoAddress(String profilePhotoAddress) {
        this.profilePhotoAddress = profilePhotoAddress;
    }


    public String getIsShortlisted() {
        return isShortlisted;
    }

    public void setIsShortlisted(String isShortlisted) {
        this.isShortlisted = isShortlisted;
    }

    public String getIsInterestsent() {
        return isInterestsent;
    }

    public void setIsInterestsent(String isInterestsent) {
        this.isInterestsent = isInterestsent;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public Level_1_cardModal(String profileId, String firstName, String lastName, String dob, String age, String profilePhotoAddress, String isShortlisted, String isInterestsent, String isLiked) {
        this.profileId = profileId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.age = age;
        this.profilePhotoAddress = profilePhotoAddress;
        this.isShortlisted = isShortlisted;
        this.isInterestsent = isInterestsent;
        this.isLiked = isLiked;
    }
}
