package com.sdgvvk.v1.modal;

public class CustomerActivityModal {
    public CustomerActivityModal() {
    }

    private String liked;
    private String shortlist;
    private String interestsSent;
    private String ignored;
    private String contactViewed;

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getShortlist() {
        return shortlist;
    }

    public void setShortlist(String shortlist) {
        this.shortlist = shortlist;
    }

    public String getInterestsSent() {
        return interestsSent;
    }

    public void setInterestsSent(String interestsSent) {
        this.interestsSent = interestsSent;
    }

    public String getIgnored() {
        return ignored;
    }

    public void setIgnored(String ignored) {
        this.ignored = ignored;
    }

    public String getContactViewed() {
        return contactViewed;
    }

    public void setContactViewed(String contactViewed) {
        this.contactViewed = contactViewed;
    }
}
