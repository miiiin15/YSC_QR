package com.example.ysc_qr_;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.github.mikephil.charting.components.YAxis;

public class StatsActivity extends AppCompatActivity {
    Button btn_search;
    TextView tv_total, tv_common, tv_over, tv_date ;
    DatePickerDialog.OnDateSetListener mOnDateSetListener;
    BarChart chart;
    ArrayList dataValues = new ArrayList();
    ArrayList date = new ArrayList();
    int [] coloarry = new int[] {Color.parseColor("#3300ff"),Color.parseColor("#ff0033")};
    int commonD=0, overD=0;

    public void Find(String first){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        commonD = jsonObject.getInt("common");
                        overD = jsonObject.getInt("over");
                    }
                    else{Toast.makeText(getApplicationContext(),"fail", Toast.LENGTH_SHORT).show();}
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                tv_common.setText(commonD+"");
                tv_over.setText(overD+"");
                tv_total.setText((overD+commonD)+"");
            }
        };
        RequestQueue queue = Volley.newRequestQueue(StatsActivity.this);
                FindStatsRequest findStatsRequest = new FindStatsRequest(first,responseListener);
                queue.add(findStatsRequest);
    }

    public float[] setG(String first){
        final float arr[] = new float[]{0,0};
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        arr[0] = jsonObject.getInt("common");
                        arr[1] = jsonObject.getInt("over");
                    }
                    else{Toast.makeText(getApplicationContext(),"fail", Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RequestQueue queue = Volley.newRequestQueue(StatsActivity.this);
        FindStatsRequest findStatsRequest = new FindStatsRequest(first,responseListener);
        queue.add(findStatsRequest);

        return arr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_activity);

        btn_search = (Button)findViewById(R.id.btn_search);
        chart = findViewById(R.id.barchart);
        tv_total = (TextView)findViewById(R.id.total);
        tv_common = (TextView)findViewById(R.id.common);
        tv_over = (TextView)findViewById(R.id.over);
        tv_date = (TextView)findViewById(R.id.Date);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCalendar =  Calendar.getInstance();
                int year = mCalendar.get(Calendar.YEAR);
                int month=mCalendar.get(Calendar.MONTH);//[]={0,1,2,3,4,5,6,7,8,9,10,11,12};
                int day = mCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePickerDialog = new DatePickerDialog(
                        StatsActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mOnDateSetListener, year,month,day);
                mDatePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDatePickerDialog.show();
            }
        });

        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month= month+1;

                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                String d = year+"-"+month+"-"+dayOfMonth;
                Date sDate = new Date();
                String []DateList = new String[5];

                try {
                    sDate = sd.parse(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar cal = Calendar.getInstance();
                cal.setTime(sDate);

                for(int i = 0; i<5; i++){
                    if(i==0){
                        date.clear();
                        dataValues.clear();
                    }
                    cal.add(Calendar.DAY_OF_YEAR,-1);
                    DateList[i] = sd.format(cal.getTime());
                    date.add(DateList[i].substring(5));
                    dataValues.add(new BarEntry(setG(DateList[i]), i));
                }

                tv_date.setText(year+"년 "+month+"월 "+dayOfMonth+"일 유동량");
                Find(d);

                BarDataSet bardataset = new BarDataSet(dataValues, "");
                chart.animateY(5000);
                BarData data = new BarData(date, bardataset);
                bardataset.setColors(coloarry);
                YAxis lAxis = chart.getAxisLeft();
                YAxis rAxis = chart.getAxisRight();
                lAxis.setStartAtZero(true);
                rAxis.setStartAtZero(true);
                lAxis.setAxisMaxValue(50);
                rAxis.setAxisMaxValue(50);
                bardataset.setStackLabels(new String[] {"정상","고온"});
                chart.setData(data);
            }
        };
    }
}
