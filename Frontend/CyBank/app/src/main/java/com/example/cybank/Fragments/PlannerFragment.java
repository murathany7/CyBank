package com.example.cybank.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cybank.Logic.FragmentsManager;
import com.example.cybank.Logic.Helper;
import com.example.cybank.R;
import com.example.cybank.UIActivity;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Fragment to plan future transactions
 */
public class PlannerFragment extends Fragment implements View.OnClickListener {

    public static PlannerFragment mInstance;
    public static ListView listEvents;
    public static TextView datePickerTextView;
    static Date selected;
    private static CompactCalendarView compactCalendarView;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", /*Locale.getDefault()*/Locale.ENGLISH);
    private final SimpleDateFormat datetimeFormat = new SimpleDateFormat("h:mm a MM/dd/yyyy", /*Locale.getDefault()*/Locale.ENGLISH);
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", /*Locale.getDefault()*/Locale.ENGLISH);
    private final SimpleDateFormat dateFormatView = new SimpleDateFormat("E, dd MMM yyyy", /*Locale.getDefault()*/Locale.ENGLISH);

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.

    /**
     * Creates the calendar and let's user make selections
     * @param inflater
     * @param parent
     * @param savedInstanceState
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View rootView = inflater.inflate(R.layout.fragment_planner, parent, false);
        listEvents = (ListView)rootView.findViewById(R.id.listview_events);
        FloatingActionButton addAccountButton = (FloatingActionButton)rootView.findViewById(R.id.floatingadd);
        addAccountButton.setOnClickListener(this);
        datePickerTextView = (TextView)rootView.findViewById(R.id.date_picker_text_view);
        compactCalendarView = (CompactCalendarView)rootView.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setLocale(TimeZone.getDefault(), /*Locale.getDefault()*/Locale.ENGLISH);
        compactCalendarView.setShouldDrawDaysHeader(true);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                setSubtitle(dateFormatView.format(dateClicked));
                selected = dateClicked;
                setEventList();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setSubtitle(dateFormatView.format(firstDayOfNewMonth));
                selected = firstDayOfNewMonth;
                setEventList();
            }
        });
        selected = new Date();
        setCurrentDate(selected);
        mInstance = this;
        return rootView;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try {
            Helper.getEventDataRequest();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void setCurrentDate(Date date) {
        setSubtitle(dateFormatView.format(date));
        if (compactCalendarView != null) {
            compactCalendarView.setCurrentDate(date);
        }
    }

    private void setSubtitle(String subtitle) {
        if (datePickerTextView != null) {
            datePickerTextView.setText(subtitle);
        }
    }

    public static void setEvents() {
        if (Helper.getEventData() != null) {
            for (int i = 0; i < Helper.getEventData().size(); i++) {
                JSONObject obj = Helper.getEventData().get(i);
                String objDate = null;
                try {
                    objDate = obj.getString("submissionDate");
                    objDate = objDate.substring(0, objDate.length() - 14);
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(objDate);
                    long timeinmilli = date.getTime();
                    Event x = new Event(Color.RED, timeinmilli);
                    compactCalendarView.addEvent(x);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void setEventList(){
        PlannerAdapter adapter = new PlannerAdapter(UIActivity.getUIContext(), sortEventsList(Helper.getEventData(), selected), selected);
        listEvents.setAdapter(adapter);
        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private ArrayList<JSONObject> sortEventsList(ArrayList<JSONObject> list, Date selectedDate) {
        ArrayList<JSONObject> arraylist = new ArrayList<JSONObject>();
        for (int i = 0; i < list.size(); i++) {
            JSONObject obj = list.get(i);
            String objDate = null;
            try {
                objDate = obj.getString("submissionDate");
                objDate = objDate.substring(0, objDate.length() - 14);
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(objDate);
                objDate = dateFormat.format(date);
                if (date.equals(selectedDate)) {
                    arraylist.add(list.get(i));
                }
            } catch (ParseException | JSONException e) {
                e.printStackTrace();
            }

        }
        return arraylist;
    }

    @Override
    public void onClick(View v) {
        try {
            FragmentsManager.nextFragment(TransferFragment.class, true);
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    /**
     * Adapter for account list
     */
    class PlannerAdapter extends ArrayAdapter<JSONObject> {
        /**
         * Sets super with context and get requested array
         *  @param c
         * @param array*/
        PlannerAdapter(Context c, ArrayList<JSONObject> array, Date dateClicked) {
            super(c, R.layout.list_events, array);
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
            View row = layoutInflater.inflate(R.layout.list_events, parent, false);
            TextView myName = row.findViewById(R.id.txt_transaction_name);
            TextView myBal = row.findViewById(R.id.txt_transaction_balance);
            TextView myTime = row.findViewById(R.id.txt_transaction_time);
            ImageView images = row.findViewById(R.id.image);
            try {
                JSONObject obj = getItem(position);
                String objDate = obj.getString("submissionDate");
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(objDate);
                objDate = timeFormat.format(date);
                myTime.setText(objDate);
                myName.setText(obj.getString("description"));
                if (!obj.isNull("transactionType"))
                    images.setImageResource(obj.getInt("transactionType"));
                else
                    images.setImageResource(R.drawable.utilities);
                DecimalFormat df = new DecimalFormat("#.00");
                Double bal = obj.getDouble("amount");
                String stringdouble = df.format(bal);
                if (obj.getInt("amount") < 0) {
                    myBal.setTextColor(Color.parseColor("#ff0000"));
                    stringdouble = stringdouble.replaceAll("-", "");
                    myBal.setText("-$" + stringdouble);
                } else {
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