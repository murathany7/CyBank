package com.example.cybank.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.cybank.Logic.FragmentsManager;
import com.example.cybank.Logic.Helper;
import com.example.cybank.R;
import com.example.cybank.UIActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoanFragment extends Fragment {
    public static AccountsFragment mInstance;
    FloatingActionButton upinfo;
    public static ListView listLoan;
    SwipeRefreshLayout swlayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loan, parent, false);
        listLoan=(ListView)rootView.findViewById(R.id.loan_lview);
        LoanAdapter adapter = new LoanAdapter(UIActivity.getUIContext(), Helper.getLoanData());
        listLoan.setAdapter(adapter);
        listLoan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Helper.setLoanPosition(position);
                try {
                    FragmentsManager.nextFragment(LoanInfoFragment.class, true);
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        swlayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperefresh);
        swlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoanAdapter adapter = new LoanAdapter(UIActivity.getUIContext(),Helper.getLoanData());
                listLoan.setAdapter(adapter);
                swlayout.setRefreshing(false);
            }
        });
        try {
            Helper.getLoanDataRequest();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        upinfo = rootView.findViewById(R.id.updateinfo);
        upinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FragmentsManager.nextFragment(LoanInfoUpdaterFragment.class, true);
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        Helper.getLoanData();
        return rootView;
    }

    static class LoanAdapter extends ArrayAdapter<JSONObject> {
        /**
         * Sets super with context and get requested array
         *  @param c
         * @param array*/
        LoanAdapter(Context c, ArrayList<JSONObject> array) {
            super(c, R.layout.loan_view, array);
        }

        /**
         * Sets list view with get requested array by parsing for json object based on position being filled
         *
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.loan_view, parent, false);
            TextView myName = row.findViewById(R.id.minimum_payment);
            TextView myNum = row.findViewById(R.id.min_payment);
            TextView myBal = row.findViewById(R.id.remainingBal);
            try {
                JSONObject obj = getItem(position);
                myName.setText("Min payment:"+obj.getString("minimumPayment"));
                myNum.setText("Loan amount:" + obj.getString("principleBalance"));

                String bal = obj.getString("remainingBalance");
                myBal.setText("Balance: $" + bal);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return row;
        }
    }
}