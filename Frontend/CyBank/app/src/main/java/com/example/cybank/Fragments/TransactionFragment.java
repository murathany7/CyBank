package com.example.cybank.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cybank.Logic.FragmentsManager;
import com.example.cybank.Logic.GeneralLogic;
import com.example.cybank.Logic.Helper;
import com.example.cybank.Network.ServerRequest;
import com.example.cybank.R;
import com.example.cybank.UIActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This is the fragment to manage transactions
 */
public class TransactionFragment extends Fragment implements View.OnClickListener {
    public static TransactionFragment mInstance;
    public static ListView listTransactions;
    Button delBut;
    SwipeRefreshLayout swlayout;
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View rootView = inflater.inflate(R.layout.fragment_transaction, parent, false);
        listTransactions = (ListView)rootView.findViewById(R.id.listview_transactions);
        FloatingActionButton addAccountButton = (FloatingActionButton)rootView.findViewById(R.id.floatingadd);
        addAccountButton.setOnClickListener(this);
        mInstance = this;
        swlayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperRefresher);
        swlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Helper.getAccountDataRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int position = Helper.getAccountPosition();
                TransactionAdapter adapter = null;
                try {
                    adapter = new TransactionAdapter(UIActivity.getUIContext(), Helper.JSONArraytoArraylistObj(Helper.getAccountData().get(position).getJSONArray("transactions")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listTransactions.setAdapter(adapter);
                swlayout.setRefreshing(false);
            }
        });
        delBut = (Button) rootView.findViewById(R.id.delTAcc);
        if(!Helper.isAdmin)
            delBut.setVisibility(View.INVISIBLE);
        delBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerRequest serverRequest = new ServerRequest();
                final GeneralLogic logic = new GeneralLogic(serverRequest);
                try {
                    logic.deleteRequest("api/accounts/delete?aid="+Helper.accountIdArr.get(Helper.getAccountPosition()), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Helper.getAccountDataRequest();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(UIActivity.getUIContext(),"Account was deleted.",Toast.LENGTH_LONG).show();

                        }}, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                Log.d("taqqqqq", error.getLocalizedMessage());
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

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getActivity().setTitle("Transactions");
        try {
            setListView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        TransferFragment nextFrag = new TransferFragment();
        FragmentsManager.addFragment(this.getActivity(), nextFrag, true);

    }
    /**
     * Adds get request to queue taking the response and converts it to Arraylist of type JSONObject.
     * Sets the ArrayList to the account adapter and looks for item clicks position for TransactionView. Uses that position to set an JSONArray for TransactionView
     */
    public void setListView() throws JSONException {
        int position = Helper.getAccountPosition();
        TransactionAdapter adapter = new TransactionAdapter(UIActivity.getUIContext(), Helper.JSONArraytoArraylistObj(Helper.getAccountData().get(position).getJSONArray("transactions")));
        listTransactions.setAdapter(adapter);
        listTransactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Helper.isAdmin) {
                JSONObject obj = (JSONObject) parent.getItemAtPosition(position);
                String tid = null;
                try {
                    tid = obj.get("transactionId").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    Log.d("tiiiid", tid);
                ServerRequest serverRequest = new ServerRequest();
                final GeneralLogic logic = new GeneralLogic(serverRequest);
                try {
                    logic.deleteRequest("api/transactions/delete?tid="+tid);
                    Helper.getAccountDataRequest();
                    Toast.makeText(UIActivity.getUIContext(),"Transaction was refunded.",Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }}
        });
    }


    /**
     * Adapter for account list
     */
    static class TransactionAdapter extends ArrayAdapter<JSONObject> {
        /**
         * Sets super with context and get requested array
         *  @param c
         * @param array*/
        TransactionAdapter(Context c, ArrayList<JSONObject> array) {
            super(c, R.layout.list_transactions, array);
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
            View row = layoutInflater.inflate(R.layout.list_transactions, parent, false);
            TextView myName = row.findViewById(R.id.txt_transaction_name);
            TextView myBal = row.findViewById(R.id.txt_transaction_balance);
            TextView myTime = row.findViewById(R.id.txt_transaction_time);
            ImageView images = row.findViewById(R.id.image);
            try {
                JSONObject obj = getItem(position);
                myName.setText(obj.getString("description"));
                String time = obj.getString("submissionDate");
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(time);
                SimpleDateFormat simpleformat = new SimpleDateFormat("h:mm a MM/dd/yyyy");
                time = simpleformat.format(date);
                myTime.setText(time);
                if (!obj.isNull("transactionType")) {
                    images.setImageResource(obj.getInt("transactionType"));
                }else
                    images.setImageResource(R.drawable.utilities);
                DecimalFormat df = new DecimalFormat("#.00");
                Double bal = obj.getDouble("amount");
                String stringdouble = df.format(bal);
                if (obj.getInt("amount") < 0){
                    myBal.setTextColor(Color.parseColor("#ff0000"));
                    stringdouble = stringdouble.replaceAll("-","");
                    myBal.setText("-$" + stringdouble);
                }else{
                    myBal.setTextColor(Color.parseColor("#00cf60"));
                    myBal.setText("+$" + stringdouble);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            return row;
        }
    }
}