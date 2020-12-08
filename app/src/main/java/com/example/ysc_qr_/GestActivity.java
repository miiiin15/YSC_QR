package com.example.ysc_qr_;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;


public class GestActivity extends AppCompatActivity {

    public  static  Boolean CheckInput(String inputname, int inputmajor){ // 입력값 유효성 검사

        String regex_name = "^[ㄱ-ㅎ가-힣]{2,6}$"; // 2~6자리의 한글 이름
        String regex_major = "^\\d{7,9}$"; // 7,9자리의 숫자
        boolean permit =  false;

        boolean result_name = Pattern.matches(regex_name, inputname);
        boolean result_major = Pattern.matches(regex_major, String.valueOf(inputmajor));
        if(result_name&&result_major) {
            permit = true;
        }
        return permit;

    }

    private EditText gst_name, gst_major;
    private Button btn_gst;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gest_activity);

        gst_name = findViewById(R.id.Gst_Name);
        gst_major = findViewById(R.id.Gst_Major);
        btn_gst = findViewById(R.id.btn_Gst);

        btn_gst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = "";
                int userMajor = 0;

                    try {
//게스트
                        userName = gst_name.getText().toString();
                        userMajor = Integer.parseInt(gst_major.getText().toString());

                        if(CheckInput(userName,userMajor)){
                            Intent intent = new Intent(getApplicationContext(), VisitorActivity.class);
                            intent.putExtra("userName",userName);
                            intent.putExtra("userMajor",userMajor);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"유효하지 않은 입력값 입니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(),"빈칸을 체워 주세요.",Toast.LENGTH_SHORT).show();
                    }
            }
        });

    }
}
