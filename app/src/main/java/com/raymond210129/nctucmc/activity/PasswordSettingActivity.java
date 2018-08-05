package com.raymond210129.nctucmc.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.raymond210129.nctucmc.R;
//import com.raymond210129.nctucmc.activity.RegisterActivity;
import com.raymond210129.nctucmc.app.AppConfig;
import com.raymond210129.nctucmc.app.AppController;
import com.raymond210129.nctucmc.helper.SQLiteHandler;
import com.raymond210129.nctucmc.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PasswordSettingActivity extends AppCompatActivity {
    private static final String TAG = PasswordSettingActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager sessionManager;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_setting);

        final EditText oldPassword = findViewById(R.id.input_old_password);
        final EditText newPassword = findViewById(R.id.input_new_password);
        final EditText confirmPassword = findViewById(R.id.conform_new_password);
        Button confirmChange = findViewById(R.id.confirm_change);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new SQLiteHandler(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());

        confirmChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputOldPassword = oldPassword.getText().toString().trim();
                String inputNewPassword = newPassword.getText().toString().trim();
                String inputConfirmPassword = confirmPassword.getText().toString().trim();

                if(inputOldPassword.equals(inputNewPassword))
                {
                    Toast.makeText(getApplicationContext(), "新舊密碼相同", Toast.LENGTH_LONG).show();
                }
                else if(!inputNewPassword.equals(inputConfirmPassword))
                {
                    Toast.makeText(getApplicationContext(), "兩欄新密碼不一致", Toast.LENGTH_LONG).show();
                }
                else
                {
                    changePassword(inputOldPassword, inputNewPassword);
                }

            }
        });
    }
    private void changePassword(final String oldPassword, final String newPassword) {
        HashMap<String, String> user;
        user = db.getUserDetails();
        final String name = user.get("name");
        String tag_string_req = "req_change_password";
        pDialog.setMessage("變更密碼中...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHANGEPASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "change password response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        sessionManager.setLogin(false);
                        Toast.makeText(getApplicationContext(),
                                "密碼變更完成，請以新密碼重新登入", Toast.LENGTH_LONG)
                                .show();
                        Intent intent = new Intent(PasswordSettingActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Change Password Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to change password url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("oldPassword", oldPassword);
                params.put("newPassword", newPassword);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog(){
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
