package com.example.cybank;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.cybank.Logic.GeneralLogic;
import com.example.cybank.Logic.Helper;
import com.example.cybank.Network.ServerRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Request Error";
    EditText email, password;
    Button login, signup;
    TextView error;
    private static LoginActivity mInstance;
    private RequestQueue mRequestQueue;

    public static synchronized LoginActivity getInstance() {
        return mInstance;
    }

    /**
     * Gets current volley request queue
     * 
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Adds incoming volley requests to queue
     * 
     * @param req
     * @param tag
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * Creates screen for logging in
     * 
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mInstance = this;
        email = (EditText) findViewById(R.id.emailInput);
        password = (EditText) findViewById(R.id.passwordinput);
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
        ServerRequest serverRequest = new ServerRequest();
        final GeneralLogic logic = new GeneralLogic(serverRequest);

        login.setOnClickListener(new View.OnClickListener() {
            /**
             * Upon clicking verifies data with backend before sending user to AccountsView
             * 
             * @param v
             */
            @Override
            public void onClick(View v) {
                Log.d("buum", "onClick: aaa");
                String url = "api/user/login";
                JSONObject json = new JSONObject();
                try {
                    json.put("email", email.getText().toString());
                    json.put("password", password.getText().toString());
                    // json.put("email", "test55@gmail.com");
                    // json.put("password", "123456789");
                    // json.put("email", "admin@gmail.com");
                    // json.put("password", "123456789");

                    logic.postRequest(json, url, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("tagii", response.toString());
                            try {
                                if (response.getString("uniqueUserId") != null
                                        && response.getString("accountType") != null) {
                                    Helper.setUID(response.getString("uniqueUserId"));
                                    Helper.setName(response.getString("name"));
                                    if (response.getString("accountType").equals("C")) {
                                        Helper.setIsAdmin(false);
                                    } else {
                                        Helper.setIsAdmin(true);
                                    }
                                    Intent intent = new Intent(LoginActivity.this, UIActivity.class);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("boik", error.toString());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        /**
         * Upon clicking sends user to sighnup screen
         */
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

}