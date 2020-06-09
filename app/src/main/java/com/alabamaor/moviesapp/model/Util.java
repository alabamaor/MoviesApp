package com.alabamaor.moviesapp.model;

import android.content.Context;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

public class Util {

    public static CircularProgressDrawable getProgressDrawable(Context context) {
        CircularProgressDrawable cpd = new CircularProgressDrawable(context);
        cpd.setStrokeWidth(10f);
        cpd.setCenterRadius(50f);
        cpd.start();
        return cpd;
    }

}
