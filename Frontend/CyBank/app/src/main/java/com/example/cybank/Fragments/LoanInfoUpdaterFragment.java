package com.example.cybank.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cybank.Logic.FragmentsManager;
import com.example.cybank.Logic.GeneralLogic;
import com.example.cybank.Logic.Helper;
import com.example.cybank.Network.ServerRequest;
import com.example.cybank.R;
import com.example.cybank.UIActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class LoanInfoUpdaterFragment extends Fragment {
    EditText ssn, age, income, loandesired;
    Button reqbut;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loan_info_updater, container, false);
        age = rootView.findViewById(R.id.age);
        ssn = rootView.findViewById(R.id.ssn);
        income = rootView.findViewById(R.id.income);
        loandesired = rootView.findViewById(R.id.loand);
        reqbut = rootView.findViewById(R.id.reqBut);
        ServerRequest serverRequest = new ServerRequest();
        final GeneralLogic logic = new GeneralLogic(serverRequest);

        reqbut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = "api/user/update";
                JSONObject json = new JSONObject();
                try {
                    json.put("uniqueUserId", Helper.getUID());
                    json.put("income", income.getText().toString());
                    json.put("ssn", ssn.getText().toString());
                    json.put("age", age.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Random rand = new Random();
                Log.d("tagoo", age.getText().toString() + "b");
                int iincome = Integer.parseInt(income.getText().toString());
                int loanwanted = Integer.parseInt(loandesired.getText().toString());
                if (age.getText().toString().length() < 2) {
                    Log.d("tagoo", age.getText().toString() + "b");
                    Toast.makeText(UIActivity.getUIContext(), "Please enter your age", Toast.LENGTH_LONG).show();

                } else if (Integer.parseInt(age.getText().toString()) < 21) {
                    Toast.makeText(UIActivity.getUIContext(), "You must be at least 21 to apply for a loan.", Toast.LENGTH_LONG).show();
                } else if (ssn.getText().toString().length() < 2) {
                    Toast.makeText(UIActivity.getUIContext(), "Please add a SSN", Toast.LENGTH_LONG).show();
                } else if (loandesired.getText().toString().length() < 2) {
                    Toast.makeText(UIActivity.getUIContext(), "Enter desired loan amount", Toast.LENGTH_LONG).show();
                } else if (ssn.getText().toString().length() < 2) {
                    Toast.makeText(UIActivity.getUIContext(), "Enter your SSN", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        logic.putRequest(json, url, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("onemli", response.toString());
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("onemli", error.toString());
                            }
                        });
                        if(iincome<100000&&loanwanted>20000) {
                            Toast.makeText(UIActivity.getUIContext(),"Please apply for a less amount. Income is not enough.",Toast.LENGTH_LONG).show();
                        } else {
                            int minimumPayment = loanwanted/12;
                            JSONObject njson = new JSONObject();

                            njson.put("minimumPayment", String.valueOf(minimumPayment));
                            njson.put("principleBalance", String.valueOf(loanwanted) );
                            njson.put("remainingBalance", String.valueOf(loanwanted));

                            logic.postRequest(njson, "api/loans/add/"+Helper.getUID(), new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        Helper.getLoanDataRequest();
                                        FragmentsManager.nextFragment(LoanFragment.class,true);
                                        Toast.makeText(UIActivity.getUIContext(),"Thanks for applying for a loan. You can now refresh Loans page to see details.",Toast.LENGTH_LONG).show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (java.lang.InstantiationException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("loanonm", error.toString());
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return rootView;
    }
}