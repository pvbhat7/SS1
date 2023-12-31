package com.sdgvvk.v1.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.sdgvvk.v1.LocalCache;
import com.sdgvvk.v1.R;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.sdgvvk.v1.modal.Customer;
import com.sdgvvk.v1.modal.FilterModal;

import java.util.ArrayList;
import java.util.List;

public class ProfileExportActivity extends AppCompatActivity {

    public static List<Customer> temp_level2list = new ArrayList<>();

    TextView mode, profilefoundtext;
    AutoCompleteTextView minHeight, maxHeight, minAge, maxAge, gender;
    Button searchBtn, exportBtn,savetophoneBtn;
    LinearLayout filterlayout, resultlayout;
    CardView filter_card;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_export);

        /*List<Level_1_cardModal> list = LocalCache.getLevel1List(this);
        for(Level_1_cardModal obj : list){
            String b64 = convertImageToBase64(obj.getProfilephotoaddress());
            Drawable drawable = HelperUtils.convertBitmapToDrawable(this, HelperUtils.convertBase64ToBitmap(b64));
        }*/
        /*Glide.with(activity)
                .load(HelperUtils.convertBitmapToDrawable(activity,HelperUtils.convertBase64ToBitmap(b64)))
                .placeholder(HelperUtils.convertBitmapToDrawable(activity,HelperUtils.convertBase64ToBitmap(b64)))
                .into((ImageView) view.findViewById(R.id.profilephotoaddresss));*/
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
            ApiCallUtil.blist = new ArrayList<>();
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
        savetophoneBtn.setOnClickListener(view -> {
            savetophoneBtn.setEnabled(false);
            ApiCallUtil.persistBitmap(this);
        });
    }

    public void init() {
        resultlayout = findViewById(R.id.resultlayout);
        exportBtn = findViewById(R.id.exportBtn);
        savetophoneBtn = findViewById(R.id.savetophoneBtn);
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

        gender.setAdapter(new ArrayAdapter(this, R.layout.day_dropdown_list_item, genderArray));

        String[] heightArray = {"4 feet | 121 cm", "4 feet 1 inch | 124 cm", "4 feet 2 inches | 127 cm", "4 feet 3 inches | 130 cm", "4 feet 4 inches | 132 cm", "4 feet 5 inches | 135 cm", "4 feet 6 inches | 138 cm", "4 feet 7 inches | 140 cm", "4 feet 8 inches | 143 cm", "4 feet 9 inches | 145 cm", "4 feet 10 inches | 148 cm", "4 feet 11 inches | 151 cm", "5 feet | 152 cm", "5 feet 1 inch | 155 cm", "5 feet 2 inches | 157 cm", "5 feet 3 inches | 160 cm", "5 feet 4 inches | 163 cm", "5 feet 5 inches | 165 cm", "5 feet 6 inches | 168 cm", "5 feet 7 inches | 170 cm", "5 feet 8 inches | 173 cm", "5 feet 9 inches | 175 cm", "5 feet 10 inches | 178 cm", "5 feet 11 inches | 180 cm", "6 feet | 183 cm", "6 feet 1 inch | 185 cm", "6 feet 2 inches | 188 cm", "6 feet 3 inches | 191 cm", "6 feet 4 inches | 193 cm", "6 feet 5 inches | 196 cm", "6 feet 6 inches | 198 cm", "6 feet 7 inches | 201 cm", "6 feet 8 inches | 203 cm", "6 feet 9 inches | 206 cm", "6 feet 10 inches | 208 cm", "6 feet 11 inches | 211 cm", "7 feet | 213 cm", "7 feet 1 inch | 216 cm", "7 feet 2 inches | 218 cm", "7 feet 3 inches | 221 cm", "7 feet 4 inches | 224 cm", "7 feet 5 inches | 226 cm", "7 feet 6 inches | 229 cm", "7 feet 7 inches | 231 cm", "7 feet 8 inches | 234 cm", "7 feet 9 inches | 237 cm", "7 feet 10 inches | 239 cm", "7 feet 11 inches | 242 cm"};
        minHeight.setText("4 feet | 121 cm");
        maxHeight.setText("7 feet 11 inches | 242 cm");
        minHeight.setAdapter(new ArrayAdapter(this, R.layout.day_dropdown_list_item, heightArray));
        maxHeight.setAdapter(new ArrayAdapter(this, R.layout.day_dropdown_list_item, heightArray));

        String[] ageArray = new String[38]; // Since you want to fill it till 55 (55 - 18 + 1)

        for (int i = 18; i <= 55; i++) {
            ageArray[i - 18] = Integer.toString(i);
        }
        minAge.setText("18");
        maxAge.setText("70");
        minAge.setAdapter(new ArrayAdapter(this, R.layout.day_dropdown_list_item, ageArray));
        maxAge.setAdapter(new ArrayAdapter(this, R.layout.day_dropdown_list_item, ageArray));
    }

    public void exportProfiles() {
        exportBtn.setEnabled(false);
        findViewById(R.id.export_parentlayout).setVisibility(View.GONE);
        //runOnUiThread(() -> dynamicLayoutCreation());
        //dynamicLayoutCreation();

        List<Customer> list = ProfileExportActivity.temp_level2list;
        LinearLayout parentLayout = findViewById(R.id.exportview_view);
        LayoutInflater inflater = LayoutInflater.from(this);

        View v = inflater.inflate(R.layout.export_profile_list_item, parentLayout, false);
        parentLayout.addView(v);
        for(Customer c : list){
            List<Customer> cl = new ArrayList<>();
            cl.add(c);
            ApiCallUtil.dynamicLayoutCreation(this,cl,v);
        }

    }

}
