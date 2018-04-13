package com.quickboxdemo.activities;

import android.Manifest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.quickboxdemo.R;
import com.quickboxdemo.utils.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Srinivas on 2/21/2018.
 */

public class DashBoardActivity extends AppCompatActivity {


    private SharedPreferences mSharedPreferences;
    GridViewAdapter adapter;
    public ArrayList<String> symptoms_Array;

    private GridView gridView;
    private View btnGo;
    private ArrayList<String> selectedStrings;
    private static String[] numbers;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        pd = new ProgressDialog(DashBoardActivity.this);
        getSymptomsList();

        gridView = (GridView) findViewById(R.id.grid);
        btnGo = findViewById(R.id.button);
        selectedStrings = new ArrayList<>();
        symptoms_Array = new ArrayList<>();

        //set listener for Button event
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                me();
                Intent intent = new Intent(getApplicationContext(), OpponentsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, false);
                intent.putStringArrayListExtra("SELECTED_LETTER", selectedStrings);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void getSymptomsList() {

        RequestQueue requestQueue_Symptoms = Volley.newRequestQueue(this);
        String tag_string_req = "getSymptomsList";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        pd.setMessage("Loading");
        pd.show();


        StringRequest request = new StringRequest(Request.Method.GET, AppConfig.url_SypmtomsList, new Response.Listener<String>() {
            @Override
            public void onResponse(String responce) {
                pd.dismiss();
                Log.d("responce", "" + responce);
                if (null != responce) {
                    try {
                        JSONArray jsonArray = new JSONArray(responce);
                        if (jsonArray.length() > 0) {
                            symptoms_Array = new ArrayList<>(jsonArray.length());
                            numbers = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String symptom = jsonObject.getString("symptom");
                                symptoms_Array.add(symptom);
                                numbers[i] = symptoms_Array.get(i);
                            }

                            adapter = new GridViewAdapter(numbers, DashBoardActivity.this);
                            gridView.setAdapter(adapter);

                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                    int selectedIndex = adapter.selectedPositions.indexOf(position);
                                    if (selectedIndex > -1) {
                                        adapter.selectedPositions.remove(selectedIndex);
                                        ((GridItemView) v).display(false);
                                        selectedStrings.remove((String) parent.getItemAtPosition(position));
                                    } else {
                                        adapter.selectedPositions.add(position);
                                        ((GridItemView) v).display(true);
                                        selectedStrings.add((String) parent.getItemAtPosition(position));

                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "No Symptoms !", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; ");
                headers.put("Accept", "application/json;");

                return headers;
            }


        };
        requestQueue_Symptoms.add(request);
    }


    public void me() {
        String jsdhf = "";
        if (selectedStrings.size() > 0) {
            for (int i = 0; i < selectedStrings.size(); i++) {
                if (i < selectedStrings.size() - 1) {
                    jsdhf = jsdhf + selectedStrings.get(i) + ", ";
                } else {
                    jsdhf = jsdhf + selectedStrings.get(i) + ".";
                    Toast.makeText(getApplicationContext(), "Selected : " + jsdhf, Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}