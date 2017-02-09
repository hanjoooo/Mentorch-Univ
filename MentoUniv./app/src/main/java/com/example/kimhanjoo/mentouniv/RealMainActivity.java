package com.example.kimhanjoo.mentouniv;

import android.app.ActivityGroup;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;


public class RealMainActivity extends ActivityGroup {
    private BackPressCloseHandler backPressCloseHandler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_main);
        createTab();


        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    private void createTab() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup(getLocalActivityManager());

        tabHost.addTab(tabHost.newTabSpec("TAB1").setIndicator("홈")
                .setContent(new Intent(this, MainActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("TAB2").setIndicator("게시판")
                .setContent(new Intent(this, CommunityActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("TAB3").setIndicator("마이페이지")
                .setContent(new Intent(this, MypageActivity.class)));



    }
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }









}