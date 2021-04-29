package com.example.cybank.Network;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cybank.Fragments.ErrorFragment;
import com.example.cybank.Logic.FragmentsManager;
import com.example.cybank.Logic.IVolleyListener;
import com.example.cybank.LoginActivity;
import com.example.cybank.UIActivity;

import org.json.JSONObject;

public class ServerRequest implements IServerRequest {

    private String tag_json_obj = "json_obj_req";
    private IVolleyListener l;

    @Override
    public void sendToServer(String url, JSONObject json, String methodType) {
       sendToServer(url, json, methodType,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null ) {
                            try {
                                l.onSuccess(response.toString());
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            }
                            System.out.println(response.toString());
                        } else {
                            l.onError("Null Response object received");
                        }
                    }},

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        l.onError(error.getMessage());
//                        try {
//                            FragmentsManager.nextFragmentBundle(ErrorFragment.class, false, error.getMessage());
//                        } catch (InstantiationException e) {
//                            e.printStackTrace();
//                        } catch (IllegalAccessException e) {
//                            e.printStackTrace();
//                        }
                    }
                }
        );
    }

    @Override
    public void sendToServer(String url, JSONObject json, String methodType, Response.Listener<JSONObject> onResponse, Response.ErrorListener onError) {

        int method = Request.Method.GET;

        if (methodType.equals("POST")) {
            method = Request.Method.POST;
        } else if(methodType.equals("PUT")) {
            method = Request.Method.PUT;
        } else if(methodType.equals("DELETE")) {
            method = Request.Method.DELETE;
        }
        JsonObjectRequest registerUserRequest = new JsonObjectRequest(method, url, json, onResponse, onError);

        LoginActivity.getInstance().addToRequestQueue(registerUserRequest, tag_json_obj);
    }

    @Override
    public void addVolleyListener(IVolleyListener logic) {
        l = logic;
    }

}
