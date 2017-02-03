package com.example.kimhanjoo.mentouniv;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProblemActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "Storage#MainActivity";

    private static final int RC_TAKE_PICTURE = 101;
    private static final int PICK_FROM_ALBUM =55;

    private static final String KEY_FILE_URI = "key_file_uri";
    private static final String KEY_DOWNLOAD_URL = "key_download_url";

    private BroadcastReceiver mBroadcastReceiver;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;

    private Uri mDownloadUrl = null;
    private Uri mFileUri = null;

    private GoogleApiClient mGoogleApiClient;
    TextView proTitle;
    TextView proMain;
    EditText edProTitle;
    EditText edProMain;
    ImageView image;


    long now = System.currentTimeMillis();
    SimpleDateFormat sdfNow;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("photos");
    DatabaseReference mchildRef;
    DatabaseReference mchild1Ref;
    DatabaseReference mchild2Ref;
    DatabaseReference mchild3Ref;
    DatabaseReference mchild4Ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        proTitle = (TextView)findViewById(R.id.protitle);
        proMain = (TextView)findViewById(R.id.promain);
        edProTitle = (EditText)findViewById(R.id.edprotitle);
        edProMain = (EditText)findViewById(R.id.edpromain);

        image = (ImageView)findViewById(R.id.imageView);

        // Click listeners
        findViewById(R.id.button_camera).setOnClickListener(this);
        findViewById(R.id.btproblem).setOnClickListener(this);


        // Restore instance state
        if (savedInstanceState != null) {
            mFileUri = savedInstanceState.getParcelable(KEY_FILE_URI);
            mDownloadUrl = savedInstanceState.getParcelable(KEY_DOWNLOAD_URL);
        }
        onNewIntent(getIntent());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // Local broadcast receiver
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive:" + intent);
                hideProgressDialog();

                switch (intent.getAction()) {
                    case MyDownloadService.DOWNLOAD_COMPLETED:
                        // Get number of bytes downloaded
                        long numBytes = intent.getLongExtra(MyDownloadService.EXTRA_BYTES_DOWNLOADED, 0);

                        // Alert success
                        showMessageDialog(getString(R.string.success), String.format(Locale.getDefault(),
                                "%d bytes downloaded from %s",
                                numBytes,
                                intent.getStringExtra(MyDownloadService.EXTRA_DOWNLOAD_PATH)));
                        break;
                    case MyDownloadService.DOWNLOAD_ERROR:
                        // Alert failure
                        showMessageDialog("Error", String.format(Locale.getDefault(),
                                "Failed to download from %s",
                                intent.getStringExtra(MyDownloadService.EXTRA_DOWNLOAD_PATH)));
                        break;
                    case MyUploadService.UPLOAD_COMPLETED:
                    case MyUploadService.UPLOAD_ERROR:
                        onUploadResultIntent(intent);
                        break;
                }
            }
        };

    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // Check if this Activity was launched by clicking on an upload notification
        if (intent.hasExtra(MyUploadService.EXTRA_DOWNLOAD_URL)) {
            onUploadResultIntent(intent);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI(mAuth.getCurrentUser());

        // Register receiver for uploads and downloads
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(mBroadcastReceiver, MyDownloadService.getIntentFilter());
        manager.registerReceiver(mBroadcastReceiver, MyUploadService.getIntentFilter());


    }

    @Override
    public void onStop() {
        super.onStop();

        // Unregister download receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle out) {
        out.putParcelable(KEY_FILE_URI, mFileUri);
        out.putParcelable(KEY_DOWNLOAD_URL, mDownloadUrl);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
       /* if (requestCode == RC_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                mFileUri = data.getData();

                if (mFileUri != null) {
                    uploadFromUri(mFileUri);
                } else {
                    Log.w(TAG, "File URI is null");
                }
            } else {
                Toast.makeText(this, "Taking picture failed.", Toast.LENGTH_SHORT).show();
            }
        }
        */
        if(requestCode==PICK_FROM_ALBUM){
            mFileUri=data.getData();
            if(resultCode==RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                   // String name_Str = getImageNameToUri(data.getData());
                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap 	= MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    int height=image_bitmap.getHeight();
                    int width=image_bitmap.getWidth();
                    image_bitmap = Bitmap.createScaledBitmap(image_bitmap, 400, height/(width/400), true);
                    //배치해놓은 ImageView에 set
                    image.setImageBitmap(image_bitmap);


                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            if(mFileUri!=null) {
                findViewById(R.id.layout_download).setVisibility(View.VISIBLE);
            }
            else {
                findViewById(R.id.layout_download).setVisibility(View.GONE);
            }
        }

    }



    private void uploadFromUri(Uri fileUri) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

        // Save the File URI
        mFileUri = fileUri;

        // Clear the last download, if any

        mDownloadUrl = null;
        updateUI(mAuth.getCurrentUser());
        // Start MyUploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background
        startService(new Intent(this, MyUploadService.class)
                .putExtra(MyUploadService.EXTRA_FILE_URI, fileUri)
                .setAction(MyUploadService.ACTION_UPLOAD));

        // Show loading spinner
        showProgressDialog(getString(R.string.progress_uploading));




    }



    private void launchCamera() {
        Log.d(TAG, "launchCamera");

        if (mFileUri != null) {
            uploadFromUri(mFileUri);
        } else {
            Log.w(TAG, "File URI is null");
        }
    }
    private void localCamera() {
        Log.d(TAG, "launchCamera");

        // Pick an image from storage
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FROM_ALBUM);

    }


    private void onUploadResultIntent(Intent intent) {
        // Got a new intent from MyUploadService with a success or failure
        mDownloadUrl = intent.getParcelableExtra(MyUploadService.EXTRA_DOWNLOAD_URL);
        mFileUri = intent.getParcelableExtra(MyUploadService.EXTRA_FILE_URI);

        updateUI(mAuth.getCurrentUser());
    }

    private void updateUI(FirebaseUser user) {
        // Signed in or Signed out
        if (user != null) {
            findViewById(R.id.layout_storage).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layout_storage).setVisibility(View.GONE);
        }

        // Download URL and Download button

        if (mDownloadUrl != null) {
            ((TextView) findViewById(R.id.picture_download_uri))
                    .setText(mDownloadUrl.toString());

            mchildRef = mConditionRef.child(user.getUid());
            sdfNow = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
            String time = sdfNow.format(new Date(System.currentTimeMillis()));
            mchild1Ref =  mchildRef.child(time);
            mchild2Ref = mchild1Ref.child("사진");
            mchild3Ref = mchild1Ref.child("제목");
            mchild4Ref = mchild1Ref.child("내용");
            mchild2Ref.setValue(mDownloadUrl.toString());
            mchild3Ref.setValue(edProTitle.getText().toString());
            mchild4Ref.setValue(edProMain.getText().toString());
            findViewById(R.id.layout_download).setVisibility(View.VISIBLE);

        } else {
            ((TextView) findViewById(R.id.picture_download_uri))
                    .setText(null);
            //findViewById(R.id.layout_download).setVisibility(View.GONE);
        }
    }

    private void showMessageDialog(String title, String message) {
        AlertDialog ad = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .create();
        ad.show();
    }

    private void showProgressDialog(String caption) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.setMessage(caption);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_camera) {
            localCamera();
        }
        else if(i==R.id.btproblem){
            if( edProTitle.getText().toString().length() == 0 ) {
                Toast.makeText(ProblemActivity.this, "문제제목을 입력하세요!", Toast.LENGTH_SHORT).show();
                edProTitle.requestFocus();
                return;
            }
            if(mFileUri==null && edProMain.getText().toString().length()==0){
                Toast.makeText(ProblemActivity.this, "문제사진 또는 문제내용을 입력해주세요!", Toast.LENGTH_SHORT).show();
                return;
            }
            launchCamera();
            Toast.makeText(ProblemActivity.this, "문제를 등록하였습니다!", Toast.LENGTH_SHORT).show();
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
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();

    }





    public String getImageNameToUri(Uri data)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        return imgName;
    }
}
