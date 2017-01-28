package com.example.kimhanjoo.mentouniv;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileActivity extends AppCompatActivity {
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("users");

    TextView email;
    TextView name;
    TextView universe;
    TextView grade;
    TextView rpduf;
    TextView gkrrhk;
    Button home;
    DatabaseReference mchildRef;
    DatabaseReference mchild1Ref;
    DatabaseReference mchild2Ref;
    DatabaseReference mchild3Ref;
    DatabaseReference mchild4Ref;
    DatabaseReference mchild5Ref;
    DatabaseReference mchild6Ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        email = (TextView)findViewById(R.id.email);
        name = (TextView)findViewById(R.id.name);
        universe = (TextView)findViewById(R.id.universe);
        grade = (TextView)findViewById(R.id.grade);
        mAuth = FirebaseAuth.getInstance();
        home =(Button)findViewById(R.id.home);
        rpduf = (TextView)findViewById(R.id.rpduf);
        gkrrhk = (TextView)findViewById(R.id.gkrrhk);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                mchildRef = mConditionRef.child(user.getUid());
                mchild1Ref = mchildRef.child("이메일");
                mchild2Ref = mchildRef.child("이름");
                mchild3Ref = mchildRef.child("대학교");
                mchild4Ref = mchildRef.child("계열");
                mchild5Ref = mchildRef.child("분야");
                mchild6Ref = mchildRef.child("학년");

                mchild1Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String txt = dataSnapshot.getValue(String.class);
                        email.setText(txt);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
                mchild2Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String txt = dataSnapshot.getValue(String.class);
                        name.setText(txt);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
                mchild3Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String txt = dataSnapshot.getValue(String.class);
                        universe.setText(txt);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
                mchild4Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String txt = dataSnapshot.getValue(String.class);
                        rpduf.setText(txt);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
                mchild5Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String txt = dataSnapshot.getValue(String.class);
                        gkrrhk.setText(txt);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

                mchild6Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String txt = dataSnapshot.getValue(String.class);
                        grade.setText(txt);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        };

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });


    }
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
