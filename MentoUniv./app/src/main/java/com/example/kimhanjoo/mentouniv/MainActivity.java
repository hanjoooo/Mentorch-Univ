package com.example.kimhanjoo.mentouniv;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout mlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mlayout = (RelativeLayout) findViewById(R.id.activity_main);
        mlayout.setBackgroundColor(Color.rgb(148,210,238));
    }
}
