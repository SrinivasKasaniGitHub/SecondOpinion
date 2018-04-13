package com.quickboxdemo.activities;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.helper.Utils;
import com.quickblox.sample.core.utils.KeyboardUtils;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.sample.core.utils.Toaster;

import com.quickblox.users.model.QBUser;

import com.quickboxdemo.R;
import com.quickboxdemo.services.CallService;
import com.quickboxdemo.utils.Consts;
import com.quickboxdemo.utils.QBEntityCallbackImpl;
import com.quickboxdemo.utils.UsersUtils;
import com.quickboxdemo.utils.ValidationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends BaseActivity {

    private String TAG = LoginActivity.class.getSimpleName();

    ProgressDialog progressDialog;

    private EditText userNameEditText;
    private EditText chatRoomNameEditText;
    Button btn_Login;
    AppCompatTextView txt_Register;
    AppCompatTextView txt_forgot;
    EditText edt_login_pwd;

    private QBUser userForSave;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        initUI();
        progressDialog=new ProgressDialog(this);

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (!isEnteredUserNameValid()){
                    userNameEditText.requestFocus();
                    userNameEditText.setError("User Name not Empty !");
                }else if (edt_login_pwd.getText().toString().equals("")){
                    edt_login_pwd.requestFocus();
                    edt_login_pwd.setError("Password is not Empty !");
                }else {
                    login_Authenticaation(userNameEditText.getText().toString(),edt_login_pwd.getText().toString());
                }



              /*  if (isEnteredUserNameValid() && isEnteredRoomNameValid()) {
                    hideKeyboard();
                    startSignUpNewUser(createUserWithEnteredData());
                }*/
            }
        });

        txt_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_Register=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent_Register);

            }
        });

        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_forgot=new Intent(getApplicationContext(),ForgotPasswordSctivity.class);
                startActivity(intent_forgot);
            }
        });
    }

    @Override
    protected View getSnackbarAnchorView() {
        return findViewById(R.id.root_view_login_activity);
    }

    public void login_Authenticaation(final String dUserName,final String dPwd) {

        RequestQueue requestQueue_Authenticaation = Volley.newRequestQueue(this);
        String tag_string_req = "authentication";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        Map<String, String> params = new HashMap<String, String>();

        //d_F_Name, d_L_Name, d_Speciality, d_Location, d_DId, d_UserName, d_Pwd, d_Cpwd

        params.put("userId", dUserName);
        params.put("password", dPwd);
        params.put("userType", "Doctor");


        JSONObject jsonObject=new JSONObject(params);


        JsonObjectRequest req_Authentication = new JsonObjectRequest(Request.Method.POST,AppConfig.url_User_Authentication, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                try {
                    if (null!=response.toString()){
                        String reg_Responce_Code=response.getString("responseCode");
                        String reg_responce=response.getString("response");
                        if (reg_Responce_Code.equals("A100")){
                            showToast(reg_responce);
                        }else if (reg_Responce_Code.equals("A000")){
                            showToast(reg_responce);
                            hideKeyboard();
                            startSignUpNewUser(createUserWithEnteredData());
                        }else{
                            showToast("User Login Failed please try again !");
                        }


                    }else{
                        showToast("User Login Failed !");
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue_Authenticaation.add(req_Authentication);
    }

    private void initUI() {
        setActionBarTitle(R.string.title_login_activity);
        userNameEditText = (EditText) findViewById(R.id.user_name);
        btn_Login = (Button) findViewById(R.id.btn_Login);
        txt_Register=(AppCompatTextView)findViewById(R.id.txt_register);
        txt_forgot=(AppCompatTextView)findViewById(R.id.txt_forgot);
        userNameEditText.addTextChangedListener(new LoginEditTextWatcher(userNameEditText));
        edt_login_pwd=(EditText)findViewById(R.id.login_pwd);

        chatRoomNameEditText = (EditText) findViewById(R.id.chat_room_name);
        chatRoomNameEditText.setText("ghyuijkolpr");
        chatRoomNameEditText.addTextChangedListener(new LoginEditTextWatcher(chatRoomNameEditText));
    }
    public void showToast(final String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_login_user_done:
                if (isEnteredUserNameValid() && isEnteredRoomNameValid()) {
                    hideKeyboard();
                    startSignUpNewUser(createUserWithEnteredData());
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isEnteredRoomNameValid() {
        return ValidationUtils.isRoomNameValid(this, chatRoomNameEditText);
    }

    private boolean isEnteredUserNameValid() {
        return ValidationUtils.isUserNameValid(this, userNameEditText);
    }

    private void hideKeyboard() {
        KeyboardUtils.hideKeyboard(userNameEditText);
        KeyboardUtils.hideKeyboard(chatRoomNameEditText);
    }

    private void startSignUpNewUser(final QBUser newUser) {
        showProgressDialog(R.string.dlg_creating_new_user);
        requestExecutor.signUpNewUser(newUser, new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser result, Bundle params) {
                        loginToChat(result);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        if (e.getHttpStatusCode() == Consts.ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                            signInCreatedUser(newUser, true);
                        } else {
                            hideProgressDialog();
                            Toaster.longToast(R.string.sign_up_error);
                        }
                    }
                }
        );
    }

    private void loginToChat(final QBUser qbUser) {
        qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);

        userForSave = qbUser;
        startLoginService(qbUser);
    }

    private void startOpponentsActivity() {
        Intent intent_DashBoard = new Intent(getApplicationContext(), DashBoardActivity.class);
        startActivity(intent_DashBoard);
        finish();
    }

    private void saveUserData(QBUser qbUser) {
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        sharedPrefsHelper.save(Consts.PREF_CURREN_ROOM_NAME, qbUser.getTags().get(0));
        sharedPrefsHelper.saveQbUser(qbUser);
    }

    private QBUser createUserWithEnteredData() {
        return createQBUserWithCurrentData(String.valueOf(userNameEditText.getText()),
                String.valueOf(chatRoomNameEditText.getText()));
    }

    private QBUser createQBUserWithCurrentData(String userName, String chatRoomName) {
        QBUser qbUser = null;
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(chatRoomName)) {
            StringifyArrayList<String> userTags = new StringifyArrayList<>();
            userTags.add(chatRoomName);

            qbUser = new QBUser();
            qbUser.setFullName(userName);
            qbUser.setLogin(getCurrentDeviceId());
            qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);
            qbUser.setTags(userTags);
        }

        return qbUser;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Consts.EXTRA_LOGIN_RESULT_CODE) {
            hideProgressDialog();
            boolean isLoginSuccess = data.getBooleanExtra(Consts.EXTRA_LOGIN_RESULT, false);
            String errorMessage = data.getStringExtra(Consts.EXTRA_LOGIN_ERROR_MESSAGE);

            if (isLoginSuccess) {
                saveUserData(userForSave);

                signInCreatedUser(userForSave, false);
            } else {
                Toaster.longToast(getString(R.string.login_chat_login_error) + errorMessage);
                userNameEditText.setText(userForSave.getFullName());
                chatRoomNameEditText.setText(userForSave.getTags().get(0));
            }
        }
    }

    private void signInCreatedUser(final QBUser user, final boolean deleteCurrentUser) {
        requestExecutor.signInUser(user, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser result, Bundle params) {
                if (deleteCurrentUser) {
                    removeAllUserData(result);
                } else {
                    startOpponentsActivity();
                }
            }

            @Override
            public void onError(QBResponseException responseException) {
                hideProgressDialog();
                Toaster.longToast(R.string.sign_up_error);
            }
        });
    }

    private void removeAllUserData(final QBUser user) {
        requestExecutor.deleteCurrentUser(user.getId(), new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                UsersUtils.removeUserData(getApplicationContext());
                startSignUpNewUser(createUserWithEnteredData());
            }

            @Override
            public void onError(QBResponseException e) {
                hideProgressDialog();
                Toaster.longToast(R.string.sign_up_error);
            }
        });
    }

    private void startLoginService(QBUser qbUser) {
        Intent tempIntent = new Intent(this, CallService.class);
        PendingIntent pendingIntent = createPendingResult(Consts.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
        CallService.start(this, qbUser, pendingIntent);
    }

    private String getCurrentDeviceId() {
        return Utils.generateDeviceId(this);
    }

    private class LoginEditTextWatcher implements TextWatcher {
        private EditText editText;

        private LoginEditTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            editText.setError(null);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
