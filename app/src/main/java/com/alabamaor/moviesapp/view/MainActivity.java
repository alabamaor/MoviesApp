package com.alabamaor.moviesapp.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.alabamaor.moviesapp.R;

public class MainActivity extends AppCompatActivity {

    public static void start(AppCompatActivity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }

    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("");
        mNavController = Navigation.findNavController(this, R.id.host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController);


    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(mNavController, (DrawerLayout) null);
    }


}