package com.example.cybank.Network;

import com.android.volley.Response;
import com.example.cybank.Logic.IVolleyListener;

import org.json.JSONObject;

public interface IServerRequest {
    public void sendToServer(String url, JSONObject newUserObj, String methodType, Response.Listener<JSONObject> onResponse, Response.ErrorListener onError);
    public void sendToServer(String url, JSONObject newUserObj, String methodType);
    public void addVolleyListener(IVolleyListener logic);
}
