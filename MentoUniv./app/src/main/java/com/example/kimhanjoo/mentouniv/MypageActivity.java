package com.example.kimhanjoo.mentouniv;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MypageActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private BackPressCloseHandler backPressCloseHandler;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("users");
    DatabaseReference mchildRef;
    DatabaseReference mchild1Ref;
    DatabaseReference mchild2Ref;
    DatabaseReference mchild3Ref;
    DatabaseReference mchild4Ref;
    DatabaseReference mchild5Ref;
    DatabaseReference mchild6Ref;

    TextView txnickname;
    TextView txname;
    TextView txuniverse;
    TextView txdetail;


    String detail1;
    String detail2;
    String detail3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        txnickname = (TextView)findViewById(R.id.nickname);
        txname = (TextView)findViewById(R.id.name);
        txuniverse = (TextView)findViewById(R.id.universe);
        txdetail = (TextView)findViewById(R.id.detail);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.profile_change).setOnClickListener(this);
        findViewById(R.id.image_change).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.pr_manage).setOnClickListener(this);
        findViewById(R.id.pr_upload).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
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
                            txnickname.setText(txt);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    mchild2Ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String txt = dataSnapshot.getValue(String.class);
                            txname.setText(txt);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    mchild3Ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String txt = dataSnapshot.getValue(String.class);
                            txuniverse.setText(txt);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    mchild4Ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            detail1 = dataSnapshot.getValue(String.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    mchild5Ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            detail2 = dataSnapshot.getValue(String.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    mchild6Ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            detail3 = dataSnapshot.getValue(String.class);
                            txdetail.setText(detail1 + "/" + detail2 + "/" + detail3);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        };
        backPressCloseHandler = new BackPressCloseHandler(this);
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.profile_change) {
            Intent intent = new Intent(getApplicationContext(),ChangeProfileActivity.class);
            startActivity(intent);
        }
        else if(i==R.id.image_change){
        }
        else if(i==R.id.logout){
            signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        else if(i==R.id.pr_upload){
            Intent intent = new Intent(getApplicationContext(),ProblemActivity.class);
            startActivity(intent);
        }
        else if(i==R.id.pr_manage){

        }
        else if(i==R.id.pr_record){

        }
    }

    private void signOut() {
        mAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                });
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
