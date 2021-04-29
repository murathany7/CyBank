package com.example.cybank.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class LoanPaymentFragment extends Fragment {
    Spinner accSpin;
    EditText amount;
    int selectedAccountPosition = 0, fullLoan, remainingBal;
    Button pay;
    String ulid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loan_payment, container, false);
        amount = (EditText) rootView.findViewById(R.id.payAmount) ;
        pay = (Button) rootView.findViewById(R.id.payBalance);
        accSpin = (Spinner) rootView.findViewById(R.id.accSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UIActivity.getUIContext(), android.R.layout.simple_spinner_item, Helper.accountNameArr);
        accSpin.setAdapter(adapter);
        accSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedAccountPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerRequest serverRequest = new ServerRequest();
                final GeneralLogic logic = new GeneralLogic(serverRequest);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("amount","-"+ amount.getText().toString());
                    jsonObject.put("description", "Loan Payment");
                    logic.postRequest(jsonObject,"api/transactions/add/" + Helper.accountIdArr.get(selectedAccountPosition), new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
//                            try {
//                                Helper.getAccountDataRequest();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                            JSONObject json = Helper.getLoanData().get(Helper.getLoanPosition());
                            JSONObject toReturn = new JSONObject();
                            int minimumPayment;
                            try {
                                ulid = json.getString("uniqueLoanId");
                                remainingBal = json.getInt("remainingBalance");
                                fullLoan = json.getInt("principleBalance");
                                remainingBal = remainingBal - Integer.parseInt(amount.getText().toString());
                                Log.d("remainingbal", String.valueOf(remainingBal));
                                minimumPayment = remainingBal / 12;
                                Log.d("ahabu",json.getString("uniqueLoanId") );
                                toReturn.put("uniqueLoanId",ulid);
                                toReturn.put("remainingBalance",String.valueOf(remainingBal));
                                toReturn.put("minimumPayment",String.valueOf(minimumPayment));
                                toReturn.put("uniqueLoanId",json.getString("uniqueLoanId"));
                                toReturn.put("principleBalance",json.getString("principleBalance"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                logic.putRequest(toReturn,"api/loans/update", new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(UIActivity.getUIContext(),"Thanks for your payment. You are now re-directed into accounts page.",Toast.LENGTH_LONG).show();
                                        try {
                                            Helper.getLoanDataRequest();
                                            Helper.getAccountDataRequest();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
//                                        try {
//                                            FragmentsManager.nextFragment(LoanFragment.class,true);
//                                        } catch (java.lang.InstantiationException e) {
//                                            e.printStackTrace();
//                                        } catch (IllegalAccessException e) {
//                                            e.printStackTrace();
//                                        }
                                        Log.d("finaal", response.toString());
                                    }
                                },new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("finaal", error.toString());
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("hadee", error.toString());
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        return rootView;
    }
}