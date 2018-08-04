package com.raymond210129.nctucmc.activity.Main.booking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.raymond210129.nctucmc.R;
import com.raymond210129.nctucmc.app.AppConfig;
import com.raymond210129.nctucmc.app.AppController;
import com.raymond210129.nctucmc.dataStructure.Quartet;
import com.raymond210129.nctucmc.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Booking_Search extends AppCompatActivity{
    private static final String TAG = Booking_Search.class.getSimpleName();
    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    public ProgressDialog pDialog;
    private ArrayList<Quartet<String, String, String, String>> mData = new ArrayList<>();
    private HashMap<String, String> user;
    private String name;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        user = db.getUserDetails();
        name = user.get("name");
        Intent intent = getIntent();
        JSONObject jsonObject;
        String date = intent.getStringExtra("SELECTED_DATE");
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("test");
        showDialog();
        String Url = AppConfig.URL_LOOKUPDATE;
        String msg = "";
        try
        {
            HttpsURLConnection conn = (HttpsURLConnection) new URL(Url).openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(4000);
            conn.setConnectTimeout(4000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            String data = "date="+ URLEncoder.encode(date, "UTF-8");
            //conn.connect();
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                int len = 0;
                byte buffer[] = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    message.write(buffer, 0, len);
                }
                is.close();
                message.close();
                msg = new String(message.toByteArray());
                jsonObject = new JSONObject(msg);
            }

        }
        catch(Exception e)
        {
            Log.e(TAG,Log.getStackTraceString(e));
        }
        String status = "";
        hideDialog();

        for(int i = 0; i < 24; i++) {
            status = "1";
            try
            {
                jsonObject = new JSONObject(msg);
                status = jsonObject.getString("s"+ i);

                if(!status.equals("0"))
                {
                    JSONObject occupied = jsonObject.getJSONObject("s" + i);
                    status = occupied.getString("user");
                }
                else
                {
                    status = "";
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                status = "error";
            }
            if(status.equals(""))
            {
                mData.add(new Quartet<>(i + "" , status, date, name));
            }
            else
            {
                mData.add(new Quartet<>( i + "",  status, date, name));
            }
        }
        // 連結元件
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // 設置RecyclerView為列表型態
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 設置格線
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        bookingAdapter = new BookingAdapter(mData, Booking_Search.this);
        recyclerView.setAdapter(bookingAdapter);

        bookingAdapter.setOnItemLongClickListener(new BookingAdapter.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(View view, final int position) {
                final String slot = mData.get(position).getFirst();
                final String date = mData.get(position).getThird();
                if(!mData.get(position).getSecond().equals(""))
                {
                    String tag_string_req = "req_unbooking";
                    pDialog.setMessage("取消預約中...");
                    showDialog();
                    StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_UNBOOKING, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "UnBooking Response: " + response);
                            hideDialog();
                            try {
                                int result = Integer.parseInt(response);
                                if (result == 0) {
                                    Toast.makeText(getApplicationContext(), "取消預約成功", Toast.LENGTH_SHORT).show();
                                    mData.get(position).setSecond("");
                                    bookingAdapter.notifyDataSetChanged();
                                } else if (result == 1) {
                                    Toast.makeText(getApplicationContext(), "Internal error code 1", Toast.LENGTH_LONG).show();
                                } else if (result == 3) {
                                    Toast.makeText(getApplicationContext(), "Internal error code 3", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Internal error unhandled", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Booking error: " + e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Login Error: " + error.getMessage());
                            Toast.makeText(getApplicationContext(),
                                    error.getMessage(), Toast.LENGTH_LONG).show();
                            hideDialog();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("date", date);
                            params.put("slot", slot);
                            return params;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                }

            }
        });

        bookingAdapter.setOnItemClickListener(new BookingAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, final int position) {
                //Toast.makeText(Booking_Search.this, "test", Toast.LENGTH_LONG).show();
                final String slot = mData.get(position).getFirst();
                final String date = mData.get(position).getThird();
                final String name = mData.get(position).getForth();
                if(mData.get(position).getSecond().equals(""))
                {
                    String tag_string_req = "req_booking";
                    pDialog.setMessage("預約中...");
                    showDialog();

                    StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_BOOKING, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "Login Response: " + response);
                            hideDialog();
                            try {
                                int result = Integer.parseInt(response);
                                if (result == 0) {
                                    Toast.makeText(getApplicationContext(), "預約成功", Toast.LENGTH_SHORT).show();
                                    mData.get(position).setSecond(mData.get(position).getForth());
                                    bookingAdapter.notifyDataSetChanged();
                                } else if (result == 1) {
                                    Toast.makeText(getApplicationContext(), "已經有人比你搶先一步預約囉，請重新查詢", Toast.LENGTH_LONG).show();
                                } else if (result == 2) {
                                    Toast.makeText(getApplicationContext(), "Internal error code 2", Toast.LENGTH_LONG).show();
                                } else if (result == 3) {
                                    Toast.makeText(getApplicationContext(), "Internal error code 3", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Internal error unhandled", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Booking error: " + e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Login Error: " + error.getMessage());
                            Toast.makeText(getApplicationContext(),
                                    error.getMessage(), Toast.LENGTH_LONG).show();
                            hideDialog();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("date", date);
                            params.put("slot", slot);
                            params.put("name", name);
                            return params;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                }
            }
        });

    }





    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
