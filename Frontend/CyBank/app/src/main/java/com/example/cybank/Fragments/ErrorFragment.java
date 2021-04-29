package com.example.cybank.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cybank.R;
import com.example.cybank.UIActivity;

public class ErrorFragment extends Fragment {
    Button back;
    TextView error;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_error, container, false);
        back = (Button) rootView.findViewById(R.id.errorback);
        error = (TextView) rootView.findViewById(R.id.errortext);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        Bundle bundle = this.getArguments();
        String myE = null;
        if (bundle != null) {
            myE = bundle.getString("data");
        }
        error.setText(myE);
        return rootView;
    }

}