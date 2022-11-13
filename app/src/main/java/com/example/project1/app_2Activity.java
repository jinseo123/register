package com.example.project1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class app_2Activity extends AppCompatActivity {

    Button signup_local_choose;
    AlertDialog.Builder builder;
    String[] locals;
    RadioGroup radioGroup;

    private FirebaseAuth mFirebaseAuth;//파이어 베이스 인증 처리
    private DatabaseReference mDatabaseRef;//실시간 데이터 베이스DB
    private EditText mEtId,mEtPwd,mEtname,mEtnickname,mEtrepwd;//회원가입 입력 필드
    private Button mBtnRegister;//회원가입 입력 버튼
    private RadioButton mBtnmale;
    private RadioButton mBtnfemale;
    private RadioGroup mBtnsex;
    //private Button doublecheck;//중복 체크 버튼
    public String gender;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app2);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("ssufit");

        mEtId = findViewById(R.id.signup_edit_id);
        mEtPwd = findViewById(R.id.signup_edit_password);
        mEtname = findViewById(R.id.signup_edit_name);
        mEtnickname = findViewById(R.id.signup_edit_nickname);
        mEtrepwd = findViewById(R.id.signup_edit_repassword);
        mBtnRegister = findViewById(R.id.signup_btn_signup);
        mBtnmale = findViewById(R.id.signup_radiobutton_male);
        mBtnfemale = findViewById(R.id.signup_radiobutton_female);
        mBtnsex = findViewById(R.id.singup_gender_radiogroup);

       // doublecheck = findViewById(R.id.signup_btn_doublecheck);
       /*mBtnsex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup radioGroup, int i) {
               RadioButton gender = radioGroup.findViewById(i);
               switch (i){
                   case R.id.signup_radiobutton_male:
                       gender = "남자";
                       break;
                   case R.id.signup_radiobutton_female:
                       gender =
               }
           }
       });*/



        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //회원가입 처리 시작
                String strEmail = mEtId.getText().toString();//아이디 입력한 값 회원가입 버튼누르면 문자열로 값 가져오기
                String strPwd = mEtPwd.getText().toString();
                String pwdcheck = mEtrepwd.getText().toString().trim();



                if (!mEtId.getText().toString().equals("") && !mEtPwd.getText().toString().equals("")) {
                    //이메일 비번 공백 아닌 경우
                    if(gender!=null && !mEtnickname.getText().toString().equals("")) {
                        if (strPwd.equals(pwdcheck)) {
                            Log.d(TAG, "등록 버튼" + strEmail + ", " + strPwd);
                            final ProgressDialog mDialog = new ProgressDialog(app_2Activity.this);
                            mDialog.setMessage("가입중입니다...");
                            mDialog.show();


                            mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(app_2Activity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {//가입 성공시 처리
                                    if (task.isSuccessful()) {
                                        mDialog.dismiss();
                                        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                        String uid = firebaseUser.getUid();
                                        String email = firebaseUser.getEmail();
                                        String pwd = strPwd;
                                        String strname = mEtname.getText().toString().trim();
                                        String nickname = mEtnickname.getText().toString().trim();


                                        UserAccount account = new UserAccount();


                                        HashMap<Object, String> hashMap = new HashMap<>();

                                        hashMap.put("uid", uid);
                                        hashMap.put("email", email);
                                        hashMap.put("name", strname);
                                        hashMap.put("pwd", pwd);
                                        hashMap.put("nickname", nickname);
                                        hashMap.put("gender", gender);

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getReference("Users");
                                        reference.child(uid).setValue(hashMap);


                                        Toast.makeText(app_2Activity.this, "회원가입에 성공", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mDialog.dismiss();

                                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                        switch (errorCode) {
                                            case "ERROR_INVALID_EMAIL":
                                                Toast.makeText(app_2Activity.this, "잘못된 이메일 주소 형식입니다.", Toast.LENGTH_SHORT).show();
                                                mEtId.setError("잘못된 이메일 형식.");
                                                mEtId.requestFocus();
                                                break;
                                            case "ERROR_EMAIL_ALREADY_IN_USE":
                                                Toast.makeText(app_2Activity.this, "이미 사용중인 이메일 입니다.", Toast.LENGTH_SHORT).show();
                                                mEtId.setError("이미 사용중인 이메일 입니다.");
                                                mEtId.requestFocus();
                                                break;
                                            case "ERROR_WEAK_PASSWORD":
                                                Toast.makeText(app_2Activity.this, "비밀번호 길이가 너무 짧습니다.", Toast.LENGTH_LONG).show();
                                                mEtPwd.setError("비밀번호 길이가 너무 짧습니다.");
                                                mEtPwd.requestFocus();
                                                break;

                                        }

                                    }
                                }
                            });

                        } else {
                            Toast.makeText(app_2Activity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else {
                        Toast.makeText(app_2Activity.this, "빈칸을 모두 채워주세요.",Toast.LENGTH_SHORT).show();
                        return;
                    }

                } else {
                    Toast.makeText(app_2Activity.this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        signup_local_choose = findViewById(R.id.singup_local_choosebutton);

        signup_local_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        radioGroup = (RadioGroup) findViewById(R.id.singup_gender_radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.signup_radiobutton_male) {
                    Toast.makeText(app_2Activity.this, "남성을 선택하였습니다.", Toast.LENGTH_SHORT).show();
                    gender = "남자";
                } else if (i == R.id.signup_radiobutton_female) {
                    Toast.makeText(app_2Activity.this, "여성을 선택하였습니다", Toast.LENGTH_SHORT).show();
                    gender = "여자";
                }
            }
        });
    }

    public void showDialog() {
        locals = getResources().getStringArray(R.array.locals);
        builder = new AlertDialog.Builder(app_2Activity.this);
        builder.setTitle("지역을 선택하세요");
        builder.setItems(locals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                Toast.makeText(getApplicationContext(), "선택한 지역은 " + locals[which], Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}