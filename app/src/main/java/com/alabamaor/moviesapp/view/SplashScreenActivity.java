package com.alabamaor.moviesapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.alabamaor.moviesapp.R;
import com.alabamaor.moviesapp.viewModel.SplashScreenViewModel;

public class SplashScreenActivity extends AppCompatActivity {

    private SplashScreenViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mViewModel = new ViewModelProvider(this).get(SplashScreenViewModel.class);
        mViewModel.init();
        observeData();
    }
    private void observeData() {
        mViewModel.getIsLoadingDone().observe(this, isFinish -> {
            if (isFinish != null) {
                if (isFinish){
                    MainActivity.start(this);
               }
            }
        });

    }

}