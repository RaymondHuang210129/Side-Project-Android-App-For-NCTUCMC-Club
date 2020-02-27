package com.raymond210129.nctucmc.activity.Main.poll;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.raymond210129.nctucmc.R;
import com.raymond210129.nctucmc.activity.Main.booking.BookingAdapter;
import com.raymond210129.nctucmc.dataStructure.Quartet;

import java.util.ArrayList;
import java.util.List;

public class PollAdapter extends RecyclerView.Adapter<com.raymond210129.nctucmc.activity.Main.poll.PollAdapter.ViewHolder> {
    private ArrayList<String> mData = null;
    private Context context = null;

    PollAdapter(ArrayList<String> data, Context context)
    {
        mData = data;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textItem.setText(mData.get(position));
        holder.delete.setText("刪除");
        final int temp = holder.getAdapterPosition();

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position);
            }
        });
    }

    public void addItem(String text, int position)
    {
        mData.add(position, text);
        notifyItemInserted(position);
    }

    public void removeItem(int position)
    {
        mData.remove(position);
        //notifyItemRemoved(position);
        notifyDataSetChanged();
        Log.d("HHH", String.valueOf(position) + " " + mData.size() );
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textItem;
        private Button delete;
        ViewHolder(View itemView){
            super(itemView);
            textItem = itemView.findViewById(R.id.selection_item);
            delete = itemView.findViewById(R.id.selection_delete);
        }
    }
}
