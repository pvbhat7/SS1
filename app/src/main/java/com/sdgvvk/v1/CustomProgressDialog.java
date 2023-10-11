package com.sdgvvk.v1;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(@NonNull Context context, String title) {
        super(context);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(params);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        View view = LayoutInflater.from(context).inflate(R.layout.loading_layout,null);
        if(title == null && title.isEmpty())
        view.findViewById(R.id.loaderText).setVisibility(View.GONE);
        else{
            view.findViewById(R.id.loaderText).setVisibility(View.VISIBLE);
            ((TextView)view.findViewById(R.id.loaderText)).setText(title);
        }
        setContentView(view);

    }
}
