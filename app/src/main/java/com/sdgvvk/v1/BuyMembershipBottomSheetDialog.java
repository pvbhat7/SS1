package com.sdgvvk.v1;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.sdgvvk.v1.api.ApiCallUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BuyMembershipBottomSheetDialog extends BottomSheetDialogFragment {

    Activity activity;


    public BuyMembershipBottomSheetDialog(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.buy_membership_bottom_sheet, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.bsmembershipRecyclerview);
        ApiCallUtil.getMembershipPlans(recyclerView , this.getActivity());


        return v;
    }
}
