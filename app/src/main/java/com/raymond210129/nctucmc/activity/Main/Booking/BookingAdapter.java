package com.raymond210129.nctucmc.activity.Main.Booking;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.raymond210129.nctucmc.R;
import com.raymond210129.nctucmc.activity.RegisterActivity;
import com.raymond210129.nctucmc.app.AppConfig;
import com.raymond210129.nctucmc.dataStructure.Quartet;
import com.raymond210129.nctucmc.dataStructure.Triplet;
import com.raymond210129.nctucmc.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    private static final String TAG = BookingAdapter.class.getSimpleName();
    private List<Quartet<String, String, String, String>> mData;
    private SQLiteHandler db;
    private ProgressDialog pDialog = null;
    private Context context = null;

    private OnItemClickListener onItemClickListener = null;
    public static interface OnItemClickListener{
        void OnItemClick(View view, int position);
    }

    private OnItemLongClickListener onItemLongClickListener = null;
    public static interface OnItemLongClickListener{
        void OnItemLongClick(View view, int position);
    }

    BookingAdapter(List<Quartet<String, String, String, String>> data, Context context)
    {
        mData = data;
        this.context = context;
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textItem;
        private Button button;
        ViewHolder(View itemView){
            super(itemView);
            textItem = itemView.findViewById(R.id.textItem);
            button = itemView.findViewById(R.id.booking_summit);
            pDialog = new ProgressDialog(context);
            pDialog.setCancelable(false);


        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }




    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position)
    {
        int time = Integer.parseInt(mData.get(position).getFirst()) + 1;
        viewHolder.textItem.setText(mData.get(position).getFirst() + ":00 ~ " + time + ":00");
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        int currentSlot = Integer.parseInt(new SimpleDateFormat("HH").format(Calendar.getInstance().getTime()));
        if(mData.get(position).getThird().compareTo(date) < 0 || (mData.get(position).getThird().compareTo(date) == 0 && currentSlot >= position))
        {
            viewHolder.button.setEnabled(false);
        }
        else
        {
            viewHolder.button.setEnabled(!(!mData.get(position).getSecond().equals("") && !mData.get(position).getSecond().equals(mData.get(position).getForth()))); // 有人且不是自己

        }
        if(!mData.get(position).getSecond().equals("") && !mData.get(position).getSecond().equals(mData.get(position).getForth()))
        {
            viewHolder.button.setText(mData.get(position).getSecond());
        }
        else if(mData.get(position).getSecond().equals(""))
        {
            viewHolder.button.setText("預約");
        }
        else
        {
            viewHolder.button.setText("已預約");
        }
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(onItemClickListener != null)
                {
                    onItemClickListener.OnItemClick(view, position);
                }
            }
        });
        viewHolder.button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(onItemLongClickListener != null)
                {
                    onItemLongClickListener.OnItemLongClick(view, position);
                }
                return true;
            }
        });


    }

    @Override
    public int getItemCount(){
        return mData.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    public boolean setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener)
    {
        this.onItemLongClickListener = onItemLongClickListener;
        return true;
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
