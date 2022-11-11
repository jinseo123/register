package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class app_2Activity extends AppCompatActivity {

    Button signup_local_choose;
    AlertDialog.Builder builder;
    String[] locals;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app2);

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
                } else if (i == R.id.signup_radiobutton_female) {
                    Toast.makeText(app_2Activity.this, "여성을 선택하였습니다", Toast.LENGTH_SHORT).show();
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