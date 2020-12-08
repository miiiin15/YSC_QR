package com.example.ysc_qr_;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    public void Find(String first, String second,final int kind){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        String result = "";
                        switch (kind){
                            case 0 :
                                result = jsonObject.getString("userID");
                                break;
                            case 1:
                                result = jsonObject.getString("userPassword");
                                break;
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                        builder.setTitle("결과").setMessage("조회 결과는 [ "+ result + " ] 입니다.");

                        builder.setNegativeButton("확인", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Toast.makeText(getApplicationContext(), "다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }else{
                        Toast.makeText(getApplicationContext(),"입력 정보와 일치하는 아이디/패스워드가 없습니다.",Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        switch (kind){
            case 0 :
                FindIDRequest findIDRequest = new FindIDRequest(first,second,responseListener);
                queue.add(findIDRequest);
                break;
            case 1:
                FindPWDRequest findPWDRequest = new FindPWDRequest(first,second,responseListener);
                queue.add(findPWDRequest);
                break;
        }

    }

    private EditText edt_id,edt_pwd;
    private Button btn_login,btn_register,btn_find,btn_gest;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        final LinearLayout find_id = (LinearLayout) View.inflate(LoginActivity.this, R.layout.find_id, null);
        final LinearLayout find_pwd = (LinearLayout) View.inflate(LoginActivity.this, R.layout.find_password, null);

        edt_id = findViewById(R.id.Login_ID);
        edt_pwd = findViewById(R.id.Login_Password);
        btn_login = findViewById(R.id.Login_btn);
        btn_register = findViewById(R.id.Register_btn);
        btn_find = findViewById(R.id.find_btn);
        btn_gest = findViewById(R.id.gest_btn);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String userID = edt_id.getText().toString();
                String userPwd = edt_pwd.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                String userID = jsonObject.getString("userID");
                                String userPassword = jsonObject.getString("userPassword");
                                String userName = jsonObject.getString("userName");
                                int userMajor = jsonObject.getInt("userMajor");
                                Toast.makeText(getApplicationContext(),userName+"님 환영합니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), VisitorActivity.class);
                                intent.putExtra("userID",userID);
                                intent.putExtra("userName",userName);
                                intent.putExtra("userMajor",userMajor);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getApplicationContext(),"로그인 실패",Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID,userPwd,responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(LoginActivity.this);

                builder.setTitle("아이디/비밀번호 찾기");

                builder.setItems(R.array.find, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int pos)
                    {
                        String[] items = getResources().getStringArray(R.array.find);
                        switch (pos){
                            case 0:
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setView(find_id)
                                        .setPositiveButton("탐색", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                EditText in_name = (EditText) find_id.findViewById(R.id.input_name);
                                                EditText in_num = (EditText) find_id.findViewById(R.id.input_num);

                                                String str_name = in_name.getText().toString();
                                                String str_num = in_num.getText().toString();

                                                Find(str_name,str_num,0);

                                            }
                                        })
                                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                                return;
                            case 1:
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setView(find_pwd)
                                        .setPositiveButton("탐색", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                EditText in_id = (EditText) find_pwd.findViewById(R.id.input_id);
                                                EditText in_num = (EditText) find_pwd.findViewById(R.id.input_num);

                                                String str_id = in_id.getText().toString();
                                                String str_num = in_num.getText().toString();

                                                Find(str_id,str_num,1);

                                            }
                                        })
                                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                                return;
                        }
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btn_gest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GestActivity.class);
                Toast.makeText(getApplicationContext(),"비회원으로 접속합니다.",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}
