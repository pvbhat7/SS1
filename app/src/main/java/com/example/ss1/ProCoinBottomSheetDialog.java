package com.example.ss1;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ss1.api.ApiCallUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ProCoinBottomSheetDialog extends BottomSheetDialogFragment {

    Activity activity;


    public ProCoinBottomSheetDialog(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pro_coin_bottom_sheet, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.bsmembershipRecyclerview);
        ApiCallUtil.getMembershipPlans(recyclerView , this.getActivity());


        return v;
    }
}
