package com.example.cybank.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.cybank.Logic.Helper;
import com.example.cybank.R;
import com.example.cybank.UIActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Fragment that contains transfers.
 */
public class TransferFragment extends Fragment implements View.OnClickListener {
    EditText transferName, transferAmount;
    Button datePicker;
    public static Spinner dynamicSpinner;
    int selectedAccountPosition = 0;
    Date selectedDate;

    /**
     * Inflate the layout for this fragment
     * @param inflater
     * @param parent
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_transfer, parent, false);
        ImageButton forward = (ImageButton) rootView.findViewById(R.id.forwardbutton);
        ImageButton back = (ImageButton) rootView.findViewById(R.id.backbutton);
        datePicker = (Button) rootView.findViewById(R.id.datePicker);
        transferName = (EditText) rootView.findViewById(R.id.transferName);
        transferAmount = (EditText) rootView.findViewById(R.id.transferAmount);
        dynamicSpinner = (Spinner) rootView.findViewById(R.id.accountspinner);
        forward.setOnClickListener(this);
        back.setOnClickListener(this);
        datePicker.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UIActivity.getUIContext(), android.R.layout.simple_spinner_item, Helper.accountNameArr);
        dynamicSpinner.setAdapter(adapter);

        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedAccountPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedAccountPosition = Helper.getAccountPosition();
            }
        });
        return rootView;
    }

    /**
     * This is the button onclick to submit the transfer and to navigate back.
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backbutton:
                getActivity().onBackPressed();
                break;
            case R.id.forwardbutton:
                String url = "api/transactions/add/" + Helper.accountIdArr.get(selectedAccountPosition);
                JSONObject json = new JSONObject();
                try {
                    json.put("amount", transferAmount.getText().toString());
                    json.put("description", transferName.getText().toString());
                    json.put("submissionDate", selectedDate);
                    Helper.sendDataToFrag(json, url, null, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.datePicker:
                selectedDate = getUserDate();
                break;

        }
    }

    /**
     * This method is to get the date that user selected
     * @return Time that was selected
     */
    public Date getUserDate(){
        Calendar cal = Calendar.getInstance();
        if (PlannerFragment.selected != null)
            cal.setTime(PlannerFragment.selected);
        else
            cal.setTime(new Date());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(UIActivity.getUIContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(UIActivity.getUIContext(),
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        cal.set(year, monthOfYear, dayOfMonth, hourOfDay, minute);
                                    }
                                }, hour, min, false);
                        timePickerDialog.show();
                    }
                }, year, month, day);
        datePickerDialog.show();
        return cal.getTime();
    }


}