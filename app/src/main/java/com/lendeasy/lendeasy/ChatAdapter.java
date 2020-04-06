package com.lendeasy.lendeasy;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    List<ChatModel> arrayList = new ArrayList<>();
    Context context;

    public ChatAdapter(List<ChatModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public void updateRecylerView(List<ChatModel> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_activity_chat, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.chatTV.setText(arrayList.get(position).getMessage());
        holder.timeTV.setText(getTime(arrayList.get(position).getTimeStamp()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public String getTime(Timestamp time) {

        Log.d("time",""+time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getSeconds() * 1000);
        String date = DateFormat.format("hh:mm a", calendar).toString();
        return date;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView chatTV;
        TextView timeTV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatTV = itemView.findViewById(R.id.chatTV);
            timeTV = itemView.findViewById(R.id.timeTV);
        }
    }
}
