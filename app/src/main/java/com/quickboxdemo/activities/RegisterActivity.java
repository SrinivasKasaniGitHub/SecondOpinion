package com.quickboxdemo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.quickboxdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Srinivas on 3/20/2018.
 */

public class RegisterActivity extends AppCompatActivity {

    //D=Doctor, P=patient;

    AppCompatButton btn_doctor, btn_patient;
    RadioGroup radioGroup_Dp;
    RadioButton radioButton_doctor, radioButton_patient;
    LinearLayout lyt_doctor, lyt_patient;
    AppCompatTextView txt_Login;
    AppCompatButton btn_Register;
    EditText edt_Txt_F_Name, edt_Txt_L_Name, edt_Txt_Speciality, edt_Txt_Location,
            edt_Txt_DoctorId, edt_Txt_UserName, edt_Txt_Pwd, edt_Txt_CPWD, edtTxt_DEmail;

    EditText edtTxt_pfName, edtTxt_plName, edtTxt_pUName,edtTxt_PEmail, edtTxt_ppwd, edtTxt_pRpwd;
    String d_F_Name, d_L_Name, d_Speciality, d_Location, d_DId, d_UserName, d_Pwd, d_Cpwd;
    String p_F_Name, p_L_Name, p_UserName, p_Pwd, p_Cpwd;
    String userType = "Doctor";
    String cond_Type = "D";
    ProgressDialog progressDialog;

    String d_eMailId, p_eMailId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        progressDialog = new ProgressDialog(this);
        initviews();
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (cond_Type.equals("D")) {

                    d_F_Name = edt_Txt_F_Name.getText().toString();
                    d_L_Name = edt_Txt_L_Name.getText().toString();
                    d_Speciality = edt_Txt_Speciality.getText().toString();
                    d_Location = edt_Txt_Location.getText().toString();
                    d_DId = edt_Txt_DoctorId.getText().toString();
                    d_UserName = edt_Txt_UserName.getText().toString();
                    d_eMailId = edtTxt_DEmail.getText().toString();
                    d_Pwd = edt_Txt_Pwd.getText().toString();
                    d_Cpwd = edt_Txt_CPWD.getText().toString();


                    if ("".equals(d_F_Name)) {
                        edt_Txt_F_Name.requestFocus();
                        edt_Txt_F_Name.setError("Please Enter the First Name !");
                    } else if ("".equals(d_L_Name)) {
                        edt_Txt_L_Name.requestFocus();
                        edt_Txt_L_Name.setError("Please Enter the Last Name !");
                    } else if ("".equals(d_Speciality)) {
                        edt_Txt_Speciality.requestFocus();
                        edt_Txt_Speciality.setError("Please Enter the Speciality!");
                    } else if ("".equals(d_Location)) {
                        edt_Txt_Location.requestFocus();
                        edt_Txt_Location.setError("Please Enter the Location!");
                    } else if ("".equals(d_DId)) {
                        edt_Txt_DoctorId.requestFocus();
                        edt_Txt_DoctorId.setError("Please enter your Id !");
                    } else if ("".equals(d_UserName)) {
                        edt_Txt_UserName.requestFocus();
                        edt_Txt_UserName.setError("Please Enter UserName !");
                    } else if (!d_eMailId.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                        edtTxt_DEmail.requestFocus();
                        edtTxt_DEmail.setError("InValid Email !");
                    } else if ("".equals(d_Pwd)) {
                        edt_Txt_Pwd.requestFocus();
                        edt_Txt_Pwd.setError("Please Enter the Password !");
                    } else if ("".equals(d_Pwd) || !d_Pwd.equals(d_Cpwd)) {
                        edt_Txt_CPWD.requestFocus();
                        edt_Txt_CPWD.setError("Passwords are Not matched !");
                    } else {
                        register(d_F_Name, d_L_Name, d_Speciality, d_Location, d_DId, d_UserName, d_Pwd, userType,d_eMailId);
                    }
                } else if (cond_Type.equals("P")) {
                    // Patient

                    p_F_Name = edtTxt_pfName.getText().toString();
                    p_L_Name = edtTxt_plName.getText().toString();
                    p_UserName = edtTxt_pUName.getText().toString();
                    p_eMailId=edtTxt_PEmail.getText().toString();
                    p_Pwd = edtTxt_ppwd.getText().toString();
                    p_Cpwd = edtTxt_pRpwd.getText().toString();

                    if ("".equals(p_F_Name)) {
                        edtTxt_pfName.requestFocus();
                        edtTxt_pfName.setError("Please Enter the First Name !");
                    } else if ("".equals(p_L_Name)) {
                        edtTxt_plName.requestFocus();
                        edtTxt_plName.setError("Please Enter the Last Name !");
                    } else if ("".equals(p_UserName)) {
                        edtTxt_pUName.requestFocus();
                        edtTxt_pUName.setError("Please Enter UserName !");
                    } else if (!p_eMailId.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                        edtTxt_PEmail.requestFocus();
                        edtTxt_PEmail.setError("InValid Email !");
                    }else if ("".equals(p_Pwd)) {
                        edtTxt_ppwd.requestFocus();
                        edtTxt_ppwd.setError("Please Enter the Password !");
                    } else if ("".equals(p_Pwd) || !p_Pwd.equals(p_Cpwd)) {
                        edtTxt_pRpwd.requestFocus();
                        edtTxt_pRpwd.setError("Passwords are Not matched !");
                    } else {
                        register(p_F_Name, p_L_Name, "", "", "", p_UserName, p_Pwd, userType,p_eMailId);
                    }
                }


            }
        });

        txt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        radioGroup_Dp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkeId) {
                switch (checkeId) {
                    case R.id.radioGRpDoctor:
                        lyt_doctor.setVisibility(View.VISIBLE);
                        lyt_patient.setVisibility(View.GONE);
                        userType = "Doctor";
                        cond_Type = "D";
                        break;
                    case R.id.radioGRpPatient:
                        lyt_doctor.setVisibility(View.GONE);
                        lyt_patient.setVisibility(View.VISIBLE);
                        userType = "Patient";
                        cond_Type = "P";
                        break;
                    default:
                        break;

                }

            }
        });

    }

    public void initviews() {
        btn_doctor = (AppCompatButton) findViewById(R.id.btn_doctor);
        btn_patient = (AppCompatButton) findViewById(R.id.btn_patient);
        radioGroup_Dp = (RadioGroup) findViewById(R.id.radioGRpDP);
        radioButton_doctor = (RadioButton) findViewById(R.id.radioGRpDoctor);
        radioButton_patient = (RadioButton) findViewById(R.id.radioGRpPatient);
        lyt_doctor = (LinearLayout) findViewById(R.id.lyt_Doctor);
        lyt_patient = (LinearLayout) findViewById(R.id.lyt_Patient);
        txt_Login = (AppCompatTextView) findViewById(R.id.txt_login);
        btn_Register = (AppCompatButton) findViewById(R.id.btn_Register);
        edt_Txt_F_Name = (EditText) findViewById(R.id.edtTxt_fName);
        edt_Txt_L_Name = (EditText) findViewById(R.id.edtTxt_lName);
        edt_Txt_Speciality = (EditText) findViewById(R.id.edtTxt_speclty);
        edt_Txt_Location = (EditText) findViewById(R.id.edtTxt_location);
        edt_Txt_DoctorId = (EditText) findViewById(R.id.edtTxt_dID);
        edt_Txt_UserName = (EditText) findViewById(R.id.edtTxt_UName);
        edtTxt_DEmail = (EditText) findViewById(R.id.edtTxt_DEmail);
        edt_Txt_Pwd = (EditText) findViewById(R.id.edtTxt_pwd);
        edt_Txt_CPWD = (EditText) findViewById(R.id.edtTxt_Rpwd);

        //Patient

        edtTxt_pfName = (EditText) findViewById(R.id.edtTxt_pfName);
        edtTxt_plName = (EditText) findViewById(R.id.edtTxt_plName);
        edtTxt_pUName = (EditText) findViewById(R.id.edtTxt_pUName);
        edtTxt_PEmail=(EditText)findViewById(R.id.edtTxt_PEmail);
        edtTxt_ppwd = (EditText) findViewById(R.id.edtTxt_ppwd);
        edtTxt_pRpwd = (EditText) findViewById(R.id.edtTxt_pRpwd);


    }

    public void showToast(final String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void userRegister(final String dF_Name, final String dL_Name, final String dSpeciality,
                             final String dLocation, final String dDId, final String dUserName, final String dPwd) {
        String tag_string_req = "doctor_Registration";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("responce", "" + s.toString());
                try {
                    JSONArray array = new JSONArray(s);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            public String getPostBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; ");
                headers.put("Accept", "application/json;");

                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                //d_F_Name, d_L_Name, d_Speciality, d_Location, d_DId, d_UserName, d_Pwd, d_Cpwd

                params.put("firstName", dF_Name);
                params.put("lastName", dL_Name);
                params.put("speciality", dSpeciality);
                params.put("city", dLocation);
                params.put("docId", dDId);
                params.put("userId", dUserName);
                params.put("password", dPwd);
                params.put("userType", userType);


                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req);
    }


    public void register(final String dF_Name, final String dL_Name, final String dSpeciality,
                         final String dLocation, final String dDId, final String dUserName,
                         final String dPwd, final String userType,final String email) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        progressDialog.setMessage("Loading");
        progressDialog.show();

        Map<String, String> params = new HashMap<String, String>();

        //d_F_Name, d_L_Name, d_Speciality, d_Location, d_DId, d_UserName, d_Pwd, d_Cpwd

        params.put("firstName", dF_Name);
        params.put("lastName", dL_Name);
        params.put("speciality", dSpeciality);
        params.put("city", dLocation);
        params.put("docId", dDId);
        params.put("userId", dUserName);
        params.put("password", dPwd);
        params.put("userType", userType);
        params.put("email", email);


        JSONObject jsonObject = new JSONObject(params);


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_REGISTER, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();
                try {
                    if (null != response.toString()) {
                        Log.d("regresponce", "" + response.toString());
                        String reg_Responce_Code = response.getString("responseCode");
                        String reg_responce = response.getString("response");
                        if (reg_Responce_Code.equals("R400")) {
                            showToast(reg_responce);
                        } else if (reg_Responce_Code.equals("R000")) {
                            showToast(reg_responce);
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        } else {
                            showToast("User Registration Failed please try again !");
                        }


                    } else {
                        showToast("User Registration Failed !");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
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
                //{"firstName":"nvfyjgf","password":"hjkl","userId":"hghg","userType":"Doctor","speciality":"tfyfu","city":"hgfyf","lastName":"hgcyf","docId":"jyfyf"}
                return headers;
            }

           /* @Override    public byte[] getBody() {
                try {
                    return postBody == null ? null : postBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            postBody, "utf-8");
                    return null;
                }
            }*/
        };

        // add the request object to the queue to be executed
        requestQueue.add(req);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
