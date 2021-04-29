package com.example.cybank.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.cybank.Logic.FragmentsManager;
import com.example.cybank.Logic.GeneralLogic;
import com.example.cybank.Logic.Helper;
import com.example.cybank.Network.ServerRequest;
import com.example.cybank.R;

import org.json.JSONException;
import org.json.JSONObject;


public class AddAccountFragment extends Fragment implements View.OnClickListener {
    EditText accountName, accountInitialBal;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_account, parent, false);
        ImageButton forward = (ImageButton) rootView.findViewById(R.id.forwardbutton);
        ImageButton back = (ImageButton) rootView.findViewById(R.id.backbutton);
        accountName = (EditText) rootView.findViewById(R.id.accountName);
        accountInitialBal = (EditText) rootView.findViewById(R.id.initialBalance);
        forward.setOnClickListener(this);
        back.setOnClickListener(this);
        ServerRequest serverRequest = new ServerRequest();
        final GeneralLogic logic = new GeneralLogic(serverRequest);
        return rootView;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backbutton:
                getActivity().onBackPressed();
                break;
            case R.id.forwardbutton:
                String url = "api/accounts/add?initialBalance="+ accountInitialBal.getText().toString();
                JSONObject json = new JSONObject();
                try {
                    json.put("accountName", accountName.getText().toString());
                    //json.put("", accountInitialBal.getText().toString());
                    json.put("accountType", "C");
                    json.put("uniqueUserId", Helper.getUID());
                    Helper.sendDataToFrag(json, url, null, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            break;

        }
    }
}