package com.example.kimhanjoo.mentouniv;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private EditText edname;
    private Button btnDone;
    private Button btnCancel;
    private FirebaseAuth mAuth;
    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "EmailPassword";
    private int x=0;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);


        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);
        edname =(EditText) findViewById(R.id.edname);
        btnDone = (Button) findViewById(R.id.btnDone);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        mAuth = FirebaseAuth.getInstance();

        // 비밀번호 일치 검사
        etPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = etPassword.getText().toString();
                String confirm = etPasswordConfirm.getText().toString();

                if( password.equals(confirm) ) {
                    etPassword.setBackgroundColor(Color.GREEN);
                    etPasswordConfirm.setBackgroundColor(Color.GREEN);
                } else {
                    etPassword.setBackgroundColor(Color.RED);
                    etPasswordConfirm.setBackgroundColor(Color.RED);
                }
            }



            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String[] optionLavala = getResources().getStringArray(R.array.spinnerArray1);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,optionLavala
        );

        Spinner obj=(Spinner)findViewById(R.id.spinner1);
        obj.setAdapter(adapter);
        setSpinner(R.id.spinner2,R.array.spinnerArray2,android.R.layout.simple_spinner_dropdown_item);
        setSpinner(R.id.spinner3,R.array.spinnerArray3,android.R.layout.simple_spinner_item);
        setSpinner(R.id.spinner4,R.array.spinnerArray4,android.R.layout.simple_spinner_item);

        getSpinner(R.id.spinner1).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?>parentView,View selectedView, int position,long id){
                printChecked(selectedView,position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView){

            }
        });
        getSpinner(R.id.spinner2).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?>parentView,View selectedView, int position,long id){
                printChecked(selectedView,position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView){

            }
        });
        getSpinner(R.id.spinner3).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?>parentView,View selectedView, int position,long id){
                printChecked(selectedView,position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView){

            }
        });
        getSpinner(R.id.spinner4).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?>parentView,View selectedView, int position,long id){
                printChecked(selectedView,position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView){

            }
        });




        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void printChecked(View v, int position){
        Spinner sp1 = (Spinner)findViewById(R.id.spinner1);
        Spinner sp2 = (Spinner)findViewById(R.id.spinner2);
        Spinner sp3 = (Spinner)findViewById(R.id.spinner3);
        Spinner sp4 = (Spinner)findViewById(R.id.spinner4);
    }
    public void setSpinner(int objId, int optionLabelId, int listStyle){
        setSpinner(objId,optionLabelId,-1,listStyle,null);
    }
    public void setSpinner(int objId,int optionLabelId, int optionId, int liststyle, String defaultVal){
        String[] optionLavala = getResources().getStringArray(optionLabelId);
        ArrayAdapter<String> adaptor = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,optionLavala);
        if(liststyle>-1){
            adaptor.setDropDownViewResource(liststyle);
            Spinner obj = (Spinner)findViewById(objId);
            obj.setAdapter(adaptor);
            if(defaultVal != null){
                String[] option = getResources().getStringArray(optionId);
                int thei=0;
                for(int a=0;a<option.length;a++){
                    if(defaultVal.equals(option[a])){
                        thei=a;
                        break;
                    }
                }
                obj.setSelection(adaptor.getPosition(optionLavala[thei]));
            }
            else{
                obj.setSelection(adaptor.getPosition(defaultVal));
            }
        }
    }
    public Spinner getSpinner(int objId){
        return (Spinner)findViewById(objId);
    }
    public String getSpinnerVal(int objId){
        return getSpinneVal(objId,null);
    }
    private String getSpinneVal(int objId, String[] option){
        String rtn="";
        Spinner sp=((Spinner)findViewById(objId));
        if(sp != null){
            int selectedIndex=sp.getSelectedItemPosition();
            if(option!=null){
                rtn=""+selectedIndex;
            }else{
                if(option.length>selectedIndex){
                    rtn=option[selectedIndex];
                }
            }
        }
        return rtn;
    }

    protected void onStart(){
        super.onStart();
        final int[] can = {1};
        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(x==1){
                    mAuthListener = new FirebaseAuth.AuthStateListener(){
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            mConditionRef.setValue(user.getUid());
                        }
                    };
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 이메일 입력 확인
                if( etEmail.getText().toString().length() == 0 ) {
                    Toast.makeText(RegistActivity.this, "이메일(아이디) 입력하세요!", Toast.LENGTH_SHORT).show();
                    etEmail.requestFocus();
                    return;
                }

                // 비밀번호 입력 확인
                if( etPassword.getText().toString().length() == 0 ) {
                    Toast.makeText(RegistActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    etPassword.requestFocus();
                    return;
                }

                // 비밀번호 확인 입력 확인
                if( etPasswordConfirm.getText().toString().length() == 0 ) {
                    Toast.makeText(RegistActivity.this, "비밀번호 확인을 입력하세요!", Toast.LENGTH_SHORT).show();
                    etPasswordConfirm.requestFocus();
                    return;
                }

                // 비밀번호 일치 확인
                if( !etPassword.getText().toString().equals(etPasswordConfirm.getText().toString()) ) {
                    Toast.makeText(RegistActivity.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                    etPassword.setText("");
                    etPasswordConfirm.setText("");
                    etPassword.requestFocus();
                    return;
                }
                // 이름 입력 확인
                if( edname.getText().toString().length() == 0 ) {
                    Toast.makeText(RegistActivity.this, "이름 입력하세요!", Toast.LENGTH_SHORT).show();
                    edname.requestFocus();
                    return;
                }

                if(can[0]==0){
                    Toast.makeText(RegistActivity.this, "이미 있는 아이디입니!", Toast.LENGTH_SHORT).show();
                    etEmail.requestFocus();
                    return;
                }


                // 대학교 선택 확인
                Intent result = new Intent();
                result.putExtra("ID", etEmail.getText().toString());
                result.putExtra("password",etPassword.getText().toString());
                result.putExtra("name",edname.getText().toString());


                createAccount(etEmail.getText().toString(),etPassword.getText().toString());

                // [END create_user_with_email]
                // 자신을 호출한 Activity로 데이터를 보낸다.
                if(x==1) {
                    setResult(RESULT_OK, result);
                    finish();
                }
            }
        });

    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            x=0;
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegistActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            x=0;
                        }
                        else if(task.isSuccessful()){
                            x=1;
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required.");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required.");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }




}
