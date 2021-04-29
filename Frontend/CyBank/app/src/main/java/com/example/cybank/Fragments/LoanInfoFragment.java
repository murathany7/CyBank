package com.example.cybank.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cybank.Logic.FragmentsManager;
import com.example.cybank.Logic.GeneralLogic;
import com.example.cybank.Logic.Helper;
import com.example.cybank.Network.ServerRequest;
import com.example.cybank.R;
import com.example.cybank.UIActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoanInfoFragment extends Fragment {
    TextView minPayment,balance,fullLoan;
    Button payBal,forgiveLoan;
    String uniqueLoanId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loan_info, parent, false);

        minPayment = (TextView) rootView.findViewById(R.id.mini_payment);
        balance = (TextView) rootView.findViewById(R.id.f_balance);
        fullLoan = (TextView) rootView.findViewById(R.id.fullAmount);
        forgiveLoan = (Button) rootView.findViewById(R.id.forgiveLoan);
        payBal = (Button) rootView.findViewById(R.id.addBal);
        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        JSONObject jsonObject = Helper.getLoanData().get(Helper.getLoanPosition());
        try {
            uniqueLoanId = jsonObject.getString("uniqueLoanId");
            minPayment.setText("$"+jsonObject.getString("minimumPayment"));
            balance.setText("$"+jsonObject.getString("remainingBalance"));
            fullLoan.setText("$"+jsonObject.getString("principleBalance"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        payBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FragmentsManager.nextFragment(LoanPaymentFragment.class, true);
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        if(!Helper.isAdmin)
            forgiveLoan.setVisibility(View.INVISIBLE);
        forgiveLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerRequest serverRequest = new ServerRequest();
                final GeneralLogic logic = new GeneralLogic(serverRequest);
                try {
                    logic.deleteRequest("api/loans/delete/"+uniqueLoanId, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Helper.getLoanDataRequest();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                FragmentsManager.nextFragment(LoanFragment.class, true);
                            } catch (java.lang.InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(UIActivity.getUIContext(),"Loan was forgiven.",Toast.LENGTH_LONG).show();

                        }}, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                FragmentsManager.nextFragment(LoanFragment.class, true);
                                Toast.makeText(UIActivity.getUIContext(),"Loan was forgiven. Refresh the page to see changes",Toast.LENGTH_LONG).show();
                            } catch (java.lang.InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}