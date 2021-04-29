package com.example.cybank.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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

import java.util.ArrayList;

/**
 * Support team initial page fragment.
 */
public class SupportFragment extends Fragment implements View.OnClickListener {
    static ArrayList<JSONObject> Users;
    String[] userTextArr ;
    String[] accountIdArr;
    int userIndex =0;

    /**
     * When this activity is created, it loads all the user data and navigates to the user to perform moderator actions.
     * @param inflater
     * @param parent
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_support, parent, false);
        Spinner dynamicSpinner = (Spinner) rootView.findViewById(R.id.spinner);
        Button goBut = (Button) rootView.findViewById(R.id.goAccBut);
        goBut.setOnClickListener(this);
        ServerRequest serverRequest = new ServerRequest();
        final GeneralLogic logic = new GeneralLogic(serverRequest);
        try {
            logic.getRequest("api/user/get?userType=C",
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Users = Helper.JSONObjecttoArraylistObj("data", response);
                                userTextArr = new String[Users.size()];
                                accountIdArr = new String[Users.size()];
                                for (int i = 0; i < Users.size(); i++){
                                    JSONObject obj = Users.get(i);
                                    try {
                                        userTextArr[i] = obj.getString("email");
                                        accountIdArr[i] = obj.getString("uniqueUserId");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                };
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UIActivity.getUIContext(), android.R.layout.simple_spinner_item, userTextArr);

                                dynamicSpinner.setAdapter(adapter);

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
        };
        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userIndex=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    /**
     * this is the button onclick function to get all the user info to list to support team.
     * @param v
     */
    @Override
    public void onClick(View v) {
        Log.d("TAG", accountIdArr[userIndex]);
        switch (v.getId()) {
            case R.id.goAccBut:
                Helper.setUID(accountIdArr[userIndex]);
                Helper.setCang(true);
                Intent intent = new Intent(getContext(),UIActivity.class);
                startActivity(intent);
//                try {
//                    Helper.getAccountDataRequest();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Intent intent = new Intent(getContext(),UIActivity.class);
//                startActivity(intent);
                break;

        }
    }
}