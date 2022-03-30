package com.example.test;

import android.os.Bundle;

import com.example.test.fragment.TabsFragment;

import androidx.appcompat.app.AppCompatActivity;


public class TabActivity extends AppCompatActivity {


    private TabsFragment tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_tab);
        //初始化tabs Fragment组件
        tabs = (TabsFragment) getSupportFragmentManager().findFragmentById(R.id.footer_tab);
    }





}
