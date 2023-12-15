package com.sdgvvk.v1;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sdgvvk.v1.api.ApiCallUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BuyMembershipBottomSheetDialog extends BottomSheetDialogFragment {

    Activity activity;
    public BuyMembershipBottomSheetDialog() {

    }

    public BuyMembershipBottomSheetDialog(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.buy_membership_bottom_sheet, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.bsmembershipRecyclerview);

        CardView membership_card = v.findViewById(R.id.membership_card);

        membership_card.setVisibility(View.VISIBLE);
        ObjectAnimator anim = null;
        anim = ObjectAnimator.ofInt(membership_card, "backgroundColor", Color.YELLOW, Color.WHITE);

        //linearLayout.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        anim.setDuration(1500);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();

        membership_card.setOnClickListener(view -> Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            String adminPhone = LocalCache.getAdminPhone(activity);
                            if(adminPhone.isEmpty() || adminPhone == "")
                                callIntent.setData(Uri.parse("tel:+917972864487" ));
                            else
                                callIntent.setData(Uri.parse("tel:+91"+adminPhone ));
                            activity.startActivity(callIntent);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check());

        ApiCallUtil.getMembershipPlans(recyclerView , this.getActivity());


        return v;
    }
}
