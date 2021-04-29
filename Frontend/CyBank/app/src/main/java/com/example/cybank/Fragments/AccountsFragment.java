package com.example.cybank.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * The is the fragment to manage accounts as a user
 */
public class AccountsFragment extends Fragment implements View.OnClickListener {
    public static AccountsFragment mInstance;
    public static ListView listAccount;
    SwipeRefreshLayout swlayout;
    Button deleteAccBut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accounts, parent, false);
        listAccount = (ListView) rootView.findViewById(R.id.listview_accounts);
        FloatingActionButton addAccountButton = (FloatingActionButton) rootView.findViewById(R.id.floatingadd);
        addAccountButton.setOnClickListener(this);
        getActivity().setTitle("Accounts");
        mInstance = this;

        swlayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperrefresh);
        swlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                AccountAdapter adapter = new AccountAdapter(UIActivity.getUIContext(), Helper.getAccountData());
                listAccount.setAdapter(adapter);
                swlayout.setRefreshing(false);
            }
        });
        deleteAccBut = (Button) rootView.findViewById(R.id.delAcc);
        if (!Helper.isAdmin)
            deleteAccBut.setVisibility(View.INVISIBLE);
        deleteAccBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerRequest serverRequest = new ServerRequest();
                final GeneralLogic logic = new GeneralLogic(serverRequest);
                try {
                    logic.deleteRequest("api/user/delete?uid=" + Helper.getUID(), response -> {
                        Log.d("TAG", "onResponse: aopkdpqw");
                        try {
                            FragmentsManager.nextFragment(SupportFragment.class, true);
                        } catch (java.lang.InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(UIActivity.getUIContext(), "User was deleted.", Toast.LENGTH_LONG).show();

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                Log.d("qqqqqqq", error.getLocalizedMessage());
                                FragmentsManager.nextFragmentBundle(ErrorFragment.class, false, error.getMessage());
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

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setListView();
    }

    @Override
    public void onClick(View v) {
        try {
            FragmentsManager.nextFragment(AddAccountFragment.class, false);
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * Adds get request to queue taking the response and converts it to Arraylist of
     * type JSONObject. Sets the ArrayList to the account adapter and looks for item
     * clicks position for TransactionView. Uses that position to set an JSONArray
     * for TransactionView
     */
    public static void setListView() {
        AccountAdapter adapter = new AccountAdapter(UIActivity.getUIContext(), Helper.getAccountData());
        listAccount.setAdapter(adapter);
        listAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Helper.setAccountPosition(position);
                    FragmentsManager.nextFragment(TransactionFragment.class, true);
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Adapter for account list
     */
    static class AccountAdapter extends ArrayAdapter<JSONObject> {
        /**
         * Sets super with context and get requested array
         * 
         * @param c
         * @param array
         */
        AccountAdapter(Context c, ArrayList<JSONObject> array) {
            super(c, R.layout.list_accounts, array);
        }

        /**
         * Sets list view with get requested array by parsing for json object based on
         * position being filled
         *
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_accounts, parent, false);
            TextView myName = row.findViewById(R.id.txt_account_name);
            TextView myNum = row.findViewById(R.id.txt_account_number);
            TextView myBal = row.findViewById(R.id.txt_account_balance);
            try {
                JSONObject obj = getItem(position);
                myName.setText(obj.getString("accountName"));
                myNum.setText("#" + obj.getString("uniqueAccountId"));
                DecimalFormat df = new DecimalFormat("#.00");
                Double bal = obj.getDouble("balance");
                String stringdouble = df.format(bal);
                myBal.setText("$" + stringdouble);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return row;
        }
    }

}