package com.example.cybank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cybank.Logic.GeneralLogic;
import com.example.cybank.Logic.Helper;
import com.example.cybank.Network.ServerRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class UserReport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);
        TextView numAcc,numTracs,numLoans,highestAcc,lowestAcc,aveTracs,aveAcc;
        aveTracs = (TextView) findViewById(R.id.avTrac);
        numAcc = (TextView) findViewById(R.id.numAccs);
        numLoans = (TextView) findViewById(R.id.numLoans);
        numTracs = (TextView) findViewById(R.id.numTracs);
        lowestAcc = (TextView) findViewById(R.id.lowestAccR);
        highestAcc = (TextView) findViewById(R.id.highestAccR);
        aveAcc = (TextView) findViewById(R.id.accBalR);
        ServerRequest serverRequest = new ServerRequest();
        final GeneralLogic logic = new GeneralLogic(serverRequest);
        try {
            logic.getRequest("api/user/report?id=" + Helper.getUID(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                aveTracs.setText(response.getString("averageTransactionAmount"));
                                numLoans.setText(response.getString("numLoans"));
                                lowestAcc.setText(response.getString("lowestValueAccount"));
                                highestAcc.setText(response.getString("highestValueAccount"));
                                numTracs.setText(response.getString("numTransactions"));
                                aveAcc.setText(response.getString("averageAccountBalance"));
                                numAcc.setText(response.getString("numAccounts"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}