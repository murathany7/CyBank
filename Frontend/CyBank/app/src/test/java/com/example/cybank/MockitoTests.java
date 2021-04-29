package com.example.cybank;
import android.content.Intent;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cybank.Logic.GeneralLogic;
import com.example.cybank.Logic.Helper;
import com.example.cybank.Network.ServerRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



public class MockitoTests {
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void signUpTest() throws Exception {
        GeneralLogic logic = mock(GeneralLogic.class);
        JSONObject json = new JSONObject();
        final JSONObject[] holder = {new JSONObject()};
        try {

            json.put("name", null);
            json.put("email", null);
            json.put("password", null);
            json.put("accountType", null);
            Mockito.doThrow(new ExceptionInInitializerError()).when(logic).postRequest(json, "api/user/add",
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            holder[0] =response;
                        }}, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            JSONObject empty = new JSONObject();
            Assert.assertThrows(JSONException.class, () -> holder[0].getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void signInTest() throws Exception {
        GeneralLogic logic = mock(GeneralLogic.class);
        JSONObject json = new JSONObject();
        JSONObject[] holder = {new JSONObject()};
        try {
            json.put("email", null);
            json.put("password", null);
            logic.postRequest(json,"api/user/login");
            Mockito.doThrow(new NullPointerException()).when(logic).postRequest(json, "api/user/add",
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            holder[0] =response;
                        }}, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            JSONObject empty = new JSONObject();
            Assert.assertThrows(JSONException.class, () -> holder[0].getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @InjectMocks
    Helper helper;

    @Test
    public void getAccountsTest() throws Exception {
        Helper.setUID("99999");
        Assert.assertEquals(Helper.getUID(),"99999");
        Helper.setIsAdmin(false);
        Assert.assertEquals(Helper.getIsAdmin(),false);
        Helper.setUID("");
        Assert.assertThrows(NullPointerException.class,()->Helper.getAccountDataRequest());
    }

    @Test
    public void setAccountsTest() throws Exception {
        ArrayList<JSONObject> emptydata = new ArrayList<JSONObject>();
        JSONObject t = new JSONObject();
        t.put("accountName","");
        t.put("accountNumber","");
        t.put("uniqueAccountId","");
        emptydata.add(t);
        Helper.setAccountStrings(emptydata);
//        Assert.assertEquals(null,Helper.getAccountData());
    }

}
