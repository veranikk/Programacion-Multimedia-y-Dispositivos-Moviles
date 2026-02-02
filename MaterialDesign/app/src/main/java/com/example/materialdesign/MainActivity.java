package com.example.materialdesign;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.example.materialdesign.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new fragment_pestana1(), "Pestaña 1");
        adapter.addFragment(new fragment_pestana2(), "Pestaña 2");
        adapter.addFragment(new fragment_pestana3(), "Pestaña 3");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                int position = tab.getPosition();
                Toast.makeText(MainActivity.this, "Pestaña "+
                        (position + 1)+ "seleccionada", Toast.LENGTH_SHORT).show();
            }

            public void onTabUnselected(TabLayout.Tab tab){
                int position = tab.getPosition();
                Toast.makeText(MainActivity.this, "Pestaña "+
                        (position + 1)+ "seleccionada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Toast.makeText(MainActivity.this, "Pestaña "+
                        (position + 2)+ "seleccionada", Toast.LENGTH_SHORT).show();
            }
        });

    }



}