package com.example.ysc_qr_;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    public  static  Boolean CheckInput(String inputid,String inputpwd,String inputname, int inputmajor){ // 입력값 유효성 검사

        String regex_idpwd = "^[a-zA-Z0-9]{4,20}$"; //4~20자리의 영어 숫자
        String regex_name = "^[ㄱ-ㅎ가-힣]{2,6}$"; // 2~6자리의 한글 이름
        String regex_major = "^\\d{7,9}$"; // 7,9자리의 숫자
        boolean permit =  false;

        boolean result_id = Pattern.matches(regex_idpwd, inputid);
        boolean result_pwd = Pattern.matches(regex_idpwd, inputpwd);
        boolean result_name = Pattern.matches(regex_name, inputname);
        boolean result_major = Pattern.matches(regex_major, String.valueOf(inputmajor));
        if(result_id&&result_pwd&&result_name&&result_major) {
            permit = true;
        }
        return permit;

    }

    private EditText edt_id, edt_pwd,edt_re_pwd, edt_name, edt_major;
    private Button btn_reg,btn_ckid,btn_find;
    private AlertDialog dialog;
    private boolean check=false,checkmajor=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        edt_id = findViewById(R.id.Reg_ID);
        edt_pwd = findViewById(R.id.Reg_Password);
        edt_re_pwd = findViewById(R.id.Reg_Re_Password);
        edt_name = findViewById(R.id.Reg_Name);
        edt_major = findViewById(R.id.Reg_Major);
        btn_reg = findViewById(R.id.btn_register);
        btn_ckid = findViewById(R.id.btn_idcheck);
        btn_find = findViewById(R.id.btn_find);

        btn_ckid.setOnClickListener(new View.OnClickListener() { //아이디 중복 체크
            @Override
            public void onClick(View v) {
                String userID=edt_id.getText().toString();
                if(check)
                {
                    return;
                }
                if(userID.equals("")||userID.length()<4||userID.length()>20){ // 입력값 유효성 검사
                    Toast.makeText(getApplicationContext(),"4~20자리의 아이디를 입력해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse=new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder=new AlertDialog.Builder( RegisterActivity.this );
                                dialog=builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                edt_id.setEnabled(false);
                                check=true;
                                btn_ckid.setText("확인");
                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder( RegisterActivity.this );
                                dialog=builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Registercheck registercheck=new Registercheck(userID,responseListener);
                RequestQueue queue= Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registercheck);
            }
        });

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userMajor=edt_major.getText().toString();
                if(checkmajor)
                {
                    return;
                }
                if(userMajor.equals("")||userMajor.length()<7||userMajor.length()>10||userMajor.length()==8){ // 입력값 유효성 검사 7자리 9자리 숫자만 입력가능
                    Toast.makeText(getApplicationContext(),"7또는9자리의 번호를 입력해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse=new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder=new AlertDialog.Builder( RegisterActivity.this );
                                dialog=builder.setMessage("사용할 수 있는 번호입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                edt_major.setEnabled(false);
                                checkmajor=true;
                                btn_find.setText("확인");
                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder( RegisterActivity.this );
                                dialog=builder.setMessage("이미 등록된 번호입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Registercheck2 registercheck=new Registercheck2(userMajor,responseListener);
                RequestQueue queue= Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registercheck);
            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userID = "";
                String userPwd = "";
                String userRePwd = "";
                String userName = "";
                int userMajor = 0;

                if(check&&checkmajor){
                    try {
                        userID = edt_id.getText().toString();
                        userPwd = edt_pwd.getText().toString();
                        userRePwd = edt_re_pwd.getText().toString();
                        userName = edt_name.getText().toString();
                        userMajor = Integer.parseInt(edt_major.getText().toString());

                        if(CheckInput(userID,userPwd,userName,userMajor)){
                            if(userPwd.equals(userRePwd)){
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if(success){

                                                Toast.makeText(getApplicationContext(),"회원가입 완료",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                                                startActivity(intent);
                                            }else {
                                                Toast.makeText(getApplicationContext(),"회원가입 실패",Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                        }catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                RegisterRequest registerRequest = new RegisterRequest(userID,userPwd,userName,userMajor,responseListener);
                                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                                queue.add(registerRequest);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"유효하지 않은 입력값 입니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(),"빈칸을 체워 주세요.",Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"아이디와 학(사)번 중복 확인을 해야합니다.",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
