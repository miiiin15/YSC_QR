package com.example.ysc_qr_;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SendRequest extends StringRequest {

    final static private String URL = "http://miiiin.dothome.co.kr/SendQR.php";
    private Map<String, String> map;

    public SendRequest(String userName, int userMajor,Double temper,Response.Listener<String> listener){
        super(Method.POST, URL, listener,null);
        map = new HashMap<>();
        map.put("userName",userName);
        map.put("userMajor",userMajor+"");
        map.put("temper",temper+"");
        //map.put("created",created);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
