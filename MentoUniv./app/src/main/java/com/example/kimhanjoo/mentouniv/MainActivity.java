package com.example.kimhanjoo.mentouniv;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout mlayout;
    private FirebaseAuth mAuth;
    ListView list;
    ListViewAdapter adapter;
    EditText editsearch;
    String[] rank;
    String[] country;
    String[] population;

    Button btlogout;
    ArrayList<WorldPopulation> arraylist = new ArrayList<WorldPopulation>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mlayout = (RelativeLayout) findViewById(R.id.activity_main);
        mlayout.setBackgroundColor(Color.rgb(148,210,238));
        // Generate sample data
        rank = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };

        country = new String[] { "미분적분학", "물리및실험2", "전자기학",
                "미분방정식", "회로이", "미분방정식", "Nigeria", "물리전자공학",
                "공학수", "미분적분학" };

        population = new String[] { "어려움", "어려움",
                "쉬움", "보통", "어려움", "보통",
                "보통", "쉬움", "어려움", "보통" };

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        btlogout = (Button) findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();

        for (int i = 0; i < rank.length; i++)
        {
            WorldPopulation wp = new WorldPopulation(rank[i], country[i],
                    population[i]);
            // Binds all strings into an array
            arraylist.add(wp);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

        btlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Not using options menu in this tutorial

    private void signOut() {
        mAuth.signOut();
    }
}
