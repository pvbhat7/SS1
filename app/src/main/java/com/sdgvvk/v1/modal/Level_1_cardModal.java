package com.sdgvvk.v1.modal;

import java.util.Objects;

public class Level_1_cardModal {

    private String profileId;

    private String creationdate;
    private String name;

    private String city;
    private String firstname;
    private String lastname;

    private String followup_flag;
    private String noticount;

    private String mobile;

    private String mobile1;
    private String mobile2;
    private String mobile3;
    private String mobile4;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(String creationdate) {
        this.creationdate = creationdate;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getMobile3() {
        return mobile3;
    }

    public void setMobile3(String mobile3) {
        this.mobile3 = mobile3;
    }

    public String getMobile4() {
        return mobile4;
    }

    public void setMobile4(String mobile4) {
        this.mobile4 = mobile4;
    }

    public String getFollowup_flag() {
        return followup_flag;
    }

    public void setFollowup_flag(String followup_flag) {
        this.followup_flag = followup_flag;
    }

    public String getNoticount() {
        return noticount;
    }

    public void setNoticount(String noticount) {
        this.noticount = noticount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Level_1_cardModal that = (Level_1_cardModal) o;
        return Objects.equals(profileId, that.profileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId);
    }
}
