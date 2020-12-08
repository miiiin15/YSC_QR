package com.example.ysc_qr_;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FindPWDRequest extends StringRequest {

    final static private String URL = "http://miiiin.dothome.co.kr/Find_pwd.php";
    private Map<String, String> map;

    public FindPWDRequest(String userID, String userMajor, Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID",userID);
        map.put("userMajor",userMajor);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

    
}
