package com.example.cybank.Logic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Response;
import com.example.cybank.Fragments.ErrorFragment;
import com.example.cybank.Network.IServerRequest;
import com.example.cybank.UIActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that provides the general logic for post and get requests
 */
public class GeneralLogic implements IVolleyListener {

    private static final String TAG = "Request Error";
    String globalError;
    IServerRequest serverRequest;
    String ip = "http://10.24.227.130:8080/";
    Class sendFragment;
    Boolean isReplace = true;

    public GeneralLogic(IServerRequest serverRequest) {
        this.serverRequest = serverRequest;
        serverRequest.addVolleyListener(this);

    }
    public void postRequest(JSONObject json, String urladd, Response.Listener<JSONObject> onResponse, Response.ErrorListener onError) throws JSONException {
        final String url = ip + urladd;
        serverRequest.sendToServer(url, json, "POST", onResponse, onError);
    }
    public void postRequest(JSONObject json, String urladd) throws JSONException {
        final String url = ip + urladd;
        serverRequest.sendToServer(url, json, "POST");
    }
    public void putRequest(JSONObject json, String urladd) throws JSONException {
        final String url = ip + urladd;
        serverRequest.sendToServer(url, json, "PUT");
    }
    public void putRequest(JSONObject json, String urladd, Response.Listener<JSONObject> onResponse, Response.ErrorListener onError) throws JSONException {
        final String url = ip + urladd;
        serverRequest.sendToServer(url, json, "PUT", onResponse, onError);
    }

    public void deleteRequest(String urladd) throws JSONException {
        final String url = ip + urladd;
        serverRequest.sendToServer(url, null,"DELETE");
    }
    public void deleteRequest(String urladd, Response.Listener<JSONObject> onResponse, Response.ErrorListener onError) throws JSONException {
        final String url = ip + urladd;
        serverRequest.sendToServer(url, null,"DELETE",onResponse,onError);
    }

    public void postRequestToFrag(JSONObject json, String urladd, Class toFragment, Boolean replace) throws JSONException {
        final String url = ip + urladd;
        isReplace = replace;
        sendFragment = toFragment;
        serverRequest.sendToServer(url, json, "POST");
    }
    public void getRequest(String urladd, Response.Listener<JSONObject> onResponse, Response.ErrorListener onError) throws JSONException {
        final String url = ip + urladd;
        serverRequest.sendToServer(url, null, "GET", onResponse, onError);
    }
    public void getRequest(String urladd) throws JSONException {
        final String url = ip + urladd;
        serverRequest.sendToServer(url, null, "GET");
    }
    public void getRequestToFrag(String urladd, Class toFragment) throws JSONException {
        final String url = ip + urladd;
        sendFragment = toFragment;
        serverRequest.sendToServer(url, null, "GET");
    }

    @Override
    public void onSuccess(String response) throws IllegalAccessException, InstantiationException {
        if (sendFragment != null)
            FragmentsManager.nextFragmentBundle(sendFragment, isReplace, response);
        else {
            Activity activity = (Activity) UIActivity.getUIContext();
            activity.onBackPressed();
            try {
                Helper.getAccountDataRequest();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        sendFragment = null;
    }

    @Override
    public void onError (String errorMessage) {
//        globalError = errorMessage;
//        try {
//            FragmentsManager.nextFragmentBundle(ErrorFragment.class, false, errorMessage);
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
    }
}
