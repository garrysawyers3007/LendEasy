package com.lendeasy.lendeasy.ChatFunctionality;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.lendeasy.lendeasy.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {
    List<ChatModel> arrayList = new ArrayList<>();
    String senderId;
    Context context;


    public ChatAdapter(List<ChatModel> arrayList, String senderId, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.senderId = senderId;
    }

    public void updateRecylerView(List<ChatModel> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    public String getTime(Timestamp time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getSeconds() * 1000);
        String date = DateFormat.format("hh:mm a", calendar).toString();
        return date;
    }

    //    public String getDate(Timestamp time) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(time.getSeconds() * 1000);
//        String date = DateFormat.format("hh:mm a", calendar).toString();
//        return date;
//    }
    @Override
    public int getItemViewType(int position) {
//if(hasDateChanged(arrayList.get(position-1),arrayList.get(position))){
//return 0;
//}

        if (arrayList.get(position).getSenderId().equals(senderId))
            return 1;
        else
            return 2;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
//        if(viewType==0){
//
//        }
        if (viewType == 1) {
            view = layoutInflater.inflate(R.layout.row_activity_chat, parent, false);
            return new ViewHolder1(view);
        } else {
            view = layoutInflater.inflate(R.layout.row_activity_chat2, parent, false);
            return new ViewHolder2(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (arrayList.get(position).getSenderId().equals(senderId)) {
            ViewHolder1 viewHolder1 = (ViewHolder1) holder;
            viewHolder1.chatTV.setText(arrayList.get(position).getMessage());
            viewHolder1.timeTV.setText(getTime(arrayList.get(position).getTimeStamp()));
        } else {
            ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            viewHolder2.chatTV2.setText(arrayList.get(position).getMessage());
            viewHolder2.timeTV2.setText(getTime(arrayList.get(position).getTimeStamp()));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView chatTV;
        TextView timeTV;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            chatTV = itemView.findViewById(R.id.chatTV);
            timeTV = itemView.findViewById(R.id.timeTV);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView chatTV2;
        TextView timeTV2;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            chatTV2 = itemView.findViewById(R.id.chatTV2);
            timeTV2 = itemView.findViewById(R.id.timeTV2);
        }
    }

//    public class DateViewHolder extends RecyclerView.ViewHolder {
//        TextView dateTV;
//
//        public DateViewHolder(@NonNull View itemView) {
//            super(itemView);
//            dateTV = itemView.findViewById(R.id.dateTV);
//
//        }
//    }


//    public boolean hasDateChanged(ChatModel previousChat,ChatModel currentChat){
//        Date previousDay=previousChat.getTimeStamp().toDate();
//        Date currentDay=currentChat.getTimeStamp().toDate();
//
//        Long differenceInDays=(currentDay.getTime()-previousDay.getTime())/(1000*3600*24);
//        if(differenceInDays>1)
//            return true;
//        else
//return false;
//    }
}
