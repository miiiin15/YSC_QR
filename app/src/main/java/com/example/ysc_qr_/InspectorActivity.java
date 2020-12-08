package com.example.ysc_qr_;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class InspectorActivity extends AppCompatActivity {

    public  static  Boolean CheckInput(Double input_temper){ // 온도 값 유효성 검사

        boolean result = false;

        if(input_temper>35&&input_temper<42) { //정상체온 확인
            result = true;
        }

        return result;
    }

    private IntentIntegrator qrScan;
    private Button reScan,stats;
    private String userName,userMajor;
    private String[] value;
    private double temper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspector_activity);

        reScan = (Button)findViewById(R.id.Scanner_btn);
        stats = (Button)findViewById(R.id.Stats_btn);

        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.setPrompt("QR코드를 사각형에 맞춰주세요.");
        qrScan.initiateScan();

        Toast.makeText(getApplicationContext(),"QR코드를 스캔해주세요", Toast.LENGTH_SHORT).show();

        reScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"QR코드를 스캔해주세요", Toast.LENGTH_SHORT).show();
                qrScan.initiateScan();
            }
        });
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StatsActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        AlertDialog.Builder ad = new AlertDialog.Builder(InspectorActivity.this);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "스캐너를 닫습니다.", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "스캔 완료 : " + result.getContents(), Toast.LENGTH_LONG).show();
                value = result.getContents().split(" ");
                userName = value[0]; userMajor = value[1];

                ad.setTitle(userName + "님의 체온 입력");       // 제목 설정
                ad.setMessage("측정한 체온을 입력해주세요");   // 내용 설정

                final EditText et = new EditText(InspectorActivity.this);
                ad.setView(et);

                ad.setPositiveButton("전송", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        temper = Double.parseDouble(et.getText().toString());

                        //전송부

                        if(CheckInput(temper)){
                            try{

                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if(success){
                                                Toast.makeText(getApplicationContext(),"전송 되었습니다.",Toast.LENGTH_SHORT).show();

                                            }else {
                                                Toast.makeText(getApplicationContext(),"전송 실패",Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                        }catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                SendRequest sendRequest = new SendRequest(userName,Integer.parseInt(userMajor),temper,responseListener);
                                RequestQueue queue = Volley.newRequestQueue(InspectorActivity.this);
                                queue.add(sendRequest);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"유효하지 않은 온도값 입니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
