package com.sdgvvk.v1.modal;

public class FilterModal {

    public String cpid;
    public String minAge;
    public String maxAge;
    public String minHeight;
    public String maxHeight;
    public String gender;

    public String marriagestatus;

    public String religion;

    public FilterModal(String cpid, String minAge, String maxAge, String minHeight, String maxHeight,String marriagestatus,String religion,String gender) {
        this.cpid = cpid;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.marriagestatus = marriagestatus;
        this.religion = religion;
        this.gender = gender;
    }

    public FilterModal(String cpid, String minAge, String maxAge, String minHeight, String maxHeight,String gender) {
        this.cpid = cpid;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
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

    @Override
    public String toString() {
        return "FilterModal{" +
                "cpid='" + cpid + '\'' +
                ", minAge='" + minAge + '\'' +
                ", maxAge='" + maxAge + '\'' +
                ", minHeight='" + minHeight + '\'' +
                ", maxHeight='" + maxHeight + '\'' +
                '}';
    }
}
