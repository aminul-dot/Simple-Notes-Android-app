package com.letsseetech.data.simplenotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
Toolbar Myappbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Myappbar = findViewById(R.id.myappbar);
        setSupportActionBar(Myappbar);
        addFragment();
    }

    private void addFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        GloginFragment sampleFragment=new GloginFragment();
        fragmentTransaction.add(R.id.fragmentContainer,sampleFragment);
        fragmentTransaction.commit();
    }




}