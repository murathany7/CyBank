

package com.example.cybank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.cybank.Logic.GeneralLogic;
import com.example.cybank.Network.ServerRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {
    EditText name, email, password1, password2;
    TextView error;
    Button signUpButton;
    Switch sw;
    public static SignUpActivity mInstance;

    /**
     * Creates screen to create a new user
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance=this;
        setContentView(R.layout.activity_sign_up_page);
        name = (EditText) findViewById(R.id.Name);
        email = (EditText) findViewById(R.id.email);
        error = (TextView) findViewById(R.id.error);
        password1 = (EditText) findViewById(R.id.Password1);
        password2 = (EditText) findViewById(R.id.Password2);
        signUpButton = (Button) findViewById(R.id.button);
        sw = (Switch) findViewById(R.id.switch1);
        ServerRequest serverRequest = new ServerRequest();
        final GeneralLogic logic = new GeneralLogic(serverRequest);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Upon clicking sends its data to backend
             * @param v
             */
            @Override
            public void onClick(View v) {
                String url = "api/user/add";
                String p1 = password1.getText().toString();
                String p2 = password2.getText().toString();
                String uname = name.getText().toString();
                String uemail = email.getText().toString();
                String type = "C";
                boolean isEmployee = sw.isChecked();
                if(isEmployee)
                    type = "S";
                Log.d("11111", type);
                if (!p1.equals(p2)) {
                    error.setText("Passwords are not the same");
                    Toast.makeText(SignUpActivity.this, "Passwords are not the same", Toast.LENGTH_LONG).show();
                } else if (p1.length() < 7) {
                    error.setText("Enter a password at least 8 characters");
                    Toast.makeText(SignUpActivity.this, "Enter a password at least 8 characters", Toast.LENGTH_LONG).show();
                } else if (uname.length() < 1) {
                    error.setText("Enter a valid name");
                    Toast.makeText(SignUpActivity.this, "Enter a valid name", Toast.LENGTH_LONG).show();
                } else if (uemail.length() < 1) {
                    error.setText("Enter a valid email");
                    Toast.makeText(SignUpActivity.this, "Enter a valid email", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("name", uname);
                        json.put("email", uemail);
                        json.put("password", p1);
                        json.put("accountType", type);
                        logic.postRequest(json, "api/user/add",
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                    }}, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        int i = 0;
                                    }
                                });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}