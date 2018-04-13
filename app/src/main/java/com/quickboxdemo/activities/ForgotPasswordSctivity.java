package com.quickboxdemo.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.quickboxdemo.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Srinivas on 4/4/2018.
 */

public class ForgotPasswordSctivity extends AppCompatActivity {

    EditText edt_Email, edt_UserId;
    Button btn_Submit;
    AppCompatTextView txt_Login;
    CharSequence eMailId;
    String userId;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_activity);
        edt_UserId = (EditText) findViewById(R.id.edt_UserId);
        edt_Email = (EditText) findViewById(R.id.edt_Email);
        btn_Submit = (Button) findViewById(R.id.btn_submit);
        txt_Login = (AppCompatTextView) findViewById(R.id.txt_FLogin);

        progressDialog=new ProgressDialog(this);

        txt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_Login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent_Login);
            }
        });

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eMailId = edt_Email.getText().toString();
                userId = edt_UserId.getText().toString();
                if (!edt_Email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                    edt_Email.setError("Invalid Email Address");
                } else if ("".equals(userId)) {
                    showToast("Please Enter UserId !");
                } else {
                    forgotMethod(userId, eMailId.toString());
                }

            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    public void forgotMethod(final String userId, final String Email) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        progressDialog.setMessage("Loading");
        progressDialog.show();

        Map<String, String> params = new HashMap<String, String>();

        params.put("userId", userId);
        params.put("email", Email);
        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConfig.url_FORGOT_PASSWORD, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();

                try {
                    if (null != response.toString()) {
                        Log.d("regresponce", "" + response.toString());
                        String reg_responce = response.getString("response");
                        if (null != reg_responce) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ForgotPasswordSctivity.this);
                            alertDialog.setCancelable(false);
                            alertDialog.setTitle("PASSWORD");
                            alertDialog.setMessage("Password is : " + reg_responce);

                            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent_Login = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent_Login);
                                    dialogInterface.cancel();
                                    finish();

                                }
                            });

                            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            alertDialog.show();
                        } else {
                            showToast("User Password recreation Failed please try again !");
                        }

                    } else {
                        showToast("User Password recreation Failed please try again !");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("User Password recreation Failed please try again !");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error in response", "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(req);

    }

    public void showToast(final String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
