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

import com.example.ss1.api.ApiCallUtil;
import com.example.ss1.modal.Customer;
import com.example.ss1.modal.FilterModal;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SearchProfileBottomSheetDialog extends BottomSheetDialogFragment {

    Activity activity;
    AutoCompleteTextView minHeight , maxHeight , minAge , maxAge , status , religion;

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

        status.setText("single");
        religion.setText("Hindu");
        minHeight.setText("4' 1\" | 124 cm");
        maxHeight.setText("7' 11\" | 242 cm");

        status.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.package_list_item, new String[]{"single", "married", "divorsed", "widowed"}));
        religion.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.package_list_item, new String[]{"Hindu", "Brahmin", "Muslim", "Cristian", "Sikh", "Jain"}));
        minHeight.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.package_list_item, new String[]{"4' 1\"  |  124 cm", "4' 2\"  |  127 cm", "4' 3\"  |  130 cm", "4' 4\"  |  132 cm", "4' 5\"  |  135 cm", "4' 6\"  |  138 cm", "4' 7\"  |  140 cm", "4' 8\"  |  143 cm", "4' 9\"  |  145 cm", "4' 10\"  |  148 cm", "4' 11\"  |  151 cm", "5'  |  152 cm", "5' 1\"  |  155 cm", "5' 2\"  |  157 cm", "5' 3\"  |  160 cm", "5' 4\"  |  163 cm", "5' 5\"  |  165 cm", "5' 6\"  |  168 cm", "5' 7\"  |  170 cm", "5' 8\"  |  173 cm", "5' 9\"  |  175 cm", "5' 10\"  |  178 cm", "5' 11\"  |  180 cm", "6'  |  183 cm", "6' 1\"  |  185 cm", "6' 2\"  |  188 cm", "6' 3\"  |  191 cm", "6' 4\"  |  193 cm", "6' 5\"  |  196 cm", "6' 6\"  |  198 cm", "6' 7\"  |  201 cm", "6' 8\"  |  203 cm", "6' 9\"  |  206 cm", "6' 10\"  |  208 cm", "6' 11\"  |  211 cm", "7'  |  213 cm", "7' 1\"  |  216 cm", "7' 2\"  |  218 cm", "7' 3\"  |  221 cm", "7' 4\"  |  224 cm", "7' 5\"  |  226 cm", "7' 6\"  |  229 cm", "7' 7\"  |  231 cm", "7' 8\"  |  234 cm", "7' 9\"  |  237 cm", "7' 10\"  |  239 cm", "7' 11\"  |  242 cm"}));
        maxHeight.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.package_list_item, new String[]{"4' 1\"  |  124 cm", "4' 2\"  |  127 cm", "4' 3\"  |  130 cm", "4' 4\"  |  132 cm", "4' 5\"  |  135 cm", "4' 6\"  |  138 cm", "4' 7\"  |  140 cm", "4' 8\"  |  143 cm", "4' 9\"  |  145 cm", "4' 10\"  |  148 cm", "4' 11\"  |  151 cm", "5'  |  152 cm", "5' 1\"  |  155 cm", "5' 2\"  |  157 cm", "5' 3\"  |  160 cm", "5' 4\"  |  163 cm", "5' 5\"  |  165 cm", "5' 6\"  |  168 cm", "5' 7\"  |  170 cm", "5' 8\"  |  173 cm", "5' 9\"  |  175 cm", "5' 10\"  |  178 cm", "5' 11\"  |  180 cm", "6'  |  183 cm", "6' 1\"  |  185 cm", "6' 2\"  |  188 cm", "6' 3\"  |  191 cm", "6' 4\"  |  193 cm", "6' 5\"  |  196 cm", "6' 6\"  |  198 cm", "6' 7\"  |  201 cm", "6' 8\"  |  203 cm", "6' 9\"  |  206 cm", "6' 10\"  |  208 cm", "6' 11\"  |  211 cm", "7'  |  213 cm", "7' 1\"  |  216 cm", "7' 2\"  |  218 cm", "7' 3\"  |  221 cm", "7' 4\"  |  224 cm", "7' 5\"  |  226 cm", "7' 6\"  |  229 cm", "7' 7\"  |  231 cm", "7' 8\"  |  234 cm", "7' 9\"  |  237 cm", "7' 10\"  |  239 cm", "7' 11\"  |  242 cm"}));

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
            Customer c = LocalCache.getLoggedInCustomer(this.getActivity());


            searchBtn.setEnabled(false);
            String maxHeight_ = "" , minHeight_  = "" , minAge_ = "" , maxAge_ = "",_status = "",_religion =  "",_gender = "";
            minAge_ = minAge.getText().toString().trim();
            maxAge_ = maxAge.getText().toString().trim();
            _status = status.getText().toString().trim();
            _religion = religion.getText().toString().trim();
            _gender = c.getGender().equalsIgnoreCase("male") ? "female" : "male";


            String[] parts1 = minHeight.getText().toString().trim().split("\\|");
            for (String part : parts1) {
                if (part.contains("cm")) {
                    String[] cmSplit = part.trim().split("\\s+");
                    minHeight_ = cmSplit[0];
                }
            }

            String[] parts2 = maxHeight.getText().toString().trim().split("\\|");
            for (String part : parts2) {
                if (part.contains("cm")) {
                    String[] cmSplit = part.trim().split("\\s+");
                    maxHeight_ = cmSplit[0];
                }
            }
            FilterModal modal = new FilterModal(c.getProfileId(),minAge_,maxAge_,minHeight_,maxHeight_,_status,_religion,_gender);
            ApiCallUtil.getFilteredLevel1Profiles(this.getActivity(),fragment,modal);
        });
    }

    private void init(View v){
        minHeight = v.findViewById(R.id.minHeight);
        maxHeight = v.findViewById(R.id.maxHeight);
        minAge = v.findViewById(R.id.minAge);
        maxAge = v.findViewById(R.id.maxAge);
        status = v.findViewById(R.id.status);
        religion = v.findViewById(R.id.religion);
        searchBtn = v.findViewById(R.id.searchBtn);
    }
}
