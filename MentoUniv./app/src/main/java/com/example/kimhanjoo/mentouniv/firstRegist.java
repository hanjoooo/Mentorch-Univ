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

public class firstRegist extends BaseActivity {

    private EditText edname;
    private EditText ednickname;
    private Button btnDone;
    private Button btnCancel;
    private FirebaseAuth mAuth;

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "EmailPassword";


    DatabaseReference mchildRef;  DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("users");
    DatabaseReference mchild1Ref;
    DatabaseReference mchild2Ref;
    DatabaseReference mchild3Ref;
    DatabaseReference mchild4Ref;
    DatabaseReference mchild5Ref;
    DatabaseReference mchild6Ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_regist);

        ednickname = (EditText)findViewById(R.id.ednickname);
        edname =(EditText) findViewById(R.id.edname);
        btnDone = (Button) findViewById(R.id.btnDone);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        mAuth = FirebaseAuth.getInstance();
        final Spinner spinner1 = (Spinner)findViewById(R.id.spinner1);
        final Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
        final Spinner spinner3 = (Spinner)findViewById(R.id.spinner3);
        final Spinner spinner4 = (Spinner)findViewById(R.id.spinner4);


        String[] optionLavala = getResources().getStringArray(R.array.spinnerArray1);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,optionLavala
        );

        final Spinner obj=(Spinner)findViewById(R.id.spinner1);
        obj.setAdapter(adapter);
        setSpinner(R.id.spinner2,R.array.spinnerArray2,android.R.layout.simple_spinner_dropdown_item);
        setSpinner(R.id.spinner3,R.array.spinnerArray3,android.R.layout.simple_spinner_item);
        setSpinner(R.id.spinner4,R.array.spinnerArray4,android.R.layout.simple_spinner_item);

        getSpinner(R.id.spinner1).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?>parentView, View selectedView, int position, long id){
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


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                updateUI(user);
                // [END_EXCLUDE]
            }
        };
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(firstRegist.this, "에러 발생!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // 이름 입력 확인
                if( edname.getText().toString().length() == 0 ) {
                    Toast.makeText(firstRegist.this, "이름 입력하세요!", Toast.LENGTH_SHORT).show();
                    edname.requestFocus();
                    return;
                }


                // 대학교 선택 확인
                String univers = spinner1.getSelectedItem().toString();
                String rpduf = spinner2.getSelectedItem().toString();
                String gkrrhk = spinner3.getSelectedItem().toString();
                String grade = spinner4.getSelectedItem().toString();

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);


                // [END create_user_with_email]
                // 자신을 호출한 Activity로 데이터를 보낸다.
                mchild1Ref.setValue(ednickname.getText().toString());
                mchild2Ref.setValue(edname.getText().toString());
                mchild3Ref.setValue(univers);
                mchild4Ref.setValue(rpduf);
                mchild5Ref.setValue(gkrrhk);
                mchild6Ref.setValue(grade);
                finish();
                startActivity(intent);

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
        mAuth.addAuthStateListener(mAuthListener);

        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            mchildRef = mConditionRef.child(user.getUid());
            mchild1Ref = mchildRef.child("닉네임");
            mchild2Ref = mchildRef.child("이름");
            mchild3Ref = mchildRef.child("대학교");
            mchild4Ref = mchildRef.child("계열");
            mchild5Ref = mchildRef.child("분야");
            mchild6Ref = mchildRef.child("학년");

        } else {

        }
    }
}