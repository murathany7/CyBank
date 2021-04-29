package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText username, password;
    Button login;
    TextView error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.usernameInput);
        password = (EditText) findViewById(R.id.passwordinput);
        login = (Button) findViewById(R.id.login);
        error = (TextView) findViewById(R.id.errorMessage);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().trim().equals("Raj") && password.getText().toString().trim().equals("Singh")){
                    error.setText("Login Success");

                }else{
                    error.setText("Login Failed");

                }
            }
        });
    }
}