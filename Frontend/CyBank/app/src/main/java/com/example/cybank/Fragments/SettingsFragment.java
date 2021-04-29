package com.example.cybank.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.cybank.Fragments.LoanFragment;
import com.example.cybank.Fragments.LoanInfoFragment;
import com.example.cybank.Fragments.LoanInfoUpdaterFragment;
import com.example.cybank.Logic.FragmentsManager;
import com.example.cybank.Logic.GeneralLogic;
import com.example.cybank.Logic.Helper;
import com.example.cybank.Network.ServerRequest;
import com.example.cybank.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsFragment extends Fragment {
    EditText email, password;
    Button send;
    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, parent, false);
        send = (Button) rootView.findViewById(R.id.submit);
        email = (EditText) rootView.findViewById(R.id.editTextEmailAddress);
        password = (EditText) rootView.findViewById(R.id.editTextPassword);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendToServer(email.getText().toString(), password.getText().toString());
            }
        });
        return rootView;
    }
    public static void sendToServer(String Email, String Password){
        String url = "api/user/update";
        JSONObject json = new JSONObject();
        try {
            json.put("email", Email);
            json.put("password", Password);
            json.put("uniqueUserId", Helper.getUID());
            ServerRequest serverRequest = new ServerRequest();
            final GeneralLogic logic = new GeneralLogic(serverRequest);
            logic.putRequest(json, url);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}