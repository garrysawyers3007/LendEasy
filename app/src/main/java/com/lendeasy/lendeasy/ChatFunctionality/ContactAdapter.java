package com.lendeasy.lendeasy.ChatFunctionality;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lendeasy.lendeasy.R;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    CustomClickListener customClickListener;
    List<Contact> arrayList = new ArrayList<>();


    public ContactAdapter(List<Contact> arrayList) {
        this.arrayList = arrayList;
    }

    public void updateRecylerView(List<Contact> list) {
        this.arrayList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTV.setText(arrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setCustomClickListener(CustomClickListener customClickListener) {
        this.customClickListener = customClickListener;
    }


    public interface CustomClickListener {
        void onClick(Contact contact);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && customClickListener != null) {
                        customClickListener.onClick(arrayList.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}