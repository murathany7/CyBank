package com.example.experiment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class firstPage extends AppCompatActivity {
    String fullname;
    TextView welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        Intent intent=getIntent();
        fullname=intent.getStringExtra("fullName");
        welcome=findViewById(R.id.welcomeV);
        welcome.setText("Welcome "+ fullname);

    }
}