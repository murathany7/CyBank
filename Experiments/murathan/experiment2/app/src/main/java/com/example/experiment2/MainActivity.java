package com.example.experiment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText edittextfullname,edittextemail;
    Button loginbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edittextfullname=findViewById(R.id.editTextTextPersonName);
        edittextemail=findViewById(R.id.editTextTextEmailAddress);
        loginbutton=findViewById(R.id.button);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,firstPage.class);
                startActivity(intent);
                String fullName = edittextfullname.getText().toString();
                intent.putExtra("fullName", fullName);
                startActivity(intent);
            }
        });
    }
}