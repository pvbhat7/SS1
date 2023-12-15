package com.sdgvvk.v1.modal;

public class FilterModal {

    public String cpid;

    public String vcpid;
    public String minAge;
    public String maxAge;
    public String minHeight;
    public String maxHeight;
    public String gender;

    public String education;

    public String occupation;

    public String city;
    public String lastname;

    public String caste;


    public FilterModal(String cpid, String minAge, String maxAge, String minHeight, String maxHeight, String gender, String education, String occupation, String city, String lastname, String caste) {
        this.cpid = cpid;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.gender = gender;
        this.education = education;
        this.occupation = occupation;
        this.city = city;
        this.lastname = lastname;
        this.caste = caste;
    }

    public FilterModal(String cpid, String minAge, String maxAge, String minHeight, String maxHeight, String gender) {
        this.cpid = cpid;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.gender = gender;
    }

    public FilterModal(String cpid, String vcpid, String gender) {
        this.cpid = cpid;
        this.vcpid = vcpid;
        this.gender = gender;
    }

    public String getCpid() {
        return cpid;
    }

    public void setCpid(String cpid) {
        this.cpid = cpid;
    }

    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public String getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(String minHeight) {
        this.minHeight = minHeight;
    }

    public String getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(String maxHeight) {
        this.maxHeight = maxHeight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getVcpid() {
        return vcpid;
    }

    public void setVcpid(String vcpid) {
        this.vcpid = vcpid;
    }
}
