package com.example.experiment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Calendar end_calendar, start_calendar;
    long start_millis, end_millis, total_millis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void setTime() {
        Date date = new Date();   // given date
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        calendar.get(Calendar.HOUR_OF_DAY);

        final TextView qq = (TextView) findViewById(R.id.yazi);
        qq.setText(String.valueOf(calendar.get(Calendar.DATE)));
        TextView tv_countdown = (TextView) findViewById(R.id.yazi);
        Date aaa = new Date();
        start_calendar = Calendar.getInstance();
        end_calendar = Calendar.getInstance();
        start_calendar.setTime(aaa);
        if(aaa.getHours()<17) {
            end_calendar.set(2021, aaa.getMonth(), aaa.getDate(),17,0);
        } else if(aaa.getHours()<22) {
            end_calendar.set(2021, aaa.getMonth(), aaa.getDate(),22,0);
        } else {
            end_calendar.set(2021, aaa.getMonth(), aaa.getDate()+1,17,0);

        }

        start_millis = start_calendar.getTimeInMillis();
        end_millis = end_calendar.getTimeInMillis();
        total_millis = (end_millis - start_millis);

        new CountDownTimer(total_millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                millisUntilFinished -= TimeUnit.DAYS.toMillis(days);

                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);

                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                qq.setText(days + ":" + hours + ":" + minutes + ":" + seconds); //You can compute the millisUntilFinished on hours/minutes/seconds
            }

            @Override
            public void onFinish() {
                setTime();
            }
        }.start();
    }

    public void disable(View v) {
        v.setEnabled(false);
        Button button = (Button) v;
        View bb = findViewById(R.id.button2);
        final Button bbb = (Button) bb;
        final TextView qq = (TextView) findViewById(R.id.yazi);
        final TextView q = (TextView) qq;

        bbb.setText("changed");
        button.setText("disabled");
    }
    public void changeTime(View v) {
        setTime();
    }

    public void handleTxt(View v) {
        EditText t = findViewById(R.id.source);
        String input = t.getText().toString();
        Toast.makeText(this, input, Toast.LENGTH_LONG).show();
    }

//    public void lSet(View v) {
//        Intent i = new Intent(this, SettingsActivity.class);
//        startActivity(i);
//    }
}