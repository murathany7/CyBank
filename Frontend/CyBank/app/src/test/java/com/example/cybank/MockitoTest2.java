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

public class MockitoTest2 {
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void LoanTest() throws Exception {
        GeneralLogic logic = mock(GeneralLogic.class);
        JSONObject json = new JSONObject();
        final JSONObject[] holder = {new JSONObject()};
        try {
            json.put("name", null);
            json.put("email", null);
            json.put("principleAmount", null);
            json.put("minimumBalance", null);
            Mockito.doThrow(new ExceptionInInitializerError()).when(logic).postRequest(json, "api/loans/add",
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


    @InjectMocks
    Helper helper;

    @Test
    public void getLoansTest() throws Exception {
        Helper.setUID("99999");
        Assert.assertEquals(Helper.getUID(),"99999");
        Helper.setIsAdmin(false);
        Assert.assertEquals(Helper.getIsAdmin(),false);
        Helper.setUID("");
        Assert.assertEquals(null,Helper.getLoanData());
    }

    @Test
    public void setLoanTest() throws Exception {
        ArrayList<JSONObject> emptydata = new ArrayList<JSONObject>();
        JSONObject t = new JSONObject();
        t.put("principleBalance","");
        t.put("uniqueLoanId","");
        t.put("remainingBalance","");
        t.put("minimumPayment","");
        emptydata.add(t);
        Helper.setLoanStrings(emptydata);
        Assert.assertEquals(null,Helper.getLoanData());
    }
}
