package com.example.ss1.activity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.example.ss1.LocalCache;
import com.example.ss1.R;
import com.example.ss1.api.Api;
import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.modal.Customer;
import com.example.ss1.modal.FilterModal;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProfileExportActivity extends AppCompatActivity {

    public static List<Customer> temp_level2list = new ArrayList<>();

    TextView mode, profilefoundtext;
    AutoCompleteTextView minHeight, maxHeight, minAge, maxAge, gender;
    Button searchBtn, exportBtn;
    LinearLayout filterlayout, resultlayout;
    CardView filter_card;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_export_dialog);
        //setContentView(R.layout.export_profile_list_item);

        init();
        onclickListeners();


    }

    private void onclickListeners() {

        filter_card.setOnClickListener(view -> {
            searchBtn.setEnabled(true);
            resultlayout.setVisibility(View.GONE);
            if (mode.getText().toString().trim().equalsIgnoreCase("opened")) {
                filterlayout.setVisibility(View.GONE);
                mode.setText("closed");
            } else {
                mode.setText("opened");
                filterlayout.setVisibility(View.VISIBLE);
            }

        });


        searchBtn.setOnClickListener(view -> {
            searchBtn.setEnabled(false);
            String maxHeight_ = "", minHeight_ = "", minAge_ = "", maxAge_ = "", gender_ = "";
            minAge_ = minAge.getText().toString().trim();
            maxAge_ = maxAge.getText().toString().trim();
            gender_ = gender.getText().toString().trim();
            Customer c = LocalCache.getLoggedInCustomer(ProfileExportActivity.this);
            String[] parts1 = minHeight.getText().toString().trim().split("\\|");
            String[] parts2 = maxHeight.getText().toString().trim().split("\\|");

            for (String part : parts1) {
                if (part.contains("cm")) {
                    String[] cmSplit = part.trim().split("\\s+");
                    minHeight_ = cmSplit[0];
                }
            }
            for (String part : parts2) {
                if (part.contains("cm")) {
                    String[] cmSplit = part.trim().split("\\s+");
                    maxHeight_ = cmSplit[0];
                }
            }

            FilterModal modal = new FilterModal(c.getProfileId(), minAge_, maxAge_, minHeight_, maxHeight_, gender_);
            ApiCallUtil.getFilteredLevel2Profiles(ProfileExportActivity.this, modal);

        });
        exportBtn.setOnClickListener(view -> exportProfiles());
    }

    public void init() {
        resultlayout = findViewById(R.id.resultlayout);
        exportBtn = findViewById(R.id.exportBtn);
        profilefoundtext = findViewById(R.id.profilefoundtext);
        filterlayout = findViewById(R.id.filterlayout);
        mode = findViewById(R.id.mode);
        filter_card = findViewById(R.id.filter_card);
        gender = findViewById(R.id.gender);
        minHeight = findViewById(R.id.minHeight);
        maxHeight = findViewById(R.id.maxHeight);
        minAge = findViewById(R.id.minAge);
        maxAge = findViewById(R.id.maxAge);
        searchBtn = findViewById(R.id.searchBtn);

        mode.setText("opened");
        gender.setText("female");
        String[] genderArray = {"female", "male"};

        gender.setAdapter(new ArrayAdapter(this, R.layout.package_list_item, genderArray));

        String[] heightArray = {"4 feet | 121 cm", "4 feet 1 inch | 124 cm", "4 feet 2 inches | 127 cm", "4 feet 3 inches | 130 cm", "4 feet 4 inches | 132 cm", "4 feet 5 inches | 135 cm", "4 feet 6 inches | 138 cm", "4 feet 7 inches | 140 cm", "4 feet 8 inches | 143 cm", "4 feet 9 inches | 145 cm", "4 feet 10 inches | 148 cm", "4 feet 11 inches | 151 cm", "5 feet | 152 cm", "5 feet 1 inch | 155 cm", "5 feet 2 inches | 157 cm", "5 feet 3 inches | 160 cm", "5 feet 4 inches | 163 cm", "5 feet 5 inches | 165 cm", "5 feet 6 inches | 168 cm", "5 feet 7 inches | 170 cm", "5 feet 8 inches | 173 cm", "5 feet 9 inches | 175 cm", "5 feet 10 inches | 178 cm", "5 feet 11 inches | 180 cm", "6 feet | 183 cm", "6 feet 1 inch | 185 cm", "6 feet 2 inches | 188 cm", "6 feet 3 inches | 191 cm", "6 feet 4 inches | 193 cm", "6 feet 5 inches | 196 cm", "6 feet 6 inches | 198 cm", "6 feet 7 inches | 201 cm", "6 feet 8 inches | 203 cm", "6 feet 9 inches | 206 cm", "6 feet 10 inches | 208 cm", "6 feet 11 inches | 211 cm", "7 feet | 213 cm", "7 feet 1 inch | 216 cm", "7 feet 2 inches | 218 cm", "7 feet 3 inches | 221 cm", "7 feet 4 inches | 224 cm", "7 feet 5 inches | 226 cm", "7 feet 6 inches | 229 cm", "7 feet 7 inches | 231 cm", "7 feet 8 inches | 234 cm", "7 feet 9 inches | 237 cm", "7 feet 10 inches | 239 cm", "7 feet 11 inches | 242 cm"};
        minHeight.setText("4 feet | 121 cm");
        maxHeight.setText("7 feet 11 inches | 242 cm");
        minHeight.setAdapter(new ArrayAdapter(this, R.layout.package_list_item, heightArray));
        maxHeight.setAdapter(new ArrayAdapter(this, R.layout.package_list_item, heightArray));

        String[] ageArray = new String[38]; // Since you want to fill it till 55 (55 - 18 + 1)

        for (int i = 18; i <= 55; i++) {
            ageArray[i - 18] = Integer.toString(i);
        }
        minAge.setText("18");
        maxAge.setText("70");
        minAge.setAdapter(new ArrayAdapter(this, R.layout.package_list_item, ageArray));
        maxAge.setAdapter(new ArrayAdapter(this, R.layout.package_list_item, ageArray));
    }

    public void exportProfiles() {
        exportBtn.setEnabled(false);
        findViewById(R.id.export_parentlayout).setVisibility(View.GONE);
        //runOnUiThread(() -> dynamicLayoutCreation());
        //dynamicLayoutCreation();
        ApiCallUtil.counter = 0;
        ApiCallUtil.dynamicLayoutCreation(this);
    }

    public void dynamicLayoutCreation() {
        try {
// Assuming you have a list of profile objects like this
            List<Customer> list = temp_level2list;

            // Get a reference to the parent layout where you want to add profile items
            LinearLayout parentLayout = findViewById(R.id.exportview_view);

            // Get a reference to the LayoutInflater
            LayoutInflater inflater = LayoutInflater.from(this);

            // Loop through the profiles and populate the layouts
            for (Customer obj : list) {
                // Inflate the profile item layout
                View v = inflater.inflate(R.layout.export_profile_list_item, null);

                // TODO: 21-Sep-23 fill data
                Glide.with(this)
                        .load(obj.getProfilephotoaddress())
                        .placeholder(R.drawable.oops)
                        .into((ImageView) v.findViewById(R.id.profilephotoaddresss));
                ((TextView) v.findViewById(R.id.profileid)).setText("A"+obj.getProfileId());
                ((TextView) v.findViewById(R.id.name)).setText(obj.getFirstname() + " " + obj.getLastname());
                ((TextView) v.findViewById(R.id.birthdate)).setText(obj.getBirthdate());
                ((TextView) v.findViewById(R.id.birthtime)).setText(obj.getBirthtime());
                ((TextView) v.findViewById(R.id.birthplace)).setText(obj.getBirthplace());
                ((TextView) v.findViewById(R.id.height)).setText(obj.getHeight());
                ((TextView) v.findViewById(R.id.bloodgroup)).setText(obj.getBloodgroup());
                ((TextView) v.findViewById(R.id.zodiac)).setText(obj.getZodiac());
                ((TextView) v.findViewById(R.id.education)).setText(obj.getEducation());
                ((TextView) v.findViewById(R.id.occupation)).setText(obj.getOccupation());
                ((TextView) v.findViewById(R.id.religion)).setText(obj.getReligion());
                ((TextView) v.findViewById(R.id.caste)).setText(obj.getCaste());
                ((TextView) v.findViewById(R.id.marriagestatus)).setText(obj.getMarriagestatus());
                ((TextView) v.findViewById(R.id.fathername)).setText(obj.getFathername());
                ((TextView) v.findViewById(R.id.mothername)).setText(obj.getMothername());
                ((TextView) v.findViewById(R.id.relatives)).setText(obj.getRelatives());
                ((TextView) v.findViewById(R.id.family)).setText(obj.getFamily());
                ((TextView) v.findViewById(R.id.expectations)).setText(obj.getExpectations());


                // Add the inflated layout to the parent layout
                parentLayout.addView(v);

                // Measure and layout the view
                v.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                );
                v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

                // Create a Bitmap with the same dimensions as the view
                Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);

                // Create a Canvas to draw the view onto the Bitmap
                Canvas canvas = new Canvas(bitmap);

                // Draw the view onto the Canvas
                v.draw(canvas);


                // Assuming you have a Bitmap object named 'bitmap' containing the generated image
// and 'obj' is your profile object

// Define the filename
                String filename = "profile_" + obj.getProfileId() + ".png";

// Get the content resolver
                ContentResolver resolver = getContentResolver();

// Define the image details
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

// Define the selection criteria to find an existing image with the same filename
                String selection = MediaStore.Images.Media.DISPLAY_NAME + "=?";
                String[] selectionArgs = new String[]{filename};

// Check if an image with the same filename already exists
                Cursor cursor = resolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null,
                        selection,
                        selectionArgs,
                        null
                );

                if (cursor != null && cursor.moveToFirst()) {
                    // An image with the same filename exists, so update it

                    // Get the index of the _ID column
                    int idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);

                    if (idColumnIndex >= 0) {
                        // Get the ID of the existing image
                        long existingImageId = cursor.getLong(idColumnIndex);
                        Uri existingImageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, existingImageId);

                        // Update the image in the MediaStore
                        resolver.update(existingImageUri, values, null, null);
                    } else {
                        // Handle the case where the _ID column index is invalid or not found
                        // You can choose to log an error or handle this situation as needed
                    }

                    // Close the cursor
                    cursor.close();
                } else {
                    // No image with the same filename found, so insert the new image

                    // Insert the image into the MediaStore
                    Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    if (imageUri != null) {
                        // Open an output stream to write the Bitmap to the MediaStore
                        OutputStream outputStream = resolver.openOutputStream(imageUri);

                        // Compress the Bitmap as a PNG image and write it to the output stream
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                        // Close the output stream
                        outputStream.close();
                    }
                }

// Now, the image with the specified filename has either been updated or inserted in the MediaStore


            }
        } catch (IOException e) {
            Log.i("local_logs", "DynamicLayoutCreationTask " + e.toString());
        }
    }
}
