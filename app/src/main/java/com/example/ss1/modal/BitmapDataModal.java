package com.example.ss1.modal;

import android.graphics.Bitmap;

public class BitmapDataModal {

    public Bitmap bitmap;
    public String profileId;

    public BitmapDataModal(Bitmap bitmap, String profileId) {
        this.bitmap = bitmap;
        this.profileId = profileId;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
}
