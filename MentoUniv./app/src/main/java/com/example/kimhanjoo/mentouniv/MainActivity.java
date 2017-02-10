package com.example.kimhanjoo.mentouniv;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, SwipeRefreshLayout.OnRefreshListener {

    private RelativeLayout mlayout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private BackPressCloseHandler backPressCloseHandler;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("photos");
    DatabaseReference mchildRef;
    DatabaseReference mchild1Ref;
    DatabaseReference mchild2Ref;
    DatabaseReference mchild3Ref;
    ListView list;
    ListViewAdapter adapter;
    EditText editsearch;
    String[] rank;
    String[] country;
    String[] population;
    String title;

    Button btprofile;
    //Button btlogout;
    Button btproblem;
    Button bthome;
    Button btviewall;
    SwipeRefreshLayout mSwipeRefreshLayout;



    ArrayList<WorldPopulation> arraylist = new ArrayList<WorldPopulation>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mlayout = (RelativeLayout) findViewById(R.id.activity_main);
        mlayout.setBackgroundColor(Color.rgb(148,210,238));
        btviewall = (Button)findViewById(R.id.btview);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);;
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                mConditionRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       //title = dataSnapshot.getValue().toString();
                        //Log.v("ppp", title);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        };
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

        //btlogout = (Button) findViewById(R.id.logout);
        //btprofile=(Button)findViewById(R.id.profile);
        //btproblem = (Button)findViewById(R.id.problem);
       // bthome = (Button)findViewById(R.id.bthome);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


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
        /*
        btproblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ProblemActivity.class);
                startActivity(intent);
            }
        });
        btprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MypageActivity.class);
                startActivity(intent);
            }
        });
        bthome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        */
        btviewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(intent);
            }
        });

        backPressCloseHandler = new BackPressCloseHandler(this);


    }

    // Not using options menu in this tutorial
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }
        else if(i==R.id.user_bell){

            return true;
        }
        else if(i==R.id.user_help){
            Toast.makeText(this, "도움말 선택.", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }

    }

    private void signOut() {
        showProgressDialog();
        mAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                });
        hideProgressDialog();
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public void onRefresh() {
        // 새로고침 코드
        // 새로고침 완료
        mSwipeRefreshLayout.setRefreshing(false);
    }

}
