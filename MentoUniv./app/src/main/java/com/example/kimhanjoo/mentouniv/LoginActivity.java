package com.example.kimhanjoo.mentouniv;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class LoginActivity extends AppCompatActivity {

    Button login;
    Button regist;
    ImageButton kakao;
    ImageButton facebook;
    private RelativeLayout mlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mlayout = (RelativeLayout) findViewById(R.id.activity_main);
        mlayout.setBackgroundColor(Color.rgb(148,210,238));

        login = (Button) findViewById(R.id.Login);
        regist = (Button) findViewById(R.id.Regist);
        kakao = (ImageButton)findViewById(R.id.kakaoLogin);
        facebook=(ImageButton)findViewById(R.id.facebookLogin);
        login.setBackgroundColor(Color.rgb(-215,109,156));
        regist.setBackgroundColor(Color.rgb(-215,109,156));

        kakao.setBackgroundColor(Color.rgb(148,210,238));
        facebook.setBackgroundColor(Color.rgb(148,210,238));

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegistActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
