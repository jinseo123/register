package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class app_1Activity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;//파이어 베이스 인증 처리
    private DatabaseReference mDatabaseRef;//실시간 데이터 베이스DB
    private EditText mEtId,mEtPwd;//로그인 입력 필드


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app1);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("ssufit");

        mEtId = findViewById(R.id.login_edit_id);
        mEtPwd = findViewById(R.id.login_edit_password);


        Button btn_login =findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //로그인 요청
                String strEmail = mEtId.getText().toString();//아이디 입력한 값 로그인 버튼누르면 문자열로 값 가져오기
                String strPwd = mEtPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(app_1Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //로그인 성공
                            Intent intent = new Intent(app_1Activity.this , MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(app_1Activity.this, "아이디 또는 비밀번호를 다시 확인해주세요.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 버튼 누르면 ... 이동
                Intent intent = new Intent(app_1Activity.this, app_2Activity.class);
                startActivity(intent);
            }
        });
    }

}