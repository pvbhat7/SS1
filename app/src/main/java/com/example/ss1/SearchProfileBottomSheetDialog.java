package com.example.ss1;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.api.ApiUtils;
import com.example.ss1.modal.Customer;
import com.example.ss1.modal.FilterModal;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class SearchProfileBottomSheetDialog extends BottomSheetDialogFragment {

    Activity activity;
    AutoCompleteTextView minHeight , maxHeight , minAge , maxAge;

    Button searchBtn;
    Fragment fragment;


    public SearchProfileBottomSheetDialog(Activity activity, Fragment fragment) {
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_profile_bottom_sheet, container, false);

        init(v);
        onclickListeners();

        String[] heightArray = {"4 feet | 121 cm", "4 feet 1 inch | 124 cm", "4 feet 2 inches | 127 cm", "4 feet 3 inches | 130 cm", "4 feet 4 inches | 132 cm", "4 feet 5 inches | 135 cm", "4 feet 6 inches | 138 cm", "4 feet 7 inches | 140 cm", "4 feet 8 inches | 143 cm", "4 feet 9 inches | 145 cm", "4 feet 10 inches | 148 cm", "4 feet 11 inches | 151 cm", "5 feet | 152 cm", "5 feet 1 inch | 155 cm", "5 feet 2 inches | 157 cm", "5 feet 3 inches | 160 cm", "5 feet 4 inches | 163 cm", "5 feet 5 inches | 165 cm", "5 feet 6 inches | 168 cm", "5 feet 7 inches | 170 cm", "5 feet 8 inches | 173 cm", "5 feet 9 inches | 175 cm", "5 feet 10 inches | 178 cm", "5 feet 11 inches | 180 cm", "6 feet | 183 cm", "6 feet 1 inch | 185 cm", "6 feet 2 inches | 188 cm", "6 feet 3 inches | 191 cm", "6 feet 4 inches | 193 cm", "6 feet 5 inches | 196 cm", "6 feet 6 inches | 198 cm", "6 feet 7 inches | 201 cm", "6 feet 8 inches | 203 cm", "6 feet 9 inches | 206 cm", "6 feet 10 inches | 208 cm", "6 feet 11 inches | 211 cm", "7 feet | 213 cm", "7 feet 1 inch | 216 cm", "7 feet 2 inches | 218 cm", "7 feet 3 inches | 221 cm", "7 feet 4 inches | 224 cm", "7 feet 5 inches | 226 cm", "7 feet 6 inches | 229 cm", "7 feet 7 inches | 231 cm", "7 feet 8 inches | 234 cm", "7 feet 9 inches | 237 cm", "7 feet 10 inches | 239 cm", "7 feet 11 inches | 242 cm"};
        minHeight.setText("4 feet | 121 cm");
        maxHeight.setText("7 feet 11 inches | 242 cm");
        minHeight.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.package_list_item, heightArray));
        maxHeight.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.package_list_item, heightArray));

        String[] ageArray = new String[38]; // Since you want to fill it till 55 (55 - 18 + 1)

        for (int i = 18; i <= 55; i++) {
            ageArray[i - 18] = Integer.toString(i);
        }
        minAge.setText("18");
        maxAge.setText("70");
        minAge.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.package_list_item, ageArray));
        maxAge.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.package_list_item, ageArray));


        return v;
    }

    private void onclickListeners() {
        searchBtn.setOnClickListener(view -> {
            searchBtn.setEnabled(false);
            String maxHeight_ = "" , minHeight_  = "" , minAge_ = "" , maxAge_ = "";
            minAge_ = minAge.getText().toString().trim();
            maxAge_ = maxAge.getText().toString().trim();
            Customer c = LocalCache.retrieveLoggedInCustomer(this.getActivity());
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
            FilterModal modal = new FilterModal(c.getProfileId(),minAge_,maxAge_,minHeight_,maxHeight_);
            ApiCallUtil.filterProfiles(this.getActivity(),fragment,modal);
        });
    }

    private void init(View v){
        minHeight = v.findViewById(R.id.minHeight);
        maxHeight = v.findViewById(R.id.maxHeight);
        minAge = v.findViewById(R.id.minAge);
        maxAge = v.findViewById(R.id.maxAge);
        searchBtn = v.findViewById(R.id.searchBtn);
    }
}
