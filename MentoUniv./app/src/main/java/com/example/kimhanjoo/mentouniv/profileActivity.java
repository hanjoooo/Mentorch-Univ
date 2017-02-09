package com.example.kimhanjoo.mentouniv;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class profileActivity extends BaseActivity {
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private RelativeLayout mlayout;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("users");

    TextView nickname;
    TextView name;
    TextView universe;
    TextView grade;
    TextView rpduf;
    TextView gkrrhk;

    TextView ednickname;
    TextView edname;
    TextView eduniverse;
    TextView edrpduf;
    TextView edgkrrhk;
    TextView edgrade;


    Button btchange;
    DatabaseReference mchildRef;
    DatabaseReference mchild1Ref;
    DatabaseReference mchild2Ref;
    DatabaseReference mchild3Ref;
    DatabaseReference mchild4Ref;
    DatabaseReference mchild5Ref;
    DatabaseReference mchild6Ref;

    String compare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nickname = (TextView)findViewById(R.id.nickname);
        name = (TextView)findViewById(R.id.name);
        universe = (TextView)findViewById(R.id.universe);
        grade = (TextView)findViewById(R.id.grade);
        mAuth = FirebaseAuth.getInstance();
        rpduf = (TextView)findViewById(R.id.rpduf);
        gkrrhk = (TextView)findViewById(R.id.gkrrhk);

        ednickname=(TextView)findViewById(R.id.ednickname);
        edname=(TextView)findViewById(R.id.edname);
        eduniverse =(TextView)findViewById(R.id.eduniverse);
        edrpduf = (TextView)findViewById(R.id.edrpduf);
        edgkrrhk=(TextView)findViewById(R.id.edgkrrhk);
        edgrade=(TextView)findViewById(R.id.edgrade);

        btchange =(Button)findViewById(R.id.change);

        mlayout = (RelativeLayout) findViewById(R.id.activity_profile);
        mlayout.setBackgroundColor(Color.rgb(148,210,238));

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                mchildRef = mConditionRef.child(user.getUid());
                mchild1Ref = mchildRef.child("닉네임");
                mchild2Ref = mchildRef.child("이름");
                mchild3Ref = mchildRef.child("대학교");
                mchild4Ref = mchildRef.child("계열");
                mchild5Ref = mchildRef.child("분야");
                mchild6Ref = mchildRef.child("학년");

                mchild1Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String txt = dataSnapshot.getValue(String.class);
                        ednickname.setText(txt);
                        compare=txt;
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
                mchild2Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String txt = dataSnapshot.getValue(String.class);
                        edname.setText(txt);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
                mchild3Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String txt = dataSnapshot.getValue(String.class);
                        eduniverse.setText(txt);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
                mchild4Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String txt = dataSnapshot.getValue(String.class);
                        edrpduf.setText(txt);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
                mchild5Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String txt = dataSnapshot.getValue(String.class);
                        edgkrrhk.setText(txt);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

                mchild6Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String txt = dataSnapshot.getValue(String.class);
                        edgrade.setText(txt);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

                btchange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*
                        showProgressDialog();
                        mRootRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Object txt = dataSnapshot.getValue(Object.class);

                                if(!txt.toString().contains(ednickname.getText().toString()) ||
                                        compare.equals(ednickname.getText().toString())) {
                                    Toast.makeText(profileActivity.this, "프로필이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                    mchild1Ref.setValue(ednickname.getText().toString());
                                    mchild2Ref.setValue(edname.getText().toString());
                                    mchild3Ref.setValue(eduniverse.getText().toString());
                                    mchild4Ref.setValue(edrpduf.getText().toString());
                                    mchild5Ref.setValue(edgkrrhk.getText().toString());
                                    mchild6Ref.setValue(edgrade.getText().toString());

                                }
                                else{
                                    Toast.makeText(profileActivity.this, "이미 있는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(profileActivity.this, "데이터 오류", Toast.LENGTH_SHORT).show();
                            }
                        });

                        hideProgressDialog();
                        */
                        Intent intent = new Intent(getApplicationContext(),ChangeProfileActivity.class);
                        startActivity(intent);
                    }
                });
            }


        };






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
