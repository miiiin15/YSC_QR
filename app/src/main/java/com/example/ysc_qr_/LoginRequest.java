package com.example.ysc_qr_;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    final static private String URL = "http://miiiin.dothome.co.kr/Login.php";
    private Map<String, String> map;

    public LoginRequest(String userID, String userPassword, Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID",userID);
        map.put("userPassword",userPassword);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

    
}
